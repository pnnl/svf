package gov.pnnl.svf.demo;

import com.jogamp.opengl.GLAutoDrawable;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;

/**
 * Interface for a demo.
 *
 * @param <W> the window type
 * @param <C> the component type
 *
 * @author Arthur Bleeker
 */
public interface Demo<W, C extends GLAutoDrawable> {

    /**
     * Demo window height.
     */
    int WINDOW_SIZE_HEIGHT = 800;
    /**
     * Demo window width.
     */
    int WINDOW_SIZE_WIDTH = 1200;

    /**
     * Starts the demo.
     *
     * @param window reference to the window
     * @param scene  reference to the scene
     *
     * @throws NullPointerException if window or scene is null
     */
    void startDemo(W window, AbstractScene<C> scene);

    /**
     * Creates a window, scene, and then starts the demo.
     *
     * @param debug true for debug mode
     */
    void runDemo(boolean debug);

    /**
     * Create the scene builder.
     *
     * @param debug true for debug mode
     *
     * @return a new builder
     */
    SceneBuilder createBuilder(boolean debug);

    /**
     * Create the scene.
     *
     * @param window  reference to the window or null
     * @param builder reference to a scene builder
     *
     * @return a new demo scene
     */
    AbstractScene<C> createScene(W window, SceneBuilder builder);

    /**
     * Create the window that will display the scene.
     *
     * @param builder reference to a scene builder
     *
     * @return a new window
     */
    W createWindow(SceneBuilder builder);
}
