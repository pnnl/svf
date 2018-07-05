package gov.pnnl.svf.hint;

import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.camera.DrawingCamera;
import gov.pnnl.svf.camera.DrivingCamera;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.camera.SimpleCamera;

/**
 * Hint used to determine camera style used in the scene. Only one camera should
 * be specified, otherwise the lowest ordinal hint will be chosen. These camera
 * hints should be supported by the scene creator. Camera hints are listed in
 * priority order. If more than one hint is specified the system will utilize
 * the highest priority.
 *
 * @author Amelia Bleeker
 */
public enum CameraHint {

    /**
     * No camera
     */
    NONE(null),
    /**
     * Dragging camera
     */
    DRAGGING(DraggingCamera.class),
    /**
     * Orbit camera
     */
    ORBIT(OrbitCamera.class),
    /**
     * Simple camera
     */
    SIMPLE(SimpleCamera.class),
    /**
     * Driving camera
     */
    DRIVING(DrivingCamera.class);

    private final Class<? extends DrawingCamera> cameraType;

    private CameraHint(final Class<? extends DrawingCamera> cameraType) {
        this.cameraType = cameraType;
    }

    /**
     * The drawing camera type for this hint.
     *
     * @return the camera type
     */
    public Class<? extends DrawingCamera> getCameraType() {
        return cameraType;
    }
}
