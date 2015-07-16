package gov.pnnl.svf.geometry;

import java.io.Serializable;

public class Arc2D extends Shape2D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned arc *
     */
    public final static Arc2D ZERO = new Arc2D();
    protected final double angle;
    protected final double radius;
    protected final double sector;
    protected final double arcWidth;
    protected final double arcHeight;

    /**
     * Constructor for zero size arc.
     */
    public Arc2D() {
        this(0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param copy the object to copy
     */
    public Arc2D(final double x, final double y, final Arc2D copy) {
        super(x, y);
        angle = copy.angle;
        radius = copy.radius;
        sector = copy.sector;
        arcWidth = copy.arcWidth;
        arcHeight = copy.arcHeight;
    }

    /**
     * Parameterized Constructor
     * <p/>
     * The radius and angle will be normalized.
     *
     * @param angle     the middle angle of the arc in degrees
     * @param radius    the center radius of the sector
     * @param arcWidth  the arc width must be positive
     * @param arcHeight the arc height must be positive
     *
     * @throws IllegalArgumentException if width or height is less than zero
     */
    public Arc2D(final double angle, final double radius, final double arcWidth, final double arcHeight) {
        this(0.0, 0.0, angle, radius, arcWidth, arcHeight);
    }

    /**
     * Parameterized Constructor
     * <p/>
     * The radius and angle will be normalized.
     *
     * @param x         the x offset of the arc
     * @param y         the y offset of the arc
     * @param angle     the middle angle of the arc in degrees
     * @param radius    the center radius of the sector
     * @param arcWidth  the arc width must be positive
     * @param arcHeight the arc height must be positive
     *
     * @throws IllegalArgumentException if width or height is less than zero
     */
    public Arc2D(final double x, final double y, final double angle, final double radius, final double arcWidth, final double arcHeight) {
        super(x, y);
        if (Double.compare(arcWidth, 0.0) < 0) {
            throw new IllegalArgumentException("arcWidth");
        }
        if (Double.compare(arcHeight, 0.0) < 0) {
            throw new IllegalArgumentException("arcHeight");
        }
        this.angle = Arc2D.normalize(angle);
        this.radius = Math.abs(radius);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        if (this.radius > 0.0) {
            sector = (180.0 * this.arcWidth) / (Math.PI * this.radius);
        } else {
            sector = 0.0;
        }
    }

    /**
     * Static convenience constructor
     *
     * @param start the start of the arc in degrees
     * @param angle the angle of the arc in degrees
     * @param inner the inner radius
     * @param outer the outer radius
     *
     * @return a new arc
     *
     * @throws IllegalArgumentException if end is less than start or outer is
     *                                  less than inner
     */
    public static Arc2D newInstance(final double start, final double angle, final double inner, final double outer) {
        return Arc2D.newInstance(0.0, 0.0, start, angle, inner, outer);
    }

    /**
     * Static convenience constructor
     *
     * @param x     the x offset of the arc
     * @param y     the y offset of the arc
     * @param start the start of the arc in degrees
     * @param angle the angle of the arc in degrees
     * @param inner the inner radius
     * @param outer the outer radius
     *
     * @return a new arc
     *
     * @throws IllegalArgumentException if outer is less than inner
     */
    public static Arc2D newInstance(final double x, final double y, final double start, final double angle, final double inner, final double outer) {
        if (outer < inner) {
            throw new IllegalArgumentException("outer");
        }
        final double arcHeight = outer - inner;
        final double radius = inner + (arcHeight / 2.0);
        final double arcWidth = (angle * Math.PI * radius) / 180.0;
        return new Arc2D(x, y, start + (angle / 2.0), radius, arcWidth, arcHeight);
    }

    @Override
    public double getHeight() {
        return radius * 2.0 + arcHeight;
    }

    @Override
    public double getWidth() {
        return radius * 2.0 + arcHeight;
    }

    /**
     * The middle angle of the arc (sector middle) in degrees.
     *
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * The center of radius of the arc (sector center).
     *
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * The sector angle of the arc in degrees.
     *
     * @return the arc length
     */
    public double getSector() {
        return sector;
    }

    /**
     * The width of the arc.
     *
     * @return the arc width
     */
    public double getArcWidth() {
        return arcWidth;
    }

    /**
     * The height of the arc.
     *
     * @return the arc height
     */
    public double getArcHeight() {
        return arcHeight;
    }

    @Override
    public boolean contains(final double x, final double y) {
        // translate point
        final double fx = x - this.x;
        final double fy = y - this.y;
        // convert point to radial
        final double a = Math.atan2(fy, fx);
        final double r = Math.sqrt(fx * fx + fy * fy);
        return a >= angle - (sector * 0.5) && a <= angle + (sector * 0.5)
               && r >= radius - (arcHeight * 0.5) && r <= radius + (arcHeight * 0.5);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 29 * hash + (int) (Double.doubleToLongBits(angle) ^ (Double.doubleToLongBits(angle) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(radius) ^ (Double.doubleToLongBits(radius) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(sector) ^ (Double.doubleToLongBits(sector) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(arcWidth) ^ (Double.doubleToLongBits(arcWidth) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(arcHeight) ^ (Double.doubleToLongBits(arcHeight) >>> 32));
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
        final Arc2D other = (Arc2D) obj;
        if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle)) {
            return false;
        }
        if (Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius)) {
            return false;
        }
        if (Double.doubleToLongBits(sector) != Double.doubleToLongBits(other.sector)) {
            return false;
        }
        if (Double.doubleToLongBits(arcWidth) != Double.doubleToLongBits(other.arcWidth)) {
            return false;
        }
        if (Double.doubleToLongBits(arcHeight) != Double.doubleToLongBits(other.arcHeight)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Arc2D{" + "x=" + x + ", y=" + y + ",angle=" + angle + ", radius=" + radius + ", sector=" + sector + ", arcWidth=" + arcWidth + ", arcHeight="
               + arcHeight
               + '}';
    }

    private static double normalize(final double angle) {
        if (Double.compare(angle, -360.0) < 0) {
            return (angle + (Math.floor(angle / -360.0) + 1.0) * 360.0) % 360.0;
        } else if (Double.compare(angle, 0.0) < 0) {
            return (angle + 360) % 360.0;
        } else {
            return angle % 360.0;
        }
    }

    /**
     * This builder has two constructor modes. The common mode uses angle,
     * radius, arcWidth, and arcHeight. The alternate uses angle, start, inner,
     * and outer. At least one alternate only value (start, inner, or outer)
     * must be set to use the alternate constructor mode.
     */
    public static class Builder implements BuilderCommon, BuilderAlternate {

        private double x = 0.0;
        private double y = 0.0;
        private double angle = 0.0;
        // primary constructor type
        private double radius = 0.0;
        private double arcWidth = 0.0;
        private double arcHeight = 0.0;
        // alternate constructor type
        private double start = 0.0;
        private double inner = 0.0;
        private double outer = 0.0;
        // flag for alternate constructor
        private boolean alternate = false;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        @Override
        public Builder x(final double x) {
            this.x = x;
            return this;
        }

        @Override
        public Builder y(final double y) {
            this.y = y;
            return this;
        }

        @Override
        public Builder angle(final double angle) {
            this.angle = angle;
            return this;
        }

        @Override
        public BuilderAlternate start(final double start) {
            this.start = start;
            alternate = true;
            return this;
        }

        @Override
        public BuilderAlternate inner(final double inner) {
            this.inner = inner;
            alternate = true;
            return this;
        }

        @Override
        public BuilderAlternate outer(final double outer) {
            this.outer = outer;
            alternate = true;
            return this;
        }

        @Override
        public BuilderCommon radius(final double radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public BuilderCommon arcWidth(final double arcWidth) {
            this.arcWidth = arcWidth;
            return this;
        }

        @Override
        public BuilderCommon arcHeight(final double arcHeight) {
            this.arcHeight = arcHeight;
            return this;
        }

        @Override
        public Arc2D build() {
            if (alternate) {
                return Arc2D.newInstance(x, y, start, angle, inner, outer);
            } else {
                return new Arc2D(x, y, angle, radius, arcWidth, arcHeight);
            }
        }
    }

    public static interface BuilderCommon {

        BuilderCommon x(double x);

        BuilderCommon y(double y);

        BuilderCommon angle(double angle);

        BuilderCommon radius(double radius);

        BuilderCommon arcWidth(double arcWidth);

        BuilderCommon arcHeight(double arcHeight);

        Arc2D build();
    }

    public static interface BuilderAlternate {

        BuilderAlternate x(double x);

        BuilderAlternate y(double y);

        BuilderAlternate angle(double angle);

        BuilderAlternate start(double start);

        BuilderAlternate inner(double inner);

        BuilderAlternate outer(double outer);

        Arc2D build();
    }
}
