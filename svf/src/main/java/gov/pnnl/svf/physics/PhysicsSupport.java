package gov.pnnl.svf.physics;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.support.AbstractSupport;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Allows the actor to be moved about a scene by using physics. The actor must
 * have TransformSupport in order to be moved by physics. This support object is
 * intended for use by the standard physics engine.
 *
 * @author Amelia Bleeker
 *
 */
public class PhysicsSupport extends AbstractSupport<Object> {

    // zero mass doesn't work in this type of physics model
    private final static double MINIMUM_MASS = 0.0001;
    /**
     * String representation of a field in this object.
     */
    public static final String MAX_SPEED = "maxSpeed";
    /**
     * String representation of a field in this object.
     */
    public static final String SPEED = "speed";
    /**
     * String representation of a field in this object.
     */
    public static final String DRAG = "drag";
    /**
     * String representation of a field in this object.
     */
    public static final String FRICTION = "friction";
    /**
     * String representation of a field in this object.
     */
    public static final String MASS = "mass";
    /**
     * String representation of a field in this object.
     */
    public static final String DRAG_AREA = "dragArea";
    /**
     * String representation of a field in this object.
     */
    public static final String DRAG_COEFFICIENT = "dragCoefficient";
    /**
     * String representation of a field in this object.
     */
    public static final String COEFFICIENT_OF_FRICTION = "coefficientOfFriction";

    /**
     * Creates a new PhysicsSupport object for the actor.
     *
     * @param actor The actor that owns this support object.
     *
     * @return the new instance
     */
    public static PhysicsSupport newInstance(final Actor actor) {
        final PhysicsSupport instance = new PhysicsSupport(actor);
        actor.add(instance);
        return instance;
    }

    private Vector3D forces = Vector3D.ZERO;
    private Vector3D acceleration = Vector3D.ZERO;
    private Vector3D velocity = Vector3D.ZERO;
    private double maxSpeed = 1.0;
    private double speed = 0.0;
    private double drag = 0.0;
    private double friction = 0.0;
    private double mass = 1.0;
    private double dragArea = 1.0;
    private double dragCoefficient = 0.5;
    private double coefficientOfFriction = 0.0;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The actor that owns this support object.
     */
    protected PhysicsSupport(final Actor actor) {
        super(actor);
    }

