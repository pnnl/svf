package gov.pnnl.svf.demo.jbox2d;

import gov.pnnl.svf.demo.awt.AbstractAwtDemo;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.test.AbstractDemoHelper;
import java.text.MessageFormat;
import com.jogamp.opengl.awt.GLCanvas;
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
public class JBox2dDemoTest extends AbstractDemoHelper<AbstractAwtDemo, JFrame, SceneBuilder, AbstractScene<GLCanvas>> {

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
    public void testJBox2dDistanceJointDemoDispose() throws InterruptedException {
        demo = new JBox2dDistanceJointDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Test
    public void testJBox2dPhysicsDemoDispose() throws InterruptedException {
        demo = new JBox2dPhysicsDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Test
    public void testJBox2dRevoluteJointDemoDispose() throws InterruptedException {
        demo = new JBox2dRevoluteJointDemo();
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
