package gov.pnnl.svf.scene;

/**
 * Performance and debug related metrics for a scene.
 *
 * @author Arthur Bleeker
 */
public interface SceneMetrics {

    /**
     * Increment the current vbo buffer count
     */
    void incrementVboBufferCount();

    /**
     * Increment the current display list count
     */
    void incrementDisplayListCount();

    /**
     * Decrement the current vbo buffer count
     */
    void decrementVboBufferCount();

    /**
     * Decrement the current display list count
     */
    void decrementDisplayListCount();

    /**
     * @return the current vbo buffer count
     */
    int getVboBufferCount();

    /**
     * @return the current display list count
     */
    int getDisplayListCount();

    /**
     * @return the total number of actors culled last draw cycle
     */
    long getLastCulledActors();

    /**
     * @return the total number of milliseconds it took to render last draw
     *         cycle
     */
    long getLastDrawLength();

    /**
     * @return the total number of milliseconds it took for the last update
     */
    long getLastUpdateLength();

    /**
     * @return the total number of vertices rendered last draw cycle
     */
    long getLastVerticesRendered();

    /**
     * @return the size of the attrib stack depth at the end of the last draw
     *         cycle
     */
    int getLastAttribStackDepth();

    /**
     * @return the size of the modelview stack depth at the end of the last draw
     *         cycle
     */
    int getLastModelviewStackDepth();

    /**
     * @return the size of the projection stack depth at the end of the last
     *         draw cycle
     */
    int getLastProjectionStackDepth();

    /**
     * @return the size of the texture stack depth at the end of the last draw
     *         cycle
     */
    int getLastTextureStackDepth();

    /**
     * @return the total number of errors at the end of the last draw cycle
     */
    int getLastErrorsReported();

    /**
     * @return a string representation of the scene collections during the last
     *         draw cycle
     */
    String getCollectionsInfo();
}
