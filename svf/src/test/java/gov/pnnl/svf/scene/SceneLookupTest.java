package gov.pnnl.svf.scene;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.animation.RotationAnimationSupport;
import gov.pnnl.svf.picking.PickingSupport;
import gov.pnnl.svf.support.ChildSupport;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class SceneLookupTest {

    private static final Logger logger = Logger.getLogger(SceneLookupTest.class.getName());
    private static final int ACTOR_COUNT = 10000;
    private static final int CHILDREN_COUNT = 3;
    private static final Random random = new Random();
    private static volatile SceneExt scene;

    /**
     * Set up for testing
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    }

    /**
     * Clean up after testing
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDown() throws Exception {
        if (scene != null) {
            scene.dispose();
        }
    }

    /**
     * Test of add method, of class SceneLookupImpl.
     */
    @Test
    public void testAdd() {
        final SceneLookupImpl lookup = new SceneLookupImpl(scene);

        final Actor parent = new ShapeActor(scene, "parent");
        final Actor child = new ShapeActor(scene, "child");
        ChildSupport.newInstance(parent).add(child);

        lookup.add(parent);
        Assert.assertEquals(2, lookup.lookupAll(Actor.class).size());
        Assert.assertEquals(parent, lookup.getActors().get("parent"));
        Assert.assertEquals(child, lookup.getActors().get("child"));

        lookup.remove(parent);
        Assert.assertEquals(0, lookup.lookupAll(Actor.class).size());
        Assert.assertNull(lookup.getActors().get("parent"));
        Assert.assertNull(lookup.getActors().get("child"));

        lookup.add(parent);
        Assert.assertEquals(2, lookup.lookupAll(Actor.class).size());
        Assert.assertEquals(parent, lookup.getActors().get("parent"));
        Assert.assertEquals(child, lookup.getActors().get("child"));
    }

    /**
     * Test performance of add method, of class SceneLookupImpl.
     */
    @Test
    public void testAddPerformance() {
        final List<Actor> actors = SceneLookupTest.createPerformanceTestActors(ACTOR_COUNT, CHILDREN_COUNT);

        long start = System.currentTimeMillis();
        for (final Actor actor : actors) {
            scene.add(actor);
        }
        logger.log(Level.INFO, "The method add() for {0} actors with {1} children took {2} seconds.",
                   new Object[]{ACTOR_COUNT, CHILDREN_COUNT, String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0)});

        start = System.currentTimeMillis();
        for (final Actor actor : actors) {
            scene.remove(actor);
        }
        logger.log(Level.INFO, "The method remove() for {0} actors with {1} children took {2} seconds.",
                   new Object[]{ACTOR_COUNT, CHILDREN_COUNT, String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0)});
    }

    /**
     * Test of addAll method, of class SceneLookupImpl.
     */
    @Test
    public void testAddAll() {
        final SceneLookupImpl lookup = new SceneLookupImpl(scene);

        final List<Actor> actors = new ArrayList<>();
        final List<Actor> children = new ArrayList<>();
        for (int i = 0; i < ACTOR_COUNT; i++) {
            final Actor parent = new ShapeActor(scene, "parent" + i);
            final Actor child = new ShapeActor(scene, "child" + i);
            ChildSupport.newInstance(parent).add(child);
            actors.add(parent);
            children.add(child);
        }

        lookup.addAll(actors);
        Assert.assertEquals(ACTOR_COUNT * 2, lookup.lookupAll(Actor.class).size());
        Assert.assertEquals(actors.get(3), lookup.getActors().get("parent3"));
        Assert.assertEquals(children.get(2), lookup.getActors().get("child2"));

        lookup.remove(actors.get(8));
        Assert.assertEquals(ACTOR_COUNT * 2 - 2, lookup.lookupAll(Actor.class).size());
        Assert.assertNull(lookup.getActors().get("parent8"));
        Assert.assertNull(lookup.getActors().get("child8"));

        lookup.add(actors.get(8));
        Assert.assertEquals(ACTOR_COUNT * 2, lookup.lookupAll(Actor.class).size());
        Assert.assertEquals(actors.get(8), lookup.getActors().get("parent8"));
        Assert.assertEquals(children.get(8), lookup.getActors().get("child8"));

        lookup.removeAll(actors);
        Assert.assertEquals(0, lookup.lookupAll(Actor.class).size());
        Assert.assertEquals(null, lookup.getActors().get("parent3"));
        Assert.assertEquals(null, lookup.getActors().get("child2"));
    }

    /**
     * Test performance of addAll method, of class SceneLookupImpl.
     */
    @Test
    public void testAddAllPerformance() {
        final List<Actor> actors = SceneLookupTest.createPerformanceTestActors(ACTOR_COUNT, CHILDREN_COUNT);

        long start = System.currentTimeMillis();
        scene.addAll(actors);
        logger.log(Level.INFO, "The method addAll() for {0} actors with {1} children took {2} seconds.",
                   new Object[]{ACTOR_COUNT, CHILDREN_COUNT, String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0)});

        start = System.currentTimeMillis();
        scene.removeAll(actors);
        logger.log(Level.INFO, "The method removeAll() for {0} actors with {1} children took {2} seconds.",
                   new Object[]{ACTOR_COUNT, CHILDREN_COUNT, String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0)});
    }

    /**
     * Test of getActors method, of class SceneLookupImpl.
     */
    @Test
    public void testGetActors() {
        final SceneLookupImpl lookup = new SceneLookupImpl(scene);

        Assert.assertNull(lookup.getActors().get("a"));
        final Actor a = new ShapeActor(scene, "a");
        lookup.add(a);
        Assert.assertNotNull(lookup.getActors().get("a"));
    }

    /**
     * Test of getVisibleRootActors method, of class SceneLookupImpl.
     */
    @Test
    public void testGetVisibleRootActors() {
        final SceneLookupImpl lookup = new SceneLookupImpl(scene);

        final Actor a = new ShapeActor(scene, "a");
        a.setVisible(true);
        lookup.add(a);
        final Actor b = new ShapeActor(scene, "b");
        b.setVisible(false);
        lookup.add(b);

        Assert.assertEquals(1, lookup.getVisibleRootActors().size());
        Assert.assertTrue(lookup.getVisibleRootActors().contains(a));

        a.setVisible(false);
        Assert.assertEquals(0, lookup.getVisibleRootActors().size());
        Assert.assertFalse(lookup.getVisibleRootActors().contains(a));

        a.setVisible(true);
        b.setVisible(true);
        Assert.assertEquals(2, lookup.getVisibleRootActors().size());
        Assert.assertTrue(lookup.getVisibleRootActors().contains(a));
        Assert.assertTrue(lookup.getVisibleRootActors().contains(b));
    }

    /**
     * Test of lookup method, of class SceneLookupImpl.
     */
    @Test
    public void testLookup() {
        final SceneLookupImpl lookup = new SceneLookupImpl(scene);

        final Actor a = new ShapeActor(scene, "a");
        lookup.add(a);
        Assert.assertEquals(a, lookup.lookup(Actor.class));

        final Actor b = new ShapeActor(scene, "b");
        lookup.add(b);
        Assert.assertEquals(b, lookup.lookup(Actor.class));

        // this no longer holds true and shouldn't have been the case anyway
        //        lookup.remove(b);
        //        Assert.assertEquals(a, lookup.lookup(Actor.class));
    }

    /**
     * Test of lookupAll method, of class SceneLookupImpl.
     */
    @Test
    public void testLookupAll() {
        final SceneLookupImpl lookup = new SceneLookupImpl(scene);

        final Actor a = new ShapeActor(scene, "a");
        lookup.add(a);
        Assert.assertEquals(1, lookup.lookupAll(Actor.class).size());
        Assert.assertTrue(lookup.lookupAll(Actor.class).contains(a));

        final Actor b = new ShapeActor(scene, "b");
        lookup.add(b);
        Assert.assertEquals(2, lookup.lookupAll(Actor.class).size());
        Assert.assertTrue(lookup.lookupAll(Actor.class).contains(a));
        Assert.assertTrue(lookup.lookupAll(Actor.class).contains(b));

        lookup.remove(b);
        Assert.assertEquals(1, lookup.lookupAll(Actor.class).size());
        Assert.assertTrue(lookup.lookupAll(Actor.class).contains(a));
        Assert.assertFalse(lookup.lookupAll(Actor.class).contains(b));
    }

    /**
     * Test of remove method, of class SceneLookupImpl.
     */
    @Test
    public void testRemove() {
        final SceneLookupImpl lookup = new SceneLookupImpl(scene);

        final Actor a = new ShapeActor(scene, "a");
        lookup.add(a);
        lookup.remove(a);
        Assert.assertNull(lookup.getActors().get("a"));
    }

    private static List<Actor> createPerformanceTestActors(final int actorCount, final int childrenCount) {
        final List<Actor> actors = new ArrayList<>();
        for (int i = 0; i < actorCount; i++) {
            // create parent
            final ShapeActor parent = new ShapeActor(scene, "parent" + i);
            TransformSupport.newInstance(parent);
            ColorSupport.newInstance(parent);
            PickingSupport.newInstance(parent);
            RotationAnimationSupport.newInstance(parent, 100L, 0L, true, random.nextDouble());
            // create children
            final ChildSupport children = ChildSupport.newInstance(parent);
            for (int j = 0; j < childrenCount; j++) {
                final Actor child = new ShapeActor(scene, "child" + i + j);
                TransformSupport.newInstance(child);
                ColorSupport.newInstance(child);
                children.add(child);
            }
            // add to return list
            actors.add(parent);
        }
        return actors;
    }
}
