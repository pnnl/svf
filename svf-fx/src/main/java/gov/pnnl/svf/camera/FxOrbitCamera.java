package gov.pnnl.svf.camera;

import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.FxCameraUtils;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Basic orbit camera for AWT. Left mouse button will orbit the camera. Right
 * mouse button will zoom the camera. Holding control with the left mouse button
 * will drag the camera.
 *
 * @author Arthur Bleeker
 */
public class FxOrbitCamera extends OrbitCamera implements FxCamera {

    private static final Set<EventType<InputEvent>> EVENT_TYPES = Collections.unmodifiableSet(Collections.singleton(InputEvent.ANY));

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     */
    public FxOrbitCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param id    The unique id for this actor.
     */
    public FxOrbitCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene reference to the containing scene
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public FxOrbitCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public Set<EventType<InputEvent>> getEventTypes() {
        return EVENT_TYPES;
    }

    @Override
    public void handle(final InputEvent event) {
        // note: instanceof not necessary but included for static code analysis
        // check for input event type
        if (event instanceof javafx.scene.input.KeyEvent && Objects.equals(event.getEventType(), javafx.scene.input.KeyEvent.KEY_TYPED)) {
            // key typed
            switch (((javafx.scene.input.KeyEvent) event).getCharacter().charAt(0)) {
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
        } else if (event instanceof javafx.scene.input.KeyEvent && Objects.equals(event.getEventType(), javafx.scene.input.KeyEvent.KEY_RELEASED)) {
            // key released
            released();
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_DRAGGED)) {
            // mouse dragged
            dragged((int) ((MouseEvent) event).getX(), (int) ((MouseEvent) event).getY());
            fireMouseDraggedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_PRESSED)) {
            // mouse pressed
            pressed((int) ((MouseEvent) event).getX(), (int) ((MouseEvent) event).getY(), FxCameraUtils.getButton((MouseEvent) event));
            fireMousePressedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_RELEASED)) {
            // mouse released
            released();
            fireMouseReleasedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_MOVED)) {
            // mouse moved
            moved((int) ((MouseEvent) event).getX(), (int) ((MouseEvent) event).getY());
            fireMouseMovedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_ENTERED)) {
            // mouse entered
            fireMouseEnteredEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_EXITED)) {
            // mouse exited
            exited();
            fireMouseExitedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof ScrollEvent && Objects.equals(event.getEventType(), ScrollEvent.SCROLL)) {
            // mouse scrolled
            scrolled((int) ((ScrollEvent) event).getX(), (int) ((ScrollEvent) event).getY(), (int) -((ScrollEvent) event).getTextDeltaY());
            fireMouseScrolledEvent(FxCameraUtils.newCameraEvent(this, (ScrollEvent) event), (int) -((ScrollEvent) event).getTextDeltaY());
        }
    }
}
