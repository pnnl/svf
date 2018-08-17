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
 */
public class LongInterpolationIndexer extends AbstractInterpolationIndexer {

    private final long min;
    private final long max;
    private long value;

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
    public LongInterpolationIndexer(final long min, final long max, final long value, final int size) {
        super(size);
        if (min > max) {
            throw new IllegalArgumentException("min > max");
        }
        this.min = min;
        this.max = max;
        reset(value);
    }

    protected LongInterpolationIndexer(final Builder builder) {
        this(builder.min(), builder.max(), builder.value(), builder.size());
    }

    /**
     * Reset the indexer to a starting state.
     *
     * @param value the value to reset with
     *
     * @throws NullPointerException     if value is null
     * @throws IllegalArgumentException if value is out of range
     */
    public void reset(final long value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException("value");
        }
        this.value = value;
        // find a starting point
        this.index = (int) MathUtil.interpolation(new long[]{this.min, 0L},
                                                  new long[]{this.max, (long) (size - 1)},
                                                  new long[]{this.value, 0L}, Axis.Y)[CartesianConst.Y];
        this.low = -1;
        this.high = -1;
    }

    /**
     * @return the minimum value
     */
    public long getMin() {
        return min;
    }

    /**
     * @return the maximum value
     */
    public long getMax() {
        return max;
    }

    /**
     * @return the value
     */
    public long getValue() {
        return value;
    }

    public static class Builder extends AbstractInterpolationIndexer.Builder {

        protected long min;
        protected long max;
        protected long value;

        protected Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        @Override
        public int size() {
            return super.size();
        }

        @Override
        public Builder size(final int size) {
            return (Builder) super.size(size);
        }

        public long min() {
            return this.min;
        }

        public long max() {
            return this.max;
        }

        public long value() {
            return this.value;
        }

        public Builder min(final long min) {
            this.min = min;
            return this;
        }

        public Builder max(final long max) {
            this.max = max;
            return this;
        }

        public Builder value(final long value) {
            this.value = value;
            return this;
        }

        public LongInterpolationIndexer build() {
            return new LongInterpolationIndexer(this);
        }
    }

}
