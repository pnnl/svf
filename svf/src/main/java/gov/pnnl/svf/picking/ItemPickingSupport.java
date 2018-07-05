package gov.pnnl.svf.picking;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.support.AbstractSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This class provides item picking support for an actor. An item picking camera
 * must be present in the scene for picking to occur. Items must also be present
 * in the items list in order to be picked. Items must be contained within the
 * drawable area of an actor. Item picking only works for scene items.
 *
 * @author Amelia Bleeker
 */
public class ItemPickingSupport extends AbstractSupport<ItemPickingSupportListener> implements ItemPickable, Pickable {

    private final List<Object> items = Collections.synchronizedList(new ArrayList<>());
    private final ItemPickableActor drawable;

    /**
     * Constructor
     *
     * @param actor The item pickable actor.
     *
     * @return the new instance of this object
     */
    public static ItemPickingSupport newInstance(final ItemPickableActor actor) {
        final ItemPickingSupport instance = new ItemPickingSupport(actor);
        actor.add(instance);
        return instance;
    }

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The item pickable actor.
     */
    protected ItemPickingSupport(final ItemPickableActor actor) {
        super(actor);
        drawable = actor;
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * @return a reference to the list of items
     */
    public List<Object> getItems() {
        return items;
    }

    /**
     * Convenience method for <code>getItems().add(item)</code>.
     *
     * @param item the item to add
     */
    public void addItem(final Object item) {
        items.add(item);
    }

    /**
     * Notify all of the listeners that an item in this actor has been picked.
     *
     * @param items the items that were picked
     * @param event The event for the picked event that occurred.
     */
    public void notifyPicked(final Set<Object> items, final PickingCameraEvent event) {
        for (final ItemPickingSupportListener listener : getListeners()) {
            listener.itemsPicked(getActor(), items, event);
        }
    }

    @Override
    public String toString() {
        return "ItemPickingSupport{" + '}';
    }

    @Override
    public void itemPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event, final Object item) {
        drawable.itemPickingDraw(gl, glu, camera, event, item);
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        drawable.pickingDraw(gl, glu, camera, event);
    }
}
