package gov.pnnl.svf.demo;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.DynamicBorderedShapeActor;
import gov.pnnl.svf.actor.DynamicShapeActor;
import gov.pnnl.svf.animation.AbstractAnimationSupport;
import gov.pnnl.svf.animation.AnimationSupportListener;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.color.ColorPalette;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.geometry.Chart;
import gov.pnnl.svf.core.geometry.Path;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Arc2D;
import gov.pnnl.svf.geometry.Chart2D;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Path2D;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Polygon2D;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.RoundedRectangle2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Shape2D;
import gov.pnnl.svf.geometry.Shape3D;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.physics.CollisionSupport;
import gov.pnnl.svf.physics.PhysicsEngine;
import gov.pnnl.svf.physics.PhysicsSupport;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ColorPickingCameraAdapter;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ColorPickingSupportListener;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.ItemPickingSupport;
import gov.pnnl.svf.picking.ItemPickingSupportListener;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.picking.PickingCameraAdapter;
import gov.pnnl.svf.picking.PickingSupport;
import gov.pnnl.svf.picking.PickingSupportListener;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.texture.Texture2dSupport;
import gov.pnnl.svf.util.BundlingUtil;
import gov.pnnl.svf.util.CameraUtil;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.MemLogger;
import gov.pnnl.svf.util.PerfLogger;
import gov.pnnl.svf.util.TextUtil;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Demo loader for various chart types in a scene.
 *
 * @author Arthur Bleeker
 */
public class DynamicDemoLoader implements DemoLoader {

