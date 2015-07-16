package gov.pnnl.svf.support;

/**
 * Event object for the StateSupport class.
 *
 * @param <T> the state type enumeration
 *
 * @author Arthur Bleeker
 */
@SuppressWarnings("rawtypes")
public class StateEvent<T extends Enum> {

    private final T state;
    private final boolean active;

    /**
     * Constructor
     *
     * @param state  the state
     * @param active true if active
     */
    protected StateEvent(final T state, final boolean active) {
        super();
        if (state == null) {
            throw new NullPointerException("state");
        }
        this.state = state;
        this.active = active;
    }

    /**
     * @return the state
     */
    public T getState() {
        return state;
    }

    /**
     * @return true if active
     */
    public boolean isActive() {
        return active;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StateEvent<T> other = (StateEvent<T>) obj;
        if (this.state != other.state && (this.state == null || !this.state.equals(other.state))) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.state != null ? this.state.hashCode() : 0);
        hash = 79 * hash + (this.active ? 1 : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "StateEvent{" + "state=" + state + ", active=" + active + '}';
    }
}
