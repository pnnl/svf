package gov.pnnl.svf.test;

/**
 * Interface that creates objects used for testing.
 *
 * @author Arthur Bleeker
 */
public interface ObjectFactory<T> {

    /**
     * This method should return a copy of the value object.
     *
     * @param object that should be copied
     *
     * @return a new copy of the value object
     */
    T copyValueObject(final T object);

    /**
     * This factory method should return a new unique value object.
     *
     * @return a new unique value object
     */
    T newValueObject();

    /**
     * This method should set all of the fields to null using the supplied
     * setters.
     *
     * @param object to set the fields to null
     */
    void setFieldsToNull(T object);

}
