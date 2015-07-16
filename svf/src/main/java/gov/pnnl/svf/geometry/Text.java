package gov.pnnl.svf.geometry;

import java.awt.Font;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * Represents text.
 *
 * @author Arthur Bleeker
 */
public interface Text {

    /**
     * The font for the text.
     *
     * @return the text font
     */
    Font getFont();

    /**
     * The character offsets.
     *
     * @return the character offsets
     */
    DoubleList getOffsets();

    /**
     * @return the text
     */
    String getText();

}
