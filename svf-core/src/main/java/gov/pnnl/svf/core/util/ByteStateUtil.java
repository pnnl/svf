package gov.pnnl.svf.core.util;

/**
 * Utility class for working with byte boolean state.
 *
 * @author Arthur Bleeker
 */
public class ByteStateUtil {

    /**
     * Mask that represents no true values.
     */
    public static final byte NONE = (byte) 0x00;
    /**
     * Mask that represents all true values.
     */
    public static final byte ALL = (byte) 0xFF;
    /**
     * Masks for all of the state values.
     */
    private static final byte[] MASKS = new byte[]{
        (byte) 0x01,
        (byte) 0x02,
        (byte) 0x04,
        (byte) 0x08,
        (byte) 0x10,
        (byte) 0x20,
        (byte) 0x40,
        (byte) 0x80};

    /**
     * Constructor private for utility class
     */
    protected ByteStateUtil() {
    }

    /**
     * An array list containing all of the state masks that can be used. There
     * are a total of 8 masks available.
     *
     * @return a copy of the state masks list
     */
    public static byte[] getMasks() {
        return ByteStateUtil.getMasks(null);
    }

    /**
     * An array list containing all of the state masks that can be used. There
     * are a total of 8 masks available.
     *
     * @param out an existing array to copy into or null
     *
     * @return a copy of the state masks list
     */
    public static byte[] getMasks(byte[] out) {
        if (out == null || out.length < MASKS.length) {
            out = new byte[MASKS.length];
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
    public static boolean isValue(final byte state, final byte mask) {
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
    public static byte setValue(final byte state, final byte mask) {
        return (byte) (state | mask);
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
    public static byte setValue(final byte state, final byte mask, final boolean value) {
        if (value) {
            return (byte) (state | mask);
        } else {
            return (byte) (state & ~mask);
        }
    }

    /**
     * Set all of the value states to true.
     *
     * @param state the current state
     *
     * @return the new state
     */
    public static byte setValues(final byte state) {
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
    public static byte setValues(final byte state, final boolean value) {
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
    public static byte clearValues(final byte state) {
        return NONE;
    }

    /**
     * Converts the byte state to a full 8 digit string.
     *
     * @param state the state
     *
     * @return the state as a string
     */
    public static String toString(final byte state) {
        String binary = Integer.toBinaryString(state);
        binary = binary.substring(binary.lastIndexOf('1'), binary.length());
        binary = ByteStateUtil.leftPad(binary);
        return binary;
    }

    private static String leftPad(final String binary) {
        final StringBuilder sb = new StringBuilder(binary);
        while (sb.length() < 8) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }
}
