package gov.pnnl.svf.core.geometry;

/**
 * Text alignment in a two dimensional environment.
 *
 * @author Amelia Bleeker
 */
public enum TextAlign {

    /**
     * Multiple lines of text are centered within the area.
     */
    CENTER(0), // 0b0000
    /**
     * Multiple lines of text are aligned on the left side of the area.
     */
    LEFT(1), // 0b0001
    /**
     * Multiple lines of text are aligned on the right side of the area.
     */
    RIGHT(2), // 0b0010
    /**
     * Multiple lines of text are centered and justified by increasing the
     * spacing between words up to three times the distance.
     */
    JUSTIFY(4), // 0b0100
    /**
     * Multiple lines of text are aligned on the left side of the area and
     * justified by increasing the spacing between words up to three times the
     * distance.
     */
    LEFT_JUSTIFY(5), // 0b0101
    /**
     * Multiple lines of text are aligned on the right side of the area and
     * justified by increasing the spacing between words up to three times the
     * distance.
     */
    RIGHT_JUSTIFY(6), // 0b0110
    ;
    private final int mask;

    private TextAlign(final int mask) {
        this.mask = mask;
    }

    /**
     * Test this text alignment to see if it includes the left side.
     *
     * @return true if this alignment includes the left side
     */
    public boolean isLeft() {
        return containsTextAlign(TextAlign.LEFT);
    }

    /**
     * Test this text alignment to see if it includes the right side.
     *
     * @return true if this alignment includes the right side
     */
    public boolean isRight() {
        return containsTextAlign(TextAlign.RIGHT);
    }

    /**
     * Test this text alignment to see if it includes justify.
     *
     * @return true if this alignment includes justify
     */
    public boolean isJustify() {
        return containsTextAlign(TextAlign.JUSTIFY);
    }

    /**
     * Test this text alignment to see if it includes the specified text
     * alignment or text alignments. Only center can include center.
     *
     * @param textAlign the alignment to test
     *
     * @return true if this alignment includes the given alignment
     *
     * @throws NullPointerException if the alignment argument is null
     */
    public boolean containsTextAlign(final TextAlign textAlign) {
        if (textAlign == null) {
            throw new NullPointerException("textAlign");
        }
        if (textAlign == CENTER) {
            return this == CENTER;
        }
        return (mask & textAlign.mask) == textAlign.mask;
    }
}
