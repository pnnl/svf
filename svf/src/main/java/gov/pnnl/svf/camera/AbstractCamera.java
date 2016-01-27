package gov.pnnl.svf.camera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.AbstractActor;
import gov.pnnl.svf.core.util.Transformer;
import gov.pnnl.svf.event.CameraEvent;
import gov.pnnl.svf.geometry.Frustum;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.Updatable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Abstract class for creating a view into a scene.
 *
 * @author Arthur Bleeker
 *
 */
public abstract class AbstractCamera extends AbstractActor implements CameraExt<CameraListener>, Updatable {

    /**
     * The default type for this actor.
     */
    public static final String DEFAULT_TYPE = "camera";
    private final static Vector3D FORWARD_VECTOR = Vector3D.MINUS_K;
    private final static Vector3D UP_VECTOR = Vector3D.PLUS_J;
    private final static Vector3D PERP_VECTOR = Vector3D.PLUS_I;
    private final static CalculateVectorListener vectorListener = new CalculateVectorListener();
    private final static CalculateFrustumListener frustumListener = new CalculateFrustumListener();
    //    private final static ViewportTransformerListener viewportListener = new ViewportTransformerListener();
    private final List<CameraListener> listeners = Collections.synchronizedList(new ArrayList<CameraListener>());
    private Rectangle viewport = new Rectangle(0, 0, 100, 100);
    private Transformer<Rectangle> viewportTransformer = new ViewportTransformer();
    private Frustum frustum;
    private Vector3D location = Vector3D.ZERO;
    private Vector3D up = UP_VECTOR;
    private Vector3D look = FORWARD_VECTOR;
    private Vector3D perp = PERP_VECTOR;
    private Vector3D cameraUp = UP_VECTOR;
    private double fieldOfView = 25.0;
    private double nearClip = 0.1;
    private double farClip = 1000.0;
    private long verticesCounter = 0L;
    private long verticesRendered = 0L;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this camera is in.
     */
    public AbstractCamera(final Scene scene) {
        super(scene, DEFAULT_TYPE, scene.getFactory().newUuid(scene));
        initialize();
    }

    /**
     * Constructor
     *
     * @param scene reference to the scene that this camera is in
     * @param id    The unique id for this actor.
     */
    public AbstractCamera(final Scene scene, final String id) {
        super(scene, DEFAULT_TYPE, id);
        initialize();
    }

