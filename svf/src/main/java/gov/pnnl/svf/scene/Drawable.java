package gov.pnnl.svf.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;

/**
 * Interface for classes that require drawing. This interface should not be used
 * to mark an item for drawing in the scene lookup. Use the DrawableItem
 * interface instead.
 *
 * @author Arthur Bleeker
 *
 */
public interface Drawable extends SceneItem {

    /**
     * Called during the draw cycle.
     *
     * @param gl     Reference to GL
     * @param glu    Reference to GLU
     * @param camera Reference to camera used during this draw call. May be null
     *               if there is no active camera.
     */
    void draw(GL2 gl, GLUgl2 glu, Camera camera);

    /**
     * Called after the actor has been drawn. Use this method to clean up or
     * reset the GL state machine.
     *
     * @param gl     Reference to GL
     * @param glu    Reference to GLU
     * @param camera Reference to camera used during this draw call. May be null
     *               if there is no active camera.
     */
    void endDraw(GL2 gl, GLUgl2 glu, Camera camera);
}
