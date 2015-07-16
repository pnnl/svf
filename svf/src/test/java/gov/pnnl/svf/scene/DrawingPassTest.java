package gov.pnnl.svf.scene;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class DrawingPassTest {

    /**
     * Test of isScene method, of class DrawingPass.
     */
    @Test
    public void testIsScene() {
        for (final DrawingPass drawingPass : DrawingPass.values()) {
            if (drawingPass.name().contains("SCENE") || drawingPass.name().contains("ALL")) {
                Assert.assertTrue(drawingPass.isScene());
            } else {
                Assert.assertFalse(drawingPass.isScene());
            }
        }
    }

    /**
     * Test of isInterface method, of class DrawingPass.
     */
    @Test
    public void testIsInterface() {
        for (final DrawingPass drawingPass : DrawingPass.values()) {
            if (drawingPass.name().contains("INTERFACE") || drawingPass.name().contains("ALL")) {
                Assert.assertTrue(drawingPass.isInterface());
            } else {
                Assert.assertFalse(drawingPass.isInterface());
            }
        }
    }

    /**
     * Test of isOverlay method, of class DrawingPass.
     */
    @Test
    public void testIsOverlay() {
        for (final DrawingPass drawingPass : DrawingPass.values()) {
            if (drawingPass.name().contains("OVERLAY") || drawingPass.name().contains("ALL")) {
                Assert.assertTrue(drawingPass.isOverlay());
            } else {
                Assert.assertFalse(drawingPass.isOverlay());
            }
        }
    }

    /**
     * Test of isPicking method, of class DrawingPass.
     */
    @Test
    public void testIsPicking() {
        for (final DrawingPass drawingPass : DrawingPass.values()) {
            if (drawingPass.name().contains("PICKING") || drawingPass.name().contains("ALL")) {
                Assert.assertTrue(drawingPass.isPicking());
            } else {
                Assert.assertFalse(drawingPass.isPicking());
            }
        }
    }

    /**
     * Test of addDrawingPass method, of class DrawingPass.
     */
    @Test
    public void testAddDrawingPass() {
        Assert.assertEquals(DrawingPass.NONE, DrawingPass.NONE.addDrawingPass(DrawingPass.NONE));
        Assert.assertEquals(DrawingPass.OVERLAY, DrawingPass.NONE.addDrawingPass(DrawingPass.OVERLAY));
        Assert.assertEquals(DrawingPass.OVERLAY, DrawingPass.OVERLAY.addDrawingPass(DrawingPass.OVERLAY));
    }

    /**
     * Test of getDrawingPass method, of class DrawingPass.
     */
    @Test
    public void testGetDrawingPass() {
        Assert.assertEquals(DrawingPass.NONE, DrawingPass.getDrawingPass(DrawingPass.NONE));
        Assert.assertEquals(DrawingPass.SCENE_INTERFACE_OVERLAY, DrawingPass.getDrawingPass(DrawingPass.NONE, DrawingPass.SCENE, DrawingPass.INTERFACE_OVERLAY));
        Assert.assertEquals(DrawingPass.ALL,
                            DrawingPass.getDrawingPass(DrawingPass.NONE, DrawingPass.SCENE, DrawingPass.INTERFACE_OVERLAY, DrawingPass.INTERFACE_PICKING));
    }

    /**
     * Test of containsDrawingPass method, of class DrawingPass.
     */
    @Test
    public void testContainsDrawingPass() {
        Assert.assertTrue(DrawingPass.ALL.containsDrawingPass(DrawingPass.NONE));
        Assert.assertTrue(DrawingPass.NONE.containsDrawingPass(DrawingPass.NONE));

        Assert.assertTrue(DrawingPass.INTERFACE_OVERLAY.containsDrawingPass(DrawingPass.INTERFACE));
        Assert.assertTrue(DrawingPass.INTERFACE_OVERLAY.containsDrawingPass(DrawingPass.OVERLAY));
        Assert.assertFalse(DrawingPass.INTERFACE_OVERLAY.containsDrawingPass(DrawingPass.SCENE));

        Assert.assertTrue(DrawingPass.ALL.containsDrawingPass(DrawingPass.SCENE_OVERLAY));
    }
}
