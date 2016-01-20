package gov.pnnl.svf.actor;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.MathUtil;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.GeometryUtil;
import gov.pnnl.svf.util.Shape2DUtil;
import java.util.EnumSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.math.geometry.Vector3D;

/**
 * An actor that represents a horizontal scrollbar. Vertical scrollbar can be
 * created by rotating the actor. Minimum and maximum represent the total area.
 * The scrollbar knob represents the range from <code>(value) to (value +
 * extent)</code>. The minimum size of the rendered scroll bar knob will be a
 * square centered on the value. Interactions for the scrollbar need to be
 * implemented. Ensure that the knob is added to the picking support in order to
 * receive picking events for it. The instance must also be added as a listener
 * to item or color picking support.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractScrollbarActor extends AbstractActor implements ScrollbarActor {

    private static final double SQRT_2 = Math.sqrt(2.0);
    /**
     * The default zoom timer length.
     */
    private static final long DEFAULT_SCROLL_TIMER_LENGTH = 100L;
    /**
     * Timer used to wait for the scroll to settle before setting scrolling to
     * false.
     */
    private final ScrollTimer scrollTimer = new ScrollTimer(DEFAULT_SCROLL_TIMER_LENGTH);
    private final ScrollbarKnob knob;
    private Color foregroundColor = Color.LIGHT_GRAY;
    private Color backgroundColor;
    private double minimum = 0.0;
    private double maximum = 1000.0;
    private double extent = 100.0;
    private double width = 10.0;
    private double value = 0.0;
    private double scrollUnit = 10.0;
    /**
     * whether the mouse is currently being dragged
     */
    private boolean dragging = false;
    /**
     * whether the mouse is currently being scrolled
     */
    private boolean scrolling = false;
    /**
     * the last known or 'current' mouse location in the scene composite
     */
    private int currentMouseX = -1;
    /**
     * the last known or 'current' mouse location in the scene composite
     */
    private int currentMouseY = -1;
    /**
     * the location of the mouse the last time is was pressed
     */
    private int pressedMouseLocationX = -1;
    /**
     * the location of the mouse the last time is was pressed
     */
    private int pressedMouseLocationY = -1;

    /**
     * Default Constructor
     *
     * @param scene Reference to the scene that this actor belongs to. The scene
     *              will be added to the lookup.
     * @param type  The type of actor.
     * @param id    The unique id for this actor.
     *
     * @throws NullPointerException     if any parameters are null
     * @throws IllegalArgumentException if type or id are empty
     */
    protected AbstractScrollbarActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
        this.knob = new ScrollbarKnob(this);
    }

    /**
     * A reference to the scrollbar knob.
     *
     * @return the scrollbar knob
     */
    @Override
    public ScrollbarKnob getKnob() {
        return knob;
    }

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
    @Override
    public AbstractScrollbarActor setRange(final double minimum, final double maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException();
        }
        setMinimum(minimum);
        setMaximum(maximum);
        return this;
    }

    /**
     * This foreground color will override color support. The foregroundColor
     * can be null.
     *
     *
     * @return the foregroundColor
     */
    @Override
    public Color getForegroundColor() {
        synchronized (this) {
            return foregroundColor;
        }
    }

    /**
     * This foreground color will override color support. The foregroundColor
     * can be null.
     *
     * @param foregroundColor the foregroundColor to set
     *
     * @return this instance
     */
    @Override
    public AbstractScrollbarActor setForegroundColor(final Color foregroundColor) {
        final Color oldForegroundColor;
        synchronized (this) {
            oldForegroundColor = this.foregroundColor;
            this.foregroundColor = foregroundColor;
        }
        getPropertyChangeSupport().firePropertyChange(FOREGROUND_COLOR, oldForegroundColor, foregroundColor);
        return this;
    }

    /**
     * The background color will override color support. The background
     * foregroundColor can be null.
     *
     * @return the backgroundColor
     */
    @Override
    public Color getBackgroundColor() {
        synchronized (this) {
            return backgroundColor;
        }
    }

    /**
     * The background color will override color support. The background
     * foregroundColor can be null.
     *
     * @param backgroundColor the backgroundColor to set
     *
     * @return this instance
     */
    @Override
    public AbstractScrollbarActor setBackgroundColor(final Color backgroundColor) {
        final Color oldBackgroundColor;
        synchronized (this) {
            oldBackgroundColor = this.backgroundColor;
            this.backgroundColor = backgroundColor;
        }
        getPropertyChangeSupport().firePropertyChange(BACKGROUND_COLOR, oldBackgroundColor, backgroundColor);
        return this;
    }

    /**
     * The minimum value for the scrollbar.
     *
     * @return the minimum
     */
    @Override
    public double getMinimum() {
        synchronized (this) {
            return minimum;
        }
    }

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
    @Override
    public AbstractScrollbarActor setMinimum(final double minimum) {
        final double oldMinimum;
        final double oldExtent;
        double adjustedExtent;
        final double oldValue;
        double adjustedValue;
        synchronized (this) {
            if (minimum > this.maximum) {
                throw new IllegalArgumentException("minimum");
            }
            oldMinimum = this.minimum;
            this.minimum = minimum;
            // adjust extent
            oldExtent = this.extent;
            adjustedExtent = this.extent;
            adjustedExtent = Math.min(this.maximum - this.minimum, adjustedExtent);
            this.extent = adjustedExtent;
            // adjust value
            oldValue = this.value;
            adjustedValue = this.value;
            adjustedValue = Math.max(this.minimum, adjustedValue);
            adjustedValue = Math.min(this.maximum - this.extent, adjustedValue);
        }
        getPropertyChangeSupport().firePropertyChange(MINIMUM, oldMinimum, minimum);
        getPropertyChangeSupport().firePropertyChange(EXTENT, oldExtent, adjustedExtent);
        getPropertyChangeSupport().firePropertyChange(VALUE, oldValue, adjustedValue);
        return this;
    }

    /**
     * The maximum value for the scrollbar. Note that actual maximum is <code>(maximum
     * - extent)</code>.
     *
     * @return the maximum
     */
    @Override
    public double getMaximum() {
        synchronized (this) {
            return maximum;
        }
    }

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
    @Override
    public AbstractScrollbarActor setMaximum(final double maximum) {
        final double oldMaximum;
        final double oldExtent;
        double adjustedExtent;
        final double oldValue;
        double adjustedValue;
        synchronized (this) {
            if (maximum < this.minimum) {
                throw new IllegalArgumentException("maximum");
            }
            oldMaximum = this.maximum;
            this.maximum = maximum;
            // adjust extent
            oldExtent = this.extent;
            adjustedExtent = this.extent;
            adjustedExtent = Math.min(this.maximum - this.minimum, adjustedExtent);
            this.extent = adjustedExtent;
            // adjust value
            oldValue = this.value;
            adjustedValue = this.value;
            adjustedValue = Math.max(this.minimum, adjustedValue);
            adjustedValue = Math.min(this.maximum - this.extent, adjustedValue);
        }
        getPropertyChangeSupport().firePropertyChange(MAXIMUM, oldMaximum, maximum);
        getPropertyChangeSupport().firePropertyChange(EXTENT, oldExtent, adjustedExtent);
        getPropertyChangeSupport().firePropertyChange(VALUE, oldValue, adjustedValue);
        return this;
    }

    /**
     * The extent or visible area of the scrollbar.
     *
     * @return the extent
     */
    @Override
    public double getExtent() {
        synchronized (this) {
            return extent;
        }
    }

    /**
     * The extent or visible area of the scrollbar. The value and extent will be
     * adjusted accordingly if necessary.
     *
     * @param extent the extent
     *
     * @return this instance
     */
    @Override
    public AbstractScrollbarActor setExtent(final double extent) {
        final double oldExtent;
        double adjustedExtent = extent;
        final double oldValue;
        double adjustedValue;
        synchronized (this) {
            oldExtent = this.extent;
            adjustedExtent = Math.min(this.maximum - this.minimum, adjustedExtent);
            this.extent = adjustedExtent;
            // adjust value
            oldValue = this.value;
            adjustedValue = this.value;
            adjustedValue = Math.max(this.minimum, adjustedValue);
            adjustedValue = Math.min(this.maximum - this.extent, adjustedValue);
        }
        getPropertyChangeSupport().firePropertyChange(EXTENT, oldExtent, adjustedExtent);
        getPropertyChangeSupport().firePropertyChange(VALUE, oldValue, adjustedValue);
        return this;
    }

    /**
     * The width of the scrollbar.
     *
     * @return the width
     */
    @Override
    public double getWidth() {
        synchronized (this) {
            return width;
        }
    }

    /**
     * The width of the scrollbar.
     *
     * @param width the width
     *
     * @return this instance
     *
     * @throws IllegalArgumentException if width is less than 0.0
     */
    @Override
    public AbstractScrollbarActor setWidth(final double width) {
        if (width < 0.0) {
            throw new IllegalArgumentException("width");
        }
        final double old;
        synchronized (this) {
            old = this.width;
            this.width = width;
        }
        getPropertyChangeSupport().firePropertyChange(WIDTH, old, width);
        return this;
    }

    /**
     * The value of the scrollbar. The actual scrollbar will be drawn from the
     * <code>(value) to (value + extent)</code>.
     *
     * @return the value
     */
    @Override
    public double getValue() {
        synchronized (this) {
            return value;
        }
    }

    /**
     * The value of the scrollbar. The value will be adjusted accordingly if
     * necessary. The actual scrollbar will be drawn from the
     * <code>(value) to (value + extent)</code>.
     *
     * @param value the value
     *
     * @return this instance
     */
    @Override
    public AbstractScrollbarActor setValue(final double value) {
        final double old;
        double adjusted = value;
        synchronized (this) {
            old = this.value;
            adjusted = Math.max(this.minimum, adjusted);
            adjusted = Math.min(this.maximum - this.extent, adjusted);
            this.value = adjusted;
        }
        getPropertyChangeSupport().firePropertyChange(VALUE, old, adjusted);
        // set scrolling
        if (Double.compare(old, adjusted) != 0) {
            setScrolling(true);
        }
        return this;
    }

    /**
     * The amount the value changes per wheel click. Specifying 0.0 will disable
     * wheel scroll and negative numbers will invert it.
     *
     * @return the scroll unit
     */
    @Override
    public double getScrollUnit() {
        synchronized (this) {
            return scrollUnit;
        }
    }

    /**
     * The amount the value changes per wheel click. Specifying 0.0 will disable
     * wheel scroll and negative numbers will invert it.
     *
     * @param scrollUnit the scroll unit
     *
     * @return this instance
     */
    @Override
    public AbstractScrollbarActor setScrollUnit(final double scrollUnit) {
        final double old;
        synchronized (this) {
            old = this.scrollUnit;
            this.scrollUnit = scrollUnit;
        }
        getPropertyChangeSupport().firePropertyChange(SCROLL_UNIT, old, scrollUnit);
        return this;
    }

    /**
     * @return true if the camera is currently being dragged
     */
    @Override
    public boolean isDragging() {
        synchronized (this) {
            return dragging;
        }
    }

    /**
     * @param dragging true if the mouse is currently being dragged
     */
    void setDragging(final boolean dragging) {
        final boolean old;
        synchronized (this) {
            old = this.dragging;
            this.dragging = dragging;
        }
        getPropertyChangeSupport().firePropertyChange(DRAGGING, old, dragging);
    }

    /**
     * @return true if the mouse is currently being scrolled
     */
    @Override
    public boolean isScrolling() {
        synchronized (this) {
            return scrolling;
        }
    }

    /**
     * This should be set to true in subclasses. Subclasses should not set this
     * value to false, this is handled internally with a timer.
     *
     * @param scrolling true if the mouse is currently being scrolled
     */
    void setScrolling(final boolean scrolling) {
        final boolean old;
        synchronized (this) {
            old = this.scrolling;
            this.scrolling = scrolling;
            if (scrolling) {
                scrollTimer.schedule();
            }
        }
        getPropertyChangeSupport().firePropertyChange(SCROLLING, old, scrolling);
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // gather variables
        final Color foregroundColor;
        final Color backgroundColor;
        final Metrics metrics;
        synchronized (this) {
            foregroundColor = this.foregroundColor;
            backgroundColor = this.backgroundColor;
            metrics = createRectangles();
        }
        // background
        GeometryUtil.pushColor(gl, backgroundColor);
        drawBackground(gl, glu, metrics.getBackground());
        GeometryUtil.popColor(gl, backgroundColor);
        // foreground
        GeometryUtil.pushColor(gl, foregroundColor);
        drawForeground(gl, glu, metrics.getForeground());
        GeometryUtil.popColor(gl, foregroundColor);
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        final Metrics metrics = createRectangles();
        drawForeground(gl, glu, metrics.getForeground());
    }

    @Override
    public void itemPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event, final Object item) {
        final Metrics metrics = createRectangles();
        if (item == knob) {
            drawForeground(gl, glu, metrics.getForeground());
        } else if (item == this) {
            drawBackground(gl, glu, metrics.getBackground());
        }
    }

    @Override
    public void colorPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
        final Metrics metrics = createRectangles();
        final Color background = support.getMapping(this);
        if (background != null) {
            GeometryUtil.pushColor(gl, background);
            drawBackground(gl, glu, metrics.getBackground());
            GeometryUtil.popColor(gl, background);
        }
        final Color foreground = support.getMapping(knob);
        if (foreground != null) {
            GeometryUtil.pushColor(gl, foreground);
            drawForeground(gl, glu, metrics.getForeground());
            GeometryUtil.popColor(gl, foreground);
        }
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
    }

    @Override
    public void itemsPicked(final Actor actor, final Set<Object> items, final PickingCameraEvent event) {
        // knob has been picked
        if (items.contains(knob)) {
            if (event.getTypes().containsAll(EnumSet.of(CameraEventType.LEFT, CameraEventType.DOWN))) {
                pressed(event.getX(), event.getY());
            }
        }
        // area has been scrolled
        if (event.getTypes().contains(CameraEventType.WHEEL_UP)) {
            scrolled(-event.getClicks());
        } else if (event.getTypes().contains(CameraEventType.WHEEL_DOWN)) {
            scrolled(event.getClicks());
        }
    }

    /**
     * Draw the background of the scrollbar.
     *
     * @param gl   reference to current gl
     * @param glu  reference to shared glu
     * @param area the area for the background
     */
    protected void drawBackground(final GL2 gl, final GLUgl2 glu, final Rectangle2D area) {
        Shape2DUtil.drawShape(gl, area);
    }

    /**
     * Draw the foreground or knob of the scrollbar.
     *
     * @param gl   reference to current gl
     * @param glu  reference to shared glu
     * @param area the area for the foreground
     */
    protected void drawForeground(final GL2 gl, final GLUgl2 glu, final Rectangle2D area) {
        Shape2DUtil.drawShape(gl, area);
    }

    /**
     * Mouse button has been pressed. This should be called by the picking
     * support when the knob is picked.
     *
     * @param x the new x mouse coordinate
     * @param y the new y mouse coordinate
     */
    protected void pressed(final int x, final int y) {
        currentMouseX = x;
        currentMouseY = y;
        pressedMouseLocationX = x;
        pressedMouseLocationY = y;
        setDragging(true);
    }

    /**
     * Mouse button has been released. This should be called directly by the
     * mouse listener.
     */
    protected void released() {
        // the mouse is no longer being dragged
        currentMouseX = -1;
        currentMouseY = -1;
        pressedMouseLocationX = -1;
        pressedMouseLocationY = -1;
        setDragging(false);
    }

    /**
     * Mouse has exited the scene. This should be called directly by the mouse
     * listener.
     */
    protected void exited() {
        // we don't want to stop dragging when the mouse leaves the scene
//        // stop dragging when the mouse leaves the scene
//        currentMouseX = -1;
//        currentMouseY = -1;
//        pressedMouseLocationX = -1;
//        pressedMouseLocationY = -1;
//        setDragging(false);
    }

    /**
     * Mouse has been dragged. This should be called directly by the mouse
     * listener.
     *
     * @param x the new x mouse coordinate
     * @param y the new y mouse coordinate
     */
    protected void dragged(final int x, final int y) {
        moved(x, y);
    }

    /**
     * Mouse has been moved. This should be called directly by the mouse
     * listener.
     *
     * @param x the new x mouse coordinate
     * @param y the new y mouse coordinate
     */
    protected void moved(final int x, final int y) {
        // fill fields if they are null
        currentMouseX = currentMouseX == -1 ? x : currentMouseX;
        currentMouseY = currentMouseY == -1 ? y : currentMouseY;
        pressedMouseLocationX = pressedMouseLocationX == -1 ? x : pressedMouseLocationX;
        pressedMouseLocationY = pressedMouseLocationY == -1 ? y : pressedMouseLocationY;
        // get previous and current points
        final int previousX = currentMouseX;
        final int previousY = currentMouseY;
        currentMouseX = x;
        currentMouseY = y;
        if (isDragging()) {
            // get the amount the mouse has moved
            // notice that y value gets negated since opengl y is inverted from mouse
            final Vector3D vector = new Vector3D(previousX - currentMouseX, currentMouseY - previousY, 0.0);
            // find the orientation and project onto the user's mouse movement vector
            final Vector3D orientation = findOrientation();
            final double dot = Vector3D.dotProduct(orientation, vector);
            // gather variables
            final double minimum;
            final double maximum;
            final double extent;
            synchronized (this) {
                minimum = this.minimum;
                maximum = this.maximum;
                extent = this.extent;
            }
            // apply the movement
            final double value = MathUtil.scale(dot, extent, 0.0, maximum, minimum);
            setValue(getValue() + value);
        }
    }

    /**
     * Mouse wheel moved. This should be called while within the confines of the
     * area being scrolled.
     *
     * @param amount the number of wheel clicks
     */
    protected void scrolled(final int amount) {
        // scroll the scrollbar orientation so we can determine which way to scroll
        final Vector3D orientation = findOrientation();
        final double direction;
        // y axis dominant but rounding may cause very small y values to dominate a horizontal placement
        if (orientation.getX() == -1.0) {
            direction = -1.0;
        } else if (orientation.getX() == 1.0) {
            direction = 1.0;
        } else if (orientation.getY() < 0.0) {
            direction = -1.0;
        } else if (orientation.getY() > 0.0) {
            direction = 1.0;
        } else if (orientation.getX() < 0.0) {
            direction = -1.0;
        } else if (orientation.getX() > 0.0) {
            direction = 1.0;
        } else {
            // it has no orientation?
            direction = -1.0;
        }
        setValue(getValue() + (amount * getScrollUnit() * direction));
    }

    private Vector3D findOrientation() {
        // get the scrollbar's orientation
        Vector3D orientation = Vector3D.MINUS_J;
        final TransformSupport transform = lookup(TransformSupport.class);
        if (transform != null) {
            // get variables
            final Vector3D translation = transform.getTranslation();
            final double rotation = transform.getRotation();
            final Vector3D rotationAxis = transform.getRotationAxis();
            final Vector3D scale = transform.getScale();
            // create a transformation matrix
            final double[] matrix = MathUtil.newIdentityMatrix();
            MathUtil.translate(matrix, translation.getX(), translation.getY(), translation.getZ());
            MathUtil.rotate(matrix, rotation, rotationAxis.getX(), rotationAxis.getY(), rotationAxis.getZ());
            MathUtil.scale(matrix, scale.getX(), scale.getY(), scale.getZ());
            // transform the vector
            final double[] vector = new double[4];
            MathUtil.multiplyMatrixByVector(matrix, new double[]{orientation.getX(), orientation.getY(), orientation.getZ(), 0.0}, vector);
            orientation = new Vector3D(vector[0], vector[1], vector[2]);
        }
        return orientation;
    }

    /**
     * Creates the rectangles that represent the space for the background and
     * foreground.
     *
     * @return the rectangle metrics
     */
    protected Metrics createRectangles() {
        // gather variables
        final double minimum;
        final double maximum;
        final double extent;
        final double width;
        final double value;
        synchronized (this) {
            minimum = this.minimum;
            maximum = this.maximum;
            extent = this.extent;
            width = this.width;
            value = this.value;
        }
        // background
        final Rectangle2D background = new Rectangle2D(width, extent);
        // foreground
        final double y = MathUtil.scale(value, maximum, minimum, extent, 0.0);
        final double h = MathUtil.scale(extent, maximum, minimum, extent, 0.0);
        final Rectangle2D foreground = new Rectangle2D(0.0, y + (h * 0.5) - (extent * 0.5), width, Math.max(width, h));
        // return rectangles
        return new Metrics(foreground, background);
    }

    /**
     * Container that hold calculation values.
     */
    protected static class Metrics {

        private final Rectangle2D foreground;
        private final Rectangle2D background;

        /**
         * Constructor
         *
         * @param foreground the foreground
         * @param background the background
         */
        protected Metrics(final Rectangle2D foreground, final Rectangle2D background) {
            this.foreground = foreground;
            this.background = background;
        }

        /**
         * @return the foreground
         */
        protected Rectangle2D getForeground() {
            return foreground;
        }

        /**
         * @return the background
         */
        protected Rectangle2D getBackground() {
            return background;
        }

    }

    private class ScrollTimer extends Timer {

        protected final long scrollTimerLength;
        protected final AtomicLong lastScheduled = new AtomicLong(0L);

        private ScrollTimer(final long scrollTimerLength) {
            super("ScrollbarActor_ScrollTimer", true);
            this.scrollTimerLength = scrollTimerLength;
            scheduleAtFixedRate(new ZoomTimerTask(), 0L, scrollTimerLength / 4);
        }

        /**
         * Schedule this task for execution.
         */
        private void schedule() {
            lastScheduled.set(System.currentTimeMillis());
        }

        private class ZoomTimerTask extends TimerTask {

            @Override
            public void run() {
                if (System.currentTimeMillis() - lastScheduled.get() >= scrollTimerLength) {
                    setScrolling(false);
                }
            }
        }
    }

}
