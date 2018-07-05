package gov.pnnl.svf.core.util;

/**
 * Support for utilizing state values to indicate current draw state. This class
 * is not thread safe.
 *
 * @author Amelia Bleeker
 */
public class DrawState extends AbstractState {

    private static final byte ATTRIB_MASK = StateUtil.getMasks()[0];
    private static final byte MATRIX_TEXTURE_MASK = StateUtil.getMasks()[1];
    private static final byte MATRIX_MODELVIEW_MASK = StateUtil.getMasks()[2];
    private static final byte MATRIX_PROJECTION_MASK = StateUtil.getMasks()[3];
    private static final byte RENDERED_MASK = StateUtil.getMasks()[4];
    private static final byte EXTRA_A_MASK = StateUtil.getMasks()[5];
    private static final byte EXTRA_B_MASK = StateUtil.getMasks()[6];
    private static final byte EXTRA_C_MASK = StateUtil.getMasks()[7];
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public DrawState() {
    }

    /**
     * @return true if the rendered state value is set
     */
    public boolean isRendered() {
        return StateUtil.isValue(state, RENDERED_MASK);
    }

    /**
     * @return true if the attrib state value is set
     */
    public boolean isAttrib() {
        return StateUtil.isValue(state, ATTRIB_MASK);
    }

    /**
     * @return true if the matrix model view state value is set
     */
    public boolean isMatrixModelview() {
        return StateUtil.isValue(state, MATRIX_MODELVIEW_MASK);
    }

    /**
     * @return true if the matrix projection state value is set
     */
    public boolean isMatrixProjection() {
        return StateUtil.isValue(state, MATRIX_PROJECTION_MASK);
    }

    /**
     * @return true if the matrix texture state value is set
     */
    public boolean isMatrixTexture() {
        return StateUtil.isValue(state, MATRIX_TEXTURE_MASK);
    }

    /**
     * @return true if the extra A state value is set
     */
    public boolean isExtraA() {
        return StateUtil.isValue(state, EXTRA_A_MASK);
    }

    /**
     * @return true if the extra B state value is set
     */
    public boolean isExtraB() {
        return StateUtil.isValue(state, EXTRA_B_MASK);
    }

    /**
     * @return true if the extra C state value is set
     */
    public boolean isExtraC() {
        return StateUtil.isValue(state, EXTRA_C_MASK);
    }

    /**
     * Set the rendered state value to true.
     *
     * @return this instance
     */
    public DrawState setRendered() {
        state = StateUtil.setValue(state, RENDERED_MASK);
        return this;
    }

    /**
     * Set the attrib state value to true.
     *
     * @return this instance
     */
    public DrawState setAttrib() {
        state = StateUtil.setValue(state, ATTRIB_MASK);
        return this;
    }

    /**
     * Set the matrix model view state value to true.
     *
     * @return this instance
     */
    public DrawState setMatrixModelview() {
        state = StateUtil.setValue(state, MATRIX_MODELVIEW_MASK);
        return this;
    }

    /**
     * Set the matrix projection state value to true.
     *
     * @return this instance
     */
    public DrawState setMatrixProjection() {
        state = StateUtil.setValue(state, MATRIX_PROJECTION_MASK);
        return this;
    }

    /**
     * Set the matrix texture state value to true.
     *
     * @return this instance
     */
    public DrawState setMatrixTexture() {
        state = StateUtil.setValue(state, MATRIX_TEXTURE_MASK);
        return this;
    }

    /**
     * Set the extra A state value to true.
     *
     * @return this instance
     */
    public DrawState setExtraA() {
        state = StateUtil.setValue(state, EXTRA_A_MASK);
        return this;
    }

    /**
     * Set the extra B state value to true.
     *
     * @return this instance
     */
    public DrawState setExtraB() {
        state = StateUtil.setValue(state, EXTRA_B_MASK);
        return this;
    }

    /**
     * Set the extra C state value to true.
     *
     * @return this instance
     */
    public DrawState setExtraC() {
        state = StateUtil.setValue(state, EXTRA_C_MASK);
        return this;
    }

    /**
     * Set the rendered state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public DrawState setRendered(final boolean value) {
        state = StateUtil.setValue(state, RENDERED_MASK, value);
        return this;
    }

    /**
     * Set the attrib state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public DrawState setAttrib(final boolean value) {
        state = StateUtil.setValue(state, ATTRIB_MASK, value);
        return this;
    }

    /**
     * Set the matrix model view state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public DrawState setMatrixModelview(final boolean value) {
        state = StateUtil.setValue(state, MATRIX_MODELVIEW_MASK, value);
        return this;
    }

    /**
     * Set the matrix projection state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public DrawState setMatrixProjection(final boolean value) {
        state = StateUtil.setValue(state, MATRIX_PROJECTION_MASK, value);
        return this;
    }

    /**
     * Set the matrix texture state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public DrawState setMatrixTexture(final boolean value) {
        state = StateUtil.setValue(state, MATRIX_TEXTURE_MASK, value);
        return this;
    }

    /**
     * Set the extra A state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public DrawState setExtraA(final boolean value) {
        state = StateUtil.setValue(state, EXTRA_A_MASK, value);
        return this;
    }

    /**
     * Set the extra B state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public DrawState setExtraB(final boolean value) {
        state = StateUtil.setValue(state, EXTRA_B_MASK, value);
        return this;
    }

    /**
     * Set the extra C state value to true.
     *
     * @param value true to set the value, false to unset
     *
     * @return this instance
     */
    public DrawState setExtraC(final boolean value) {
        state = StateUtil.setValue(state, EXTRA_C_MASK, value);
        return this;
    }
}