    /**
     * Constructor
     *
     * @param scene reference to the scene that this camera is in
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public AbstractCamera(final Scene scene, final String type, final String id) {
        super(scene, type, id);
        initialize();
    }

    private void initialize() {
        setDrawingPass(DrawingPass.SCENE_INTERFACE);
        // calculate vectors
        // calculate the perpendicular vector
        perp = Vector3D.crossProduct(up, look);
        // calculate the cameraUp vector
        cameraUp = Vector3D.crossProduct(look, perp);
        calculateFrustum();
        getPropertyChangeSupport().addPropertyChangeListener(UP, vectorListener);
        getPropertyChangeSupport().addPropertyChangeListener(LOOK, vectorListener);
        getPropertyChangeSupport().addPropertyChangeListener(VIEWPORT, frustumListener);
        getPropertyChangeSupport().addPropertyChangeListener(LOCATION, frustumListener);
        getPropertyChangeSupport().addPropertyChangeListener(LOOK, frustumListener);
        getPropertyChangeSupport().addPropertyChangeListener(UP, frustumListener);
        getPropertyChangeSupport().addPropertyChangeListener(PERP, frustumListener);
        getPropertyChangeSupport().addPropertyChangeListener(FIELD_OF_VIEW, frustumListener);
        getPropertyChangeSupport().addPropertyChangeListener(NEAR_CLIP, frustumListener);
        getPropertyChangeSupport().addPropertyChangeListener(FAR_CLIP, frustumListener);
        //        getPropertyChangeSupport().addPropertyChangeListener(VIEWPORT_TRANSFORMER, viewportListener);
    }

    @Override
    public void dispose() {
        super.dispose();
        listeners.clear();
    }

    @Override
    public void addListener(final CameraListener listener) {
        listeners.remove(listener);
        listeners.add(listener);
    }

    @Override
    public void removeListener(final CameraListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void clearListeners() {
        listeners.clear();
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // do nothing, most cameras don't need to draw themselves
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // do nothing, most cameras don't need to draw themselves
    }

    @Override
    public Vector3D getCameraUp() {
        synchronized (this) {
            return cameraUp;
        }
    }

    @Override
    public double getFarClip() {
        synchronized (this) {
            return farClip;
        }
    }

    @Override
    public double getFieldOfView() {
        synchronized (this) {
            return fieldOfView;
        }
    }

    @Override
    public Vector3D getLook() {
        synchronized (this) {
            return look;
        }
    }

    @Override
    public double getNearClip() {
        synchronized (this) {
            return nearClip;
        }
    }

    @Override
    public Vector3D getPerp() {
        synchronized (this) {
            return perp;
        }
    }

    @Override
    public Vector3D getUp() {
        synchronized (this) {
            return up;
        }
    }

    @Override
    public Vector3D getLocation() {
        synchronized (this) {
            return location;
        }
    }

    @Override
    public Rectangle getViewport() {
        synchronized (this) {
            return viewport;
        }
    }

    @Override
    public Transformer<Rectangle> getViewportTransformer() {
        synchronized (this) {
            return viewportTransformer;
        }
    }

    @Override
    public Frustum getFrustum() {
        synchronized (this) {
            return frustum;
        }
    }

    @Override
    public void incrementVerticesCounter(final long count) {
        verticesCounter += count;
    }

    @Override
    public long getVerticesCounter() {
        return verticesRendered;
    }

    @Override
    public void resetVerticesCounter() {
        verticesRendered = verticesCounter;
        verticesCounter = 0L;
    }

    @Override
    public void makeOrtho2D(final GL2 gl, final GLUgl2 glu) {
        final Rectangle viewport = getViewport();
        if (viewport.getWidth() == 0 || viewport.getHeight() == 0) {
            return;
        }
        // set up the viewport for projection
        gl.glViewport(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        // set the transformation as actual screen pixels
        glu.gluOrtho2D(0, viewport.getWidth(), 0, viewport.getHeight());
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void endOrtho2D(final GL2 gl, final GLUgl2 glu) {
        // no operation
    }

    @Override
    public void makePerspective(final GL2 gl, final GLUgl2 glu) {
        final Rectangle viewport = getViewport();
        if (viewport.getWidth() == 0 || viewport.getHeight() == 0) {
            return;
        }
        // set up the viewport for projection
        gl.glViewport(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        // set the perspective to the correct aspect ratio and set the model
        // view matrix mode
        final double aspect = getAspectRatio(viewport);
        final double near = getNearClip();
        final double far = getFarClip();
        glu.gluPerspective(getFieldOfView(), aspect, near, far);
        gl.glDepthRange(near, far);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void endPerspective(final GL2 gl, final GLUgl2 glu) {
        // no operation
    }

    @Override
    public void setLookAt(final GL2 gl, final GLUgl2 glu) {
        final Vector3D location;
        final Vector3D look;
        final Vector3D up;
        synchronized (this) {
            location = this.location;
            look = this.look;
            up = this.up;
        }
        final Vector3D target = location.add(look);
        glu.gluLookAt(location.getX(), location.getY(), location.getZ(), target.getX(),
                      target.getY(), target.getZ(), up.getX(), up.getY(), up.getZ());
    }

    @Override
    public Camera setLocation(final Vector3D location) {
        if (location == null) {
            throw new NullPointerException("location");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.location;
            this.location = location;
        }
        getPropertyChangeSupport().firePropertyChange(LOCATION, old, location);
        return this;
    }

    /**
     * @param cameraUp the normalized vector that represents up to the camera
     */
    private Camera setCameraUp(final Vector3D cameraUp) {
        if (cameraUp == null) {
            throw new NullPointerException("cameraUp");
        }
        if (Vector3D.ZERO.equals(cameraUp)) {
            return this;
        }
        final Vector3D old;
        synchronized (this) {
            old = this.cameraUp;
            this.cameraUp = cameraUp;
        }
        getPropertyChangeSupport().firePropertyChange(CAMERA_UP, old, cameraUp);
        return this;
    }

    @Override
    public Camera setFarClip(final double farClip) {
        final double old;
        synchronized (this) {
            old = this.farClip;
            this.farClip = farClip;
        }
        getPropertyChangeSupport().firePropertyChange(FAR_CLIP, old, farClip);
        return this;
    }

    @Override
    public Camera setFieldOfView(final double fieldOfView) {
        final double old;
        synchronized (this) {
            old = this.fieldOfView;
            this.fieldOfView = fieldOfView;
        }
        getPropertyChangeSupport().firePropertyChange(FIELD_OF_VIEW, old, fieldOfView);
        return this;
    }

    @Override
    public Camera setLook(final Vector3D look) {
        if (look == null) {
            throw new NullPointerException("look");
        }
        if (Vector3D.ZERO.equals(look)) {
            return this;
        }
        final Vector3D old;
        final Vector3D normalized = look.normalize();
        synchronized (this) {
            old = this.look;
            this.look = normalized;
        }
        getPropertyChangeSupport().firePropertyChange(LOOK, old, normalized);
        return this;
    }

