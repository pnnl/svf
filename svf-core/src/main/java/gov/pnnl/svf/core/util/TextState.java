package gov.pnnl.svf.core.util;

/**
 * Support for utilizing state values to indicate current text state. This class
 * is not thread safe.
 *
 * @author Amelia Bleeker
 */
public class TextState extends AbstractState {

    private static final byte DIRTY_MASK = StateUtil.getMasks()[0];
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[1];
    private static final byte INITIALIZED_MASK = StateUtil.getMasks()[2];
    private static final byte INTERNAL_DIRTY_MASK = StateUtil.getMasks()[3];
    private static final byte ANTI_ALIASED_MASK = StateUtil.getMasks()[4];
    private static final byte MIP_MAPS_MASK = StateUtil.getMasks()[5];
    private static final byte FRACTIONAL_MASK = StateUtil.getMasks()[6];
    private static final byte SMOOTHING_MASK = StateUtil.getMasks()[7];
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public TextState() {
    }

    /**
     * @return true if the anti aliased state value is set
     */
    public boolean isAntiAliased() {
        return StateUtil.isValue(state, ANTI_ALIASED_MASK);
    }

    /**
     * Set the anti aliased state value to true.
     *
     * @return this instance
     */
    public TextState setAntiAliased() {
        state = StateUtil.setValue(state, ANTI_ALIASED_MASK);
        return this;
    }

    /**
     * Set the anti aliased state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public TextState setAntiAliased(final boolean value) {
        state = StateUtil.setValue(state, ANTI_ALIASED_MASK, value);
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
    public TextState setDirty() {
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
    public TextState setDirty(final boolean value) {
        state = StateUtil.setValue(state, DIRTY_MASK, value);
        return this;
    }

    /**
     * @return true if the mip maps state value is set
     */
    public boolean isMipMaps() {
        return StateUtil.isValue(state, MIP_MAPS_MASK);
    }

    /**
     * Set the mip maps state value to true.
     *
     * @return this instance
     */
    public TextState setMipMaps() {
        state = StateUtil.setValue(state, MIP_MAPS_MASK);
        return this;
    }

    /**
     * Set the mip maps state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public TextState setMipMaps(final boolean value) {
        state = StateUtil.setValue(state, MIP_MAPS_MASK, value);
        return this;
    }

    /**
     * @return true if the fractional state value is set
     */
    public boolean isFractional() {
        return StateUtil.isValue(state, FRACTIONAL_MASK);
    }

    /**
     * Set the fractional state value to true.
     *
     * @return this instance
     */
    public TextState setFractional() {
        state = StateUtil.setValue(state, FRACTIONAL_MASK);
        return this;
    }

    /**
     * Set the fractional state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public TextState setFractional(final boolean value) {
        state = StateUtil.setValue(state, FRACTIONAL_MASK, value);
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
    public TextState setDisposed() {
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
    public TextState setDisposed(final boolean value) {
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
    public TextState setInitialized() {
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
    public TextState setInitialized(final boolean value) {
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
    public TextState setInternalDirty() {
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
    public TextState setInternalDirty(final boolean value) {
        state = StateUtil.setValue(state, INTERNAL_DIRTY_MASK, value);
        return this;
    }

    /**
     * @return true if the smoothing state value is set
     */
    public boolean isSmoothing() {
        return StateUtil.isValue(state, SMOOTHING_MASK);
    }

    /**
     * Set the smoothing state value to true.
     *
     * @return this instance
     */
    public TextState setSmoothing() {
        state = StateUtil.setValue(state, SMOOTHING_MASK);
        return this;
    }

    /**
     * Set the smoothing state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public TextState setSmoothing(final boolean value) {
        state = StateUtil.setValue(state, SMOOTHING_MASK, value);
        return this;
    }
}
