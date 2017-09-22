package gov.pnnl.svf.camera;

import gov.pnnl.svf.scene.Scene;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;

/**
 * A simple camera that allows movement within a scene. This class should be
 * overridden in order to implement user interaction with the camera.
 *
 * @author Arthur Bleeker
 *
 */
public class SimpleCamera extends AbstractCamera implements DrawingCamera {

    /**
     * String representation of a field in this object.
     */
    public static final String MAX_ROTATE = "maxRotate";
    /**
     * String representation of a field in this object.
     */
    public static final String IGNORE_VIEWPORT = "ignoreViewport";
    private static final double PI_OVER_2 = Math.PI / 2.0f;
    private static final double PI = Math.PI;
    // maximum amount of up and down rotation allowed
    private double maxRotate = 1.0;
    private boolean ignoreViewport = false;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public SimpleCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public SimpleCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public SimpleCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    /**
     * The default is false and the camera will only respond to mouse events
     * contained within it's viewport.
     *
     * @return true if this camera should ignore the viewport and respond to all
     *         mouse events
     */
    public boolean isIgnoreViewport() {
        synchronized (this) {
            return ignoreViewport;
        }
    }

    /**
     * The default is false and the camera will only respond to mouse events
     * contained within it's viewport.
     *
     * @param ignoreViewport true if this camera should ignore the viewport and
     *                       respond to all mouse events
     *
     * @return this instance
     */
    public SimpleCamera setIgnoreViewport(final boolean ignoreViewport) {
        final boolean old;
        synchronized (this) {
            old = this.ignoreViewport;
            this.ignoreViewport = ignoreViewport;
        }
        getPropertyChangeSupport().firePropertyChange(IGNORE_VIEWPORT, old, ignoreViewport);
        return this;
    }

    /**
     * The maximum amount of rotation that is allowed for moving the camera up
     * and down. 0 is no rotation 1 is full rotation.
     *
     * @return the max rotate
     */
    public double getMaxRotate() {
        synchronized (this) {
            return maxRotate;
        }
    }

    /**
     * Move camera backward
     *
     * @param amount
     */
    public void moveBackward(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getLook());
        setLocation(location.subtract(vector));
    }

    /**
     * Move camera down
     *
     * @param amount
     */
    public void moveDown(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getCameraUp());
        setLocation(location.subtract(vector));
    }

    /**
     * Move camera forward
     *
     * @param amount
     */
    public void moveForward(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getLook());
        setLocation(location.add(vector));
    }

    /**
     * Move camera left
     *
     * @param amount
     */
    public void moveLeft(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getPerp());
        setLocation(location.add(vector));
    }

    /**
     * Move camera right
     *
     * @param amount
     */
    public void moveRight(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getPerp());
        setLocation(location.subtract(vector));
    }

    /**
     * Move camera up
     *
     * @param amount
     */
    public void moveUp(final double amount) {
        // get the current location
        final Vector3D location = getLocation();
        final Vector3D vector = new Vector3D(amount, getCameraUp());
        setLocation(location.add(vector));
    }

    /**
     * Rotate camera down.
     *
     * @param amount
     */
    public void rotateDown(final double amount) {
        rotateUpDown(amount);

    }

    /**
     * Rotate camera to the left.
     *
     * @param amount
     */
    public void rotateLeft(final double amount) {
        rotateLeftRight(amount);
    }

    /**
     * Rotate camera to the right.
     *
     * @param amount
     */
    public void rotateRight(final double amount) {
        rotateLeftRight(-amount);
    }

    /**
     * Rotate camera up.
     *
     * @param amount
     */
    public void rotateUp(final double amount) {
        rotateUpDown(-amount);
    }

    private void rotateLeftRight(final double amount) {
        // use quaternion rotation to avoid gimbal lock and keep the rotation
        // smooth
        final Rotation rotation = new Rotation(getCameraUp(), amount);
        setLook(rotation.applyTo(getLook()));
    }

    private void rotateUpDown(final double amount) {
        // orbit the camera around the target
        final double angle = Math.acos(Vector3D.dotProduct(getUp(), getLook()));
        final double upperAngle = PI_OVER_2 - (PI_OVER_2 * getMaxRotate());
        final double lowerAngle = PI - upperAngle;
        // restrict the movement of the camera in the up and down direction
        // to prevent the camera from going upside down
        if (((amount < 0.0) && (angle > upperAngle)) || ((amount > 0.0f) && (angle < lowerAngle))) {
            double adjusted = amount;
            // downward camera movement
            if ((amount > 0.0f) && ((lowerAngle - angle) < amount)) {
                adjusted = lowerAngle - angle;
            }
            // upward camera movement
            if ((amount < 0.0f) && ((angle - upperAngle) < -amount)) {
                adjusted = -(angle - upperAngle);
            }
            // use quaternion rotation to avoid gimbal lock and keep the
            // rotation smooth
            final Rotation rotation = new Rotation(getPerp(), adjusted);
            setLook(rotation.applyTo(getLook()));
        }
    }

    /**
     * The maximum amount of rotation that is allowed for moving the camera up
     * and down. 0 is no rotation 1 is full rotation.
     *
     * @param maxRotate the max rotate
     *
     * @return this instance
     */
    public SimpleCamera setMaxRotate(final double maxRotate) {
        if ((maxRotate < 0.0) || (maxRotate > 1.0)) {
            throw new IllegalArgumentException("maxRotate");
        }
        final double old;
        synchronized (this) {
            old = this.maxRotate;
            this.maxRotate = maxRotate;
        }
        getPropertyChangeSupport().firePropertyChange(MAX_ROTATE, old, maxRotate);
        return this;
    }
}
