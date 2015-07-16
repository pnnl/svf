package gov.pnnl.svf.demo;

import gov.pnnl.svf.geometry.Rectangle2D;

/**
 *
 * @author Arthur Bleeker
 */
public enum Quadrant {

    /**
     * top left
     */
    TOP_LEFT(new Rectangle2D(-0.25, 0.25, 0.5, 0.5)),
    /**
     * top right
     */
    TOP_RIGHT(new Rectangle2D(0.25, 0.25, 0.5, 0.5)),
    /**
     * bottom left
     */
    BOTTOM_LEFT(new Rectangle2D(-0.25, -0.25, 0.5, 0.5)),
    /**
     * bottom right
     */
    BOTTOM_RIGHT(new Rectangle2D(0.25, -0.25, 0.5, 0.5));

    private final Rectangle2D shape;

    private Quadrant(final Rectangle2D shape) {
        this.shape = shape;
    }

    /**
     * The shape for this quadrant.
     *
     * @return the shape
     */
    public Rectangle2D getShape() {
        return shape;
    }
}
