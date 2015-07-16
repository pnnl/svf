package gov.pnnl.svf.core.util;

/**
 * Interfaced used to transform a property.
 *
 * @param <T> the type of value that is being transformed
 *
 * @author Arthur Bleeker
 */
public interface Transformer<T> {

    /**
     * Transforms the value.
     *
     * @param value the value to transform
     *
     * @return the transformed value
     */
    T transform(T value);
}
