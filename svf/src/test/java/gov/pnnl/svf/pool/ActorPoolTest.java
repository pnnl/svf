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
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ActorPoolTest {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    @Test
    public void testActorPool() {
        final ActorPoolManagerImpl manager = new ActorPoolManagerImpl();
        scene.add(manager);
        manager.addPool(new Shape2DActorPoolImpl(scene));

        final Pool<ShapeActor> pool = manager.getPool(ShapeActor.class, null);
        Assert.assertEquals(0, pool.getPoolSize());
        final ShapeActor actor = pool.checkOut("0", null);
        try {
            Thread.sleep(100L);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertNotNull(actor);
        Assert.assertEquals("0", actor.getId());
        Assert.assertEquals(0, pool.getPoolSize());
        pool.checkIn(actor);
        try {
            Thread.sleep(100L);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertEquals(1, pool.getPoolSize());
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
