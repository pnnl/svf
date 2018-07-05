package gov.pnnl.svf.demo;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.awt.TextRenderer;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.BorderedShapeActor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.color.ColorPalette;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.geometry.Path;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Arc2D;
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
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ColorPickingSupportListener;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.text.TextService;
import gov.pnnl.svf.util.BundlingUtil;
import gov.pnnl.svf.util.CameraUtil;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.LayoutUtil;
import gov.pnnl.svf.util.MemLogger;
import gov.pnnl.svf.util.PerfLogger;
import gov.pnnl.svf.util.TextUtil;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Demo loader for various shape types in a scene.
 *
 * @author Amelia Bleeker
 */
public class ShapeDemoLoader implements DemoLoader {

    private static final double PADDING = 1.2;
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

    /**
     * Constructor
     */
    public ShapeDemoLoader() {
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
        // shapes
        // zero column
        newBorderedActor(scene, new Rectangle2D(-3.0 * PADDING, 1.0 * PADDING, 0.5, 0.5));
        newBorderedActor(scene, new Circle2D(-3.0 * PADDING, 0.0 * PADDING, 0.25));
        newBorderedActor(scene, new Rectangle2D(-3.0 * PADDING, -1.0 * PADDING, 0.5, 0.5))
                .setBorder(Border.getBorder(true, false, false, true));
        // first column
        newActor(scene, new Rectangle2D(-2.0 * PADDING, 1.0 * PADDING, 0.5, 0.5));
        newActor(scene, new Rectangle2D(-2.0 * PADDING, 0.0 * PADDING, 1.0, 0.5));
        newActor(scene, new Rectangle2D(-2.0 * PADDING, -1.0 * PADDING, 0.5, 1.0));
        // second column
        newActor(scene, Arc2D.newInstance(-1.0 * PADDING, 1.0 * PADDING, 45.0, 180.0, 0.2, 0.5));
        newActor(scene, new Circle2D(-1.0 * PADDING, 0.0 * PADDING, 0.5));
        newActor(scene, Arc2D.newInstance(-1.0 * PADDING, -1.0 * PADDING, 45.0, 180.0, 0.0, 0.5));
        // third column
        newActor(scene, new Point2D(0.0 * PADDING, 1.0 * PADDING))
                .setThickness(7.0f);
        newActor(scene, Rectangle2D.ONE)
                .setThickness(7.0f);
        newActor(scene, new Rectangle2D(0.0 * PADDING, -1.0 * PADDING, 0.0, 0.0))
                .setThickness(7.0f);
        // fourth column
        final List<Point2D> star = Arrays.asList(new Point2D(-0.3, -0.5),
                                                 new Point2D(0.0, 0.3),
                                                 new Point2D(0.3, -0.5),
                                                 new Point2D(-0.45, 0.0),
                                                 new Point2D(0.45, 0.0));
        newActor(scene, new Polygon2D(1.0 * PADDING, 1.0 * PADDING, star));
        newActor(scene, new Polygon2D(1.0 * PADDING, 0.0 * PADDING, star));
        newActor(scene, new Polygon2D(1.0 * PADDING, -1.0 * PADDING, star));
        // fifth column
        final List<Point2D> s = Arrays.asList(new Point2D(0.5, 0.5),
                                              new Point2D(-0.5, 0.5),
                                              new Point2D(-0.5, 0.0),
                                              new Point2D(0.5, 0.0),
                                              new Point2D(0.5, -0.5),
                                              new Point2D(-0.5, -0.5));
        newActor(scene, new Path2D(2.0 * PADDING, 1.0 * PADDING, s.toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(2.0 * PADDING, 0.0 * PADDING, Path.BEZIER, s.toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(2.0 * PADDING, 0.0 * PADDING, Path.POINTS, s.toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        newActor(scene, new Path2D(2.0 * PADDING, -1.0 * PADDING, Path.NURBS, s.toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(2.0 * PADDING, -1.0 * PADDING, Path.POINTS, s.toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        // sixth column
        newActor(scene, new Path2D(3.0 * PADDING, 1.0 * PADDING, Path.NURBS, BundlingUtil.createBundledPoint2D(s, 0.75, true).toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(3.0 * PADDING, 1.0 * PADDING, Path.POINTS, BundlingUtil.createBundledPoint2D(s, 0.75, true).toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        newActor(scene, new Path2D(3.0 * PADDING, 0.0 * PADDING, Path.NURBS, BundlingUtil.createBundledPoint2D(s, 0.5, true).toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(3.0 * PADDING, 0.0 * PADDING, Path.POINTS, BundlingUtil.createBundledPoint2D(s, 0.5, true).toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        newActor(scene, new Path2D(3.0 * PADDING, -1.0 * PADDING, Path.NURBS, BundlingUtil.createBundledPoint2D(s, 0.0, true).toArray(new Point2D[s.size()])))
                .setThickness(3.0f);
        newActor(scene, new Path2D(3.0 * PADDING, -1.0 * PADDING, Path.POINTS, BundlingUtil.createBundledPoint2D(s, 0.0, true).toArray(new Point2D[s.size()])))
                .setThickness(5.0f);
        // seventh column
        newActor(scene, new RoundedRectangle2D(4.0 * PADDING, 1.0 * PADDING, 1.0, 0.8, 0.1));
        newBorderedActor(scene, new RoundedRectangle2D(4.0 * PADDING, 0.0 * PADDING, 0.5, 0.5, 0.1));
        newBorderedActor(scene, new RoundedRectangle2D(4.0 * PADDING, -1.0 * PADDING, 0.75, 0.5, 1.0))
                .setBorder(Border.getBorder(true, false, false, true));
        // top text rows
        BorderedShapeActor bordered;
        bordered = newBorderedActor(scene, new Text3D(-3.0 * PADDING, 3.0 * PADDING, 0.0, new Font("Vivaldi", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setBorder(Border.TOP_LEFT);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(0.0 * PADDING, 3.0 * PADDING, 0.0, new Font("Cambria", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setColor(Color.WHITE);
        bordered.setBackgroundColor(Color.GRAY);
        bordered.setBorder(Border.TOP);
        bordered.setBorderColor(Color.WHITE);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(3.0 * PADDING, 3.0 * PADDING, 0.0, new Font("Calibri", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setBorder(Border.TOP_RIGHT);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(-3.0 * PADDING, 2.5 * PADDING, 0.0, new Font("Comic Sans MS", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setColor(Color.WHITE);
        bordered.setBackgroundColor(Color.GRAY);
        bordered.setBorder(Border.LEFT);
        bordered.setBorderColor(Color.WHITE);
        bordered.setBorderThickness(0.04f);
        final ShapeActor actor = newActor(scene, new Text3D(0.0 * PADDING, 2.5 * PADDING, 0.0, Text3D.DEFAULT_FONT, "Text3D Shape3DActor"));
        actor.setBackgroundColor(Color.WHITE);
        bordered = newBorderedActor(scene, new Text3D(3.0 * PADDING, 2.5 * PADDING, 0.0, new Font("Lucida Console", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setColor(Color.WHITE);
        bordered.setBackgroundColor(Color.GRAY);
        bordered.setBorder(Border.RIGHT);
        bordered.setBorderColor(Color.WHITE);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(-3.0 * PADDING, 2.0 * PADDING, 0.0, new Font("Courier New", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setBorder(Border.BOTTOM_LEFT);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(0.0 * PADDING, 2.0 * PADDING, 0.0, new Font("Georgia", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()),
                                                      "Text3D Shape3DActor"));
        bordered.setColor(Color.WHITE);
        bordered.setBackgroundColor(Color.GRAY);
        bordered.setBorder(Border.BOTTOM);
        bordered.setBorderColor(Color.WHITE);
        bordered.setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, new Text3D(3.0 * PADDING, 2.0 * PADDING, 0.0,
                                                      new Font("Times New Roman", Font.PLAIN, Text3D.DEFAULT_FONT.getSize()), "Text3D Shape3DActor"));
        bordered.setBorder(Border.BOTTOM_RIGHT);
        bordered.setBorderThickness(0.04f);
        // bottom row
        newBorderedActor(scene, Arc2D.newInstance(-3.0 * PADDING, -2.0 * PADDING, 45.0, 180.0, 0.2, 0.5))
                .setBorderThickness(0.04f);
        bordered = newBorderedActor(scene, TextUtil.fitText(
                                    new Text3D(0.0 * PADDING, -2.0 * PADDING, 0.0, Text3D.DEFAULT_FONT, "Text3D Shape3DActor that will need to be truncated to fit."), 6.0));
        bordered.setBorderThickness(0.04f);
        // test for jogl text implementations
        final Rectangle viewport = scene.getViewport();
        final TextRendererActor jogl2d = new TextRendererActor(scene);
        jogl2d.setDrawingPass(DrawingPass.INTERFACE);
        jogl2d.setOrigin(Alignment.RIGHT_BOTTOM);
        jogl2d.setShape(new Text2D(viewport.getWidth(), 0.0, "JOGL 2D Text"));
        scene.getPropertyChangeSupport().addPropertyChangeListener(Scene.VIEWPORT, (final PropertyChangeEvent evt) -> {
                                                               final Rectangle viewport1 = (Rectangle) evt.getNewValue();
                                                               jogl2d.setShape(new Text2D(viewport1.getWidth(), 0.0, "JOGL 2D Text"));
                                                           });
        ColorSupport.newInstance(jogl2d).setColor(palette.next());
        TransformSupport.newInstance(jogl2d);
        ColorPickingSupport.newInstance(jogl2d).addListener(listener);
        scene.add(jogl2d);

        final TextRendererActor jogl3d = new TextRendererActor(scene);
        jogl3d.setShape(new Text3D(3.0 * PADDING, -2.0 * PADDING, 0.0, "JOGL 3D Text"));
        ColorSupport.newInstance(jogl3d).setColor(palette.next());
        TransformSupport.newInstance(jogl3d);
        ColorPickingSupport.newInstance(jogl3d).addListener(listener);
        scene.add(jogl3d);
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
        actor.setBorderColor(Color.DARK_GRAY);
        actor.setBorderThickness(0.25);
        ColorSupport.newInstance(actor).setColor(palette.next());
        TransformSupport.newInstance(actor);
        ColorPickingSupport.newInstance(actor).addListener(listener);
        scene.add(actor);
        return actor;
    }

    protected static class TextRendererActor extends ShapeActor {

        private final TextRenderer renderer2D;
        private final TextRenderer renderer3D;

        protected TextRendererActor(final Scene scene) {
            super(scene, "text-renderer", scene.getFactory().newUuid(scene));
            final TextService textService = scene.lookup(TextService.class);
            renderer3D = new TextRenderer(Text3D.DEFAULT_FONT,
                                          textService.isAntiAliased(),
                                          textService.isFractional(),
                                          null,
                                          textService.isMipMaps());
            renderer3D.setSmoothing(textService.isSmoothing());
            renderer2D = new TextRenderer(Text2D.DEFAULT_FONT,
                                          textService.isAntiAliased(),
                                          textService.isFractional(),
                                          null,
                                          textService.isMipMaps());
            renderer2D.setSmoothing(textService.isSmoothing());
        }

        @Override
        public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
            final Shape s = getShape();
            if (s instanceof Text2D) {
                final Text2D text = (Text2D) s;
                final Point2D offset = LayoutUtil.findOrigin(text, getOrigin());
                final Rectangle viewport = getScene().getViewport();
                setColor(gl, renderer2D);
                renderer2D.beginRendering(viewport.getWidth(), viewport.getHeight());
                renderer2D.draw(text.getText(),
                                (int) (offset.getX() - text.getWidth() / 2.0),
                                (int) (offset.getY() - text.getHeight() / 2.0));
                renderer2D.endRendering();
            } else if (s instanceof Text3D) {
                final Text3D text = (Text3D) s;
                final Point2D offset = LayoutUtil.findOrigin(text, getOrigin());
                setColor(gl, renderer3D);
                renderer3D.begin3DRendering();
                renderer3D.draw3D(text.getText(),
                                  (float) (text.getX() - text.getWidth() / 2.0 + offset.getX()),
                                  (float) (text.getY() - text.getHeight() / 2.0 + offset.getY()),
                                  (float) (text.getZ() - text.getDepth() / 2.0),
                                  (float) Text3D.TEXT_SCALE);
                renderer3D.end3DRendering();
            }
        }

        @Override
        public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
            // no operation
        }

        @Override
        public void initializeLists(final GL2 gl, final GLUgl2 glu) {
            // no operation
            count = 0;
            list = INITIALIZED;
        }

        private void setColor(final GL2 gl, final TextRenderer tr) {
            final Color c = getColor();
            if (c != null) {
                tr.setColor(c.toAwtColor());
            } else {
                final float[] a = new float[4];
                gl.glGetFloatv(GL2ES1.GL_CURRENT_COLOR, a, 0);
                tr.setColor(a[0], a[1], a[2], a[3]);
            }
        }

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
