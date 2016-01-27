package gov.pnnl.svf.camera;

import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.scene.Scene;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Camera used to orbit around a target in scene space.
 *
 * @author Arthur Bleeker
 */
public class OrbitCamera extends SimpleCamera {

    /**
     * String representation of a field in this object.
     */
    public static final String TARGET = "target";
    /**
     * String representation of a field in this object.
     */
    public static final String MOVE_MULTIPLIER = "moveMultiplier";
    /**
     * String representation of a field in this object.
     */
    public static final String ZOOM_MULTIPLIER = "zoomMultiplier";
    /**
     * String representation of a field in this object.
     */
    public static final String ROTATE_MULTIPLIER = "rotateMultiplier";
    private Vector3D target;
    private float moveMultiplier = 1.0f;
    private float zoomMultiplier = 1.0f;
    private float rotateMultiplier = 1.0f;
    /**
     * scaling factor for moving the camera
     */
    protected static final float MOVE_FACTOR = 0.04f;
    /**
     * scaling factor for zooming the camera
     */
    protected static final float ZOOM_FACTOR = 0.1f;
    /**
     * scaling factor for rotating the camera
     */
    protected static final float ROTATE_FACTOR = 0.04f;
    /**
     * scaling factor for dragging the camera
     */
    protected static final float DRAG_FACTOR = 0.004f;
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
     * the button that was pressed the last time
     */
    private CameraEventType pressedMouseButton = CameraEventType.NONE;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public OrbitCamera(final Scene scene) {
        super(scene);
        target = Vector3D.ZERO;
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public OrbitCamera(final Scene scene, final String id) {
        super(scene, id);
        target = Vector3D.ZERO;
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public OrbitCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
        target = Vector3D.ZERO;
    }

    /**
     * @return the scalar used to modify the move value
     */
    public float getMoveMultiplier() {
        synchronized (this) {
            return moveMultiplier;
        }
    }

    /**
     * @return the scalar used to modify the rotate value
     */
    public float getRotateMultiplier() {
        synchronized (this) {
            return rotateMultiplier;
        }
    }

    /**
     *
     * @return the target vector
     */
    public Vector3D getTarget() {
        synchronized (this) {
            return target;
        }
    }

    /**
     * @return the scalar used to modify the zoom value
     */
    public float getZoomMultiplier() {
        synchronized (this) {
            return zoomMultiplier;
        }
    }

    /**
     * Mouse button has been pressed.
     *
     * @param x      the new x mouse coordinate
     * @param y      the new y mouse coordinate
     * @param button the mouse button
     */
    protected void pressed(final int x, final int y, final CameraEventType button) {
        if (!isIgnoreViewport()) {
            final Rectangle viewport = getViewport();
            final Rectangle sceneViewport = getScene().getViewport();
            final double multiplier = getCanvasMultiplier();
            if (!viewport.contains((int) (x * multiplier), sceneViewport.getHeight() - (int) (y * multiplier))) {
                return;
            }
        }
        currentMouseX = x;
        currentMouseY = y;
        pressedMouseLocationX = x;
        pressedMouseLocationY = y;
        pressedMouseButton = button;
    }

    /**
     * Mouse button has been released.
     */
    protected void released() {
        // the camera is no longer being dragged or zoomed
        currentMouseX = -1;
        currentMouseY = -1;
        pressedMouseLocationX = -1;
        pressedMouseLocationY = -1;
        pressedMouseButton = CameraEventType.NONE;
    }

    /**
     * Mouse has exited the scene.
     */
    protected void exited() {
        // stop dragging when the mouse leaves the scene
        currentMouseX = -1;
        currentMouseY = -1;
        pressedMouseLocationX = -1;
        pressedMouseLocationY = -1;
        pressedMouseButton = CameraEventType.NONE;
    }

    /**
     * Mouse has been dragged.
     *
     * @param x the new x mouse coordinate
     * @param y the new y mouse coordinate
     */
    protected void dragged(final int x, final int y) {
        if (!isIgnoreViewport()) {
            final Rectangle viewport = getViewport();
            final Rectangle sceneViewport = getScene().getViewport();
            final double multiplier = getCanvasMultiplier();
            if (!viewport.contains((int) (x * multiplier), sceneViewport.getHeight() - (int) (y * multiplier))) {
                return;
            }
        }
        // the camera is being dragged
        moved(x, y);
    }

    /**
     * Mouse has been moved.
     *
     * @param x the new x mouse coordinate
     * @param y the new y mouse coordinate
     */
    protected void moved(final int x, final int y) {
        final Rectangle viewport = getViewport();
        final Rectangle sceneViewport = getScene().getViewport();
        final double multiplier = getCanvasMultiplier();
        if (!isIgnoreViewport() && !viewport.contains((int) (x * multiplier), sceneViewport.getHeight() - (int) (y * multiplier))) {
            return;
        }
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
        // get the distance dragged and set the current x and y position
        final Vector3D dragged = new Vector3D(currentMouseX - previousX, currentMouseY - previousY, 0.0);
        // left mouse button
        if (pressedMouseButton == CameraEventType.LEFT) {
            // orbit
            orbitUpDown((float) dragged.getY() * ROTATE_FACTOR * getRotateMultiplier());
            orbitLeftRight((float) dragged.getX() * -ROTATE_FACTOR * getRotateMultiplier());
        }
        // right mouse button
        if (pressedMouseButton == CameraEventType.RIGHT) {
            zoomInOut((float) dragged.getY() * ZOOM_FACTOR * getZoomMultiplier());
        }
    }

    /**
     * Mouse wheel moved.
     *
     * @param x      the new x mouse coordinate
     * @param y      the new y mouse coordinate
     * @param amount the number of wheel clicks
     */
    protected void scrolled(final int x, final int y, final int amount) {
        final Rectangle viewport = getViewport();
        final Rectangle sceneViewport = getScene().getViewport();
        final double multiplier = getCanvasMultiplier();
        if (!isIgnoreViewport() && !viewport.contains((int) (x * multiplier), sceneViewport.getHeight() - (int) (y * multiplier))) {
            return;
        }
        zoomInOut(amount * getZoomMultiplier());
    }

    @Override
    public void moveBackward(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getLook());
        setLocation(location.subtract(vector));
        final Vector3D translated = getTarget().subtract(vector);
        setTarget(translated);
    }

