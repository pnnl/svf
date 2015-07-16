package gov.pnnl.svf.actor;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.util.ColorUtil;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Shape;
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
public class ShapeActorTest extends AbstractObjectTestBase<ShapeActor> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final Random random = new Random();

    public ShapeActorTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected ShapeActor copyValueObject(final ShapeActor object) {
        final ShapeActor copy = new ShapeActor(object.getScene(), object.getType(), object.getId());
        copy.setCamera(object.getCamera());
        copy.setDirty(object.isDirty());
        copy.setDrawingPass(object.getDrawingPass());
        copy.setPassNumber(object.getPassNumber());
        copy.setThickness(object.getThickness());
        copy.setType(object.getType());
        copy.setVisible(object.isVisible());
        copy.setWire(object.isWire());
        copy.setBackgroundColor(object.getBackgroundColor());
        copy.setColor(object.getColor());
        copy.setOrigin(object.getOrigin());
        copy.setShape(object.getShape());
        return copy;
    }

    @Override
    protected ShapeActor newValueObject() {
        final ShapeActor object = ShapeActor.Builder.construct()
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
                .backgroundColor(random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null)
                .color(random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null)
                .origin(Alignment.values()[random.nextInt(Alignment.values().length)])
                .shape(random.nextBoolean() ? Rectangle.ZERO : null)
                .build();
        return object;
    }

    @Override
    protected void setFieldsToNull(final ShapeActor object) {
        object.setBackgroundColor(null)
                .setColor(null)
                .setShape(null)
                .setCamera(null);
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setBackgroundColorTest() {
        final Color a = Color.RED;
        final Color b = Color.BROWN;
        final ShapeActor actor = newValueObject();
        actor.setBackgroundColor(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, ShapeActor.BACKGROUND_COLOR, b, Color.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setColorTest() {
        final Color a = Color.RED;
        final Color b = Color.BROWN;
        final ShapeActor actor = newValueObject();
        actor.setColor(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, ShapeActor.COLOR, b, Color.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setOriginTest() {
        final Alignment a = Alignment.BOTTOM;
        final Alignment b = Alignment.RIGHT;
        final ShapeActor actor = newValueObject();
        actor.setOrigin(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, ShapeActor.ORIGIN, b, Alignment.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setShapeTest() {
        final Shape a = Rectangle.ZERO;
        final Shape b = Circle2D.ONE;
        final ShapeActor actor = newValueObject();
        actor.setShape(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, ShapeActor.SHAPE, b, Shape.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setCameraTest() {
        final Camera a = new SimpleCamera(scene);
        final Camera b = new SimpleCamera(scene);
        final ShapeActor actor = newValueObject();
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
        final ShapeActor actor = newValueObject();
        actor.setDrawingPass(DrawingPass.SCENE);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.DRAWING_PASS, DrawingPass.INTERFACE);
        Assert.assertTrue(actor.isDirty());
    }

    @Test
    public void setIdTest() {
        final ShapeActor actor = newValueObject();
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
        final ShapeActor actor = newValueObject();
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
        final ShapeActor actor = newValueObject();
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
        final ShapeActor actor = newValueObject();
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
        final ShapeActor actor = newValueObject();
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
        final ShapeActor actor = newValueObject();
        actor.setWire(true);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.WIRE, false);
        Assert.assertTrue(actor.isDirty());
    }
}
