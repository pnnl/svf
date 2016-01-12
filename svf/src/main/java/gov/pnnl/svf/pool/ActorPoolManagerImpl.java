package gov.pnnl.svf.pool;

import gov.pnnl.svf.core.collections.KeyValuePair;
import gov.pnnl.svf.core.util.StateUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages all of the actor pools. This class is thread safe.
 *
 * @author Arthur Bleeker
 */
public class ActorPoolManagerImpl implements ActorPoolManager {

    private static final byte DISPOSED_MASK = StateUtil.getMasks()[1];
    private byte state = StateUtil.NONE;
    private final Map<KeyValuePair<Class<?>, String>, ActorPool<?>> pools;

    /**
     * Constructor
     */
    public ActorPoolManagerImpl() {
        pools = Collections.synchronizedMap(new HashMap<KeyValuePair<Class<?>, String>, ActorPool<?>>());
    }

    @Override
    public void addPool(final ActorPool<?> pool) {
        if (pool == null) {
            throw new NullPointerException("pool");
        }
        final KeyValuePair<Class<?>, String> key = new KeyValuePair<Class<?>, String>(pool.getPoolClass(), pool.getPoolType());
        synchronized (pools) {
            final ActorPool<?> existing = pools.put(key, pool);
            if (existing != null) {
                // put it back the way it was
                pools.put(key, existing);
                throw new IllegalArgumentException("pool");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Pool<T> getPool(final Class<T> clazz, final String type) {
        final KeyValuePair<Class<?>, String> key = new KeyValuePair<Class<?>, String>(clazz, type);
        return (Pool<T>) pools.get(key);
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
        final List<ActorPool<?>> temp;
        synchronized (pools) {
            temp = new ArrayList<>(pools.values());
        }
        for (final ActorPool<?> pool : temp) {
            pool.dispose();
        }
        pools.clear();
    }
}
