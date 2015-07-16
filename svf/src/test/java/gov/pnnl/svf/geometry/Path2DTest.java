package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Path2DTest extends AbstractObjectTestBase<Path2D> {

    private final Random random = new Random();

    public Path2DTest() {
    }

    @Override
    protected Path2D copyValueObject(final Path2D object) {
        return new Path2D(object.getX(), object.getY(), object.getPoints());
    }

    @Override
    protected Path2D newValueObject() {
        final List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < random.nextInt(10); i++) {
            points.add(new Point2D(random.nextInt(), random.nextInt()));
        }
        return new Path2D(random.nextInt(), random.nextInt(), points);
    }

    @Override
    protected void setFieldsToNull(final Path2D object) {
        // no fields to set to null
    }

    @Test
    public void testPath2DBuilder() {
        final Path2D a = new Path2D(new Point2D(1.0, 2.0));
        final Path2D aa = Path2D.Builder.construct()
                .points(new Point2D(1.0, 2.0))
                .build();

        final Path2D b = new Path2D(1.0, 2.0, new Point2D(1.0, 2.0));
        final Path2D bb = Path2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .points(new Point2D(1.0, 2.0))
                .build();

        final Path2D c = new Path2D(1.0, 2.0, new Point2D(1.0, 2.0), new Point2D(2.0, 3.0));
        final Path2D cc = Path2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .addPoint(new Point2D(1.0, 2.0))
                .addPoint(2.0, 3.0)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
    }
}
