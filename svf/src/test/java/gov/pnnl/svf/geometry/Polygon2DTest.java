package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Polygon2DTest extends AbstractObjectTestBase<Polygon2D> {

    private final Random random = new Random();

    public Polygon2DTest() {
    }

    @Override
    protected Polygon2D copyValueObject(final Polygon2D object) {
        return new Polygon2D(object.getX(), object.getY(), object.getContours());
    }

    @Override
    protected Polygon2D newValueObject() {
        final List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < random.nextInt(10) + 3; i++) {
            points.add(new Point2D(random.nextInt(), random.nextInt()));
        }
        return new Polygon2D(random.nextInt(), random.nextInt(), points);
    }

    @Override
    protected void setFieldsToNull(final Polygon2D object) {
        // no fields to set to null
    }

    @Test
    public void testPolygon2DBuilder() {
        final Polygon2D a = new Polygon2D(Arrays.asList(new Point2D(1.0, 2.0), new Point2D(2.0, 3.0), new Point2D(3.0, 4.0)));
        final Polygon2D aa = Polygon2D.Builder.construct()
                .contours(Arrays.asList(new Point2D(1.0, 2.0), new Point2D(2.0, 3.0), new Point2D(3.0, 4.0)))
                .build();

        final Polygon2D b = new Polygon2D(1.0, 2.0, new Point2D(1.0, 2.0), new Point2D(2.0, 3.0), new Point2D(3.0, 4.0));
        final Polygon2D bb = Polygon2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .points(new Point2D(1.0, 2.0), new Point2D(2.0, 3.0), new Point2D(3.0, 4.0))
                .build();

        final Polygon2D c = new Polygon2D(1.0, 2.0, new Point2D(1.0, 2.0), new Point2D(2.0, 3.0), new Point2D(3.0, 4.0));
        final Polygon2D cc = Polygon2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .addPoint(new Point2D(1.0, 2.0))
                .addPoint(2.0, 3.0)
                .addPoint(3.0, 4.0)
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
    }
}
