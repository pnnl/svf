package gov.pnnl.svf.core.geometry;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class AlignmentTest {

    public AlignmentTest() {
    }

    /**
     * Test of isLeft method, of class Alignment.
     */
    @Test
    public void testIsLeft() {
        for (final Alignment alignment : Alignment.values()) {
            if (alignment.toString().contains("LEFT")) {
                Assert.assertTrue(alignment.isLeft());
            } else {
                Assert.assertFalse(alignment.isLeft());
            }
        }
    }

    /**
     * Test of isRight method, of class Alignment.
     */
    @Test
    public void testIsRight() {
        for (final Alignment alignment : Alignment.values()) {
            if (alignment.toString().contains("RIGHT")) {
                Assert.assertTrue(alignment.isRight());
            } else {
                Assert.assertFalse(alignment.isRight());
            }
        }
    }

    /**
     * Test of isTop method, of class Alignment.
     */
    @Test
    public void testIsTop() {
        for (final Alignment alignment : Alignment.values()) {
            if (alignment.toString().contains("TOP")) {
                Assert.assertTrue(alignment.isTop());
            } else {
                Assert.assertFalse(alignment.isTop());
            }
        }
    }

    /**
     * Test of isBottom method, of class Alignment.
     */
    @Test
    public void testIsBottom() {
        for (final Alignment alignment : Alignment.values()) {
            if (alignment.toString().contains("BOTTOM")) {
                Assert.assertTrue(alignment.isBottom());
            } else {
                Assert.assertFalse(alignment.isBottom());
            }
        }
    }

    /**
     * Test of containsAlignment method, of class Alignment.
     */
    @Test
    public void testContainsAlignment() {
        for (final Alignment alignment : Alignment.values()) {
            for (final Alignment test : Alignment.values()) {
                final String[] parts = test.toString().split("_");
                for (final String part : parts) {
                    final Alignment contains = Alignment.valueOf(part);
                    if (alignment.toString().contains(part)) {
                        Assert.assertTrue(alignment + " should contain " + contains, alignment.containsAlignment(contains));
                    } else {
                        Assert.assertFalse(alignment + " shouldn't contain " + contains, alignment.containsAlignment(contains));
                    }
                }
            }
        }
    }

}
