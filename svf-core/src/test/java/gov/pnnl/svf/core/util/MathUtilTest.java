package gov.pnnl.svf.core.util;

import gov.pnnl.svf.core.geometry.Axis;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author D3X573
 */
public class MathUtilTest {

    /**
     * Test of newIdentityMatrix method, of class MathUtil.
     */
    @Test
    public void testNewIdentityMatrix() {
        Assert.assertTrue(MathUtil.newIdentityMatrix() != MathUtil.newIdentityMatrix());
        Assert.assertArrayEquals(MathUtil.newIdentityMatrix(), MathUtil.newIdentityMatrix(), 0.0);
    }

    /**
     * Test of index method, of class MathUtil.
     */
    @Test
    public void testIndex() {
        Assert.assertEquals(0, MathUtil.index(0, 0));
        Assert.assertEquals(1, MathUtil.index(1, 0));
        Assert.assertEquals(4, MathUtil.index(0, 1));
    }

    /**
     * Test of toString method, of class MathUtil.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(MathUtil.toString(MathUtil.newIdentityMatrix()));
    }

    /**
     * Test of translate method, of class MathUtil.
     */
    @Test
    public void testTranslate() {
        final double[] matrix = MathUtil.newIdentityMatrix();
        MathUtil.translate(matrix, 1, 2, 3);
        Assert.assertArrayEquals(new double[]{
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            1.0, 2.0, 3.0, 1.0
        }, matrix, 0.0);
    }

    /**
     * Test of scale method, of class MathUtil.
     */
    @Test
    public void testScale_4args() {
        final double[] matrix = MathUtil.newIdentityMatrix();
        MathUtil.scale(matrix, 1, 2, 3);
        Assert.assertArrayEquals(new double[]{
            1.0, 0.0, 0.0, 0.0,
            0.0, 2.0, 0.0, 0.0,
            0.0, 0.0, 3.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        }, matrix, 0.0);
    }

    /**
     * Test of rotate method, of class MathUtil.
     */
    @Test
    public void testRotate() {
        final double[] matrix = MathUtil.newIdentityMatrix();
        MathUtil.rotate(matrix, 90, 1, 0, 0);
        Assert.assertArrayEquals(new double[]{
            1.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, -1.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        }, matrix, 0.000000001);
    }

    /**
     * Test of multiply method, of class MathUtil.
     */
    @Test
    public void testMultiply() {
        final double[] matrix = MathUtil.newIdentityMatrix();
        MathUtil.multiply(MathUtil.newIdentityMatrix(), MathUtil.newIdentityMatrix(), matrix);
        Assert.assertArrayEquals(new double[]{
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        }, matrix, 0.0);
    }

    /**
     * Test of multiplyMatrixByMatrix method, of class MathUtil.
     */
    @Test
    public void testMultiplyMatrixByMatrix() {
        final double[] matrix = MathUtil.newIdentityMatrix();
        MathUtil.multiplyMatrixByMatrix(MathUtil.newIdentityMatrix(), MathUtil.newIdentityMatrix(), matrix);
        Assert.assertArrayEquals(new double[]{
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        }, matrix, 0.0);
    }

    /**
     * Test of multiplyMatrixByVector method, of class MathUtil.
     */
    @Test
    public void testMultiplyMatrixByVector() {
        final double[] matrix = MathUtil.newIdentityMatrix();
        MathUtil.multiplyMatrixByVector(MathUtil.newIdentityMatrix(), MathUtil.newIdentityMatrix(), matrix);
        Assert.assertArrayEquals(new double[]{
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        }, matrix, 0.0);
    }

    /**
     * Test of scale method, of class MathUtil.
     */
    @Test
    public void testScale_5args() {
        Assert.assertEquals(5.0, MathUtil.scale(60.0, 110.0, 10.0, 2.5, 7.5), 0.0);
    }

    /**
     * Test of interpolation method, of class MathUtil.
     */
    @Test
    public void testInterpolation_4args_1() {
        Assert.assertArrayEquals(new double[]{
            2.0, 3.0
        }, MathUtil.interpolation(new double[]{
            1.0, 2.0
        }, new double[]{
            4.0, 5.0
        }, new double[]{
            2.0, 3.0
        }, Axis.X), 0.0);
    }

    /**
     * Test of interpolation method, of class MathUtil.
     */
    @Test
    public void testInterpolation_4args_2() {
        Assert.assertArrayEquals(new double[]{
            2L, 3L
        }, MathUtil.interpolation(new double[]{
            1L, 2L
        }, new double[]{
            4L, 5L
        }, new double[]{
            2L, 3L
        }, Axis.X), 0L);
    }

}
