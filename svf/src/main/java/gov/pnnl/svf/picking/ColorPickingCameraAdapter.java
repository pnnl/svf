package gov.pnnl.svf.picking;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.event.PickingCameraEvent;

/**
 * Event listener for a picking camera.
 *
 * @author Amelia Bleeker
 */
public class ColorPickingCameraAdapter extends PickingCameraAdapter implements ColorPickingCameraListener {

    @Override
    public void itemEntered(final Actor actor, final Object item, final PickingCameraEvent event) {
        // no operation
    }

    @Override
    public void itemExited(final Actor actor, final Object item, final PickingCameraEvent event) {
        // no operation
    }

    @Override
    public void mouseMoved(final PickingCameraEvent event) {
        // no operation
    }
}
