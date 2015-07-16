package gov.pnnl.svf.scene;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.DrawingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.service.DrawableService;
import gov.pnnl.svf.update.UninitializeTask;
import java.util.Collection;
import java.util.List;

/**
 * Interface for container that holds all of the scene collections.
 *
 * @author Arthur Bleeker
 */
public interface SceneCollections extends Disposable {

    /**
     * Collection of all the root actors in the scene that require drawing.
     *
     * @return a reference to a mutable collection
     */
    Collection<Actor> getActors();

    /**
     * Collection of all the uninitializables in the scene.
     *
     * @return a reference to a mutable collection
     */
    Collection<UninitializeTask> getUninitializables();

    /**
     * Collection of all the initializables in the scene.
     *
     * @return a reference to a mutable collection
     */
    Collection<Initializable> getInitializables();

    /**
     * Collection of all the updatables in the scene.
     *
     * @return a reference to a mutable collection
     */
    Collection<Updatable> getUpdatables();

    /**
     * Collection of all the services in the scene.
     *
     * @return a reference to a mutable collection
     */
    List<DrawableService> getServices();

    /**
     * Collection of all the picking cameras in the scene.
     *
     * @return a reference to a mutable collection
     */
    Collection<PickingCamera> getPickingCameras();

    /**
     * Collection of all the drawing cameras in the scene.
     *
     * @return a reference to a mutable collection
     */
    Collection<DrawingCamera> getDrawingCameras();

    /**
     * Collection of all the drawables in the scene.
     *
     * @return a reference to a mutable collection
     */
    List<DrawableItem> getDrawables();
}
