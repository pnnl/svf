package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.awt.Font;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import junit.framework.Assert;
import org.apache.commons.collections.primitives.RandomAccessDoubleList;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Text2DTest extends AbstractObjectTestBase<Text2D> {

    private final Random random = new Random();

    public Text2DTest() {
        ignore.put(RandomAccessDoubleList.class.getName(), Collections.singletonList("_modCount"));
    }

    @Test
    public void testEmptyBounds() {
        final Text2D a = new Text2D("");
        Assert.assertEquals(0.0, a.getWidth());
        Assert.assertEquals(0.0, a.getHeight());
    }

    @Test
    public void testStringBounds() {
        final Text2D a = new Text2D("Lorem ipsum dolor sit amet...");
        Assert.assertEquals(a.getOffsets().get(a.getOffsets().size() - 1), a.getWidth(), a.getFont().getSize());
        Assert.assertEquals(14.0, a.getHeight(), 4.0);
    }

    @Override
    protected Text2D copyValueObject(final Text2D object) {
        return new Text2D(object.getX(), object.getY(), object.getFont(), object.getText());
    }

    @Override
    protected Text2D newValueObject() {
        return new Text2D(random.nextInt(), random.nextInt(), UUID.randomUUID().toString());
    }

    @Override
    protected void setFieldsToNull(final Text2D object) {
        // no fields to set to null
    }

    @Test
    public void testText2DBuilder() {
        final Text2D a = new Text2D();
        final Text2D aa = Text2D.Builder.construct()
                .build();

        final Text2D b = new Text2D(1.0, 2.0);
        final Text2D bb = Text2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .build();

        final Text2D c = new Text2D(1.0, 2.0, "text");
        final Text2D cc = Text2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .text("text")
                .build();

        final Text2D d = new Text2D(1.0, 2.0, new Font("Arial", Font.BOLD, 123), "text");
        final Text2D dd = Text2D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .text("text")
                .font(new Font("Arial", Font.BOLD, 123))
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
        Assert.assertEquals(d, dd);
    }
}
