package gov.pnnl.svf.core.util;

import java.io.Serializable;

/**
 * Abstract base class for working with state.
 *
 * @author Amelia Bleeker
 *
 */
public class AbstractState implements Serializable {

    private static final long serialVersionUID = 1L;

    protected byte state = StateUtil.NONE;

    /**
     * Constructor
     */
    protected AbstractState() {
        super();
    }

    /**
     * Reset all of the internal value states to false.
     */
    public void clearValues() {
        state = StateUtil.clearValues(state);
    }

    /**
     * Set all of the internal value states to true.
     */
    public void setValues() {
        state = StateUtil.setValues(state);
    }

    /**
     * Set all of the internal value states to the indicated value.
     *
     * @param value the value to set
     */
    public void setValues(final boolean value) {
        state = StateUtil.setValues(state, value);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + state;
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final AbstractState other = (AbstractState) obj;
        if (state != other.state) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "State{" + "state=" + StateUtil.toString(state) + '}';
    }

}
