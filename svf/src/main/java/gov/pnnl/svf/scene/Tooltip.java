package gov.pnnl.svf.scene;

import gov.pnnl.svf.core.color.Color;
import java.awt.Font;

/**
 * Interface for working with the toolip in a scene.
 *
 * @author Amelia Bleeker
 */
public interface Tooltip extends Observable, Disposable {

    /**
     * String representation of a field in this object.
     */
    String BACKGROUND_COLOR = "backgroundColor";
    /**
     * String representation of a field in this object.
     */
    String BORDER_COLOR = "borderColor";
    /**
     * String representation of a field in this object.
     */
    String BORDER_PADDING = "borderPadding";
    /**
     * String representation of a field in this object.
     */
    String BORDER_THICKNESS = "borderThickness";
    /**
     * String representation of a field in this object.
     */
    String SENSITIVITY = "sensitivity";
    /**
     * The default background color 'platinum' used for the tooltip.
     */
    Color DEFAULT_BACKGROUND_COLOR = new Color("#E5E4E2"); // platinum
    /**
     * The default border color 'charcoal' used for the tooltip.
     */
    Color DEFAULT_BORDER_COLOR = new Color("#36454F"); // charcoal
    /**
     * The default font used for the tooltip.
     */
    Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 14);
    /**
     * The default font color 'charcoal' used for the tooltip.
     */
    Color DEFAULT_FONT_COLOR = new Color("#36454F"); // charcoal
    /**
     * String representation of a field in this object.
     */
    String FONT = "font";
    /**
     * String representation of a field in this object.
     */
    String FONT_COLOR = "fontColor";

    /**
     * Get the value of backgroundColor
     *
     * @return the value of backgroundColor
     */
    Color getBackgroundColor();

    /**
     * Get the value of borderColor
     *
     * @return the value of borderColor
     */
    Color getBorderColor();

    /**
     * Get the value of borderPadding
     *
     * @return the value of borderPadding
     */
    double getBorderPadding();

    /**
     * Get the value of borderThickness
     *
     * @return the value of borderThickness
     */
    double getBorderThickness();

    /**
     * Get the value of font
     *
     * @return the value of font
     */
    Font getFont();

    /**
     * Get the value of fontColor
     *
     * @return the value of fontColor
     */
    Color getFontColor();

    /**
     * The sensitivity of the tooltip. This is the distance of mouse movement
     * required to hide the tooltip.
     *
     * @return the sensitivity
     */
    int getSensitivity();

    /**
     * Set the value of backgroundColor
     *
     * @param backgroundColor new value of backgroundColor
     *
     * @throws NullPointerException if background color is null
     */
    void setBackgroundColor(Color backgroundColor);

    /**
     * Set the value of borderColor
     *
     * @param borderColor new value of borderColor
     *
     * @throws NullPointerException if the border color is null
     */
    void setBorderColor(Color borderColor);

    /**
     * Set the value of borderPadding
     *
     * @param borderPadding new value of borderPadding
     *
     * @throws IllegalArgumentException if border padding is less than zero
     */
    void setBorderPadding(double borderPadding);

    /**
     * Set the value of borderThickness
     *
     * @param borderThickness new value of borderThickness
     *
     * @throws IllegalArgumentException if border thickness is less than zero
     */
    void setBorderThickness(double borderThickness);

    /**
     * Set the value of font
     *
     * @param font new value of font
     */
    void setFont(Font font);

    /**
     * Set the value of fontColor
     *
     * @param fontColor new value of fontColor
     *
     * @throws NullPointerException if font color is null
     */
    void setFontColor(Color fontColor);

    /**
     * The sensitivity of the tooltip. This is the distance of mouse movement
     * required to hide the tooltip.
     *
     * @param sensitivity the sensitivity
     *
     * @throws IllegalArgumentException if sensitivity is less than zero
     */
    void setSensitivity(int sensitivity);

    /**
     * Show the tooltip in the scene.
     *
     * @param text the text for the tooltip
     * @param x    the x scene viewport coordinate
     * @param y    the y scene viewport coordinate
     */
    void showTooltip(String text, int x, int y);

    /**
     * Hide the tooltip in the scene.
     */
    void hideTooltip();

    /**
     * Check the distance that the mouse has moved and hide if greater than the
     * sensitivity allows.
     *
     * @param x the x location in screen coords
     * @param y the y location in screen coords
     */
    void moved(int x, int y);
}
