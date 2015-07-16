package gov.pnnl.svf.shape;

/**
 * Shape used to represent a Text area
 *
 * @author Peter Nordquist
 */
public interface TextAreaShape extends TextShape {

    /**
     * String representation of a field in this object.
     */
    String WRAP = "wrap";

    /**
     * The behavior for either wrapping the text or abbreviating it.
     *
     * @return the current value for if we are wrapping the text
     */
    boolean isWrap();

    /**
     * Sets the behavior for either wrapping the text or abbreviating it.
     *
     * @param wrap true to wrap, false to abbreviate
     */
    void setWrap(boolean wrap);
}
