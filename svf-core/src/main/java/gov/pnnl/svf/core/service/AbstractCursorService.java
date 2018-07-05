package gov.pnnl.svf.core.service;

import gov.pnnl.svf.core.util.PropertyChangeSupportWrapper;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Abstract base class for changing the cursor according to the current state.
 *
 * @param <C> cursor type is the cursor type used by the scene user interface
 *
 * @author Amelia Bleeker
 */
public abstract class AbstractCursorService<C> implements CursorService, PropertyChangeListener {

    private static final Logger logger = Logger.getLogger(AbstractCursorService.class.getName());
    private static final Set<CursorType> SUBTYPES_SHIFT_CTRL_ALT = EnumSet.of(CursorType.SHIFT, CursorType.CTRL, CursorType.ALT);
    private static final Set<CursorType> SUBTYPES_SHIFT_CTRL = EnumSet.of(CursorType.SHIFT, CursorType.CTRL);
    private static final Set<CursorType> SUBTYPES_SHIFT_ALT = EnumSet.of(CursorType.SHIFT, CursorType.ALT);
    private static final Set<CursorType> SUBTYPES_CTRL_ALT = EnumSet.of(CursorType.CTRL, CursorType.ALT);

    protected final PropertyChangeSupport pcs = new PropertyChangeSupportWrapper(this);
    protected final Map<CursorType, C> cursors;
    protected final Set<CursorType> cursorTypes;
    protected final Set<CursorType> activeCursorTypes;

    /**
     * Constructor
     */
    protected AbstractCursorService() {
        super();
        cursors = new EnumMap<>(CursorType.class);
        cursorTypes = EnumSet.allOf(CursorType.class);
        activeCursorTypes = EnumSet.noneOf(CursorType.class);
        pcs.addPropertyChangeListener(this);
    }

    @Override
    public void dispose() {
        // no operation
    }

    @Override
    public CursorType getActiveCursorType() {
        synchronized (this) {
            if (activeCursorTypes.isEmpty()) {
                return null;
            } else {
                return activeCursorTypes.iterator().next();
            }
        }
    }

    @Override
    public void addActiveCursorType(CursorType activeCursorType) {
        if (activeCursorType == null) {
            throw new NullPointerException("activeCursorType");
        }
        final Object old;
        synchronized (this) {
            // determine if this state should get set
            if (!cursorTypes.contains(activeCursorType)) {
                return;
            }
            // set the state
            old = getActiveCursorType();
            this.activeCursorTypes.add(activeCursorType);
            updateActiveCursorTypes();
            activeCursorType = getActiveCursorType();
        }
        pcs.firePropertyChange(ACTIVE_CURSOR_TYPE, old, activeCursorType);
    }

    @Override
    public void removeActiveCursorType(CursorType activeCursorType) {
        if (activeCursorType == null) {
            throw new NullPointerException("activeCursorType");
        }
        final Object old;
        synchronized (this) {
            // determine if this state should get set
            if (!cursorTypes.contains(activeCursorType)) {
                return;
            }
            // set the state
            old = getActiveCursorType();
            this.activeCursorTypes.remove(activeCursorType);
            updateActiveCursorTypes();
            activeCursorType = getActiveCursorType();
        }
        pcs.firePropertyChange(ACTIVE_CURSOR_TYPE, old, activeCursorType);
    }

    @Override
    public Set<CursorType> getCursorTypes() {
        final Set<CursorType> copy;
        synchronized (this) {
            if (cursorTypes.isEmpty()) {
                copy = Collections.emptySet();
            } else {
                copy = Collections.unmodifiableSet(EnumSet.copyOf(cursorTypes));
            }
        }
        return copy;
    }

    @Override
    public void setCursorTypes(final CursorType... cursorTypes) {
        if (cursorTypes == null) {
            throw new NullPointerException("cursorTypes");
        }
        if (cursorTypes.length == 0) {
            setCursorTypes(Collections.<CursorType>emptySet());
        } else {
            setCursorTypes(EnumSet.copyOf(Arrays.asList(cursorTypes)));
        }
    }

    @Override
    public void setCursorTypes(final Set<CursorType> cursorTypes) {
        if (cursorTypes == null) {
            throw new NullPointerException("cursorTypes");
        }
        final Set<CursorType> oldCursorTypes;
        final Object oldActiveCursorType;
        final Object newActiveCursorType;
        synchronized (this) {
            oldCursorTypes = getCursorTypes();
            this.cursorTypes.clear();
            this.cursorTypes.addAll(cursorTypes);
            // filter active cursor types if necessary
            oldActiveCursorType = getActiveCursorType();
            this.activeCursorTypes.retainAll(this.cursorTypes);
            newActiveCursorType = getActiveCursorType();
        }
        pcs.firePropertyChange(CURSOR_TYPES, oldCursorTypes, cursorTypes);
        pcs.firePropertyChange(ACTIVE_CURSOR_TYPE, oldActiveCursorType, newActiveCursorType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setCursor(final CursorType cursorType, final Object cursor) {
        if (cursorType == null) {
            throw new NullPointerException("cursorType");
        }
        if (cursor == null) {
            throw new NullPointerException("cursor");
        }
        final C old;
        synchronized (this) {
            old = cursors.put(cursorType, (C) cursor);
        }
        pcs.firePropertyChange(CURSOR, old, cursor);
    }

    @Override
    public C getCursor(final CursorType cursorType) {
        if (cursorType == null) {
            throw new NullPointerException("cursorType");
        }
        synchronized (this) {
            return cursors.get(cursorType);
        }
    }

    private void updateActiveCursorTypes() {
        if (activeCursorTypes.containsAll(SUBTYPES_SHIFT_CTRL_ALT)) {
            activeCursorTypes.add(CursorType.SHIFT_CTRL_ALT);
        } else {
            activeCursorTypes.remove(CursorType.SHIFT_CTRL_ALT);
        }
        if (activeCursorTypes.containsAll(SUBTYPES_SHIFT_CTRL)) {
            activeCursorTypes.add(CursorType.SHIFT_CTRL);
        } else {
            activeCursorTypes.remove(CursorType.SHIFT_CTRL);
        }
        if (activeCursorTypes.containsAll(SUBTYPES_SHIFT_ALT)) {
            activeCursorTypes.add(CursorType.SHIFT_ALT);
        } else {
            activeCursorTypes.remove(CursorType.SHIFT_ALT);
        }
        if (activeCursorTypes.containsAll(SUBTYPES_CTRL_ALT)) {
            activeCursorTypes.add(CursorType.CTRL_ALT);
        } else {
            activeCursorTypes.remove(CursorType.CTRL_ALT);
        }
    }
}
