package gov.pnnl.svf.newt.service;

import com.jogamp.newt.opengl.GLWindow;
import gov.pnnl.svf.core.service.AbstractBusyService;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.core.service.CursorType;
import gov.pnnl.svf.newt.scene.NewtScene;
import gov.pnnl.svf.scene.Scene;

/**
 * Newt implementation of the busy service.
 *
 * @author Amelia Bleeker
 */
public class NewtBusyService extends AbstractBusyService {

    private final BusyRunnable showBusy;
    private final BusyRunnable hideBusy;
    private final Scene scene;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public NewtBusyService(final NewtScene scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        final GLWindow root = scene.getComponent();
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
     * @author Amelia Bleeker
     */
    protected static class BusyRunnable implements Runnable {

        private final Scene scene;
        private final GLWindow component;
        private final boolean show;

        /**
         * Constructor
         *
         * @param scene     reference to the scene
         * @param component reference to the component
         * @param show      true to show busy
         */
        private BusyRunnable(final Scene scene, final GLWindow component, final boolean show) {
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
                // currently no way to set the cursor in NEWT
//                component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            } else { // if (!show) {
                // currently no way to set the cursor in NEWT
//                component.setCursor(null);
            }
        }
    }
}