    @Override
    public Camera setNearClip(final double nearClip) {
        final double old;
        synchronized (this) {
            old = this.nearClip;
            this.nearClip = nearClip;
        }
        getPropertyChangeSupport().firePropertyChange(NEAR_CLIP, old, nearClip);
        return this;
    }

    @Override
    public Camera setUp(final Vector3D up) {
        if (up == null) {
            throw new NullPointerException("up");
        }
        if (Vector3D.ZERO.equals(up)) {
            return this;
        }
        final Vector3D old;
        synchronized (this) {
            old = this.up;
            this.up = up;
        }
        getPropertyChangeSupport().firePropertyChange(UP, old, up);
        return this;
    }

    @Override
    public Camera setViewport(final Rectangle viewport) {
        if (viewport == null) {
            throw new NullPointerException("viewport");
        }
        final Rectangle old;
        final Rectangle transformed;
        synchronized (this) {
            old = this.viewport;
            transformed = viewportTransformer.transform(viewport);
            this.viewport = transformed;
        }
        getPropertyChangeSupport().firePropertyChange(VIEWPORT, old, transformed);
        return this;
    }

    @Override
    public Camera setViewportTransformer(final Transformer<Rectangle> viewportTransformer) {
        if (viewportTransformer == null) {
            throw new NullPointerException("viewportTransformer");
        }
        final Transformer<Rectangle> old;
        synchronized (this) {
            old = this.viewportTransformer;
            this.viewportTransformer = viewportTransformer;
        }
        getPropertyChangeSupport().firePropertyChange(VIEWPORT_TRANSFORMER, old, viewportTransformer);
        setViewport(getScene().getViewport());
        return this;
    }

    @Override
    public String toString() {
        return "AbstractCamera{" + "viewport=" + getViewport() + "up=" + getUp() + "look=" + getLook() + "perp=" + getPerp() + "cameraUp=" + getCameraUp()
               + "fieldOfView=" + getFieldOfView() + "nearClip=" + getNearClip() + "farClip=" + getFarClip() + '}';
    }

    @Override
    public void update(final long delta) {
        // only calculate when necessary
        //        calculateVectors();
        //        calculateFrustum();
    }

    /**
     * The canvas multiplier for determining the difference of size between the
     * canvas and the view. A multiple of 2 would mean that the canvas is twice
     * the size of the view. This is common for Mac Retina displays.
     *
     * @return the canvas multiple
     */
    public double getCanvasMultiplier() {
        // find the difference between the canvas and the view
        final Scene scene = getScene();
        final Rectangle viewport = scene.getViewport();
        final Rectangle bounds = scene.getBounds();
        if (viewport.getWidth() > 0 && bounds.getWidth() > 0) {
            return (double) viewport.getWidth() / (double) bounds.getWidth();
        } else {
            return 1.0;
        }
    }

    /**
     * Calculate the aspect ratio of this camera.
     *
     * @param viewport the viewport
     *
     * @return the aspect ratio width / height
     */
    protected double getAspectRatio(final Rectangle viewport) {
        final int w = viewport.getWidth();
        final int h = viewport.getHeight();
        final double aspectRatio = w == 0 || h == 0 ? 1.0 : (double) w / (double) h;
        return aspectRatio;
    }

    /**
     * @param event the event to fire as a direct call to the listeners
     *
     * @see
     * gov.pnnl.svf.camera.CameraListener#mouseDragged(gov.pnnl.svf.event.CameraEvent)
     */
    protected void fireMouseDraggedEvent(final CameraEvent event) {
        synchronized (listeners) {
            for (final CameraListener listener : listeners) {
                listener.mouseDragged(event);
            }
        }
    }

    /**
     * @param event the event to fire as a direct call to the listeners
     *
     * @see
     * gov.pnnl.svf.camera.CameraListener#mouseEntered(gov.pnnl.svf.event.CameraEvent)
     */
    protected void fireMouseEnteredEvent(final CameraEvent event) {
        synchronized (listeners) {
            for (final CameraListener listener : listeners) {
                listener.mouseEntered(event);
            }
        }
    }

    /**
     * @param event the event to fire as a direct call to the listeners
     *
     * @see
     * gov.pnnl.svf.camera.CameraListener#mouseExited(gov.pnnl.svf.event.CameraEvent)
     */
    protected void fireMouseExitedEvent(final CameraEvent event) {
        synchronized (listeners) {
            for (final CameraListener listener : listeners) {
                listener.mouseExited(event);
            }
        }
    }

