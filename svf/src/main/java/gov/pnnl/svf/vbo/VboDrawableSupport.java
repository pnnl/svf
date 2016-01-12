package gov.pnnl.svf.vbo;

import com.jogamp.opengl.GL;
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
import gov.pnnl.svf.scene.SceneMetrics;
import gov.pnnl.svf.support.AbstractSupport;
import gov.pnnl.svf.update.Task;
import gov.pnnl.svf.update.UninitializeTask;
import gov.pnnl.svf.update.WorkerUpdateTaskRunnable;
import gov.pnnl.svf.util.ShapeUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Support for actors that need to draw that require performance enhancement
 * through the use of vertex buffer objects (VBO)s.
 *
 * @author Arthur Bleeker
 */
public class VboDrawableSupport extends AbstractSupport<Object> implements Initializable, WorkerUpdateTaskRunnable, DrawableSupport {

    /**
     * String representation of a field in this object.
     */
    protected static final String DISPOSE = "dispose";
    protected static final int[] EMPTY_BUFFER = new int[0];
    protected final VboDrawableItem vboDrawable;
    protected final Set<String> initializeFields;
    protected final UninitializeListener uninitializeListener;
    protected List<VertexBufferObject> vbos;
    protected List<VertexBufferObject> pickingVbos;
    protected List<VertexBufferObject> colorPickingVbos;
    /**
     * The reference pointer for the VBO buffers. This field should only be
     * accessed on the OpenGL active context thread.
     */
    protected int[] vboBuffers;
    protected int[] pickingVboBuffers;
    protected int[] colorPickingVboBuffers;
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
    protected VboDrawableSupport(final Actor actor, final VboDrawableItem vboDrawable, final Set<String> initializeFields) {
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
    public static VboDrawableSupport newInstance(final Actor actor, final VboDrawableItem vboDrawable, final Set<String> initializeFields) {
        final VboDrawableSupport instance = new VboDrawableSupport(actor, vboDrawable, initializeFields);
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
        final List<VertexBufferObject> vbos;
        synchronized (this) {
            vbos = this.vbos;
        }
        if (vbos != null && !vbos.isEmpty()) {
            drawBuffers(gl, vbos, vboBuffers);
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
            drawBuffers(gl, pickingVbos, pickingVboBuffers);
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
            drawBuffers(gl, colorPickingVbos, colorPickingVboBuffers);
        }
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        if (isDisposed()) {
            return;
        }
        initializeVbos(gl, glu);
        final List<VertexBufferObject> vbos;
        final List<VertexBufferObject> pickingVbos;
        final List<VertexBufferObject> colorPickingVbos;
        synchronized (this) {
            vbos = this.vbos;
            pickingVbos = this.pickingVbos;
            colorPickingVbos = this.colorPickingVbos;
        }
        final boolean initialized = (vbos != null ? vboBuffers != null : true)
                                    && (pickingVbos != null ? pickingVboBuffers != null : true)
                                    && (colorPickingVbos != null ? colorPickingVboBuffers != null : true);
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
        if (vboBuffers != null && vboBuffers.length > 0) {
            gl.glDeleteBuffers(vboBuffers.length, vboBuffers, 0);
            // update metrics
            final SceneMetrics metrics = getScene().getExtended().getSceneMetrics();
            for (int i = 0; i < vboBuffers.length; i++) {
                metrics.decrementVboBufferCount();
            }
        }
        if (pickingVboBuffers != null && pickingVboBuffers.length > 0) {
            gl.glDeleteBuffers(pickingVboBuffers.length, pickingVboBuffers, 0);
            // update metrics
            final SceneMetrics metrics = getScene().getExtended().getSceneMetrics();
            for (int i = 0; i < pickingVboBuffers.length; i++) {
                metrics.decrementVboBufferCount();
            }
        }
        if (colorPickingVboBuffers != null && colorPickingVboBuffers.length > 0) {
            gl.glDeleteBuffers(colorPickingVboBuffers.length, colorPickingVboBuffers, 0);
            // update metrics
            final SceneMetrics metrics = getScene().getExtended().getSceneMetrics();
            for (int i = 0; i < colorPickingVboBuffers.length; i++) {
                metrics.decrementVboBufferCount();
            }
        }
        vboBuffers = null;
        pickingVboBuffers = null;
        colorPickingVboBuffers = null;
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
        // prepare for vbo binding
        final List<VertexBufferObject> vbos;
        final List<VertexBufferObject> pickingVbos;
        final List<VertexBufferObject> colorPickingVbos;
        synchronized (this) {
            vbos = this.vbos;
            pickingVbos = this.pickingVbos;
            colorPickingVbos = this.colorPickingVbos;
            // cleanup
            // we need to keep them around for now
//            this.vbos = null;
//            this.pickingVbos = null;
//            this.colorPickingVbos = null;
        }
        vertices = 0;
        // generate, bind, and populate vbos
        if (vbos != null) {
            vboBuffers = genBuffers(gl, vbos);
            populateBuffers(gl, vbos, vboBuffers);
        } else {
            vboBuffers = null;
        }
        // generate, bind, and populate picking vbos
        if (pickingVbos != null) {
            pickingVboBuffers = genBuffers(gl, pickingVbos);
            populateBuffers(gl, pickingVbos, pickingVboBuffers);
        } else {
            pickingVboBuffers = null;
        }
        // generate, bind, and populate color picking vbos
        if (colorPickingVbos != null) {
            colorPickingVboBuffers = genBuffers(gl, colorPickingVbos);
            populateBuffers(gl, colorPickingVbos, colorPickingVboBuffers);
        } else {
            colorPickingVboBuffers = null;
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

    private int[] genBuffers(final GL2 gl, final List<VertexBufferObject> list) {
        if (list.isEmpty()) {
            return EMPTY_BUFFER;
        }
        // generate vbo buffers
        int size = 0;
        for (int i = 0; i < list.size(); i++) {
            final VertexBufferObject vbo = list.get(i);
            if (vbo.getTexCoords() != null) {
                size++;// tex coords
            }
            if (vbo.getColorDataType() == VboDataType.PER_VERTEX) {
                size++;// colors
            }
            if (vbo.getNormalDataType() == VboDataType.PER_VERTEX) {
                size++;// normals
            }
            size++;// vertices
        }
        final int[] buffers = new int[size];
        gl.glGenBuffers(buffers.length, buffers, 0);
        // update metrics
        final SceneMetrics metrics = getScene().getExtended().getSceneMetrics();
        for (int i = 0; i < size; i++) {
            metrics.incrementVboBufferCount();
        }
        return buffers;
    }

    private void populateBuffers(final GL2 gl, final List<VertexBufferObject> list, final int[] buffers) {
        if (list.isEmpty()) {
            return;
        }
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            final VertexBufferObject vbo = list.get(i);
            // texture coords
            if (vbo.getTexCoords() != null) {
                final DoubleBuffer texCoordsBuffer = DoubleBuffer.wrap(vbo.getTexCoords());
                texCoordsBuffer.rewind();
                gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[index++]);
                gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo.getTexCoords().length * 8L, texCoordsBuffer, GL.GL_STATIC_DRAW);
            }
            // colors
            if (vbo.getColorDataType() == VboDataType.PER_VERTEX) {
                final FloatBuffer colorsBuffer = FloatBuffer.wrap(vbo.getColors());
                colorsBuffer.rewind();
                gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[index++]);
                gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo.getColors().length * 4L, colorsBuffer, GL.GL_STATIC_DRAW);
            }
            // normals
            if (vbo.getNormalDataType() == VboDataType.PER_VERTEX) {
                final DoubleBuffer normalsBuffer = DoubleBuffer.wrap(vbo.getVertices());
                normalsBuffer.rewind();
                gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[index++]);
                gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo.getNormals().length * 8L, normalsBuffer, GL.GL_STATIC_DRAW);
            }
            // vertices
            final DoubleBuffer verticesBuffer = DoubleBuffer.wrap(vbo.getVertices());
            verticesBuffer.rewind();
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[index++]);
            gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo.getVertices().length * 8L, verticesBuffer, GL.GL_STATIC_DRAW);
            // calculate vertices
            vertices += vbo.getVertices().length / vbo.getVertexDimension();
        }
    }

    private void drawBuffers(final GL2 gl, final List<VertexBufferObject> list, final int[] buffers) {
        if (list.isEmpty() || buffers == null || buffers.length == 0) {
            return;
        }
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            final VertexBufferObject vbo = list.get(i);
            gl.glPushAttrib(GL2.GL_ENABLE_BIT);
            // texture coords
            if (vbo.getTexCoords() != null) {
                gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
                gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[index++]);
                gl.glTexCoordPointer(vbo.getTexCoordDimension(), GL2.GL_DOUBLE, 0, 0);
            }
            // colors
            switch (vbo.getColorDataType()) {
                case SINGLE:
                    ShapeUtil.pushColor(gl, vbo.getColors(), 0);
                    break;
                case PER_VERTEX:
                    gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
                    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[index++]);
                    gl.glColorPointer(4, GL2.GL_FLOAT, 0, 0);
                    break;
                default:
                // no need to handle all cases
            }
            // normals
            switch (vbo.getNormalDataType()) {
                case SINGLE:
                    gl.glNormal3dv(vbo.getNormals(), 0);
                    break;
                case PER_VERTEX:
                    gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
                    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[index++]);
                    gl.glNormalPointer(GL2.GL_DOUBLE, 0, 0);
                    break;
                default:
                // no need to handle all cases
            }
            // vertices
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[index++]);
            gl.glVertexPointer(vbo.getVertexDimension(), GL2.GL_DOUBLE, 0, 0);
            // draw
            gl.glDrawArrays(vbo.getMode(), 0, vbo.getSize());
            // cleanup
            if (vbo.getColorDataType() == VboDataType.SINGLE) {
                ShapeUtil.popColor(gl, vbo.getColors(), 0);
            }
            gl.glPopAttrib();
        }
    }

    /**
     * Listener used to uninitialize the call list support when necessary.
     */
    protected static class UninitializeListener implements PropertyChangeListener {

        final VboDrawableSupport support;
        final Set<String> initializeFields;

        /**
         * Constructor
         *
         * @param support          the support
         * @param initializeFields the initializable fields
         */
        protected UninitializeListener(final VboDrawableSupport support, final Set<String> initializeFields) {
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
