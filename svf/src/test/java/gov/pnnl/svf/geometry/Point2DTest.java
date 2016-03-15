package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Point2DTest extends AbstractObjectTestBase<Point2D> {

    private final Random random = new Random();

    public Point2DTest() {
    }

    @Override
    protected Point2D copyValueObject(final Point2D object) {
        return new Point2D(object.getX(), object.getY());
    }

    @Override
    protected Point2D newValueObject() {
        return new Point2D(random.nextInt(), random.nextInt());
    }

    @Override
    protected void setFieldsToNull(final Point2D object) {
        // no fields to set to null
    }

    @Test
    public void testPoint2DBuilder() {
        final Point2D a = new Point2D();
        final Point2D aa = Point2D.Builder.construct()
                .build();

        final Point2D b = new Point2D(1.0, 2.0);
        final Point2D bb = Point2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
    }
}
