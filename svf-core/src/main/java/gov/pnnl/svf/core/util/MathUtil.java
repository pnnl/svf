package gov.pnnl.svf.core.util;

import gov.pnnl.svf.core.geometry.Axis;
import java.text.MessageFormat;

/**
 * Utility class for math related methods.
 *
 * @author Amelia Bleeker
 */
public class MathUtil {

    /**
     * An identity matrix.
     */
    private static final double[] IDENTITY = new double[]{
        1.0, 0.0, 0.0, 0.0,
        0.0, 1.0, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    };

    /**
     * Constructor kept private for static class
     */
    private MathUtil() {
    }

    /**
     * Create and return a new identity matrix.
     *
     * @return a new identity matrix
     */
    public static double[] newIdentityMatrix() {
        final double[] copy = new double[IDENTITY.length];
        System.arraycopy(IDENTITY, 0, copy, 0, copy.length);
        return copy;
    }

    /**
     * Find the matrix index for the specified row and column. Note that OpenGL
     * matrices have rows and columns swapped.
     *
     * @param row    the row index
     * @param column the column index
     *
     * @return the matrix index
     */
    public static int index(final int row, final int column) {
        return column * 4 + row;
    }

    /**
     * Format the matrix or vector value into a human readable string.
     *
     * @param value the matrix or vector to format
     *
     * @return a human readable string
     */
    public static String toString(final double[] value) {
        switch (value.length) {
            case 3:
                return MessageFormat.format("[{0,number,#000.00}|{1,number,#000.00}|{2,number,#000.00}]", value[0], value[1], value[2]);
            case 4:
                return MessageFormat.format("[{0,number,#000.00}|{1,number,#000.00}|{2,number,#000.00}|{3,number,#000.00}]", value[0], value[1], value[2], value[3]);
            case 16:
                return MessageFormat.format("[{0,number,#000.00}|{1,number,#000.00}|{2,number,#000.00}|{3,number,#000.00}]\n"
                                            + "[{4,number,#000.00}|{5,number,#000.00}|{6,number,#000.00}|{7,number,#000.00}]\n"
                                            + "[{8,number,#000.00}|{9,number,#000.00}|{10,number,#000.00}|{11,number,#000.00}]\n"
                                            + "[{12,number,#000.00}|{13,number,#000.00}|{14,number,#000.00}|{15,number,#000.00}]",
                                            value[0], value[1], value[2], value[3],
                                            value[4], value[5], value[6], value[7],
                                            value[8], value[9], value[10], value[11],
                                            value[12], value[13], value[14], value[15]);
            default:
                throw new IllegalArgumentException("value");
        }
    }

    /**
     * Create a translation matrix. The matrix argument is both the input and
     * output.
     *
     * @param matrix the input and output matrix
     * @param x      the x translation value
     * @param y      the y translation value
     * @param z      the z translation value
     */
    public static void translate(final double[] matrix, final double x, final double y, final double z) {
        matrix[12] = matrix[0] * x + matrix[4] * y + matrix[8] * z + matrix[12];
        matrix[13] = matrix[1] * x + matrix[5] * y + matrix[9] * z + matrix[13];
        matrix[14] = matrix[2] * x + matrix[6] * y + matrix[10] * z + matrix[14];
        matrix[15] = matrix[3] * x + matrix[7] * y + matrix[11] * z + matrix[15];
    }

    /**
     * Create a scaling matrix. The matrix argument is both the input and
     * output.
     *
     * @param matrix the input and output matrix
     * @param x      the x scaling value
     * @param y      the y scaling value
     * @param z      the z scaling value
     */
    public static void scale(final double[] matrix, final double x, final double y, final double z) {
        matrix[0] *= x;
        matrix[4] *= y;
        matrix[8] *= z;
        matrix[1] *= x;
        matrix[5] *= y;
        matrix[9] *= z;
        matrix[2] *= x;
        matrix[6] *= y;
        matrix[10] *= z;
        matrix[3] *= x;
        matrix[7] *= y;
        matrix[11] *= z;
    }

