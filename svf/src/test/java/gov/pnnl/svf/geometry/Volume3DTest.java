package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Volume3DTest extends AbstractObjectTestBase<Volume3D> {

    private final Random random = new Random();

    public Volume3DTest() {
    }

    @Override
    protected Volume3D copyValueObject(final Volume3D object) {
        return new Volume3D(object.getX(), object.getY(), object.getZ(), object.getWidth(), object.getHeight(), object.getDepth(), object.getSlices());
    }

    @Override
    protected Volume3D newValueObject() {
        return new Volume3D(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE));
    }

    @Override
    protected void setFieldsToNull(final Volume3D object) {
        // no fields to set to null
    }

    @Test
    public void testCircl2DBuilder() {
        final Volume3D a = new Volume3D(1.0, 2.0, 3.0);
        final Volume3D aa = Volume3D.Builder.construct()
                .width(1.0)
                .height(2.0)
                .depth(3.0)
                .build();

        final Volume3D b = new Volume3D(1.0, 2.0, 3.0, 1.5, 2.5, 3.5);
        final Volume3D bb = Volume3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .width(1.5)
                .height(2.5)
                .depth(3.5)
                .build();

        final Volume3D c = new Volume3D(1.0, 2.0, 3.0, 1.5, 2.5, 3.5, 12345);
        final Volume3D cc = Volume3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .width(1.5)
                .height(2.5)
                .depth(3.5)
                .slices(12345)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
    }
}
