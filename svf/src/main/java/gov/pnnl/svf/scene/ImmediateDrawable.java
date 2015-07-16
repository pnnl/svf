package gov.pnnl.svf.scene;

import gov.pnnl.svf.camera.Camera;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * Interface for classes that require drawing using immediate mode.
 *
 * @author Arthur Bleeker
 *
 */
public interface ImmediateDrawable {

    /**
     * Called during the draw cycle.
     *
     * @param gl     Reference to GL
     * @param glu    Reference to GLU
     * @param camera Reference to camera used during this draw call. May be null
     *               if there is no active camera.
     */
    void drawImmediate(GL2 gl, GLUgl2 glu, Camera camera);
}
