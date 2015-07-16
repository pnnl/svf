package gov.pnnl.svf.picking;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.collections.CountingSet;
import gov.pnnl.svf.core.collections.KeyValuePair;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
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
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * This camera builds a color picking view for a scene. Actor's that have color
 * picking support will be notified when clicked or double clicked in a camera
 * view that is attached to this picking camera.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractColorPickingCamera extends AbstractPickingCamera implements ColorPickingCamera {

    private static final Logger logger = Logger.getLogger(AbstractColorPickingCamera.class.getName());
    // viewport width index
    public static final int WIDTH = 2;
    // viewport height index
    public static final int HEIGHT = 3;
    protected final List<ColorPickingCameraListener> listeners = Collections.synchronizedList(new ArrayList<ColorPickingCameraListener>());
    private final ColorPickingUtils colorUtils = getScene().getExtended().getColorPickingUtils();
    private KeyValuePair<ColorPickingSupport, Object> currentPick;

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractColorPickingCamera(final Scene scene, final Camera camera) {
        super(scene, camera);
        initialize();
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractColorPickingCamera(final Scene scene, final String id, final Camera camera) {
        super(scene, id, camera);
        initialize();
    }

    /**
     * Constructor
     *
     * @param scene  The scene where this picking camera will live.
     * @param type   The type of this actor.
     * @param id     The unique id for this actor.
     * @param camera The view into the scene that will be used for picking.
     */
    protected AbstractColorPickingCamera(final Scene scene, final String type, final String id, final Camera camera) {
        super(scene, type, id, camera);
        initialize();
    }

    private void initialize() {
        setDrawingPass(DrawingPass.SCENE_INTERFACE_PICKING);
        // add the color picking camera drawing passes to it's reference camera
        getReferenceCamera().setDrawingPass(getReferenceCamera().getDrawingPass().addDrawingPass(getDrawingPass()));
        setPickTypes(CameraEventType.Collections.ALL_TYPES);
    }

    @Override
    public void dispose() {
        super.dispose();
        listeners.clear();
    }

    @Override
    public void addListener(final PickingCameraListener listener) {
        super.addListener(listener);
        if (listener instanceof ColorPickingCameraListener) {
            listeners.remove((ColorPickingCameraListener) listener);
            listeners.add((ColorPickingCameraListener) listener);
        }
    }

    @Override
    public void removeListener(final PickingCameraListener listener) {
        super.removeListener(listener);
        if (listener instanceof ColorPickingCameraListener) {
            listeners.remove((ColorPickingCameraListener) listener);
        }
    }

    @Override
    public void clearListeners() {
        super.clearListeners();
        listeners.clear();
    }

    @Override
    public void makePerspective(final GL2 gl, final GLUgl2 glu) {
        referenceCamera.getExtended().makePerspective(gl, glu);
    }

    @Override
    public void checkPickingHit(final GL2 gl, final GLUgl2 glu, final PickingSupport support) {
        // no operation
    }

    @Override
    public KeyValuePair<ColorPickingSupport, Object> checkPickingHit(final int x, final int y) {
        return colorUtils.checkPickingHit(x, y);
    }

    @Override
    public CountingSet<KeyValuePair<ColorPickingSupport, Object>> checkPickingHit(final int x, final int y, final int w, final int h) {
        return colorUtils.checkPickingHit(x, y, w, h);
    }

    @Override
    public void update(final long delta) {
        super.update(delta);
        final double margin = getSensitivity();
        // check current object under mouse and fire events
        final Set<PickingCameraEvent> events = getEvents();
        for (final PickingCameraEvent event : events) {
            // check for move events that don't have an action
            if (!Collections.disjoint(event.getTypes(), CameraEventType.Collections.MOVEMENT_TYPES)
                && Collections.disjoint(event.getTypes(), CameraEventType.Collections.ACTION_TYPES)) {//event.getTypes().contains(PickType.MOVE)) {
                final KeyValuePair<ColorPickingSupport, Object> hit = checkPickingHit(event.getX(), event.getY());
                final boolean fireEvent;
                final KeyValuePair<ColorPickingSupport, Object> current;
                if (currentPick == null ? hit == null : currentPick.equals(hit)) {
                    // it's still in dead space or on the same item
                    // don't fire any entered or exited events
                    fireEvent = false;
                    current = null;
                } else {
                    fireEvent = true;
                    current = AbstractColorPickingCamera.this.currentPick;
                }
                // fire events
                synchronized (listeners) {
                    for (final ColorPickingCameraListener listener : listeners) {
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
                // adjust method parameters for margin
                final Set<KeyValuePair<ColorPickingSupport, Object>> hits = checkPickingHit(event.getX(),
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

        private final KeyValuePair<ColorPickingSupport, Object> current;
        private final ColorPickingCameraListener listener;
        private final PickingCameraEvent event;
        private final KeyValuePair<ColorPickingSupport, Object> hits;

        private RunnableMoveEventImpl(final KeyValuePair<ColorPickingSupport, Object> current, final ColorPickingCameraListener listener,
                                      final PickingCameraEvent event, final KeyValuePair<ColorPickingSupport, Object> hit) {
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

        private final Set<KeyValuePair<ColorPickingSupport, Object>> hits;
        private final PickingCameraEvent event;

        private RunnableEventImpl(final Set<KeyValuePair<ColorPickingSupport, Object>> hits, final PickingCameraEvent event) {
            this.hits = hits;
            this.event = event;
        }

        @Override
        public void run() {
            if (hits.size() == 1) {
                for (final KeyValuePair<ColorPickingSupport, Object> hit : hits) {
                    try {
                        hit.getKey().notifyPicked(Collections.singleton(hit.getValue()), event);
                    } catch (final NullPointerException ex) {
                        logger.log(Level.WARNING, MessageFormat.format("{0}: Error notifying pick.", event.getSource().getScene()), ex);
                    }
                }
            } else {
                // group them by support class
                final Map<ColorPickingSupport, Set<Object>> map = new HashMap<>();
                for (final KeyValuePair<ColorPickingSupport, Object> hit : hits) {
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
                for (final Map.Entry<ColorPickingSupport, Set<Object>> entry : map.entrySet()) {
                    entry.getKey().notifyPicked(entry.getValue(), event);
                }
            }
        }
    }
}
