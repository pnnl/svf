package gov.pnnl.svf.newt.camera;

import com.jogamp.newt.event.MouseEvent;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.newt.util.NewtCameraUtils;
import gov.pnnl.svf.scene.Scene;

/**
 * Camera used to view a 3d scene from one direction and allow the user to drag
 * the camera on the x/y plane using the mouse.
 *
 * @author Amelia Bleeker
 */
public class NewtDraggingCamera extends DraggingCamera implements NewtCamera {

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public NewtDraggingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public NewtDraggingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public NewtDraggingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        dragged(event.getX(), event.getY());
        fireMouseDraggedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        // no op
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        pressed(event.getX(), event.getY(), NewtCameraUtils.getButton(event));
        fireMousePressedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        released();
        fireMouseReleasedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        moved(event.getX(), event.getY());
        fireMouseMovedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseWheelMoved(final MouseEvent event) {
        scrolled(event.getX(), event.getY(), -(int) event.getRotation()[1]);
        fireMouseScrolledEvent(NewtCameraUtils.newCameraEvent(this, event), -(int) event.getRotation()[1]);
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        // no op
        fireMouseEnteredEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        exited();
        fireMouseExitedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }
}
