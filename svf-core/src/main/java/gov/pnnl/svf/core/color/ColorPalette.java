package gov.pnnl.svf.core.color;

import java.util.Collection;

/**
 * Color palette is a container and repository for a set of colors.
 *
 * @author Amelia Bleeker
 */
public class ColorPalette extends AbstractColorPalette<Color> {

    /**
     * Constructor
     *
     * @param colors a non empty series of colors
     */
    public ColorPalette(final Color... colors) {
        super(colors);
    }

    /**
     * Constructor
     *
     * @param colors a non empty collection of colors
     */
    public ColorPalette(final Collection<? extends Color> colors) {
        super(colors);
    }
}
