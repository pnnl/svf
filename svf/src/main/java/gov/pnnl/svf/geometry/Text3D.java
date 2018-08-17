package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.geometry.TextAlign;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleCollections;
import org.apache.commons.collections.primitives.DoubleList;

public class Text3D extends Shape3D implements Text, Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Scalar used to shrink text so it renders correctly in 3d space.
     */
    public static final double TEXT_SCALE = 0.0072;
    /**
     * Default font Arial 14.
     */
    public final static Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 36);
    protected final static DoubleList ZERO_OFFSETS = DoubleCollections.singletonDoubleList(0.0);
    protected final static BufferedImage IMAGE = new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);
    /**
     * Constant zero-dimensioned text
     */
    public final static Text3D ZERO = new Text3D();
    protected final String text;
    protected final List<String> textLines;
    protected final Font font;
    protected final TextAlign align;
    protected final DoubleList offsets;
    protected final DoubleList lineOffsets;
    protected final double lineHeight;
    protected final double width;
    protected final double height;

    /**
     * Constructor
     */
    public Text3D() {
        super();
        text = "";
        textLines = Collections.singletonList("");
        font = DEFAULT_FONT;
        align = TextAlign.CENTER;
        offsets = ZERO_OFFSETS;
        lineOffsets = ZERO_OFFSETS;
        lineHeight = 0.0;
        width = 0.0;
        height = 0.0;
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param z    the new z offset
     * @param copy the object to copy
     */
    public Text3D(final double x, final double y, final double z, final Text3D copy) {
        super(x, y, z);
        text = copy.text;
        textLines = copy.textLines;
        font = copy.font;
        align = copy.align;
        offsets = copy.offsets;
        lineOffsets = copy.lineOffsets;
        lineHeight = copy.lineHeight;
        width = copy.width;
        height = copy.height;
    }

    /**
     * Constructor
     *
     * @param x the x offset for this shape
     * @param y the y offset for this shape
     * @param z the z offset for this shape
     */
    public Text3D(final double x, final double y, final double z) {
        super(x, y, z);
        text = "";
        textLines = Collections.singletonList("");
        font = DEFAULT_FONT;
        align = TextAlign.CENTER;
        offsets = ZERO_OFFSETS;
        lineOffsets = ZERO_OFFSETS;
        lineHeight = 0.0;
        width = 0.0;
        height = 0.0;
    }

    /**
     * Constructor
     *
     * @param text the text
     *
     * @throws NullPointerException if any arguments are null
     */
    public Text3D(final String text) {
        this(0.0, 0.0, 0.0, DEFAULT_FONT, TextAlign.CENTER, text);
    }

    /**
     * Constructor
     *
     * @param font font for the text
     * @param text the text
     *
     * @throws NullPointerException if any arguments are null
     */
    public Text3D(final Font font, final String text) {
        this(0.0, 0.0, 0.0, font, TextAlign.CENTER, text);
    }

    /**
     * Constructor
     *
     * @param x    the x offset for this shape
     * @param y    the y offset for this shape
     * @param z    the z offset for this shape
     * @param text the text
     *
     * @throws NullPointerException if text is null
     */
    public Text3D(final double x, final double y, final double z, final String text) {
        this(x, y, z, DEFAULT_FONT, TextAlign.CENTER, text);
    }

    /**
     * Constructor
     *
     * @param x    the x offset for this shape
     * @param y    the y offset for this shape
     * @param z    the z offset for this shape
     * @param font the font for the text
     * @param text the text
     *
     * @throws NullPointerException if any arguments are null
     */
    public Text3D(final double x, final double y, final double z, final Font font, final String text) {
        this(x, y, z, font, TextAlign.CENTER, text);
    }

    /**
     * Constructor
     *
     * @param x     the x offset for this shape
     * @param y     the y offset for this shape
     * @param z     the z offset for this shape
     * @param font  the font for the text
     * @param align the text align
     * @param text  the text
     *
     * @throws NullPointerException if any arguments are null
     */
    public Text3D(final double x, final double y, final double z, final Font font, final TextAlign align, final String text) {
        super(x, y, z);
        if (font == null) {
            throw new NullPointerException("font");
        }
        if (text == null) {
            throw new NullPointerException("text");
        }
        this.align = Objects.requireNonNull(align, "align");
        this.font = font;
        this.text = text.replaceAll("\\r?\\n", "");
        if (text.isEmpty()) {
            textLines = Collections.singletonList("");
            offsets = ZERO_OFFSETS;
            lineOffsets = ZERO_OFFSETS;
            lineHeight = 0.0;
            width = 0.0;
            height = 0.0;
        } else {
            final FontMetrics fm = IMAGE.getGraphics().getFontMetrics(font);
            textLines = Collections.unmodifiableList(Arrays.asList(text.split("\\r?\\n")));
            offsets = new ArrayDoubleList(text.length());
            final DoubleList widths = new ArrayDoubleList(textLines.size());
            final DoubleList heights = new ArrayDoubleList(textLines.size());
            for (final String textLine : textLines) {
                final GlyphVector gv = font.createGlyphVector(fm.getFontRenderContext(), textLine);
                final java.awt.geom.Rectangle2D bounds = gv.getLogicalBounds();
                final double offset = bounds.getX() * TEXT_SCALE;
                final double padding = font.getSize() * 0.2 * TEXT_SCALE;
                widths.add(padding + offset + (bounds.getWidth() * TEXT_SCALE) + padding);
                heights.add(fm.getHeight() * TEXT_SCALE);
                final float[] positions = gv.getGlyphPositions(0, textLine.length(), new float[textLine.length() * 2]);
                for (int i = 0; i < textLine.length() * 2; i += 2) {
                    offsets.add(padding + offset + (positions[i] * TEXT_SCALE));
                }
            }
            // find the max width and the total height
            double maxWidth = 0.0;
            for (int i = 0; i < widths.size(); i++) {
                maxWidth = Math.max(maxWidth, widths.get(i));
            }
            width = maxWidth;
            double maxHeight = 0.0;
            for (int i = 0; i < heights.size(); i++) {
                maxHeight = Math.max(maxHeight, heights.get(i));
            }
            height = maxHeight * textLines.size();
            // calculate the line offsets
            lineHeight = maxHeight;
            lineOffsets = new ArrayDoubleList(textLines.size());
            for (int i = 0; i < widths.size(); i++) {
                if (align.containsTextAlign(TextAlign.CENTER)) {
                    lineOffsets.add((maxWidth - widths.get(i)) / 2.0);
                } else if (align.containsTextAlign(TextAlign.LEFT)) {
                    lineOffsets.add(0.0);
                } else if (align.containsTextAlign(TextAlign.RIGHT)) {
                    lineOffsets.add(maxWidth - widths.get(i));
                }
            }
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<String> getTextLines() {
        return textLines;
    }

    @Override
    public DoubleList getLineOffsets() {
        return DoubleCollections.unmodifiableDoubleList(lineOffsets);
    }

    @Override
    public double getLineHeight() {
        return lineHeight;
    }

    @Override
    public TextAlign getAlign() {
        return align;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public DoubleList getOffsets() {
        return DoubleCollections.unmodifiableDoubleList(offsets);
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getDepth() {
        return 0.0;
    }

    @Override
    public boolean contains(final double x, final double y, final double z) {
        return x >= this.x - (width * 0.5) && x <= this.x + (width * 0.5)
               && y >= this.y - (height * 0.5) && y <= this.y + (height * 0.5)
               && z == this.z;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 41 * hash + (text != null ? text.hashCode() : 0);
        hash = 41 * hash + (font != null ? font.hashCode() : 0);
        hash = 41 * hash + (align != null ? align.hashCode() : 0);
        hash = 41 * hash + (int) (Double.doubleToLongBits(width) ^ (Double.doubleToLongBits(width) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(height) ^ (Double.doubleToLongBits(height) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Text3D other = (Text3D) obj;
        if (text != other.text && (text == null || !text.equals(other.text))) {
            return false;
        }
        if (font != other.font && (font == null || !font.equals(other.font))) {
            return false;
        }
        if (align != other.align && (align == null || !align.equals(other.align))) {
            return false;
        }
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Text3D{" + "x=" + x + ", y=" + y + ", z=" + z + ", width=" + width + ", height=" + height + ", depth=" + "0.0" + ", font=" + font + ", align=" + align + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double z = 0.0;
        private String text = "";
        private Font font = DEFAULT_FONT;
        private TextAlign align = TextAlign.CENTER;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public double x() {
            return this.x;
        }

        public double y() {
            return this.y;
        }

        public double z() {
            return this.z;
        }

        public String text() {
            return this.text;
        }

        public Font font() {
            return this.font;
        }

        public TextAlign align() {
            return this.align;
        }

        public Builder x(final double x) {
            this.x = x;
            return this;
        }

        public Builder y(final double y) {
            this.y = y;
            return this;
        }

        public Builder z(final double z) {
            this.z = z;
            return this;
        }

        public Builder text(final String text) {
            this.text = text;
            return this;
        }

        public Builder font(final Font font) {
            this.font = font;
            return this;
        }

        public Builder align(final TextAlign align) {
            this.align = align;
            return this;
        }

        public Text3D build() {
            return new Text3D(x, y, z, font, align, text);
        }
    }

}
