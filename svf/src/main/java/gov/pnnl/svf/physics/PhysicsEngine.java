package gov.pnnl.svf.physics;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.Updatable;
import gov.pnnl.svf.support.TransformSupport;
import java.util.Collection;
import java.util.HashSet;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support object for enabling physics in a scene. This object must be added to
 * the scene to enable physics. This physics engine currently only supports
 * keeping an object inside of the scene boundaries.
 *
 * @author Amelia Bleeker
 *
 */
public class PhysicsEngine implements Updatable, Disposable {

    private final static double FLUID_DENSITY = 10.0;
    private final Collection<PhysicsSupport> movables = new HashSet<>();
    private final Collection<CollisionSupport> collisionables = new HashSet<>();
    private final Scene scene;
    private boolean disposed = false;

    /**
     * Constructor
     *
     * @param scene reference to the scene implementing the physics
     */
    public PhysicsEngine(final Scene scene) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
    }

    @Override
    public void update(final long delta) {
        // update all of the physics
        scene.lookupAll(PhysicsSupport.class, movables);
        for (final PhysicsSupport support : movables) {
            applyPhysics(delta, support);
        }
        // update all of the collisions
        scene.lookupAll(CollisionSupport.class, collisionables);
        for (final CollisionSupport support : collisionables) {
            checkCollisions(support);
        }
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return disposed;
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            disposed = true;
        }
        movables.clear();
        collisionables.clear();
    }

    private void applyPhysics(final long delta, final PhysicsSupport support) {
        // calculate change in time
        final double t = delta / 1000.0;
        // create an acceleration vector from the accumulated forces
        Vector3D forces = new Vector3D(1.0 / support.getMass(), support.getForces());
        final Vector3D acceleration = forces;
        forces = Vector3D.ZERO;
        // scale the acceleration according to the time change
        final Vector3D a = new Vector3D(t, acceleration);
        // add the acceleration to the current velocity
        Vector3D velocity = support.getVelocity().add(a);
        // limit the max velocity
        double speed = velocity.getNorm();
        if (speed > support.getMaxSpeed()) {
            speed = support.getMaxSpeed();
            velocity = new Vector3D(support.getMaxSpeed(), velocity.normalize());
        }
        // apply drag only if it has drag and speed
        double drag;
        if ((support.getDragCoefficient() > 0.0f) && (speed > 0.0f)) {
            // calculate drag
            double scale = 0.5f * FLUID_DENSITY * speed * speed * support.getDragArea() * support.getDragCoefficient();
            // scale for mass and time
            scale *= 1.0f / support.getMass() * t;
            Vector3D dragVector = new Vector3D(scale, velocity.normalize());
            drag = dragVector.getNorm();
            if (drag > speed) {
                dragVector = new Vector3D(speed, dragVector.normalize());
            }
            velocity = velocity.subtract(dragVector);
            speed = velocity.getNorm();
        } else {
            drag = 0.0f;
        }
        // apply the constant kinetic friction
        double friction;
        if ((support.getCoefficientOfFriction() > 0.0f) && (speed > 0.0f)) {
            Vector3D frictionVector = new Vector3D(support.getMass() * support.getCoefficientOfFriction() * t, velocity.normalize());
            friction = frictionVector.getNorm();
            if (friction > speed) {
                frictionVector = new Vector3D(speed, frictionVector.normalize());
            }
            velocity = velocity.subtract(frictionVector);
            speed = velocity.getNorm();
        } else {
            friction = 0.0;
        }
        // update the values in the support object
        support.setForces(forces);
        support.setVelocity(velocity);
        support.setAcceleration(acceleration);
        support.setSpeed(speed);
        support.setDrag(drag);
        support.setFriction(friction);
        // apply the translation
        final TransformSupport transform = support.getActor().lookup(TransformSupport.class);
        if (transform != null) {
            transform.setTranslation(transform.getTranslation().add(new Vector3D(t, velocity)));
        }
    }

    /**
     * Checks for collisions with the scene boundaries and other actors in the
     * scene. This function will apply the appropriate forces when collision
     * occurs.
     *
     * This is really a place holder function for a very specific purpose:
     * keeping objects inside the scene.
     *
     * @param support The collision support object.
     */
    private void checkCollisions(final CollisionSupport support) {
        final Actor actor = support.getActor();
        if (actor == null) {
            return;
        }
        final Vector3D boundary = actor.getScene().getBoundary();
        final Vector3D center = actor.getScene().getCenter();
        final TransformSupport transform = actor.lookup(TransformSupport.class);
        if (transform != null) {
            // get reference to a physics object if it has one
            final PhysicsSupport physics = actor.lookup(PhysicsSupport.class);
            // check for scene boundary collisions
            if (support.containsCollisionType("scene")) {
                // create actor variables
                final double aWidth = transform.getScale().getX() / 2.0;
                final double aHeight = transform.getScale().getY() / 2.0;
                final double aDepth = transform.getScale().getZ() / 2.0;
                final double aLeft = transform.getTranslation().getX() - aWidth;
                final double aRight = transform.getTranslation().getX() + aWidth;
                final double aTop = transform.getTranslation().getY() + aHeight;
                final double aBottom = transform.getTranslation().getY() - aHeight;
                final double aFront = transform.getTranslation().getZ() - aDepth;
                final double aBack = transform.getTranslation().getZ() + aDepth;
                // create scene variables
                final double sWidth = boundary.getX() / 2.0;
                final double sHeight = boundary.getY() / 2.0;
                final double sDepth = boundary.getZ() / 2.0;
                final double sLeft = center.getX() - sWidth;
                final double sRight = center.getX() + sWidth;
                final double sTop = center.getY() + sHeight;
                final double sBottom = center.getY() - sHeight;
                final double sFront = center.getZ() - sDepth;
                final double sBack = center.getZ() + sDepth;
                // check the boundaries
                if (aLeft < sLeft) {
                    transform.setTranslation(new Vector3D(sLeft + aWidth, transform.getTranslation().getY(), transform.getTranslation().getZ()));
                    if (physics != null) {
                        physics.setVelocity(new Vector3D(0.0, physics.getVelocity().getY(), physics.getVelocity().getZ()));
                    }
                } else if (aRight > sRight) {
                    transform.setTranslation(new Vector3D(sRight - aWidth, transform.getTranslation().getY(), transform.getTranslation().getZ()));
                    if (physics != null) {
                        physics.setVelocity(new Vector3D(0.0, physics.getVelocity().getY(), physics.getVelocity().getZ()));
                    }
                }
                if (aTop > sTop) {
                    transform.setTranslation(new Vector3D(transform.getTranslation().getX(), sTop - aHeight, transform.getTranslation().getZ()));
                    if (physics != null) {
                        physics.setVelocity(new Vector3D(physics.getVelocity().getX(), 0.0, physics.getVelocity().getZ()));
                    }
                } else if (aBottom < sBottom) {
                    transform.setTranslation(new Vector3D(transform.getTranslation().getX(), sBottom + aHeight, transform.getTranslation().getZ()));
                    if (physics != null) {
                        physics.setVelocity(new Vector3D(physics.getVelocity().getX(), 0.0, physics.getVelocity().getZ()));
                    }
                }
                if (aFront < sFront) {
                    transform.setTranslation(new Vector3D(transform.getTranslation().getX(), transform.getTranslation().getY(), sFront + aDepth));
                    if (physics != null) {
                        physics.setVelocity(new Vector3D(physics.getVelocity().getX(), physics.getVelocity().getY(), 0.0));
                    }
                } else if (aBack > sBack) {
                    transform.setTranslation(new Vector3D(transform.getTranslation().getX(), transform.getTranslation().getY(), sBack - aDepth));
                    if (physics != null) {
                        physics.setVelocity(new Vector3D(physics.getVelocity().getX(), physics.getVelocity().getY(), 0.0));
                    }
                }
            }
        }
    }
}
