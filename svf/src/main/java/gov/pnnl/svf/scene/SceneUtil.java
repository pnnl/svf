package gov.pnnl.svf.scene;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.util.PropertyChangeSupportWrapper;
import gov.pnnl.svf.core.util.StateUtil;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.picking.ColorPickingUtils;
import gov.pnnl.svf.update.TaskManager;
import gov.pnnl.svf.update.UpdateTaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class for containing common utility methods and fields for a scene.
 *
 * @author Arthur Bleeker
 */
public class SceneUtil implements Disposable {

    /**
     * State mask for boolean field in this actor.
     */
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[0];
    /**
     * State used by flags in this actor to save memory space. Up to 8 states
     * can be used and should go in the following order: 0x01, 0x02, 0x04, 0x08,
     * 0x10, 0x20, 0x40, and (byte) 0x80. Set the initial state in the
     * constructor by or-ing the new states with the current state.
     */
    private byte state = StateUtil.NONE;
    private final SceneExt scene;
    private final SceneBuilder builder;
    private final PropertyChangeSupport pcs;
    private final SceneLookupImpl lookup;
    private final ColorPickingUtils colorPickingUtils;
    private final TaskManager taskManager;
    private final ViewportListener viewportListener;
    private Rectangle viewport;

    /**
     * Constructor
     *
     * @param scene   reference to the parent scene
     * @param builder reference to the scene builder used to construct the scene
     */
    public SceneUtil(final SceneExt scene, final SceneBuilder builder) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        if (builder == null) {
            throw new NullPointerException("builder");
        }
        this.scene = scene;
        this.builder = new ImmutableSceneBuilder(builder);
        pcs = new PropertyChangeSupportWrapper(this.scene);
        lookup = new SceneLookupWrapper(this, this.scene);
        taskManager = new UpdateTaskManager(this.scene);
        colorPickingUtils = new ColorPickingUtils(this.scene);
        viewportListener = new ViewportListener(this);
        viewport = Rectangle.ZERO;
        pcs.addPropertyChangeListener(Scene.VIEWPORT, viewportListener);
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return StateUtil.isValue(state, DISPOSED_MASK);
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            state = StateUtil.setValue(state, DISPOSED_MASK);
        }
        final PropertyChangeListener[] listeners = pcs.getPropertyChangeListeners();
        for (final PropertyChangeListener listener : listeners) {
            pcs.removePropertyChangeListener(listener);
        }
        pcs.removePropertyChangeListener(Scene.VIEWPORT, viewportListener);
        colorPickingUtils.dispose();
        taskManager.dispose();
        viewportListener.dispose();
        lookup.dispose();
    }

    /**
     * @return a reference to an immutable scene builder that was used to
     *         construct the scene
     */
    public SceneBuilder getSceneBuilder() {
        return builder;
    }

    /**
     * @return property change support for the scene
     */
    public PropertyChangeSupport getPropertyChangeSupport() {
        return pcs;
    }

    /**
     * @return the scene lookup
     */
    public SceneLookupImpl getSceneLookup() {
        return lookup;
    }

    /**
     * @return the color utils
     */
    public ColorPickingUtils getColorPickingUtils() {
        return colorPickingUtils;
    }

    /**
     * @return the task manager
     */
    public TaskManager getTaskManager() {
        return taskManager;
    }

    /**
     * @return the viewport
     */
    public Rectangle getViewport() {
        synchronized (this) {
            return viewport;
        }
    }

    /**
     * @param viewport set the viewport
     */
    public void setViewport(final Rectangle viewport) {
        if (viewport == null) {
            throw new NullPointerException("viewport");
        }
        final Rectangle old;
        synchronized (this) {
            old = this.viewport;
            this.viewport = viewport;
        }
        pcs.firePropertyChange(Scene.VIEWPORT, old, viewport);
    }

    protected static class SceneLookupWrapper extends SceneLookupImpl {

        private final SceneUtil utils;

        protected SceneLookupWrapper(final SceneUtil utils, final Scene scene) {
            super(scene);
            this.utils = utils;
        }

        @Override
        public <T> void add(final T object) {
            if (scene.isDisposed()) {
                return;
            }
            super.add(object);
            if (object instanceof Camera) {
                final Camera camera = (Camera) object;
                camera.setViewport(utils.getViewport());
            }
        }

        @Override
        public <T> void addAll(final Collection<T> objects) {
            if (scene.isDisposed()) {
                return;
            }
            super.addAll(objects);
            for (final T object : objects) {
                if (object instanceof Camera) {
                    final Camera camera = (Camera) object;
                    camera.setViewport(utils.getViewport());
                }
            }
        }
    }

    protected static class ViewportListener implements PropertyChangeListener, Disposable {

        /**
         * State mask for boolean field in this actor.
         */
        private static final byte DISPOSED_MASK = StateUtil.getMasks()[0];
        /**
         * State used by flags in this actor to save memory space. Up to 8
         * states can be used and should go in the following order: 0x01, 0x02,
         * 0x04, 0x08, 0x10, 0x20, 0x40, and (byte) 0x80. Set the initial state
         * in the constructor by or-ing the new states with the current state.
         */
        private byte state = StateUtil.NONE;
        private final SceneLookupImpl lookup;
        private final List<Camera> cameras = new ArrayList<>();

        protected ViewportListener(final SceneUtil utils) {
            lookup = utils.getSceneLookup();
        }

        @Override
        public boolean isDisposed() {
            synchronized (this) {
                return StateUtil.isValue(state, DISPOSED_MASK);
            }
        }

        @Override
        public void dispose() {
            synchronized (this) {
                if (isDisposed()) {
                    return;
                }
                state = StateUtil.setValue(state, DISPOSED_MASK);
            }
            synchronized (cameras) {
                cameras.clear();
            }
        }

        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            final Rectangle viewport = (Rectangle) event.getNewValue();
            synchronized (cameras) {
                lookup.lookupAll(Camera.class, cameras);
                for (final Camera camera : cameras) {
                    camera.setViewport(viewport);
                }
            }
        }
    }
}
