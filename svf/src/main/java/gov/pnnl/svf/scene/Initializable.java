package gov.pnnl.svf.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;

/**
 * Interface for classes that require initialization.
 *
 * @author Amelia Bleeker
 *
 */
public interface Initializable extends SceneItem {

    /**
     * Method is called before rendering occurs outside of the start and end
     * block.
     *
     * @param gl  Reference to GL2.
     * @param glu Reference to GLU.
     */
    void initialize(GL2 gl, GLUgl2 glu);

    /**
     * @return true if the object has been initialized
     */
    boolean isInitialized();

    /**
     *
     * @return true if this initialize task is slow or long running
     */
    boolean isSlow();

    /**
     * Method is called after rendering occurs and object needs to be
     * un-initialized.
     *
     * @param gl  Reference to GL.
     * @param glu Reference to GLU.
     */
    void unInitialize(GL2 gl, GLUgl2 glu);
}
