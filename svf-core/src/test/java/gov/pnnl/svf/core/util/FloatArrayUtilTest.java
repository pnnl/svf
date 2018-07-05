package gov.pnnl.svf.core.util;

import gov.pnnl.svf.test.PerformanceStats;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class FloatArrayUtilTest {

    private static final Long SEED = 1L;
    private static final int ITERATIONS = 10;
    private static final int SIZE = 1000000;
    private final Random random = new Random(SEED);

    public FloatArrayUtilTest() {
    }

    /**
     * Test of sort method, of class FloatArrayUtil.
     */
    @Test
    public void testSort() {
        long total = 0L;
        for (int j = 0; j < ITERATIONS; j++) {
            final float[] a = createRandomArray(SIZE);
//        System.out.println(Arrays.toString(a));
            // Arrays.sort()
            final float[] b = Arrays.copyOf(a, a.length);
            long start = System.currentTimeMillis();
            Arrays.sort(b);
            long stop = System.currentTimeMillis();
            System.out.println("Arrays.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(b));
            // Collections.sort()
            final float[] c = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            final List<Float> cc = new ArrayList<>(c.length);
            for (int i = 0; i < c.length; i++) {
                cc.add(c[i]);
            }
            Collections.sort(cc, Float::compareTo);
            for (int i = 0; i < cc.size(); i++) {
                c[i] = cc.get(i);
            }
            stop = System.currentTimeMillis();
            System.out.println("Collections.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(c));
            // FloatArrayUtil.sort()
            final float[] d = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            FloatArrayUtil.sort(d, Float::compare);
            stop = System.currentTimeMillis();
            System.out.println("FloatArrayUtil.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
            total += stop - start;
//        System.out.println(Arrays.toString(d));
            // Arrays.parallelSort()
            // uncomment to build and test when using JDK_1.8
//            final float[] e = Arrays.copyOf(a, a.length);
//            start = System.currentTimeMillis();
//            Arrays.parallelSort(e);
//            stop = System.currentTimeMillis();
//            System.out.println("Arrays.parallelSort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(e));
            // ensure they are all the same
            Assert.assertArrayEquals("Failed on iteration " + j, b, c, 0.0F);
            Assert.assertArrayEquals("Failed on iteration " + j, c, d, 0.0F);
        }
        PerformanceStats.write("FloatArrayUtil.sort(float[" + SIZE + "])", 1, total / ITERATIONS);
    }

    private float[] createRandomArray(final int size) {
        final float[] array = new float[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextFloat();
        }
        return array;
    }

}
