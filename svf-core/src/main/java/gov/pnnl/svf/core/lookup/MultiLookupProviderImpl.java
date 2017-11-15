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
public class MultiLookupProviderImpl extends LookupProviderImpl implements MultiLookupProvider {

    // linked hash set is used to preserve ordering and enable fast searching
    private final Map<Class<?>, Set<? extends Object>> map = Collections.synchronizedMap(new HashMap<>());

    /**
     * Constructor
     */
    public MultiLookupProviderImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param multiLookupProvider the multi lookup provider to copy
     *
     * @throws NullPointerException if multi lookup provider is null
     */
    public MultiLookupProviderImpl(final MultiLookupProvider multiLookupProvider) {
        super(multiLookupProvider);
        if (multiLookupProvider instanceof MultiLookupProviderImpl) {
            synchronized (((MultiLookupProviderImpl) multiLookupProvider).map) {
                ((MultiLookupProviderImpl) multiLookupProvider).map.entrySet().forEach((entry) -> {
                    map.put(entry.getKey(), entry.getValue().size() == 1 ? entry.getValue() : new HashSet<>(entry.getValue()));
                });
            }
        } else {
            multiLookupProvider.lookupAll().forEach((object) -> {
                addSuperclass(object.getClass(), object);
            });
        }
    }

    @Override
    public void clear() {
        super.clear();
        synchronized (map) {
            for (final Set<? extends Object> list : map.values()) {
                if (list != null && list.size() > 1) {
                    list.clear();
                }
            }
            map.clear();
        }
    }

    @Override
    public <T extends Object> void add(final T object) {
        if (object == null) {
            throw new NullPointerException("object");
        }
        if (object instanceof Class<?>) {
            throw new IllegalArgumentException("object");
        }
        // add to the single object lookup
        super.add(object);
        // add to the multi object lookup
        addSuperclass(object.getClass(), object);
    }

    @Override
    public <T extends Object> void addAll(final Collection<T> objects) {
        if (objects == null) {
            throw new NullPointerException("objects");
        }
        if (objects.isEmpty()) {
            return;
        }
        // check to make sure the first object isn't a class type
        if (objects.iterator().next() instanceof Class<?>) {
            throw new IllegalArgumentException("object");
        }
        // iterate through the collection and add them to the lookup
        for (final T object : objects) {
            // add to the single object lookup
            super.add(object);
            // add to the multi object lookup
            addSuperclass(object.getClass(), object);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Object> Set<T> lookupAll(final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        synchronized (map) {
            // use the multi object lookup
            final Set<?> obj = map.get(type);
            if (obj == null || obj.isEmpty()) {
                return Collections.<T>emptySet();
            } else if (obj.size() == 1) {
                // this will be an immutable singleton set
                return (Set<T>) obj;
            } else {
                // make a copy of the set
                final Set<T> newList = new HashSet<>();
                newList.addAll((Set<T>) obj);
                return newList;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void lookupAll(final Class<T> type, final Collection<T> out) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        if (out == null) {
            throw new NullPointerException("out");
        }
        out.clear();
        synchronized (map) {
            // use the multi object lookup
            final Set<?> obj = map.get(type);
            if (obj != null) {
                out.addAll((Set<T>) obj);
            }
        }
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
            // remove from the single object lookup
            super.remove(object);
            // remove from the multi object lookup
            final Iterator<Entry<Class<?>, Set<? extends Object>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                final Entry<Class<?>, Set<? extends Object>> entry = iterator.next();
                if (entry.getValue().size() == 1 && entry.getValue().contains(object)) {
                    iterator.remove();
                    removed = true;
                } else if (entry.getValue().remove(object)) {
                    removed = true;
                }
            }
        }
        return removed;
    }

    @Override
    public <T extends Object> boolean removeAll(final Collection<T> objects) {
        if (objects == null) {
            throw new NullPointerException("objects");
        }
        if (objects.isEmpty()) {
            return false;
        }
        // check to make sure the first object isn't a class type
        if (objects.iterator().next() instanceof Class<?>) {
            throw new IllegalArgumentException("object");
        }
        boolean removed = false;
        synchronized (map) {
            for (final T object : objects) {
                // remove from the single object lookup
                super.remove(object);
                // remove from the multi object lookup
                final Iterator<Entry<Class<?>, Set<? extends Object>>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Entry<Class<?>, Set<? extends Object>> entry = iterator.next();
                    if (entry.getValue().size() == 1 && entry.getValue().contains(object)) {
                        iterator.remove();
                        removed = true;
                    } else if (entry.getValue().remove(object)) {
                        removed = true;
                    }
                }
            }
        }
        return removed;
    }

    @Override
    public Set<Object> lookupAll() {
        return lookupAll(Object.class);
    }

    @Override
    public void lookupAll(final Collection<Object> out) {
        lookupAll(Object.class, out);
    }

    private <T extends Object> void addSuperclass(final Class<? extends T> type, final Object object) {
        if (type != null) {
            addObjectToList(type, object);
            // add additional interfaces if there are any
            for (final Class<?> itype : type.getInterfaces()) {
                addSuperclass(itype, object);
            }
            addSuperclass(type.getSuperclass(), object);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Object> void addObjectToList(final Class<? extends T> type, final T object) {
        final Set<T> list = (Set<T>) map.get(type);
        if (list == null) {
            final Set<T> temp = Collections.singleton(object);
            map.put(type, temp);
        } else if (list.size() == 1) {
            final Set<T> temp = new HashSet<>();
            temp.addAll(list);
            temp.add(object);
            map.put(type, temp);
        } else {
            list.add(object);
        }
    }
}
