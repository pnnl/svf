package gov.pnnl.svf.text;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.util.TextState;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Initializable;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.service.AbstractDrawableService;
import gov.pnnl.svf.update.UninitializeTask;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for text services.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractTextService extends AbstractDrawableService implements TextService {

    /**
     * map of all of the text renderers
     */
    protected final Map<Font, Map<TextState, TextRenderer>> map = new HashMap<>();
    private final TextState state = new TextState();

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    protected AbstractTextService(final Scene scene) {
        super(scene, 0);
        state.setAntiAliased();
        state.setSmoothing();
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return state.isDisposed();
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (state.isDisposed()) {
                return;
            }
            state.setDisposed();
        }
        UninitializeTask.schedule(getScene(), new DisposeSupport(this));
    }

    @Override
    public boolean isAntiAliased() {
        synchronized (this) {
            return state.isAntiAliased();
        }
    }

    @Override
    public boolean isFractional() {
        synchronized (this) {
            return state.isFractional();
        }
    }

    @Override
    public boolean isMipMaps() {
        synchronized (this) {
            return state.isMipMaps();
        }
    }

    @Override
    public boolean isSmoothing() {
        synchronized (this) {
            return state.isSmoothing();
        }
    }

    @Override
    public TextService setAntiAliased(final boolean antiAliased) {
        synchronized (this) {
            state.setAntiAliased(antiAliased);
        }
        return this;
    }

    @Override
    public TextService setFractional(final boolean fractional) {
        synchronized (this) {
            state.setFractional(fractional);
        }
        return this;
    }

    @Override
    public TextService setMipMaps(final boolean mipMaps) {
        synchronized (this) {
            state.setMipMaps(mipMaps);
        }
        return this;
    }

    @Override
    public TextService setSmoothing(final boolean smoothing) {
        synchronized (this) {
            state.setSmoothing(smoothing);
        }
        return this;
    }

    @Override
    public TextRenderer getTextRenderer(final Font font) {
        synchronized (this) {
            return getTextRenderer(font, state);
        }
    }

    @Override
    public TextRenderer getTextRenderer(final Font font, final boolean antiAliased, final boolean mipMaps, final boolean fractional, final boolean smoothing) {
        synchronized (this) {
            return getTextRenderer(font, new TextState()
                                   .setAntiAliased(antiAliased)
                                   .setMipMaps(mipMaps)
                                   .setFractional(fractional)
                                   .setSmoothing(smoothing));
        }
    }

    @Override
    public void disposeTextRenderer(final Font font) {
        if (font == null) {
            throw new NullPointerException("font");
        }
        UninitializeTask.schedule(getScene(), new DisposeSupport(this, font, null));
    }

    @Override
    public void disposeTextRenderer(final Font font, final boolean antiAliased, final boolean mipMaps, final boolean fractional, final boolean smoothing) {
        if (font == null) {
            throw new NullPointerException("font");
        }
        final TextState state = new TextState()
                .setAntiAliased(antiAliased)
                .setMipMaps(mipMaps)
                .setFractional(fractional)
                .setSmoothing(smoothing);
        UninitializeTask.schedule(getScene(), new DisposeSupport(this, font, state));
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // not needed
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // dispose and remove the expired renderers
        // expiring text is currently problematic
        //        synchronized (this) {
        //            for (final Iterator<Entry<String, Map<TextState, TextRenderer>>> it = map.entrySet().iterator(); it.hasNext();) {
        //                final Entry<String, Map<TextState, TextRenderer>> entry = it.next();
        //                for (final Iterator<Entry<TextState, TextRenderer>> i = entry.getValue().entrySet().iterator(); i.hasNext();) {
        //                    final Entry<TextState, TextRenderer> text = i.next();
        //                    if (text.getValue().isExpired()) {
        //                        text.getValue().dispose();
        //                        i.remove();
        //                    }
        //                }
        //            }
        //        }
    }

    private TextRenderer getTextRenderer(final Font font, final TextState state) {
        if (font == null) {
            throw new NullPointerException("font");
        }
        synchronized (this) {
            Map<TextState, TextRenderer> values = map.get(font);
            if (values == null) {
                values = new HashMap<>();
                map.put(font, values);
            }
            TextRenderer value = values.get(state);
            if (value == null) {
                value = newTextRenderer(font, state);
                values.put(state, value);
            }
            return value;
        }
    }

    /**
     * Create a new text renderer.
     *
     * @param font  the font for the new text
     * @param state the state to use for the new text
     *
     * @return a new text renderer
     */
    protected abstract TextRenderer newTextRenderer(Font font, TextState state);

    protected static class DisposeSupport implements Initializable {

        protected final AbstractTextService factory;
        protected boolean initialized = true;
        protected final Font font;
        protected final TextState state;

        /**
         * Constructor
         *
         * @param factory
         */
        protected DisposeSupport(final AbstractTextService factory) {
            this(factory, null, null);
        }

        /**
         * Constructor
         *
         * @param factory
         * @param font
         * @param state
         */
        protected DisposeSupport(final AbstractTextService factory, final Font font, final TextState state) {
            super();
            this.factory = factory;
            this.font = font;
            this.state = state;
        }

        @Override
        public void initialize(final GL2 gl, final GLUgl2 glu) {
            // no operation
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }

        @Override
        public boolean isSlow() {
            return false;
        }

        @Override
        public Scene getScene() {
            return factory.getScene();
        }

        @Override
        public boolean isVisible() {
            return true;
        }

        @Override
        public DrawingPass getDrawingPass() {
            return DrawingPass.ALL;
        }

        @Override
        public void unInitialize(final GL2 gl, final GLUgl2 glu) {
            synchronized (factory) {
                if (font != null && state != null) {
                    // dispose a specific text renderer
                    final Map<TextState, TextRenderer> values = factory.map.get(font);
                    if (values != null) {
                        final TextRenderer renderer = values.remove(state);
                        if (renderer != null) {
                            renderer.dispose();
                        }
                    }
                } else if (font != null) {// && state == null) {
                    // dispose all text renderers for the specified font
                    final Map<TextState, TextRenderer> values = factory.map.remove(font);
                    if (values != null) {
                        for (final TextRenderer renderer : values.values()) {
                            renderer.dispose();
                        }
                    }
                } else {
                    // dispose all of them
                    for (final Map<TextState, TextRenderer> values : factory.map.values()) {
                        for (final TextRenderer renderer : values.values()) {
                            renderer.dispose();
                        }
                    }
                    factory.map.clear();
                }
            }
            initialized = false;
        }
    }
}
