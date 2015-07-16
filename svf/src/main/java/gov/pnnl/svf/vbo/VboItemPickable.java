package gov.pnnl.svf.vbo;

import java.util.List;

/**
 * Interface for objects that perform a VBO picking render that maps items to an
 * object reference. Add and remove item to object references by using the
 * ItemPickingSupport.
 *
 * @author Arthur Bleeker
 */
public interface VboItemPickable {

    /**
     * This method is called when an item picking VBO needs to be created. This
     * may be called off of the GL draw thread.
     *
     * @param item The item to draw.
     *
     * @return the VBOs or an empty list
     */
    List<VertexBufferObject> createItemPickingVbos(Object item);
}
