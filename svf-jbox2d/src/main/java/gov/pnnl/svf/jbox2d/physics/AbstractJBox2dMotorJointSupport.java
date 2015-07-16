package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.Actor;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Abstract support object for joints between two actors that utilize a motor.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractJBox2dMotorJointSupport extends JBox2dJointSupport {

    /**
     * String representation of a field in this object.
     */
    public static final String MOTOR_SPEED = "motorSpeed";
    /**
     * String representation of a field in this object.
     */
    public static final String MAX_MOTOR_TORQUE = "maxMotorTorque";
    private final Vector3D anchor;
    private final boolean enableMotor;
    private float motorSpeed;
    private float maxMotorTorque;

    /**
     * Constructor
     *
     * @param actor          reference to the main actor that will determine
     *                       visibility and lifecycle
     * @param type           the joint type
     * @param support1       first actor support object
     * @param support2       second actor support object
     * @param anchor         the anchor
     * @param enableMotor    true to enable the motor
     * @param motorSpeed     the motor speed
     * @param maxMotorTorque the maximum motor torque
     */
    protected AbstractJBox2dMotorJointSupport(final Actor actor, final JBox2dJointType type, final JBox2dPhysicsSupport support1,
                                              final JBox2dPhysicsSupport support2,
                                              final Vector3D anchor, final boolean enableMotor, final float motorSpeed, final float maxMotorTorque) {
        super(actor, type, support1, support2);
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }
        if (maxMotorTorque < 0.0f) {
            throw new IllegalArgumentException("maxMotorTorque");
        }
        this.anchor = anchor;
        this.enableMotor = enableMotor;
        this.motorSpeed = motorSpeed;
        this.maxMotorTorque = maxMotorTorque;
        getPropertyChangeSupport().addPropertyChangeListener(MOTOR_SPEED, listener);
        getPropertyChangeSupport().addPropertyChangeListener(MAX_MOTOR_TORQUE, listener);
    }

    /**
     * @return the anchor for this joint in world coordinates
     */
    public Vector3D getAnchor() {
        return anchor;
    }

    /**
     * @return the maximum torque that the motor can apply
     */
    public float getMaxMotorTorque() {
        synchronized (this) {
            return maxMotorTorque;
        }
    }

    /**
     * @return the current motor speed
     */
    public float getMotorSpeed() {
        synchronized (this) {
            return motorSpeed;
        }
    }

    /**
     * @return true if this joint has the motor enabled
     */
    public boolean isEnableMotor() {
        return enableMotor;
    }

    /**
     * @param maxMotorTorque the maximum torque that the motor can apply
     */
    public void setMaxMotorTorque(final float maxMotorTorque) {
        if (maxMotorTorque < 0.0f) {
            throw new IllegalArgumentException("maxMotorTorque");
        }
        final float old;
        synchronized (this) {
            old = this.maxMotorTorque;
            this.maxMotorTorque = maxMotorTorque;
        }
        getPropertyChangeSupport().firePropertyChange(MAX_MOTOR_TORQUE, old, maxMotorTorque);
    }

    /**
     * @param motorSpeed the current motor speed
     */
    public void setMotorSpeed(final float motorSpeed) {
        final float old;
        synchronized (this) {
            old = this.motorSpeed;
            this.motorSpeed = motorSpeed;
        }
        getPropertyChangeSupport().firePropertyChange(MOTOR_SPEED, old, motorSpeed);
    }
}
