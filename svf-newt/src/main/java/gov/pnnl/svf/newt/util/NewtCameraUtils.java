package gov.pnnl.svf.newt.util;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.CameraEvent;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.picking.PickingCamera;
import java.awt.Point;
import java.util.EnumSet;
import java.util.Set;

/**
 * Utilities for working with Newt cameras.
 *
 * @author Amelia Bleeker
 */
public class NewtCameraUtils {

    /**
     * Constructor private for utility class
     */
    private NewtCameraUtils() {
        super();
    }

    /**
     * Add the correct event button types to the set according to the mouse
     * event.
     *
     * @param evt   the mouse event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addButtonTypes(final InputEvent evt, final Set<CameraEventType> types) {
        types.add(getButton(evt));
    }

    /**
     * Add the correct event modifier types to the set according to the mouse
     * event.
     *
     * @param evt   the mouse event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addModifierTypes(final InputEvent evt, final Set<CameraEventType> types) {
        if (evt.isAltDown()) {
            types.add(CameraEventType.ALT);
        }
        if (evt.isControlDown()) {
            types.add(CameraEventType.CTRL);
        }
        if (evt.isShiftDown()) {
            types.add(CameraEventType.SHIFT);
        }
    }

    /**
     * Get the button for the supplied event.
     *
     * @param event the mouse event
     *
     * @return the button
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEventType getButton(final InputEvent event) {
        if (event instanceof MouseEvent) {
            // wheel event type
            if (((MouseEvent) event).getRotation()[1] > 0) {
                return CameraEventType.WHEEL_UP;
            } else if (((MouseEvent) event).getRotation()[1] < 0) {
                return CameraEventType.WHEEL_DOWN;
            } else {
                // mouse button event type
                switch (((MouseEvent) event).getButton()) {
                    case MouseEvent.BUTTON1:
                        return CameraEventType.LEFT;
                    case MouseEvent.BUTTON2:
                        return CameraEventType.MIDDLE;
                    case MouseEvent.BUTTON3:
                        return CameraEventType.RIGHT;
                    default:
                        return CameraEventType.NONE;
                }
            }
        } else if (event instanceof KeyEvent) {
            // key event type
            return CameraEventType.KEY;
        } else {
            // unknown
            return CameraEventType.NONE;
        }
    }

    /**
     * Create a new camera event.
     *
     * @param camera the camera that created the event
     * @param event  the mouse event
     *
     * @return the new camera event
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEvent newCameraEvent(final Camera camera, final KeyEvent event, final Point location) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        NewtCameraUtils.addButtonTypes(event, types);
        NewtCameraUtils.addModifierTypes(event, types);
        final CameraEvent pickEvent = new CameraEvent(
                camera,
                (int) location.getX(),
                (int) location.getY(),
                event.getKeyChar(),
                types);
        return pickEvent;
    }

    /**
     * Create a new camera event.
     *
     * @param camera the camera that created the event
     * @param event  the mouse event
     *
     * @return the new camera event
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEvent newCameraEvent(final Camera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        NewtCameraUtils.addButtonTypes(event, types);
        NewtCameraUtils.addModifierTypes(event, types);
        final CameraEvent pickEvent = new CameraEvent(
                camera,
                event.getX(),
                event.getY(),
                Math.max(event.getClickCount(), Math.abs((int) Math.ceil(event.getRotation()[1]))),
                types);
        return pickEvent;
    }

    /**
     * Create a new camera event.
     *
     * @param camera the camera that created the event
     * @param event  the mouse event
     *
     * @return the new camera event
     *
     * @throws NullPointerException if event is null
     */
    public static PickingCameraEvent newCameraEvent(final PickingCamera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        NewtCameraUtils.addButtonTypes(event, types);
        NewtCameraUtils.addModifierTypes(event, types);
        final PickingCameraEvent pickEvent = new PickingCameraEvent(
                camera,
                event.getX(),
                event.getY(),
                Math.max(event.getClickCount(), Math.abs((int) Math.ceil(event.getRotation()[1]))),
                types);
        return pickEvent;
    }
}
