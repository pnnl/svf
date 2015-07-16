package gov.pnnl.svf.demo.swt;

import com.jogamp.opengl.swt.GLCanvas;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.test.AbstractDemoHelper;
import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 *
 * @author Arthur Bleeker
 */
public class SwtDemoTest extends AbstractDemoHelper<AbstractSwtDemo, Shell, SceneBuilder, AbstractScene<GLCanvas>> {

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
        demo = new SwtShapeDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        // let it initialize and display
        final Timer stop = new Timer();
        stop.schedule(new TimerTask() {
            @Override
            public void run() {
                window.getDisplay().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        window.close();
                    }
                });
            }
        }, BEGIN_WAIT_TIME);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Test
    public void testTextDemoDispose() throws InterruptedException {
        demo = new SwtTextDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        // let it initialize and display
        final Timer stop = new Timer();
        stop.schedule(new TimerTask() {
            @Override
            public void run() {
                window.getDisplay().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        window.close();
                    }
                });
            }
        }, BEGIN_WAIT_TIME);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Test
    public void testPickingDemoDispose() throws InterruptedException {
        demo = new SwtPickingDemo();
        builder = demo.createBuilder(DEBUG);
        window = demo.createWindow(builder);
        scene = demo.createScene(window, builder);
        // let it initialize and display
        final Timer stop = new Timer();
        stop.schedule(new TimerTask() {
            @Override
            public void run() {
                window.getDisplay().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        window.close();
                    }
                });
            }
        }, BEGIN_WAIT_TIME);
        demo.startDemo(window, scene);
        // do leak check
        memLeakCheck();
    }

    @Override
    protected void disposeScene() {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                // dispose
                if (scene != null) {
                    scene.dispose();
                }
                if (window != null) {
                    for (final Control control : window.getChildren()) {
                        control.dispose();
                    }
                }
                if (window != null) {
                    window.dispose();
                }
            }
        });
    }
}
