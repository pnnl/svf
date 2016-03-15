package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Arrays;
import org.junit.Assert;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ClippingPlaneSupportTest {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public ClippingPlaneSupportTest() {
    }

    /**
     * Test of clearClippingPlanes method, of class ClippingPlaneSupport.
     */
    @Test
    public void testClearClippingPlanes() {
        final ShapeActor actor = new ShapeActor(scene);
        final ClippingPlaneSupport clipping = ClippingPlaneSupport.newInstance(actor);
        Assert.assertEquals(0, clipping.getClippingPlanesSize());
        clipping.addClippingPlane(1.0, 0.0, 0.0, 0.0);
        Assert.assertEquals(1, clipping.getClippingPlanesSize());
        clipping.clearClippingPlanes();
        Assert.assertEquals(0, clipping.getClippingPlanesSize());
    }

    /**
     * Test of addClippingPlane method, of class ClippingPlaneSupport.
     */
    @Test
    public void testAddClippingPlane_4args() {
        final ShapeActor actor = new ShapeActor(scene);
        final ClippingPlaneSupport clipping = ClippingPlaneSupport.newInstance(actor);
        clipping.addClippingPlane(1.0, 0.0, 0.5, 0.25);
        // System.out.println("4 args: " + Arrays.toString(clipping.getClippingPlane(0)));
        Assert.assertEquals(1, clipping.getClippingPlanesSize());
        Assert.assertTrue(Arrays.equals(new double[]{1.0, 0.0, 0.5, 0.25}, clipping.getClippingPlane(0)));
    }

    /**
     * Test of addClippingPlane method, of class ClippingPlaneSupport.
     */
    @Test
    public void testAddClippingPlane_3args() {
        final ShapeActor actor = new ShapeActor(scene);
        final ClippingPlaneSupport clipping = ClippingPlaneSupport.newInstance(actor);
        final Vector3D one = new Vector3D(1.0, -6.0, 0.0);
        final Vector3D two = new Vector3D(-4.0, 2.0, -5.0);
        final Vector3D three = new Vector3D(-2.0, 4.0, 1.0);
        clipping.addClippingPlane(one, two, three);
        // System.out.println("3 args: " + Arrays.toString(clipping.getClippingPlane(0)));
        Assert.assertEquals(1, clipping.getClippingPlanesSize());
        Assert.assertTrue(Arrays.equals(new double[]{58.0, 20.0, -26.0, 62.0}, clipping.getClippingPlane(0)));
    }

    /**
     * Test of addClippingPlane method, of class ClippingPlaneSupport.
     */
    @Test
    public void testAddClippingPlane_2args() {
        final ShapeActor actor = new ShapeActor(scene);
        final ClippingPlaneSupport clipping = ClippingPlaneSupport.newInstance(actor);
        final Vector3D one = new Vector3D(1.0, 0.0, 0.0);
        clipping.addClippingPlane(one, -0.25);
        // System.out.println("2 args: " + Arrays.toString(clipping.getClippingPlane(0)));
        Assert.assertEquals(1, clipping.getClippingPlanesSize());
        Assert.assertTrue(Arrays.equals(new double[]{1.0, 0.0, 0.0, -0.25}, clipping.getClippingPlane(0)));
    }

    /**
     * Test of newInstance method, of class ClippingPlaneSupport.
     */
    @Test
    public void testNewInstance() {
        final ShapeActor actor = new ShapeActor(scene);
        final ClippingPlaneSupport clipping = ClippingPlaneSupport.newInstance(actor);
        Assert.assertNotNull(clipping);
    }
}
