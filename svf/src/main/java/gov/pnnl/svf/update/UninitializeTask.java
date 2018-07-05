package gov.pnnl.svf.update;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Initializable;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneExt;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class used to uninitialize an object in the scene. Any time this class
 * is found the object will be uninitialized as soon as possible. This task will
 * get removed from the scene after it has been uninitialized.
 *
 * @author Amelia Bleeker
 */
public class UninitializeTask implements Initializable {

    private static final Logger logger = Logger.getLogger(UninitializeTask.class.toString());

    /**
     * Schedules an un-initialize task to be run during the next draw cycle.
     *
     * @param scene         The scene for this task.
     * @param initializable the object to un-initialize
     */
    public static void schedule(final Scene scene, final Initializable initializable) {
        final UninitializeTask task = new UninitializeTask(scene, initializable);
        scene.add(task);
    }

    private final SceneExt scene;
    private final Initializable initializable;

    private UninitializeTask(final Scene scene, final Initializable initializable) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        if (initializable == null) {
            throw new NullPointerException("initializable");
        }
        this.scene = scene.getExtended();
        this.initializable = initializable;
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        // we don't want to reinitialize this
//        initializable.initialize(gl, glu);
    }

    @Override
    public boolean isInitialized() {
        return initializable.isInitialized();
    }

    @Override
    public boolean isSlow() {
        return initializable.isSlow();
    }

    @Override
    public Scene getScene() {
        return initializable.getScene();
    }

    @Override
    public boolean isVisible() {
        return initializable.isVisible();
    }

    @Override
    public DrawingPass getDrawingPass() {
        return initializable.getDrawingPass();
    }

    @Override
    public void unInitialize(final GL2 gl, final GLUgl2 glu) {
        if (initializable.isInitialized()) {
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.FINE, "{0}: Uninitializing the scene object: {1}", new Object[]{getScene(), initializable});
            }
            initializable.unInitialize(gl, glu);
        }
        scene.remove(this);
    }

    @Override
    public String toString() {
        return "UninitializeTask{" + "initializable=" + initializable + '}';
    }
}
