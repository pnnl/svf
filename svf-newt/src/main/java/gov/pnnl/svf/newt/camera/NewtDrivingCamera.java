package gov.pnnl.svf.newt.camera;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import gov.pnnl.svf.camera.DrivingCamera;
import gov.pnnl.svf.newt.util.NewtCameraUtils;
import gov.pnnl.svf.scene.Scene;

/**
 * Camera that uses the keyboard WASD, QE, RF, and TG to drive the camera around
 * the scene. This camera is intended to be used as a test view for the
 * SimpleCamera class.
 *
 * @author Arthur Bleeker
 */
public class NewtDrivingCamera extends DrivingCamera implements NewtCamera, KeyListener {

    // private static final Logger logger = Logger.getLogger(NewtDrivingCamera.class.toString());
    private static final double SENSITIVITY = 0.025;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     *
     * @see SimpleCamera#SimpleCamera(Scene)
     */
    public NewtDrivingCamera(final Scene scene) {
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
    public NewtDrivingCamera(final Scene scene, final String id) {
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
    public NewtDrivingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    /**
     * For reverse compatibility.
     *
     * @param evt
     *
     * @deprecated
     */
    @Deprecated
    public void keyTyped(final KeyEvent evt) {
        // for reverse compatibility
    }

    /**
     *
     * @param evt
     */
    @Override
    public void keyPressed(final KeyEvent evt) {
        // not needed
    }

    /**
     *
     * @param evt
     */
    @Override
    public void keyReleased(final KeyEvent evt) {
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
    public void mouseDragged(final MouseEvent event) {
        fireMouseDraggedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        // no op
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        fireMousePressedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        fireMouseReleasedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        fireMouseMovedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseWheelMoved(final MouseEvent event) {
        fireMouseScrolledEvent(NewtCameraUtils.newCameraEvent(this, event), -(int) event.getRotation()[1]);
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        fireMouseEnteredEvent(NewtCameraUtils.newCameraEvent(this, event));
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        fireMouseExitedEvent(NewtCameraUtils.newCameraEvent(this, event));
    }
}
