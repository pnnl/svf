package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.test.PerformanceStats;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class CountingHashSetTest {

    private static final int PERFORMANCE_TEST_SIZE = 1000000;
    private static final Long[] LIST = new Long[PERFORMANCE_TEST_SIZE];

    static {
        final Random random = new Random(0);
        for (int i = 0; i < PERFORMANCE_TEST_SIZE; i++) {
            LIST[i] = random.nextLong();
        }
    }

    @Test
    public void testPerformanceAdd() {
        // hash set
        final Set<Long> hashSet = new HashSet<>(PERFORMANCE_TEST_SIZE);
        final long hashSetStart = System.currentTimeMillis();
        for (Long LIST1 : LIST) {
            hashSet.add(LIST1);
        }
        final long hashSetEnd = System.currentTimeMillis();
        // counting hash set
        final CountingSet<Long> countingHashSet = new CountingHashSet<>(PERFORMANCE_TEST_SIZE);
        final long countingHashSetStart = System.currentTimeMillis();
        for (Long LIST1 : LIST) {
            countingHashSet.add(LIST1);
        }
        final long countingHashSetEnd = System.currentTimeMillis();
        PerformanceStats.write("HashSet.add(object)", PERFORMANCE_TEST_SIZE, hashSetEnd - hashSetStart);
        PerformanceStats.write("CountingHashSet.add(object)", PERFORMANCE_TEST_SIZE, countingHashSetEnd - countingHashSetStart);
    }

    @Test
    public void testPerformanceRemove() {
        // hash set
        final Set<Long> hashSet = new HashSet<>(Arrays.asList(LIST));
        final long hashSetStart = System.currentTimeMillis();
        for (Long LIST1 : LIST) {
            hashSet.remove(LIST1);
        }
        final long hashSetEnd = System.currentTimeMillis();
        // counting hash set
        final CountingSet<Long> countingHashSet = new CountingHashSet<>(Arrays.asList(LIST));
        final long countingHashSetStart = System.currentTimeMillis();
        for (Long LIST1 : LIST) {
            countingHashSet.remove(LIST1);
        }
        final long countingHashSetEnd = System.currentTimeMillis();
        PerformanceStats.write("HashSet.remove(object)", PERFORMANCE_TEST_SIZE, hashSetEnd - hashSetStart);
        PerformanceStats.write("CountingHashSet.remove(object)", PERFORMANCE_TEST_SIZE, countingHashSetEnd - countingHashSetStart);
    }

    @Test
    public void testPerformanceContains() {
        // hash set
        final Set<Long> hashSet = new HashSet<>(Arrays.asList(LIST));
        final long hashSetStart = System.currentTimeMillis();
        for (Long LIST1 : LIST) {
            hashSet.contains(LIST1);
        }
        final long hashSetEnd = System.currentTimeMillis();
        // counting hash set
        final CountingSet<Long> countingHashSet = new CountingHashSet<>(Arrays.asList(LIST));
        final long countingHashSetStart = System.currentTimeMillis();
        for (Long LIST1 : LIST) {
            countingHashSet.contains(LIST1);
        }
        final long countingHashSetEnd = System.currentTimeMillis();
        PerformanceStats.write("HashSet.contains(object)", PERFORMANCE_TEST_SIZE, hashSetEnd - hashSetStart);
        PerformanceStats.write("CountingHashSet.contains(object)", PERFORMANCE_TEST_SIZE, countingHashSetEnd - countingHashSetStart);
    }

    @Test
    public void testPerformanceGetCount() {
        // counting hash set
        final CountingSet<Long> countingHashSet = new CountingHashSet<>(Arrays.asList(LIST));
        final long countingHashSetStart = System.currentTimeMillis();
        for (Long LIST1 : LIST) {
            countingHashSet.getCount(LIST1);
        }
        final long countingHashSetEnd = System.currentTimeMillis();
        PerformanceStats.write("CountingHashSet.getCount(object)", PERFORMANCE_TEST_SIZE, countingHashSetEnd - countingHashSetStart);
    }

    /**
     * Test of add method, of class CountingHashSet.
     */
    @Test
    public void testAdd() {
        final CountingHashSet<CameraEventType> instance = new CountingHashSet<>();
        Assert.assertEquals(0, instance.getCount(CameraEventType.SINGLE));
        instance.add(CameraEventType.SINGLE);
        Assert.assertEquals(1, instance.getCount(CameraEventType.SINGLE));
    }

    /**
     * Test of remove method, of class CountingHashSet.
     */
    @Test
    public void testRemove() {
        final CountingHashSet<CameraEventType> instance = new CountingHashSet<>(EnumSet.of(CameraEventType.SINGLE));
        Assert.assertEquals(1, instance.getCount(CameraEventType.SINGLE));
        instance.remove(CameraEventType.SINGLE);
        Assert.assertEquals(0, instance.getCount(CameraEventType.SINGLE));
    }

    /**
     * Test of clear method, of class CountingHashSet.
     */
    @Test
    public void testClear() {
        final CountingHashSet<CameraEventType> instance = new CountingHashSet<>(EnumSet.allOf(CameraEventType.class));
        for (final CameraEventType value : CameraEventType.values()) {
            Assert.assertEquals(1, instance.getCount(value));
        }
        instance.clear();
        for (final CameraEventType value : CameraEventType.values()) {
            Assert.assertEquals(0, instance.getCount(value));
        }
    }

    /**
     * Test of getCount method, of class CountingHashSet.
     */
    @Test
    public void testGetCount() {
        final CountingHashSet<CameraEventType> instance = new CountingHashSet<>(EnumSet.allOf(CameraEventType.class));
        instance.add(CameraEventType.DOUBLE);
        instance.add(CameraEventType.DOUBLE);
        instance.add(CameraEventType.DOUBLE);
        instance.add(CameraEventType.SINGLE);
        Assert.assertEquals(1, instance.getCount(CameraEventType.ANY));
        Assert.assertEquals(4, instance.getCount(CameraEventType.DOUBLE));
        Assert.assertEquals(2, instance.getCount(CameraEventType.SINGLE));
        Assert.assertEquals(0, instance.getCount((CameraEventType) null));
        instance.remove(CameraEventType.SINGLE);
        Assert.assertEquals(1, instance.getCount(CameraEventType.SINGLE));
        instance.removeAll(Arrays.asList(new CameraEventType[]{CameraEventType.DOUBLE, CameraEventType.DOUBLE}));
        Assert.assertEquals(2, instance.getCount(CameraEventType.DOUBLE));
    }

    /**
     * Test of clone method, of class CountingHashSet.
     */
    @Test
    public void testClone() {
        final CountingHashSet<?> instance = new CountingHashSet<>(EnumSet.allOf(CameraEventType.class));
        final Object result = instance.clone();
        Assert.assertEquals(instance, result);
    }

    /**
     * Test of emptySet method, of class CountingHashSet.
     */
    @Test
    public void testEmptySet() {
        final CountingSet<Object> expResult = new CountingHashSet<>();
        final CountingSet<Object> result = CountingHashSet.<Object>emptySet();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of singleton method, of class CountingHashSet.
     */
    @Test
    public void testSingleton() {
        final Integer o = 1;
        final CountingSet<Integer> expResult = new CountingHashSet<>(Collections.singletonList(o));
        final CountingSet<Integer> result = CountingHashSet.singleton(o);
        Assert.assertEquals(expResult, result);
    }

    private enum CameraEventType {

        /**
         * Mouse event type for a null pick. This type can occur when all
         * buttons are released.
         */
        NONE,
        /**
         * Mouse event type that represents a down button click.
         */
        DOWN,
        /**
         * Mouse event type that represents a single button click.
         */
        SINGLE,
        /**
         * Mouse event type that represents a double button click.
         */
        DOUBLE,
        /**
         * Mouse event type for a hover mouse event.
         */
        HOVER,
        /**
         * Mouse event type for a mouse wheel scroll up event.
         */
        WHEEL_UP,
        /**
         * Mouse event type for a mouse wheel scroll down event.
         */
        WHEEL_DOWN,
        /**
         * Mouse event type that represents the left button.
         */
        LEFT,
        /**
         * Mouse event type that represents the middle button.
         */
        MIDDLE,
        /**
         * Mouse event type that represents the right button.
         */
        RIGHT,
        /**
         * Mouse event type for a click and drag mouse event. This type can
         * replace the singe or double click type.
         */
        AREA,
        /**
         * Mouse event type that represents the shift modifier key.
         */
        SHIFT,
        /**
         * Mouse event type that represents the alt modifier key.
         */
        ALT,
        /**
         * Mouse event type that represents the control modifier key.
         */
        CTRL,
        /**
         * Mouse event type for a drag mouse event.
         */
        DRAG,
        /**
         * Mouse event type for a move mouse event.
         */
        MOVE,
        /**
         * Mouse event type that represents any mouse event.
         */
        ANY;
    }
}
