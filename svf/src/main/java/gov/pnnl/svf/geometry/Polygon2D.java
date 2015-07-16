package gov.pnnl.svf.geometry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Polygon2D extends Shape2D implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Constant zero-dimensioned polygon
     */
    public final static Polygon2D ZERO;
    protected final static List<List<Point2D>> CONTOURS;

    static {
        final List<List<Point2D>> temp = new ArrayList<>();
        temp.add(Collections.unmodifiableList(Arrays.asList(Point2D.ZERO, Point2D.ZERO, Point2D.ZERO)));
        CONTOURS = Collections.unmodifiableList(temp);
        ZERO = new Polygon2D();
    }
    protected final List<List<Point2D>> contours;
    protected final Point2D offset;
    protected final Point2D centroid;
    protected final double width;
    protected final double height;

    /**
     * Constructor
     */
    public Polygon2D() {
        super();
        contours = CONTOURS;
        offset = Point2D.ZERO;
        centroid = Point2D.ZERO;
        width = 0.0;
        height = 0.0;
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param copy the object to copy
     */
    public Polygon2D(final double x, final double y, final Polygon2D copy) {
        super(x, y);
        contours = copy.contours;
        offset = copy.offset;
        centroid = copy.centroid;
        width = copy.width;
        height = copy.height;
    }

    /**
     * Constructor
     *
     * @param x the x offset for this shape
     * @param y the y offset for this shape
     */
    public Polygon2D(final double x, final double y) {
        super(x, y);
        contours = CONTOURS;
        offset = Point2D.ZERO;
        centroid = Point2D.ZERO;
        width = 0.0;
        height = 0.0;
    }

    /**
     * Constructor
     *
     * @param contours the list of contours for this polygon
     *
     * @throws NullPointerException     if contours is null
     * @throws IllegalArgumentException if no contours are present or a contour
     *                                  contains less than 3 points
     */
    public Polygon2D(final Collection<Point2D>... contours) {
        this(0.0, 0.0, contours);
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param points the list of points for this polygon
     *
     * @throws NullPointerException     if points is null
     * @throws IllegalArgumentException if it contains less than 3 points
     */
    @SuppressWarnings("unchecked")
    public Polygon2D(final double x, final double y, final Point2D... points) {
        this(x, y, Arrays.asList(points));
    }

    /**
     * Constructor
     *
     * @param x        the x offset for this shape
     * @param y        the y offset for this shape
     * @param contours the list of contours for this polygon
     *
     * @throws NullPointerException     if contours is null
     * @throws IllegalArgumentException if no contours are present or a contour
     *                                  contains less than 3 points
     */
    public Polygon2D(final double x, final double y, final Collection<Point2D>... contours) {
        this(x, y, Arrays.asList(contours));
    }

    /**
     * Constructor
     *
     * @param x        the x offset for this shape
     * @param y        the y offset for this shape
     * @param contours the list of contours for this polygon
     * @param <C>      the contours collection type
     *
     * @throws NullPointerException     if contours is null
     * @throws IllegalArgumentException if no contours are present or a contour
     *                                  contains less than 3 points
     */
    public <C extends Collection<Point2D>> Polygon2D(final double x, final double y, final Collection<C> contours) {
        super(x, y);
        if (contours == null) {
            throw new NullPointerException("contours");
        }
        if (contours.isEmpty()) {
            throw new IllegalArgumentException("contours");
        }
        // find the width and height
        double left = Double.MAX_VALUE;
        double right = -Double.MAX_VALUE;
        double bottom = Double.MAX_VALUE;
        double top = -Double.MAX_VALUE;
        // find the centroid
        double centroidX = 0.0;
        double centroidY = 0.0;
        // find the area
        double area = 0.0;
        final Collection<Point2D> first = contours.iterator().next();
        if (first != null) {
            Point2D previous = null;
            for (final Point2D point : first) {
                // width and height
                left = Math.min(left, point.getX());
                right = Math.max(right, point.getX());
                bottom = Math.min(bottom, point.getY());
                top = Math.max(top, point.getY());
                // centroid and area
                if (previous != null) {
                    centroidX += (previous.getX() + point.getX()) * (previous.getY() * point.getX() - previous.getX() * point.getY());
                    centroidY += (previous.getY() + point.getY()) * (previous.getY() * point.getX() - previous.getX() * point.getY());
                    area += (previous.getX() * point.getY()) - (previous.getY() * point.getX());
                }
                previous = point;
            }
            // join to first point
            final Point2D point = first.iterator().next();
            // previous can't be null at this point
            centroidX += (previous.getX() + point.getX()) * (previous.getY() * point.getX() - previous.getX() * point.getY());
            centroidY += (previous.getY() + point.getY()) * (previous.getY() * point.getX() - previous.getX() * point.getY());
            area += (previous.getX() * point.getY()) - (previous.getY() * point.getX());
        }
        width = right - left;
        height = top - bottom;
        offset = new Point2D(left + (width / 2.0), bottom + (height / 2.0));
        area = Math.abs(area * 0.5);
        centroidX /= (6 * area);
        centroidY /= (6 * area);
        centroid = new Point2D(centroidX, centroidY);
        // build the contours with offset to place the center at x and y
        final List<List<Point2D>> tempContours = new ArrayList<>();
        for (final Collection<Point2D> contour : contours) {
            if (contour.size() < 3) {
                throw new IllegalArgumentException("contour");
            }
            final ArrayList<Point2D> tempContour = new ArrayList<>();
            for (final Point2D point : contour) {
                tempContour.add(new Point2D(point.getX(), point.getY()));
            }
            tempContours.add(Collections.unmodifiableList(tempContour));
        }
        this.contours = Collections.unmodifiableList(tempContours);
    }

    /**
     * Total width of the polygon.
     *
     * @return the width
     */
    @Override
    public double getWidth() {
        return width;
    }

    /**
     * Total height of the polygon.
     *
     * @return the height
     */
    @Override
    public double getHeight() {
        return height;
    }

    /**
     * @return the offset to use if centering the polygon
     */
    public Point2D getOffset() {
        return offset;
    }

    /**
     * @return the centroid center of the polygon
     */
    public Point2D getCentroid() {
        return centroid;
    }

    /**
     * Get a list of the individual contours for this polygon.
     *
     * @return an immutable list of contours
     */
    public List<List<Point2D>> getContours() {
        return contours;
    }

    @Override
    public boolean contains(final double x, final double y) {
        if (!(x >= this.x + offset.x - (width * 0.5) && x <= this.x + offset.x + (width * 0.5)
              && y >= this.y + offset.y - (height * 0.5) && y <= this.y + offset.y + (height * 0.5))) {
            return false;
        }
        int hits = 0;
        // iterate through the contours using the wind even odd algorithm
        for (final List<Point2D> contour : contours) {
            Point2D previous = contour.get(contour.size() - 1);
            Point2D current;

            // Walk the edges of the polygon
            for (int i = 0; i < contour.size(); previous = current, i++) {
                current = contour.get(i);

                if (current.equals(previous)) {
                    continue;
                }

                double leftx;
                if (current.getX() < previous.getX()) {
                    if (x >= previous.getX()) {
                        continue;
                    }
                    leftx = current.getX();
                } else {
                    if (x >= current.getX()) {
                        continue;
                    }
                    leftx = previous.getX();
                }

                double test1, test2;
                if (current.getY() < previous.getY()) {
                    if (y < current.getY() || y >= previous.getY()) {
                        continue;
                    }
                    if (x < leftx) {
                        hits++;
                        continue;
                    }
                    test1 = x - current.getX();
                    test2 = y - current.getY();
                } else {
                    if (y < previous.getY() || y >= current.getY()) {
                        continue;
                    }
                    if (x < leftx) {
                        hits++;
                        continue;
                    }
                    test1 = x - previous.getX();
                    test2 = y - previous.getY();
                }

                if (test1 < (test2 / (previous.getY() - current.getY()) * (previous.getX() - current.getX()))) {
                    hits++;
                }
            }
        }
        return ((hits & 1) != 0);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 41 * hash + (contours != null ? contours.hashCode() : 0);
        hash = 41 * hash + (offset != null ? offset.hashCode() : 0);
        hash = 41 * hash + (centroid != null ? centroid.hashCode() : 0);
        hash = 41 * hash + (int) (Double.doubleToLongBits(width) ^ (Double.doubleToLongBits(width) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(height) ^ (Double.doubleToLongBits(height) >>> 32));
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
        final Polygon2D other = (Polygon2D) obj;
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        if (offset != other.offset && (offset == null || !offset.equals(other.offset))) {
            return false;
        }
        if (centroid != other.centroid && (centroid == null || !centroid.equals(other.centroid))) {
            return false;
        }
        // test collection as a last resort
        if (contours != other.contours && (contours == null || !contours.equals(other.contours))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Polygon2D{" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + '}';
    }

    public static class Builder implements BuilderCommon, BuilderAlternate, BuilderContours {

        private double x = 0.0;
        private double y = 0.0;
        private Collection<Point2D>[] contours;

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
        @SuppressWarnings("unchecked")
        public BuilderCommon points(final Point2D... points) {
            this.contours = new List[]{points != null ? Arrays.asList(points) : Collections.<Point2D>emptyList()};
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public BuilderCommon points(final List<Point2D> points) {
            this.contours = new List[]{points};
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public BuilderAlternate addPoint(final Point2D point) {
            if (point != null) {
                if (contours == null) {
                    contours = new List[]{new ArrayList<>()};
                }
                contours[0].add(point);
            }
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public BuilderAlternate addPoint(final double x, final double y) {
            if (contours == null) {
                contours = new List[]{new ArrayList<>()};
            }
            contours[0].add(new Point2D(x, y));
            return this;
        }

        @Override
        public BuilderContours contours(final Collection<Point2D>... contours) {
            this.contours = contours;
            return this;
        }

        @Override
        public Polygon2D build() {
            return new Polygon2D(x, y, contours);
        }
    }

    public static interface BuilderCommon {

        BuilderCommon x(double x);

        BuilderCommon y(double y);

        BuilderCommon points(Point2D... points);

        BuilderCommon points(List<Point2D> points);

        Polygon2D build();
    }

    public static interface BuilderAlternate {

        BuilderAlternate x(double x);

        BuilderAlternate y(double y);

        BuilderAlternate addPoint(Point2D point);

        BuilderAlternate addPoint(double x, double y);

        Polygon2D build();
    }

    public static interface BuilderContours {

        BuilderContours x(double x);

        BuilderContours y(double y);

        BuilderContours contours(Collection<Point2D>... contours);

        Polygon2D build();
    }

}
