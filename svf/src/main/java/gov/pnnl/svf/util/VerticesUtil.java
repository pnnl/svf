package gov.pnnl.svf.util;

import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Path2D;
import gov.pnnl.svf.geometry.Path3D;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Point3D;
import gov.pnnl.svf.geometry.Rectangle2D;

/**
 * Utility class for getting vertices for geometry.
 *
 * @author Arthur Bleeker
 */
public class VerticesUtil {

    /**
     * Constructor kept private for static utility class.
     */
    private VerticesUtil() {
    }

    /**
     * Get the vertices represented by this point.
     *
     * @param point the point
     *
     * @return the vertices
     *
     * @throws NullPointerException if point is null
     */
    public static double[] getVertices(final Point2D point) {
        if (point == null) {
            throw new NullPointerException("point");
        }
        return new double[]{point.getX(), point.getY(), 0.0};
    }

    /**
     * Get the vertices represented by this point.
     *
     * @param point the point
     *
     * @return the vertices
     *
     * @throws NullPointerException if point is null
     */
    public static double[] getVertices(final Point3D point) {
        if (point == null) {
            throw new NullPointerException("point");
        }
        return new double[]{point.getX(), point.getY(), point.getZ()};
    }

    /**
     * Get the vertices represented by this path.
     *
     * @param path the point
     *
     * @return the vertices as a GL_LINE_STRIP
     *
     * @throws NullPointerException if point is null
     */
    public static double[][] getVertices(final Path2D path) {
        if (path == null) {
            throw new NullPointerException("point");
        }
        final double[][] list = new double[path.getPoints().size()][];
        for (int i = 0; i < path.getPoints().size(); i++) {
            final Point2D point = path.getPoints().get(i);
            list[i] = new double[]{point.getX(), point.getY(), 0.0};
        }
        return list;
    }

    /**
     * Get the vertices represented by this path.
     *
     * @param path the point
     *
     * @return the vertices as a GL_LINE_STRIP
     *
     * @throws NullPointerException if point is null
     */
    public static double[][] getVertices(final Path3D path) {
        if (path == null) {
            throw new NullPointerException("point");
        }
        final double[][] list = new double[path.getPoints().size()][];
        for (int i = 0; i < path.getPoints().size(); i++) {
            final Point3D point = path.getPoints().get(i);
            list[i] = new double[]{point.getX(), point.getY(), point.getZ()};
        }
        return list;
    }

    /**
     * Get the vertices represented by this rectangle.
     *
     * @param rectangle the rectangle
     *
     * @return the vertices as a GL_POLYGON
     *
     * @throws NullPointerException if rectangle is null
     */
    public static double[][] getVertices(final Rectangle2D rectangle) {
        if (rectangle == null) {
            throw new NullPointerException("rectangle");
        }
        final double x = rectangle.getX();
        final double y = rectangle.getY();
        final double width = rectangle.getWidth();
        final double height = rectangle.getHeight();
        return new double[][]{{x - (width * 0.5), y - (height * 0.5), 0.0},
                              {x - (width * 0.5), y + (height * 0.5), 0.0},
                              {x + (width * 0.5), y + (height * 0.5), 0.0},
                              {x + (width * 0.5), y - (height * 0.5), 0.0}};
    }

    /**
     * Get the vertices represented by this circle.
     *
     * @param circle the circle
     *
     * @return the vertices as a GL_FAN
     *
     * @throws NullPointerException if circle is null
     */
    public static double[][] getVertices(final Circle2D circle) {
        return VerticesUtil.getVertices(circle, 360);
    }

    /**
     * Get the vertices represented by this circle.
     *
     * @param circle the circle
     * @param slices the number of slices
     *
     * @return the vertices as a GL_FAN
     *
     * @throws NullPointerException if circle is null
     */
    public static double[][] getVertices(final Circle2D circle, final int slices) {
        if (circle == null) {
            throw new NullPointerException("circle");
        }
        final double x = circle.getX();
        final double y = circle.getY();
        final double r = circle.getRadius();
        // make fan from center
        final double s = 2.0 * Math.PI / slices;
        final double[][] vertices = new double[slices + 1][3];
        vertices[0][0] = x;
        vertices[0][1] = y;
        for (int i = 0; i <= slices; i++) {
            double a;
            if (i == slices) {
                a = 0.0;
            } else {
                a = i * s;
            }
            vertices[i + 1][0] = x + r * Math.sin(a);
            vertices[i + 1][1] = y + r * Math.cos(a);
        }
        return vertices;
    }
}
