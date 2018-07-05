package gov.pnnl.svf.core.util;

import gov.pnnl.svf.test.PerformanceStats;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class BezierCurveEvaluatorTest {

    private static final int ITERATIONS = 100000000;

    /**
     * Test of evaluate method, of class BezierCurveEvaluator.
     */
    @Test
    public void testBezierEvaluator() {
        final BezierCurveEvaluator evaluator = new BezierCurveEvaluator(2);
        final double[][] points = new double[][]{
            {0.0, 0.0},
            {1.0, 0.0},
            {0.0, 1.0},
            {1.0, 1.0},};
        final double[] out = new double[2];
        evaluator.evaluate(points, 0.5, out);

        Assert.assertArrayEquals(out, evaluator.evaluate(points, 0.5), 0.00001);
    }

    @Test
    public void testPerformanceDegree2() {
        final BezierCurveEvaluator evaluator = new BezierCurveEvaluator(2);
        final double[][] points = new double[][]{
            {0.0, 0.0},
            {1.0, 0.0},
            {0.0, 1.0},
            {1.0, 1.0},};
        final double[] points2 = new double[]{
            0.0, 0.0,
            1.0, 0.0,
            0.0, 1.0,
            1.0, 1.0,};
        final double[] out = new double[2];
        long start;
        long end;
        // test first function
        start = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            evaluator.evaluate(points, (double) i / (double) ITERATIONS, out);
        }
        end = System.currentTimeMillis();
        PerformanceStats.write("BezierCurveEvaluator.bezierEvaluator(double[][], double, double[])", ITERATIONS, end - start);

        // test second function
        start = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            evaluator.evaluate(points, (double) i / (double) ITERATIONS);
        }
        end = System.currentTimeMillis();
        PerformanceStats.write("BezierCurveEvaluator.bezierEvaluator(double[][], double)", ITERATIONS, end - start);

        // test third function
        start = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            evaluator.evaluate(points2, (double) i / (double) ITERATIONS, out);
        }
        end = System.currentTimeMillis();
        PerformanceStats.write("BezierCurveEvaluator.bezierEvaluator(double[], double, double[])", ITERATIONS, end - start);

        // test fourth function
        start = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            evaluator.evaluate(points2, (double) i / (double) ITERATIONS);
        }
        end = System.currentTimeMillis();
        PerformanceStats.write("BezierCurveEvaluator.bezierEvaluator(double[], double)", ITERATIONS, end - start);
    }
}
