package gov.pnnl.svf.geometry;

import java.io.Serializable;

/**
 * Immutable shape that represents a point in 2d space.
 *
 * @author Arthur Bleeker
 */
public class Point2D extends Shape2D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned point *
     */
    public final static Point2D ZERO = new Point2D();

    /**
     * Constructor
     */
    public Point2D() {
        super();
    }

    /**
     * Constructor
     *
     * @param x the x offset for this shape
     * @param y the y offset for this shape
     */
    public Point2D(final double x, final double y) {
        super(x, y);
    }

    @Override
    public double getWidth() {
        return 0.0;
    }

    @Override
    public double getHeight() {
        return 0.0;
    }

    @Override
    public boolean contains(final double x, final double y) {
        return Double.compare(this.x, x) == 0 && Double.compare(this.y, y) == 0;
    }

    @Override
    public String toString() {
        return "Point2D{" + "x=" + x + ", y=" + y + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public Builder x(final double x) {
            this.x = x;
            return this;
        }

        public Builder y(final double y) {
            this.y = y;
            return this;
        }

        public Point2D build() {
            return new Point2D(x, y);
        }
    }

}
