package gov.pnnl.svf.demo.newt;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import gov.pnnl.svf.demo.AbstractDemo;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.util.StringUtil;

/**
 * Abstract class for an NEWT demo.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractNewtDemo extends AbstractDemo<GLWindow, GLWindow> {

    //    private static final Logger logger = Logger.getLogger(AbstractNewtDemo.class.getName());
    @Override
    public void startDemo(final GLWindow window, final AbstractScene<GLWindow> scene) {
        window.setTitle("Scientific Visualization Framework " + StringUtil.getHumanReadableName(scene));
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowDestroyNotify(final WindowEvent we) {
                scene.dispose();
            }
        });
        scene.start();
    }

    @Override
    public void runDemo(final boolean debug) {
        final SceneBuilder builder = createBuilder(debug);
        final GLWindow window = createWindow(builder);
        final AbstractScene<GLWindow> scene = createScene(window, builder);
        startDemo(window, scene);
    }

    @Override
    public GLWindow createWindow(final SceneBuilder builder) {
        final GLWindow window = GLWindow.create(builder.getGLCapabilities());
        window.setSize(WINDOW_SIZE_WIDTH, WINDOW_SIZE_HEIGHT);
        return window;
    }
}
