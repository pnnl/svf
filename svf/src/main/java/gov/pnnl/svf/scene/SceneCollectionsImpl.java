package gov.pnnl.svf.scene;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.DrawingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.service.DrawableService;
import gov.pnnl.svf.update.UninitializeTask;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Implementation of scene collections.
 *
 * @author Arthur Bleeker
 */
public class SceneCollectionsImpl implements SceneCollections {

    private final Collection<Actor> actors = new LinkedHashSet<>();
    private final Collection<UninitializeTask> uninitializables = new HashSet<>();
    private final Collection<Initializable> initializables = new HashSet<>();
    private final Collection<Updatable> updatables = new HashSet<>();
    private final List<DrawableService> services = new ArrayList<>();
    private final Collection<PickingCamera> pickingCameras = new HashSet<>();
    private final Collection<DrawingCamera> drawingCameras = new HashSet<>();
    private final List<DrawableItem> drawables = new ArrayList<>();
    private boolean disposed = false;

    /**
     * Constructor
     */
    public SceneCollectionsImpl() {
        super();
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return disposed;
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (disposed) {
                return;
            }
            disposed = true;
        }
    }

    @Override
    public Collection<Actor> getActors() {
        return actors;
    }

    @Override
    public Collection<DrawingCamera> getDrawingCameras() {
        return drawingCameras;
    }

    @Override
    public Collection<PickingCamera> getPickingCameras() {
        return pickingCameras;
    }

    @Override
    public List<DrawableItem> getDrawables() {
        return drawables;
    }

    @Override
    public Collection<Initializable> getInitializables() {
        return initializables;
    }

    @Override
    public List<DrawableService> getServices() {
        // services need to be ordered and there shouldn't be more than a handful anyway
        Collections.sort(services);
        return services;
    }

    @Override
    public Collection<UninitializeTask> getUninitializables() {
        return uninitializables;
    }

    @Override
    public Collection<Updatable> getUpdatables() {
        return updatables;
    }

    @Override
    public String toString() {
        return "SceneCollections [actors=" + actors.size()
               + ", initializables=" + initializables.size()
               + ", uninitializables=" + uninitializables.size()
               + ", drawables=" + drawables.size()
               + ", updatables=" + updatables.size()
               + ", services=" + services.size()
               + ", drawingCameras=" + drawingCameras.size()
               + ", pickingCameras=" + pickingCameras.size()
               + "]";
    }

}
