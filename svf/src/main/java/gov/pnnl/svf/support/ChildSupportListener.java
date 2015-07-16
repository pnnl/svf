package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;

/**
 * Listener for listening to events from a child support object.
 *
 * @author Arthur Bleeker
 */
public interface ChildSupportListener {

    /**
     * Called when the actor that owns ChildSupport has a child added to it.
     *
     * @param child The child actor that was added.
     */
    void childAdded(Actor child);

    /**
     * Called when the actor that owns ChildSupport has a child removed from it.
     *
     * @param child The child actor that was removed.
     */
    void childRemoved(Actor child);
}
