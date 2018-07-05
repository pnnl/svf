package gov.pnnl.svf.picking;

import gov.pnnl.svf.event.PickingCameraEvent;

/**
 * Event listener for a picking camera.
 *
 * @author Amelia Bleeker
 */
public class PickingCameraAdapter implements PickingCameraListener {

    @Override
    public void picked(final PickingCameraEvent event) {
        // no operation
    }

    @Override
    public void nothingPicked(final PickingCameraEvent event) {
        // no operation
    }

    @Override
    public void prePicked(final PickingCameraEvent event) {
        // no operation
    }

    @Override
    public void postPicked(final PickingCameraEvent event) {
        // no operation
    }
}
