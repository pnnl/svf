package gov.pnnl.svf.vbo;

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
import gov.pnnl.svf.support.AbstractSupport;
import gov.pnnl.svf.update.Task;
import gov.pnnl.svf.update.UninitializeTask;
import gov.pnnl.svf.update.WorkerUpdateTaskRunnable;
import gov.pnnl.svf.util.ShapeUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Support for actors that need to draw that require performance enhancement
 * through the use of arrays.
 *
 * @author Arthur Bleeker
 */
public class ArrayDrawableSupport extends AbstractSupport<Object> implements Initializable, WorkerUpdateTaskRunnable, DrawableSupport {

    /**
     * String representation of a field in this object.
     */
    protected static final String DISPOSE = "dispose";
    protected final VboDrawableItem vboDrawable;
    protected final Set<String> initializeFields;
    protected final UninitializeListener uninitializeListener;
    protected List<VertexBufferObject> vbos;
    protected List<VertexBufferObject> pickingVbos;
    protected List<VertexBufferObject> colorPickingVbos;
    /**
     * The total number of vertices rendered for performance tracking. This
     * field should only be accessed on the OpenGL active context thread.
     */
    protected int vertices = 0;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor            The owning actor.
     * @param vboDrawable      The VBO drawable.
     * @param initializeFields The set of fields that should cause this actor to
     *                         reinitialize.
     */
    protected ArrayDrawableSupport(final Actor actor, final VboDrawableItem vboDrawable, final Set<String> initializeFields) {
        super(actor);
        if (vboDrawable == null) {
            throw new NullPointerException("vboDrawable");
        }
        if (initializeFields == null) {
            throw new NullPointerException("initializeFields");
        }
        this.vboDrawable = vboDrawable;
        this.initializeFields = new HashSet<>(initializeFields);
        this.uninitializeListener = new UninitializeListener(this, this.initializeFields);
        actor.getPropertyChangeSupport().addPropertyChangeListener(uninitializeListener);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor            The owning actor
     * @param vboDrawable      The VBO drawable.
     * @param initializeFields The set of fields that should cause this actor to
     *                         reinitialize.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static ArrayDrawableSupport newInstance(final Actor actor, final VboDrawableItem vboDrawable, final Set<String> initializeFields) {
        final ArrayDrawableSupport instance = new ArrayDrawableSupport(actor, vboDrawable, initializeFields);
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
        final List<VertexBufferObject> vbos;
        synchronized (this) {
            vbos = this.vbos;
        }
        if (vbos != null && !vbos.isEmpty()) {
            for (final VertexBufferObject vbo : vbos) {
                drawVbo(gl, vbo);
            }
        }
        camera.getExtended().incrementVerticesCounter(vertices);
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        final List<VertexBufferObject> pickingVbos;
        synchronized (this) {
            pickingVbos = this.pickingVbos;
        }
        if (pickingVbos == null) {
            return;
        } else if (pickingVbos.isEmpty()) {
            draw(gl, glu, camera);
        } else {
            for (final VertexBufferObject vbo : pickingVbos) {
                drawVbo(gl, vbo);
            }
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
        final List<VertexBufferObject> colorPickingVbos;
        synchronized (this) {
            colorPickingVbos = this.colorPickingVbos;
        }
        if (colorPickingVbos != null && !colorPickingVbos.isEmpty()) {
            for (final VertexBufferObject vbo : colorPickingVbos) {
                drawVbo(gl, vbo);
            }
        }
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        if (isDisposed()) {
            return;
        }
        initializeVbos(gl, glu);
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
     * Initializes the VBOs for the actor.
     *
     * @param gl  reference to the gl
     * @param glu reference to the glu
     */
    protected void initializeVbos(final GL2 gl, final GLUgl2 glu) {
        // calculate vertices
        final List<VertexBufferObject> vbos;
        final List<VertexBufferObject> pickingVbos;
        final List<VertexBufferObject> colorPickingVbos;
        synchronized (this) {
            vbos = this.vbos;
            pickingVbos = this.pickingVbos;
            colorPickingVbos = this.colorPickingVbos;
        }
        vertices = 0;
        if (vbos != null) {
            for (int i = 0; i < vbos.size(); i++) {
                final VertexBufferObject vbo = vbos.get(i);
                vertices += vbo.getSize();
            }
        }
        if (pickingVbos != null) {
            for (int i = 0; i < pickingVbos.size(); i++) {
                final VertexBufferObject vbo = pickingVbos.get(i);
                vertices += vbo.getSize();
            }
        }
        if (colorPickingVbos != null) {
            for (int i = 0; i < colorPickingVbos.size(); i++) {
                final VertexBufferObject vbo = colorPickingVbos.get(i);
                vertices += vbo.getSize();
            }
        }
    }

    @Override
    public boolean runBefore(final Task task) {
        // no operation
        return true;
    }

    @Override
    public void run(final Task task) {
        final List<VertexBufferObject> vbos;
        final List<VertexBufferObject> pickingVbos;
        final List<VertexBufferObject> colorPickingVbos;
        // primary drawing
        vbos = vboDrawable.createVbos();
        // picking drawing
        pickingVbos = vboDrawable.createPickingVbos();
        // color picking drawing
        final ColorPickingSupport colorPickingSupport = getActor().lookup(ColorPickingSupport.class);
        if (colorPickingSupport != null) {
            colorPickingVbos = vboDrawable.createColorPickingVbos(colorPickingSupport);
        } else {
            colorPickingVbos = Collections.emptyList();
        }
        synchronized (this) {
            this.vbos = vbos;
            this.pickingVbos = pickingVbos;
            this.colorPickingVbos = colorPickingVbos;
        }
        // bind the vbos
        UninitializeTask.schedule(getScene(), this);
    }

    @Override
    public boolean runAfter(final Task task) {
        // no operation
        return true;
    }

    @Override
    public void disposed(final Task task) {
        // no operation
    }

    private void drawVbo(final GL2 gl, final VertexBufferObject vbo) {
        boolean color;
        boolean normal;
        boolean texCoord;
        // handle single color and determine if color is per vertex
        switch (vbo.getColorDataType()) {
            case SINGLE:
                ShapeUtil.pushColor(gl, vbo.getColors(), 0);
            // fall through to next case
            case NONE:
                color = false;
                break;
            case PER_VERTEX:
                color = true;
                break;
            default:
                throw new IllegalArgumentException("Unhandled enumeration type passed to switch statement: " + vbo.getColorDataType());
        }
        // handle single normal and determine if normal is per vertex
        switch (vbo.getNormalDataType()) {
            case SINGLE:
                gl.glNormal3dv(vbo.getNormals(), 0);
            // fall through to next case
            case NONE:
                normal = false;
                break;
            case PER_VERTEX:
                normal = true;
                break;
            default:
                throw new IllegalArgumentException("Unhandled enumeration type passed to switch statement: " + vbo.getNormalDataType());
        }
        // determine if tex coords are specified
        texCoord = vbo.getTexCoords() != null;
        // draw
        gl.glBegin(vbo.getMode());
        for (int i = 0; i < vbo.getSize(); i++) {
            if (color) {
                ShapeUtil.pushColor(gl, vbo.getColors(), i * 4);
            }
            if (normal) {
                gl.glNormal3dv(vbo.getNormals(), i * 3);
            }
            if (texCoord) {
                switch (vbo.getTexCoordDimension()) {
                    case 1:
                        gl.glTexCoord1dv(vbo.getTexCoords(), i);
                        break;
                    case 2:
                        gl.glTexCoord2dv(vbo.getTexCoords(), i * 2);
                        break;
                    case 3:
                        gl.glTexCoord3dv(vbo.getTexCoords(), i * 3);
                        break;
                    case 4:
                        gl.glTexCoord4dv(vbo.getTexCoords(), i * 4);
                        break;
                    default:
                    // fail silently since this is handled within the vbo object
                }
            }
            switch (vbo.getVertexDimension()) {
                case 2:
                    gl.glVertex2dv(vbo.getVertices(), i * 2);
                    break;
                case 3:
                    gl.glVertex3dv(vbo.getVertices(), i * 3);
                    break;
                default:
                // fail silently since this is handled within the vbo object
            }
            if (color) {
                ShapeUtil.popColor(gl, vbo.getColors(), i * 4);
            }
        }
        gl.glEnd();
        // handle single color and determine if color is per vertex
        switch (vbo.getColorDataType()) {
            case SINGLE:
                ShapeUtil.popColor(gl, vbo.getColors(), 0);
                break;
            default:
            // no need to handle all cases
        }
    }

    /**
     * Listener used to uninitialize the call list support when necessary.
     */
    protected static class UninitializeListener implements PropertyChangeListener {

        final ArrayDrawableSupport support;
        final Set<String> initializeFields;

        /**
         * Constructor
         *
         * @param support          the support
         * @param initializeFields the initializable fields
         */
        protected UninitializeListener(final ArrayDrawableSupport support, final Set<String> initializeFields) {
            this.support = support;
            this.initializeFields = initializeFields;
        }

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (initializeFields.contains(evt.getPropertyName())) {
                // create the call lists
                support.getScene().getDefaultTaskManager().schedule(support);
            }
        }
    }

}
