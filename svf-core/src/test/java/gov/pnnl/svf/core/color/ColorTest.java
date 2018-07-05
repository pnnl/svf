package gov.pnnl.svf.core.color;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class ColorTest extends AbstractObjectTestBase<Color> {

    private final Random random = new Random();

    public ColorTest() {
    }

    @Test
    public void color() {
        final Color color = new Color();
        Assert.assertNotNull(color);
        Assert.assertEquals(1.0f, color.getRed(), 0.0001f);
        Assert.assertEquals(1.0f, color.getGreen(), 0.0001f);
        Assert.assertEquals(1.0f, color.getBlue(), 0.0001f);
        Assert.assertEquals(1.0f, color.getAlpha(), 0.0001f);
    }

    @Test
    public void colorString() {
        // 6 length uppercase
        final Color color1 = new Color("FFFFFF");
        Assert.assertNotNull(color1);
        Assert.assertEquals(Color.WHITE, color1);
        // 6 length lowercase
        final Color color2 = new Color("ffaabb");
        Assert.assertNotNull(color2);
        Assert.assertEquals(new Color(255, 170, 187), color2);
        // 8 length uppercse
        final Color color3 = new Color("FF12BA");
        Assert.assertNotNull(color3);
        Assert.assertEquals(new Color(255, 18, 186), color3);
        // 8 length lowercase
        final Color color4 = new Color("fb12ad");
        Assert.assertNotNull(color4);
        Assert.assertEquals(new Color(251, 18, 173), color4);
    }

    @Test
    public void colorByte() {
        final Color color = new Color((byte) 255, (byte) 0, (byte) 204, (byte) 51);
        Assert.assertNotNull(color);
        Assert.assertEquals(new Color(255, 0, 204, 51), color);
    }

    /**
     * Test of getRed method, of class Color.
     */
    @Test
    public void testGetRed() {
        final float expResult = 0.5f;
        final Color instance = new Color(expResult, 0.0f, 0.0f, 0.0f);
        final float result = instance.getRed();
        Assert.assertEquals(expResult, result, 2.0f / 255.0f);
    }

    /**
     * Test of getGreen method, of class Color.
     */
    @Test
    public void testGetGreen() {
        final float expResult = 0.5f;
        final Color instance = new Color(0.0f, expResult, 0.0f, 0.0f);
        final float result = instance.getGreen();
        Assert.assertEquals(expResult, result, 2.0f / 255.0f);
    }

    /**
     * Test of getBlue method, of class Color.
     */
    @Test
    public void testGetBlue() {
        final float expResult = 0.5f;
        final Color instance = new Color(0.0f, 0.0f, expResult, 0.0f);
        final float result = instance.getBlue();
        Assert.assertEquals(expResult, result, 2.0f / 255.0f);
    }

    /**
     * Test of getAlpha method, of class Color.
     */
    @Test
    public void testGetAlpha() {
        final float expResult = 0.5f;
        final Color instance = new Color(0.0f, 0.0f, 0.0f, expResult);
        final float result = instance.getAlpha();
        Assert.assertEquals(expResult, result, 2.0f / 255.0f);
    }

    /**
     * Test of getRed method, of class Color.
     */
    @Test
    public void testGetRedBuilder() {
        final float expResult = 0.5f;
        final Color instance = Color.Builder.construct()
                .r(expResult)
                .build();
        final float result = instance.getRed();
        Assert.assertEquals(expResult, result, 2.0f / 255.0f);
    }

    /**
     * Test of getGreen method, of class Color.
     */
    @Test
    public void testGetGreenBuilder() {
        final float expResult = 0.5f;
        final Color instance = Color.Builder.construct()
                .g(expResult)
                .build();
        final float result = instance.getGreen();
        Assert.assertEquals(expResult, result, 2.0f / 255.0f);
    }

    /**
     * Test of getBlue method, of class Color.
     */
    @Test
    public void testGetBlueBuilder() {
        final float expResult = 0.5f;
        final Color instance = Color.Builder.construct()
                .b(expResult)
                .build();
        final float result = instance.getBlue();
        Assert.assertEquals(expResult, result, 2.0f / 255.0f);
    }

    /**
     * Test of getAlpha method, of class Color.
     */
    @Test
    public void testGetAlphaBuilder() {
        final float expResult = 0.5f;
        final Color instance = Color.Builder.construct()
                .a(expResult)
                .build();
        final float result = instance.getAlpha();
        Assert.assertEquals(expResult, result, 2.0f / 255.0f);
    }

    /**
     * Test of toRgbArray method, of class Color.
     */
    @Test
    public void testToRgbArray() {
        final float[] expResult = new float[]{0.1f, 0.2f, 0.3f};
        final Color instance = new Color(expResult[0], expResult[1], expResult[2]);
        final float[] result = instance.toRgbArray();
        Assert.assertEquals(3, result.length);
        Assert.assertEquals(expResult[0], result[0], 2.0f / 255.0f);
        Assert.assertEquals(expResult[1], result[1], 2.0f / 255.0f);
        Assert.assertEquals(expResult[2], result[2], 2.0f / 255.0f);
    }

    /**
     * Test of toRgbaArray method, of class Color.
     */
    @Test
    public void testToRgbaArray() {
        final float[] expResult = new float[]{0.1f, 0.2f, 0.3f, 0.4f};
        final Color instance = new Color(expResult[0], expResult[1], expResult[2], expResult[3]);
        final float[] result = instance.toRgbaArray();
        Assert.assertEquals(4, result.length);
        Assert.assertEquals(expResult[0], result[0], 2.0f / 255.0f);
        Assert.assertEquals(expResult[1], result[1], 2.0f / 255.0f);
        Assert.assertEquals(expResult[2], result[2], 2.0f / 255.0f);
        Assert.assertEquals(expResult[3], result[3], 2.0f / 255.0f);
    }

    /**
     * Test of toArgbArray method, of class Color.
     */
    @Test
    public void testToArgbArray() {
        final float[] expResult = new float[]{0.4f, 0.1f, 0.2f, 0.3f};
        final Color instance = new Color(expResult[1], expResult[2], expResult[3], expResult[0]);
        final float[] result = instance.toArgbArray();
        Assert.assertEquals(4, result.length);
        Assert.assertEquals(expResult[0], result[0], 2.0f / 255.0f);
        Assert.assertEquals(expResult[1], result[1], 2.0f / 255.0f);
        Assert.assertEquals(expResult[2], result[2], 2.0f / 255.0f);
        Assert.assertEquals(expResult[3], result[3], 2.0f / 255.0f);
    }

    /**
     * Test of toRgbArray method, of class Color.
     */
    @Test
    public void testToRgbArray_array() {
        final float[] expResult = new float[]{0.1f, 0.2f, 0.3f};
        final Color instance = new Color(expResult[0], expResult[1], expResult[2]);
        final float[] result = new float[3];
        instance.toRgbArray(result);
        Assert.assertEquals(3, result.length);
        Assert.assertEquals(expResult[0], result[0], 2.0f / 255.0f);
        Assert.assertEquals(expResult[1], result[1], 2.0f / 255.0f);
        Assert.assertEquals(expResult[2], result[2], 2.0f / 255.0f);
    }

    /**
     * Test of toRgbaArray method, of class Color.
     */
    @Test
    public void testToRgbaArray_array() {
        final float[] expResult = new float[]{0.1f, 0.2f, 0.3f, 0.4f};
        final Color instance = new Color(expResult[0], expResult[1], expResult[2], expResult[3]);
        final float[] result = new float[4];
        instance.toRgbaArray(result);
        Assert.assertEquals(4, result.length);
        Assert.assertEquals(expResult[0], result[0], 2.0f / 255.0f);
        Assert.assertEquals(expResult[1], result[1], 2.0f / 255.0f);
        Assert.assertEquals(expResult[2], result[2], 2.0f / 255.0f);
        Assert.assertEquals(expResult[3], result[3], 2.0f / 255.0f);
    }

    /**
     * Test of toArgbArray method, of class Color.
     */
    @Test
    public void testToArgbArray_array() {
        final float[] expResult = new float[]{0.4f, 0.1f, 0.2f, 0.3f};
        final Color instance = new Color(expResult[1], expResult[2], expResult[3], expResult[0]);
        final float[] result = new float[4];
        instance.toArgbArray(result);
        Assert.assertEquals(4, result.length);
        Assert.assertEquals(expResult[0], result[0], 2.0f / 255.0f);
        Assert.assertEquals(expResult[1], result[1], 2.0f / 255.0f);
        Assert.assertEquals(expResult[2], result[2], 2.0f / 255.0f);
        Assert.assertEquals(expResult[3], result[3], 2.0f / 255.0f);
    }

    /**
     * Test of toAwtColor method, of class Color.
     */
    @Test
    public void testToAwtColor() {
        final java.awt.Color expResult = new java.awt.Color(25, 51, 237, 67);
        final Color instance = new Color(expResult.getRed(), expResult.getGreen(), expResult.getBlue(), expResult.getAlpha());
        final java.awt.Color result = instance.toAwtColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of toInt method, of class Color.
     */
    @Test
    public void testToInt() {
        final Color expResult = new Color(25, 51, 237, 67);
        final int value = expResult.toInt();
        final Color result = Color.fromInt(value);
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }

    @Override
    protected Color copyValueObject(final Color object) {
        return new Color(object.getRed(), object.getGreen(), object.getBlue(), object.getAlpha());
    }

    @Override
    protected Color newValueObject() {
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());
    }

    @Override
    protected void setFieldsToNull(final Color object) {
        // no settable fields
    }
}
