package gov.pnnl.svf.vbo;

import java.util.List;

/**
 * Interface for classes that require drawing using VBOs.
 *
 * @author Arthur Bleeker
 *
 */
public interface VboDrawable {

    /**
     * This method is called when the drawing VBOs need to be created. This may
     * be called off of the GL draw thread.
     *
     * @return the VBOs or an empty list
     */
    List<VertexBufferObject> createVbos();
}
