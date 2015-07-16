package gov.pnnl.svf.swt.service;

import com.jogamp.opengl.swt.GLCanvas;
import gov.pnnl.svf.core.service.AbstractBusyService;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.core.service.CursorType;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.swt.scene.SwtScene;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * SWT implementation of the busy service.
 *
 * @author Arthur Bleeker
 */
public class SwtBusyService extends AbstractBusyService {

    private final BusyRunnable showBusy;
    private final BusyRunnable hideBusy;
    private final Scene scene;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public SwtBusyService(final SwtScene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        final GLCanvas root = scene.getComponent();
        showBusy = new BusyRunnable(scene, root, true);
        hideBusy = new BusyRunnable(scene, root, false);
    }

    @Override
    protected void showBusy(final boolean value) {
        if (value) {
            scene.getFactory().runOnUiThread(scene, showBusy);
        } else {
            scene.getFactory().runOnUiThread(scene, hideBusy);
        }
    }

    /**
     * Runnable that will show or hide the busy cursor.
     *
     * @author Arthur Bleeker
     */
    protected static class BusyRunnable implements Runnable {

        private final Scene scene;
        private final GLCanvas component;
        private final boolean show;

        /**
         * Constructor
         *
         * @param scene     reference to the scene
         * @param component reference to the component
         * @param show      true to show busy
         */
        private BusyRunnable(final Scene scene, final GLCanvas component, final boolean show) {
            if (scene == null) {
                throw new NullPointerException("scene");
            }
            if (component == null) {
                throw new NullPointerException("component");
            }
            this.scene = scene;
            this.component = component;
            this.show = show;
        }

        @Override
        public void run() {
            final CursorService cursorService = scene.lookup(CursorService.class);
            if (cursorService != null && show) {
                cursorService.addActiveCursorType(CursorType.BUSY);
            } else if (cursorService != null && !show) {
                cursorService.removeActiveCursorType(CursorType.BUSY);
            } else if (show) {
                final Display display = Display.getCurrent();
                if (!display.isDisposed()) {
                    if (!component.isDisposed()) {
                        component.setCursor(display.getSystemCursor(SWT.CURSOR_WAIT));
                    }
                }
            } else { // if (!show) {
                final Display display = Display.getCurrent();
                if (!display.isDisposed()) {
                    if (!component.isDisposed()) {
                        component.setCursor(display.getSystemCursor(SWT.CURSOR_ARROW));
                    }
                }
            }
        }
    }
}
