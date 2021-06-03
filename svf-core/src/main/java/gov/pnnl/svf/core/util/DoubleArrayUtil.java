package gov.pnnl.svf.core.util;

import java.util.Random;

/**
 * Utility for working with double arrays and primitive collections.
 * Implementation based on qsort4 from Programming Pearls.
 *
 * @author Amelia Bleeker
 */
public class DoubleArrayUtil {

    private static final int MIN_QSORT_SIZE = 256;

    /**
     * Constructor kept private for static utility pattern
     */
    private DoubleArrayUtil() {
    }

    /**
     * Sort an array using a comparator.
     *
     * @param array      the array to sort
     * @param comparator the comparator used for sorting
     *
     * @throws NullPointerException if any arguments are null
     */
    public static void sort(final double[] array, final DoubleComparator comparator) {
        quickSort(array, 0, array.length - 1, comparator, new Random());
    }

    /**
     * Sort an array using a comparator.
     *
     * @param array      the array to sort
     * @param lower      the lower index to sort
     * @param upper      the upper index to sort
     * @param comparator the comparator used for sorting
     *
     * @throws NullPointerException if any arguments are null
     */
    public static void sort(final double[] array, final int lower, final int upper, final DoubleComparator comparator) {
        quickSort(array, lower, upper, comparator, new Random());
    }

    private static void insertSort(final double[] array, final int lower, final int upper, final DoubleComparator comparator) {
        for (int i = lower + 1; i < upper + 1; i++) {
            double t = array[i];
            int j = i;
            for (; j > 0 && comparator.compare(array[j - 1], t) > 0; j--) {
                array[j] = array[j - 1];
            }
            array[j] = t;
        }
    }

    private static void quickSort(final double[] array, final int lower, final int upper, final DoubleComparator comparator, final Random random) {
        if (upper - lower < MIN_QSORT_SIZE) {
            insertSort(array, lower, upper, comparator);
            return;
        }
        swap(array, lower, lower + random.nextInt(upper - lower + 1));
        final double t = array[lower];
        int i = lower;
        int j = upper + 1;
        while (true) {
            do {
                i++;
            } while (i <= upper && comparator.compare(array[i], t) < 0);
            do {
                j--;
            } while (comparator.compare(array[j], t) > 0);
            if (i > j) {
                break;
            }
            swap(array, i, j);
        }
        swap(array, lower, j);
        quickSort(array, lower, j - 1, comparator, random);
        quickSort(array, j + 1, upper, comparator, random);
    }

    private static void swap(final double[] array, final int i, final int j) {
        final double t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    /**
     * Interface for comparing two doubles.
     */
    public static interface DoubleComparator {

        /**
         * Compares its two arguments for order. Returns a negative integer,
         * zero, or a positive integer as the first argument is less than, equal
         * to, or greater than the second.<p>
         *
         * In the foregoing description, the notation
         * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
         * <i>signum</i> function, which is defined to return one of
         * {@code -1},
         * {@code 0}, or {@code 1} according to whether the value of
         * <i>expression</i> is negative, zero or positive.<p>
         *
         * The implementor must ensure that {@code sgn(compare(x, y)) ==
         * -sgn(compare(y, x))} for all {@code x} and {@code y}. (This
         * implies that {@code compare(x, y)} must throw an exception if and
         * only if {@code compare(y, x)} throws an exception.)
         * <p>
         *
         * The implementor must also ensure that the relation is transitive:
         * {@code ((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))}
         * implies
         * {@code compare(x, z)&gt;0}.<p>
         *
         * Finally, the implementor must ensure that {@code compare(x, y)==0}
         * implies that {@code sgn(compare(x, z))==sgn(compare(y, z))} for all
         * {@code z}.<p>
         *
         * It is generally the case, but <i>not</i> strictly required that
         * {@code (compare(x, y)==0) == (x.equals(y))}. Generally speaking, any
         * comparator that violates this condition should clearly indicate this
         * fact. The recommended language is "Note: this comparator imposes
         * orderings that are inconsistent with equals."
         *
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         *
         * @return a negative integer, zero, or a positive integer as the first
         *         argument is less than, equal to, or greater than the second.
         *
         * @throws NullPointerException if an argument is null and this
         *                              comparator does not permit null
         *                              arguments
         * @throws ClassCastException   if the arguments' types prevent them
         *                              from being compared by this comparator.
         */
        int compare(double o1, double o2);
    }
}
