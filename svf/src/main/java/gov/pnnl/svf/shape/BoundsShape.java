package gov.pnnl.svf.shape;

import gov.pnnl.svf.geometry.Shape2D;

/**
 * This shape represents the bounds.
 *
 * @param <T> The 2D shape represented
 *
 * @author Arthur Bleeker
 */
public interface BoundsShape<T extends Shape2D> {

    /**
     * String representation of a field in this object.
     */
    String BOUNDS = "bounds";

    /**
     * @return the bounds of this actor
     */
    T getBounds();
}
