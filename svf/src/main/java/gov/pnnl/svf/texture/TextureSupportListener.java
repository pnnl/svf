package gov.pnnl.svf.texture;

/**
 * @author Amelia Bleeker
 */
public interface TextureSupportListener {

    /**
     * An error occurred while loading the texture into the scene.
     *
     * @param support The TextureSupport
     */
    void textureError(TextureSupport support);

    /**
     * Called when a texture is loaded into the scene.
     *
     * @param support The TextureSupport.
     */
    void textureLoaded(TextureSupport support);

    /**
     * Called when a texture is unloaded from the scene.
     *
     * @param support The TextureSupport.
     */
    void textureUnloaded(TextureSupport support);
}
