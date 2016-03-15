/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.core.collections;

/**
 * This indexer will iterate through indices in a linear fashion in reverse
 * order.
 *
 * @author D3X573
 */
public class ReverseIndexer implements Indexer {

    private final int size; // the size of the collection
    private int index; // the current index

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

    @Override
    public int peek() {
        return index;
    }

    @Override
    public int next() {
        if (index == -1) {
            return index;
        }
        return index--;
    }

    @Override
    public int count() {
        return index == -1 ? size : size - index - 1;
    }

    @Override
    public int size() {
        return size;
    }

}
