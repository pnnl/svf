package gov.pnnl.svf.newt.util;

import com.jogamp.newt.event.MouseEvent;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.CameraEvent;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.event.ScaledCameraEvent;
import gov.pnnl.svf.event.ScaledPickingCameraEvent;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.util.CameraUtil;
import java.util.EnumSet;
import java.util.Set;

/**
 * Utilities for working with Newt cameras.
 *
 * @author Arthur Bleeker
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
    public static void addButtonTypes(final MouseEvent evt, final Set<CameraEventType> types) {
        // button type
        if (evt.getRotation()[1] > 0) {
            types.add(CameraEventType.WHEEL_UP);
        } else if (evt.getRotation()[1] < 0) {
            types.add(CameraEventType.WHEEL_DOWN);
        } else {
            switch (evt.getButton()) {
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
     * @param evt   the mouse event
     * @param types the set of types
     *
     * @throws NullPointerException if event or types is null
     */
    public static void addModifierTypes(final MouseEvent evt, final Set<CameraEventType> types) {
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
        // determine if there is scale difference
        final Point2D scale = CameraUtil.getCanvasScale(camera.getScene());
        if (Point2D.ONE.equals(scale)) {
            final CameraEvent pickEvent = new CameraEvent(
                    camera,
                    event.getX(),
                    event.getY(),
                    Math.max(event.getClickCount(), Math.abs((int) Math.ceil(event.getRotation()[1]))),
                    types);
            return pickEvent;
        } else {
            final ScaledCameraEvent pickEvent = new ScaledCameraEvent(
                    camera,
                    event.getX(),
                    event.getY(),
                    Math.max(event.getClickCount(), Math.abs((int) Math.ceil(event.getRotation()[1]))),
                    types,
                    scale);
            return pickEvent;
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
    public static PickingCameraEvent newCameraEvent(final PickingCamera camera, final MouseEvent event) {
        final Set<CameraEventType> types = EnumSet.noneOf(CameraEventType.class);
        NewtCameraUtils.addButtonTypes(event, types);
        NewtCameraUtils.addModifierTypes(event, types);
        // determine if there is scale difference
        final Point2D scale = CameraUtil.getCanvasScale(camera.getScene());
        if (Point2D.ONE.equals(scale)) {
            final PickingCameraEvent pickEvent = new PickingCameraEvent(
                    camera,
                    event.getX(),
                    event.getY(),
                    Math.max(event.getClickCount(), Math.abs((int) Math.ceil(event.getRotation()[1]))),
                    types);
            return pickEvent;
        } else {
            final ScaledPickingCameraEvent pickEvent = new ScaledPickingCameraEvent(
                    camera,
                    event.getX(),
                    event.getY(),
                    Math.max(event.getClickCount(), Math.abs((int) Math.ceil(event.getRotation()[1]))),
                    types,
                    scale);
            return pickEvent;
        }
    }
}
