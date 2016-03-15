package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ChildSupportTest extends AbstractObjectTestBase<ChildSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public ChildSupportTest() {
        collisionIterateLength = 1000;
    }

    @Test
    public void testAddWithParentSupport() {
        final ChildSupport object = newValueObject();
        Assert.assertNotNull(object);
        final ShapeActor child = new ShapeActor(scene);
        object.add(child);
        Assert.assertNotNull(child.lookup(ParentSupport.class));
        Assert.assertEquals(object.getActor(), child.lookup(ParentSupport.class).getParent());
    }

    /**
     * Test of newInstance method, of class ChildSupport.
     */
    @Test
    public void testNewInstance_Actor() {
        final ChildSupport object = newValueObject();
        Assert.assertNotNull(object);
        final ShapeActor child = new ShapeActor(scene);
        object.add(child);
        Assert.assertNotNull(child.lookup(ParentSupport.class));
        Assert.assertEquals(object.getActor(), child.lookup(ParentSupport.class).getParent());
    }

    /**
     * Test of newInstance method, of class ChildSupport.
     */
    @Test
    public void testNewInstance_Actor_boolean() {
        // ordered
        {
            final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), true);
            Assert.assertNotNull(support);
            final LinkedHashSet<Actor> children = new LinkedHashSet<>();
            for (int i = 0; i < 10; i++) {
                final ShapeActor child = new ShapeActor(scene);
                children.add(child);
            }
            support.addAll(children);
            Assert.assertEquals(children, new LinkedHashSet<>(support.getChildren()));
        }
        // non ordered
        {
            final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
            Assert.assertNotNull(support);
            final Set<Actor> children = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                final ShapeActor child = new ShapeActor(scene);
                children.add(child);
            }
            support.addAll(children);
            Assert.assertEquals(children, new HashSet<>(support.getChildren()));
        }
    }

    /**
     * Test of add method, of class ChildSupport.
     */
    @Test
    public void testAdd() {
        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
        Assert.assertNotNull(support);
        final Set<Actor> children = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final ShapeActor child = new ShapeActor(scene);
            children.add(child);
            support.add(child);
        }
        Assert.assertEquals(children, new HashSet<>(support.getChildren()));
    }

    /**
     * Test of addAll method, of class ChildSupport.
     */
    @Test
    public void testAddAll() {
        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
        Assert.assertNotNull(support);
        final Set<Actor> children = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final ShapeActor child = new ShapeActor(scene);
            children.add(child);
        }
        support.addAll(children);
        Assert.assertEquals(children, new HashSet<>(support.getChildren()));
    }

    //    /**
    //     * Test of addInstance method, of class ChildSupport.
    //     */
    //    @Test(expected = UnsupportedOperationException.class)
    //    public void testAddInstance() {
    //        final ChildSupport object = newValueObject();
    //        object.addInstance(new ShapeActor(scene));
    //    }
    /**
     * Test of clear method, of class ChildSupport.
     */
    @Test
    public void testClear() {
        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
        Assert.assertNotNull(support);
        final Set<Actor> children = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final ShapeActor child = new ShapeActor(scene);
            children.add(child);
        }
        support.addAll(children);
        support.clear();
        Assert.assertEquals(0, support.size());
    }

    /**
     * Test of contains method, of class ChildSupport.
     */
    @Test
    public void testContains() {
        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
        Assert.assertNotNull(support);
        ShapeActor child = null;
        final Set<Actor> children = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            child = new ShapeActor(scene);
            children.add(child);
        }
        support.addAll(children);
        Assert.assertTrue(support.getChildren().contains(child));
    }

    /**
     * Test of containsAll method, of class ChildSupport.
     */
    @Test
    public void testContainsAll() {
        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
        Assert.assertNotNull(support);
        final Set<Actor> children = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final ShapeActor child = new ShapeActor(scene);
            children.add(child);
        }
        support.addAll(children);
        Assert.assertTrue(support.getChildren().containsAll(children));
    }

    /**
     * Test of isEmpty method, of class ChildSupport.
     */
    @Test
    public void testIsEmpty() {
        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
        Assert.assertNotNull(support);
        final Set<Actor> children = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final ShapeActor child = new ShapeActor(scene);
            children.add(child);
        }
        support.addAll(children);
        Assert.assertFalse(support.getChildren().isEmpty());
        support.clear();
        Assert.assertTrue(support.getChildren().isEmpty());
    }

    /**
     * Test of remove method, of class ChildSupport.
     */
    @Test
    public void testRemove() {
        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
        Assert.assertNotNull(support);
        final ShapeActor removed = new ShapeActor(scene);
        support.add(removed);
        final Set<Actor> children = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final ShapeActor child = new ShapeActor(scene);
            children.add(child);
        }
        support.addAll(children);
        Assert.assertNotSame(children, new HashSet<>(support.getChildren()));
        support.remove(removed);
        Assert.assertEquals(children, new HashSet<>(support.getChildren()));
    }

    /**
     * Test of removeAll method, of class ChildSupport.
     */
    @Test
    public void testRemoveAll() {
        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
        Assert.assertNotNull(support);
        final ShapeActor removed = new ShapeActor(scene);
        support.add(removed);
        final Set<Actor> children = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final ShapeActor child = new ShapeActor(scene);
            children.add(child);
        }
        support.addAll(children);
        Assert.assertNotSame(children, new HashSet<>(support.getChildren()));
        support.removeAll(children);
        Assert.assertEquals(Collections.<Actor>singleton(removed), new HashSet<>(support.getChildren()));
    }

    //    /**
    //     * Test of retainAll method, of class ChildSupport.
    //     */
    //    @Test(expected = UnsupportedOperationException.class)
    //    public void testRetainAll() {
    //        final ChildSupport support = ChildSupport.newInstance(new ShapeActor(scene), false);
    //        Assert.assertNotNull(support);
    //        final Set<Actor> children = new HashSet<Actor>();
    //        for (int i = 0; i < 10; i++) {
    //            final ShapeActor child = new ShapeActor(scene);
    //            children.add(child);
    //        }
    //        support.addAll(children);
    //        Assert.assertEquals(children, new HashSet<Actor>(support.getChildren()));
    //        support.getChildren().retainAll(children);
    //        Assert.assertEquals(children, new HashSet<Actor>(support.getChildren()));
    //    }
    @Override
    protected ChildSupport copyValueObject(final ChildSupport object) {
        return object;
    }

    @Override
    protected ChildSupport newValueObject() {
        return ChildSupport.newInstance(new ShapeActor(scene));
    }

    @Override
    protected void setFieldsToNull(final ChildSupport object) {
        // no nullable fields
    }
}
