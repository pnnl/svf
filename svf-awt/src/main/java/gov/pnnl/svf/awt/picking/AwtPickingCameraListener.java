package gov.pnnl.svf.awt.picking;

import gov.pnnl.svf.awt.util.AwtCameraUtils;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.picking.AbstractPickingCamera;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.Timer;

/**
 * Listener used to listen for picking events.
 *
 * @author Arthur Bleeker
 */
class AwtPickingCameraListener implements MouseListener, MouseMotionListener, MouseWheelListener {

    private static final int AREA_SIZE = 4;
    private static final Set<CameraEventType> MOVE = Collections.unmodifiableSet(EnumSet.of(CameraEventType.MOVE));
    protected final AbstractPickingCamera camera;
    protected final TimerAction action;
    protected final Timer timer;
    private int x = 0;
    private int y = 0;

    /**
     * Constructor
     *
     * @param camera
     */
    AwtPickingCameraListener(final AbstractPickingCamera camera) {
        super();
        this.camera = camera;
        action = new TimerAction();
        timer = new Timer(400, action);
        timer.setCoalesce(false);
        timer.setRepeats(false);
    }

    /**
     * Specifies the initial delay value.
     *
     * @param milliseconds the number of milliseconds to delay (after the cursor
     *                     has paused) before firing a hover event
     *
     * @see #getInitialDelay
     */
    public void setInitialDelay(final int milliseconds) {
        timer.setInitialDelay(milliseconds);
    }

    /**
     * Returns the initial delay value.
     *
     * @return an integer representing the initial delay value, in milliseconds
     *
     * @see #setInitialDelay
     */
    public int getInitialDelay() {
        return timer.getInitialDelay();
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        // no operation
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        x = event.getX();
        y = event.getY();
        action.startHoverTimer(event);
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        action.stopHoverTimer();
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        final Rectangle viewport = camera.getViewport();
        final Rectangle sceneViewport = camera.getScene().getViewport();
        final double multiplier = camera.getCanvasMultiplier();
        if (!viewport.contains((int) (x * multiplier), sceneViewport.getHeight() - (int) (y * multiplier))) {
            return;
        }
        x = event.getX();
        y = event.getY();
        action.stopHoverTimer();
        // mouse down event
        final Set<CameraEventType> types = EnumSet.of(CameraEventType.DOWN);
        AwtCameraUtils.addButtonTypes(event, types);
        AwtCameraUtils.addModifierTypes(event, types);
        // process the pick
        camera.addEvent(new PickingCameraEvent(camera, event.getX(), event.getY(), event.getClickCount(), types));
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        AwtCameraUtils.addButtonTypes(event, types);
        AwtCameraUtils.addModifierTypes(event, types);
        // click count
        if ((Math.abs(x - event.getX()) > camera.getDragSensitivity()) || (Math.abs(y - event.getY()) > camera.getDragSensitivity())) {
            types.add(CameraEventType.DRAG);
        } else if (event.getClickCount() == 1) {
            types.add(CameraEventType.SINGLE);
        } else if (event.getClickCount() == 2) {
            types.add(CameraEventType.DOUBLE);
        }
        // process the area pick
        final int w = Math.abs(x - event.getX()) + 1;
        final int h = Math.abs(y - event.getY()) + 1;
        if (w > AREA_SIZE || h > AREA_SIZE) {
            final Set<CameraEventType> areaTypes = EnumSet.copyOf(types);
            areaTypes.add(CameraEventType.AREA);
            camera.addEvent(new PickingCameraEvent(camera, Math.min(x, event.getX()) + w / 2, Math.min(y, event.getY()) + h / 2, w, h, event.getClickCount(), areaTypes));
        }
        // process the pick
        camera.addEvent(new PickingCameraEvent(camera, event.getX(), event.getY(), event.getClickCount(), types));
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        mouseMoved(event);
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        // only consider move events that occur inside the camera space
        final Rectangle viewport = camera.getViewport();
        final Rectangle sceneViewport = camera.getScene().getViewport();
        final double multiplier = camera.getCanvasMultiplier();
        if (!viewport.contains((int) (x * multiplier), sceneViewport.getHeight() - (int) (y * multiplier))) {
            return;
        }
        // filter move events here to reduce garbage
        if (camera.getPickTypes().contains(CameraEventType.MOVE)) {
            // create mouse moved currentEvent
            // we don't add button or modifier types to a move event
            final PickingCameraEvent pickEvent = new PickingCameraEvent(camera, event.getX(), event.getY(), event.getClickCount(), MOVE);
            camera.addEvent(pickEvent);
        }
        action.startHoverTimer(event);
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent event) {
        // down button
        final Rectangle viewport = camera.getViewport();
        final Rectangle sceneViewport = camera.getScene().getViewport();
        final double multiplier = camera.getCanvasMultiplier();
        if (!viewport.contains((int) (x * multiplier), sceneViewport.getHeight() - (int) (y * multiplier))) {
            return;
        }
        x = event.getX();
        y = event.getY();
        action.stopHoverTimer();
        // up button
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        AwtCameraUtils.addButtonTypes(event, types);
        AwtCameraUtils.addModifierTypes(event, types);
        // process the pick
        camera.addEvent(new PickingCameraEvent(camera, event.getX(), event.getY(), Math.abs(event.getWheelRotation()), types));
    }

    private class TimerAction implements ActionListener {

        private MouseEvent event;

        private void startHoverTimer(final MouseEvent event) {
            this.event = event;
            timer.restart();
            //            System.out.println("started");
        }

        private void stopHoverTimer() {
            event = null;
            timer.stop();
            //            System.out.println("stopped");
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (this.event != null) {
                final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
                types.add(CameraEventType.HOVER);
                AwtCameraUtils.addModifierTypes(this.event, types);
                camera.addEvent(new PickingCameraEvent(camera, this.event.getX(), this.event.getY(), 0, types));
                //                System.out.println("hovered");
            }
        }
    }
}
