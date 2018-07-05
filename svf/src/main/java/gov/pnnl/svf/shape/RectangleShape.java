package gov.pnnl.svf.shape;

/**
 * This shape represents a rectangle.
 *
 * @author Amelia Bleeker
 */
public interface RectangleShape {

    /**
     * String representation of a field in this object.
     */
    String WIDTH = "width";
    /**
     * String representation of a field in this object.
     */
    String HEIGHT = "height";

    /**
     * @return the width
     */
    double getWidth();

    /**
     * @return the height
     */
    double getHeight();

    /**
     * @param width the height
     */
    void setWidth(double width);

    /**
     * @param height the height
     */
    void setHeight(double height);
}
