package gov.pnnl.svf.core.collections;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class AbstractInterpolationIndexerTest {

    public AbstractInterpolationIndexerTest() {
    }

    /**
     * Test of size method, of class AbstractInterpolationIndexer.
     */
    @Test
    public void testSize() {
        final AbstractInterpolationIndexer indexer = new DoubleInterpolationIndexer(0.0, 1.0, 0.5, 10001);
        Assert.assertEquals(10001, indexer.size());
    }

    /**
     * Test of count method, of class AbstractInterpolationIndexer.
     */
    @Test
    public void testCount() {
        final AbstractInterpolationIndexer indexer = new DoubleInterpolationIndexer(0.0, 1.0, 0.5, 10001);
        Assert.assertEquals(0, indexer.count());
        indexer.next();
        Assert.assertEquals(1, indexer.count());
        for (int i = 0; i < 10000; i++) {
            indexer.next();
        }
        Assert.assertEquals(10001, indexer.count());
    }

    /**
     * Test of peek method, of class AbstractInterpolationIndexer.
     */
    @Test
    public void testPeek() {
        final AbstractInterpolationIndexer indexer = new DoubleInterpolationIndexer(0.0, 1.0, 0.5, 10001);
        indexer.next();
    }

    /**
     * Test of next method, of class AbstractInterpolationIndexer.
     */
    @Test
    public void testNext() {
        final AbstractInterpolationIndexer indexer = new DoubleInterpolationIndexer(0.0, 1.0, 0.5, 2);
        Assert.assertEquals(0, indexer.next());
        Assert.assertEquals(1, indexer.next());
        Assert.assertEquals(-1, indexer.next());
    }

}
