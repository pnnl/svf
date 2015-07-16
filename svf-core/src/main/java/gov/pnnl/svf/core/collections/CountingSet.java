package gov.pnnl.svf.core.collections;

import java.util.Set;

/**
 * Interface for a set that counts the number of instances added to a set. The
 * count will increase by one every time a duplicate item is added. The count
 * will reset to zero when that item is removed from the counting set.
 *
 * @param <E> the type of elements maintained by this set
 *
 * @author Arthur Bleeker
 */
public interface CountingSet<E> extends Set<E> {

    /**
     * The number of items added for this element.
     *
     * @param element the element
     *
     * @return the number of items added for this element
     */
    int getCount(E element);
}
