package gov.pnnl.svf.awt.scene;

import com.jogamp.opengl.awt.GLCanvas;
import gov.pnnl.svf.awt.geometry.AwtRectangle;
import gov.pnnl.svf.geometry.Rectangle;
import static gov.pnnl.svf.scene.AbstractScene.DEFAULT_TYPE;
import gov.pnnl.svf.scene.SceneBuilder;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

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
public class AwtScene extends AbstractAwtScene<GLCanvas> {

    private static final Logger logger = Logger.getLogger(AwtScene.class.toString());
    protected final AwtListener awtListener = new AwtListener(this);

    /**
     * Constructor
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public AwtScene(final GLCanvas component, final SceneBuilder builder) {
        super(component, builder);
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
    public AwtScene(final GLCanvas component, final SceneBuilder builder, final String id) {
        super(component, builder, DEFAULT_TYPE, id);
        initialize();
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
    public AwtScene(final GLCanvas component, final SceneBuilder builder, final String type, final String id) {
        super(component, builder, type, id);
        initialize();
    }

    /**
     * Convenience constructor that creates and starts a new scene.
     *
     * @param component the component that's used to render this scene
     * @param builder   The builder object used for scene defaults.
     *
     * @return a new AwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static AwtScene newInstance(final GLCanvas component, final SceneBuilder builder) {
        final AwtScene scene = new AwtScene(component, builder);
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
     * @return a new AwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static AwtScene newInstance(final GLCanvas component, final SceneBuilder builder, final String id) {
        final AwtScene scene = new AwtScene(component, builder, id);
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
     * @return a new AwtScene
     *
     * @throws IllegalArgumentException if component is not a supported type
     */
    public static AwtScene newInstance(final GLCanvas component, final SceneBuilder builder, final String type, final String id) {
        final AwtScene scene = new AwtScene(component, builder, type, id);
        scene.start();
        return scene;
    }

    @Override
    public void dispose() {
        getFactory().runOnUiThread(this, new Runnable() {
                               @Override
                               public void run() {
                                   // remove the resize behavior
                                   try {
                                       final Window window = (Window) SwingUtilities.getRoot(getComponent());
                                       window.removeWindowStateListener(awtListener);
                                   } catch (final RuntimeException ex) {
                                       logger.log(Level.WARNING, MessageFormat.format("{0}: Exception while removing window listener.", AwtScene.this), ex);
                                   }
                               }
                           });
        super.dispose();
    }

    @Override
    public Rectangle getBounds() {
        return new AwtRectangle(getComponent().getBounds());
    }

    private void initialize() {
        getComponent().addHierarchyListener(awtListener);
        // add the resize and other awt behavior
        final Window window = (Window) SwingUtilities.getRoot(getComponent());
        if (window != null) {
            window.addWindowStateListener(awtListener);
        }
    }

    /**
     * AWT listeners
     */
    protected class AwtListener implements WindowStateListener, HierarchyListener {

        private final AwtScene scene;

        protected AwtListener(final AwtScene scene) {
            this.scene = scene;
        }

        @Override
        public void windowStateChanged(final WindowEvent event) {
            scene.draw();
        }

        @Override
        public void hierarchyChanged(final HierarchyEvent event) {
            // remove old listener
            final Window old = (Window) SwingUtilities.getRoot(event.getChangedParent());
            if (old != null) {
                old.removeWindowStateListener(this);
            }
            // add new listener
            final Window window = (Window) SwingUtilities.getRoot(getComponent());
            if (window != null) {
                window.removeWindowStateListener(this);
                window.addWindowStateListener(this);
            }
        }
    }
}
