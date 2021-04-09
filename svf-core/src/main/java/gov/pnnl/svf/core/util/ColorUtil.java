package gov.pnnl.svf.core.util;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.color.ColorGradient;
import gov.pnnl.svf.core.color.ColorPalette;
import gov.pnnl.svf.core.texture.TextureType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility for working with the Color classes.
 *
 * @author Amelia Bleeker
 *
 */
public class ColorUtil {

    /**
     * The maximum number of colors that can be generated for a color palette.
     * Consider creating multiple palettes with varying saturation and lightness
     * for a larger number of colors.
     */
    public static final int MAX_COLORS = 100;
    private static final float GOLDEN_RATIO_CONJUGATE = 0.618033988749895f;
    private static final int MAX_INVALID = 1000;
    private static final int HUE_COUNT = 6;
    private static final float HUE_INCREMENT = 1.0f / HUE_COUNT;
    private static final float HUE_SHIFT = HUE_INCREMENT * GOLDEN_RATIO_CONJUGATE;
    private static final float DISTANCE_DELTA = 0.01f;
    private static final Logger logger = Logger.getLogger(ColorUtil.class.toString());
    private final static Random random = new Random();

    /**
     * Constructor kept private for static class
     */
    private ColorUtil() {
    }

    /**
     * Creates a new color by using compositing (alpha transparency) by using
     * the destination color as a solid background and blending the source color
     * onto it with the supplied transparency.
     *
     * @param dstColor     The destination or background color, alpha will be
     *                     ignored.
     * @param srcColor     The source or overlay color, the transparency value
     *                     will be used as the alpha value.
     * @param transparency The transparency for the overlay. {x: 0.0 &le; x &le;
     *                     1.0}
     *
     * @return the newly created color
     *
     * @throws NullPointerException     if the dstColor or srcColor argument is
     *                                  null
     * @throws IllegalArgumentException if the transparency is not in the set {x
     *                                  : 0.0 &le; x &le; 1.0}
     */
    public static Color createBlendedColor(final Color dstColor, final Color srcColor, final float transparency) {
        if (dstColor == null) {
            throw new NullPointerException("dstColor");
        }
        if (srcColor == null) {
            throw new NullPointerException("srcColor");
        }
        if ((Float.compare(transparency, 0.0f) < 0) || (Float.compare(transparency, 1.0f) > 0)) {
            throw new IllegalArgumentException("transparency");
        }
        // get the destination color values
        final float dstRed = dstColor.getRed();
        final float dstGreen = dstColor.getGreen();
        final float dstBlue = dstColor.getBlue();
        // get the source color values
        final float srcRed = srcColor.getRed() * transparency;
        final float srcGreen = srcColor.getGreen() * transparency;
        final float srcBlue = srcColor.getBlue() * transparency;
        // compute the new color (C = Cs + Cd(1 - a))
        return new Color(srcRed + dstRed * (1.0f - transparency), srcGreen + dstGreen * (1.0f - transparency), srcBlue + dstBlue * (1.0f - transparency), 1.0f);
    }

    /**
     * Creates a byte buffer of data that represents a gradient of colors.
     *
     * @param gradient The gradient to use for the texture
     * @param type     The type of texture data to create
     * @param length   The length of the lookup texture {x: 1 &le; x &le; 1024}
     *
     * @return the newly created lookup texture
     */
    public static ByteBuffer createLookupTexture(final ColorGradient gradient, final TextureType type, final int length) {
        if (gradient == null) {
            throw new NullPointerException("gradient");
        }
        if (type == null) {
            throw new NullPointerException("type");
        }
        if (length < 1 || length > 1024) {
            throw new IllegalArgumentException("length");
        }
        logger.log(Level.INFO, "Creating an {0} gradient texture of length {1}.", new Object[]{type, length});
        // create a byte buffer to store the texture data
        ByteBuffer buffer;
        switch (type) {
            case ALPHA:
                buffer = ByteBuffer.allocateDirect(length * 1);
                break;
            case RGB:
                buffer = ByteBuffer.allocateDirect(length * 3);
                break;
            case RGBA:
                buffer = ByteBuffer.allocateDirect(length * 4);
                break;
            default:
                logger.log(Level.WARNING, "Unknown enum type {0}", type);
                return null;
        }
        // this isn't the fastest way to do this...
        for (int i = 0; i < length; i++) {
            final Color color = gradient.getColor((double) i / (double) (length - 1));
            switch (type) {
                case ALPHA:
                    buffer.put(ColorUtil.convertToUByte(color.getAlpha()));
                    break;
                case RGB:
                    buffer.put(ColorUtil.convertToUByte(color.getRed()));
                    buffer.put(ColorUtil.convertToUByte(color.getGreen()));
                    buffer.put(ColorUtil.convertToUByte(color.getBlue()));
                    break;
                case RGBA:
                    buffer.put(ColorUtil.convertToUByte(color.getRed()));
                    buffer.put(ColorUtil.convertToUByte(color.getGreen()));
                    buffer.put(ColorUtil.convertToUByte(color.getBlue()));
                    buffer.put(ColorUtil.convertToUByte(color.getAlpha()));
                    break;
            }
        }
        // return the buffer
        return buffer;
    }

