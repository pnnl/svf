package gov.pnnl.svf.vbo;

import gov.pnnl.svf.picking.ColorPickingSupport;
import java.util.List;

/**
 * Interface for objects that perform a VBO picking render that maps colors to
 * an object reference. Add and remove color to object references by using the
 * ColorPickingCamera.
 *
 * @author Arthur Bleeker
 */
public interface VboColorPickable {

    /**
     * This method is called when the color picking VBOs need to be created.
     * This may be called off of the GL draw thread.
     *
     * @param support the color item picking support object
     *
     * @return the VBOs or an empty list
     */
    List<VertexBufferObject> createColorPickingVbos(ColorPickingSupport support);
}
