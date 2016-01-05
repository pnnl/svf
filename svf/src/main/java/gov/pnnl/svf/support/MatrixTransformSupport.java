package gov.pnnl.svf.support;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.AbstractCamera;
import gov.pnnl.svf.camera.Camera;
import java.util.Arrays;

/**
 * Support for objects that can be transformed in a GLScene. This transform
 * support sub type stores the current transformation matrices.
 *
 * @author Arthur Bleeker
 *
 */
public class MatrixTransformSupport extends TransformSupport {

    private final double[] modelview = new double[]{
        1.0, 0.0, 0.0, 0.0,
        0.0, 1.0, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0};
    private final double[] projection = new double[]{
        1.0, 0.0, 0.0, 0.0,
        0.0, 1.0, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0};

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The actor that owns this transform support object.
     */
    protected MatrixTransformSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Constructor
     *
     * @param actor The actor that owns this transform support object.
     *
     * @return a reference to the support object
     */
    public static MatrixTransformSupport newInstance(final Actor actor) {
        final MatrixTransformSupport instance = new MatrixTransformSupport(actor);
        actor.add(instance);
        return instance;
    }

    /**
     * Get a copy of the last modelview matrix. Prefer getMatrix(double[]) for
     * most calls to reduce garbage.
     *
     * @param matrix matrix to fill with the last transformation matrix or null
     *               to get a copy of the matrix back
     *
     * @return a copy of the last transformation matrix or null if matrix was
     *         passed in
     */
    public double[] getModelviewMatrix(final double[] matrix) {
        synchronized (modelview) {
            if (matrix != null) {
                System.arraycopy(modelview, 0, matrix, 0, modelview.length);
                return matrix;
            } else {
                return Arrays.copyOf(modelview, modelview.length);
            }
        }
    }

    /**
     * Get a copy of the last projection matrix. Prefer getMatrix(double[]) for
     * most calls to reduce garbage.
     *
     * @param matrix matrix to fill with the last transformation matrix or null
     *               to get a copy of the matrix back
     *
     * @return a copy of the last transformation matrix or null if matrix was
     *         passed in
     */
    public double[] getProjectionMatrix(final double[] matrix) {
        synchronized (projection) {
            if (matrix != null) {
                System.arraycopy(projection, 0, matrix, 0, projection.length);
                return matrix;
            } else {
                return Arrays.copyOf(projection, projection.length);
            }
        }
    }

    @Override
    public void pushTransform(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        super.pushTransform(gl, glu, camera);
        // get the current transformation matrix for this
        if (camera != null && camera instanceof AbstractCamera) {
            synchronized (modelview) {
                gl.glGetDoublev(GLMatrixFunc.GL_MODELVIEW_MATRIX, modelview, 0);
            }
            synchronized (projection) {
                gl.glGetDoublev(GLMatrixFunc.GL_PROJECTION_MATRIX, projection, 0);
            }
        }
    }
}
