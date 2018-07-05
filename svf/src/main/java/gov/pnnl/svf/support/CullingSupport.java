package gov.pnnl.svf.support;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Shape2DUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support object used to cull root actors in a scene according to the current
 * visible camera. This object uses location and radius to create a bounding
 * area. Set scale to zero to use a single point for a bounding area.
 * <p>
 * If a TransformSupport object exists then this object finds the location by
 * adding the offset to the translation. It determines the final radius by
 * multiplying the largest scale value with the supplied scale value. If no
 * TransformSupport object exists then this object utilizes the offset and scale
 * directly to determine location and radius.
 *
 * @author Amelia Bleeker
 */
public class CullingSupport extends AbstractSupport<Object> implements Drawable {

    /**
     * String representation of a field in this object.
     */
    public static final String OFFSET = "offset";
    /**
     * String representation of a field in this object.
     */
    public static final String SCALE = "scale";
    private static final float[] DEBUG_COLOR = new Color((byte) 255, (byte) 105, (byte) 180, (byte) 255).toRgbaArray();
    /**
     * listener used to respond to culling events
     */
    protected final CullingListener cullingListener;
    /**
     * listener used to respond to transform events
     */
    protected final TransformListener transformListener;
    /**
     * reference to actor transform
     */
    protected final TransformSupport transform;
    /**
     * the offset for the bounds
     */
    protected Vector3D offset = Vector3D.ZERO;
    /**
     * the scale for the bounds
     */
    protected double scale = 1.0;
    /**
     * the location of the bounds
     */
    protected Vector3D location = Vector3D.ZERO;
    /**
     * the radius of the bounds
     */
    protected double radius = 1.0;

    /**
     * Constructor protected to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor     The actor that owns this support object.
     * @param transform the transform support object used to determine final
     *                  offset and radius
     *
     * @throws NullPointerException if the actor is null
     */
    protected CullingSupport(final Actor actor, final TransformSupport transform) {
        super(actor);
        this.transform = transform;
        cullingListener = new CullingListener();
        if (transform != null) {
            transformListener = new TransformListener();
            transform.getPropertyChangeSupport().addPropertyChangeListener(TransformSupport.TRANSLATION, transformListener);
            transform.getPropertyChangeSupport().addPropertyChangeListener(TransformSupport.SCALE, transformListener);
            transformListener.propertyChange(new PropertyChangeEvent(transform, TransformSupport.TRANSLATION, null, transform.getTranslation()));
            transformListener.propertyChange(new PropertyChangeEvent(transform, TransformSupport.SCALE, null, transform.getScale()));
        } else {
            transformListener = null;
        }
        getPropertyChangeSupport().addPropertyChangeListener(cullingListener);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor The owning actor.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static CullingSupport newInstance(final Actor actor) {
        return CullingSupport.newInstance(actor, null);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor     The owning actor.
     * @param transform the transform support object used to determine final
     *                  offset and radius
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static CullingSupport newInstance(final Actor actor, final TransformSupport transform) {
        final CullingSupport instance = new CullingSupport(actor, transform);
        actor.add(instance);
        return instance;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (transform != null) {
            transform.getPropertyChangeSupport().removePropertyChangeListener(TransformSupport.TRANSLATION, transformListener);
            transform.getPropertyChangeSupport().removePropertyChangeListener(TransformSupport.SCALE, transformListener);
        }
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        final Vector3D offset = getOffset();
        final double scale = getScale();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);
        gl.glColor4fv(DEBUG_COLOR, 0);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, DEBUG_COLOR, 0);
        final Circle2D circle = new Circle2D(offset.getX(), offset.getY(), scale);
        final int vertices = Shape2DUtil.drawShape(gl, circle);
        gl.glPopAttrib();
        camera.getExtended().incrementVerticesCounter(vertices);
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
    }

    @Override
    public Scene getScene() {
        return getActor().getScene();
    }

    @Override
    public boolean isVisible() {
        return getActor().isVisible();
    }

    @Override
    public DrawingPass getDrawingPass() {
        return getActor().getDrawingPass();
    }

    /**
     * @return the offset
     */
    public Vector3D getOffset() {
        synchronized (this) {
            return offset;
        }
    }

    /**
     * @param offset the offset
     *
     * @return a reference to the support object
     */
    public CullingSupport setOffset(final Vector3D offset) {
        if (offset == null) {
            throw new NullPointerException("offset");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.offset;
            this.offset = offset;
        }
        getPropertyChangeSupport().firePropertyChange(OFFSET, old, offset);
        return this;
    }

    /**
     * @return the scale
     */
    public double getScale() {
        synchronized (this) {
            return scale;
        }
    }

    /**
     * @param scale the scale
     *
     * @return a reference to the support object
     */
    public CullingSupport setScale(final double scale) {
        if (scale < 0.0) {
            throw new IllegalArgumentException("scale");
        }
        final double old;
        synchronized (this) {
            old = this.scale;
            this.scale = scale;
        }
        getPropertyChangeSupport().firePropertyChange(SCALE, old, scale);
        return this;
    }

    /**
     * The location of the origin of this root actor in scene space.
     *
     * @return the location
     */
    public Vector3D getLocation() {
        synchronized (this) {
            return location;
        }
    }

    /**
     * The final radius of this root actor in scene space.
     *
     * @return the radius
     */
    public double getRadius() {
        synchronized (this) {
            return radius;
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "CullingSupport{" + "location=" + location + ", radius=" + radius + '}';
        }
    }

    /**
     * Listener used to respond to transform changes.
     */
    protected class TransformListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (TransformSupport.TRANSLATION.equals(evt.getPropertyName())) {
                final Vector3D value = (Vector3D) evt.getNewValue();
                synchronized (CullingSupport.this) {
                    location = value.add(offset);
                }
            } else if (TransformSupport.SCALE.equals(evt.getPropertyName())) {
                final Vector3D value = (Vector3D) evt.getNewValue();
                synchronized (CullingSupport.this) {
                    radius = Math.max(Math.max(value.getX(), value.getY()), value.getZ()) * scale;
                }
            }
        }
    }

    /**
     * Listener used to respond to culling support changes.
     */
    protected class CullingListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (CullingSupport.OFFSET.equals(evt.getPropertyName())) {
                if (transformListener != null) {
                    transformListener.propertyChange(new PropertyChangeEvent(transform, TransformSupport.TRANSLATION, null, transform.getTranslation()));
                } else {
                    synchronized (CullingSupport.this) {
                        location = offset;
                    }
                }
            } else if (CullingSupport.SCALE.equals(evt.getPropertyName())) {
                if (transformListener != null) {
                    transformListener.propertyChange(new PropertyChangeEvent(transform, TransformSupport.SCALE, null, transform.getScale()));
                } else {
                    synchronized (CullingSupport.this) {
                        radius = scale;
                    }
                }
            }
        }
    }
}
