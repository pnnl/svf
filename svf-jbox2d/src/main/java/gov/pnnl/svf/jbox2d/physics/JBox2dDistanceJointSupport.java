package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.Actor;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support object for creating a distance joint between two actors.
 *
 * @author Arthur
 */
public class JBox2dDistanceJointSupport extends JBox2dJointSupport {

    /**
     * String representation of a field in this object.
     */
    public static final String ANCHOR_1 = "anchor1";
    /**
     * String representation of a field in this object.
     */
    public static final String ANCHOR_2 = "anchor2";
    private final float damping;
    private final float frequency;
    private Vector3D anchor1;
    private Vector3D anchor2;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor     The actor that owns this support object.
     * @param type      the type of physics joint
     * @param support1  physics support object for the first actor
     * @param anchor1   anchor point on the first actor
     * @param support2  physics support object for the second actor
     * @param anchor2   anchor point on the second actor
     * @param frequency frequency of the tension in Hz, must be positive
     * @param damping   damping of the joint, must be between 0 and 1 inclusive
     */
    protected JBox2dDistanceJointSupport(final Actor actor, final JBox2dJointType type, final JBox2dPhysicsSupport support1, final Vector3D anchor1,
                                         final JBox2dPhysicsSupport support2, final Vector3D anchor2, final float frequency, final float damping) {
        super(actor, type, support1, support2);
        if (anchor1 == null) {
            throw new NullPointerException("anchor1");
        }
        if (anchor2 == null) {
            throw new NullPointerException("anchor2");
        }
        if (frequency <= 0.0f) {
            throw new IllegalArgumentException("frequency");
        }
        if (damping < 0.0f || damping > 1.0f) {
            throw new IllegalArgumentException("damping");
        }
        this.anchor1 = anchor1;
        this.anchor2 = anchor2;
        this.damping = damping;
        this.frequency = frequency;
        getPropertyChangeSupport().addPropertyChangeListener(ANCHOR_1, listener);
        getPropertyChangeSupport().addPropertyChangeListener(ANCHOR_2, listener);
    }

    /**
     * Constructor
     *
     * @param actor     The actor that owns this support object.
     * @param support1  physics support object for the first actor
     * @param anchor1   anchor point on the first actor
     * @param support2  physics support object for the second actor
     * @param anchor2   anchor point on the second actor
     * @param frequency frequency of the tension in Hz, must be positive
     * @param damping   damping of the joint, must be between 0 and 1 inclusive
     *
     * @return a new support object
     */
    public static JBox2dDistanceJointSupport newInstance(final Actor actor, final JBox2dPhysicsSupport support1, final Vector3D anchor1,
                                                         final JBox2dPhysicsSupport support2, final Vector3D anchor2, final float frequency, final float damping) {
        final JBox2dDistanceJointSupport instance = new JBox2dDistanceJointSupport(actor, JBox2dJointType.DISTANCE, support1, anchor1, support2, anchor2,
                                                                                   frequency, damping);
        actor.add(instance);
        final JBox2dPhysicsEngine engine = actor.getScene().lookup(JBox2dPhysicsEngine.class);
        if (engine != null) {
            engine.addJoint(instance);
        }
        return instance;
    }

    /**
     * @return the first anchor point
     */
    public Vector3D getAnchor1() {
        synchronized (this) {
            return anchor1;
        }
    }

    /**
     * @return the second anchor point
     */
    public Vector3D getAnchor2() {
        synchronized (this) {
            return anchor2;
        }
    }

    /**
     * @return the damping
     */
    public float getDamping() {
        return damping;
    }

    /**
     * @return the frequency
     */
    public float getFrequency() {
        return frequency;
    }

    /**
     * @param anchor1 the first anchor point
     */
    public void setAnchor1(final Vector3D anchor1) {
        if (anchor1 == null) {
            throw new NullPointerException("anchor1");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.anchor1;
            this.anchor1 = anchor1;
        }
        getPropertyChangeSupport().firePropertyChange(ANCHOR_1, old, anchor1);
    }

    /**
     * @param anchor2 the second anchor point
     */
    public void setAnchor2(final Vector3D anchor2) {
        if (anchor2 == null) {
            throw new NullPointerException("anchor2");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.anchor2;
            this.anchor2 = anchor2;
        }
        getPropertyChangeSupport().firePropertyChange(ANCHOR_2, old, anchor2);
    }
}
