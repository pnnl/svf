package gov.pnnl.svf.demo.newt;

import com.jogamp.newt.opengl.GLWindow;
import gov.pnnl.svf.demo.Demo;
import gov.pnnl.svf.demo.ShapeDemoLoader;
import gov.pnnl.svf.newt.scene.NewtScene;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import java.lang.reflect.Constructor;

/**
 *
 * @author Arthur Bleeker
 */
public class NewtShapeDemo extends AbstractNewtDemo {

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
    public AbstractScene<GLWindow> createScene(final GLWindow window, final SceneBuilder builder) {
        final NewtScene scene = new NewtScene(window, builder) {
            @Override
            public void load() {
                final ShapeDemoLoader loader = new ShapeDemoLoader();
                loader.load(this);
            }
        };
        return scene;
    }
}
