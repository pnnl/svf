package gov.pnnl.svf.core.lookup;

import java.util.Objects;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class LookupProviderFactoryTest {

    public LookupProviderFactoryTest() {
    }

    /**
     * Test of newLookupProvider method, of class LookupProviderFactory.
     */
    @Test
    public void testNewLookupProvider() {
        // not null
        Assert.assertNotNull(LookupProviderFactory.newLookupProvider());
        // new instance
        Assert.assertTrue(LookupProviderFactory.newLookupProvider() != LookupProviderFactory.newLookupProvider());
    }

    /**
     * Test of newLookupProvider method, of class LookupProviderFactory.
     */
    @Test
    public void testNewLookupProviderCopy() {
        final String string = "test-string";
        final Foo complex = new Foo();
        final LookupProvider lp = LookupProviderFactory.newLookupProvider();
        lp.add(string);
        lp.add(complex);
        final LookupProvider lpc = LookupProviderFactory.newLookupProvider(lp);
        Assert.assertEquals(lp.lookupAll(), lpc.lookupAll());
        Assert.assertEquals(lp.lookup(Object.class), lpc.lookup(Object.class));
        Assert.assertEquals(lp.lookup(String.class), lpc.lookup(String.class));
        Assert.assertEquals(lp.lookup(Foo.class), lpc.lookup(Foo.class));
        Assert.assertEquals(lp.lookup(Bar.class), lpc.lookup(Bar.class));
        Assert.assertEquals(lp.lookup(Ed.class), lpc.lookup(Ed.class));
        Assert.assertEquals(lp.lookup(Y.class), lpc.lookup(Y.class));
        // ensure that copy doesn't change
        lp.remove(string);
        final Set<Object> a = lp.lookupAll();
        final Set<Object> b = lpc.lookupAll();
        Assert.assertFalse(Objects.equals(a, b));
    }

    /**
     * Test of newMultiLookupProvider method, of class LookupProviderFactory.
     */
    @Test
    public void testNewMultiLookupProvider() {
        // not null
        Assert.assertNotNull(LookupProviderFactory.newMultiLookupProvider());
        // new instance
        Assert.assertTrue(LookupProviderFactory.newMultiLookupProvider() != LookupProviderFactory.newMultiLookupProvider());
    }

    /**
     * Test of newMultiLookupProvider method, of class LookupProviderFactory.
     */
    @Test
    public void testNewMultiLookupProviderCopy() {
        final Object simple = new Object();
        final String string = "test-string";
        final Foo complex = new Foo();
        final MultiLookupProvider mlp = LookupProviderFactory.newMultiLookupProvider();
        mlp.add(simple);
        mlp.add(string);
        mlp.add(complex);
        final MultiLookupProvider mlpc = LookupProviderFactory.newMultiLookupProvider(mlp);
        Assert.assertEquals(mlp.lookupAll(), mlpc.lookupAll());
        Assert.assertEquals(mlp.lookup(Object.class), mlpc.lookup(Object.class));
        Assert.assertEquals(mlp.lookupAll(Object.class), mlpc.lookupAll(Object.class));
        Assert.assertEquals(mlp.lookup(String.class), mlpc.lookup(String.class));
        Assert.assertEquals(mlp.lookupAll(String.class), mlpc.lookupAll(String.class));
        Assert.assertEquals(mlp.lookup(Foo.class), mlpc.lookup(Foo.class));
        Assert.assertEquals(mlp.lookupAll(Foo.class), mlpc.lookupAll(Foo.class));
        Assert.assertEquals(mlp.lookup(Bar.class), mlpc.lookup(Bar.class));
        Assert.assertEquals(mlp.lookupAll(Bar.class), mlpc.lookupAll(Bar.class));
        Assert.assertEquals(mlp.lookup(Ed.class), mlpc.lookup(Ed.class));
        Assert.assertEquals(mlp.lookupAll(Ed.class), mlpc.lookupAll(Ed.class));
        Assert.assertEquals(mlp.lookup(Y.class), mlpc.lookup(Y.class));
        Assert.assertEquals(mlp.lookupAll(Y.class), mlpc.lookupAll(Y.class));
        // ensure that copy doesn't change
        mlp.remove(simple);
        final Set<Object> a = mlp.lookupAll();
        final Set<Object> b = mlpc.lookupAll();
        Assert.assertFalse(Objects.equals(a, b));
    }

    public static class Foo extends Bar {
    }

    protected static class Bar implements Ed {
    }

    static interface Ed extends Y {
    }

    private static interface Y {
    }

}
