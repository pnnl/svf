package gov.pnnl.svf.support;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.CountingCamera;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ItemPickableActor;
import gov.pnnl.svf.scene.DrawableSupport;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Initializable;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneMetrics;
import gov.pnnl.svf.update.UninitializeTask;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Support for dynamic actors that need to draw that require performance
 * enhancement through the use of display lists.
 *
 * @author Arthur Bleeker
 */
public class DisplayListDrawableSupport extends AbstractSupport<Object> implements Initializable, DrawableSupport {

    /**
     * String representation of a field in this object.
     */
    protected static final String DISPOSE = "dispose";
    /**
     * list value that indicates that it is not initialized
     */
    protected static final int UNINITIALIZED = -1;
    /**
     * list value that indicates that it is initialized but has no lists
     */
    protected static final int INITIALIZED = -2;
    protected final CountingCamera countingCamera = new CountingCamera();
    protected final DisplayListDrawableItem callListDrawable;
    protected final Set<String> initializeFields;
    protected final UninitializeListener uninitializeListener;
    /**
     * The reference pointer for the list. Use -1 to indicate an uninitialized
     * list. This field should only be accessed on the OpenGL active context
     * thread.
     */
    protected int list = UNINITIALIZED;
    /**
     * The number of sequential display lists that this actor uses. This field
     * should only be accessed on the OpenGL active context thread.
     */
    protected int count = 0;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor            The owning actor.
     * @param callListDrawable The call list callListDrawable.
     * @param initializeFields The set of fields that should cause this actor to
     *                         reinitialize.
     */
    protected DisplayListDrawableSupport(final Actor actor, final DisplayListDrawableItem callListDrawable, final Set<String> initializeFields) {
        super(actor);
        if (callListDrawable == null) {
            throw new NullPointerException("callListDrawable");
        }
        if (initializeFields == null) {
            throw new NullPointerException("initializeFields");
        }
        this.callListDrawable = callListDrawable;
        this.initializeFields = new HashSet<>(initializeFields);
        this.uninitializeListener = new UninitializeListener(this, this.initializeFields);
        actor.getPropertyChangeSupport().addPropertyChangeListener(uninitializeListener);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor            The owning actor
     * @param callListDrawable The call list drawable.
     * @param initializeFields The set of fields that should cause this actor to
     *                         reinitialize.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static DisplayListDrawableSupport newInstance(final Actor actor, final DisplayListDrawableItem callListDrawable, final Set<String> initializeFields) {
        final DisplayListDrawableSupport instance = new DisplayListDrawableSupport(actor, callListDrawable, initializeFields);
        actor.add(instance);
        return instance;
    }

    @Override
    public boolean isVisible() {
        return getActor().isVisible();
    }

    @Override
    public DrawingPass getDrawingPass() {
        return getActor().getDrawingPass();
    }

    @Override
    public Scene getScene() {
        return getActor().getScene();
    }

    @Override
    public void dispose() {
        super.dispose();
        actor.getPropertyChangeSupport().removePropertyChangeListener(uninitializeListener);
        uninitializeListener.propertyChange(new PropertyChangeEvent(this, DISPOSE, null, this));
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (gl.glIsList(list)) {
            gl.glCallList(list);
        }
        camera.getExtended().incrementVerticesCounter(countingCamera.getVerticesCounter());
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        if (gl.glIsList(list + 1)) {
            gl.glCallList(list + 1);
        }
    }

    @Override
    public void itemPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event, final Object item) {
        if (item == getActor()) {
            pickingDraw(gl, glu, camera, event);
        } else if (getActor() instanceof ItemPickableActor) {
            // item picking is always immediate mode
            ((ItemPickableActor) getActor()).itemPickingDraw(gl, glu, camera, event, item);
        }
    }

    @Override
    public void colorPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
        if (count == 3) {
            if (gl.glIsList(list + 2)) {
                gl.glCallList(list + 2);
            }
        }
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        if (isDisposed()) {
            return;
        }
        initializeLists(gl, glu);
        if ((list != UNINITIALIZED) && (count > 0)) {
            // update metrics
            final SceneMetrics metrics = getScene().getExtended().getSceneMetrics();
            for (int i = 0; i < count; i++) {
                metrics.incrementDisplayListCount();
            }
        }
        final boolean initialized = list != UNINITIALIZED;
        synchronized (this) {
            supportState.setInitialized(initialized);
        }
    }

    @Override
    public boolean isInitialized() {
        synchronized (this) {
            return supportState.isInitialized();
        }
    }

    @Override
    public boolean isSlow() {
        return false;
    }

    @Override
    public void unInitialize(final GL2 gl, final GLUgl2 glu) {
        if ((list != UNINITIALIZED) && (count > 0)) {
            gl.glDeleteLists(list, count);
            // update metrics
            final SceneMetrics metrics = getScene().getExtended().getSceneMetrics();
            for (int i = 0; i < count; i++) {
                metrics.decrementDisplayListCount();
            }
        }
        list = UNINITIALIZED;
        countingCamera.resetVerticesCounter();
        synchronized (this) {
            supportState.setInitialized(false);
        }
    }

    /**
     * Initializes the lists for the actor.
     *
     * @param gl  reference to the gl
     * @param glu reference to the glu
     */
    protected void initializeLists(final GL2 gl, final GLUgl2 glu) {
        final ColorPickingSupport colorPickingSupport = getActor().lookup(ColorPickingSupport.class);
        count = colorPickingSupport != null ? 3 : 2;
        list = gl.glGenLists(count);
        // prepare for call list initialization
        callListDrawable.prepare(gl, glu);
        // primary drawing
        gl.glNewList(list, GL2.GL_COMPILE);
        callListDrawable.drawImmediate(gl, glu, countingCamera);
        gl.glEndList();
        // picking drawing
        gl.glNewList(list + 1, GL2.GL_COMPILE);
        callListDrawable.pickingDrawImmediate(gl, glu, countingCamera);
        gl.glEndList();
        if (colorPickingSupport != null) {
            // color picking drawing
            gl.glNewList(list + 2, GL2.GL_COMPILE);
            callListDrawable.colorPickingDrawImmediate(gl, glu, countingCamera, colorPickingSupport);
            gl.glEndList();
        }
    }

    /**
     * Listener used to uninitialize the call list support when necessary.
     */
    protected static class UninitializeListener implements PropertyChangeListener {

        final Initializable initializable;
        final Set<String> initializeFields;

        /**
         * Constructor
         *
         * @param initializable    the initializable
         * @param initializeFields the initializable fields
         */
        protected UninitializeListener(final Initializable initializable, final Set<String> initializeFields) {
            this.initializable = initializable;
            this.initializeFields = initializeFields;
        }

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (initializeFields.contains(evt.getPropertyName())) {
                UninitializeTask.schedule(initializable.getScene(), initializable);
            }
        }
    }

}
