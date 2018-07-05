package gov.pnnl.svf.core.util;

/**
 * Utility class for working with short boolean state.
 *
 * @author Amelia Bleeker
 */
public class ShortStateUtil {

    /**
     * Mask that represents no true values.
     */
    public static final short NONE = (short) 0x0000;
    /**
     * Mask that represents all true values.
     */
    public static final short ALL = (short) 0xFFFF;
    /**
     * Masks for all of the state values.
     */
    private static final short[] MASKS = new short[]{
        (short) 0x0001, (short) 0x0002, (short) 0x0004, (short) 0x0008,
        (short) 0x0010, (short) 0x0020, (short) 0x0040, (short) 0x0080,
        (short) 0x0100, (short) 0x0200, (short) 0x0400, (short) 0x0800,
        (short) 0x1000, (short) 0x2000, (short) 0x4000, (short) 0x8000};

    /**
     * Constructor private for utility class
     */
    protected ShortStateUtil() {
    }

    /**
     * An array list containing all of the state masks that can be used. There
     * are a total of 8 masks available.
     *
     * @return a copy of the state masks list
     */
    public static short[] getMasks() {
        return ShortStateUtil.getMasks(null);
    }

    /**
     * An array list containing all of the state masks that can be used. There
     * are a total of 8 masks available.
     *
     * @param out an existing array to copy into or null
     *
     * @return a copy of the state masks list
     */
    public static short[] getMasks(short[] out) {
        if (out == null || out.length < MASKS.length) {
            out = new short[MASKS.length];
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
    public static boolean isValue(final short state, final short mask) {
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
    public static short setValue(final short state, final short mask) {
        return (short) (state | mask);
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
    public static short setValue(final short state, final short mask, final boolean value) {
        if (value) {
            return (short) (state | mask);
        } else {
            return (short) (state & ~mask);
        }
    }

    /**
     * Set all of the value states to true.
     *
     * @param state the current state
     *
     * @return the new state
     */
    public static short setValues(final short state) {
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
    public static short setValues(final short state, final boolean value) {
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
    public static short clearValues(final short state) {
        return NONE;
    }

    /**
     * Converts the short state to a full 16 digit string.
     *
     * @param state the state
     *
     * @return the state as a string
     */
    public static String toString(final short state) {
        String binary = Integer.toBinaryString(state);
        binary = binary.substring(binary.lastIndexOf('1'), binary.length());
        binary = ShortStateUtil.leftPad(binary);
        return binary;
    }

    private static String leftPad(final String binary) {
        final StringBuilder sb = new StringBuilder(binary);
        while (sb.length() < 16) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }
}
