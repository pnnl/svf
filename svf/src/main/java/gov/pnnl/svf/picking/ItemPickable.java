package gov.pnnl.svf.picking;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.PickingCameraEvent;

/**
 * Interface for objects that perform a picking render that maps items to an
 * object reference. Add and remove item to object references by using the
 * ItemPickingSupport.
 *
 * @author Arthur Bleeker
 */
public interface ItemPickable {

    /**
     * Called during the draw cycle.
     *
     * @param gl     Reference to GL
     * @param glu    Reference to GLU
     * @param camera Reference to camera used during this draw call. May be null
     *               if there is no active camera.
     * @param event  Reference to the event that triggered this item pick.
     * @param item   The item to draw.
     */
    void itemPickingDraw(GL2 gl, GLUgl2 glu, Camera camera, PickingCameraEvent event, Object item);
}
