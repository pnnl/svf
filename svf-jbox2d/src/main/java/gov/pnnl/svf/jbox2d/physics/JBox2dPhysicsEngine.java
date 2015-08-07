package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.AbstractActor;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.DynamicShapeActor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.NamedThreadFactory;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Shape2D;
import gov.pnnl.svf.geometry.Shape3D;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneExt;
import gov.pnnl.svf.scene.Updatable;
import gov.pnnl.svf.shape.BoundsShape;
import gov.pnnl.svf.shape.RadialShape;
import gov.pnnl.svf.shape.RectangleShape;
import gov.pnnl.svf.shape.VolumeShape;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.update.UpdateTask;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.MouseJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

/**
 * This is a 2D physics engine that is responsible for updating any objects with
 * JBox2D support.
 *
 * @author Arthur Bleeker
 */
public class JBox2dPhysicsEngine extends AbstractActor implements Updatable {

    private static final Logger logger = Logger.getLogger(JBox2dPhysicsEngine.class.toString());
    /**
     * String representation of a field in this object.
     */
    public static final String UPDATING = "updating";
    /**
     * String representation of a field in this object.
     */
    public static final String PAUSED = "paused";
    /**
     * String representation of a field in this object.
     */
    public static final String ANIMATE = "animate";
    /**
     * String representation of a field in this object.
     */
    public static final String ANIMATE_DURATION = "animateDuration";
    /**
     * The actor name used to identify the bottom scene boundary.
     */
    public static final String SCENE_BOUNDARY_BOTTOM = "scene-boundary-bottom";
    /**
     * The actor name used to identify the top scene boundary.
     */
    public static final String SCENE_BOUNDARY_TOP = "scene-boundary-top";
    /**
     * The actor name used to identify the left scene boundary.
     */
    public static final String SCENE_BOUNDARY_LEFT = "scene-boundary-left";
    /**
     * The actor name used to identify the right scene boundary.
     */
    public static final String SCENE_BOUNDARY_RIGHT = "scene-boundary-right";
    protected final Set<JBox2dContactListener> contactListeners = Collections.synchronizedSet(new HashSet<JBox2dContactListener>());
    protected final ExecutorService executor = Executors.newSingleThreadExecutor(new NamedThreadFactory(getClass(), "Update"));
    protected final AtomicBoolean updating = new AtomicBoolean(false);
    protected final AtomicLong modifiedDelta = new AtomicLong(0L);
    protected final AtomicBoolean update = new AtomicBoolean(false);
    protected final SceneExt scene;
    protected final World world;
    protected final Object worldSync = new Object();
    protected boolean paused = false;
    protected boolean animate = false;
    protected long animateDuration = 1L;
    protected JBox2dContactFilter contactFilter = new JBox2dContactFilterImpl();

    /**
     * Constructor
     *
     * @param scene reference to the scene implementing the physics
     */
    public JBox2dPhysicsEngine(final Scene scene) {
        this(scene, null, 1.0f, 0.0f, 0.0f);
    }

