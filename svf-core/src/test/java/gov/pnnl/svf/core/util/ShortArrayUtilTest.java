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
public class ShortArrayUtilTest {

    private static final Long SEED = 1L;
    private static final int ITERATIONS = 10;
    private static final int SIZE = 1000000;
    private final Random random = new Random(SEED);

    public ShortArrayUtilTest() {
    }

    /**
     * Test of sort method, of class ShortArrayUtil.
     */
    @Test
    public void testSort() {
        long total = 0L;
        for (int j = 0; j < ITERATIONS; j++) {
            final short[] a = createRandomArray(SIZE);
//        System.out.println(Arrays.toString(a));
            // Arrays.sort()
            final short[] b = Arrays.copyOf(a, a.length);
            long start = System.currentTimeMillis();
            Arrays.sort(b);
            long stop = System.currentTimeMillis();
            System.out.println("Arrays.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(b));
            // Collections.sort()
            final short[] c = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            final List<Short> cc = new ArrayList<>(c.length);
            for (int i = 0; i < c.length; i++) {
                cc.add(c[i]);
            }
            Collections.sort(cc, new Comparator<Short>() {

                         @Override
                         public int compare(Short o1, Short o2) {
                             return o1.compareTo(o2);
                         }
                     });
            for (int i = 0; i < cc.size(); i++) {
                c[i] = cc.get(i);
            }
            stop = System.currentTimeMillis();
            System.out.println("Collections.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(c));
            // ShortArrayUtil.sort()
            final short[] d = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            ShortArrayUtil.sort(d, new ShortArrayUtil.ShortComparator() {

                            @Override
                            public int compare(short o1, short o2) {
                                return Short.compare(o1, o2);
                            }
                        });
            stop = System.currentTimeMillis();
            System.out.println("ShortArrayUtil.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
            total += stop - start;
//        System.out.println(Arrays.toString(d));
            // Arrays.parallelSort()
            // uncomment to build and test when using JDK_1.8
//            final short[] e = Arrays.copyOf(a, a.length);
//            start = System.currentTimeMillis();
//            Arrays.parallelSort(e);
//            stop = System.currentTimeMillis();
//            System.out.println("Arrays.parallelSort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(e));
            // ensure they are all the same
            Assert.assertArrayEquals("Failed on iteration " + j, b, c);
            Assert.assertArrayEquals("Failed on iteration " + j, c, d);
        }
        PerformanceStats.write("ShortArrayUtil.sort(short[" + SIZE + "])", 1, total / ITERATIONS);
    }

    private short[] createRandomArray(final int size) {
        final short[] array = new short[size];
        for (int i = 0; i < size; i++) {
            array[i] = (short) random.nextInt();
        }
        return array;
    }

}
