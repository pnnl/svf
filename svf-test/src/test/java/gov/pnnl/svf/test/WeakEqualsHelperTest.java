package gov.pnnl.svf.test;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class WeakEqualsHelperTest {

    public WeakEqualsHelperTest() {
    }

    /**
     * Test of weakEquals method, of class WeakEqualsHelper.
     */
    @Test
    public void testWeakEquals_1() {
        final Object o1 = new Foo("text", 100, 1.0, null);
        final Object o2 = new Foo("text", 100, 1.0, 0.0);
        Assert.assertTrue(WeakEqualsHelper.weakEquals(o1, o2, true));
    }

    /**
     * Test of weakEquals method, of class WeakEqualsHelper.
     */
    @Test
    public void testWeakEquals_2() {
        final Object o1 = new Foo("text", 100, 1.0, 0.0);
        final Object o2 = new Foo("text", 100, 1.0, 0.0);
        Assert.assertTrue(WeakEqualsHelper.weakEquals(o1, o2));
    }

    /**
     * Test of weakEquals method, of class WeakEqualsHelper.
     */
    @Test
    public void testWeakEquals_3() {
        final Object o1 = new Foo("text", 100, 1.0, 0.0);
        final Object o2 = new Foo("text", 100, 1.1, 0.0);
        Assert.assertFalse(WeakEqualsHelper.weakEquals(o1, o2));
    }

    public static class Foo {

        private final String a;
        private int b;
        private Double c;
        private transient Double d;

        public Foo(String a, int b, Double c, Double d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

    }
}
