package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;

/**
 * @author Alex Gibson
 */
public interface HighlightSupportListener {

    /**
     * Called when the highlight of this actor changes.
     *
     * @param actor The actor that is changing highlight state.
     */
    void hightlightChanged(Actor actor);
}