    /**
     * Constructor
     *
     * @param scene       reference to the scene implementing the physics
     * @param gravity     The gravity for the scene
     * @param friction    the coefficient of friction, must be an number from 0
     *                    to 1 inclusive
     * @param restitution the coefficient of restitution or elasticity of the
     *                    actor, must be a number from 0 to 1 inclusive
     * @param z           the z plane to render debug info on
     */
    public JBox2dPhysicsEngine(final Scene scene, final Vector3D gravity, final float friction, final float restitution, final float z) {
        super(scene, "jobx2d-physics-engine", scene.getFactory().newUuid(scene));
        if (friction < 0.0f || friction > 1.0f) {
            throw new IllegalArgumentException("friction");
        }
        if (restitution < 0.0f || restitution > 1.0f) {
            throw new IllegalArgumentException("restitution");
        }
        // don't draw physics debug by default
        setVisible(false);
        this.scene = scene.getExtended();
        world = new World(gravity == null ? new Vec2(0.0f, 0.0f) : new Vec2((float) gravity.getX(), (float) gravity.getY()), true);
        final JBox2dDebugDraw debug = new JBox2dDebugDraw(scene, z);
        debug.setFlags(-1);//DebugDraw.e_aabbBit | DebugDraw.e_jointBit | DebugDraw.e_pairBit | DebugDraw.e_shapeBit | DebugDraw.e_dynamicTreeBit | DebugDraw.e_centerOfMassBit
        world.setDebugDraw(debug);
        world.setWarmStarting(false);
        world.setContactFilter(new ContactFilter() {
            @Override
            public boolean shouldCollide(final Fixture fixtureA, final Fixture fixtureB) {
                final JBox2dPhysicsSupport support1 = (JBox2dPhysicsSupport) fixtureA.m_body.m_userData;
                final JBox2dPhysicsSupport support2 = (JBox2dPhysicsSupport) fixtureB.m_body.m_userData;
                return contactFilter.shouldCollide(support1, support2);
            }
        });
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(final Contact contact) {
                if (!contactListeners.isEmpty()) {
                    final JBox2dPhysicsSupport obj1 = (JBox2dPhysicsSupport) contact.m_fixtureA.m_body.m_userData;
                    final JBox2dPhysicsSupport obj2 = (JBox2dPhysicsSupport) contact.m_fixtureB.m_body.m_userData;
                    synchronized (contactListeners) {
                        for (final JBox2dContactListener contactListener : contactListeners) {
                            contactListener.beginContact(obj1, obj2);
                        }
                    }
                }
            }

            @Override
            public void endContact(final Contact contact) {
                if (!contactListeners.isEmpty()) {
                    final JBox2dPhysicsSupport obj1 = (JBox2dPhysicsSupport) contact.m_fixtureA.m_body.m_userData;
                    final JBox2dPhysicsSupport obj2 = (JBox2dPhysicsSupport) contact.m_fixtureB.m_body.m_userData;
                    synchronized (contactListeners) {
                        for (final JBox2dContactListener contactListener : contactListeners) {
                            contactListener.endContact(obj1, obj2);
                        }
                    }
                }
            }

            @Override
            public void preSolve(final Contact contact, final Manifold oldManifold) {
                // not used
            }

            @Override
            public void postSolve(final Contact contact, final ContactImpulse impulse) {
                // not used
            }
        });
        setDrawingPass(DrawingPass.SCENE);
        // create the scene boundary actors
        // bottom
        final JBox2dSceneBoundaryActor bottom = new JBox2dSceneBoundaryActor(this.scene, SCENE_BOUNDARY_BOTTOM);
        TransformSupport.newInstance(bottom);
        ColorSupport.newInstance(bottom).setColor(Color.TRANSPARENT);
        addPhysics(JBox2dPhysicsSupport.newInstance(bottom, JBox2dPhysicsType.STATIC, 0.0f, friction, 0.0f, restitution));
        scene.add(bottom);
        // top
        final JBox2dSceneBoundaryActor top = new JBox2dSceneBoundaryActor(this.scene, SCENE_BOUNDARY_TOP);
        TransformSupport.newInstance(top);
        ColorSupport.newInstance(top).setColor(Color.TRANSPARENT);
        addPhysics(JBox2dPhysicsSupport.newInstance(top, JBox2dPhysicsType.STATIC, 0.0f, friction, 0.0f, restitution));
        scene.add(top);
        // left
        final JBox2dSceneBoundaryActor left = new JBox2dSceneBoundaryActor(this.scene, SCENE_BOUNDARY_LEFT);
        TransformSupport.newInstance(left);
        ColorSupport.newInstance(left).setColor(Color.TRANSPARENT);
        addPhysics(JBox2dPhysicsSupport.newInstance(left, JBox2dPhysicsType.STATIC, 0.0f, friction, 0.0f, restitution));
        scene.add(left);
        // right
        final JBox2dSceneBoundaryActor right = new JBox2dSceneBoundaryActor(this.scene, SCENE_BOUNDARY_RIGHT);
        TransformSupport.newInstance(right);
        ColorSupport.newInstance(right).setColor(Color.TRANSPARENT);
        addPhysics(JBox2dPhysicsSupport.newInstance(right, JBox2dPhysicsType.STATIC, 0.0f, friction, 0.0f, restitution));
        scene.add(right);
        // reset boundary
        resize();
    }

    @Override
    public void dispose() {
        super.dispose();
        contactListeners.clear();
        executor.shutdownNow();
    }

    private void resize() {
        final Vector3D boundary = scene.getBoundary();
        final Vector3D center = scene.getCenter();
        if (boundary == null || center == null) {
            logger.log(Level.WARNING, "{0}: Cannot resize physics borders, scene boundary or center is null.", scene);
            return;
        }
        synchronized (worldSync) {
            final Actor bottom = scene.getActor(SCENE_BOUNDARY_BOTTOM);
            if (bottom != null) {
                final TransformSupport transformSupport = bottom.lookup(TransformSupport.class);
                if (transformSupport != null) {
                    transformSupport.setScale(new Vector3D(boundary.getX(), 0.2, 1.0)).setTranslation(
                            new Vector3D(center.getX(), center.getY() - (boundary.getY() * 0.5), 0.0));
                }
            }
            final Actor top = scene.getActor(SCENE_BOUNDARY_TOP);
            if (top != null) {
                final TransformSupport transformSupport = top.lookup(TransformSupport.class);
                if (transformSupport != null) {
                    transformSupport.setScale(new Vector3D(boundary.getX(), 0.2, 1.0)).setTranslation(
                            new Vector3D(center.getX(), center.getY() + (boundary.getY() * 0.5), 0.0));
                }
            }
            final Actor left = scene.getActor(SCENE_BOUNDARY_LEFT);
            if (left != null) {
                final TransformSupport transformSupport = left.lookup(TransformSupport.class);
                if (transformSupport != null) {
                    transformSupport.setScale(new Vector3D(0.2, boundary.getY(), 1.0)).setTranslation(
                            new Vector3D(center.getX() - (boundary.getX() * 0.5), center.getY(), 0.0));
                }
            }
            final Actor right = scene.getActor(SCENE_BOUNDARY_RIGHT);
            if (right != null) {
                final TransformSupport transformSupport = right.lookup(TransformSupport.class);
                if (transformSupport != null) {
                    transformSupport.setScale(new Vector3D(0.2, boundary.getY(), 1.0)).setTranslation(
                            new Vector3D(center.getX() + (boundary.getX() * 0.5), center.getY(), 0.0));
                }
            }
        }
    }

    /**
     * @return true if the physics is currently updating
     */
    public boolean isUpdating() {
        return updating.get();
    }

    /**
     * @return true if the engine is paused
     */
    public boolean isPaused() {
        synchronized (this) {
            return paused;
        }
    }

    /**
     * Pausing the engine will prevent it from updating in regular time steps.
     * Calling update manually will still update the engine when paused. This
     * allows the engine to be manually stepped and updated.
     *
     * @param paused true to pause the engine
     */
    public void setPaused(final boolean paused) {
        final boolean old;
        synchronized (this) {
            old = this.paused;
            this.paused = paused;
        }
        getPropertyChangeSupport().firePropertyChange(PAUSED, old, paused);
    }

    /**
     * @return true if the actors are animating to their new positions
     */
    public boolean isAnimate() {
        synchronized (this) {
            return animate;
        }
    }

    /**
     * @param animate true if the actors are animating to their new positions
     */
    public void setAnimate(final boolean animate) {
        final boolean old;
        synchronized (this) {
            old = this.animate;
            this.animate = animate;
        }
        getPropertyChangeSupport().firePropertyChange(ANIMATE, old, animate);
    }

    /**
     * @return the number of milliseconds for the animation duration
     */
    public long getAnimateDuration() {
        synchronized (this) {
            return animateDuration;
        }
    }

    /**
     * @param animateDuration the number of milliseconds for the animation
     *                        duration
     */
    public void setAnimateDuration(final long animateDuration) {
        if (animateDuration <= 0L) {
            throw new IllegalArgumentException("animateDuration");
        }
        final long old;
        synchronized (this) {
            old = this.animateDuration;
            this.animateDuration = animateDuration;
        }
        getPropertyChangeSupport().firePropertyChange(ANIMATE_DURATION, old, animateDuration);
    }

    /**
     * The contact filter provides a means to filter which objects can contact
     * one another.
     *
     * @param contactFilter the contact filter to set
     */
    public void setContactFilter(final JBox2dContactFilter contactFilter) {
        if (contactFilter == null) {
            throw new NullPointerException("contactFilter");
        }
        synchronized (worldSync) {
            this.contactFilter = contactFilter;
            world.setContactFilter(new ContactFilterImpl(contactFilter));
        }
    }

    /**
     * Add a new contact listener.
     *
     * @param contactListener the contact listener to add
     */
    public void addContactListener(final JBox2dContactListener contactListener) {
        if (contactListener == null) {
            throw new NullPointerException("contactListener");
        }
        contactListeners.add(contactListener);
    }

    /**
     * Remove a contact listener.
     *
     * @param contactListener the contact listener to remove
     */
    public void removeContactListener(final JBox2dContactListener contactListener) {
        if (contactListener == null) {
            throw new NullPointerException("contactListener");
        }
        contactListeners.remove(contactListener);
    }

    /**
     * Adds an anchor that connects two points on the specified actors. It is no
     * longer necessary to call this method directly.
     *
     * @param support the joint support object
     *
     * @throws NullPointerException if support is null
     */
    public void addJoint(final JBox2dJointSupport support) {
        if (support == null) {
            throw new NullPointerException("support");
        }
        if (!support.setPhysicsEngine(this)) {
            return;
        }
        final Body body1 = (Body) support.getSupport1().getPhysicsData();
        if (body1 == null) {
            throw new IllegalArgumentException("Actor must have physics added by the JBox2d physics engine.");
        }
        final Body body2 = (Body) support.getSupport2().getPhysicsData();
        if (body2 == null) {
            throw new IllegalArgumentException("Actor must have physics added by the JBox2d physics engine.");
        }
        final TransformSupport transform1 = support.getSupport1().getActor().lookup(TransformSupport.class);
        if (transform1 == null) {
            throw new IllegalArgumentException("Actor must have a TransformSupport object in its lookup.");
        }
        final TransformSupport transform2 = support.getSupport2().getActor().lookup(TransformSupport.class);
        if (transform2 == null) {
            throw new IllegalArgumentException("Actor must have a TransformSupport object in its lookup.");
        }
        switch (support.getType()) {
            case DISTANCE: {
                if (!(support instanceof JBox2dDistanceJointSupport)) {
                    throw new IllegalArgumentException("Support object must be of type JBox2dDistanceJointSupport to declare the DISTANCE type.");
                }
                final JBox2dDistanceJointSupport distance = (JBox2dDistanceJointSupport) support;
                // first anchor point
                Vector3D anchor1 = distance.getAnchor1();
                if (!Vector3D.ZERO.equals(anchor1)) {
                    // scale
                    anchor1 = new Vector3D(anchor1.getX() * transform1.getScale().getX(),
                                           anchor1.getY() * transform1.getScale().getY(),
                                           anchor1.getZ() * transform1.getScale().getZ());
                    // rotation
                    final Rotation rotation1 = new Rotation(Vector3D.PLUS_K,
                                                            Math.toRadians(transform1.getRotationAxis().getZ() > 0.0 ? transform1.getRotation() : 360.0 - transform1.getRotation()));
                    anchor1 = rotation1.applyTo(anchor1);
                }
                // translation
                final Vector3D location1 = transform1.getTranslation().add(anchor1);
                // second anchor point
                Vector3D anchor2 = distance.getAnchor2();
                if (!Vector3D.ZERO.equals(anchor2)) {
                    // scale
                    anchor2 = new Vector3D(anchor2.getX() * transform2.getScale().getX(),
                                           anchor2.getY() * transform2.getScale().getY(),
                                           anchor2.getZ() * transform2.getScale().getZ());
                    // rotation
                    final Rotation rotation2 = new Rotation(Vector3D.PLUS_K,
                                                            Math.toRadians(transform2.getRotationAxis().getZ() > 0.0 ? transform2.getRotation() : 360.0 - transform2.getRotation()));
                    anchor2 = rotation2.applyTo(anchor2);
                }
                // translation
                final Vector3D location2 = transform2.getTranslation().add(anchor2);
                // build the distance joint
                synchronized (worldSync) {
                    final DistanceJointDef def = new DistanceJointDef();
                    def.initialize(body1, body2,
                                   new Vec2((float) location1.getX(),
                                            (float) location1.getY()),
                                   new Vec2((float) location2.getX(),
                                            (float) location2.getY()));
                    def.collideConnected = true;
                    def.frequencyHz = distance.getFrequency();
                    def.dampingRatio = distance.getDamping();
                    def.length = 0.0f;
                    final Joint joint = world.createJoint(def);
                    joint.setUserData(distance);
                    distance.setPhysicsData(joint);
                }
                break;
            }
            case REVOLUTE: {
                if (!(support instanceof JBox2dRevoluteJointSupport)) {
                    throw new IllegalArgumentException("Support object must be of type JBox2dRevoluteJointSupport to declare the REVOLUTE type.");
                }
                final JBox2dRevoluteJointSupport revolute = (JBox2dRevoluteJointSupport) support;
                // anchor point
                // Vector3D anchor = revolute.getAnchor();
                // if (!Vector3D.ZERO.equals(anchor)) {
                // // scale
                // anchor = new Vector3D(anchor.getX() * transform2.getScale().getX(),
                // anchor.getY() * transform1.getScale().getY(),
                // anchor.getZ() * transform1.getScale().getZ());
                // // rotation
                // final Rotation rotation = new Rotation(Vector3D.PLUS_K,
                // Math.toRadians(transform1.getRotationAxis().getZ() > 0.0 ? transform1.getRotation() : 360.0 - transform1.getRotation()));
                // anchor = rotation.applyTo(anchor);
                // }
                // translation
                final Vector3D location = revolute.getAnchor();// transform1.getTranslation().add(anchor);
                synchronized (worldSync) {
                    final RevoluteJointDef def = new RevoluteJointDef();
                    def.initialize(body1, body2, new Vec2((float) location.getX(), (float) location.getY()));
                    def.enableLimit = revolute.isEnableLimit();
                    def.lowerAngle = (float) Math.toRadians(revolute.getLowerAngle());
                    def.upperAngle = (float) Math.toRadians(revolute.getUpperAngle());
                    def.enableMotor = revolute.isEnableMotor();
                    def.maxMotorTorque = revolute.getMaxMotorTorque();
                    def.motorSpeed = revolute.getMotorSpeed();
                    final Joint joint = world.createJoint(def);
                    joint.setUserData(revolute);
                    revolute.setPhysicsData(joint);
                }
                break;
            }
            case MOUSE: {
                if (!(support instanceof JBox2dMouseJointSupport)) {
                    throw new IllegalArgumentException("Support object must be of type JBox2dMouseJointSupport to declare the MOUSE type.");
                }
                final JBox2dMouseJointSupport mouse = (JBox2dMouseJointSupport) support;
                synchronized (worldSync) {
                    final MouseJointDef def = new MouseJointDef();
                    def.bodyA = body1;
                    def.bodyB = body2;
                    def.collideConnected = false;
                    def.maxForce = 100.0f * mouse.getSupport2().getDensity();
                    def.target.set((float) mouse.getAnchor().getX(), (float) mouse.getAnchor().getY());
                    final Joint joint = world.createJoint(def);
                    joint.setUserData(mouse);
                    mouse.setPhysicsData(joint);
                }
                break;
            }
        }
    }

    /**
     * Add physics to an actor in the scene. It is no longer necessary to call
     * this method directly.
     *
     * @param support the support object being added to the scene.
     *
     * @throws NullPointerException     if support is null
     * @throws IllegalArgumentException if transform support is missing from
     *                                  actor
     */
    public void addPhysics(final JBox2dPhysicsSupport support) {
        if (support == null) {
            throw new NullPointerException("support");
        }
        if (!support.setPhysicsEngine(this)) {
            return;
        }
        final TransformSupport transform = support.getActor().lookup(TransformSupport.class);
        if (transform == null) {
            throw new IllegalArgumentException("Actor must have a TransformSupport object in its lookup.");
        }
        synchronized (worldSync) {
            // create the body
            final BodyDef bodyDef = new BodyDef();
            switch (support.getType()) {
                case DYNAMIC:
                    bodyDef.type = BodyType.DYNAMIC;
                    break;
                case STATIC:
                    bodyDef.type = BodyType.STATIC;
                    break;
                case KINEMATIC:
                    bodyDef.type = BodyType.KINEMATIC;
                    break;
                default:
                    throw new IllegalArgumentException("Unkown enum type for the PhysicsType.");
            }
            bodyDef.position.set((float) transform.getTranslation().getX(), (float) transform.getTranslation().getY());
            bodyDef.angle = (float) Math.toRadians(transform.getRotation());
            bodyDef.linearDamping = support.getDamping();
            bodyDef.angularDamping = support.getDamping();
            final Body body = world.createBody(bodyDef);
            if (body == null) {
                logger.log(Level.WARNING, "{0}: Scene failed to create a physics body for: {1}", new Object[]{scene, support.getActor()});
                return;
            }
            body.setUserData(support);
            support.setPhysicsData(body);
            JBox2dPhysicsEngine.createFixture(support, transform, body);
        }
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        if (scene.getSceneBuilder().isDebug()) {
            gl.glPushAttrib(GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);
            drawState.setAttrib();
            synchronized (worldSync) {
                world.drawDebugData();
            }
        }
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (drawState.isAttrib()) {
            gl.glPopAttrib();
        }
        drawState.clearValues();
    }

    /**
     * Update the physics engine.
     *
     * @param duration the duration in milliseconds
     *
     * @return the remaining duration after update
     */
    protected long updatePhysics(final long duration) {
        final boolean animateActors = isAnimate();
        final long animationDuration = getAnimateDuration();
        final int steps;
        final JBox2dActorUpdate updater;
        synchronized (worldSync) {
            // step the world and convert milliseconds to seconds
            steps = (int) Math.floor(duration / (1000.0 / 30.0));
            if (steps < 1) {
                return duration;
            }
            final long start = System.currentTimeMillis();
            for (int i = 0; i < steps; i++) {
                world.step((float) duration / (float) steps / 1000.0f, 10, 10);
                //                world.clearForces();
            }
            final long end = System.currentTimeMillis();
            logger.log(Level.FINE, "{0}: Performing {1} JBox2d physics engine update steps took {2} milliseconds.", new Object[]{scene, steps, (end - start)});
            updater = animateActors ? new JBox2dActorUpdate(animationDuration) : new JBox2dActorUpdate();
            // iterate through the bodies so that they can be updated
            Body body = world.getBodyList();
            while (body != null) {
                if (body.getUserData() instanceof JBox2dPhysicsSupport) {
                    final JBox2dPhysicsSupport support = (JBox2dPhysicsSupport) body.getUserData();
                    final TransformSupport transform = support.getActor().lookup(TransformSupport.class);
                    if (transform != null) {
                        switch (support.getType()) {
                            case DYNAMIC:
                            case KINEMATIC:
                                // update the rotation of the body if it isn't controlled by physics
                                if (!support.isApplyRotation()) {
                                    body.setTransform(body.getPosition(), (float) Math.toRadians(transform.getRotation()));
                                }
                                // dynamic actors get their position and sometimes rotation from the physics engine
                                updater.addUpdateInfo(support, transform, body.getPosition().x, body.getPosition().y, body.getAngle());
                                break;
                            case STATIC:
                                // static actors get their position from the framework
                                body.setTransform(new Vec2((float) transform.getTranslation().getX(),
                                                           (float) transform.getTranslation().getY()),
                                                  (float) Math.toRadians(transform.getRotation()));
                                break;
                        }
                        // update impulse
                        final Vector3D impulse = support.getImpulse();
                        if (!Vector3D.ZERO.equals(impulse)) {
                            body.applyLinearImpulse(new Vec2((float) impulse.getX(), (float) impulse.getZ()), new Vec2(0.0f, 0.0f));
                            support.setImpulse(Vector3D.ZERO);
                        }
                        // update any other related body info
                        if (support.isChanged()) {
                            final Fixture fixtureList = body.getFixtureList();
                            if (fixtureList != null) {
                                body.destroyFixture(fixtureList);
                            }
                            body.createFixture(JBox2dPhysicsEngine.createShape(support, transform), support.getDensity());
                            body.setAwake(true);
                            support.clearChanged();
                        }
                    }
                    if (support.isDisposed()) {
                        if (scene.getSceneBuilder().isVerbose()) {
                            logger.log(Level.FINE, "{0}: Destroying a physics collision body in the world.", scene);
                        }
                        // destroy the body
                        final Body destroy = body;
                        body = body.getNext();
                        world.destroyBody(destroy);
                        continue;
                    }
                }
                // just move on to the next body
                body = body.getNext();
            }
            // iterate through the joints so that they can be updated
            Joint joint = world.getJointList();
            while (joint != null) {
                if (joint.getUserData() instanceof JBox2dJointSupport) {
                    final JBox2dJointSupport support = (JBox2dJointSupport) joint.getUserData();
                    // recreate the joint if necessary
                    if (support.isDisposed() || (JBox2dJointType.DISTANCE.equals(support.getType()) && support.isChanged())) {
                        if (support.isDisposed() && scene.getSceneBuilder().isVerbose()) {
                            logger.log(Level.FINE, "{0}: Destroying a physics joint body in the world.", scene);
                        }
                        // destroy the joint
                        final Joint destroy = joint;
                        joint = joint.getNext();
                        world.destroyJoint(destroy);
                        continue;
                    }
                    // update or recreate as necessary
                    if (support.isChanged() && !support.isDisposed()) {
                        support.clearChanged();
                        switch (support.getType()) {
                            case MOUSE:
                            case DISTANCE:
                                addJoint(support);
                                continue;
                            case REVOLUTE:
                                final JBox2dRevoluteJointSupport revolute = (JBox2dRevoluteJointSupport) joint.getUserData();
                                final RevoluteJoint revoluteJoint = (RevoluteJoint) joint;
                                if (revolute.isEnableMotor()) {
                                    revoluteJoint.setMaxMotorTorque(revolute.getMaxMotorTorque());
                                    revoluteJoint.setMotorSpeed(revolute.getMotorSpeed());
                                }
                                break;
                        }
                    }
                    // update the angle on revolute joints
                    if (JBox2dJointType.REVOLUTE.equals(support.getType()) && !support.isDisposed()) {
                        final JBox2dRevoluteJointSupport revolute = (JBox2dRevoluteJointSupport) joint.getUserData();
                        final RevoluteJoint revoluteJoint = (RevoluteJoint) joint;
                        if (revolute.isEnableAngle()) {
                            // set the motor speed to maintain a specific angle
                            // this will override setting the actual motor speed
                            final float angleError = revoluteJoint.getJointAngle() - (float) Math.toRadians(revolute.getAngle());
                            revoluteJoint.setMotorSpeed(-revolute.getGain() * angleError);
                        }
                    }
                }
                // just move on to the next joint
                joint = joint.getNext();
            }
        }
        // schedule the update pushes on the scene update thread
        UpdateTask.schedule(scene, updater);
        return (long) (duration - (steps * (1000.0 / 30.0)));
    }

    @Override
    public void update(final long delta) {
        if (!isPaused()) {
            // we are not paused and will step each time this is called
            modifiedDelta.addAndGet(delta);
            if (!updating.getAndSet(true)) {
                getPropertyChangeSupport().firePropertyChange(UPDATING, false, true);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            modifiedDelta.addAndGet(updatePhysics(modifiedDelta.getAndSet(0L)));
                        } catch (final RuntimeException ex) {
                            logger.log(Level.WARNING, MessageFormat.format("{0}: Exception thrown while updating the physics engine.", scene), ex);
                        } finally {
                            updating.set(false);
                            getPropertyChangeSupport().firePropertyChange(UPDATING, true, false);
                        }
                    }
                });
            }
        } else if (update.getAndSet(false) && !updating.getAndSet(true)) {
            getPropertyChangeSupport().firePropertyChange(UPDATING, false, true);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        modifiedDelta.addAndGet(updatePhysics(modifiedDelta.getAndSet(0L)));
                    } catch (final RuntimeException ex) {
                        logger.log(Level.WARNING, MessageFormat.format("{0}: Exception thrown while updating the physics engine.", scene), ex);
                    } finally {
                        updating.set(false);
                        getPropertyChangeSupport().firePropertyChange(UPDATING, true, false);
                    }
                }
            });
        }
    }

    /**
     * Step the physics engine while paused.
     *
     * @param duration the duration in milliseconds to step
     */
    public void step(final long duration) {
        update.set(true);
        modifiedDelta.set(duration);
    }

    private static void createFixture(final JBox2dPhysicsSupport support, final TransformSupport transform, final Body body) throws IllegalArgumentException {
        // create the fixture
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = support.getDensity();
        fixtureDef.friction = support.getFriction();
        fixtureDef.restitution = support.getRestitution();
        // determine the actor base shape
        fixtureDef.shape = JBox2dPhysicsEngine.createShape(support, transform);
        body.createFixture(fixtureDef);
    }

    private static Shape createShape(final JBox2dPhysicsSupport support, final TransformSupport transform) {
        final Vector3D transformScale = transform.getScale();
        final Vector3D supportScale = support.getScale();
        final Vector3D supportPadding = support.getPadding();
        if (support.getActor() instanceof RectangleShape
            || support.getActor() instanceof VolumeShape) {
            // object is rectangle
            final PolygonShape shape = new PolygonShape();
            final float w = (float) ((transformScale.getX() * supportScale.getX() * 0.5) + supportPadding.getX());
            final float h = (float) ((transformScale.getY() * supportScale.getY() * 0.5) + supportPadding.getY());
            shape.setAsBox(w <= 0.0f ? 0.0000001f : w, h <= 0.0000001f ? 0.0f : h, new Vec2(0.0f, 0.0f), 0.0f);
            return shape;
        } // TODO handle new text actors
        //        else if (support.getActor() instanceof TextActor) {
        //            // object is rectangle but doesn't follow the scale of the transform exactly
        //            final TextActor actor = (TextActor) support.getActor();
        //            final PolygonShape shape = new PolygonShape();
        //            final Rectangle2D bounds = actor.getBounds();
        //            final float w = (float) ((bounds.getWidth() * transformScale.getX() * supportScale.getX() * 0.5) + supportPadding.getX());
        //            final float h = (float) ((bounds.getHeight() * transformScale.getY() * supportScale.getY() * 0.5) + supportPadding.getY());
        //            final Vec2 center = new Vec2((float) (bounds.getX() * transformScale.getX() * supportScale.getX()),
        //                                         (float) (bounds.getY() * transformScale.getY() * supportScale.getY()));
        //            shape.setAsBox(w <= 0.0f ? 0.0000001f : w, h <= 0.0f ? 0.0000001f : h, center, 0.0f);
        //            return shape;
        //        }
        else if (support.getActor() instanceof RadialShape) {
            // object is a circle
            final CircleShape shape = new CircleShape();
            shape.m_p.set(new Vec2(0.0f, 0.0f));
            if (transformScale.getX() > transformScale.getY()) {
                final float r = (float) ((transformScale.getX() * supportScale.getX()) + supportPadding.getX());
                shape.m_radius = r <= 0.0f ? 0.0000001f : r;
            } else {
                final float r = (float) ((transformScale.getY() * supportScale.getY()) + supportPadding.getY());
                shape.m_radius = r <= 0.0f ? 0.0000001f : r;
            }
            return shape;
        } else if (support.getActor() instanceof ShapeActor || support.getActor() instanceof DynamicShapeActor) {
            final gov.pnnl.svf.geometry.Shape s;
            if (support.getActor() instanceof ShapeActor) {
                s = ((ShapeActor) support.getActor()).getShape();
            } else if (support.getActor() instanceof DynamicShapeActor) {
                s = ((DynamicShapeActor) support.getActor()).getShape();
            } else {
                s = null;
            }
            if (s != null) {
                if (s instanceof Circle2D) {
                    // object is a circle
                    final Circle2D c = (Circle2D) s;
                    final CircleShape shape = new CircleShape();
                    shape.m_p.set(new Vec2((float) c.getX(), (float) c.getY()));
                    if (transformScale.getX() > transformScale.getY()) {
                        final float r = (float) ((c.getRadius() * transformScale.getX() * supportScale.getX()) + supportPadding.getX());
                        shape.m_radius = r <= 0.0f ? 0.0000001f : r;
                    } else {
                        final float r = (float) ((c.getRadius() * transformScale.getY() * supportScale.getY()) + supportPadding.getY());
                        shape.m_radius = r <= 0.0f ? 0.0000001f : r;
                    }
                    return shape;
                } else if (s instanceof Shape2D) {
                    final Shape2D o = (Shape2D) s;
                    // object is rectangle
                    final PolygonShape shape = new PolygonShape();
                    final float w = (float) ((o.getWidth() * transformScale.getX() * supportScale.getX() * 0.5) + supportPadding.getX());
                    final float h = (float) ((o.getHeight() * transformScale.getY() * supportScale.getY() * 0.5) + supportPadding.getY());
                    shape.setAsBox(w <= 0.0f ? 0.0000001f : w, h <= 0.0000001f ? 0.0f : h, new Vec2((float) o.getX(), (float) o.getY()), 0.0f);
                    return shape;
                } else if (s instanceof Shape3D) {
                    final Shape3D o = (Shape3D) s;
                    // object is rectangle
                    final PolygonShape shape = new PolygonShape();
                    final float w = (float) ((o.getWidth() * transformScale.getX() * supportScale.getX() * 0.5) + supportPadding.getX());
                    final float h = (float) ((o.getHeight() * transformScale.getY() * supportScale.getY() * 0.5) + supportPadding.getY());
                    shape.setAsBox(w <= 0.0f ? 0.0000001f : w, h <= 0.0000001f ? 0.0f : h, new Vec2((float) o.getX(), (float) o.getY()), 0.0f);
                    return shape;
                } else {
                    // object is not supported
                    throw new IllegalArgumentException("Actor type is not supported by the JBox2d physics engine.");
                }
            } else {
                // object has no shape
                final PolygonShape shape = new PolygonShape();
                final float w = (float) ((transformScale.getX() * supportScale.getX() * 0.5) + supportPadding.getX());
                final float h = (float) ((transformScale.getY() * supportScale.getY() * 0.5) + supportPadding.getY());
                shape.setAsBox(w <= 0.0f ? 0.0000001f : w, h <= 0.0000001f ? 0.0f : h, new Vec2(0.0f, 0.0f), 0.0f);
                return shape;
            }
        } else if (support.getActor() instanceof BoundsShape<?>) {
            // object has boundary shape
            final PolygonShape shape = new PolygonShape();
            final float w = (float) ((transformScale.getX() * supportScale.getX() * 0.5) + supportPadding.getX());
            final float h = (float) ((transformScale.getY() * supportScale.getY() * 0.5) + supportPadding.getY());
            shape.setAsBox(w <= 0.0f ? 0.0000001f : w, h <= 0.0000001f ? 0.0f : h, new Vec2(0.0f, 0.0f), 0.0f);
            return shape;
        } else {
            // object is not supported
            throw new IllegalArgumentException("Actor type is not supported by the JBox2d physics engine: " + support.getActor().getClass().getSimpleName());
        }
    }

    private static class JBox2dContactFilterImpl implements JBox2dContactFilter {

        @Override
        public boolean shouldCollide(final JBox2dPhysicsSupport obj1, final JBox2dPhysicsSupport obj2) {
            return true;
        }
    }

    private static class ContactFilterImpl extends ContactFilter {

        private final JBox2dContactFilter contactFilter;

        private ContactFilterImpl(final JBox2dContactFilter contactFilter) {
            this.contactFilter = contactFilter;
        }

        @Override
        public boolean shouldCollide(final Fixture fixtureA, final Fixture fixtureB) {
            final JBox2dPhysicsSupport support1 = (JBox2dPhysicsSupport) fixtureA.m_body.m_userData;
            final JBox2dPhysicsSupport support2 = (JBox2dPhysicsSupport) fixtureB.m_body.m_userData;
            return contactFilter.shouldCollide(support1, support2);
        }
    }
}
