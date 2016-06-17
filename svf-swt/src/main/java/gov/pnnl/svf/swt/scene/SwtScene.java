package gov.pnnl.svf.swt.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.swt.GLCanvas;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.SceneBuilder;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;

/**
 * <p>
 * This default implementation provides a dragging and color picking camera.</p>
 * <p>
 * Lists of actors and references to cameras should not be kept in subclasses.
 * These objects should be located using the lookup (inversion of control). This
 * allows flexibility in that other classes can add and remove actors and
 * cameras during runtime. It also allows for a single place of reference for
 * access to these objects.</P>
 *
 * @author Arthur Bleeker
 */
public class SwtScene extends AbstractScene<GLCanvas> {

    private static final Logger logger = Logger.getLogger(SwtScene.class.getName());
    protected final SwtListener swtListener = new SwtListener(this);

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public SwtScene(final GLCanvas component, final SceneBuilder builder) {
        super(new SwtSceneFactory(), component, builder);
        initialize();
    }

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param id        the unique id for the scene
     *
     * @throws IllegalArgumentException if component is not a supported type
     * @throws IllegalArgumentException if id is empty
     */
    public SwtScene(final GLCanvas component, final SceneBuilder builder, final String id) {
        super(new SwtSceneFactory(), component, builder, DEFAULT_TYPE, id);
    }

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param type      the type identifier for scene
     * @param id        the unique id for the scene
     *
     * @throws IllegalArgumentException if component is not a supported type
     * @throws IllegalArgumentException if type or id are empty
     */
    public SwtScene(final GLCanvas component, final SceneBuilder builder, final String type, final String id) {
        super(new SwtSceneFactory(), component, builder, type, id);
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @return a new SwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static SwtScene newInstance(final GLCanvas component, final SceneBuilder builder) {
        final SwtScene scene = new SwtScene(component, builder);
        scene.start();
        return scene;
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param id        the unique id for the scene
     *
     * @return a new SwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static SwtScene newInstance(final GLCanvas component, final SceneBuilder builder, final String id) {
        final SwtScene scene = new SwtScene(component, builder, id);
        scene.start();
        return scene;
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     * @param type      the type identifier for scene
     * @param id        the unique id for the scene
     *
     * @return a new SwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static SwtScene newInstance(final GLCanvas component, final SceneBuilder builder, final String type, final String id) {
        final SwtScene scene = new SwtScene(component, builder, type, id);
        scene.start();
        return scene;
    }

    @Override
    public void initialize(final GL2 gl) {
        // no operation
    }

    @Override
    public void dispose() {
        getFactory().runOnUiThread(this, new Runnable() {
                               @Override
                               public void run() {
                                   // remove the resize behavior
                                   try {
                                       getComponent().removeControlListener(swtListener);
                                       getComponent().removeDisposeListener(swtListener);
                                       getComponent().removePaintListener(swtListener);
                                       getComponent().getShell().removeShellListener(swtListener);
                                   } catch (final RuntimeException ex) {
                                       if (getSceneBuilder().isVerbose()) {
                                           logger.log(Level.WARNING, MessageFormat.format("{0}: Exception while removing shell listener.", SwtScene.this), ex);
                                       }
                                   }
                               }
                           });
        super.dispose();
    }

    /**
     * Reshape the scene to fit the component bounds.
     */
    protected void reshape() {
        try {
            final Rectangle bounds = SwtScene.this.getComponent().getBounds();
            final gov.pnnl.svf.geometry.Rectangle viewport = new gov.pnnl.svf.geometry.Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
            sceneUtil.setViewport(viewport);
        } catch (final RuntimeException ex) {
            if (getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Exception while setting viewport.", SwtScene.this), ex);
            }
        }
    }

    private void initialize() {
        // add the resize and other swt behavior
        getComponent().addControlListener(swtListener);
        getComponent().addDisposeListener(swtListener);
        getComponent().addPaintListener(swtListener);
        getComponent().getShell().addShellListener(swtListener);
    }

    /**
     * SWT listeners
     */
    protected static class SwtListener extends ShellAdapter implements ControlListener, DisposeListener, PaintListener {

        protected final SwtScene scene;

        /**
         * Constructor
         *
         * @param scene reference to the scene
         */
        protected SwtListener(final SwtScene scene) {
            this.scene = scene;
        }

        @Override
        public void shellActivated(final ShellEvent se) {
            scene.draw();
        }

        @Override
        public void controlMoved(final ControlEvent ce) {
            scene.draw();
        }

        @Override
        public void controlResized(final ControlEvent ce) {
            scene.reshape();
        }

        @Override
        public void widgetDisposed(final DisposeEvent de) {
            // do nothing
        }

        @Override
        public void paintControl(final PaintEvent pe) {
            scene.draw();
        }
    }
}
