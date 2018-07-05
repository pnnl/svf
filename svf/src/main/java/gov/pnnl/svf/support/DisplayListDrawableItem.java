package gov.pnnl.svf.support;

import gov.pnnl.svf.picking.ImmediateColorPickable;
import gov.pnnl.svf.picking.ImmediatePickable;
import gov.pnnl.svf.scene.ImmediateDrawable;
import gov.pnnl.svf.vbo.PreparableItem;

/**
 * Interface used to initialize and create the display lists required for an
 * item.
 *
 * @author Amelia Bleeker
 */
public interface DisplayListDrawableItem extends PreparableItem, ImmediateDrawable, ImmediatePickable, ImmediateColorPickable {
}
