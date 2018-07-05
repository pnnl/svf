package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;

/**
 * This class is used to provide a reference to the parent of a class that is
 * added as a child with ChildSupport.
 *
 * @author Amelia Bleeker
 */
public class ParentSupport extends AbstractSupport<Object> {

    /**
     * String representation of a field in this object.
     */
    public static final String PARENT = "parent";
    private Actor parent = null;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The owning actor.
     */
    protected ParentSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor The actor that owns this support instance.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    static ParentSupport newInstance(final Actor actor) {
        final ParentSupport instance = new ParentSupport(actor);
        actor.add(instance);
        return instance;
    }

    /**
     * @return the parent of the actor that owns this support object
     */
    public Actor getParent() {
        synchronized (this) {
            return parent;
        }
    }

    /**
     * @param parent the parent of the actor that owns this support object to
     *               set
     *
     * @return a reference to the support object
     */
    ParentSupport setParent(final Actor parent) {
        final Actor old;
        synchronized (this) {
            old = this.parent;
            this.parent = parent;
        }
        getPropertyChangeSupport().firePropertyChange(PARENT, old, parent);
        return this;
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "ParentSupport{" + "parent=" + parent + '}';
        }
    }
}
