package gov.pnnl.svf.core.geometry;

/**
 * Alignment in a two dimensional environment.
 *
 * @author Arthur Bleeker
 */
public enum Alignment {

    /**
     * Text origin is center or text is centered in an area.
     */
    CENTER(0), // 0b0000
    /**
     * Text origin is left center or text is centered to the left in an area.
     */
    LEFT(1), // 0b0001
    /**
     * Text origin is right center or text is centered to the right in an area.
     */
    RIGHT(2), // 0b0010
    /**
     * Text origin is center top or text is centered at the top in an area.
     */
    TOP(4), // 0b0100
    /**
     * Text origin is left top or text is placed at the left top in an area.
     */
    LEFT_TOP(5), // 0b0101
    /**
     * Text origin is right top or text is placed at the right top in an area.
     */
    RIGHT_TOP(6), // 0b0110
    /**
     * Text origin is center bottom or text is centered at the bottom in an
     * area.
     */
    BOTTOM(8), // 0b1000
    /**
     * Text origin is left bottom or text is placed at the left bottom in an
     * area.
     */
    LEFT_BOTTOM(9), // 0b1001
    /**
     * Text origin is right bottom or text is placed at the right bottom in an
     * area.
     */
    RIGHT_BOTTOM(10), // 0b1010
    ;
    private final int mask;

    private Alignment(final int mask) {
        this.mask = mask;
    }

    /**
     * Test this alignment to see if it includes the left side.
     *
     * @return true if this alignment includes the left side
     */
    public boolean isLeft() {
        return containsAlignment(Alignment.LEFT);
    }

    /**
     * Test this alignment to see if it includes the right side.
     *
     * @return true if this alignment includes the right side
     */
    public boolean isRight() {
        return containsAlignment(Alignment.RIGHT);
    }

    /**
     * Test this alignment to see if it includes the top side.
     *
     * @return true if this alignment includes the top side
     */
    public boolean isTop() {
        return containsAlignment(Alignment.TOP);
    }

    /**
     * Test this alignment to see if it includes the bottom side.
     *
     * @return true if this alignment includes the bottom side
     */
    public boolean isBottom() {
        return containsAlignment(Alignment.BOTTOM);
    }

    /**
     * Test this alignment to see if it includes the specified alignment or
     * alignments. Only center can include center.
     *
     * @param alignment the alignment to test
     *
     * @return true if this alignment includes the given alignment
     *
     * @throws NullPointerException if the alignment argument is null
     */
    public boolean containsAlignment(final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }
        if (alignment == CENTER) {
            return this == CENTER;
        }
        return (mask & alignment.mask) == alignment.mask;
    }
}
