package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Plane3DTest extends AbstractObjectTestBase<Plane3D> {

    private final Random random = new Random();

    public Plane3DTest() {
    }

    @Override
    protected Plane3D copyValueObject(final Plane3D object) {
        return new Plane3D(object.getX(), object.getY(), object.getZ(), object.getDistance());
    }

    @Override
    protected Plane3D newValueObject() {
        return new Plane3D(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt());
    }

    @Override
    protected void setFieldsToNull(final Plane3D object) {
        // no fields to set to null
    }

    @Test
    public void testPlane3DBuilder() {
        final Plane3D a = new Plane3D(1.0, 2.0, 3.0, 4.0);
        final Plane3D aa = Plane3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .distance(4.0)
                .build();
        // TODO this may not be correct
//        final Plane3D b = Plane3D.newInstance(new Vector3D(1.0, 2.0, 3.0), 4.0);
//        final Plane3D bb = Plane3D.Builder.construct()
//                .x(1.0)
//                .y(2.0)
//                .z(3.0)
//                .distance(4.0)
//                .build();
        final Plane3D c = Plane3D.newInstance(new Vector3D(1.0, 2.0, 3.0), new Vector3D(3.0, 1.0, 2.0), new Vector3D(2.0, 3.0, 1.0));
        final Plane3D cc = Plane3D.Builder.construct()
                .first(new Vector3D(1.0, 2.0, 3.0))
                .second(new Vector3D(3.0, 1.0, 2.0))
                .third(new Vector3D(2.0, 3.0, 1.0))
                .build();

        Assert.assertEquals(a, aa);
//        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
    }
}
