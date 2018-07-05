package gov.pnnl.svf.shape;

/**
 * Shape that contains points.
 *
 * @author Amelia Bleeker
 */
public interface PointsShape {

    /**
     * String representation of a field in this object.
     */
    String POINTS_SIZE = "pointsSize";
    /**
     * String representation of a field in this object.
     */
    String POINTS_VISIBLE = "pointsVisible";

    /**
     * The size to draw the points that make up the path.
     *
     * @return the point size
     */
    float getPointsSize();

    /**
     * @return true if the points are visible
     */
    boolean isPointsVisible();

    /**
     * The size to draw the points that make up the path.
     *
     * @param pointsSize the point size to set
     */
    void setPointsSize(float pointsSize);

    /**
     * @param pointsVisible set to true to make the points visible
     */
    void setPointsVisible(boolean pointsVisible);
}
