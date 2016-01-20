package gov.pnnl.svf.demo;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.ActorExt;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Cuboid3D;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsEngine;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsSupport;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsType;
import gov.pnnl.svf.jbox2d.physics.JBox2dRevoluteJointSupport;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import org.apache.commons.math.geometry.Vector3D;

public class JBox2dRevoluteJointLoader implements DemoLoader {

    private static final boolean DISK = true;
    private static final int NUM_BALLS = 10;
    private static final int NUM_SQUARES = 10;

    /**
     * Constructor
     */
    public JBox2dRevoluteJointLoader() {
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
                            (0.5 * scene.getBoundary().getX()) - (scene.getBoundary().getX() / 2.0),
                            6.0 - (scene.getBoundary().getY() / 2.0),
                            z)).
                    setScale(new Vector3D(0.5, 0.5, 1.0)).
                    setRotationAxis(Vector3D.PLUS_K).
                    setRotation(0.0);
            // physics
            physics.addPhysics(JBox2dPhysicsSupport.newInstance(user, JBox2dPhysicsType.STATIC, 1.0f, 0.1f, 0.0f, 0.7f));
            scene.add(user);
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
            // add a spinner
            final ActorExt spinner = Actor.Builder.construct()
                    .scene(scene)
                    .passNumber((byte) 1)
                    .drawingPass(DrawingPass.SCENE)
                    .shape(Rectangle2D.ONE)
                    .build();
            ColorSupport.newInstance(spinner).setColor(Color.BLUE);
            TransformSupport.newInstance(spinner).
                    setTranslation(transform.getTranslation()).
                    setScale(new Vector3D(9.5, 1.0, 1.0)).
                    setRotationAxis(Vector3D.PLUS_K).
                    setRotation(0.0);
            // physics
            final JBox2dPhysicsSupport support = JBox2dPhysicsSupport.newInstance(spinner, JBox2dPhysicsType.DYNAMIC, 1.0f, 0.5f, 0.3f, 0.6f);
            physics.addPhysics(support);
            final JBox2dRevoluteJointSupport revolute = JBox2dRevoluteJointSupport.newInstance(user, spinner, transform.getTranslation(), false, 0.0f, 0.0f,
                                                                                               true, 10000.0f, 10000.0f, false, 0.0f, 0.0f);
            physics.addJoint(revolute);
            scene.add(spinner);
        }
        // add a tilted board
        final ActorExt tilted = Actor.Builder.construct()
                .scene(scene)
                .passNumber((byte) 1)
                .drawingPass(DrawingPass.SCENE)
                .shape(Rectangle2D.ONE)
                .build();
        ColorSupport.newInstance(tilted).setColor(Color.BLUE);
        TransformSupport.newInstance(tilted).
                setTranslation(new Vector3D(17.0, -17.0, 0.0)).
                setScale(new Vector3D(25.0, 1.0, 1.0)).
                setRotationAxis(Vector3D.PLUS_K).
                setRotation(45.0);
        // physics
        physics.addPhysics(JBox2dPhysicsSupport.newInstance(tilted, JBox2dPhysicsType.STATIC, 1.0f, 0.5f, 0.3f, 0.6f));
        scene.add(tilted);
        // add a spring board
        final ActorExt floor = Actor.Builder.construct()
                .scene(scene)
                .passNumber((byte) 1)
                .drawingPass(DrawingPass.SCENE)
                .shape(Rectangle2D.ONE)
                .build();
        ColorSupport.newInstance(floor).setColor(Color.BLUE);
        final TransformSupport floorTransform = TransformSupport.newInstance(floor).
                setTranslation(new Vector3D(-17.0, -17.0, 0.0)).
                setScale(new Vector3D(25.0, 1.0, 1.0)).
                setRotationAxis(Vector3D.PLUS_K).
                setRotation(-45.0);
        // physics
        JBox2dPhysicsSupport support = JBox2dPhysicsSupport.newInstance(floor, JBox2dPhysicsType.STATIC, 1.0f, 0.5f, 0.3f, 0.6f);
        physics.addPhysics(support);
        scene.add(floor);
        // the spring
        final ActorExt spring = Actor.Builder.construct()
                .scene(scene)
                .passNumber((byte) 1)
                .drawingPass(DrawingPass.SCENE)
                .shape(Rectangle2D.ONE)
                .build();
        ColorSupport.newInstance(spring).setColor(Color.BLUE);
        TransformSupport.newInstance(spring).
                setTranslation(floorTransform.getTranslation().add(new Vector3D(3.5, 0.0, 0.0))).
                setScale(new Vector3D(8.0, 1.0, 1.0)).
                setRotationAxis(Vector3D.PLUS_K).
                setRotation(0.0);
        // physics
        support = JBox2dPhysicsSupport.newInstance(spring, JBox2dPhysicsType.DYNAMIC, 1.0f, 0.5f, 0.3f, 0.6f);
        physics.addPhysics(support);
        scene.add(spring);
        // add the joint
        JBox2dRevoluteJointSupport revolute = JBox2dRevoluteJointSupport.newInstance(floor, spring, floorTransform.getTranslation(),
                                                                                     true, 45.0f, 135.0f,
                                                                                     true, 0.0f, 1000.0f,
                                                                                     true, 90.0f, 10.0f);
        physics.addJoint(revolute);
        // add some dynamic planes made into a chain
        ActorExt previous = null;
        for (int i = 0; i < NUM_SQUARES; i++) {
            // planes
            final ActorExt plane = Actor.Builder.construct()
                    .scene(scene)
                    .passNumber((byte) 1)
                    .drawingPass(DrawingPass.SCENE)
                    .shape(Rectangle2D.ONE)
                    .build();
            ColorSupport.newInstance(plane).setColor(Color.GREEN);
            final TransformSupport transform = TransformSupport.newInstance(plane).
                    setTranslation(new Vector3D(
                            (i * 2.0) - ((NUM_SQUARES * 2.0) / 2.0),
                            2.0,
                            z)).
                    setScale(new Vector3D(3.0, 1.0, 1.0)).
                    setRotationAxis(Vector3D.PLUS_K).
                    setRotation(0.0);
            // physics
            physics.addPhysics(JBox2dPhysicsSupport.newInstance(plane, JBox2dPhysicsType.DYNAMIC, 1.0f, 0.5f, 0.7f, 0.1f));
            scene.add(plane);
            // revolute joint
            if (i > 0) {
                revolute = JBox2dRevoluteJointSupport.newInstance(previous, plane, transform.getTranslation().subtract(new Vector3D(1.0, 0.0, 0.0)),
                                                                  true, -45.0f, 45.0f,
                                                                  true, 0.0f, 10.0f,
                                                                  false, 0.0f, 0.0f);
                physics.addJoint(revolute);
            }
            previous = plane;
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
                            (((double) i / (double) NUM_BALLS) * (scene.getBoundary().getX() - 1.0)) - ((scene.getBoundary().getX() - 1.0) / 2.0),
                            (0.9 * scene.getBoundary().getY()) - (scene.getBoundary().getY() / 2.0),
                            z)).
                    setScale(new Vector3D(0.5, 0.5, 1.0)).
                    setRotationAxis(Vector3D.PLUS_K).
                    setRotation(0.0);
            // physics
            physics.addPhysics(JBox2dPhysicsSupport.newInstance(disk, JBox2dPhysicsType.DYNAMIC, 1.0f, 0.5f, 0.3f, 0.6f));
            final double r = random.nextDouble() * 2.0 * Math.PI;
            final double p = random.nextDouble() * 100.0;
            support.setImpulse(new Vector3D(Math.cos(r) * p, Math.sin(r) * p, 0.0));
            scene.add(disk);
        }
    }
}
