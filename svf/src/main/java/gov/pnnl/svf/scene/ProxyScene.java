package gov.pnnl.svf.scene;

import com.jogamp.opengl.GL2;

/**
 * Dummy implementation of a scene. This object is mainly used for testing.
 *
 * @author Arthur Bleeker
 */
public class ProxyScene extends AbstractScene<ProxyGLCanvas> {

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public ProxyScene(final ProxyGLCanvas component, final SceneBuilder builder) {
        super(new ProxySceneFactory(), component, builder);
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @return a new SwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static ProxyScene newInstance(final ProxyGLCanvas component, final SceneBuilder builder) {
        final ProxyScene scene = new ProxyScene(component, builder);
        scene.start();
        return scene;
    }

    @Override
    public void initialize(final GL2 gl) {
        // no operation
    }
}
