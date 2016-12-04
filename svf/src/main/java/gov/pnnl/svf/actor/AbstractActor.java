package gov.pnnl.svf.actor;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.lookup.LookupProvider;
import gov.pnnl.svf.core.lookup.LookupProviderFactory;
import gov.pnnl.svf.core.util.ActorState;
import gov.pnnl.svf.core.util.DrawState;
import gov.pnnl.svf.core.util.PropertyChangeSupportWrapper;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.AbstractSupport;
import gov.pnnl.svf.support.ChildSupport;
import gov.pnnl.svf.support.ParentSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Base class for drawing actors in a scene. Actors will only maintain one of
 * each type of support class in their lookup. Existing support classes will be
 * deregistered if replaced. </p>
 * <p>
 * Subclasses should only provide geometry for drawing the actor. All subclassed
 * actors should provide at a minimum: vertices, normals, and texture
 * coordinates. </p>
 * <p>
 * Support for things like textures or translation are supported through support
 * classes that get added to the actor's lookup. Subclasses should not provide
 * state and getters and setter for generic functionality. These should be
 * provided by 'Support' classes that are added to the actor's lookup. </p>
 * <p>
 * There are certain situations where you will want to push state and other
 * complex functionality into an actor for performance. </p>
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractActor implements ActorExt {

    private static final DirtyListener listener = new DirtyListener();
    private static final Logger logger = Logger.getLogger(AbstractActor.class.toString());
    protected final ActorState actorState = new ActorState();
    protected final DrawState drawState = new DrawState();
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupportWrapper(this);
    private final Collection<Disposable> disposables = Collections.synchronizedSet(new HashSet<Disposable>());
    private final LookupProvider lookup = LookupProviderFactory.newLookupProvider();
    private Camera camera = null;
    private DrawingPass drawingPass = DrawingPass.SCENE;
    private byte passNumber = 0;
    private float thickness = 1.0f;
    private final Scene scene;
    private String id;
    private String type;

    /**
     * Default Constructor
     *
     * @param scene Reference to the scene that this actor belongs to. The scene
     *              will be added to the lookup.
     * @param type  The type of actor.
     * @param id    The unique id for this actor.
     *
     * @throws NullPointerException     if any parameters are null
     * @throws IllegalArgumentException if type or id are empty
     */
    protected AbstractActor(final Scene scene, final String type, final String id) {
        if ((scene == null) || (type == null) || (id == null)) {
            throw new NullPointerException();
        }
        if (type.isEmpty() || id.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.scene = scene;
        this.id = id;
        this.type = type;
    }

    {
        actorState.setDirty();
        actorState.setRoot();
        actorState.setVisible();
        // add a listener to listen for changes to itself
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        draw(gl, glu, camera);
        endDraw(gl, glu, camera);
    }

    @Override
    public void itemPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event, final Object item) {
        if (item == this) {
            draw(gl, glu, camera);
            endDraw(gl, glu, camera);
        }
    }

    @Override
    public void colorPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
        final Color color = support.getMapping(this);
        if (color != null) {
            final float[] c = color.toRgbaArray();
            gl.glColor4fv(c, 0);
            draw(gl, glu, camera);
            endDraw(gl, glu, camera);
        }
    }

    @Override
    public void clear() {
        final Set<Object> all = lookup.lookupAll();
        lookup.clear();
        disposables.clear();
        propertyChangeSupport.firePropertyChange(LOOKUP, all, null);
    }

    @Override
    public <T> void add(final T object) {
        lookup.add(object);
        // determine if this actor is a root or not
        if (object instanceof ParentSupport) {
            setRoot(false);
        }
        if (object instanceof Disposable) {
            // we only want to dispose support objects that this actor owns
            if (object instanceof AbstractSupport) {
                if (((AbstractSupport<?>) object).getActor() == this) {
                    disposables.add((Disposable) object);
                }
            } else {
                disposables.add((Disposable) object);
            }
        }
        propertyChangeSupport.firePropertyChange(LOOKUP, null, object);
    }

    @Override
    public Set<Object> lookupAll() {
        return lookup.lookupAll();
    }

    @Override
    public void lookupAll(final Collection<Object> out) {
        lookup.lookupAll(out);
    }

    @Override
    public void dispose() {
        // set the disposed flag
        synchronized (this) {
            if (actorState.isDisposed()) {
                return;
            }
            actorState.setDisposed();
        }
        propertyChangeSupport.removePropertyChangeListener(listener);
        // remove the property change listeners
        final PropertyChangeListener[] listeners = propertyChangeSupport.getPropertyChangeListeners();
        for (final PropertyChangeListener listener : listeners) {
            // remove the listener
            // also removes listeners that are associated with a specific field
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
        // dispose of the children
        final ChildSupport children = lookup.lookup(ChildSupport.class);
        if (children != null) {
            for (final Actor child : children.getChildren()) {
                child.dispose();
            }
            children.dispose();
        }
        // dispose of the disposable objects
        final Collection<Disposable> copy;
        synchronized (disposables) {
            copy = new ArrayList<>(disposables);
        }
        for (final Disposable disposable : copy) {
            disposable.dispose();
        }
        // clear out the lookup
        lookup.clear();
        // remove from the scene
        scene.remove(this);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractActor other = (AbstractActor) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public Camera getCamera() {
        synchronized (this) {
            return camera;
        }
    }

    @Override
    public DrawingPass getDrawingPass() {
        synchronized (this) {
            return drawingPass;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public byte getPassNumber() {
        synchronized (this) {
            return passNumber;
        }
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public float getThickness() {
        synchronized (this) {
            return thickness;
        }
    }

    @Override
    public String getType() {
        synchronized (this) {
            return type;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        synchronized (this) {
            result = prime * result + (type == null ? 0 : type.hashCode());
        }
        return result;
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return actorState.isDisposed();
        }
    }

    @Override
    public boolean isDirty() {
        synchronized (this) {
            return actorState.isDirty();
        }
    }

    @Override
    public boolean isRoot() {
        synchronized (this) {
            return actorState.isRoot();
        }
    }

    @Override
    public boolean isVisible() {
        synchronized (this) {
            return actorState.isVisible();
        }
    }

    @Override
    public boolean isWire() {
        synchronized (this) {
            return actorState.isWire();
        }
    }

    @Override
    public <T> T lookup(final Class<? extends T> object) {
        return lookup.lookup(object);
    }

    @Override
    public <T> boolean remove(final T object) {
        final boolean removed = lookup.remove(object);
        if (removed) {
            // remove the support object from the scene if this actor owns it
            if (object instanceof AbstractSupport<?>) {
                if (((AbstractSupport<?>) object).getActor() == this) {
                    scene.remove(object);
                }
                // determine if this actor is a root or not
                if (object instanceof ParentSupport) {
                    setRoot(true);
                }
            }
            if (object instanceof Disposable) {
                disposables.remove((Disposable) object);
            }
            if (removed) {
                propertyChangeSupport.firePropertyChange(LOOKUP, object, null);
            }
        }
        return removed;
    }

    @Override
    public Actor setCamera(final Camera camera) {
        final Camera old;
        synchronized (this) {
            old = this.camera;
            this.camera = camera;
        }
        propertyChangeSupport.firePropertyChange(CAMERA, old, camera);
        return this;
    }

    @Override
    public Actor setDirty(final boolean dirty) {
        final boolean old;
        synchronized (this) {
            old = isDirty();
            actorState.setDirty(dirty);
        }
        propertyChangeSupport.firePropertyChange(DIRTY, old, dirty);
        return this;
    }

    @Override
    public Actor setDrawingPass(final DrawingPass drawingPass) {
        if (drawingPass == null) {
            throw new NullPointerException("drawingPass");
        }
        final DrawingPass old;
        synchronized (this) {
            old = this.drawingPass;
            this.drawingPass = drawingPass;
        }
        propertyChangeSupport.firePropertyChange(DRAWING_PASS, old, drawingPass);
        return this;
    }

    /**
     * This setter is for use by the Actor pool only. Changing the id while an
     * actor is in use will cause problems with looking up actors by id. This
     * method is not thread safe.
     *
     * @param id the new id for the actor
     *
     * @return this instance
     */
    public Actor setId(final String id) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        if (id.isEmpty()) {
            throw new IllegalArgumentException("id");
        }
        this.id = id;
        return this;
    }

    @Override
    public Actor setPassNumber(final byte passNumber) {
        if (passNumber < 0) {
            throw new IllegalArgumentException("passNumber");
        }
        final int old;
        synchronized (this) {
            old = this.passNumber;
            this.passNumber = passNumber;
        }
        propertyChangeSupport.firePropertyChange(PASS_NUMBER, old, passNumber);
        return this;
    }

    @Override
    public Actor setPassNumber(final int passNumber) {
        if (passNumber > 127) {
            throw new IllegalArgumentException("passNumber");
        }
        setPassNumber((byte) passNumber);
        return this;
    }

    @Override
    public Actor setThickness(final float thickness) {
        if (Float.compare(thickness, 0.0f) < 0) {
            throw new IllegalArgumentException("thickness");
        }
        final double old;
        synchronized (this) {
            old = this.thickness;
            this.thickness = thickness;
        }
        propertyChangeSupport.firePropertyChange(THICKNESS, old, thickness);
        return this;
    }

    @Override
    public Actor setType(final String type) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        final String old;
        synchronized (this) {
            old = this.type;
            this.type = type;
        }
        propertyChangeSupport.firePropertyChange(TYPE, old, type);
        return this;
    }

    @Override
    public Actor setVisible(final boolean visible) {
        final boolean old;
        synchronized (this) {
            old = isVisible();
            actorState.setVisible(visible);
        }
        propertyChangeSupport.firePropertyChange(VISIBLE, old, visible);
        return this;
    }

    @Override
    public Actor setWire(final boolean wire) {
        final boolean old;
        synchronized (this) {
            old = isWire();
            actorState.setWire(wire);
        }
        propertyChangeSupport.firePropertyChange(WIRE, old, wire);
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "id=" + getId() + "type=" + getType() + "visible=" + isVisible() + '}';
    }

    private void setRoot(final boolean root) {
        final boolean old;
        synchronized (this) {
            old = isRoot();
            actorState.setRoot(root);
        }
        propertyChangeSupport.firePropertyChange(ROOT, old, root);
    }

    /**
     * Listener used to set the actor to dirty.
     */
    protected static class DirtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            final Actor actor = (Actor) evt.getSource();
            if (DIRTY.equals(evt.getPropertyName())) {
                if ((Boolean) evt.getNewValue()) {
                    actor.getScene().draw(actor.getDrawingPass());
                }
            } else {
                // set to debug to troubleshoot actors that constantly dirty the scene
                if (actor.getScene().getExtended().getSceneBuilder().isVerbose()) {
                    logger.log(Level.FINE, "{0}: Actor {1} was set to dirty by the property: {2}", new Object[]{actor.getScene(), evt.getSource(), evt.getPropertyName()});
                    logger.log(Level.FINER, "{0}: Old- {1} New- {2}", new Object[]{actor.getScene(), evt.getOldValue(), evt.getNewValue()});
                }
                // dirty the actor
                actor.setDirty(true);
            }
        }
    }
}
