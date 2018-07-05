package gov.pnnl.svf.picking;

import gov.pnnl.svf.event.PickingCameraEvent;

/**
 * Event listener for a picking camera.
 *
 * @author Amelia Bleeker
 */
public interface PickingCameraListener {

    /**
     * Called before a picked event gets fired. This call may block the scene
     * drawing thread.
     *
     * @param event the event for the pick
     */
    void prePicked(PickingCameraEvent event);

    /**
     * Called when a picking camera event happens.
     *
     * @param event the event for the pick
     */
    void picked(PickingCameraEvent event);

    /**
     * Called after a picked event gets handled.
     *
     * @param event the event for the pick
     */
    void postPicked(PickingCameraEvent event);

    /**
     * Called when nothing in the scene is picked.
     *
     * @param event the event for the pick
     */
    void nothingPicked(PickingCameraEvent event);
}
