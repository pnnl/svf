package gov.pnnl.svf.demo.jbox2d;

import com.jogamp.opengl.awt.GLCanvas;
import gov.pnnl.svf.demo.AbstractDemo;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.util.StringUtil;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Abstract class for a JBox2d demo.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractJBox2dDemo extends AbstractDemo<JFrame, GLCanvas> {

    //    private static final Logger logger = Logger.getLogger(AbstractJBox2dDemo.class.getName());
    @Override
    public void startDemo(final JFrame window, final AbstractScene<GLCanvas> scene) {
        window.add(scene.getComponent(), BorderLayout.CENTER);
        window.validate();
        window.setTitle("Scientific Visualization Framework " + StringUtil.getHumanReadableName(scene));
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(final WindowEvent we) {
                scene.dispose();
            }

            @Override
            public void windowClosing(final WindowEvent we) {
                scene.dispose();
            }

        });
        scene.start();
    }

    @Override
    public void runDemo(final boolean debug) {
        final SceneBuilder builder = createBuilder(debug);
        final JFrame window = createWindow(builder);
        final AbstractScene<GLCanvas> scene = createScene(window, builder);
        startDemo(window, scene);
    }

    @Override
    public JFrame createWindow(final SceneBuilder builder) {
        final JFrame frame = new JFrame();
        frame.setSize(WINDOW_SIZE_WIDTH, WINDOW_SIZE_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        return frame;
    }
}
