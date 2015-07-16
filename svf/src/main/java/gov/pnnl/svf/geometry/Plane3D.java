package gov.pnnl.svf.geometry;

import java.io.Serializable;
import org.apache.commons.math.geometry.Vector3D;

public class Plane3D extends Shape3D implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final double distance;

    /**
     * Constructor for the plane Ax + By + Cz + D = 0
     *
     * @param x        the x normal
     * @param y        the y normal
     * @param z        the z normal
     * @param distance the distance from origin
     */
    public Plane3D(final double x, final double y, final double z, final double distance) {
        super(x, y, z);
        this.distance = distance;
    }

    /**
     * Convenience constructor for the plane Ax + By + Cz + D = 0
     *
     * @param normal   the plane normal (must be normalized)
     * @param distance the distance from origin
     *
     * @return the new instance
     *
     * @throws NullPointerException if normal is null
     */
    public static Plane3D newInstance(final Vector3D normal, final double distance) {
        if (normal == null) {
            throw new NullPointerException("normal");
        }
        Vector3D n;
        try {
            n = normal.normalize();
        } catch (final ArithmeticException ex) {
            // can't do anything with a zero normal
            n = Vector3D.ZERO;
        }
        return new Plane3D(n.getX(), n.getY(), n.getZ(), distance);
    }

    /**
     * Constructor takes three points in counter clockwise rotation
     *
     * @param first  first point
     * @param second second point
     * @param third  third point
     *
     * @return the new instance
     *
     * @throws NullPointerException if any points are null
     */
    public static Plane3D newInstance(final Vector3D first, final Vector3D second, final Vector3D third) {
        if (first == null) {
            throw new NullPointerException("first");
        }
        if (second == null) {
            throw new NullPointerException("second");
        }
        if (third == null) {
            throw new NullPointerException("third");
        }
        final Vector3D aux1 = first.subtract(second);
        final Vector3D aux2 = third.subtract(second);
        Vector3D normal;
        try {
            normal = Vector3D.crossProduct(aux2, aux1).normalize();
        } catch (final ArithmeticException ex) {
            // can't do anything with a zero normal
            normal = Vector3D.ZERO;
        }
        return Plane3D.newInstance(normal, -(Vector3D.dotProduct(normal, second)));
    }

    /**
     * The distance of the supplied point from the frustum origin. A point on
     * the normal side will be positive.
     *
     * @param point the point to test
     *
     * @return the distance
     *
     * @throws NullPointerException if point is null
     */
    public double distance(final Vector3D point) {
        if (point == null) {
            throw new NullPointerException("point");
        }
        return distance(point.getX(), point.getY(), point.getZ());
    }

    /**
     * The distance of the supplied point from the frustum origin. A point on
     * the normal side will be positive.
     *
     * @param x x of the point to test
     * @param y y of the point to test
     * @param z z of the point to test
     *
     * @return the distance
     */
    public double distance(final double x, final double y, final double z) {
        if (Double.compare(0.0, x) == 0
            && Double.compare(0.0, y) == 0
            && Double.compare(0.0, z) == 0) {
            return distance + 0.0;
        } else {
            // distance + dot product
            return distance + (this.x * x) + (this.y * y) + (this.z * z);
        }
    }

    /**
     * A plane contains the point if it's located on the non-normal side of the
     * plane.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     *
     * @return true if this shape contains the point
     */
    @Override
    public boolean contains(final double x, final double y, final double z) {
        return Double.compare(distance(x, y, z), 0.0) < 0;
    }

    /**
     * A plane contains the point if it's located on the non-normal side of the
     * plane.
     *
     * @param point the point to test
     *
     * @return true if this shape contains the point
     *
     * @throws NullPointerException if the point is null
     */
    public boolean contains(final Vector3D point) {
        return Double.compare(distance(point), 0.0) < 0;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    @Override
    public double getHeight() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getWidth() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getDepth() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + (int) (Double.doubleToLongBits(distance) ^ (Double.doubleToLongBits(distance) >>> 32));
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
        final Plane3D other = (Plane3D) obj;
        if (Double.doubleToLongBits(distance) != Double.doubleToLongBits(other.distance)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Plane3D{" + "x=" + x + ", y=" + y + ", z=" + z + ", distance=" + distance + '}';
    }

    public static class Builder implements BuilderCommon, BuilderAlternate {

        // primary constructor type
        private double x = 0.0;
        private double y = 0.0;
        private double z = 0.0;
        private double distance;
        // alternate constructor type
        private Vector3D first = Vector3D.ZERO;
        private Vector3D second = Vector3D.ZERO;
        private Vector3D third = Vector3D.ZERO;
        // flag for alternate constructor
        private boolean alternate = false;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        @Override
        public BuilderCommon x(final double x) {
            this.x = x;
            return this;
        }

        @Override
        public BuilderCommon y(final double y) {
            this.y = y;
            return this;
        }

        @Override
        public BuilderCommon z(final double z) {
            this.z = z;
            return this;
        }

        @Override
        public BuilderCommon distance(final double distance) {
            this.distance = distance;
            return this;
        }

        @Override
        public BuilderAlternate first(final Vector3D first) {
            this.first = first;
            alternate = true;
            return this;
        }

        @Override
        public BuilderAlternate second(final Vector3D second) {
            this.second = second;
            alternate = true;
            return this;
        }

        @Override
        public BuilderAlternate third(final Vector3D third) {
            this.third = third;
            alternate = true;
            return this;
        }

        @Override
        public Plane3D build() {
            if (alternate) {
                return Plane3D.newInstance(first, second, third);
            } else {
                return new Plane3D(x, y, z, distance);
            }
        }
    }

    public static interface BuilderCommon {

        BuilderCommon x(double x);

        BuilderCommon y(double y);

        BuilderCommon z(double z);

        BuilderCommon distance(double distance);

        Plane3D build();
    }

    public static interface BuilderAlternate {

        BuilderAlternate first(Vector3D first);

        BuilderAlternate second(Vector3D second);

        BuilderAlternate third(Vector3D third);

        Plane3D build();
    }

}