    /**
     * @param force The force to apply to this actor.
     */
    public void addForce(final Vector3D force) {
        if (force == null) {
            throw new NullPointerException("impulse");
        }
        synchronized (this) {
            forces = forces.add(force);
        }
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * @return the acceleration
     */
    public Vector3D getAcceleration() {
        synchronized (this) {
            return acceleration;
        }
    }

    /**
     * Creates friction applied to the actor as if it is always sliding across a
     * surface.
     *
     * @return the coefficientOfFriction
     */
    public double getCoefficientOfFriction() {
        synchronized (this) {
            return coefficientOfFriction;
        }
    }

    /**
     * @return the drag
     */
    public double getDrag() {
        synchronized (this) {
            return drag;
        }
    }

    /**
     * @return the dragArea
     */
    public double getDragArea() {
        synchronized (this) {
            return dragArea;
        }
    }

    /**
     * @return the dragCoefficient
     */
    public double getDragCoefficient() {
        synchronized (this) {
            return dragCoefficient;
        }
    }

    /**
     * @return the the combined forces that are currently acting on this actor
     */
    public Vector3D getForces() {
        synchronized (this) {
            return forces;
        }
    }

    /**
     * @return the friction
     */
    public double getFriction() {
        synchronized (this) {
            return friction;
        }
    }

    /**
     * @return the mass
     */
    public double getMass() {
        synchronized (this) {
            return mass;
        }
    }

    /**
     * @return the max speed
     */
    public double getMaxSpeed() {
        synchronized (this) {
            return maxSpeed;
        }
    }

    /**
     * @return the current speed of the actor
     */
    public double getSpeed() {
        synchronized (this) {
            return speed;
        }
    }

    /**
     * @return the velocity
     */
    public Vector3D getVelocity() {
        synchronized (this) {
            return velocity;
        }
    }

    /**
     * @param acceleration the acceleration to set
     */
    public void setAcceleration(final Vector3D acceleration) {
        synchronized (this) {
            this.acceleration = acceleration;
        }
    }

    /**
     * Creates friction applied to the actor as if it is always sliding across a
     * surface.
     *
     * @param coefficientOfFriction the coefficientOfFriction to set
     *
     * @return a reference to the support object
     */
    public PhysicsSupport setCoefficientOfFriction(final double coefficientOfFriction) {
        double old;
        synchronized (this) {
            old = this.coefficientOfFriction;
            this.coefficientOfFriction = coefficientOfFriction;
        }
        getPropertyChangeSupport().firePropertyChange(COEFFICIENT_OF_FRICTION, old, coefficientOfFriction);
        return this;
    }

    /**
     * @param drag the drag to set
     */
    public void setDrag(final double drag) {
        synchronized (this) {
            this.drag = drag;
        }
    }

    /**
     * @param dragArea the dragArea to set
     *
     * @return a reference to the support object
     */
    public PhysicsSupport setDragArea(final double dragArea) {
        double old;
        synchronized (this) {
            old = this.dragArea;
            this.dragArea = dragArea;
        }
        getPropertyChangeSupport().firePropertyChange(DRAG_AREA, old, dragArea);
        return this;
    }

    /**
     * @param dragCoefficient the dragCoefficient to set
     *
     * @return a reference to the support object
     */
    public PhysicsSupport setDragCoefficient(final double dragCoefficient) {
        double temp = dragCoefficient;
        if (temp < 0.0) {
            temp = 0.0;
        }
        if (temp > 1.0) {
            temp = 1.0;
        }
        double old;
        synchronized (this) {
            old = this.dragCoefficient;
            this.dragCoefficient = temp;
        }
        getPropertyChangeSupport().firePropertyChange(DRAG_COEFFICIENT, old, dragCoefficient);
        return this;
    }

    /**
     * @param forces the forces to set
     */
    public void setForces(final Vector3D forces) {
        synchronized (this) {
            this.forces = forces;
        }
    }

    /**
     * @param friction the friction to set
     */
    public void setFriction(final double friction) {
        synchronized (this) {
            this.friction = friction;
        }
    }

    /**
     * @param mass the mass to set
     *
     * @return a reference to the support object
     */
    public PhysicsSupport setMass(final double mass) {
        double old;
        synchronized (this) {
            old = this.mass;
            if (mass < MINIMUM_MASS) {
                this.mass = MINIMUM_MASS;
            } else {
                this.mass = mass;
            }
        }
        getPropertyChangeSupport().firePropertyChange(MASS, old, mass);
        return this;
    }

    /**
     * @param maxSpeed the max speed to set
     */
    public void setMaxSpeed(final double maxSpeed) {
        double old;
        synchronized (this) {
            old = this.maxSpeed;
            this.maxSpeed = maxSpeed;
        }
        getPropertyChangeSupport().firePropertyChange(MAX_SPEED, old, maxSpeed);
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(final double speed) {
        synchronized (this) {
            this.speed = speed;
        }
    }

    /**
     * @param velocity the current velocity of the actor
     */
    public void setVelocity(final Vector3D velocity) {
        if (velocity == null) {
            throw new NullPointerException("velocity");
        }
        synchronized (this) {
            this.velocity = velocity;
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            final StringBuilder builder = new StringBuilder();
            builder.append("PhysicsSupport [acceleration=");
            builder.append(acceleration);
            builder.append(", coefficientOfFriction=");
            builder.append(coefficientOfFriction);
            builder.append(", drag=");
            builder.append(drag);
            builder.append(", dragArea=");
            builder.append(dragArea);
            builder.append(", dragCoefficient=");
            builder.append(dragCoefficient);
            builder.append(", forces=");
            builder.append(forces);
            builder.append(", friction=");
            builder.append(friction);
            builder.append(", mass=");
            builder.append(mass);
            builder.append(", maxSpeed=");
            builder.append(maxSpeed);
            builder.append(", speed=");
            builder.append(speed);
            builder.append(", velocity=");
            builder.append(velocity);
            builder.append("]");
            return builder.toString();
        }
    }
}
