package gov.pnnl.svf.scene;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.lookup.LookupProviderFactory;
import gov.pnnl.svf.core.lookup.MultiLookupProvider;
import gov.pnnl.svf.core.util.StateUtil;
import gov.pnnl.svf.pool.ActorPoolManager;
import gov.pnnl.svf.pool.Pool;
import gov.pnnl.svf.support.AbstractSupport;
import gov.pnnl.svf.support.ChildSupport;
import gov.pnnl.svf.support.ChildSupportListener;
import gov.pnnl.svf.support.ParentSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;

/**
 * Class that provides additional functionality for a scene lookup.
 *
 * @author Arthur Bleeker
 */
public class SceneLookupImpl implements SceneLookup {

    private static final Logger logger = Logger.getLogger(SceneLookupImpl.class.toString());
    private static final int POOL_SIZE = 10;
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
    protected final MultiLookupProvider lookup = LookupProviderFactory.newMultiLookupProvider();
    protected final Map<String, Actor> actors = Collections.synchronizedMap(new HashMap<>());
    protected final Set<Actor> root = Collections.synchronizedSet(new LinkedHashSet<>());
    protected final Set<Actor> visible = Collections.synchronizedSet(new HashSet<>());
    // map of disposable objects and whether they have been removed from the scene or not
    protected final Map<Disposable, Boolean> disposables = Collections.synchronizedMap(new HashMap<>());
    protected final ChildSupportListener childListener = new ChildSupportListenerImpl();
    protected final PropertyChangeListener rootListener = new RootListenerImpl();
    protected final PropertyChangeListener visibleListener = new VisibleListenerImpl();
    protected final ObjectPool<List<Object>> pool = new StackObjectPool<>(new ListPoolableObjectFactoryImpl(), POOL_SIZE, 1);
    protected final SceneExt scene;
    private final UpdatableImpl cleanup;

