package gov.pnnl.svf.picking;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.support.AbstractSupport;

/**
 * This class provides picking support for an actor. A picking camera must be
 * present in the scene for picking to occur. Picking only works for scene
 * items.
 *
 * @author Arthur Bleeker
 *
 */
public class PickingSupport extends AbstractSupport<PickingSupportListener> implements Pickable {

    private final PickableActor drawable;

    /**
     * Constructor
     *
     * @param actor The owning actor.
     *
     * @return the new instance of this object
     */
    public static PickingSupport newInstance(final PickableActor actor) {
        final PickingSupport instance = new PickingSupport(actor);
        actor.add(instance);
        return instance;
    }

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The owning actor.
     */
    protected PickingSupport(final PickableActor actor) {
        super(actor);
        drawable = actor;
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * Notify all of the listeners that this actor has been picked.
     *
     * @param event The event for the picked event that occurred.
     */
    public void notifyPicked(final PickingCameraEvent event) {
        for (final PickingSupportListener listener : getListeners()) {
            listener.picked(getActor(), event);
        }
    }

    @Override
    public String toString() {
        return "PickingSupport{" + '}';
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        drawable.pickingDraw(gl, glu, camera, event);
    }
}
