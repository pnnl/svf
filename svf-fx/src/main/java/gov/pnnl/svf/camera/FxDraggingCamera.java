package gov.pnnl.svf.camera;

import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.FxCameraUtils;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Camera used to view a 3d scene from one direction and allow the user to drag
 * the camera on the x/y plane using the mouse.
 *
 * @author Arthur Bleeker
 */
public class FxDraggingCamera extends DraggingCamera implements FxCamera {

    private static final Set<EventType<InputEvent>> EVENT_TYPES = Collections.unmodifiableSet(Collections.singleton(InputEvent.ANY));

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public FxDraggingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public FxDraggingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public FxDraggingCamera(final Scene scene, final String type, final String id) {
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
        if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_DRAGGED)) {
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
