package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ConfigUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Arthur Bleeker
 *
 */
public class AbstractSupportTest {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public AbstractSupportTest() {
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.support.AbstractSupport#addListener(java.lang.Object)}.
     */
    @Test
    public void testAddListener() {
        final ShapeActor actor = new ShapeActor(scene);
        final ColorSupport support = ColorSupport.newInstance(actor);
        Assert.assertEquals(0, support.getListeners().size());
        support.addListener(new Object());
        Assert.assertEquals(1, support.getListeners().size());
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.support.AbstractSupport#removeListener(java.lang.Object)}.
     */
    @Test
    public void testRemoveListener() {
        final ShapeActor actor = new ShapeActor(scene);
        final ColorSupport support = ColorSupport.newInstance(actor);
        Assert.assertEquals(0, support.getListeners().size());
        final Object listener = new Object();
        support.addListener(listener);
        Assert.assertEquals(1, support.getListeners().size());
        support.removeListener(listener);
        Assert.assertEquals(0, support.getListeners().size());
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.support.AbstractSupport#clearListeners()}.
     */
    @Test
    public void testClearListeners() {
        final ShapeActor actor = new ShapeActor(scene);
        final ColorSupport support = ColorSupport.newInstance(actor);
        Assert.assertEquals(0, support.getListeners().size());
        support.addListener(new Object());
        Assert.assertEquals(1, support.getListeners().size());
        support.clearListeners();
        Assert.assertEquals(0, support.getListeners().size());
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.support.AbstractSupport#getActor()}.
     */
    @Test
    public void testGetActor() {
        final ShapeActor actor = new ShapeActor(scene);
        final ColorSupport support = ColorSupport.newInstance(actor);
        Assert.assertNotNull(support.getActor());
        Assert.assertTrue(actor == support.getActor());
    }
}
