package gov.pnnl.svf.picking;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.ProxyMouseEvent;
import gov.pnnl.svf.scene.Scene;

/**
 * This camera builds a picking view for a scene. Actor's that have picking
 * support will be notified when clicked or double clicked in a camera view that
 * is attached to this picking camera. Picking is only supported on cameras that
 * view the entire scene and fill the viewport.
 *
 * @author Amelia Bleeker
 */
public class ProxyItemPickingCamera extends AbstractItemPickingCamera {

    private final ProxyPickingCameraListener listener = new ProxyPickingCameraListener(this);

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param camera The view into the scene that will be used for picking.
     */
    public ProxyItemPickingCamera(final Scene scene, final Camera camera) {
        super(scene, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    public ProxyItemPickingCamera(final Scene scene, final String id, final Camera camera) {
        super(scene, id, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param type   The type of this actor.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    public ProxyItemPickingCamera(final Scene scene, final String type, final String id, final Camera camera) {
        super(scene, type, id, camera);
    }

    public void mousePressed(final ProxyMouseEvent event) {
        listener.mousePressed(event);
    }

    public void mouseReleased(final ProxyMouseEvent event) {
        listener.mouseReleased(event);
    }

    public void mouseEntered(final ProxyMouseEvent event) {
        listener.mouseEntered(event);
    }

    public void mouseExited(final ProxyMouseEvent event) {
        listener.mouseExited(event);
    }

    public void mouseDragged(final ProxyMouseEvent event) {
        listener.mouseDragged(event);
    }

    public void mouseMoved(final ProxyMouseEvent event) {
        listener.mouseMoved(event);
    }

    public void mouseWheelMoved(final ProxyMouseEvent event) {
        listener.mouseWheelMoved(event);
    }
}
