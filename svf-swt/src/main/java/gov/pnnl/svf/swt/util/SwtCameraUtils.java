package gov.pnnl.svf.swt.util;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.CameraEvent;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.picking.PickingCamera;
import java.util.EnumSet;
import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

/**
 * Utilities for AWT cameras.
 *
 * @author Arthur Bleeker
 */
public class SwtCameraUtils {

    /**
     * Constructor private for util class
     */
    private SwtCameraUtils() {
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
        types.add(getButton(event));
    }

    /**
     * Add the correct event button types to the set according to the key event.
     *
     * @param event the key event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addButtonTypes(final KeyEvent event, final Set<CameraEventType> types) {
        types.add(getButton(event));
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
        if ((event.stateMask & SWT.SHIFT) != 0) {
            types.add(CameraEventType.SHIFT);
        }
        if ((event.stateMask & SWT.CTRL) != 0) {
            types.add(CameraEventType.CTRL);
        }
        if ((event.stateMask & SWT.ALT) != 0) {
            types.add(CameraEventType.ALT);
        }
    }

    /**
     * Add the correct event modifier types to the set according to the key
     * event.
     *
     * @param event the key event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addModifierTypes(final KeyEvent event, final Set<CameraEventType> types) {
        // process modifier keys
        if ((event.stateMask & SWT.SHIFT) != 0) {
            types.add(CameraEventType.SHIFT);
        }
        if ((event.stateMask & SWT.CTRL) != 0) {
            types.add(CameraEventType.CTRL);
        }
        if ((event.stateMask & SWT.ALT) != 0) {
            types.add(CameraEventType.ALT);
        }
    }

    /**
     * Get the button for the supplied event.
     *
     * @param event the mouse event
     *
     * @return the button type
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEventType getButton(final MouseEvent event) {
        // process the button number
        switch (event.button) {
            case 0:
                // button type
                if (event.count > 0) {
                    return CameraEventType.WHEEL_UP;
                } else if (event.count < 0) {
                    return CameraEventType.WHEEL_DOWN;
                } else {
                    return CameraEventType.NONE;
                }
            case 1:
                return CameraEventType.LEFT;
            case 2:
                return CameraEventType.MIDDLE;
            case 3:
                return CameraEventType.RIGHT;
            default:
                return CameraEventType.NONE;
        }
    }

    /**
     * Get the button for the supplied event.
     *
     * @param event the key event
     *
     * @return the key type
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEventType getButton(final KeyEvent event) {
        // process the button number
        return CameraEventType.KEY;
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
        SwtCameraUtils.addButtonTypes(event, types);
        SwtCameraUtils.addModifierTypes(event, types);
        final CameraEvent pickEvent = new CameraEvent(
                camera,
                location.x,
                location.y,
                event.character,
                types);
        return pickEvent;
    }

    /**
     * Create a new camera event.
     *
     * @param camera the camera that pushed this event
     * @param event  the mouse event
     *
     * @return the new camera event
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEvent newCameraEvent(final Camera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        SwtCameraUtils.addButtonTypes(event, types);
        SwtCameraUtils.addModifierTypes(event, types);
        final CameraEvent pickEvent = new CameraEvent(camera, event.x, event.y, Math.abs(event.count), types);
        return pickEvent;
    }

    /**
     * Create a new camera event.
     *
     * @param camera the camera that pushed this event
     * @param event  the mouse event
     *
     * @return the new camera event
     *
     * @throws NullPointerException if event is null
     */
    public static PickingCameraEvent newCameraEvent(final PickingCamera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        SwtCameraUtils.addButtonTypes(event, types);
        SwtCameraUtils.addModifierTypes(event, types);
        final PickingCameraEvent pickEvent = new PickingCameraEvent(camera, event.x, event.y, Math.abs(event.count), types);
        return pickEvent;
    }
}
