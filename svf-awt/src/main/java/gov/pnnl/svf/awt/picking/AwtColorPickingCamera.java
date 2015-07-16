package gov.pnnl.svf.awt.picking;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.picking.AbstractColorPickingCamera;
import gov.pnnl.svf.scene.Scene;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * This referenceCamera builds a picking view for a scene. Actor's that have
 * picking support will be notified when clicked or double clicked in a
 * referenceCamera view that is attached to this picking referenceCamera.
 * Picking is only supported on cameras that view the entire scene and fill the
 * viewport.
 *
 * @author Arthur Bleeker
 */
public class AwtColorPickingCamera extends AbstractColorPickingCamera implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final AwtPickingCameraListener listener = new AwtPickingCameraListener(this);

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param camera The view into the scene that will be used for picking.
     */
    public AwtColorPickingCamera(final Scene scene, final Camera camera) {
        super(scene, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    public AwtColorPickingCamera(final Scene scene, final String id, final Camera camera) {
        super(scene, id, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param type   The type of this actor.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    public AwtColorPickingCamera(final Scene scene, final String type, final String id, final Camera camera) {
        super(scene, type, id, camera);
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        listener.mouseClicked(event);
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        listener.mousePressed(event);
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        listener.mouseReleased(event);
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        listener.mouseEntered(event);
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        listener.mouseExited(event);
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        listener.mouseDragged(event);
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        listener.mouseMoved(event);
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent event) {
        listener.mouseWheelMoved(event);
    }
}
