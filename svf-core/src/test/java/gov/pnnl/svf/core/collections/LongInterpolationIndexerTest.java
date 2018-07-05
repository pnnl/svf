package gov.pnnl.svf.core.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class LongInterpolationIndexerTest {

    private static final List<Long> VALUES = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

    public LongInterpolationIndexerTest() {
    }

    /**
     * Test of reset method, of class LongInterpolationIndexer.
     */
    @Test
    public void testReset() {
        final List<Object> order = new ArrayList<>(VALUES.size());
        final Set<Integer> indexes = new HashSet<>();
        final AtomicInteger iterations = new AtomicInteger(0);
        final LongInterpolationIndexer indexer = new LongInterpolationIndexer(0L, 10L, 7L, VALUES.size());
        for (int i = 0; i < VALUES.size() / 2; i++) {
            indexer.next();
        }
        indexer.reset(3L);
        int i = indexer.next();
        while (i != -1) {
            order.add(i);
            indexes.add(i);
            iterations.incrementAndGet();
            i = indexer.next();
        }
        System.out.println("LongInterpolationIndexer Reset Order: " + order);
        Assert.assertEquals(VALUES.size(), iterations.get());
        Assert.assertEquals(VALUES.size(), indexes.size());
    }

    /**
     * Test of peek method, of class LongInterpolationIndexer.
     */
    @Test
    public void testPeek() {
        final LongInterpolationIndexer indexer = new LongInterpolationIndexer(0L, 10L, 7L, VALUES.size());
        int i = indexer.next();
        while (i != -1) {
            Assert.assertEquals(i, indexer.peek());
            i = indexer.next();
        }
    }

    /**
     * Test of next method, of class LongInterpolationIndexer.
     */
    @Test
    public void testNext() {
        final List<Object> order = new ArrayList<>(VALUES.size());
        final Set<Integer> indexes = new HashSet<>();
        final AtomicInteger iterations = new AtomicInteger(0);
        final LongInterpolationIndexer indexer = new LongInterpolationIndexer(0L, 10L, 7L, VALUES.size());
        int i = indexer.next();
        while (i != -1) {
            order.add(i);
            indexes.add(i);
            iterations.incrementAndGet();
            i = indexer.next();
        }
        System.out.println("LongInterpolationIndexer Reset Order: " + order);
        Assert.assertEquals(VALUES.size(), iterations.get());
        Assert.assertEquals(VALUES.size(), indexes.size());
    }

    /**
     * Test of getMin method, of class LongInterpolationIndexer.
     */
    @Test
    public void testGetMin() {
        final LongInterpolationIndexer indexer = new LongInterpolationIndexer(0L, 10L, 7L, VALUES.size());
        Assert.assertEquals(0L, indexer.getMin());
    }

    /**
     * Test of getMax method, of class LongInterpolationIndexer.
     */
    @Test
    public void testGetMax() {
        final LongInterpolationIndexer indexer = new LongInterpolationIndexer(0L, 10L, 7L, VALUES.size());
        Assert.assertEquals(10L, indexer.getMax());
    }

    /**
     * Test of getValue method, of class LongInterpolationIndexer.
     */
    @Test
    public void testGetValue() {
        final LongInterpolationIndexer indexer = new LongInterpolationIndexer(0L, 10L, 7L, VALUES.size());
        Assert.assertEquals(7L, indexer.getValue());
    }

}
