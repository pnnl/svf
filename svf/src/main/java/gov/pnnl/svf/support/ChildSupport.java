package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.util.StateUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * This support class is used to create a hierarchical relationship between
 * actors. Actors that are children of the parent actor will inherit most
 * support classes including ColorSupport, *AnimationSupport, TextureSupport,
 * and TransformSupport. Actors that are made children will get a ParentSupport
 * object added to their lookup. Child actors should not be added directly to
 * the scene. Equals will not compare children elements.
 *
 * @author Amelia Bleeker
 *
 */
public class ChildSupport extends AbstractSupport<ChildSupportListener> {

    /**
     * State mask for boolean field in this actor.
     */
    protected static final byte ORDERED_MASK = StateUtil.getMasks()[0];
    /**
     * State mask for boolean field in this actor.
     */
    protected static final byte INHERIT_MASK = StateUtil.getMasks()[1];
    private final byte state;
    private Collection<Actor> children;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor   The owning actor.
     * @param ordered true if the children should maintain insert order
     * @param inherit true if the children should inherit the parent's state
     */
    protected ChildSupport(final Actor actor, final boolean ordered, final boolean inherit) {
        super(actor);
        byte temp = StateUtil.NONE;
        temp = StateUtil.setValue(temp, ORDERED_MASK, ordered);
        temp = StateUtil.setValue(temp, INHERIT_MASK, inherit);
        state = temp;
        children = ChildSupport.newCollection(null, ordered);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor The actor that owns this support instance.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static ChildSupport newInstance(final Actor actor) {
        final ChildSupport instance = new ChildSupport(actor, false, true);
        actor.add(instance);
        return instance;
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor   The actor that owns this support instance.
     * @param ordered true if the children should maintain insert order
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static ChildSupport newInstance(final Actor actor, final boolean ordered) {
        final ChildSupport instance = new ChildSupport(actor, ordered, true);
        actor.add(instance);
        return instance;
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor   The actor that owns this support instance.
     * @param ordered true if the children should maintain insert order
     * @param inherit true if the children should inherit the parent's state
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static ChildSupport newInstance(final Actor actor, final boolean ordered, final boolean inherit) {
        final ChildSupport instance = new ChildSupport(actor, ordered, inherit);
        actor.add(instance);
        return instance;
    }

    /**
     * @return true if this child support object maintains insert order
     */
    public boolean isOrdered() {
        return StateUtil.isValue(state, ORDERED_MASK);
    }

    /**
     * @return true if this child support object's children inherits its parents
     *         state
     */
    public boolean isInherit() {
        return StateUtil.isValue(state, INHERIT_MASK);
    }

    /**
     * Get a read only and static collection of all the children. For
     * performance reasons this collection can be modified. However, it should
     * not be modified utilizing the returned collection.
     *
     * @return the children
     */
    public Collection<Actor> getChildren() {
        synchronized (this) {
            return children;
        }
    }

    /**
     * Add a child.
     *
     * @param child the child to add
     *
     * @return boolean success of the add
     */
    public boolean add(final Actor child) {
        if (child == null) {
            return false;
        }
        final boolean added;
        synchronized (this) {
            final Collection<Actor> temp = ChildSupport.newCollection(children, StateUtil.isValue(state, ORDERED_MASK));
            added = temp.add(child);
            children = temp;
        }
        if (added) {
            notifyChildAdded(child);
        }
        return added;
    }

    /**
     * Add a collection of children.
     *
     * @param children the collection of children to add
     *
     * @return boolean success of the add all
     */
    public boolean addAll(final Collection<? extends Actor> children) {
        if ((children == null) || children.isEmpty()) {
            return false;
        }
        final List<Actor> added = new ArrayList<>();
        synchronized (this) {
            final Collection<Actor> temp = ChildSupport.newCollection(this.children, StateUtil.isValue(state, ORDERED_MASK));
            for (final Actor child : children) {
                if (temp.add(child)) {
                    added.add(child);
                }
            }
            this.children = temp;
        }
        for (final Actor child : added) {
            notifyChildAdded(child);
        }
        return !added.isEmpty();
    }

    /**
     * Clear all of the children.
     */
    public void clear() {
        final Collection<Actor> children;
        synchronized (this) {
            children = this.children;
            if (!children.isEmpty()) {
                this.children = ChildSupport.newCollection(null, StateUtil.isValue(state, ORDERED_MASK));
            }
        }
        for (final Actor child : children) {
            notifyChildRemoved(child);
        }
    }

    /**
     * Remove a child.
     *
     * @param child the child to remove
     *
     * @return boolean success of the remove
     */
    public boolean remove(final Actor child) {
        if (child == null) {
            return false;
        }
        final boolean removed;
        synchronized (this) {
            final Collection<Actor> temp = ChildSupport.newCollection(children, StateUtil.isValue(state, ORDERED_MASK));
            removed = temp.remove(child);
            children = temp;
        }
        if (removed) {
            notifyChildRemoved(child);
        }
        return removed;
    }

    /**
     * Remove a collection of children.
     *
     * @param children the collection of children to remove
     *
     * @return boolean success of the remove all
     */
    public boolean removeAll(final Collection<? extends Actor> children) {
        if ((children == null) || children.isEmpty()) {
            return false;
        }
        final List<Actor> removed = new ArrayList<>();
        synchronized (this) {
            final Collection<Actor> temp = ChildSupport.newCollection(this.children, StateUtil.isValue(state, ORDERED_MASK));
            for (final Actor child : children) {
                if (temp.remove(child)) {
                    removed.add(child);
                }
            }
            this.children = temp;
        }
        for (final Actor child : removed) {
            notifyChildRemoved(child);
        }
        return !removed.isEmpty();
    }

    /**
     * The number of children.
     *
     * @return the number of children
     */
    public int size() {
        synchronized (this) {
            return children.size();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        clear();
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    @Override
    public String toString() {
        final int maxLen = 10;
        final StringBuilder builder = new StringBuilder();
        builder.append("ChildSupport [children=");
        builder.append(children != null ? toString(children, maxLen) : null);
        builder.append(", getActor()=");
        builder.append(getActor());
        builder.append("]");
        return builder.toString();
    }

    private String toString(final Collection<?> collection, final int maxLen) {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (final Iterator<?> iterator = collection.iterator(); iterator.hasNext() && (i < maxLen); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }

    private void notifyChildAdded(final Actor child) {
        final ParentSupport parent = ParentSupport.newInstance(child);
        parent.setParent(getActor());
        for (final ChildSupportListener listener : getListeners()) {
            listener.childAdded(child);
        }
    }

    private void notifyChildRemoved(final Actor child) {
        final ParentSupport parent = child.lookup(ParentSupport.class);
        if (parent != null) {
            child.remove(parent);
        }
        for (final ChildSupportListener listener : getListeners()) {
            listener.childRemoved(child);
        }
    }

    private static Collection<Actor> newCollection(final Collection<Actor> children, final boolean ordered) {
        if (ordered) {
            return children != null ? new LinkedHashSet<>(children) : new LinkedHashSet<>();
        } else {
            return children != null ? new HashSet<>(children) : new HashSet<>();
        }
    }
}
