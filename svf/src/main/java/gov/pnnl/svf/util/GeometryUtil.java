package gov.pnnl.svf.util;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import gov.pnnl.svf.core.geometry.Border;

/**
 * Utility class for working with Apache Commons Math and SVF geometry.
 *
 * @author Arthur Bleeker
 */
public class GeometryUtil extends AbstractGLUtil {

    /**
     * Constructor kept private for static utility class
     */
    private GeometryUtil() {
    }

    /**
     * Draw a 2D or 3D point.
     *
     * @param gl     reference to current GL
     * @param vertex vertex of point
     */
    public static void drawPoint(final GL2 gl, final double[] vertex) {
        // draw a point
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex3d(vertex[0], vertex[1], vertex.length == 3 ? vertex[2] : 0.0);
        gl.glEnd();
    }

    /**
     * Draw a rectangle polygon. Vertices are in the order: left bottom, left
     * top, right top, right bottom.
     *
     * @param gl       reference to current GL
     * @param vertices vertices in clockwise rotation
     */
    public static void drawPolygon(final GL2 gl, final double[][] vertices) {
        // create a square polygon
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glNormal3dv(NORMAL, 0);

        // first triangle
        gl.glVertex3dv(vertices[RIGHT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_BOTTOM], 0);

        // second triangle
        gl.glVertex3dv(vertices[LEFT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_TOP], 0);

        // end draw
        gl.glEnd();
    }

    /**
     * Draw a rectangle polygon. Vertices are in the order: left bottom, left
     * top, right top, right bottom.
     *
     * @param gl       reference to current GL
     * @param vertices vertices in clockwise rotation
     * @param normal   normal
     */
    public static void drawPolygon(final GL2 gl, final double[][] vertices, final double[] normal) {
        // create a square polygon
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glNormal3dv(normal, 0);

        // first triangle
        gl.glVertex3dv(vertices[LEFT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);

        // second triangle
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_TOP], 0);

