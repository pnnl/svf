package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class Sphere3D extends Shape3D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned sphere
     */
    public final static Sphere3D ZERO = new Sphere3D();
    /**
     * Constant one-dimensioned sphere
     */
    public final static Sphere3D ONE = new Sphere3D(1.0);
    protected final double radius;

    /**
     * Constructor for zero size sphere.
     */
    public Sphere3D() {
        this(0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param z    the new z offset
     * @param copy the object to copy
     */
    public Sphere3D(final double x, final double y, final double z, final Sphere3D copy) {
        super(x, y, z);
        radius = copy.radius;
    }

    /**
     * Parameterized Constructor
     * <p>
     * The radius will be normalized.
     *
     * @param radius the radius
     */
    public Sphere3D(final double radius) {
        this(0.0, 0.0, 0.0, radius);
    }

    /**
     * Parameterized Constructor
     * <p>
     * The radius will be normalized.
     *
     * @param x      the x offset of the sphere
     * @param y      the y offset of the sphere
     * @param z      the z offset of the sphere
     * @param radius the radius
     */
    public Sphere3D(final double x, final double y, final double z, final double radius) {
        super(x, y, z);
        this.radius = Math.abs(radius);
    }

    /**
     * Diameter of the sphere.
     *
     * @return the height
     */
    @Override
    public double getHeight() {
        return radius * 2.0;
    }

    /**
     * Diameter of the sphere.
     *
     * @return the width
     */
    @Override
    public double getWidth() {
        return radius * 2.0;
    }

    /**
     * Diameter of the sphere.
     *
     * @return the depth
     */
    @Override
    public double getDepth() {
        return radius * 2.0;
    }

    /**
     * The radius of the sphere.
     *
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    @Override
    public boolean contains(final double x, final double y, final double z) {
        // translate point
        final double fx = x - this.x;
        final double fy = y - this.y;
        final double fz = z - this.z;
        // find the distance
        final double d = Math.cbrt(fx * fx + fy * fy + fz * fz);
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
        final Sphere3D other = (Sphere3D) obj;
        if (Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Sphere3D{" + "x=" + x + ", y=" + y + ", z=" + z + ",radius=" + radius + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double z = 0.0;
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

        public Builder z(final double z) {
            this.z = z;
            return this;
        }

        public Builder radius(final double radius) {
            this.radius = radius;
            return this;
        }

        public Sphere3D build() {
            return new Sphere3D(x, y, z, radius);
        }
    }

}
