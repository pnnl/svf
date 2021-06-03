package gov.pnnl.svf.test;

import java.util.UUID;
import org.junit.Test;

/**
 *
 * @author D3X573
 */
public class AbstractObjectTestBaseBTest extends AbstractObjectTestBase<ObjectBImpl> {

    @Override
    protected ObjectBImpl copyValueObject(ObjectBImpl object) {
        return new ObjectBImpl(object);
    }

    @Override
    protected ObjectBImpl newValueObject() {
        return new ObjectBImpl(UUID.randomUUID().toString());
    }

    @Override
    protected void setFieldsToNull(ObjectBImpl object) {
        object.setValue(null);
    }

    /**
     * Test of testBoundField method, of class AbstractObjectTestBase.
     */
    @Test
    public void testTestBoundField_3args_1() {
        final ObjectBImpl instance = newValueObject();
        testBoundField(instance, "value", (Object) "foo");
    }

    /**
     * Test of testBoundField method, of class AbstractObjectTestBase.
     */
    @Test
    public void testTestBoundField_3args_2() {
        final ObjectBImpl instance = newValueObject();
        testBoundField(instance, "value", "foo");
    }

    /**
     * Test of testBoundField method, of class AbstractObjectTestBase.
     */
    @Test
    public void testTestBoundField_4args() {
        final ObjectBImpl instance = newValueObject();
        testBoundField(instance, "value", (Object) "foo", String.class);
    }
}
