package gov.pnnl.svf.pool;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.picking.PickingSupport;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ChildSupport;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.ConfigUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ActorPoolManagerTest {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public ActorPoolManagerTest() {
    }

    /**
     * Test of addActorPool method, of class ActorPoolManagerImpl.
     */
    @Test
    public void testAddActorPool() {
        final ActorPoolManagerImpl manager = new ActorPoolManagerImpl();

        Assert.assertNull(manager.getPool(ShapeActor.class, null));
        manager.addPool(new Shape2DActorPoolImpl(scene));
        Assert.assertNotNull(manager.getPool(ShapeActor.class, null));
    }

    /**
     * Test of getActorPool method, of class ActorPoolManagerImpl.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetActorPool() {
        final ActorPoolManagerImpl manager = new ActorPoolManagerImpl();

        manager.addPool(new Shape2DActorPoolImpl(scene));
        manager.addPool(new Shape2DActorPoolImpl(scene));
    }

    /**
     * Test of getActorPool method, of class ActorPoolManagerImpl.
     */
    @Test
    public void testGetActorPool2() {
        final ActorPoolManagerImpl manager = new ActorPoolManagerImpl();
        final Shape2DActorPoolImpl expected = new Shape2DActorPoolImpl(scene);

        manager.addPool(expected);
        try {
            manager.addPool(new Shape2DActorPoolImpl(scene));
        } catch (final IllegalArgumentException ex) {
            // ignore exception
        }
        Assert.assertTrue(expected == manager.getPool(ShapeActor.class, null));
    }

    private static class Shape2DActorPoolImpl extends AbstractActorPool<ShapeActor> {

        Shape2DActorPoolImpl(final Scene scene) {
            super(scene);
        }

        @Override
        protected ShapeActor create(final String id, final Object data) {
            final ShapeActor actor = new ShapeActor(getScene(), id);
            TransformSupport.newInstance(actor);
            ColorSupport.newInstance(actor);
            PickingSupport.newInstance(actor);
            ChildSupport.newInstance(actor);
            return actor;
        }

        @Override
        protected void initialize(final ShapeActor actor, final Object data) {
            // no init necessary
        }

        @Override
        protected boolean validate(final ShapeActor actor) {
            return actor != null;
        }
    }
}
