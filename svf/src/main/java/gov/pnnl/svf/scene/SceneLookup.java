package gov.pnnl.svf.scene;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.lookup.MultiLookupProvider;

/**
 * Interface that provides additional functionality for a scene lookup.
 *
 * @author Arthur Bleeker
 */
public interface SceneLookup extends Disposable, MultiLookupProvider {

    /**
     * Get an actor by id.
     *
     * @param id the id of the actor
     *
     * @return the actor or null
     */
    Actor getActor(String id);

    /**
     * Get an actor in the scene by id. This method will check out the actor
     * from the actor pool if no actor is found.
     *
     * @param clazz the class of actor
     * @param type  the type of actor
     * @param id    the id of the actor
     * @param data  the actor pool data object
     * @param <T>   the type of actor
     *
     * @return the located actor, a new one checked out from the actor pool, or
     *         null
     */
    <T extends Actor> T getActor(Class<T> clazz, String type, String id, Object data);
}
