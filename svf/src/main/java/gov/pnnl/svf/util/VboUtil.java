package gov.pnnl.svf.util;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.constant.DimensionConst;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.vbo.VertexBufferObject;
import java.util.List;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.collections.primitives.IntList;

/**
 * Utility class for working with Apache Commons Math and SVF geometry.
 *
 * @author Arthur Bleeker
 */
public class VboUtil extends AbstractGLUtil implements DimensionConst {

    private static final Logger logger = Logger.getLogger(VboUtil.class.getName());
    protected static final DoubleList RECTANGLE_TEX_COORDS = new ArrayDoubleList(12);
    protected static final DoubleList CIRCLE_TEX_COORDS = new ArrayDoubleList(2 + CIRCLE_POINTS.length * 2);

    static {
        // rectangle
        // vertex left top
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[LEFT_TOP][0]);
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[LEFT_TOP][1]);
        // vertex left bottom
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[LEFT_BOTTOM][0]);
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[LEFT_BOTTOM][1]);
        // vertex right bottom
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[RIGHT_BOTTOM][0]);
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[RIGHT_BOTTOM][1]);
        // vertext right bottom
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[RIGHT_BOTTOM][0]);
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[RIGHT_BOTTOM][1]);
        // vertex right top
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[RIGHT_TOP][0]);
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[RIGHT_TOP][1]);
        // vertex left top
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[LEFT_TOP][0]);
        RECTANGLE_TEX_COORDS.add(TEX_COORDS[LEFT_TOP][1]);
        // circle
        // center vertex
        CIRCLE_TEX_COORDS.add(0.5);
        CIRCLE_TEX_COORDS.add(0.5);
        for (int i = CIRCLE_POINTS.length - 1; i >= 0; i--) {
            // vertex
            CIRCLE_TEX_COORDS.add(0.5 + (CIRCLE_POINTS[i][X] / 2.0));
            CIRCLE_TEX_COORDS.add(0.5 + (CIRCLE_POINTS[i][Y] / 2.0));
        }
    }

    /**
     * Constructor kept private for static utility class
     */
    protected VboUtil() {
    }

    /**
     * Add a 2D or 3D point.
     *
     * @param output the list to add to
     * @param input  the input
     *
     * @return the mode
     */
    public static int addPoint(final DoubleList output, final double... input) {
        for (int i = 0; i < input.length; i++) {
            output.add(input[i]);
        }
        // end add
        return GL.GL_POINTS;
    }

    /**
     * Add a rectangle polygon. Vertices are in the order: left bottom, left
     * top, right top, right bottom.
     *
     * @param output the list to add to
     * @param input  the input in clockwise rotation
     *
     * @return the mode
     */
    public static int addPolygon(final DoubleList output, final double[][] input) {
        // create a square polygon
        // first triangle
        addPoint(output, input[RIGHT_TOP]);
        addPoint(output, input[LEFT_TOP]);
        addPoint(output, input[LEFT_BOTTOM]);
        // second triangle
        addPoint(output, input[LEFT_BOTTOM]);
        addPoint(output, input[RIGHT_BOTTOM]);
        addPoint(output, input[RIGHT_TOP]);
        // end add
        return GL2.GL_TRIANGLES;
    }

    /**
     * Add a rectangle polygon.
     *
     * @param output the list to add to
     * @param input  the input
     * @param v1     index of first input
     * @param v2     index of second input
     * @param v3     index of third input
     * @param v4     index of fourth input
     *
     * @return the mode
     */
    public static int addPolygon(final DoubleList output, final double[][] input,
                                 final int v1, final int v2, final int v3, final int v4) {
        // create a square polygon
        // first triangle
        addPoint(output, input[v1]);
        addPoint(output, input[v2]);
        addPoint(output, input[v3]);
        // second triangle
        addPoint(output, input[v3]);
        addPoint(output, input[v4]);
        addPoint(output, input[v1]);
        // end add
        return GL2.GL_TRIANGLES;
    }

    /**
     * Add a rectangle.
     *
     * @param output the list to add to
     * @param x      the x location of center of rectangle
     * @param y      the y location of center of rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     *
     * @return the mode
     */
    public static int addRectangle(final DoubleList output, final double x, final double y, final double width, final double height) {
        final double halfWidth = width / 2.0;
        final double halfHeight = height / 2.0;
        final double left = x - halfWidth;
        final double right = x + halfWidth;
        final double bottom = y - halfHeight;
        final double top = y + halfHeight;
        // create a rectangle polygon
        // vertex left top
        addPoint(output, left, top);
        // vertex left bottom
        addPoint(output, left, bottom);
        // vertex right bottom
        addPoint(output, right, bottom);
        // vertext right bottom
        addPoint(output, right, bottom);
        // vertex right top
        addPoint(output, right, top);
        // vertex left top
        addPoint(output, left, top);
        // end add
        return GL2.GL_TRIANGLES;
    }

    /**
     * Add a rectangle.
     *
     * @param output the list to add to
     * @param x      the x location of center of rectangle
     * @param y      the y location of center of rectangle
     * @param z      the z location of center of rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     *
     * @return the mode
     */
    public static int addRectangle(final DoubleList output, final double x, final double y, final double z, final double width, final double height) {
        final double halfWidth = width / 2.0;
        final double halfHeight = height / 2.0;
        final double left = x - halfWidth;
        final double right = x + halfWidth;
        final double bottom = y - halfHeight;
        final double top = y + halfHeight;
        // create a rectangle polygon
        // vertex left top
        addPoint(output, left, top, z);
        // vertex left bottom
        addPoint(output, left, bottom, z);
        // vertex right bottom
        addPoint(output, right, bottom, z);
        // vertext right bottom
        addPoint(output, right, bottom, z);
        // vertex right top
        addPoint(output, right, top, z);
        // vertex left top
        addPoint(output, left, top, z);
        // end add
        return GL2.GL_TRIANGLES;
    }

    /**
     * Add a circle.
     *
     * @param output the list to add to
     * @param x      the x location of center of circle
     * @param y      the y location of center of circle
     * @param radius the radius of the circle
     *
     * @return the mode
     */
    public static int addCircle(final DoubleList output, final double x, final double y, final double radius) {
        // center vertex
        addPoint(output, x, y);
        for (int i = CIRCLE_POINTS.length - 1; i >= 0; i--) {
            // vertex
            addPoint(output, x + (CIRCLE_POINTS[i][X] * radius), y + (CIRCLE_POINTS[i][Y] * radius));
        }
        // end add
        return GL2.GL_TRIANGLE_FAN;
    }

    /**
     * Get the texture coordinates for a circle with a specified slice number.
     *
     * @param slices the number of slices to render with
     *
     * @return a list of tex coords
     */
    public static DoubleList circleTexCoords(final int slices) {
        final double angle = (2.0 * Math.PI) / slices;
        final DoubleList texCoords = new ArrayDoubleList();
        // center vertex
        addPoint(texCoords, 0.5, 0.5);
        for (double a = 2.0 * Math.PI; a > 0.0; a -= angle) {
            final double ax = Math.sin(a);
            final double ay = Math.cos(a);
            // vertex
            addPoint(texCoords, 0.5 + (ax / 2.0), 0.5 + (ay / 2.0));
        }
        // final vertex
        addPoint(texCoords, 0.5, 1.0);
        return texCoords;
    }

    /**
     * Add a circle.
     *
     * @param output the list to add to
     * @param x      the x location of center of circle
     * @param y      the y location of center of circle
     * @param radius the radius of the circle
     * @param slices the number of slices to render with
     *
     * @return the mode
     */
    public static int addCircle(final DoubleList output, final double x, final double y, final double radius, final int slices) {
        final double angle = (2.0 * Math.PI) / slices;
        // create a polygon
        // center vertex
        addPoint(output, x, y);
        for (double a = 2.0 * Math.PI; a > 0.0; a -= angle) {
            final double ax = Math.sin(a);
            final double ay = Math.cos(a);
            // vertex
            addPoint(output, x + (ax * radius), y + (ay * radius));
        }
        // final vertex
        addPoint(output, x, y + radius, 0.0);
        // end add
        return GL2.GL_TRIANGLE_FAN;
    }

    /**
     * Get the texture coordinates for an arc.
     *
     * @param start the start in degrees
     * @param end   the end in degrees
     *
     * @return a list of tex coords
     */
    public static DoubleList arcTexCoords(final double start, final double end) {
        final int startIndex = normalizeIndex((int) Math.ceil(start));
        final int endIndex = normalizeIndex((int) Math.floor(end));
        final DoubleList texCoords = new ArrayDoubleList();
        // center point
        addPoint(texCoords, 0.5, 0.5);
        // end side
        double ax = Math.sin(Math.toRadians(end));
        double ay = Math.cos(Math.toRadians(end));
        // vertex
        addPoint(texCoords, 0.5 + (ax / 2.0), 0.5 + (ay / 2.0));
        // outer vertices
        for (int i = endIndex; i >= startIndex; i--) {
            // vertex
            addPoint(texCoords, 0.5 + (CIRCLE_POINTS[i][X] / 2.0), 0.5 + (CIRCLE_POINTS[i][Y] / 2.0));
        }
        // start side
        ax = Math.sin(Math.toRadians(start));
        ay = Math.cos(Math.toRadians(start));
        // vertex
        addPoint(texCoords, 0.5 + (ax / 2.0), 0.5 + (ay / 2.0));
        return texCoords;
    }

    /**
     * Add an arc.
     *
     * @param output the list to add to
     * @param x      the x location of center of circle
     * @param y      the y location of center of circle
     * @param radius the radius of the circle
     * @param start  the start in degrees
     * @param end    the end in degrees
     *
     * @return the mode
     */
    public static int addArc(final DoubleList output, final double x, final double y, final double radius, final double start, final double end) {
        final int startIndex = normalizeIndex((int) Math.ceil(start));
        final int endIndex = normalizeIndex((int) Math.floor(end));
        // create a triangle fan
        // center point
        addPoint(output, x, y);
        // end side
        double ax = Math.sin(Math.toRadians(end));
        double ay = Math.cos(Math.toRadians(end));
        // vertex
        addPoint(output, x + (ax * radius), y + (ay * radius));
        // outer vertices
        for (int i = endIndex; i >= startIndex; i--) {
            // vertex
            addPoint(output, x + (CIRCLE_POINTS[i][X] * radius), y + (CIRCLE_POINTS[i][Y] * radius));
        }
        // start side
        ax = Math.sin(Math.toRadians(start));
        ay = Math.cos(Math.toRadians(start));
        // vertex
        addPoint(output, x + (ax * radius), y + (ay * radius));
        return GL.GL_TRIANGLE_FAN;
    }

    /**
     * Get the texture coordinates for an arc with a specified slice number.
     *
     * @param start  the start in degrees
     * @param end    the end in degrees
     * @param slices the number of slices to render with
     *
     * @return a list of tex coords
     */
    public static DoubleList arcTexCoords(final double start, final double end, final int slices) {
        final DoubleList texCoords = new ArrayDoubleList();
        final double angle = Math.toRadians(end - start) / slices;
        // create a triangle fan
        // center point
        addPoint(texCoords, 0.5, 0.5);
        // circle points
        for (double a = end; a >= start; a -= angle) {
            final double ax = Math.sin(a);
            final double ay = Math.cos(a);
            // vertex
            addPoint(texCoords, 0.5 + (ax / 2.0), 0.5 + (ay / 2.0));
        }
        return texCoords;
    }

    /**
     * Add an arc.
     *
     * @param output the list to add to
     * @param x      the x location of center of circle
     * @param y      the y location of center of circle
     * @param radius the radius of the circle
     * @param start  the start in degrees
     * @param end    the end in degrees
     * @param slices the number of slices to render with
     *
     * @return the mode
     */
    public static int addArc(final DoubleList output, final double x, final double y, final double radius, final double start, final double end, final int slices) {
        final double angle = Math.toRadians(end - start) / slices;
        // create a triangle fan
        // center point
        addPoint(output, x, y);
        // circle points
        for (double a = end; a >= start; a -= angle) {
            final double ax = Math.sin(a);
            final double ay = Math.cos(a);
            // vertex
            addPoint(output, x + (ax * radius), y + (ay * radius));
        }
        return GL.GL_TRIANGLE_FAN;
    }

    /**
     * Add an arc with an inner and outer radius.
     * <p/>
     * TODO implement texture coords method
     *
     * @param output      the list to add to
     * @param x           the x location of center of circle
     * @param y           the y location of center of circle
     * @param innerRadius the inner radius of the circle
     * @param outerRadius the outer radius of the circle
     * @param start       the start in degrees
     * @param end         the end in degrees
     *
     * @return the mode
     */
    public static int addArc(final DoubleList output, final double x, final double y, final double innerRadius, final double outerRadius, final double start, final double end) {
        final int startIndex = normalizeIndex((int) Math.ceil(start));
        final int endIndex = normalizeIndex((int) Math.floor(end));
        // end side
        double ax = Math.sin(Math.toRadians(end));
        double ay = Math.cos(Math.toRadians(end));
        // inner vertex
        addPoint(output, x + (ax * innerRadius), y + (ay * innerRadius));
        // outer vertex
        addPoint(output, x + (ax * outerRadius), y + (ay * outerRadius));
        // inner vertices
        for (int i = endIndex; i >= startIndex; i--) {
            final int a = ShapeUtil.normalizeIndex(i);
            // inner vertex
            addPoint(output, x + (CIRCLE_POINTS[a][X] * innerRadius), y + (CIRCLE_POINTS[a][Y] * innerRadius));
            // outer vertex
            addPoint(output, x + (CIRCLE_POINTS[a][X] * outerRadius), y + (CIRCLE_POINTS[a][Y] * outerRadius));
        }
        // start side
        ax = Math.sin(Math.toRadians(start));
        ay = Math.cos(Math.toRadians(start));
        // inner vertex
        addPoint(output, x + (ax * innerRadius), y + (ay * innerRadius));
        // outer vertex
        addPoint(output, x + (ax * outerRadius), y + (ay * outerRadius));
        // end add
        return GL2.GL_QUAD_STRIP;
    }

    /**
     * Add an arc with an inner and outer radius.
     * <p/>
     * TODO implement texture coords method
     *
     * @param output      the list to add to
     * @param x           the x location of center of circle
     * @param y           the y location of center of circle
     * @param innerRadius the inner radius of the circle
     * @param outerRadius the outer radius of the circle
     * @param start       the start in degrees
     * @param end         the end in degrees
     * @param slices      the number of slices to render with
     *
     * @return the mode
     */
    public static int addArc(final DoubleList output, final double x, final double y, final double innerRadius, final double outerRadius, final double start, final double end, final int slices) {
        final double angle = Math.toRadians(end - start) / slices;
        // vertices
        for (double a = end; a >= start; a -= angle) {
            final double ax = Math.sin(a);
            final double ay = Math.cos(a);
            // inner vertex
            addPoint(output, x + (ax * innerRadius), y + (ay * innerRadius));
            // outer vertex
            addPoint(output, x + (ax * outerRadius), y + (ay * outerRadius));
        }
        // end add
        return GL2.GL_QUAD_STRIP;
    }

    /**
     * Add a border around a circle.
     *
     * @param output    the list to add to
     * @param x         the x location of center of circle
     * @param y         the y location of center of circle
     * @param radius    the radius of the circle
     * @param border    the border to draw
     * @param thickness the thickness of the border
     *
     * @return the mode
     */
    public static int addCircleBorder(final DoubleList output, final double x, final double y, final double radius, final Border border, final double thickness) {
        // calculate metrics
        final double innerRadius = radius;
        final double outerRadius = radius + thickness;
        // draw an arc all of the way around
        for (int i = CIRCLE_POINTS.length - 1; i >= 0; i--) {
            final int a = normalizeIndex(i);
            // inner vertex
            addPoint(output, x + (CIRCLE_POINTS[a][X] * innerRadius), y + (CIRCLE_POINTS[a][Y] * innerRadius));
            // outer vertex
            addPoint(output, x + (CIRCLE_POINTS[a][X] * outerRadius), y + (CIRCLE_POINTS[a][Y] * outerRadius));
        }
        // end add
        return GL2.GL_QUAD_STRIP;
    }

    /**
     * Add a border around a rectangle.
     *
     * @param output    the empty list to add to
     * @param x         the x location of center of rectangle
     * @param y         the y location of center of rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     * @param border    the border to draw
     * @param thickness the thickness of the border
     *
     * @return the modes
     */
    public static int[] addRectangleBorder(final List<DoubleList> output, final double x, final double y, final double width, final double height, final Border border,
                                           final double thickness) {
        if (border == Border.ALL) {
            return VboUtil.addRectangleBorder(output, x, y, width, height, thickness);
        }
        final IntList modes = new ArrayIntList(8);
        // calculate the metrics
        final double xLeft = x - (width * 0.5);
        final double xRight = x + (width * 0.5);
        final double yBottom = y - (height * 0.5);
        final double yTop = y + (height * 0.5);
        // verts
        final double verts[][] = new double[4][];
        // top
        if (border.isTop()) {
            verts[0] = new double[]{xLeft, yTop};
            verts[1] = new double[]{xLeft, yTop + thickness};
            verts[2] = new double[]{xRight, yTop + thickness};
            verts[3] = new double[]{xRight, yTop};
            final ArrayDoubleList vertices = new ArrayDoubleList(12);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // bottom
        if (border.isBottom()) {
            verts[0] = new double[]{xLeft, yBottom - thickness};
            verts[1] = new double[]{xLeft, yBottom};
            verts[2] = new double[]{xRight, yBottom};
            verts[3] = new double[]{xRight, yBottom - thickness};
            final ArrayDoubleList vertices = new ArrayDoubleList(12);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // left
        if (border.isLeft()) {
            verts[0] = new double[]{xLeft - thickness, yBottom};
            verts[1] = new double[]{xLeft - thickness, yTop};
            verts[2] = new double[]{xLeft, yTop};
            verts[3] = new double[]{xLeft, yBottom};
            final ArrayDoubleList vertices = new ArrayDoubleList(12);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // right
        if (border.isRight()) {
            verts[0] = new double[]{xRight, yBottom};
            verts[1] = new double[]{xRight, yTop};
            verts[2] = new double[]{xRight + thickness, yTop};
            verts[3] = new double[]{xRight + thickness, yBottom};
            final ArrayDoubleList vertices = new ArrayDoubleList(12);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // upper left corner
        if (border.isLeft() && border.isTop()) {
            verts[0] = new double[]{xLeft - thickness, yTop};
            verts[1] = new double[]{xLeft - thickness, yTop + thickness};
            verts[2] = new double[]{xLeft, yTop + thickness};
            verts[3] = new double[]{xLeft, yTop};
            final ArrayDoubleList vertices = new ArrayDoubleList(12);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // upper right corner
        if (border.isRight() && border.isTop()) {
            verts[0] = new double[]{xRight, yTop};
            verts[1] = new double[]{xRight, yTop + thickness};
            verts[2] = new double[]{xRight + thickness, yTop + thickness};
            verts[3] = new double[]{xRight + thickness, yTop};
            final ArrayDoubleList vertices = new ArrayDoubleList(12);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // lower left corner
        if (border.isLeft() && border.isBottom()) {
            verts[0] = new double[]{xLeft - thickness, yBottom - thickness};
            verts[1] = new double[]{xLeft - thickness, yBottom};
            verts[2] = new double[]{xLeft, yBottom};
            verts[3] = new double[]{xLeft, yBottom - thickness};
            final ArrayDoubleList vertices = new ArrayDoubleList(12);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // lower right corner
        if (border.isRight() && border.isBottom()) {
            verts[0] = new double[]{xRight, yBottom - thickness};
            verts[1] = new double[]{xRight, yBottom};
            verts[2] = new double[]{xRight + thickness, yBottom};
            verts[3] = new double[]{xRight + thickness, yBottom - thickness};
            final ArrayDoubleList vertices = new ArrayDoubleList(12);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        return modes.toArray();
    }

    /**
     * Add a border around a rectangle.
     *
     * @param output    the empty list to add to
     * @param x         the x location of center of rectangle
     * @param y         the y location of center of rectangle
     * @param z         the z location of center of rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     * @param border    the border to draw
     * @param thickness the thickness of the border
     *
     * @return the modes
     */
    public static int[] addRectangleBorder(final List<DoubleList> output, final double x, final double y, final double z, final double width, final double height, final Border border,
                                           final double thickness) {
        if (border == Border.ALL) {
            return VboUtil.addRectangleBorder(output, x, y, z, width, height, thickness);
        }
        final IntList modes = new ArrayIntList(8);
        // calculate the metrics
        final double xLeft = x - (width * 0.5);
        final double xRight = x + (width * 0.5);
        final double yBottom = y - (height * 0.5);
        final double yTop = y + (height * 0.5);
        // verts
        final double verts[][] = new double[4][];
        // top
        if (border.isTop()) {
            verts[0] = new double[]{xLeft, yTop, z};
            verts[1] = new double[]{xLeft, yTop + thickness, z};
            verts[2] = new double[]{xRight, yTop + thickness, z};
            verts[3] = new double[]{xRight, yTop, z};
            final ArrayDoubleList vertices = new ArrayDoubleList(18);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // bottom
        if (border.isBottom()) {
            verts[0] = new double[]{xLeft, yBottom - thickness, z};
            verts[1] = new double[]{xLeft, yBottom, z};
            verts[2] = new double[]{xRight, yBottom, z};
            verts[3] = new double[]{xRight, yBottom - thickness, z};
            final ArrayDoubleList vertices = new ArrayDoubleList(18);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // left
        if (border.isLeft()) {
            verts[0] = new double[]{xLeft - thickness, yBottom, z};
            verts[1] = new double[]{xLeft - thickness, yTop, z};
            verts[2] = new double[]{xLeft, yTop, z};
            verts[3] = new double[]{xLeft, yBottom, z};
            final ArrayDoubleList vertices = new ArrayDoubleList(18);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // right
        if (border.isRight()) {
            verts[0] = new double[]{xRight, yBottom, z};
            verts[1] = new double[]{xRight, yTop, z};
            verts[2] = new double[]{xRight + thickness, yTop, z};
            verts[3] = new double[]{xRight + thickness, yBottom, z};
            final ArrayDoubleList vertices = new ArrayDoubleList(18);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // upper left corner
        if (border.isLeft() && border.isTop()) {
            verts[0] = new double[]{xLeft - thickness, yTop, z};
            verts[1] = new double[]{xLeft - thickness, yTop + thickness, z};
            verts[2] = new double[]{xLeft, yTop + thickness, z};
            verts[3] = new double[]{xLeft, yTop, z};
            final ArrayDoubleList vertices = new ArrayDoubleList(18);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // upper right corner
        if (border.isRight() && border.isTop()) {
            verts[0] = new double[]{xRight, yTop, z};
            verts[1] = new double[]{xRight, yTop + thickness, z};
            verts[2] = new double[]{xRight + thickness, yTop + thickness, z};
            verts[3] = new double[]{xRight + thickness, yTop, z};
            final ArrayDoubleList vertices = new ArrayDoubleList(18);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // lower left corner
        if (border.isLeft() && border.isBottom()) {
            verts[0] = new double[]{xLeft - thickness, yBottom - thickness, z};
            verts[1] = new double[]{xLeft - thickness, yBottom, z};
            verts[2] = new double[]{xLeft, yBottom, z};
            verts[3] = new double[]{xLeft, yBottom - thickness, z};
            final ArrayDoubleList vertices = new ArrayDoubleList(18);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        // lower right corner
        if (border.isRight() && border.isBottom()) {
            verts[0] = new double[]{xRight, yBottom - thickness, z};
            verts[1] = new double[]{xRight, yBottom, z};
            verts[2] = new double[]{xRight + thickness, yBottom, z};
            verts[3] = new double[]{xRight + thickness, yBottom - thickness, z};
            final ArrayDoubleList vertices = new ArrayDoubleList(18);
            output.add(vertices);
            modes.add(addPolygon(vertices, verts));
        }
        return modes.toArray();
    }

    /**
     * Build the VBO.
     *
     * @param vbo       the vbo to build
     * @param normals   optional normals to populate
     * @param texCoords optional texture coordinates to populate (caller must
     *                  specify correct tex coord dimension)
     * @param color     option color to populate
     *
     * @return the built VBO
     */
    protected static VertexBufferObject buildVbo(final VertexBufferObject.Builder vbo, final double[] normals, final double[] texCoords, final Color color) {
        if (normals != null) {
            vbo.normals(normals);
        }
        if (texCoords != null) {
            vbo.texCoords(texCoords);
        }
        if (color != null) {
            vbo.colors(color.toRgbaArray());
        }
        return vbo.build();
    }

    private static int[] addRectangleBorder(final List<DoubleList> output, final double x, final double y, final double width, final double height, final double thickness) {
        final ArrayDoubleList vertices = new ArrayDoubleList(12 * 4);
        output.add(vertices);
        // calculate the metrics
        final double xLeft = x - (width * 0.5);
        final double xRight = x + (width * 0.5);
        final double yBottom = y - (height * 0.5);
        final double yTop = y + (height * 0.5);
        // verts
        final double verts[][] = new double[4][];
        // top
        verts[0] = new double[]{xLeft, yTop};
        verts[1] = new double[]{xLeft, yTop + thickness};
        verts[2] = new double[]{xRight, yTop + thickness};
        verts[3] = new double[]{xRight, yTop};
        addPolygon(vertices, verts);
        // bottom
        verts[0] = new double[]{xLeft, yBottom - thickness};
        verts[1] = new double[]{xLeft, yBottom};
        verts[2] = new double[]{xRight, yBottom};
        verts[3] = new double[]{xRight, yBottom - thickness};
        addPolygon(vertices, verts);
        // left
        verts[0] = new double[]{xLeft - thickness, yBottom - thickness};
        verts[1] = new double[]{xLeft - thickness, yTop + thickness};
        verts[2] = new double[]{xLeft, yTop + thickness};
        verts[3] = new double[]{xLeft, yBottom - thickness};
        addPolygon(vertices, verts);
        // right
        verts[0] = new double[]{xRight, yBottom - thickness};
        verts[1] = new double[]{xRight, yTop + thickness};
        verts[2] = new double[]{xRight + thickness, yTop + thickness};
        verts[3] = new double[]{xRight + thickness, yBottom - thickness};
        final int mode = addPolygon(vertices, verts);
        return new int[]{mode};
    }

    private static int[] addRectangleBorder(final List<DoubleList> output, final double x, final double y, final double z, final double width, final double height, final double thickness) {
        final ArrayDoubleList vertices = new ArrayDoubleList(18 * 4);
        output.add(vertices);
        // calculate the metrics
        final double xLeft = x - (width * 0.5);
        final double xRight = x + (width * 0.5);
        final double yBottom = y - (height * 0.5);
        final double yTop = y + (height * 0.5);
        // verts
        final double verts[][] = new double[4][];
        // top
        verts[0] = new double[]{xLeft, yTop, z};
        verts[1] = new double[]{xLeft, yTop + thickness, z};
        verts[2] = new double[]{xRight, yTop + thickness, z};
        verts[3] = new double[]{xRight, yTop, z};
        addPolygon(vertices, verts);
        // bottom
        verts[0] = new double[]{xLeft, yBottom - thickness, z};
        verts[1] = new double[]{xLeft, yBottom, z};
        verts[2] = new double[]{xRight, yBottom, z};
        verts[3] = new double[]{xRight, yBottom - thickness, z};
        addPolygon(vertices, verts);
        // left
        verts[0] = new double[]{xLeft - thickness, yBottom - thickness, z};
        verts[1] = new double[]{xLeft - thickness, yTop + thickness, z};
        verts[2] = new double[]{xLeft, yTop + thickness, z};
        verts[3] = new double[]{xLeft, yBottom - thickness, z};
        addPolygon(vertices, verts);
        // right
        verts[0] = new double[]{xRight, yBottom - thickness, z};
        verts[1] = new double[]{xRight, yTop + thickness, z};
        verts[2] = new double[]{xRight + thickness, yTop + thickness, z};
        verts[3] = new double[]{xRight + thickness, yBottom - thickness, z};
        final int mode = addPolygon(vertices, verts);
        return new int[]{mode};
    }
}
