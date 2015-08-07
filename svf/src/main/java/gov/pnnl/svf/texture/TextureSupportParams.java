package gov.pnnl.svf.texture;

import com.jogamp.opengl.GL2;

/**
 * Interface used for overriding the texture parameters during texture
 * initialization.
 *
 * @author Arthur Bleeker
 */
public interface TextureSupportParams {

    /**
     * Override this function to set your own texture parameters when the
     * texture gets initialized.
     *
     * @param gl Reference to the current gl context
     */
    void overrideParams(GL2 gl);
}
