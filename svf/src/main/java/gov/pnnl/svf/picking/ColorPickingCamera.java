package gov.pnnl.svf.picking;

import gov.pnnl.svf.core.collections.CountingSet;
import gov.pnnl.svf.core.collections.KeyValuePair;

/**
 * Interface that represents a color picking camera in the scene. Color picking
 * works for both scene and user interface items.
 *
 * @author Amelia Bleeker
 */
public interface ColorPickingCamera extends PickingCamera {

    /**
     * Check for a picking hit using the specified screen coordinates. Only one
     * item per pixel can be returned regardless of how many items are occupying
     * the same space.
     *
     * @param x the x coordinate of the center of the pick in screen pixels
     * @param y the y coordinate of the center of the pick in screen pixels
     *
     * @return the color picking support object and item for the actor picked or
     *         null
     */
    KeyValuePair<ColorPickingSupport, Object> checkPickingHit(int x, int y);

    /**
     * Check for a picking hit using the specified screen coordinates. Only one
     * item per pixel can be returned regardless of how many items are occupying
     * the same space.
     *
     * @param x      the x coordinate of the center of the pick in screen pixels
     * @param y      the y coordinate of the center of the pick in screen pixels
     * @param width  the width of the pick in pixels
     * @param height the height of the pick in pixels
     *
     * @return the set of color picking support objects and items for the actors
     *         picked or null
     */
    CountingSet<KeyValuePair<ColorPickingSupport, Object>> checkPickingHit(int x, int y, int width, int height);
}
