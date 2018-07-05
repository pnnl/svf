package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.Actor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Abstract support object for working with JBox2d physics.
 *
 * @author Amelia Bleeker
 */
public abstract class JBox2dJointSupport extends AbstractJBox2dSupport<Object> {

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
     * Listener used to set the changed field to true; notify of a change.
     */
    protected final PropertyChangeListener listener = new ChangeListener();
    private final JBox2dJointType type;
    private final JBox2dPhysicsSupport support1;
    private final JBox2dPhysicsSupport support2;
    private Object physicsData = null;
    private boolean disposed = false;
    private boolean changed = false;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor    The actor that owns this support object.
     * @param type     the type of physics joint
     * @param support1 physics support object for the first actor
     * @param support2 physics support object for the second actor
     */
    protected JBox2dJointSupport(final Actor actor, final JBox2dJointType type, final JBox2dPhysicsSupport support1, final JBox2dPhysicsSupport support2) {
        super(actor);
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        if (support1 == null) {
            throw new NullPointerException("support1");
        }
        if (support2 == null) {
            throw new NullPointerException("support2");
        }
        this.type = type;
        this.support1 = support1;
        this.support2 = support2;
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
        super.dispose();
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
     * @return the first support object
     */
    public JBox2dPhysicsSupport getSupport1() {
        return support1;
    }

    /**
     * @return the second support object
     */
    public JBox2dPhysicsSupport getSupport2() {
        return support2;
    }

    /**
     * @return the physics type
     */
    public JBox2dJointType getType() {
        return type;
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

    private class ChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            setChanged(true);
        }
    }
}
