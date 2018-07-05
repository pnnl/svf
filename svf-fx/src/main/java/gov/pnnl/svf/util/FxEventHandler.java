package gov.pnnl.svf.util;

import java.util.Set;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

/**
 * Event handler wrapper that identifies the event types used.
 *
 * @param <T> the event type
 *
 * @author Amelia Bleeker
 */
public interface FxEventHandler<T extends Event> extends EventHandler<T> {

    /**
     * @return the set of event types for this handler
     */
    Set<EventType<T>> getEventTypes();
}
