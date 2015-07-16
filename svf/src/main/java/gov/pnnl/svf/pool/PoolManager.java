package gov.pnnl.svf.pool;

import gov.pnnl.svf.scene.Disposable;

/**
 * This interface is a marker for a manager that manages one of the pools. This
 * interface should be subclassed to mark a specific pool type.
 *
 * @author Arthur Bleeker
 * @param <P> Type of object stored in pool
 */
public interface PoolManager<P extends Pool<?>> extends Disposable {

    /**
     * Add another pool to the pool manager. Only one pool for each class and
     * type combination can be added.
     *
     * @param pool the pool to add
     */
    void addPool(P pool);

    /**
     * Returns the pool for the class and type if one exists.
     *
     * @param clazz the object type of pool to retrieve
     * @param type  the type of pool
     * @param <T>   the type of pool to retrieve
     *
     * @return the pool or null
     */
    <T> Pool<T> getPool(Class<T> clazz, String type);
}
