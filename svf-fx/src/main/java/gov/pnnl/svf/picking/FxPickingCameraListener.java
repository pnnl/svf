package gov.pnnl.svf.picking;

import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.util.FxCameraUtils;
import gov.pnnl.svf.util.FxEventHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javax.swing.Timer;

/**
 * Listener used to listen for picking events.
 *
 * @author Arthur Bleeker
 */
class FxPickingCameraListener implements FxEventHandler<InputEvent> {

    private static final Set<EventType<InputEvent>> EVENT_TYPES = Collections.unmodifiableSet(Collections.singleton(InputEvent.ANY));
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
    FxPickingCameraListener(final AbstractPickingCamera camera) {
        super();
        this.camera = camera;
        action = new TimerAction();
        timer = new Timer(400, action);
        timer.setCoalesce(false);
        timer.setRepeats(false);
    }

    @Override
    public Set<EventType<InputEvent>> getEventTypes() {
        return EVENT_TYPES;
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
    public void handle(final InputEvent event) {
        // note: instanceof not necessary but included for static code analysis
        if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_ENTERED)) {
            // mouse entered
            x = (int) ((MouseEvent) event).getX();
            y = (int) ((MouseEvent) event).getY();
            action.startHoverTimer((MouseEvent) event);
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_EXITED)) {
            // mouse exited
            action.stopHoverTimer();
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_PRESSED)) {
            // mouse pressed
            final Rectangle viewport = camera.getViewport();
            final Rectangle sceneViewport = camera.getScene().getViewport();
            if (!viewport.contains(x, sceneViewport.getHeight() - y)) {
                return;
            }
            x = (int) ((MouseEvent) event).getX();
            y = (int) ((MouseEvent) event).getY();
            action.stopHoverTimer();
            // mouse down event
            final Set<CameraEventType> types = EnumSet.of(CameraEventType.DOWN);
            FxCameraUtils.addButtonTypes((MouseEvent) event, types);
            FxCameraUtils.addModifierTypes((MouseEvent) event, types);
            // process the pick
            camera.addEvent(new PickingCameraEvent(camera, (int) ((MouseEvent) event).getX(), (int) ((MouseEvent) event).getY(), ((MouseEvent) event).getClickCount(), types));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_RELEASED)) {
            // mouse released
            final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
            FxCameraUtils.addButtonTypes((MouseEvent) event, types);
            FxCameraUtils.addModifierTypes((MouseEvent) event, types);
            // click count
            if ((Math.abs(x - (int) ((MouseEvent) event).getX()) > camera.getDragSensitivity())
                || (Math.abs(y - (int) ((MouseEvent) event).getY()) > camera.getDragSensitivity())) {
                types.add(CameraEventType.DRAG);
            } else if (((MouseEvent) event).getClickCount() == 1) {
                types.add(CameraEventType.SINGLE);
            } else if (((MouseEvent) event).getClickCount() == 2) {
                types.add(CameraEventType.DOUBLE);
            }
            // process the area pick
            final int w = Math.abs(x - (int) ((MouseEvent) event).getX()) + 1;
            final int h = Math.abs(y - (int) ((MouseEvent) event).getY()) + 1;
            if (w > AREA_SIZE || h > AREA_SIZE) {
                final Set<CameraEventType> areaTypes = EnumSet.copyOf(types);
                areaTypes.add(CameraEventType.AREA);
                camera.addEvent(new PickingCameraEvent(camera, Math.min(x, (int) ((MouseEvent) event).getX()) + w / 2, Math.min(y,
                                                                                                                                (int) ((MouseEvent) event).getY())
                                                                                                                       + h / 2, w, h, ((MouseEvent) event).getClickCount(), areaTypes));
            }
            // process the pick
            camera.addEvent(new PickingCameraEvent(camera, (int) ((MouseEvent) event).getX(), (int) ((MouseEvent) event).getY(), ((MouseEvent) event).getClickCount(), types));
        } else if (event instanceof ScrollEvent && Objects.equals(event.getEventType(), ScrollEvent.SCROLL)) {
            // mouse scrolled
            // down button
            final Rectangle viewport = camera.getViewport();
            final Rectangle sceneViewport = camera.getScene().getViewport();
            if (!viewport.contains(x, sceneViewport.getHeight() - y)) {
                return;
            }
            x = (int) ((ScrollEvent) event).getX();
            y = (int) ((ScrollEvent) event).getY();
            action.stopHoverTimer();
            // up button
            final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
            FxCameraUtils.addButtonTypes((ScrollEvent) event, types);
            FxCameraUtils.addModifierTypes((ScrollEvent) event, types);
            // process the pick
            camera.addEvent(new PickingCameraEvent(camera, (int) ((ScrollEvent) event).getX(), (int) ((ScrollEvent) event).getY(), (int) Math.abs(((ScrollEvent) event).getDeltaY()), types));
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_DRAGGED)) {
            // mouse dragged
            action.startHoverTimer((MouseEvent) event);
        } else if (event instanceof MouseEvent && Objects.equals(event.getEventType(), MouseEvent.MOUSE_MOVED)) {
            // mouse moved
            // only consider move events that occur inside the camera space
            final Rectangle viewport = camera.getViewport();
            final Rectangle sceneViewport = camera.getScene().getViewport();
            if (!viewport.contains(x, sceneViewport.getHeight() - y)) {
                return;
            }
            // filter move events here to reduce garbage
            if (camera.getPickTypes().contains(CameraEventType.MOVE)) {
                // create mouse moved currentEvent
                // we don't add button or modifier types to a move event
                final PickingCameraEvent pickEvent = new PickingCameraEvent(camera, (int) ((MouseEvent) event).getX(), (int) ((MouseEvent) event).getY(), ((MouseEvent) event).getClickCount(), MOVE);
                camera.addEvent(pickEvent);
            }
            action.startHoverTimer((MouseEvent) event);
        }
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
                FxCameraUtils.addModifierTypes(this.event, types);
                camera.addEvent(new PickingCameraEvent(camera, (int) this.event.getX(), (int) this.event.getY(), 0, types));
                //                System.out.println("hovered");
            }
        }
    }
}
