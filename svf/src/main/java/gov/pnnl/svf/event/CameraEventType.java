package gov.pnnl.svf.event;

import java.util.EnumSet;
import java.util.Set;

/**
 * Type of camera event that occurred.
 *
 * @author Arthur Bleeker
 */
public enum CameraEventType {

    /**
     * Mouse event type for a null pick. This type can occur when all buttons
     * are released.
     */
    NONE,
    /**
     * Mouse event type that represents a down button click.
     */
    DOWN,
    /**
     * Mouse event type that represents a single button click.
     */
    SINGLE,
    /**
     * Mouse event type that represents a double button click.
     */
    DOUBLE,
    /**
     * Mouse event type for a hover mouse event.
     */
    HOVER,
    /**
     * Mouse event type for a mouse wheel scroll up event.
     */
    WHEEL_UP,
    /**
     * Mouse event type for a mouse wheel scroll down event.
     */
    WHEEL_DOWN,
    /**
     * Mouse event type that represents the left button.
     */
    LEFT,
    /**
     * Mouse event type that represents the middle button.
     */
    MIDDLE,
    /**
     * Mouse event type that represents the right button.
     */
    RIGHT,
    /**
     * Mouse event type for a click and drag mouse event. This type can replace
     * the singe or double click type.
     */
    AREA,
    /**
     * Mouse event type that represents the shift modifier key.
     */
    SHIFT,
    /**
     * Mouse event type that represents the alt modifier key.
     */
    ALT,
    /**
     * Mouse event type that represents the control modifier key.
     */
    CTRL,
    /**
     * Mouse event type for a drag mouse event.
     */
    DRAG,
    /**
     * Mouse event type for a move mouse event.
     */
    MOVE,
    /**
     * Mouse event type that represents any mouse event.
     */
    ANY;

    /**
     * Holds the various collections for the event types.
     */
    public static class Collections {

        /**
         * Constructor kept private for utility class.
         */
        private Collections() {
        }

        /**
         * Set containing all of the event types that represent an action: NONE,
         * DOWN, SINGLE, DOUBLE, HOVER, AREA
         */
        public static final Set<CameraEventType> ACTION_TYPES = java.util.Collections.unmodifiableSet(EnumSet.of(CameraEventType.NONE, CameraEventType.DOWN,
                                                                                                                 CameraEventType.SINGLE, CameraEventType.DOUBLE, CameraEventType.HOVER, CameraEventType.AREA));
        /**
         * Set containing all of the event types that represent a button: LEFT,
         * MIDDLE, RIGHT, WHEEL_DOWN, WHEEL_UP
         */
        public static final Set<CameraEventType> BUTTON_TYPES = java.util.Collections.unmodifiableSet(EnumSet.of(CameraEventType.LEFT, CameraEventType.MIDDLE,
                                                                                                                 CameraEventType.RIGHT, CameraEventType.WHEEL_DOWN, CameraEventType.WHEEL_UP));
        /**
         * Set containing all of the event types that represent a modifier key:
         * SHIFT, ALT, CTRL
         */
        public static final Set<CameraEventType> MODIFIER_TYPES = java.util.Collections.unmodifiableSet(EnumSet.of(CameraEventType.SHIFT, CameraEventType.ALT,
                                                                                                                   CameraEventType.CTRL));
        /**
         * Set containing all of the event types that represent a movement:
         * MOVE, DRAG
         */
        public static final Set<CameraEventType> MOVEMENT_TYPES = java.util.Collections.unmodifiableSet(EnumSet.of(CameraEventType.MOVE, CameraEventType.DRAG));
        /**
         * Set containing all of the event types that represent all types
         * (except the ANY type.)
         */
        public static final Set<CameraEventType> ALL_TYPES;

        static {
            final Set<CameraEventType> temp = EnumSet.allOf(CameraEventType.class);
            temp.remove(CameraEventType.ANY);
            ALL_TYPES = java.util.Collections.unmodifiableSet(temp);
        }
    }
}
