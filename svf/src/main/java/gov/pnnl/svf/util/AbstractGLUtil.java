package gov.pnnl.svf.util;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.util.gl2.GLUT;
import gov.pnnl.svf.constant.NurbsConst;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.constant.CartesianConst;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Point3D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Abstract utility class that has common methods and constants for GL drawing.
 *
 * @author Arthur Bleeker
 */
abstract class AbstractGLUtil implements NurbsConst, CartesianConst {

    /**
     * Represents a general direction.
     * <p/>
     * directions {back, front, right, left, top, bottom}
     */
    protected enum Direction {

        BACK,
        FRONT,
        RIGHT,
        LEFT,
        TOP,
        BOTTOM
    }
    protected static final int BEZIER_EVALUATORS = 10;
    protected static final double BEZIER_SMOOTHNESS = 0.2;
    protected static final double[][] TEX_COORDS = new double[][]{{0.0, 1.0}, {0.0, 0.0}, {1.0, 0.0}, {1.0, 1.0}};
    protected static final double[] NORMAL = new double[]{0.0, 0.0, 1.0};
    protected static final double[][] CIRCLE_POINTS = new double[360 + 1][2];
    protected static final int LEFT_BOTTOM = 0;
    protected static final int LEFT_TOP = 1;
    protected static final int RIGHT_TOP = 2;
    protected static final int RIGHT_BOTTOM = 3;
    protected static final Vector3D[] DIRECTIONS = {new Vector3D(0.0f, 0.0f, 1.0f), new Vector3D(0.0f, 0.0f, -1.0f), new Vector3D(1.0f, 0.0f, 0.0f), new Vector3D(-1.0f, 0.0f, 0.0f), new Vector3D(0.0f, 1.0f, 0.0f), new Vector3D(0.0f, -1.0f, 0.0f)};
    protected static final double[][] NORMALS = {new double[]{0.0f, 0.0f, 1.0f}, new double[]{0.0f, 0.0f, -1.0f}, new double[]{1.0f, 0.0f, 0.0f}, new double[]{-1.0f, 0.0f, 0.0}, new double[]{0.0f, 1.0f, 0.0f}, new double[]{0.0f, -1.0f, 0.0f}};
    protected static final GLUT GLUT = new GLUT();

    static {
        for (int a = 0; a < 360 + 1; a++) {
            CIRCLE_POINTS[a][X] = Math.sin(Math.toRadians(a));
            CIRCLE_POINTS[a][Y] = Math.cos(Math.toRadians(a));
        }
    }

    /**
     * Constructor kept private for util pattern
     */
    protected AbstractGLUtil() {
    }

