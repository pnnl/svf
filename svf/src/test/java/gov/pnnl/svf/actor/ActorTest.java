package gov.pnnl.svf.actor;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.util.ColorUtil;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Volume3D;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.WeakEqualsHelper;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for Actor
 */
public class ActorTest {

    private static final int BUILDER_ITERATIONS = 1000;
    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final Random random = new Random();
    private final Map<String, List<String>> ignore = new HashMap<>();

    public ActorTest() {
        ignore.put(AbstractActor.class.getName(), Arrays.asList("propertyChangeSupport"));
        ignore.put(AbstractDynamicActor.class.getName(), Arrays.asList("loadListener"));
    }

    /**
     * Tests the invisible actor builder
     */
    @Test
    public void invisibleActorBuilderTest() {
        for (int i = 0; i < BUILDER_ITERATIONS; i++) {
            final String type = "actor";
            final String id = scene.getFactory().newUuid(scene);
            final Camera camera = random.nextBoolean() ? scene.lookup(Camera.class) : null;
            final boolean dirty = random.nextBoolean();
            final DrawingPass drawingPass = DrawingPass.values()[random.nextInt(DrawingPass.values().length)];
            final byte passNumber = (byte) random.nextInt(Byte.MAX_VALUE);
            final float thickness = random.nextFloat();
            final boolean visible = random.nextBoolean();
            final boolean wire = random.nextBoolean();
            // test
            final Actor a = InvisibleActor.Builder.construct()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .build();

            final Actor b = Actor.Builder.construct()
                    .invisible()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .build();

            // test equality
            final boolean result = WeakEqualsHelper.weakEquals(a, b, ignore);
            Assert.assertTrue(WeakEqualsHelper.getErrors().toString(), result);
        }
    }

    /**
     * Tests the node actor builder
     */
    @Test
    public void nodeActorBuilderTest() {
        for (int i = 0; i < BUILDER_ITERATIONS; i++) {
            final String type = "actor";
            final String id = scene.getFactory().newUuid(scene);
            final Camera camera = random.nextBoolean() ? scene.lookup(Camera.class) : null;
            final boolean dirty = random.nextBoolean();
            final DrawingPass drawingPass = DrawingPass.values()[random.nextInt(DrawingPass.values().length)];
            final byte passNumber = (byte) random.nextInt(Byte.MAX_VALUE);
            final float thickness = random.nextFloat();
            final boolean visible = random.nextBoolean();
            final boolean wire = random.nextBoolean();
            // test
            final Actor a = NodeActor.Builder.construct()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .build();

            final Actor b = Actor.Builder.construct()
                    .node()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .build();

            // test equality
            final boolean result = WeakEqualsHelper.weakEquals(a, b, ignore);
            Assert.assertTrue(WeakEqualsHelper.getErrors().toString(), result);
        }
    }

    /**
     * Tests the shape actor builder
     */
    @Test
    public void shapeActorBuilderTest() {
        for (int i = 0; i < BUILDER_ITERATIONS; i++) {
            final String type = "actor";
            final String id = scene.getFactory().newUuid(scene);
            final Camera camera = random.nextBoolean() ? scene.lookup(Camera.class) : null;
            final boolean dirty = random.nextBoolean();
            final DrawingPass drawingPass = DrawingPass.values()[random.nextInt(DrawingPass.values().length)];
            final byte passNumber = (byte) random.nextInt(Byte.MAX_VALUE);
            final float thickness = random.nextFloat();
            final boolean visible = random.nextBoolean();
            final boolean wire = random.nextBoolean();
            final Color backgroundColor = random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null;
            final Color color = random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null;
            final Alignment origin = Alignment.values()[random.nextInt(Alignment.values().length)];
            final Shape shape = random.nextBoolean() ? Rectangle.ZERO : null;
            // test
            final Actor a = DynamicShapeActor.Builder.construct()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .backgroundColor(backgroundColor)
                    .color(color)
                    .origin(origin)
                    .shape(shape)
                    .build();

            final Actor b = Actor.Builder.construct()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .backgroundColor(backgroundColor)
                    .color(color)
                    .origin(origin)
                    .shape(shape)
                    .build();

            // test equality
            final boolean result = WeakEqualsHelper.weakEquals(a, b, ignore);
            Assert.assertTrue(WeakEqualsHelper.getErrors().toString(), result);
        }
    }

