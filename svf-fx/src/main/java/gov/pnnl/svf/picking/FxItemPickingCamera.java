package gov.pnnl.svf.picking;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.FxEventHandler;
import java.util.Collections;
import java.util.Set;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;

/**
 * This camera builds a picking view for a scene. Actor's that have picking
 * support will be notified when clicked or double clicked in a camera view that
 * is attached to this picking camera. Picking is only supported on cameras that
 * view the entire scene and fill the viewport.
 *
 * @author Arthur Bleeker
 *
 */
public class FxItemPickingCamera extends AbstractItemPickingCamera implements FxEventHandler<InputEvent> {

    private static final Set<EventType<InputEvent>> EVENT_TYPES = Collections.unmodifiableSet(Collections.singleton(InputEvent.ANY));
    private final FxPickingCameraListener listener = new FxPickingCameraListener(this);

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param camera The view into the scene that will be used for picking.
     */
    public FxItemPickingCamera(final Scene scene, final Camera camera) {
        super(scene, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    public FxItemPickingCamera(final Scene scene, final String id, final Camera camera) {
        super(scene, id, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param type   The type of this actor.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    public FxItemPickingCamera(final Scene scene, final String type, final String id, final Camera camera) {
        super(scene, type, id, camera);
    }

    @Override
    public Set<EventType<InputEvent>> getEventTypes() {
        return EVENT_TYPES;
    }

    @Override
    public void handle(final InputEvent event) {
        listener.handle(event);
    }
}
