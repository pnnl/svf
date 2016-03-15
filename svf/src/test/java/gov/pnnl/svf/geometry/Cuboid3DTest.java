package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Cuboid3DTest extends AbstractObjectTestBase<Cuboid3D> {

    private final Random random = new Random();

    public Cuboid3DTest() {
    }

    @Override
    protected Cuboid3D copyValueObject(final Cuboid3D object) {
        return new Cuboid3D(object.getX(), object.getY(), object.getZ(), object.getWidth(), object.getHeight(), object.getDepth());
    }

    @Override
    protected Cuboid3D newValueObject() {
        return new Cuboid3D(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE));
    }

    @Override
    protected void setFieldsToNull(final Cuboid3D object) {
        // no fields to set to null
    }

    @Test
    public void testCircl2DBuilder() {
        final Cuboid3D a = new Cuboid3D(1.0, 2.0, 3.0);
        final Cuboid3D aa = Cuboid3D.Builder.construct()
                .width(1.0)
                .height(2.0)
                .depth(3.0)
                .build();

        final Cuboid3D b = new Cuboid3D(1.0, 2.0, 3.0, 1.5, 2.5, 3.5);
        final Cuboid3D bb = Cuboid3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .width(1.5)
                .height(2.5)
                .depth(3.5)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
    }
}
