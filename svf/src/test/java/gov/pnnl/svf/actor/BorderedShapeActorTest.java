package gov.pnnl.svf.actor;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Border;
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
public class BorderedShapeActorTest extends AbstractObjectTestBase<BorderedShapeActor> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final Random random = new Random();

    public BorderedShapeActorTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected BorderedShapeActor copyValueObject(final BorderedShapeActor object) {
        final BorderedShapeActor copy = new BorderedShapeActor(object.getScene(), object.getType(), object.getId());
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
        copy.setBorder(object.getBorder());
        copy.setBorderColor(object.getBorderColor());
        copy.setBorderThickness(object.getBorderThickness());
        return copy;
    }

    @Override
    protected BorderedShapeActor newValueObject() {
        final BorderedShapeActor object = BorderedShapeActor.Builder.construct()
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
                .border(Border.getBorder(random.nextBoolean(), random.nextBoolean(), random.nextBoolean(), random.nextBoolean()))
                .borderColor(random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null)
                .borderThickness(random.nextDouble())
                .build();
        return object;
    }

    @Override
    protected void setFieldsToNull(final BorderedShapeActor object) {
        object.setBorderColor(null)
                .setBackgroundColor(null)
                .setColor(null)
                .setShape(null)
                .setCamera(null);
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setBorderThicknessTest() {
        final BorderedShapeActor actor = newValueObject();
        actor.setBorderThickness(0.0);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, BorderedShapeActor.BORDER_THICKNESS, 1.0);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setBorderColorTest() {
        final Color a = Color.RED;
        final Color b = Color.BROWN;
        final BorderedShapeActor actor = newValueObject();
        actor.setBorderColor(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, BorderedShapeActor.BORDER_COLOR, b, Color.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setBorderTest() {
        final Border a = Border.ALL;
        final Border b = Border.NONE;
        final BorderedShapeActor actor = newValueObject();
        actor.setBorder(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, BorderedShapeActor.BORDER, b, Border.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setBackgroundColorTest() {
        final Color a = Color.RED;
        final Color b = Color.BROWN;
        final BorderedShapeActor actor = newValueObject();
        actor.setBackgroundColor(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, BorderedShapeActor.BACKGROUND_COLOR, b, Color.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setColorTest() {
        final Color a = Color.RED;
        final Color b = Color.BROWN;
        final BorderedShapeActor actor = newValueObject();
        actor.setColor(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, BorderedShapeActor.COLOR, b, Color.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setOriginTest() {
        final Alignment a = Alignment.BOTTOM;
        final Alignment b = Alignment.RIGHT;
        final BorderedShapeActor actor = newValueObject();
        actor.setOrigin(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, BorderedShapeActor.ORIGIN, b, Alignment.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setShapeTest() {
        final Shape a = Rectangle.ZERO;
        final Shape b = Circle2D.ONE;
        final BorderedShapeActor actor = newValueObject();
        actor.setShape(a);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, BorderedShapeActor.SHAPE, b, Shape.class);
        Assert.assertTrue(actor.isDirty());
    }

    /**
     * Tests whether the actor is set as dirty when the a property is changed
     */
    @Test
    public void setCameraTest() {
        final Camera a = new SimpleCamera(scene);
        final Camera b = new SimpleCamera(scene);
        final BorderedShapeActor actor = newValueObject();
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
        final BorderedShapeActor actor = newValueObject();
        actor.setDrawingPass(DrawingPass.SCENE);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.DRAWING_PASS, DrawingPass.INTERFACE);
        Assert.assertTrue(actor.isDirty());
    }

    @Test
    public void setIdTest() {
        final BorderedShapeActor actor = newValueObject();
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
        final BorderedShapeActor actor = newValueObject();
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
        final BorderedShapeActor actor = newValueObject();
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
        final BorderedShapeActor actor = newValueObject();
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
        final BorderedShapeActor actor = newValueObject();
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
        final BorderedShapeActor actor = newValueObject();
        actor.setWire(true);
        actor.setDirty(false);
        Assert.assertFalse(actor.isDirty());
        testBoundField(actor, Actor.WIRE, false);
        Assert.assertTrue(actor.isDirty());
    }
}
