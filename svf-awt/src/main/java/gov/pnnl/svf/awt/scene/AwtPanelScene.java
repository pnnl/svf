package gov.pnnl.svf.awt.scene;

import gov.pnnl.svf.scene.SceneBuilder;
import com.jogamp.opengl.awt.GLJPanel;

/**
 * <p>
 * This default implementation provides a dragging and color picking camera.</p>
 * <p>
 * Lists of actors and references to cameras should not be kept in subclasses.
 * These objects should be located using the lookup (inversion of control). This
 * allows flexibility in that other classes can add and remove actors and
 * cameras during runtime. It also allows for a single place of reference for
 * access to these objects.</P>
 *
 * @author Arthur Bleeker
 */
public class AwtPanelScene extends AbstractAwtScene<GLJPanel> {

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public AwtPanelScene(final GLJPanel component, final SceneBuilder builder) {
        super(component, AwtPanelScene.configure(builder));
    }

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param id        the unique id for the scene
     *
     * @throws IllegalArgumentException if component is not a supported type
     * @throws IllegalArgumentException if id is empty
     */
    public AwtPanelScene(final GLJPanel component, final SceneBuilder builder, final String id) {
        super(component, builder, DEFAULT_TYPE, id);
    }

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param type      the type identifier for scene
     * @param id        the unique id for the scene
     *
     * @throws IllegalArgumentException if component is not a supported type
     * @throws IllegalArgumentException if type or id are empty
     */
    public AwtPanelScene(final GLJPanel component, final SceneBuilder builder, final String type, final String id) {
        super(component, builder, type, id);
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @return a new AwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static AwtPanelScene newInstance(final GLJPanel component, final SceneBuilder builder) {
        final AwtPanelScene scene = new AwtPanelScene(component, builder);
        scene.start();
        return scene;
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param id        the unique id for the scene
     *
     * @return a new AwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static AwtPanelScene newInstance(final GLJPanel component, final SceneBuilder builder, final String id) {
        final AwtPanelScene scene = new AwtPanelScene(component, builder, id);
        scene.start();
        return scene;
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param type      the type identifier for scene
     * @param id        the unique id for the scene
     *
     * @return a new AwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static AwtPanelScene newInstance(final GLJPanel component, final SceneBuilder builder, final String type, final String id) {
        final AwtPanelScene scene = new AwtPanelScene(component, builder, type, id);
        scene.start();
        return scene;
    }

    private static SceneBuilder configure(final SceneBuilder builder) {
        // GLJPanel is single buffered and cannot use the front buffer for color picking
        if (!builder.isAuxiliaryBuffers()) {
            builder.setTextureColorPicking(true);
        }
        return builder;
    }
}
