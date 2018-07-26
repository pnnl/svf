package gov.pnnl.svf.core.collections;

/**
 * An indexer is used to iterate through a series of indices. An indexer is
 * guaranteed to iterate through every index one time.
 *
 * @author Amelia Bleeker
 */
public interface Indexer {

    /**
     * @return the current index
     */
    int peek();

    /**
     * @return the next index or -1 if there is no next index
     */
    int next();

    /**
     * @return the current number of returned indices
     */
    int count();

    /**
     * @return the total number of indices.
     */
    int size();

}
