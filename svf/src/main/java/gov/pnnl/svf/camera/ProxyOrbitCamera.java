package gov.pnnl.svf.camera;

import gov.pnnl.svf.event.ProxyMouseEvent;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ProxyCameraUtils;
import java.awt.event.KeyEvent;

/**
 * Basic orbit camera for AWT. Left mouse button will orbit the camera. Right
 * mouse button will zoom the camera. Holding control with the left mouse button
 * will drag the camera.
 *
 * @author Amelia Bleeker
 */
public class ProxyOrbitCamera extends OrbitCamera {

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     */
    public ProxyOrbitCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param id    The unique id for this actor.
     */
    public ProxyOrbitCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public ProxyOrbitCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    public void keyPressed(final char event) {
        if (isVisible()) {
            synchronized (this) {
                switch (event) {
                    case 'w':
                    case 'W':
                    case KeyEvent.VK_UP:
                        moveUp(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 's':
                    case 'S':
                    case KeyEvent.VK_DOWN:
                        moveDown(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 'a':
                    case 'A':
                    case KeyEvent.VK_LEFT:
                        moveLeft(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 'd':
                    case 'D':
                    case KeyEvent.VK_RIGHT:
                        moveRight(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 'r':
                    case 'R':
                    case KeyEvent.VK_ADD:
                        moveForward(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 'f':
                    case 'F':
                    case KeyEvent.VK_SUBTRACT:
                        moveBackward(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    default:
                    // no operation
                }
            }
        }
    }

    public void keyReleased(final KeyEvent event) {
        released();
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
