package gov.pnnl.svf.swt.picking;

import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.picking.AbstractPickingCamera;
import gov.pnnl.svf.swt.util.SwtCameraUtils;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;

/**
 * This camera builds a picking view for a scene. Actor's that have picking
 * support will be notified when clicked or double clicked in a camera view that
 * is attached to this picking camera.
 *
 * @author Arthur Bleeker
 *
 */
class SwtPickingCameraListener implements MouseListener, MouseMoveListener, MouseTrackListener, MouseWheelListener {

    private static final int AREA_SIZE = 4;
    private static final Set<CameraEventType> MOVE = Collections.unmodifiableSet(EnumSet.of(CameraEventType.MOVE));
    private final AbstractPickingCamera camera;
    private int x = 0;
    private int y = 0;
    private boolean dragging = false;

    /**
     * Constructor
     *
     * @param camera
     */
    SwtPickingCameraListener(final AbstractPickingCamera camera) {
        super();
        this.camera = camera;
    }

    @Override
    public void mouseDoubleClick(final MouseEvent evt) {
        // no operation
    }

    @Override
    public void mouseDown(final MouseEvent evt) {
        x = evt.x;
        y = evt.y;
        dragging = true;
        // mouse down event
        final Set<CameraEventType> types = EnumSet.of(CameraEventType.DOWN);
        SwtCameraUtils.addButtonTypes(evt, types);
        SwtCameraUtils.addModifierTypes(evt, types);
        camera.addEvent(new PickingCameraEvent(camera, evt.x, evt.y, evt.count, types));
    }

    @Override
    public void mouseEnter(final MouseEvent evt) {
        x = evt.x;
        y = evt.y;
        if (evt.button > 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseExit(final MouseEvent evt) {
        // no operation
        dragging = false;
    }

    @Override
    public void mouseHover(final MouseEvent evt) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        types.add(CameraEventType.HOVER);
        SwtCameraUtils.addModifierTypes(evt, types);
        camera.addEvent(new PickingCameraEvent(camera, evt.x, evt.y, evt.count, types));
    }

    @Override
    public void mouseMove(final MouseEvent evt) {
        // we don't want to listen to drag events
        if (dragging) {
            return;
        }
        // filter move events here to reduce garbage
        if (camera.getPickTypes().contains(CameraEventType.MOVE)) {
            // create mouse moved currentEvent
            // we don't add button or modifier types to a move event
            final PickingCameraEvent pickEvent = new PickingCameraEvent(camera, evt.x, evt.y, evt.count, MOVE);
            camera.addEvent(pickEvent);
        }
    }

    @Override
    public void mouseUp(final MouseEvent evt) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        SwtCameraUtils.addButtonTypes(evt, types);
        SwtCameraUtils.addModifierTypes(evt, types);
        if ((Math.abs(x - evt.x) > camera.getDragSensitivity()) || (Math.abs(y - evt.y) > camera.getDragSensitivity())) {
            types.add(CameraEventType.DRAG);
        } else if (evt.count == 1) {
            types.add(CameraEventType.SINGLE);
        } else if (evt.count == 2) {
            types.add(CameraEventType.DOUBLE);
        }
        // process the area pick
        final int w = Math.abs(x - evt.x) + 1;
        final int h = Math.abs(y - evt.y) + 1;
        if (w > AREA_SIZE || h > AREA_SIZE) {
            final Set<CameraEventType> areaTypes = EnumSet.copyOf(types);
            areaTypes.add(CameraEventType.AREA);
            camera.addEvent(new PickingCameraEvent(camera, Math.min(x, evt.x) + w / 2, Math.min(y, evt.y) + h / 2, w, h, evt.count, areaTypes));
        }
        camera.addEvent(new PickingCameraEvent(camera, evt.x, evt.y, evt.count, types));
        dragging = false;
    }

    @Override
    public void mouseScrolled(final MouseEvent event) {
        // down button
        final Rectangle viewport = camera.getViewport();
        final Rectangle sceneViewport = camera.getScene().getViewport();
        if (!viewport.contains(x, sceneViewport.getHeight() - y)) {
            return;
        }
        x = event.x;
        y = event.y;
        // up button
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        SwtCameraUtils.addButtonTypes(event, types);
        SwtCameraUtils.addModifierTypes(event, types);
        // process the pick
        camera.addEvent(new PickingCameraEvent(camera, event.x, event.y, Math.abs(event.count), types));
        dragging = false;
    }
}
