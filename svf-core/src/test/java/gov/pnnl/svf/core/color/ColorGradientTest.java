package gov.pnnl.svf.core.color;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class ColorGradientTest extends AbstractObjectTestBase<ColorGradient> {

    private static final int MAX_ENTRIES = 100;
    private final ColorEntryTest colorEntryTest = new ColorEntryTest();
    private final Random random = new Random();

    public ColorGradientTest() {
    }

    @Test
    public void testGetColorOne() {
        final ColorGradient instance = new ColorGradient();
        // anywhere in the gradient should be white for a default gradient
        Assert.assertEquals(Color.WHITE, instance.getColor(0.0));
        Assert.assertEquals(Color.WHITE, instance.getColor(0.5));
        Assert.assertEquals(Color.WHITE, instance.getColor(0.234565));
        Assert.assertEquals(Color.WHITE, instance.getColor(1.0));
    }

    @Test
    public void testGetColorTwo() {
        final ColorGradient instance = new ColorGradient(new ColorEntry(0.2343243, Color.WHITE));
        // anywhere in the gradient should be white for this gradient
        Assert.assertEquals(Color.WHITE, instance.getColor(0.0));
        Assert.assertEquals(Color.WHITE, instance.getColor(0.5));
        Assert.assertEquals(Color.WHITE, instance.getColor(0.234565));
        Assert.assertEquals(Color.WHITE, instance.getColor(1.0));
    }

    @Test
    public void testGetColorThree() {
        final ColorGradient instance = new ColorGradient(ColorEntry.INDIGO);
        // anywhere in the gradient should be white for this gradient
        Assert.assertEquals(Color.INDIGO, instance.getColor(0.0));
        Assert.assertEquals(Color.INDIGO, instance.getColor(0.5));
        Assert.assertEquals(Color.INDIGO, instance.getColor(0.234565));
        Assert.assertEquals(Color.INDIGO, instance.getColor(1.0));
    }

    @Test
    public void testGetColorFour() {
        final ColorGradient instance = new ColorGradient(
                new ColorEntry(0.0, Color.RED),
                new ColorEntry(0.2, Color.ORANGE),
                new ColorEntry(0.4, Color.YELLOW),
                new ColorEntry(0.6, Color.GREEN),
                new ColorEntry(0.8, Color.BLUE),
                new ColorEntry(0.8, Color.INDIGO),
                new ColorEntry(1.0, Color.VIOLET));
        // pick something between red and orange
        Assert.assertEquals(Color.RED, instance.getColor(0.0));
        Assert.assertEquals(new Color(1.0f, 0.24901961f, 0.0f), instance.getColor(0.1));
        Assert.assertEquals(Color.VIOLET, instance.getColor(1.0));
    }

    @Test
    public void testGetColorRed() {
        final ColorGradient instance = new ColorGradient(ColorEntry.WHITE,
                                                         new ColorEntry(1.0, Color.RED));
        // anywhere in the gradient should be white for this gradient
        Assert.assertEquals(Color.WHITE, instance.getColor(0.0));
        Assert.assertEquals(new Color(1.0f, 0.5f, 0.5f, 1.0f), instance.getColor(0.5));
        Assert.assertEquals(Color.RED, instance.getColor(1.0));
    }

    @Test
    public void testGetColorGreen() {
        final ColorGradient instance = new ColorGradient(ColorEntry.WHITE,
                                                         new ColorEntry(1.0, Color.GREEN));
        // anywhere in the gradient should be white for this gradient
        Assert.assertEquals(Color.WHITE, instance.getColor(0.0));
        Assert.assertEquals(new Color(0.5f, 1.0f, 0.5f, 1.0f), instance.getColor(0.5));
        Assert.assertEquals(Color.GREEN, instance.getColor(1.0));
    }

    @Test
    public void testGetColorBlue() {
        final ColorGradient instance = new ColorGradient(ColorEntry.WHITE,
                                                         new ColorEntry(1.0, Color.BLUE));
        // anywhere in the gradient should be white for this gradient
        Assert.assertEquals(Color.WHITE, instance.getColor(0.0));
        Assert.assertEquals(new Color(0.5f, 0.5f, 1.0f, 1.0f), instance.getColor(0.5));
        Assert.assertEquals(Color.BLUE, instance.getColor(1.0));
    }

    @Test
    public void testGetColorAlpha() {
        final ColorGradient instance = new ColorGradient(ColorEntry.WHITE,
                                                         new ColorEntry(1.0, new Color(1.0f, 1.0f, 1.0f, 0.0f)));
        // anywhere in the gradient should be white for this gradient
        Assert.assertEquals(Color.WHITE, instance.getColor(0.0));
        Assert.assertEquals(new Color(1.0f, 1.0f, 1.0f, 0.5f), instance.getColor(0.5));
        Assert.assertEquals(new Color(1.0f, 1.0f, 1.0f, 0.0f), instance.getColor(1.0));
    }

    @Override
    protected ColorGradient copyValueObject(final ColorGradient object) {
        final ColorGradient copy = new ColorGradient(object);
        return copy;
    }

    @Override
    protected ColorGradient newValueObject() {
        final List<ColorEntry> temp = new ArrayList<>();
        for (int i = 0; i < random.nextInt(MAX_ENTRIES - 10) + 10; i++) {
            temp.add(colorEntryTest.newValueObject());
        }
        final ColorGradient instance = new ColorGradient(temp);
        return instance;
    }

    @Override
    protected void setFieldsToNull(final ColorGradient object) {
        // no operation
    }
}
