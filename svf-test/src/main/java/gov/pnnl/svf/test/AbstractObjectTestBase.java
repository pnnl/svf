package gov.pnnl.svf.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Base class for testing a value object. Provides helper functionality for
 * testing common methods such as equals and hashcode.
 *
 * @author Arthur Bleeker
 * @param <T> Object type to be tested
 *
 */
public abstract class AbstractObjectTestBase<T extends Object> {

    private static final Logger logger = Logger.getLogger(AbstractObjectTestBase.class.toString());
    /**
     * default number of times to iterate over consistency tests
     */
    protected static final int DEFAULT_ITERATE_LENGTH = 100;
    /**
     * default number of times to iterate over hash code collision tests
     */
    protected static final int DEFAULT_COLLISION_ITERATE_LENGTH = 100000;
    /**
     * default number of hash code collisions that are acceptable
     */
    protected static final int DEFAULT_ACCEPTABLE_COLLISIONS = 100;
    /**
     * default timeout in milliseconds for bound value object test
     */
    protected static final int DEFAULT_EVENT_TIMEOUT = 1000;
    /**
     * number of times to iterate over consistency tests
     */
    protected int iterateLength = DEFAULT_ITERATE_LENGTH;
    /**
     * number of times to iterate over hash code collision tests
     */
    protected int collisionIterateLength = DEFAULT_COLLISION_ITERATE_LENGTH;
    /**
     * number of hash code collisions that are acceptable
     */
    protected int acceptableCollisions = DEFAULT_ACCEPTABLE_COLLISIONS;
    /**
     * timeout in milliseconds for bound value object test
     */
    protected int eventTimeout = DEFAULT_EVENT_TIMEOUT;
    /**
     * map of class to field name to ignore when comparing fields during
     * serialization test
     */
    protected final Map<String, List<String>> ignore = new HashMap<>();

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(final Description description) {
            logger.info(MessageFormat.format("Starting test {0}", description.getMethodName()));
        }

