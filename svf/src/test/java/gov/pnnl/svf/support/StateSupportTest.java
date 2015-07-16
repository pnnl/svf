package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class StateSupportTest extends AbstractObjectTestBase<AbstractStateSupport<?>> {

    private final Random random = new Random();
    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public StateSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test of setActive method, of class StateSupport.
     */
    @Test
    public void testSetActive() {
        final ShapeActor actor = new ShapeActor(scene);
        final SelectionStateSupport support = SelectionStateSupport.newInstance(actor);

        Assert.assertFalse(support.isActive(SelectionState.SELECTED));
        Assert.assertFalse(support.isActive(SelectionState.HIGHLIGHTED));
        Assert.assertFalse(support.isActive(SelectionState.RELATED));

        support.setActive(SelectionState.RELATED, true);

        Assert.assertFalse(support.isActive(SelectionState.SELECTED));
        Assert.assertFalse(support.isActive(SelectionState.HIGHLIGHTED));
        Assert.assertTrue(support.isActive(SelectionState.RELATED));

        support.setActive(SelectionState.RELATED, false);

        Assert.assertFalse(support.isActive(SelectionState.SELECTED));
        Assert.assertFalse(support.isActive(SelectionState.HIGHLIGHTED));
        Assert.assertFalse(support.isActive(SelectionState.RELATED));
    }

    /**
     * Test of getActiveStates method, of class StateSupport.
     */
    @Test
    public void testGetActiveStates() {
        final ShapeActor actor = new ShapeActor(scene);
        final SelectionStateSupport support = SelectionStateSupport.newInstance(actor);

        Assert.assertEquals(Collections.<SelectionState>emptySet(), support.getActiveStates());

        support.setActive(SelectionState.RELATED, true);

        Assert.assertEquals(EnumSet.of(SelectionState.RELATED), support.getActiveStates());

        support.setActive(SelectionState.RELATED, false);

        Assert.assertEquals(Collections.<SelectionState>emptySet(), support.getActiveStates());
    }

    /**
     * Test of getAllowedStates method, of class StateSupport.
     */
    @Test
    public void testGetAllowedStates() {
        final ShapeActor actor = new ShapeActor(scene);
        final SelectionStateSupport support = SelectionStateSupport.newInstance(actor);

        Assert.assertEquals(EnumSet.allOf(SelectionState.class), support.getAllowedStates());
    }

    /**
     * Test of getAllowedStates method, of class StateSupport.
     */
    @Test
    public void testGetAllowedStates_1() {
        final ShapeActor actor = new ShapeActor(scene);
        final SelectionStateSupport support = SelectionStateSupport.newInstance(actor, EnumSet.of(SelectionState.SELECTED, SelectionState.HIGHLIGHTED));

        Assert.assertEquals(EnumSet.of(SelectionState.SELECTED, SelectionState.HIGHLIGHTED), support.getAllowedStates());
    }

    @Override
    protected AbstractStateSupport<?> copyValueObject(final AbstractStateSupport<?> object) {
        return object;
    }

    @Override
    protected void setFieldsToNull(final AbstractStateSupport<?> object) {
        // no fields that accept null;
    }

    @Override
    protected AbstractStateSupport<?> newValueObject() {
        final ShapeActor actor = new ShapeActor(scene);
        final SelectionStateSupport support = SelectionStateSupport.newInstance(actor);
        support.setActive(SelectionState.values()[random.nextInt(SelectionState.values().length)], random.nextBoolean());
        return support;
    }
}
