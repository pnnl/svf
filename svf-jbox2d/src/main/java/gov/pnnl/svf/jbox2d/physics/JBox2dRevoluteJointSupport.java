package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.Actor;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support object for creating a revolute joint between two actors.
 *
 * @author Arthur Bleeker
 */
public class JBox2dRevoluteJointSupport extends AbstractJBox2dMotorJointSupport {

    /**
     * String representation of a field in this object.
     */
    public static final String ANGLE = "angle";
    /**
     * String representation of a field in this object.
     */
    public static final String GAIN = "gain";
    private final boolean enableLimit;
    private final float lowerAngle;
    private final float upperAngle;
    private final boolean enableAngle;
    private float angle;
    private float gain;

    private JBox2dRevoluteJointSupport(final Actor actor, final JBox2dJointType type, final JBox2dPhysicsSupport support1, final JBox2dPhysicsSupport support2,
                                       final Vector3D anchor,
                                       final boolean enableLimit, final float lowerAngle, final float upperAngle,
                                       final boolean enableMotor, final float motorSpeed, final float maxMotorTorque,
                                       final boolean enableAngle, final float angle, final float gain) {
        super(actor, type, support1, support2, anchor, enableMotor, motorSpeed, maxMotorTorque);
        if (gain < 0.0f) {
            throw new IllegalArgumentException("gain");
        }
        // if (enableAngle && (angle < lowerAngle || angle > upperAngle)) {
        // throw new IllegalArgumentException("angle");
        // }
        this.enableLimit = enableLimit;
        this.lowerAngle = lowerAngle;
        this.upperAngle = upperAngle;
        this.enableAngle = enableAngle;
        this.angle = angle;
        this.gain = gain;
        getPropertyChangeSupport().addPropertyChangeListener(ANGLE, listener);
        getPropertyChangeSupport().addPropertyChangeListener(GAIN, listener);
    }

    /**
     * Constructor for a joint with no limit, motor, or desired angle.
     *
     * @param actor1 The primary actor
     * @param actor2 The secondary actor
     * @param anchor the anchor point for the joint in world coordinates
     *
     * @return the newly created support object
     */
    public static JBox2dRevoluteJointSupport newInstance(final Actor actor1, final Actor actor2, final Vector3D anchor) {
        if (actor1 == null) {
            throw new NullPointerException("actor1");
        }
        if (actor2 == null) {
            throw new NullPointerException("actor2");
        }
        final JBox2dPhysicsSupport support1 = actor1.lookup(JBox2dPhysicsSupport.class);
        final JBox2dPhysicsSupport support2 = actor2.lookup(JBox2dPhysicsSupport.class);
        final JBox2dRevoluteJointSupport instance = new JBox2dRevoluteJointSupport(actor1, JBox2dJointType.REVOLUTE, support1, support2, anchor, false, 0.0f,
                                                                                   0.0f, false, 0.0f, 0.0f, false, 0.0f, 0.0f);
        final JBox2dPhysicsEngine engine = actor1.getScene().lookup(JBox2dPhysicsEngine.class);
        if (engine != null) {
            engine.addJoint(instance);
        }
        return instance;
    }

    /**
     * Constructor for a joint with additional properties.
     *
     * @param actor1         the primary actor
     * @param actor2         the secondary actor
     * @param anchor         the anchor point for the joint in world coordinates
     * @param enableLimit
     * @param lowerAngle
     * @param upperAngle
     * @param enableMotor
     * @param motorSpeed
     * @param maxMotorTorque
     * @param enableAngle
     * @param angle
     * @param gain
     *
     * @return the newly created support object
     */
    public static JBox2dRevoluteJointSupport newInstance(final Actor actor1, final Actor actor2, final Vector3D anchor,
                                                         final boolean enableLimit, final float lowerAngle, final float upperAngle,
                                                         final boolean enableMotor, final float motorSpeed, final float maxMotorTorque,
                                                         final boolean enableAngle, final float angle, final float gain) {
        if (actor1 == null) {
            throw new NullPointerException("actor1");
        }
        if (actor2 == null) {
            throw new NullPointerException("actor2");
        }
        final JBox2dPhysicsSupport support1 = actor1.lookup(JBox2dPhysicsSupport.class);
        final JBox2dPhysicsSupport support2 = actor2.lookup(JBox2dPhysicsSupport.class);
        final JBox2dRevoluteJointSupport instance = new JBox2dRevoluteJointSupport(actor1, JBox2dJointType.REVOLUTE, support1, support2, anchor, enableLimit,
                                                                                   lowerAngle, upperAngle, enableMotor, motorSpeed, maxMotorTorque, enableAngle, angle, gain);
        return instance;
    }

    /**
     * @return the desired angle
     */
    public float getAngle() {
        synchronized (this) {
            return angle;
        }
    }

    /**
     * @return the amount of torque to use to maintain the desired angle
     */
    public float getGain() {
        synchronized (this) {
            return gain;
        }
    }

    /**
     * @return the lower allowed angle
     */
    public float getLowerAngle() {
        return lowerAngle;
    }

    /**
     * @return the upper allowed angle
     */
    public float getUpperAngle() {
        return upperAngle;
    }

    /**
     * @return true if this joint will attempt to maintain a set angle
     */
    public boolean isEnableAngle() {
        return enableAngle;
    }

    /**
     * @return true if this joint will limit the angle
     */
    public boolean isEnableLimit() {
        return enableLimit;
    }

    /**
     * @param angle the desired angle
     */
    public void setAngle(final float angle) {
        // if (enableAngle && (angle < lowerAngle || angle > upperAngle)) {
        // throw new IllegalArgumentException("angle");
        // }
        final float old;
        synchronized (this) {
            old = this.angle;
            this.angle = angle;
        }
        getPropertyChangeSupport().firePropertyChange(ANGLE, old, angle);
    }

    /**
     * @param gain the amount of torque to use to maintain the desired angle
     */
    public void setGain(final float gain) {
        if (gain < 0.0f) {
            throw new IllegalArgumentException("gain");
        }
        final float old;
        synchronized (this) {
            old = this.gain;
            this.gain = gain;
        }
        getPropertyChangeSupport().firePropertyChange(GAIN, old, gain);
    }
}
