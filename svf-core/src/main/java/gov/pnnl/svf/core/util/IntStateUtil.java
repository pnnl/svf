package gov.pnnl.svf.core.util;

/**
 * Utility class for working with int boolean state.
 *
 * @author Amelia Bleeker
 */
public class IntStateUtil {

    /**
     * Mask that represents no true values.
     */
    public static final int NONE = 0x00000000;
    /**
     * Mask that represents all true values.
     */
    public static final int ALL = 0xFFFFFFFF;
    /**
     * Masks for all of the state values.
     */
    private static final int[] MASKS = new int[]{
        0x00000001, 0x00000002, 0x00000004, 0x00000008,
        0x00000010, 0x00000020, 0x00000040, 0x00000080,
        0x00000100, 0x00000200, 0x00000400, 0x00000800,
        0x00001000, 0x00002000, 0x00004000, 0x00008000,
        0x00010000, 0x00020000, 0x00040000, 0x00080000,
        0x00100000, 0x00200000, 0x00400000, 0x00800000,
        0x01000000, 0x02000000, 0x04000000, 0x08000000,
        0x10000000, 0x20000000, 0x40000000, 0x80000000};

    /**
     * Constructor private for utility class
     */
    protected IntStateUtil() {
    }

    /**
     * An array list containing all of the state masks that can be used. There
     * are a total of 32 masks available.
     *
     * @return a copy of the state masks list
     */
    public static int[] getMasks() {
        return IntStateUtil.getMasks(null);
    }

    /**
     * An array list containing all of the state masks that can be used. There
     * are a total of 32 masks available.
     *
     * @param out an existing array to copy into or null
     *
     * @return a copy of the state masks list
     */
    public static int[] getMasks(int[] out) {
        if (out == null || out.length < MASKS.length) {
            out = new int[MASKS.length];
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
    public static boolean isValue(final int state, final int mask) {
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
    public static int setValue(final int state, final int mask) {
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
    public static int setValue(final int state, final int mask, final boolean value) {
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
    public static int setValues(final int state) {
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
    public static int setValues(final int state, final boolean value) {
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
    public static int clearValues(final int state) {
        return NONE;
    }

    /**
     * Converts the int state to a full 32 digit string.
     *
     * @param state the state
     *
     * @return the state as a string
     */
    public static String toString(final int state) {
        String binary = Integer.toBinaryString(state);
        binary = binary.substring(binary.lastIndexOf('1'), binary.length());
        binary = IntStateUtil.leftPad(binary);
        return binary;
    }

    private static String leftPad(final String binary) {
        final StringBuilder sb = new StringBuilder(binary);
        while (sb.length() < 32) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }
}
