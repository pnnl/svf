package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class RoundedRectangle2DTest extends AbstractObjectTestBase<RoundedRectangle2D> {

    private final Random random = new Random();

    public RoundedRectangle2DTest() {
    }

    @Override
    protected RoundedRectangle2D copyValueObject(final RoundedRectangle2D object) {
        return new RoundedRectangle2D(object.getX(), object.getY(), object.getWidth(), object.getHeight(), object.getRoundness());
    }

    @Override
    protected RoundedRectangle2D newValueObject() {
        return new RoundedRectangle2D(random.nextInt(), random.nextInt(), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE), random.nextDouble());
    }

    @Override
    protected void setFieldsToNull(final RoundedRectangle2D object) {
        // no fields to set to null
    }

    @Test
    public void testRectangleBuilder() {
        final RoundedRectangle2D a = new RoundedRectangle2D();
        final RoundedRectangle2D aa = RoundedRectangle2D.Builder.construct()
                .build();

        final RoundedRectangle2D b = new RoundedRectangle2D(1.0, 2.0, 3.0, 4.0, 0.12345);
        final RoundedRectangle2D bb = RoundedRectangle2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .width(3.0)
                .height(4.0)
                .roundness(0.12345)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangle1() {
        new RoundedRectangle2D(0, 0, -1, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangle2() {
        new RoundedRectangle2D(0, 0, 0, -1, 0);
    }
}
