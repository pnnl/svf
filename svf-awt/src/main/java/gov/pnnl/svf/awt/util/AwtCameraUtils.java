package gov.pnnl.svf.awt.util;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.CameraEvent;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.picking.PickingCamera;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EnumSet;
import java.util.Set;

/**
 * Utilities for AWT cameras.
 *
 * @author Arthur Bleeker
 */
public class AwtCameraUtils {

    /**
     * Constructor private for util class
     */
    private AwtCameraUtils() {
        super();
    }

    /**
     * Add the correct event button types to the set according to the input
     * event.
     *
     * @param event the input event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addButtonTypes(final InputEvent event, final Set<CameraEventType> types) {
        types.add(getButton(event));
    }

    /**
     * Add the correct event modifier types to the set according to the input
     * event.
     *
     * @param event the input event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addModifierTypes(final InputEvent event, final Set<CameraEventType> types) {
        // process modifier keys
        if (event.isAltDown()) {
            types.add(CameraEventType.ALT);
        }
        if (event.isControlDown()) {
            types.add(CameraEventType.CTRL);
        }
        if (event.isShiftDown()) {
            types.add(CameraEventType.SHIFT);
        }
    }

    /**
     * Get the button for the supplied event.
     *
     * @param event the input event
     *
     * @return the button event type
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEventType getButton(final InputEvent event) {
        if (event instanceof MouseWheelEvent) {
            // wheel event type
            if (((MouseWheelEvent) event).getWheelRotation() < 0) {
                return CameraEventType.WHEEL_UP;
            } else if (((MouseWheelEvent) event).getWheelRotation() > 0) {
                return CameraEventType.WHEEL_DOWN;
            } else {
                return CameraEventType.NONE;
            }
        } else if (event instanceof MouseEvent) {
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
     * @param camera   camera that generated the event
     * @param event    the key event
     * @param location the event location
     *
     * @return the new camera event
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEvent newCameraEvent(final Camera camera, final KeyEvent event, final Point location) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        AwtCameraUtils.addButtonTypes(event, types);
        AwtCameraUtils.addModifierTypes(event, types);
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
     * @param camera camera that generated the event
     * @param event  the mouse event
     *
     * @return the new camera event
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEvent newCameraEvent(final Camera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        AwtCameraUtils.addButtonTypes(event, types);
        AwtCameraUtils.addModifierTypes(event, types);
        final CameraEvent pickEvent = new CameraEvent(
                camera,
                event.getX(),
                event.getY(),
                Math.max(event.getClickCount(), event instanceof MouseWheelEvent ? Math.abs(((MouseWheelEvent) event).getWheelRotation()) : 0),
                types);
        return pickEvent;
    }

    /**
     * Create a new camera event.
     *
     * @param camera camera that generated the event
     * @param event  the mouse event
     *
     * @return the new camera event
     *
     * @throws NullPointerException if event is null
     */
    public static PickingCameraEvent newCameraEvent(final PickingCamera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        AwtCameraUtils.addButtonTypes(event, types);
        AwtCameraUtils.addModifierTypes(event, types);
        final PickingCameraEvent pickEvent = new PickingCameraEvent(
                camera,
                event.getX(),
                event.getY(),
                Math.max(event.getClickCount(), event instanceof MouseWheelEvent ? Math.abs(((MouseWheelEvent) event).getWheelRotation()) : 0),
                types);
        return pickEvent;
    }
}
