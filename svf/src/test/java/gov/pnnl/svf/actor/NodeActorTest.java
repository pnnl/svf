package gov.pnnl.svf.actor;

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
public class NodeActorTest extends AbstractObjectTestBase<NodeActor> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final Random random = new Random();

    public NodeActorTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected NodeActor copyValueObject(final NodeActor object) {
        final NodeActor copy = new NodeActor(object.getScene(), object.getType(), object.getId());
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
    protected NodeActor newValueObject() {
        final NodeActor object = NodeActor.Builder.construct()
                .scene(scene)
                .type("actor")
                .id(scene.getFactory().newUuid(scene))
                .camera(random.nextBoolean() ? scene.lookup(Camera.class) : null)
                .dirty(random.nextBoolean())
                .drawingPass(DrawingPass.values()[random.nextInt(DrawingPass.values().length)])
                .passNumber((byte) random.nextInt(Byte.MAX_VALUE))
                .thickness(random.nextFloat())
                .visible(random.nextBoolean())
                .wire(random.nextBoolean())
                .build();
        return object;
    }

    @Override
    protected void setFieldsToNull(final NodeActor object) {
        object.setCamera(null);
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setCameraTest() {
        final Camera a = new SimpleCamera(scene);
        final Camera b = new SimpleCamera(scene);
        final NodeActor actor = newValueObject();
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
        final NodeActor actor = newValueObject();
        actor.setDrawingPass(DrawingPass.SCENE);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.DRAWING_PASS, DrawingPass.INTERFACE);
        Assert.assertTrue(actor.isDirty());
    }

    @Test
    public void setIdTest() {
        final NodeActor actor = newValueObject();
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
        final NodeActor actor = newValueObject();
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
        final NodeActor actor = newValueObject();
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
        final NodeActor actor = newValueObject();
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
        final NodeActor actor = newValueObject();
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
        final NodeActor actor = newValueObject();
        actor.setWire(true);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.WIRE, false);
        Assert.assertTrue(actor.isDirty());
    }
}
