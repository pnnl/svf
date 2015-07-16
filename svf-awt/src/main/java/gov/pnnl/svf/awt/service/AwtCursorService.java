package gov.pnnl.svf.awt.service;

import gov.pnnl.svf.awt.scene.AwtPanelScene;
import gov.pnnl.svf.awt.scene.AwtScene;
import gov.pnnl.svf.awt.util.AwtKeyUtil;
import gov.pnnl.svf.core.service.AbstractCursorService;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.core.service.CursorType;
import gov.pnnl.svf.scene.Scene;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * AWT implementation of the cursor service.
 *
 * @author Arthur Bleeker
 */
public class AwtCursorService extends AbstractCursorService<Cursor> implements PropertyChangeListener, KeyListener, MouseListener {

    private static final Logger logger = Logger.getLogger(AwtCursorService.class.getName());

    private final ShowCursorRunnable showCursor;
    private final AwtKeyUtil keyUtil;
    private final Scene scene;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public AwtCursorService(final AwtScene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        showCursor = new ShowCursorRunnable(this, scene.getComponent());
        this.keyUtil = new AwtKeyUtil();
        pcs.addPropertyChangeListener(this);
    }

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public AwtCursorService(final AwtPanelScene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        showCursor = new ShowCursorRunnable(this, scene.getComponent());
        this.keyUtil = new AwtKeyUtil();
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
    public void keyTyped(final KeyEvent evt) {
        // no operation
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

    /**
     * Update all of the cursor types according to the current key state.
     */
    private void updateKeyCursorTypes() {
        if (keyUtil.isShiftDown()) {
            addActiveCursorType(CursorType.SHIFT);
        } else {
            removeActiveCursorType(CursorType.SHIFT);
        }
        if (keyUtil.isControlDown()) {
            addActiveCursorType(CursorType.CTRL);
        } else {
            removeActiveCursorType(CursorType.CTRL);
        }
        if (keyUtil.isAltDown()) {
            addActiveCursorType(CursorType.ALT);
        } else {
            removeActiveCursorType(CursorType.ALT);
        }
    }

    private void loadCursor(final CursorType cursorType, final String url, final int x, final int y) {
        try (final InputStream inputStream = CursorService.class.getClassLoader().getResourceAsStream(url)) {
            final BufferedImage bufferedImage = ImageIO.read(inputStream);
            final Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(bufferedImage, new Point(x, y), url);
            setCursor(cursorType, cursor);
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to load the cursor type {0} from URL {1}.", new Object[]{cursorType, url});
        }
    }

    /**
     * Runnable that will show the active cursor.
     *
     * @author Arthur Bleeker
     */
    protected static class ShowCursorRunnable implements Runnable {

        private final AwtCursorService service;
        private final Component component;

        /**
         * Constructor
         *
         * @param service   reference to the busy service
         * @param component reference to the component
         */
        protected ShowCursorRunnable(final AwtCursorService service, final Component component) {
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
            component.setCursor(cursor);
        }
    }
}
