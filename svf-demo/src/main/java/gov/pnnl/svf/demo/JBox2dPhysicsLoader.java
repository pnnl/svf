package gov.pnnl.svf.demo;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.ActorExt;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.animation.AnimationSupportListener;
import gov.pnnl.svf.animation.ColorAnimationSupport;
import gov.pnnl.svf.animation.RotationAnimationSupport;
import gov.pnnl.svf.animation.ScaleAnimationSupport;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Cuboid3D;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.jbox2d.physics.JBox2dChildTransformSupport;
import gov.pnnl.svf.jbox2d.physics.JBox2dContactFilter;
import gov.pnnl.svf.jbox2d.physics.JBox2dContactListener;
import gov.pnnl.svf.jbox2d.physics.JBox2dMouseJointSupport;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsEngine;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsSupport;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsType;
import gov.pnnl.svf.jbox2d.physics.JBox2dSceneBoundaryActor;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.picking.PickingSupport;
import gov.pnnl.svf.picking.PickingSupportListener;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ChildSupport;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import org.apache.commons.math.geometry.Vector3D;

public class JBox2dPhysicsLoader implements DemoLoader {

    private static final boolean DISK = true;
    private static final int NUM_BALLS = 10;
    private static final int NUM_SQUARES = 10;
    private static final Vector3D[] SCALES = {new Vector3D(0.5, 0.5, 1.0), new Vector3D(2.0, 2.0, 1.0), new Vector3D(5.0, 5.0, 1.0)};

    /**
     * Constructor
     */
    public JBox2dPhysicsLoader() {
    }

