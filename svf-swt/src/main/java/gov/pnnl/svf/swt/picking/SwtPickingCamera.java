package gov.pnnl.svf.swt.picking;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.picking.AbstractPickingCamera;
import gov.pnnl.svf.scene.Scene;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;

/**
 * This camera builds a picking view for a scene. Actor's that have picking
 * support will be notified when clicked or double clicked in a camera view that
 * is attached to this picking camera. Picking is only supported on cameras that
 * view the entire scene and fill the viewport.
 *
 * @author Arthur Bleeker
 */
public class SwtPickingCamera extends AbstractPickingCamera implements MouseListener, MouseMoveListener, MouseTrackListener, MouseWheelListener, KeyListener {

    private final SwtPickingCameraListener listener = new SwtPickingCameraListener(this);

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param camera The view into the scene that will be used for picking.
     */
    public SwtPickingCamera(final Scene scene, final Camera camera) {
        super(scene, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    public SwtPickingCamera(final Scene scene, final String id, final Camera camera) {
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
    public SwtPickingCamera(final Scene scene, final String type, final String id, final Camera camera) {
        super(scene, type, id, camera);
    }

    @Override
    public void keyPressed(final KeyEvent evt) {
        listener.keyPressed(evt);
    }

    @Override
    public void keyReleased(final KeyEvent evt) {
        listener.keyReleased(evt);
    }

    @Override
    public void mouseDoubleClick(final MouseEvent evt) {
        listener.mouseDoubleClick(evt);
    }

    @Override
    public void mouseDown(final MouseEvent evt) {
        listener.mouseDown(evt);
    }

    @Override
    public void mouseEnter(final MouseEvent evt) {
        listener.mouseEnter(evt);
    }

    @Override
    public void mouseExit(final MouseEvent evt) {
        listener.mouseExit(evt);
    }

    @Override
    public void mouseHover(final MouseEvent evt) {
        listener.mouseHover(evt);
    }

    @Override
    public void mouseMove(final MouseEvent evt) {
        listener.mouseMove(evt);
    }

    @Override
    public void mouseUp(final MouseEvent evt) {
        listener.mouseUp(evt);
    }

    @Override
    public void mouseScrolled(final MouseEvent evt) {
        listener.mouseScrolled(evt);
    }
}
