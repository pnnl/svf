package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class Circle2D extends Shape2D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned circle
     */
    public final static Circle2D ZERO = new Circle2D();
    /**
     * Constant one-dimensioned circle
     */
    public final static Circle2D ONE = new Circle2D(1.0);
    protected final double radius;

    /**
     * Constructor for zero size circle.
     */
    public Circle2D() {
        this(0.0, 0.0, 0.0);
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param copy the object to copy
     */
    public Circle2D(final double x, final double y, final Circle2D copy) {
        super(x, y);
        radius = copy.radius;
    }

    /**
     * Parameterized Constructor
     * <p>
     * The radius will be normalized.
     *
     * @param radius the radius of the circle
     */
    public Circle2D(final double radius) {
        this(0.0, 0.0, radius);
    }

    /**
     * Parameterized Constructor
     * <p>
     * The radius will be normalized.
     *
     * @param x      the x offset of the circle
     * @param y      the y offset of the circle
     * @param radius the radius of the circle
     */
    public Circle2D(final double x, final double y, final double radius) {
        super(x, y);
        this.radius = Math.abs(radius);
    }

    /**
     * Diameter of the circle.
     *
     * @return the height
     */
    @Override
    public double getHeight() {
        return radius * 2.0;
    }

    /**
     * Diameter of the circle.
     *
     * @return the width
     */
    @Override
    public double getWidth() {
        return radius * 2.0;
    }

    /**
     * The radius of the circle.
     *
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    @Override
    public boolean contains(final double x, final double y) {
        // translate point
        final double fx = x - this.x;
        final double fy = y - this.y;
        // find the distance
        final double d = Math.sqrt(fx * fx + fy * fy);
        return d <= radius;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 19 * hash + (int) (Double.doubleToLongBits(radius) ^ (Double.doubleToLongBits(radius) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Circle2D other = (Circle2D) obj;
        if (Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Circle2D{" + "x=" + x + ", y=" + y + ",radius=" + radius + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double radius = 0.0;

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

        public Builder radius(final double radius) {
            this.radius = radius;
            return this;
        }

        public Circle2D build() {
            return new Circle2D(x, y, radius);
        }
    }
}
