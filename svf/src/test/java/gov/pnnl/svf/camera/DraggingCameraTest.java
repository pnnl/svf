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
public class DraggingCameraTest extends AbstractObjectTestBase<DraggingCamera> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public DraggingCameraTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test of setZoomToMouse method, of class DraggingCamera.
     */
    @Test
    public void testSetZoomToMouse() {
        final DraggingCamera camera = newValueObject();
        testBoundField(camera, DraggingCamera.ZOOM_TO_MOUSE, false);
    }

    /**
     * Test of setDragMultiplier method, of class DraggingCamera.
     */
    @Test
    public void testSetDragMultiplier() {
        final DraggingCamera camera = newValueObject();
        testBoundField(camera, DraggingCamera.DRAG_MULTIPLIER, 1.678f);
    }

    /**
     * Test of setZoomMultiplier method, of class DraggingCamera.
     */
    @Test
    public void testSetZoomMultiplier() {
        final DraggingCamera camera = newValueObject();
        testBoundField(camera, DraggingCamera.ZOOM_MULTIPLIER, 1.678f);
    }

    /**
     * Test of setZoomMax method, of class DraggingCamera.
     */
    @Test
    public void testSetZoomMax() {
        final DraggingCamera camera = newValueObject();
        testBoundField(camera, DraggingCamera.ZOOM_MAX, 10.0f);
    }

    /**
     * Test of setZoomMin method, of class DraggingCamera.
     */
    @Test
    public void testSetZoomMin() {
        final DraggingCamera camera = newValueObject();
        testBoundField(camera, DraggingCamera.ZOOM_MIN, 1.1f);
    }

    @Override
    protected DraggingCamera copyValueObject(final DraggingCamera object) {
        final DraggingCamera copy = new DraggingCamera(object.getScene(), object.getType(), object.getId());
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
        copy.setDragMultiplier(object.getDragMultiplier());
        copy.setZoomMax(object.getZoomMax());
        copy.setZoomMin(object.getZoomMin());
        copy.setZoomMultiplier(object.getZoomMultiplier());
        return copy;
    }

    @Override
    protected DraggingCamera newValueObject() {
        final DraggingCamera object = new DraggingCamera(scene);
        return object;
    }

    @Override
    protected void setFieldsToNull(final DraggingCamera object) {
        // no operation
    }
}
