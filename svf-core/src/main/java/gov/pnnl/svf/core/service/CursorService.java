package gov.pnnl.svf.core.service;

import java.util.Set;

/**
 * Service for changing the cursor according to the current state.
 *
 * @author Arthur Bleeker
 */
public interface CursorService {

    /**
     * Cursor add resource location.
     */
    public static final String CURSOR_ADD_RESOURCE = "gov/pnnl/svf/resources/select-add.png";
    /**
     * Cursor remove resource location.
     */
    public static final String CURSOR_REMOVE_RESOURCE = "gov/pnnl/svf/resources/select-remove.png";
    /**
     * Cursor intersection resource location.
     */
    public static final String CURSOR_INTERSECTION_RESOURCE = "gov/pnnl/svf/resources/select-intersection.png";
    /**
     * Cursor toggle resource location.
     */
    public static final String CURSOR_TOGGLE_RESOURCE = "gov/pnnl/svf/resources/select-toggle.png";
    /**
     * String representation of a field in this object.
     */
    public static final String CURSOR = "cursor";
    /**
     * String representation of a field in this object.
     */
    public static final String CURSOR_TYPES = "cursorTypes";
    /**
     * String representation of a field in this object.
     */
    public static final String ACTIVE_CURSOR_TYPE = "activeCursorType";

    /**
     * Initialize the service.
     */
    void initialize();

    /**
     * Dispose the service.
     */
    void dispose();

    /**
     * Get the cursor type that is currently the active one.
     *
     * @return the cursor type that is currently active
     */
    CursorType getActiveCursorType();

    /**
     * Add a cursor type as active.
     *
     * @param activeCursorType add a cursor type that is currently active
     */
    void addActiveCursorType(CursorType activeCursorType);

    /**
     * Remove a cursor type to set it as inactive.
     *
     * @param activeCursorType remove a cursor type that is currently not active
     */
    void removeActiveCursorType(CursorType activeCursorType);

    /**
     * Set of all the cursor types that this support object will respond to.
     *
     * @return a copy of the cursor types
     */
    Set<CursorType> getCursorTypes();

    /**
     * Set of all the cursor types that this support object will respond to.
     *
     * @param cursorTypes the cursor types
     */
    void setCursorTypes(CursorType... cursorTypes);

    /**
     * Set of all the cursor types that this support object will respond to.
     *
     * @param cursorTypes the cursor types
     */
    void setCursorTypes(Set<CursorType> cursorTypes);

    /**
     * Set the cursor associated with the supplied type.
     *
     * @param cursorType the cursor type
     * @param cursor     the cursor to associate with the type
     */
    void setCursor(CursorType cursorType, Object cursor);

    /**
     * Get the cursor associated with the supplied type.
     *
     * @param cursorType the cursor type
     *
     * @return the cursor or null if no type is currently associated
     */
    Object getCursor(CursorType cursorType);

}
