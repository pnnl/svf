package gov.pnnl.svf.picking;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.event.PickingCameraEvent;

/**
 * Listener used to listen to picking events.
 *
 * @author Amelia Bleeker
 */
public interface PickingSupportListener {

    /**
     * Called when the actor is picked using a mouse.
     *
     * @param actor The actor that was picked.
     * @param event The event for the picking event that occurred.
     */
    void picked(Actor actor, PickingCameraEvent event);
}
