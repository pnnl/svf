package gov.pnnl.svf.picking;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.collections.CountingSet;
import gov.pnnl.svf.core.collections.KeyValuePair;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.scene.Initializable;
import gov.pnnl.svf.scene.Scene;
import java.nio.IntBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This referenceCamera builds a picking view for a scene. Actor's that have
 * picking support will be notified when clicked or double clicked in a
 * referenceCamera view that is attached to this picking referenceCamera.
 *
 * @author Amelia Bleeker
 */
public abstract class AbstractItemPickingCamera extends AbstractPickingCamera implements ItemPickingCamera, Initializable {

    private static final Logger logger = Logger.getLogger(AbstractItemPickingCamera.class.getName());

    protected final List<ItemPickingCameraListener> listeners = Collections.synchronizedList(new ArrayList<>());
    private final int maxNameStackDepth[] = {64};
    private IntBuffer nameStackBuffer;
    private boolean initialized = false;
    private KeyValuePair<ItemPickingSupport, Object> currentPick;

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractItemPickingCamera(final Scene scene, final Camera camera) {
        super(scene, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractItemPickingCamera(final Scene scene, final String id, final Camera camera) {
        super(scene, id, camera);
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking referenceCamera will live.
     * @param type   The type of this actor.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractItemPickingCamera(final Scene scene, final String type, final String id, final Camera camera) {
        super(scene, type, id, camera);
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            gl.glGetIntegerv(GL2.GL_MAX_NAME_STACK_DEPTH, maxNameStackDepth, 0);
            nameStackBuffer = Buffers.newDirectIntBuffer(maxNameStackDepth[0]);// ByteBuffer.allocateDirect(maxNameStackDepth[0] * 4).asIntBuffer();
            initialized = true;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        listeners.clear();
    }

    @Override
    public boolean isInitialized() {
        synchronized (this) {
            return initialized;
        }
    }

    @Override
    public boolean isSlow() {
        return false;
    }

    @Override
    public void unInitialize(final GL2 gl, final GLUgl2 glu) {
        synchronized (this) {
            initialized = false;
        }
    }

    @Override
    public void addListener(final PickingCameraListener listener) {
        super.addListener(listener);
        if (listener instanceof ItemPickingCameraListener) {
            listeners.remove((ItemPickingCameraListener) listener);
            listeners.add((ItemPickingCameraListener) listener);
        }
    }

    @Override
    public void removeListener(final PickingCameraListener listener) {
        super.removeListener(listener);
        if (listener instanceof ItemPickingCameraListener) {
            listeners.remove((ItemPickingCameraListener) listener);
        }
    }

    @Override
    public void clearListeners() {
        super.clearListeners();
        listeners.clear();
    }

    /**
     * Override and return true if this implementation provides custom picking.
     *
     * @return true for custom picking; false otherwise
     */
    protected boolean isCustomPicking() {
        return false;
    }

    /**
     * Override this method when providing custom picking hit. Check for a
     * picking hit using the specified screen coordinates. Depending on
     * implementation only one item per pixel may be returned regardless of how
     * many items are occupying the same space.
     *
     * @param x the x coordinate of the center of the pick in screen pixels
     * @param y the y coordinate of the center of the pick in screen pixels
     *
     * @return the item picking support object and item for the actor picked or
     *         null
     */
    protected KeyValuePair<ItemPickingSupport, Object> checkPickingHit(final int x, final int y) {
        // no operation by default
        return null;
    }

    /**
     * Override this method when providing custom picking hit. Check for a
     * picking hit using the specified screen coordinates. Depending on
     * implementation only one item per pixel may be returned regardless of how
     * many items are occupying the same space.
     *
     * @param x      the x coordinate of the center of the pick in screen pixels
     * @param y      the y coordinate of the center of the pick in screen pixels
     * @param width  the width of the pick in pixels
     * @param height the height of the pick in pixels
     *
     * @return the set of item picking support objects and items for the actors
     *         picked or null
     */
    protected CountingSet<KeyValuePair<ItemPickingSupport, Object>> checkPickingHit(final int x, final int y, final int width, final int height) {
        // no operation by default
        return null;
    }

    @Override
    public void checkPickingHit(final GL2 gl, final GLUgl2 glu, final PickingSupport support) {
        // exit if this picking camera is utilizing custom picking
        if (isCustomPicking()) {
            return;
        }
        super.checkPickingHit(gl, glu, support);
    }

    @Override
    public void checkPickingHit(final GL2 gl, final GLUgl2 glu, final ItemPickingSupport support) {
        // exit if this picking camera is utilizing custom picking
        if (isCustomPicking()) {
            return;
        }
        // continue
        // only pick if this actor is viewable in the referenceCamera
        if (support.getActor().isCamera(referenceCamera)) {
            // check for a pick on the whole actor first
            gl.glRenderMode(GL2.GL_SELECT);
            gl.glInitNames();
            gl.glPushName(0);
            support.pickingDraw(gl, glu, referenceCamera, currentEvent);
            int hits = gl.glRenderMode(GL2.GL_RENDER);
            gl.glPopName();
            if (hits > 0) {
                // there is a hit on the whole actor, check it's parts
                totalPicked++;
                // find the scene coordscoords
                currentEvent.setLocation(coords.unProject(gl, glu, currentEvent.getX(), currentEvent.getY()));
                final Set<Object> picked = new HashSet<>();
                // check individual item hits here
                final List<Object> items = new ArrayList<>(support.getItems());
                int stack;
                IntBuffer buffer;
                synchronized (this) {
                    stack = maxNameStackDepth[0];
                    buffer = nameStackBuffer;
                }
                final int groups = items.size() / stack + (items.size() % stack > 0 ? 1 : 0);
                for (int group = 0; group < groups; group++) {
                    // batch check items up to the max buffer size
                    gl.glSelectBuffer(stack, buffer);
                    gl.glRenderMode(GL2.GL_SELECT);
                    gl.glInitNames();
                    for (int i = group * stack; i < items.size(); i++) {
                        final Object item = items.get(i);
                        gl.glPushName(i);
                        support.itemPickingDraw(gl, glu, referenceCamera, currentEvent, item);
                        gl.glPopName();
                    }
                    hits = gl.glRenderMode(GL2.GL_RENDER);
                    int offset = 0;
                    if (hits > 0) {
                        // there was a hit
                        for (int i = 0; i < hits; i++) {
                            final int names = buffer.get(offset++);
                            // z order is relative so it makes no sense to use it because
                            // we can't guarantee that all of the items will fit in a single
                            // batch
                            offset++; // remove if z1 is used
                            offset++; // remove if z2 is used
                            //                            final float z1 = (buffer.get(offset++) & 0xffffffffL);
                            //                            final float z2 = (buffer.get(offset++) & 0xffffffffL);
                            for (int j = 0; j < names; j++) {
                                final int name = buffer.get(offset++);
                                picked.add(items.get(name));
                            }
                        }
                    }
                }
                if (!picked.isEmpty()) {
                    eventPushThread.execute(new Runnable() {
                        private final PickingCameraEvent event = AbstractItemPickingCamera.this.currentEvent;

                        @Override
                        public void run() {
                            support.notifyPicked(picked, event);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void update(final long delta) {
        super.update(delta);
        // exit if this picking camera is not utilizing custom picking
        if (!isCustomPicking()) {
            return;
        }
        // continue
        final double margin = getSensitivity();
        // check current object under mouse and fire events
        final Set<PickingCameraEvent> events = getEvents();
        for (final PickingCameraEvent event : events) {
            // check for move events that don't have an action
            if (!Collections.disjoint(event.getTypes(), CameraEventType.Collections.MOVEMENT_TYPES)
                && Collections.disjoint(event.getTypes(), CameraEventType.Collections.ACTION_TYPES)) {//event.getTypes().contains(PickType.MOVE)) {
                final KeyValuePair<ItemPickingSupport, Object> hit = checkPickingHit(event.getX(), event.getY());
                final boolean fireEvent;
                final KeyValuePair<ItemPickingSupport, Object> current;
                if (currentPick == null ? hit == null : currentPick.equals(hit)) {
                    // it's still in dead space or on the same item
                    // don't fire any entered or exited events
                    fireEvent = false;
                    current = null;
                } else {
                    fireEvent = true;
                    current = AbstractItemPickingCamera.this.currentPick;
                }
                // fire events
                synchronized (listeners) {
                    for (final ItemPickingCameraListener listener : listeners) {
                        if (fireEvent) {
                            eventPushThread.execute(new RunnableMoveEventImpl(current, listener, event, hit));
                        }
                        listener.mouseMoved(event);
                    }
                }
                // fire support move event
                if (hit != null) {
                    // notify the picking hit
                    eventPushThread.execute(new RunnableEventImpl(Collections.singleton(hit), event));
                }
                currentPick = hit;
            } else {
                // all other picking events
                final Set<KeyValuePair<ItemPickingSupport, Object>> hits = checkPickingHit(event.getX(),
                                                                                           event.getY(),
                                                                                           event.getWidth() + (int) Math.floor(margin + margin),
                                                                                           event.getHeight() + (int) Math.floor(margin + margin));
                start(event);
                if (!hits.isEmpty()) {
                    if (!Collections.disjoint(event.getTypes(), CameraEventType.Collections.ACTION_TYPES)) {//event.getTypes().contains(PickType.DOWN) || event.getTypes().contains(PickType.SINGLE) || event.getTypes().contains(PickType.DOUBLE) || event.getTypes().contains(PickType.HOVER)) {
                        totalPicked += hits.size();
                    }
                    // notify the picking hit
                    eventPushThread.execute(new RunnableEventImpl(hits, event));
                }
                end(event);
            }
        }
    }

    private static class RunnableMoveEventImpl implements Runnable {

        private final KeyValuePair<ItemPickingSupport, Object> current;
        private final ItemPickingCameraListener listener;
        private final PickingCameraEvent event;
        private final KeyValuePair<ItemPickingSupport, Object> hits;

        private RunnableMoveEventImpl(final KeyValuePair<ItemPickingSupport, Object> current, final ItemPickingCameraListener listener,
                                      final PickingCameraEvent event, final KeyValuePair<ItemPickingSupport, Object> hit) {
            this.current = current;
            this.listener = listener;
            this.event = event;
            hits = hit;
        }

        @Override
        public void run() {
            if (current != null) {
                // fire exited currentEvent
                listener.itemExited(current.getKey().getActor(), current.getValue(), event);
            }
            if (hits != null) {
                // fire the entered currentEvent
                listener.itemEntered(hits.getKey().getActor(), hits.getValue(), event);
            }
        }
    }

    private static class RunnableEventImpl implements Runnable {

        private final Set<KeyValuePair<ItemPickingSupport, Object>> hits;
        private final PickingCameraEvent event;

        private RunnableEventImpl(final Set<KeyValuePair<ItemPickingSupport, Object>> hits, final PickingCameraEvent event) {
            this.hits = hits;
            this.event = event;
        }

        @Override
        public void run() {
            if (hits.size() == 1) {
                for (final KeyValuePair<ItemPickingSupport, Object> hit : hits) {
                    try {
                        hit.getKey().notifyPicked(Collections.singleton(hit.getValue()), event);
                    } catch (final NullPointerException ex) {
                        logger.log(Level.WARNING, MessageFormat.format("{0}: Error notifying pick.", event.getSource().getScene()), ex);
                    }
                }
            } else {
                // group them by support class
                final Map<ItemPickingSupport, Set<Object>> map = new HashMap<>();
                for (final KeyValuePair<ItemPickingSupport, Object> hit : hits) {
                    final Set<Object> objects = map.get(hit.getKey());
                    if (objects == null) {
                        // new entry
                        map.put(hit.getKey(), new HashSet<>(Collections.singleton(hit.getValue())));
                    } else {
                        // just add the new value
                        objects.add(hit.getValue());
                    }
                }
                // send batched events
                for (final Map.Entry<ItemPickingSupport, Set<Object>> entry : map.entrySet()) {
                    entry.getKey().notifyPicked(entry.getValue(), event);
                }
            }
        }
    }
}
