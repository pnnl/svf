package gov.pnnl.svf.core.util;

/**
 * Support for utilizing state values to indicate current actor state. This
 * class is not thread safe.
 *
 * @author Arthur Bleeker
 */
public class ActorState extends AbstractState {

    private static final byte VISIBLE_MASK = StateUtil.getMasks()[0];
    private static final byte DIRTY_MASK = StateUtil.getMasks()[1];
    private static final byte ROOT_MASK = StateUtil.getMasks()[2];
    private static final byte WIRE_MASK = StateUtil.getMasks()[3];
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[4];
    private static final byte INITIALIZED_MASK = StateUtil.getMasks()[5];
    private static final byte INTERNAL_DIRTY_MASK = StateUtil.getMasks()[6];
    private static final byte EXTRA_MASK = StateUtil.getMasks()[7];
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public ActorState() {
    }

    /**
     * @return true if the visible state value is set
     */
    public boolean isVisible() {
        return StateUtil.isValue(state, VISIBLE_MASK);
    }

    /**
     * Set the visible state value to true.
     *
     * @return this instance
     */
    public ActorState setVisible() {
        state = StateUtil.setValue(state, VISIBLE_MASK);
        return this;
    }

    /**
     * Set the visible state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public ActorState setVisible(final boolean value) {
        state = StateUtil.setValue(state, VISIBLE_MASK, value);
        return this;
    }

    /**
     * @return true if the dirty state value is set
     */
    public boolean isDirty() {
        return StateUtil.isValue(state, DIRTY_MASK);
    }

    /**
     * Set the dirty state value to true.
     *
     * @return this instance
     */
    public ActorState setDirty() {
        state = StateUtil.setValue(state, DIRTY_MASK);
        return this;
    }

    /**
     * Set the dirty state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public ActorState setDirty(final boolean value) {
        state = StateUtil.setValue(state, DIRTY_MASK, value);
        return this;
    }

    /**
     * @return true if the root state value is set
     */
    public boolean isRoot() {
        return StateUtil.isValue(state, ROOT_MASK);
    }

    /**
     * Set the root state value to true.
     *
     * @return this instance
     */
    public ActorState setRoot() {
        state = StateUtil.setValue(state, ROOT_MASK);
        return this;
    }

    /**
     * Set the root state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public ActorState setRoot(final boolean value) {
        state = StateUtil.setValue(state, ROOT_MASK, value);
        return this;
    }

    /**
     * @return true if the wire state value is set
     */
    public boolean isWire() {
        return StateUtil.isValue(state, WIRE_MASK);
    }

    /**
     * Set the wire state value to true.
     *
     * @return this instance
     */
    public ActorState setWire() {
        state = StateUtil.setValue(state, WIRE_MASK);
        return this;
    }

    /**
     * Set the wire state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public ActorState setWire(final boolean value) {
        state = StateUtil.setValue(state, WIRE_MASK, value);
        return this;
    }

    /**
     * @return true if the disposed state value is set
     */
    public boolean isDisposed() {
        return StateUtil.isValue(state, DISPOSED_MASK);
    }

    /**
     * Set the disposed state value to true.
     *
     * @return this instance
     */
    public ActorState setDisposed() {
        state = StateUtil.setValue(state, DISPOSED_MASK);
        return this;
    }

    /**
     * Set the disposed state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public ActorState setDisposed(final boolean value) {
        state = StateUtil.setValue(state, DISPOSED_MASK, value);
        return this;
    }

    /**
     * @return true if the initialized state value is set
     */
    public boolean isInitialized() {
        return StateUtil.isValue(state, INITIALIZED_MASK);
    }

    /**
     * Set the initialized state value to true.
     *
     * @return this instance
     */
    public ActorState setInitialized() {
        state = StateUtil.setValue(state, INITIALIZED_MASK);
        return this;
    }

    /**
     * Set the initialized state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public ActorState setInitialized(final boolean value) {
        state = StateUtil.setValue(state, INITIALIZED_MASK, value);
        return this;
    }

    /**
     * @return true if the internalDirty state value is set
     */
    public boolean isInternalDirty() {
        return StateUtil.isValue(state, INTERNAL_DIRTY_MASK);
    }

    /**
     * Set the internalDirty state value to true.
     *
     * @return this instance
     */
    public ActorState setInternalDirty() {
        state = StateUtil.setValue(state, INTERNAL_DIRTY_MASK);
        return this;
    }

    /**
     * Set the internalDirty state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public ActorState setInternalDirty(final boolean value) {
        state = StateUtil.setValue(state, INTERNAL_DIRTY_MASK, value);
        return this;
    }

    /**
     * @return true if the extra state value is set
     */
    public boolean isExtra() {
        return StateUtil.isValue(state, EXTRA_MASK);
    }

    /**
     * Set the extra state value to true.
     *
     * @return this instance
     */
    public ActorState setExtra() {
        state = StateUtil.setValue(state, EXTRA_MASK);
        return this;
    }

    /**
     * Set the extra state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public ActorState setExtra(final boolean value) {
        state = StateUtil.setValue(state, EXTRA_MASK, value);
        return this;
    }
}
