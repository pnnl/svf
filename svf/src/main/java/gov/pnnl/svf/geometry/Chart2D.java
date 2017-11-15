package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Chart;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Chart2D extends Shape2D implements Serializable {

    private static final long serialVersionUID = 1L;
    private final static Comparator<Point2D> COMPARATOR = (Point2D o1, Point2D o2) -> Double.compare(o1.getX(), o2.getX());
    /**
     * Value that represents no color in a chart.
     */
    public final static int NO_COLOR = -1;
    /**
     * Constant zero-dimensioned chart
     */
    public final static Chart2D ZERO = new Chart2D();
    protected final Chart style;
    protected final List<Point2D> points;
    protected final Point2D minimum;
    protected final Point2D maximum;

    /**
     * Constructor
     */
    public Chart2D() {
        super();
        style = Chart.NONE;
        points = Collections.emptyList();
        minimum = Point2D.ZERO;
        maximum = Point2D.ZERO;
    }

    /**
     * Constructor used to specify a new offset.
     *
     * @param x    the new x offset
     * @param y    the new y offset
     * @param copy the object to copy
     */
    public Chart2D(final double x, final double y, final Chart2D copy) {
        super(x, y);
        style = copy.style;
        points = copy.points;
        minimum = copy.minimum;
        maximum = copy.maximum;
    }

    /**
     * Constructor
     *
     * @param x the x offset for this shape
     * @param y the y offset for this shape
     */
    public Chart2D(final double x, final double y) {
        super(x, y);
        style = Chart.NONE;
        points = Collections.emptyList();
        minimum = Point2D.ZERO;
        maximum = Point2D.ZERO;
    }

    /**
     * Constructor
     *
     * @param points the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final Point2D... points) {
        this(0.0, 0.0, Chart.NONE, points);
    }

    /**
     * Constructor
     *
     * @param points the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final Collection<Point2D> points) {
        this(0.0, 0.0, Chart.NONE, points);
    }

    /**
     * Constructor
     *
     * @param style  style for the path
     * @param points the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final Chart style, final Point2D... points) {
        this(0.0, 0.0, style, points);
    }

    /**
     * Constructor
     *
     * @param style  style for the path
     * @param points the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final Chart style, final Collection<Point2D> points) {
        this(0.0, 0.0, style, points);
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param points the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final double x, final double y, final Point2D... points) {
        this(x, y, Chart.NONE, points);
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param points the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final double x, final double y, final Collection<Point2D> points) {
        this(x, y, Chart.NONE, points);
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param style  style for the path
     * @param points the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final double x, final double y, final Chart style, final Point2D... points) {
        this(x, y, style, null, null, points);
    }

    /**
     * Constructor
     *
     * @param x      the x offset for this shape
     * @param y      the y offset for this shape
     * @param style  style for the path
     * @param points the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final double x, final double y, final Chart style, final Collection<Point2D> points) {
        this(x, y, style, null, null, points);
    }

    /**
     * Constructor
     *
     * @param x       the x offset for this shape
     * @param y       the y offset for this shape
     * @param style   style for the path
     * @param minimum minimum values or null to use minimum from points
     * @param maximum maximum values or null to use maximum from points
     * @param points  the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final double x, final double y, final Chart style, final Point2D minimum, final Point2D maximum, final Point2D... points) {
        this(x, y, style, minimum, maximum, Arrays.asList(points));
    }

    /**
     * Constructor
     *
     * @param x       the x offset for this shape
     * @param y       the y offset for this shape
     * @param style   style for the path
     * @param minimum minimum values or null to use minimum from points
     * @param maximum maximum values or null to use maximum from points
     * @param points  the list of points for this chart
     *
     * @throws NullPointerException if points is null
     */
    public Chart2D(final double x, final double y, final Chart style, final Point2D minimum, final Point2D maximum, final Collection<Point2D> points) {
        super(x, y);
        if (style == null) {
            throw new NullPointerException("style");
        }
        this.style = style;
        if (points == null || points.isEmpty()) {
            this.points = Collections.emptyList();
            this.minimum = Point2D.ZERO;
            this.maximum = Point2D.ZERO;
        } else {
            final List<Point2D> temp = new ArrayList<>(points);
            switch (style) {
                case SCATTER:
                case LINE:
                case AREA:
                    Collections.sort(temp, COMPARATOR);
                    break;
                default:
                    // only previous styles need to be sorted
                    break;
            }
            this.points = Collections.unmodifiableList(temp);
            double xMin = Double.MAX_VALUE;
            double xMax = Double.MIN_VALUE;
            double yMin = Double.MAX_VALUE;
            double yMax = Double.MIN_VALUE;
            if (minimum == null || maximum == null) {
                for (final Point2D point : this.points) {
                    xMin = Math.min(xMin, point.getX());
                    xMax = Math.max(xMax, point.getX());
                    yMin = Math.min(yMin, point.getY());
                    yMax = Math.max(yMax, point.getY());
                }
            }
            if (minimum == null) {
                this.minimum = new Point2D(xMin, yMin);
            } else {
                this.minimum = minimum;
            }
            if (maximum == null) {
                this.maximum = new Point2D(xMax, yMax);
            } else {
                this.maximum = maximum;
            }
        }
    }

    /**
     * The style for this chart to use when drawing.
     *
     * @return the chart style
     */
    public Chart getStyle() {
        return style;
    }

    /**
     * The minimum range value.
     *
     * @return the minimum
     */
    public Point2D getMinimum() {
        return minimum;
    }

    /**
     * The maximum range value.
     *
     * @return the maximum
     */
    public Point2D getMaximum() {
        return maximum;
    }

    /**
     * List of immutable points.
     *
     * @return list of points
     */
    public List<Point2D> getPoints() {
        return points;
    }

    @Override
    public double getWidth() {
        return 1.0;
    }

    @Override
    public double getHeight() {
        return 1.0;
    }

    @Override
    public boolean contains(final double x, final double y) {
        return x >= this.x - 0.5 && x <= this.x + 0.5
               && y >= this.y - 0.5 && y <= this.y + 0.5;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 41 * hash + (style != null ? style.hashCode() : 0);
        hash = 41 * hash + (minimum != null ? minimum.hashCode() : 0);
        hash = 41 * hash + (maximum != null ? maximum.hashCode() : 0);
        hash = 41 * hash + (points != null ? points.hashCode() : 0);
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
        final Chart2D other = (Chart2D) obj;
        if (style != other.style && (style == null || !style.equals(other.style))) {
            return false;
        }
        if (minimum != other.minimum && (minimum == null || !minimum.equals(other.minimum))) {
            return false;
        }
        if (maximum != other.maximum && (maximum == null || !maximum.equals(other.maximum))) {
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
        return "Chart2D{" + "x=" + x + ", y=" + y + ", xAxis=" + minimum + ", yAxis=" + maximum + ", style=" + style + '}';
    }

    public static class Builder implements BuilderNone, BuilderPoint, BuilderColor {

        private double x = 0.0;
        private double y = 0.0;
        private Chart style = Chart.NONE;
        private List<Point2D> points = Collections.emptyList();
        private Point2D minimum = null;
        private Point2D maximum = null;

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
        public BuilderNone style(Chart style) {
            this.style = style;
            return this;
        }

        public BuilderNone styleNone() {
            this.style = Chart.NONE;
            return this;
        }

        public BuilderPoint styleArea() {
            this.style = Chart.AREA;
            return this;
        }

        public BuilderColor styleBar() {
            this.style = Chart.BAR;
            return this;
        }

        public BuilderPoint styleLine() {
            this.style = Chart.LINE;
            return this;
        }

        public BuilderColor stylePie() {
            this.style = Chart.PIE;
            return this;
        }

        public BuilderPoint styleScatter() {
            this.style = Chart.SCATTER;
            return this;
        }

        @Override
        public BuilderNone points(final Point2D... points) {
            this.points = points != null ? Arrays.asList(points) : Collections.<Point2D>emptyList();
            return this;
        }

        @Override
        public BuilderNone points(final List<Point2D> points) {
            this.points = points;
            return this;
        }

        @Override
        public BuilderPoint addPoint(final Point2D point) {
            if (point != null) {
                if (points == null || points.isEmpty()) {
                    points = new ArrayList<>();
                }
                points.add(point);
            }
            return this;
        }

        @Override
        public BuilderPoint addPoint(final double x, final double y) {
            if (points == null || points.isEmpty()) {
                points = new ArrayList<>();
            }
            points.add(new Point2D(x, y));
            return this;
        }

        @Override
        public Builder addPoint(final double value) {
            if (points == null || points.isEmpty()) {
                points = new ArrayList<>();
            }
            points.add(new Point2D(NO_COLOR, value));
            return this;
        }

        @Override
        public Builder addPoint(final Color color, final double value) {
            if (points == null || points.isEmpty()) {
                points = new ArrayList<>();
            }
            points.add(new Point2D(color != null ? color.toInt() : NO_COLOR, value));
            return this;
        }

        @Override
        public Builder minimum(final Point2D minimum) {
            this.minimum = minimum;
            return this;
        }

        @Override
        public Builder maximum(final Point2D maximum) {
            this.maximum = maximum;
            return this;
        }

        @Override
        public Chart2D build() {
            return new Chart2D(x, y, style, minimum, maximum, points);
        }
    }

    public static interface BuilderNone {

        BuilderNone x(double x);

        BuilderNone y(double y);

        BuilderNone style(Chart style);

        BuilderNone points(Point2D... points);

        BuilderNone points(List<Point2D> points);

        BuilderNone minimum(Point2D minimum);

        BuilderNone maximum(Point2D maximum);

        Chart2D build();
    }

    public static interface BuilderPoint {

        BuilderPoint x(double x);

        BuilderPoint y(double y);

        BuilderPoint addPoint(Point2D point);

        BuilderPoint addPoint(double x, double y);

        BuilderPoint minimum(Point2D minimum);

        BuilderPoint maximum(Point2D maximum);

        Chart2D build();
    }

    public static interface BuilderColor {

        BuilderColor x(double x);

        BuilderColor y(double y);

        BuilderColor addPoint(double value);

        BuilderColor addPoint(Color color, double value);

        BuilderColor minimum(Point2D minimum);

        BuilderColor maximum(Point2D maximum);

        Chart2D build();
    }
}
