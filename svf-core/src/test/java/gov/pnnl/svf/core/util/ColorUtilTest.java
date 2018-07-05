package gov.pnnl.svf.core.util;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.color.ColorEntry;
import gov.pnnl.svf.core.color.ColorGradient;
import gov.pnnl.svf.core.color.ColorPalette;
import gov.pnnl.svf.core.texture.TextureType;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class ColorUtilTest {

    private static final float DELTA = 0.0123f;
    private static final int ITERATIONS = 100;

    public ColorUtilTest() {
    }

    /**
     * Test of createRandomColor method, of class ColorUtil.
     */
    @Test
    public void testCreateRandomColor() {
        final float min = 0.2F;
        final float max = 0.75F;
        for (int i = 0; i < ITERATIONS; i++) {
            final Color result = ColorUtil.createRandomColor(min, max);
            Assert.assertTrue(result.getRed() >= min);
            Assert.assertTrue(result.getRed() <= max);
            Assert.assertTrue(result.getGreen() >= min);
            Assert.assertTrue(result.getGreen() <= max);
            Assert.assertTrue(result.getBlue() >= min);
            Assert.assertTrue(result.getBlue() <= max);
            Assert.assertEquals(1.0f, result.getAlpha(), 0.0f);
        }
    }

    /**
     * Test of createRandomColor method, of class ColorUtil.
     */
    @Test
    public void testCreateRandomColorHsl() {
        final float[] hsb = new float[3];
        final float min = 45 / 255.0f; // normalize the value
        final float max = 201 / 255.0f; // normalize the value
        for (int i = 0; i < ITERATIONS; i++) {
            final Color result = ColorUtil.createRandomColor(min, max, min, max, min, max);
            java.awt.Color.RGBtoHSB((int) (result.getRed() * 255.0f), (int) (result.getGreen() * 255.0f), (int) (result.getBlue() * 255.0f), hsb);
            for (final float f : hsb) {
                Assert.assertTrue(MessageFormat.format("{2} {0} less than min {1}", f, min), f >= min - DELTA);
                Assert.assertTrue(MessageFormat.format("{2} {0} greater than max {1}", f, max), f <= max + DELTA);
            }
            Assert.assertEquals(1.0f, result.getAlpha(), 0.0f);
        }
    }

    @Test
    public void testCreateColorPalette() {
        final ColorPalette palette = ColorUtil.createColorPalette(ITERATIONS, 0.75f, 0.5f);
        Assert.assertNotNull(palette);
        Assert.assertEquals(ITERATIONS, palette.getColors().size());
    }

    @Test
    public void testCreateColorPalette_Color() {
        final Color base = Color.ORANGE;
        final ColorPalette palette = ColorUtil.createColorPalette(ITERATIONS, base);
        Assert.assertNotNull(palette);
        Assert.assertEquals(ITERATIONS, palette.getColors().size());
        Assert.assertEquals(base, palette.getColors().get(0));
    }

    /**
     * Test of createBlendedColor method, of class ColorUtil.
     */
    @Test
    public void testCreateBlendedColor() {
        final Color dstColor = Color.BLACK;
        final Color srcColor = Color.WHITE;
        final float transparency = 0.4F;
        final Color expResult = Color.GRAY;
        final Color result = ColorUtil.createBlendedColor(dstColor, srcColor, transparency);
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of createLookupTexture method, of class ColorUtil.
     */
    @Test
    public void testCreateLookupTextureAlpha() {
        final ColorGradient gradient = new ColorGradient(new ColorEntry(0.0, new Color(0.0f, 0.0f, 0.0f, 0.0f)),
                                                         new ColorEntry(1.0, Color.WHITE));
        final TextureType type = TextureType.ALPHA;
        final int length = 9;
        final ByteBuffer result = ColorUtil.createLookupTexture(gradient, type, length);
        Assert.assertNotNull(result);
        Assert.assertEquals(length, result.limit());
        Assert.assertEquals(length, result.position());
        result.rewind();
        final byte[] expResult = new byte[]{0, 31, 63, 95, 127, -97, -65, -33, -1};
        for (int i = 0; i < expResult.length; i++) {
            final byte value = result.get();
            // System.out.println(value);
            Assert.assertEquals(expResult[i], value);
        }
    }

    /**
     * Test of createLookupTexture method, of class ColorUtil.
     */
    @Test
    public void testCreateLookupTextureRgb() {
        final ColorGradient gradient = new ColorGradient(new ColorEntry(0.0, new Color(0.0f, 0.0f, 0.0f, 0.0f)),
                                                         new ColorEntry(1.0, Color.INDIGO));
        final TextureType type = TextureType.RGB;
        final int length = 3;
        final ByteBuffer result = ColorUtil.createLookupTexture(gradient, type, length);
        Assert.assertNotNull(result);
        Assert.assertEquals(length * 3, result.limit());
        Assert.assertEquals(length * 3, result.position());
        result.rewind();
        final byte[] expResult = new byte[]{0, 0, 0, 37, 0, 65, 75, 0, -126};
        for (int i = 0; i < expResult.length; i++) {
            final byte value = result.get();
            // System.out.println(value);
            Assert.assertEquals(expResult[i], value);
        }
    }

    /**
     * Test of createLookupTexture method, of class ColorUtil.
     */
    @Test
    public void testCreateLookupTextureRgba() {
        final ColorGradient gradient = new ColorGradient(new ColorEntry(0.0, new Color(0.0f, 0.0f, 0.0f, 0.0f)),
                                                         new ColorEntry(1.0, Color.INDIGO));
        final TextureType type = TextureType.RGBA;
        final int length = 3;
        final ByteBuffer result = ColorUtil.createLookupTexture(gradient, type, length);
        Assert.assertNotNull(result);
        Assert.assertEquals(length * 4, result.limit());
        Assert.assertEquals(length * 4, result.position());
        result.rewind();
        final byte[] expResult = new byte[]{0, 0, 0, 0, 37, 0, 65, 127, 75, 0, -126, -1};
        for (int i = 0; i < expResult.length; i++) {
            final byte value = result.get();
            // System.out.println(value);
            Assert.assertEquals(expResult[i], value);
        }
    }

    /**
     * Test of createLookupTexture method, of class ColorUtil.
     */
    @Test
    public void testCreateLookupTextureRgba2() {
        final ColorGradient gradient = new ColorGradient(new ColorEntry(0.0, Color.RED),
                                                         new ColorEntry(0.5, Color.WHITE),
                                                         new ColorEntry(1.0, Color.BLUE));
        final TextureType type = TextureType.RGBA;
        final int length = 10;
        final ByteBuffer result = ColorUtil.createLookupTexture(gradient, type, length);
        Assert.assertNotNull(result);
        Assert.assertEquals(length * 4, result.limit());
        Assert.assertEquals(length * 4, result.position());
        result.rewind();
        for (int i = 0; i < length; i++) {
            final byte r = result.get();
            final byte g = result.get();
            final byte b = result.get();
            final byte a = result.get();
            final Color color = new Color(r, g, b, a);
            final Color expected = gradient.getColor((double) i / (double) (length - 1));
            // System.out.println(i + " Expected " + expected);
            // System.out.println(i + "   Actual " + color);
            Assert.assertEquals(expected.getRed(), color.getRed(), 0.01f);
            Assert.assertEquals(expected.getGreen(), color.getGreen(), 0.01f);
            Assert.assertEquals(expected.getBlue(), color.getBlue(), 0.01f);
            Assert.assertEquals(expected.getAlpha(), color.getAlpha(), 0.01f);
        }
    }

    /**
     * Test of createLookupTexture method, of class ColorUtil.
     */
    @Test
    public void testCreateLookupTextureRgba3() {
        final ColorGradient gradient = new ColorGradient(new ColorEntry(0.0, Color.BLACK),
                                                         new ColorEntry(0.5, new Color("FFBF00")),
                                                         new ColorEntry(1.0, Color.WHITE));
        final TextureType type = TextureType.RGBA;
        final int length = 10;
        final ByteBuffer result = ColorUtil.createLookupTexture(gradient, type, length);
        Assert.assertNotNull(result);
        Assert.assertEquals(length * 4, result.limit());
        Assert.assertEquals(length * 4, result.position());
        result.rewind();
        for (int i = 0; i < length; i++) {
            final byte r = result.get();
            final byte g = result.get();
            final byte b = result.get();
            final byte a = result.get();
            final Color color = new Color(r, g, b, a);
            final Color expected = gradient.getColor((double) i / (double) (length - 1));
            // System.out.println(i + " Expected " + expected);
            // System.out.println(i + "   Actual " + color);
            Assert.assertEquals(expected.getRed(), color.getRed(), 0.01f);
            Assert.assertEquals(expected.getGreen(), color.getGreen(), 0.01f);
            Assert.assertEquals(expected.getBlue(), color.getBlue(), 0.01f);
            Assert.assertEquals(expected.getAlpha(), color.getAlpha(), 0.01f);
        }
    }
}
