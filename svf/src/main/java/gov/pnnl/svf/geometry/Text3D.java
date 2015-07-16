package gov.pnnl.svf.geometry;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.Serializable;
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
    protected final Font font;
    protected final DoubleList offsets;
    protected final double width;
    protected final double height;

    /**
     * Constructor
     */
    public Text3D() {
        super();
        text = "";
        font = DEFAULT_FONT;
        offsets = ZERO_OFFSETS;
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
        font = copy.font;
        offsets = copy.offsets;
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
        font = DEFAULT_FONT;
        offsets = ZERO_OFFSETS;
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
        this(0.0, 0.0, 0.0, DEFAULT_FONT, text);
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
        this(0.0, 0.0, 0.0, font, text);
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
        this(x, y, z, DEFAULT_FONT, text);
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
        super(x, y, z);
        if (font == null) {
            throw new NullPointerException("font");
        }
        if (text == null) {
            throw new NullPointerException("text");
        }
        this.font = font;
        this.text = text;
        if (text.isEmpty()) {
            offsets = ZERO_OFFSETS;
            width = 0.0;
            height = 0.0;
        } else {
            final FontMetrics fm = IMAGE.getGraphics().getFontMetrics(font);
            final GlyphVector gv = font.createGlyphVector(fm.getFontRenderContext(), text);
            final java.awt.geom.Rectangle2D bounds = gv.getLogicalBounds();
            final double offset = bounds.getX() * TEXT_SCALE;
            final double padding = font.getSize() * 0.2 * TEXT_SCALE;
            width = padding + offset + (bounds.getWidth() * TEXT_SCALE) + padding;
            height = fm.getHeight() * TEXT_SCALE;
            offsets = new ArrayDoubleList(text.length());
            final float[] positions = gv.getGlyphPositions(0, text.length(), new float[text.length() * 2]);
            for (int i = 0; i < text.length() * 2; i += 2) {
                offsets.add(padding + offset + (positions[i] * TEXT_SCALE));
            }
        }
    }

    @Override
    public String getText() {
        return text;
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
        return "Text3D{" + "x=" + x + ", y=" + y + ", z=" + z + ", width=" + width + ", height=" + height + ", depth=" + "0.0" + ", font=" + font + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double z = 0.0;
        private String text = "";
        private Font font = null;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
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

        public Text3D build() {
            if (font == null) {
                return new Text3D(x, y, z, text);
            } else {
                return new Text3D(x, y, z, font, text);
            }
        }
    }

}
