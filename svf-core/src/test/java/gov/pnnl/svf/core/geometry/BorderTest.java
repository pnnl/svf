package gov.pnnl.svf.core.geometry;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class BorderTest {

    public BorderTest() {
    }

    /**
     * Test of getBorder method, of class Border.
     */
    @Test
    public void testGetBorder() {
        Assert.assertEquals(Border.ALL, Border.getBorder(true, true, true, true));
        Assert.assertEquals(Border.BOTTOM, Border.getBorder(false, false, false, true));
        Assert.assertEquals(Border.BOTTOM_LEFT, Border.getBorder(true, false, false, true));
        Assert.assertEquals(Border.BOTTOM_LEFT_RIGHT, Border.getBorder(true, true, false, true));
        Assert.assertEquals(Border.BOTTOM_RIGHT, Border.getBorder(false, true, false, true));
        Assert.assertEquals(Border.BOTTOM_TOP, Border.getBorder(false, false, true, true));
        Assert.assertEquals(Border.BOTTOM_TOP_LEFT, Border.getBorder(true, false, true, true));
        Assert.assertEquals(Border.BOTTOM_TOP_RIGHT, Border.getBorder(false, true, true, true));
        Assert.assertEquals(Border.LEFT, Border.getBorder(true, false, false, false));
        Assert.assertEquals(Border.LEFT_RIGHT, Border.getBorder(true, true, false, false));
        Assert.assertEquals(Border.NONE, Border.getBorder(false, false, false, false));
        Assert.assertEquals(Border.RIGHT, Border.getBorder(false, true, false, false));
        Assert.assertEquals(Border.TOP, Border.getBorder(false, false, true, false));
        Assert.assertEquals(Border.TOP_LEFT, Border.getBorder(true, false, true, false));
        Assert.assertEquals(Border.TOP_LEFT_RIGHT, Border.getBorder(true, true, true, false));
        Assert.assertEquals(Border.TOP_RIGHT, Border.getBorder(false, true, true, false));
    }

    /**
     * Test of isLeft method, of class Border.
     */
    @Test
    public void testIsLeft() {
        for (final Border border : Border.values()) {
            if (border.toString().contains("LEFT") || border.toString().contains("ALL")) {
                Assert.assertTrue(border.isLeft());
            } else {
                Assert.assertFalse(border.isLeft());
            }
        }
    }

    /**
     * Test of isRight method, of class Border.
     */
    @Test
    public void testIsRight() {
        for (final Border border : Border.values()) {
            if (border.toString().contains("RIGHT") || border.toString().contains("ALL")) {
                Assert.assertTrue(border.isRight());
            } else {
                Assert.assertFalse(border.isRight());
            }
        }
    }

    /**
     * Test of isTop method, of class Border.
     */
    @Test
    public void testIsTop() {
        for (final Border border : Border.values()) {
            if (border.toString().contains("TOP") || border.toString().contains("ALL")) {
                Assert.assertTrue(border.isTop());
            } else {
                Assert.assertFalse(border.isTop());
            }
        }
    }

    /**
     * Test of isBottom method, of class Border.
     */
    @Test
    public void testIsBottom() {
        for (final Border border : Border.values()) {
            if (border.toString().contains("BOTTOM") || border.toString().contains("ALL")) {
                Assert.assertTrue(border.isBottom());
            } else {
                Assert.assertFalse(border.isBottom());
            }
        }
    }

    /**
     * Test of containsBorder method, of class Border.
     */
    @Test
    public void testContainsBorder() {
        for (final Border border : Border.values()) {
            for (final Border test : Border.values()) {
                final String[] parts = test.toString().split("_");
                for (final String part : parts) {
                    final Border contains = Border.valueOf(part);
                    if (border.toString().contains(part) || (border == Border.ALL && contains != Border.NONE)) {
                        Assert.assertTrue(border + " should contain " + contains, border.containsBorder(contains));
                    } else {
                        Assert.assertFalse(border + " shouldn't contain " + contains, border.containsBorder(contains));
                    }
                }
            }
        }
    }

}
