package gov.pnnl.svf.color;

import gov.pnnl.svf.core.color.Color;
import java.io.Serializable;

/**
 * Immutable color class optimized for use with JOGL. This class provides a
 * convenience constructor for use with JavaFX.
 *
 * @author Amelia Bleeker
 */
public class FxColor extends Color implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     *
     * @param color AWT color
     */
    public FxColor(final javafx.scene.paint.Color color) {
        super((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }

    /**
     * Constructor
     *
     * @param color AWT color
     * @param alpha the alpha value to use
     */
    public FxColor(final javafx.scene.paint.Color color, final float alpha) {
        super((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), alpha);
    }

    /**
     * Constructor
     *
     * @param color AWT color
     * @param alpha the alpha value to use
     */
    public FxColor(final javafx.scene.paint.Color color, final int alpha) {
        super((int) (color.getRed() * 255.0), (int) (color.getGreen() * 255.0), (int) (color.getBlue() * 255.0), alpha);
    }

    /**
     * @return the color as an JavaFX color
     */
    public javafx.scene.paint.Color toFxColor() {
        return new javafx.scene.paint.Color(getRed(), getGreen(), getBlue(), getAlpha());
    }
}
