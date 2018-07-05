package gov.pnnl.svf.physics;

import gov.pnnl.svf.actor.Actor;

/**
 * Listener used to listen for events during a collision.
 *
 * @author Amelia Bleeker
 */
public interface CollisionSupportListener {

    /**
     * Called when an actor has collided with another actor.
     *
     * @param actor1 The first actor involved in the collision.
     * @param actor2 The second actor involved in the collision.
     */
    void collidedWithActor(Actor actor1, Actor actor2);

    /**
     * Called when the actor has collided with the boundary.
     *
     * @param actor The actor that collided with the boundary.
     */
    void collidedWithBoundary(Actor actor);
}
