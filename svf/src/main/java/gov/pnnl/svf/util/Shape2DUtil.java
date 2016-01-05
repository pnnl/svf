package gov.pnnl.svf.util;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUnurbs;
import com.jogamp.opengl.glu.GLUtessellator;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.util.BezierCurveEvaluator;
import gov.pnnl.svf.core.util.MathUtil;
import gov.pnnl.svf.geometry.Arc2D;
import gov.pnnl.svf.geometry.Chart2D;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Path2D;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Polygon2D;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.RoundedRectangle2D;
import gov.pnnl.svf.geometry.Text2D;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility class containing all of the 2D shape methods.
 *
 * @author Arthur Bleeker
 */
public class Shape2DUtil extends ShapeUtil {

    private static final Logger logger = Logger.getLogger(Shape2DUtil.class.getName());

    /**
     * Constructor kept private for static utility class
     */
    protected Shape2DUtil() {
    }

    /**
     * Draw a point using the supplied shape.
     *
     * @param gl    reference to the current GL
     * @param point the point to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or shape are null
     */
    public static int drawShape(final GL2 gl, final Point2D point) {
        // draw a point
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex3d(point.getX(), point.getY(), 0.0);
        gl.glEnd();
        return 1;
    }

    /**
     * Draw a path using the supplied shape.
     *
     * @param gl   reference to the current GL
     * @param path the path to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or path are null
     */
    public static int drawShape(final GL2 gl, final Path2D path) {
        final List<Point2D> points = path.getPoints();
        // draw a path
        int vertices = 0;
        switch (path.getStyle()) {
            case NONE:
                gl.glBegin(GL.GL_LINE_STRIP);
                for (final Point2D point : points) {
                    gl.glVertex3d(point.getX() + path.getX(), point.getY() + path.getY(), 0.0);
                    vertices++;
                }
                gl.glEnd();
                break;
            case POINTS:
                gl.glBegin(GL.GL_POINTS);
                for (final Point2D point : points) {
                    gl.glVertex3d(point.getX() + path.getX(), point.getY() + path.getY(), 0.0);
                    vertices++;
                }
                gl.glEnd();
                break;
            case NURBS:
                // create the knots
                // these are created in ascending order with a step size of 1 to create
                // a uniform curve that begins and terminates at the start and end
                // control point
                final float[] knots = new float[points.size() + NURBS_ORDER];
                int value = 0;
                for (int i = 0; i < knots.length; i++) {
                    if (i > NURBS_DEGREE && i < knots.length - (NURBS_DEGREE)) {
                        value++;
                    }
                    knots[i] = value;
                }
                // create the control points for the curve
                final float[] control = new float[points.size() * NURBS_DEGREE];
                for (int i = 0; i < points.size(); i++) {
                    final Point2D point = points.get(i);
                    control[i * NURBS_DEGREE] = (float) (point.getX() + path.getX());
                    control[i * NURBS_DEGREE + 1] = (float) (point.getY() + path.getY());
                }
                // draw the curve
                final GLUgl2 glu = new GLUgl2();
                final GLUnurbs nurbs = glu.gluNewNurbsRenderer();
                glu.gluBeginCurve(nurbs);
                glu.gluNurbsCurve(nurbs, knots.length, knots, NURBS_DEGREE, control, NURBS_ORDER, GL2.GL_MAP1_VERTEX_3);
                glu.gluEndCurve(nurbs);
                // finish
                vertices += points.size() * (10 + 1); // this number is a guess-timation
                break;
            case BEZIER:
                // create a series of bezier curves
                final BezierCurveEvaluator evaluator = new BezierCurveEvaluator(2);
                final List<double[]> curves = createCurves2D(points);
                for (final double[] curve : curves) {
                    // draw straight sections of lines to simulate a curve
                    final double[] vertex = new double[2];
                    gl.glBegin(GL.GL_LINE_STRIP);
                    gl.glVertex3d(curve[0] + path.getX(), curve[1] + path.getY(), 0.0);
                    for (int i = 1; i < BEZIER_EVALUATORS; i++) {
                        evaluator.evaluate(curve, (double) i / (double) BEZIER_EVALUATORS, vertex);
                        gl.glVertex3d(vertex[0] + path.getX(), vertex[1] + path.getY(), 0.0);
                    }
                    gl.glVertex3d(curve[6] + path.getX(), curve[7] + path.getY(), 0.0);
                    gl.glEnd();
                }
                vertices += curves.size() * (BEZIER_EVALUATORS + 1);
                break;
            default:
                throw new IllegalArgumentException("Unhandled enum passed to control statement: " + path.getStyle());
        }
        return vertices;
    }