    /**
     * Constructor
     *
     * @param scene reference to the parent scene
     */
    public SceneLookupImpl(final Scene scene) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene.getExtended();
        this.cleanup = new UpdatableImpl(this);
        lookup.add(cleanup);
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
        // check disposables
        final List<Disposable> leaked = new ArrayList<>();
        synchronized (disposables) {
            for (final Map.Entry<Disposable, Boolean> entry : disposables.entrySet()) {
                if (!entry.getKey().isDisposed() && entry.getValue()) {
                    leaked.add(entry.getKey());
                }
            }
        }
        if (!leaked.isEmpty()) {
            logger.log(Level.WARNING, "{0}: Found {1} disposable{2} removed from the scene but not disposed!", new Object[]{scene, leaked.size(), (leaked.size() == 1 ? "" : "s")});
            logger.log(Level.FINE, "{0}: Disposables Leaked: {0}", new Object[]{scene, leaked});
        }
        // clear collections
        clear();
        try {
            pool.close();
        } catch (final Exception ex) {
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to close the list pool.", scene), ex);
            }
        }
    }

    @Override
    public void clear() {
        lookup.clear();
        synchronized (this) {
            actors.clear();
            root.clear();
            visible.clear();
        }
        try {
            pool.clear();
        } catch (final Exception ex) {
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to clear the list pool.", scene), ex);
            }
        }
    }

    @Override
    public <T> void add(final T object) {
        // actors have additional actions
        if (object instanceof Actor) {
            final Actor actor = (Actor) object;
            // add the actor to the actor id lookup
            addActor(actor);
            // add the support objects if it's an actor
            final List<Object> all = borrowObject();
            try {
                actor.lookupAll(all);
                for (final Object obj : all) {
                    if (object instanceof AbstractSupport) {
                        lookupAdd(obj);
                    }
                }
            } finally {
                returnObject(all);
            }
        }
        // add the scene listeners
        scene.addListeners(object);
        lookupAdd(object);
        // debugging
        if (scene.getSceneBuilder().isVerbose()) {
            logger.log(Level.FINE, "{0}: Object was added to the scene lookup: {1}", new Object[]{scene, object});
        }
        // only dirty the scene for scene items
        if (object instanceof SceneItem && ((SceneItem) object).isVisible()) {
            scene.draw(((SceneItem) object).getDrawingPass());
        }
    }

    @Override
    public <T> void addAll(final Collection<T> objects) {
        DrawingPass modified = DrawingPass.NONE;
        for (final T object : objects) {
            // actors have additional actions
            if (object instanceof Actor) {
                final Actor actor = (Actor) object;
                // add the actor to the actor id lookup
                addActor(actor);
                // add the support objects if it's an actor
                final List<Object> all = borrowObject();
                try {
                    actor.lookupAll(all);
                    for (final Object obj : all) {
                        if (object instanceof AbstractSupport) {
                            lookupAdd(obj);
                        }
                    }
                } finally {
                    returnObject(all);
                }
            }
            if (object instanceof SceneItem) {
                final SceneItem item = (SceneItem) object;
                if (item.isVisible()) {
                    modified = modified.addDrawingPass(item.getDrawingPass());
                }
            }
            // add the scene listeners
            scene.addListeners(object);
        }
        // add to the lookup
        lookupAddAll(objects);
        // debugging
        if (scene.getSceneBuilder().isVerbose()) {
            logger.log(Level.FINE, "{0}: Objects were added to the scene lookup: {1}", new Object[]{scene, objects.size()});
        }
        // only dirty the scene as necessary
        if (modified != DrawingPass.NONE) {
            scene.draw(modified);
        }
    }

    @Override
    public <T> T lookup(final Class<? extends T> object) {
        return lookup.lookup(object);
    }

    @Override
    public <T> Set<T> lookupAll(final Class<T> object) {
        return lookup.lookupAll(object);
    }

    @Override
    public <T> void lookupAll(final Class<T> object, final Collection<T> out) {
        lookup.lookupAll(object, out);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <T> boolean remove(final T object) {
        // remove the scene listeners
        scene.removeListeners(object);
        // actors have additional actions
        if (object instanceof Actor) {
            final Actor actor = (Actor) object;
            // remove the actor from the actor id lookup
            removeActor(actor);
            // remove the support objects if it's an actor
            final List<Object> all = borrowObject();
            try {
                actor.lookupAll(all);
                for (final Object obj : all) {
                    // only remove the support object's that this actor owns
                    if (object instanceof AbstractSupport && actor.equals(((AbstractSupport) obj).getActor())) {
                        lookupRemove(obj);
                    }
                }
            } finally {
                returnObject(all);
            }
        }
        final boolean removed = lookupRemove(object);
        // only dirty the scene for actors
        if (removed) {
            // debugging
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.FINE, "{0}: Object was removed from the scene lookup: {1}", new Object[]{scene, object});
            }
            // only dirty the scene for scene items
            if (object instanceof SceneItem && ((SceneItem) object).isVisible()) {
                scene.draw(((SceneItem) object).getDrawingPass());
            }
        }
        return removed;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <T> boolean removeAll(final Collection<T> objects) {
        DrawingPass modified = DrawingPass.NONE;
        for (final Object object : objects) {
            // remove the scene listeners
            scene.removeListeners(object);
            // actors have additional actions
            if (object instanceof Actor) {
                final Actor actor = (Actor) object;
                // remove the actor from the actor id lookup
                removeActor(actor);
                // remove the support objects if it's an actor
                final List<Object> all = borrowObject();
                try {
                    actor.lookupAll(all);
                    for (final Object obj : all) {
                        // only remove the support object's that this actor owns
                        if (object instanceof AbstractSupport && actor.equals(((AbstractSupport) obj).getActor())) {
                            lookupRemove(obj);
                        }
                    }
                } finally {
                    returnObject(all);
                }
            }
            // only dirty the scene for scene items
            if (object instanceof SceneItem) {
                final SceneItem item = (SceneItem) object;
                if (item.isVisible()) {
                    modified = modified.addDrawingPass(item.getDrawingPass());
                }
            }
        }
        final boolean removed = lookupRemoveAll(objects);
        // only dirty the scene for actors
        if (removed) {
            // debugging
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.FINE, "{0}: Objects were removed from the scene lookup: {1}", new Object[]{scene, objects});
            }
            // only dirty the scene as necessary
            if (modified != DrawingPass.NONE) {
                scene.draw(modified);
            }
        }
        return removed;
    }

    @Override
    public Set<Object> lookupAll() {
        return lookup.lookupAll();
    }

    @Override
    public void lookupAll(final Collection<Object> out) {
        lookup.lookupAll(out);
    }

    @Override
    public Actor getActor(final String id) {
        return actors.get(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Actor> T getActor(final Class<T> clazz, final String type, final String id, final Object data) {
        T actor = (T) getActor(id);
        if (actor == null) {
            final ActorPoolManager manager = lookup.lookup(ActorPoolManager.class);
            if (manager != null) {
                final Pool<T> pool = manager.getPool(clazz, type);
                if (pool != null) {
                    actor = pool.checkOut(id, data);
                }
            }
        }
        return actor;
    }

    /**
     * Set of all the root actors in the scene that are currently visible. The
     * collection returned from this method should never be modified.
     *
     * @return a reference to the visible root actors
     */
    protected Collection<Actor> getVisibleRootActors() {
        final Collection<Actor> copy;
        synchronized (root) {
            copy = new ArrayList<>(root);
        }
        synchronized (visible) {
            copy.retainAll(visible);
        }
        return copy;
    }

    /**
     * Set of all the root actors in the scene that are currently visible.
     *
     * @param out reference to the collection to populate
     */
    protected void getVisibleRootActors(final Collection<Actor> out) {
        if (out == null) {
            throw new NullPointerException("out");
        }
        out.clear();
        synchronized (root) {
            out.addAll(root);
        }
        synchronized (visible) {
            out.retainAll(visible);
        }
    }

    /**
     * Map of actor id to the actor.
     *
     * @return the map of id to actor
     */
    protected Map<String, Actor> getActors() {
        return actors;
    }

    private void addActor(final Actor actor) {
        synchronized (actors) {
            final String id = actor.getId();
            final Actor found = actors.get(id);
            if (found != null && found != actor) {
                throw new IllegalArgumentException("Scene lookup already contains an actor with the id: " + id);
            }
            actors.put(id, actor);
        }
        actor.getPropertyChangeSupport().addPropertyChangeListener(Actor.ROOT, rootListener);
        actor.getPropertyChangeSupport().addPropertyChangeListener(Actor.VISIBLE, visibleListener);
        rootListener.propertyChange(new PropertyChangeEvent(actor, Actor.ROOT, null, actor.isRoot()));
        visibleListener.propertyChange(new PropertyChangeEvent(actor, Actor.VISIBLE, null, actor.isVisible()));
        final ChildSupport children = actor.lookup(ChildSupport.class);
        if (children != null) {
            // add a listener so that when a child gets added
            // it will automatically get added to the scene
            children.removeListener(childListener);
            children.addListener(childListener);
            // recursively add the children
            addAll(children.getChildren());
        }
    }

    private void removeActor(final Actor actor) {
        // only remove the actor from the actor map if it matches the instance
        final Actor found = actors.get(actor.getId());
        if (actor == found) {
            actors.remove(actor.getId());
            actor.getPropertyChangeSupport().removePropertyChangeListener(Actor.VISIBLE, visibleListener);
            actor.getPropertyChangeSupport().removePropertyChangeListener(Actor.ROOT, rootListener);
            root.remove(actor);
            visible.remove(actor);
        }
        final ChildSupport children = actor.lookup(ChildSupport.class);
        if (children != null) {
            // remove the listener that gets called when a child gets added
            children.removeListener(childListener);
            // recursively remove the children from the scene
            for (final Actor child : children.getChildren()) {
                remove(child);
            }
        }
        // if this is a child of something that is still active in the scene then remove it from the parent's children list
        final ParentSupport parentSupport = actor.lookup(ParentSupport.class);
        if (parentSupport != null) {
            final Actor parent = parentSupport.getParent();
            if (parent != null) {
                final Actor instance = scene.getActor(parent.getId());
                if (instance != null) {
                    final ChildSupport parentsChildren = parent.lookup(ChildSupport.class);
                    if (parentsChildren != null) {
                        parentsChildren.remove(actor);
                    }
                }
            }
        }
    }

    private List<Object> borrowObject() {
        try {
            return pool.borrowObject();
        } catch (final Exception ex) {
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to borrow list from pool.", scene), ex);
            }
            return new ArrayList<>();
        }
    }

    private void returnObject(final List<Object> obj) {
        try {
            pool.returnObject(obj);
        } catch (final Exception ex) {
            logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to return list to pool.", scene), ex);
        }
    }

    private void lookupAdd(final Object object) {
        // check for disposable
        if (object instanceof Disposable) {
            disposables.put((Disposable) object, Boolean.FALSE);
        }
        lookup.add(object);
    }

    private <T> void lookupAddAll(final Collection<T> objects) {
        // check for disposables
        for (Object object : objects) {
            if (object instanceof Disposable) {
                disposables.put((Disposable) object, Boolean.FALSE);
            }
        }
        lookup.addAll(objects);
    }

    private boolean lookupRemove(final Object object) {
        // check for disposable
        if (object instanceof Disposable) {
            disposables.put((Disposable) object, Boolean.TRUE);
        }
        return lookup.remove(object);
    }

    private <T> boolean lookupRemoveAll(final Collection<T> objects) {
        // check for disposables
        for (final Object object : objects) {
            if (object instanceof Disposable) {
                disposables.put((Disposable) object, Boolean.TRUE);
            }
        }
        return lookup.removeAll(objects);
    }

    private class ChildSupportListenerImpl implements ChildSupportListener {

        @Override
        public void childAdded(final Actor child) {
            add(child);
        }

        @Override
        public void childRemoved(final Actor child) {
            remove(child);
        }
    }

    private class RootListenerImpl implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            // add the actor to the visible list if it's a root and it's visible
            final Actor actor = (Actor) evt.getSource();
            if (actor.isRoot()) {
                // add it to the root set if necessary
                root.add(actor);
            } else {
                root.remove(actor);
            }
        }
    }

    private class VisibleListenerImpl implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            // add the actor to the visible list if it's a root and it's visible
            final Actor actor = (Actor) evt.getSource();
            if (actor.isVisible()) {
                // remove it from the visible set if necessary
                visible.add(actor);
            } else {
                visible.remove(actor);
            }
        }
    }

    protected static class ListPoolableObjectFactoryImpl extends BasePoolableObjectFactory<List<Object>> {

        protected ListPoolableObjectFactoryImpl() {
        }

        @Override
        public List<Object> makeObject() throws Exception {
            return new ArrayList<>();
        }

        @Override
        public void passivateObject(final List<Object> obj) throws Exception {
            obj.clear();
        }
    }

    /**
     * Update used to perform cleanup in the scene lookup.
     */
    protected static class UpdatableImpl implements Updatable {

        private static final long INTERVAL = 1000L;
        private final SceneLookupImpl parent;
        private long elapsed = 0L;

        protected UpdatableImpl(final SceneLookupImpl parent) {
            this.parent = parent;
        }

        @Override
        public void update(final long delta) {
            synchronized (this) {
                elapsed += delta;
                // only update every second or more
                if (elapsed >= INTERVAL) {
                    // cleanup disposables
                    synchronized (parent.disposables) {
                        for (final Iterator<Map.Entry<Disposable, Boolean>> it = parent.disposables.entrySet().iterator(); it.hasNext();) {
                            final Map.Entry<Disposable, Boolean> entry = it.next();
                            if (entry.getKey().isDisposed() && entry.getValue()) {
                                it.remove();
                            }
                        }
                    }
                    // reset counts
                    elapsed = 0L;
                }
            }
        }
    }
}
