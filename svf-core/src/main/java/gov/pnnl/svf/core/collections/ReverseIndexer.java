package gov.pnnl.svf.core.collections;

/**
 * This indexer will iterate through indices in a linear fashion in reverse
 * order.
 *
 * @author Amelia Bleeker
 */
public class ReverseIndexer implements Indexer {

    private final int size; // the size of the collection
    private int index; // the current index
    private int prev; // the previous index
    private int count; // the current count

    /**
     * Constructor
     *
     * @param size the size of the collection (number of values to iterate
     *             through)
     *
     * @throws IllegalArgumentException if size is less than zero
     */
    public ReverseIndexer(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size");
        }
        this.size = size;
        this.index = this.size - 1;
    }

    protected ReverseIndexer(final Builder builder) {
        this(builder.size());
    }

    @Override
    public int peek() {
        return prev;
    }

    @Override
    public int next() {
        if (index == -1) {
            return index;
        }
        count++;
        prev = index;
        return index--;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public int size() {
        return size;
    }

    public static class Builder {

        protected int size;

        protected Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public int size() {
            return this.size;
        }

        public Builder size(final int size) {
            this.size = size;
            return this;
        }

        public ReverseIndexer build() {
            return new ReverseIndexer(this);
        }
    }

}