    /**
     * Draw text using the supplied shape.
     *
     * @param gl   reference to the current GL
     * @param text the text to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or text are null
     */
    public static int drawShape(final GL2 gl, final Text2D text) {
        GeometryUtil.drawRectangle(gl, text.getX(), text.getY(), text.getWidth(), text.getHeight());
        return 4;
    }

    /**
     * Draw a rectangle using the supplied shape.
     *
     * @param gl        reference to the current GL
     * @param rectangle the rectangle to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or rectangle are null
     */
    public static int drawShape(final GL2 gl, final Rectangle rectangle) {
        GeometryUtil.drawRectangle(gl, rectangle.getX() + (rectangle.getWidth() / 2.0), rectangle.getY() + (rectangle.getHeight() / 2.0), rectangle.getWidth(), rectangle.getHeight());
        return 4;
    }

    /**
     * Draw a rectangle using the supplied shape.
     *
     * @param gl        reference to the current GL
     * @param rectangle the rectangle to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or rectangle are null
     */
    public static int drawShape(final GL2 gl, final Rectangle2D rectangle) {
        GeometryUtil.drawRectangle(gl, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        return 4;
    }

    /**
     * Draw a rounded rectangle using the supplied shape.
     *
     * @param gl        reference to the current GL
     * @param rectangle the rectangle to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or rectangle are null
     */
    public static int drawShape(final GL2 gl, final RoundedRectangle2D rectangle) {
        return drawShape(gl, rectangle, 90);
    }

    /**
     * Draw a rounded rectangle using the supplied shape.
     *
     * @param gl        reference to the current GL
     * @param rectangle the rectangle to draw
     * @param slices    the number of slices used for the corners
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or rectangle are null
     */
    public static int drawShape(final GL2 gl, final RoundedRectangle2D rectangle, final int slices) {
        final double roundness = rectangle.getRoundness();
        final double width = rectangle.getWidth() - (roundness * 2.0);
        final double height = rectangle.getHeight() - (roundness * 2.0);
        // center
        GeometryUtil.drawRectangle(gl, rectangle.getX(), rectangle.getY(), width, height);
        // top
        GeometryUtil.drawRectangle(gl, rectangle.getX(), rectangle.getY() + (height + roundness) / 2.0, width, roundness);
        // right
        GeometryUtil.drawRectangle(gl, rectangle.getX() + (width + roundness) / 2.0, rectangle.getY(), roundness, height);
        // bottom
        GeometryUtil.drawRectangle(gl, rectangle.getX(), rectangle.getY() - (height + roundness) / 2.0, width, roundness);
        // left
        GeometryUtil.drawRectangle(gl, rectangle.getX() - (width + roundness) / 2.0, rectangle.getY(), roundness, height);
        // top right arc
        GeometryUtil.drawArc(gl, rectangle.getX() + width / 2.0, rectangle.getY() + height / 2.0, roundness, 0.0, 90.0, slices);
        // bottom right arc
        GeometryUtil.drawArc(gl, rectangle.getX() + width / 2.0, rectangle.getY() - height / 2.0, roundness, 90.0, 180.0, slices);
        // bottom left arc
        GeometryUtil.drawArc(gl, rectangle.getX() - width / 2.0, rectangle.getY() - height / 2.0, roundness, 180.0, 270.0, slices);
        // top left arc
        GeometryUtil.drawArc(gl, rectangle.getX() - width / 2.0, rectangle.getY() + height / 2.0, roundness, 270.0, 360.0, slices);
        // vertices
        return (4 * (slices + 1)) + (6 * 4);
    }

    /**
     * Draw a circle using the supplied shape.
     *
     * @param gl     reference to the current GL
     * @param circle the circle to draw
     * @param slices number of slices for the circle (must be at least 3)
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException     if gl or circle are null
     * @throws IllegalArgumentException if slices is less than three
     */
    public static int drawShape(final GL2 gl, final Circle2D circle, final int slices) {
        if (slices < 3) {
            throw new IllegalArgumentException("slices");
        }
        final double radius = circle.getRadius();
        GeometryUtil.drawCircle(gl, circle.getX(), circle.getY(), radius, slices);
        return slices + 2;
    }

    /**
     * Draw a circle using the supplied shape. This method is optimized to use
     * 360 degree reference points for drawing the circle.
     *
     * @param gl     reference to the current GL
     * @param circle the circle to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or circle are null
     */
    public static int drawShape(final GL2 gl, final Circle2D circle) {
        GeometryUtil.drawCircle(gl, circle.getX(), circle.getY(), circle.getRadius());
        return CIRCLE_POINTS.length + 1;
    }

    /**
     * Draw an arc using the supplied shape.
     *
     * @param gl     reference to the current GL
     * @param arc    the arc to draw
     * @param slices number of slices for the arc (must be at least 2)
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException     if gl or arc are null
     * @throws IllegalArgumentException if slices is less than 3
     */
    public static int drawShape(final GL2 gl, final Arc2D arc, final int slices) {
        if (slices < 3) {
            throw new IllegalArgumentException("slices");
        }
        final double innerRadius = arc.getRadius() - (arc.getHeight() / 2.0);
        final double outerRadius = arc.getRadius() + (arc.getHeight() / 2.0);
        final double start = Math.toRadians(arc.getAngle() - (arc.getSector() / 2.0));
        final double end = Math.toRadians(arc.getAngle() + (arc.getSector() / 2.0));
        // create a polygon
        int vertices = 0;
        if (Double.compare(innerRadius, 0.0) == 0) {
            GeometryUtil.drawArc(gl, arc.getX(), arc.getY(), outerRadius, start, end, slices);
            vertices = slices + 1;
        } else {
            GeometryUtil.drawArc(gl, arc.getX(), arc.getY(), innerRadius, outerRadius, start, end, slices);
            vertices = slices + slices + 2;
        }
        return vertices;
    }

    /**
     * Draw an arc using the supplied shape. This method is optimized to use 360
     * degree reference points for drawing the arc.
     *
     * @param gl  reference to the current GL
     * @param arc the arc to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or arc are null
     */
    public static int drawShape(final GL2 gl, final Arc2D arc) {
        final double innerRadius = arc.getRadius() - (arc.getArcHeight() / 2.0);
        final double outerRadius = arc.getRadius() + (arc.getArcHeight() / 2.0);
        final double start = arc.getAngle() - (arc.getSector() / 2.0);
        final double end = arc.getAngle() + (arc.getSector() / 2.0);
        final int startIndex = (int) Math.ceil(arc.getAngle() - (arc.getSector() / 2.0));
        final int endIndex = (int) Math.floor(arc.getAngle() + (arc.getSector() / 2.0));
        // create a polygon
        int vertices = 0;
        if (Double.compare(innerRadius, 0.0) == 0) {
            GeometryUtil.drawArc(gl, arc.getX(), arc.getY(), outerRadius, start, end);
            vertices = endIndex - startIndex + 3;
        } else {
            GeometryUtil.drawArc(gl, arc.getX(), arc.getY(), innerRadius, outerRadius, start, end);
            vertices = (endIndex - startIndex + 2) * 2;
        }
        return vertices;
    }

    /**
     * Draw a polygon using the supplied shape.
     *
     * @param gl      reference to the current GL
     * @param polygon the polygon to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or polygon are null
     */
    public static int drawShape(final GL2 gl, final Polygon2D polygon) {
        final GLUtessellator tessellator = GLU.gluNewTess();
        GLU.gluTessNormal(tessellator, NORMAL[0], NORMAL[1], NORMAL[2]);
        GLU.gluTessProperty(tessellator, GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
        final TessellatorCallbackGl callback = new TessellatorCallbackGl(gl);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_VERTEX, callback);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_BEGIN, callback);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_END, callback);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_ERROR, callback);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_COMBINE, callback);
        GLU.gluTessBeginPolygon(tessellator, null);
        int vertices = 0;
        for (final List<Point2D> points : polygon.getContours()) {
            GLU.gluTessBeginContour(tessellator);
            for (final Point2D point : points) {
                final double[] tess = new double[]{polygon.getX() + point.getX(), polygon.getY() + point.getY(), 0.0};
                GLU.gluTessVertex(tessellator, tess, 0, tess);
            }
            GLU.gluTessEndContour(tessellator);
            vertices += points.size() * 4; // just a guess-timate
        }
        GLU.gluTessEndPolygon(tessellator);
        // cleanup
        GLU.gluDeleteTess(tessellator);
        return vertices;
    }

    /**
     * Draw a chart using the supplied shape.
     *
     * @param gl    reference to the current GL
     * @param chart the chart to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or chart are null
     */
    public static int drawShape(final GL2 gl, final Chart2D chart) {
        int vertices = 0;
        switch (chart.getStyle()) {
            case NONE:
                // draw nothing
                break;
            case SCATTER:
                // draw a series of points
                gl.glBegin(GL.GL_POINTS);
                for (int i = 0; i < chart.getPoints().size(); i++) {
                    final Point2D point = chart.getPoints().get(i);
                    gl.glVertex3d(MathUtil.scale(point.getX(), chart.getMaximum().getX(), chart.getMinimum().getX(), 0.5, -0.5) + chart.getX(), MathUtil.scale(point.getY(), chart.getMaximum().getY(), chart.getMinimum().getY(), 0.5, -0.5) + chart.getY(), 0.0);
                    vertices++;
                }
                gl.glEnd();
                break;
            case LINE:
                // draw a series of lines
                gl.glBegin(GL.GL_LINE_STRIP);
                for (int i = 0; i < chart.getPoints().size(); i++) {
                    final Point2D point = chart.getPoints().get(i);
                    gl.glVertex3d(MathUtil.scale(point.getX(), chart.getMaximum().getX(), chart.getMinimum().getX(), 0.5, -0.5) + chart.getX(), MathUtil.scale(point.getY(), chart.getMaximum().getY(), chart.getMinimum().getY(), 0.5, -0.5) + chart.getY(), 0.0);
                    vertices++;
                }
                gl.glEnd();
                break;
            case AREA: {
                // draw a filled polygon
                final double[][] verts = new double[4][3];
                for (int i = 1; i < chart.getPoints().size(); i++) {
                    final Point2D left = chart.getPoints().get(i - 1);
                    final Point2D right = chart.getPoints().get(i);
                    // find vertices
                    verts[0][0] = MathUtil.scale(left.getX(), chart.getMaximum().getX(), chart.getMinimum().getX(), 0.5, -0.5) + chart.getX();
                    verts[0][1] = chart.getY() - 0.5;
                    verts[1][0] = verts[0][0];
                    verts[1][1] = MathUtil.scale(left.getY(), chart.getMaximum().getY(), chart.getMinimum().getY(), 0.5, -0.5) + chart.getY();
                    verts[2][0] = MathUtil.scale(right.getX(), chart.getMaximum().getX(), chart.getMinimum().getX(), 0.5, -0.5) + chart.getX();
                    verts[2][1] = MathUtil.scale(right.getY(), chart.getMaximum().getY(), chart.getMinimum().getY(), 0.5, -0.5) + chart.getY();
                    verts[3][0] = verts[2][0];
                    verts[3][1] = verts[0][1];
                    // draw polygon
                    GeometryUtil.drawPolygon(gl, verts);
                    vertices += 4;
                }
                break;
            }
            case BAR: {
                // draw a series of bars
                final double[][] verts = new double[4][3];
                final double padding = 0.2 / (double) chart.getPoints().size();
                final double width = 0.8 / (double) chart.getPoints().size();
                double x = padding / 2.0;
                for (int i = 0; i < chart.getPoints().size(); i++) {
                    final Point2D point = chart.getPoints().get(i);
                    // find vertices
                    verts[0][0] = x + chart.getX() - 0.5;
                    verts[0][1] = chart.getY() - 0.5;
                    verts[1][0] = verts[0][0];
                    verts[1][1] = MathUtil.scale(point.getY(), chart.getMaximum().getY(), chart.getMinimum().getY(), 0.5, -0.5) + chart.getY();
                    verts[2][0] = x + width + chart.getX() - 0.5;
                    verts[2][1] = verts[1][1];
                    verts[3][0] = verts[2][0];
                    verts[3][1] = verts[0][1];
                    // check for color
                    if (point.getX() == Chart2D.NO_COLOR) {
                        // draw polygon
                        GeometryUtil.drawPolygon(gl, verts);
                    } else {
                        // draw polygon with color
                        final Color color = Color.fromInt((int) point.getX());
                        pushColor(gl, color);
                        GeometryUtil.drawPolygon(gl, verts);
                        popColor(gl, color);
                    }
                    // increment x
                    x += (width + padding);
                    vertices += 4;
                }
                break;
            }
            case PIE: {
                // get the total
                double total = 0.0;
                for (final Point2D point : chart.getPoints()) {
                    total += point.getY();
                }
                // draw the pies
                double a = 0.0;
                for (final Point2D point : chart.getPoints()) {
                    final double s = point.getY() / total * 360.0;
                    // check for color
                    if (point.getX() == Chart2D.NO_COLOR) {
                        // draw polygon
                        GeometryUtil.drawArc(gl, chart.getX(), chart.getY(), 0.5, a, a + s);
                    } else {
                        // draw polygon with color
                        final Color color = Color.fromInt((int) point.getX());
                        pushColor(gl, color);
                        GeometryUtil.drawArc(gl, chart.getX(), chart.getY(), 0.5, a, a + s);
                        popColor(gl, color);
                    }
                    // incrememnt arc
                    a += s;
                    vertices += (int) (s - a) + 3;
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unhandled enum passed to control statement: " + chart.getStyle());
        }
        return vertices;
    }

    /**
     * Draws a border around a point.
     *
     * @param gl        reference to the current GL
     * @param point     the point to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the point border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Point2D point, final Border border, final double thickness) {
        // draw border around a point
        return drawBorder(gl, new Rectangle2D(point.getX(), point.getY(), 1.0, 1.0), border, thickness);
    }

    /**
     * Draws a border around a rectangle.
     *
     * @param gl        reference to the current GL
     * @param rectangle the rectangle to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the rectangle border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Rectangle2D rectangle, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return 0;
        }
        GeometryUtil.drawRectangleBorder(gl, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), border, thickness);
        // calculate metrics
        int vertices = 0;
        if (border.containsBorder(Border.ALL)) {
            vertices += 16;
        } else {
            vertices += border.isTop() ? 4 : 0;
            vertices += border.isBottom() ? 4 : 0;
            vertices += border.isLeft() ? 4 : 0;
            vertices += border.isRight() ? 4 : 0;
            vertices += border.isLeft() || border.isTop() ? 4 : 0;
            vertices += border.isLeft() || border.isBottom() ? 4 : 0;
            vertices += border.isRight() || border.isTop() ? 4 : 0;
            vertices += border.isRight() || border.isBottom() ? 4 : 0;
        }
        return vertices;
    }

    /**
     * Draw a border around a rounded rectangle.
     *
     * @param gl        reference to the current GL
     * @param rectangle the rectangle to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final RoundedRectangle2D rectangle, final Border border, final double thickness) {
        final double roundness = rectangle.getRoundness();
        final double width = rectangle.getWidth() - (roundness * 2.0);
        final double height = rectangle.getHeight() - (roundness * 2.0);
        // top
        if (border.isTop()) {
            GeometryUtil.drawRectangle(gl, rectangle.getX(), rectangle.getY() + (height + roundness + roundness + thickness) / 2.0, width, thickness);
        }
        // right
        if (border.isRight()) {
            GeometryUtil.drawRectangle(gl, rectangle.getX() + (width + roundness + roundness + thickness) / 2.0, rectangle.getY(), thickness, height);
        }
        // bottom
        if (border.isBottom()) {
            GeometryUtil.drawRectangle(gl, rectangle.getX(), rectangle.getY() - (height + roundness + roundness + thickness) / 2.0, width, thickness);
        }
        // left
        if (border.isLeft()) {
            GeometryUtil.drawRectangle(gl, rectangle.getX() - (width + roundness + roundness + thickness) / 2.0, rectangle.getY(), thickness, height);
        }
        // top right arc
        if (border.isTop() && border.isRight()) {
            GeometryUtil.drawArc(gl, rectangle.getX() + width / 2.0, rectangle.getY() + height / 2.0, roundness, roundness + thickness, 0.0, 90.0);
        }
        // bottom right arc
        if (border.isBottom() && border.isRight()) {
            GeometryUtil.drawArc(gl, rectangle.getX() + width / 2.0, rectangle.getY() - height / 2.0, roundness, roundness + thickness, 90.0, 180.0);
        }
        // bottom left arc
        if (border.isBottom() && border.isLeft()) {
            GeometryUtil.drawArc(gl, rectangle.getX() - width / 2.0, rectangle.getY() - height / 2.0, roundness, roundness + thickness, 180.0, 270.0);
        }
        // top left arc
        if (border.isTop() && border.isLeft()) {
            GeometryUtil.drawArc(gl, rectangle.getX() - width / 2.0, rectangle.getY() + height / 2.0, roundness, roundness + thickness, 270.0, 360.0);
        }
        // vertices
        return (4 * (90 + 1)) + (4 * 4);
    }

    /**
     * Draws a border around text.
     *
     * @param gl        reference to the current GL
     * @param text      the text to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the rectangle border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Text2D text, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return 0;
        }
        GeometryUtil.drawRectangleBorder(gl, text.getX(), text.getY(), text.getWidth(), text.getHeight(), border, thickness);
        // calculate metrics
        int vertices = 0;
        if (border.containsBorder(Border.ALL)) {
            vertices += 16;
        } else {
            vertices += border.isTop() ? 4 : 0;
            vertices += border.isBottom() ? 4 : 0;
            vertices += border.isLeft() ? 4 : 0;
            vertices += border.isRight() ? 4 : 0;
            vertices += border.isLeft() || border.isTop() ? 4 : 0;
            vertices += border.isLeft() || border.isBottom() ? 4 : 0;
            vertices += border.isRight() || border.isTop() ? 4 : 0;
            vertices += border.isRight() || border.isBottom() ? 4 : 0;
        }
        return vertices;
    }

    /**
     * Draws a border around a chart.
     *
     * @param gl        reference to the current GL
     * @param chart     the chart to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the rectangle border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Chart2D chart, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0) {
            return 0;
        }
        int vertices = 0;
        switch (chart.getStyle()) {
            case NONE:
            case SCATTER:
            case LINE:
            case AREA:
            case BAR:
                GeometryUtil.drawRectangleBorder(gl, chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight(), border, thickness);
                // calculate metrics
                if (border.containsBorder(Border.ALL)) {
                    vertices += 16;
                } else {
                    vertices += border.isTop() ? 4 : 0;
                    vertices += border.isBottom() ? 4 : 0;
                    vertices += border.isLeft() ? 4 : 0;
                    vertices += border.isRight() ? 4 : 0;
                    vertices += border.isLeft() || border.isTop() ? 4 : 0;
                    vertices += border.isLeft() || border.isBottom() ? 4 : 0;
                    vertices += border.isRight() || border.isTop() ? 4 : 0;
                    vertices += border.isRight() || border.isBottom() ? 4 : 0;
                }
                break;
            case PIE:
                GeometryUtil.drawCircleBorder(gl, chart.getX(), chart.getY(), 0.5, border, thickness);
                vertices = drawBorder(gl, new Circle2D(chart.getX(), chart.getY(), 0.5), border, thickness);
                break;
            default:
                throw new IllegalArgumentException("Unhandled enum passed to switch statement: " + chart.getStyle());
        }
        return vertices;
    }

    /**
     * Draws a border around a circle.
     *
     * @param gl        reference to the current GL
     * @param circle    the circle to draw
     * @param border    the border, any border is treated as all borders
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the circle border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Circle2D circle, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return 0;
        }
        GeometryUtil.drawCircleBorder(gl, circle.getX(), circle.getY(), circle.getRadius(), border, thickness);
        return CIRCLE_POINTS.length + CIRCLE_POINTS.length;
    }

    /**
     * Draws a border around an arc.
     *
     * @param gl        reference to the current GL
     * @param arc       the arc to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the arc border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Arc2D arc, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return 0;
        }
        // calculate metrics
        double borderSector = (180.0 * thickness) / (Math.PI * arc.getRadius());
        borderSector = Math.max(0.0, borderSector);
        double innerRadius = arc.getRadius() - ((arc.getArcHeight() + thickness) * 0.5);
        innerRadius = Math.max(0.0, innerRadius);
        double outerRadius = arc.getRadius() + ((arc.getArcHeight() + thickness) * 0.5);
        outerRadius = Math.max(0.0, outerRadius);
        double innerWidth = MathUtil.scale(innerRadius, arc.getRadius(), 0.0, arc.getArcWidth(), 0.0);
        innerWidth = Math.max(0.0, innerWidth);
        double outerWidth = MathUtil.scale(outerRadius, arc.getRadius(), 0.0, arc.getArcWidth(), 0.0);
        outerWidth = Math.max(0.0, outerWidth);
        int vertices = 0;
        // draw the border as multiple arcs
        if (border.containsBorder(Border.ALL)) {
            // top
            vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), outerRadius, outerWidth, thickness));
            // bottom
            vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), innerRadius, innerWidth, thickness));
            // left
            vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, thickness + arc.getArcHeight() + thickness));
            // right
            vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, thickness + arc.getArcHeight() + thickness));
        } else {
            // top
            if (border.isTop()) {
                vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), outerRadius, outerWidth, thickness));
            }
            // bottom
            if (border.isBottom()) {
                vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), innerRadius, innerWidth, thickness));
            }
            // left
            if (border.isLeft()) {
                vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, arc.getArcHeight()));
            }
            // right
            if (border.isRight()) {
                vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, arc.getArcHeight()));
            }
            // top left
            if (border.isTop() || border.isLeft()) {
                vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), outerRadius, thickness, thickness));
            }
            // top right
            if (border.isTop() || border.isRight()) {
                vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), outerRadius, thickness, thickness));
            }
            // bottom left
            if (border.isBottom() || border.isLeft()) {
                vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), innerRadius, thickness, thickness));
            }
            // bottom right
            if (border.isBottom() || border.isRight()) {
                vertices += drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), innerRadius, thickness, thickness));
            }
        }
        return vertices;
    }

}
