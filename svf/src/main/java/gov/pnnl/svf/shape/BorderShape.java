package gov.pnnl.svf.shape;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;

/**
 * This shape represents a border, with optional padding, around a shape.
 *
 * @author Arthur Bleeker
 */
public interface BorderShape {

    /**
     * String representation of a field in this object.
     */
    String BORDER = "border";
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
     * @return the border for this shape
     */
    Border getBorder();

    /**
     * @return the color for the border
     */
    Color getBorderColor();

    /**
     * @return the relative border padding
     */
    double getBorderPadding();

    /**
     * @return the relative border thickness
     */
    double getBorderThickness();

    /**
     * @param border the border for this shape
     */
    void setBorder(Border border);

    /**
     * @param borderColor the color to draw the border
     */
    void setBorderColor(Color borderColor);

    /**
     * @param borderPadding the relative border padding
     */
    void setBorderPadding(double borderPadding);

    /**
     * @param borderThickness the relative border thickness
     */
    void setBorderThickness(double borderThickness);
}
