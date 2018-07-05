package gov.pnnl.svf.physics;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.support.AbstractSupport;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class allows for collisions with other objects and enables the scene to
 * keep this object within its boundaries.
 *
 * At this time rotations of the collision boundaries according to the actor
 * rotations is not supported.
 *
 * @author Amelia Bleeker
 *
 */
public class CollisionSupport extends AbstractSupport<CollisionSupportListener> {

    /**
     * String representation of a field in this object.
     */
    public static final String COLLISION_TYPE_LIST = "collisionTypeList";

    /**
     *
     * @param actor The actor that owns this support object.
     *
     * @return The newly created instance of this support object.
     */
    public static CollisionSupport newInstance(final Actor actor) {
        final CollisionSupport instance = new CollisionSupport(actor);
        actor.add(instance);
        return instance;
    }

    private final Set<String> collisionTypeList = new HashSet<>();

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The actor that owns this support object.
     */
    protected CollisionSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Add an actor type that this object can collide with. Use "scene" as the
     * actor type for constraining this object within the scene boundaries.
     *
     * @param type The type of actor.
     */
    public void addCollisionType(final String type) {
        synchronized (this) {
            collisionTypeList.add(type);
        }
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * Checks to see if this actor should collide with a certain actor type. Use
     * "scene" as the actor type for constraining this object within the scene
     * boundaries.
     *
     * @param type The type of actor.
     *
     * @return true if this actor should collide with the actor type.
     */
    public boolean containsCollisionType(final String type) {
        synchronized (this) {
            return collisionTypeList.contains(type);
        }
    }

    /**
     * Notify all of the listeners that this actor collided with another actor.
     *
     * @param actor The actor that this actor collided with.
     */
    public void notifyCollisionWithActor(final Actor actor) {
        for (final CollisionSupportListener listener : getListeners()) {
            listener.collidedWithActor(getActor(), actor);
        }
    }

    /**
     * Notify all of the listeners that this actor collided with the scene
     * boundaries.
     */
    public void notifyCollisionWithBoundary() {
        for (final CollisionSupportListener listener : getListeners()) {
            listener.collidedWithBoundary(getActor());
        }
    }

    /**
     * Removes the type from the set of actor types that this actor can collide
     * with. Use "scene" as the actor type for constraining this object within
     * the scene boundaries.
     *
     * @param type The type of actor.
     */
    public void removeCollisionType(final String type) {
        synchronized (this) {
            collisionTypeList.remove(type);
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            final int maxLen = 10;
            final StringBuilder builder = new StringBuilder();
            builder.append("CollisionSupport [collisionTypeList=");
            builder.append(collisionTypeList != null ? toString(collisionTypeList, maxLen) : null);
            builder.append(", getActor()=");
            builder.append(getActor());
            builder.append("]");
            return builder.toString();
        }
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
}
