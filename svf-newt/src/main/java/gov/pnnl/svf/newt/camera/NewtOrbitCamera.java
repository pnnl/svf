package gov.pnnl.svf.newt.camera;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.newt.util.NewtCameraUtils;
import gov.pnnl.svf.scene.Scene;

/**
 * Basic orbit camera for AWT. Left mouse button will orbit the camera. Right
 * mouse button will zoom the camera. Holding control with the left mouse button
 * will drag the camera.
 *
 * @author Amelia Bleeker
 */
public class NewtOrbitCamera extends OrbitCamera implements NewtCamera, KeyListener {

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     */
    public NewtOrbitCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param id    The unique id for this actor.
     */
    public NewtOrbitCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public NewtOrbitCamera(final Scene scene, final String type, final String id) {
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

    @Override
    public void keyPressed(final KeyEvent event) {
        if (isVisible()) {
            synchronized (this) {
                switch (event.getKeyCode()) {
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

    @Override
    public void keyReleased(final KeyEvent event) {
        released();
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
