package gov.pnnl.svf.awt.scene;

import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

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
 * @param <C> the component type that's used to render this scene
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractAwtScene<C extends GLAutoDrawable> extends AbstractScene<C> {

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    protected AbstractAwtScene(final C component, final SceneBuilder builder) {
        super(new AwtSceneFactory(), component, builder);
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
    protected AbstractAwtScene(final C component, final SceneBuilder builder, final String id) {
        super(new AwtSceneFactory(), component, builder, DEFAULT_TYPE, id);
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
    protected AbstractAwtScene(final C component, final SceneBuilder builder, final String type, final String id) {
        super(new AwtSceneFactory(), component, builder, type, id);
    }

    @Override
    public void initialize(final GL2 gl) {
        // no operation
    }
}
