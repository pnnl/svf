package gov.pnnl.svf.vbo;

import java.util.List;

/**
 * Interface for objects that perform a picking render for an actor using VBOs.
 * This interface can be implemented for actors that wish to pick using
 * different geometry than they use to render.
 *
 * @author Arthur Bleeker
 */
public interface VboPickable {

    /**
     * This method is called when a picking draw VBO needs to be created. This
     * may be called off of the GL draw thread.
     *
     * @return the VBOs or an empty list
     */
    List<VertexBufferObject> createPickingVbos();
}
