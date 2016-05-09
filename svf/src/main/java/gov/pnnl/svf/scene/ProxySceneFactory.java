package gov.pnnl.svf.scene;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.camera.DrivingCamera;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.camera.ProxyDraggingCamera;
import gov.pnnl.svf.camera.ProxyDrivingCamera;
import gov.pnnl.svf.camera.ProxyOrbitCamera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.service.AbstractBusyService;
import gov.pnnl.svf.core.service.BusyService;
import gov.pnnl.svf.core.util.NamedThreadFactory;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.picking.ProxyColorPickingCamera;
import gov.pnnl.svf.picking.ProxyItemPickingCamera;
import gov.pnnl.svf.picking.ProxyPickingCamera;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Scene factory for use with proxy scenes.
 *
 * @author Arthur Bleeker
 */
public class ProxySceneFactory implements SceneFactory {

    private final ExecutorService executorUI = Executors.newSingleThreadExecutor(new NamedThreadFactory(ProxyScene.class, "UI"));
    private final ExecutorService executorWorker = Executors.newCachedThreadPool(new NamedThreadFactory(ProxyScene.class, "Worker"));
    private final AtomicLong counter = new AtomicLong();

    /**
     * Constructor
     */
    public ProxySceneFactory() {
    }

    @Override
    public String newUuid(final Scene scene) {
        return scene.getId() + "-" + counter.getAndIncrement();
    }

    @Override
    public TooltipActor createTooltip(final Scene scene) {
        return new TooltipActor(scene);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createService(final Scene scene, final Class<T> type) {
        if (BusyService.class.equals(type)) {
            return (T) new BusyServiceImpl();
        } else {
            throw new IllegalArgumentException("type");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Camera> C createCamera(final Scene scene, final Class<C> type) {
        if (DraggingCamera.class.equals(type)) {
            return (C) new ProxyDraggingCamera(scene);
        } else if (OrbitCamera.class.equals(type)) {
            return (C) new ProxyOrbitCamera(scene);
        } else if (DrivingCamera.class.equals(type)) {
            return (C) new ProxyDrivingCamera(scene);
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
            return (P) new ProxyColorPickingCamera(scene, camera);
        } else if (ItemPickingCamera.class.equals(type)) {
            return (P) new ProxyItemPickingCamera(scene, camera);
        } else if (PickingCamera.class.equals(type)) {
            return (P) new ProxyPickingCamera(scene, camera);
        } else {
            throw new IllegalArgumentException("type");
        }
    }

    @Override
    public void runOnUiThread(final Scene scene, final Runnable runnable) {
        runnable.run();
    }

    @Override
    public boolean runOnUiThread(final Scene scene, final Runnable runnable, final boolean synchronous) {
        if (synchronous) {
            runnable.run();
        } else {
            executorUI.execute(runnable);
        }
        return synchronous;
    }

    @Override
    public void runOffUiThread(final Scene scene, final Runnable runnable) {
        runnable.run();
    }

    @Override
    public boolean runOffUiThread(final Scene scene, final Runnable runnable, final boolean synchronous) {
        if (synchronous) {
            runnable.run();
        } else {
            executorWorker.execute(runnable);
        }
        return synchronous;
    }

    protected static class BusyServiceImpl extends AbstractBusyService {

        protected BusyServiceImpl() {
        }

        @Override
        protected void showBusy(final boolean value) {
            // no operation
        }
    }
}
