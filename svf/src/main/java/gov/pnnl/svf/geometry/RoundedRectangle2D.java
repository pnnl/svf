package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class RoundedRectangle2D extends Rectangle2D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * The default amount of roundness.
     */
    public final static double DEFAULT_ROUNDNESS = 0.25;
    /**
     * Constant zero-dimensioned rectangle
     */
    public final static RoundedRectangle2D ZERO = new RoundedRectangle2D();
    /**
     * Constant one-dimensioned rectangle
     */
    public final static RoundedRectangle2D ONE = new RoundedRectangle2D(1.0, 1.0, DEFAULT_ROUNDNESS);
    protected final double roundness;

    /**
     * Constructor for zero size rectangle.
     */
    public RoundedRectangle2D() {
        this(0.0, 0.0, 0.0, 0.0, DEFAULT_ROUNDNESS);
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param copy the object to copy
     */
    public RoundedRectangle2D(final double x, final double y, final RoundedRectangle2D copy) {
        this(x, y, copy.width, copy.height, copy.getRoundness());
    }

    /**
     * Parameterized Constructor
     *
     * @param width     must be positive
     * @param height    must be positive
     * @param roundness must be positive
     *
     * @throws IllegalArgumentException if width, height, or roundness is less
     *                                  than zero
     */
    public RoundedRectangle2D(final double width, final double height, final double roundness) {
        this(0.0, 0.0, width, height, roundness);
    }

    /**
     * Parameterized Constructor
     *
     * @param x         the horizontal center of the rectangle
     * @param y         the vertical center of the rectangle
     * @param width     must be positive
     * @param height    must be positive
     * @param roundness must be positive
     *
     * @throws IllegalArgumentException if width, height, or roundness is less
     *                                  than zero
     */
    public RoundedRectangle2D(final double x, final double y, final double width, final double height, final double roundness) {
        super(x, y, width, height);
        if (Double.compare(roundness, 0.0) < 0) {
            throw new IllegalArgumentException("roundness");
        }
        // can't be more than half of the shortest edge
        this.roundness = Math.min(Math.min(width * 0.5, height * 0.5), roundness);
    }

    /**
     * Roundness of the rectangle. This value will already be adjusted so that
     * it is never more then half of the shortest edge.
     *
     * @return the roundness
     */
    public double getRoundness() {
        return roundness;
    }

    @Override
    public boolean contains(final double x, final double y) {
        // TODO implement more precise contains method
        return x >= this.x - (width * 0.5) && x <= this.x + (width * 0.5)
               && y >= this.y - (height * 0.5) && y <= this.y + (height * 0.5);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + (int) (Double.doubleToLongBits(roundness) ^ (Double.doubleToLongBits(roundness) >>> 32));
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
        final RoundedRectangle2D other = (RoundedRectangle2D) obj;
        if (Double.doubleToLongBits(roundness) != Double.doubleToLongBits(other.roundness)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rectangle2D{" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", roundness=" + roundness + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double width = 0.0;
        private double height = 0.0;
        private double roundness = DEFAULT_ROUNDNESS;

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

        public Builder roundness(final double roundness) {
            this.roundness = roundness;
            return this;
        }

        public RoundedRectangle2D build() {
            return new RoundedRectangle2D(x, y, width, height, roundness);
        }
    }

}
