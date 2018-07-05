package gov.pnnl.svf.scene;

/**
 * Scene timers that retain references to times and stages of the scene
 * renderer.
 *
 * @author Amelia Bleeker
 */
public interface SceneTimers extends SceneMetrics {

    /**
     *
     * @param drawing set the scene as drawing
     *
     * @return true if the scene is drawing
     */
    boolean getAndSetDrawing(boolean drawing);

    /**
     *
     * @param repaint set the scene as requiring a repaint
     *
     * @return true if the scene requires a repaint
     */
    DrawingPass getAndSetRepaint(DrawingPass repaint);

    /**
     *
     * @param updating set the scene as updating
     *
     * @return true if the scene is updating
     */
    boolean getAndSetUpdating(boolean updating);

    /**
     *
     * @return the current drawing pass
     */
    DrawingPass getCurrentDrawingPass();

    /**
     *
     * @return true if the scene is drawing
     */
    boolean getDrawing();

    /**
     *
     * @return true if the scene requires a repaint
     */
    DrawingPass getRepaint();

    /**
     *
     * @return true if the scene is updating
     */
    boolean getUpdating();

    /**
     *
     * @param currentDrawingPass set the current drawing pass
     */
    void setCurrentDrawingPass(DrawingPass currentDrawingPass);

    /**
     *
     * @param drawing set the scene as drawing
     */
    void setDrawing(boolean drawing);

    /**
     *
     * @param lastCulledActors set the last number of culled actors
     */
    void setLastCulledActors(long lastCulledActors);

    /**
     *
     * @param lastDrawLength set the last draw length in milliseconds
     */
    void setLastDrawLength(long lastDrawLength);

    /**
     *
     * @param lastUpdateLength set the last update length in milliseconds
     */
    void setLastUpdateLength(long lastUpdateLength);

    /**
     *
     * @param lastVerticesRendered set the last number of vertices rendered
     */
    void setLastVerticesRendered(long lastVerticesRendered);

    /**
     * @param lastAttribStackDepth the size of the attrib stack depth at the end
     *                             of the last draw cycle
     */
    void setLastAttribStackDepth(int lastAttribStackDepth);

    /**
     * @param lastModelviewStackDepth the size of the modelview stack depth at
     *                                the end of the last draw cycle
     */
    void setLastModelviewStackDepth(int lastModelviewStackDepth);

    /**
     * @param lastProjectionStackDepth the size of the projection stack depth at
     *                                 the end of the last draw cycle
     */
    void setLastProjectionStackDepth(int lastProjectionStackDepth);

    /**
     * @param lastTextureStackDepth the size of the texture stack depth at the
     *                              end of the last draw cycle
     */
    void setLastTextureStackDepth(int lastTextureStackDepth);

    /**
     * @param lastErrorsReported the total number of errors at the end of the
     *                           last draw cycle
     */
    void setLastErrorsReported(int lastErrorsReported);

    /**
     *
     * @param repaint set to true to require a repaint
     */
    void setRepaint(DrawingPass repaint);

    /**
     *
     * @param updating set to true if updating
     */
    void setUpdating(boolean updating);

    /**
     * @param info a string representation of the scene collections during the
     *             last draw cycle
     */
    void setCollectionsInfo(String info);
}
