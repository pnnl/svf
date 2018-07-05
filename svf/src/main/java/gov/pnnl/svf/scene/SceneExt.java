package gov.pnnl.svf.scene;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.picking.ColorPickingUtils;
import java.util.Collection;
import java.util.Set;

/**
 * Extended interface for the scene that exposes less used properties.
 *
 * @author Amelia Bleeker
 */
public interface SceneExt extends Scene {

    /**
     *
     * @return a reference to the current GL instance
     */
    GL getGL();

    /**
     *
     * @return a reference to the current GLU instance
     */
    GLU getGLU();

    /**
     *
     * @return a reference to the current GLUT instance
     */
    GLUT getGLUT();

    /**
     *
     * @return a reference to the current GL Profile
     */
    GLProfile getGLProfile();

    /**
     * @return the current drawing pass or null if the scene is not currently
     *         drawing
     */
    DrawingPass getCurrentDrawingPass();

    /**
     * Collection of all the root actors in the scene that are currently
     * visible.
     *
     * @return a reference to the visible root actors
     */
    Collection<Actor> getVisibleRootActors();

    /**
     * Collection of all the root actors in the scene that are currently
     * visible.
     *
     * @param out a reference to a collection to populate with the visible root
     *            actors
     */
    void getVisibleRootActors(Collection<Actor> out);

    /**
     * Utility class used for color picking.
     *
     * @return a reference to the color picking utils for this scene
     */
    ColorPickingUtils getColorPickingUtils();

    /**
     * Get a reference to an immutable scene builder used to construct this
     * scene.
     *
     * @return an immutable scene builder
     */
    SceneBuilder getSceneBuilder();

    /**
     * This set represents all of the drawable types that can be present in an
     * actors lookup.
     *
     * @return an immutable set of the drawable types
     */
    Set<Class<? extends Drawable>> getDrawableTypes();

    /**
     * The scene metrics contain performance and debug related information.
     *
     * @return a reference to the scene metrics
     */
    SceneMetrics getSceneMetrics();
}