    @Override
    public void load(final Scene scene) {
        // create a camera
        final SimpleCamera camera = scene.getFactory().createCamera(scene, SimpleCamera.class);
        camera.setLocation(new Vector3D(0.0, 0.0, 120.0));
        ColorSupport.newInstance(camera).setColor(Color.BLACK);
        scene.add(camera);
        // log the fps and vps
        if (!scene.getExtended().getSceneBuilder().isDebug()) {
            FpsLogger.newInstance(scene, 1000 * 10);
            PerfLogger.newInstance(scene, 1000 * 10);
        }
        scene.setBoundary(new Vector3D(50.0, 50.0, 1.0));
        // set up the world physics
        final JBox2dPhysicsEngine physics = new JBox2dPhysicsEngine(scene, new Vector3D(0.0, -10.0, 0.0f), 0.5f, 0.0f, 1.0f);
        physics.setContactFilter(new JBox2dContactFilter() {
            @Override
            public boolean shouldCollide(final JBox2dPhysicsSupport obj1, final JBox2dPhysicsSupport obj2) {
                if (obj1.getActor() instanceof JBox2dSceneBoundaryActor
                    || obj2.getActor() instanceof JBox2dSceneBoundaryActor) {
                    // all objects should collide with the boundary
                    return true;
                }
                final Shape shape1 = obj1.getActor() instanceof ShapeActor ? ((ShapeActor) obj1.getActor()).getShape() : null;
                final Shape shape2 = obj2.getActor() instanceof ShapeActor ? ((ShapeActor) obj2.getActor()).getShape() : null;
                if (("disk".equals(obj1.getActor().getId()) && ((shape2 instanceof Circle2D) || (shape2 instanceof Text3D)))
                    || ("disk".equals(obj2.getActor().getId()) && ((shape1 instanceof Circle2D) || (shape1 instanceof Text3D)))) {
                    // user controlled disk can't interact with balls or text
                    return false;
                }
                return true;
            }
        });
        physics.addContactListener(new JBox2dContactListener() {
            @Override
            public void beginContact(final JBox2dPhysicsSupport obj1, final JBox2dPhysicsSupport obj2) {
                final Shape shape1 = obj1.getActor() instanceof ShapeActor ? ((ShapeActor) obj1.getActor()).getShape() : null;
                final Shape shape2 = obj2.getActor() instanceof ShapeActor ? ((ShapeActor) obj2.getActor()).getShape() : null;
                if (shape1 instanceof Circle2D) {
                    ColorAnimationSupport.newInstance(obj1.getActor(), 100L, 0L, false, Color.RED);
                }
                if (shape2 instanceof Circle2D) {
                    ColorAnimationSupport.newInstance(obj2.getActor(), 100L, 0L, false, Color.RED);
                }
            }

            @Override
            public void endContact(final JBox2dPhysicsSupport obj1, final JBox2dPhysicsSupport obj2) {
                final Shape shape1 = obj1.getActor() instanceof ShapeActor ? ((ShapeActor) obj1.getActor()).getShape() : null;
                final Shape shape2 = obj2.getActor() instanceof ShapeActor ? ((ShapeActor) obj2.getActor()).getShape() : null;
                if (shape1 instanceof Circle2D) {
                    ColorAnimationSupport.newInstance(obj1.getActor(), 100L, 0L, false, Color.BLUE);
                }
                if (shape2 instanceof Circle2D) {
                    ColorAnimationSupport.newInstance(obj2.getActor(), 100L, 0L, false, Color.BLUE);
                }
            }
        });
        // set the physics to visible to render physics debug information
        physics.setVisible(false);
        scene.add(physics);
        // create a cube around the scene bounds
        final Actor boundary = Actor.Builder.construct()
                .scene(scene)
                .type("bounds")
                .passNumber((byte) 0)
                .drawingPass(DrawingPass.SCENE)
                .shape(Cuboid3D.ONE)
                .color(new Color(1.0f, 1.0f, 1.0f, 0.1f))
                .build();
        TransformSupport.newInstance(boundary).setScale(scene.getBoundary()).setTranslation(scene.getCenter());
        scene.add(boundary);
        // create some actors
        final double z = 0.0;
        final Random random = new Random();
        // add a user controlled disk
        if (DISK) {
            final Actor user = Actor.Builder.construct()
                    .scene(scene)
                    .passNumber((byte) 1)
                    .drawingPass(DrawingPass.SCENE)
                    .shape(Circle2D.ONE)
                    .build();
            ColorSupport.newInstance(user).setColor(Color.BLUE);
            final TransformSupport transform = TransformSupport.newInstance(user).
                    setTranslation(new Vector3D(
                                    0.66 * scene.getBoundary().getX() - scene.getBoundary().getX() / 2.0,
                                    0.5 * scene.getBoundary().getY() - scene.getBoundary().getY() / 2.0,
                                    z)).
                    setScale(new Vector3D(0.5, 0.5, 1.0)).
                    setRotationAxis(Vector3D.PLUS_K).
                    setRotation(0.0);
            scene.add(user);
            JBox2dPhysicsLoader.createScaleAnimation(user, SCALES[1]);
            RotationAnimationSupport.newInstance(user, 1000L, 0L, true, 360.0);
            // random box thingy to test derived transform
            final ActorExt thingy = Actor.Builder.construct()
                    .scene(scene)
                    .passNumber((byte) 1)
                    .drawingPass(DrawingPass.SCENE)
                    .shape(Rectangle2D.ONE)
                    .type("thingy")
                    .build();
            ColorSupport.newInstance(thingy).setColor(Color.INDIGO);
            JBox2dChildTransformSupport.newInstance(thingy).
                    setScale(new Vector3D(2.0, 2.0, 1.0)).
                    setTranslation(new Vector3D(1.0, 1.0, 0.0)).
                    setRotationAxis(Vector3D.PLUS_K).
                    setRotation(45.0);
            ChildSupport.newInstance(user).add(thingy);
            // physics
            physics.addPhysics(JBox2dPhysicsSupport.newInstance(user, JBox2dPhysicsType.STATIC, 1.0f, 0.1f, 0.0f, 0.7f));
            physics.addPhysics(JBox2dPhysicsSupport.newInstance(thingy, JBox2dPhysicsType.STATIC, 1.0f, 0.1f, 0.0f, 0.7f));
            scene.add(new KeyAdapter() {
                @Override
                public void keyTyped(final KeyEvent evt) {
                    switch (evt.getKeyChar()) {
                        case 'w':
                        case 'W':
                            transform.setTranslation(transform.getTranslation().add(new Vector3D(0.0, 0.3, 0.0)));
                            break;
                        case 's':
                        case 'S':
                            transform.setTranslation(transform.getTranslation().add(new Vector3D(0.0, -0.3, 0.0)));
                            break;
                        case 'a':
                        case 'A':
                            transform.setTranslation(transform.getTranslation().add(new Vector3D(-0.3, 0.0, 0.0)));
                            break;
                        case 'd':
                        case 'D':
                            transform.setTranslation(transform.getTranslation().add(new Vector3D(0.3, 0.0, 0.0)));
                            break;
                        default:
                        // no operation
                    }
                }
            });
            scene.add(new org.eclipse.swt.events.KeyAdapter() {

                @Override
                public void keyReleased(final org.eclipse.swt.events.KeyEvent evt) {
                    switch (evt.character) {
                        case 'w':
                        case 'W':
                            transform.setTranslation(transform.getTranslation().add(new Vector3D(0.0, 0.3, 0.0)));
                            break;
                        case 's':
                        case 'S':
                            transform.setTranslation(transform.getTranslation().add(new Vector3D(0.0, -0.3, 0.0)));
                            break;
                        case 'a':
                        case 'A':
                            transform.setTranslation(transform.getTranslation().add(new Vector3D(-0.3, 0.0, 0.0)));
                            break;
                        case 'd':
                        case 'D':
                            transform.setTranslation(transform.getTranslation().add(new Vector3D(0.3, 0.0, 0.0)));
                            break;
                        default:
                        // no operation
                    }
                }
            });
        }
        // add some dynamic planes
        for (int i = 0; i < NUM_SQUARES; i++) {
            // planes
            for (int j = 0; j < i + 1; j++) {
                final ActorExt plane = Actor.Builder.construct()
                        .scene(scene)
                        .passNumber((byte) 1)
                        .drawingPass(DrawingPass.SCENE)
                        .shape(Rectangle2D.ONE)
                        .build();
                ColorSupport.newInstance(plane).setColor(Color.GREEN);
                TransformSupport.newInstance(plane).
                        setTranslation(new Vector3D(
                                        j * 1.01 - (i * 1.01) / 2.0,
                                        (NUM_SQUARES - i) * 1.01 - (scene.getBoundary().getY() / 2.0 + 1.0) + 5.0,
                                        z)).
                        setRotationAxis(Vector3D.PLUS_K).
                        setRotation(0.0);
                scene.add(plane);
                // physics
                physics.addPhysics(JBox2dPhysicsSupport.newInstance(plane, JBox2dPhysicsType.DYNAMIC, 1.0f, 0.5f, 0.7f, 0.1f));
            }
        }
        // add some dynamic disks
        for (int i = 0; i < NUM_BALLS; i++) {
            // disks
            final ActorExt disk = Actor.Builder.construct()
                    .scene(scene)
                    .passNumber((byte) 1)
                    .drawingPass(DrawingPass.SCENE)
                    .shape(Circle2D.ONE)
                    .build();
            ColorSupport.newInstance(disk).setColor(Color.BLUE);
            TransformSupport.newInstance(disk).
                    setTranslation(new Vector3D(
                                    (double) i / (double) NUM_BALLS * (scene.getBoundary().getX() - 1.0) - (scene.getBoundary().getX() - 1.0) / 2.0,
                                    0.9 * scene.getBoundary().getY() - scene.getBoundary().getY() / 2.0,
                                    z)).
                    setScale(new Vector3D(0.5, 0.5, 1.0)).
                    setRotationAxis(Vector3D.PLUS_K).
                    setRotation(0.0);
            PickingSupport.newInstance(disk).addListener(new PickingSupportListener() {
                @Override
                public void picked(final Actor actor, final PickingCameraEvent event) {
                    if (event.getTypes().contains(CameraEventType.LEFT)) {
                        final PickingCamera picking = actor.getScene().lookup(PickingCamera.class);
                        if (picking != null) {
                            final JBox2dMouseJointSupport mouse = JBox2dMouseJointSupport.newInstance(picking, actor, new Vector3D(event.getX(), actor
                                                                                                                                   .getScene().getViewport().getHeight()
                                                                                                                                                 - event.getY(), 0.0));
                            physics.addJoint(mouse);
                        }
                    }
                }
            });
            scene.add(disk);
            // physics
            final JBox2dPhysicsSupport support = JBox2dPhysicsSupport.newInstance(disk, JBox2dPhysicsType.DYNAMIC, 1.0f, 0.5f, 0.3f, 0.6f);
            physics.addPhysics(support);
            final double r = random.nextDouble() * 2.0 * Math.PI;
            final double p = random.nextDouble() * 100.0;
            support.setImpulse(new Vector3D(Math.cos(r) * p, Math.sin(r) * p, 0.0));
        }
    }

    private static void createScaleAnimation(final Actor actor, final Vector3D scale) {
        ScaleAnimationSupport.newInstance(actor, 5L * 1000L, 0L, false, scale).
                addListener(new AnimationSupportListener() {

                    @Override
                    public void iterationCompleted() {
                        // no operation
                    }

                    @Override
                    public void animationCompleted() {
                        JBox2dPhysicsLoader.createScaleAnimation(actor, SCALES[0].equals(scale) ? SCALES[1] : SCALES[0]);
                    }
                });
    }
}
