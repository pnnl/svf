package gov.pnnl.svf.geometry;

import java.io.Serializable;

/**
 * Marker for a geometry shape.
 *
 * @author Amelia Bleeker
 */
public abstract class Shape implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    protected Shape() {
        super();
    }

    @Override
    public int hashCode() {
        return 3;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Shape;
    }

    @Override
    public String toString() {
        return "Shape{" + '}';
    }
}
