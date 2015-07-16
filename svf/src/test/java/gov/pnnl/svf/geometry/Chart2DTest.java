package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Chart;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Collections;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Chart2DTest extends AbstractObjectTestBase<Chart2D> {

    private final Random random = new Random();

    public Chart2DTest() {
    }

    @Override
    protected Chart2D copyValueObject(final Chart2D object) {
        return new Chart2D(object.getX(), object.getY(), object.getStyle(), object.getMinimum(), object.getMaximum(), object.getPoints());
    }

    @Override
    protected Chart2D newValueObject() {
        return new Chart2D(random.nextDouble(), random.nextDouble(),
                           Chart.values()[random.nextInt(Chart.values().length)],
                           random.nextBoolean() ? new Point2D(random.nextDouble(), random.nextDouble()) : null,
                           random.nextBoolean() ? new Point2D(random.nextDouble(), random.nextDouble()) : null,
                           Collections.singleton(new Point2D(random.nextDouble(), random.nextDouble())));
    }

    @Override
    protected void setFieldsToNull(final Chart2D object) {
        // no fields to set to null
    }

    @Test
    public void testCircl2DBuilder() {
        final Chart2D a = new Chart2D(new Point2D(1.0, 2.0));
        final Chart2D aa = Chart2D.Builder.construct()
                .addPoint(1.0, 2.0)
                .build();

        final Chart2D b = new Chart2D(Chart.AREA, new Point2D(1.0, 2.0));
        final Chart2D bb = Chart2D.Builder.construct()
                .styleArea()
                .addPoint(new Point2D(1.0, 2.0))
                .build();

        final Chart2D c = new Chart2D(Chart.BAR, new Point2D(Color.RED.toInt(), 1.5));
        final Chart2D cc = Chart2D.Builder.construct()
                .styleBar()
                .addPoint(Color.RED, 1.5)
                .build();

        final Chart2D d = new Chart2D(1.0, 2.0, Chart.PIE, new Point2D(0.1, 0.1), new Point2D(Integer.MAX_VALUE, 2.0), Collections.singleton(new Point2D(Color.RED.toInt(), 1.5)));
        final Chart2D dd = Chart2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .style(Chart.PIE)
                .minimum(new Point2D(0.1, 0.1))
                .maximum(new Point2D(Integer.MAX_VALUE, 2.0))
                .points(new Point2D(Color.RED.toInt(), 1.5))
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
        Assert.assertEquals(d, dd);
    }
}
