package gov.pnnl.svf.swt.service;

import com.jogamp.opengl.swt.GLCanvas;
import gov.pnnl.svf.core.service.AbstractCursorService;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.core.service.CursorType;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.swt.scene.SwtScene;
import gov.pnnl.svf.swt.util.SwtKeyUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * SWT implementation of the cursor service.
 *
 * @author Arthur Bleeker
 */
public class SwtCursorService extends AbstractCursorService<Cursor> implements PropertyChangeListener, KeyListener, MouseTrackListener {

    private static final Logger logger = Logger.getLogger(SwtCursorService.class.getName());

    private final ShowCursorRunnable showCursor;
    private final SwtKeyUtil keyUtil;
    private final Scene scene;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public SwtCursorService(final SwtScene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        showCursor = new ShowCursorRunnable(this, scene.getComponent());
        final GLCanvas root = scene.getComponent();
        this.keyUtil = new SwtKeyUtil(root);
        pcs.addPropertyChangeListener(this);
    }

    @Override
    public void initialize() {
        final Display display = Display.getCurrent();
        if (!display.isDisposed()) {
            loadCursor(display, CursorType.SHIFT, CURSOR_ADD_RESOURCE, 1, 3);
            loadCursor(display, CursorType.CTRL, CURSOR_TOGGLE_RESOURCE, 1, 3);
            loadCursor(display, CursorType.ALT, CURSOR_REMOVE_RESOURCE, 1, 3);
            loadCursor(display, CursorType.CTRL_ALT, CURSOR_INTERSECTION_RESOURCE, 1, 3);
            setCursor(CursorType.DRAGGING, display.getSystemCursor(SWT.CURSOR_HAND));
            setCursor(CursorType.ZOOMING, display.getSystemCursor(SWT.CURSOR_HAND));
            setCursor(CursorType.DEFAULT, display.getSystemCursor(SWT.CURSOR_ARROW));
        }
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
    public void mouseEnter(final MouseEvent evt) {
        updateKeyCursorTypes();
    }

    @Override
    public void mouseExit(final MouseEvent evt) {
        removeActiveCursorType(CursorType.SHIFT);
        removeActiveCursorType(CursorType.CTRL);
        removeActiveCursorType(CursorType.ALT);
    }

    @Override
    public void mouseHover(final MouseEvent evt) {
        // no operation
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

    private void loadCursor(final Display display, final CursorType cursorType, final String url, final int x, final int y) {
        try (final InputStream inputStream = CursorService.class.getClassLoader().getResourceAsStream(url)) {
            final Image image = new Image(display, inputStream);
            final Cursor cursor = new Cursor(display, image.getImageData(), x, y);
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

        private final SwtCursorService service;
        private final GLCanvas component;

        /**
         * Constructor
         *
         * @param service   reference to the busy service
         * @param component reference to the component
         */
        protected ShowCursorRunnable(final SwtCursorService service, final GLCanvas component) {
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
            if (!component.isDisposed()) {
                component.setCursor(cursor);
            }
        }
    }
}
