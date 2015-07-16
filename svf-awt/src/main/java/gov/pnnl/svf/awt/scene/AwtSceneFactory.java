package gov.pnnl.svf.awt.scene;

import gov.pnnl.svf.awt.camera.AwtDraggingCamera;
import gov.pnnl.svf.awt.camera.AwtDrivingCamera;
import gov.pnnl.svf.awt.camera.AwtOrbitCamera;
import gov.pnnl.svf.awt.picking.AwtColorPickingCamera;
import gov.pnnl.svf.awt.picking.AwtItemPickingCamera;
import gov.pnnl.svf.awt.picking.AwtPickingCamera;
import gov.pnnl.svf.awt.service.AwtBusyService;
import gov.pnnl.svf.awt.service.AwtCursorService;
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
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * Scene factory for AWT scene types.
 *
 * @author Arthur Bleeker
 */
public class AwtSceneFactory implements SceneFactory {

    private static final Logger logger = Logger.getLogger(AwtSceneFactory.class.getName());
    private final AtomicLong counter = new AtomicLong();

    /**
     * Constructor
     */
    public AwtSceneFactory() {
    }

    @Override
    public String newUuid(final Scene scene) {
        return scene.getId() + "-" + counter.getAndIncrement();
    }

    @Override
    public TooltipActor createTooltip(final Scene scene) {
        return new AwtTooltipActor(scene);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createService(final Scene scene, final Class<T> type) {
        if (BusyService.class.equals(type)) {
            if (scene instanceof AwtScene) {
                return (T) new AwtBusyService((AwtScene) scene);
            } else if (scene instanceof AwtPanelScene) {
                return (T) new AwtBusyService((AwtPanelScene) scene);
            } else {
                throw new IllegalArgumentException("scene");
            }
        } else if (CursorService.class.equals(type)) {
            if (scene instanceof AwtScene) {
                return (T) new AwtCursorService((AwtScene) scene);
            } else if (scene instanceof AwtPanelScene) {
                return (T) new AwtCursorService((AwtPanelScene) scene);
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
            return (C) new AwtDraggingCamera(scene);
        } else if (OrbitCamera.class.equals(type)) {
            return (C) new AwtOrbitCamera(scene);
        } else if (DrivingCamera.class.equals(type)) {
            return (C) new AwtDrivingCamera(scene);
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
            return (P) new AwtColorPickingCamera(scene, camera);
        } else if (ItemPickingCamera.class.equals(type)) {
            return (P) new AwtItemPickingCamera(scene, camera);
        } else if (PickingCamera.class.equals(type)) {
            return (P) new AwtPickingCamera(scene, camera);
        } else {
            throw new IllegalArgumentException("type");
        }
    }

    @Override
    public void runOnUiThread(final Scene scene, final Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    @Override
    public boolean runOnUiThread(final Scene scene, final Runnable runnable, final boolean synchronous) {
        if (synchronous) {
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(runnable);
                } catch (final InterruptedException ex) {
                    logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to run on AWT UI thread.", scene), ex);
                } catch (final InvocationTargetException ex) {
                    throw (RuntimeException) ex.getCause();
                }
            }
            return true;
        } else {
            SwingUtilities.invokeLater(runnable);
            return false;
        }
    }
}
