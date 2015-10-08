package gov.pnnl.svf.demo.awt;

import com.jogamp.opengl.awt.GLCanvas;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.test.AbstractDemoHelper;
import java.text.MessageFormat;
import javax.swing.JFrame;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 *
 * @author Arthur Bleeker
 */
public class AwtDemoTest extends AbstractDemoHelper<AbstractAwtDemo, JFrame, SceneBuilder, AbstractScene<GLCanvas>> {

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(final Description description) {
            System.out.println(MessageFormat.format("Starting test {0}", description.getMethodName()));
        }

        @Override
        protected void finished(final Description description) {
            System.out.println(MessageFormat.format("Finished test {0}", description.getMethodName()));
        }
    };

    @Test
    public void testShapeDemoDispose() throws InterruptedException {
        demo = new AwtShapeDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Test
    public void testTextDemoDispose() throws InterruptedException {
        demo = new AwtTextDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Test
    public void testPickingDemoDispose() throws InterruptedException {
        demo = new AwtPickingDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Override
    protected void disposeScene() {
        if (window != null) {
            window.dispose();
        }
    }
}
