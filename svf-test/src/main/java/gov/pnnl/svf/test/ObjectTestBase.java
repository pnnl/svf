package gov.pnnl.svf.test;

/**
 * Class that can be used as a helper for testing objects.
 *
 * @author Arthur Bleeker
 * @param <T>
 */
public class ObjectTestBase<T extends Object> extends AbstractObjectTestBase<T> {

    private final ObjectFactory<T> objectFactory;

    /**
     * Constructor. The object factory is only required for parameterless test
     * methods.
     *
     * @param objectFactory an optional object factory
     */
    public ObjectTestBase(final ObjectFactory<T> objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public void testBoundField(final T object, final String field, final Object value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final int value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final long value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final short value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final byte value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final double value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final float value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final boolean value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final char value) {
        super.testBoundField(object, field, value);
    }

    @Override
    public void testBoundField(final T object, final String field, final Object value, final Class<?> clazz) {
        super.testBoundField(object, field, value, clazz);
    }

    @Override
    protected T copyValueObject(final T object) {
        return objectFactory.copyValueObject(object);
    }

    @Override
    protected T newValueObject() {
        return objectFactory.newValueObject();
    }

    @Override
    protected void setFieldsToNull(final T object) {
        objectFactory.setFieldsToNull(object);
    }
}
