package gov.pnnl.svf.util;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneExt;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Helper class used to encapsulate the functionality for getting scene
 * coordinates. This class is not thread safe.
 *
 * @author Arthur Bleeker
 */
public class SceneCoordsHelper {

    private static final Logger logger = Logger.getLogger(SceneCoordsHelper.class.getName());
    private final SceneExt scene;
    private final double[] window = new double[3];
    private final FloatBuffer z = FloatBuffer.allocate(1);
    private final double[] position = new double[3];
    private final int[] viewport = new int[4];
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
     * Constructor
     *
     * @param scene reference to the scene
     *
     * @throws NullPointerException if the scene argument is null
     */
    public SceneCoordsHelper(final Scene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene.getExtended();
    }

    /**
     * Get the location of the pick in scene coordinates. The depth buffer must
     * be utilized for this function to work. The GL context must be current.
     *
     * @param gl  reference to gl
     * @param glu reference to glu
     * @param x   the x screen location
     * @param y   the y screen location
     *
     * @return the location of the pick or zero vector if it failed
     */
    public Vector3D unProject(final GL2 gl, final GLUgl2 glu, final int x, final int y) {
        z.clear();
        gl.glGetDoublev(GLMatrixFunc.GL_MODELVIEW_MATRIX, modelview, 0);
        gl.glGetDoublev(GLMatrixFunc.GL_PROJECTION_MATRIX, projection, 0);
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
        window[0] = x;
        window[1] = y;
        gl.glReadPixels(x, y, 1, 1, GL2ES2.GL_DEPTH_COMPONENT, GL.GL_FLOAT, z);
        window[2] = z.get();
        return unProject(window[0], window[1], window[2], modelview, projection, viewport);
    }

    /**
     * Un-project the screen coordinates to scene coordinates without the GL
     * context being current.
     *
     * @param x          the x location on the screen
     * @param y          the y location on the screen
     * @param z          the z depth in the scene
     * @param modelview  the modelview matrix
     * @param projection the projection matrix
     * @param viewport   the viewport (x, y, width, height)
     *
     * @return the unprojected scene coordinate or zero vector if failed
     */
    public Vector3D unProject(final double x, final double y, final double z, final double[] modelview, final double[] projection, final int[] viewport) {
        synchronized (this) {
            if (scene.getGLU().gluUnProject(x, viewport[3] - y, z, modelview, 0, projection, 0, viewport, 0, position, 0)) {
                return new Vector3D(position[0], position[1], position[2]);
            } else {
                if (scene.getSceneBuilder().isVerbose()) {
                    logger.log(Level.WARNING, "Unable to unproject the scene pick.");
                }
                return Vector3D.ZERO;
            }
        }
    }

    /**
     * Un-project the screen coordinates to scene coordinates without the GL
     * context being current.
     *
     * @param x          the x location on the screen
     * @param y          the y location on the screen
     * @param z          the z depth in the scene
     * @param modelview  the modelview matrix
     * @param projection the projection matrix
     * @param viewport   the viewport (x, y, width, height)
     *
     * @return the unprojected scene coordinate or zero vector if failed
     */
    public Vector3D unProject(final float x, final float y, final float z, final double[] modelview, final double[] projection, final int[] viewport) {
        synchronized (this) {
            if (scene.getGLU().gluUnProject(x, viewport[3] - y, z, modelview, 0, projection, 0, viewport, 0, position, 0)) {
                return new Vector3D(position[0], position[1], position[2]);
            } else {
                if (scene.getSceneBuilder().isVerbose()) {
                    logger.log(Level.WARNING, "Unable to unproject the scene pick.");
                }
                return Vector3D.ZERO;
            }
        }
    }
}
