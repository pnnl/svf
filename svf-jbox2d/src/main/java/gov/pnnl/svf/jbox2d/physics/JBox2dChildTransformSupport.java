package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.AbstractCamera;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.support.MatrixTransformSupport;
import gov.pnnl.svf.support.ParentSupport;
import gov.pnnl.svf.support.TransformSupport;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support for objects that can be transformed in a GLScene. This sub type
 * stores the transformation matrix and allows for additional functions
 * necessary for use in physics objects. The get function return the values of a
 * child as if it was a root actor in the scene.
 *
 * @author Arthur Bleeker
 *
 */
public class JBox2dChildTransformSupport extends MatrixTransformSupport {

    private final float[] matrix = new float[]{
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f};
    private boolean decomposed = false;
    private Vector3D translation = Vector3D.ZERO;
    private Vector3D scale = new Vector3D(1.0, 1.0, 1.0);
    private double rotation = 0.0;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The actor that owns this transform support object.
     */
    protected JBox2dChildTransformSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Constructor
     *
     * @param actor The actor that owns this transform support object.
     *
     * @return a reference to the support object
     */
    public static JBox2dChildTransformSupport newInstance(final Actor actor) {
        final JBox2dChildTransformSupport instance = new JBox2dChildTransformSupport(actor);
        actor.add(instance);
        return instance;
    }

    @Override
    public double getRotation() {
        synchronized (matrix) {
            if (!decomposed) {
                decompose();
            }
            return rotation;
        }
    }

    @Override
    public Vector3D getScale() {
        synchronized (matrix) {
            if (!decomposed) {
                decompose();
            }
            return scale;
        }
    }

    @Override
    public Vector3D getTranslation() {
        synchronized (matrix) {
            if (!decomposed) {
                decompose();
            }
            return translation;
        }
    }

    @Override
    public void pushTransform(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        super.pushTransform(gl, glu, camera);
        synchronized (matrix) {
            // get the current transformation matrix for this
            if (camera != null && camera instanceof AbstractCamera) {
                gl.glGetFloatv(GLMatrixFunc.GL_MODELVIEW_MATRIX, matrix, 0);
                decomposed = false;
            }
        }
    }

    private void decompose() {
        Vector3D oldTranslation;
        Vector3D oldScale;
        double oldRotation;
        Vector3D newTranslation;
        Vector3D newScale;
        double newRotation;
        synchronized (matrix) {
            // store the current values
            oldTranslation = translation;
            oldScale = scale;
            oldRotation = rotation;
            // XX cell 15 should be one

            // decompose the new ones
            // translation which takes into account previous scale and rotations on the new origin
            translation = new Vector3D(matrix[12], matrix[13], matrix[14]);
            // scale
            scale = new Vector3D(Math.sqrt(matrix[0] * matrix[0] + matrix[4] * matrix[4] + matrix[8] * matrix[8]),
                                 Math.sqrt(matrix[1] * matrix[1] + matrix[5] * matrix[5] + matrix[9] * matrix[9]),
                                 Math.sqrt(matrix[2] * matrix[2] + matrix[6] * matrix[6] + matrix[10] * matrix[10]));
            // rotation
            // this is in right hand coordinates for OpenGL
            // this has serious limitations because the rotation has to be a single consistent euler axis
            rotation = addRotations(getActor(), super.getRotation());
            // create references for property changes
            newTranslation = translation;
            newScale = scale;
            newRotation = rotation;
            decomposed = true;
        }
        if (!oldTranslation.equals(newTranslation)) {
            getPropertyChangeSupport().firePropertyChange(TRANSLATION, oldTranslation, newTranslation);
        }
        if (!oldScale.equals(newScale)) {
            getPropertyChangeSupport().firePropertyChange(SCALE, oldScale, newScale);
        }
        if (oldRotation != newRotation) {
            getPropertyChangeSupport().firePropertyChange(ROTATION, oldRotation, newRotation);
        }
    }

    private double addRotations(final Actor actor, double r) {
        if (actor != null) {
            final ParentSupport parent = actor.lookup(ParentSupport.class);
            if (parent != null) {
                final TransformSupport transform = parent.getParent().lookup(TransformSupport.class);
                if (transform != null) {
                    r += transform.getRotation();
                }
                return addRotations(parent.getParent(), r);
            }
        }
        return r;
    }
}
