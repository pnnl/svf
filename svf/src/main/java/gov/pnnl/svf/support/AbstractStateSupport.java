package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Add this class to actor's that can have state in a scene. It's up to the
 * individual actor to decide what the states actually mean.
 *
 * @param <T>
 *
 * @author Amelia Bleeker
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractStateSupport<T extends Enum> extends AbstractSupport<StateSupportListener<T>> {

    private final Set<T> allowed;
    private final Set<T> states;

    /**
     * Constructor
     *
     * @param actor  reference to the parent actor
     * @param state  state allowed for this support object
     * @param states additional list of states allowed for this support object
     */
    @SuppressWarnings("unchecked")
    protected AbstractStateSupport(final Actor actor, final T state, final T... states) {
        super(actor);
        if (state == null) {
            throw new NullPointerException("state");
        }
        final Set<T> tempAllowed = EnumSet.of(state);
        if (states != null) {
            tempAllowed.addAll(Arrays.asList(states));
        }
        this.allowed = Collections.unmodifiableSet(tempAllowed);
        final Set<T> tempStates = EnumSet.noneOf((Class<T>) state.getClass());
        this.states = Collections.synchronizedSet(tempStates);
    }

    /**
     * Constructor
     *
     * @param actor  reference to the parent actor
     * @param states set of states allowed for this support object
     */
    public AbstractStateSupport(final Actor actor, final Set<T> states) {
        this(actor, AbstractStateSupport.getFirst(states), AbstractStateSupport.getRest(states));
    }

    private static <T> T getFirst(final Set<T> states) {
        if (states == null || states.isEmpty()) {
            throw new IllegalArgumentException("states");
        }
        return states.iterator().next();
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] getRest(final Set<T> states) {
        T[] temp = null;
        int index = 0;
        for (final T s : states) {
            if (temp == null) {
                temp = (T[]) Array.newInstance(s.getClass(), states.size() - 1);
            } else {
                temp[index++] = s;
            }
        }
        return temp;
    }

    /**
     * Set a state as active or inactive.
     *
     * @param state  the state to set
     * @param active true to set to active
     *
     * @return a reference to the support object
     */
    public AbstractStateSupport<T> setActive(final T state, final boolean active) {
        if (state == null) {
            throw new NullPointerException("state");
        }
        if (!allowed.contains(state)) {
            throw new IllegalArgumentException("state");
        }
        if (active) {
            if (states.add(state)) {
                fireEvent(state, active);
            }
        } else if (states.remove(state)) {
            fireEvent(state, active);
        }
        return this;
    }

    private void fireEvent(final T state, final boolean active) {
        final StateEvent<T> event = new StateEvent<>(state, active);
        for (final StateSupportListener<T> listener : getListeners()) {
            listener.stateChanged(event);
        }
    }

    /**
     * Check whether a state is active or inactive.
     *
     * @param state the state to check
     *
     * @return true if it is active
     */
    public boolean isActive(final T state) {
        if (state == null) {
            throw new NullPointerException("state");
        }
        if (!allowed.contains(state)) {
            throw new IllegalArgumentException("state");
        }
        return states.contains(state);
    }

    /**
     * @return a set of all of the active states
     */
    @SuppressWarnings("unchecked")
    public Set<T> getActiveStates() {
        synchronized (states) {
            if (states.isEmpty()) {
                return Collections.emptySet();
            }
            return Collections.unmodifiableSet(EnumSet.copyOf(states));
        }
    }

    /**
     * @return a set of all of the allowed states
     */
    public Set<T> getAllowedStates() {
        return allowed;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" + "allowed=" + allowed + ", states=" + states + '}';
    }
}
