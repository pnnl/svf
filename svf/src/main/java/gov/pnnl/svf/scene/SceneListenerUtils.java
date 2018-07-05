package gov.pnnl.svf.scene;

import gov.pnnl.svf.core.util.StateUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for working with native listeners for a scene.
 *
 * @author Amelia Bleeker
 */
public class SceneListenerUtils implements Disposable {

    private static final Logger logger = Logger.getLogger(SceneListenerUtils.class.getName());
    /**
     * Regular expression used for locating the listener add method.
     */
    public static final String DEFAULT_ADD_REGEX = "(?!addPropertyChangeListener)add([\\w]*)Listener";
    /**
     * Regular expression used for locating the listener remove method.
     */
    public static final String DEFAULT_REMOVE_REGEX = "(?!removePropertyChangeListener)remove([\\w]*)Listener";
    /**
     * State mask for boolean field in this actor.
     */
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[0];
    /**
     * State used by flags in this actor to save memory space. Up to 8 states
     * can be used and should go in the following order: 0x01, 0x02, 0x04, 0x08,
     * 0x10, 0x20, 0x40, and (byte) 0x80. Set the initial state in the
     * constructor by or-ing the new states with the current state.
     */
    private byte state = StateUtil.NONE;
    private final Scene scene;
    private final String addRegex;
    private final String removeRegex;
    private final Set<Class<?>> listenerTypes;
    private final Map<Class<?>, Method> addMethods;
    private final Map<Class<?>, Method> removeMethods;

    /**
     * Constructor will automatically generate a set of listener types.
     *
     * @param scene reference to the scene
     */
    public SceneListenerUtils(final Scene scene) {
        this(scene, DEFAULT_ADD_REGEX, DEFAULT_REMOVE_REGEX);
    }

    /**
     * Constructor
     *
     * @param scene       reference to the scene
     * @param addRegex    the listener add regular expression
     * @param removeRegex the listener remove regular expression
     */
    public SceneListenerUtils(final Scene scene, final String addRegex, final String removeRegex) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        this.addRegex = addRegex == null ? DEFAULT_ADD_REGEX : addRegex;
        this.removeRegex = removeRegex == null ? DEFAULT_REMOVE_REGEX : removeRegex;
        listenerTypes = new HashSet<>();
        addMethods = new HashMap<>();
        removeMethods = new HashMap<>();
        initialize();
    }

    @Override
    public boolean isDisposed() {
        return StateUtil.isValue(state, DISPOSED_MASK);
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        state = StateUtil.setValue(state, DISPOSED_MASK);
        // no cleanup
    }

    /**
     * Attempts to add the object as a listener to the scene.
     *
     * @param object the object to attempt to add as a listener
     *
     * @return true if the object is a listener type
     */
    public boolean addListener(final Object object) {
        boolean added = false;
        for (final Class<?> listenerType : listenerTypes) {
            if (listenerType.isInstance(object)) {
                try {
                    addMethods.get(listenerType).invoke(scene.getComponent(), object);
                    added = true;
                } catch (final IllegalAccessException | InvocationTargetException | RuntimeException ex) {
                    if (scene.getExtended().getSceneBuilder().isVerbose()) {
                        logger.log(Level.WARNING, MessageFormat.format("{0}: Exception while adding object [{1}] as a listener type [{2}].",
                                                                       scene, object.getClass(), listenerType), ex);
                    }
                }
            }
        }
        return added;
    }

    /**
     * Attempts to remove the object as a listener from the scene.
     *
     * @param object the object to attempt to remove as a listener
     *
     * @return true if the object is a listener type
     */
    public boolean removeListener(final Object object) {
        boolean removed = false;
        for (final Class<?> listenerType : listenerTypes) {
            if (listenerType.isInstance(object)) {
                try {
                    removeMethods.get(listenerType).invoke(scene.getComponent(), object);
                    removed = true;
                } catch (final IllegalAccessException | InvocationTargetException | RuntimeException ex) {
                    if (scene.getExtended().getSceneBuilder().isVerbose()) {
                        logger.log(Level.WARNING, MessageFormat.format("{0}: Exception while removing object [{1}] as a listener type [{2}].",
                                                                       scene, object.getClass(), listenerType), ex);
                    }
                }
            }
        }
        return removed;
    }

    private void initialize() {
        final Pattern addPattern = Pattern.compile(addRegex);
        final Pattern removePattern = Pattern.compile(removeRegex);
        final Method[] methods = scene.getComponent().getClass().getMethods();
        // find the listener methods
        for (final Method method : methods) {
            // check for add listener method
            final Matcher addMatcher = addPattern.matcher(method.getName());
            if (addMatcher.matches() && method.getParameterTypes().length == 1) {
                final Class<?> listenerType = method.getParameterTypes()[0];
                if (!(listenerType.isAnnotation()
                      || listenerType.isAnonymousClass()
                      || listenerType.isArray()
                      || listenerType.isEnum()
                      || listenerType.isPrimitive())) {
                    listenerTypes.add(listenerType);
                    addMethods.put(listenerType, method);
                }
                continue;
            }
            // check for remove listener method
            final Matcher removeMatcher = removePattern.matcher(method.getName());
            if (removeMatcher.matches() && method.getParameterTypes().length == 1) {
                final Class<?> listenerType = method.getParameterTypes()[0];
                if (!(listenerType.isAnnotation()
                      || listenerType.isAnonymousClass()
                      || listenerType.isArray()
                      || listenerType.isEnum()
                      || listenerType.isPrimitive())) {
                    listenerTypes.add(listenerType);
                    removeMethods.put(listenerType, method);
                }
            }
        }
        // only retain matching add and remove pairs
        for (final Iterator<Class<?>> it = listenerTypes.iterator(); it.hasNext();) {
            final Class<? extends Object> listenerType = it.next();
            final Method addMethod = addMethods.get(listenerType);
            final Method removeMethod = removeMethods.get(listenerType);
            if (addMethod == null || removeMethod == null || !addMethod.getParameterTypes()[0].equals(removeMethod.getParameterTypes()[0])) {
                it.remove();
            }
        }
        addMethods.keySet().retainAll(listenerTypes);
        removeMethods.keySet().retainAll(listenerTypes);
    }
}
