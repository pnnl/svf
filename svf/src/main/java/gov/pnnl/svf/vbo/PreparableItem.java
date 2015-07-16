package gov.pnnl.svf.vbo;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * Interface used to mark an item as preparable.
 *
 * @author Arthur Bleeker
 */
public interface PreparableItem {

    /**
     * Called once while the context is current. OpenGL initialization logic
     * that needs to be performed once can be placed here.
     *
     * @param gl  reference to the current GL
     * @param glu Reference to GLU
     */
    void prepare(GL2 gl, GLUgl2 glu);

}
