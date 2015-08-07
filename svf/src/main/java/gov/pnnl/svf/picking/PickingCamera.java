package gov.pnnl.svf.picking;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.CameraExt;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import java.util.Set;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;

/**
 * Interface that represents a picking camera in the scene. Picking only works
 * for scene items. Picking is only supported on cameras that view the entire
 * scene and fill the viewport.
 *
 * @author Arthur Bleeker
 */
public interface PickingCamera extends CameraExt<PickingCameraListener> {

    /**
     * String representation of a field in this object.
     */
    String PROCESS = "process";
    /**
     * String representation of a field in this object.
     */
    String SENSITIVITY = "sensitivity";
    /**
     * String representation of a field in this object.
     */
    String DRAG_SENSITIVITY = "dragSensitivity";
    /**
     * String representation of a field in this object.
     */
    String PICK_TYPES = "pickTypes";

    /**
     * Add an event to be processed by this picking camera.
     *
     * @param event the event to add to the picking queue
     */
    void addEvent(PickingCameraEvent event);

    /**
     * Check if the actor has been picked. This method will notify individual
     * actor's if they have been picked.
     *
     * @param gl      Reference to GL.
     * @param glu     Reference to GLU.
     * @param support The actor support to check for a pick.
     */
    void checkPickingHit(GL2 gl, GLUgl2 glu, PickingSupport support);

    /**
     * The number of screen pixels the mouse can move before it's considered a
     * drag event.
     *
     * @return the sensitivity
     */
    int getDragSensitivity();

    /**
     * Sensitivity is essentially a margin for the bounds of the pick space. A
     * value of 3 will add 3 pixels around the original picking space.
     *
     * @return the sensitivity
     */
    double getSensitivity();

    /**
     * @return the set of event types to perform picking on or null for all
     *         types
     */
    Set<CameraEventType> getPickTypes();

    /**
     * The number of screen pixels the mouse can move before it's considered a
     * drag event.
     *
     * @param dragSensitivity the dragSensitivity to set
     *
     * @return this instance
     */
    PickingCamera setDragSensitivity(int dragSensitivity);

    /**
     * Sensitivity is essentially a margin for the bounds of the pick space. A
     * value of 3 will add 3 pixels around the original picking space.
     *
     * @param sensitivity the sensitivity to set
     *
     * @return this instance
     *
     * @throws IllegalArgumentException if sensitivity is less than zero
     */
    PickingCamera setSensitivity(double sensitivity);

    /**
     * The set of pick types must include all modifiers that you want to recieve
     * events for. E.g. You want to get events for a single left mouse button
     * click regardless of any buttons that may be held down. You should include
     * the types: left, single, shift, alt, and ctrl.
     *
     * @param types the set of event types to perform picking on or null for all
     *              types
     *
     * @return this instance
     */
    PickingCamera setPickTypes(Set<CameraEventType> types);

    /**
     * This method returns the current set of events and clears the current
     * event list.
     *
     * @return the current set of events
     */
    Set<PickingCameraEvent> getEvents();

    /**
     * Start picking in a scene. This is utilized by the scene to perform
     * picking.
     *
     * @param event
     */
    void start(PickingCameraEvent event);

    /**
     * End picking in a scene. This is utilized by the scene to perform picking.
     *
     * @param event
     */
    void end(PickingCameraEvent event);

    /**
     * The visual reference camera that this picking camera is attached to.
     *
     * @return the visual reference camera that this picking camera uses
     */
    Camera getReferenceCamera();
}
