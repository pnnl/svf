package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support for objects that can be transformed in a GLScene.
 *
 * @author Arthur Bleeker
 *
 */
public class TransformSupport extends AbstractSupport<Object> {

    /**
     * String representation of a field in this object.
     */
    public static final String TRANSLATION = "translation";
    /**
     * String representation of a field in this object.
     */
    public static final String ROTATION_AXIS = "rotationAxis";
    /**
     * String representation of a field in this object.
     */
    public static final String ROTATION = "rotation";
    /**
     * String representation of a field in this object.
     */
    public static final String SCALE = "scale";
    /**
     * The default scale value.
     */
    public static final Vector3D DEFAULT_SCALE = new Vector3D(1.0, 1.0, 1.0);
    protected Vector3D translation = Vector3D.ZERO;
    protected Vector3D rotationAxis = Vector3D.ZERO;
    protected double rotation = 0.0;
    protected Vector3D scale = DEFAULT_SCALE;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The actor that owns this transform support object.
     */
    protected TransformSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Constructor
     *
     * @param actor The actor that owns this transform support object.
     *
     * @return a reference to the support object
     */
    public static TransformSupport newInstance(final Actor actor) {
        final TransformSupport instance = new TransformSupport(actor);
        actor.add(instance);
        return instance;
    }

    /**
     * @return the rotation
     */
    public double getRotation() {
        synchronized (this) {
            return rotation;
        }
    }

    /**
     * @return the rotationAxis
     */
    public Vector3D getRotationAxis() {
        synchronized (this) {
            return rotationAxis;
        }
    }

    /**
     * @return the scale
     */
    public Vector3D getScale() {
        synchronized (this) {
            return scale;
        }
    }

    /**
     * @return the translation
     */
    public Vector3D getTranslation() {
        synchronized (this) {
            return translation;
        }
    }

    /**
     * Pushes the transformations to the stack.
     *
     * @param gl     reference to GL
     * @param glu    reference to GLU
     * @param camera reference to the scene camera if it's a scene drawing pass,
     *               or null if not
     */
    public void pushTransform(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        gl.glPushMatrix();
        drawState.setMatrixModelview();
        synchronized (this) {
            gl.glTranslated(translation.getX(), translation.getY(), translation.getZ());
            gl.glRotated(rotation, rotationAxis.getX(), rotationAxis.getY(), rotationAxis.getZ());
            gl.glScaled(scale.getX(), scale.getY(), scale.getZ());
        }
    }

    /**
     * Pops the transformation from the stack.
     *
     * @param gl  reference to GL
     * @param glu reference to GLU
     */
    public void popTransform(final GL2 gl, final GLUgl2 glu) {
        if (drawState.isMatrixModelview()) {
            gl.glPopMatrix();
        }
    }

    /**
     * Pushes the transformations to the stack utilizing integers. This prevents
     * floating point translate and scale from causing problems with color
     * picking in orthographic rendering. Note that rotate may not work
     * correctly.
     *
     * @param gl     reference to GL
     * @param glu    reference to GLU
     * @param camera reference to the scene camera if it's a scene drawing pass,
     *               or null if not
     */
    public void pushTransformInt(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        gl.glPushMatrix();
        drawState.setMatrixModelview();
        synchronized (this) {
            gl.glTranslated(Math.round(translation.getX()), Math.round(translation.getY()), Math.round(translation.getZ()));
            gl.glRotated(Math.round(rotation), rotationAxis.getX(), rotationAxis.getY(), rotationAxis.getZ());
            gl.glScaled(Math.round(scale.getX()), Math.round(scale.getY()), Math.round(scale.getZ()));
        }
    }

    /**
     * Pops the transformation from the stack utilizing integers. This prevents
     * floating point translate and scale from causing problems with color
     * picking in orthographic rendering. Note that rotate may not work
     * correctly.
     *
     * @param gl  reference to GL
     * @param glu reference to GLU
     */
    public void popTransformInt(final GL2 gl, final GLUgl2 glu) {
        if (drawState.isMatrixModelview()) {
            gl.glPopMatrix();
        }
    }

    /**
     * @param rotation the rotation to set
     *
     * @return a reference to the support object
     */
    public TransformSupport setRotation(double rotation) {
        // validate method parameter
        while (rotation < 0.0) {
            rotation += 360.0;
        }
        while (rotation >= 360.0) {
            rotation -= 360.0;
        }
        final double old;
        synchronized (this) {
            old = this.rotation;
            this.rotation = rotation;
        }
        getPropertyChangeSupport().firePropertyChange(ROTATION, old, rotation);
        return this;
    }

    /**
     * @param rotationAxis the rotationAxis to set
     *
     * @return a reference to the support object
     */
    public TransformSupport setRotationAxis(Vector3D rotationAxis) {
        if (rotationAxis == null) {
            throw new NullPointerException("rotationAxis");
        }
        // validate method parameter
        if (Double.compare(rotationAxis.getNorm1(), 0.0) != 0) {
            rotationAxis = rotationAxis.normalize();
        }
        final Vector3D old;
        synchronized (this) {
            old = this.rotationAxis;
            this.rotationAxis = rotationAxis;
        }
        getPropertyChangeSupport().firePropertyChange(ROTATION_AXIS, old, rotationAxis);
        return this;
    }

    /**
     * @param scale the scale to set
     *
     * @return a reference to the support object
     */
    public TransformSupport setScale(final Vector3D scale) {
        if (scale == null) {
            throw new NullPointerException("scale");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.scale;
            this.scale = scale;
        }
        getPropertyChangeSupport().firePropertyChange(SCALE, old, scale);
        return this;
    }

    /**
     * @param translation the translation to set
     *
     * @return a reference to the support object
     */
    public TransformSupport setTranslation(final Vector3D translation) {
        if (translation == null) {
            throw new NullPointerException("translation");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.translation;
            this.translation = translation;
        }
        getPropertyChangeSupport().firePropertyChange(TRANSLATION, old, translation);
        return this;
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "TransformSupport{" + "translation=" + translation + "rotationAxis=" + rotationAxis + "rotation=" + rotation + "scale=" + scale + '}';
        }
    }
}
