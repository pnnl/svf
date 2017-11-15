package gov.pnnl.svf.core.color;

import gov.pnnl.svf.core.constant.ColorConst;
import java.io.Serializable;

public class Color implements ColorConst, Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * White
     */
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f);
    /**
     * Black
     */
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);
    /**
     * Dark Gray (20%)
     */
    public static final Color DARK_GRAY = new Color(0.2f, 0.2f, 0.2f);
    /**
     * Gray (40%)
     */
    public static final Color GRAY = new Color(0.4f, 0.4f, 0.4f);
    /**
     * Light Gray (60%)
     */
    public static final Color LIGHT_GRAY = new Color(0.6f, 0.6f, 0.6f);
    /**
     * Red
     */
    public static final Color RED = new Color(255, 0, 0);
    /**
     * Blue
     */
    public static final Color BLUE = new Color(0, 0, 255);
    /**
     * Green
     */
    public static final Color GREEN = new Color(0, 255, 0);
    /**
     * Yellow
     */
    public static final Color YELLOW = new Color(255, 255, 0);
    /**
     * Orange
     */
    public static final Color ORANGE = new Color(255, 127, 0);
    /**
     * Indigo
     */
    public static final Color INDIGO = new Color(75, 0, 130);
    /**
     * Violet
     */
    public static final Color VIOLET = new Color(127, 0, 255);
    /**
     * Brown
     */
    public static final Color BROWN = new Color(150, 75, 0);
    /**
     * Transparent
     */
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private final float r;
    private final float g;
    private final float b;
    private final float a;

    /**
     * Constructor
     */
    public Color() {
        super();
        r = 1.0f;
        g = 1.0f;
        b = 1.0f;
        a = 1.0f;
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public Color(final byte r, final byte g, final byte b) {
        this((r & 0xff), (g & 0xff), (b & 0xff), 255);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     * @param a alpha component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public Color(final byte r, final byte g, final byte b, final byte a) {
        this((r & 0xff), (g & 0xff), (b & 0xff), (a & 0xff));
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0.0 &le; x &le; 1.0}
     * @param g green component {x: 0.0 &le; x &le; 1.0}
     * @param b blue component {x: 0.0 &le; x &le; 1.0}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0.0
     *                                  to 1.0 inclusive
     */
    public Color(final float r, final float g, final float b) {
        this(r, g, b, 1.0f);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0.0 &le; x &le; 1.0}
     * @param g green component {x: 0.0 &le; x &le; 1.0}
     * @param b blue component {x: 0.0 &le; x &le; 1.0}
     * @param a alpha component {x: 0.0 &le; x &le; 1.0}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0.0
     *                                  to 1.0 inclusive
     */
    public Color(final float r, final float g, final float b, final float a) {
        this((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    /**
     * Constructor
     *
     * @param hex a hex string in the form of RGB, RGBA, RRGGBB, or RRGGBBAA
     *            with or without a preceding #.
     *
     * @throws IllegalArgumentException  if r, g, b, or a is not in the range
     *                                   0.0 to 1.0 inclusive
     * @throws NumberFormatException     if the string is not a valid hex string
     * @throws IndexOutOfBoundsException if the hex string is not at least 6
     *                                   characters
     */
    public Color(final String hex) {
        this(Color.parseHex(hex, 0), Color.parseHex(hex, 1), Color.parseHex(hex, 2), Color.parseHex(hex, 3));
    }

    /**
     * Constructor
     *
     * @param color the base color to use
     * @param alpha the alpha value to use
     *
     * @throws NullPointerException     if the color is null
     * @throws IllegalArgumentException if alpha is not in the range 0.0 to 1.0
     *                                  inclusive
     */
    public Color(final Color color, final float alpha) {
        this(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    /**
     * Constructor
     *
     * @param color the base color to use
     * @param alpha the alpha value to use
     *
     * @throws NullPointerException     if the color is null
     * @throws IllegalArgumentException if alpha is not in the range 0 to 255
     *                                  inclusive
     */
    public Color(final Color color, final int alpha) {
        this(color.getRed(), color.getGreen(), color.getBlue(), alpha / 255.0f);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public Color(final int r, final int g, final int b) {
        this(r, g, b, 255);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     * @param a alpha component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public Color(final int r, final int g, final int b, final int a) {
        super();
        if (r < 0 || r > 255) {
            throw new IllegalArgumentException("r");
        }
        if (g < 0 || g > 255) {
            throw new IllegalArgumentException("g");
        }
        if (b < 0 || b > 255) {
            throw new IllegalArgumentException("b");
        }
        if (a < 0 || a > 255) {
            throw new IllegalArgumentException("a");
        }
        this.r = r / 255.0f;
        this.g = g / 255.0f;
        this.b = b / 255.0f;
        this.a = a / 255.0f;
    }

    /**
     * @return the alpha component
     */
    public float getAlpha() {
        return a;
    }

    /**
     * @return the blue component
     */
    public float getBlue() {
        return b;
    }

    /**
     * @return the green component
     */
    public float getGreen() {
        return g;
    }

    /**
     * @return the red component
     */
    public float getRed() {
        return r;
    }

    /**
     * @return the color as an ARGB array
     */
    public float[] toArgbArray() {
        return new float[]{a, r, g, b};
    }

    /**
     * @param array fills the array as an ARGB color
     */
    public void toArgbArray(final float[] array) {
        array[ARGB_ALPHA] = a;
        array[ARGB_RED] = r;
        array[ARGB_GREEN] = g;
        array[ARGB_BLUE] = b;
    }

    /**
     * @return the color as an RGBA array
     */
    public float[] toRgbaArray() {
        return new float[]{r, g, b, a};
    }

    /**
     * @param array fills the array as an RGBA color
     */
    public void toRgbaArray(final float[] array) {
        array[RGBA_RED] = r;
        array[RGBA_GREEN] = g;
        array[RGBA_BLUE] = b;
        array[RGBA_ALPHA] = a;
    }

    /**
     *
     * @return the color as an RGB array
     */
    public float[] toRgbArray() {
        return new float[]{r, g, b};
    }

    /**
     * @param array fills the array as an RGB color
     */
    public void toRgbArray(final float[] array) {
        array[RGB_RED] = r;
        array[RGB_GREEN] = g;
        array[RGB_BLUE] = b;
    }

    /**
     * @return the color as an AWT color
     */
    public java.awt.Color toAwtColor() {
        return new java.awt.Color(getRed(), getGreen(), getBlue(), getAlpha());
    }

    /**
     * Convert this color to an integer in the format (a|r|g|b).
     *
     * @return this color as an int
     */
    public int toInt() {
        return (int) (a * 255.0f) << 24
               | (int) (r * 255.0f) << 16
               | (int) (g * 255.0f) << 8
               | (int) (b * 255.0f);
    }

    /**
     * Convert an integer in the format (a|r|g|b) into a color.
     *
     * @param color the color as an int
     *
     * @return the color
     */
    public static Color fromInt(final int color) {
        return new Color((byte) ((color >> 16) & 0xff),
                         (byte) ((color >> 8) & 0xff),
                         (byte) (color & 0xff),
                         (byte) ((color >> 24) & 0xff));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(a);
        result = prime * result + Float.floatToIntBits(b);
        result = prime * result + Float.floatToIntBits(g);
        result = prime * result + Float.floatToIntBits(r);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Color other = (Color) obj;
        if (Float.floatToIntBits(a) != Float.floatToIntBits(other.a)) {
            return false;
        }
        if (Float.floatToIntBits(b) != Float.floatToIntBits(other.b)) {
            return false;
        }
        if (Float.floatToIntBits(g) != Float.floatToIntBits(other.g)) {
            return false;
        }
        if (Float.floatToIntBits(r) != Float.floatToIntBits(other.r)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Color{" + "r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + '}';
    }

    /**
     * Parse a hex index into a color part. Will return 255 for a hex with no
     * alpha value.
     *
     * @param hex   the hex string to parse
     * @param index the index of the part to parse (0 - 3)
     *
     * @return the parsed color value (r, g, b, a)
     */
    private static int parseHex(final String hex, final int index) {
        // determine if it has a # prefix
        final int offset = hex.charAt(0) == '#' ? 1 : 0;
        // determine the type of format
        switch (hex.length() - offset) {
            // RGB
            case 3:
                if (index > 2) {
                    return 255;
                }
                return Integer.parseInt(hex.substring(offset + index, offset + index + 1), 16);
            // RGBA
            case 4:
                return Integer.parseInt(hex.substring(offset + index, offset + index + 1), 16);
            // RRGGBB
            case 6:
                if (index > 2) {
                    return 255;
                }
                return Integer.parseInt(hex.substring(offset + index * 2, offset + index * 2 + 2), 16);
            // RRGGBBAA
            case 8:
                return Integer.parseInt(hex.substring(offset + index * 2, offset + index * 2 + 2), 16);
            // unsupported format
            default:
                throw new IllegalArgumentException("hex");
        }
    }

    public static class Builder {

        protected float r;
        protected float g;
        protected float b;
        protected float a;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public float r() {
            return this.r;
        }

        public float g() {
            return this.g;
        }

        public float b() {
            return this.b;
        }

        public float a() {
            return this.a;
        }

        public Builder r(final float r) {
            this.r = r;
            return this;
        }

        public Builder g(final float g) {
            this.g = g;
            return this;
        }

        public Builder b(final float b) {
            this.b = b;
            return this;
        }

        public Builder a(final float a) {
            this.a = a;
            return this;
        }

        public Color build() {
            return new Color(r, g, b, a);
        }
    }

}
