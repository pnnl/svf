package gov.pnnl.svf.picking;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.AbstractActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.CameraExt;
import gov.pnnl.svf.core.util.NamedThreadFactory;
import gov.pnnl.svf.core.util.Transformer;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Frustum;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.Updatable;
import gov.pnnl.svf.util.SceneCoordsHelper;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.commons.math.geometry.Vector3D;

/**
 * This camera builds a picking view for a scene. Actor's that have picking
 * support will be notified when clicked or double clicked in a reference camera
 * view that is attached to this picking camera.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractPickingCamera extends AbstractActor implements PickingCamera, Updatable {

    /**
     * The default type for this actor.
     */
    public static final String DEFAULT_TYPE = "picking-camera";
    private final static int CAPACITY = 64;
    private final List<PickingCameraListener> listeners = Collections.synchronizedList(new ArrayList<PickingCameraListener>());
    private Set<PickingCameraEvent> eventQueue = new LinkedHashSet<>();
    private final Object eventQueueSync = new Object();
    /**
     * the thread to use for pushing events from picking
     */
    protected final Executor eventPushThread = Executors.newCachedThreadPool(new NamedThreadFactory(getClass(), "Event"));
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(CAPACITY * 4);
    /**
     * helper object for working with and transforming coordinates
     */
    protected final SceneCoordsHelper coords = new SceneCoordsHelper(getScene());
    /**
     * reference to the camera that the user is viewing through while picking
     */
    protected final Camera referenceCamera;
    private final ReferenceCameraListener referenceCameraListener = new ReferenceCameraListener(this);
    private int dragSensitivity = 4;
    private double sensitivity = 0.0;
    private Set<CameraEventType> pickTypes;
    /**
     * the total number of objects picked during an event
     */
    protected int totalPicked = 0;
    /**
     * the event that is currently being processed
     */
    protected PickingCameraEvent currentEvent;

    /**
     * Initializer
     */
    {
        final Set<CameraEventType> types = EnumSet.copyOf(CameraEventType.Collections.ALL_TYPES);
        types.removeAll(CameraEventType.Collections.MOVEMENT_TYPES);
        pickTypes = Collections.unmodifiableSet(types);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractPickingCamera(final Scene scene, final Camera camera) {
        super(scene, DEFAULT_TYPE, scene.getFactory().newUuid(scene));
        if (camera == null) {
            throw new NullPointerException("camera");
        }
        referenceCamera = camera;
        initialize();
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractPickingCamera(final Scene scene, final String id, final Camera camera) {
        super(scene, DEFAULT_TYPE, id);
        if (camera == null) {
            throw new NullPointerException("camera");
        }
        referenceCamera = camera;
        initialize();
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param type   The type of this actor.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractPickingCamera(final Scene scene, final String type, final String id, final Camera camera) {
        super(scene, type, id);
        if (camera == null) {
            throw new NullPointerException("camera");
        }
        referenceCamera = camera;
        initialize();
    }

    private void initialize() {
        setDrawingPass(DrawingPass.SCENE_INTERFACE);
        referenceCamera.getPropertyChangeSupport().addPropertyChangeListener(referenceCameraListener);
    }

    @Override
    public void dispose() {
        super.dispose();
        referenceCamera.getPropertyChangeSupport().removePropertyChangeListener(referenceCameraListener);
    }

    @Override
    public void addEvent(final PickingCameraEvent event) {
        if (event == null) {
            throw new NullPointerException("event");
        }
        // filter the events
        if (!getPickTypes().containsAll(event.getTypes())) {
            return;
        }
        synchronized (eventQueueSync) {
            eventQueue.add(event);
        }
        // no longer necessary to redraw entire scene for a raycast
        //        setDirty(!event.getTypes().contains(CameraEventType.MOVE));
    }

    /**
     * @param event the event to fire as a direct call to the listeners
     */
    protected void fireMoveEvent(final PickingCameraEvent event) {
        synchronized (listeners) {
            for (final PickingCameraListener listener : listeners) {
                listener.picked(event);
            }
        }
    }

    @Override
    public void addListener(final PickingCameraListener listener) {
        listeners.remove(listener);
        listeners.add(listener);
    }

    @Override
    public void checkPickingHit(final GL2 gl, final GLUgl2 glu, final PickingSupport support) {
        // only pick if this actor is viewable in the referenceCamera
        if (support.getActor().isCamera(referenceCamera)) {
            gl.glRenderMode(GL2.GL_SELECT);
            gl.glInitNames();
            gl.glPushName(0);
            support.pickingDraw(gl, glu, referenceCamera, currentEvent);
            final int hits = gl.glRenderMode(GL2.GL_RENDER);
            gl.glPopName();
            if (hits > 0) {
                totalPicked++;
                // find the scene coords
                currentEvent.setLocation(coords.unProject(gl, glu, currentEvent.getX(), currentEvent.getY()));
                // process the actor as hit
                eventPushThread.execute(new Runnable() {
                    private final PickingCameraEvent event = currentEvent;

                    @Override
                    public void run() {
                        support.notifyPicked(event);
                    }
                });
            }
        }
    }

    @Override
    public void clearListeners() {
        listeners.clear();
    }

    @Override
    public void end(final PickingCameraEvent event) {
        if (event == null) {
            throw new NullPointerException("event");
        }
        if (currentEvent != event) {
            throw new IllegalArgumentException("event");
        }
        synchronized (listeners) {
            for (final PickingCameraListener listener : listeners) {
                eventPushThread.execute(new PickedRunnable(listener, event));
            }
        }
        if (totalPicked == 0 && !(Collections.disjoint(event.getTypes(), CameraEventType.Collections.ACTION_TYPES))) {
            synchronized (listeners) {
                for (final PickingCameraListener listener : listeners) {
                    eventPushThread.execute(new NothingPickedRunnable(listener, event));
                }
            }
        }
        currentEvent = null;
    }

    @Override
    public int getDragSensitivity() {
        synchronized (this) {
            return dragSensitivity;
        }
    }

    @Override
    public Camera getReferenceCamera() {
        return referenceCamera;
    }

    @Override
    public Set<PickingCameraEvent> getEvents() {
        synchronized (eventQueueSync) {
            if (eventQueue.isEmpty()) {
                return Collections.emptySet();
            }
            final Set<PickingCameraEvent> reference = eventQueue;
            eventQueue = new LinkedHashSet<>();
            return reference;
        }
    }

    @Override
    public double getSensitivity() {
        synchronized (this) {
            return sensitivity;
        }
    }

    @Override
    public void makePerspective(final GL2 gl, final GLUgl2 glu) {
        buffer.clear();
        final Rectangle viewport = getViewport();
        // set up the viewport for projection
        gl.glViewport(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        // set up selection mode
        gl.glSelectBuffer(CAPACITY, buffer.asIntBuffer());
        if (currentEvent != null) {
            final double margin = getSensitivity();
            // set up the picking matrix
            glu.gluPickMatrix(
                    currentEvent.getX() - viewport.getX(),
                    (viewport.getHeight() + viewport.getY()) - currentEvent.getY(),
                    margin + currentEvent.getWidth() + margin,
                    margin + currentEvent.getHeight() + margin,
                    new int[]{viewport.getX(),
                              viewport.getY(),
                              viewport.getWidth(),
                              viewport.getHeight()}, 0);
        }
        // set the perspective to the correct aspect ratio and set the model
        // view matrix mode
        final double aspect = (viewport.getWidth() <= 0) || (viewport.getHeight() <= 0)
                              ? 1.0f
                              : (double) viewport.getWidth() / (double) viewport.getHeight();
        glu.gluPerspective(referenceCamera.getFieldOfView(), aspect, referenceCamera.getNearClip(), referenceCamera.getFarClip());
        gl.glDepthRange(referenceCamera.getNearClip(), referenceCamera.getFarClip());
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void removeListener(final PickingCameraListener listener) {
        listeners.remove(listener);
    }

    @Override
    public PickingCamera setDragSensitivity(final int dragSensitivity) {
        final int old;
        synchronized (this) {
            old = this.dragSensitivity;
            this.dragSensitivity = dragSensitivity;
        }
        getPropertyChangeSupport().firePropertyChange(PickingCamera.DRAG_SENSITIVITY, old, dragSensitivity);
        return this;
    }

    @Override
    public void setLookAt(final GL2 gl, final GLUgl2 glu) {
        referenceCamera.setLookAt(gl, glu);
    }

    @Override
    public PickingCamera setSensitivity(final double sensitivity) {
        if (Double.compare(sensitivity, 0.0) < 0) {
            throw new IllegalArgumentException("sensitivity");
        }
        final double old;
        synchronized (this) {
            old = this.sensitivity;
            this.sensitivity = sensitivity;
        }
        getPropertyChangeSupport().firePropertyChange(PickingCamera.SENSITIVITY, old, sensitivity);
        return this;
    }

    @Override
    public Set<CameraEventType> getPickTypes() {
        synchronized (this) {
            return pickTypes;
        }
    }

    @Override
    public PickingCamera setPickTypes(final Set<CameraEventType> types) {
        synchronized (this) {
            if (types != null && !types.isEmpty() && !types.contains(CameraEventType.ANY)) {
                pickTypes = Collections.unmodifiableSet(EnumSet.copyOf(types));
            } else {
                pickTypes = Collections.unmodifiableSet(EnumSet.copyOf(CameraEventType.Collections.ALL_TYPES));
            }
        }
        return this;
    }

    @Override
    public void start(final PickingCameraEvent event) {
        if (event == null) {
            throw new NullPointerException("event");
        }
        currentEvent = event;
        totalPicked = 0;
        synchronized (listeners) {
            for (final PickingCameraListener listener : listeners) {
                listener.prePicked(event);
            }
        }
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        referenceCamera.draw(gl, glu, camera);
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        referenceCamera.endDraw(gl, glu, camera);
    }

    @Override
    public Vector3D getCameraUp() {
        return referenceCamera.getCameraUp();
    }

    @Override
    public double getFarClip() {
        return referenceCamera.getFarClip();
    }

    @Override
    public double getFieldOfView() {
        return referenceCamera.getFieldOfView();
    }

    @Override
    public Vector3D getLocation() {
        return referenceCamera.getLocation();
    }

    @Override
    public Vector3D getLook() {
        return referenceCamera.getLook();
    }

    @Override
    public double getNearClip() {
        return referenceCamera.getNearClip();
    }

    @Override
    public Vector3D getPerp() {
        return referenceCamera.getPerp();
    }

    @Override
    public Vector3D getUp() {
        return referenceCamera.getUp();
    }

    @Override
    public Rectangle getViewport() {
        return referenceCamera.getViewport();
    }

    @Override
    public Transformer<Rectangle> getViewportTransformer() {
        return referenceCamera.getViewportTransformer();
    }

    @Override
    public Frustum getFrustum() {
        return referenceCamera.getFrustum();
    }

    @Override
    public void makeOrtho2D(final GL2 gl, final GLUgl2 glu) {
        referenceCamera.getExtended().makeOrtho2D(gl, glu);
    }

    @Override
    public void endOrtho2D(final GL2 gl, final GLUgl2 glu) {
        referenceCamera.getExtended().endOrtho2D(gl, glu);
    }

    @Override
    public void endPerspective(final GL2 gl, final GLUgl2 glu) {
        referenceCamera.getExtended().endPerspective(gl, glu);
    }

    @Override
    public Camera setViewport(final Rectangle viewport) {
        return referenceCamera.setViewport(viewport);
    }

    @Override
    public Camera setViewportTransformer(final Transformer<Rectangle> transformer) {
        return referenceCamera.setViewportTransformer(transformer);
    }

    @Override
    public Camera setLocation(final Vector3D location) {
        return referenceCamera.setLocation(location);
    }

    @Override
    public Camera setNearClip(final double nearClip) {
        return referenceCamera.setNearClip(nearClip);
    }

    @Override
    public Camera setFarClip(final double farClip) {
        return referenceCamera.setFarClip(farClip);
    }

    @Override
    public Camera setFieldOfView(final double fieldOfView) {
        return referenceCamera.setFieldOfView(fieldOfView);
    }

    @Override
    public Camera setLook(final Vector3D look) {
        return referenceCamera.setLook(look);
    }

    @Override
    public Camera setUp(final Vector3D up) {
        return referenceCamera.setUp(up);
    }

    @Override
    public void incrementVerticesCounter(final long count) {
        referenceCamera.getExtended().incrementVerticesCounter(count);
    }

    @Override
    public long getVerticesCounter() {
        return referenceCamera.getExtended().getVerticesCounter();
    }

    @Override
    public void resetVerticesCounter() {
        referenceCamera.getExtended().resetVerticesCounter();
    }

    @Override
    public void update(final long delta) {
        // no operation by default
    }

    @Override
    public CameraExt<?> getExtended() {
        return this;
    }

    private static class PickedRunnable implements Runnable {

        private final PickingCameraListener listener;
        private final PickingCameraEvent event;

        private PickedRunnable(final PickingCameraListener listener, final PickingCameraEvent event) {
            this.listener = listener;
            this.event = event;
        }

        @Override
        public void run() {
            listener.picked(event);
            listener.postPicked(event);
        }
    }

    private static class NothingPickedRunnable implements Runnable {

        private final PickingCameraListener listener;
        private final PickingCameraEvent event;

        private NothingPickedRunnable(final PickingCameraListener listener, final PickingCameraEvent event) {
            this.listener = listener;
            this.event = event;
        }

        @Override
        public void run() {
            listener.nothingPicked(event);
        }
    }

    private static class ReferenceCameraListener implements PropertyChangeListener {

        private final AbstractPickingCamera picking;

        private ReferenceCameraListener(final AbstractPickingCamera picking) {
            this.picking = picking;
        }

        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            picking.getPropertyChangeSupport().firePropertyChange(event);
        }
    }
}
