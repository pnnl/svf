package gov.pnnl.svf.scene;

import gov.pnnl.svf.picking.ColorPickable;
import gov.pnnl.svf.picking.ItemPickable;
import gov.pnnl.svf.picking.Pickable;

/**
 * Interface used to mark an object as a drawable support item.
 *
 * @author Amelia Bleeker
 */
public interface DrawableSupport extends Drawable, Pickable, ItemPickable, ColorPickable {

}
