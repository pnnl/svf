package gov.pnnl.svf.awt.camera;

import gov.pnnl.svf.awt.util.AwtCameraUtils;
import gov.pnnl.svf.camera.DrivingCamera;
import gov.pnnl.svf.scene.Scene;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Camera that uses the keyboard WASD, QE, RF, and TG to drive the camera around
 * the scene. This camera is intended to be used as a test view for the
 * SimpleCamera class.
 *
 * @author Arthur Bleeker
 */
public class AwtDrivingCamera extends DrivingCamera implements KeyListener, AwtCamera {

    // private static final Logger logger = Logger.getLogger(AwtDrivingCamera.class.toString());
    private static final double SENSITIVITY = 0.025;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     *
     * @see SimpleCamera#SimpleCamera(Scene)
     */
    public AwtDrivingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     *
     * @see SimpleCamera#SimpleCamera(Scene,String)
     */
    public AwtDrivingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     *
     * @see SimpleCamera#SimpleCamera(Scene,String,String)
     */
    public AwtDrivingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void keyPressed(final KeyEvent evt) {
        // not needed
    }

    @Override
    public void keyReleased(final KeyEvent evt) {
        // not needed
    }

    @Override
    public void keyTyped(final KeyEvent evt) {
        switch (evt.getKeyChar()) {
            case 'a':
            case 'A':
                moveLeft(SENSITIVITY * 5.0);
                break;
            case 'd':
            case 'D':
                moveRight(SENSITIVITY * 5.0);
                break;
            case 'w':
            case 'W':
                moveUp(SENSITIVITY * 5.0);
                break;
            case 's':
            case 'S':
                moveDown(SENSITIVITY * 5.0);
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
                moveForward(SENSITIVITY * 5.0);
                break;
            case 'f':
            case 'F':
                moveBackward(SENSITIVITY * 5.0);
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
    public void mouseDragged(final java.awt.event.MouseEvent event) {
        fireMouseDraggedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseClicked(final java.awt.event.MouseEvent event) {
        // no op
    }

    @Override
    public void mousePressed(final java.awt.event.MouseEvent event) {
        fireMousePressedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseReleased(final java.awt.event.MouseEvent event) {
        fireMouseReleasedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseMoved(final java.awt.event.MouseEvent event) {
        fireMouseMovedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseWheelMoved(final java.awt.event.MouseWheelEvent event) {
        fireMouseScrolledEvent(AwtCameraUtils.newCameraEvent(this, event), event.getWheelRotation());
    }

    @Override
    public void mouseEntered(final java.awt.event.MouseEvent event) {
        fireMouseEnteredEvent(AwtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseExited(final java.awt.event.MouseEvent event) {
        fireMouseExitedEvent(AwtCameraUtils.newCameraEvent(this, event));
    }
}
