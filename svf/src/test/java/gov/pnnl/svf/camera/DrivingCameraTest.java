package gov.pnnl.svf.camera;

import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;

/**
 *
 * @author Arthur Bleeker
 */
public class DrivingCameraTest extends AbstractObjectTestBase<DrivingCamera> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public DrivingCameraTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected DrivingCamera copyValueObject(final DrivingCamera object) {
        final DrivingCamera copy = new DrivingCamera(object.getScene(), object.getType(), object.getId());
        copy.setCamera(object.getCamera());
        copy.setDirty(object.isDirty());
        copy.setDrawingPass(object.getDrawingPass());
        copy.setFarClip(object.getFarClip());
        copy.setFieldOfView(object.getFieldOfView());
        copy.setLook(object.getLook());
        copy.setMaxRotate(object.getMaxRotate());
        copy.setNearClip(object.getNearClip());
        copy.setPassNumber(object.getPassNumber());
        copy.setThickness(object.getThickness());
        copy.setType(object.getType());
        copy.setUp(object.getUp());
        copy.setViewport(object.getViewport());
        copy.setVisible(object.isVisible());
        copy.setWire(object.isWire());

        return copy;
    }

    @Override
    protected DrivingCamera newValueObject() {
        final DrivingCamera object = new DrivingCamera(scene);
        return object;
    }

    @Override
    protected void setFieldsToNull(final DrivingCamera object) {
        object.setCamera(null);
    }
}
