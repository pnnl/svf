package gov.pnnl.svf.newt.service;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import gov.pnnl.svf.core.service.AbstractCursorService;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.core.service.CursorType;
import gov.pnnl.svf.newt.scene.NewtScene;
import gov.pnnl.svf.scene.Scene;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Newt implementation of the cursor service.
 *
 * @author Amelia Bleeker
 */
public class NewtCursorService extends AbstractCursorService<Cursor> implements PropertyChangeListener, KeyListener, MouseListener {

    private static final Logger logger = Logger.getLogger(NewtCursorService.class.getName());

    private final ShowCursorRunnable showCursor;
    private final Scene scene;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public NewtCursorService(final NewtScene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        showCursor = new ShowCursorRunnable(this, scene.getComponent());
        pcs.addPropertyChangeListener(this);
    }

    @Override
    public void initialize() {
        loadCursor(CursorType.SHIFT, CURSOR_ADD_RESOURCE, 1, 3);
        loadCursor(CursorType.CTRL, CURSOR_TOGGLE_RESOURCE, 1, 3);
        loadCursor(CursorType.ALT, CURSOR_REMOVE_RESOURCE, 1, 3);
        loadCursor(CursorType.CTRL_ALT, CURSOR_INTERSECTION_RESOURCE, 1, 3);
        setCursor(CursorType.DRAGGING, Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setCursor(CursorType.ZOOMING, Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setCursor(CursorType.DEFAULT, Cursor.getDefaultCursor());
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        scene.getFactory().runOnUiThread(scene, showCursor);
    }

    @Override
    public void keyPressed(final KeyEvent evt) {
        updateKeyCursorTypes();
    }

    @Override
    public void keyReleased(final KeyEvent evt) {
        updateKeyCursorTypes();
    }

    @Override
    public void mouseClicked(final MouseEvent evt) {
        // no operation
    }

    @Override
    public void mousePressed(final MouseEvent evt) {
        // no operation
    }

    @Override
    public void mouseReleased(final MouseEvent evt) {
        // no operation
    }

    @Override
    public void mouseEntered(final MouseEvent evt) {
        updateKeyCursorTypes();
    }

    @Override
    public void mouseExited(final MouseEvent evt) {
        removeActiveCursorType(CursorType.SHIFT);
        removeActiveCursorType(CursorType.CTRL);
        removeActiveCursorType(CursorType.ALT);
    }

    @Override
    public void mouseMoved(final MouseEvent evt) {
        // no operation
    }

    @Override
    public void mouseDragged(final MouseEvent evt) {
        // no operation
    }

    @Override
    public void mouseWheelMoved(final MouseEvent evt) {
        // no operation
    }

    private Component getRoot(final Component component) {
        final Component parent = component.getParent();
        if (parent != null) {
            return getRoot(parent);
        } else {
            return component;
        }
    }

    private void loadCursor(final CursorType cursorType, final String url, final int x, final int y) {
        try (final InputStream inputStream = CursorService.class.getClassLoader().getResourceAsStream(url)) {
            final BufferedImage bufferedImage = ImageIO.read(inputStream);
            final Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(bufferedImage, new Point(x, y), url);
            setCursor(cursorType, cursor);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Unable to load the cursor type {0} from URL {1}.", new Object[]{cursorType, url});
        }
    }

    private void updateKeyCursorTypes() {
        // TODO update key cursor types not implemented
    }

    /**
     * Runnable that will show the active cursor.
     *
     * @author Amelia Bleeker
     */
    protected static class ShowCursorRunnable implements Runnable {

        private final NewtCursorService service;
        private final GLWindow component;

        /**
         * Constructor
         *
         * @param service   reference to the busy service
         * @param component reference to the component
         */
        protected ShowCursorRunnable(final NewtCursorService service, final GLWindow component) {
            if (service == null) {
                throw new NullPointerException("service");
            }
            if (component == null) {
                throw new NullPointerException("component");
            }
            this.service = service;
            this.component = component;
        }

        @Override
        public void run() {
            final CursorType cursorType = service.getActiveCursorType();
            final Cursor cursor = cursorType != null ? service.getCursor(cursorType) : null;
            // currently no way to set the cursor in NEWT
//            component.setCursor(cursor);
        }
    }
}
