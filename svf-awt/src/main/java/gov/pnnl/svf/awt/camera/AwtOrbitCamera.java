package gov.pnnl.svf.awt.camera;

import gov.pnnl.svf.awt.util.AwtCameraUtils;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.scene.Scene;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Basic orbit camera for AWT. Left mouse button will orbit the camera. Right
 * mouse button will zoom the camera. Holding control with the left mouse button
 * will drag the camera.
 *
 * @author Arthur Bleeker
 */
public class AwtOrbitCamera extends OrbitCamera implements KeyListener, AwtCamera {

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     */
    public AwtOrbitCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param id    The unique id for this actor.
     */
    public AwtOrbitCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public AwtOrbitCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
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
    public void keyTyped(final KeyEvent event) {
        // no operation
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
