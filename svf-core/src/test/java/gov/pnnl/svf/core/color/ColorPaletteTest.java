package gov.pnnl.svf.core.color;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class ColorPaletteTest extends AbstractObjectTestBase<ColorPalette> {

    private final List<Color> COLORS = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE);
    private final Random random = new Random();

    public ColorPaletteTest() {
        acceptableCollisions = 1000;
    }

    /**
     * Test of peek method, of class ColorPalette.
     */
    @Test
    public void testConstructor() {
        final List<ColorImpl> colors = new ArrayList<>();
        for (final Color color : COLORS) {
            colors.add(new ColorImpl(color));
        }
        final ColorPalette a = new ColorPalette(COLORS);
        final ColorPalette b = new ColorPalette(colors);
        for (int i = 0; i < COLORS.size(); i++) {
            Assert.assertEquals(a.next().toInt(), b.next().toInt());
        }
    }

    /**
     * Test of peek method, of class ColorPalette.
     */
    @Test
    public void testPeek() {
        final ColorPalette a = new ColorPalette(COLORS);
        Assert.assertEquals(Color.RED, a.next());
        Assert.assertEquals(Color.RED, a.peek());
        Assert.assertEquals(Color.GREEN, a.next());
        Assert.assertEquals(Color.GREEN, a.peek());
        Assert.assertEquals(Color.BLUE, a.next());
        Assert.assertEquals(Color.BLUE, a.peek());
        Assert.assertEquals(Color.RED, a.next());
        Assert.assertEquals(Color.RED, a.peek());
    }

    /**
     * Test of next method, of class ColorPalette.
     */
    @Test
    public void testNext() {
        final ColorPalette a = new ColorPalette(COLORS);
        Assert.assertEquals(Color.RED, a.next());
        Assert.assertEquals(Color.GREEN, a.next());
        Assert.assertEquals(Color.BLUE, a.next());
        Assert.assertEquals(Color.RED, a.next());
    }

    /**
     * Test of getColors method, of class ColorPalette.
     */
    @Test
    public void testGetColors() {
        final ColorPalette a = new ColorPalette(COLORS);
        final ColorPalette b = new ColorPalette(Color.RED, Color.GREEN, Color.BLUE);
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.getColors(), b.getColors());
    }

    @Override
    protected ColorPalette copyValueObject(final ColorPalette object) {
        return new ColorPalette(object.getColors());
    }

    @Override
    protected ColorPalette newValueObject() {
        final int size = random.nextInt(1000) + 5;
        final Set<Color> colors = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            colors.add(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
        }
        return new ColorPalette(colors);
    }

    @Override
    protected void setFieldsToNull(final ColorPalette object) {
        // no operation
    }

    private static class ColorImpl extends Color {

        private ColorImpl(Color color) {
            super(color, color.getAlpha());
        }

    }
}
