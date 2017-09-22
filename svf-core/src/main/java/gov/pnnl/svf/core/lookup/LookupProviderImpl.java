package gov.pnnl.svf.core.lookup;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class used for providing a container for classes that are added and looked up
 * according to their class, superclass, and interface types. This can be used
 * as a form of inversion of control or more specifically a type of dependency
 * injection called service lookup. It can also be used for dynamic polymorphism
 * of objects.
 *
 * @author Arthur Bleeker
 *
 */
public class LookupProviderImpl implements LookupProvider {

    private final Map<Class<?>, Object> map = Collections.synchronizedMap(new HashMap<>());

    /**
     * Constructor
     */
    public LookupProviderImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param lookupProvider the lookup provider to copy
     *
     * @throws NullPointerException if lookup provider is null
     */
    public LookupProviderImpl(final LookupProvider lookupProvider) {
        super();
        if (lookupProvider == null) {
            throw new NullPointerException("lookupProvider");
        }
        if (lookupProvider instanceof LookupProviderImpl) {
            synchronized (((LookupProviderImpl) lookupProvider).map) {
                ((LookupProviderImpl) lookupProvider).map.entrySet().forEach((entry) -> {
                    map.put(entry.getKey(), entry.getValue());
                });
            }
        } else {
            lookupProvider.lookupAll().forEach((object) -> {
                addSuperclass(object.getClass(), object);
            });
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public <T extends Object> void add(final T object) {
        if (object == null) {
            throw new NullPointerException("object");
        }
        if (object instanceof Class<?>) {
            throw new IllegalArgumentException("object");
        }
        synchronized (this) {
            addSuperclass(object.getClass(), object);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Object> T lookup(final Class<? extends T> type) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        return (T) map.get(type);
    }

    @Override
    public <T extends Object> boolean remove(final T object) {
        if (object == null) {
            throw new NullPointerException("object");
        }
        if (object instanceof Class<?>) {
            throw new IllegalArgumentException("object");
        }
        boolean removed = false;
        synchronized (map) {
            final Iterator<Entry<Class<?>, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getValue() == object) {
                    iterator.remove();
                    removed = true;
                }
            }
        }
        return removed;
    }

    @Override
    public Set<Object> lookupAll() {
        final Set<Object> copy;
        synchronized (map) {
            copy = new HashSet<>(map.values());
        }
        return copy;
    }

    @Override
    public void lookupAll(final Collection<Object> out) {
        synchronized (map) {
            out.addAll(map.values());
        }
    }

    private <T extends Object> void addSuperclass(final Class<? extends T> type, final Object object) {
        if (type != null) {
            map.put(type, object);
            // add additional interfaces if there are any
            for (final Class<?> itype : type.getInterfaces()) {
                addSuperclass(itype, object);
            }
            addSuperclass(type.getSuperclass(), object);
        }
    }
}
