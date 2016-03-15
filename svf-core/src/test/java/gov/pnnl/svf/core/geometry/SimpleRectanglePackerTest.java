package gov.pnnl.svf.core.geometry;

import java.awt.Point;
import java.awt.Rectangle;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class SimpleRectanglePackerTest {

    public SimpleRectanglePackerTest() {
    }

    /**
     * Test of pack method, of class SimpleRectanglePacker.
     */
    @Test
    public void testPack() {
        final RectanglePacker packer = new SimpleRectanglePacker(35, 25);
        Assert.assertEquals(new Point(0, 0), packer.pack(10, 10));
        Assert.assertEquals(new Point(10, 0), packer.pack(5, 5));
        Assert.assertEquals(new Point(15, 0), packer.pack(9, 9));
        Assert.assertEquals(new Point(24, 0), packer.pack(7, 4));
        Assert.assertEquals(new Point(31, 0), packer.pack(3, 3));
        Assert.assertEquals(new Rectangle(0, 0, 34, 10), packer.getActualArea());
        Assert.assertEquals(new Rectangle(34, 0, 1, 25), packer.getAvailableArea());
        Assert.assertEquals(new Point(0, 10), packer.pack(5, 5));
        Assert.assertEquals(new Point(5, 10), packer.pack(20, 10));
        Assert.assertEquals(new Rectangle(0, 0, 34, 20), packer.getActualArea());
        Assert.assertEquals(new Rectangle(25, 10, 10, 15), packer.getAvailableArea());
        Assert.assertNull(packer.pack(100, 100));
    }

    /**
     * Test of getMaximumArea method, of class SimpleRectanglePacker.
     */
    @Test
    public void testGetMaximumArea() {
        final RectanglePacker packer = new SimpleRectanglePacker(100, 100);
        Assert.assertEquals(new Rectangle(0, 0, 100, 100), packer.getMaximumArea());
        packer.pack(10, 10);
        Assert.assertEquals(new Rectangle(0, 0, 100, 100), packer.getMaximumArea());
    }

    /**
     * Test of getActualArea method, of class SimpleRectanglePacker.
     */
    @Test
    public void testGetActualArea() {
        final RectanglePacker packer = new SimpleRectanglePacker(100, 100);
        Assert.assertEquals(new Rectangle(0, 0, 0, 0), packer.getActualArea());
        packer.pack(10, 10);
        Assert.assertEquals(new Rectangle(0, 0, 10, 10), packer.getActualArea());
    }

    /**
     * Test of getAvailableArea method, of class SimpleRectanglePacker.
     */
    @Test
    public void testGetAvailableArea() {
        final RectanglePacker packer = new SimpleRectanglePacker(100, 100);
        Assert.assertEquals(new Rectangle(0, 0, 100, 100), packer.getAvailableArea());
        packer.pack(10, 10);
        Assert.assertEquals(new Rectangle(10, 0, 90, 100), packer.getAvailableArea());
    }

}
