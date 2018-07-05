package gov.pnnl.svf.text;

import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.service.DrawableService;
import java.awt.Font;

/**
 * This service is responsible for maintaining a cache of text. Just add an
 * implementation of this class to the scene and then retrieve the instance of
 * it by using the scene lookup.
 *
 * @author Amelia Bleeker
 */
public interface TextService extends DrawableService, Disposable {

    /**
     * The default for a new TextRenderer.
     *
     * @return true if anti aliased
     */
    boolean isAntiAliased();

    /**
     * The default for a new TextRenderer.
     *
     * @return true if mip maps
     */
    boolean isMipMaps();

    /**
     * The default for a new TextRenderer.
     *
     * @return true if fractional metrics
     */
    boolean isFractional();

    /**
     * The default for a new TextRenderer.
     *
     * @return true if smoothing
     */
    boolean isSmoothing();

    /**
     * The default for a new TextRenderer.
     *
     * @param antiAliased true if anti aliased
     *
     * @return the text service
     */
    TextService setAntiAliased(boolean antiAliased);

    /**
     * The default for a new TextRenderer.
     *
     * @param mipMaps true if mip maps
     *
     * @return the text service
     */
    TextService setMipMaps(boolean mipMaps);

    /**
     * The default for a new TextRenderer.
     *
     * @param fractional true if fractional metrics
     *
     * @return the text service
     */
    TextService setFractional(boolean fractional);

    /**
     * The default for a new TextRenderer.
     *
     * @param smoothing true if smoothing
     *
     * @return the text service
     */
    TextService setSmoothing(boolean smoothing);

    /**
     * Get a cached or new text renderer based on the supplied parameters. This
     * method must be called with an active OpenGL context.
     *
     * @param font the font for the text renderer
     *
     * @return a cached or new text renderer
     */
    TextRenderer getTextRenderer(Font font);

    /**
     * Get a cached or new text renderer based on the supplied parameters. This
     * method must be called with an active OpenGL context.
     *
     * @param font        the font for the text renderer
     * @param antiAliased true if anti aliased
     * @param mipMaps     true for mip maps
     * @param fractional  true for fractional metrics
     * @param smoothing   true for smoothing
     *
     * @return a cached or new text renderer
     */
    TextRenderer getTextRenderer(Font font, boolean antiAliased, boolean mipMaps, boolean fractional, boolean smoothing);

    /**
     * Dispose a cached text renderer based on the supplied parameters. This
     * will dispose all of the text renderers utilizing this font.
     *
     * @param font the font for the text renderer
     */
    void disposeTextRenderer(Font font);

    /**
     * Dispose a cached text renderer based on the supplied parameters. This
     * will dispose the exact text renderer utilizing this font and options.
     *
     * @param font        the font for the text renderer
     * @param antiAliased true if anti aliased
     * @param mipMaps     true for mip maps
     * @param fractional  true for fractional metrics
     * @param smoothing   true for smoothing
     */
    void disposeTextRenderer(Font font, boolean antiAliased, boolean mipMaps, boolean fractional, boolean smoothing);
}
