package gov.pnnl.svf.newt.scene;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;

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
public class NewtScene extends AbstractScene<GLWindow> {

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public NewtScene(final GLWindow component, final SceneBuilder builder) {
        super(new NewtSceneFactory(), component, builder);
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
    public NewtScene(final GLWindow component, final SceneBuilder builder, final String id) {
        super(new NewtSceneFactory(), component, builder, DEFAULT_TYPE, id);
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
    public NewtScene(final GLWindow component, final SceneBuilder builder, final String type, final String id) {
        super(new NewtSceneFactory(), component, builder, type, id);
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @return a new NewtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static NewtScene newInstance(final GLWindow component, final SceneBuilder builder) {
        final NewtScene scene = new NewtScene(component, builder);
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
     * @return a new NewtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static NewtScene newInstance(final GLWindow component, final SceneBuilder builder, final String id) {
        final NewtScene scene = new NewtScene(component, builder, id);
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
     * @return a new NewtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static NewtScene newInstance(final GLWindow component, final SceneBuilder builder, final String type, final String id) {
        final NewtScene scene = new NewtScene(component, builder, type, id);
        scene.start();
        return scene;
    }

    @Override
    public void initialize(final GL2 gl) {
        // no operation
    }
}
