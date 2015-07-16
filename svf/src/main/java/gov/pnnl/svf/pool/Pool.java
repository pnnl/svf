package gov.pnnl.svf.pool;

import gov.pnnl.svf.scene.Disposable;

/**
 * Interface used to represent a pool of objects.
 *
 * @author Arthur Bleeker
 *
 * @param <T> type of object managed by this pool
 */
public interface Pool<T> extends Disposable {

    /**
     * The default maximum allowable pool size.
     */
    int DEFAULT_MAX_POOL_SIZE = 100;
    /**
     * Signifies a pool size that will never shrink.
     */
    int UNBOUNDED_MAX_POOL_SIZE = 0;

    /**
     * Puts an object back into the pool. Objects are validated on checkout.
     *
     * @param object object to place back in the pool
     */
    void checkIn(T object);

    /**
     * This method will return a fully constructed and validated object from the
     * pool or create a new one.
     *
     * @param id   id for the new object
     * @param data optional data for object creation
     *
     * @return an actor
     */
    T checkOut(String id, Object data);

    /**
     * @return the type or id of the pool
     */
    String getPoolType();

    /**
     * @return the class of object contained in this pool
     */
    Class<T> getPoolClass();

    /**
     * This method should be used sparingly.
     *
     * @return the number of objects in the pool
     */
    int getPoolSize();

    /**
     * @return the maximum number of objects allowed in the pool
     */
    int getMaxPoolSize();

}
