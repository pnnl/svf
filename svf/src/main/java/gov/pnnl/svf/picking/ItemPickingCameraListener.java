package gov.pnnl.svf.picking;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.event.PickingCameraEvent;

/**
 * Interface for an item picking camera listener.
 *
 * @author Amelia Bleeker
 */
public interface ItemPickingCameraListener extends PickingCameraListener {

    /**
     * Called when an item in the scene is entered.
     *
     * @param actor the actor that was picked
     * @param item  the item in the actor that was entered
     * @param event The event for the picking event that occurred.
     */
    void itemEntered(Actor actor, Object item, PickingCameraEvent event);

    /**
     * Called when an item in the scene is exited.
     *
     * @param actor the actor that was picked
     * @param item  the item in the actor that was exited
     * @param event The event for the picking event that occurred.
     */
    void itemExited(Actor actor, Object item, PickingCameraEvent event);

    /**
     * Called when the mouse has been moved.
     *
     * @param event the event for the mouse moved event that occurred.
     */
    void mouseMoved(PickingCameraEvent event);
}
