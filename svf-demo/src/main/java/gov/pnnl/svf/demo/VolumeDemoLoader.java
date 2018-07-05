package gov.pnnl.svf.demo;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.actor.VolumeActor;
import gov.pnnl.svf.animation.RotationAnimationSupport;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.color.ColorPalette;
import gov.pnnl.svf.core.texture.TextureType;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Shape2D;
import gov.pnnl.svf.geometry.Shape3D;
import gov.pnnl.svf.geometry.Volume3D;
import gov.pnnl.svf.physics.CollisionSupport;
import gov.pnnl.svf.physics.PhysicsEngine;
import gov.pnnl.svf.physics.PhysicsSupport;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ColorPickingSupportListener;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ClippingPlaneSupport;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.texture.Texture3dSupport;
import gov.pnnl.svf.util.CameraUtil;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.MemLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Demo loader for various shape types in a scene.
 *
 * @author Amelia Bleeker
 */
public class VolumeDemoLoader implements DemoLoader {

    private static final int VOLUME_SIZE = 8;
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
    private final Random random = new Random(0L);
    private final ColorPickingSupportListener listener = new ColorPickingSupportListenerImpl();

    /**
     * Constructor
     */
    public VolumeDemoLoader() {
    }

    @Override
    public void load(final Scene scene) {
        // create a camera
        final Camera camera = scene.getFactory().createCamera(scene, OrbitCamera.class);
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
        // volume actor
        final ShapeActor volume = newVolumeActor(scene);
        RotationAnimationSupport.newInstance(volume, 30L * 1000L, 0L, true, 360.0);
    }

    private ShapeActor newVolumeActor(final Scene scene) {
        // volume
        final VolumeActor volume = new VolumeActor(scene);
        volume.setShape(new Volume3D(5.0, 4.0, 3.0, VOLUME_SIZE));
        ColorSupport.newInstance(volume).setColor(Color.WHITE);
        TransformSupport.newInstance(volume).setRotationAxis(Vector3D.PLUS_J);
        // create a gradient 3d texture
        final ByteBuffer data = ByteBuffer.allocateDirect(VOLUME_SIZE * VOLUME_SIZE * VOLUME_SIZE * 4);
        for (int x = 0; x < VOLUME_SIZE; x++) {
            for (int y = 0; y < VOLUME_SIZE; y++) {
                for (int z = 0; z < VOLUME_SIZE; z++) {
                    data.put((byte) ((VOLUME_SIZE * 100.0 + x * 155.0) / VOLUME_SIZE)); // red
                    data.put((byte) ((VOLUME_SIZE * 100.0 + y * 155.0) / VOLUME_SIZE)); // green
                    data.put((byte) ((VOLUME_SIZE * 100.0 + z * 155.0) / VOLUME_SIZE)); // blue
                    data.put((byte) (255 * random.nextDouble())); // alpha
                }
            }
        }
        Texture3dSupport.newInstance(volume, TextureType.RGBA, data, VOLUME_SIZE);
        final ClippingPlaneSupport clipping = ClippingPlaneSupport.newInstance(volume);
        clipping.addClippingPlane(new Vector3D(1.0, -1.0, 0.0).normalize(), 0.4);
        clipping.addClippingPlane(new Vector3D(-1.0, 1.0, 0.0).normalize(), 0.4);
        ColorPickingSupport.newInstance(volume).addListener(listener);
        scene.add(volume);
        return volume;
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
