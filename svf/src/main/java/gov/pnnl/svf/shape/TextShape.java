package gov.pnnl.svf.shape;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import java.awt.Font;

/**
 * Shape used to represent text.
 *
 * @author Arthur Bleeker
 */
public interface TextShape {

    /**
     * String representation of a field in this object.
     */
    String ALIGNMENT = "alignment";
    /**
     * String representation of a field in this object.
     */
    String TEXT = "text";
    /**
     * String representation of a field in this object.
     */
    String BACKGROUND_COLOR = "backgroundColor";
    /**
     * String representation of a field in this object.
     */
    String FONT = "font";

    /**
     *
     * @return the alignment of the text
     */
    Alignment getAlignment();

    /**
     * @return the text of the label
     */
    String getText();

    /**
     * @return the background color
     */
    Color getBackgroundColor();

    /**
     * The font can be set to null to utilize the texture and region support
     * objects from the text actors lookup.
     *
     * @return the font
     */
    Font getFont();

    /**
     *
     * @param alignment the alignment of the text
     *
     * @throws NullPointerException if the alignment is null
     */
    void setAlignment(Alignment alignment);

    /**
     * @param text the text of the label to set
     *
     * @throws NullPointerException if the text argument is null
     */
    void setText(String text);

    /**
     * @param backgroundColor the background color
     *
     * @throws NullPointerException if the background color argument is null
     */
    void setBackgroundColor(Color backgroundColor);

    /**
     * The font can be set to null to utilize the texture and region support
     * objects from the text actors lookup.
     *
     * @param font the font
     */
    void setFont(Font font);
}
