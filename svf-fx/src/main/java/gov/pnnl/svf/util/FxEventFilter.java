package gov.pnnl.svf.util;

import java.util.Set;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

/**
 * Event filter wrapper that identifies the event types used.
 *
 * @param <T> the event type
 *
 * @author Arthur Bleeker
 */
public interface FxEventFilter<T extends Event> extends EventHandler<T> {

    /**
     * @return the set of event types for this handler
     */
    Set<EventType<T>> getEventTypes();
}
