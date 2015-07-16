package gov.pnnl.svf.scene;

import gov.pnnl.svf.util.FxEventHandler;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;

/**
 * JavaFX implementation of the tooltip actor.
 *
 * @author Arthur Bleeker
 */
public class FxTooltipActor extends TooltipActor implements FxEventHandler<InputEvent> {

    private static final Set<EventType<InputEvent>> EVENT_TYPES = Collections.unmodifiableSet(Collections.singleton(InputEvent.ANY));

    /**
     * Constructor
     *
     * @param scene The parent scene for the actor.
     */
    public FxTooltipActor(final Scene scene) {
        super(scene);
    }

    @Override
    public Set<EventType<InputEvent>> getEventTypes() {
        return EVENT_TYPES;
    }

    @Override
    public void handle(final InputEvent event) {
        // note: instanceof not necessary but included for static code analysis
        if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_DRAGGED)) {
            // mouse dragged
            getScene().getTooltip().moved((int) ((MouseEvent) event).getX(), (int) ((MouseEvent) event).getY());
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_MOVED)) {
            // mouse moved
            getScene().getTooltip().moved((int) ((MouseEvent) event).getX(), (int) ((MouseEvent) event).getY());
        } else if (Objects.equals(event.getEventType(), MouseEvent.MOUSE_PRESSED)) {
            // mouse pressed
            getScene().getTooltip().hideTooltip();
        }
    }
}
