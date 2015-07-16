package gov.pnnl.svf.camera;

import gov.pnnl.svf.scene.Scene;

/**
 * A simple camera that allows movement within a scene. This class should be
 * overridden in order to implement user interaction with the camera.
 *
 * @author Arthur Bleeker
 */
public class DrivingCamera extends SimpleCamera {

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public DrivingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public DrivingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public DrivingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }
}
