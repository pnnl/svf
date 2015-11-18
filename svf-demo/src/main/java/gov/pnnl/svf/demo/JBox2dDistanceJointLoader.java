package gov.pnnl.svf.demo;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.AbstractActor;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.animation.AnimationSupportListener;
import gov.pnnl.svf.animation.ScaleAnimationSupport;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Cuboid3D;
import gov.pnnl.svf.jbox2d.physics.JBox2dDistanceJointSupport;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsEngine;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsSupport;
import gov.pnnl.svf.jbox2d.physics.JBox2dPhysicsType;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.apache.commons.math.geometry.Vector3D;

public class JBox2dDistanceJointLoader implements DemoLoader {

    private static final boolean DISK = true;
    private static final int NUM_BARNICLES = 10;
    private static final Vector3D[] SCALES = {new Vector3D(0.5, 0.5, 1.0), new Vector3D(2.0, 2.0, 1.0), new Vector3D(5.0, 5.0, 1.0)};

    /**
     * Constructor
     */
    public JBox2dDistanceJointLoader() {
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
            JBox2dDistanceJointLoader.createScaleAnimation(user, SCALES[1]);
            // physics
            JBox2dPhysicsSupport.newInstance(user, JBox2dPhysicsType.STATIC, 1.0f, 0.1f, 0.0f, 0.7f);
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
        // distance joint
        // disk 1
        final Actor disk1 = Actor.Builder.construct()
                .scene(scene)
                .passNumber((byte) 1)
                .drawingPass(DrawingPass.SCENE)
                .shape(Circle2D.ONE)
                .build();
        ColorSupport.newInstance(disk1).setColor(Color.INDIGO);
        final TransformSupport transform1 = TransformSupport.newInstance(disk1).
                setTranslation(new Vector3D(
                                0.33 * scene.getBoundary().getX() - scene.getBoundary().getX() / 2.0,
                                0.5 * scene.getBoundary().getY() - scene.getBoundary().getY() / 2.0,
                                z)).
                setScale(new Vector3D(1.0, 1.0, 1.0)).
                setRotationAxis(Vector3D.PLUS_K).
                setRotation(0.0);
        scene.add(disk1);
        // physics
        final JBox2dPhysicsSupport support1 = JBox2dPhysicsSupport.newInstance(disk1, JBox2dPhysicsType.STATIC, 1.0f, 0.3f, 0.0f, 0.0f);
        for (int i = 0; i < NUM_BARNICLES; i++) {
            // disk 2
            final Actor disk2 = Actor.Builder.construct()
                    .scene(scene)
                    .passNumber((byte) 1)
                    .drawingPass(DrawingPass.SCENE)
                    .shape(Circle2D.ONE)
                    .build();
            ColorSupport.newInstance(disk2).setColor(Color.INDIGO);
            final double x = Math.sin(2.0 * Math.PI * ((double) i / (double) NUM_BARNICLES));
            final double y = Math.cos(2.0 * Math.PI * ((double) i / (double) NUM_BARNICLES));
            final TransformSupport transform2 = TransformSupport.newInstance(disk2).
                    setTranslation(new Vector3D(
                                    x * 10.0 + transform1.getTranslation().getX(),
                                    y * 10.0 + transform1.getTranslation().getY(),
                                    z)).
                    setScale(new Vector3D(0.5, 0.5, 1.0)).
                    setRotationAxis(Vector3D.PLUS_K).
                    setRotation(0.0);
            scene.add(disk2);
            // physics
            final JBox2dPhysicsSupport support2 = JBox2dPhysicsSupport.newInstance(disk2, JBox2dPhysicsType.DYNAMIC, 1.0f, 0.3f, 0.5f, 0.0f);
            // joint
            JBox2dDistanceJointSupport.newInstance(disk2, support1, new Vector3D(x, y, 0.0f), support2, Vector3D.ZERO, 1.0f, 1.0f);
            //          // draw a link between them
            final AbstractActor link = new AbstractActor(scene, "link", scene.getFactory().newUuid(scene)) {
                @Override
                public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
                    gl.glBegin(GL.GL_LINE_STRIP);
                    gl.glVertex3d(transform1.getTranslation().getX() + x, transform1.getTranslation().getY() + y, transform1.getTranslation().getZ());
                    gl.glVertex3d(transform2.getTranslation().getX(), transform2.getTranslation().getY(), transform2.getTranslation().getZ());
                    gl.glEnd();
                }

                @Override
                public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
                    // not necessary
                }
            };
            link.setDrawingPass(DrawingPass.SCENE);
            link.setPassNumber(1);
            ColorSupport.newInstance(link).setColor(Color.INDIGO);
            scene.add(link);
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
                        JBox2dDistanceJointLoader.createScaleAnimation(actor, SCALES[0].equals(scale) ? SCALES[1] : SCALES[0]);
                    }
                });
    }
}
