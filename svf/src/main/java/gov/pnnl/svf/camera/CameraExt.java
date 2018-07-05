package gov.pnnl.svf.camera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;

/**
 * Extended interface for a camera.
 *
 * @author Amelia Bleeker
 *
 * @param <T> Listener type for this camera
 */
public interface CameraExt<T> extends Camera {

    /**
     * Makes this the active orthographic 2d camera for the scene.
     *
     * @param gl  Reference to GL
     * @param glu Reference to GLU
     */
    void makeOrtho2D(GL2 gl, GLUgl2 glu);

    /**
     * Makes this the active perspective camera for the scene.
     *
     * @param gl  Reference to GL
     * @param glu Reference to GLU
     */
    void makePerspective(GL2 gl, GLUgl2 glu);

    /**
     * Called at the end of the active orthographic 2d pass for this camera in
     * the scene. The base implementation does nothing.
     *
     * @param gl  Reference to GL
     * @param glu Reference to GLU
     */
    void endOrtho2D(GL2 gl, GLUgl2 glu);

    /**
     * Called at the end of the active perspective pass for this camera in the
     * scene. The base implementation does nothing.
     *
     * @param gl  Reference to GL
     * @param glu Reference to GLU
     */
    void endPerspective(GL2 gl, GLUgl2 glu);

    /**
     * Returns the last number of vertices recorded. This method is not thread
     * safe and should only be called on the OpenGL active context thread.
     *
     * @return the number of vertices recorded
     */
    long getVerticesCounter();

    /**
     * Add vertices to the total count of rendered vertices this draw cycle.
     * This method is not thread safe and should only be called on the OpenGL
     * active context thread.
     *
     * @param count called by objects being rendered in order to track
     *              application performance
     */
    void incrementVerticesCounter(long count);

    /**
     * Resets the vertices counter. This method should be called at the end of a
     * complete rendering cycle. This method is not thread safe and should only
     * be called on the OpenGL active context thread.
     */
    void resetVerticesCounter();

    /**
     * Add a listener.
     *
     * @param listener
     */
    void addListener(T listener);

    /**
     * Remove a listener.
     *
     * @param listener
     */
    void removeListener(T listener);

    /**
     * Clears all of the listeners.
     */
    void clearListeners();
}
