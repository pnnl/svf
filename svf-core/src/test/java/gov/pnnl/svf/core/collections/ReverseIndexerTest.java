package gov.pnnl.svf.core.collections;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class ReverseIndexerTest {

    public ReverseIndexerTest() {
    }

    /**
     * Test of peek method, of class ReverseIndexer.
     */
    @Test
    public void testPeek() {
        final Indexer indexer = new ReverseIndexer(101);
        for (int i = 100; i >= 0; i--) {
            Assert.assertEquals(i, indexer.next());
            Assert.assertEquals(i, indexer.peek());
        }
    }

    /**
     * Test of next method, of class ReverseIndexer.
     */
    @Test
    public void testNext() {
        final Indexer indexer = new ReverseIndexer(101);
        for (int i = 100; i >= 0; i--) {
            Assert.assertEquals(i, indexer.next());
        }
    }

    /**
     * Test of count method, of class ReverseIndexer.
     */
    @Test
    public void testCount() {
        final Indexer indexer = new ReverseIndexer(101);
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
     * Test of size method, of class ReverseIndexer.
     */
    @Test
    public void testSize() {
        final Indexer indexer = new ReverseIndexer(101);
        Assert.assertEquals(101, indexer.size());
    }

}
