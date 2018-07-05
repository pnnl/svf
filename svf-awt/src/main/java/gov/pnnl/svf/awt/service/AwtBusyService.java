package gov.pnnl.svf.awt.service;

import gov.pnnl.svf.awt.scene.AwtPanelScene;
import gov.pnnl.svf.awt.scene.AwtScene;
import gov.pnnl.svf.core.service.AbstractBusyService;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.core.service.CursorType;
import gov.pnnl.svf.scene.Scene;
import java.awt.Component;
import java.awt.Cursor;

/**
 * AWT implementation of the busy service.
 *
 * @author Amelia Bleeker
 */
public class AwtBusyService extends AbstractBusyService {

    private final BusyRunnable showBusy;
    private final BusyRunnable hideBusy;
    private final Scene scene;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public AwtBusyService(final AwtScene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        final Component root = getRoot(scene.getComponent());
        showBusy = new BusyRunnable(scene, root, true);
        hideBusy = new BusyRunnable(scene, root, false);
    }

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public AwtBusyService(final AwtPanelScene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        final Component root = getRoot(scene.getComponent());
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

    private Component getRoot(final Component component) {
        final Component parent = component.getParent();
        if (parent != null) {
            return getRoot(parent);
        } else {
            return component;
        }
    }

    /**
     * Runnable that will show or hide the busy cursor.
     *
     * @author Amelia Bleeker
     */
    protected static class BusyRunnable implements Runnable {

        private final Scene scene;
        private final Component component;
        private final boolean show;

        /**
         * Constructor
         *
         * @param scene     reference to the scene
         * @param component reference to the component
         * @param show      true to show busy
         */
        private BusyRunnable(final Scene scene, final Component component, final boolean show) {
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
                component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            } else { // if (!show) {
                component.setCursor(null);
            }
        }
    }
}
