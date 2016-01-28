package gov.pnnl.svf.awt.util;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.CameraEvent;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.event.ScaledCameraEvent;
import gov.pnnl.svf.event.ScaledPickingCameraEvent;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.util.CameraUtil;
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
     * Add the correct event button types to the set according to the mouse
     * event.
     *
     * @param event the mouse event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addButtonTypes(final MouseEvent event, final Set<CameraEventType> types) {
        if (event instanceof MouseWheelEvent) {
            // button type
            if (((MouseWheelEvent) event).getWheelRotation() < 0) {
                types.add(CameraEventType.WHEEL_UP);
            } else if (((MouseWheelEvent) event).getWheelRotation() > 0) {
                types.add(CameraEventType.WHEEL_DOWN);
            } else {
                types.add(CameraEventType.NONE);
            }
        } else {
            // process the button number
            switch (event.getButton()) {
                case MouseEvent.BUTTON1:
                    types.add(CameraEventType.LEFT);
                    break;
                case MouseEvent.BUTTON2:
                    types.add(CameraEventType.MIDDLE);
                    break;
                case MouseEvent.BUTTON3:
                    types.add(CameraEventType.RIGHT);
                    break;
                default:
                    types.add(CameraEventType.NONE);
                    break;
            }
        }
    }

    /**
     * Add the correct event modifier types to the set according to the mouse
     * event.
     *
     * @param event the mouse event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addModifierTypes(final MouseEvent event, final Set<CameraEventType> types) {
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
     * @param event the mouse event
     *
     * @return the button
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEventType getButton(final MouseEvent event) {
        switch (event.getButton()) {
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
        // determine if there is scale difference
        final Point2D scale = CameraUtil.getCanvasScale(camera.getScene());
        if (Point2D.ONE.equals(scale)) {
            final CameraEvent pickEvent = new CameraEvent(
                    camera,
                    event.getX(),
                    event.getY(),
                    Math.max(event.getClickCount(), event instanceof MouseWheelEvent ? Math.abs(((MouseWheelEvent) event).getWheelRotation()) : 0),
                    types);
            return pickEvent;
        } else {
            final ScaledCameraEvent pickEvent = new ScaledCameraEvent(
                    camera,
                    event.getX(),
                    event.getY(),
                    Math.max(event.getClickCount(), event instanceof MouseWheelEvent ? Math.abs(((MouseWheelEvent) event).getWheelRotation()) : 0),
                    types,
                    scale);
            return pickEvent;
        }
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
        // determine if there is scale difference
        final Point2D scale = CameraUtil.getCanvasScale(camera.getScene());
        if (Point2D.ONE.equals(scale)) {
            final PickingCameraEvent pickEvent = new PickingCameraEvent(
                    camera,
                    event.getX(),
                    event.getY(),
                    Math.max(event.getClickCount(), event instanceof MouseWheelEvent ? Math.abs(((MouseWheelEvent) event).getWheelRotation()) : 0),
                    types);
            return pickEvent;
        } else {
            final ScaledPickingCameraEvent pickEvent = new ScaledPickingCameraEvent(
                    camera,
                    event.getX(),
                    event.getY(),
                    Math.max(event.getClickCount(), event instanceof MouseWheelEvent ? Math.abs(((MouseWheelEvent) event).getWheelRotation()) : 0),
                    types,
                    scale);
            return pickEvent;
        }
    }
}
