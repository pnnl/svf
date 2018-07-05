package gov.pnnl.svf.core.service;

/**
 * Cursor types in priority order. Multi types are used for referencing cursors
 * only.
 *
 * @author Amelia Bleeker
 */
public enum CursorType {

    /**
     * Camera is being dragged.
     */
    DRAGGING,
    /**
     * Camera is being zoomed.
     */
    ZOOMING,
    /**
     * Shift, ctrl, and alt keys are held down. Used for referencing a cursor.
     */
    SHIFT_CTRL_ALT,
    /**
     * Shift and ctrl keys are held down. Used for referencing a cursor.
     */
    SHIFT_CTRL,
    /**
     * Shift and alt keys are held down. Used for referencing a cursor.
     */
    SHIFT_ALT,
    /**
     * Ctrl and alt keys are held down. Used for referencing a cursor.
     */
    CTRL_ALT,
    /**
     * Shift key is held down.
     */
    SHIFT,
    /**
     * Ctrl key is held down.
     */
    CTRL,
    /**
     * Alt key is held down.
     */
    ALT,
    /**
     * System if busy.
     */
    BUSY,
    /**
     * Represents the default cursor.
     */
    DEFAULT;
}
