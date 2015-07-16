package gov.pnnl.svf.core.geometry;

/**
 * Represents a path style in a dimensional environment.
 *
 * @author Arthur Bleeker
 */
public enum Path {

    /**
     * No style
     */
    NONE,
    /**
     * Draw points
     */
    POINTS,
    /**
     * Draw using NURBS
     */
    NURBS,
    /**
     * Draw using Bezier curves
     */
    BEZIER;
}
