package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.test.WeakEqualsHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class LinearIndexerTest {

    public LinearIndexerTest() {
    }

    /**
     * Test of Builder method, of class LinearIndexer.
     */
    @Test
    public void testBuilder() {
        Assert.assertTrue(WeakEqualsHelper.weakEquals(new LinearIndexer(10),
                LinearIndexer.Builder.construct().size(10).build()
        ));
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
