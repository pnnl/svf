package gov.pnnl.svf.actor;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.hint.OpenGLHint;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.scene.DrawableSupport;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.DisplayListDrawableItem;
import gov.pnnl.svf.support.DisplayListDrawableSupport;
import gov.pnnl.svf.support.ImmediateDrawableItem;
import gov.pnnl.svf.support.ImmediateDrawableSupport;
import gov.pnnl.svf.vbo.ArrayDrawableSupport;
import gov.pnnl.svf.vbo.VboDrawableItem;
import gov.pnnl.svf.vbo.VboDrawableSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class for actors that need to draw that require performance
 * enhancement through the use of call lists or vertex array buffers.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractDynamicActor extends AbstractActor implements VboDrawableItem, DisplayListDrawableItem, ImmediateDrawableItem {

    /**
     * String representation of a field in this object.
     */
    protected static final String DISPOSE = "dispose";
    protected final LoadListener loadListener;
    private DrawableSupport drawableSupport;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this actor belongs to. The scene
     *              will be added to the lookup.
     *
     * @param type  The type of actor.
     * @param id    The unique id for this actor.
     */
    protected AbstractDynamicActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
        this.loadListener = new LoadListener(this);
        scene.getPropertyChangeSupport().addPropertyChangeListener(Scene.LOADED, loadListener);
        getPropertyChangeSupport().addPropertyChangeListener(loadListener);
    }

    @Override
    public void dispose() {
        super.dispose();
        getPropertyChangeSupport().firePropertyChange(DISPOSE, false, true);
        getScene().getPropertyChangeSupport().removePropertyChangeListener(Scene.LOADED, loadListener);
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        final DrawableSupport drawableSupport;
        synchronized (this) {
            drawableSupport = this.drawableSupport;
        }
        if (drawableSupport != null) {
            drawableSupport.draw(gl, glu, camera);
        } else {
            drawImmediate(gl, glu, camera);
        }
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        final DrawableSupport drawableSupport;
        synchronized (this) {
            drawableSupport = this.drawableSupport;
        }
        if (drawableSupport != null) {
            drawableSupport.pickingDraw(gl, glu, camera, event);
        } else {
            pickingDrawImmediate(gl, glu, camera);
        }
    }

    @Override
    public void itemPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event, final Object item) {
        final DrawableSupport drawableSupport;
        synchronized (this) {
            drawableSupport = this.drawableSupport;
        }
        if (drawableSupport != null) {
            drawableSupport.itemPickingDraw(gl, glu, camera, event, item);
        } else {
            pickingDrawImmediate(gl, glu, camera);
        }
    }

    @Override
    public void colorPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
        final DrawableSupport drawableSupport;
        synchronized (this) {
            drawableSupport = this.drawableSupport;
        }
        if (drawableSupport != null) {
            drawableSupport.colorPickingDraw(gl, glu, camera, support);
        } else {
            colorPickingDrawImmediate(gl, glu, camera, support);
        }
    }

    /**
     * Return the set of fields that should cause this actor to reinitialize.
     * <p/>
     * <code>
     * final Set&lt;String&gt; fields = super.getInitializeFields();<br/>
     * fields.add(FIELD);<br/>
     * return fields;<br/>
     * </code>
     *
     * @return a set of fields
     */
    protected Set<String> getInitializeFields() {
        final Set<String> fields = new HashSet<>();
        fields.add(LOOKUP);
        fields.add(DISPOSE);
        return fields;
    }

    /**
     * This method is used to override the automatic OpenGLHint. This method
     * must return a hint.
     *
     * @param openGLHints the supported lists of OpenGLHints
     * @param openGLHint  the suggested OpenGLHint to use
     *
     * @return the OpenGLHint to use
     *
     * @throws IllegalStateException if called too soon
     */
    protected abstract OpenGLHint overrideOpenGLHint(final Set<OpenGLHint> openGLHints, OpenGLHint openGLHint);

    /**
     * Updates the drawable support object. This is automatically called when
     * necessary.
     */
    protected void updateSupport() {
        final Set<OpenGLHint> openGLHints = getScene().getExtended().getSceneBuilder().copyHints(OpenGLHint.class);
        // find the highest priority OpenGL hint
        OpenGLHint openGLHint = OpenGLHint.IMMEDIATE_DRAW;
        for (final OpenGLHint value : OpenGLHint.values()) {
            if (openGLHints.contains(value)) {
                openGLHint = value;
                break;
            }
        }
        openGLHint = overrideOpenGLHint(openGLHints, openGLHint);
        // locate new drawableSupport
        DrawableSupport drawableSupport;
        synchronized (this) {
            drawableSupport = this.drawableSupport;
            this.drawableSupport = null;
        }
        if (drawableSupport != null) {
            if (drawableSupport instanceof Disposable) {
                ((Disposable) drawableSupport).dispose();
            }
            remove(drawableSupport);
        }
        switch (openGLHint) {
            case VBO_DRAW:
                drawableSupport = VboDrawableSupport.newInstance(this, this, getInitializeFields());
                break;
            case ARRAY_DRAW:
                drawableSupport = ArrayDrawableSupport.newInstance(this, this, getInitializeFields());
                break;
            case DISPLAY_LISTS:
                drawableSupport = DisplayListDrawableSupport.newInstance(this, this, getInitializeFields());
                break;
            case IMMEDIATE_DRAW:
                drawableSupport = ImmediateDrawableSupport.newInstance(this, this, getInitializeFields());
                break;
            default:
                throw new IllegalArgumentException("Unhandled enumeration type passed to switch statement: " + openGLHint);
        }
        synchronized (this) {
            this.drawableSupport = drawableSupport;
        }
    }

    /**
     * Listener used to update the drawable support once the scene is loaded.
     */
    protected static class LoadListener implements PropertyChangeListener {

        private final AbstractDynamicActor actor;

        /**
         * Constructor
         *
         * @param actor reference to the parent actor
         */
        protected LoadListener(final AbstractDynamicActor actor) {
            this.actor = actor;
        }

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (!Actor.LOOKUP.equals(evt.getPropertyName()) && actor.getInitializeFields().contains(evt.getPropertyName())) {
                try {
                    if (actor.getScene().isLoaded()) {
                        actor.updateSupport();
                        actor.getScene().getPropertyChangeSupport().removePropertyChangeListener(Scene.LOADED, this);
                        actor.getPropertyChangeSupport().removePropertyChangeListener(this);
                    }
                } catch (final IllegalStateException ex) {
                    // called too soon
                }
            }
        }
    }
}
