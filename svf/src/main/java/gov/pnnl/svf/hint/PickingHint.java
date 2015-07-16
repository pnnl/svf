package gov.pnnl.svf.hint;

import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;

/**
 * Hint used to determine picking style used in the scene. These picking hints
 * should be supported by the scene creator. Picking hints are listed in
 * priority order. If <code>NONE</code> is specified then all other picking
 * hints will be removed. If a single picking camera is specified the system
 * will attempt to create that picking camera. If that picking type is not
 * supported then it will move down the list and create a single camera. If more
 * than one hint is specified the system will attempt to create multiple picking
 * cameras.
 *
 * @author Arthur Bleeker
 */
public enum PickingHint {

    /**
     * No picking.
     */
    NONE(null),
    /**
     * Color picking.
     */
    COLOR_PICKING(ColorPickingCamera.class),
    /**
     * Standard ray cast picking of whole and subparts of actors.
     */
    ITEM_PICKING(ItemPickingCamera.class),
    /**
     * Standard ray cast picking of whole actors.
     */
    PICKING(PickingCamera.class);

    private final Class<? extends PickingCamera> cameraType;

    private PickingHint(final Class<? extends PickingCamera> cameraType) {
        this.cameraType = cameraType;
    }

    /**
     * The picking camera type for this hint.
     *
     * @return the camera type
     */
    public Class<? extends PickingCamera> getCameraType() {
        return cameraType;
    }
}
