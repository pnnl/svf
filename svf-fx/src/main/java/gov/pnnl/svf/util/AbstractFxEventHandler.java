package gov.pnnl.svf.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Abstract class for JavaFX event handlers used in a Fx scene.
 *
 * @param <T> the event type
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractFxEventHandler<T extends Event> implements FxEventHandler<T> {

    private final Set<EventType<T>> eventTypes;

    /**
     * Constructor
     *
     * @param eventTypes collection of event types handled by this handler
     */
    @SuppressWarnings("unchecked")
    public AbstractFxEventHandler(final EventType<T>... eventTypes) {
        this(Arrays.asList(eventTypes));
    }

    /**
     * Constructor
     *
     * @param eventTypes collection of event types handled by this handler
     */
    public AbstractFxEventHandler(final Collection<EventType<T>> eventTypes) {
        super();
        if (eventTypes == null) {
            throw new NullPointerException("eventTypes");
        }
        this.eventTypes = Collections.unmodifiableSet(new HashSet<>(eventTypes));
    }

    @Override
    public Set<EventType<T>> getEventTypes() {
        // return of unmodifiable collection
        return eventTypes;
    }
}
