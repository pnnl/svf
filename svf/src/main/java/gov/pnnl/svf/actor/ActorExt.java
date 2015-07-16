package gov.pnnl.svf.actor;

import gov.pnnl.svf.picking.ColorPickableActor;
import gov.pnnl.svf.picking.ItemPickableActor;
import gov.pnnl.svf.picking.PickableActor;

/**
 * Interface that represents an object that can be drawn in a scene.
 *
 * @author Arthur Bleeker
 *
 */
public interface ActorExt extends Actor, PickableActor, ItemPickableActor, ColorPickableActor {
}
