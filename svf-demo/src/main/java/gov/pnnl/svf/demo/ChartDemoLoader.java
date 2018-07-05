package gov.pnnl.svf.demo;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.BorderedShapeActor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.color.ColorPalette;
import gov.pnnl.svf.core.geometry.Chart;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Chart2D;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Shape2D;
import gov.pnnl.svf.geometry.Shape3D;
import gov.pnnl.svf.physics.CollisionSupport;
import gov.pnnl.svf.physics.PhysicsEngine;
import gov.pnnl.svf.physics.PhysicsSupport;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ColorPickingSupportListener;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.CameraUtil;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.MemLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Demo loader for various chart types in a scene.
 *
 * @author Amelia Bleeker
 */
public class ChartDemoLoader implements DemoLoader {

    private static final double PADDING = 1.2;
    private static final int DATA_POINTS = 16;
    // immutable collection static for flyweight pattern
    private static final List<Color> COLORS = new ArrayList<>(25);

    // static constructor
    static {
        COLORS.add(new Color(120, 65, 65));
        COLORS.add(new Color(160, 75, 75));
        COLORS.add(new Color(175, 105, 80));
        COLORS.add(new Color(175, 125, 80));
        COLORS.add(new Color(175, 150, 80));
        COLORS.add(new Color(175, 165, 80));
        COLORS.add(new Color(160, 160, 75));
        COLORS.add(new Color(160, 175, 80));
        COLORS.add(new Color(135, 170, 80));
        COLORS.add(new Color(100, 140, 75));
        COLORS.add(new Color(85, 135, 80));
        COLORS.add(new Color(60, 130, 75));
        COLORS.add(new Color(55, 115, 80));
        COLORS.add(new Color(55, 125, 100));
        COLORS.add(new Color(75, 130, 115));
        COLORS.add(new Color(85, 150, 140));
        COLORS.add(new Color(80, 140, 150));
        COLORS.add(new Color(70, 100, 130));
        COLORS.add(new Color(60, 80, 110));
        COLORS.add(new Color(55, 60, 95));
        COLORS.add(new Color(90, 80, 115));
        COLORS.add(new Color(100, 65, 120));
        COLORS.add(new Color(150, 100, 140));
        COLORS.add(new Color(135, 85, 125));
        COLORS.add(new Color(120, 65, 95));
    }

    private final ColorPalette palette = new ColorPalette(COLORS);
    private final ColorPickingSupportListener listener = new ColorPickingSupportListenerImpl();
    private final Random random = new Random();

    /**
     * Constructor
     */
    public ChartDemoLoader() {
    }

