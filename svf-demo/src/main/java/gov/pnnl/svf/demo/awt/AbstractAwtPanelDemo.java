package gov.pnnl.svf.demo.awt;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.demo.AbstractDemo;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.update.Task;
import gov.pnnl.svf.update.WorkerUpdateTaskAdapter;
import gov.pnnl.svf.util.StringUtil;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Set;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

/**
 * Abstract class for an AWT panel demo.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractAwtPanelDemo extends AbstractDemo<JFrame, GLJPanel> {

    //    private static final Logger logger = Logger.getLogger(AbstractAwtPanelDemo.class.getName());
    @Override
    public void startDemo(final JFrame window, final AbstractScene<GLJPanel> scene) {
        window.add(scene.getComponent(), BorderLayout.CENTER);
        window.validate();
        window.setTitle("Scientific Visualization Framework " + StringUtil.getHumanReadableName(scene));
        window.setVisible(true);
        scene.start();
    }

    @Override
    public void runDemo(final boolean debug) {
        final SceneBuilder builder = createBuilder(debug);
        builder.setAuxiliaryBuffers(false);
        final JFrame window = createWindow(builder);
        final AbstractScene<GLJPanel> scene = createScene(window, builder);
        scene.getComponent().addKeyListener(new KeyAdapter() {
            private boolean wire = false;

            @Override
            public void keyTyped(final KeyEvent evt) {
                if (evt.getKeyChar() == 'p' || evt.getKeyChar() == 'P') {
                    scene.getScreenshot().capture(new File("screenshot.png"));
                } else if (evt.getKeyChar() == 'w' || evt.getKeyChar() == 'W') {
                    final boolean wire = this.wire = !this.wire;
                    scene.getDefaultTaskManager().schedule(new WorkerUpdateTaskAdapter() {

                        @Override
                        public void run(final Task task) {
                            final Set<Actor> actors = scene.lookupAll(Actor.class);
                            for (final Actor actor : actors) {
                                actor.setWire(wire);
                            }
                        }
                    });
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                scene.dispose();
            }
        }, "Shutdown"));
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
