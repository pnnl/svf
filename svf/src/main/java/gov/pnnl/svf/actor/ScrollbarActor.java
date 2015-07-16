package gov.pnnl.svf.actor;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.picking.ColorPickableActor;
import gov.pnnl.svf.picking.ColorPickingSupportListener;
import gov.pnnl.svf.picking.ItemPickableActor;
import gov.pnnl.svf.picking.ItemPickingSupportListener;
import gov.pnnl.svf.picking.PickableActor;

/**
 * Interface for all scrollbar actor implementations.
 *
 * @author Arthur Bleeker
 */
public interface ScrollbarActor extends Actor, PickableActor, ItemPickableActor, ColorPickableActor, ColorPickingSupportListener, ItemPickingSupportListener {

    /**
     * String representation of a field in this object.
     */
    String BACKGROUND_COLOR = "backgroundColor";
    /**
     * String representation of a field in this object.
     */
    String DRAGGING = "dragging";
    /**
     * String representation of a field in this object.
     */
    String EXTENT = "extent";
    /**
     * String representation of a field in this object.
     */
    String FOREGROUND_COLOR = "foregroundColor";
    /**
     * String representation of a field in this object.
     */
    String MAXIMUM = "maximum";
    /**
     * String representation of a field in this object.
     */
    String MINIMUM = "minimum";
    /**
     * String representation of a field in this object.
     */
    String SCROLLING = "scrolling";
    /**
     * String representation of a field in this object.
     */
    String SCROLL_UNIT = "scrollUnit";
    /**
     * String representation of a field in this object.
     */
    String VALUE = "value";
    /**
     * String representation of a field in this object.
     */
    String WIDTH = "width";

    /**
     * The background color will override color support. The background
     * foregroundColor can be null.
     *
     * @return the backgroundColor
     */
    Color getBackgroundColor();

    /**
     * The extent or visible area of the scrollbar.
     *
     * @return the extent
     */
    double getExtent();

    /**
     * This foreground color will override color support. The foregroundColor
     * can be null.
     *
     *
     * @return the foregroundColor
     */
    Color getForegroundColor();

    /**
     * A reference to the scrollbar knob.
     *
     * @return the scrollbar knob
     */
    ScrollbarKnob getKnob();

    /**
     * The maximum value for the scrollbar. Note that actual maximum is <code>(maximum
     * - extent)</code>.
     *
     * @return the maximum
     */
    double getMaximum();

    /**
     * The minimum value for the scrollbar.
     *
     * @return the minimum
     */
    double getMinimum();

    /**
     * The amount the value changes per wheel click. Specifying 0.0 will disable
     * wheel scroll and negative numbers will invert it.
     *
     * @return the scroll unit
     */
    double getScrollUnit();

    /**
     * The value of the scrollbar. The actual scrollbar will be drawn from the
     * <code>(value) to (value + extent)</code>.
     *
     * @return the value
     */
    double getValue();

    /**
     * The width of the scrollbar.
     *
     * @return the width
     */
    double getWidth();

    /**
     * @return true if the camera is currently being dragged
     */
    boolean isDragging();

    /**
     * @return true if the mouse is currently being scrolled
     */
    boolean isScrolling();

    /**
     * The background color will override color support. The background
     * foregroundColor can be null.
     *
     * @param backgroundColor the backgroundColor to set
     *
     * @return this instance
     */
    AbstractScrollbarActor setBackgroundColor(final Color backgroundColor);

    /**
     * The extent or visible area of the scrollbar. The value and extent will be
     * adjusted accordingly if necessary.
     *
     * @param extent the extent
     *
     * @return this instance
     */
    AbstractScrollbarActor setExtent(final double extent);

    /**
     * This foreground color will override color support. The foregroundColor
     * can be null.
     *
     * @param foregroundColor the foregroundColor to set
     *
     * @return this instance
     */
    AbstractScrollbarActor setForegroundColor(final Color foregroundColor);

    /**
     * The maximum value for the scrollbar. Note that actual maximum is (maximum
     * - extent). The value and extent will be adjusted accordingly if
     * necessary.
     *
     * @param maximum the maximum
     *
     * @return this instance
     *
     * @throws IllegalArgumentException if maximum is less than minimum
     */
    AbstractScrollbarActor setMaximum(final double maximum);

    /**
     * The minimum value for the scrollbar. The value and extent will be
     * adjusted accordingly if necessary.
     *
     * @param minimum the minimum
     *
     * @return this instance
     *
     * @throws IllegalArgumentException if minimum is greater than maximum
     */
    AbstractScrollbarActor setMinimum(final double minimum);

    /**
     * Convenience function for set minimum and set maximum. The value and
     * extent will be adjusted accordingly if necessary.
     *
     * @param minimum the minimum
     * @param maximum the maximum
     *
     * @return this instance
     *
     * @throws IllegalArgumentException if minimum is greater than maximum
     */
    AbstractScrollbarActor setRange(final double minimum, final double maximum);

    /**
     * The amount the value changes per wheel click. Specifying 0.0 will disable
     * wheel scroll and negative numbers will invert it.
     *
     * @param scrollUnit the scroll unit
     *
     * @return this instance
     */
    AbstractScrollbarActor setScrollUnit(final double scrollUnit);

    /**
     * The value of the scrollbar. The value will be adjusted accordingly if
     * necessary. The actual scrollbar will be drawn from the
     * <code>(value) to (value + extent)</code>.
     *
     * @param value the value
     *
     * @return this instance
     */
    AbstractScrollbarActor setValue(final double value);

    /**
     * The width of the scrollbar.
     *
     * @param width the width
     *
     * @return this instance
     *
     * @throws IllegalArgumentException if width is less than 0.0
     */
    AbstractScrollbarActor setWidth(final double width);

}
