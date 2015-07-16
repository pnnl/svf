package gov.pnnl.svf.awt.camera;

import gov.pnnl.svf.awt.util.AwtCameraUtils;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.scene.Scene;

/**
 * Camera used to view a 3d scene from one direction and allow the user to drag
 * the camera on the x/y plane using the mouse.
 *
 * @author Arthur Bleeker
 */
public class AwtDraggingCamera extends DraggingCamera implements AwtCamera {

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public AwtDraggingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public AwtDraggingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public AwtDraggingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void mouseDragged(final java.awt.event.MouseEvent event) {
        dragged(event.getX(), event.getY());
        fireMouseDraggedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseClicked(final java.awt.event.MouseEvent event) {
        // no op
    }

    @Override
    public void mousePressed(final java.awt.event.MouseEvent event) {
        pressed(event.getX(), event.getY(), AwtCameraUtils.getButton(event));
        fireMousePressedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseReleased(final java.awt.event.MouseEvent event) {
        released();
        fireMouseReleasedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseMoved(final java.awt.event.MouseEvent event) {
        moved(event.getX(), event.getY());
        fireMouseMovedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseWheelMoved(final java.awt.event.MouseWheelEvent event) {
        scrolled(event.getX(), event.getY(), event.getWheelRotation());
        fireMouseScrolledEvent(AwtCameraUtils.newCameraEvent(this, event), event.getWheelRotation());
    }

    @Override
    public void mouseEntered(final java.awt.event.MouseEvent event) {
        // no op
        fireMouseEnteredEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseExited(final java.awt.event.MouseEvent event) {
        exited();
        fireMouseExitedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }
}
