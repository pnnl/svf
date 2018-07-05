package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.geometry.Path;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Immutable shape that represents a path in 3d space.
 *
 * @author Amelia Bleeker
 */
public class Path3D extends Shape3D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned path
     */
    public final static Path3D ZERO = new Path3D();
    protected final Path style;
    protected final List<Point3D> points;
    protected final Point3D offset;
    protected final double width;
    protected final double height;
    protected final double depth;

    /**
     * Constructor
     */
    public Path3D() {
        super();
        style = Path.NONE;
        points = Collections.emptyList();
        offset = Point3D.ZERO;
        width = 0.0;
        height = 0.0;
        depth = 0.0;
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param z    the new z offset
     * @param copy the object to copy
     */
    public Path3D(final double x, final double y, final double z, final Path3D copy) {
        super(x, y, z);
        style = copy.style;
        points = copy.points;
        offset = copy.offset;
        width = copy.width;
        height = copy.height;
        depth = copy.depth;
    }

    /**
     * Constructor
     *
     * @param x the x offset for this shape
     * @param y the y offset for this shape
     * @param z the z offset for this shape
     */
    public Path3D(final double x, final double y, final double z) {
        super(x, y, z);
        style = Path.NONE;
        points = Collections.emptyList();
        offset = Point3D.ZERO;
        width = 0.0;
        height = 0.0;
        depth = 0.0;
    }

    /**
     * Constructor
     *
     * @param points the list of points for this path
     *
     * @throws NullPointerException if points is null
     */
    public Path3D(final Point3D... points) {
        this(0.0, 0.0, 0.0, Path.NONE, points);
    }

    /**
     * Constructor
     *
     * @param points the list of points for this path
     *
     * @throws NullPointerException if points is null
     */
    public Path3D(final Collection<Point3D> points) {
        this(0.0, 0.0, 0.0, Path.NONE, points);
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param z      the z offset for this shape
     * @param points the list of points for this path
     *
     * @throws NullPointerException if points is null
     */
    public Path3D(final double x, final double y, final double z, final Point3D... points) {
        this(x, y, z, Path.NONE, points);
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param z      the z offset for this shape
     * @param points the list of points for this path
     *
     * @throws NullPointerException if points is null
     */
    public Path3D(final double x, final double y, final double z, final Collection<Point3D> points) {
        this(x, y, z, Path.NONE, points);
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param z      the z offset for this shape
     * @param style  style for the path
     * @param points the list of points for this path
     *
     * @throws NullPointerException if points is null
     */
    public Path3D(final double x, final double y, final double z, final Path style, final Point3D... points) {
        this(x, y, z, style, Arrays.asList(points));
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param z      the z offset for this shape
     * @param style  style for the path
     * @param points the list of points for this path
     *
     * @throws NullPointerException if points is null
     */
    public Path3D(final double x, final double y, final double z, final Path style, final Collection<Point3D> points) {
        super(x, y, z);
        if (style == null) {
            throw new NullPointerException("style");
        }
        this.style = style;
        if (points == null || points.isEmpty()) {
            this.points = Collections.emptyList();
            offset = Point3D.ZERO;
            width = 0.0;
            height = 0.0;
            depth = 0.0;
        } else {
            double left = Double.MAX_VALUE;
            double right = -Double.MAX_VALUE;
            double bottom = Double.MAX_VALUE;
            double top = -Double.MAX_VALUE;
            double front = Double.MAX_VALUE;
            double back = -Double.MAX_VALUE;
            for (final Point3D point : points) {
                left = Math.min(left, point.getX());
                right = Math.max(right, point.getX());
                bottom = Math.min(bottom, point.getY());
                top = Math.max(top, point.getY());
                front = Math.max(front, point.getZ());
                back = Math.min(back, point.getZ());
            }
            width = right - left;
            height = top - bottom;
            depth = front - back;
            offset = new Point3D(left + (width / 2.0), bottom + (height / 2.0), back + (depth / 2.0));
            final List<Point3D> temp = new ArrayList<>(points);
            this.points = Collections.unmodifiableList(temp);
        }
    }

    /**
     * The style for this path to use when drawing.
     *
     * @return the path style
     */
    public Path getStyle() {
        return style;
    }

    /**
     * List of immutable points.
     *
     * @return list of points
     */
    public List<Point3D> getPoints() {
        return points;
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

    /**
     * @return the offset to use if centering the path
     */
    public Point3D getOffset() {
        return offset;
    }

    @Override
    public boolean contains(final double x, final double y, final double z) {
        for (final Point3D point : points) {
            if (Double.compare(point.getX(), x) == 0
                && Double.compare(point.getY(), y) == 0
                && Double.compare(point.getZ(), z) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 41 * hash + (style != null ? style.hashCode() : 0);
        hash = 41 * hash + (points != null ? points.hashCode() : 0);
        hash = 41 * hash + (offset != null ? offset.hashCode() : 0);
        hash = 41 * hash + (int) (Double.doubleToLongBits(width) ^ (Double.doubleToLongBits(width) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(height) ^ (Double.doubleToLongBits(height) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(depth) ^ (Double.doubleToLongBits(depth) >>> 32));
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
        final Path3D other = (Path3D) obj;
        if (style != other.style && (style == null || !style.equals(other.style))) {
            return false;
        }
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        if (Double.doubleToLongBits(depth) != Double.doubleToLongBits(other.depth)) {
            return false;
        }
        if (offset != other.offset && (offset == null || !offset.equals(other.offset))) {
            return false;
        }
        // test collection as a last resort
        if (points != other.points && (points == null || !points.equals(other.points))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Path3D{" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", depth=" + depth + ", style=" + style + '}';
    }

    public static class Builder {

        private double x = 0.0;
        private double y = 0.0;
        private double z = 0.0;
        private Path style = Path.NONE;
        private List<Point3D> points = Collections.emptyList();

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

        public Builder style(final Path style) {
            this.style = style;
            return this;
        }

        public Builder points(final Point3D... points) {
            this.points = points != null ? Arrays.asList(points) : Collections.<Point3D>emptyList();
            return this;
        }

        public Builder points(final List<Point3D> points) {
            this.points = points;
            return this;
        }

        public Builder addPoint(final Point3D point) {
            if (point != null) {
                if (points == null || points.isEmpty()) {
                    points = new ArrayList<>();
                }
                points.add(point);
            }
            return this;
        }

        public Builder addPoint(final double x, final double y, final double z) {
            if (points == null || points.isEmpty()) {
                points = new ArrayList<>();
            }
            points.add(new Point3D(x, y, z));
            return this;
        }

        public Path3D build() {
            return new Path3D(x, y, z, style, points);
        }
    }
}