    /**
     * Create a rotation matrix. The matrix argument is both the input and
     * output.
     *
     * @param matrix the input and output matrix
     * @param angle  the angle in degrees
     * @param x      the x rotation value
     * @param y      the y rotation value
     * @param z      the z rotation value
     */
    public static void rotate(final double[] matrix, final double angle, double x, double y, double z) {
        double xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c, s, c;
        double m[] = newIdentityMatrix();
        boolean optimized = false;
        s = Math.sin(Math.toRadians(angle));
        c = Math.cos(Math.toRadians(angle));
        // optimized path for an euler angle rotation
        if (x == 0.0F) {
            if (y == 0.0F) {
                if (z != 0.0F) {
                    optimized = true;
                    // rotate only around z-axis
                    m[index(0, 0)] = c;
                    m[index(1, 1)] = c;
                    if (z < 0.0F) {
                        m[index(0, 1)] = s;
                        m[index(1, 0)] = -s;
                    } else {
                        m[index(0, 1)] = -s;
                        m[index(1, 0)] = s;
                    }
                }
            } else if (z == 0.0F) {
                optimized = true;
                // rotate only around y-axis
                m[index(0, 0)] = c;
                m[index(2, 2)] = c;
                if (y < 0.0F) {
                    m[index(0, 2)] = -s;
                    m[index(2, 0)] = s;
                } else {
                    m[index(0, 2)] = s;
                    m[index(2, 0)] = -s;
                }
            }
        } else if (y == 0.0F) {
            if (z == 0.0F) {
                optimized = true;
                // rotate only around x-axis
                m[index(1, 1)] = c;
                m[index(2, 2)] = c;
                if (x < 0.0F) {
                    m[index(1, 2)] = s;
                    m[index(2, 1)] = -s;
                } else {
                    m[index(1, 2)] = -s;
                    m[index(2, 1)] = s;
                }
            }
        }
        // not optimized path
        if (!optimized) {
            final double mag = Math.sqrt(x * x + y * y + z * z);
            if (mag <= 1.0e-4) {
                // no rotation, leave mat as-is
                return;
            }
            x /= mag;
            y /= mag;
            z /= mag;
            /*
             * Arbitrary axis rotation matrix.
             *
             * This is composed of 5 matrices, Rz, Ry, T, Ry', Rz', multiplied
             * like so: Rz * Ry * T * Ry' * Rz'. T is the final rotation (which
             * is about the X-axis), and the two composite transforms Ry' * Rz'
             * and Rz * Ry are (respectively) the rotations necessary from the
             * arbitrary axis to the X-axis then back. They are all elementary
             * rotations.
             *
             * Rz' is a rotation about the Z-axis, to bring the axis vector into
             * the x-z plane. Then Ry' is applied, rotating about the Y-axis to
             * bring the axis vector parallel with the X-axis. The rotation
             * about the X-axis is then performed. Ry and Rz are simply the
             * respective inverse transforms to bring the arbitrary axis back to
             * its original orientation. The first transforms Rz' and Ry' are
             * considered inverses, since the data from the arbitrary axis gives
             * you info on how to get to it, not how to get away from it, and an
             * inverse must be applied.
             *
             * The basic calculation used is to recognize that the arbitrary
             * axis vector (x, y, z), since it is of unit length, actually
             * represents the sines and cosines of the angles to rotate the
             * X-axis to the same orientation, with theta being the angle about
             * Z and phi the angle about Y (in the order described above) as
             * follows:
             *
             * cos ( theta ) = x / sqrt ( 1 - z^2 ) sin ( theta ) = y / sqrt ( 1
             * - z^2 )
             *
             * cos ( phi ) = sqrt ( 1 - z^2 ) sin ( phi ) = z
             *
             * Note that cos ( phi ) can further be inserted to the above
             * formulas:
             *
             * cos ( theta ) = x / cos ( phi ) sin ( theta ) = y / sin ( phi )
             *
             * ...etc. Because of those relations and the standard trigonometric
             * relations, it is pssible to reduce the transforms down to what is
             * used below. It may be that any primary axis chosen will give the
             * same results (modulo a sign convention) using thie method.
             *
             * Particularly nice is to notice that all divisions that might have
             * caused trouble when parallel to certain planes or axis go away
             * with care paid to reducing the expressions. After checking, it
             * does perform correctly under all cases, since in all the cases of
             * division where the denominator would have been zero, the
             * numerator would have been zero as well, giving the expected
             * result.
             */
            xx = x * x;
            yy = y * y;
            zz = z * z;
            xy = x * y;
            yz = y * z;
            zx = z * x;
            xs = x * s;
            ys = y * s;
            zs = z * s;
            one_c = 1.0F - c;
            // We already hold the identity-matrix so we can skip some statements
            m[index(0, 0)] = (one_c * xx) + c;
            m[index(0, 1)] = (one_c * xy) - zs;
            m[index(0, 2)] = (one_c * zx) + ys;
            // m[index(0,3)] = 0.0F;
            m[index(1, 0)] = (one_c * xy) + zs;
            m[index(1, 1)] = (one_c * yy) + c;
            m[index(1, 2)] = (one_c * yz) - xs;
            // m[index(1,3)] = 0.0F;
            m[index(2, 0)] = (one_c * zx) - ys;
            m[index(2, 1)] = (one_c * yz) + xs;
            m[index(2, 2)] = (one_c * zz) + c;
            // m[index(2,3)] = 0.0F;
            // m[index(3,0)] = 0.0F;
            // m[index(3,1)] = 0.0F;
            // m[index(3,2)] = 0.0F;
            // m[index(3,3)] = 1.0F;
        }
        // multiply the input matrix by the rotation matrix
        final double[] copy = new double[matrix.length];
        System.arraycopy(matrix, 0, copy, 0, matrix.length);
        multiplyMatrixByMatrix(copy, m, matrix);
    }

