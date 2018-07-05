package gov.pnnl.svf.camera;

import gov.pnnl.svf.event.CameraEvent;

/**
 * Event listener for a camera.
 *
 * @author Amelia Bleeker
 */
public interface CameraListener {

    /**
     * Invoked when a mouse button has been pressed on this camera.
     *
     * @param event the event for the pick
     */
    void mousePressed(CameraEvent event);

    /**
     * Invoked when a mouse button has been released on this camera.
     *
     * @param event the event for the pick
     */
    void mouseReleased(CameraEvent event);

    /**
     * Invoked when the mouse enters a component.
     *
     * @param event the event for the pick
     */
    void mouseEntered(CameraEvent event);

    /**
     * Invoked when the mouse exits a component.
     *
     * @param event the event for the pick
     */
    void mouseExited(CameraEvent event);

    /**
     * Invoked when a mouse button is pressed on this camera and then dragged.
     * Mouse dragged events will continue to be delivered to the component where
     * the drag originated until the mouse button is released (regardless of
     * whether the mouse position is within the bounds of this camera).
     *
     * @param event the event for the pick
     */
    void mouseDragged(CameraEvent event);

    /**
     * Invoked when the mouse cursor has been moved onto a component but no
     * buttons have been pushed.
     *
     * @param event the event for the pick
     */
    void mouseMoved(CameraEvent event);

    /**
     * Invoked when the mouse wheel is rotated.
     *
     *
     * @param event  the event for the pick
     * @param amount the amount that the wheel is moved
     */
    void mouseScrolled(CameraEvent event, int amount);
}
