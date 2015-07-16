package gov.pnnl.svf.swt.camera;

import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.swt.util.SwtCameraUtils;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Camera used to view a 3d scene from one direction and allow the user to drag
 * the camera on the x/y plane using the mouse.
 *
 * @author Arthur Bleeker
 */
public class SwtDraggingCamera extends DraggingCamera implements SwtCamera {

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public SwtDraggingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public SwtDraggingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public SwtDraggingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void dragDetected(final DragDetectEvent event) {
        dragged(event.x, event.y);
        fireMouseDraggedEvent(SwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseDoubleClick(final MouseEvent event) {
        // no op
    }

    @Override
    public void mouseDown(final MouseEvent event) {
        pressed(event.x, event.y, SwtCameraUtils.getButton(event));
        fireMousePressedEvent(SwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseUp(final MouseEvent event) {
        released();
        fireMouseReleasedEvent(SwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseMove(final MouseEvent event) {
        moved(event.x, event.y);
        fireMouseMovedEvent(SwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseScrolled(final MouseEvent event) {
        scrolled(event.x, event.y, -event.count);
        fireMouseScrolledEvent(SwtCameraUtils.newCameraEvent(this, event), -event.count);
    }

    @Override
    public void mouseEnter(final MouseEvent event) {
        // no op
        fireMouseEnteredEvent(SwtCameraUtils.newCameraEvent(this, event));
        // give the control focus so the mouse scroll events get fired
        final Composite composite = (Composite) getScene().getComponent();
        composite.setFocus();
    }

    @Override
    public void mouseExit(final MouseEvent event) {
        exited();
        fireMouseExitedEvent(SwtCameraUtils.newCameraEvent(this, event));
        // give the shell focus so the mouse scroll is disabled when the mouse
        // goes outside of this scene
        final Composite composite = (Composite) getScene().getComponent();
        composite.getShell().setFocus();
    }

    @Override
    public void mouseHover(final MouseEvent event) {
        // no operation
    }
}
