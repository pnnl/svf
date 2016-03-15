/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.core.collections;

/**
 * Utility class for finding and creating an Indexer.
 *
 * @author D3X573
 */
public class IndexerUtil {

    /**
     * Constructor is private for static util pattern
     */
    private IndexerUtil() {
    }

    /**
     * Indexer that will step through a series of indexes according to an
     * interpolated value. E.G. A list with ten values ranging from 1 to 10. If
     * you supply a value of 7 to start then the indexes returned will be
     * something like: 6, 5, 7, 4, 8, 3, 9, 2, 1, 0. This class is not thread
     * safe.
     *
     * @param min   the minimum number
     * @param max   the maximum number
     * @param value the value
     * @param size  the size of the collection (number of values to iterate
     *              through)
     * @param <T>   the comparable number type
     *
     * @return the new indexer instance
     *
     * @throws NullPointerException     if any arguments are null
     * @throws IllegalArgumentException if size is less than zero, min is
     *                                  greater than max, or value is out of
     *                                  range
     */
    public static <T extends Number & Comparable<T>> Indexer create(final T min, final T max, final T value, final int size) {
        return new InterpolationIndexer<>(min, max, value, size);
    }

    /**
     * Indexer that will step through a series of indexes according to an
     * interpolated value. E.G. A list with ten values ranging from 1 to 10. If
     * you supply a value of 7 to start then the indexes returned will be
     * something like: 6, 5, 7, 4, 8, 3, 9, 2, 1, 0. This class is not thread
     * safe.
     *
     * @param min   the minimum number
     * @param max   the maximum number
     * @param value the value
     * @param size  the size of the collection (number of values to iterate
     *              through)
     *
     * @return the new indexer instance
     *
     * @throws NullPointerException     if any arguments are null
     * @throws IllegalArgumentException if size is less than zero, min is
     *                                  greater than max, or value is out of
     *                                  range
     */
    public static Indexer create(final double min, final double max, final double value, final int size) {
        return new DoubleInterpolationIndexer(min, max, value, size);
    }

    /**
     * Indexer that will step through a series of indexes according to an
     * interpolated value. E.G. A list with ten values ranging from 1 to 10. If
     * you supply a value of 7 to start then the indexes returned will be
     * something like: 6, 5, 7, 4, 8, 3, 9, 2, 1, 0. This class is not thread
     * safe.
     *
     * @param min   the minimum number
     * @param max   the maximum number
     * @param value the value
     * @param size  the size of the collection (number of values to iterate
     *              through)
     *
     * @return the new indexer instance
     *
     * @throws NullPointerException     if any arguments are null
     * @throws IllegalArgumentException if size is less than zero, min is
     *                                  greater than max, or value is out of
     *                                  range
     */
    public static Indexer create(final long min, final long max, final long value, final int size) {
        return new LongInterpolationIndexer(min, max, value, size);
    }

    /**
     * This indexer will iterate through indices in a linear fashion.
     *
     * @param size the size of the collection (number of values to iterate
     *             through)
     *
     * @return the new indexer instance
     *
     * @throws IllegalArgumentException if size is less than zero
     */
    public static Indexer create(final int size) {
        return new LinearIndexer(size);
    }

    /**
     * This indexer will iterate through indices in a linear fashion in reverse
     * order.
     *
     * @param size    the size of the collection (number of values to iterate
     *                through)
     * @param reverse true to return an indexer that will operate in reverse
     *
     * @return the new indexer instance
     *
     * @throws IllegalArgumentException if size is less than zero
     */
    public static Indexer create(final int size, final boolean reverse) {
        if (reverse) {
            return new ReverseIndexer(size);
        } else {
            return new LinearIndexer(size);
        }
    }
}
