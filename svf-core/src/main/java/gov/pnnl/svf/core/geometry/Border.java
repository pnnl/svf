package gov.pnnl.svf.core.geometry;

/**
 * Represents a border style in a two dimensional environment.
 *
 * @author Amelia Bleeker
 */
public enum Border {

    /**
     * No borders
     */
    NONE(0), // 0b0000
    /**
     * Left border only
     */
    LEFT(1), // 0b0001
    /**
     * Right border only
     */
    RIGHT(2), // 0b0010
    /**
     * Left and right borders
     */
    LEFT_RIGHT(3), // 0b0011
    /**
     * Top border only
     */
    TOP(4), // 0b0100
    /**
     * Top and left borders
     */
    TOP_LEFT(5), // 0b0101
    /**
     * Top and right borders
     */
    TOP_RIGHT(6), // 0b0110
    /**
     * Top, left, and right borders
     */
    TOP_LEFT_RIGHT(7), // 0b0111
    /**
     * Bottom border only
     */
    BOTTOM(8), // 0b1000
    /**
     * Bottom and left borders
     */
    BOTTOM_LEFT(9), // 0b1001
    /**
     * Bottom and right borders
     */
    BOTTOM_RIGHT(10), // 0b1010
    /**
     * Bottom, left, and right borders
     */
    BOTTOM_LEFT_RIGHT(11), // 0b1011
    /**
     * Bottom and top borders
     */
    BOTTOM_TOP(12), // 0b1100
    /**
     * Bottom, top, and left borders
     */
    BOTTOM_TOP_LEFT(13), // 0b1101
    /**
     * Bottom, top, and right borders
     */
    BOTTOM_TOP_RIGHT(14), // 0b1110
    /**
     * All four borders
     */
    ALL(15); // 0b1111
    private final int mask;

    private Border(final int mask) {
        this.mask = mask;
    }

    /**
     * Get a border type.
     *
     * @param left   true to include the left border
     * @param right  true to include the right border
     * @param top    true to include the top border
     * @param bottom true to include the bottom border
     *
     * @return the border type
     */
    public static Border getBorder(final boolean left, final boolean right, final boolean top, final boolean bottom) {
        return Border.values()[(left ? LEFT.mask : NONE.mask)
                               + (right ? RIGHT.mask : NONE.mask)
                               + (top ? TOP.mask : NONE.mask)
                               + (bottom ? BOTTOM.mask : NONE.mask)];
    }

    /**
     * Test this border to see if it includes the left side.
     *
     * @return true if this border includes the left side
     */
    public boolean isLeft() {
        return containsBorder(Border.LEFT);
    }

    /**
     * Test this border to see if it includes the right side.
     *
     * @return true if this border includes the right side
     */
    public boolean isRight() {
        return containsBorder(Border.RIGHT);
    }

    /**
     * Test this border to see if it includes the top side.
     *
     * @return true if this border includes the top side
     */
    public boolean isTop() {
        return containsBorder(Border.TOP);
    }

    /**
     * Test this border to see if it includes the bottom side.
     *
     * @return true if this border includes the bottom side
     */
    public boolean isBottom() {
        return containsBorder(Border.BOTTOM);
    }

    /**
     * Test this border to see if it includes the specified border or borders.
     *
     * @param border the border to test
     *
     * @return true if this border includes the given border or borders
     *
     * @throws NullPointerException if the border argument is null
     */
    public boolean containsBorder(final Border border) {
        if (border == null) {
            throw new NullPointerException("border");
        }
        if (border == NONE) {
            return this == NONE;
        }
        return (mask & border.mask) == border.mask;
    }
}
