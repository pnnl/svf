package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.core.constant.CartesianConst;
import gov.pnnl.svf.core.geometry.Axis;
import gov.pnnl.svf.core.util.MathUtil;

/**
 * Indexer that will step through a series of indexes according to an
 * interpolated value. E.G. A list with ten values ranging from 1 to 10. If you
 * supply a value of 7 to start then the indexes returned will be something
 * like: 6, 5, 7, 4, 8, 3, 9, 2, 1, 0. This class is not thread safe.
 *
 * @author Amelia Bleeker
 * @param <T> the comparable number type
 */
@SuppressWarnings("unchecked")
public class InterpolationIndexer<T extends Number & Comparable<T>> extends AbstractInterpolationIndexer {

    private final Number min;
    private final Number max;
    private Number value;

    /**
     * Constructor
     *
     * @param min   the minimum number
     * @param max   the maximum number
     * @param value the value
     * @param size  the size of the collection (number of values to iterate
     *              through)
     *
     * @throws NullPointerException     if any arguments are null
     * @throws IllegalArgumentException if size is less than zero, min is
     *                                  greater than max, or value is out of
     *                                  range
     */
    public InterpolationIndexer(final T min, final T max, final T value, final int size) {
        super(size);
        if (min == null) {
            throw new NullPointerException("min");
        }
        if (max == null) {
            throw new NullPointerException("max");
        }
        this.min = min;
        this.max = max;
        reset(value);
    }

    /**
     * Reset the indexer to a starting state.
     *
     * @param value the value to reset with
     *
     * @throws NullPointerException     if value is null
     * @throws IllegalArgumentException if value is out of range
     */
    public void reset(final T value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        if (((Comparable) min).compareTo((Comparable) max) > 0) {
            throw new IllegalArgumentException("min > max");
        }
        if (((Comparable) value).compareTo((Comparable) min) < 0
            || ((Comparable) value).compareTo((Comparable) max) > 0) {
            throw new IllegalArgumentException("value");
        }
        this.value = value;
        // find a starting point
        this.index = (int) MathUtil.interpolation(new double[]{this.min.doubleValue(), 0.0},
                                                  new double[]{this.max.doubleValue(), (double) (size - 1)},
                                                  new double[]{this.value.doubleValue(), 0.0}, Axis.Y)[CartesianConst.Y];
        this.low = -1;
        this.high = -1;
        this.dir = false;
    }

    /**
     * @return the minimum value
     */
    public T getMin() {
        return (T) min;
    }

    /**
     * @return the maximum value
     */
    public T getMax() {
        return (T) max;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return (T) value;
    }

}
