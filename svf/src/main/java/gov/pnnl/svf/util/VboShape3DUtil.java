package gov.pnnl.svf.util;

import com.jogamp.opengl.GL;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.BezierCurveEvaluator;
import gov.pnnl.svf.geometry.Path3D;
import gov.pnnl.svf.geometry.PathVector3D;
import gov.pnnl.svf.geometry.Point3D;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.vbo.VertexBufferObject;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Utility class containing all of the 3D VBO shape methods.
 *
 * @author Arthur Bleeker
 */
public class VboShape3DUtil extends VboUtil {

    private static final Logger logger = Logger.getLogger(VboShape3DUtil.class.getName());

    /**
     * Constructor kept private for static utility class
     */
    protected VboShape3DUtil() {
    }

    /**
     * Create a point using the supplied shape.
     *
     * @param point the point to draw
     * @param color the optional color
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException if gl or shape are null
     */
    public static VertexBufferObject createShape(final Point3D point, final Color color) {
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .mode(GL.GL_POINTS)
                .vertexDimension(THREE_D)
                .vertices(new double[]{point.getX(), point.getY(), point.getZ()});
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
     * @throws NullPointerException if gl or path are null
     */
    public static VertexBufferObject createShape(final Path3D path, final Color color) {
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(THREE_D);
        final List<Point3D> points = path.getPoints();
        final DoubleList vertices;
        // draw a path
        switch (path.getStyle()) {
            case NONE:
                vertices = new ArrayDoubleList(points.size() * 3);
                for (int i = 0; i < points.size(); i++) {
                    final Point3D point = points.get(i);
                    vertices.add(point.getX() + path.getX());
                    vertices.add(point.getY() + path.getY());
                    vertices.add(point.getZ() + path.getZ());
                }
                break;
            case POINTS:
                vbo.mode(GL.GL_POINTS);
                vertices = new ArrayDoubleList(points.size() * 3);
                for (int i = 0; i < points.size(); i++) {
                    final Point3D point = points.get(i);
                    vertices.add(point.getX() + path.getX());
                    vertices.add(point.getY() + path.getY());
                    vertices.add(point.getZ() + path.getZ());
                }
                break;
            case NURBS:
                throw new IllegalArgumentException("NURBS path type is not supported for VBO.");
            case BEZIER:
                // create a series of bezier curves
                vbo.mode(GL.GL_LINE_STRIP);
                final BezierCurveEvaluator evaluator = new BezierCurveEvaluator(3);
                final List<double[]> curves = createCurves3D(points);
                vertices = new ArrayDoubleList((curves.size() * (1 + BEZIER_EVALUATORS - 1 + 1)) * 3);
                final double[] vertex = new double[3];
                for (int c = 0; c < curves.size(); c++) {
                    final double[] curve = curves.get(c);
                    // draw straight sections of lines to simulate a curve
                    vertices.add(curve[0] + path.getX());
                    vertices.add(curve[1] + path.getY());
                    vertices.add(curve[2] + path.getZ());
                    for (int i = 1; i < BEZIER_EVALUATORS; i++) {
                        evaluator.evaluate(curve, (double) i / (double) BEZIER_EVALUATORS, vertex);
                        vertices.add(vertex[0] + path.getX());
                        vertices.add(vertex[1] + path.getY());
                        vertices.add(vertex[2] + path.getZ());
                    }
                    vertices.add(curve[9] + path.getX());
                    vertices.add(curve[10] + path.getY());
                    vertices.add(curve[11] + path.getZ());
                }
                break;
            default:
                throw new IllegalArgumentException("Unhandled enum passed to control statement: " + path.getStyle());
        }
        vbo.vertices(vertices.toArray());
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
     * @throws NullPointerException if gl or path are null
     */
    public static VertexBufferObject createShape(final PathVector3D path, final Color color) {
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(THREE_D);
        final List<Vector3D> points = path.getPoints();
        final DoubleList vertices;
        // draw a path
        switch (path.getStyle()) {
            case NONE:
                vertices = new ArrayDoubleList(points.size() * 3);
                for (int i = 0; i < points.size(); i++) {
                    final Vector3D point = points.get(i);
                    vertices.add(point.getX() + path.getX());
                    vertices.add(point.getY() + path.getY());
                    vertices.add(point.getZ() + path.getZ());
                }
                break;
            case POINTS:
                vbo.mode(GL.GL_POINTS);
                vertices = new ArrayDoubleList(points.size() * 3);
                for (int i = 0; i < points.size(); i++) {
                    final Vector3D point = points.get(i);
                    vertices.add(point.getX() + path.getX());
                    vertices.add(point.getY() + path.getY());
                    vertices.add(point.getZ() + path.getZ());
                }
                break;
            case NURBS:
                throw new IllegalArgumentException("NURBS path type is not supported for VBO.");
            case BEZIER:
                // create a series of bezier curves
                vbo.mode(GL.GL_LINE_STRIP);
                final BezierCurveEvaluator evaluator = new BezierCurveEvaluator(3);
                final List<double[]> curves = createCurvesVector3D(points);
                vertices = new ArrayDoubleList((curves.size() * (1 + BEZIER_EVALUATORS - 1 + 1)) * 3);
                final double[] vertex = new double[3];
                for (int c = 0; c < curves.size(); c++) {
                    final double[] curve = curves.get(c);
                    // draw straight sections of lines to simulate a curve
                    vertices.add(curve[0] + path.getX());
                    vertices.add(curve[1] + path.getY());
                    vertices.add(curve[2] + path.getZ());
                    for (int i = 1; i < BEZIER_EVALUATORS; i++) {
                        evaluator.evaluate(curve, (double) i / (double) BEZIER_EVALUATORS, vertex);
                        vertices.add(vertex[0] + path.getX());
                        vertices.add(vertex[1] + path.getY());
                        vertices.add(vertex[2] + path.getZ());
                    }
                    vertices.add(curve[9] + path.getX());
                    vertices.add(curve[10] + path.getY());
                    vertices.add(curve[11] + path.getZ());
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
    public static VertexBufferObject createShape(final Text3D text, final Color color, final boolean texCoords) {
        final DoubleList vertices = new ArrayDoubleList(18);
        final int mode = addRectangle(vertices, text.getX(), text.getY(), text.getWidth(), text.getHeight());
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(THREE_D)
                .mode(mode)
                .vertices(vertices.toArray());
        if (texCoords) {
            vbo.texCoordDimension(TWO_D);
            return buildVbo(vbo, NORMAL, RECTANGLE_TEX_COORDS.toArray(), color);
        } else {
            return buildVbo(vbo, NORMAL, null, color);
        }
    }

}
