package gov.pnnl.svf.scene;

import gov.pnnl.svf.OffscreenTestScene;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.geometry.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ScreenshotImplTest {

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;
    private static final OffscreenTestScene scene = new OffscreenTestScene(WIDTH, HEIGHT);
    private File file;
    private File invalid;

    @Before
    public void setUp() throws Exception {
        file = File.createTempFile(UUID.randomUUID().toString(), ".png");
        invalid = File.createTempFile(UUID.randomUUID().toString(), ".invalid");
        scene.setUp();
        // create a purple rectangle that fills the screen
        final ShapeActor actor = new ShapeActor(scene);
        actor.setDrawingPass(DrawingPass.INTERFACE);
        actor.setColor(Color.INDIGO);
        actor.setOrigin(Alignment.LEFT_BOTTOM);
        actor.setShape(new Rectangle2D(WIDTH, HEIGHT));
        // add rectangle to the scene
        scene.add(actor);
        // pause to wait for scene to fully render
        Thread.sleep(100L);
    }

    @After
    public void tearDown() throws Exception {
        scene.tearDown();
        if (file != null) {
            file.delete();
        }
        if (invalid != null) {
            invalid.delete();
        }
    }

    /**
     * Test of dispatch method, of class ScreenshotImpl.
     */
    @Test
    public void testDispatch() {
        final ScreenshotImpl instance = new ScreenshotImpl();
        // nothing waiting
        instance.dispatch(null);
        // queue capture
        instance.capture(file);
        try {
            instance.dispatch(null);
            Assert.fail();
        } catch (final RuntimeException ex) {
            Assert.assertNotNull(ex);
        }
    }

    /**
     * Test of capture method, of class ScreenshotImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCapture_File() throws Exception {
        scene.getScreenshot().capture(file);
        // we have to wait until the file is there since we aren't using a callback
        final long start = System.currentTimeMillis();
        while (file.length() <= 0L && System.currentTimeMillis() <= start + 5000L) {
            try {
                Thread.sleep(1000L);
            } catch (final InterruptedException ex) {
                // ignore exception
            }
        }
        Assert.assertTrue(file.length() > 0L);
        checkScreenshot(file);
    }

    /**
     * Test of capture method, of class ScreenshotImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCapture_File_ScreenshotListener() throws Exception {
        final AtomicReference<File> value = new AtomicReference<>();
        final AtomicReference<Exception> fault = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        scene.getScreenshot().capture(file, new ScreenshotListener() {

                                  @Override
                                  public void succeeded(final File file) {
                                      value.set(file);
                                      latch.countDown();
                                  }

                                  @Override
                                  public void failed(final File file, final Exception ex) {
                                      value.set(file);
                                      fault.set(ex);
                                      latch.countDown();
                                  }
                              });
        // we have to wait until the file is there since we aren't using a callback
        final long start = System.currentTimeMillis();
        while (latch.getCount() > 0 && System.currentTimeMillis() <= start + 5000L) {
            try {
                Thread.sleep(1000L);
            } catch (final InterruptedException ex) {
                // ignore exception
            }
        }
        Assert.assertTrue(file.length() > 0L);
        Assert.assertEquals(file, value.get());
        Assert.assertNull(fault.get());
        checkScreenshot(file);
    }

    /**
     * Test of capture method, of class ScreenshotImpl.
     */
    @Test
    public void testCapture_File_ScreenshotListener_fault() {
        final AtomicReference<File> value = new AtomicReference<>();
        final AtomicReference<Exception> fault = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        scene.getScreenshot().capture(invalid, new ScreenshotListener() {

                                  @Override
                                  public void succeeded(final File file) {
                                      value.set(file);
                                      latch.countDown();
                                  }

                                  @Override
                                  public void failed(final File file, final Exception ex) {
                                      value.set(file);
                                      fault.set(ex);
                                      latch.countDown();
                                  }
                              });
        // we have to wait until the file is there since we aren't using a callback
        final long start = System.currentTimeMillis();
        while (latch.getCount() > 0 && System.currentTimeMillis() <= start + 5000L) {
            try {
                Thread.sleep(1000L);
            } catch (final InterruptedException ex) {
                // ignore exception
            }
        }
        Assert.assertTrue(invalid.length() == 0L);
        Assert.assertEquals(invalid, value.get());
        Assert.assertNotNull(fault.get());
    }

    private void checkScreenshot(final File file) throws IOException {
        final BufferedImage image = ImageIO.read(file);
        final float[] pixel = image.getData().getPixel(WIDTH / 2, HEIGHT / 2, new float[3]);
        for (int i = 0; i < pixel.length; i++) {
            pixel[i] /= 255.0f;
        }
        Assert.assertArrayEquals(Color.INDIGO.toRgbArray(), pixel, 0.1f);
    }

}
