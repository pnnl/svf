package gov.pnnl.svf.util;

import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class FpsLoggerTest {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public FpsLoggerTest() {
    }

    /**
     * Test of newInstance method, of class FpsLogger.
     */
    @Test
    public void testNewInstance() {
        final FpsLogger instance = FpsLogger.newInstance(scene, 1000L);
        Assert.assertNotNull(instance);
    }

    /**
     * Test of newInstance method, of class FpsLogger.
     */
    @Test(expected = NullPointerException.class)
    public void testNewInstanceNullPointerEx() {
        FpsLogger.newInstance(null, 1000L);
    }

    /**
     * Test of update method, of class FpsLogger.
     */
    @Test
    public void testUpdate() {
        final FpsLogger instance = FpsLogger.newInstance(scene, 1000L);
        // draw 30 times per second
        for (int i = 0; i < 30; i++) {
            instance.draw(null, null, null);
        }
        instance.update(1000L);
        Assert.assertEquals(30, instance.getAverageFps());
        // draw 121 times per second
        for (int i = 0; i < 121; i++) {
            instance.draw(null, null, null);
        }
        instance.update(1000L);
        Assert.assertEquals(121, instance.getAverageFps());
        // draw 100 times per second
        for (int i = 0; i < 150; i++) {
            instance.draw(null, null, null);
        }
        instance.update(1500L);
        Assert.assertEquals(100, instance.getAverageFps());
    }

    /**
     * Test of getText method, of class FpsLogger.
     */
    @Test
    public void testGetText() {
        final FpsLogger instance = FpsLogger.newInstance(scene, 1000L);
        Assert.assertNotNull(instance.getText());

        final String text = "text: ";
        instance.setText(text);
        Assert.assertEquals(text, instance.getText());
    }

    /**
     * Test of setText method, of class FpsLogger.
     */
    @Test(expected = NullPointerException.class)
    public void testSetText() {
        final FpsLogger instance = FpsLogger.newInstance(scene, 1000L);
        instance.setText(null);
    }

    /**
     * Test of draw method, of class FpsLogger.
     */
    @Test
    public void testDraw() {
        final FpsLogger instance = FpsLogger.newInstance(scene, 1000L);
        instance.draw(null, null, null);
    }

    /**
     * Test of endDraw method, of class FpsLogger.
     */
    @Test
    public void testEndDraw() {
        final FpsLogger instance = FpsLogger.newInstance(scene, 1000L);
        instance.endDraw(null, null, null);
    }
}
