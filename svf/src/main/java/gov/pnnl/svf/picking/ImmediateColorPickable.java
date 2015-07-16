package gov.pnnl.svf.picking;

import gov.pnnl.svf.camera.Camera;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * Interface for objects that perform a picking render that maps colors to an
 * object reference. Add and remove color to object references by using the
 * ColorPickingCamera.
 *
 * @author Arthur Bleeker
 */
public interface ImmediateColorPickable {

    /**
     * Called during the color pickable draw cycle. There is no end draw color
     * method so unlike a normal draw call the state must be reset before this
     * method call ends. GL state should not be modified except for changes to
     * color for drawing individual items.
     *
     * @param gl      Reference to GL
     * @param glu     Reference to GLU
     * @param camera  Reference to camera used during this draw call. May be
     *                null if there is no active camera.
     * @param support the color item picking support object
     */
    void colorPickingDrawImmediate(GL2 gl, GLUgl2 glu, Camera camera, ColorPickingSupport support);
}
