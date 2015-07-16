package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class Rectangle extends Shape implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned rectangle *
     */
    public final static Rectangle ZERO = new Rectangle();
    protected final int x;
    protected final int y;
    protected final int width;
    protected final int height;

    /**
     * Constructor for zero size rectangle.
     */
    public Rectangle() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param copy the object to copy
     */
    public Rectangle(final int x, final int y, final Rectangle copy) {
        this(x, y, copy.width, copy.height);
    }

    /**
     * Parameterized Constructor
     *
     * @param x      the left of the rectangle
     * @param y      the bottom of the rectangle
     * @param width  must be positive
     * @param height must be positive
     *
     * @throws IllegalArgumentException if width or height is less than zero
     */
    public Rectangle(final int x, final int y, final int width, final int height) {
        super();
        if (width < 0) {
            throw new IllegalArgumentException("width");
        }
        if (height < 0) {
            throw new IllegalArgumentException("height");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Height of the rectangle.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Width of the rectangle.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * X or left location of the rectangle.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Y or bottom location of the rectangle.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param x the x or left coordinate
     * @param y the y or bottom coordinate
     *
     * @return true if the rectangle contains the point
     */
    public boolean contains(final int x, final int y) {
        return x >= this.x && x <= this.x + width
               && y >= this.y && y <= this.y + height;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 79 * hash + x;
        hash = 79 * hash + y;
        hash = 79 * hash + width;
        hash = 79 * hash + height;
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
        final Rectangle other = (Rectangle) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        if (width != other.width) {
            return false;
        }
        if (height != other.height) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rectangle{" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + '}';
    }

    public static class Builder {

        private int x = 0;
        private int y = 0;
        private int width = 0;
        private int height = 0;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public Builder x(final int x) {
            this.x = x;
            return this;
        }

        public Builder y(final int y) {
            this.y = y;
            return this;
        }

        public Builder width(final int width) {
            this.width = width;
            return this;
        }

        public Builder height(final int height) {
            this.height = height;
            return this;
        }

        public Rectangle build() {
            return new Rectangle(x, y, width, height);
        }
    }

}
