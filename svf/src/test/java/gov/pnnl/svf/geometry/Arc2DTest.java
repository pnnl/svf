package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Arc2DTest extends AbstractObjectTestBase<Arc2D> {

    private final Random random = new Random();

    public Arc2DTest() {
    }

    @Override
    protected Arc2D copyValueObject(final Arc2D object) {
        return new Arc2D(object.getAngle(), object.getRadius(), object.getArcWidth(), object.getArcHeight());
    }

    @Override
    protected Arc2D newValueObject() {
        return new Arc2D(random.nextInt(), random.nextInt(), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE));
    }

    @Override
    protected void setFieldsToNull(final Arc2D object) {
        // no fields to set to null
    }

    @Test
    public void testArc() {
        final Arc2D a = new Arc2D(45.0, 10.0, (Math.PI / 4.0) * 10.0, 1.0);
        final Arc2D b = new Arc2D(0.0, 0.0, 45.0, 10.0, (Math.PI / 4.0) * 10.0, 1.0);
        final Arc2D c = Arc2D.newInstance(22.5, 45.0, 9.5, 10.5);
        final Arc2D d = Arc2D.newInstance(0.0, 0.0, 22.5, 45.0, 9.5, 10.5);

        Assert.assertEquals(a, b);
        Assert.assertEquals(b, c);
        Assert.assertEquals(c, d);
    }

    @Test
    public void testArcBuilder() {
        final Arc2D a = new Arc2D(45.0, 10.0, (Math.PI / 4.0) * 10.0, 1.0);
        final Arc2D aa = Arc2D.Builder.construct()
                .angle(45.0)
                .radius(10.0)
                .arcWidth((Math.PI / 4.0) * 10.0)
                .arcHeight(1.0)
                .build();

        final Arc2D b = new Arc2D(0.0, 0.0, 45.0, 10.0, (Math.PI / 4.0) * 10.0, 1.0);
        final Arc2D bb = Arc2D.Builder.construct()
                .x(0.0)
                .y(0.0)
                .angle(45.0)
                .radius(10.0)
                .arcWidth((Math.PI / 4.0) * 10.0)
                .arcHeight(1.0)
                .build();

        final Arc2D c = Arc2D.newInstance(22.5, 45.0, 9.5, 10.5);
        final Arc2D cc = Arc2D.Builder.construct()
                .start(22.5)
                .angle(45.0)
                .inner(9.5)
                .outer(10.5)
                .build();

        final Arc2D d = Arc2D.newInstance(0.0, 0.0, 22.5, 45.0, 9.5, 10.5);
        final Arc2D dd = Arc2D.Builder.construct()
                .x(0.0)
                .y(0.0)
                .start(22.5)
                .angle(45.0)
                .inner(9.5)
                .outer(10.5)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
        Assert.assertEquals(d, dd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArc1() {
        new Arc2D(0, 0, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArc2() {
        new Arc2D(0, 0, 0, -1);
    }

    @Test
    public void testArc3() {
        final Arc2D a = new Arc2D(45.0, 10.0, (Math.PI / 4.0) * 10.0, 1.0);
        for (int i = 0; i < iterateLength; i++) {
            final Arc2D b = new Arc2D((random.nextInt(1000) * (random.nextBoolean() ? 360.0 : -360.0)) + 45.0, 10.0, (Math.PI / 4.0) * 10.0, 1.0);
            Assert.assertEquals(a, b);
        }
    }
}
