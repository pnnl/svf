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
 * @author Arthur Bleeker
 */
public class InterpolationIndexerTest {

    private static final List<Double> VALUES = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);

    public InterpolationIndexerTest() {
    }

    /**
     * Test of reset method, of class InterpolationIndexer.
     */
    @Test
    public void testReset() {
        final List<Object> order = new ArrayList<>(VALUES.size());
        final Set<Integer> indexes = new HashSet<>();
        final AtomicInteger iterations = new AtomicInteger(0);
        final InterpolationIndexer<Double> indexer = new InterpolationIndexer<>(0.0, 10.0, 7.0, VALUES.size());
        for (int i = 0; i < VALUES.size() / 2; i++) {
            indexer.next();
        }
        indexer.reset(3.0);
        int i = indexer.next();
        while (i != -1) {
            order.add(i);
            indexes.add(i);
            iterations.incrementAndGet();
            i = indexer.next();
        }
        System.out.println("InterpolationIndexer Reset Order: " + order);
        Assert.assertEquals(VALUES.size(), iterations.get());
        Assert.assertEquals(VALUES.size(), indexes.size());
    }

    /**
     * Test of peek method, of class InterpolationIndexer.
     */
    @Test
    public void testPeek() {
        final InterpolationIndexer<Double> indexer = new InterpolationIndexer<>(0.0, 10.0, 7.0, VALUES.size());
        int i = indexer.next();
        while (i != -1) {
            Assert.assertEquals(i, indexer.peek());
            i = indexer.next();
        }
    }

    /**
     * Test of next method, of class InterpolationIndexer.
     */
    @Test
    public void testNext() {
        final List<Object> order = new ArrayList<>(VALUES.size());
        final Set<Integer> indexes = new HashSet<>();
        final AtomicInteger iterations = new AtomicInteger(0);
        final InterpolationIndexer<Double> indexer = new InterpolationIndexer<>(0.0, 10.0, 7.0, VALUES.size());
        int i = indexer.next();
        while (i != -1) {
            order.add(i);
            indexes.add(i);
            iterations.incrementAndGet();
            i = indexer.next();
        }
        System.out.println("InterpolationIndexer Reset Order: " + order);
        Assert.assertEquals(VALUES.size(), iterations.get());
        Assert.assertEquals(VALUES.size(), indexes.size());
    }

    /**
     * Test of getMin method, of class InterpolationIndexer.
     */
    @Test
    public void testGetMin() {
        final InterpolationIndexer<Double> indexer = new InterpolationIndexer<>(0.0, 10.0, 7.0, VALUES.size());
        Assert.assertEquals((Double) 0.0, indexer.getMin());
    }

    /**
     * Test of getMax method, of class InterpolationIndexer.
     */
    @Test
    public void testGetMax() {
        final InterpolationIndexer<Double> indexer = new InterpolationIndexer<>(0.0, 10.0, 7.0, VALUES.size());
        Assert.assertEquals((Double) 10.0, indexer.getMax());
    }

    /**
     * Test of getValue method, of class InterpolationIndexer.
     */
    @Test
    public void testGetValue() {
        final InterpolationIndexer<Double> indexer = new InterpolationIndexer<>(0.0, 10.0, 7.0, VALUES.size());
        Assert.assertEquals((Double) 7.0, indexer.getValue());
    }

}
