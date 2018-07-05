package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.support.TransformSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support object for enabling physics on an actor.
 *
 * @author Amelia
 */
public class JBox2dPhysicsSupport extends AbstractJBox2dSupport<Object> {

    /**
     * String representation of a field in this object.
     */
    public static final String PHYSICS_DATA = "physicsData";
    /**
     * String representation of a field in this object.
     */
    public static final String DISPOSED = "disposed";
    /**
     * String representation of a field in this object.
     */
    public static final String CHANGED = "changed";
    /**
     * String representation of a field in this object.
     */
    public static final String IMPULSE = "impulse";
    /**
     * String representation of a field in this object.
     */
    public static final String SCALE = "scale";
    /**
     * String representation of a field in this object.
     */
    public static final String PADDING = "padding";
    /**
     * String representation of a field in this object.
     */
    public static final String APPLY_ROTATION = "applyRotation";
    private static final Vector3D ONE = new Vector3D(1.0, 1.0, 1.0);
    private final PropertyChangeListener listener = new ChangeListener();
    private final JBox2dPhysicsType type;
    private final float density;
    private final float friction;
    private final float damping;
    private final float restitution;
    private Object physicsData = null;
    private boolean disposed = false;
    private boolean changed = false;
    private Vector3D impulse = Vector3D.ZERO;
    private Vector3D scale = ONE;
    private Vector3D padding = Vector3D.ZERO;
    private boolean applyRotation = true;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor       The actor that owns this support object.
     * @param type        the type of physics actor
     * @param density     the density of the actor, must be positive
     * @param friction    the coefficient of friction, must be an number from 0
     *                    to 1 inclusive
     * @param damping     the damping coefficient or fluid friction, must be an
     *                    number from 0 to 1 inclusive
     * @param restitution the coefficient of restitution or elasticity of the
     *                    actor, must be a number from 0 to 1 inclusive
     */
    protected JBox2dPhysicsSupport(final Actor actor, final JBox2dPhysicsType type, final float density, final float friction, final float damping,
                                   final float restitution) {
        super(actor);
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        if (density < 0.0f) {
            throw new IllegalArgumentException("mass");
        }
        if (friction < 0.0f || friction > 1.0f) {
            throw new IllegalArgumentException("friction");
        }
        if (damping < 0.0f || damping > 1.0f) {
            throw new IllegalArgumentException("damping");
        }
        if (restitution < 0.0f || restitution > 1.0f) {
            throw new IllegalArgumentException("restitution");
        }
        this.type = type;
        this.density = density;
        this.friction = friction;
        this.damping = damping;
        this.restitution = restitution;
        final TransformSupport transform = actor.lookup(TransformSupport.class);
        if (transform == null) {
            throw new IllegalArgumentException("Actor must have a TransformSupport object in its lookup in order to use JBox2dPhysicsSupport.");
        }
        transform.getPropertyChangeSupport().addPropertyChangeListener(TransformSupport.SCALE, listener);
        getPropertyChangeSupport().addPropertyChangeListener(SCALE, listener);
        getPropertyChangeSupport().addPropertyChangeListener(PADDING, listener);
        // TODO handle new text actors
        //        if (actor instanceof TextActor) {
        //            actor.getPropertyChangeSupport().addPropertyChangeListener(BoundsShape.BOUNDS, listener);
        //        }
    }

