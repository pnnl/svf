package gov.pnnl.svf.util;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.Scene;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * This class will log the average frame per second (FPS) at the specified
 * interval.
 *
 * @author Arthur Bleeker
 */
public class FpsLogger extends AbstractLogger {

    private static final Logger logger = Logger.getLogger(FpsLogger.class.toString());
    private int fps = 0;

    private FpsLogger(final Scene scene, final long interval) {
        super(scene, 0, interval);
        text = "Average FPS: {0}";
    }

    /**
     * Creates a new instance of this class and registers it with the scene.
     *
     * @param scene    The scene to log the FPS for.
     * @param interval The interval in milliseconds to log at
     *
     * @return the new instance
     */
    public static FpsLogger newInstance(final Scene scene, final long interval) {
        final FpsLogger instance = new FpsLogger(scene, interval);
        scene.add(instance);
        return instance;
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (camera == null) {
            synchronized (this) {
                counter++;
            }
        }
    }

    /**
     * @return the average FPS during the last period
     */
    public int getAverageFps() {
        synchronized (this) {
            return fps;
        }
    }

    /**
     * The text uses MessageFormat and will insert the following metrics:
     * <p/>
     * Average Frames Per Second: {0}
     *
     * @param text The text to show in the logging message.
     */
    @Override
    public void setText(final String text) {
        super.setText(text);
    }

    @Override
    public void update(final long delta) {
        synchronized (this) {
            elapsed += delta;
            // only update the text every second or more
            if (elapsed >= interval) {
                // log the fps
                fps = (int) (counter / (elapsed / 1000.0));
                output = MessageFormat.format(text, String.format("%04d", fps));
                logger.log(Level.INFO, output);
                counter = 0L;
                elapsed = 0L;
            }
        }
    }

    @Override
    public String toString() {
        return "FpsLogger{" + "interval=" + interval + '}';
    }
}
