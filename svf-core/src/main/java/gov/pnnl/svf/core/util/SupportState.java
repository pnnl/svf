package gov.pnnl.svf.core.util;

/**
 * Support for utilizing state values to indicate current support state. This
 * class is not thread safe.
 *
 * @author Amelia Bleeker
 */
public class SupportState extends AbstractState {

    private static final byte DIRTY_MASK = StateUtil.getMasks()[0];
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[1];
    private static final byte INITIALIZED_MASK = StateUtil.getMasks()[2];
    private static final byte INTERNAL_DIRTY_MASK = StateUtil.getMasks()[3];
    private static final byte SELECTED_MASK = StateUtil.getMasks()[4];
    private static final byte HIGHLIGHTED_MASK = StateUtil.getMasks()[5];
    private static final byte RELATED_MASK = StateUtil.getMasks()[6];
    private static final byte EXTRA_MASK = StateUtil.getMasks()[7];
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public SupportState() {
    }

    /**
     * @return true if the selected state value is set
     */
    public boolean isSelected() {
        return StateUtil.isValue(state, SELECTED_MASK);
    }

    /**
     * Set the selected state value to true.
     */
    public void setSelected() {
        state = StateUtil.setValue(state, SELECTED_MASK);
    }

    /**
     * Set the selected state value to true.
     *
     * @param value true to set the value, false to unset
     */
    public void setSelected(final boolean value) {
        state = StateUtil.setValue(state, SELECTED_MASK, value);
    }

    /**
     * @return true if the dirty state value is set
     */
    public boolean isDirty() {
        return StateUtil.isValue(state, DIRTY_MASK);
    }

    /**
     * Set the dirty state value to true.
     */
    public void setDirty() {
        state = StateUtil.setValue(state, DIRTY_MASK);
    }

    /**
     * Set the dirty state value to true.
     *
     * @param value true to set the value, false to unset
     */
    public void setDirty(final boolean value) {
        state = StateUtil.setValue(state, DIRTY_MASK, value);
    }

    /**
     * @return true if the highlighted state value is set
     */
    public boolean isHighlighted() {
        return StateUtil.isValue(state, HIGHLIGHTED_MASK);
    }

    /**
     * Set the highlighted state value to true.
     */
    public void setHighlighted() {
        state = StateUtil.setValue(state, HIGHLIGHTED_MASK);
    }

    /**
     * Set the highlighted state value to true.
     *
     * @param value true to set the value, false to unset
     */
    public void setHighlighted(final boolean value) {
        state = StateUtil.setValue(state, HIGHLIGHTED_MASK, value);
    }

    /**
     * @return true if the related state value is set
     */
    public boolean isRelated() {
        return StateUtil.isValue(state, RELATED_MASK);
    }

    /**
     * Set the related state value to true.
     */
    public void setRelated() {
        state = StateUtil.setValue(state, RELATED_MASK);
    }

    /**
     * Set the related state value to true.
     *
     * @param value true to set the value, false to unset
     */
    public void setRelated(final boolean value) {
        state = StateUtil.setValue(state, RELATED_MASK, value);
    }

    /**
     * @return true if the disposed state value is set
     */
    public boolean isDisposed() {
        return StateUtil.isValue(state, DISPOSED_MASK);
    }

    /**
     * Set the disposed state value to true.
     */
    public void setDisposed() {
        state = StateUtil.setValue(state, DISPOSED_MASK);
    }

    /**
     * Set the disposed state value to true.
     *
     * @param value true to set the value, false to unset
     */
    public void setDisposed(final boolean value) {
        state = StateUtil.setValue(state, DISPOSED_MASK, value);
    }

    /**
     * @return true if the initialized state value is set
     */
    public boolean isInitialized() {
        return StateUtil.isValue(state, INITIALIZED_MASK);
    }

    /**
     * Set the initialized state value to true.
     */
    public void setInitialized() {
        state = StateUtil.setValue(state, INITIALIZED_MASK);
    }

    /**
     * Set the initialized state value to true.
     *
     * @param value true to set the value, false to unset
     */
    public void setInitialized(final boolean value) {
        state = StateUtil.setValue(state, INITIALIZED_MASK, value);
    }

    /**
     * @return true if the internalDirty state value is set
     */
    public boolean isInternalDirty() {
        return StateUtil.isValue(state, INTERNAL_DIRTY_MASK);
    }

    /**
     * Set the internalDirty state value to true.
     */
    public void setInternalDirty() {
        state = StateUtil.setValue(state, INTERNAL_DIRTY_MASK);
    }

    /**
     * Set the internalDirty state value to true.
     *
     * @param value true to set the value, false to unset
     */
    public void setInternalDirty(final boolean value) {
        state = StateUtil.setValue(state, INTERNAL_DIRTY_MASK, value);
    }

    /**
     * @return true if the extra state value is set
     */
    public boolean isExtra() {
        return StateUtil.isValue(state, EXTRA_MASK);
    }

    /**
     * Set the extra state value to true.
     */
    public void setExtra() {
        state = StateUtil.setValue(state, EXTRA_MASK);
    }

    /**
     * Set the extra state value to true.
     *
     * @param value true to set the value, false to unset
     */
    public void setExtra(final boolean value) {
        state = StateUtil.setValue(state, EXTRA_MASK, value);
    }
}