        // end draw
        gl.glEnd();
    }

    /**
     * Draw a rectangle polygon. Vertices are in the order: left bottom, left
     * top, right top, right bottom.
     *
     * @param gl        reference to current GL
     * @param vertices  vertices in clockwise rotation
     * @param texCoords texture coordinates
     */
    public static void drawPolygon(final GL2 gl, final double[][] vertices, final double[][] texCoords) {
        // create a square polygon
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glNormal3dv(NORMAL, 0);

        // first triangle
        gl.glTexCoord2dv(texCoords[LEFT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_TOP], 0);

        gl.glTexCoord2dv(texCoords[LEFT_BOTTOM], 0);
        gl.glVertex3dv(vertices[LEFT_BOTTOM], 0);

        gl.glTexCoord2dv(texCoords[RIGHT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);

        // second triangle
        gl.glTexCoord2dv(texCoords[RIGHT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);

        gl.glTexCoord2dv(texCoords[RIGHT_TOP], 0);
        gl.glVertex3dv(vertices[RIGHT_TOP], 0);

        gl.glTexCoord2dv(texCoords[LEFT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_TOP], 0);

        // end draw
        gl.glEnd();
    }

    /**
     * Draw a rectangle polygon. Vertices are in the order: left bottom, left
     * top, right top, right bottom.
     *
     * @param gl        reference to current GL
     * @param vertices  vertices in clockwise rotation
     * @param texCoords texture coordinates
     * @param normals   normals
     */
    public static void drawPolygon(final GL2 gl, final double[][] vertices, final double[][] texCoords, final double[][] normals) {
        // create a square polygon
        gl.glBegin(GL2.GL_TRIANGLES);

        // first triangle
        gl.glNormal3dv(normals[LEFT_TOP], 0);
        gl.glTexCoord2dv(texCoords[LEFT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_TOP], 0);

        gl.glNormal3dv(normals[LEFT_BOTTOM], 0);
        gl.glTexCoord2dv(texCoords[LEFT_BOTTOM], 0);
        gl.glVertex3dv(vertices[LEFT_BOTTOM], 0);

        gl.glNormal3dv(normals[RIGHT_BOTTOM], 0);
        gl.glTexCoord2dv(texCoords[RIGHT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);

        // second triangle
        gl.glNormal3dv(normals[RIGHT_BOTTOM], 0);
        gl.glTexCoord2dv(texCoords[RIGHT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);

        gl.glNormal3dv(normals[RIGHT_TOP], 0);
        gl.glTexCoord2dv(texCoords[RIGHT_TOP], 0);
        gl.glVertex3dv(vertices[RIGHT_TOP], 0);

        gl.glNormal3dv(normals[LEFT_TOP], 0);
        gl.glTexCoord2dv(texCoords[LEFT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_TOP], 0);

        // end draw
        gl.glEnd();
    }

    /**
     * Draw a rectangle polygon. Vertices are in the order: left bottom, left
     * top, right top, right bottom.
     *
     * @param gl        reference to current GL
     * @param vertices  vertices in clockwise rotation
     * @param texCoords texture coordinates
     * @param normal    normal
     */
    public static void drawPolygon(final GL2 gl, final double[][] vertices, final double[][] texCoords, final double[] normal) {
        // create a square polygon
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glNormal3dv(normal, 0);

        // first triangle
        gl.glTexCoord2dv(texCoords[LEFT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_TOP], 0);

        gl.glTexCoord2dv(texCoords[LEFT_BOTTOM], 0);
        gl.glVertex3dv(vertices[LEFT_BOTTOM], 0);

        gl.glTexCoord2dv(texCoords[RIGHT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);

        // second triangle
        gl.glTexCoord2dv(texCoords[RIGHT_BOTTOM], 0);
        gl.glVertex3dv(vertices[RIGHT_BOTTOM], 0);

        gl.glTexCoord2dv(texCoords[RIGHT_TOP], 0);
        gl.glVertex3dv(vertices[RIGHT_TOP], 0);

        gl.glTexCoord2dv(texCoords[LEFT_TOP], 0);
        gl.glVertex3dv(vertices[LEFT_TOP], 0);

        // end draw
        gl.glEnd();
    }

    /**
     * Draw a rectangle polygon.
     *
     * @param gl        reference to current GL
     * @param vertices  vertices
     * @param texCoords texture coordinates
     * @param normal    normal
     * @param v1        index of first vertex
     * @param v2        index of second vertex
     * @param v3        index of third vertex
     * @param v4        index of fourth vertex
     */
    public static void drawPolygon(final GL2 gl, final double[][] vertices, final double[][] texCoords, final double[] normal,
                                   final int v1, final int v2, final int v3, final int v4) {
        // create a square polygon
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glNormal3dv(normal, 0);

        // first triangle
        gl.glTexCoord2dv(texCoords[v1], 0);
        gl.glVertex3dv(vertices[v1], 0);

        gl.glTexCoord2dv(texCoords[v2], 0);
        gl.glVertex3dv(vertices[v2], 0);

        gl.glTexCoord2dv(texCoords[v3], 0);
        gl.glVertex3dv(vertices[v3], 0);

        // second triangle
        gl.glTexCoord2dv(texCoords[v3], 0);
        gl.glVertex3dv(vertices[v3], 0);

        gl.glTexCoord2dv(texCoords[v4], 0);
        gl.glVertex3dv(vertices[v4], 0);

        gl.glTexCoord2dv(texCoords[v1], 0);
        gl.glVertex3dv(vertices[v1], 0);

        // end draw
        gl.glEnd();
    }

    /**
     * Draw a rectangle.
     *
     * @param gl     reference to current GL
     * @param x      the x location of center of rectangle
     * @param y      the y location of center of rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public static void drawRectangle(final GL2 gl, final double x, final double y, final double width, final double height) {
        final double halfWidth = width / 2.0;
        final double halfHeight = height / 2.0;
        final double left = x - halfWidth;
        final double right = x + halfWidth;
        final double bottom = y - halfHeight;
        final double top = y + halfHeight;
        // create a rectangle polygon
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glNormal3dv(NORMAL, 0);
        // vertex left top
        gl.glTexCoord2dv(TEX_COORDS[LEFT_TOP], 0);
        gl.glVertex3d(left, top, 0.0);
        // vertex left bottom
        gl.glTexCoord2dv(TEX_COORDS[LEFT_BOTTOM], 0);
        gl.glVertex3d(left, bottom, 0.0);
        // vertex right bottom
        gl.glTexCoord2dv(TEX_COORDS[RIGHT_BOTTOM], 0);
        gl.glVertex3d(right, bottom, 0.0);
        // vertext right bottom
        gl.glTexCoord2dv(TEX_COORDS[RIGHT_BOTTOM], 0);
        gl.glVertex3d(right, bottom, 0.0);
        // vertex right top
        gl.glTexCoord2dv(TEX_COORDS[RIGHT_TOP], 0);
        gl.glVertex3d(right, top, 0.0);
        // vertex left top
        gl.glTexCoord2dv(TEX_COORDS[LEFT_TOP], 0);
        gl.glVertex3d(left, top, 0.0);
        // end draw
        gl.glEnd();
    }

    /**
     * Draw a circle.
     *
     * @param gl     reference to current GL
     * @param x      the x location of center of circle
     * @param y      the y location of center of circle
     * @param radius the radius of the circle
     */
    public static void drawCircle(final GL2 gl, final double x, final double y, final double radius) {
        // create a polygon
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3dv(NORMAL, 0);

        // vertices
        // center vertex
        gl.glTexCoord2d(0.5, 0.5);
        gl.glVertex3d(x, y, 0.0);
        for (int i = CIRCLE_POINTS.length - 1; i >= 0; i--) {
            // vertex
            gl.glTexCoord2d(0.5 + (CIRCLE_POINTS[i][X] / 2.0), 0.5 + (CIRCLE_POINTS[i][Y] / 2.0));
            gl.glVertex3d(x + (CIRCLE_POINTS[i][X] * radius), y + (CIRCLE_POINTS[i][Y] * radius), 0.0);
        }
        // end draw
        gl.glEnd();
    }

    /**
     * Draw a circle.
     *
     * @param gl     reference to current GL
     * @param x      the x location of center of circle
     * @param y      the y location of center of circle
     * @param radius the radius of the circle
     * @param slices the number of slices to render with
     */
    public static void drawCircle(final GL2 gl, final double x, final double y, final double radius, final int slices) {
        final double angle = (2.0 * Math.PI) / slices;
        // create a polygon
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3dv(NORMAL, 0);
        // center vertex
        gl.glTexCoord2d(0.5, 0.5);
        gl.glVertex3d(x, y, 0.0);
        for (double a = 2.0 * Math.PI; a > 0.0; a -= angle) {
            final double ax = Math.sin(a);
            final double ay = Math.cos(a);
            // vertex
            gl.glTexCoord2d(0.5 + (ax / 2.0), 0.5 + (ay / 2.0));
            gl.glVertex3d(x + (ax * radius), y + (ay * radius), 0.0);
        }
        // final vertex
        gl.glTexCoord2d(0.5, 1.0);
        gl.glVertex3d(x, y + radius, 0.0);
        // end draw
        gl.glEnd();
    }

    /**
     * Draw an arc.
     *
     * @param gl     reference to current GL
     * @param x      the x location of center of circle
     * @param y      the y location of center of circle
     * @param radius the radius of the circle
     * @param start  the start in degrees
     * @param end    the end in degrees
     */
    public static void drawArc(final GL2 gl, final double x, final double y, final double radius, final double start, final double end) {
        final int startIndex = normalizeIndex((int) Math.ceil(start));
        final int endIndex = normalizeIndex((int) Math.floor(end));
        // create a triangle fan
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3dv(NORMAL, 0);
        // center point
        gl.glTexCoord2d(0.5, 0.5);
        gl.glVertex3d(x, y, 0.0);
        // end side
        double ax = Math.sin(Math.toRadians(end));
        double ay = Math.cos(Math.toRadians(end));
        // vertex
        gl.glTexCoord2d(0.5 + (ax / 2.0), 0.5 + (ay / 2.0));
        gl.glVertex3d(x + (ax * radius), y + (ay * radius), 0.0);
        // outer vertices
        for (int i = endIndex; i >= startIndex; i--) {
            // vertex
            gl.glTexCoord2d(0.5 + (CIRCLE_POINTS[i][X] / 2.0), 0.5 + (CIRCLE_POINTS[i][Y] / 2.0));
            gl.glVertex3d(x + (CIRCLE_POINTS[i][X] * radius), y + (CIRCLE_POINTS[i][Y] * radius), 0.0);
        }
        // start side
        ax = Math.sin(Math.toRadians(start));
        ay = Math.cos(Math.toRadians(start));
        // vertex
        gl.glTexCoord2d(0.5 + (ax / 2.0), 0.5 + (ay / 2.0));
        gl.glVertex3d(x + (ax * radius), y + (ay * radius), 0.0);
        gl.glEnd();
    }

    /**
     * Draw an arc.
     *
     * @param gl     reference to current GL
     * @param x      the x location of center of circle
     * @param y      the y location of center of circle
     * @param radius the radius of the circle
     * @param start  the start in degrees
     * @param end    the end in degrees
     * @param slices the number of slices to render with
     */
    public static void drawArc(final GL2 gl, final double x, final double y, final double radius, final double start, final double end, final int slices) {
        final double angle = Math.toRadians(end - start) / slices;
        // create a triangle fan
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3dv(NORMAL, 0);
        // center point
        gl.glVertex3d(x, y, 0.0);
        // circle points
        for (double a = end; a >= start; a -= angle) {
            final double ax = Math.sin(a);
            final double ay = Math.cos(a);
            // vertex
            gl.glVertex3d(x + (ax * radius), y + (ay * radius), 0.0);
        }
        gl.glEnd();
    }

    /**
     * Draw an arc with an inner and outer radius.
     * <p/>
     * TODO implement texture coords
     *
     * @param gl          reference to current GL
     * @param x           the x location of center of circle
     * @param y           the y location of center of circle
     * @param innerRadius the inner radius of the circle
     * @param outerRadius the outer radius of the circle
     * @param start       the start in degrees
     * @param end         the end in degrees
     */
    public static void drawArc(final GL2 gl, final double x, final double y, final double innerRadius, final double outerRadius, final double start, final double end) {
        final int startIndex = normalizeIndex((int) Math.ceil(start));
        final int endIndex = normalizeIndex((int) Math.floor(end));
        gl.glBegin(GL2.GL_QUAD_STRIP);
        gl.glNormal3dv(NORMAL, 0);
        // end side
        double ax = Math.sin(Math.toRadians(end));
        double ay = Math.cos(Math.toRadians(end));
        // inner vertex
        gl.glVertex3d(x + (ax * innerRadius), y + (ay * innerRadius), 0.0);
        // outer vertex
        gl.glVertex3d(x + (ax * outerRadius), y + (ay * outerRadius), 0.0);
        // inner vertices
        for (int i = endIndex; i >= startIndex; i--) {
            final int a = ShapeUtil.normalizeIndex(i);
            // inner vertex
            gl.glVertex3d(x + (CIRCLE_POINTS[a][X] * innerRadius), y + (CIRCLE_POINTS[a][Y] * innerRadius), 0.0);
            // outer vertex
            gl.glVertex3d(x + (CIRCLE_POINTS[a][X] * outerRadius), y + (CIRCLE_POINTS[a][Y] * outerRadius), 0.0);
        }
        // start side
        ax = Math.sin(Math.toRadians(start));
        ay = Math.cos(Math.toRadians(start));
        // inner vertex
        gl.glVertex3d(x + (ax * innerRadius), y + (ay * innerRadius), 0.0);
        // outer vertex
        gl.glVertex3d(x + (ax * outerRadius), y + (ay * outerRadius), 0.0);
        // end draw
        gl.glEnd();
    }

    /**
     * Draw an arc with an inner and outer radius.
     * <p/>
     * TODO implement texture coords
     *
     * @param gl          reference to current GL
     * @param x           the x location of center of circle
     * @param y           the y location of center of circle
     * @param innerRadius the inner radius of the circle
     * @param outerRadius the outer radius of the circle
     * @param start       the start in degrees
     * @param end         the end in degrees
     * @param slices      the number of slices to render with
     */
    public static void drawArc(final GL2 gl, final double x, final double y, final double innerRadius, final double outerRadius, final double start, final double end, final int slices) {
        final double angle = Math.toRadians(end - start) / slices;
        gl.glBegin(GL2.GL_QUAD_STRIP);
        gl.glNormal3dv(NORMAL, 0);
        // vertices
        for (double a = end; a >= start; a -= angle) {
            final double ax = Math.sin(a);
            final double ay = Math.cos(a);
            // inner vertex
            gl.glVertex3d(x + (ax * innerRadius), y + (ay * innerRadius), 0.0);
            // outer vertex
            gl.glVertex3d(x + (ax * outerRadius), y + (ay * outerRadius), 0.0);
        }
        // end draw
        gl.glEnd();
    }

    /**
     * Draw a border around a circle.
     *
     * @param gl        reference to current GL
     * @param x         the x location of center of circle
     * @param y         the y location of center of circle
     * @param radius    the radius of the circle
     * @param border    the border to draw
     * @param thickness the thickness of the border
     */
    public static void drawCircleBorder(final GL2 gl, final double x, final double y, final double radius, final Border border, final double thickness) {
        // calculate metrics
        final double innerRadius = radius;
        final double outerRadius = radius + thickness;
        // draw an arc all of the way around
        gl.glBegin(GL2.GL_QUAD_STRIP);
        gl.glNormal3dv(NORMAL, 0);
        for (int i = CIRCLE_POINTS.length - 1; i >= 0; i--) {
            final int a = normalizeIndex(i);
            // inner vertex
            gl.glVertex3d(x + (CIRCLE_POINTS[a][X] * innerRadius), y + (CIRCLE_POINTS[a][Y] * innerRadius), 0.0);
            // outer vertex
            gl.glVertex3d(x + (CIRCLE_POINTS[a][X] * outerRadius), y + (CIRCLE_POINTS[a][Y] * outerRadius), 0.0);
        }
        // end draw
        gl.glEnd();
    }

    /**
     * Draw a border around a rectangle.
     *
     * @param gl        reference to current GL
     * @param x         the x location of center of rectangle
     * @param y         the y location of center of rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     * @param border    the border to draw
     * @param thickness the thickness of the border
     */
    public static void drawRectangleBorder(final GL2 gl, final double x, final double y, final double width, final double height, final Border border,
                                           final double thickness) {
        if (border == Border.ALL) {
            GeometryUtil.drawRectangleBorder(gl, x, y, width, height, thickness);
            return;
        }
        // calculate the metrics
        final double xLeft = x - (width * 0.5);
        final double xRight = x + (width * 0.5);
        final double yBottom = y - (height * 0.5);
        final double yTop = y + (height * 0.5);
        // verts
        final double verts[][] = new double[4][3];
        // top
        if (border.isTop()) {
            verts[0] = new double[]{xLeft, yTop, 0.0};
            verts[1] = new double[]{xLeft, yTop + thickness, 0.0};
            verts[2] = new double[]{xRight, yTop + thickness, 0.0};
            verts[3] = new double[]{xRight, yTop, 0.0};
            GeometryUtil.drawPolygon(gl, verts);
        }
        // bottom
        if (border.isBottom()) {
            verts[0] = new double[]{xLeft, yBottom - thickness, 0.0};
            verts[1] = new double[]{xLeft, yBottom, 0.0};
            verts[2] = new double[]{xRight, yBottom, 0.0};
            verts[3] = new double[]{xRight, yBottom - thickness, 0.0};
            GeometryUtil.drawPolygon(gl, verts);
        }
        // left
        if (border.isLeft()) {
            verts[0] = new double[]{xLeft - thickness, yBottom, 0.0};
            verts[1] = new double[]{xLeft - thickness, yTop, 0.0};
            verts[2] = new double[]{xLeft, yTop, 0.0};
            verts[3] = new double[]{xLeft, yBottom, 0.0};
            GeometryUtil.drawPolygon(gl, verts);
        }
        // right
        if (border.isRight()) {
            verts[0] = new double[]{xRight, yBottom, 0.0};
            verts[1] = new double[]{xRight, yTop, 0.0};
            verts[2] = new double[]{xRight + thickness, yTop, 0.0};
            verts[3] = new double[]{xRight + thickness, yBottom, 0.0};
            GeometryUtil.drawPolygon(gl, verts);
        }
        // upper left corner
        if (border.isLeft() && border.isTop()) {
            verts[0] = new double[]{xLeft - thickness, yTop, 0.0};
            verts[1] = new double[]{xLeft - thickness, yTop + thickness, 0.0};
            verts[2] = new double[]{xLeft, yTop + thickness, 0.0};
            verts[3] = new double[]{xLeft, yTop, 0.0};
            GeometryUtil.drawPolygon(gl, verts);
        }
        // upper right corner
        if (border.isRight() && border.isTop()) {
            verts[0] = new double[]{xRight, yTop, 0.0};
            verts[1] = new double[]{xRight, yTop + thickness, 0.0};
            verts[2] = new double[]{xRight + thickness, yTop + thickness, 0.0};
            verts[3] = new double[]{xRight + thickness, yTop, 0.0};
            GeometryUtil.drawPolygon(gl, verts);
        }
        // lower left corner
        if (border.isLeft() && border.isBottom()) {
            verts[0] = new double[]{xLeft - thickness, yBottom - thickness, 0.0};
            verts[1] = new double[]{xLeft - thickness, yBottom, 0.0};
            verts[2] = new double[]{xLeft, yBottom, 0.0};
            verts[3] = new double[]{xLeft, yBottom - thickness, 0.0};
            GeometryUtil.drawPolygon(gl, verts);
        }
        // lower right corner
        if (border.isRight() && border.isBottom()) {
            verts[0] = new double[]{xRight, yBottom - thickness, 0.0};
            verts[1] = new double[]{xRight, yBottom, 0.0};
            verts[2] = new double[]{xRight + thickness, yBottom, 0.0};
            verts[3] = new double[]{xRight + thickness, yBottom - thickness, 0.0};
            GeometryUtil.drawPolygon(gl, verts);
        }
    }

    private static void drawRectangleBorder(final GL2 gl, final double x, final double y, final double width, final double height, final double thickness) {
        // calculate the metrics
        final double xLeft = x - (width * 0.5);
        final double xRight = x + (width * 0.5);
        final double yBottom = y - (height * 0.5);
        final double yTop = y + (height * 0.5);
        // verts
        final double verts[][] = new double[4][3];
        // top
        verts[0] = new double[]{xLeft, yTop, 0.0};
        verts[1] = new double[]{xLeft, yTop + thickness, 0.0};
        verts[2] = new double[]{xRight, yTop + thickness, 0.0};
        verts[3] = new double[]{xRight, yTop, 0.0};
        GeometryUtil.drawPolygon(gl, verts);
        // bottom
        verts[0] = new double[]{xLeft, yBottom - thickness, 0.0};
        verts[1] = new double[]{xLeft, yBottom, 0.0};
        verts[2] = new double[]{xRight, yBottom, 0.0};
        verts[3] = new double[]{xRight, yBottom - thickness, 0.0};
        GeometryUtil.drawPolygon(gl, verts);
        // left
        verts[0] = new double[]{xLeft - thickness, yBottom - thickness, 0.0};
        verts[1] = new double[]{xLeft - thickness, yTop + thickness, 0.0};
        verts[2] = new double[]{xLeft, yTop + thickness, 0.0};
        verts[3] = new double[]{xLeft, yBottom - thickness, 0.0};
        GeometryUtil.drawPolygon(gl, verts);
        // right
        verts[0] = new double[]{xRight, yBottom - thickness, 0.0};
        verts[1] = new double[]{xRight, yTop + thickness, 0.0};
        verts[2] = new double[]{xRight + thickness, yTop + thickness, 0.0};
        verts[3] = new double[]{xRight + thickness, yBottom - thickness, 0.0};
        GeometryUtil.drawPolygon(gl, verts);
    }
}
