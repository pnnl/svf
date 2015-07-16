package gov.pnnl.svf.core.color;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ColorEntryTest extends AbstractObjectTestBase<ColorEntry> {

    private final Random random = new Random();

    public ColorEntryTest() {
    }

    /**
     * Test of getColor method, of class ColorEntry.
     */
    @Test
    public void testGetColor() {
        final Color expResult = new Color(0.1f, 0.2f, 0.3f, 0.4f);
        final ColorEntry instance = new ColorEntry(0.5, expResult);
        final Color result = instance.getColor();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, of class ColorEntry.
     */
    @Test
    public void testGetValue() {
        final double expResult = 0.534;
        final ColorEntry instance = new ColorEntry(expResult, Color.WHITE);
        final double result = instance.getValue();
        Assert.assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getColor method, of class ColorEntry.
     */
    @Test
    public void testGetColorBuilder() {
        final Color expResult = new Color(0.1f, 0.2f, 0.3f, 0.4f);
        final ColorEntry instance = ColorEntry.Builder.construct()
                .value(0.5)
                .color(expResult)
                .build();
        final Color result = instance.getColor();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, of class ColorEntry.
     */
    @Test
    public void testGetValueBuilder() {
        final double expResult = 0.534;
        final ColorEntry instance = ColorEntry.Builder.construct()
                .value(expResult)
                .color(Color.WHITE)
                .build();
        final double result = instance.getValue();
        Assert.assertEquals(expResult, result, 0.0);
    }

    @Override
    protected ColorEntry copyValueObject(final ColorEntry object) {
        return new ColorEntry(object.getValue(), object.getColor());
    }

    @Override
    protected ColorEntry newValueObject() {
        return new ColorEntry(random.nextDouble(), new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()));
    }

    @Override
    protected void setFieldsToNull(final ColorEntry object) {
        // no mutable fields
    }
}
