package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class RectangleTest extends AbstractObjectTestBase<Rectangle> {

    private final Random random = new Random();

    public RectangleTest() {
    }

    @Override
    protected Rectangle copyValueObject(final Rectangle object) {
        return new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight());
    }

    @Override
    protected Rectangle newValueObject() {
        return new Rectangle(random.nextInt(), random.nextInt(), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE));
    }

    @Override
    protected void setFieldsToNull(final Rectangle object) {
        // no fields to set to null
    }

    @Test
    public void testRectangleBuilder() {
        final Rectangle a = new Rectangle();
        final Rectangle aa = Rectangle.Builder.construct()
                .build();

        final Rectangle b = new Rectangle(1, 2, 3, 4);
        final Rectangle bb = Rectangle.Builder.construct()
                .x(1)
                .y(2)
                .width(3)
                .height(4)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangle1() {
        new Rectangle(0, 0, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangle2() {
        new Rectangle(0, 0, 0, -1);
    }
}
