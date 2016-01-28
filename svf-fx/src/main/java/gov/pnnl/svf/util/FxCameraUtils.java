package gov.pnnl.svf.util;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.CameraEvent;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.event.ScaledCameraEvent;
import gov.pnnl.svf.event.ScaledPickingCameraEvent;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.picking.PickingCamera;
import java.util.EnumSet;
import java.util.Set;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Utilities for JavaFX cameras.
 *
 * @author Arthur Bleeker
 */
public class FxCameraUtils {

    /**
     * Constructor private for util class
     */
    private FxCameraUtils() {
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
        // process the button number
        switch (event.getButton()) {
            case PRIMARY:
                types.add(CameraEventType.LEFT);
                break;
            case MIDDLE:
                types.add(CameraEventType.MIDDLE);
                break;
            case SECONDARY:
                types.add(CameraEventType.RIGHT);
                break;
            case NONE:
            default:
                types.add(CameraEventType.NONE);
                break;
        }
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
    public static void addButtonTypes(final ScrollEvent event, final Set<CameraEventType> types) {
        // button type
        if (Double.compare(event.getDeltaY(), 0.0) > 0) {
            types.add(CameraEventType.WHEEL_UP);
        } else if (Double.compare(event.getDeltaY(), 0.0) < 0) {
            types.add(CameraEventType.WHEEL_DOWN);
        } else {
            types.add(CameraEventType.NONE);
        }
    }

    /**
     * Add the correct event modifier types to the set according to the mouse
     * event.
     *
     * @param event the gesture event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addModifierTypes(final GestureEvent event, final Set<CameraEventType> types) {
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
            case PRIMARY:
                return CameraEventType.LEFT;
            case MIDDLE:
                return CameraEventType.MIDDLE;
            case SECONDARY:
                return CameraEventType.RIGHT;
            case NONE:
            default:
                return CameraEventType.NONE;
        }
    }

    /**
     * Create a new pick event.
     *
     * @param camera the camera that pushed this event
     * @param event  the scroll event
     *
     * @return the new pick event
     *
     * @throws NullPointerException if event is null
     */
    public static PickingCameraEvent newCameraEvent(final PickingCamera camera, final ScrollEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        FxCameraUtils.addButtonTypes(event, types);
        FxCameraUtils.addModifierTypes(event, types);
        // determine if there is scale difference
        final Point2D scale = CameraUtil.getCanvasScale(camera.getScene());
        if (Point2D.ONE.equals(scale)) {
            final PickingCameraEvent pickEvent = new PickingCameraEvent(camera, (int) event.getX(), (int) event.getY(), (int) Math.abs(event.getDeltaY()), types);
            return pickEvent;
        } else {
            final ScaledPickingCameraEvent pickEvent = new ScaledPickingCameraEvent(camera, (int) event.getX(), (int) event.getY(), (int) Math.abs(event.getDeltaY()), types, scale);
            return pickEvent;
        }
    }

    /**
     * Create a new pick event.
     *
     * @param camera the camera that pushed this event
     * @param event  the mouse event
     *
     * @return the new pick event
     *
     * @throws NullPointerException if event is null
     */
    public static PickingCameraEvent newCameraEvent(final PickingCamera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        FxCameraUtils.addButtonTypes(event, types);
        FxCameraUtils.addModifierTypes(event, types);
        // determine if there is scale difference
        final Point2D scale = CameraUtil.getCanvasScale(camera.getScene());
        if (Point2D.ONE.equals(scale)) {
            final PickingCameraEvent pickEvent = new PickingCameraEvent(camera, (int) event.getX(), (int) event.getY(), event.getClickCount(), types);
            return pickEvent;
        } else {
            final ScaledPickingCameraEvent pickEvent = new ScaledPickingCameraEvent(camera, (int) event.getX(), (int) event.getY(), event.getClickCount(), types, scale);
            return pickEvent;
        }
    }

    /**
     * Create a new pick event.
     *
     * @param camera the camera that pushed this event
     * @param event  the scroll event
     *
     * @return the new pick event
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEvent newCameraEvent(final Camera camera, final ScrollEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        FxCameraUtils.addButtonTypes(event, types);
        FxCameraUtils.addModifierTypes(event, types);
        // determine if there is scale difference
        final Point2D scale = CameraUtil.getCanvasScale(camera.getScene());
        if (Point2D.ONE.equals(scale)) {
            final CameraEvent pickEvent = new CameraEvent(camera, (int) event.getX(), (int) event.getY(), (int) Math.abs(event.getDeltaY()), types);
            return pickEvent;
        } else {
            final ScaledCameraEvent pickEvent = new ScaledCameraEvent(camera, (int) event.getX(), (int) event.getY(), (int) Math.abs(event.getDeltaY()), types, scale);
            return pickEvent;
        }
    }

    /**
     * Create a new pick event.
     *
     * @param camera the camera that pushed this event
     * @param event  the mouse event
     *
     * @return the new pick event
     *
     * @throws NullPointerException if event is null
     */
    public static CameraEvent newCameraEvent(final Camera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        FxCameraUtils.addButtonTypes(event, types);
        FxCameraUtils.addModifierTypes(event, types);
        // determine if there is scale difference
        final Point2D scale = CameraUtil.getCanvasScale(camera.getScene());
        if (Point2D.ONE.equals(scale)) {
            final CameraEvent pickEvent = new CameraEvent(camera, (int) event.getX(), (int) event.getY(), event.getClickCount(), types);
            return pickEvent;
        } else {
            final ScaledCameraEvent pickEvent = new ScaledCameraEvent(camera, (int) event.getX(), (int) event.getY(), event.getClickCount(), types, scale);
            return pickEvent;
        }
    }
}
