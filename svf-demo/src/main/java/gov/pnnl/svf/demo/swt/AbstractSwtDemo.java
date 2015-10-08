package gov.pnnl.svf.demo.swt;

import com.jogamp.opengl.swt.GLCanvas;
import gov.pnnl.svf.demo.AbstractDemo;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.util.StringUtil;
import java.io.File;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Abstract class for an SWT demo.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractSwtDemo extends AbstractDemo<Shell, GLCanvas> {

    //    private static final Logger logger = Logger.getLogger(AbstractSwtDemo.class.getName());
    @Override
    public void startDemo(final Shell window, final AbstractScene<GLCanvas> scene) {
        window.setText("Scientific Visualization Framework " + StringUtil.getHumanReadableName(scene));
        window.open();
        scene.start();
        final Display display = window.getDisplay();
        while (!window.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        scene.dispose();
        display.dispose();
        window.dispose();
    }

    @Override
    public void runDemo(final boolean debug) {
        final SceneBuilder builder = createBuilder(debug);
        final Shell window = createWindow(builder);
        final AbstractScene<GLCanvas> scene = createScene(window, builder);
        scene.getComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(final KeyEvent evt) {
                if (evt.character == 'p' || evt.character == 'P') {
                    scene.getScreenshot().capture(new File("screenshot.png"));
                }
            }
        });
        startDemo(window, scene);
    }

    @Override
    public Shell createWindow(final SceneBuilder builder) {
        final Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        shell.setSize(WINDOW_SIZE_WIDTH, WINDOW_SIZE_HEIGHT);
        return shell;
    }
}
