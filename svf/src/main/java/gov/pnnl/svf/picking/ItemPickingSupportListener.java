package gov.pnnl.svf.picking;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.event.PickingCameraEvent;
import java.util.Set;

/**
 * Event listener for a item picking support object.
 *
 * @author Amelia Bleeker
 */
public interface ItemPickingSupportListener {

    /**
     * Called when an item in the scene is picked.
     *
     * @param actor the actor that was picked
     * @param items the set of items in the actor that were picked
     * @param event The event for the picking event that occurred.
     */
    void itemsPicked(Actor actor, Set<Object> items, PickingCameraEvent event);
}
