package gov.pnnl.svf.scene;

import gov.pnnl.svf.core.color.Color;
import java.util.Set;
import javax.media.opengl.GLCapabilities;

/**
 * Immutable scene builder.
 *
 * @author Arthur Bleeker
 */
public class ImmutableSceneBuilder implements SceneBuilder {

    private final SceneBuilder sceneBuilder;

    /**
     * Constructor
     *
     * @param sceneBuilder the scene builder to create an immutable wrapper for
     *
     * @throws NullPointerException if scene builder is null
     */
    public ImmutableSceneBuilder(final SceneBuilder sceneBuilder) {
        if (sceneBuilder == null) {
            throw new NullPointerException("sceneBuilder");
        }
        this.sceneBuilder = sceneBuilder;
    }

    @Override
    public Class<? extends Drawable>[] copyDrawableTypes() {
        return sceneBuilder.copyDrawableTypes();
    }

    @Override
    public Set<? extends Enum<?>> copyHints() {
        return sceneBuilder.copyHints();
    }

    @Override
    public <E extends Enum<E>> Set<E> copyHints(final Class<E> type) {
        return sceneBuilder.copyHints(type);
    }

    @Override
    public String getDrawableTypes() {
        return sceneBuilder.getDrawableTypes();
    }

    @Override
    public GLCapabilities getGLCapabilities() {
        return sceneBuilder.getGLCapabilities();
    }

    @Override
    public String getHints() {
        return sceneBuilder.getHints();
    }

    @Override
    public Color getBackground() {
        return sceneBuilder.getBackground();
    }

    @Override
    public int getMaxInitializations() {
        return sceneBuilder.getMaxInitializations();
    }

    @Override
    public int getNumberOfSceneDrawingPasses() {
        return sceneBuilder.getNumberOfSceneDrawingPasses();
    }

    @Override
    public int getNumberOfUserInterfaceDrawingPasses() {
        return sceneBuilder.getNumberOfUserInterfaceDrawingPasses();
    }

    @Override
    public int getTargetFps() {
        return sceneBuilder.getTargetFps();
    }

    @Override
    public boolean isDebug() {
        return sceneBuilder.isDebug();
    }

    @Override
    public boolean isVerbose() {
        return sceneBuilder.isVerbose();
    }

    @Override
    public boolean isDisplayFps() {
        return sceneBuilder.isDisplayFps();
    }

    @Override
    public boolean isAuxiliaryBuffers() {
        return sceneBuilder.isAuxiliaryBuffers();
    }

    @Override
    public boolean isTextureColorPicking() {
        return sceneBuilder.isTextureColorPicking();
    }

    @Override
    public boolean isFullScreenAntiAliasing() {
        return sceneBuilder.isFullScreenAntiAliasing();
    }

    @Override
    public boolean isDebugColorPicking() {
        return sceneBuilder.isDebugColorPicking();
    }

    @Override
    public boolean isLighting() {
        return sceneBuilder.isLighting();
    }

    @Override
    public boolean isBlending() {
        return sceneBuilder.isBlending();
    }

    @Override
    public int hashCode() {
        return sceneBuilder.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return sceneBuilder.equals(obj);
    }

    @Override
    public String toString() {
        return sceneBuilder.toString();
    }

    @Override
    public SceneBuilder setDrawableTypes(final String drawableTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E extends Enum<E>> SceneBuilder setHint(final E hint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E extends Enum<E>> SceneBuilder addHint(final E hint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E extends Enum<E>> SceneBuilder removeHint(final E hint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SceneBuilder setHints(final String hints) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setGLCapabilities(final GLCapabilities glCapabilities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setBackground(final Color background) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setDebug(final boolean debug) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setDisplayFps(final boolean displayFps) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setMaxInitializations(final int maxInitializations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setNumberOfSceneDrawingPasses(final int numberOfSceneDrawingPasses) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setNumberOfUserInterfaceDrawingPasses(final int numberOfUserInterfaceDrawingPasses) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setTargetFps(final int targetFps) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setVerbose(final boolean verbose) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setAuxiliaryBuffers(final boolean auxiliaryBuffers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setBlending(final boolean blending) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setDebugColorPicking(final boolean colorPicking) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setFullScreenAntiAliasing(final boolean fullScrenAntiAliasing) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSceneBuilder setLighting(final boolean lighting) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SceneBuilder setTextureColorPicking(final boolean textureColorPicking) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SceneBuilder updateDrawableTypes(final Class<? extends Drawable>... drawableTypes) {
        throw new UnsupportedOperationException();
    }
}
