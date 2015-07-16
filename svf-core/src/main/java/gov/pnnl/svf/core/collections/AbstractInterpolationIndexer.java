package gov.pnnl.svf.core.collections;

/**
 * Abstract base class for an indexer that will step through a series of indexes
 * according to an interpolated value. E.G. A list with ten values ranging from
 * 1 to 10. If you supply a value of 7 to start then the indexes returned will
 * be something like: 6, 5, 7, 4, 8, 3, 9, 2, 1, 0. This class is not thread
 * safe.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractInterpolationIndexer {

    protected final int size; // the size of the collection
    protected int index; // the current index
    protected int low; // the lowest index used
    protected int high; // the highest index used
    protected boolean dir; // the current direction

    /**
     * Constructor
     *
     * @param size the size of the collection (number of values to iterate
     *             through)
     *
     * @throws IllegalArgumentException if size is less than zero
     */
    protected AbstractInterpolationIndexer(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size");
        }
        this.size = size;
    }

    /**
     * @return the current index
     */
    public int peek() {
        return index;
    }

    /**
     * @return the next index or -1 if there is no next index
     */
    public int next() {
        if (low == -1 || high == -1) {
            // starting point
            low = index;
            high = index;
            return index;
        }
        if (dir = !dir) {
            // attempt to go high
            if (high == size - 1) {
                // can't go high so go low
                if (low == 0) {
                    // can't go low so quit
                    return -1;
                }
                low--;
                index = low;
            } else {
                high++;
                index = high;
            }
        } else {
            // attempt to go low
            if (low == 0) {
                // can't go low so go high
                if (high == size - 1) {
                    // can't go high so quit
                    return -1;
                }
                high++;
                index = high;
            } else {
                low--;
                index = low;
            }
        }
        return index;
    }

}
