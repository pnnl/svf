package gov.pnnl.svf.scene;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.picking.PickingCamera;

/**
 * Interface for a factory that can create implementation specific scene items.
 *
 * @author Amelia Bleeker
 */
public interface SceneFactory {

    /**
     * Generate a new UUID for this scene.
     *
     * @param scene reference to the scene
     *
     * @return a new UUID
     */
    String newUuid(Scene scene);

    /**
     * Create a new tooltip.
     *
     * @param scene reference to the scene
     *
     * @return a tool tip for this scene type
     */
    TooltipActor createTooltip(Scene scene);

    /**
     * Create a new service type.
     *
     * @param <T>   the service type
     * @param scene reference to the scene
     * @param type  the type of service to create
     *
     * @return a new service
     *
     * @throws IllegalArgumentException if the type is not valid
     */
    <T> T createService(Scene scene, Class<T> type);

    /**
     * Create a new camera type.
     *
     * @param <C>   the camera type
     *
     * @param scene reference to the scene
     * @param type  the type of camera to create
     *
     * @return a new camera
     *
     * @throws IllegalArgumentException if the type is not valid
     */
    <C extends Camera> C createCamera(Scene scene, Class<C> type);

    /**
     * Create a new picking camera type.
     *
     * @param <P>    the picking camera type
     *
     * @param scene  reference to the scene
     * @param camera the reference viewing camera
     * @param type   the type of picking camera to create
     *
     * @return a new picking camera
     *
     * @throws IllegalArgumentException if the type is not valid
     */
    <P extends PickingCamera> P createPickingCamera(Scene scene, Camera camera, Class<P> type);

    /**
     * Run this on the proper UI thread. If this is called the UI thread then it
     * will be run immediately. Otherwise it is run asynchronously.
     *
     * @param scene    reference to the scene
     * @param runnable the runnable
     */
    void runOnUiThread(Scene scene, Runnable runnable);

    /**
     * Run this on the proper UI thread. If synchronous this call should not
     * return until run. Otherwise this task may, or may not, run immediately if
     * called on the UI thread.
     *
     * @param scene       reference to the scene
     * @param runnable    the runnable
     * @param synchronous true for synchronous execution
     *
     * @return true if run synchronously
     */
    boolean runOnUiThread(Scene scene, Runnable runnable, boolean synchronous);

    /**
     * Run this off of the UI thread. If this is called off the UI thread then
     * it will be run immediately. Otherwise it is run asynchronously.
     *
     * @param scene    reference to the scene
     * @param runnable the runnable
     */
    void runOffUiThread(Scene scene, Runnable runnable);

    /**
     * Run this off of the UI thread. If synchronous this call should not return
     * until run. Otherwise this task may, or may not, run immediately if called
     * off of the UI thread.
     *
     * @param scene       reference to the scene
     * @param runnable    the runnable
     * @param synchronous true for synchronous execution
     *
     * @return true if run synchronously
     */
    boolean runOffUiThread(Scene scene, Runnable runnable, boolean synchronous);
}
