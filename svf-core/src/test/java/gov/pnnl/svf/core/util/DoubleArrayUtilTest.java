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
 * @author Arthur Bleeker
 */
public class DoubleArrayUtilTest {

    private static final Long SEED = 1L;
    private static final int ITERATIONS = 10;
    private static final int SIZE = 1000000;
    private final Random random = new Random(SEED);

    public DoubleArrayUtilTest() {
    }

    /**
     * Test of sort method, of class DoubleArrayUtil.
     */
    @Test
    public void testSort() {
        long total = 0L;
        for (int j = 0; j < ITERATIONS; j++) {
            final double[] a = createRandomArray(SIZE);
//        System.out.println(Arrays.toString(a));
            // Arrays.sort()
            final double[] b = Arrays.copyOf(a, a.length);
            long start = System.currentTimeMillis();
            Arrays.sort(b);
            long stop = System.currentTimeMillis();
            System.out.println("Arrays.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(b));
            // Collections.sort()
            final double[] c = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            final List<Double> cc = new ArrayList<>(c.length);
            for (int i = 0; i < c.length; i++) {
                cc.add(c[i]);
            }
            Collections.sort(cc, new Comparator<Double>() {

                         @Override
                         public int compare(Double o1, Double o2) {
                             return o1.compareTo(o2);
                         }
                     });
            for (int i = 0; i < cc.size(); i++) {
                c[i] = cc.get(i);
            }
            stop = System.currentTimeMillis();
            System.out.println("Collections.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(c));
            // DoubleArrayUtil.sort()
            final double[] d = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            DoubleArrayUtil.sort(d, new DoubleArrayUtil.DoubleComparator() {

                             @Override
                             public int compare(double o1, double o2) {
                                 return Double.compare(o1, o2);
                             }
                         });
            stop = System.currentTimeMillis();
            System.out.println("DoubleArrayUtil.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
            total += stop - start;
//        System.out.println(Arrays.toString(d));
            // ensure they are all the same
            Assert.assertArrayEquals("Failed on iteration " + j, b, c, 0.0);
            Assert.assertArrayEquals("Failed on iteration " + j, c, d, 0.0);
        }
        PerformanceStats.write("DoubleArrayUtil.sort(double[" + SIZE + "])", 1, total / ITERATIONS);
    }

    private double[] createRandomArray(final int size) {
        final double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextDouble();
        }
        return array;
    }

}
