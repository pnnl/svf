package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class Rectangle2D extends Shape2D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned rectangle
     */
    public final static Rectangle2D ZERO = new Rectangle2D();
    /**
     * Constant one-dimensioned rectangle
     */
    public final static Rectangle2D ONE = new Rectangle2D(1.0, 1.0);
    protected final double width;
    protected final double height;

    /**
     * Constructor for zero size rectangle.
     */
    public Rectangle2D() {
        this(0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param copy the object to copy
     */
    public Rectangle2D(final double x, final double y, final Rectangle2D copy) {
        this(x, y, copy.width, copy.height);
    }

    /**
     * Parameterized Constructor
     *
     * @param width  must be positive
     * @param height must be positive
     *
     * @throws IllegalArgumentException if width or height is less than zero
     */
    public Rectangle2D(final double width, final double height) {
        this(0.0, 0.0, width, height);
    }

    /**
     * Parameterized Constructor
     *
     * @param x      the horizontal center of the rectangle
     * @param y      the vertical center of the rectangle
     * @param width  must be positive
     * @param height must be positive
     *
     * @throws IllegalArgumentException if width or height is less than zero
     */
    public Rectangle2D(final double x, final double y, final double width, final double height) {
        super(x, y);
        if (Double.compare(width, 0.0) < 0) {
            throw new IllegalArgumentException("width");
        }
        if (Double.compare(height, 0.0) < 0) {
            throw new IllegalArgumentException("height");
        }
        this.width = width;
        this.height = height;
    }

    /**
     * Width of the rectangle.
     *
     * @return the width
     */
    @Override
    public double getWidth() {
        return width;
    }

    /**
     * Height of the rectangle.
     *
     * @return the height
     */
    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public boolean contains(final double x, final double y) {
        return x >= this.x - (width * 0.5) && x <= this.x + (width * 0.5)
               && y >= this.y - (height * 0.5) && y <= this.y + (height * 0.5);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + (int) (Double.doubleToLongBits(width) ^ (Double.doubleToLongBits(width) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(height) ^ (Double.doubleToLongBits(height) >>> 32));
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
        final Rectangle2D other = (Rectangle2D) obj;
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rectangle2D{" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double width = 0.0;
        private double height = 0.0;

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

        public Builder width(final double width) {
            this.width = width;
            return this;
        }

        public Builder height(final double height) {
            this.height = height;
            return this;
        }

        public Rectangle2D build() {
            return new Rectangle2D(x, y, width, height);
        }
    }

}
