package gov.pnnl.svf.camera;

import gov.pnnl.svf.event.ProxyMouseEvent;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ProxyCameraUtils;

/**
 * Camera that uses the keyboard WASD, QE, RF, and TG to drive the camera around
 * the scene. This camera is intended to be used as a test view for the
 * SimpleCamera class.
 *
 * @author Amelia Bleeker
 */
public class ProxyDrivingCamera extends DrivingCamera {

    // private static final Logger logger = Logger.getLogger(ProxyDrivingCamera.class.toString());
    private static final double SENSITIVITY = 0.025;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     *
     * @see SimpleCamera#SimpleCamera(Scene)
     */
    public ProxyDrivingCamera(final Scene scene) {
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
    public ProxyDrivingCamera(final Scene scene, final String id) {
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
    public ProxyDrivingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    public void keyTyped(final char evt) {
        switch (evt) {
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

    public void mouseDragged(final ProxyMouseEvent event) {
        fireMouseDraggedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mousePressed(final ProxyMouseEvent event) {
        fireMousePressedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mouseReleased(final ProxyMouseEvent event) {
        fireMouseReleasedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mouseMoved(final ProxyMouseEvent event) {
        fireMouseMovedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mouseWheelMoved(final ProxyMouseEvent event) {
        fireMouseScrolledEvent(ProxyCameraUtils.newCameraEvent(this, event), event.getWheelRotation());
    }

    public void mouseEntered(final ProxyMouseEvent event) {
        fireMouseEnteredEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }

    public void mouseExited(final ProxyMouseEvent event) {
        fireMouseExitedEvent(ProxyCameraUtils.newCameraEvent(this, event));
    }
}
