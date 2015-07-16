package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.util.DrawState;
import gov.pnnl.svf.core.util.PropertyChangeSupportWrapper;
import gov.pnnl.svf.core.util.SupportState;
import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.scene.Observable;
import gov.pnnl.svf.scene.Scene;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class for support classes that need reference to the enclosing actor
 * or listeners.
 *
 * @author Arthur Bleeker
 * @param <I> The type of listener that this support object supports.
 *
 */
public abstract class AbstractSupport<I> implements Disposable, Observable {

    protected static final DirtyListener listener = new DirtyListener();
    protected final SupportState supportState = new SupportState();
    protected final DrawState drawState = new DrawState();
    protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupportWrapper(this);
    /**
     * list of listeners for specific support listener types
     */
    protected List<I> listeners;
    protected Set<Actor> actors;
    protected final Actor actor;

    /**
     * Constructor
     *
     * @param actor
     */
    protected AbstractSupport(final Actor actor) {
        if (actor == null) {
            throw new NullPointerException("actor");
        }
        this.actor = actor;
        supportState.setDirty();
        registerActor(actor);
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Adds this support object to another actor.
     *
     * @param actor the actor to add this support object to
     *
     * @throws UnsupportedOperationException if this support object doesn't
     *                                       support sharing
     */
    public void addInstance(final Actor actor) {
        if (actor == null) {
            throw new NullPointerException("actor");
        }
        final Set<Actor> temp;
        synchronized (this) {
            if (actors == null) {
                actors = Collections.synchronizedSet(new HashSet<Actor>());
                actors.add(this.actor);
            }
            temp = actors;
        }
        temp.add(actor);
        registerActor(actor);
        actor.add(this);
    }

    /**
     * Removes this support object from another actor.
     *
     * @param actor the actor to remove this support object from
     */
    public void removeInstance(final Actor actor) {
        if (actor == null) {
            throw new NullPointerException("actor");
        }
        final Set<Actor> temp;
        synchronized (this) {
            temp = this.actors;
        }
        if (temp != null) {
            temp.remove(actor);
        }
        actor.remove(this);
    }

    /**
     * Add a listener to this support object.
     *
     * @param listener
     */
    public void addListener(final I listener) {
        synchronized (this) {
            final List<I> temp = AbstractSupport.newCollection(this.listeners);
            temp.remove(listener);
            temp.add(listener);
            this.listeners = temp;
        }
    }

    /**
     * Clear all listeners from this support object.
     */
    public void clearListeners() {
        synchronized (this) {
            this.listeners = null;
        }
    }

    @Override
    public void dispose() {
        final Set<Actor> temp;
        synchronized (this) {
            if (supportState.isDisposed()) {
                return;
            }
            // mark as disposed
            supportState.setDisposed();
            // remove the support listeners
            clearListeners();
            temp = actors;
        }
        // remove this from actors
        if (temp != null) {
            synchronized (temp) {
                for (final Actor actor : temp) {
                    actor.remove(this);
                }
            }
            temp.clear();
        } else {
            this.actor.remove(this);
        }
        // remove the property change listeners
        final PropertyChangeListener[] pcListeners = propertyChangeSupport.getPropertyChangeListeners();
        for (final PropertyChangeListener listener : pcListeners) {
            // remove the listener
            // also removes listeners that are associated with a specific field
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }

    /**
     * Will return the original actor if this support object is applied to
     * multiple actors.
     *
     * @return the actor that is implementing this support object
     */
    public Actor getActor() {
        return actor;
    }

    /**
     * The collection returned from this method should never be modified.
     *
     * @return an immutable and static view of the current list of listeners
     */
    public List<I> getListeners() {
        synchronized (this) {
            if (this.listeners != null) {
                return this.listeners;
            } else {
                return Collections.<I>emptyList();
            }
        }
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return supportState.isDisposed();
        }
    }

    /**
     * Remove a listener from this support object.
     *
     * @param listener
     */
    public void removeListener(final I listener) {
        synchronized (this) {
            if (this.listeners != null) {
                final List<I> temp = AbstractSupport.newCollection(listeners);
                temp.remove(listener);
                this.listeners = temp;
            }
        }
    }

    private void registerActor(final Actor actor) {
        // register this object with the actor's scene
        final Scene scene = actor.getScene();
        // deregister the existing object type
        final AbstractSupport<?> support = actor.lookup(this.getClass());
        if (support != null) {
            actor.remove(support);
            // only remove from the scene if this actor owns the support object
            if (actor == support.getActor()) {
                scene.remove(support);
                support.dispose();
            }
        }
        scene.add(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.actor != null ? this.actor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractSupport<?> other = (AbstractSupport<?>) obj;
        if (this.actor != other.actor && (this.actor == null || !this.actor.equals(other.actor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "actor=" + getActor() + '}';
    }

    private static <I> List<I> newCollection(final List<I> listeners) {
        return listeners != null ? new ArrayList<>(listeners) : new ArrayList<I>();
    }

    /**
     * Listener used to set the registered actor(s) to dirty.
     */
    protected static class DirtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            final AbstractSupport<?> support = (AbstractSupport<?>) evt.getSource();
            final Set<Actor> temp;
            synchronized (support) {
                temp = support.actors;
            }
            if (temp != null) {
                synchronized (temp) {
                    for (final Actor actor : temp) {
                        actor.setDirty(true);
                    }
                }
            } else {
                support.actor.setDirty(true);
            }
        }
    }
}
