package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class Point3D extends Shape3D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned point *
     */
    public final static Point3D ZERO = new Point3D();

    /**
     * Constructor
     */
    public Point3D() {
        super();
    }

    /**
     * Constructor
     *
     * @param x the x offset for this shape
     * @param y the y offset for this shape
     * @param z the z offset for this shape
     */
    public Point3D(final double x, final double y, final double z) {
        super(x, y, z);
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
    public double getDepth() {
        return 0.0;
    }

    @Override
    public boolean contains(final double x, final double y, final double z) {
        return this.x == x && this.y == y && this.z == z;
    }

    @Override
    public String toString() {
        return "Point3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double z = 0.0;

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

        public Point3D build() {
            return new Point3D(x, y, z);
        }
    }

}
