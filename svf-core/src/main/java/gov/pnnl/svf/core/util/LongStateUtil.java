package gov.pnnl.svf.core.util;

/**
 * Utility class for working with long boolean state.
 *
 * @author Amelia Bleeker
 */
public class LongStateUtil {

    /**
     * Mask that represents no true values.
     */
    public static final long NONE = 0x0000000000000000L;
    /**
     * Mask that represents all true values.
     */
    public static final long ALL = 0xFFFFFFFFFFFFFFFFL;
    /**
     * Masks for all of the state values.
     */
    private static final long[] MASKS = new long[]{
        0x0000000000000001L, 0x0000000000000002L, 0x0000000000000004L, 0x0000000000000008L,
        0x0000000000000010L, 0x0000000000000020L, 0x0000000000000040L, 0x0000000000000080L,
        0x0000000000000100L, 0x0000000000000200L, 0x0000000000000400L, 0x0000000000000800L,
        0x0000000000001000L, 0x0000000000002000L, 0x0000000000004000L, 0x0000000000008000L,
        0x0000000000010000L, 0x0000000000020000L, 0x0000000000040000L, 0x0000000000080000L,
        0x0000000000100000L, 0x0000000000200000L, 0x0000000000400000L, 0x0000000000800000L,
        0x0000000001000000L, 0x0000000002000000L, 0x0000000004000000L, 0x0000000008000000L,
        0x0000000010000000L, 0x0000000020000000L, 0x0000000040000000L, 0x0000000080000000L,
        0x0000000100000000L, 0x0000000200000000L, 0x0000000400000000L, 0x0000000800000000L,
        0x0000001000000000L, 0x0000002000000000L, 0x0000004000000000L, 0x0000008000000000L,
        0x0000010000000000L, 0x0000020000000000L, 0x0000040000000000L, 0x0000080000000000L,
        0x0000100000000000L, 0x0000200000000000L, 0x0000400000000000L, 0x0000800000000000L,
        0x0001000000000000L, 0x0002000000000000L, 0x0004000000000000L, 0x0008000000000000L,
        0x0010000000000000L, 0x0020000000000000L, 0x0040000000000000L, 0x0080000000000000L,
        0x0100000000000000L, 0x0200000000000000L, 0x0400000000000000L, 0x0800000000000000L,
        0x1000000000000000L, 0x2000000000000000L, 0x4000000000000000L, 0x8000000000000000L};

    /**
     * Constructor private for utility class
     */
    protected LongStateUtil() {
    }

    /**
     * An array list containing all of the state masks that can be used. There
     * are a total of 32 masks available.
     *
     * @return a copy of the state masks list
     */
    public static long[] getMasks() {
        return LongStateUtil.getMasks(null);
    }

    /**
     * An array list containing all of the state masks that can be used. There
     * are a total of 32 masks available.
     *
     * @param out an existing array to copy longo or null
     *
     * @return a copy of the state masks list
     */
    public static long[] getMasks(long[] out) {
        if (out == null || out.length < MASKS.length) {
            out = new long[MASKS.length];
        }
        System.arraycopy(MASKS, 0, out, 0, MASKS.length);
        return out;
    }

    /**
     * Check whether the value is set on the state.
     *
     * @param state the current state
     * @param mask  the value mask
     *
     * @return true if the value is set
     */
    public static boolean isValue(final long state, final long mask) {
        return (state & mask) == mask;
    }

    /**
     * Set the value state.
     *
     * @param state the current state
     * @param mask  the value mask
     *
     * @return the new state
     */
    public static long setValue(final long state, final long mask) {
        return (state | mask);
    }

    /**
     * Set the value state to the indicated value.
     *
     * @param state the current state
     * @param mask  the value mask
     * @param value the new value
     *
     * @return the new state
     */
    public static long setValue(final long state, final long mask, final boolean value) {
        if (value) {
            return (state | mask);
        } else {
            return (state & ~mask);
        }
    }

    /**
     * Set all of the value states to true.
     *
     * @param state the current state
     *
     * @return the new state
     */
    public static long setValues(final long state) {
        return ALL;
    }

    /**
     * Set all of the value states to the supplied value.
     *
     * @param state the current state
     * @param value the value to set
     *
     * @return the new state
     */
    public static long setValues(final long state, final boolean value) {
        if (value) {
            return ALL;
        } else {
            return NONE;
        }
    }

    /**
     * Set all of the value states to false.
     *
     * @param state the current state
     *
     * @return the new state
     */
    public static long clearValues(final long state) {
        return NONE;
    }

    /**
     * Converts the long state to a full 64 digit string.
     *
     * @param state the state
     *
     * @return the state as a string
     */
    public static String toString(final long state) {
        final String temp = String.format("%64s", Long.toBinaryString(state & ALL)).replace(' ', '0');
        return temp;
    }
}
