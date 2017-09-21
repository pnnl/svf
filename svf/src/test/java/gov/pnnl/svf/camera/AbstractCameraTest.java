package gov.pnnl.svf.camera;

import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class AbstractCameraTest extends AbstractObjectTestBase<AbstractCamera> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public AbstractCameraTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test of setFarClip method, of class AbstractCamera.
     */
    @Test
    public void testSetFarClip() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AbstractCamera actor = newValueObject();
        actor.setFarClip(0.345);
        actor.setDirty(false);
        actor.getPropertyChangeSupport().addPropertyChangeListener(Camera.FAR_CLIP, new PropertyChangeListener() {
                                                               @Override
                                                               public void propertyChange(final PropertyChangeEvent evt) {
                                                                   Assert.assertTrue(evt.getNewValue() instanceof Double);
                                                                   passed.set(true);
                                                               }
                                                           });
        actor.setFarClip(0.9);
        Assert.assertTrue(passed.get());
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Test of setFieldOfView method, of class AbstractCamera.
     */
    @Test
    public void testSetFieldOfView() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AbstractCamera actor = newValueObject();
        actor.setFieldOfView(0.345);
        actor.setDirty(false);
        actor.getPropertyChangeSupport().addPropertyChangeListener(Camera.FIELD_OF_VIEW, new PropertyChangeListener() {
                                                               @Override
                                                               public void propertyChange(final PropertyChangeEvent evt) {
                                                                   Assert.assertTrue(evt.getNewValue() instanceof Double);
                                                                   passed.set(true);
                                                               }
                                                           });
        actor.setFieldOfView(0.9);
        Assert.assertTrue(passed.get());
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Test of setLook method, of class AbstractCamera.
     */
    @Test
    public void testSetLook() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AbstractCamera actor = newValueObject();
        actor.setLook(new Vector3D(0.1, 0.2, 0.3));
        actor.setDirty(false);
        actor.getPropertyChangeSupport().addPropertyChangeListener(Camera.LOOK, new PropertyChangeListener() {
                                                               @Override
                                                               public void propertyChange(final PropertyChangeEvent evt) {
                                                                   Assert.assertTrue(evt.getNewValue() instanceof Vector3D);
                                                                   passed.set(true);
                                                               }
                                                           });
        actor.setLook(new Vector3D(0.4, 0.4, 0.4));
        Assert.assertTrue(passed.get());
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Test of setNearClip method, of class AbstractCamera.
     */
    @Test
    public void testSetNearClip() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AbstractCamera actor = newValueObject();
        actor.setNearClip(0.345);
        actor.setDirty(false);
        actor.getPropertyChangeSupport().addPropertyChangeListener(Camera.NEAR_CLIP, new PropertyChangeListener() {
                                                               @Override
                                                               public void propertyChange(final PropertyChangeEvent evt) {
                                                                   Assert.assertTrue(evt.getNewValue() instanceof Double);
                                                                   passed.set(true);
                                                               }
                                                           });
        actor.setNearClip(0.9);
        Assert.assertTrue(passed.get());
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Test of setUp method, of class AbstractCamera.
     */
    @Test
    public void testSetUp() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AbstractCamera actor = newValueObject();
        actor.setUp(new Vector3D(0.1, 0.2, 0.3));
        actor.setDirty(false);
        actor.getPropertyChangeSupport().addPropertyChangeListener(Camera.UP, new PropertyChangeListener() {
                                                               @Override
                                                               public void propertyChange(final PropertyChangeEvent evt) {
                                                                   Assert.assertTrue(evt.getNewValue() instanceof Vector3D);
                                                                   passed.set(true);
                                                               }
                                                           });
        actor.setUp(new Vector3D(0.4, 0.4, 0.4));
        Assert.assertTrue(passed.get());
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Test of setViewport method, of class AbstractCamera.
     */
    @Test
    public void testSetViewport() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AbstractCamera actor = newValueObject();
        actor.setViewport(new Rectangle(0, 0, 150, 150));
        actor.setDirty(false);
        actor.getPropertyChangeSupport().addPropertyChangeListener(Camera.VIEWPORT, new PropertyChangeListener() {
                                                               @Override
                                                               public void propertyChange(final PropertyChangeEvent evt) {
                                                                   Assert.assertTrue(evt.getNewValue() instanceof Rectangle);
                                                                   passed.set(true);
                                                               }
                                                           });
        actor.setViewport(new Rectangle(20, 20, 50, 50));
        Assert.assertTrue(passed.get());
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Test of update method, of class AbstractCamera.
     */
    @Test
    public void testUpdate() {
        final AbstractCamera actor = newValueObject();
        actor.setUp(Vector3D.PLUS_J);
        actor.setLook(Vector3D.PLUS_K);
        actor.update(1000L);
        Assert.assertEquals(Vector3D.PLUS_I, actor.getPerp());
    }

    @Override
    protected AbstractCamera copyValueObject(final AbstractCamera object) {
        final AbstractCameraImpl copy = new AbstractCameraImpl(object.getScene(), object.getType(), object.getId());
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

        return copy;
    }

    @Override
    protected AbstractCamera newValueObject() {
        final AbstractCameraImpl object = new AbstractCameraImpl(scene);
        return object;
    }

    @Override
    protected void setFieldsToNull(final AbstractCamera object) {
        // no operation
    }

    public static class AbstractCameraImpl extends AbstractCamera {

        public AbstractCameraImpl(final Scene scene) {
            super(scene);
        }

        public AbstractCameraImpl(final Scene scene, final String type, final String id) {
            super(scene, type, id);
        }

        public AbstractCameraImpl(final Scene scene, final String id) {
            super(scene, id);
        }
    }
}
