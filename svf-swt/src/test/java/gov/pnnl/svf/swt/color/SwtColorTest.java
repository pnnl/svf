package gov.pnnl.svf.swt.color;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class SwtColorTest {

    public SwtColorTest() {
    }

    @Test
    public void testConstructor() {
        final org.eclipse.swt.graphics.Color expResult = new org.eclipse.swt.graphics.Color(null, 25, 51, 237);
        final SwtColor instance = new SwtColor(expResult);
        final org.eclipse.swt.graphics.Color result = instance.toSwtColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);

        expResult.dispose();
        result.dispose();
    }

    @Test
    public void testConstructorAlphaInt() {
        final org.eclipse.swt.graphics.Color expResult = new org.eclipse.swt.graphics.Color(null, 25, 51, 237);
        final SwtColor instance = new SwtColor(expResult, 255);
        final org.eclipse.swt.graphics.Color result = instance.toSwtColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);

        expResult.dispose();
        result.dispose();
    }

    @Test
    public void testConstructorAlphaFloat() {
        final org.eclipse.swt.graphics.Color expResult = new org.eclipse.swt.graphics.Color(null, 25, 51, 237);
        final SwtColor instance = new SwtColor(expResult, 1.0f);
        final org.eclipse.swt.graphics.Color result = instance.toSwtColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);

        expResult.dispose();
        result.dispose();
    }

    @Test
    public void testConstructorRGB() {
        final org.eclipse.swt.graphics.RGB expResult = new org.eclipse.swt.graphics.RGB(25, 51, 237);
        final SwtColor instance = new SwtColor(expResult);
        final org.eclipse.swt.graphics.RGB result = instance.toRgbColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testConstructorRGBAlphaInt() {
        final org.eclipse.swt.graphics.RGB expResult = new org.eclipse.swt.graphics.RGB(25, 51, 237);
        final SwtColor instance = new SwtColor(expResult, 255);
        final org.eclipse.swt.graphics.RGB result = instance.toRgbColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testConstructorRGBAlphaFloat() {
        final org.eclipse.swt.graphics.RGB expResult = new org.eclipse.swt.graphics.RGB(25, 51, 237);
        final SwtColor instance = new SwtColor(expResult, 1.0f);
        final org.eclipse.swt.graphics.RGB result = instance.toRgbColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of toSwtColor method, of class Color.
     */
    @Test
    public void testToSwtColor() {
        final org.eclipse.swt.graphics.Color expResult = new org.eclipse.swt.graphics.Color(null, 25, 51, 237);
        final SwtColor instance = new SwtColor(expResult.getRed(), expResult.getGreen(), expResult.getBlue());
        final org.eclipse.swt.graphics.Color result = instance.toSwtColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }
}