    @Override
    public void load(final Scene scene) {
        // create a camera
        final Camera camera = scene.getFactory().createCamera(scene, DraggingCamera.class);
        camera.setLocation(new Vector3D(1.0, 0.0, 15.0));
        ColorSupport.newInstance(camera).setColor(Color.BLACK);
        scene.add(camera);
        // create a picking camera
        final PickingCamera picking = scene.getFactory().createPickingCamera(scene, camera, PickingCamera.class);
        scene.add(picking);
        // create an item picking camera
        final ItemPickingCamera itemPicking = scene.getFactory().createPickingCamera(scene, camera, ItemPickingCamera.class);
        scene.add(itemPicking);
        // create a color picking camera
        final ColorPickingCamera colorPicking = scene.getFactory().createPickingCamera(scene, camera, ColorPickingCamera.class);
        scene.add(colorPicking);
        // log the fps and vps
        if (!scene.getExtended().getSceneBuilder().isDebug()) {
            FpsLogger.newInstance(scene, 1000 * 10);
            PerfLogger.newInstance(scene, 1000 * 10);
            MemLogger.newInstance(scene, 1000 * 10);
        }
        // physics
        scene.setBoundary(new Vector3D(40.0, 40.0, 40.0));
        final PhysicsEngine physics = new PhysicsEngine(scene);
        scene.add(physics);
        PhysicsSupport.newInstance(camera);
        final CollisionSupport collision = CollisionSupport.newInstance(camera);
        collision.addCollisionType("scene");
        // data
        final List<Point2D> data = new ArrayList<>(DATA_POINTS);
        final List<Point2D> color = new ArrayList<>(DATA_POINTS);
        final List<Point2D> noColor = new ArrayList<>(DATA_POINTS);
        for (int i = 0; i < DATA_POINTS; i++) {
            final double x = random.nextDouble();
            final double y = random.nextDouble();
            data.add(new Point2D(x, y));
            color.add(new Point2D(palette.next().toInt(), y));
            noColor.add(new Point2D(Chart2D.NO_COLOR, y));
        }
        final Point2D[] dataArray = data.toArray(new Point2D[data.size()]);
        final Point2D[] colorArray = color.toArray(new Point2D[color.size()]);
        final Point2D[] noColorArray = noColor.toArray(new Point2D[noColor.size()]);
        // shapes
        // zero column
        newBorderedActor(scene, new Chart2D(-3.0 * PADDING, 2.0 * PADDING, Chart.PIE, colorArray))
                .setBorderThickness(0.02);
        newBorderedActor(scene, new Chart2D(-3.0 * PADDING, 1.0 * PADDING, Chart.SCATTER, dataArray))
                .setBorderThickness(0.02);
        newBorderedActor(scene, new Chart2D(-3.0 * PADDING, 0.0 * PADDING, Chart.LINE, dataArray))
                .setBorderThickness(0.02);
        newBorderedActor(scene, new Chart2D(-3.0 * PADDING, -1.0 * PADDING, Chart.AREA, dataArray))
                .setBorderThickness(0.02);
        newBorderedActor(scene, new Chart2D(-3.0 * PADDING, -2.0 * PADDING, Chart.BAR, colorArray))
                .setBorderThickness(0.02);
        // first column
        newActor(scene, new Chart2D(-2.0 * PADDING, 2.0 * PADDING, Chart.PIE, new Point2D(0.0, 0.0), new Point2D(1.0, 1.0), colorArray))
                .setThickness(1.0f);
        newActor(scene, new Chart2D(-2.0 * PADDING, 1.0 * PADDING, Chart.SCATTER, new Point2D(0.0, 0.0), new Point2D(1.0, 1.0), dataArray))
                .setThickness(5.0f);
        newActor(scene, new Chart2D(-2.0 * PADDING, 0.0 * PADDING, Chart.LINE, new Point2D(0.0, 0.0), new Point2D(1.0, 1.0), dataArray))
                .setThickness(3.0f);
        newActor(scene, new Chart2D(-2.0 * PADDING, -1.0 * PADDING, Chart.AREA, new Point2D(0.0, 0.0), new Point2D(1.0, 1.0), dataArray))
                .setThickness(1.0f);
        newActor(scene, new Chart2D(-2.0 * PADDING, -2.0 * PADDING, Chart.BAR, new Point2D(0.0, 0.0), new Point2D(1.0, 1.0), colorArray))
                .setThickness(1.0f);
        // second column
        newActor(scene, new Chart2D(-1.0 * PADDING, 2.0 * PADDING, Chart.PIE, noColorArray))
                .setThickness(1.0f);
        newActor(scene, new Chart2D(-1.0 * PADDING, 1.0 * PADDING, Chart.SCATTER, dataArray))
                .setThickness(5.0f);
        newActor(scene, new Chart2D(-1.0 * PADDING, 0.0 * PADDING, Chart.LINE, dataArray))
                .setThickness(3.0f);
        newActor(scene, new Chart2D(-1.0 * PADDING, -1.0 * PADDING, Chart.AREA, dataArray))
                .setThickness(1.0f);
        newActor(scene, new Chart2D(-1.0 * PADDING, -2.0 * PADDING, Chart.BAR, noColorArray))
                .setThickness(1.0f);
        // third column
        newActor(scene, new Chart2D(0.0 * PADDING, -1.0 * PADDING, Chart.PIE))
                .setThickness(7.0f);
        newActor(scene, new Chart2D(0.0 * PADDING, 1.0 * PADDING))
                .setThickness(7.0f);
        newActor(scene, Chart2D.ZERO)
                .setThickness(7.0f);
        newActor(scene, new Chart2D(0.0 * PADDING, -1.0 * PADDING, Chart.NONE))
                .setThickness(7.0f);
        newActor(scene, new Chart2D(0.0 * PADDING, -1.0 * PADDING, Chart.NONE, new Point2D(0.0, -0.5), new Point2D(1.0, 2.0), dataArray))
                .setThickness(7.0f);
    }