    /**
     * Creates a new PhysicsSupport object for the actor.
     *
     * @param actor       The actor that owns this support object.
     * @param type        the type of physics actor
     * @param density     the density of the actor, must be positive
     * @param friction    the coefficient of friction, must be an number from 0
     *                    to 1 inclusive
     * @param damping     the damping coefficient or fluid friction, must be an
     *                    number from 0 to 1 inclusive
     * @param restitution the coefficient of restitution or elasticity of the
     *                    actor, must be a number from 0 to 1 inclusive
     *
     * @return the new instance
     */
    public static JBox2dPhysicsSupport newInstance(final Actor actor, final JBox2dPhysicsType type, final float density, final float friction,
                                                   final float damping, final float restitution) {
        final JBox2dPhysicsSupport instance = new JBox2dPhysicsSupport(actor, type, density, friction, damping, restitution);
        actor.add(instance);
        final JBox2dPhysicsEngine engine = actor.getScene().lookup(JBox2dPhysicsEngine.class);
        if (engine != null) {
            engine.addPhysics(instance);
        }
        return instance;
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * Clears the changed status.
     */
    public void clearChanged() {
        setChanged(false);
    }

    @Override
    public void dispose() {
        setDisposed(true);
        final TransformSupport transform = getActor().lookup(TransformSupport.class);
        if (transform != null) {
            transform.getPropertyChangeSupport().removePropertyChangeListener(TransformSupport.SCALE, listener);
        }
        // handle new text actors
        //        if (getActor() instanceof TextActor) {
        //            getActor().getPropertyChangeSupport().removePropertyChangeListener(BoundsShape.BOUNDS, listener);
        //        }
        super.dispose();
    }

    /**
     * @return the damping
     */
    public float getDamping() {
        return damping;
    }

    /**
     * @return the density
     */
    public float getDensity() {
        return density;
    }

    /**
     * @return the friction
     */
    public float getFriction() {
        return friction;
    }

    /**
     * @return the impulse to add to this actor
     */
    public Vector3D getImpulse() {
        synchronized (this) {
            return impulse;
        }
    }

    /**
     * @return the scale padding for the collision bounding box of this actor
     */
    public Vector3D getPadding() {
        synchronized (this) {
            return padding;
        }
    }

    /**
     * @return the physics data object
     */
    public Object getPhysicsData() {
        synchronized (this) {
            return physicsData;
        }
    }

    /**
     * @return the restitution
     */
    public float getRestitution() {
        return restitution;
    }

    /**
     * @return the scale multiplier for the collision bounding box of this actor
     */
    public Vector3D getScale() {
        synchronized (this) {
            return scale;
        }
    }

    /**
     * @return the physics type
     */
    public JBox2dPhysicsType getType() {
        return type;
    }

    /**
     * @return true if the physics should apply rotation to the transform
     */
    public boolean isApplyRotation() {
        synchronized (this) {
            return applyRotation;
        }
    }

    /**
     * @return true if actor objects that this support object is interested in
     *         have changed
     */
    public boolean isChanged() {
        synchronized (this) {
            return changed;
        }
    }

    /**
     * @return true if disposed
     */
    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return disposed;
        }
    }

    /**
     * @param applyRotation set to true to apply rotation to the transform
     */
    public void setApplyRotation(final boolean applyRotation) {
        final boolean old;
        synchronized (this) {
            old = this.applyRotation;
            this.applyRotation = applyRotation;
        }
        getPropertyChangeSupport().firePropertyChange(APPLY_ROTATION, old, applyRotation);
    }

    private void setChanged(final boolean changed) {
        final boolean old;
        synchronized (this) {
            old = this.changed;
            this.changed = changed;
        }
        getPropertyChangeSupport().firePropertyChange(CHANGED, old, changed);
    }

    private void setDisposed(final boolean disposed) {
        final boolean old;
        synchronized (this) {
            old = this.disposed;
            this.disposed = disposed;
        }
        getPropertyChangeSupport().firePropertyChange(DISPOSED, old, disposed);
    }

    /**
     * @param impulse the impulse to add to this actor
     */
    public void setImpulse(final Vector3D impulse) {
        if (impulse == null) {
            throw new NullPointerException("impulse");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.impulse;
            this.impulse = impulse;
        }
        getPropertyChangeSupport().firePropertyChange(IMPULSE, old, impulse);
    }

    /**
     * @param padding the scale padding for the collision bounding box of this
     *                actor
     */
    public void setPadding(final Vector3D padding) {
        if (padding == null) {
            throw new NullPointerException("padding");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.padding;
            this.padding = padding;
        }
        getPropertyChangeSupport().firePropertyChange(PADDING, old, padding);
    }

    /**
     * @param physicsData the physics data object
     */
    public void setPhysicsData(final Object physicsData) {
        final Object old;
        synchronized (this) {
            old = this.physicsData;
            this.physicsData = physicsData;
        }
        getPropertyChangeSupport().firePropertyChange(PHYSICS_DATA, old, physicsData);
    }

    /**
     * @param scale the scale multiplier for the collision bounding box of this
     *              actor
     */
    public void setScale(final Vector3D scale) {
        if (scale == null) {
            throw new NullPointerException("scale");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.scale;
            this.scale = scale;
        }
        getPropertyChangeSupport().firePropertyChange(SCALE, old, scale);
    }

    private class ChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            setChanged(true);
        }
    }
}
