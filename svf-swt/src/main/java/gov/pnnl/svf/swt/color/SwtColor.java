package gov.pnnl.svf.swt.color;

import gov.pnnl.svf.core.color.Color;
import java.io.Serializable;

/**
 * Immutable color class optimized for use with JOGL. This class provides a
 * convenience constructor for use with SWT.
 *
 * @author Arthur Bleeker
 */
public class SwtColor extends Color implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public SwtColor() {
        super();
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public SwtColor(final byte r, final byte g, final byte b) {
        super(r, g, b);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     * @param a alpha component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public SwtColor(final byte r, final byte g, final byte b, final byte a) {
        super(r, g, b, a);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0.0 &le; x &le; 1.0}
     * @param g green component {x: 0.0 &le; x &le; 1.0}
     * @param b blue component {x: 0.0 &le; x &le; 1.0}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0.0
     *                                  to 1.0 inclusive
     */
    public SwtColor(final float r, final float g, final float b) {
        super(r, g, b);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0.0 &le; x &le; 1.0}
     * @param g green component {x: 0.0 &le; x &le; 1.0}
     * @param b blue component {x: 0.0 &le; x &le; 1.0}
     * @param a alpha component {x: 0.0 &le; x &le; 1.0}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0.0
     *                                  to 1.0 inclusive
     */
    public SwtColor(final float r, final float g, final float b, final float a) {
        super(r, g, b, a);
    }

    /**
     * Constructor
     *
     * @param hex a hex string in the form of RGB, RGBA, RRGGBB, or RRGGBBAA
     *            with or without a preceding #.
     *
     * @throws IllegalArgumentException  if r, g, b, or a is not in the range
     *                                   0.0 to 1.0 inclusive
     * @throws NumberFormatException     if the string is not a valid hex string
     * @throws IndexOutOfBoundsException if the hex string is not at least 6
     *                                   characters
     */
    public SwtColor(final String hex) {
        super(hex);
    }

    /**
     * Constructor
     *
     * @param color the base color to use
     * @param alpha the alpha value to use
     *
     * @throws NullPointerException     if the color is null
     * @throws IllegalArgumentException if alpha is not in the range 0.0 to 1.0
     *                                  inclusive
     */
    public SwtColor(final Color color, final float alpha) {
        super(color, alpha);
    }

    /**
     * Constructor
     *
     * @param color the base color to use
     * @param alpha the alpha value to use
     *
     * @throws NullPointerException     if the color is null
     * @throws IllegalArgumentException if alpha is not in the range 0 to 255
     *                                  inclusive
     */
    public SwtColor(final Color color, final int alpha) {
        super(color, alpha);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public SwtColor(final int r, final int g, final int b) {
        super(r, g, b);
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     * @param a alpha component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public SwtColor(final int r, final int g, final int b, final int a) {
        super(r, g, b, a);
    }

    /**
     * Constructor
     *
     * @param color SWT color
     */
    public SwtColor(final org.eclipse.swt.graphics.Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Constructor
     *
     * @param color SWT color
     * @param alpha the alpha value to use
     */
    public SwtColor(final org.eclipse.swt.graphics.Color color, final float alpha) {
        super(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
    }

    /**
     * Constructor
     *
     * @param color SWT color
     * @param alpha the alpha value to use
     */
    public SwtColor(final org.eclipse.swt.graphics.Color color, final int alpha) {
        super(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    /**
     * Constructor
     *
     * @param color RGB color
     */
    public SwtColor(final org.eclipse.swt.graphics.RGB color) {
        super(color.red, color.green, color.blue);
    }

    /**
     * Constructor
     *
     * @param color RGB color
     * @param alpha the alpha value to use
     */
    public SwtColor(final org.eclipse.swt.graphics.RGB color, final float alpha) {
        super(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, alpha);
    }

    /**
     * Constructor
     *
     * @param color RGB color
     * @param alpha the alpha value to use
     */
    public SwtColor(final org.eclipse.swt.graphics.RGB color, final int alpha) {
        super(color.red, color.green, color.blue, alpha);
    }

    /**
     * @return the color as an SWT color
     */
    public org.eclipse.swt.graphics.Color toSwtColor() {
        return new org.eclipse.swt.graphics.Color(null, (int) (getRed() * 255.0f), (int) (getGreen() * 255.0f), (int) (getBlue() * 255.0f));
    }

    /**
     * @return the color as an RGB color
     */
    public org.eclipse.swt.graphics.RGB toRgbColor() {
        return new org.eclipse.swt.graphics.RGB((int) (getRed() * 255.0f), (int) (getGreen() * 255.0f), (int) (getBlue() * 255.0f));
    }
}
