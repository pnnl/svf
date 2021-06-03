package gov.pnnl.svf.test;

import java.util.Random;
import java.util.UUID;
import org.junit.Test;

/**
 *
 * @author D3X573
 */
public class ObjectTestBaseTest {

    /**
     * Test of testBoundField method, of class ObjectTestBase.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testTestBoundField() throws Exception {
        final ObjectFactory<ObjectCImpl> factory = new ObjectFactory<ObjectCImpl>() {
            private final Random random = new Random(0L);

            @Override
            public ObjectCImpl copyValueObject(ObjectCImpl object) {
                return new ObjectCImpl(object);
            }

            @Override
            public ObjectCImpl newValueObject() {
                final ObjectCImpl instance = new ObjectCImpl(UUID.randomUUID().toString());
                instance.setValueBoolean(random.nextBoolean());
                instance.setValueByte((byte) random.nextInt());
                instance.setValueDouble((double) random.nextDouble());
                instance.setValueFloat((float) random.nextDouble());
                instance.setValueInt(random.nextInt());
                instance.setValueLong(random.nextLong());
                instance.setValueShort((short) random.nextInt());
                return instance;
            }

            @Override
            public void setFieldsToNull(ObjectCImpl object) {
                object.setValue(null);
            }
        };
        final ObjectTestBase<ObjectCImpl> instance = new ObjectTestBase<>(factory);
        instance.testEqualsObject();
        instance.testEqualsObjectDifferent();
        instance.testEqualsObjectNullFields();
        instance.testHashCode();
        instance.testHashCodeCollisions();
        instance.testBoundField(new ObjectCImpl(""), "value", (Object) "foo");
        instance.testBoundField(new ObjectCImpl(""), "value", "foo");
        instance.testBoundField(new ObjectCImpl(""), "value", (Object) "foo", String.class);
        instance.testBoundField(new ObjectCImpl(""), "valueBoolean", true);
        instance.testBoundField(new ObjectCImpl(""), "valueByte", (byte) 13);
        instance.testBoundField(new ObjectCImpl(""), "valueDouble", 0.13);
        instance.testBoundField(new ObjectCImpl(""), "valueFloat", 0.13F);
        instance.testBoundField(new ObjectCImpl(""), "valueInt", 13);
        instance.testBoundField(new ObjectCImpl(""), "valueLong", 13L);
        instance.testBoundField(new ObjectCImpl(""), "valueShort", (short) 13);
        instance.testSerializable();
    }

}
