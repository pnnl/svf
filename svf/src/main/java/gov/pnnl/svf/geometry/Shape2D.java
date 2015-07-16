package gov.pnnl.svf.geometry;

import java.io.Serializable;

public abstract class Shape2D extends Shape implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final double x;
    protected final double y;

    /**
     * Constructor
     */
    protected Shape2D() {
        super();
        x = 0.0;
        y = 0.0;
    }

    /**
     * Constructor
     *
     * @param x the x offset for this shape
     * @param y the y offset for this shape
     */
    protected Shape2D(final double x, final double y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * The x offset for this shape.
     *
     * @return the x offset for this shape
     */
    public double getX() {
        return x;
    }

    /**
     * The y offset for this shape.
     *
     * @return the y offset for this shape
     */
    public double getY() {
        return y;
    }

    /**
     * Width of this shape.
     *
     * @return the width
     */
    public abstract double getWidth();

    /**
     * Height of this shape.
     *
     * @return the height
     */
    public abstract double getHeight();

    /**
     * Test the shape to see if it contains the point.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     *
     * @return true if this shape contains the point
     */
    public abstract boolean contains(final double x, final double y);

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 29 * hash + (int) (Double.doubleToLongBits(x) ^ (Double.doubleToLongBits(x) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(y) ^ (Double.doubleToLongBits(y) >>> 32));
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
        final Shape2D other = (Shape2D) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Shape2D{" + "x=" + x + ", y=" + y + '}';
    }
}
