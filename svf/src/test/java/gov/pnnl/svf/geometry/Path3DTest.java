package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Path3DTest extends AbstractObjectTestBase<Path3D> {

    private final Random random = new Random();

    public Path3DTest() {
    }

    @Override
    protected Path3D copyValueObject(final Path3D object) {
        return new Path3D(object.getX(), object.getY(), object.getZ(), object.getPoints());
    }

    @Override
    protected Path3D newValueObject() {
        final List<Point3D> points = new ArrayList<>();
        for (int i = 0; i < random.nextInt(10); i++) {
            points.add(new Point3D(random.nextInt(), random.nextInt(), random.nextInt()));
        }
        return new Path3D(random.nextInt(), random.nextInt(), random.nextInt(), points);
    }

    @Override
    protected void setFieldsToNull(final Path3D object) {
        // no fields to set to null
    }

    @Test
    public void testPath3DBuilder() {
        final Path3D a = new Path3D(new Point3D(1.0, 2.0, 3.0));
        final Path3D aa = Path3D.Builder.construct()
                .points(new Point3D(1.0, 2.0, 3.0))
                .build();

        final Path3D b = new Path3D(1.0, 2.0, 3.0, new Point3D(1.0, 2.0, 3.0));
        final Path3D bb = Path3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .points(new Point3D(1.0, 2.0, 3.0))
                .build();

        final Path3D c = new Path3D(1.0, 2.0, 3.0, new Point3D(1.0, 2.0, 3.0), new Point3D(2.0, 3.0, 4.0));
        final Path3D cc = Path3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .addPoint(new Point3D(1.0, 2.0, 3.0))
                .addPoint(2.0, 3.0, 4.0)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
    }
}
