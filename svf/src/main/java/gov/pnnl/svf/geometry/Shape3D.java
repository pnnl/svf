package gov.pnnl.svf.geometry;

import java.io.Serializable;

public abstract class Shape3D extends Shape implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final double x;
    protected final double y;
    protected final double z;

    /**
     * Constructor
     */
    protected Shape3D() {
        this(0.0, 0.0, 0.0);
    }

    /**
     * Constructor
     *
     * @param x the x offset for this shape
     * @param y the y offset for this shape
     * @param z the z offset for this shape
     */
    protected Shape3D(final double x, final double y, final double z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
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
     * The z offset for this shape.
     *
     * @return the z offset for this shape
     */
    public double getZ() {
        return z;
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
     * Depth of this shape.
     *
     * @return the depth
     */
    public abstract double getDepth();

    /**
     * Test the shape to see if it contains the point.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     *
     * @return true if this shape contains the point
     */
    public abstract boolean contains(final double x, final double y, final double z);

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 41 * hash + (int) (Double.doubleToLongBits(x) ^ (Double.doubleToLongBits(x) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(y) ^ (Double.doubleToLongBits(y) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(z) ^ (Double.doubleToLongBits(z) >>> 32));
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
        final Shape3D other = (Shape3D) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Shape3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
