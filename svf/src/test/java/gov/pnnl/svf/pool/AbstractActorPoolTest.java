package gov.pnnl.svf.pool;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.ConfigUtil;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class AbstractActorPoolTest {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public AbstractActorPoolTest() {
    }

    /**
     * Test of getScene method, of class AbstractActorPool.
     */
    @Test
    public void testGetScene() {
        final Shape2DActorPoolImpl pool = new Shape2DActorPoolImpl(scene);

        Assert.assertTrue(pool.getScene() == scene);
    }

    /**
     * Test of getPoolType method, of class AbstractActorPool.
     */
    @Test
    public void testGetPoolName() {
        final Shape2DActorPoolImpl pool = new Shape2DActorPoolImpl(scene);

        Assert.assertEquals(null, pool.getPoolType());
    }

    /**
     * Test of checkOut method, of class AbstractActorPool.
     */
    @Test
    public void testCheckOut() {
        final Shape2DActorPoolImpl pool = new Shape2DActorPoolImpl(scene);

        Assert.assertEquals(0, pool.getPoolSize());

        final ShapeActor actor1 = pool.checkOut("1", null);
        Assert.assertEquals(0, pool.getPoolSize());
        Assert.assertNotNull(actor1);
        Assert.assertEquals(Color.GREEN, actor1.lookup(ColorSupport.class).getColor());
        Assert.assertTrue(pool.validate(actor1));

        final ShapeActor actor2 = pool.checkOut("2", null);
        Assert.assertEquals(0, pool.getPoolSize());
        Assert.assertNotNull(actor2);
        Assert.assertEquals(Color.GREEN, actor2.lookup(ColorSupport.class).getColor());
        Assert.assertTrue(pool.validate(actor2));
    }

    /**
     * Test of checkIn method, of class AbstractActorPool.
     */
    @Test
    public void testCheckIn() {
        final Shape2DActorPoolImpl pool = new Shape2DActorPoolImpl(scene);

        Assert.assertEquals(0, pool.getPoolSize());

        final ShapeActor actor1 = pool.checkOut("1", null);
        pool.checkIn(actor1);
        try {
            Thread.sleep(100);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertEquals(1, pool.getPoolSize());

        final ShapeActor actor2 = pool.checkOut("2", null);
        Assert.assertTrue(actor1 == actor2);
        Assert.assertEquals(actor1, actor2);
        try {
            Thread.sleep(100);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertEquals(0, pool.getPoolSize());

        actor2.remove(actor2.lookup(TransformSupport.class));
        pool.checkIn(actor2);
        try {
            Thread.sleep(100);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertEquals(1, pool.getPoolSize());

        pool.checkIn(actor1);
        try {
            Thread.sleep(100);
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
            return actor;
        }

        @Override
        protected void initialize(final ShapeActor actor, final Object data) {
            actor.lookup(ColorSupport.class).setColor(Color.GREEN);
        }

        @Override
        protected boolean validate(final ShapeActor actor) {
            if (actor == null) {
                return false;
            }
            if (actor.lookup(ColorSupport.class) == null) {
                return false;
            }
            if (actor.lookup(TransformSupport.class) == null) {
                return false;
            }
            return true;
        }
    }
}
