package gov.pnnl.svf.newt.picking;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.picking.AbstractColorPickingCamera;
import gov.pnnl.svf.scene.Scene;

/**
 * This camera builds a picking view for a scene. Actor's that have picking
 * support will be notified when clicked or double clicked in a camera view that
 * is attached to this picking camera. Picking is only supported on cameras that
 * view the entire scene and fill the viewport.
 *
 * @author Arthur Bleeker
 */
public class NewtColorPickingCamera extends AbstractColorPickingCamera implements MouseListener {

    private final NewtPickingCameraListener listener = new NewtPickingCameraListener(this);

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param camera The view into the scene that will be used for picking.
     */
    public NewtColorPickingCamera(final Scene scene, final Camera camera) {
        super(scene, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param id
     * @param camera The view into the scene that will be used for picking.
     */
    public NewtColorPickingCamera(final Scene scene, final String id, final Camera camera) {
        super(scene, id, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param type
     * @param id
     * @param camera The view into the scene that will be used for picking.
     */
    public NewtColorPickingCamera(final Scene scene, final String type, final String id, final Camera camera) {
        super(scene, type, id, camera);
    }

    @Override
    public void mouseClicked(final MouseEvent evt) {
        listener.mouseClicked(evt);
    }

    @Override
    public void mouseDragged(final MouseEvent evt) {
        listener.mouseDragged(evt);
    }

    @Override
    public void mouseEntered(final MouseEvent evt) {
        listener.mouseEntered(evt);
    }

    @Override
    public void mouseExited(final MouseEvent evt) {
        listener.mouseExited(evt);
    }

    @Override
    public void mouseMoved(final MouseEvent evt) {
        listener.mouseMoved(evt);
    }

    @Override
    public void mousePressed(final MouseEvent evt) {
        listener.mousePressed(evt);
    }

    @Override
    public void mouseReleased(final MouseEvent evt) {
        listener.mouseReleased(evt);
    }

    @Override
    public void mouseWheelMoved(final MouseEvent evt) {
        listener.mouseWheelMoved(evt);
    }
}
