package gov.pnnl.svf.picking;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.core.collections.KeyValuePair;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.SceneExt;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Set;
import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ColorUtilsTest {

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
     * Test of newMapping method, of class ColorPickingUtils.
     */
    @SuppressWarnings("unused")
    @Test
    public void testNewMapping() {
        final ColorPickingUtils utils = scene.getColorPickingUtils();
        final int start = utils.getMappingRemaining();
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(Rectangle2D.ONE);
        final ColorPickingSupport support = ColorPickingSupport.newInstance(actor);
        scene.add(actor);
        Assert.assertEquals(start - 1, utils.getMappingRemaining());
    }

    /**
     * Test of clearMapping method, of class ColorPickingUtils.
     */
    @SuppressWarnings("unused")
    @Test
    public void testClearMapping() {
        final ColorPickingUtils utils = scene.getColorPickingUtils();
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(Rectangle2D.ONE);
        final ColorPickingSupport support = ColorPickingSupport.newInstance(actor);
        scene.add(actor);
        final int start = utils.getMappingRemaining();
        actor.dispose();
        Assert.assertEquals(start + 1, utils.getMappingRemaining());
    }

    /**
     * Test of getMapping method, of class ColorPickingUtils.
     */
    @Test
    public void testGetMapping() {
        final ColorPickingUtils utils = scene.getColorPickingUtils();
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(Rectangle2D.ONE);
        final ColorPickingSupport support = ColorPickingSupport.newInstance(actor);
        scene.add(actor);
        Assert.assertEquals(new KeyValuePair<ColorPickingSupport, Object>(support, actor), utils.getMapping(support.getMapping(actor)));
    }

    @SuppressWarnings("unused")
    @Test
    public void testNextColor() {
        final ColorPickingUtils utils = scene.getColorPickingUtils();
        while (utils.getMappingSize() < 100000) {
            final ShapeActor actor = new ShapeActor(scene);
            actor.setShape(Rectangle2D.ONE);
            ColorPickingSupport.newInstance(actor);
            scene.add(actor);
        }
        // dispose actors
        final Set<Actor> actors = scene.lookupAll(Actor.class);
        for (final Actor actor : actors) {
            scene.remove(actor);
            actor.dispose();
        }
        Assert.assertEquals(0, utils.getMappingSize());
    }
}
