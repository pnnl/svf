package gov.pnnl.svf.util;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.CameraEvent;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.event.ProxyMouseEvent;
import gov.pnnl.svf.picking.PickingCamera;
import java.util.EnumSet;
import java.util.Set;

/**
 * Utilities for proxy cameras.
 *
 * @author Arthur Bleeker
 */
public class ProxyCameraUtils {

    /**
     * Constructor private for util class
     */
    private ProxyCameraUtils() {
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
    public static void addButtonTypes(final ProxyMouseEvent event, final Set<CameraEventType> types) {
        // process the button number
        switch (event.getButton()) {
            case WHEEL:
                if (event.getWheelRotation() < 0) {
                    types.add(CameraEventType.WHEEL_UP);
                } else if (event.getWheelRotation() > 0) {
                    types.add(CameraEventType.WHEEL_DOWN);
                } else {
                    types.add(CameraEventType.NONE);
                }
                break;
            case BUTTON1:
                types.add(CameraEventType.LEFT);
                break;
            case BUTTON2:
                types.add(CameraEventType.MIDDLE);
                break;
            case BUTTON3:
                types.add(CameraEventType.RIGHT);
                break;
            default:
                types.add(CameraEventType.NONE);
                break;
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
    public static void addModifierTypes(final ProxyMouseEvent event, final Set<CameraEventType> types) {
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
    public static CameraEventType getButton(final ProxyMouseEvent event) {
        switch (event.getButton()) {
            case BUTTON1:
                return CameraEventType.LEFT;
            case BUTTON2:
                return CameraEventType.MIDDLE;
            case BUTTON3:
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
    public static CameraEvent newCameraEvent(final Camera camera, final ProxyMouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        ProxyCameraUtils.addButtonTypes(event, types);
        ProxyCameraUtils.addModifierTypes(event, types);
        final CameraEvent pickEvent = new CameraEvent(camera, event.getX(), event.getY(), Math.max(event.getClickCount(), Math.abs(event.getWheelRotation())), types);
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
    public static PickingCameraEvent newCameraEvent(final PickingCamera camera, final ProxyMouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        ProxyCameraUtils.addButtonTypes(event, types);
        ProxyCameraUtils.addModifierTypes(event, types);
        final PickingCameraEvent pickEvent = new PickingCameraEvent(camera, event.getX(), event.getY(), Math.max(event.getClickCount(), Math.abs(event.getWheelRotation())), types);
        return pickEvent;
    }
}
