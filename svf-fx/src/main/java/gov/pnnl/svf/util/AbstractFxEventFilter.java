package gov.pnnl.svf.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Abstract class for JavaFX event filters used in a Fx scene.
 *
 * @param <T> the event type
 *
 * @author Amelia Bleeker
 */
public abstract class AbstractFxEventFilter<T extends Event> implements FxEventFilter<T> {

    private final Set<EventType<T>> eventTypes;

    /**
     * Constructor
     *
     * @param eventTypes collection of event types handled by this filter
     */
    @SuppressWarnings("unchecked")
    public AbstractFxEventFilter(final EventType<T>... eventTypes) {
        this(Arrays.asList(eventTypes));
    }

    /**
     * Constructor
     *
     * @param eventTypes collection of event types handled by this filter
     */
    public AbstractFxEventFilter(final Collection<EventType<T>> eventTypes) {
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
