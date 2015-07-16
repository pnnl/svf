package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import junit.framework.Assert;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class PathVector3DTest extends AbstractObjectTestBase<PathVector3D> {

    private final Random random = new Random();

    public PathVector3DTest() {
    }

    @Override
    protected PathVector3D copyValueObject(final PathVector3D object) {
        return new PathVector3D(object.getX(), object.getY(), object.getZ(), object.getPoints());
    }

    @Override
    protected PathVector3D newValueObject() {
        final List<Vector3D> points = new ArrayList<>();
        for (int i = 0; i < random.nextInt(10); i++) {
            points.add(new Vector3D(random.nextInt(), random.nextInt(), random.nextInt()));
        }
        return new PathVector3D(random.nextInt(), random.nextInt(), random.nextInt(), points);
    }

    @Override
    protected void setFieldsToNull(final PathVector3D object) {
        // no fields to set to null
    }

    @Test
    public void testPathVector3DBuilder() {
        final PathVector3D a = new PathVector3D(new Vector3D(1.0, 2.0, 3.0));
        final PathVector3D aa = PathVector3D.Builder.construct()
                .points(new Vector3D(1.0, 2.0, 3.0))
                .build();

        final PathVector3D b = new PathVector3D(1.0, 2.0, 3.0, new Vector3D(1.0, 2.0, 3.0));
        final PathVector3D bb = PathVector3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .points(new Vector3D(1.0, 2.0, 3.0))
                .build();

        final PathVector3D c = new PathVector3D(1.0, 2.0, 3.0, new Vector3D(1.0, 2.0, 3.0), new Vector3D(2.0, 3.0, 4.0));
        final PathVector3D cc = PathVector3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .addPoint(new Vector3D(1.0, 2.0, 3.0))
                .addPoint(2.0, 3.0, 4.0)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
    }
}
