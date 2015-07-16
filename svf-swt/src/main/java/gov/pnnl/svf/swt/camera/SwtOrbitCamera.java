package gov.pnnl.svf.swt.camera;

import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.swt.util.SwtCameraUtils;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Basic orbit camera for SWT. Left mouse button will orbit the camera. Right
 * mouse button will zoom the camera. Holding control with the left mouse button
 * will drag the camera.
 *
 * @author Arthur Bleeker
 */
public class SwtOrbitCamera extends OrbitCamera implements KeyListener, SwtCamera {

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     */
    public SwtOrbitCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param id    The unique id for this actor.
     */
    public SwtOrbitCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public SwtOrbitCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        if (isVisible()) {
            synchronized (this) {
                switch (event.character) {
                    case 'w':
                    case 'W':
                        moveUp(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 's':
                    case 'S':
                        moveDown(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 'a':
                    case 'A':
                        moveLeft(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 'd':
                    case 'D':
                        moveRight(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 'r':
                    case 'R':
                        moveForward(MOVE_FACTOR * getMoveMultiplier());
                        break;
                    case 'f':
                    case 'F':
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
