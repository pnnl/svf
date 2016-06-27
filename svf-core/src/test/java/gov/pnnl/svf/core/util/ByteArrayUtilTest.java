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
public class ByteArrayUtilTest {

    private static final Long SEED = 1L;
    private static final int ITERATIONS = 10;
    private static final int SIZE = 1000000;
    private final Random random = new Random(SEED);

    public ByteArrayUtilTest() {
    }

    /**
     * Test of sort method, of class ByteArrayUtil.
     */
    @Test
    public void testSort() {
        long total = 0L;
        for (int j = 0; j < ITERATIONS; j++) {
            final byte[] a = createRandomArray(SIZE);
//        System.out.println(Arrays.toString(a));
            // Arrays.sort()
            final byte[] b = Arrays.copyOf(a, a.length);
            long start = System.currentTimeMillis();
            Arrays.sort(b);
            long stop = System.currentTimeMillis();
            System.out.println("Arrays.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(b));
            // Collections.sort()
            final byte[] c = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            final List<Byte> cc = new ArrayList<>(c.length);
            for (int i = 0; i < c.length; i++) {
                cc.add(c[i]);
            }
            Collections.sort(cc, new Comparator<Byte>() {

                @Override
                public int compare(Byte o1, Byte o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int i = 0; i < cc.size(); i++) {
                c[i] = cc.get(i);
            }
            stop = System.currentTimeMillis();
            System.out.println("Collections.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(c));
            // ByteArrayUtil.sort()
            final byte[] d = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            ByteArrayUtil.sort(d, new ByteArrayUtil.ByteComparator() {

                @Override
                public int compare(byte o1, byte o2) {
                    return Byte.compare(o1, o2);
                }
            });
            stop = System.currentTimeMillis();
            System.out.println("ByteArrayUtil.sort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
            total += stop - start;
//        System.out.println(Arrays.toString(d));
            // Arrays.parallelSort()
            final byte[] e = Arrays.copyOf(a, a.length);
            start = System.currentTimeMillis();
            Arrays.parallelSort(e);
            stop = System.currentTimeMillis();
            System.out.println("Arrays.parallelSort() took " + (stop - start) + " ms to sort " + SIZE + " entries.");
//        System.out.println(Arrays.toString(e));
            // ensure they are all the same
            Assert.assertArrayEquals("Failed on iteration " + j, b, c);
            Assert.assertArrayEquals("Failed on iteration " + j, c, d);
        }
        PerformanceStats.write("ByteArrayUtil.sort(byte[" + SIZE + "])", 1, total / ITERATIONS);
    }

    private byte[] createRandomArray(final int size) {
        final byte[] array = new byte[size];
        for (int i = 0; i < size; i++) {
            array[i] = (byte) random.nextInt();
        }
        return array;
    }

}
