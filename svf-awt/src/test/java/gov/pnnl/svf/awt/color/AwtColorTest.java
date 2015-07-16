package gov.pnnl.svf.awt.color;

import gov.pnnl.svf.core.color.Color;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class AwtColorTest {

    public AwtColorTest() {
    }

    @Test
    public void testConstructor() {
        final java.awt.Color expResult = new java.awt.Color(25, 51, 237, 67);
        final Color instance = new AwtColor(expResult);
        final java.awt.Color result = instance.toAwtColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testConstructorAlphaInt() {
        final java.awt.Color expResult = new java.awt.Color(25, 51, 237, 67);
        final Color instance = new AwtColor(expResult, 67);
        final java.awt.Color result = instance.toAwtColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void testConstructorAlphaFloat() {
        final java.awt.Color expResult = new java.awt.Color(25, 51, 237, 67);
        final Color instance = new AwtColor(expResult, 67 / 255.0f);
        final java.awt.Color result = instance.toAwtColor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
    }
}
