package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.geometry.TextAlign;
import java.awt.Font;
import java.util.List;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * Represents text.
 *
 * @author Amelia Bleeker
 */
public interface Text {

    /**
     * The font for the text.
     *
     * @return the text font
     */
    Font getFont();

    /**
     * The text align for multiple lines of text.
     *
     * @return the text align
     */
    TextAlign getAlign();

    /**
     * The character offsets.
     *
     * @return the character offsets
     */
    DoubleList getOffsets();

    /**
     * The line offsets.
     *
     * @return the line offsets
     */
    DoubleList getLineOffsets();

    /**
     * The height of an individual line.
     *
     * @return the line height
     */
    double getLineHeight();

    /**
     * @return the text
     */
    String getText();

    /**
     * @return the text for each line
     */
    List<String> getTextLines();

}