    /**
     * Multiply the two matrices.
     *
     * @param m   the first matrix
     * @param n   the second matrix
     * @param out the output matrix
     */
    public static void multiply(final double[] m, final double[] n, final double[] out) {
        switch (m.length) {
            case 16:
                switch (n.length) {
                    case 16:
                        multiplyMatrixByMatrix(m, n, out);
                        break;
                    case 4:
                        multiplyMatrixByVector(m, n, out);
                        break;
                    default:
                        // unhandled n length
                        throw new IllegalArgumentException("n");
                }
                break;
            default:
                // unhandled m length
                throw new IllegalArgumentException("m");
        }
    }

    /**
     * Multiply a 4x4 matrix by another 4x4 matrix (m * n) = out. This function
     * does not do parameter checking.
     *
     * @param m   an input 4x4 matrix in OpenGL order
     * @param n   an input 4x4 matrix in OpenGL order
     * @param out the resulting 4x4 matrix in OpenGL order
     */
    public static void multiplyMatrixByMatrix(final double[] m, final double[] n, final double[] out) {
        out[0] = m[0] * n[0] + m[4] * n[1] + m[8] * n[2] + m[12] * n[3];
        out[4] = m[0] * n[4] + m[4] * n[5] + m[8] * n[6] + m[12] * n[7];
        out[8] = m[0] * n[8] + m[4] * n[9] + m[8] * n[10] + m[12] * n[11];
        out[12] = m[0] * n[12] + m[4] * n[13] + m[8] * n[14] + m[12] * n[15];

        out[1] = m[1] * n[0] + m[5] * n[1] + m[9] * n[2] + m[13] * n[3];
        out[5] = m[1] * n[4] + m[5] * n[5] + m[9] * n[6] + m[13] * n[7];
        out[9] = m[1] * n[8] + m[5] * n[9] + m[9] * n[10] + m[13] * n[11];
        out[13] = m[1] * n[12] + m[5] * n[13] + m[9] * n[14] + m[13] * n[15];

        out[2] = m[2] * n[0] + m[6] * n[1] + m[10] * n[2] + m[14] * n[3];
        out[6] = m[2] * n[4] + m[6] * n[5] + m[10] * n[6] + m[14] * n[7];
        out[10] = m[2] * n[8] + m[6] * n[9] + m[10] * n[10] + m[14] * n[11];
        out[14] = m[2] * n[12] + m[6] * n[13] + m[10] * n[14] + m[14] * n[15];

        out[3] = m[3] * n[0] + m[7] * n[1] + m[11] * n[2] + m[15] * n[3];
        out[7] = m[3] * n[4] + m[7] * n[5] + m[11] * n[6] + m[15] * n[7];
        out[11] = m[3] * n[8] + m[7] * n[9] + m[11] * n[10] + m[15] * n[11];
        out[15] = m[3] * n[12] + m[7] * n[13] + m[11] * n[14] + m[15] * n[15];
    }

