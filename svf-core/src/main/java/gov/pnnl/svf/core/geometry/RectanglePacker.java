package gov.pnnl.svf.core.geometry;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Interface for a rectangle packer implementation.
 *
 * @author Arthur Bleeker
 */
public interface RectanglePacker {

    /**
     * Allocates space for a rectangle in the packing area.
     *
     * @param width  the width of the rectangle to allocate
     * @param height the height of the rectangle to allocate
     *
     * @return the location at which the rectangle has been placed or null if
     *         the rectangle does not fit in the packing area
     */
    Point pack(int width, int height);

    /**
     * The maximum area that can be used by this rectangle packer.
     *
     * @return the maximum area
     */
    Rectangle getMaximumArea();

    /**
     * The actual area that is currently used by this rectangle packer.
     *
     * @return the actual area
     */
    Rectangle getActualArea();

    /**
     * The area that is available for use by this rectangle packer.
     *
     * @return the available area
     */
    Rectangle getAvailableArea();
}
