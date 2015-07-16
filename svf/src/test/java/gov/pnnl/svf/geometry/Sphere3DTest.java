package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Sphere3DTest extends AbstractObjectTestBase<Sphere3D> {

    private final Random random = new Random();

    public Sphere3DTest() {
    }

    @Override
    protected Sphere3D copyValueObject(final Sphere3D object) {
        return new Sphere3D(object.getX(), object.getY(), object.getZ(), object.getRadius());
    }

    @Override
    protected Sphere3D newValueObject() {
        return new Sphere3D(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt(Integer.MAX_VALUE));
    }

    @Override
    protected void setFieldsToNull(final Sphere3D object) {
        // no fields to set to null
    }

    @Test
    public void testSphere3DBuilder() {
        final Sphere3D a = new Sphere3D(0.5);
        final Sphere3D aa = Sphere3D.Builder.construct()
                .radius(0.5)
                .build();

        final Sphere3D b = new Sphere3D(1.0, 2.0, 3.0, 0.5);
        final Sphere3D bb = Sphere3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .radius(0.5)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
    }
}
