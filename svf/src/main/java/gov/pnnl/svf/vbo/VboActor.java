package gov.pnnl.svf.vbo;

import gov.pnnl.svf.actor.Actor;

/**
 * Interface that represents an actor that can be drawn in a scene using VBOs.
 * VboSupport must be present to allow the creation and use of VBOs.
 *
 * @author Arthur Bleeker
 *
 */
public interface VboActor extends Actor, VboPickable, VboItemPickable, VboColorPickable {
}
