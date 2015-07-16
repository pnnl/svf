package gov.pnnl.svf.swt.scene;

import com.jogamp.opengl.swt.GLCanvas;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.camera.DrivingCamera;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.service.BusyService;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneFactory;
import gov.pnnl.svf.scene.TooltipActor;
import gov.pnnl.svf.swt.camera.SwtDraggingCamera;
import gov.pnnl.svf.swt.camera.SwtDrivingCamera;
import gov.pnnl.svf.swt.camera.SwtOrbitCamera;
import gov.pnnl.svf.swt.picking.SwtColorPickingCamera;
import gov.pnnl.svf.swt.picking.SwtItemPickingCamera;
import gov.pnnl.svf.swt.picking.SwtPickingCamera;
import gov.pnnl.svf.swt.service.SwtBusyService;
import gov.pnnl.svf.swt.service.SwtCursorService;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;

/**
 * Scene factory for SWT scene types.
 *
 * @author Arthur Bleeker
 */
public class SwtSceneFactory implements SceneFactory {

    private static final Logger logger = Logger.getLogger(SwtSceneFactory.class.getName());
    private final AtomicLong counter = new AtomicLong();

    /**
     * Constructor
     */
    public SwtSceneFactory() {
    }

    @Override
    public String newUuid(final Scene scene) {
        return scene.getId() + "-" + counter.getAndIncrement();
    }

    @Override
    public TooltipActor createTooltip(final Scene scene) {
        return new SwtTooltipActor(scene);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createService(final Scene scene, final Class<T> type) {
        if (BusyService.class.equals(type)) {
            if (scene instanceof SwtScene) {
                return (T) new SwtBusyService((SwtScene) scene);
            } else {
                throw new IllegalArgumentException("scene");
            }
        } else if (CursorService.class.equals(type)) {
            if (scene instanceof SwtScene) {
                return (T) new SwtCursorService((SwtScene) scene);
            } else {
                throw new IllegalArgumentException("scene");
            }
        } else {
            throw new IllegalArgumentException("type");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Camera> C createCamera(final Scene scene, final Class<C> type) {
        if (DraggingCamera.class.equals(type)) {
            return (C) new SwtDraggingCamera(scene);
        } else if (OrbitCamera.class.equals(type)) {
            return (C) new SwtOrbitCamera(scene);
        } else if (DrivingCamera.class.equals(type)) {
            return (C) new SwtDrivingCamera(scene);
        } else if (SimpleCamera.class.equals(type)) {
            return (C) new SimpleCamera(scene);
        } else {
            throw new IllegalArgumentException("type");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P extends PickingCamera> P createPickingCamera(final Scene scene, final Camera camera, final Class<P> type) {
        if (ColorPickingCamera.class.equals(type)) {
            return (P) new SwtColorPickingCamera(scene, camera);
        } else if (ItemPickingCamera.class.equals(type)) {
            return (P) new SwtItemPickingCamera(scene, camera);
        } else if (PickingCamera.class.equals(type)) {
            return (P) new SwtPickingCamera(scene, camera);
        } else {
            throw new IllegalArgumentException("type");
        }
    }

    @Override
    public void runOnUiThread(final Scene scene, final Runnable runnable) {
        try {
            if (Display.getCurrent() != null) {
                runnable.run();
            } else {
                final GLCanvas component = (GLCanvas) scene.getComponent();
                if (component != null) {
                    final Display display = component.getDisplay();
                    if (display != null && !display.isDisposed()) {
                        display.asyncExec(runnable);
                    }
                }
            }
        } catch (final SWTException ex) {
            if (scene.getExtended().getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to run on SWT UI thread.", scene), ex);
            }
        }
    }

    @Override
    public boolean runOnUiThread(final Scene scene, final Runnable runnable, final boolean synchronous) {
        try {
            if (synchronous) {
                if (Display.getCurrent() != null) {
                    runnable.run();
                } else {
                    final GLCanvas component = (GLCanvas) scene.getComponent();
                    if (component != null) {
                        final Display display = component.getDisplay();
                        if (display != null && !display.isDisposed()) {
                            display.syncExec(runnable);
                        }
                    }
                }
                return true;
            } else {
                final GLCanvas component = (GLCanvas) scene.getComponent();
                if (component != null) {
                    final Display display = component.getDisplay();
                    if (display != null && !display.isDisposed()) {
                        display.asyncExec(runnable);
                    }
                }
                return false;
            }
        } catch (final SWTException ex) {
            if (scene.getExtended().getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to run on SWT UI thread.", scene), ex);
            }
        }
        return false;
    }
}
