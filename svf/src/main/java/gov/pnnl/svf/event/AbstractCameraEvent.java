package gov.pnnl.svf.event;

import gov.pnnl.svf.camera.Camera;
import java.util.Set;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Camera event.
 *
 * @param <C> the camera type the generates this event
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractCameraEvent<C extends Camera> {

    private final Set<CameraEventType> types;
    private final C source;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int context;
    private Vector3D location;

    /**
     * Constructor
     *
     * @param source  the source camera of the event
     * @param x       the x coord in screen space
     * @param y       the y coord in screen space
     * @param context the amount which can represent button or wheel clicks or a
     *                specific key
     * @param types   the types of events
     */
    protected AbstractCameraEvent(final C source, final int x, final int y, final int context, final Set<CameraEventType> types) {
        this(source, x, y, 1, 1, context, types);
    }

    /**
     * Constructor
     *
     * @param source  the source camera of the event
     * @param x       the x coord center in screen space
     * @param y       the y coord center in screen space
     * @param width   the width in screen space
     * @param height  the height in screen space
     * @param context the amount which can represent button or wheel clicks or a
     *                specific key
     * @param types   the types of events
     *
     * @throws IllegalArgumentException if the width or height is less than 1,
     *                                  or clicks is less than 0
     */
    protected AbstractCameraEvent(final C source, final int x, final int y, final int width, final int height, final int context, final Set<CameraEventType> types) {
        if (source == null) {
            throw new NullPointerException("source");
        }
        if (width < 1) {
            throw new IllegalArgumentException("width");
        }
        if (height < 1) {
            throw new IllegalArgumentException("height");
        }
        if (context < 0) {
            throw new IllegalArgumentException("context");
        }
        if (types == null) {
            throw new NullPointerException("types");
        }
        this.source = source;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.context = context;
        this.types = types;
    }

    /**
     * The source camera that generated the event.
     *
     * @return the source camera
     */
    public C getSource() {
        return source;
    }

    /**
     * A set of mouse buttons, keys, and modifiers for this event. The
     * collection returned from this method should never be modified.
     *
     * @return the event types
     */
    public Set<CameraEventType> getTypes() {
        return types;
    }

    /**
     * Represents the x coordinate of the center of the event in screen space.
     *
     * @return the x location of the event
     */
    public int getX() {
        return x;
    }

    /**
     * Represents the y coordinate of the center of the event in screen space.
     *
     * @return the y location of the event
     */
    public int getY() {
        return y;
    }

    /**
     * Represents the width of the event in screen space. This value will always
     * be greater than zero.
     *
     * @return the width of the event
     */
    public int getWidth() {
        return width;
    }

    /**
     * Represents the height of the event in screen space. This value will
     * always be greater than zero.
     *
     * @return the height of the event
     */
    public int getHeight() {
        return height;
    }

    /**
     * Represents the number of button or wheel clicks or a specific key. This
     * value will always be positive.
     *
     * @return the button or wheel clicks or key
     */
    public int getContext() {
        return context;
    }

    /**
     * This location is populated using GL unproject for raycast type picking
     * cameras. This value will only be accurate if depth testing is enabled for
     * the scene. Color picking cameras don't pick during an active GL context
     * and cannot populate this information.
     *
     * @param location the location of the event in scene coords
     */
    public void setLocation(final Vector3D location) {
        synchronized (this) {
            this.location = location;
        }
    }

    /**
     * This location is populated using GL unproject for raycast type picking
     * cameras. This value will only be accurate if depth testing is enabled for
     * the scene. Color picking cameras don't pick during an active GL context
     * and cannot populate this information.
     *
     * @return the location of the event in scene coords
     */
    public Vector3D getLocation() {
        synchronized (this) {
            return location;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 67 * hash + (this.types != null ? this.types.hashCode() : 0);
        hash = 67 * hash + this.x;
        hash = 67 * hash + this.y;
        hash = 67 * hash + this.width;
        hash = 67 * hash + this.height;
        hash = 67 * hash + this.context;
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractCameraEvent<?> other = (AbstractCameraEvent<?>) obj;
        if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) {
            return false;
        }
        if (this.types != other.types && (this.types == null || !this.types.equals(other.types))) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.width != other.width) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        if (this.context != other.context) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PickEvent{" + "source=" + source + ", types=" + types + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", context=" + context + '}';
    }
}
