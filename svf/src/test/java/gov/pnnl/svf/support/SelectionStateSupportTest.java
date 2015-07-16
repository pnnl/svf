package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.EnumSet;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class SelectionStateSupportTest extends AbstractObjectTestBase<SelectionStateSupport> {

    private final Random random = new Random();
    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public SelectionStateSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test of newInstance method, of class SelectionStateSupport.
     */
    @Test
    public void testNewInstance_Actor() {
        final ShapeActor actor = new ShapeActor(scene);
        Assert.assertNotNull(SelectionStateSupport.newInstance(actor));
    }

    /**
     * Test of newInstance method, of class SelectionStateSupport.
     */
    @Test
    public void testNewInstance_Actor_Set() {
        final ShapeActor actor = new ShapeActor(scene);
        Assert.assertNotNull(SelectionStateSupport.newInstance(actor, EnumSet.of(SelectionState.SELECTED, SelectionState.HIGHLIGHTED)));
    }

    @Override
    protected SelectionStateSupport copyValueObject(final SelectionStateSupport object) {
        return object;
    }

    @Override
    protected void setFieldsToNull(final SelectionStateSupport object) {
        // no fields that accept null;
    }

    @Override
    protected SelectionStateSupport newValueObject() {
        final ShapeActor actor = new ShapeActor(scene);
        final SelectionStateSupport support = SelectionStateSupport.newInstance(actor);
        support.setActive(SelectionState.values()[random.nextInt(SelectionState.values().length)], random.nextBoolean());
        return support;
    }
}
