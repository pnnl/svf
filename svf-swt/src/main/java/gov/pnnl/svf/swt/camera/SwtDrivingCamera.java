package gov.pnnl.svf.swt.camera;

import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.swt.util.SwtCameraUtils;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Camera that uses the keyboard WASD, QE, RF, and TG to drive the camera around
 * the scene. This camera is intended to be used as a test view for the
 * SimpleCamera class.
 *
 * @author Arthur Bleeker
 */
public class SwtDrivingCamera extends SimpleCamera implements KeyListener, SwtCamera {

    private static final float SENSITIVITY = 0.025f;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     *
     * @see SimpleCamera#SimpleCamera(Scene)
     */
    public SwtDrivingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     *
     * @see SimpleCamera#SimpleCamera(Scene)
     */
    public SwtDrivingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     *
     * @see SimpleCamera#SimpleCamera(Scene)
     */
    public SwtDrivingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void keyPressed(final KeyEvent evt) {
        switch (evt.character) {
            case 'a':
            case 'A':
                moveLeft(SENSITIVITY * 5.0f);
                break;
            case 'd':
            case 'D':
                moveRight(SENSITIVITY * 5.0f);
                break;
            case 'w':
            case 'W':
                moveUp(SENSITIVITY * 5.0f);
                break;
            case 's':
            case 'S':
                moveDown(SENSITIVITY * 5.0f);
                break;
            case 'q':
            case 'Q':
                rotateLeft(SENSITIVITY);
                break;
            case 'e':
            case 'E':
                rotateRight(SENSITIVITY);
                break;
            case 'r':
            case 'R':
                moveForward(SENSITIVITY * 5.0f);
                break;
            case 'f':
            case 'F':
                moveBackward(SENSITIVITY * 5.0f);
                break;
            case 't':
            case 'T':
                rotateUp(SENSITIVITY);
                break;
            case 'g':
            case 'G':
                rotateDown(SENSITIVITY);
                break;
            default:
            // no operation
        }
    }

    @Override
    public void keyReleased(final KeyEvent evt) {
        // not needed
    }

    @Override
    public void dragDetected(final DragDetectEvent event) {
        fireMouseDraggedEvent(SwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseDoubleClick(final MouseEvent event) {
        // no op
    }

    @Override
    public void mouseDown(final MouseEvent event) {
        fireMousePressedEvent(SwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseUp(final MouseEvent event) {
        fireMouseReleasedEvent(SwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseMove(final MouseEvent event) {
        fireMouseMovedEvent(SwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseScrolled(final MouseEvent event) {
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
