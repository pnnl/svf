package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Point3DTest extends AbstractObjectTestBase<Point3D> {

    private final Random random = new Random();

    public Point3DTest() {
    }

    @Override
    protected Point3D copyValueObject(final Point3D object) {
        return new Point3D(object.getX(), object.getY(), object.getZ());
    }

    @Override
    protected Point3D newValueObject() {
        return new Point3D(random.nextInt(), random.nextInt(), random.nextInt());
    }

    @Override
    protected void setFieldsToNull(final Point3D object) {
        // no fields to set to null
    }

    @Test
    public void testCircl2DBuilder() {
        final Point3D a = new Point3D();
        final Point3D aa = Point3D.Builder.construct()
                .build();

        final Point3D b = new Point3D(1.0, 2.0, 3.0);
        final Point3D bb = Point3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
    }
}