    private static final Logger logger = Logger.getLogger(DynamicDemoLoader.class.getName());
    private static final String TOOLTIP_ID = "apd-tooltip";
    private static final double PADDING = 1.2;
    private static final int DATA_POINTS = 16;
    private static final double OFFSET = 9.0;
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
    public DynamicDemoLoader() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(final Scene scene) {
        // create a camera
        final Camera camera = scene.getFactory().createCamera(scene, DraggingCamera.class);
        camera.setLocation(new Vector3D(5.0, 0.0, 30.0));
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
        final List<Point2D> chartData = new ArrayList<>(DATA_POINTS * 1000);
        for (int i = 0; i < DATA_POINTS * 1000; i++) {
            final double x = random.nextDouble();
            final double y = random.nextDouble();
            chartData.add(new Point2D(x, y));
        }
        final DynamicShapeActor chartActor = newActor(scene, new Chart2D(0.0 * PADDING, 4.0 * PADDING, Chart.AREA, chartData));
        chartActor.setOrigin(Alignment.CENTER);
        chartActor.setThickness(1.0f);
        chartActor.lookup(TransformSupport.class)
                .setScale(new Vector3D(10.0, 1.0, 1.0))
                .setTranslation(new Vector3D(5.0, 0.0, 0.0));
        final AtomicInteger iteration = new AtomicInteger(0);
        final AbstractAnimationSupport chartAnimation = new AbstractAnimationSupport(chartActor, 1000L, 0L, true) {

            @Override
            protected void animate(double fraction) {
                final boolean forward = iteration.get() % 2 == 0;
                fraction = forward ? fraction : 1.0 - fraction;
                final Point2D[] temp = chartData.toArray(new Point2D[chartData.size()]);
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = new Point2D(temp[i].getX(), temp[i].getY() + (fraction * temp[i].getX()));
                }
                chartActor.setShape(new Chart2D(0.0 * PADDING, 4.0 * PADDING, Chart.AREA, temp));
            }
        };
        chartAnimation.addListener(new AnimationSupportListener() {

            @Override
            public void iterationCompleted() {
                iteration.incrementAndGet();
            }

            @Override
            public void animationCompleted() {
                // no operation
            }
        });
        chartActor.add(chartAnimation);
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
        // shapes
        // zero column
        newBorderedActor(scene, new Rectangle2D(OFFSET + -3.0 * PADDING, 1.0 * PADDING, 0.5, 0.5));
        newBorderedActor(scene, new Circle2D(OFFSET + -3.0 * PADDING, 0.0 * PADDING, 0.25));
        newBorderedActor(scene, new Rectangle2D(OFFSET + -3.0 * PADDING, -1.0 * PADDING, 0.5, 0.5))
                .setBorder(Border.getBorder(true, false, false, true));
        // first column
        newActor(scene, new Rectangle2D(OFFSET + -2.0 * PADDING, 1.0 * PADDING, 0.5, 0.5));
        newActor(scene, new Rectangle2D(OFFSET + -2.0 * PADDING, 0.0 * PADDING, 1.0, 0.5));
        newActor(scene, new Rectangle2D(OFFSET + -2.0 * PADDING, -1.0 * PADDING, 0.5, 1.0));
        // second column
        newActor(scene, Arc2D.newInstance(OFFSET + -1.0 * PADDING, 1.0 * PADDING, 45.0, 180.0, 0.2, 0.5));
        newActor(scene, new Circle2D(OFFSET + -1.0 * PADDING, 0.0 * PADDING, 0.5));
        newActor(scene, Arc2D.newInstance(OFFSET + -1.0 * PADDING, -1.0 * PADDING, 45.0, 180.0, 0.0, 0.5));
        // third column
        newActor(scene, new Point2D(OFFSET + 0.0 * PADDING, 1.0 * PADDING))
                .setThickness(7.0f);
        newActor(scene, new Rectangle2D(OFFSET + Rectangle2D.ONE.getX(), Rectangle2D.ONE.getY(), Rectangle2D.ONE))
                .setThickness(7.0f);
        newActor(scene, new Rectangle2D(OFFSET + 0.0 * PADDING, -1.0 * PADDING, 0.0, 0.0))
                .setThickness(7.0f);
        // fourth column
        final List<Point2D> star = Arrays.asList(new Point2D(-0.3, -0.5),
                                                 new Point2D(0.0, 0.3),
                                                 new Point2D(0.3, -0.5),
                                                 new Point2D(-0.45, 0.0),
                                                 new Point2D(0.45, 0.0));
        newActor(scene, new Polygon2D(OFFSET + 1.0 * PADDING, 1.0 * PADDING, star));
        newActor(scene, new Polygon2D(OFFSET + 1.0 * PADDING, 0.0 * PADDING, star));
        newActor(scene, new Polygon2D(OFFSET + 1.0 * PADDING, -1.0 * PADDING, star));
        // fifth column
        final List<Point2D> s = Arrays.asList(new Point2D(0.5, 0.5),
                                              new Point2D(-0.5, 0.5),
                                              new Point2D(-0.5, 0.0),
                                              new Point2D(0.5, 0.0),
                                              new Point2D(0.5, -0.5),
                                              new Point2D(-0.5, -0.5));
        newActor(scene, new Path2D(OFFSET + 2.0 * PADDING, 1.0 * PADDING, s.toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(OFFSET + 2.0 * PADDING, 0.0 * PADDING, Path.BEZIER, s.toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(OFFSET + 2.0 * PADDING, 0.0 * PADDING, Path.POINTS, s.toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        newActor(scene, new Path2D(OFFSET + 2.0 * PADDING, -1.0 * PADDING, Path.NURBS, s.toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(OFFSET + 2.0 * PADDING, -1.0 * PADDING, Path.POINTS, s.toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        // sixth column
        newActor(scene, new Path2D(OFFSET + 3.0 * PADDING, 1.0 * PADDING, Path.NURBS, BundlingUtil.createBundledPoint2D(s, 0.75, true).toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(OFFSET + 3.0 * PADDING, 1.0 * PADDING, Path.POINTS, BundlingUtil.createBundledPoint2D(s, 0.75, true).toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        newActor(scene, new Path2D(OFFSET + 3.0 * PADDING, 0.0 * PADDING, Path.NURBS, BundlingUtil.createBundledPoint2D(s, 0.5, true).toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(OFFSET + 3.0 * PADDING, 0.0 * PADDING, Path.POINTS, BundlingUtil.createBundledPoint2D(s, 0.5, true).toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        newActor(scene, new Path2D(OFFSET + 3.0 * PADDING, -1.0 * PADDING, Path.NURBS, BundlingUtil.createBundledPoint2D(s, 0.0, true).toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(OFFSET + 3.0 * PADDING, -1.0 * PADDING, Path.POINTS, BundlingUtil.createBundledPoint2D(s, 0.0, true).toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        // seventh column
        newActor(scene, new RoundedRectangle2D(OFFSET + 4.0 * PADDING, 1.0 * PADDING, 1.0, 0.8, 0.1));
        newBorderedActor(scene, new RoundedRectangle2D(OFFSET + 4.0 * PADDING, 0.0 * PADDING, 0.5, 0.5, 0.1));
        newBorderedActor(scene, new RoundedRectangle2D(OFFSET + 4.0 * PADDING, -1.0 * PADDING, 0.75, 0.5, 1.0))
                .setBorder(Border.getBorder(true, false, false, true));
        // top text rows
        DynamicBorderedShapeActor bordered;
        bordered = newBorderedActor(scene, new Text3D(OFFSET + -3.0 * PADDING, 3.0 * PADDING, 0.0, new Font("Vivaldi", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setBorder(Border.TOP_LEFT);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(OFFSET + 0.0 * PADDING, 3.0 * PADDING, 0.0, new Font("Cambria", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setColor(Color.WHITE);
        bordered.setBackgroundColor(Color.GRAY);
        bordered.setBorder(Border.TOP);
        bordered.setBorderColor(Color.WHITE);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(OFFSET + 3.0 * PADDING, 3.0 * PADDING, 0.0, new Font("Calibri", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setBorder(Border.TOP_RIGHT);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(OFFSET + -3.0 * PADDING, 2.5 * PADDING, 0.0, new Font("Comic Sans MS", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setColor(Color.WHITE);
        bordered.setBackgroundColor(Color.GRAY);
        bordered.setBorder(Border.LEFT);
        bordered.setBorderColor(Color.WHITE);
        bordered.setBorderThickness(0.04f);
        final DynamicShapeActor actor = newActor(scene, new Text3D(OFFSET + 0.0 * PADDING, 2.5 * PADDING, 0.0, Text3D.DEFAULT_FONT, "Text3D Shape3DActor"));
        actor.setBackgroundColor(Color.WHITE);
        bordered = newBorderedActor(scene, new Text3D(OFFSET + 3.0 * PADDING, 2.5 * PADDING, 0.0, new Font("Lucida Console", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setColor(Color.WHITE);
        bordered.setBackgroundColor(Color.GRAY);
        bordered.setBorder(Border.RIGHT);
        bordered.setBorderColor(Color.WHITE);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(OFFSET + -3.0 * PADDING, 2.0 * PADDING, 0.0, new Font("Courier New", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setBorder(Border.BOTTOM_LEFT);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(OFFSET + 0.0 * PADDING, 2.0 * PADDING, 0.0, new Font("Georgia", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setColor(Color.WHITE);
        bordered.setBackgroundColor(Color.GRAY);
        bordered.setBorder(Border.BOTTOM);
        bordered.setBorderColor(Color.WHITE);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(OFFSET + 3.0 * PADDING, 2.0 * PADDING, 0.0,
                                                      new Font("Times New Roman", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()), "Text3D Shape3DActor"));
        bordered.setBorder(Border.BOTTOM_RIGHT);
        bordered.setBorderThickness(0.04f);
        // bottom row
        newBorderedActor(scene, Arc2D.newInstance(OFFSET + -3.0 * PADDING, -2.0 * PADDING, 45.0, 180.0, 0.2, 0.5))
                .setBorderThickness(0.04f);
        final Text3D text3D = new Text3D(OFFSET + 0.0 * PADDING, -2.0 * PADDING, 0.0, Text3D.DEFAULT_FONT, "Text3D Shape3DActor that will need to be truncated to fit.");
        bordered = newBorderedActor(scene, TextUtil.fitText(text3D, 6.0));
        bordered.setBorderThickness(0.04f);
        // test for jogl text implementations
        final Rectangle viewport = scene.getViewport();
        final ShapeDemoLoader.TextRendererActor jogl2d = new ShapeDemoLoader.TextRendererActor(scene);
        jogl2d.setDrawingPass(DrawingPass.INTERFACE);
        jogl2d.setOrigin(Alignment.RIGHT_BOTTOM);
        jogl2d.setShape(new Text2D(viewport.getWidth(), 0.0, "JOGL 2D Text"));
        scene.getPropertyChangeSupport().addPropertyChangeListener(Scene.VIEWPORT, new PropertyChangeListener() {

                                                               @Override
                                                               public void propertyChange(final PropertyChangeEvent evt) {
                                                                   final Rectangle viewport = (Rectangle) evt.getNewValue();
                                                                   jogl2d.setShape(new Text2D(viewport.getWidth(), 0.0, "JOGL 2D Text"));
                                                               }
                                                           });
        ColorSupport.newInstance(jogl2d).setColor(palette.next());
        TransformSupport.newInstance(jogl2d);
        ColorPickingSupport.newInstance(jogl2d).addListener(listener);
        scene.add(jogl2d);

        final ShapeDemoLoader.TextRendererActor jogl3d = new ShapeDemoLoader.TextRendererActor(scene);
        jogl3d.setShape(new Text3D(3.0 * PADDING, -2.0 * PADDING, 0.0, "JOGL 3D Text"));
        ColorSupport.newInstance(jogl3d).setColor(palette.next());
        TransformSupport.newInstance(jogl3d);
        ColorPickingSupport.newInstance(jogl3d).addListener(listener);
        scene.add(jogl3d);

        // create a tooltip
        final DynamicBorderedShapeActor tooltip = tooltipLoad(scene);
        final TransformSupport tooltipTransform = tooltip.lookup(TransformSupport.class);
        final ColorPickingCamera colorPickingCamera = scene.lookup(ColorPickingCamera.class);
        colorPickingCamera.addListener(new ColorPickingCameraAdapter() {
            @Override
            public void itemEntered(final Actor actor, final Object item, final PickingCameraEvent event) {
                if (item instanceof Quadrant) {
                    tooltipTransform.setTranslation(new Vector3D(event.getX() + 14, camera.getViewport().getHeight() - event.getY() - 20, 0.0));
                    tooltip.setShape(new Text2D(item.toString()));
                    tooltip.setVisible(true);
                }
            }

            @Override
            public void itemExited(final Actor actor, final Object item, final PickingCameraEvent event) {
                tooltip.setVisible(false);
            }

            @Override
            public void mouseMoved(final PickingCameraEvent event) {
                tooltipTransform.setTranslation(new Vector3D(event.getX() + 14, camera.getViewport().getHeight() - event.getY() - 20, 0.0));
            }
        });
        // ui picking
        final DynamicShapeActor uiPickingText = uiColorPickingLoad(scene);
        // standard picking
        final DynamicShapeActor pickingText = pickingLoad(scene);
        // item picking
        final DynamicShapeActor itemPickingText = itemPickingLoad(scene);
        // color picking
        final DynamicShapeActor colorPickingText = colorPickingLoad(scene);
        // listeners to reset the label text
        picking.addListener(new PickingCameraAdapter() {
            @Override
            public void nothingPicked(final PickingCameraEvent event) {
                pickingText.setShape(new Text3D("Picking"));
            }
        });
        itemPicking.addListener(new PickingCameraAdapter() {
            @Override
            public void nothingPicked(final PickingCameraEvent event) {
                itemPickingText.setShape(new Text3D("Item Picking"));
            }
        });
        colorPicking.addListener(new PickingCameraAdapter() {
            @Override
            public void nothingPicked(final PickingCameraEvent event) {
                uiPickingText.setShape(new Text2D("UI Color Picking"));
                colorPickingText.setShape(new Text3D("Color Picking"));
            }
        });
    }

    private DynamicShapeActor newActor(final Scene scene, final Shape shape) {
        final DynamicShapeActor actor = new DynamicShapeActor(scene);
        actor.setShape(shape);
        ColorSupport.newInstance(actor).setColor(palette.next());
        TransformSupport.newInstance(actor);
        ColorPickingSupport.newInstance(actor).addListener(listener);
        scene.add(actor);
        return actor;
    }

    private DynamicBorderedShapeActor newBorderedActor(final Scene scene, final Shape shape) {
        final DynamicBorderedShapeActor actor = new DynamicBorderedShapeActor(scene);
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

    private static DynamicBorderedShapeActor tooltipLoad(final Scene scene) {
        // create a tooltip
        final DynamicBorderedShapeActor tooltip = new DynamicBorderedShapeActor(scene, TOOLTIP_ID, TOOLTIP_ID);
        tooltip.setVisible(false);
        tooltip.setDrawingPass(DrawingPass.INTERFACE);
        tooltip.setPassNumber(1);
        tooltip.setOrigin(Alignment.LEFT_TOP);
        tooltip.setColor(Color.RED);
        tooltip.setBorderColor(Color.ORANGE);
        TransformSupport.newInstance(tooltip);
        scene.add(tooltip);
        // return the tooltip
        return tooltip;
    }

    private static DynamicShapeActor uiColorPickingLoad(final Scene scene) {
        // bordered text actor
        final DynamicShapeActor label = new DynamicShapeActor(scene);
        label.setOrigin(Alignment.LEFT_BOTTOM);
        label.setShape(new Text2D("UI Color Picking"));
        label.setDrawingPass(DrawingPass.INTERFACE);
        label.setPassNumber(0);
        ColorSupport.newInstance(label).setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        TransformSupport.newInstance(label).setTranslation(new Vector3D(175.0, 5.0, 0.0));
        scene.add(label);
        // plane
        final ItemPickingActor plane = new ItemPickingActor(scene);
        plane.setDrawingPass(DrawingPass.INTERFACE);
        plane.setPassNumber(0);
        ColorSupport.newInstance(plane).setColor(Color.GRAY);
        TransformSupport.newInstance(plane).setTranslation(new Vector3D(45.0, 45.0, 0.0)).setScale(new Vector3D(80.0, 80.0, 1.0));
        final ColorPickingSupport planePicking = ColorPickingSupport.newInstance(plane);
        planePicking.newMapping(Quadrant.TOP_LEFT);
        planePicking.newMapping(Quadrant.TOP_RIGHT);
        planePicking.newMapping(Quadrant.BOTTOM_LEFT);
        planePicking.newMapping(Quadrant.BOTTOM_RIGHT);
        planePicking.addListener(new ColorPickingSupportListener() {
            @Override
            public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
                final StringBuilder sb = new StringBuilder();
                for (final CameraEventType type : event.getTypes()) {
                    sb.append(type.toString());
                    sb.append(" | ");
                }
                for (final Object quadrant : items) {
                    sb.append(quadrant.toString());
                    sb.append(" | ");
                }
                sb.append("UI PLANE");
                label.setShape(new Text2D(sb.toString()));
            }
        });
        scene.add(plane);
        // texture
        final ItemPickingActor texture = new ItemPickingActor(scene);
        texture.setDrawingPass(DrawingPass.INTERFACE);
        texture.setPassNumber(0);
        ColorSupport.newInstance(texture).setColor(Color.WHITE);
        TransformSupport.newInstance(texture).setTranslation(new Vector3D(130.0, 45.0, 0.0)).setScale(new Vector3D(80.0, 80.0, 1.0));
        final URL url = ClassLoader.getSystemResource("gov/pnnl/svf/resources/PNNL_Color_Logo_Horizontal.png");
        if (url == null) {
            logger.log(Level.WARNING, "Unable to load the texture for the demo texture plane.");
        }
        Texture2dSupport.newInstance(texture, url);
        final ColorPickingSupport texturePicking = ColorPickingSupport.newInstance(texture);
        texturePicking.newMapping(Quadrant.TOP_LEFT);
        texturePicking.newMapping(Quadrant.TOP_RIGHT);
        texturePicking.newMapping(Quadrant.BOTTOM_LEFT);
        texturePicking.newMapping(Quadrant.BOTTOM_RIGHT);
        texturePicking.addListener(new ColorPickingSupportListener() {
            @Override
            public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
                final StringBuilder sb = new StringBuilder();
                for (final CameraEventType type : event.getTypes()) {
                    sb.append(type.toString());
                    sb.append(" | ");
                }
                for (final Object quadrant : items) {
                    sb.append(quadrant.toString());
                    sb.append(" | ");
                }
                sb.append("UI TEXTURE");
                label.setShape(new Text2D(sb.toString()));
            }
        });
        scene.add(texture);
        // return the label reference
        return label;
    }

    private static DynamicShapeActor pickingLoad(final Scene scene) {
        // bordered text actor
        final DynamicShapeActor label = new DynamicShapeActor(scene);
        label.setOrigin(Alignment.LEFT);
        label.setShape(new Text3D("Picking"));
        label.setDrawingPass(DrawingPass.SCENE);
        label.setPassNumber(0);
        ColorSupport.newInstance(label).setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        TransformSupport.newInstance(label).setTranslation(new Vector3D(2.7, 2.0, 0.0)).setScale(new Vector3D(1.0, 1.0, 1.0));
        scene.add(label);
        // plane
        final DynamicShapeActor plane = new DynamicShapeActor(scene);
        plane.setShape(Rectangle2D.ONE);
        plane.setDrawingPass(DrawingPass.SCENE);
        plane.setPassNumber(1);
        ColorSupport.newInstance(plane).setColor(Color.GRAY);
        TransformSupport.newInstance(plane).setTranslation(new Vector3D(1.0, 2.0, 0.0));
        PickingSupport.newInstance(plane).addListener(new PickingSupportListener() {
            @Override
            public void picked(final Actor actor, final PickingCameraEvent event) {
                final StringBuilder sb = new StringBuilder();
                for (final CameraEventType type : event.getTypes()) {
                    sb.append(type.toString());
                    sb.append(" | ");
                }
                sb.append("PLANE");
                label.setShape(new Text3D(sb.toString()));
                // zoom to actor
                if (event.getTypes().containsAll(EnumSet.<CameraEventType>of(CameraEventType.LEFT, CameraEventType.DOUBLE))) {
                    final DraggingCamera camera = scene.lookup(DraggingCamera.class);
                    if (camera != null) {
                        final TransformSupport transform = actor.lookup(TransformSupport.class);
                        if (transform != null) {
                            final Rectangle2D bounds = new Rectangle2D(transform.getTranslation().getX(),
                                                                       transform.getTranslation().getY(),
                                                                       transform.getScale().getX(),
                                                                       transform.getScale().getY());
                            CameraUtil.fitInViewport(camera, bounds, true);
                        }
                    }
                }
            }
        });
        scene.add(plane);
        // texture
        final ItemPickingActor texture = new ItemPickingActor(scene);
        texture.setDrawingPass(DrawingPass.SCENE);
        texture.setPassNumber(1);
        ColorSupport.newInstance(texture).setColor(Color.WHITE);
        TransformSupport.newInstance(texture).setTranslation(new Vector3D(2.1, 2.0, 0.0));
        final URL url = ClassLoader.getSystemResource("gov/pnnl/svf/resources/PNNL_Color_Logo_Horizontal.png");
        if (url == null) {
            logger.log(Level.WARNING, "Unable to load the texture for the demo texture plane.");
        }
        Texture2dSupport.newInstance(texture, url);
        PickingSupport.newInstance(texture).addListener(new PickingSupportListener() {
            @Override
            public void picked(final Actor actor, final PickingCameraEvent event) {
                final StringBuilder sb = new StringBuilder();
                for (final CameraEventType type : event.getTypes()) {
                    sb.append(type.toString());
                    sb.append(" | ");
                }
                sb.append("TEXTURE");
                label.setShape(new Text3D(sb.toString()));
                // zoom to actor
                if (event.getTypes().containsAll(EnumSet.<CameraEventType>of(CameraEventType.LEFT, CameraEventType.DOUBLE))) {
                    final DraggingCamera camera = scene.lookup(DraggingCamera.class);
                    if (camera != null) {
                        final TransformSupport transform = actor.lookup(TransformSupport.class);
                        if (transform != null) {
                            final Rectangle2D bounds = new Rectangle2D(transform.getTranslation().getX(),
                                                                       transform.getTranslation().getY(),
                                                                       transform.getScale().getX(),
                                                                       transform.getScale().getY());
                            CameraUtil.fitInViewport(camera, bounds, true);
                        }
                    }
                }
            }
        });
        scene.add(texture);
        // return the label reference
        return label;
    }

    private static DynamicShapeActor itemPickingLoad(final Scene scene) {
        // bordered text actor
        final DynamicShapeActor label = new DynamicShapeActor(scene);
        label.setOrigin(Alignment.LEFT);
        label.setShape(new Text3D("Item Picking"));
        label.setDrawingPass(DrawingPass.SCENE);
        label.setPassNumber(0);
        ColorSupport.newInstance(label).setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        TransformSupport.newInstance(label).setTranslation(new Vector3D(2.7, 0.0, 0.0)).setScale(new Vector3D(1.0, 1.0, 1.0));
        scene.add(label);
        // plane
        final ItemPickingActor plane = new ItemPickingActor(scene);
        plane.setDrawingPass(DrawingPass.SCENE);
        plane.setPassNumber(1);
        ColorSupport.newInstance(plane).setColor(Color.GRAY);
        TransformSupport.newInstance(plane).setTranslation(new Vector3D(1.0, 0.0, 0.0));
        final ItemPickingSupport planePicking = ItemPickingSupport.newInstance(plane);
        planePicking.getItems().add(Quadrant.TOP_LEFT);
        planePicking.getItems().add(Quadrant.TOP_RIGHT);
        planePicking.getItems().add(Quadrant.BOTTOM_LEFT);
        planePicking.getItems().add(Quadrant.BOTTOM_RIGHT);
        planePicking.addListener(new ItemPickingSupportListener() {
            @Override
            public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
                final StringBuilder sb = new StringBuilder();
                for (final CameraEventType type : event.getTypes()) {
                    sb.append(type.toString());
                    sb.append(" | ");
                }
                for (final Object quadrant : items) {
                    sb.append(quadrant.toString());
                    sb.append(" | ");
                }
                sb.append("PLANE");
                label.setShape(new Text3D(sb.toString()));
            }
        });
        scene.add(plane);
        // texture
        final ItemPickingActor texture = new ItemPickingActor(scene);
        texture.setDrawingPass(DrawingPass.SCENE);
        texture.setPassNumber(1);
        ColorSupport.newInstance(texture).setColor(Color.WHITE);
        TransformSupport.newInstance(texture).setTranslation(new Vector3D(2.1, 0.0, 0.0));
        final URL url = ClassLoader.getSystemResource("gov/pnnl/svf/resources/PNNL_Color_Logo_Horizontal.png");
        if (url == null) {
            logger.log(Level.WARNING, "Unable to load the texture for the demo texture plane.");
        }
        Texture2dSupport.newInstance(texture, url);
        final ItemPickingSupport texturePicking = ItemPickingSupport.newInstance(texture);
        texturePicking.getItems().add(Quadrant.TOP_LEFT);
        texturePicking.getItems().add(Quadrant.TOP_RIGHT);
        texturePicking.getItems().add(Quadrant.BOTTOM_LEFT);
        texturePicking.getItems().add(Quadrant.BOTTOM_RIGHT);
        texturePicking.addListener(new ItemPickingSupportListener() {
            @Override
            public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
                final StringBuilder sb = new StringBuilder();
                for (final CameraEventType type : event.getTypes()) {
                    sb.append(type.toString());
                    sb.append(" | ");
                }
                for (final Object quadrant : items) {
                    sb.append(quadrant.toString());
                    sb.append(" | ");
                }
                sb.append("TEXTURE");
                label.setShape(new Text3D(sb.toString()));
            }
        });
        scene.add(texture);
        // return the label reference
        return label;
    }

    private static DynamicShapeActor colorPickingLoad(final Scene scene) {
        // bordered text actor
        final DynamicShapeActor label = new DynamicShapeActor(scene);
        label.setOrigin(Alignment.LEFT);
        label.setShape(new Text3D("Color Picking"));
        label.setDrawingPass(DrawingPass.SCENE);
        label.setPassNumber(0);
        ColorSupport.newInstance(label).setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        TransformSupport.newInstance(label).setTranslation(new Vector3D(2.7, -2.0, 0.0)).setScale(new Vector3D(1.0, 1.0, 1.0));
        scene.add(label);
        // plane
        final ItemPickingActor plane = new ItemPickingActor(scene);
        plane.setDrawingPass(DrawingPass.SCENE);
        plane.setPassNumber(1);
        ColorSupport.newInstance(plane).setColor(Color.GRAY);
        TransformSupport.newInstance(plane).setTranslation(new Vector3D(1.0, -2.0, 0.0));
        final ColorPickingSupport planePicking = ColorPickingSupport.newInstance(plane);
        planePicking.newMapping(Quadrant.TOP_LEFT);
        planePicking.newMapping(Quadrant.TOP_RIGHT);
        planePicking.newMapping(Quadrant.BOTTOM_LEFT);
        planePicking.newMapping(Quadrant.BOTTOM_RIGHT);
        planePicking.addListener(new ColorPickingSupportListener() {
            @Override
            public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
                final StringBuilder sb = new StringBuilder();
                for (final CameraEventType type : event.getTypes()) {
                    sb.append(type.toString());
                    sb.append(" | ");
                }
                for (final Object quadrant : items) {
                    sb.append(quadrant.toString());
                    sb.append(" | ");
                }
                sb.append("PLANE");
                label.setShape(new Text3D(sb.toString()));
            }
        });
        scene.add(plane);
        // texture
        final ItemPickingActor texture = new ItemPickingActor(scene);
        texture.setDrawingPass(DrawingPass.SCENE);
        texture.setPassNumber(1);
        ColorSupport.newInstance(texture).setColor(Color.WHITE);
        TransformSupport.newInstance(texture).setTranslation(new Vector3D(2.1, -2.0, 0.0));
        final URL url = ClassLoader.getSystemResource("gov/pnnl/svf/resources/PNNL_Color_Logo_Horizontal.png");
        if (url == null) {
            logger.log(Level.WARNING, "Unable to load the texture for the demo texture plane.");
        }
        Texture2dSupport.newInstance(texture, url);
        final ColorPickingSupport texturePicking = ColorPickingSupport.newInstance(texture);
        texturePicking.newMapping(Quadrant.TOP_LEFT);
        texturePicking.newMapping(Quadrant.TOP_RIGHT);
        texturePicking.newMapping(Quadrant.BOTTOM_LEFT);
        texturePicking.newMapping(Quadrant.BOTTOM_RIGHT);
        texturePicking.addListener(new ColorPickingSupportListener() {
            @Override
            public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
                final StringBuilder sb = new StringBuilder();
                for (final CameraEventType type : event.getTypes()) {
                    sb.append(type.toString());
                    sb.append(" | ");
                }
                for (final Object quadrant : items) {
                    sb.append(quadrant.toString());
                    sb.append(" | ");
                }
                sb.append("TEXTURE");
                label.setShape(new Text3D(sb.toString()));
            }
        });
        scene.add(texture);
        // return the label reference
        return label;
    }

    protected class ColorPickingSupportListenerImpl implements ColorPickingSupportListener {

        protected ColorPickingSupportListenerImpl() {
        }

        @Override
        public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
            if (event.getTypes().contains(CameraEventType.HOVER)) {
                actor.getScene()
                        .getTooltip()
                        .showTooltip(actor instanceof DynamicShapeActor ? String.valueOf(((DynamicShapeActor) actor).getShape()) : actor.getType(), event.getX(),
                                     event.getY());
            }
            if (event.getTypes().containsAll(EnumSet.of(CameraEventType.LEFT, CameraEventType.SINGLE))) {
                final ColorSupport color = actor.lookup(ColorSupport.class);
                if (color != null) {
                    color.setColor(palette.next());
                }
            }
            if (event.getTypes().containsAll(EnumSet.of(CameraEventType.LEFT, CameraEventType.DOUBLE))) {
                final Shape shape = ((DynamicShapeActor) actor).getShape();
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
