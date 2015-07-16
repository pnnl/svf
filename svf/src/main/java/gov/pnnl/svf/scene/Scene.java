package gov.pnnl.svf.scene;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.update.TaskManager;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Interface for a OpenGL scene.
 *
 * @author Arthur Bleeker
 *
 */
public interface Scene extends SceneLookup, Disposable, Observable {

    /**
     * String representation of a field in this object.
     */
    String BOUNDARY = "boundary";
    /**
     * String representation of a field in this object.
     */
    String CENTER = "center";
    /**
     * String representation of a field in this object.
     */
    String SCENE_BACKGROUND = "sceneBackground";
    /**
     * String representation of a field in this object.
     */
    String VIEWPORT = "viewport";
    /**
     * String representation of a field in this object.
     */
    String LOADED = "loaded";

    /**
     * @return the id
     */
    String getId();

    /**
     * @return the type
     */
    String getType();

    /**
     * @return the component that's used to render this scene
     */
    GLAutoDrawable getComponent();

    /**
     * @return the scene factory
     */
    SceneFactory getFactory();

    /**
     * Abstract class will override to add listeners to the base class that get
     * added to the lookup.
     *
     * @param object reference to the object getting added to the lookup
     */
    void addListeners(Object object);

    /**
     * Mark the scene for drawing during the next draw cycle.
     */
    void draw();

    /**
     * Mark this pass in the scene for drawing during the next draw cycle.
     *
     * @param pass the pass to mark for drawing
     */
    void draw(DrawingPass pass);

    /**
     * Get the drawing passes which are currently dirty.
     *
     * @return the currently dirty drawing passes
     */
    DrawingPass getDirtied();

    /**
     * The boundary of the scene for keeping objects with CollisionSupport
     * inside of the scene.
     *
     * @return the boundary of this scene
     */
    Vector3D getBoundary();

    /**
     * The center of the scene boundaries for keeping objects with
     * CollisionSupport inside of the scene.
     *
     * @return the center
     */
    Vector3D getCenter();

    /**
     * @return the maxTextureLoadsPerFrame
     */
    int getMaxInitializations();

    /**
     * The total scene viewport area.
     *
     * @return the viewport
     */
    Rectangle getViewport();

    /**
     * @return the background color of the scene
     */
    Color getSceneBackground();

    /**
     * @return a reference to the default task manager
     */
    TaskManager getDefaultTaskManager();

    /**
     * Perform all of the scene initialization logic here. This will run after
     * the initial initialization as well as all GL resets. Use this method to
     * override default OpenGL scene parameters.
     *
     * @param gl Reference to the active GL
     */
    void initialize(GL2 gl);

    /**
     * Flag indicating that the scene is loaded.
     *
     * @return true if the scene has been loaded.
     */
    boolean isLoaded();

    /**
     * Abstract class will override to remove listeners from the base class that
     * get added to the lookup.
     *
     * @param object reference to the object getting added to the lookup
     */
    void removeListeners(Object object);

    /**
     * The boundary of the scene for keeping objects with CollisionSupport
     * inside of the scene.
     *
     * @param boundary the boundary of the scene to set
     */
    void setBoundary(Vector3D boundary);

    /**
     * The center of the scene boundaries for keeping objects with
     * CollisionSupport inside of the scene.
     *
     * @param center the center to set
     */
    void setCenter(Vector3D center);

    /**
     * @param maxInitializations the maxInitializations to set
     */
    void setMaxInitializations(final int maxInitializations);

    /**
     * @param background the background color of the scene to set
     */
    void setSceneBackground(Color background);

    /**
     * Starts animation of the scene. Initialize needs to be called prior to
     * calling this method.
     */
    void start();

    /**
     * Stops animation of the scene. All animation and drawing will cease when
     * this method is called. It's recommended to set the scene visibility to
     * false when draw is no longer needed.
     */
    void stop();

    /**
     * Get a reference to the tooltip interface.
     *
     * @return the tooltip interface
     */
    Tooltip getTooltip();

    /**
     * Get a reference to the screenshot interface.
     *
     * @return the screenshot interface
     */
    Screenshot getScreenshot();

    /**
     * Get a reference to the extended API for the scene.
     *
     * @return a reference to the extended interface
     */
    SceneExt getExtended();

    /**
     * This method will not return until the scene is loaded.
     *
     * @param timeout the number of milliseconds to wait
     *
     * @return true if the scene is loaded when this method returns
     */
    boolean waitForLoad(long timeout);
}
