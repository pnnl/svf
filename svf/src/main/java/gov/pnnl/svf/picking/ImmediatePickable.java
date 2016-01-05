package gov.pnnl.svf.picking;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;

/**
 * Interface for objects that perform a picking render for an actor using
 * immediate mode.
 *
 * @author Arthur Bleeker
 */
public interface ImmediatePickable {

    /**
     * Called during the draw cycle.
     *
     * @param gl     Reference to GL
     * @param glu    Reference to GLU
     * @param camera Reference to camera used during this draw call. May be null
     *               if there is no active camera.
     */
    void pickingDrawImmediate(GL2 gl, GLUgl2 glu, Camera camera);
}
