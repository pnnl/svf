package gov.pnnl.svf.awt.scene;

import com.jogamp.opengl.awt.GLCanvas;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.awt.AwtTestScene;
import gov.pnnl.svf.geometry.Rectangle2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class AwtAbstractSceneTest {

    private static final AwtTestScene scene = new AwtTestScene();

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
     * Test of getGLUT method, of class AwtAbstractScene.
     */
    @Test
    public void testGetGLUT() {
        Assert.assertNotNull(scene.getGLUT());
    }

    //    @Test
    //    public void testUnloadTexture() {
    //        final Actor actor = new PlaneActor(scene);
    //        scene.add(actor);
    //        final ByteBuffer data = ByteBuffer.allocateDirect(16 * 16 * 4);
    //        for (int x = 0; x < 16; x++) {
    //            for (int y = 0; y < 16; y++) {
    //                data.put((byte) (x * 255.0 / 16));
    //                data.put((byte) 0);
    //                data.put((byte) (y * 255.0 / 16));
    //                data.put((byte) 28);
    //            }
    //        }
    //        final TextureSupport texture = Texture2dSupport.newInstance(actor, TextureType.RGBA, data, 16, 16);
    //        Assert.assertEquals(true, actor.isVisible());
    //        int tries = 10;
    //        while (!texture.isInitialized() && tries > 0) {
    //            try {
    //                Thread.sleep(1000);
    //            } catch (InterruptedException ex) {
    //                // ignore
    //            }
    //            tries--;
    //        }
    //        Assert.assertEquals(true, texture.isInitialized());
    //        actor.setVisible(false);
    //        tries = 10;
    //        while (texture.isInitialized() && tries > 0) {
    //            try {
    //                Thread.sleep(1000);
    //            } catch (InterruptedException ex) {
    //                // ignore
    //            }
    //            tries--;
    //        }
    //        Assert.assertEquals(false, texture.isInitialized());
    //    }
    /**
     * Test of getActor method, of class AwtAbstractScene.
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
     * Test of getActor method, of class AwtAbstractScene.
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

    /**
     * Test of add method, of class AwtAbstractScene.
     *
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    @Test
    public void testAdd() throws InterruptedException, InvocationTargetException {
        final GLCanvas panel = (GLCanvas) scene.getComponent();
        // create listeners
        final KeyListener listener0 = new KeyAdapter() {
        };

        final MouseListener listener1 = new MouseAdapter() {
        };
        final MouseMotionListener listener2 = new MouseMotionAdapter() {
        };
        final MouseWheelListener listener3 = new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent event) {
                // no operation
            }
        };
        final ComponentListener listener4 = new ComponentAdapter() {
        };
        final ContainerListener listener5 = new ContainerAdapter() {
        };
        final FocusListener listener6 = new FocusAdapter() {
        };
        // add all the listeners on the event thread
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                scene.add(listener0);
                scene.add(listener1);
                scene.add(listener2);
                scene.add(listener3);
                scene.add(listener4);
                scene.add(listener5);
                scene.add(listener6);
            }
        });
        // ensure they are all added to the panel
        Assert.assertTrue(containsValue(panel.getKeyListeners(), listener0));
        Assert.assertTrue(containsValue(panel.getMouseListeners(), listener1));
        Assert.assertTrue(containsValue(panel.getMouseMotionListeners(), listener2));
        Assert.assertTrue(containsValue(panel.getMouseWheelListeners(), listener3));
        Assert.assertTrue(containsValue(panel.getComponentListeners(), listener4));
        //        Assert.assertTrue(containsValue(panel.getContainerListeners(), listener5));
        Assert.assertTrue(containsValue(panel.getFocusListeners(), listener6));
    }

    /**
     * Test of remove method, of class AwtAbstractScene.
     *
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    @Test
    public void testRemove() throws InterruptedException, InvocationTargetException {
        final GLCanvas panel = (GLCanvas) scene.getComponent();
        // create listeners
        final KeyListener listener0 = new KeyAdapter() {
        };

        final MouseListener listener1 = new MouseAdapter() {
        };
        final MouseMotionListener listener2 = new MouseMotionAdapter() {
        };
        final MouseWheelListener listener3 = new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent event) {
                // no operation
            }
        };
        final ComponentListener listener4 = new ComponentAdapter() {
        };
        final ContainerListener listener5 = new ContainerAdapter() {
        };
        final FocusListener listener6 = new FocusAdapter() {
        };
        // add all the listeners on the event thread
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                scene.add(listener0);
                scene.add(listener1);
                scene.add(listener2);
                scene.add(listener3);
                scene.add(listener4);
                scene.add(listener5);
                scene.add(listener6);
            }
        });
        // ensure they are all added to the panel
        Assert.assertTrue(containsValue(panel.getKeyListeners(), listener0));
        Assert.assertTrue(containsValue(panel.getMouseListeners(), listener1));
        Assert.assertTrue(containsValue(panel.getMouseMotionListeners(), listener2));
        Assert.assertTrue(containsValue(panel.getMouseWheelListeners(), listener3));
        Assert.assertTrue(containsValue(panel.getComponentListeners(), listener4));
        //        Assert.assertTrue(containsValue(panel.getContainerListeners(), listener5));
        Assert.assertTrue(containsValue(panel.getFocusListeners(), listener6));
        // remove all the listeners on the event thread
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                scene.remove(listener0);
                scene.remove(listener1);
                scene.remove(listener2);
                scene.remove(listener3);
                scene.remove(listener4);
                scene.remove(listener5);
                scene.remove(listener6);
            }
        });
        // ensure they are all removed from the panel
        Assert.assertFalse(containsValue(panel.getKeyListeners(), listener0));
        Assert.assertFalse(containsValue(panel.getMouseListeners(), listener1));
        Assert.assertFalse(containsValue(panel.getMouseMotionListeners(), listener2));
        Assert.assertFalse(containsValue(panel.getMouseWheelListeners(), listener3));
        Assert.assertFalse(containsValue(panel.getComponentListeners(), listener4));
        //        Assert.assertFalse(containsValue(panel.getContainerListeners(), listener5));
        Assert.assertFalse(containsValue(panel.getFocusListeners(), listener6));
    }

    private <T> boolean containsValue(final T[] array, final T value) {
        for (final T object : array) {
            if (object == value) {
                return true;
            }
        }
        return false;
    }
}
