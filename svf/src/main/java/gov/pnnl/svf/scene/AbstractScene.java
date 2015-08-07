package gov.pnnl.svf.scene;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.DrawingCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.service.BusyService;
import gov.pnnl.svf.core.util.NamedThreadFactory;
import gov.pnnl.svf.core.util.StateUtil;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.ShapeServiceImpl;
import gov.pnnl.svf.hint.CameraHint;
import gov.pnnl.svf.hint.PickingHint;
import gov.pnnl.svf.picking.ColorPickingUtils;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.text.TextServiceImpl;
import gov.pnnl.svf.update.TaskManager;
import gov.pnnl.svf.util.ConfigUtil;
import gov.pnnl.svf.util.Lookup;
import gov.pnnl.svf.vbo.VboShapeServiceImpl;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.EventListener;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import org.apache.commons.math.geometry.Vector3D;

/**
 * <P>
 * This scene handles many different types of components in a similar and
 * consistent manner.</p>
 * <p>
 * Subclass this to create your own scene. Override default scene values by
 * overriding the initialize method. Initialize all cameras and actors in the
 * load method. </p>
 * <p>
 * Lists of actors and references to cameras should not be kept in subclasses.
 * These objects should be located using the lookup (inversion of control). This
 * allows flexibility in that other classes can add and remove actors and
 * cameras during runtime. It also allows for a single place of reference for
 * access to these objects.</P>
 *
 * @param <C> the component type that's used to render this scene
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractScene<C extends GLAutoDrawable> implements SceneExt {

    private static final Logger logger = Logger.getLogger(AbstractScene.class.getName());

    private static final AtomicLong counter = new AtomicLong();
    /**
     * The default type for this scene.
     */
    public static final String DEFAULT_TYPE = "scene";
    /**
     * State mask for boolean field in this actor.
     */
    protected static final byte LOADED_MASK = StateUtil.getMasks()[0];
    /**
     * State mask for boolean field in this actor.
     */
    protected static final byte DISPOSED_MASK = StateUtil.getMasks()[1];
    /**
     * State used by flags in this actor to save memory space. Up to 8 states
     * can be used and should go in the following order: 0x01, 0x02, 0x04, 0x08,
     * 0x10, 0x20, 0x40, and (byte) 0x80. Set the initial state in the
     * constructor by or-ing the new states with the current state.
     */
    protected byte state = StateUtil.NONE;
    protected final String id;
    protected final String type;
    protected final SceneListenerUtils sceneListenerUtils;
    protected final SceneUtil sceneUtil;
    protected final SceneRenderer sceneRenderer;
    protected final Tooltip tooltip;
    protected final ScreenshotImpl screenshot;
    protected final GLAnimatorControl animator;
    protected final GLEventListener listener;
    protected final SceneFactory factory;
    protected final BusyService busyService;
    protected final C component;
    protected final GLUT glut;
    protected GLUgl2 glu;
    protected GL2 gl;

    /**
     * Constructor
     *
     * @param factory   the factory that's used to create specific scene items
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public AbstractScene(final SceneFactory factory, final C component, final SceneBuilder builder) {
        this(factory, component, builder, DEFAULT_TYPE, String.valueOf(counter.incrementAndGet()));
    }

    /**
     * Constructor
     *
     * @param factory   the factory that's used to create specific scene items
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param id        the unique id for the scene
     *
     * @throws IllegalArgumentException if component is not a supported type
     * @throws IllegalArgumentException if id is empty
     */
    public AbstractScene(final SceneFactory factory, final C component, final SceneBuilder builder, final String id) {
        this(factory, component, builder, DEFAULT_TYPE, id);
    }

    /**
     * Constructor
     *
     * @param factory   the factory that's used to create specific scene items
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param type      the type identifier for scene
     * @param id        the unique id for the scene
     *
     * @throws IllegalArgumentException if component is not a supported type
     * @throws IllegalArgumentException if type or id are empty
     */
    public AbstractScene(final SceneFactory factory, final C component, final SceneBuilder builder, final String type, final String id) {
        if (factory == null) {
            throw new NullPointerException("factory");
        }
        if (component == null) {
            throw new NullPointerException("component");
        }
        if (builder == null) {
            throw new NullPointerException("builder");
        }
        if (type == null) {
            throw new NullPointerException("type");
        }
        if (id == null) {
            throw new NullPointerException("id");
        }
        if (type.isEmpty() || id.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // construct
        this.type = type;
        this.id = id;
        this.factory = factory;
        this.component = component;
        sceneListenerUtils = new SceneListenerUtils(this);
        glut = new GLUT();
        listener = new GLEventListenerImpl(this, builder);
        sceneUtil = new SceneUtil(this, builder);
        sceneRenderer = new SceneRenderer(this, builder);
        sceneRenderer.setBackground(builder.getBackground());
        sceneRenderer.setMaxInitializations(builder.getMaxInitializations());
        // create the text service
        TextServiceImpl.newInstance(this);
        ShapeServiceImpl.newInstance(this);
        VboShapeServiceImpl.newInstance(this);
        // start the busy service
        busyService = factory.createService(this, BusyService.class);
        add(busyService);
        busyService.initialize();
        // tooltip
        this.tooltip = new TooltipImpl(this);
        // screenshot
        this.screenshot = new ScreenshotImpl();
        // disable buffer auto swap
        this.component.setAutoSwapBufferMode(false);
        // add the GL event listener
        this.component.addGLEventListener(listener);
        // animator
        final int targetFps = sceneUtil.getSceneBuilder().getTargetFps();
        this.animator = new FPSAnimator(component, targetFps, true);
        logger.log(Level.FINE, "{0}: Creating a new AbstractScene.", this);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public C getComponent() {
        return component;
    }

    @Override
    public SceneFactory getFactory() {
        return factory;
    }

    @Override
    public void start() {
        if (isDisposed()) {
            return;
        }
        logger.log(Level.FINE, "{0}: Starting the AbstractScene.", this);
        try {
            animator.start();
        } catch (final RuntimeException ex) {
            // ignore, animator can have problems here in the
            // underlying implementation
            logger.log(Level.WARNING, MessageFormat.format("{0}: Error occurred when attempting to start animator.", this), ex);
        }
        // add the scene to the global lookup
        Lookup.getLookup().add(this);
    }

    @Override
    public void stop() {
        if (isDisposed()) {
            return;
        }
        logger.log(Level.FINE, "{0}: Stopping the AbstractScene.", this);
        try {
            animator.stop();
        } catch (final RuntimeException ex) {
            // ignore, animator can have problems here in the
            // underlying implementation
            logger.log(Level.WARNING, MessageFormat.format("{0}: Error occurred when attempting to stop animator.", this), ex);
        }
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return StateUtil.isValue(state, DISPOSED_MASK);
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            state = StateUtil.setValue(state, DISPOSED_MASK);
        }
        logger.log(Level.FINE, "{0}: Shutting down the AbstractScene.", this);
        try {
            animator.stop();
        } catch (final RuntimeException ex) {
            // ignore, animator can have problems here in the
            // underlying implementation
            logger.log(Level.WARNING, MessageFormat.format("{0}: Error occurred when attempting to stop animator.", this), ex);
        }
        // remove the GL event listener
        factory.runOnUiThread(this, new Runnable() {
            @Override
            public void run() {
                try {
                    component.removeGLEventListener(listener);
                } catch (final RuntimeException ex) {
                    logger.log(Level.WARNING, MessageFormat.format("{0}: Exception while removing GLEventListener.", this), ex);
                }
            }
        });
        // remove the scene reference from the global lookup
        Lookup.getLookup().remove(this);
        // remove listener types
        final Set<EventListener> objects = lookupAll(EventListener.class);
        for (final Object object : objects) {
            removeListeners(object);
        }
        // disposables
        final Set<Disposable> disposables = lookupAll(Disposable.class);
        for (final Disposable disposable : disposables) {
            disposable.dispose();
        }
        // clear the lookup
        clear();
        // stop the services
        busyService.dispose();
        sceneUtil.dispose();
        sceneRenderer.dispose();
        tooltip.dispose();
    }

    @Override
    public void addListeners(final Object object) {
        factory.runOnUiThread(this, new Runnable() {
            @Override
            public void run() {
                sceneListenerUtils.addListener(object);
            }
        });
    }

    @Override
    public void removeListeners(final Object object) {
        factory.runOnUiThread(this, new Runnable() {
            @Override
            public void run() {
                sceneListenerUtils.removeListener(object);
            }
        });
    }

    @Override
    public void draw() {
        sceneRenderer.setRepaint(DrawingPass.ALL);
    }

    @Override
    public void draw(final DrawingPass pass) {
        sceneRenderer.setRepaint(pass);
    }

    @Override
    public DrawingPass getDirtied() {
        return sceneRenderer.getRepaint();
    }

    @Override
    public Actor getActor(final String id) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        return sceneUtil.getSceneLookup().getActor(id);
    }

    @Override
    public <T extends Actor> T getActor(final Class<T> clazz, final String type, final String id, final Object data) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        if (id == null) {
            throw new NullPointerException("id");
        }
        return sceneUtil.getSceneLookup().getActor(clazz, type, id, data);
    }

    @Override
    public Vector3D getBoundary() {
        return sceneRenderer.getBoundary();
    }

    @Override
    public Vector3D getCenter() {
        return sceneRenderer.getCenter();
    }

    @Override
    public Rectangle getViewport() {
        return sceneUtil.getViewport();
    }

    @Override
    public Set<Class<? extends Drawable>> getDrawableTypes() {
        return sceneRenderer.getDrawableTypes();
    }

    @Override
    public GLProfile getGLProfile() {
        final GL gl = getGL();
        if (gl != null) {
            final GLProfile profile = gl.getGLProfile();
            if (profile != null) {
                return profile;
            }
        }
        return sceneUtil.getSceneBuilder().getGLCapabilities().getGLProfile();
    }

    @Override
    public GL getGL() {
        synchronized (this) {
            return sceneUtil.getSceneBuilder().isDebug() ? ConfigUtil.newDebugGL(gl) : gl;
        }
    }

    @Override
    public GLU getGLU() {
        synchronized (this) {
            return glu;
        }
    }

    @Override
    public GLUT getGLUT() {
        synchronized (this) {
            return glut;
        }
    }

    @Override
    public SceneExt getExtended() {
        return this;
    }

    @Override
    public SceneBuilder getSceneBuilder() {
        return sceneUtil.getSceneBuilder();
    }

    @Override
    public Tooltip getTooltip() {
        return tooltip;
    }

    @Override
    public Screenshot getScreenshot() {
        return screenshot;
    }

    @Override
    public ColorPickingUtils getColorPickingUtils() {
        return sceneUtil.getColorPickingUtils();
    }

    @Override
    public DrawingPass getCurrentDrawingPass() {
        return sceneRenderer.getCurrentDrawingPass();
    }

    @Override
    public TaskManager getDefaultTaskManager() {
        return sceneUtil.getTaskManager();
    }

    @Override
    public int getMaxInitializations() {
        return sceneRenderer.getMaxInitializations();
    }

    @Override
    public Color getSceneBackground() {
        return sceneRenderer.getBackground();
    }

    @Override
    public SceneMetrics getSceneMetrics() {
        return sceneRenderer.getSceneMetrics();
    }

    @Override
    public Collection<Actor> getVisibleRootActors() {
        return sceneUtil.getSceneLookup().getVisibleRootActors();
    }

    @Override
    public void getVisibleRootActors(final Collection<Actor> out) {
        sceneUtil.getSceneLookup().getVisibleRootActors(out);
    }

    @Override
    public boolean isLoaded() {
        synchronized (this) {
            return StateUtil.isValue(state, LOADED_MASK);
        }
    }

    @Override
    public <T> T lookup(final Class<? extends T> object) {
        return sceneUtil.getSceneLookup().lookup(object);
    }

    @Override
    public <T> Set<T> lookupAll(final Class<T> object) {
        return sceneUtil.getSceneLookup().lookupAll(object);
    }

    @Override
    public <T> void lookupAll(final Class<T> object, final Collection<T> out) {
        sceneUtil.getSceneLookup().lookupAll(object, out);
    }

    @Override
    public void clear() {
        sceneUtil.getSceneLookup().clear();
    }

    @Override
    public <T> void add(final T object) {
        sceneUtil.getSceneLookup().add(object);
    }

    @Override
    public <T> void addAll(final Collection<T> objects) {
        sceneUtil.getSceneLookup().addAll(objects);
    }

    @Override
    public <T> boolean remove(final T object) {
        return sceneUtil.getSceneLookup().remove(object);
    }

    @Override
    public <T> boolean removeAll(final Collection<T> objects) {
        return sceneUtil.getSceneLookup().removeAll(objects);
    }

    @Override
    public Set<Object> lookupAll() {
        return sceneUtil.getSceneLookup().lookupAll();
    }

    @Override
    public void lookupAll(final Collection<Object> out) {
        sceneUtil.getSceneLookup().lookupAll(out);
    }

    @Override
    public void setBoundary(final Vector3D boundary) {
        sceneRenderer.setBoundary(boundary);
    }

    @Override
    public void setCenter(final Vector3D center) {
        sceneRenderer.setCenter(center);
    }

    @Override
    public void setMaxInitializations(final int maxInitializations) {
        sceneRenderer.setMaxInitializations(maxInitializations);
    }

    @Override
    public void setSceneBackground(final Color background) {
        sceneRenderer.setBackground(background);
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return sceneUtil.getPropertyChangeSupport();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "type=" + type + ", id=" + id + '}';
    }

    @Override
    public boolean waitForLoad(final long timeout) {
        final CountDownLatch latch = new CountDownLatch(1);
        final PropertyChangeListener listener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                latch.countDown();
            }
        };
        getPropertyChangeSupport().addPropertyChangeListener(Scene.LOADED, listener);
        if (!isLoaded()) {
            try {
                if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
                    logger.log(Level.WARNING, MessageFormat.format("{0} failed to load within the time allowed: {1} ms", getClass().getSimpleName(), timeout));
                }
            } catch (final InterruptedException ex) {
                // ignore
            }
        }
        getPropertyChangeSupport().removePropertyChangeListener(Scene.LOADED, listener);
        return isLoaded();
    }

    /**
     * Perform all initial scene loading logic here. This function will get
     * called once after the initial GL initialization.
     * <br/>
     * <h3>e.g.:</h3>
     * <p>
     * <code>
     * final SimpleCamera camera = new SimpleCamera(this);<br/>
     * add(camera);<br/>
     * <br/>
     * final ColoredPlaneActor plane = new ColoredPlaneActor(this);<br/>
     * add(plane);<br/>
     * </code> </p>
     */
    @SuppressWarnings("unchecked")
    public void load() {
        // drawing camera
        final Set<CameraHint> cameraHints = getSceneBuilder().copyHints(CameraHint.class);
        if (cameraHints.size() != 1) {
            logger.log(Level.WARNING, "Camera hint not specified!");
        }
        if (cameraHints.contains(CameraHint.NONE)) {
            return;
        }
        final Class<? extends DrawingCamera> cameraType = cameraHints.iterator().next().getCameraType();
        // create the camera
        final DrawingCamera camera = getFactory().createCamera(this, cameraType);
        camera.setLocation(new Vector3D(0.0, 0.0, -10.0));
        add(camera);
        // picking cameras
        final Set<PickingHint> pickingHints = getSceneBuilder().copyHints(PickingHint.class);
        if (pickingHints.contains(PickingHint.NONE)) {
            // no picking cameras
        } else {
            for (final PickingHint pickingHint : pickingHints) {
                final Class<? extends PickingCamera> pickingType = pickingHint.getCameraType();
                final PickingCamera picking = pickingType == null ? null : getFactory().createPickingCamera(this, camera, pickingType);
                add(picking);
            }
        }
    }

    protected static class GLEventListenerImpl implements GLEventListener {

        protected final ExecutorService executor = Executors.newFixedThreadPool(2, new NamedThreadFactory(AbstractScene.class, "Update"));
        protected final Runnable runnable = new RunnableImpl();
        protected final AbstractScene<?> scene;
        protected final SceneBuilder builder;
        protected Future<?> running;

        protected GLEventListenerImpl(final AbstractScene<?> scene, final SceneBuilder builder) {
            this.scene = scene;
            this.builder = builder;
        }

        @Override
        public void init(final GLAutoDrawable drawable) {
            logger.log(Level.FINE, "{0}: Initializing the scene.", this);
            final GLUgl2 glu;
            synchronized (scene) {
                glu = new GLUgl2();
                scene.glu = glu;
            }
            // reset all of the OpenGL scene options
            // set up the gl fields
            final GL2 gl;
            synchronized (scene) {
                scene.gl = drawable.getGL() != null ? drawable.getGL().getGL2() : null;
                final GL glt = scene.getGL();
                gl = glt != null ? glt.getGL2() : null;
            }
            // initialize OpenGL
            scene.sceneRenderer.initialize(gl, glu);
            scene.initialize(gl);
            // check load status
            final boolean load;
            synchronized (scene) {
                load = !StateUtil.isValue(scene.state, LOADED_MASK);
                if (load) {
                    scene.state = StateUtil.setValue(scene.state, LOADED_MASK);
                }
            }
            if (load) {
                logger.log(Level.INFO, "Configuring the specified hints: {0}", builder.getHints());
                // configure the hints prior to loading the scene
                ConfigUtil.configureHints(builder, gl);
                // load the scene
                logger.log(Level.INFO, "Loading the scene using hints: {0}", builder.getHints());
                scene.load();
                scene.getPropertyChangeSupport().firePropertyChange(Scene.LOADED, false, true);
            }
            scene.draw();
        }

        @Override
        public void dispose(final GLAutoDrawable drawable) {
            logger.log(Level.FINE, "{0}: Disposing the scene.", this);
            final GLUgl2 glu;
            synchronized (scene) {
                glu = scene.glu;
            }
            final GL glt = scene.getGL();
            final GL2 gl = glt != null ? glt.getGL2() : null;
            if (gl != null) {
                // dispose objects
                final Set<Disposable> disposables = scene.lookupAll(Disposable.class);
                for (final Disposable disposable : disposables) {
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
                }
                // uninitialize objects
                final Set<Initializable> initializables = scene.lookupAll(Initializable.class);
                for (final Initializable initializable : initializables) {
                    if (!initializable.isInitialized()) {
                        initializable.unInitialize(gl, glu);
                    }
                }
            }
        }

        @Override
        public void display(final GLAutoDrawable drawable) {
            final Rectangle viewport = scene.sceneUtil.getViewport();
            if (viewport.getWidth() > 0 && viewport.getHeight() > 0) {
                final GLUgl2 glu;
                synchronized (scene) {
                    glu = scene.glu;
                }
                final GL glt = scene.getGL();
                final GL2 gl = glt != null ? glt.getGL2() : null;
                // schedule update
                synchronized (this) {
                    if (running == null || running.isDone()) {
                        running = executor.submit(runnable);
                    }
                }
                // render
                if (gl != null) {
                    if (scene.sceneRenderer.performRender(gl, glu)) {
                        try {
                            scene.component.swapBuffers();
                        } catch (final GLException ex) {
                            if (scene.getSceneBuilder().isVerbose()) {
                                logger.log(Level.WARNING, MessageFormat.format("{0}: Exception while swapping buffers.", scene), ex);
                            }
                        }
                    }
                    // screenshot
                    scene.screenshot.dispatch(gl);
                }
            }
        }

        @Override
        public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, final int height) {
            logger.log(Level.FINE, "{0}: Reshaping the scene.", this);
            final Rectangle viewport = new Rectangle(x, y, width, height);
            scene.sceneUtil.setViewport(viewport);
        }

        /**
         * Runnable for updating the scene.
         */
        protected class RunnableImpl implements Runnable {

            /**
             * Constructor
             */
            protected RunnableImpl() {
            }

            @Override
            public void run() {
                scene.sceneRenderer.performUpdate();
            }
        }
    }
}
