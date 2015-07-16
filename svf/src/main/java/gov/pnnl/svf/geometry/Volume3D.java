package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class Volume3D extends Cuboid3D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned volume
     */
    public final static Volume3D ZERO = new Volume3D();
    /**
     * Constant one-dimensioned volume (cube)
     */
    public final static Volume3D ONE = new Volume3D(1.0, 1.0, 1.0);
    /**
     * Default number of slices utilized.
     */
    public final static int DEFAULT_SLICES = 64;
    protected final int slices;

    /**
     * Constructor for zero size volume.
     */
    public Volume3D() {
        this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, DEFAULT_SLICES);
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param z    the new z offset
     * @param copy the object to copy
     */
    public Volume3D(final double x, final double y, final double z, final Volume3D copy) {
        super(x, y, z, copy.width, copy.height, copy.depth);
        slices = copy.slices;
    }

    /**
     * Constructor for zero size volume.
     *
     * @param slices must be greater than zero
     */
    public Volume3D(final int slices) {
        this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, slices);
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
    public Volume3D(final double width, final double height, final double depth) {
        this(0.0, 0.0, 0.0, width, height, depth, DEFAULT_SLICES);
    }

    /**
     * Parameterized Constructor
     *
     * @param width  must be positive
     * @param height must be positive
     * @param depth  must be positive
     * @param slices must be greater than zero
     *
     * @throws IllegalArgumentException if width, height, or depth is less than
     *                                  zero
     */
    public Volume3D(final double width, final double height, final double depth, final int slices) {
        this(0.0, 0.0, 0.0, width, height, depth, slices);
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
    public Volume3D(final double x, final double y, final double z, final double width, final double height, final double depth) {
        this(x, y, z, width, height, depth, DEFAULT_SLICES);
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
     * @param slices must be greater than zero
     *
     * @throws IllegalArgumentException if width, height, or depth is less than
     *                                  zero
     */
    public Volume3D(final double x, final double y, final double z, final double width, final double height, final double depth, final int slices) {
        super(x, y, z, width, height, depth);
        if (slices < 1) {
            throw new IllegalArgumentException("slices");
        }
        this.slices = slices;
    }

    /**
     * The number of slices utilized to draw the volume.
     *
     * @return the slices
     */
    public int getSlices() {
        return slices;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + slices;
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
        final Volume3D other = (Volume3D) obj;
        if (slices != other.slices) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Volume3D{" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", depth=" + depth + ", slices=" + slices + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double z = 0.0;
        private double width = 0.0;
        private double height = 0.0;
        private double depth = 0.0;
        private int slices = DEFAULT_SLICES;

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

        public Builder slices(final int slices) {
            this.slices = slices;
            return this;
        }

        public Volume3D build() {
            return new Volume3D(x, y, z, width, height, depth, slices);
        }
    }
}
