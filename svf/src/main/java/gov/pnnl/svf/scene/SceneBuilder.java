package gov.pnnl.svf.scene;

import com.jogamp.opengl.GLCapabilities;
import gov.pnnl.svf.core.color.Color;
import java.util.Set;

/**
 * Interface for the scene builder object.
 *
 * @author Arthur Bleeker
 */
public interface SceneBuilder {

    /**
     * A copy of all of the drawable support types that get drawn before their
     * actor does.
     *
     * @return a list of all the drawable types
     */
    Class<? extends Drawable>[] copyDrawableTypes();

    /**
     * A copy of all of the hints for this scene.
     *
     * @return a copy of all the scene hints
     */
    Set<? extends Enum<?>> copyHints();

    /**
     * A copy of a filtered set of hints for this scene.
     *
     * @param type the hint type to copy
     * @param <E>  hint enumeration type
     *
     * @return a copy of the scene hints for the specified type
     */
    <E extends Enum<E>> Set<E> copyHints(Class<E> type);

    /**
     * Set a single hint of this type. All other existing hints of this type
     * will be removed.
     *
     * @param hint the scene hint to set
     * @param <E>  hint enumeration type
     *
     * @return the builder instance
     */
    <E extends Enum<E>> SceneBuilder setHint(E hint);

    /**
     * Add a single hint of this type to the existing hints.
     *
     * @param hint the scene hint to add
     * @param <E>  hint enumeration type
     *
     * @return the builder instance
     */
    <E extends Enum<E>> SceneBuilder addHint(E hint);

    /**
     * Remove this hint if it exists.
     *
     * @param hint the scene hint to remove
     * @param <E>  hint enumeration type
     *
     * @return the builder instance
     */
    <E extends Enum<E>> SceneBuilder removeHint(E hint);

    /**
     * @return a string representation of the drawable types
     */
    String getDrawableTypes();

    /**
     * @return the GL capabilities
     */
    GLCapabilities getGLCapabilities();

    /**
     * @return a string representation of the hints
     */
    String getHints();

    /**
     * @return the background
     */
    Color getBackground();

    /**
     * @return the maxInitializations
     */
    int getMaxInitializations();

    /**
     * @return the number of rendering passes in the scene, perspective,
     *         rendering phase
     */
    int getNumberOfSceneDrawingPasses();

    /**
     * @return the number of rendering passes in the user interface,
     *         orthographic, rendering phase
     */
    int getNumberOfUserInterfaceDrawingPasses();

    /**
     * @return the targetFps
     */
    int getTargetFps();

    /**
     * @return true if the scene is to be run in debug mode.
     */
    boolean isDebug();

    /**
     * @return true if the scene is logging extra information.
     */
    boolean isVerbose();

    /**
     * @return true to show and log the FPS
     */
    boolean isDisplayFps();

    /**
     * @return true to utilize auxiliary buffers if available
     */
    boolean isAuxiliaryBuffers();

    /**
     * @return true to utilize a texture color picking buffer
     */
    boolean isTextureColorPicking();

    /**
     * @return true to utilize full screen anti-aliasing if available
     */
    boolean isFullScreenAntiAliasing();

    /**
     * @return true to debug color picking if the scene supports it
     */
    boolean isDebugColorPicking();

    /**
     * @return true to enable lighting
     */
    boolean isLighting();

    /**
     * @return true to enable blending
     */
    boolean isBlending();

    /**
     * @param auxiliaryBuffers the auxiliaryBuffers to set
     *
     * @return the builder instance
     */
    SceneBuilder setAuxiliaryBuffers(boolean auxiliaryBuffers);

    /**
     * @param textureColorPicking true to utilize a texture color picking buffer
     *
     * @return the builder instance
     */
    SceneBuilder setTextureColorPicking(boolean textureColorPicking);

    /**
     * @param fullScreenAntiAliasing the fullScreenAntiAliasing to set
     *
     * @return the builder instance
     */
    SceneBuilder setFullScreenAntiAliasing(boolean fullScreenAntiAliasing);

    /**
     * @param debugColorPicking the debugColorPicking to set
     *
     * @return the builder instance
     */
    SceneBuilder setDebugColorPicking(boolean debugColorPicking);

    /**
     * @param lighting the lighting to set
     *
     * @return the builder instance
     */
    SceneBuilder setLighting(boolean lighting);

    /**
     * @param blending the blending to set
     *
     * @return the builder instance
     */
    SceneBuilder setBlending(boolean blending);

    /**
     * Update all of the drawable support types that get drawn before their
     * actor does.
     *
     * @param drawableTypes list of the drawable types
     *
     * @return the builder instance
     */
    SceneBuilder updateDrawableTypes(Class<? extends Drawable>... drawableTypes);

    /**
     * @param drawableTypes a string representation of the drawable types
     *
     * @return the builder instance
     */
    SceneBuilder setDrawableTypes(String drawableTypes);

    /**
     * @param glCapabilities the GL capabilities to set
     *
     * @return the builder instance
     */
    SceneBuilder setGLCapabilities(GLCapabilities glCapabilities);

    /**
     * @param hints a string representation of the hints
     *
     * @return the builder instance
     */
    SceneBuilder setHints(String hints);

    /**
     * @param background the background to set
     *
     * @return the builder instance
     */
    SceneBuilder setBackground(Color background);

    /**
     * @param debug set to true to run the scene in debug mode.
     *
     * @return the builder instance
     */
    SceneBuilder setDebug(boolean debug);

    /**
     * @param verbose set to true for the scene to log extra information.
     *
     * @return the builder instance
     */
    SceneBuilder setVerbose(boolean verbose);

    /**
     * @param displayFps set to true to show the FPS
     *
     * @return the builder instance
     */
    SceneBuilder setDisplayFps(boolean displayFps);

    /**
     * @param maxInitializations the maxInitializations to set
     *
     * @return the builder instance
     */
    SceneBuilder setMaxInitializations(int maxInitializations);

    /**
     * @param numberOfSceneDrawingPasses the number of rendering passes in the
     *                                   scene, perspective, rendering phase
     *
     * @return the builder instance
     */
    SceneBuilder setNumberOfSceneDrawingPasses(int numberOfSceneDrawingPasses);

    /**
     * @param numberOfUserInterfaceDrawingPasses the number of rendering passes
     *                                           in the user interface,
     *                                           orthographic, rendering phase
     *
     * @return the builder instance
     */
    SceneBuilder setNumberOfUserInterfaceDrawingPasses(int numberOfUserInterfaceDrawingPasses);

    /**
     * @param targetFps the targetFps to set
     *
     * @return the builder instance
     */
    SceneBuilder setTargetFps(int targetFps);
}
