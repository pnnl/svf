package gov.pnnl.svf.core.util;

/**
 * Class used to evaluate Bezier curves. This class is not thread safe.
 *
 * @author Arthur Bleeker
 */
public class BezierCurveEvaluator {

    private final int degree;
    private final double[] ab;
    private final double[] bc;
    private final double[] cd;
    private final double[] abc;
    private final double[] bcd;

    /**
     * Constructor
     *
     * @param degree the degree of the component used in the function {x: 0.0
     *               &le; x &lt; &#8734;}
     */
    public BezierCurveEvaluator(final int degree) {
        if (degree <= 0) {
            throw new IllegalArgumentException("degree");
        }
        this.degree = degree;
        ab = new double[degree];
        bc = new double[degree];
        cd = new double[degree];
        abc = new double[degree];
        bcd = new double[degree];
    }

    /**
     * Evaluate the given points at the specified step. Parameters are not
     * checked to keep the function performant.
     *
     * @param points a 2d array of start, control, control, and end
     * @param step   the step to evaluate where {0.0 &le; step &le; 1.0}
     *
     * @return the evaluated point
     */
    public double[] evaluate(final double[][] points, final double step) {
        final double[] ret = new double[degree];
        evaluate(points, step, ret);
        return ret;
    }

    /**
     * Evaluate the given points at the specified step. Parameters are not
     * checked to keep the function performant.
     *
     * @param points a 1d array of start (a,b,c), control (a,b,c), control
     *               (a,b,c), and end (a,b,c)
     * @param step   the step to evaluate where {0.0 &le; step &le; 1.0}
     *
     * @return out the evaluated point
     */
    public double[] evaluate(final double[] points, final double step) {
        final double[] ret = new double[degree];
        evaluate(points, step, ret);
        return ret;
    }

    /**
     * Evaluate the given points at the specified step. Parameters are not
     * checked to keep the function performant.
     *
     * @param points a 2d array of start, control, control, and end
     * @param step   the step to evaluate where {0.0 &le; step &le; 1.0}
     * @param out    the evaluated point
     */
    public void evaluate(final double[][] points, final double step, final double[] out) {
        final double stepInverse = 1.0 - step;
        for (int i = 0; i < degree; i++) {
            ab[i] = points[0][i] * stepInverse + points[1][i] * step;
        }
        for (int i = 0; i < degree; i++) {
            bc[i] = points[1][i] * stepInverse + points[2][i] * step;
        }
        for (int i = 0; i < degree; i++) {
            cd[i] = points[2][i] * stepInverse + points[3][i] * step;
        }
        for (int i = 0; i < degree; i++) {
            abc[i] = ab[i] * stepInverse + cd[i] * step;
        }
        for (int i = 0; i < degree; i++) {
            bcd[i] = bc[i] * stepInverse + cd[i] * step;
        }
        for (int i = 0; i < degree; i++) {
            out[i] = abc[i] * stepInverse + bcd[i] * step;
        }
    }

    /**
     * Evaluate the given points at the specified step. Parameters are not
     * checked to keep the function performant.
     *
     * @param points a 1d array of start (a,b,c), control (a,b,c), control
     *               (a,b,c), and end (a,b,c)
     * @param step   the step to evaluate where {0.0 &le; step &le; 1.0}
     * @param out    the evaluated point
     */
    public void evaluate(final double[] points, final double step, final double[] out) {
        final double stepInverse = 1.0 - step;
        for (int i = 0; i < degree; i++) {
            ab[i] = points[i] * stepInverse + points[degree + i] * step;
        }
        for (int i = 0; i < degree; i++) {
            bc[i] = points[degree + i] * stepInverse + points[degree * 2 + i] * step;
        }
        for (int i = 0; i < degree; i++) {
            cd[i] = points[degree * 2 + i] * stepInverse + points[degree * 3 + i] * step;
        }
        for (int i = 0; i < degree; i++) {
            abc[i] = ab[i] * stepInverse + cd[i] * step;
        }
        for (int i = 0; i < degree; i++) {
            bcd[i] = bc[i] * stepInverse + cd[i] * step;
        }
        for (int i = 0; i < degree; i++) {
            out[i] = abc[i] * stepInverse + bcd[i] * step;
        }
    }
}
