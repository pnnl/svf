package gov.pnnl.svf.pool;

import gov.pnnl.svf.actor.AbstractActor;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.util.NamedThreadFactory;
import gov.pnnl.svf.core.util.StateUtil;
import gov.pnnl.svf.scene.Scene;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class is used to pool actors for scenes that need to destroy and recreate
 * actors often. This class is thread safe.
 *
 * @author Arthur Bleeker
 * @param <T> the type of actor that this pool manages
 */
public abstract class AbstractActorPool<T extends Actor> implements ActorPool<T> {

    private static final Logger logger = Logger.getLogger(AbstractActorPool.class.toString());
    /**
     * pool of actors
     */
    protected final LinkedList<T> pool;
    /**
     * executor used to run long running pool management tasks
     */
    protected final ExecutorService executor;
    /**
     * State mask for the disposed flag.
     */
    protected static final byte DISPOSED_MASK = StateUtil.getMasks()[0];
    protected byte state = StateUtil.NONE;
    private final Scene scene;
    private final Class<T> clazz;
    private final String type;
    private final int maxPoolSize;

    /**
     * Constructor
     *
     * @param scene the scene for this pool
     */
    public AbstractActorPool(final Scene scene) {
        this(scene, UNBOUNDED_MAX_POOL_SIZE);
    }

    /**
     * Constructor
     *
     * @param scene       the scene for this pool
     * @param maxPoolSize the maximum number of elements allowed in the pool
     */
    public AbstractActorPool(final Scene scene, final int maxPoolSize) {
        this(scene, null, maxPoolSize);
    }

    /**
     * Constructor
     *
     * @param scene       the scene for this pool
     * @param type        the actor type for this pool
     * @param maxPoolSize the maximum number of elements allowed in the pool
     */
    @SuppressWarnings("unchecked")
    public AbstractActorPool(final Scene scene, final String type, final int maxPoolSize) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        this.maxPoolSize = maxPoolSize;
        pool = new LinkedList<>();
        clazz = (Class<T>) AbstractActorPool.getTypeArguments(AbstractActorPool.class, this.getClass()).get(0);
        this.type = type;
        executor = Executors.newCachedThreadPool(new NamedThreadFactory(clazz.getClass(), "ActorPool_Worker"));
    }

    /**
     * @return the scene for this pool
     */
    public Scene getScene() {
        return scene;
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
        executor.shutdownNow();
        T actor;
        synchronized (pool) {
            actor = pool.poll();
        }
        while (actor != null) {
            actor.dispose();
            synchronized (pool) {
                actor = pool.poll();
            }
        }
    }

    @Override
    public String getPoolType() {
        return type;
    }

    @Override
    public Class<T> getPoolClass() {
        return clazz;
    }

    @Override
    public int getPoolSize() {
        synchronized (pool) {
            return pool.size();
        }
    }

    @Override
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * Create a new actor. This actor should be fully constructed with all
     * support objects added to it. All actors created by this pool must be of
     * the type AbstractActor.
     *
     * @param id   id for the new actor
     * @param data optional data for actor creation
     *
     * @return a newly created object
     */
    protected abstract T create(String id, Object data);

    /**
     * Initialize the actor.
     *
     * @param actor actor to initialize
     * @param data  optional data for actor initialization
     */
    protected abstract void initialize(T actor, Object data);

    /**
     * This method should validate the actor. Any actor that fails validation
     * will be disposed and removed from the pool.
     *
     * @param actor object to validate
     *
     * @return true if the actor passes validation
     */
    protected abstract boolean validate(T actor);

    @Override
    public T checkOut(final String id, final Object data) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        if (id.isEmpty()) {
            throw new NullPointerException("id");
        }
        try {
            // get an actor from the pool
            final T actor;
            synchronized (pool) {
                actor = pool.poll();
            }
            // validate it
            if (actor != null) {
                if (validate(actor)) {
                    // set the id, initialize, and return
                    ((AbstractActor) actor).setId(id);
                    initialize(actor, data);
                    return actor;
                } else {
                    // failed validation so dispose and create a new one
                    actor.dispose();
                }
            }
        } catch (final NoSuchElementException ex) {
            // no more actors in the pool
            // just drop out and create and return a new actor
            if (getScene().getExtended().getSceneBuilder().isVerbose()) {
                logger.log(Level.INFO, "{0}: Actor pool is empty.  Creating a new actor...", scene);
            }
        }
        final T actor = create(id, data);
        initialize(actor, data);
        return actor;
    }

    @Override
    public void checkIn(final T actor) {
        if (actor == null) {
            throw new NullPointerException("actor");
        }
        // remove the actor from the scene
        actor.getScene().remove(actor);
        executor.execute(new CheckInWorker(actor));
    }

    /**
     * Checks in an actor to the pool.
     */
    protected class CheckInWorker implements Runnable {

        private final T actor;

        /**
         * Constructor
         *
         * @param actor the actor to check in
         */
        protected CheckInWorker(final T actor) {
            this.actor = actor;
        }

        @Override
        public void run() {
            // we can't allow duplicates to exist in the set
            synchronized (pool) {
                for (final T t : pool) {
                    if (actor == t) {
                        // already in the pool
                        return;
                    }
                }
                // add it to the pool
                pool.offer(actor);
                // if pool is too big then remove an element
                if (getMaxPoolSize() != UNBOUNDED_MAX_POOL_SIZE && pool.size() > getMaxPoolSize()) {
                    pool.poll();
                }
            }
        }
    }

    /**
     * Get the underlying class for a type, or null if the type is a variable
     * type.
     *
     * @param type the type
     *
     * @return the underlying class
     */
    private static Class<?> getClass(final Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return AbstractActorPool.getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            final Type componentType = ((GenericArrayType) type).getGenericComponentType();
            final Class<?> componentClass = AbstractActorPool.getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get the actual type arguments a child class has used to extend a generic
     * base class.
     *
     * @param baseClass  the base class
     * @param childClass the child class
     *
     * @return a list of the raw classes for the actual type arguments.
     */
    private static <T> List<Class<?>> getTypeArguments(final Class<T> baseClass, final Class<? extends T> childClass) {
        final Map<Type, Type> resolvedTypes = new HashMap<>();
        Type type = childClass;
        // start walking up the inheritance hierarchy until we hit baseClass
        while (!AbstractActorPool.getClass(type).equals(baseClass)) {
            if (type instanceof Class) {
                // there is no useful information for us in raw types, so just keep going.
                type = ((Class<?>) type).getGenericSuperclass();
            } else {
                final ParameterizedType parameterizedType = (ParameterizedType) type;
                final Class<?> rawType = (Class<?>) parameterizedType.getRawType();

                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                final TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }

                if (!rawType.equals(baseClass)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }

        // finally, for each actual type argument provided to baseClass, determine (if possible)
        // the raw class for that type argument.
        final Type[] actualTypeArguments;
        if (type instanceof Class) {
            actualTypeArguments = ((Class<?>) type).getTypeParameters();
        } else {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        final List<Class<?>> typeArgumentsAsClasses = new ArrayList<>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments) {
            while (resolvedTypes.containsKey(baseType)) {
                baseType = resolvedTypes.get(baseType);
            }
            typeArgumentsAsClasses.add(AbstractActorPool.getClass(baseType));
        }
        return typeArgumentsAsClasses;
    }
}
