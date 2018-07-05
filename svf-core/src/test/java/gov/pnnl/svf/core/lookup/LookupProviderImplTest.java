package gov.pnnl.svf.core.lookup;

import java.beans.PropertyChangeSupport;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Amelia Bleeker
 *
 */
public class LookupProviderImplTest {

    public LookupProviderImplTest() {
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.lookup.LookupProviderImpl#add(java.lang.Object)}.
     */
    @Test
    public void testAdd() {
        final LookupProvider lookup = new LookupProviderImpl();
        Assert.assertNotNull(lookup);
        // lookup of a random object should return null
        Assert.assertNull(lookup.lookup(Object.class));
        // add a string to the lookup, the string should be returned when looked up
        final String string = "test-string";
        lookup.add(string);
        final String found = lookup.lookup(String.class);
        Assert.assertNotNull(found);
        Assert.assertEquals(string, found);
        // add a new string, this one should replace the existing one
        final String string2 = "another-test-string";
        lookup.add(string2);
        final String found2 = lookup.lookup(String.class);
        Assert.assertNotNull(found2);
        Assert.assertEquals(string2, found2);
        // add a propertychangesupport class
        final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        lookup.add(pcs);
        final PropertyChangeSupport found3 = lookup.lookup(PropertyChangeSupport.class);
        Assert.assertNotNull(found3);
        Assert.assertEquals(pcs, found3);
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.lookup.LookupProviderImpl#remove(java.lang.Object)}.
     */
    @Test
    public void testRemove() {
        final LookupProvider lookup = new LookupProviderImpl();
        Assert.assertNotNull(lookup);
        // lookup of a string object should return null
        Assert.assertNull(lookup.lookup(String.class));
        // add a string to the lookup, the string should be returned when looked up
        final String string = "test-string";
        lookup.add(string);
        final String found = lookup.lookup(String.class);
        Assert.assertNotNull(found);
        Assert.assertEquals(string, found);
        // remove the string from the lookup
        final boolean removed = lookup.remove(string);
        Assert.assertTrue(removed);
        // look for a string type in the lookup
        final String found2 = lookup.lookup(String.class);
        Assert.assertNull(found2);
        // removing a type not in the lookup should return false
        final boolean removed2 = lookup.remove(new PropertyChangeSupport(this));
        Assert.assertFalse(removed2);
    }

    /**
     * Tests whether the lookup removes the object from all lookup lists.
     */
    @Test
    public void testOne() {
        final LookupProvider lookup = new LookupProviderImpl();
        Assert.assertNotNull(lookup);
        // create a simple object type
        final Object simple = new Object();
        // initial set should contain nothing
        Assert.assertNull(lookup.lookup(Object.class));
        // add the simple object and do a lookup
        lookup.add(simple);
        Assert.assertNotNull(lookup.lookup(Object.class));
        lookup.remove(simple);
        Assert.assertNull(lookup.lookup(Object.class));
        // create a complex object type
        final Foo complex = new Foo();
        // initial lookups should be null
        Assert.assertNull(lookup.lookup(Object.class));
        Assert.assertNull(lookup.lookup(Foo.class));
        Assert.assertNull(lookup.lookup(Bar.class));
        Assert.assertNull(lookup.lookup(Ed.class));
        Assert.assertNull(lookup.lookup(Y.class));
        // add the complex object type and do a lookup
        lookup.add(complex);
        Assert.assertNotNull(lookup.lookup(Object.class));
        Assert.assertNotNull(lookup.lookup(Foo.class));
        Assert.assertNotNull(lookup.lookup(Bar.class));
        Assert.assertNotNull(lookup.lookup(Ed.class));
        Assert.assertNotNull(lookup.lookup(Y.class));
        lookup.remove(complex);
        Assert.assertNull(lookup.lookup(Object.class));
        Assert.assertNull(lookup.lookup(Foo.class));
        Assert.assertNull(lookup.lookup(Bar.class));
        Assert.assertNull(lookup.lookup(Ed.class));
        Assert.assertNull(lookup.lookup(Y.class));
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
