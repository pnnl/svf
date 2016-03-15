package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Circle2DTest extends AbstractObjectTestBase<Circle2D> {

    private final Random random = new Random();

    public Circle2DTest() {
    }

    @Override
    protected Circle2D copyValueObject(final Circle2D object) {
        return new Circle2D(object.getX(), object.getY(), object.getRadius());
    }

    @Override
    protected Circle2D newValueObject() {
        return new Circle2D(random.nextInt(), random.nextInt(), random.nextInt(Integer.MAX_VALUE));
    }

    @Override
    protected void setFieldsToNull(final Circle2D object) {
        // no fields to set to null
    }

    @Test
    public void testCircle2DBuilder() {
        final Circle2D a = new Circle2D(0.5);
        final Circle2D aa = Circle2D.Builder.construct()
                .radius(0.5)
                .build();

        final Circle2D b = new Circle2D(1.0, 2.0, 0.5);
        final Circle2D bb = Circle2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .radius(0.5)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
    }
}