    private ShapeActor newActor(final Scene scene, final Shape shape) {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(shape);
        ColorSupport.newInstance(actor).setColor(palette.next());
        TransformSupport.newInstance(actor);
        ColorPickingSupport.newInstance(actor).addListener(listener);
        scene.add(actor);
        return actor;
    }

    private BorderedShapeActor newBorderedActor(final Scene scene, final Shape shape) {
        final BorderedShapeActor actor = new BorderedShapeActor(scene);
        actor.setShape(shape);
        actor.setBackgroundColor(Color.LIGHT_GRAY);
        actor.setBorderColor(Color.DARK_GRAY);
        actor.setBorderThickness(0.25);
        ColorSupport.newInstance(actor).setColor(palette.next());
        TransformSupport.newInstance(actor);
        ColorPickingSupport.newInstance(actor).addListener(listener);
        scene.add(actor);
        return actor;
    }

    protected class ColorPickingSupportListenerImpl implements ColorPickingSupportListener {

        protected ColorPickingSupportListenerImpl() {
        }

        @Override
        public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
            if (event.getTypes().contains(CameraEventType.HOVER)) {
                actor.getScene()
                        .getTooltip()
                        .showTooltip(actor instanceof ShapeActor ? String.valueOf(((ShapeActor) actor).getShape()) : actor.getType(), event.getX(),
                                     event.getY());
            }
            if (event.getTypes().containsAll(EnumSet.of(CameraEventType.LEFT, CameraEventType.SINGLE))) {
                final ColorSupport color = actor.lookup(ColorSupport.class);
                if (color != null) {
                    color.setColor(palette.next());
                }
            }
            if (event.getTypes().containsAll(EnumSet.of(CameraEventType.LEFT, CameraEventType.DOUBLE))) {
                final Shape shape = ((ShapeActor) actor).getShape();
                final DraggingCamera camera = actor.getScene().lookup(DraggingCamera.class);
                if (shape != null && camera != null) {
                    final TransformSupport transform = actor.lookup(TransformSupport.class);
                    if (transform != null) {
                        final double x;
                        final double y;
                        final double w;
                        final double h;
                        if (shape instanceof Shape2D) {
                            x = ((Shape2D) shape).getX();
                            y = ((Shape2D) shape).getY();
                            w = ((Shape2D) shape).getWidth();
                            h = ((Shape2D) shape).getHeight();
                        } else if (shape instanceof Shape3D) {
                            x = ((Shape3D) shape).getX();
                            y = ((Shape3D) shape).getY();
                            w = ((Shape3D) shape).getWidth();
                            h = ((Shape3D) shape).getHeight();
                        } else {
                            return;
                        }
                        // we need to account for the translation from the transform object
                        final Rectangle2D rectangle = new Rectangle2D(
                                x + transform.getTranslation().getX(),
                                y + transform.getTranslation().getY(),
                                w * transform.getScale().getX(),
                                h * transform.getScale().getY());
                        CameraUtil.fitInViewport(camera, rectangle, true);
                    } else {
                        CameraUtil.fitInViewport(camera, shape, true);
                    }
                }
            }
        }
    }

}
