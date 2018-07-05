package gov.pnnl.svf.geometry;

/**
 * Service for working with shape renderers.
 *
 * @author Amelia Bleeker
 */
public interface ShapeService {

    /**
     * Get a cached or new shape renderer based on the supplied shape type.
     *
     * @param type the type of shape for the renderer
     * @param <T>  the type of shape for the renderer
     *
     * @return a cached or new shape renderer
     */
    <T extends Shape> ShapeRenderer getShapeRenderer(Class<T> type);

    /**
     * Set a shape renderer. The same renderer can be utilized for multiple
     * types assuming that the base shape renderer type is compatible.
     *
     * @param type          the type of shape for the renderer
     * @param shapeRenderer the new shape renderer or null to clear a shape
     *                      renderer
     * @param <T>           the type of shape for the renderer
     */
    <T extends Shape> void setShapeRenderer(Class<T> type, ShapeRenderer shapeRenderer);
}
