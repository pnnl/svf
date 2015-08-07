package gov.pnnl.svf.demo.awt;

import gov.pnnl.svf.awt.scene.AwtScene;
import gov.pnnl.svf.demo.Demo;
import gov.pnnl.svf.demo.VolumeDemoLoader;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import java.lang.reflect.Constructor;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.JFrame;

/**
 *
 * @author Arthur Bleeker
 */
public class AwtVolumeDemo extends AbstractAwtDemo {

    /**
     * This main method must be copied into the final class for reflection to
     * work correctly.
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        boolean debug = false;
        for (final String string : args) {
            if ("debug".equals(string)) {
                debug = true;
            }
        }
        final Class<?> current = Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());
        final Constructor<?> constructor = current.getConstructor((Class<?>[]) null);
        final Demo<?, ?> demo = (Demo<?, ?>) constructor.newInstance((Object[]) null);
        demo.runDemo(debug);
    }

    @Override
    public AbstractScene<GLCanvas> createScene(final JFrame window, final SceneBuilder builder) {
        final GLCanvas component = new GLCanvas(builder.getGLCapabilities());
        final AwtScene scene = new AwtScene(component, builder) {

            @Override
            public void initialize(GL2 gl) {
                gl.glDisable(GL.GL_DEPTH_TEST);
            }

            @Override
            public void load() {
                final VolumeDemoLoader loader = new VolumeDemoLoader();
                loader.load(this);
            }

        };
        return scene;
    }
}
