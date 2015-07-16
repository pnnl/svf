package gov.pnnl.svf.actor;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.Initializable;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.update.UninitializeTask;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * Abstract base class for actors that need to draw that require performance
 * enhancement through the use of call lists.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractCallListActor extends AbstractActor implements Initializable {

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
    protected final UninitializeListener listener = new UninitializeListener();
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
     * The total number of vertices rendered for performance tracking. This
     * field should only be accessed on the OpenGL active context thread.
     */
    protected int vertices = 0;

    /**
     * Constructor
     *
     * @param scene Reference to the scene that this actor belongs to. The scene
     *              will be added to the lookup.
     *
     * @param type  The type of actor.
     * @param id    The unique id for this actor.
     */
    protected AbstractCallListActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
    }

    @Override
    public void dispose() {
        super.dispose();
        listener.propertyChange(new PropertyChangeEvent(this, DISPOSE, null, this));
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        for (int i = 0; i < count; i++) {
            if (gl.glIsList(list + i)) {
                gl.glCallList(list + i);
            }
        }
        camera.getExtended().incrementVerticesCounter(vertices);
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
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
     * Override this method to initialize the lists for the actor. An example of
     * how to use this to create a single call list:
     * <p/>
     * <code>
     * count = 1;<br/>
     * list = gl.glGenLists(count);<br/>
     * gl.glNewList(list, GL2.GL_COMPILE);<br/>
     * // perform rendering steps here...<br/>
     * gl.glEndList();<br/>
     * </code>
     *
     * @param gl  reference to the gl
     * @param glu reference to the glu
     */
    protected abstract void initializeLists(GL2 gl, GLUgl2 glu);

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        if (isDisposed()) {
            return;
        }
        initializeLists(gl, glu);
        final boolean initialized = list != UNINITIALIZED;
        synchronized (this) {
            actorState.setInitialized(initialized);
        }
    }

    @Override
    public boolean isInitialized() {
        synchronized (this) {
            return actorState.isInitialized();
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
        }
        list = UNINITIALIZED;
        vertices = 0;
        synchronized (this) {
            actorState.setInitialized(false);
        }
    }

    /**
     * Listener used to uninitialize the call list actor when necessary.
     */
    protected static class UninitializeListener implements PropertyChangeListener {

        private Set<String> fields = null;

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            final AbstractCallListActor actor = (AbstractCallListActor) evt.getSource();
            if (fields == null) {
                fields = actor.getInitializeFields();
            }
            if (fields.contains(evt.getPropertyName())) {
                UninitializeTask.schedule(actor.getScene(), actor);
            }
        }
    }
}
