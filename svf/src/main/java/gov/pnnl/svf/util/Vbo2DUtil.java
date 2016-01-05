package gov.pnnl.svf.util;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUtessellator;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.constant.DimensionConst;
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
import static gov.pnnl.svf.util.AbstractGLUtil.createCurves2D;
import gov.pnnl.svf.vbo.VertexBufferObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * Utility class for working with Apache Commons Math and SVF geometry.
 *
 * @author Arthur Bleeker
 */
public class Vbo2DUtil extends VboUtil implements DimensionConst {

    private static final Logger logger = Logger.getLogger(Vbo2DUtil.class.getName());

    /**
     * Constructor kept private for static utility class
     */
    protected Vbo2DUtil() {
    }

    /**
     * Create a point using the supplied shape.
     *
     * @param point the point to create
     * @param color the optional color
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException if shape is null
     */
    public static VertexBufferObject createShape(final Point2D point, final Color color) {
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .mode(GL.GL_POINTS)
                .vertexDimension(TWO_D)
                .vertices(new double[]{point.getX(), point.getY()});
        return buildVbo(vbo, null, null, color);
    }

    /**
     * Create a path using the supplied shape.
     *
     * @param path  the path to draw
     * @param color the optional color
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException if shape is null
     */
    public static VertexBufferObject createShape(final Path2D path, final Color color) {
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D);
        final List<Point2D> points = path.getPoints();
        final DoubleList vertices;
        // draw a path
        switch (path.getStyle()) {
            case NONE:
                vbo.mode(GL.GL_LINE_STRIP);
                vertices = new ArrayDoubleList(points.size() * 2);
                for (int i = 0; i < points.size(); i++) {
                    final Point2D point = points.get(i);
                    vertices.add(point.getX() + path.getX());
                    vertices.add(point.getY() + path.getY());
                }
                break;
            case POINTS:
                vbo.mode(GL.GL_POINTS);
                vertices = new ArrayDoubleList(points.size() * 2);
                for (int i = 0; i < points.size(); i++) {
                    final Point2D point = points.get(i);
                    vertices.add(point.getX() + path.getX());
                    vertices.add(point.getY() + path.getY());
                }
                break;
            case NURBS:
                throw new IllegalArgumentException("NURBS path type is not supported for VBO.");
            case BEZIER:
                // create a series of bezier curves
                vbo.mode(GL.GL_LINE_STRIP);
                final BezierCurveEvaluator evaluator = new BezierCurveEvaluator(2);
                final List<double[]> curves = createCurves2D(points);
                vertices = new ArrayDoubleList((curves.size() * (1 + BEZIER_EVALUATORS - 1 + 1)) * 2);
                final double[] vertex = new double[2];
                for (int c = 0; c < curves.size(); c++) {
                    final double[] curve = curves.get(c);
                    // draw straight sections of lines to simulate a curve
                    vertices.add(curve[0] + path.getX());
                    vertices.add(curve[1] + path.getY());
                    for (int i = 1; i < BEZIER_EVALUATORS; i++) {
                        evaluator.evaluate(curve, (double) i / (double) BEZIER_EVALUATORS, vertex);
                        vertices.add(vertex[0] + path.getX());
                        vertices.add(vertex[1] + path.getY());
                    }
                    vertices.add(curve[6] + path.getX());
                    vertices.add(curve[7] + path.getY());
                }
                break;
            default:
                throw new IllegalArgumentException("Unhandled enum passed to control statement: " + path.getStyle());
        }
        vbo.vertices(vertices.toArray());
        return buildVbo(vbo, null, null, color);
    }

    /**
     * Create text using the supplied shape.
     *
     *
     * @param text      the text to draw
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException if shape is null
     */
    public static VertexBufferObject createShape(final Text2D text, final Color color, final boolean texCoords) {
        final DoubleList vertices = new ArrayDoubleList(12);
        final int mode = addRectangle(vertices, text.getX(), text.getY(), text.getWidth(), text.getHeight());
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        if (texCoords) {
            vbo.texCoordDimension(TWO_D);
            return buildVbo(vbo, NORMAL, RECTANGLE_TEX_COORDS.toArray(), color);
        } else {
            return buildVbo(vbo, NORMAL, null, color);
        }
    }

    /**
     * Create a rectangle using the supplied shape.
     *
     * @param rectangle the rectangle to draw
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException if shape is null
     */
    public static VertexBufferObject createShape(final Rectangle rectangle, final Color color, final boolean texCoords) {
        final DoubleList vertices = new ArrayDoubleList(12);
        final int mode = addRectangle(vertices, rectangle.getX() + (rectangle.getWidth() / 2.0), rectangle.getY() + (rectangle.getHeight() / 2.0), rectangle.getWidth(), rectangle.getHeight());
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        if (texCoords) {
            vbo.texCoordDimension(TWO_D);
            return buildVbo(vbo, NORMAL, RECTANGLE_TEX_COORDS.toArray(), color);
        } else {
            return buildVbo(vbo, NORMAL, null, color);
        }
    }

    /**
     * Create a rectangle using the supplied shape.
     *
     * @param rectangle the rectangle to draw
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException if shape is null
     */
    public static VertexBufferObject createShape(final Rectangle2D rectangle, final Color color, final boolean texCoords) {
        final DoubleList vertices = new ArrayDoubleList(12);
        final int mode = addRectangle(vertices, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        if (texCoords) {
            vbo.texCoordDimension(TWO_D);
            return buildVbo(vbo, NORMAL, RECTANGLE_TEX_COORDS.toArray(), color);
        } else {
            return buildVbo(vbo, NORMAL, null, color);
        }
    }

    /**
     * Create a rounded rectangle using the supplied shape.
     *
     * @param rectangle the rectangle to draw
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException if shape is null
     */
    public static List<VertexBufferObject> createShape(final RoundedRectangle2D rectangle, final Color color, final boolean texCoords) {
        return createShape(rectangle, 90, color, texCoords);
    }

    /**
     * Create a rounded rectangle using the supplied shape.
     *
     * @param rectangle the rectangle to draw
     * @param slices    the number of slices used for the corners
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException if shape is null
     */
    public static List<VertexBufferObject> createShape(final RoundedRectangle2D rectangle, final int slices, final Color color, final boolean texCoords) {
        final List<VertexBufferObject> vbos = new ArrayList<>(9);
        final double roundness = rectangle.getRoundness();
        final double width = rectangle.getWidth() - (roundness * 2.0);
        final double height = rectangle.getHeight() - (roundness * 2.0);
        VertexBufferObject.Builder vbo;
        DoubleList vertices;
        // center
        vertices = new ArrayDoubleList(12);
        int mode = addRectangle(vertices, rectangle.getX(), rectangle.getY(), width, height);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        if (texCoords) {
            vbo.texCoordDimension(TWO_D);
            vbos.add(buildVbo(vbo, NORMAL, RECTANGLE_TEX_COORDS.toArray(), color));
        } else {
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // top
        vertices = new ArrayDoubleList(12);
        mode = addRectangle(vertices, rectangle.getX(), rectangle.getY() + (height + roundness) / 2.0, width, roundness);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        // right
        vertices = new ArrayDoubleList(12);
        mode = addRectangle(vertices, rectangle.getX() + (width + roundness) / 2.0, rectangle.getY(), roundness, height);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        // bottom
        vertices = new ArrayDoubleList(12);
        mode = addRectangle(vertices, rectangle.getX(), rectangle.getY() - (height + roundness) / 2.0, width, roundness);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        // left
        vertices = new ArrayDoubleList(12);
        mode = addRectangle(vertices, rectangle.getX() - (width + roundness) / 2.0, rectangle.getY(), roundness, height);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        // top right arc
        vertices = new ArrayDoubleList((slices + 2) * 2);
        mode = addArc(vertices, rectangle.getX() + width / 2.0, rectangle.getY() + height / 2.0, roundness, 0.0, 90.0, slices);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        // bottom right arc
        vertices = new ArrayDoubleList((slices + 2) * 2);
        mode = addArc(vertices, rectangle.getX() + width / 2.0, rectangle.getY() - height / 2.0, roundness, 90.0, 180.0, slices);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        // bottom left arc
        vertices = new ArrayDoubleList((slices + 2) * 2);
        mode = addArc(vertices, rectangle.getX() - width / 2.0, rectangle.getY() - height / 2.0, roundness, 180.0, 270.0, slices);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        // top left arc
        vertices = new ArrayDoubleList((slices + 2) * 2);
        mode = addArc(vertices, rectangle.getX() - width / 2.0, rectangle.getY() + height / 2.0, roundness, 270.0, 360.0, slices);
        vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        // vbos
        return vbos;
    }

    /**
     * Create a circle using the supplied shape. This method is optimized to use
     * 360 degree reference points for drawing the circle.
     *
     * @param circle    the circle to draw
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException if shape is null
     */
    public static VertexBufferObject createShape(final Circle2D circle, final Color color, final boolean texCoords) {
        final DoubleList vertices = new ArrayDoubleList((CIRCLE_POINTS.length + 1) * 2);
        final int mode = addCircle(vertices, circle.getX(), circle.getY(), circle.getRadius());
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        if (texCoords) {
            vbo.texCoordDimension(TWO_D);
            return buildVbo(vbo, NORMAL, CIRCLE_TEX_COORDS.toArray(), color);
        } else {
            return buildVbo(vbo, NORMAL, null, color);
        }
    }

    /**
     * Create a circle using the supplied shape.
     *
     * @param circle    the circle to draw
     * @param slices    number of slices for the circle (must be at least 3)
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException     if shape is null
     * @throws IllegalArgumentException if slices is less than three
     */
    public static VertexBufferObject createShape(final Circle2D circle, final int slices, final Color color, final boolean texCoords) {
        if (slices < 3) {
            throw new IllegalArgumentException("slices");
        }
        final double radius = circle.getRadius();
        final DoubleList vertices = new ArrayDoubleList((slices + 1) * 2);
        final int mode = addCircle(vertices, circle.getX(), circle.getY(), radius, slices);
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        if (texCoords) {
            vbo.texCoordDimension(TWO_D);
            return buildVbo(vbo, NORMAL, circleTexCoords(slices).toArray(), color);
        } else {
            return buildVbo(vbo, NORMAL, null, color);
        }
    }

    /**
     * Create an arc using the supplied shape. This method is optimized to use
     * 360 degree reference points for drawing the arc.
     *
     * @param arc       the arc to draw
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException if shape is null
     */
    public static VertexBufferObject createShape(final Arc2D arc, final Color color, final boolean texCoords) {
        final double innerRadius = arc.getRadius() - (arc.getArcHeight() / 2.0);
        final double outerRadius = arc.getRadius() + (arc.getArcHeight() / 2.0);
        final double start = arc.getAngle() - (arc.getSector() / 2.0);
        final double end = arc.getAngle() + (arc.getSector() / 2.0);
        // create a polygon
        if (Double.compare(innerRadius, 0.0) == 0) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, arc.getX(), arc.getY(), outerRadius, start, end);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            if (texCoords) {
                vbo.texCoordDimension(TWO_D);
                return buildVbo(vbo, NORMAL, arcTexCoords(start, end).toArray(), color);
            } else {
                return buildVbo(vbo, NORMAL, null, color);
            }
        } else {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, arc.getX(), arc.getY(), innerRadius, outerRadius, start, end);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            return buildVbo(vbo, NORMAL, null, color);
        }
    }

    /**
     * Create an arc using the supplied shape.
     *
     * @param arc       the arc to draw
     * @param slices    number of slices for the arc (must be at least 2)
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException     if shape is null
     * @throws IllegalArgumentException if slices is less than 3
     */
    public static VertexBufferObject createShape(final Arc2D arc, final int slices, final Color color, final boolean texCoords) {
        if (slices < 3) {
            throw new IllegalArgumentException("slices");
        }
        final double innerRadius = arc.getRadius() - (arc.getHeight() / 2.0);
        final double outerRadius = arc.getRadius() + (arc.getHeight() / 2.0);
        final double start = Math.toRadians(arc.getAngle() - (arc.getSector() / 2.0));
        final double end = Math.toRadians(arc.getAngle() + (arc.getSector() / 2.0));
        // create a polygon
        if (Double.compare(innerRadius, 0.0) == 0) {
            final DoubleList vertices = new ArrayDoubleList((slices + 2) * 2);
            final int mode = addArc(vertices, arc.getX(), arc.getY(), outerRadius, start, end, slices);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            if (texCoords) {
                vbo.texCoordDimension(TWO_D);
                return buildVbo(vbo, NORMAL, arcTexCoords(start, end, slices).toArray(), color);
            } else {
                return buildVbo(vbo, NORMAL, null, color);
            }
        } else {
            final DoubleList vertices = new ArrayDoubleList((slices + 2) * 2);
            final int mode = addArc(vertices, arc.getX(), arc.getY(), innerRadius, outerRadius, start, end, slices);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray())
                    .texCoordDimension(TWO_D);
            return buildVbo(vbo, NORMAL, null, color);
        }
    }

    /**
     * Create a polygon using the supplied shape.
     *
     * @param polygon the polygon to draw
     * @param color   the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException if shape is null
     */
    public static List<VertexBufferObject> createShape(final Polygon2D polygon, final Color color) {
        final GLUtessellator tessellator = GLU.gluNewTess();
        GLU.gluTessNormal(tessellator, NORMAL[0], NORMAL[1], NORMAL[2]);
        GLU.gluTessProperty(tessellator, GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
        final TessellatorCallbackVbo callback = new TessellatorCallbackVbo(color);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_VERTEX, callback);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_BEGIN, callback);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_END, callback);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_ERROR, callback);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_COMBINE, callback);
        GLU.gluTessBeginPolygon(tessellator, null);
        for (final List<Point2D> points : polygon.getContours()) {
            GLU.gluTessBeginContour(tessellator);
            for (final Point2D point : points) {
                final double[] tess = new double[]{polygon.getX() + point.getX(), polygon.getY() + point.getY(), 0.0};
                GLU.gluTessVertex(tessellator, tess, 0, tess);
            }
            GLU.gluTessEndContour(tessellator);
        }
        GLU.gluTessEndPolygon(tessellator);
        // cleanup
        GLU.gluDeleteTess(tessellator);
        return callback.getVbos();
    }

    /**
     * Create a chart using the supplied shape.
     *
     * @param chart the chart to draw
     * @param color the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException if shape is null
     */
    public static List<VertexBufferObject> createShape(final Chart2D chart, final Color color) {
        final List<VertexBufferObject> vbos = new ArrayList<>();
        VertexBufferObject.Builder vbo;
        DoubleList vertices;
        switch (chart.getStyle()) {
            case NONE:
                // draw nothing
                break;
            case SCATTER:
                // draw a series of points
                vertices = new ArrayDoubleList(chart.getPoints().size() * 2);
                for (int i = 0; i < chart.getPoints().size(); i++) {
                    final Point2D point = chart.getPoints().get(i);
                    final double x = MathUtil.scale(point.getX(), chart.getMaximum().getX(), chart.getMinimum().getX(), 0.5, -0.5) + chart.getX();
                    final double y = MathUtil.scale(point.getY(), chart.getMaximum().getY(), chart.getMinimum().getY(), 0.5, -0.5) + chart.getY();
                    addPoint(vertices, x, y);
                }
                vbo = VertexBufferObject.Builder.construct()
                        .vertexDimension(TWO_D)
                        .mode(GL.GL_POINTS)
                        .vertices(vertices.toArray());
                vbos.add(buildVbo(vbo, null, null, color));
                break;
            case LINE:
                // draw a series of lines
                vertices = new ArrayDoubleList(chart.getPoints().size() * 2);
                for (int i = 0; i < chart.getPoints().size(); i++) {
                    final Point2D point = chart.getPoints().get(i);
                    final double x = MathUtil.scale(point.getX(), chart.getMaximum().getX(), chart.getMinimum().getX(), 0.5, -0.5) + chart.getX();
                    final double y = MathUtil.scale(point.getY(), chart.getMaximum().getY(), chart.getMinimum().getY(), 0.5, -0.5) + chart.getY();
                    addPoint(vertices, x, y);
                }
                vbo = VertexBufferObject.Builder.construct()
                        .vertexDimension(TWO_D)
                        .mode(GL.GL_LINE_STRIP)
                        .vertices(vertices.toArray());
                vbos.add(buildVbo(vbo, null, null, color));
                break;
            case AREA: {
                // draw a filled polygon
                vertices = new ArrayDoubleList(((chart.getPoints().size() - 1) * 6) * 2);
                final double[][] verts = new double[4][2];
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
                    addPolygon(vertices, verts);
                }
                vbo = VertexBufferObject.Builder.construct()
                        .vertexDimension(TWO_D)
                        .mode(GL.GL_TRIANGLES)
                        .vertices(vertices.toArray());
                vbos.add(buildVbo(vbo, null, null, color));
                break;
            }
            case BAR: {
                // draw a series of bars
                vertices = new ArrayDoubleList((chart.getPoints().size() * 6) * 2);
                final double[][] verts = new double[4][2];
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
                        // add polygon
                        addPolygon(vertices, verts);
                    } else {
                        // draw polygon with color
                        final Color c = Color.fromInt((int) point.getX());
                        final DoubleList v = new ArrayDoubleList(12);
                        final int m = addPolygon(v, verts);
                        vbo = VertexBufferObject.Builder.construct()
                                .vertexDimension(TWO_D)
                                .mode(m)
                                .vertices(v.toArray());
                        vbos.add(buildVbo(vbo, null, null, c));
                    }
                    // increment x
                    x += (width + padding);
                }
                if (!vertices.isEmpty()) {
                    vbo = VertexBufferObject.Builder.construct()
                            .vertexDimension(TWO_D)
                            .mode(GL.GL_TRIANGLES)
                            .vertices(vertices.toArray());
                    vbos.add(buildVbo(vbo, null, null, color));
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
                        final DoubleList v = new ArrayDoubleList();
                        final int m = addArc(v, chart.getX(), chart.getY(), 0.5, a, a + s);
                        vbo = VertexBufferObject.Builder.construct()
                                .vertexDimension(TWO_D)
                                .mode(m)
                                .vertices(v.toArray());
                        vbos.add(buildVbo(vbo, null, null, color));
                    } else {
                        // draw polygon with color
                        final Color c = Color.fromInt((int) point.getX());
                        final DoubleList v = new ArrayDoubleList();
                        final int m = addArc(v, chart.getX(), chart.getY(), 0.5, a, a + s);
                        vbo = VertexBufferObject.Builder.construct()
                                .vertexDimension(TWO_D)
                                .mode(m)
                                .vertices(v.toArray());
                        vbos.add(buildVbo(vbo, null, null, c));
                    }
                    // incrememnt arc
                    a += s;
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unhandled enum passed to control statement: " + chart.getStyle());
        }
        return vbos;
    }

    /**
     * Creates a border around a point.
     *
     * @param point     the point to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Point2D point, final Border border, final double thickness, final Color color) {
        // draw border around a point
        return createBorder(new Rectangle2D(point.getX(), point.getY(), 1.0, 1.0), border, thickness, color);
    }

    /**
     * Creates a border around a rectangle.
     *
     * @param rectangle the rectangle to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Rectangle2D rectangle, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
        }
        final List<VertexBufferObject> vbos = new ArrayList<>();
        final List<DoubleList> vertices = new ArrayList<>();
        final int[] modes = addRectangleBorder(vertices, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), border, thickness);
        for (int i = 0; i < vertices.size(); i++) {
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(modes[i])
                    .vertices(vertices.get(i).toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        return vbos;
    }

    /**
     * Create a border around a rounded rectangle.
     *
     * @param rectangle the rectangle to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final RoundedRectangle2D rectangle, final Border border, final double thickness, final Color color) {
        final List<VertexBufferObject> vbos = new ArrayList<>();
        final double roundness = rectangle.getRoundness();
        final double width = rectangle.getWidth() - (roundness * 2.0);
        final double height = rectangle.getHeight() - (roundness * 2.0);
        // top
        if (border.isTop()) {
            final DoubleList vertices = new ArrayDoubleList(12);
            final int mode = addRectangle(vertices, rectangle.getX(), rectangle.getY() + (height + roundness + roundness + thickness) / 2.0, width, thickness);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // right
        if (border.isRight()) {
            final DoubleList vertices = new ArrayDoubleList(12);
            final int mode = addRectangle(vertices, rectangle.getX() + (width + roundness + roundness + thickness) / 2.0, rectangle.getY(), thickness, height);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // bottom
        if (border.isBottom()) {
            final DoubleList vertices = new ArrayDoubleList(12);
            final int mode = addRectangle(vertices, rectangle.getX(), rectangle.getY() - (height + roundness + roundness + thickness) / 2.0, width, thickness);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // left
        if (border.isLeft()) {
            final DoubleList vertices = new ArrayDoubleList(12);
            final int mode = addRectangle(vertices, rectangle.getX() - (width + roundness + roundness + thickness) / 2.0, rectangle.getY(), thickness, height);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // top right arc
        if (border.isTop() && border.isRight()) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, rectangle.getX() + width / 2.0, rectangle.getY() + height / 2.0, roundness, roundness + thickness, 0.0, 90.0);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // bottom right arc
        if (border.isBottom() && border.isRight()) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, rectangle.getX() + width / 2.0, rectangle.getY() - height / 2.0, roundness, roundness + thickness, 90.0, 180.0);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // bottom left arc
        if (border.isBottom() && border.isLeft()) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, rectangle.getX() - width / 2.0, rectangle.getY() - height / 2.0, roundness, roundness + thickness, 180.0, 270.0);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // top left arc
        if (border.isTop() && border.isLeft()) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, rectangle.getX() - width / 2.0, rectangle.getY() + height / 2.0, roundness, roundness + thickness, 270.0, 360.0);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(mode)
                    .vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        return vbos;
    }

    /**
     * Creates a border around text.
     *
     * @param text      the text to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Text2D text, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
        }
        final List<VertexBufferObject> vbos = new ArrayList<>();
        final List<DoubleList> vertices = new ArrayList<>();
        final int[] modes = addRectangleBorder(vertices, text.getX(), text.getY(), text.getWidth(), text.getHeight(), border, thickness);
        for (int i = 0; i < vertices.size(); i++) {
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                    .vertexDimension(TWO_D)
                    .mode(modes[i])
                    .vertices(vertices.get(i).toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        return vbos;
    }

    /**
     * Creates a border around a chart.
     *
     * @param chart     the chart to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Chart2D chart, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
        }
        switch (chart.getStyle()) {
            case NONE:
            case SCATTER:
            case LINE:
            case AREA:
            case BAR: {
                final List<VertexBufferObject> vbos = new ArrayList<>();
                final List<DoubleList> vertices = new ArrayList<>();
                final int[] modes = addRectangleBorder(vertices, chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight(), border, thickness);
                for (int i = 0; i < vertices.size(); i++) {
                    final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                            .vertexDimension(TWO_D)
                            .mode(modes[i])
                            .vertices(vertices.get(i).toArray());
                    vbos.add(buildVbo(vbo, NORMAL, null, color));
                }
                return vbos;
            }
            case PIE: {
                final List<VertexBufferObject> vbos = new ArrayList<>();
                final DoubleList vertices = new ArrayDoubleList();
                final int mode = addCircleBorder(vertices, chart.getX(), chart.getY(), 0.5, border, thickness);
                final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                        .vertexDimension(TWO_D)
                        .mode(mode)
                        .vertices(vertices.toArray());
                vbos.add(buildVbo(vbo, NORMAL, null, color));
                return vbos;
            }
            default:
                throw new IllegalArgumentException("Unhandled enum passed to switch statement: " + chart.getStyle());
        }
    }

    /**
     * Creates a border around a circle.
     *
     * @param circle    the circle to draw
     * @param border    the border, any border is treated as all borders
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static VertexBufferObject createBorder(final Circle2D circle, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return null;
        }
        final DoubleList vertices = new ArrayDoubleList();
        final int mode = addCircleBorder(vertices, circle.getX(), circle.getY(), circle.getRadius(), border, thickness);
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray());
        return buildVbo(vbo, NORMAL, null, color);
    }

    /**
     * Creates a border around an arc.
     *
     * @param arc       the arc to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Arc2D arc, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
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
        // draw the border as multiple arcs
        final List<VertexBufferObject> vbos = new ArrayList<>();
        if (border.containsBorder(Border.ALL)) {
            // top
            vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), outerRadius, outerWidth, thickness), color, false));
            // bottom
            vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), innerRadius, innerWidth, thickness), color, false));
            // left
            vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, thickness + arc.getArcHeight() + thickness), color, false));
            // right
            vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, thickness + arc.getArcHeight() + thickness), color, false));
        } else {
            // top
            if (border.isTop()) {
                vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), outerRadius, outerWidth, thickness), color, false));
            }
            // bottom
            if (border.isBottom()) {
                vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), innerRadius, innerWidth, thickness), color, false));
            }
            // left
            if (border.isLeft()) {
                vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, arc.getArcHeight()), color, false));
            }
            // right
            if (border.isRight()) {
                vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, arc.getArcHeight()), color, false));
            }
            // top left
            if (border.isTop() || border.isLeft()) {
                vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), outerRadius, thickness, thickness), color, false));
            }
            // top right
            if (border.isTop() || border.isRight()) {
                vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), outerRadius, thickness, thickness), color, false));
            }
            // bottom left
            if (border.isBottom() || border.isLeft()) {
                vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), innerRadius, thickness, thickness), color, false));
            }
            // bottom right
            if (border.isBottom() || border.isRight()) {
                vbos.add(createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), innerRadius, thickness, thickness), color, false));
            }
        }
        return vbos;
    }

}
