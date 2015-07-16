package gov.pnnl.svf.shape;

/**
 * Shape that represents an item with a radius.
 *
 * @author Arthur Bleeker
 */
public interface RadialShape {

    /**
     * String representation of a field in this object.
     */
    String RADIUS = "radius";

    /**
     * @return the radius
     */
    double getRadius();

    /**
     * @param radius the radius to set
     */
    void setRadius(double radius);
}
