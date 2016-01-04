package gov.pnnl.svf.support;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ItemPickableActor;
import gov.pnnl.svf.scene.DrawableSupport;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Initializable;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.update.UninitializeTask;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Support for dynamic actors that need to draw using immediate mode.
 *
 * @author Arthur Bleeker
 */
public class ImmediateDrawableSupport extends AbstractSupport<Object> implements Initializable, DrawableSupport {

    /**
     * String representation of a field in this object.
     */
    protected static final String DISPOSE = "dispose";
    protected final ImmediateDrawableItem immediateDrawableItem;
    protected final Set<String> initializeFields;
    protected final UninitializeListener uninitializeListener;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor                 The owning actor.
     * @param immediateDrawableItem The immediate mode drawable.
     * @param initializeFields      The set of fields that should cause this
     *                              actor to reinitialize.
     */
    protected ImmediateDrawableSupport(final Actor actor, final ImmediateDrawableItem immediateDrawableItem, final Set<String> initializeFields) {
        super(actor);
        if (immediateDrawableItem == null) {
            throw new NullPointerException("immediateDrawableItem");
        }
        if (initializeFields == null) {
            throw new NullPointerException("initializeFields");
        }
        this.immediateDrawableItem = immediateDrawableItem;
        this.initializeFields = new HashSet<>(initializeFields);
        this.uninitializeListener = new UninitializeListener(this, this.initializeFields);
        actor.getPropertyChangeSupport().addPropertyChangeListener(uninitializeListener);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor                 The owning actor
     * @param immediateDrawableItem The immediate mode drawable.
     * @param initializeFields      The set of fields that should cause this
     *                              actor to reinitialize.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static ImmediateDrawableSupport newInstance(final Actor actor, final ImmediateDrawableItem immediateDrawableItem, final Set<String> initializeFields) {
        final ImmediateDrawableSupport instance = new ImmediateDrawableSupport(actor, immediateDrawableItem, initializeFields);
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
        actor.getPropertyChangeSupport().removePropertyChangeListener(uninitializeListener);
        uninitializeListener.propertyChange(new PropertyChangeEvent(this, DISPOSE, null, this));
        super.dispose();
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        immediateDrawableItem.drawImmediate(gl, glu, camera);
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        immediateDrawableItem.pickingDrawImmediate(gl, glu, camera);
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
        immediateDrawableItem.colorPickingDrawImmediate(gl, glu, camera, support);
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        if (isDisposed()) {
            return;
        }
        // call prepare here to create textures for text if necessary
        immediateDrawableItem.prepare(gl, glu);
        synchronized (this) {
            supportState.setInitialized(true);
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
        synchronized (this) {
            supportState.setInitialized(false);
        }
    }

    /**
     * Listener used to uninitialize the support when necessary.
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
