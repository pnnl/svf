package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;

/**
 * @author Arthur Bleeker
 */
public interface SelectionSupportListener {

    /**
     * Called when the selection of this actor changes.
     *
     * @param actor The actor that is changing selection state.
     */
    void selectionChanged(Actor actor);
}
