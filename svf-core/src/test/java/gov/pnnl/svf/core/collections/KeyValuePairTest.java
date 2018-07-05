package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class KeyValuePairTest extends AbstractObjectTestBase<KeyValuePair<String, Integer>> {

    private final Random random = new Random();

    public KeyValuePairTest() {
    }

    /**
     * Test of getKey method, of class KeyValuePair.
     */
    @Test
    public void testGetKey() {
        final String key = "key";
        final Integer value = 137;
        final KeyValuePair<String, Integer> instance = new KeyValuePair<>(key, value);

        Assert.assertEquals(key, instance.getKey());
    }

    /**
     * Test of getValue method, of class KeyValuePair.
     */
    @Test
    public void testGetValue() {
        final String key = "key";
        final Integer value = 137;
        final KeyValuePair<String, Integer> instance = new KeyValuePair<>(key, value);

        Assert.assertEquals(value, instance.getValue());
    }

    /**
     * Test of getKey method, of class KeyValuePair.
     */
    @Test
    public void testGetKeyBuilder() {
        final String key = "key";
        final Integer value = 137;
        final KeyValuePair<String, Integer> instance = KeyValuePair.Builder.<String, Integer>construct()
                .key(key)
                .value(value)
                .build();

        Assert.assertEquals(key, instance.getKey());
    }

    /**
     * Test of getValue method, of class KeyValuePair.
     */
    @Test
    public void testGetValueBuilder() {
        final String key = "key";
        final Integer value = 137;
        final KeyValuePair<String, Integer> instance = KeyValuePair.Builder.<String, Integer>construct()
                .key(key)
                .value(value)
                .build();

        Assert.assertEquals(value, instance.getValue());
    }

    @Override
    protected KeyValuePair<String, Integer> copyValueObject(final KeyValuePair<String, Integer> object) {
        return new KeyValuePair<>(object.getKey(), object.getValue());
    }

    @Override
    protected KeyValuePair<String, Integer> newValueObject() {
        return new KeyValuePair<>(UUID.randomUUID().toString(), random.nextInt());
    }

    @Override
    protected void setFieldsToNull(final KeyValuePair<String, Integer> object) {
        // no setters
    }
}
