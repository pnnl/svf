package gov.pnnl.svf.test;

import java.util.UUID;
import org.junit.Test;

/**
 *
 * @author D3X573
 */
public class AbstractObjectTestBaseATest extends AbstractObjectTestBase<ObjectAImpl> {

    @Override
    protected ObjectAImpl copyValueObject(ObjectAImpl object) {
        return new ObjectAImpl(object);
    }

    @Override
    protected ObjectAImpl newValueObject() {
        return new ObjectAImpl(UUID.randomUUID().toString());
    }

    @Override
    protected void setFieldsToNull(ObjectAImpl object) {
        object.setValue(null);
    }

    /**
     * Test of testBoundField method, of class AbstractObjectTestBase.
     */
    @Test
    public void testTestBoundField_3args_1() {
        final ObjectAImpl instance = newValueObject();
        testBoundField(instance, "value", (Object) "foo");
    }

    /**
     * Test of testBoundField method, of class AbstractObjectTestBase.
     */
    @Test
    public void testTestBoundField_3args_2() {
        final ObjectAImpl instance = newValueObject();
        testBoundField(instance, "value", "foo");
    }

    /**
     * Test of testBoundField method, of class AbstractObjectTestBase.
     */
    @Test
    public void testTestBoundField_4args() {
        final ObjectAImpl instance = newValueObject();
        testBoundField(instance, "value", (Object) "foo", String.class);
    }
}
