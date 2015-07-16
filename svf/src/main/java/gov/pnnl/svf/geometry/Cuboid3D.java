package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class Cuboid3D extends Shape3D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned cuboid
     */
    public final static Cuboid3D ZERO = new Cuboid3D();
    /**
     * Constant one-dimensioned cuboid (cube)
     */
    public final static Cuboid3D ONE = new Cuboid3D(1.0, 1.0, 1.0);
    protected final double width;
    protected final double height;
    protected final double depth;

    /**
     * Constructor for zero size rectangle.
     */
    public Cuboid3D() {
        this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param z    the new z offset
     * @param copy the object to copy
     */
    public Cuboid3D(final double x, final double y, final double z, final Cuboid3D copy) {
        super(x, y, z);
        width = copy.width;
        height = copy.height;
        depth = copy.depth;
    }

    /**
     * Parameterized Constructor
     *
     * @param width  must be positive
     * @param height must be positive
     * @param depth  must be positive
     *
     * @throws IllegalArgumentException if width, height, or depth is less than
     *                                  zero
     */
    public Cuboid3D(final double width, final double height, final double depth) {
        this(0.0, 0.0, 0.0, width, height, depth);
    }

    /**
     * Parameterized Constructor
     *
     * @param x      the horizontal center of the cuboid
     * @param y      the vertical center of the cuboid
     * @param z      the vertical center of the cuboid
     * @param width  must be positive
     * @param height must be positive
     * @param depth  must be positive
     *
     * @throws IllegalArgumentException if width, height, or depth is less than
     *                                  zero
     */
    public Cuboid3D(final double x, final double y, final double z, final double width, final double height, final double depth) {
        super(x, y, z);
        if (Double.compare(width, 0.0) < 0) {
            throw new IllegalArgumentException("width");
        }
        if (Double.compare(height, 0.0) < 0) {
            throw new IllegalArgumentException("height");
        }
        if (Double.compare(depth, 0.0) < 0) {
            throw new IllegalArgumentException("depth");
        }
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getDepth() {
        return depth;
    }

    @Override
    public boolean contains(final double x, final double y, final double z) {
        return x >= this.x - (width * 0.5) && x <= this.x + (width * 0.5)
               && y >= this.y - (height * 0.5) && y <= this.y + (height * 0.5)
               && z >= this.z - (depth * 0.5) && z <= this.z + (depth * 0.5);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + (int) (Double.doubleToLongBits(width) ^ (Double.doubleToLongBits(width) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(height) ^ (Double.doubleToLongBits(height) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(depth) ^ (Double.doubleToLongBits(depth) >>> 32));
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
        final Cuboid3D other = (Cuboid3D) obj;
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        if (Double.doubleToLongBits(depth) != Double.doubleToLongBits(other.depth)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cuboid3D{" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", depth=" + depth + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double z = 0.0;
        private double width = 0.0;
        private double height = 0.0;
        private double depth = 0.0;

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

        public Builder width(final double width) {
            this.width = width;
            return this;
        }

        public Builder height(final double height) {
            this.height = height;
            return this;
        }

        public Builder depth(final double depth) {
            this.depth = depth;
            return this;
        }

        public Cuboid3D build() {
            return new Cuboid3D(x, y, z, width, height, depth);
        }
    }

}
