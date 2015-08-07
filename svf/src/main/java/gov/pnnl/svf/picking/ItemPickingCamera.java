package gov.pnnl.svf.picking;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;

/**
 * Interface for a camera that is used for item picking. Item picking only works
 * for scene objects.
 *
 * @author Arthur Bleeker
 */
public interface ItemPickingCamera extends PickingCamera {

    /**
     * Check if the actor has been picked. This method will notify individual
     * actor's if they have been picked.
     *
     * @param gl      Reference to GL.
     * @param glu     Reference to GLU.
     * @param support The actor support to check for a pick.
     */
    void checkPickingHit(GL2 gl, GLUgl2 glu, ItemPickingSupport support);
}