    /**
     * Push this color to the gl stack.
     *
     * @param gl    reference to gl
     * @param color reference to color or null
     */
    public static void pushColor(final GL2 gl, final Color color) {
        if (color != null) {
            gl.glPushAttrib(GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);
            final float[] c = color.toRgbaArray();
            gl.glColor4fv(c, 0);
            gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, c, 0);
        }
    }

    /**
     * Pop this color from the gl stack.
     *
     * @param gl    reference to gl
     * @param color reference to color or null
     */
    public static void popColor(final GL2 gl, final Color color) {
        if (color != null) {
            gl.glPopAttrib();
        }
    }

    /**
     * Push this color to the gl stack.
     *
     * @param gl     reference to gl
     * @param color  reference to color or null
     * @param offset the index offset for the color
     */
    public static void pushColor(final GL2 gl, final float[] color, final int offset) {
        if (color != null) {
            gl.glPushAttrib(GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);
            gl.glColor4fv(color, offset);
            gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, color, offset);
        }
    }

    /**
     * Pop this color from the gl stack.
     *
     * @param gl     reference to gl
     * @param color  reference to color or null
     * @param offset the index offset for the color
     */
    public static void popColor(final GL2 gl, final float[] color, final int offset) {
        if (color != null) {
            gl.glPopAttrib();
        }
    }

    /**
     * Create a list of Bezier curve points given a list of points.
     *
     * @param points the list of points
     *
     * @return a list of Bezier curve points
     */
    protected static List<double[]> createCurves2D(final List<Point2D> points) {
        // create the list of bezier curves which consist of four control
        // points:
        // 1st point, 1st control, 2nd control, 2nd point
        final List<double[]> curves = new ArrayList<>();
        if (points.size() >= 2) {
            for (int i = 0; i < (points.size() - 1); i++) {
                final Point2D index = ShapeUtil.getControlPoint2D(points, i);
                final Point2D iPlusOne = ShapeUtil.getControlPoint2D(points, i + 1);
                final Point2D iPlusTwo = ShapeUtil.getControlPoint2D(points, i + 2);
                final Point2D iPlusThree = ShapeUtil.getControlPoint2D(points, i + 3);
                curves.add(new double[]{iPlusOne.getX(), iPlusOne.getY(), ((-BEZIER_SMOOTHNESS * index.getX()) / 3.0) + iPlusOne.getX() + ((BEZIER_SMOOTHNESS * iPlusTwo.getX()) / 3.0), ((-BEZIER_SMOOTHNESS * index.getY()) / 3.0) + iPlusOne.getY() + ((BEZIER_SMOOTHNESS * iPlusTwo.getY()) / 3.0), (((BEZIER_SMOOTHNESS * iPlusOne.getX()) / 3.0) + iPlusTwo.getX()) - ((BEZIER_SMOOTHNESS * iPlusThree.getX()) / 3.0), (((BEZIER_SMOOTHNESS * iPlusOne.getY()) / 3.0) + iPlusTwo.getY()) - ((BEZIER_SMOOTHNESS * iPlusThree.getY()) / 3.0), iPlusTwo.getX(), iPlusTwo.getY()});
            }
        }
        return curves;
    }

    /**
     * Create a list of Bezier curve points given a list of points.
     *
     * @param points the list of points
     *
     * @return a list of Bezier curve points
     */
    protected static List<double[]> createCurves3D(final List<Point3D> points) {
        // create the list of bezier curves which consist of four control
        // points:
        // 1st point, 1st control, 2nd control, 2nd point
        final List<double[]> curves = new ArrayList<>();
        if (points.size() >= 2) {
            for (int i = 0; i < (points.size() - 1); i++) {
                final Point3D index = ShapeUtil.getControlPoint3D(points, i);
                final Point3D iPlusOne = ShapeUtil.getControlPoint3D(points, i + 1);
                final Point3D iPlusTwo = ShapeUtil.getControlPoint3D(points, i + 2);
                final Point3D iPlusThree = ShapeUtil.getControlPoint3D(points, i + 3);
                curves.add(new double[]{iPlusOne.getX(), iPlusOne.getY(), iPlusOne.getZ(), ((-BEZIER_SMOOTHNESS * index.getX()) / 3.0) + iPlusOne.getX() + ((BEZIER_SMOOTHNESS * iPlusTwo.getX()) / 3.0), ((-BEZIER_SMOOTHNESS * index.getY()) / 3.0) + iPlusOne.getY() + ((BEZIER_SMOOTHNESS * iPlusTwo.getY()) / 3.0), ((-BEZIER_SMOOTHNESS * index.getZ()) / 3.0) + iPlusOne.getZ() + ((BEZIER_SMOOTHNESS * iPlusTwo.getZ()) / 3.0), (((BEZIER_SMOOTHNESS * iPlusOne.getX()) / 3.0) + iPlusTwo.getX()) - ((BEZIER_SMOOTHNESS * iPlusThree.getX()) / 3.0), (((BEZIER_SMOOTHNESS * iPlusOne.getY()) / 3.0) + iPlusTwo.getY()) - ((BEZIER_SMOOTHNESS * iPlusThree.getY()) / 3.0), (((BEZIER_SMOOTHNESS * iPlusOne.getZ()) / 3.0) + iPlusTwo.getZ()) - ((BEZIER_SMOOTHNESS * iPlusThree.getZ()) / 3.0), iPlusTwo.getX(), iPlusTwo.getY(), iPlusTwo.getZ()});
            }
        }
        return curves;
    }

    /**
     * Create a list of Bezier curve points given a list of points.
     *
     * @param points the list of points
     *
     * @return a list of Bezier curve points
     */
    protected static List<double[]> createCurvesVector3D(final List<Vector3D> points) {
        // create the list of bezier curves which consist of four control
        // points:
        // 1st point, 1st control, 2nd control, 2nd point
        final List<double[]> curves = new ArrayList<>();
        if (points.size() >= 2) {
            for (int i = 0; i < (points.size() - 1); i++) {
                final Vector3D index = ShapeUtil.getControlVector3D(points, i);
                final Vector3D iPlusOne = ShapeUtil.getControlVector3D(points, i + 1);
                final Vector3D iPlusTwo = ShapeUtil.getControlVector3D(points, i + 2);
                final Vector3D iPlusThree = ShapeUtil.getControlVector3D(points, i + 3);
                curves.add(new double[]{iPlusOne.getX(), iPlusOne.getY(), iPlusOne.getZ(), ((-BEZIER_SMOOTHNESS * index.getX()) / 3.0) + iPlusOne.getX() + ((BEZIER_SMOOTHNESS * iPlusTwo.getX()) / 3.0), ((-BEZIER_SMOOTHNESS * index.getY()) / 3.0) + iPlusOne.getY() + ((BEZIER_SMOOTHNESS * iPlusTwo.getY()) / 3.0), ((-BEZIER_SMOOTHNESS * index.getZ()) / 3.0) + iPlusOne.getZ() + ((BEZIER_SMOOTHNESS * iPlusTwo.getZ()) / 3.0), (((BEZIER_SMOOTHNESS * iPlusOne.getX()) / 3.0) + iPlusTwo.getX()) - ((BEZIER_SMOOTHNESS * iPlusThree.getX()) / 3.0), (((BEZIER_SMOOTHNESS * iPlusOne.getY()) / 3.0) + iPlusTwo.getY()) - ((BEZIER_SMOOTHNESS * iPlusThree.getY()) / 3.0), (((BEZIER_SMOOTHNESS * iPlusOne.getZ()) / 3.0) + iPlusTwo.getZ()) - ((BEZIER_SMOOTHNESS * iPlusThree.getZ()) / 3.0), iPlusTwo.getX(), iPlusTwo.getY(), iPlusTwo.getZ()});
            }
        }
        return curves;
    }

    /**
     * Get a Bezier control point given a list of points and an index.
     *
     * @param points the list of points
     * @param index  the index
     *
     * @return a control point
     */
    protected static Point2D getControlPoint2D(final List<Point2D> points, final int index) {
        // offset the list by one to allow for invisible reference points
        if (index == 0) {
            // start of control points so we need an invisible reference point
            final double x = points.get(0).getX() - ((points.get(1).getX() - points.get(0).getX()) / 2.0);
            final double y = points.get(0).getY() - ((points.get(1).getY() - points.get(0).getY()) / 2.0);
            return new Point2D(x, y);
        } else if (index == (points.size() + 1)) {
            // end of control points so we need an invisible reference point
            final double x = points.get(index - 2).getX() - ((points.get(index - 2).getX() - points.get(index - 3).getX()) / 2.0);
            final double y = points.get(index - 2).getY() - ((points.get(index - 2).getY() - points.get(index - 3).getY()) / 2.0);
            return new Point2D(x, y);
        } else {
            // actual control point
            return points.get(index - 1);
        }
    }

    /**
     * Get a Bezier control point given a list of points and an index.
     *
     * @param points the list of points
     * @param index  the index
     *
     * @return a control point
     */
    protected static Point3D getControlPoint3D(final List<Point3D> points, final int index) {
        // offset the list by one to allow for invisible reference points
        if (index == 0) {
            // start of control points so we need an invisible reference point
            final double x = points.get(0).getX() - ((points.get(1).getX() - points.get(0).getX()) / 2.0);
            final double y = points.get(0).getY() - ((points.get(1).getY() - points.get(0).getY()) / 2.0);
            final double z = points.get(0).getZ() - ((points.get(1).getZ() - points.get(0).getZ()) / 2.0);
            return new Point3D(x, y, z);
        } else if (index == (points.size() + 1)) {
            // end of control points so we need an invisible reference point
            final double x = points.get(index - 2).getX() - ((points.get(index - 2).getX() - points.get(index - 3).getX()) / 2.0);
            final double y = points.get(index - 2).getY() - ((points.get(index - 2).getY() - points.get(index - 3).getY()) / 2.0);
            final double z = points.get(index - 2).getZ() - ((points.get(index - 2).getZ() - points.get(index - 3).getZ()) / 2.0);
            return new Point3D(x, y, z);
        } else {
            // actual control point
            return points.get(index - 1);
        }
    }

    /**
     * Get a Bezier control point given a list of points and an index.
     *
     * @param points the list of points
     * @param index  the index
     *
     * @return a control point
     */
    protected static Vector3D getControlVector3D(final List<Vector3D> points, final int index) {
        // offset the list by one to allow for invisible reference points
        if (index == 0) {
            // start of control points so we need an invisible reference point
            final double x = points.get(0).getX() - ((points.get(1).getX() - points.get(0).getX()) / 2.0);
            final double y = points.get(0).getY() - ((points.get(1).getY() - points.get(0).getY()) / 2.0);
            final double z = points.get(0).getZ() - ((points.get(1).getZ() - points.get(0).getZ()) / 2.0);
            return new Vector3D(x, y, z);
        } else if (index == (points.size() + 1)) {
            // end of control points so we need an invisible reference point
            final double x = points.get(index - 2).getX() - ((points.get(index - 2).getX() - points.get(index - 3).getX()) / 2.0);
            final double y = points.get(index - 2).getY() - ((points.get(index - 2).getY() - points.get(index - 3).getY()) / 2.0);
            final double z = points.get(index - 2).getZ() - ((points.get(index - 2).getZ() - points.get(index - 3).getZ()) / 2.0);
            return new Vector3D(x, y, z);
        } else {
            // actual control point
            return points.get(index - 1);
        }
    }

    /**
     * Return a normalized index for a circle or arc given an index.
     *
     * @param i the index to normalize
     *
     * @return the normalized index
     */
    protected static int normalizeIndex(final int i) {
        int a = i;
        if (a < 0) {
            a += (a / 360 + 1) * 360;
        }
        a %= 361;
        return a;
    }

    /**
     * Find the general camera direction using the specified look vector.
     *
     * @param look the look vector
     *
     * @return a general camera direction
     */
    protected static Direction findCameraDirection(final Vector3D look) {
        final Vector3D normalized;
        if (!look.equals(Vector3D.ZERO)) {
            normalized = look.normalize();
        } else {
            normalized = look;
        }
        // there are six possible view directions
        final double[] angles = new double[6];
        for (int i = 0; i < angles.length; i++) {
            angles[i] = Math.acos(Vector3D.dotProduct(normalized, DIRECTIONS[i]));
        }
        // find the smallest angle
        int current = 0;
        for (int i = 1; i < angles.length; i++) {
            if (angles[i] < angles[current]) {
                current = i;
            }
        }
        // uncomment to reverse the directions
        //        switch (Direction.values()[current]) {
        //            case BACK:
        //                return Direction.FRONT;
        //            case FRONT:
        //                return Direction.BACK;
        //            case LEFT:
        //                return Direction.RIGHT;
        //            case RIGHT:
        //                return Direction.LEFT;
        //            case TOP:
        //                return Direction.BOTTOM;
        //            case BOTTOM:
        //                return Direction.TOP;
        //        }
        return Direction.values()[current];
    }

}
