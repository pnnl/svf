package gov.pnnl.svf.demo;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.BorderedShapeActor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle2D;
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
import gov.pnnl.svf.service.CameraCursorListener;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.texture.Texture2dSupport;
import gov.pnnl.svf.util.CameraUtil;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.MemLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.net.URL;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Demo loader for various picking camera types in a scene.
 *
 * @author Arthur Bleeker
 */
public class PickingDemoLoader implements DemoLoader {

    private static final Logger logger = Logger.getLogger(PickingDemoLoader.class.toString());
    private static final String TOOLTIP_ID = "apd-tooltip";

    /**
     * Constructor
     */
    public PickingDemoLoader() {
    }

    @Override
    public void load(final Scene scene) {
        // create a camera
        final DraggingCamera camera = scene.getFactory().createCamera(scene, DraggingCamera.class);
        camera.setLocation(new Vector3D(1.0, 0.0, 15.0));
        ColorSupport.newInstance(camera).setColor(Color.BLACK);
        scene.add(camera);
        // cursor support
        final CursorService cursorService = scene.getFactory().createService(scene, CursorService.class);
        cursorService.initialize();
        CameraCursorListener.newInstance(cursorService, camera);
        scene.add(cursorService);
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
        // create a tooltip
        final BorderedShapeActor tooltip = PickingDemoLoader.tooltipLoad(scene);
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
        final ShapeActor uiPickingText = PickingDemoLoader.uiColorPickingLoad(scene);
        // standard picking
        final ShapeActor pickingText = PickingDemoLoader.pickingLoad(scene);
        // item picking
        final ShapeActor itemPickingText = PickingDemoLoader.itemPickingLoad(scene);
        // color picking
        final ShapeActor colorPickingText = PickingDemoLoader.colorPickingLoad(scene);
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

    private static BorderedShapeActor tooltipLoad(final Scene scene) {
        // create a tooltip
        final BorderedShapeActor tooltip = new BorderedShapeActor(scene, TOOLTIP_ID, TOOLTIP_ID);
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

    private static ShapeActor uiColorPickingLoad(final Scene scene) {
        // bordered text actor
        final ShapeActor label = new ShapeActor(scene);
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

    private static ShapeActor pickingLoad(final Scene scene) {
        // bordered text actor
        final ShapeActor label = new ShapeActor(scene);
        label.setOrigin(Alignment.LEFT);
        label.setShape(new Text3D("Picking"));
        label.setDrawingPass(DrawingPass.SCENE);
        label.setPassNumber(0);
        ColorSupport.newInstance(label).setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        TransformSupport.newInstance(label).setTranslation(new Vector3D(2.0, 2.0, 0.0)).setScale(new Vector3D(1.0, 1.0, 1.0));
        scene.add(label);
        // plane
        final ShapeActor plane = new ShapeActor(scene);
        plane.setShape(Rectangle2D.ONE);
        plane.setDrawingPass(DrawingPass.SCENE);
        plane.setPassNumber(1);
        ColorSupport.newInstance(plane).setColor(Color.GRAY);
        TransformSupport.newInstance(plane).setTranslation(new Vector3D(-2.0, 2.0, 0.0));
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
        TransformSupport.newInstance(texture).setTranslation(new Vector3D(0.0, 2.0, 0.0));
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

    private static ShapeActor itemPickingLoad(final Scene scene) {
        // bordered text actor
        final ShapeActor label = new ShapeActor(scene);
        label.setOrigin(Alignment.LEFT);
        label.setShape(new Text3D("Item Picking"));
        label.setDrawingPass(DrawingPass.SCENE);
        label.setPassNumber(0);
        ColorSupport.newInstance(label).setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        TransformSupport.newInstance(label).setTranslation(new Vector3D(2.0, 0.0, 0.0)).setScale(new Vector3D(1.0, 1.0, 1.0));
        scene.add(label);
        // plane
        final ItemPickingActor plane = new ItemPickingActor(scene);
        plane.setDrawingPass(DrawingPass.SCENE);
        plane.setPassNumber(1);
        ColorSupport.newInstance(plane).setColor(Color.GRAY);
        TransformSupport.newInstance(plane).setTranslation(new Vector3D(-2.0, 0.0, 0.0));
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
        TransformSupport.newInstance(texture).setTranslation(new Vector3D(0.0, 0.0, 0.0));
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

    private static ShapeActor colorPickingLoad(final Scene scene) {
        // bordered text actor
        final ShapeActor label = new ShapeActor(scene);
        label.setOrigin(Alignment.LEFT);
        label.setShape(new Text3D("Color Picking"));
        label.setDrawingPass(DrawingPass.SCENE);
        label.setPassNumber(0);
        ColorSupport.newInstance(label).setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        TransformSupport.newInstance(label).setTranslation(new Vector3D(2.0, -2.0, 0.0)).setScale(new Vector3D(1.0, 1.0, 1.0));
        scene.add(label);
        // plane
        final ItemPickingActor plane = new ItemPickingActor(scene);
        plane.setDrawingPass(DrawingPass.SCENE);
        plane.setPassNumber(1);
        ColorSupport.newInstance(plane).setColor(Color.GRAY);
        TransformSupport.newInstance(plane).setTranslation(new Vector3D(-2.0, -2.0, 0.0));
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
        TransformSupport.newInstance(texture).setTranslation(new Vector3D(0.0, -2.0, 0.0));
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
}
