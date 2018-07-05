package gov.pnnl.svf.support;

import gov.pnnl.svf.picking.ImmediateColorPickable;
import gov.pnnl.svf.picking.ImmediatePickable;
import gov.pnnl.svf.scene.ImmediateDrawable;
import gov.pnnl.svf.vbo.PreparableItem;

/**
 * Interface used to draw an item using immediate mode.
 *
 * @author Amelia Bleeker
 */
public interface ImmediateDrawableItem extends PreparableItem, ImmediateDrawable, ImmediatePickable, ImmediateColorPickable {
}
