package gov.pnnl.svf.camera;

import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.FxCameraUtils;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Camera that uses the keyboard WASD, QE, RF, and TG to drive the camera around
 * the scene. This camera is intended to be used as a test view for the
 * SimpleCamera class.
 *
 * @author Arthur Bleeker
 *
 */
public class FxDrivingCamera extends DrivingCamera implements FxCamera {

    private static final Set<EventType<InputEvent>> EVENT_TYPES = Collections.unmodifiableSet(Collections.singleton(InputEvent.ANY));
    private static final double SENSITIVITY = 0.025;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     *
     * @see SimpleCamera#SimpleCamera(Scene)
     */
    public FxDrivingCamera(final Scene scene) {
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
    public FxDrivingCamera(final Scene scene, final String id) {
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
    public FxDrivingCamera(final Scene scene, final String type, final String id) {
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
        if (event instanceof KeyEvent && Objects.equals(event.getEventType(), KeyEvent.KEY_TYPED)) {
            // key typed
            switch (((KeyEvent) event).getCharacter().charAt(0)) {
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
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_DRAGGED)) {
            // mouse dragged
            fireMouseDraggedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_PRESSED)) {
            // mouse pressed
            fireMousePressedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_RELEASED)) {
            // mouse released
            fireMouseReleasedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_MOVED)) {
            // mouse moved
            fireMouseMovedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_ENTERED)) {
            // mouse entered
            fireMouseEnteredEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_EXITED)) {
            // mouse exited
            fireMouseExitedEvent(FxCameraUtils.newCameraEvent(this, (MouseEvent) event));
        } else if (event instanceof ScrollEvent && Objects.equals(event.getEventType(), ScrollEvent.SCROLL)) {
            // mouse scrolled
            fireMouseScrolledEvent(FxCameraUtils.newCameraEvent(this, (ScrollEvent) event), (int) -((ScrollEvent) event).getTextDeltaY());
        }
    }
}
