package gov.pnnl.svf.camera;

import gov.pnnl.svf.core.util.MathUtil;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.scene.Scene;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Camera used to view a 3d scene from one direction and allow the user to drag
 * the camera on the x/y plane using the mouse.
 *
 * @author Arthur Bleeker
 * @author Shawn Hampton
 */
public class DraggingCamera extends SimpleCamera {

    /**
     * String representation of a field in this object.
     */
    public static final String DRAGGING = "dragging";
    /**
     * String representation of a field in this object.
     */
    public static final String ZOOMING = "zooming";
    /**
     * String representation of a field in this object.
     */
    public static final String ZOOM_TO_MOUSE = "zoomToMouse";
    /**
     * String representation of a field in this object.
     */
    public static final String DRAG_MULTIPLIER = "dragMultiplier";
    /**
     * String representation of a field in this object.
     */
    public static final String ZOOM_MULTIPLIER = "zoomMultiplier";
    /**
     * String representation of a field in this object.
     */
    public static final String ZOOM_MIN = "zoomMin";
    /**
     * String representation of a field in this object.
     */
    public static final String ZOOM_MAX = "zoomMax";
    /**
     * The default zoom timer length.
     */
    public static final long DEFAULT_ZOOM_TIMER_LENGTH = 100L;
    /**
     * Timer used to wait for the zoom to settle before setting zooming to
     * false.
     */
    private ZoomTimer zoomTimer = new ZoomTimer(DEFAULT_ZOOM_TIMER_LENGTH);
    /**
     * whether the camera is currently being dragged
     */
    private boolean dragging = false;
    /**
     * whether the camera is currently being zoomed
     */
    private boolean zooming = false;
    private boolean zoomToMouse = true;
    private float dragMultiplier = 0.06f;
    private float zoomMultiplier = 0.5f;
    private float zoomMin = 0.0f;
    private float zoomMax = 100.0f;
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
    public DraggingCamera(final Scene scene) {
        super(scene);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param id    The unique id for this actor.
     */
    public DraggingCamera(final Scene scene, final String id) {
        super(scene, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public DraggingCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void dispose() {
        super.dispose();
        synchronized (this) {
            zoomTimer.cancel();
        }
    }

    /**
     * @param zoomTimerLength the amount of time to wait for zoom to settle
     *                        before setting zooming to false
     *
     * @return this instance
     */
    public DraggingCamera setZoomTimerLength(final long zoomTimerLength) {
        if (zoomTimerLength < 0) {
            throw new IllegalArgumentException("zoomTimerLength");
        }
        synchronized (this) {
            setZooming(false);
            zoomTimer.cancel();
            zoomTimer = new ZoomTimer(zoomTimerLength);
        }
        return this;
    }

    /**
     * @return true if the camera is currently being dragged
     */
    public boolean isDragging() {
        synchronized (this) {
            return dragging;
        }
    }

    /**
     * @param dragging true if this camera is currently being dragged
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
     * @return true if the camera is currently being zoomed
     */
    public boolean isZooming() {
        synchronized (this) {
            return zooming;
        }
    }

    /**
     * This should be set to true in subclasses. Subclasses should not set this
     * value to false, this is handled internally with a timer.
     *
     * @param zooming true if this camera is currently being zoomed
     */
    void setZooming(final boolean zooming) {
        final boolean old;
        synchronized (this) {
            old = this.zooming;
            this.zooming = zooming;
            if (zooming) {
                zoomTimer.schedule();
            }
        }
        getPropertyChangeSupport().firePropertyChange(ZOOMING, old, zooming);
    }

    /**
     * @return true if this camera zooms to the mouse location
     */
    public boolean isZoomToMouse() {
        synchronized (this) {
            return zoomToMouse;
        }
    }

    /**
     * @param zoomToMouse set to true to zoom to the mouse location
     *
     * @return this instance
     */
    public DraggingCamera setZoomToMouse(final boolean zoomToMouse) {
        final boolean old;
        synchronized (this) {
            old = this.zoomToMouse;
            this.zoomToMouse = zoomToMouse;
        }
        getPropertyChangeSupport().firePropertyChange(ZOOM_TO_MOUSE, old, zoomToMouse);
        return this;
    }

    /**
     * @return the scalar value used to modify the drag speed
     */
    public float getDragMultiplier() {
        synchronized (this) {
            return dragMultiplier;
        }
    }

    /**
     * @param dragMultiplier the scalar value used to modify the drag speed
     *
     * @return this instance
     */
    public DraggingCamera setDragMultiplier(final float dragMultiplier) {
        if (dragMultiplier < 0.0) {
            throw new IllegalArgumentException("dragMultiplier");
        }
        final float old;
        synchronized (this) {
            old = this.dragMultiplier;
            this.dragMultiplier = dragMultiplier;
        }
        getPropertyChangeSupport().firePropertyChange(DRAG_MULTIPLIER, old, dragMultiplier);
        return this;
    }

    /**
     * @return the scalar value used to modify the zoom speed
     */
    public float getZoomMultiplier() {
        synchronized (this) {
            return zoomMultiplier;
        }
    }

    /**
     * @param zoomMultiplier the scalar value used to modify the zoom speed
     *
     * @return this instance
     */
    public DraggingCamera setZoomMultiplier(final float zoomMultiplier) {
        if (zoomMultiplier < 0.0) {
            throw new IllegalArgumentException("zoomMultiplier");
        }
        final float old;
        synchronized (this) {
            old = this.zoomMultiplier;
            this.zoomMultiplier = zoomMultiplier;
        }
        getPropertyChangeSupport().firePropertyChange(ZOOM_MULTIPLIER, old, zoomMultiplier);
        return this;
    }

    /**
     * @return the maximum zoom value (distance of the camera from zero)
     */
    public float getZoomMax() {
        synchronized (this) {
            return zoomMax;
        }
    }

    /**
     * @param zoomMax the maximum zoom value (distance of the camera from zero)
     *
     * @return this instance
     */
    public DraggingCamera setZoomMax(final float zoomMax) {
        final float old;
        synchronized (this) {
            if (zoomMax < zoomMin) {
                throw new IllegalArgumentException("zoomMax");
            }
            old = this.zoomMax;
            this.zoomMax = zoomMax;
        }
        getPropertyChangeSupport().firePropertyChange(ZOOM_MAX, old, zoomMax);
        return this;
    }

    /**
     * @return the minimum zoom value (distance of the camera from zero)
     */
    public float getZoomMin() {
        synchronized (this) {
            return zoomMin;
        }
    }

    /**
     * @param zoomMin the minimum zoom value (distance of the camera from zero)
     *
     * @return this instance
     */
    public DraggingCamera setZoomMin(final float zoomMin) {
        final float old;
        synchronized (this) {
            if (zoomMin > zoomMax) {
                throw new IllegalArgumentException("zoomMin");
            }
            old = this.zoomMin;
            this.zoomMin = zoomMin;
        }
        getPropertyChangeSupport().firePropertyChange(ZOOM_MIN, old, zoomMin);
        return this;
    }

    /**
     * respond to a click to zoom in (by one increment)
     */
    public void clickZoomIn() {
        // zoom the camera in
        // get the current location
        final Vector3D location = getLocation();
        double z = location.getZ() - getZoomMultiplier();
        final float zoomMin = getZoomMin();
        if (Double.compare(z, zoomMin) < 0) {
            z = zoomMin;
        }
        setLocation(new Vector3D(location.getX(), location.getY(), z));
    }

    /**
     * respond to a click to zoom out (by one increment)
     */
    public void clickZoomOut() {
        // zoom the camera out
        // get the current location
        final Vector3D location = getLocation();
        double z = location.getZ() + getZoomMultiplier();
        final float zoomMax = getZoomMax();
        if (Double.compare(z, zoomMax) > 0) {
            z = zoomMax;
        }
        setLocation(new Vector3D(location.getX(), location.getY(), z));
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
        setDragging(false);
        setZooming(false);
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
        setDragging(false);
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
        if (pressedMouseButton == CameraEventType.LEFT) {
            setDragging(true);
        } else if (pressedMouseButton == CameraEventType.RIGHT) {
            setZooming(true);
        }
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
        final float dragMultiplier;
        if (viewport.getHeight() > 0) {
            dragMultiplier = getDragMultiplier() * ((float) sceneViewport.getHeight() / (float) viewport.getHeight());
        } else {
            dragMultiplier = getDragMultiplier();
        }
        if (isDragging() && !isZooming()) {
            // get the current location
            final Vector3D location = getLocation();
            // move the camera around while it is being dragged
            // scale the drag sensitivity according to the current zoom
            // we don't use linear interpolation because we want it to scale all the way to zero
            // because of the perspective shift
            final double zoomScale = location.getZ() / getZoomMax();
            // apply the new translation
            setLocation(location.add(
                    new Vector3D((currentMouseX - previousX) * -dragMultiplier * zoomScale, (currentMouseY - previousY) * dragMultiplier * zoomScale, 0.0)));
        }
        // need to check mouse button here or an initial zoom from scroll can cause problems
        if (pressedMouseButton == CameraEventType.RIGHT && isZooming() && !isDragging()) {
            // get the current location
            final Vector3D location = getLocation();
            // zoom the camera according to how far the mouse has moved
            // use the pressed point as the zoom to location
            final int mouseX = pressedMouseLocationX;
            final int mouseY = sceneViewport.getHeight() - pressedMouseLocationY;
            final double zoom = location.getZ() + getZoomMultiplier() * (currentMouseY - previousY) * dragMultiplier;
            zoomToPoint(location, mouseX, mouseY, zoom);
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
        setZooming(true);
        // zoom the camera in and out
        // get the current location
        final Vector3D location = getLocation();
        final int mouseX = x;
        final int mouseY = sceneViewport.getHeight() - y;
        final double zoom = location.getZ() - getZoomMultiplier() * -amount;
        zoomToPoint(location, mouseX, mouseY, zoom);
    }

    /**
     * Zoom the camera to the specified point.
     *
     * @param location reference to the camera location
     * @param mouseX   the mouse x screen position to zoom to
     * @param mouseY   the mouse y screen position to zoom to
     * @param zoom     the new zoom value for the camera
     */
    protected void zoomToPoint(final Vector3D location, final int mouseX, final int mouseY, final double zoom) {
        if (location != null) {
            // create local thread safe copies of working variables
            final float zoomMinCopy;
            final float zoomMaxCopy;
            final double zoomCopy;
            final boolean move;
            synchronized (this) {
                zoomMinCopy = zoomMin;
                zoomMaxCopy = zoomMax;
            }
            if (Double.compare(zoom, zoomMinCopy) < 0) {
                zoomCopy = zoomMinCopy;
                move = false;
            } else if (Double.compare(zoom, zoomMaxCopy) > 0) {
                zoomCopy = zoomMaxCopy;
                move = false;
            } else {
                zoomCopy = zoom;
                move = true;
            }
            // determine the offset of the camera for the new zoom level
            final java.awt.Point.Double offset = new java.awt.Point.Double();
            if (move && isZoomToMouse()) {
                // this only matters if the viewport is viewable
                final Rectangle viewport = getViewport();
                if (viewport.getWidth() > 0 && viewport.getHeight() > 0) {
                    // create local copies of variables
                    final double fov = getFieldOfView();
                    final double height = 2 * (Math.tan(Math.toRadians(fov) / 2) * zoomCopy);
                    final double ratio = getAspectRatio(viewport);
                    final double width = height * ratio;
                    // use linear interpolation to find the offset from the current viewport to the new viewport
                    final Rectangle2D newBounds = new Rectangle2D(location.getX() - width / 2, location.getY() - height / 2, width, height);
                    final Rectangle2D visible = findVisibleBounds(location, viewport, fov);
                    // current cursor
                    final double visibleCursorX = MathUtil.scale(mouseX, viewport.getX(), viewport.getX() + viewport.getWidth(), visible.getX(), visible.getX()
                                                                                                                                                 + visible.getWidth());
                    final double visibleCursorY = MathUtil.scale(mouseY, viewport.getY(), viewport.getY() + viewport.getHeight(), visible.getY(),
                                                                 visible.getY() + visible.getHeight());
                    // new zoomed cursor
                    final double newCursorX = MathUtil.scale(mouseX, viewport.getX(), viewport.getX() + viewport.getWidth(), newBounds.getX(), newBounds.getX()
                                                                                                                                               + newBounds.getWidth());
                    final double newCursorY = MathUtil.scale(mouseY, viewport.getY(), viewport.getY() + viewport.getHeight(), newBounds.getY(),
                                                             newBounds.getY() + newBounds.getHeight());
                    // the offset
                    final double offX = visibleCursorX - newCursorX;
                    final double offY = visibleCursorY - newCursorY;
                    // set the offset
                    offset.setLocation(-offX, -offY);
                }
            }

            // set translation
            final Vector3D newT = new Vector3D(location.getX() - offset.getX(), location.getY() - offset.getY(), zoomCopy);
            setLocation(newT);
        }
    }

    private Rectangle2D findVisibleBounds(final Vector3D location, final Rectangle viewport, final double fov) {
        final double z = location.getZ();
        final double height = 2 * (Math.tan(Math.toRadians(fov) / 2) * z);
        final double ratio = getAspectRatio(viewport);
        final double width = height * ratio;
        return new Rectangle2D(location.getX() - width / 2, location.getY() - height / 2, width, height);
    }

    private class ZoomTimer extends Timer {

        protected final long zoomTimerLength;
        protected final AtomicLong lastScheduled = new AtomicLong(0L);

        private ZoomTimer(final long zoomTimerLength) {
            super("DraggingCamera_ZoomTimer", true);
            this.zoomTimerLength = zoomTimerLength;
            scheduleAtFixedRate(new ZoomTimerTask(), 0L, zoomTimerLength / 4);
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
                if (System.currentTimeMillis() - lastScheduled.get() >= zoomTimerLength) {
                    setZooming(false);
                }
            }
        }
    }
}
