package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;

/**
 * Add this class to actor's that can be selected in a scene. It's up to the
 * individual actor to decide what a selection actually means.
 *
 * @author Amelia Bleeker
 */
public class SelectionSupport extends AbstractSupport<SelectionSupportListener> {

    /**
     * String representation of a field in this object.
     */
    public static final String ID = "id";
    /**
     * String representation of a field in this object.
     */
    public static final String SELECTED = "selected";
    private String id = "";

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The owning actor.
     */
    protected SelectionSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Constructor
     *
     * @param actor The owning actor.
     *
     * @return the new instance of this object
     */
    public static SelectionSupport newInstance(final Actor actor) {
        final SelectionSupport instance = new SelectionSupport(actor);
        actor.add(instance);
        return instance;
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * @return the id
     */
    public String getId() {
        synchronized (this) {
            return id;
        }
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        synchronized (this) {
            return supportState.isSelected();
        }
    }

    /**
     * @param id the id to set
     *
     * @return a reference to the support object
     */
    public SelectionSupport setId(final String id) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        String old;
        synchronized (this) {
            old = this.id;
            this.id = id;
        }
        getPropertyChangeSupport().firePropertyChange(ID, old, id);
        return this;
    }

    /**
     * @param selected the selected to set
     *
     * @return a reference to the support object
     */
    public SelectionSupport setSelected(final boolean selected) {
        boolean old;
        synchronized (this) {
            old = supportState.isSelected();
            supportState.setSelected(selected);
        }
        if (old != selected) {
            for (final SelectionSupportListener listener : getListeners()) {
                listener.selectionChanged(getActor());
            }
        }
        getPropertyChangeSupport().firePropertyChange(SELECTED, old, selected);
        return this;
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "SelectionSupport{" + "id=" + id + "selected=" + isSelected() + '}';
        }
    }
}