    /**
     * Creates a random color that is clamped between the min and max values.
     *
     * @param min minimum value for the color {x: 0.0 &le; x &le; 1.0}
     * @param max maximum value for the color {x: 0.0 &le; x &le; 1.0}
     *
     * @return a new random color
     */
    public static Color createRandomColor(final float min, final float max) {
        if ((min < 0.0f) || (min > 1.0f)) {
            throw new IllegalArgumentException("min");
        }
        if ((max < 0.0f) || (max > 1.0f)) {
            throw new IllegalArgumentException("max");
        }
        if (min > max) {
            throw new IllegalArgumentException("min < max");
        }
        final Color color = new Color(ColorUtil.nextFloat(min, max), ColorUtil.nextFloat(min, max), ColorUtil.nextFloat(min, max), 1.0f);
        return color;
    }

    /**
     * Creates a random color that is clamped between the min and max values.
     *
     * @param hMin minimum hue value for the color {x: 0.0 &le; x &le; 1.0}
     * @param hMax maximum hue value for the color {x: 0.0 &le; x &le; 1.0}
     * @param sMin minimum saturation value for the color {x: 0.0 &le; x &le;
     *             1.0}
     * @param sMax maximum saturation value for the color {x: 0.0 &le; x &le;
     *             1.0}
     * @param lMin minimum lightness value for the color {x: 0.0 &le; x &le;
     *             1.0}
     * @param lMax maximum lightness value for the color {x: 0.0 &le; x &le;
     *             1.0}
     *
     * @return a new random color
     */
    public static Color createRandomColor(final float hMin, final float hMax, final float sMin, final float sMax, final float lMin, final float lMax) {
        if ((hMin < 0.0f) || (hMin > 1.0f)) {
            throw new IllegalArgumentException("hMin");
        }
        if ((hMax < 0.0f) || (hMax > 1.0f)) {
            throw new IllegalArgumentException("hMax");
        }
        if (hMin > hMax) {
            throw new IllegalArgumentException("hMin < hMax");
        }
        if ((sMin < 0.0f) || (sMin > 1.0f)) {
            throw new IllegalArgumentException("sMin");
        }
        if ((sMax < 0.0f) || (sMax > 1.0f)) {
            throw new IllegalArgumentException("sMax");
        }
        if (sMin > sMax) {
            throw new IllegalArgumentException("sMin < sMax");
        }
        if ((lMin < 0.0f) || (lMin > 1.0f)) {
            throw new IllegalArgumentException("lMin");
        }
        if ((lMax < 0.0f) || (lMax > 1.0f)) {
            throw new IllegalArgumentException("lMax");
        }
        if (lMin > lMax) {
            throw new IllegalArgumentException("lMin < lMax");
        }
        return ColorUtil.hsbToColor(ColorUtil.nextFloat(hMin, hMax), ColorUtil.nextFloat(sMin, sMax), ColorUtil.nextFloat(lMin, lMax));
    }

