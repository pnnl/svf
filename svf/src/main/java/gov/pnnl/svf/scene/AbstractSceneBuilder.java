package gov.pnnl.svf.scene;

import com.jogamp.opengl.GLCapabilities;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.hint.CameraHint;
import gov.pnnl.svf.hint.PickingHint;
import gov.pnnl.svf.support.AbstractShaderSupport;
import gov.pnnl.svf.support.BlendingSupport;
import gov.pnnl.svf.support.ClippingPlaneSupport;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.texture.TextureRegionListSupport;
import gov.pnnl.svf.texture.TextureSupport;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builder object used for instantiating a new GLScene.
 *
 * @author Amelia Bleeker
 * @param <T> type of GL scene builder object
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractSceneBuilder<T extends AbstractSceneBuilder> implements SceneBuilder {

    private static final Logger logger = Logger.getLogger(AbstractSceneBuilder.class.toString());
    /**
     * The default target frames per second (FPS)
     */
    public static final int DEFAULT_TARGET_FPS = 60;
    /**
     * default background color
     */
    public static final Color DEFAULT_BACKGROUND = Color.BLACK;
    private static final Class<? extends Drawable>[] DEFAULT_DRAWABLE_TYPES_ARRAY = new Class[]{
        ColorSupport.class,
        BlendingSupport.class,
        TextureSupport.class,
        TextureRegionListSupport.class,
        AbstractShaderSupport.class,
        ClippingPlaneSupport.class};
    private static final String HINT_PACKAGE = "gov.pnnl.svf.hint.";
    /**
     * The default list of drawable support types in order of drawing before the
     * actor.
     */
    public static final List<Class<? extends Drawable>> DEFAULT_DRAWABLE_TYPES = Collections.unmodifiableList(Arrays.asList(DEFAULT_DRAWABLE_TYPES_ARRAY));
    private String hints = AbstractSceneBuilder.enumToProperty(new Enum<?>[]{CameraHint.DRAGGING, PickingHint.COLOR_PICKING});
    private String drawableTypes = AbstractSceneBuilder.classToProperty(DEFAULT_DRAWABLE_TYPES_ARRAY);
    private GLCapabilities glCapabilities = null;
    private int targetFps = DEFAULT_TARGET_FPS;
    private Color background = DEFAULT_BACKGROUND;
    private int maxInitializations = 100000;
    private int numberOfSceneDrawingPasses = 1;
    private int numberOfUserInterfaceDrawingPasses = 1;
    private boolean debug = false;
    private boolean verbose = false;
    private boolean displayFps = false;
    private boolean auxiliaryBuffers = false;
    private boolean textureColorPicking = false;
    private boolean fullScreenAntiAliasing = true;
    private boolean debugColorPicking = false;
    private boolean lighting = true;
    private boolean blending = true;

    /**
     * Default Constructor
     */
    protected AbstractSceneBuilder() {
        super();
    }

    /**
     * Copy Constructor
     *
     * @param sceneBuilder the scene builder to copy
     */
    protected AbstractSceneBuilder(final SceneBuilder sceneBuilder) {
        super();
        if (sceneBuilder == null) {
            throw new NullPointerException("sceneBuilder");
        }
        auxiliaryBuffers = sceneBuilder.isAuxiliaryBuffers();
        background = sceneBuilder.getBackground();
        blending = sceneBuilder.isBlending();
        debugColorPicking = sceneBuilder.isDebugColorPicking();
        debug = sceneBuilder.isDebug();
        displayFps = sceneBuilder.isDisplayFps();
        fullScreenAntiAliasing = sceneBuilder.isFullScreenAntiAliasing();
        glCapabilities = sceneBuilder.getGLCapabilities();
        lighting = sceneBuilder.isLighting();
        maxInitializations = sceneBuilder.getMaxInitializations();
        numberOfSceneDrawingPasses = sceneBuilder.getNumberOfSceneDrawingPasses();
        numberOfUserInterfaceDrawingPasses = sceneBuilder.getNumberOfUserInterfaceDrawingPasses();
        targetFps = sceneBuilder.getTargetFps();
        textureColorPicking = sceneBuilder.isTextureColorPicking();
        verbose = sceneBuilder.isVerbose();
        hints = sceneBuilder.getHints();
    }

    @Override
    public String getDrawableTypes() {
        return drawableTypes;
    }

    @Override
    public Set<? extends Enum<?>> copyHints() {
        final Set<Enum<?>> copy = new HashSet<>();
        final Enum[] enums = enumFromProperty(hints);
        for (final Enum e : enums) {
            copy.add(e);
        }
        return copy;
    }

    @Override
    public <E extends Enum<E>> Set<E> copyHints(final Class<E> type) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        // copy the hints
        final Set<E> copy = EnumSet.noneOf(type);
        final Enum[] enums = enumFromProperty(hints);
        for (final Enum e : enums) {
            if (e != null && type.isInstance(e)) {
                copy.add((E) e);
            }
        }
        return copy;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Enum<E>> SceneBuilder setHint(final E hint) {
        if (hint == null) {
            throw new NullPointerException("hint");
        }
        final Set<Enum<?>> copy = new HashSet<>();
        final Enum[] enums = enumFromProperty(hints);
        for (final Enum e : enums) {
            if (e != null && !hint.getClass().isInstance(e)) {
                copy.add(e);
            }
        }
        copy.add(hint);
        hints = enumToProperty(copy.toArray(new Enum[copy.size()]));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Enum<E>> SceneBuilder addHint(final E hint) {
        if (hint == null) {
            throw new NullPointerException("hint");
        }
        final Set<Enum<?>> copy = new HashSet<>();
        final Enum[] enums = enumFromProperty(hints);
        for (final Enum e : enums) {
            if (e != null) {
                copy.add(e);
            }
        }
        copy.add(hint);
        hints = enumToProperty(copy.toArray(new Enum[copy.size()]));
        return this;
    }

    @Override
    public <E extends Enum<E>> SceneBuilder removeHint(final E hint) {
        if (hint == null) {
            throw new NullPointerException("hint");
        }
        final Set<Enum<?>> copy = new HashSet<>();
        final Enum[] enums = enumFromProperty(hints);
        for (final Enum e : enums) {
            if (e != null) {
                copy.add(e);
            }
        }
        copy.remove(hint);
        hints = enumToProperty(copy.toArray(new Enum[copy.size()]));
        return this;
    }

    @Override
    public String getHints() {
        return hints;
    }

    @Override
    public SceneBuilder setHints(final String hints) {
        this.hints = hints;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Drawable>[] copyDrawableTypes() {
        return (Class<? extends Drawable>[]) classFromProperty(drawableTypes);
    }

    @Override
    public GLCapabilities getGLCapabilities() {
        return glCapabilities;
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public int getMaxInitializations() {
        return maxInitializations;
    }

    @Override
    public int getNumberOfSceneDrawingPasses() {
        return numberOfSceneDrawingPasses;
    }

    @Override
    public int getNumberOfUserInterfaceDrawingPasses() {
        return numberOfUserInterfaceDrawingPasses;
    }

    @Override
    public int getTargetFps() {
        return targetFps;
    }

    @Override
    public boolean isAuxiliaryBuffers() {
        return auxiliaryBuffers;
    }

    @Override
    public boolean isTextureColorPicking() {
        return textureColorPicking;
    }

    @Override
    public boolean isFullScreenAntiAliasing() {
        return fullScreenAntiAliasing;
    }

    @Override
    public boolean isDebugColorPicking() {
        return debugColorPicking;
    }

    @Override
    public boolean isLighting() {
        return lighting;
    }

    @Override
    public boolean isBlending() {
        return blending;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public boolean isVerbose() {
        return verbose;
    }

    @Override
    public boolean isDisplayFps() {
        return displayFps;
    }

    @Override
    public T setDrawableTypes(final String drawableTypes) {
        this.drawableTypes = drawableTypes;
        return (T) this;
    }

    @Override
    public T updateDrawableTypes(final Class<? extends Drawable>... drawableTypes) {
        if (drawableTypes == null) {
            this.drawableTypes = AbstractSceneBuilder.classToProperty(DEFAULT_DRAWABLE_TYPES_ARRAY);
        } else if (drawableTypes[0] == null) {
            this.drawableTypes = AbstractSceneBuilder.classToProperty(new Class[0]);
        } else {
            this.drawableTypes = AbstractSceneBuilder.classToProperty(drawableTypes);
        }
        return (T) this;
    }

    @Override
    public T setAuxiliaryBuffers(final boolean auxiliaryBuffers) {
        this.auxiliaryBuffers = auxiliaryBuffers;
        return (T) this;
    }

    @Override
    public T setTextureColorPicking(final boolean textureColorPicking) {
        this.textureColorPicking = textureColorPicking;
        return (T) this;
    }

    @Override
    public T setFullScreenAntiAliasing(final boolean fullScrenAntiAliasing) {
        this.fullScreenAntiAliasing = fullScrenAntiAliasing;
        return (T) this;
    }

    @Override
    public T setDebugColorPicking(final boolean colorPicking) {
        this.debugColorPicking = colorPicking;
        return (T) this;
    }

    @Override
    public T setLighting(final boolean lighting) {
        this.lighting = lighting;
        return (T) this;
    }

    @Override
    public T setBlending(final boolean blending) {
        this.blending = blending;
        return (T) this;
    }

    @Override
    public T setGLCapabilities(final GLCapabilities glCapabilities) {
        this.glCapabilities = glCapabilities;
        return (T) this;
    }

    @Override
    public T setBackground(final Color background) {
        this.background = background;
        return (T) this;
    }

    @Override
    public T setDebug(final boolean debug) {
        this.debug = debug;
        return (T) this;
    }

    @Override
    public T setVerbose(final boolean verbose) {
        this.verbose = verbose;
        return (T) this;
    }

    @Override
    public T setDisplayFps(final boolean displayFps) {
        this.displayFps = displayFps;
        return (T) this;
    }

    @Override
    public T setMaxInitializations(final int maxInitializations) {
        this.maxInitializations = maxInitializations;
        return (T) this;
    }

    @Override
    public T setNumberOfSceneDrawingPasses(final int numberOfSceneDrawingPasses) {
        this.numberOfSceneDrawingPasses = numberOfSceneDrawingPasses;
        return (T) this;
    }

    @Override
    public T setNumberOfUserInterfaceDrawingPasses(final int numberOfUserInterfaceDrawingPasses) {
        this.numberOfUserInterfaceDrawingPasses = numberOfUserInterfaceDrawingPasses;
        return (T) this;
    }

    @Override
    public T setTargetFps(final int targetFps) {
        this.targetFps = targetFps;
        return (T) this;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.drawableTypes != null ? this.drawableTypes.hashCode() : 0);
        hash = 67 * hash + (this.hints != null ? this.hints.hashCode() : 0);
        hash = 67 * hash + (this.glCapabilities != null ? this.glCapabilities.hashCode() : 0);
        hash = 67 * hash + this.targetFps;
        hash = 67 * hash + (this.background != null ? this.background.hashCode() : 0);
        hash = 67 * hash + this.maxInitializations;
        hash = 67 * hash + this.numberOfSceneDrawingPasses;
        hash = 67 * hash + this.numberOfUserInterfaceDrawingPasses;
        hash = 67 * hash + (this.debug ? 1 : 0);
        hash = 67 * hash + (this.verbose ? 1 : 0);
        hash = 67 * hash + (this.displayFps ? 1 : 0);
        hash = 67 * hash + (this.auxiliaryBuffers ? 1 : 0);
        hash = 67 * hash + (this.textureColorPicking ? 1 : 0);
        hash = 67 * hash + (this.fullScreenAntiAliasing ? 1 : 0);
        hash = 67 * hash + (this.debugColorPicking ? 1 : 0);
        hash = 67 * hash + (this.lighting ? 1 : 0);
        hash = 67 * hash + (this.blending ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SceneBuilder)) {
            return false;
        }
        final SceneBuilder other = (SceneBuilder) obj;
        final String otherDrawableTypes = other.getDrawableTypes();
        final String otherHints = other.getHints();
        final GLCapabilities otherGLCapabilities = other.getGLCapabilities();
        final int otherTargetFps = other.getTargetFps();
        final Color otherBackground = other.getBackground();
        final int otherMaxInitializations = other.getMaxInitializations();
        final int otherNumberOfSceneDrawingPasses = other.getNumberOfSceneDrawingPasses();
        final int otherNumberOfUserInterfaceDrawingPasses = other.getNumberOfUserInterfaceDrawingPasses();
        final boolean otherDebug = other.isDebug();
        final boolean otherVerbose = other.isVerbose();
        final boolean otherDisplayFps = other.isDisplayFps();
        final boolean otherAuxiliaryBuffers = other.isAuxiliaryBuffers();
        final boolean otherTextureColorPicking = other.isTextureColorPicking();
        final boolean otherFullScreenAntiAliasing = other.isFullScreenAntiAliasing();
        final boolean otherDebugColorPicking = other.isDebugColorPicking();
        final boolean otherLighting = other.isLighting();
        final boolean otherBlending = other.isBlending();
        if (!Objects.equals(this.drawableTypes, otherDrawableTypes)) {
            return false;
        }
        if (!Objects.equals(this.hints, otherHints)) {
            return false;
        }
        if (this.glCapabilities != otherGLCapabilities && (this.glCapabilities == null || !this.glCapabilities.equals(otherGLCapabilities))) {
            return false;
        }
        if (this.targetFps != otherTargetFps) {
            return false;
        }
        if (this.background != otherBackground && (this.background == null || !this.background.equals(otherBackground))) {
            return false;
        }
        if (this.maxInitializations != otherMaxInitializations) {
            return false;
        }
        if (this.numberOfSceneDrawingPasses != otherNumberOfSceneDrawingPasses) {
            return false;
        }
        if (this.numberOfUserInterfaceDrawingPasses != otherNumberOfUserInterfaceDrawingPasses) {
            return false;
        }
        if (this.debug != otherDebug) {
            return false;
        }
        if (this.verbose != otherVerbose) {
            return false;
        }
        if (this.displayFps != otherDisplayFps) {
            return false;
        }
        if (this.auxiliaryBuffers != otherAuxiliaryBuffers) {
            return false;
        }
        if (this.textureColorPicking != otherTextureColorPicking) {
            return false;
        }
        if (this.fullScreenAntiAliasing != otherFullScreenAntiAliasing) {
            return false;
        }
        if (this.debugColorPicking != otherDebugColorPicking) {
            return false;
        }
        if (this.lighting != otherLighting) {
            return false;
        }
        if (this.blending != otherBlending) {
            return false;
        }
        return true;
    }

    private static String classToProperty(final Class<?>... types) {
        return Arrays.toString(types)
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("interface", "")
                .replaceAll("class", "")
                .replaceAll(" ", "");
    }

    private static Class<?>[] classFromProperty(final String property) {
        final String[] classes = property.split(",");
        final Class<?>[] types = new Class[classes.length];
        for (int i = 0; i < types.length; i++) {
            try {
                types[i] = (Class<?>) Class.forName(classes[i]);
            } catch (final ClassNotFoundException ex) {
                AbstractSceneBuilder.logger.log(Level.WARNING, "Error converting property into class.", ex);
            }
        }
        return types;
    }

    private static String enumToProperty(final Enum<?>... enums) {
        final StringBuilder sb = new StringBuilder();
        for (final Enum<?> e : enums) {
            if (e.getClass().getName().startsWith(HINT_PACKAGE)) {
                sb.append(e.getClass().getSimpleName())
                        .append(".")
                        .append(e.name())
                        .append(",");
            } else {
                sb.append(e.getClass().getName())
                        .append(".")
                        .append(e.name())
                        .append(",");
            }
        }
        if (enums.length > 0) {
            sb.delete(sb.length() - 1, sb.length() - 1);
        }
        return sb.toString().replaceAll(" ", "");
    }

    private static Enum<?>[] enumFromProperty(final String property) {
        final String[] instances = property.split(",");
        final Enum<?>[] enums = new Enum[instances.length];
        for (int i = 0; i < enums.length; i++) {
            if (instances[i] != null && !instances[i].isEmpty()) {
                try {
                    final String cName = instances[i].substring(0, instances[i].lastIndexOf('.'));
                    final String fName = cName.contains(".") ? cName : HINT_PACKAGE + cName;
                    final String eName = instances[i].substring(instances[i].lastIndexOf('.') + 1);
                    final Class<? extends Enum> type = (Class<? extends Enum>) Class.forName(fName);
                    enums[i] = (Enum<?>) Enum.valueOf(type, eName);
                } catch (final ClassNotFoundException | IllegalArgumentException ex) {
                    AbstractSceneBuilder.logger.log(Level.WARNING, "Error converting property into enumeration.", ex);
                }
            }
        }
        return enums;
    }
}
