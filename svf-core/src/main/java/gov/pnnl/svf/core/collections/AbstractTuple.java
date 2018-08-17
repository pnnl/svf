package gov.pnnl.svf.core.collections;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Base class for a heterogeneous tuple class. This class type is immutable.
 *
 * @author Amelia Bleeker
 */
public abstract class AbstractTuple implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * Array of values stored by this tuple.
     */
    protected final Object[] values;

    /**
     * Constructor
     *
     * @param values the series of values to construct this tuple with
     */
    protected AbstractTuple(final Object... values) {
        super();
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values");
        }
        this.values = values;
    }

    /**
     * Get a value from the tuple.
     *
     * @param index the index for the tuple
     *
     * @return the value at the index
     *
     * @throws IndexOutOfBoundsException
     */
    public Object get(final int index) {
        return values[index];
    }

    /**
     * Get the size of this tuple.
     *
     * @return the size
     */
    public int size() {
        return values.length;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractTuple other = (AbstractTuple) obj;
        if (!Arrays.equals(values, other.values)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tuple" + values.length + " [" + Arrays.toString(values) + "]";
    }

}
