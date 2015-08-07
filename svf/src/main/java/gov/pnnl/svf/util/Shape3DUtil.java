package gov.pnnl.svf.util;

import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.util.BezierCurveEvaluator;
import gov.pnnl.svf.geometry.Cuboid3D;
import gov.pnnl.svf.geometry.Path3D;
import gov.pnnl.svf.geometry.PathVector3D;
import gov.pnnl.svf.geometry.Point3D;
import gov.pnnl.svf.geometry.Sphere3D;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.geometry.Volume3D;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLUnurbs;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Utility class containing all of the 3D shape methods.
 *
 * @author Arthur Bleeker
 */
public class Shape3DUtil extends ShapeUtil {

    private static final Logger logger = Logger.getLogger(Shape3DUtil.class.getName());

    /**
     * Constructor kept private for static utility class
     */
    protected Shape3DUtil() {
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
    public static int drawShape(final GL2 gl, final Point3D point) {
        // draw a point
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex3d(point.getX(), point.getY(), point.getZ());
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
    public static int drawShape(final GL2 gl, final Path3D path) {
        final List<Point3D> points = path.getPoints();
        // draw a path
        int vertices = 0;
        switch (path.getStyle()) {
            case NONE:
                gl.glBegin(GL.GL_LINE_STRIP);
                for (final Point3D point : points) {
                    gl.glVertex3d(point.getX() + path.getX(), point.getY() + path.getY(), point.getZ() + path.getZ());
                    vertices++;
                }
                gl.glEnd();
                break;
            case POINTS:
                gl.glBegin(GL.GL_POINTS);
                for (final Point3D point : points) {
                    gl.glVertex3d(point.getX() + path.getX(), point.getY() + path.getY(), point.getZ() + path.getZ());
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
                    final Point3D point = points.get(i);
                    control[i * NURBS_DEGREE] = (float) (point.getX() + path.getX());
                    control[i * NURBS_DEGREE + 1] = (float) (point.getY() + path.getY());
                    control[i * NURBS_DEGREE + 2] = (float) (point.getZ() + path.getZ());
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
                final BezierCurveEvaluator evaluator = new BezierCurveEvaluator(3);
                final List<double[]> curves = createCurves3D(points);
                for (final double[] curve : curves) {
                    // draw straight sections of lines to simulate a curve
                    final double[] vertex = new double[3];
                    gl.glBegin(GL.GL_LINE_STRIP);
                    gl.glVertex3d(curve[0] + path.getX(), curve[1] + path.getY(), curve[2] + path.getZ());
                    for (int i = 1; i < BEZIER_EVALUATORS; i++) {
                        evaluator.evaluate(curve, (double) i / (double) BEZIER_EVALUATORS, vertex);
                        gl.glVertex3d(vertex[0] + path.getX(), vertex[1] + path.getY(), vertex[2] + path.getZ());
                    }
                    gl.glVertex3d(curve[9] + path.getX(), curve[10] + path.getY(), curve[11] + path.getZ());
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
     * Draw a path using the supplied shape.
     *
     * @param gl   reference to the current GL
     * @param path the path to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or path are null
     */
    public static int drawShape(final GL2 gl, final PathVector3D path) {
        final List<Vector3D> points = path.getPoints();
        // draw a path
        int vertices = 0;
        switch (path.getStyle()) {
            case NONE:
                gl.glBegin(GL.GL_LINE_STRIP);
                for (final Vector3D point : points) {
                    gl.glVertex3d(point.getX() + path.getX(), point.getY() + path.getY(), point.getZ());
                    vertices++;
                }
                gl.glEnd();
                break;
            case POINTS:
                gl.glBegin(GL.GL_POINTS);
                for (final Vector3D point : points) {
                    gl.glVertex3d(point.getX() + path.getX(), point.getY() + path.getY(), point.getZ());
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
                    final Vector3D point = points.get(i);
                    control[i * NURBS_DEGREE] = (float) (point.getX() + path.getX());
                    control[i * NURBS_DEGREE + 1] = (float) (point.getY() + path.getY());
                    control[i * NURBS_DEGREE + 2] = (float) (point.getZ() + path.getZ());
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
                final BezierCurveEvaluator evaluator = new BezierCurveEvaluator(3);
                final List<double[]> curves = createCurvesVector3D(points);
                for (final double[] curve : curves) {
                    // draw straight sections of lines to simulate a curve
                    final double[] vertex = new double[3];
                    gl.glBegin(GL.GL_LINE_STRIP);
                    gl.glVertex3d(curve[0] + path.getX(), curve[1] + path.getY(), curve[2] + path.getZ());
                    for (int i = 1; i < BEZIER_EVALUATORS; i++) {
                        evaluator.evaluate(curve, (double) i / (double) BEZIER_EVALUATORS, vertex);
                        gl.glVertex3d(vertex[0] + path.getX(), vertex[1] + path.getY(), vertex[2] + path.getZ());
                    }
                    gl.glVertex3d(curve[9] + path.getX(), curve[10] + path.getY(), curve[11] + path.getZ());
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
    public static int drawShape(final GL2 gl, final Text3D text) {
        GeometryUtil.drawRectangle(gl, text.getX(), text.getY(), text.getWidth(), text.getHeight());
        return 4;
    }

    /**
     * Draw a cuboid using the supplied shape.
     *
     * @param gl     reference to the current GL
     * @param cuboid the cuboid to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or cuboid are null
     */
    public static int drawShape(final GL2 gl, final Cuboid3D cuboid) {
        if (cuboid.equals(Cuboid3D.ONE)) {
            GLUT.glutSolidCube(1.0f);
        } else {
            gl.glPushMatrix();
            gl.glScaled(cuboid.getWidth(), cuboid.getHeight(), cuboid.getDepth());
            gl.glTranslated(cuboid.getX(), cuboid.getY(), cuboid.getZ());
            GLUT.glutSolidCube(1.0f);
            gl.glPopMatrix();
        }
        return 8;
    }

    /**
     * Draw a volume using the supplied shape.
     *
     * @param gl     reference to the current GL
     * @param volume the volume to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or volume are null
     */
    public static int drawShape(final GL2 gl, final Volume3D volume) {
        // plane with 4 corners centered at 0, 0
        // 3 sets of a series of planes that go front to back, left to right,
        // and top to bottom
        final double[][] verts = new double[][]{ // back, front
            {-0.5, -0.5, 0.0}, {-0.5, 0.5, 0.0}, {0.5, 0.5, 0.0}, {0.5, -0.5, 0.0}, // right, let
            {0.0, -0.5, 0.5}, {0.0, 0.5, 0.5}, {0.0, 0.5, -0.5}, {0.0, -0.5, -0.5}, // top, bottom
            {-0.5, 0.0, 0.5}, {-0.5, 0.0, -0.5}, {0.5, 0.0, -0.5}, {0.5, 0.0, 0.5}};
        // create texture coordinates that span the entire plane
        // 3 sets of coords for a series of planes that go front to back, left
        // to right, and top to bottom
        final double[][] texCoords = new double[][]{ // back, front
            {0.0, 1.0, 1.0}, {0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}, // right, let
            {1.0, 1.0, 0.0}, {1.0, 0.0, 0.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}, // top, bottom
            {0.0, 0.0, 0.0}, {0.0, 0.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 0.0, 0.0}};
        // find the scale for the actor to determine the correct number
        // of slices to draw for each direction
        final float slicesX = (float) volume.getWidth() * volume.getSlices();
        final float slicesY = (float) volume.getHeight() * volume.getSlices();
        final float slicesZ = (float) volume.getDepth() * volume.getSlices();
        // find the direction that the camera is looking
        final double[] modelview = new double[12];
        gl.glGetDoublev(GLMatrixFunc.GL_MODELVIEW_MATRIX, modelview, 0);
        final Direction direction = findCameraDirection(new Vector3D(modelview[2], modelview[6], modelview[10]));
        switch (direction) {
            case BACK:
                // back
                for (int i = 0; i < (int) slicesZ + 1; i++) {
                    final float fraction = i / slicesZ;
                    for (int j = 0; j < 4; j++) {
                        verts[j][Z] = -0.5f + fraction;
                        texCoords[j][Z] = 1.0f - fraction;
                    }
                    GeometryUtil.drawPolygon(gl, verts, texCoords, NORMALS[direction.ordinal()], 3, 2, 1, 0);
                }
                return (int) slicesZ * 4;
            case FRONT:
                // front
                for (int i = 0; i < (int) slicesZ + 1; i++) {
                    final float fraction = i / slicesZ;
                    for (int j = 0; j < 4; j++) {
                        verts[j][Z] = 0.5f - fraction;
                        texCoords[j][Z] = fraction;
                    }
                    GeometryUtil.drawPolygon(gl, verts, texCoords, NORMALS[direction.ordinal()], 0, 1, 2, 3);
                }
                return (int) slicesZ * 4;
            case RIGHT:
                // right
                for (int i = 0; i < (int) slicesX + 1; i++) {
                    final float fraction = i / slicesX;
                    for (int j = 4; j < 8; j++) {
                        verts[j][X] = -0.5f + fraction;
                        texCoords[j][X] = fraction;
                    }
                    GeometryUtil.drawPolygon(gl, verts, texCoords, NORMALS[direction.ordinal()], 7, 6, 5, 4);
                }
                return (int) slicesX * 4;
            case LEFT:
                // left
                for (int i = 0; i < (int) slicesX + 1; i++) {
                    final float fraction = i / slicesX;
                    for (int j = 4; j < 8; j++) {
                        verts[j][X] = 0.5f - fraction;
                        texCoords[j][X] = 1.0f - fraction;
                    }
                    GeometryUtil.drawPolygon(gl, verts, texCoords, NORMALS[direction.ordinal()], 4, 5, 6, 7);
                }
                return (int) slicesX * 4;
            case TOP:
                // top
                for (int i = 0; i < (int) slicesY + 1; i++) {
                    final float fraction = i / slicesY;
                    for (int j = 8; j < 12; j++) {
                        verts[j][Y] = -0.5f + fraction;
                        texCoords[j][Y] = 1.0f - fraction;
                    }
                    GeometryUtil.drawPolygon(gl, verts, texCoords, NORMALS[direction.ordinal()], 11, 10, 9, 8);
                }
                return (int) slicesY * 4;
            case BOTTOM:
                // bottom
                for (int i = 0; i < (int) slicesY + 1; i++) {
                    final float fraction = i / slicesY;
                    for (int j = 8; j < 12; j++) {
                        verts[j][Y] = 0.5f - fraction;
                        texCoords[j][Y] = fraction;
                    }
                    GeometryUtil.drawPolygon(gl, verts, texCoords, NORMALS[direction.ordinal()], 8, 9, 10, 11);
                }
                return (int) slicesY * 4;
            default:
                logger.log(Level.WARNING, "Unhandled value in switch statement: {0}", direction);
                return 0;
        }
    }

    /**
     * Draw a sphere using the supplied shape.
     *
     * @param gl     reference to the current GL
     * @param sphere the sphere to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or sphere are null
     */
    public static int drawShape(final GL2 gl, final Sphere3D sphere) {
        return drawShape(gl, sphere, 360);
    }

    /**
     * Draw a sphere using the supplied shape.
     *
     * @param gl     reference to the current GL
     * @param sphere the sphere to draw
     * @param slices number of slices for the sphere (must be at least 3)
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException if gl or sphere are null
     */
    public static int drawShape(final GL2 gl, final Sphere3D sphere, final int slices) {
        if (sphere == null) {
            throw new NullPointerException("sphere");
        }
        if (slices < 4) {
            throw new IllegalArgumentException("slices");
        }
        gl.glPushMatrix();
        gl.glTranslated(sphere.getX(), sphere.getY(), sphere.getZ());
        GLUT.glutSolidSphere(sphere.getRadius(), slices, slices);
        gl.glPopMatrix();
        return slices * slices + 2;
    }

    /**
     * Draws a border around a rectangle.
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
    public static int drawBorder(final GL2 gl, final Text3D text, final Border border, final double thickness) {
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

}
