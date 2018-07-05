package gov.pnnl.svf.service;

import gov.pnnl.svf.scene.Drawable;

/**
 * Interface for services that require drawing. This interface should be used to
 * mark an item for drawing in the scene lookup. Services will run before any
 * drawing or picking occurs in the scene.
 *
 * @author Amelia Bleeker
 */
public interface DrawableService extends Drawable, Comparable<DrawableService> {

    /**
     * The priority of the drawable service. Higher priorities will be drawn
     * first.
     *
     * @return the priority of this service.
     */
    int getPriority();
}