    /**
     * Creates a color palette with varying hues.
     *
     * @param size the number of unique colors
     * @param base the base color for the palette
     *
     * @return the new palette
     *
     * @throws IllegalArgumentException if size is less than one or greater than
     *                                  MAX_COLORS
     */
    public static ColorPalette createColorPalette(final int size, final Color base) {
        return ColorUtil.createColorPalette(size, base, (Color[]) null);
    }

    /**
     * Creates a color palette with varying hues.
     *
     * @param size     the number of unique colors
     * @param base     the base color for the palette
     * @param excludes the colors (hues) to exclude from the palette
     *
     * @return the new palette
     *
     * @throws IllegalArgumentException if size is less than one or greater than
     *                                  MAX_COLORS
     */
    public static ColorPalette createColorPalette(final int size, final Color base, final Color... excludes) {
        if (size < 1) {
            throw new IllegalArgumentException("size");
        }
        if (size > MAX_COLORS) {
            throw new IllegalArgumentException("size");
        }
        if (base == null) {
            throw new NullPointerException("base");
        }
        // create the exclude list
        final Set<Color> exclude = new HashSet<>();
        if (excludes != null) {
            exclude.addAll(Arrays.asList(excludes));
        }
        // break the color base into HSB values
        final float[] hsb = ColorUtil.colorToHsb(base);
        float hue = hsb[0];
        float saturation = hsb[1];
        float lightness = hsb[2];
        // keep track of the iterations of the current saturation and lightness
        int invalid = 0;
        // create and populate the color palette list
        final List<Color> colors = new ArrayList<>(size);
        while (colors.size() < size) {
            // check if out of colors in this saturation and lightness
            if (invalid >= MAX_INVALID) {
                logger.log(Level.INFO, "Unable to create a full color palette");
                break;
            }
            // check excludes
            final Color color = ColorUtil.hsbToColor(hue, saturation, lightness);
            if (ColorUtil.isValid(color, exclude)) {
                colors.add(color);
                exclude.add(color);
            } else {
                invalid++;
                logger.log(Level.FINE, "Skipping invalid Hue: {0}", hue);
            }
            // go to next color in the hue
            hue += HUE_INCREMENT;
            // shift after each full hue pass
            if (hue >= 1.0f) {
                hue += HUE_SHIFT;
            }
            // normalize
            hue %= 1.0f;
        }
        return new ColorPalette(colors);
    }

    /**
     * Creates a color palette with varying hues.
     *
     * @param size       the number of unique colors
     * @param saturation the saturation {x: 0.0 &le; x &le; 1.0}
     * @param lightness  the lightness {x: 0.0 &le; x &le; 1.0}
     *
     * @return the new palette
     *
     * @throws IllegalArgumentException if size is less than one or greater than
     *                                  MAX_COLORS
     */
    public static ColorPalette createColorPalette(final int size, final float saturation, final float lightness) {
        return ColorUtil.createColorPalette(size, ColorUtil.hsbToColor(0.0f, saturation, lightness));
    }

    private static boolean isValid(final Color color, final Set<Color> excludes) {
        for (final Color exclude : excludes) {
            // compute the color distance
            final double distance = Math.sqrt((color.getRed() - exclude.getRed()) * (color.getRed() - exclude.getRed())
                                              + (color.getGreen() - exclude.getGreen()) * (color.getGreen() - exclude.getGreen())
                                              + (color.getBlue() - exclude.getBlue()) * (color.getBlue() - exclude.getBlue()));
            if (distance < DISTANCE_DELTA) {
                return false;
            }
        }
        return true;
    }

    private static float nextFloat(final float min, final float max) {
        return random.nextFloat() * (max - min) + min;
    }

    private static byte convertToUByte(final double value) {
        final int temp = (int) (value * 255.0);
        return (byte) temp;
    }

    private static float[] colorToHsb(final Color color) {
        return java.awt.Color.RGBtoHSB((int) (color.getRed() * 255.0f), (int) (color.getGreen() * 255.0f), (int) (color.getBlue() * 255.0f), null);
    }

    private static Color hsbToColor(final float hue, final float saturation, final float lightness) {
        final java.awt.Color temp = new java.awt.Color(java.awt.Color.HSBtoRGB(hue, saturation, lightness));
        final Color color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 255);
        return color;
    }
}
