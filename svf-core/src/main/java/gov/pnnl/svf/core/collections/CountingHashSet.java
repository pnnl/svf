package gov.pnnl.svf.core.collections;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * Interface for a set that counts the number of instances added to a set. The
 * count will increase by one every time an item is added. The count will
 * decrease by one when that item is removed from the counting set. The current
 * implementation is roughly half as performant as a HashSet.
 *
 * @param <E> the type of elements maintained by this set
 *
 * @author Amelia Bleeker
 */
public class CountingHashSet<E> extends HashSet<E> implements CountingSet<E> {

    /**
     * The empty set (immutable). This set is serializable.
     *
     * @see #emptySet()
     */
    @SuppressWarnings("rawtypes")
    public static final CountingSet EMPTY_COUNTING_SET = new EmptyCountingSet();
    private static final long serialVersionUID = 1L;
    private HashMap<E, Integer> map;

    /**
     * Constructs a new, empty counting set; the backing {@code HashMap}
     * instance has default initial capacity (16) and load factor (0.75).
     */
    public CountingHashSet() {
        super();
        map = new HashMap<>();
    }

    /**
     * Constructs a new counting set containing the elements in the specified
     * collection. The {@code HashMap} is created with default load factor
     * (0.75) and an initial capacity sufficient to contain the elements in the
     * specified collection.
     *
     * @param c the collection whose elements are to be placed into this set
     *
     * @throws NullPointerException if the specified collection is null
     */
    public CountingHashSet(final Collection<? extends E> c) {
        super();
        map = new HashMap<>(Math.max((int) (c.size() / .75f) + 1, 16));
        addAll(c);
    }

    /**
     * Constructs a new, empty counting set; the backing {@code HashMap}
     * instance has the specified initial capacity and the specified load
     * factor.
     *
     * @param initialCapacity the initial capacity of the hash map
     * @param loadFactor the load factor of the hash map
     *
     * @throws IllegalArgumentException if the initial capacity is less than
     * zero, or if the load factor is nonpositive
     */
    public CountingHashSet(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * Constructs a new, empty counting set; the backing {@code HashMap}
     * instance has the specified initial capacity and default load factor
     * (0.75).
     *
     * @param initialCapacity the initial capacity of the hash table
     *
     * @throws IllegalArgumentException if the initial capacity is less than
     * zero
     */
    public CountingHashSet(final int initialCapacity) {
        super(initialCapacity);
        map = new HashMap<>(initialCapacity);
    }

    @Override
    public boolean add(final E e) {
        final Integer count = map.get(e);
        map.put(e, count == null ? 1 : count + 1);
        return super.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        final Integer count = map.get((E) o);
        if (count != null && count > 1) {
            map.put((E) o, count - 1);
        } else {
            map.remove((E) o);
        }
        return super.remove(o);
    }

    @Override
    public void clear() {
        map.clear();
        super.clear();
    }

    @Override
    public int getCount(final E element) {
        final Integer count = map.get(element);
        if (count != null) {
            return count;
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        final CountingHashSet<E> newSet = (CountingHashSet<E>) super.clone();
        newSet.map = (HashMap<E, Integer>) map.clone();
        return newSet;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + (this.map != null ? this.map.hashCode() : 0);
        return hash;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountingSet<?>)) {
            return false;
        }
        final boolean e = super.equals(o);
        try {
            return e && mapEquals((CountingSet<E>) o);
        } catch (final ClassCastException ex) {
            return false;
        }
    }

    /**
     * Returns the empty counting set (immutable). This set is serializable.
     * Unlike the like-named field, this method is parameterized.
     *
     * <p>
     * This example illustrates the type-safe way to obtain an empty counting
     * set:
     * <pre>
     *     CountingSet&lt;String&gt; s = CountingHashSet.emptySet();
     * </pre> Implementation note: Implementations of this method need not
     * create a separate {@code CountingSet} object for each call. Using this
     * method is likely to have comparable cost to using the like-named field.
     * (Unlike this method, the field does not provide type safety.)
     *
     * @param <T> the type of elements for the empty counting set
     *
     * @return an empty counting set
     *
     * @see #EMPTY_COUNTING_SET
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    public static <T> CountingSet<T> emptySet() {
        return EMPTY_COUNTING_SET;
    }

    /**
     * Returns an immutable counting set containing only the specified object.
     * The returned set is serializable.
     *
     * @param o the sole object to be stored in the returned counting set.
     *
     * @return an immutable counting set containing only the specified object.
     *
     * @param <T> type of item contained in the set
     */
    public static <T> CountingSet<T> singleton(final T o) {
        return new SingletonCountingSet<>(o);
    }

    private boolean mapEquals(final CountingSet<E> o) {
        final Iterator<Entry<E, Integer>> e = map.entrySet().iterator();
        while (e.hasNext()) {
            final Entry<E, Integer> n = e.next();
            if (!n.getValue().equals(o.getCount(n.getKey()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the specified arguments are equal, or both null.
     *
     * @param o1 first object
     * @param o2 second object
     *
     * @return true if equal
     */
    private static boolean eq(final Object o1, final Object o2) {
        return (o1 == null ? o2 == null : o1.equals(o2));
    }

    /**
     * @serial include
     */
    private static class EmptyCountingSet extends AbstractSet<Object> implements CountingSet<Object>, Serializable {
        // use serialVersionUID from JDK 1.2.2 for interoperability

        private static final long serialVersionUID = 1582296315990362920L;

        @Override
        public Iterator<Object> iterator() {
            return new IteratorImpl();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean contains(final Object obj) {
            return false;
        }

        @Override
        public int getCount(final Object element) {
            return 0;
        }

        // Preserves singleton property
        private Object readResolve() {
            return EMPTY_COUNTING_SET;
        }

        /**
         * Iterator used for immutable collection
         */
        protected static class IteratorImpl implements Iterator<Object> {

            /**
             * Constructor
             */
            protected IteratorImpl() {
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * @serial include
     */
    private static class SingletonCountingSet<E> extends AbstractSet<E> implements CountingSet<E>, Serializable {
        // use serialVersionUID from JDK 1.2.2 for interoperability

        private static final long serialVersionUID = 3193687207550431679L;
        protected final E element;

        SingletonCountingSet(final E e) {
            element = e;
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                private boolean hasNext = true;

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public E next() {
                    if (hasNext) {
                        hasNext = false;
                        return element;
                    }
                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean contains(final Object o) {
            return CountingHashSet.eq(o, element);
        }

        @Override
        public int getCount(final E e) {
            return e == null ? 0 : e.equals(element) ? 1 : 0;
        }
    }
}