    @Override
    public void moveDown(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getCameraUp());
        setLocation(location.subtract(vector));
        final Vector3D translated = getTarget().subtract(vector);
        setTarget(translated);
    }

    @Override
    public void moveForward(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getLook());
        setLocation(location.add(vector));
        final Vector3D translated = getTarget().add(vector);
        setTarget(translated);
    }

    @Override
    public void moveLeft(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getPerp());
        setLocation(location.add(vector));
        final Vector3D translated = getTarget().add(vector);
        setTarget(translated);
    }

    @Override
    public void moveRight(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getPerp());
        setLocation(location.subtract(vector));
        final Vector3D translated = getTarget().subtract(vector);
        setTarget(translated);
    }

    @Override
    public void moveUp(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getCameraUp());
        setLocation(location.add(vector));
        final Vector3D translated = getTarget().add(vector);
        setTarget(translated);
    }

    /**
     * Orbit the camera left or right.
     *
     * @param amount the amount to orbit
     */
    public void orbitLeftRight(final float amount) {
        if (Float.compare(amount, 0.0f) == 0) {
            return;
        }
        // get the current location
        final Vector3D location = getLocation();
        // use quaternion rotation to avoid gimbal lock and keep the
        // rotation smooth
        final Rotation rotation = new Rotation(getUp(), amount);
        // create an orbit vector for setting the new location
        synchronized (this) {
            final Vector3D temp = location.subtract(target);
            setLocation(rotation.applyTo(temp).add(target));
        }
        update(0L);
    }

    /**
     * Orbit the camera up or down.
     *
     * @param amount the amount to orbit
     */
    public void orbitUpDown(final float amount) {
        if (Float.compare(amount, 0.0f) == 0) {
            return;
        }
        // get the current location
        final Vector3D location = getLocation();
        // orbit the camera around the target
        final double angle = Math.acos(Vector3D.dotProduct(getUp(), getLook()));
        final double upperAngle = (Math.PI / 2.0) - ((Math.PI / 2.0) * getMaxRotate());
        final double lowerAngle = Math.PI - upperAngle;
        // restrict the movement of the camera in the up and down direction
        // to prevent the camera from going upside down
        if (((Double.compare(amount, 0.0f) < 0) && (Double.compare(angle, upperAngle) > 0))
            || ((Double.compare(amount, 0.0f) > 0) && (Double.compare(angle, lowerAngle) < 0))) {
            double adjusted = amount;
            // downward camera movement
            if ((amount > 0.0) && ((lowerAngle - angle) < amount)) {
                adjusted = lowerAngle - angle;
            }
            // upward camera movement
            if ((amount < 0.0) && ((angle - upperAngle) < -amount)) {
                adjusted = -(angle - upperAngle);
            }
            // use quaternion rotation to avoid gimbal lock and keep the
            // rotation smooth
            final Rotation rotation = new Rotation(getPerp(), adjusted);
            // create an orbit vector for setting the new location
            synchronized (this) {
                final Vector3D temp = location.subtract(target);
                setLocation(rotation.applyTo(temp).add(target));
            }
            update(0L);
        }
    }

    /**
     * @param moveMultiplier the scalar used to modify the move value
     *
     * @return this instance
     */
    public OrbitCamera setMoveMultiplier(final float moveMultiplier) {
        final float old;
        final float current;
        synchronized (this) {
            old = this.moveMultiplier;
            if (Float.compare(moveMultiplier, 0.0f) < 0) {
                this.moveMultiplier = 0.0f;
            } else {
                this.moveMultiplier = moveMultiplier;
            }
            current = this.moveMultiplier;
        }
        getPropertyChangeSupport().firePropertyChange(MOVE_MULTIPLIER, old, current);
        return this;
    }

    /**
     * @param rotateMultiplier the scalar used to modify the rotate value
     *
     * @return this instance
     */
    public OrbitCamera setRotateMultiplier(final float rotateMultiplier) {
        final float old;
        final float current;
        synchronized (this) {
            old = this.rotateMultiplier;
            if (Float.compare(rotateMultiplier, 0.0f) < 0) {
                this.rotateMultiplier = 0.0f;
            } else {
                this.rotateMultiplier = rotateMultiplier;
            }
            current = this.rotateMultiplier;
        }
        getPropertyChangeSupport().firePropertyChange(ROTATE_MULTIPLIER, old, current);
        return this;
    }

    /**
     * The look at and orbit target.
     *
     * @param target the target
     *
     * @return this instance
     */
    public OrbitCamera setTarget(final Vector3D target) {
        if (target == null) {
            throw new NullPointerException("target");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.target;
            this.target = target;
        }
        getPropertyChangeSupport().firePropertyChange(TARGET, old, target);
        update(0L);
        return this;
    }

    /**
     * @param zoomMultiplier the scalar used to modify the zoom value
     *
     * @return this instance
     */
    public OrbitCamera setZoomMultiplier(final float zoomMultiplier) {
        final float old;
        final float current;
        synchronized (this) {
            old = this.zoomMultiplier;
            if (zoomMultiplier < 0.0f) {
                this.zoomMultiplier = 0.0f;
            } else {
                this.zoomMultiplier = zoomMultiplier;
            }
            current = this.zoomMultiplier;
        }
        getPropertyChangeSupport().firePropertyChange(ZOOM_MULTIPLIER, old, current);
        return this;
    }

    @Override
    public void update(final long delta) {
        // get the current location
        final Vector3D location = getLocation();
        synchronized (this) {
            final Vector3D diff = location.subtract(target);
            // modify it to create the new look vector
            final Vector3D look = new Vector3D(-1.0, diff);
            setLook(look);
        }
        super.update(delta);
    }

    /**
     * Zoom the camera in towards or away from the target.
     *
     * @param amount amount to zoom
     */
    public void zoomInOut(final float amount) {
        if (amount == 0.0f) {
            return;
        }
        // get the current location
        final Vector3D location = getLocation();
        synchronized (this) {
            final double distance = Vector3D.distance(location, target);
            if ((amount < 0.0f) && (distance < Math.abs(amount))) {
                final Vector3D vector = new Vector3D(distance, getLook());
                setLocation(location.subtract(vector));
            } else {
                final Vector3D vector = new Vector3D(amount, getLook());
                setLocation(location.subtract(vector));
            }
        }
    }
}
