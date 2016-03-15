package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Rectangle2DTest extends AbstractObjectTestBase<Rectangle2D> {

    private final Random random = new Random();

    public Rectangle2DTest() {
    }

    @Override
    protected Rectangle2D copyValueObject(final Rectangle2D object) {
        return new Rectangle2D(object.getX(), object.getY(), object.getWidth(), object.getHeight());
    }

    @Override
    protected Rectangle2D newValueObject() {
        return new Rectangle2D(random.nextInt(), random.nextInt(), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE));
    }

    @Override
    protected void setFieldsToNull(final Rectangle2D object) {
        // no fields to set to null
    }

    @Test
    public void testRectangleBuilder() {
        final Rectangle2D a = new Rectangle2D();
        final Rectangle2D aa = Rectangle2D.Builder.construct()
                .build();

        final Rectangle2D b = new Rectangle2D(1.0, 2.0, 3.0, 4.0);
        final Rectangle2D bb = Rectangle2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .width(3.0)
                .height(4.0)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangle1() {
        new Rectangle2D(0, 0, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangle2() {
        new Rectangle2D(0, 0, 0, -1);
    }
}
