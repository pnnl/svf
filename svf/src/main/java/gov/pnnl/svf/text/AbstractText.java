package gov.pnnl.svf.text;

import gov.pnnl.svf.core.util.TextState;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import java.awt.Font;

/**
 * Abstract implementation of text.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractText implements TextRenderer {

    // TODO expiring text is currently problematic
    //    private static final long TIMEOUT = 1000L * 60L * 1L; // 1 minute
    //    private long timestamp;
    protected final TextState state = new TextState();
    protected final Scene scene;
    protected final Font font;

    /**
     * Constructor
     *
     * @param scene       reference to the scene
     * @param font        the font for the text renderer
     * @param antiAliased true for anti aliased text
     * @param mipMaps     true for mip maps
     * @param fractional  true for fractional metrics
     * @param smoothing   true for smoothing
     */
    protected AbstractText(final Scene scene, final Font font,
                           final boolean antiAliased, final boolean mipMaps, final boolean fractional, final boolean smoothing) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        if (font == null) {
            throw new NullPointerException("font");
        }
        this.scene = scene;
        this.font = font;
        state.setAntiAliased(antiAliased);
        state.setMipMaps(mipMaps);
        state.setFractional(fractional);
        state.setSmoothing(smoothing);
        // expiring text is currently problematic
        //        timestamp = System.currentTimeMillis();
    }

    @Override
    public boolean isInitialized() {
        synchronized (this) {
            return state.isInitialized();
        }
    }

    @Override
    public boolean isSlow() {
        return false;
    }

    @Override
    public DrawingPass getDrawingPass() {
        return DrawingPass.ALL;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            updateTimestamp();
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
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public boolean isExpired() {
        // expiring text is currently problematic
        //        synchronized (this) {
        //            return System.currentTimeMillis() - timestamp > TIMEOUT;
        //        }
        return false;
    }

    @Override
    public boolean isDirty() {
        synchronized (this) {
            return state.isDirty();
        }
    }

    @Override
    public boolean isAntiAliased() {
        synchronized (this) {
            return state.isAntiAliased();
        }
    }

    @Override
    public boolean isMipMaps() {
        synchronized (this) {
            return state.isMipMaps();
        }
    }

    @Override
    public boolean isFractional() {
        synchronized (this) {
            return state.isFractional();
        }
    }

    @Override
    public boolean isSmoothing() {
        synchronized (this) {
            return state.isSmoothing();
        }
    }

    /**
     * Update the current timestamp.
     */
    protected void updateTimestamp() {
        // expiring text is currently problematic
        //        synchronized (this) {
        //            timestamp = System.currentTimeMillis();
        //        }
    }

}
