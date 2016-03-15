package gov.pnnl.svf.geometry;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.awt.Font;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import org.junit.Assert;
import org.apache.commons.collections.primitives.RandomAccessDoubleList;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Text3DTest extends AbstractObjectTestBase<Text3D> {

    private final Random random = new Random();

    public Text3DTest() {
        ignore.put(RandomAccessDoubleList.class.getName(), Collections.singletonList("_modCount"));
    }

    @Test
    public void testEmptyBounds() {
        final Text3D a = new Text3D("");
        Assert.assertEquals(0.0, a.getWidth());
        Assert.assertEquals(0.0, a.getHeight());
    }

    @Test
    public void testStringBounds() {
        final Text3D a = new Text3D("Lorem ipsum dolor sit amet...");
        Assert.assertEquals(a.getOffsets().get(a.getOffsets().size() - 1), a.getWidth(), a.getFont().getSize() * Text3D.TEXT_SCALE);
        Assert.assertEquals(36.0 * Text3D.TEXT_SCALE, a.getHeight(), 4.0);
    }

    @Override
    protected Text3D copyValueObject(final Text3D object) {
        return new Text3D(object.getX(), object.getY(), object.getZ(), object.getFont(), object.getText());
    }

    @Override
    protected Text3D newValueObject() {
        return new Text3D(random.nextInt(), random.nextInt(), random.nextInt(), UUID.randomUUID().toString());
    }

    @Override
    protected void setFieldsToNull(final Text3D object) {
        // no fields to set to null
    }

    @Test
    public void testText3DBuilder() {
        final Text3D a = new Text3D();
        final Text3D aa = Text3D.Builder.construct()
                .build();

        final Text3D b = new Text3D(1.0, 2.0, 3.0);
        final Text3D bb = Text3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .build();

        final Text3D c = new Text3D(1.0, 2.0, 3.0, "text");
        final Text3D cc = Text3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .text("text")
                .build();

        final Text3D d = new Text3D(1.0, 2.0, 3.0, new Font("Arial", Font.BOLD, 123), "text");
        final Text3D dd = Text3D.Builder.construct()
                .x(1.0)
                .y(2.0)
                .z(3.0)
                .text("text")
                .font(new Font("Arial", Font.BOLD, 123))
                .build();

        Assert.assertEquals(a, aa);
        Assert.assertEquals(b, bb);
        Assert.assertEquals(c, cc);
        Assert.assertEquals(d, dd);
    }
}
