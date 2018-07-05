package gov.pnnl.svf.shape;

/**
 * This shape represents an arc or segment of a circle which may or may not
 * originate from the center.
 *
 * @author Amelia Bleeker
 */
public interface ArcShape {

    /**
     * String representation of a field in this object.
     */
    String ARC_LENGTH = "arcLength";
    /**
     * String representation of a field in this object.
     */
    String ARC_START = "arcStart";
    /**
     * String representation of a field in this object.
     */
    String ARC_WIDTH = "arcWidth";

    /**
     * @return the length of the arc in degrees
     */
    double getArcLength();

    /**
     * @return the start of the arc in degrees
     */
    double getArcStart();

    /**
     * @return the width or relative distance from the outer to inner arc
     */
    double getArcWidth();

    /**
     * @param arcLength the length of the arc in degrees to set
     */
    void setArcLength(double arcLength);

    /**
     * @param arcStart the start of the arc in degrees to set
     */
    void setArcStart(double arcStart);

    /**
     * @param arcWidth the width or relative distance from the outer to inner
     *                 arc to set
     */
    void setArcWidth(double arcWidth);
}
