package gov.pnnl.svf.vbo;

import gov.pnnl.svf.geometry.Shape;

/**
 * Service for working with VBO shape factories.
 *
 * @author Arthur Bleeker
 */
public interface VboShapeService {

    /**
     * Get a cached or new VBO shape factory based on the supplied shape type.
     *
     * @param type the type of shape for the factory
     * @param <T>  the type of shape for the factory
     *
     * @return a cached or new VBO shape factory
     */
    <T extends Shape> VboShapeFactory getVboShapeFactory(Class<T> type);

    /**
     * Set a VBO shape factory. The same factory can be utilized for multiple
     * types assuming that the base shape factory type is compatible.
     *
     * @param type            the type of shape for the factory
     * @param vboShapeFactory the new shape factory or null to clear a shape
     *                        factory
     * @param <T>             the type of shape for the factory
     */
    <T extends Shape> void setVboShapeFactory(Class<T> type, VboShapeFactory vboShapeFactory);
}