    /**
     * Multiply a 4x4 matrix by a vector (m * n) = out. This function does not
     * do parameter checking.
     *
     * @param m   an input 4x4 matrix in OpenGL order
     * @param n   an input 4 vector
     * @param out the resulting 4 vector
     */
    public static void multiplyMatrixByVector(final double[] m, final double[] n, final double[] out) {
        out[0] = m[0] * n[0] + m[4] * n[1] + m[8] * n[2] + m[12] * n[3];
        out[1] = m[1] * n[0] + m[5] * n[1] + m[9] * n[2] + m[13] * n[3];
        out[2] = m[2] * n[0] + m[6] * n[1] + m[10] * n[2] + m[14] * n[3];
        out[3] = m[3] * n[0] + m[7] * n[1] + m[11] * n[2] + m[15] * n[3];
    }

    /**
     * Scale a value using linear interpolation. This function does not do
     * parameter checking.
     *
     * @param input   the value to scale
     * @param fromMax from max
     * @param fromMin from min
     * @param toMax   to max
     * @param toMin   to min
     *
     * @return the scaled value
     */
    public static double scale(final double input, final double fromMax, final double fromMin, final double toMax, final double toMin) {
        return toMin + (input - fromMin) * (toMax - toMin) / (fromMax - fromMin);
    }

    /**
     * Linear interpolation.
     *
     * @param from  the from point
     * @param to    the to point
     * @param value the value to solve with
     * @param axis  the axis to solve for
     *
     * @return the new interpolated point
     */
    public static double[] interpolation(final double[] from, final double[] to, final double[] value, final Axis axis) {
        if (from.length != value.length && to.length != value.length) {
            throw new IllegalArgumentException("all arguments must be the same length");
        }
        switch (value.length) {
            case 2:
                if (axis == Axis.X) {
                    // solve for x
                    if (Double.compare(to[Axis.Y.ordinal()] - from[Axis.Y.ordinal()], 0.0) == 0) {
                        return from;
                    } else {
                        return new double[]{
                            from[Axis.X.ordinal()] + (to[Axis.X.ordinal()] - from[Axis.X.ordinal()]) * (value[Axis.Y.ordinal()] - from[Axis.Y.ordinal()])
                                                     / (to[Axis.Y.ordinal()] - from[Axis.Y.ordinal()]), value[Axis.Y.ordinal()]};
                    }
                } else if (axis == Axis.Y) {
                    // solve for y
                    if (Double.compare(to[Axis.X.ordinal()] - from[Axis.X.ordinal()], 0.0) == 0) {
                        return from;
                    } else {
                        return new double[]{
                            value[Axis.X.ordinal()],
                            from[Axis.Y.ordinal()] + (to[Axis.Y.ordinal()] - from[Axis.Y.ordinal()]) * (value[Axis.X.ordinal()] - from[Axis.X.ordinal()])
                                                     / (to[Axis.X.ordinal()] - from[Axis.X.ordinal()])};
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("argument length not supported");
        }
        throw new IllegalArgumentException("axis");
    }

    /**
     * Linear interpolation.
     *
     * @param from  the from point
     * @param to    the to point
     * @param value the value to solve with
     * @param axis  the axis to solve for
     *
     * @return the new interpolated point
     */
    public static long[] interpolation(final long[] from, final long[] to, final long[] value, final Axis axis) {
        if (from.length != value.length && to.length != value.length) {
            throw new IllegalArgumentException("all arguments must be the same length");
        }
        switch (value.length) {
            case 2:
                if (axis == Axis.X) {
                    // solve for x
                    if (Double.compare(to[Axis.Y.ordinal()] - from[Axis.Y.ordinal()], 0.0) == 0) {
                        return from;
                    } else {
                        return new long[]{
                            from[Axis.X.ordinal()] + (to[Axis.X.ordinal()] - from[Axis.X.ordinal()]) * (value[Axis.Y.ordinal()] - from[Axis.Y.ordinal()])
                                                     / (to[Axis.Y.ordinal()] - from[Axis.Y.ordinal()]), value[Axis.Y.ordinal()]};
                    }
                } else if (axis == Axis.Y) {
                    // solve for y
                    if (Double.compare(to[Axis.X.ordinal()] - from[Axis.X.ordinal()], 0.0) == 0) {
                        return from;
                    } else {
                        return new long[]{
                            value[Axis.X.ordinal()],
                            from[Axis.Y.ordinal()] + (to[Axis.Y.ordinal()] - from[Axis.Y.ordinal()]) * (value[Axis.X.ordinal()] - from[Axis.X.ordinal()])
                                                     / (to[Axis.X.ordinal()] - from[Axis.X.ordinal()])};
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("argument length not supported");
        }
        throw new IllegalArgumentException("axis");
    }
}
