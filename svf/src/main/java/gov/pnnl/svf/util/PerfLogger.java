package gov.pnnl.svf.util;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.DrawableItem;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneExt;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will log the average number of rendered vertices per second at the
 * specified interval.
 *
 * @author Arthur Bleeker
 */
public class PerfLogger extends AbstractLogger implements DrawableItem {

    private static final Logger logger = Logger.getLogger(PerfLogger.class.toString());
    private final SceneExt scene;
    private long maxDrawLength = 0L;
    private long maxUpdateLength = 0L;
    private long maxVertices = 0L;
    private long maxCulledActors = 0L;
    private int maxUnclearedAttribs = 0;
    private int maxUnclearedModelview = 0;
    private int maxUnclearedProjection = 0;
    private int maxUnclearedTexture = 0;
    private int maxErrorsReported = 0;
    private int vboBufferCount = 0;
    private int displayListCount = 0;
    private int vps = 0;

    private PerfLogger(final Scene scene, final long interval) {
        super(scene, 0, interval);
        this.scene = scene.getExtended();
        text = "Average Vertices Per Second: {0} "
               + "\nMax Vertices Per Frame: {1} "
               + "\nMax Draw Length: {2} ms"
               + "\nMax Update Length: {3} ms"
               + "\nMax Actors Culled Per Frame: {4}"
               + "\nMax Uncleared Attribs Per Frame: {5}"
               + "\nMax Uncleared Modelview Matrices Per Frame: {6}"
               + "\nMax Uncleared Projection Matrices Per Frame: {7}"
               + "\nMax Uncleared Texture Matrices Per Frame: {8}"
               + "\nMax Errors Reported Per Frame: {9}"
               + "\nVBO Buffers Currently In Use: {10}"
               + "\nDisplay Lists Currently In Use: {11}";
    }

    /**
     * Creates a new instance of this class and registers it with the scene.
     *
     * @param scene    The scene to log the average vertices per second for.
     * @param interval The interval in milliseconds to log at
     *
     * @return the new instance
     */
    public static PerfLogger newInstance(final Scene scene, final long interval) {
        final PerfLogger instance = new PerfLogger(scene, interval);
        scene.add(instance);
        return instance;
    }

    @Override
    public DrawingPass getDrawingPass() {
        return DrawingPass.ALL;
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        synchronized (this) {
            final long vertices = scene.getSceneMetrics().getLastVerticesRendered();
            counter += vertices;
            maxDrawLength = Math.max(maxDrawLength, scene.getSceneMetrics().getLastDrawLength());
            maxUpdateLength = Math.max(maxUpdateLength, scene.getSceneMetrics().getLastUpdateLength());
            maxVertices = Math.max(maxVertices, vertices);
            maxCulledActors = Math.max(maxCulledActors, scene.getSceneMetrics().getLastCulledActors());
            maxUnclearedAttribs = Math.max(maxUnclearedAttribs, scene.getSceneMetrics().getLastAttribStackDepth());
            maxUnclearedModelview = Math.max(maxUnclearedModelview, scene.getSceneMetrics().getLastModelviewStackDepth());
            maxUnclearedProjection = Math.max(maxUnclearedProjection, scene.getSceneMetrics().getLastProjectionStackDepth());
            maxUnclearedTexture = Math.max(maxUnclearedTexture, scene.getSceneMetrics().getLastTextureStackDepth());
            maxErrorsReported = Math.max(maxErrorsReported, scene.getSceneMetrics().getLastErrorsReported());
            vboBufferCount = scene.getSceneMetrics().getVboBufferCount();
            displayListCount = scene.getSceneMetrics().getDisplayListCount();
        }
    }

    /**
     * The text uses MessageFormat and will insert the following metrics:
     * <p/>
     * Average Vertices Per Second: {0}
     * <p/>
     * Max Vertices Per Frame: {1}
     * <p/>
     * Max Draw Length: {2}
     * <p/>
     * Max Update Length: {3}
     * <p/>
     * Max Actors Culled Per Frame: {4}
     * <p/>
     * Max Uncleared Attribs Per Frame: {5}
     * <p/>
     * Max Uncleared Modelview Matrices Per Frame: {6}
     * <p/>
     * Max Uncleared Projection Matrices Per Frame: {7}
     * <p/>
     * Max Uncleared Texture Matrices Per Frame: {8}
     * <p/>
     * Max Errors Reported Per Frame: {9}
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
                vps = (int) (counter / (elapsed / 1000.0));
                output = MessageFormat.format(
                        text,
                        vps,
                        maxVertices,
                        maxDrawLength,
                        maxUpdateLength,
                        maxCulledActors,
                        maxUnclearedAttribs,
                        maxUnclearedModelview,
                        maxUnclearedProjection,
                        maxUnclearedTexture,
                        maxErrorsReported,
                        vboBufferCount,
                        displayListCount);
                logger.log(Level.INFO, output);
                logger.log(Level.INFO, scene.getSceneMetrics().getCollectionsInfo());
                // reset counts
                counter = 0L;
                elapsed = 0L;
                maxVertices = 0L;
                maxDrawLength = 0L;
                maxUpdateLength = 0L;
                maxCulledActors = 0L;
                maxUnclearedAttribs = 0;
                maxUnclearedModelview = 0;
                maxUnclearedProjection = 0;
                maxUnclearedTexture = 0;
                maxErrorsReported = 0;
            }
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "PerfLogger{" + "interval=" + interval + '}';
        }
    }
}
