package gov.pnnl.svf.hint;

/**
 * Hints related to OpenGL.
 *
 * @author Arthur Bleeker
 */
public enum OpenGLHint {

    /**
     * Vertex Buffer Object (VBO) rendering utilizes objects to store complete
     * vertex data (location, normal, and color) so that it can be stored on the
     * graphics processing unit (GPU). This allows the GPU to render outside of
     * the calling thread.
     */
    VBO_DRAW,
    /**
     * Display lists are a group of OpenGL commands that are compiled and cached
     * for later use. They may speed up the rendering performance. Commands that
     * return a value will be executed immediately and cannot be used in a call
     * list.
     */
    DISPLAY_LISTS,
    /**
     * Array rendering utilizes code placed between <code>glBegin()</code> and
     * <code>glEnd()</code> to draw using arrays for data. The benefit of arrays
     * is that they can be stored in CPU memory. Otherwise it'll perform
     * similarly to immediate draw.
     */
    ARRAY_DRAW,
    /**
     * Immediate mode rendering utilizes code placed between
     * <code>glBegin()</code> and <code>glEnd()</code>. This is the simplest way
     * to draw in OpenGL. However, it is also the slowest.
     */
    IMMEDIATE_DRAW;
}
