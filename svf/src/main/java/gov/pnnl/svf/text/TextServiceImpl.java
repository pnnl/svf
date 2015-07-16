package gov.pnnl.svf.text;

import gov.pnnl.svf.core.util.TextState;
import gov.pnnl.svf.scene.Scene;
import java.awt.Font;

/**
 * Implementation of the text renderer service that utilizes textures and
 * regions for individual characters.
 *
 * @author Arthur Bleeker
 */
public class TextServiceImpl extends AbstractTextService {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    private TextServiceImpl(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene reference to the scene
     *
     * @return a new instance
     */
    public static TextServiceImpl newInstance(final Scene scene) {
        final TextServiceImpl instance = new TextServiceImpl(scene);
        scene.add(instance);
        return instance;
    }

    @Override
    protected TextRenderer newTextRenderer(final Font font, final TextState state) {
        if (font == null) {
            throw new NullPointerException("font");
        }
        if (state == null) {
            throw new NullPointerException("state");
        }
        return new TextRendererImpl(getScene(), font, state.isAntiAliased(), state.isMipMaps(), state.isFractional(), state.isSmoothing());
    }

}
