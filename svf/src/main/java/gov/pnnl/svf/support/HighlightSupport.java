package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;

/**
 * Add this class to actor's that can be highlighted in a scene. It's up to the
 * individual actor to decide what a highlight actually means.
 *
 * @author Alex Gibson
 *
 */
public class HighlightSupport extends AbstractSupport<HighlightSupportListener> {

    /**
     * String representation of a field in this object.
     */
    public static final String ID = "id";
    /**
     * String representation of a field in this object.
     */
    public static final String HIGHLIGHTED = "highlighted";
    private String id = "";

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The owning actor.
     */
    protected HighlightSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Constructor
     *
     * @param actor The owning actor.
     *
     * @return a new instance of the support object
     */
    public static HighlightSupport newInstance(final Actor actor) {
        final HighlightSupport instance = new HighlightSupport(actor);
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
     * @return the highlighted
     */
    public boolean isHighlighted() {
        synchronized (this) {
            return supportState.isHighlighted();
        }
    }

    /**
     * @param highlighted the highlight to set
     *
     * @return a reference to the support object
     */
    public HighlightSupport setHighlighted(final boolean highlighted) {
        boolean old;
        synchronized (this) {
            old = supportState.isHighlighted();
            supportState.setHighlighted(highlighted);
        }
        if (old != highlighted) {
            for (final HighlightSupportListener listener : getListeners()) {
                listener.hightlightChanged(getActor());
            }
        }
        getPropertyChangeSupport().firePropertyChange(HIGHLIGHTED, old, highlighted);
        return this;
    }

    /**
     * @param id the id to set
     *
     * @return a reference to the support object
     */
    public HighlightSupport setId(final String id) {
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

    @Override
    public String toString() {
        synchronized (this) {
            return "HighlightSupport{" + "id=" + id + "highlighted=" + isHighlighted() + '}';
        }
    }
}
