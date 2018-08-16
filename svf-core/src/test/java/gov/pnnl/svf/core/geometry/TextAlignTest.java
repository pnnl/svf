package gov.pnnl.svf.core.geometry;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class TextAlignTest {

    public TextAlignTest() {
    }

    /**
     * Test of isLeft method, of class TextAlign.
     */
    @Test
    public void testIsLeft() {
        for (final TextAlign alignment : TextAlign.values()) {
            if (alignment.toString().contains("LEFT")) {
                Assert.assertTrue(alignment.isLeft());
            } else {
                Assert.assertFalse(alignment.isLeft());
            }
        }
    }

    /**
     * Test of isRight method, of class TextAlign.
     */
    @Test
    public void testIsRight() {
        for (final TextAlign alignment : TextAlign.values()) {
            if (alignment.toString().contains("RIGHT")) {
                Assert.assertTrue(alignment.isRight());
            } else {
                Assert.assertFalse(alignment.isRight());
            }
        }
    }

    /**
     * Test of isJustify method, of class TextAlign.
     */
    @Test
    public void testIsJustify() {
        for (final TextAlign alignment : TextAlign.values()) {
            if (alignment.toString().contains("JUSTIFY")) {
                Assert.assertTrue(alignment.isJustify());
            } else {
                Assert.assertFalse(alignment.isJustify());
            }
        }
    }

    /**
     * Test of containsTextAlign method, of class TextAlign.
     */
    @Test
    public void testContainsTextAlign() {
        for (final TextAlign alignment : TextAlign.values()) {
            for (final TextAlign test : TextAlign.values()) {
                final String[] parts = test.toString().split("_");
                for (final String part : parts) {
                    final TextAlign contains = TextAlign.valueOf(part);
                    if (alignment.toString().contains(part)) {
                        Assert.assertTrue(alignment + " should contain " + contains, alignment.containsTextAlign(contains));
                    } else {
                        Assert.assertFalse(alignment + " shouldn't contain " + contains, alignment.containsTextAlign(contains));
                    }
                }
            }
        }
    }

}
