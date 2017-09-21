package gov.pnnl.svf.camera;

import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class OrbitCameraTest extends AbstractObjectTestBase<OrbitCamera> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public OrbitCameraTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected OrbitCamera copyValueObject(final OrbitCamera object) {
        final OrbitCamera copy = new OrbitCamera(object.getScene(), object.getType(), object.getId());
        copy.setDirty(object.isDirty());
        copy.setDrawingPass(object.getDrawingPass());
        copy.setFarClip(object.getFarClip());
        copy.setFieldOfView(object.getFieldOfView());
        copy.setLook(object.getLook());
        copy.setNearClip(object.getNearClip());
        copy.setPassNumber(object.getPassNumber());
        copy.setThickness(object.getThickness());
        copy.setType(object.getType());
        copy.setUp(object.getUp());
        copy.setViewport(object.getViewport());
        copy.setVisible(object.isVisible());
        copy.setWire(object.isWire());
        copy.setZoomMultiplier(object.getZoomMultiplier());

        copy.setRotateMultiplier(object.getRotateMultiplier());
        return copy;
    }

    @Override
    protected OrbitCamera newValueObject() {
        final OrbitCamera object = new OrbitCamera(scene);
        return object;
    }

    @Override
    protected void setFieldsToNull(final OrbitCamera object) {
        // no operation
    }

    /**
     * Test of setMoveMultiplier method, of class OrbitCamera.
     */
    @Test
    public void testSetMoveMultiplier() {
        final OrbitCamera camera = newValueObject();
        testBoundField(camera, OrbitCamera.MOVE_MULTIPLIER, 0.567f);
    }

    /**
     * Test of setRotateMultiplier method, of class OrbitCamera.
     */
    @Test
    public void testSetRotateMultiplier() {
        final OrbitCamera camera = newValueObject();
        testBoundField(camera, OrbitCamera.ROTATE_MULTIPLIER, 0.567f);
    }

    /**
     * Test of setZoomMultiplier method, of class OrbitCamera.
     */
    @Test
    public void testSetZoomMultiplier() {
        final OrbitCamera camera = newValueObject();
        testBoundField(camera, OrbitCamera.ZOOM_MULTIPLIER, 0.567f);
    }
}