    /**
     * Tests the volume actor builder
     */
    @Test
    public void volumeActorBuilderTest() {
        for (int i = 0; i < BUILDER_ITERATIONS; i++) {
            final String type = "actor";
            final String id = scene.getFactory().newUuid(scene);
            final Camera camera = random.nextBoolean() ? scene.lookup(Camera.class) : null;
            final boolean dirty = random.nextBoolean();
            final DrawingPass drawingPass = DrawingPass.values()[random.nextInt(DrawingPass.values().length)];
            final byte passNumber = (byte) random.nextInt(Byte.MAX_VALUE);
            final float thickness = random.nextFloat();
            final boolean visible = random.nextBoolean();
            final boolean wire = random.nextBoolean();
            final Color backgroundColor = random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null;
            final Color color = random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null;
            final Alignment origin = Alignment.values()[random.nextInt(Alignment.values().length)];
            final Volume3D shape = random.nextBoolean() ? Volume3D.ONE : null;
            // test
            final Actor a = VolumeActor.Builder.construct()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .backgroundColor(backgroundColor)
                    .color(color)
                    .origin(origin)
                    .shape(shape)
                    .build();

            final Actor b;
            if (random.nextBoolean() || shape == null) {
                b = Actor.Builder.construct()
                        .volume(shape)
                        .scene(scene)
                        .type(type)
                        .id(id)
                        .camera(camera)
                        .dirty(dirty)
                        .drawingPass(drawingPass)
                        .passNumber(passNumber)
                        .thickness(thickness)
                        .visible(visible)
                        .wire(wire)
                        .backgroundColor(backgroundColor)
                        .color(color)
                        .origin(origin)
                        .build();
            } else {
                b = Actor.Builder.construct()
                        .scene(scene)
                        .type(type)
                        .id(id)
                        .camera(camera)
                        .dirty(dirty)
                        .drawingPass(drawingPass)
                        .passNumber(passNumber)
                        .thickness(thickness)
                        .visible(visible)
                        .wire(wire)
                        .backgroundColor(backgroundColor)
                        .color(color)
                        .origin(origin)
                        .shape(shape)
                        .build();
            }

            // test equality
            final boolean result = WeakEqualsHelper.weakEquals(a, b, ignore);
            Assert.assertTrue(WeakEqualsHelper.getErrors().toString(), result);
        }
    }

    /**
     * Tests the bordered shape actor builder
     */
    @Test
    public void borderedShapeActorBuilderTest() {
        for (int i = 0; i < BUILDER_ITERATIONS; i++) {
            final String type = "actor";
            final String id = scene.getFactory().newUuid(scene);
            final Camera camera = random.nextBoolean() ? scene.lookup(Camera.class) : null;
            final boolean dirty = random.nextBoolean();
            final DrawingPass drawingPass = DrawingPass.values()[random.nextInt(DrawingPass.values().length)];
            final byte passNumber = (byte) random.nextInt(Byte.MAX_VALUE);
            final float thickness = random.nextFloat();
            final boolean visible = random.nextBoolean();
            final boolean wire = random.nextBoolean();
            final Color backgroundColor = random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null;
            final Color color = random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null;
            final Alignment origin = Alignment.values()[random.nextInt(Alignment.values().length)];
            final Shape shape = random.nextBoolean() ? Rectangle.ZERO : null;
            final Border border = Border.getBorder(random.nextBoolean(), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
            final Color borderColor = random.nextBoolean() ? ColorUtil.createRandomColor(0.0f, 1.0f) : null;
            final double borderThickness = random.nextDouble();
            // test
            final Actor a = DynamicBorderedShapeActor.Builder.construct()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .backgroundColor(backgroundColor)
                    .color(color)
                    .origin(origin)
                    .shape(shape)
                    .border(border)
                    .borderColor(borderColor)
                    .borderThickness(borderThickness)
                    .build();

            final Actor b = Actor.Builder.construct()
                    .scene(scene)
                    .type(type)
                    .id(id)
                    .camera(camera)
                    .dirty(dirty)
                    .drawingPass(drawingPass)
                    .passNumber(passNumber)
                    .thickness(thickness)
                    .visible(visible)
                    .wire(wire)
                    .backgroundColor(backgroundColor)
                    .color(color)
                    .origin(origin)
                    .shape(shape)
                    .border(border)
                    .borderColor(borderColor)
                    .borderThickness(borderThickness)
                    .build();

            // test equality
            final boolean result = WeakEqualsHelper.weakEquals(a, b, ignore);
            Assert.assertTrue(WeakEqualsHelper.getErrors().toString(), result);
        }
    }
}
