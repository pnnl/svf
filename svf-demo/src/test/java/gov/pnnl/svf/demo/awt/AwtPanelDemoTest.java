package gov.pnnl.svf.demo.awt;

import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.test.AbstractDemoHelper;
import java.text.MessageFormat;
import com.jogamp.opengl.awt.GLJPanel;
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
public class AwtPanelDemoTest extends AbstractDemoHelper<AbstractAwtPanelDemo, JFrame, SceneBuilder, AbstractScene<GLJPanel>> {

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
        demo = new AwtPanelShapeDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Test
    public void testTextDemoDispose() throws InterruptedException {
        demo = new AwtPanelTextDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Test
    public void testPickingDemoDispose() throws InterruptedException {
        demo = new AwtPanelPickingDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Override
    protected void disposeScene() {
        if (scene != null) {
            scene.dispose();
        }
        if (window != null && scene != null) {
            window.remove(scene.getComponent());
        }
        if (window != null) {
            window.dispose();
        }
    }
}
