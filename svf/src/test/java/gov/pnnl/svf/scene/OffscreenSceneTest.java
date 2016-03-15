package gov.pnnl.svf.scene;

import gov.pnnl.svf.OffscreenTestScene;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.geometry.Rectangle2D;
import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class OffscreenSceneTest {

    private static final OffscreenTestScene scene = new OffscreenTestScene();

    /**
     * Set up for testing
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        scene.setUp();
    }

    /**
     * Clean up after testing
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDown() throws Exception {
        scene.tearDown();
    }

    /**
     * Test of getGLUT method, of class OffscreenScene.
     */
    @Test
    public void testGetGLUT() {
        Assert.assertNotNull(scene.getGLUT());
    }

    /**
     * Test of getActor method, of class OffscreenScene.
     */
    @Test
    public void testGetActorById() {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(new Rectangle2D(1.0, 1.0));
        Assert.assertNull(scene.getActor(actor.getId()));
        scene.add(actor);
        Assert.assertNotNull(scene.getActor(actor.getId()));
        Assert.assertEquals(actor, scene.getActor(actor.getId()));
        scene.remove(actor);
        Assert.assertNull(scene.getActor(actor.getId()));
    }

    /**
     * Test of getActor method, of class OffscreenScene.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetActorByIdDuplicate1() {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(new Rectangle2D(1.0, 1.0));
        final ShapeActor duplicate = new ShapeActor(scene, actor.getId());
        duplicate.setShape(new Rectangle2D(1.0, 1.0));
        Assert.assertNull(scene.getActor(actor.getId()));
        scene.add(actor);
        Assert.assertNotNull(scene.getActor(actor.getId()));
        Assert.assertEquals(actor, scene.getActor(actor.getId()));
        Assert.assertTrue(actor == scene.getActor(actor.getId()));
        scene.add(duplicate);
        Assert.assertTrue(duplicate == scene.getActor(actor.getId()));
    }
}
