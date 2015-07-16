package gov.pnnl.svf.pool;

import gov.pnnl.svf.actor.Actor;

/**
 * Interface is used to pool actors for scenes that need to destroy and recreate
 * actors often.
 *
 * @param <T> the type of actor contained in this pool
 *
 * @author Arthur Bleeker
 */
public interface ActorPool<T extends Actor> extends Pool<T> {

    /**
     * Puts an actor back into the pool. Actors are validated on checkout. This
     * method will remove the actor from the scene and un-initialize it.
     *
     * @param actor actor to place back in the pool
     */
    @Override
    void checkIn(T actor);
}
