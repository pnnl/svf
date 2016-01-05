package gov.pnnl.svf.actor;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for ArcActors
 */
public class AbstractActorTest extends AbstractObjectTestBase<AbstractActor> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final Random random = new Random();

    public AbstractActorTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected AbstractActor copyValueObject(final AbstractActor object) {
        final AbstractActor copy = new AbstractActorImpl(object.getScene(), object.getType(), object.getId());
        copy.setCamera(object.getCamera());
        copy.setDirty(object.isDirty());
        copy.setDrawingPass(object.getDrawingPass());
        copy.setPassNumber(object.getPassNumber());
        copy.setThickness(object.getThickness());
        copy.setType(object.getType());
        copy.setVisible(object.isVisible());
        copy.setWire(object.isWire());
        return copy;
    }

    @Override
    protected AbstractActor newValueObject() {
        final AbstractActor object = new AbstractActorImpl(scene, "actor", scene.getFactory().newUuid(scene));
        object.setCamera(random.nextBoolean() ? scene.lookup(Camera.class) : null);
        object.setDirty(random.nextBoolean());
        object.setDrawingPass(DrawingPass.values()[random.nextInt(DrawingPass.values().length)]);
        object.setPassNumber((byte) random.nextInt(Byte.MAX_VALUE));
        object.setThickness(random.nextFloat());
        object.setVisible(random.nextBoolean());
        object.setWire(random.nextBoolean());
        return object;
    }

    @Override
    protected void setFieldsToNull(final AbstractActor object) {
        object.setCamera(null);
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setCameraTest() {
        final Camera a = new SimpleCamera(scene);
        final Camera b = new SimpleCamera(scene);
        final AbstractActor actor = newValueObject();
        actor.setCamera(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.CAMERA, b, Camera.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setDrawingPassTest() {
        final AbstractActor actor = newValueObject();
        actor.setDrawingPass(DrawingPass.SCENE);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.DRAWING_PASS, DrawingPass.INTERFACE);
        Assert.assertTrue(actor.isDirty());
    }

    @Test
    public void setIdTest() {
        final AbstractActor actor = newValueObject();
        actor.setId("0");
        Assert.assertEquals("0", actor.getId());
        actor.setId("1");
        Assert.assertEquals("1", actor.getId());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setPassNumberTest() {
        final AbstractActor actor = newValueObject();
        actor.setPassNumber(0);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.PASS_NUMBER, 1);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setThicknessTest() {
        final AbstractActor actor = newValueObject();
        actor.setThickness(0.0f);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.THICKNESS, 1.0f);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setTypeTest() {
        final AbstractActor actor = newValueObject();
        actor.setType("one");
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.TYPE, "two");
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setVisibleTest() {
        final AbstractActor actor = newValueObject();
        actor.setVisible(true);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.VISIBLE, false);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setWireTest() {
        final AbstractActor actor = newValueObject();
        actor.setWire(true);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.WIRE, false);
        Assert.assertTrue(actor.isDirty());
    }

    private static class AbstractActorImpl extends AbstractActor {

        private AbstractActorImpl(final Scene scene, final String type, final String id) {
            super(scene, type, id);
        }

        @Override
        public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
