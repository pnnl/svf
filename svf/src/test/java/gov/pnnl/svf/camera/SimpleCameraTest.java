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
public class SimpleCameraTest extends AbstractObjectTestBase<SimpleCamera> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public SimpleCameraTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected SimpleCamera copyValueObject(final SimpleCamera object) {
        final SimpleCamera copy = new SimpleCamera(object.getScene(), object.getType(), object.getId());
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

        copy.setMaxRotate(object.getMaxRotate());
        return copy;
    }

    @Override
    protected SimpleCamera newValueObject() {
        final SimpleCamera object = new SimpleCamera(scene);
        return object;
    }

    @Override
    protected void setFieldsToNull(final SimpleCamera object) {
        //no fields to set
    }

    /**
     * Test of moveBackward method, of class SimpleCamera.
     */
    @Test
    public void testMoveBackward() {
        final SimpleCamera camera = newValueObject();
        camera.moveBackward(1.0);
    }

    /**
     * Test of moveDown method, of class SimpleCamera.
     */
    @Test
    public void testMoveDown() {
        final SimpleCamera camera = newValueObject();
        camera.moveDown(1.0);
    }

    /**
     * Test of moveForward method, of class SimpleCamera.
     */
    @Test
    public void testMoveForward() {
        final SimpleCamera camera = newValueObject();
        camera.moveForward(1.0);
    }

    /**
     * Test of moveLeft method, of class SimpleCamera.
     */
    @Test
    public void testMoveLeft() {
        final SimpleCamera camera = newValueObject();
        camera.moveLeft(1.0);
    }

    /**
     * Test of moveRight method, of class SimpleCamera.
     */
    @Test
    public void testMoveRight() {
        final SimpleCamera camera = newValueObject();
        camera.moveRight(1.0);
    }

    /**
     * Test of moveUp method, of class SimpleCamera.
     */
    @Test
    public void testMoveUp() {
        final SimpleCamera camera = newValueObject();
        camera.moveUp(1.0);
    }

    /**
     * Test of rotateDown method, of class SimpleCamera.
     */
    @Test
    public void testRotateDown() {
        final SimpleCamera camera = newValueObject();
        camera.rotateDown(1.0);
    }

    /**
     * Test of rotateLeft method, of class SimpleCamera.
     */
    @Test
    public void testRotateLeft() {
        final SimpleCamera camera = newValueObject();
        camera.rotateLeft(1.0);
    }

    /**
     * Test of rotateRight method, of class SimpleCamera.
     */
    @Test
    public void testRotateRight() {
        final SimpleCamera camera = newValueObject();
        camera.rotateRight(1.0);
    }

    /**
     * Test of rotateUp method, of class SimpleCamera.
     */
    @Test
    public void testRotateUp() {
        final SimpleCamera camera = newValueObject();
        camera.rotateUp(1.0);
    }

    /**
     * Test of setMaxRotate method, of class SimpleCamera.
     */
    @Test
    public void testSetMaxRotate() {
        final SimpleCamera camera = newValueObject();
        testBoundField(camera, SimpleCamera.MAX_ROTATE, 0.5);
    }
}
