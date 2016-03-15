/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.core.collections;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author D3X573
 */
public class LinearIndexerTest {

    public LinearIndexerTest() {
    }

    /**
     * Test of peek method, of class LinearIndexer.
     */
    @Test
    public void testPeek() {
        final Indexer indexer = new LinearIndexer(101);
        for (int i = 0; i < 101; i++) {
            Assert.assertEquals(i, indexer.next());
            Assert.assertEquals(i, indexer.peek());
        }
    }

    /**
     * Test of next method, of class LinearIndexer.
     */
    @Test
    public void testNext() {
        final Indexer indexer = new LinearIndexer(101);
        for (int i = 0; i < 101; i++) {
            Assert.assertEquals(i, indexer.next());
        }
    }

    /**
     * Test of count method, of class LinearIndexer.
     */
    @Test
    public void testCount() {
        final Indexer indexer = new LinearIndexer(101);
        Assert.assertEquals(0, indexer.count());
        indexer.next();
        Assert.assertEquals(1, indexer.count());
        indexer.peek();
        Assert.assertEquals(1, indexer.count());
        while (indexer.next() != -1) {
            // do nothing
        }
        Assert.assertEquals(101, indexer.count());
    }

    /**
     * Test of size method, of class LinearIndexer.
     */
    @Test
    public void testSize() {
        final Indexer indexer = new LinearIndexer(101);
        Assert.assertEquals(101, indexer.size());
    }

}