    /**
     * @param event the event to fire as a direct call to the listeners
     *
     * @see
     * gov.pnnl.svf.camera.CameraListener#mouseMoved(gov.pnnl.svf.event.CameraEvent)
     */
    protected void fireMouseMovedEvent(final CameraEvent event) {
        synchronized (listeners) {
            for (final CameraListener listener : listeners) {
                listener.mouseMoved(event);
            }
        }
    }

    /**
     * @param event the event to fire as a direct call to the listeners
     *
     * @see
     * gov.pnnl.svf.camera.CameraListener#mousePressed(gov.pnnl.svf.event.CameraEvent)
     */
    protected void fireMousePressedEvent(final CameraEvent event) {
        synchronized (listeners) {
            for (final CameraListener listener : listeners) {
                listener.mousePressed(event);
            }
        }
    }

    /**
     * @param event the event to fire as a direct call to the listeners
     *
     * @see
     * gov.pnnl.svf.camera.CameraListener#mouseReleased(gov.pnnl.svf.event.CameraEvent)
     */
    protected void fireMouseReleasedEvent(final CameraEvent event) {
        synchronized (listeners) {
            for (final CameraListener listener : listeners) {
                listener.mouseReleased(event);
            }
        }
    }

    /**
     * @param event  the event to fire as a direct call to the listeners
     * @param amount amount the mouse wheel is scrolled
     *
     * @see
     * gov.pnnl.svf.camera.CameraListener#mouseScrolled(gov.pnnl.svf.event.CameraEvent,
     * int)
     */
    protected void fireMouseScrolledEvent(final CameraEvent event, final int amount) {
        synchronized (listeners) {
            for (final CameraListener listener : listeners) {
                listener.mouseScrolled(event, amount);
            }
        }
    }

    /**
     * Calculates all of the vectors for this camera.
     */
    protected void calculateVectors() {
        final Vector3D perp;
        final Vector3D cameraUp;
        synchronized (this) {
            // calculate vectors
            // calculate the perpendicular vector
            perp = Vector3D.crossProduct(up, look);
            // calculate the cameraUp vector
            cameraUp = Vector3D.crossProduct(look, perp);
        }
        setPerp(perp);
        setCameraUp(cameraUp);
    }

    /**
     * Calculates the frustum for this camera.
     */
    protected void calculateFrustum() {
        final Frustum frustum;
        synchronized (this) {
            final double aspectRatio = getAspectRatio(viewport);
            if (Vector3D.ZERO.equals(look) || Vector3D.ZERO.equals(up) || Vector3D.ZERO.equals(perp)
                || Double.compare(0.0, fieldOfView) == 0 || Double.compare(0.0, aspectRatio) == 0) {
                frustum = Frustum.ZERO;
            } else {
                frustum = new Frustum(location, look, up, perp.negate(), Math.toRadians(fieldOfView), aspectRatio, nearClip, farClip);
            }
        }
        setFrustum(frustum);
    }

    /**
     * @param perp the normalized vector that represents perpendicular to the
     *             camera
     */
    private void setPerp(final Vector3D perp) {
        if (perp == null) {
            throw new NullPointerException("perp");
        }
        if (Vector3D.ZERO.equals(perp)) {
            return;
        }
        final Vector3D old;
        synchronized (this) {
            old = this.perp;
            this.perp = perp;
        }
        getPropertyChangeSupport().firePropertyChange(PERP, old, perp);
    }

    /**
     * @param frustum the frustum to set
     */
    private void setFrustum(final Frustum frustum) {
        if (frustum == null) {
            throw new NullPointerException("frustum");
        }
        final Frustum old;
        synchronized (this) {
            old = this.frustum;
            this.frustum = frustum;
        }
        getPropertyChangeSupport().firePropertyChange(FRUSTUM, old, frustum);
    }

    @Override
    public CameraExt<?> getExtended() {
        return this;
    }

    /**
     * Listener used to calculate the vectors.
     */
    protected static class CalculateVectorListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            final AbstractCamera camera = (AbstractCamera) evt.getSource();
            camera.calculateVectors();
        }
    }

    /**
     * Listener used to calculate the frustum.
     */
    protected static class CalculateFrustumListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            final AbstractCamera camera = (AbstractCamera) evt.getSource();
            camera.calculateFrustum();
        }
    }

    /**
     * Default transformer for the viewport that doesn't make any changes.
     */
    protected static class ViewportTransformer implements Transformer<Rectangle> {

        @Override
        public Rectangle transform(final Rectangle value) {
            return value;
        }
    }
}
