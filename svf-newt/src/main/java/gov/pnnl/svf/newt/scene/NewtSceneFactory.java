package gov.pnnl.svf.newt.scene;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.camera.DrivingCamera;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.service.BusyService;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.core.util.NamedThreadFactory;
import gov.pnnl.svf.newt.camera.NewtDraggingCamera;
import gov.pnnl.svf.newt.camera.NewtDrivingCamera;
import gov.pnnl.svf.newt.camera.NewtOrbitCamera;
import gov.pnnl.svf.newt.picking.NewtColorPickingCamera;
import gov.pnnl.svf.newt.picking.NewtItemPickingCamera;
import gov.pnnl.svf.newt.picking.NewtPickingCamera;
import gov.pnnl.svf.newt.service.NewtBusyService;
import gov.pnnl.svf.newt.service.NewtCursorService;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneFactory;
import gov.pnnl.svf.scene.TooltipActor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Scene factory for NEWT scene types.
 *
 * @author Amelia Bleeker
 */
public class NewtSceneFactory implements SceneFactory {

    private final ExecutorService executorUI = Executors.newSingleThreadExecutor(new NamedThreadFactory(NewtSceneFactory.class, "UI"));
    private final ExecutorService executorWorker = Executors.newCachedThreadPool(new NamedThreadFactory(NewtSceneFactory.class, "Worker"));
    private final AtomicLong counter = new AtomicLong();

    /**
     * Constructor
     */
    public NewtSceneFactory() {
    }

    @Override
    public String newUuid(final Scene scene) {
        return scene.getId() + "-" + counter.getAndIncrement();
    }

    @Override
    public TooltipActor createTooltip(final Scene scene) {
        return new NewtTooltipActor(scene);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createService(final Scene scene, final Class<T> type) {
        if (BusyService.class.equals(type)) {
            if (scene instanceof NewtScene) {
                return (T) new NewtBusyService((NewtScene) scene);
            } else {
                throw new IllegalArgumentException("scene");
            }
        } else if (CursorService.class.equals(type)) {
            if (scene instanceof NewtScene) {
                return (T) new NewtCursorService((NewtScene) scene);
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
            return (C) new NewtDraggingCamera(scene);
        } else if (OrbitCamera.class.equals(type)) {
            return (C) new NewtOrbitCamera(scene);
        } else if (DrivingCamera.class.equals(type)) {
            return (C) new NewtDrivingCamera(scene);
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
            return (P) new NewtColorPickingCamera(scene, camera);
        } else if (ItemPickingCamera.class.equals(type)) {
            return (P) new NewtItemPickingCamera(scene, camera);
        } else if (PickingCamera.class.equals(type)) {
            return (P) new NewtPickingCamera(scene, camera);
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
}
