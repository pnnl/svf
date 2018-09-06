/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.util;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.ProxyDraggingCamera;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class CameraUtilTest {

    /**
     * Test of fitInViewport method, of class CameraUtil.
     */
    @Test
    public void testFitInViewport() throws Exception {
        final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
        Assert.assertEquals(new Rectangle(0, 0, 1200, 800), scene.getViewport());
        final Camera camera = new ProxyDraggingCamera(scene);
        scene.start();
        try {
            Assert.assertEquals(new Rectangle(0, 0, 1200, 800), camera.getViewport());
            CameraUtil.fitInViewport(camera, Rectangle2D.ONE, false);
            Assert.assertEquals(2.2553542518310286, camera.getLocation().getZ(), 0.0);
        } finally {
            scene.stop();
        }
    }

    /**
     * Test of fitInViewport method, of class CameraUtil.
     */
    @Test
    public void testFitInViewport_a() throws Exception {
        final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
        Assert.assertEquals(new Rectangle(0, 0, 1200, 800), scene.getViewport());
        final Camera camera = new ProxyDraggingCamera(scene);
        scene.start();
        try {
            Assert.assertEquals(new Rectangle(0, 0, 1200, 800), camera.getViewport());
            CameraUtil.fitInViewport(camera, new Rectangle2D(0.0, 0.0, 11.4751196974882, 7.650079798325467), false);
            Assert.assertEquals(17.25364, camera.getLocation().getZ(), 0.0);
        } finally {
            scene.stop();
        }
    }

    /**
     * Test of findSceneView method, of class CameraUtil.
     */
    @Test
    public void testFindSceneView() throws Exception {
        final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
        Assert.assertEquals(new Rectangle(0, 0, 1200, 800), scene.getViewport());
        final Camera camera = new ProxyDraggingCamera(scene);
        scene.start();
        try {
            Assert.assertEquals(new Rectangle(0, 0, 1200, 800), camera.getViewport());
            camera.setLocation(new Vector3D(0.0, 0.0, 1.0));
            final Rectangle2D view = CameraUtil.findViewArea(camera);
            Assert.assertEquals(0.6650839879288196, view.getWidth(), 0.0);
            Assert.assertEquals(0.44338932528587977, view.getHeight(), 0.0);
        } finally {
            scene.stop();
        }
    }

    /**
     * Test of findSceneView method, of class CameraUtil.
     */
    @Test
    public void testFindSceneView_a() throws Exception {
        final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
        Assert.assertEquals(new Rectangle(0, 0, 1200, 800), scene.getViewport());
        final Camera camera = new ProxyDraggingCamera(scene);
        camera.setLook(Vector3D.PLUS_I);
        scene.start();
        try {
            Assert.assertEquals(new Rectangle(0, 0, 1200, 800), camera.getViewport());
            camera.setLocation(new Vector3D(-1.0, 0.0, 0.0));
            final Rectangle2D view = CameraUtil.findViewArea(camera);
            Assert.assertEquals(0.6650839879288196, view.getWidth(), 0.0);
            Assert.assertEquals(0.44338932528587977, view.getHeight(), 0.0);
        } finally {
            scene.stop();
        }
    }

    /**
     * Test of findSceneView method, of class CameraUtil.
     */
    @Test
    public void testFindSceneView_b() throws Exception {
        final Scene scene = new ProxyScene(new ProxyGLCanvas(800, 800), ConfigUtil.configure());
        Assert.assertEquals(new Rectangle(0, 0, 800, 800), scene.getViewport());
        final Camera camera = new ProxyDraggingCamera(scene);
        scene.start();
        try {
            Assert.assertEquals(new Rectangle(0, 0, 800, 800), camera.getViewport());
            camera.setLocation(new Vector3D(0.0, 0.0, 1.0));
            final Rectangle2D view = CameraUtil.findViewArea(camera);
            Assert.assertEquals(0.44338932528587977, view.getWidth(), 0.0);
            Assert.assertEquals(0.44338932528587977, view.getHeight(), 0.0);
        } finally {
            scene.stop();
        }
    }

    /**
     * Test of findSceneView method, of class CameraUtil.
     */
    @Test
    public void testFindSceneView_c() throws Exception {
        final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
        Assert.assertEquals(new Rectangle(0, 0, 1200, 800), scene.getViewport());
        final Camera camera = new ProxyDraggingCamera(scene);
        scene.start();
        try {
            Assert.assertEquals(new Rectangle(0, 0, 1200, 800), camera.getViewport());
            camera.setLocation(new Vector3D(0.0, 0.0, 17.25364));
            final Rectangle2D view = CameraUtil.findViewArea(camera);
            Assert.assertEquals(11.4751196974882, view.getWidth(), 0.0);
            Assert.assertEquals(7.650079798325467, view.getHeight(), 0.0);
        } finally {
            scene.stop();
        }
    }

}
