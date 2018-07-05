package gov.pnnl.svf.core.lookup;

import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Amelia Bleeker
 *
 */
public class MultiLookupProviderImplTest {

    public MultiLookupProviderImplTest() {
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.lookup.MultiLookupProviderImpl#lookupAll(java.lang.Class)}.
     */
    @Test
    public void testLookupAll() {
        final MultiLookupProvider lookup = new MultiLookupProviderImpl();
        Assert.assertNotNull(lookup);
        // add a bunch of strings to the lookup
        lookup.add("one");
        lookup.add("two");
        lookup.add("three");
        // the returned list should have three elements in it
        final Set<String> list = lookup.lookupAll(String.class);
        Assert.assertNotNull(list);
        Assert.assertEquals(3, list.size());
        // single lookup should return the last value inserted
        Assert.assertEquals("three", lookup.lookup(String.class));
        // remove a string and then check the count
        Assert.assertTrue(lookup.remove("two"));
        final Set<String> list2 = lookup.lookupAll(String.class);
        Assert.assertNotNull(list2);
        Assert.assertEquals(2, list2.size());
        // lookup all should never return a null value, only empty lists
        final Set<PropertyChangeSupport> list3 = lookup.lookupAll(PropertyChangeSupport.class);
        Assert.assertNotNull(list3);
        Assert.assertEquals(0, list3.size());
    }

    /**
     * Test lookup all
     */
    public void testModifyReturnedList() {
        final MultiLookupProvider lookup = new MultiLookupProviderImpl();
        Assert.assertNotNull(lookup);
        // add a bunch of strings to the lookup
        lookup.add("one");
        lookup.add("two");
        lookup.add("three");
        // the returned list should have three elements in it
        final Set<String> list = lookup.lookupAll(String.class);
        Assert.assertNotNull(list);
        list.clear();
        Assert.assertFalse(lookup.lookupAll(String.class).isEmpty());
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.lookup.MultiLookupProviderImpl#add(java.lang.Object)}.
     */
    @Test
    public void testAdd() {
        final MultiLookupProvider lookup = new MultiLookupProviderImpl();
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
     * {@link gov.pnnl.canopy.jogl.ui.lookup.MultiLookupProviderImpl#addAll(java.util.Collection)}.
     */
    @Test
    public void testAddAll() {
        final MultiLookupProvider lookup = new MultiLookupProviderImpl();
        Assert.assertNotNull(lookup);
        // lookup of a random object should return null
        Assert.assertNull(lookup.lookup(Object.class));
        // add a string to the lookup, the string should be returned when looked up
        final List<String> strings = Arrays.asList("test-string", "another-test-string");
        lookup.addAll(strings);
        final String found = lookup.lookup(String.class);
        Assert.assertNotNull(found);
        Assert.assertEquals(strings.get(1), found);
        // add a new string, this one should replace the existing one
        lookup.addAll(strings);
        final String found2 = lookup.lookup(String.class);
        Assert.assertNotNull(found2);
        Assert.assertEquals(strings.get(1), found2);
        // add a propertychangesupport class
        final List<PropertyChangeSupport> pcs = Arrays.asList(new PropertyChangeSupport(this));
        lookup.addAll(pcs);
        final PropertyChangeSupport found3 = lookup.lookup(PropertyChangeSupport.class);
        Assert.assertNotNull(found3);
        Assert.assertEquals(pcs.get(0), found3);
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.lookup.MultiLookupProviderImpl#remove(java.lang.Object)}.
     */
    @Test
    public void testRemove() {
        final MultiLookupProvider lookup = new MultiLookupProviderImpl();
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
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.lookup.MultiLookupProviderImpl#removeAll(java.util.Collection)}.
     */
    @Test
    public void testRemoveAll() {
        final MultiLookupProvider lookup = new MultiLookupProviderImpl();
        Assert.assertNotNull(lookup);
        // lookup of a string object should return null
        Assert.assertNull(lookup.lookup(String.class));
        // add a string to the lookup, the string should be returned when looked up
        final List<String> strings = Arrays.asList("test-string", "another-test-string");
        lookup.addAll(strings);
        final String found = lookup.lookup(String.class);
        Assert.assertNotNull(found);
        Assert.assertEquals(strings.get(1), found);
        // remove the string from the lookup
        final boolean removed = lookup.removeAll(strings);
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
        final MultiLookupProvider lookup = new MultiLookupProviderImpl();
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
