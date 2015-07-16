package gov.pnnl.svf.scene;

import javax.media.opengl.GL2;
import javax.media.opengl.GLOffscreenAutoDrawable;

/**
 * Offscreen implementation of a scene. Use the following to get an offscreen
 * auto drawable component for the scene. This scene type utilizes a proxy scene
 * factory but can accommodate any drawing or picking camera types.
 * <p/>
 * <code>GLDrawableFactory.getDesktopFactory().createOffscreenAutoDrawable(null, builder.getGLCapabilities(), null, width, height);</code>
 *
 * @author Arthur Bleeker
 */
public class OffscreenScene extends AbstractScene<GLOffscreenAutoDrawable> {

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public OffscreenScene(final GLOffscreenAutoDrawable component, final SceneBuilder builder) {
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
    public static OffscreenScene newInstance(final GLOffscreenAutoDrawable component, final SceneBuilder builder) {
        final OffscreenScene scene = new OffscreenScene(component, builder);
        scene.start();
        return scene;
    }

    @Override
    public void initialize(final GL2 gl) {
        // no operation
    }
}
