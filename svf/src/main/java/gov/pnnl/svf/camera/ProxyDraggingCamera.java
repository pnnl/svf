package gov.pnnl.svf.camera;

import gov.pnnl.svf.event.ProxyMouseEvent;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ProxyCameraUtils;

/**
 * Camera used to view a 3d scene from one direction and allow the user to drag
 * the camera on the x/y plane using the mouse.
 *
 * @author Amelia Bleeker
 */
public class ProxyDraggingCamera extends DraggingCamera {

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public ProxyDraggingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public ProxyDraggingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public ProxyDraggingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    public void mouseDragged(final ProxyMouseEvent event) {
        dragged(event.getX(), event.getY());
        fireMouseDraggedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mousePressed(final ProxyMouseEvent event) {
        pressed(event.getX(), event.getY(), ProxyCameraUtils.getButton(event));
        fireMousePressedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mouseReleased(final ProxyMouseEvent event) {
        released();
        fireMouseReleasedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mouseMoved(final ProxyMouseEvent event) {
        moved(event.getX(), event.getY());
        fireMouseMovedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mouseWheelMoved(final ProxyMouseEvent event) {
        scrolled(event.getX(), event.getY(), event.getWheelRotation());
        fireMouseScrolledEvent(ProxyCameraUtils.newCameraEvent(this, event), event.getWheelRotation());
    }

    public void mouseEntered(final ProxyMouseEvent event) {
        // no op
        fireMouseEnteredEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mouseExited(final ProxyMouseEvent event) {
        exited();
        fireMouseExitedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }
}
