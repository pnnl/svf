package gov.pnnl.svf.picking;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.support.AbstractSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * This class provides color picking support for an actor. A color picking
 * camera must be present in the scene for picking to occur. Color picking works
 * for both scene and user interface items. The actor's drawing pass is modified
 * to contain the picking pass when this support is added. Removing picking from
 * the actor's drawing pass will prevent the actor from being color pickable in
 * the scene.
 *
 * @author Arthur Bleeker
 */
public class ColorPickingSupport extends AbstractSupport<ColorPickingSupportListener> implements ColorPickable {

    private final Map<Object, Color> mapping = Collections.synchronizedMap(new HashMap<Object, Color>());
    private final ColorPickingUtils colorUtils;
    private final ColorPickableActor drawable;

    /**
     * Constructor
     *
     * @param actor The owning actor.
     *
     * @return the new instance of this object
     */
    public static ColorPickingSupport newInstance(final ColorPickableActor actor) {
        final ColorPickingSupport instance = new ColorPickingSupport(actor);
        actor.setDrawingPass(actor.getDrawingPass().addDrawingPass(DrawingPass.PICKING));
        instance.newMapping(actor);
        actor.add(instance);
        return instance;
    }

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The owning actor.
     */
    protected ColorPickingSupport(final ColorPickableActor actor) {
        super(actor);
        colorUtils = actor.getScene().getExtended().getColorPickingUtils();
        drawable = actor;
    }

    /**
     * This reference should be synchronized on if iterating.
     *
     * @return a reference to the synchronized mapped items
     */
    public Map<Object, Color> getItems() {
        return mapping;
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * Get the color associated with an item.
     *
     * @param item the item
     *
     * @return the color associated with the item or null
     */
    public Color getMapping(final Object item) {
        if (item == null) {
            throw new NullPointerException("item");
        }
        return mapping.get(item);
    }

    /**
     * Create a new item color mapping. The item must be unique.
     *
     * @param item the item
     *
     * @return this instance
     *
     * @throws IllegalArgumentException if the item is not unique
     */
    public ColorPickingSupport newMapping(final Object item) {
        if (item == null) {
            throw new NullPointerException("item");
        }
        synchronized (mapping) {
            if (mapping.containsKey(item)) {
                throw new IllegalArgumentException("item");
            }
            final Color color = colorUtils.newMapping(this, item);
            mapping.put(item, color);
        }
        return this;
    }

    /**
     * Clears the mapping for the specified item.
     *
     * @param item the item
     *
     * @return this instance
     */
    public ColorPickingSupport clearMapping(final Object item) {
        if (item == null) {
            throw new NullPointerException("item");
        }
        colorUtils.clearMapping(mapping.remove(item));
        return this;
    }

    @Override
    public void dispose() {
        super.dispose();
        Set<Object> keys;
        synchronized (mapping) {
            keys = new HashSet<>(mapping.keySet());
        }
        for (final Object key : keys) {
            clearMapping(key);
        }
    }

    /**
     * Notify all of the listeners that an item in this actor has been picked.
     *
     * @param items the items that were picked
     * @param event The event for the picked event that occurred.
     */
    public void notifyPicked(final Set<Object> items, final PickingCameraEvent event) {
        for (final ColorPickingSupportListener listener : getListeners()) {
            listener.itemsPicked(getActor(), items, event);
        }
    }

    @Override
    public String toString() {
        return "ColorPickingSupport{" + '}';
    }

    @Override
    public void colorPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
        drawable.colorPickingDraw(gl, glu, camera, support);
    }
}