        @Override
        protected void finished(final Description description) {
            logger.info(MessageFormat.format("Finished test {0}", description.getMethodName()));
        }
    };

    /**
     * test marshalling if serializable
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSerializable() throws Exception {
        // check for serializable interface
        // check serializable
        final T a = this.newValueObject();
        Assume.assumeTrue(a instanceof Serializable);
        final T copy;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(a);
            oos.flush();
            ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            copy = (T) ois.readObject();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (final IOException ex) {
                    System.out.println("Failed to close the ObjectOutputStream.");
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (final IOException ex) {
                    System.out.println("Failed to close the ObjectInputStream.");
                }
            }
        }
        // test equality
        final boolean result = WeakEqualsHelper.weakEquals(a, copy, ignore);
        Assert.assertTrue(WeakEqualsHelper.getErrors().toString(), result);
    }

    /**
     * test the equals method *
     */
    @Test
    public void testEqualsObject() {
        // set up the three objects that should be equal
        final T a = this.newValueObject();
        final T b = this.copyValueObject(a);
        final T c = this.copyValueObject(a);
        // null reference
        // not a bug since we want to ensure that passing null to an equals method will never throw an exception
        Assert.assertFalse(
                "equals method failed to return false for a null value.",
                a.equals(null));
        checkEqualsImpl(a, b, c);
        // different object reference
        Assert.assertFalse(
                "equals method failed to compare a different class object.",
                this.newValueObject().equals(new Object()));
    }

    /**
     * tests the equals method for unequal objects *
     */
    @Test
    public void testEqualsObjectDifferent() {
        // set up three objects that should not be equal
        final T a = this.newValueObject();
        final T b = this.newValueObject();
        final T c = this.newValueObject();
        checkNotEqualsImpl(a, b, c);
    }

    /**
     * test the equals method when all fields are set to null *
     */
    @Test
    public void testEqualsObjectNullFields() {
        // set up three objects with all null fields
        final T a = this.newValueObject();
        // set all of the fields to null
        this.setFieldsToNull(a);
        final T b = this.copyValueObject(a);
        final T c = this.copyValueObject(a);
        checkEqualsImpl(a, b, c);
    }

    /**
     * test the hashcode method *
     */
    @Test
    public void testHashCode() {
        // test hash code and equals method equality
        final T muppet = this.newValueObject();
        final long hash = muppet.hashCode();
        // consistent
        for (int i = 0; i < iterateLength; i++) {
            Assert.assertEquals("Failed hash code iterate consistency test.", hash,
                                muppet.hashCode());
        }
        // equals and hash are equal
        final T a = this.newValueObject();
        final T b = this.copyValueObject(a);
        Assert.assertEquals(
                "Failed equals comparison test.",
                a, b);
        Assert.assertEquals(
                "Failed hash code comparison test.",
                a.hashCode(), b.hashCode());
    }

    /**
     * tests the threshold for collisions in the hash code *
     */
    @Test
    public void testHashCodeCollisions() {
        // test hash collisions
        final Set<Integer> hashes = new HashSet<>();
        // add a bunch of different object hash codes to a set
        for (int i = 0; i < collisionIterateLength; i++) {
            hashes.add(this.newValueObject().hashCode());
        }
        // find the number of objects that failed to get added
        final int difference = collisionIterateLength - hashes.size();
        Assert.assertTrue(String.format(
                "Too many collisions in hash code (%d out of %d).", difference,
                collisionIterateLength), difference <= acceptableCollisions);
    }

    /**
     * This method should return a copy of the value object.
     *
     * @param object that should be copied
     *
     * @return a new copy of the value object
     */
    protected abstract T copyValueObject(final T object);

    /**
     * This factory method should return a new unique value object.
     *
     * @return a new unique value object
     */
    protected abstract T newValueObject();

    /**
     * This method should set all of the fields to null using the supplied
     * setters.
     *
     * @param object to set the fields to null
     */
    protected abstract void setFieldsToNull(T object);

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final Object value) {
        testBoundField(object, field, value, value.getClass());
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final int value) {
        testBoundField(object, field, value, int.class);
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final long value) {
        testBoundField(object, field, value, long.class);
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final short value) {
        testBoundField(object, field, value, short.class);
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final byte value) {
        testBoundField(object, field, value, byte.class);
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final double value) {
        testBoundField(object, field, value, double.class);
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final float value) {
        testBoundField(object, field, value, float.class);
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final boolean value) {
        testBoundField(object, field, value, boolean.class);
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     */
    protected void testBoundField(final T object, final String field, final char value) {
        testBoundField(object, field, value, char.class);
    }

    /**
     * Test a bound field by registering a listener and then verifying that the
     * field sets correctly and the listener gets fired. This test will work on
     * asynchronous events.
     *
     * @param object object with the bound field
     * @param field  name of the field to test
     * @param value  value to test the field with
     * @param clazz  the exact class type for the field
     */
    protected void testBoundField(final T object, final String field, final Object value, final Class<?> clazz) {
        try {
            // get value object methods and current value
            final String prefix = (value instanceof Boolean) ? "is" : "get";
            final Method getter = object.getClass().getMethod(prefix + field.substring(0, 1).toUpperCase(Locale.US) + field.substring(1, field.length()),
                                                              (Class<?>[]) null);
            final Method setter = object.getClass().getMethod("set" + field.substring(0, 1).toUpperCase(Locale.US) + field.substring(1, field.length()), clazz);
            final Object current = getter.invoke(object, (Object[]) null);
            // event has been fired
            final CountDownLatch latch = new CountDownLatch(1);
            // event criteria is correct
            final AtomicBoolean passed = new AtomicBoolean(false);
            // event listener
            final PropertyChangeListener listener = new PropertyChangeListener() {
                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    try {
                        if (object == evt.getSource() && field.equals(evt.getPropertyName())
                            && current == null ? evt.getOldValue() == null : current.equals(evt.getOldValue())
                                                                             && value == null ? evt.getNewValue() == null : value.equals(evt.getNewValue())) {
                            passed.set(true);
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            };
            if (!passed.get()) {
                try {
                    // classes that expose property change support through delegate methods
                    final Method add = object.getClass().getMethod("addPropertyChangeListener", String.class, PropertyChangeListener.class);
                    final Method remove = object.getClass().getMethod("removePropertyChangeListener", String.class, PropertyChangeListener.class);
                    add.invoke(object, field, listener);
                    setter.invoke(object, value);
                    remove.invoke(object, field, listener);
                } catch (final NoSuchMethodException ex) {
                    // ignore and look for next method
                    logger.log(Level.FINE,
                               "Methods addPropertyChangeListener or removePropertyChangeListener not found.  Looking for next observable method type...");
                } catch (final SecurityException ex) {
                    // ignore and look for next method
                    logger.log(Level.FINE,
                               "Unable to access methods addPropertyChangeListener or removePropertyChangeListener.  Looking for next observable method type...");
                }
            }
            if (!passed.get()) {
                try {
                    // classes that expose property change support through getter
                    final Method support = object.getClass().getMethod("getPropertyChangeSupport", (Class<?>[]) null);
                    final PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) support.invoke(object, (Object[]) null);
                    propertyChangeSupport.addPropertyChangeListener(field, listener);
                    setter.invoke(object, value);
                    propertyChangeSupport.removePropertyChangeListener(field, listener);
                } catch (final NoSuchMethodException ex) {
                    // ignore and look for next method
                    logger.log(Level.FINE, "Method getPropertyChangeSupport not found.  Looking for next observable method type...");
                } catch (final SecurityException ex) {
                    // ignore and look for next method
                    logger.log(Level.FINE, "Unable to access method getPropertyChangeSupport.  Looking for next observable method type...");
                }
            }
            // wait for listener
            final long start = System.currentTimeMillis();
            while (latch.getCount() > 0 && (start + eventTimeout) > System.currentTimeMillis()) {
                try {
                    if (latch.await(10, TimeUnit.MILLISECONDS)) {
                        // timed  out 
                    }
                } catch (final InterruptedException ex) {
                    // ignore thread inturuption
                }
            }
            // check result
            Assert.assertTrue(passed.get());
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
            Assert.fail();
        }
    }

    private void checkEqualsImpl(final T a, final T b, final T c) {
        // reflexive
        Assert.assertTrue("equals method failed reflexive test a.equals(a).", a.equals(a));
        // symmetric
        Assert.assertTrue("equals method failed symmetric test a.equals(b).", a.equals(b));
        Assert.assertTrue("equals method failed symmetric test b.equals(a).", b.equals(a));
        // transitive
        Assert.assertTrue("equals method failed transitive test a.equals(b).", a.equals(b));
        Assert.assertTrue("equals method failed transitive test b.equals(c).", b.equals(c));
        Assert.assertTrue("equals method failed transitive test a.equals(c).", a.equals(c));
        // consistent
        for (int i = 0; i < iterateLength; i++) {
            Assert.assertTrue("equals method failed consistent test a.equals(b) on iteration " + i + ".", a.equals(b));
        }
    }

    private void checkNotEqualsImpl(final T a, final T b, final T c) {
        // reflexive
        Assert.assertTrue("equals method failed reflexive test !a.equals(a).", a.equals(a));
        // symmetric
        Assert.assertFalse("equals method failed symmetric test !a.equals(b).", a.equals(b));
        Assert.assertFalse("equals method failed symmetric test !b.equals(a).", b.equals(a));
        // transitive
        Assert.assertFalse("equals method failed transitive test !a.equals(b).", a.equals(b));
        Assert.assertFalse("equals method failed transitive test !b.equals(c).", b.equals(c));
        Assert.assertFalse("equals method failed transitive test !a.equals(c).", a.equals(c));
        // consistent
        for (int i = 0; i < iterateLength; i++) {
            Assert.assertFalse("equals method failed consistent test !a.equals(b) on iteration " + i + ".", a.equals(b));
        }
    }
}
