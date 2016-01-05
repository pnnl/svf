package gov.pnnl.svf.util;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.DrawableItem;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import java.nio.IntBuffer;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import jogamp.opengl.windows.wgl.WGLExt;

/**
 * This class will log the memory usage for GPUs at the specified interval.
 *
 * @author Arthur Bleeker
 */
public class MemLogger extends AbstractLogger implements DrawableItem {

    private static final Logger logger = Logger.getLogger(MemLogger.class.toString());

    private enum Vendor {

        NONE,
        NVIDIA,
        AMD,
        ERROR;
    }
    /**
     * GL extension to get total available memory for NVIDIA GPUs.
     */
    public static final int GL_TOTAL_AVAILABLE_MEM = 0x9048;
    /**
     * GL extension to get current available memory for NVIDIA GPUs.
     */
    public static final int GL_CURRENT_AVAILABLE_MEM = 0x9049;
    private static final String NO_VALUE_MESSAGE = "no value";
    private static final int NO_VALUE = -1;
    private static final String ERROR_VALUE_MESSAGE = "error";
    private static final int ERROR_VALUE = -2;
    private int totalAvailableMem = NO_VALUE;
    private int currentAvailableMem = NO_VALUE;
    private Vendor vendor = Vendor.NONE;

    private MemLogger(final Scene scene, final long interval) {
        super(scene, 0, interval);
        text = "Total Available GPU Memory ({2}): {0} "
               + "\nCurrent Available GPU Memory ({2}): {1} ";
    }

    /**
     * Creates a new instance of this class and registers it with the scene.
     *
     * @param scene    The scene to log to.
     * @param interval The interval in milliseconds to log at
     *
     * @return the new instance
     */
    public static MemLogger newInstance(final Scene scene, final long interval) {
        final MemLogger instance = new MemLogger(scene, interval);
        scene.add(instance);
        return instance;
    }

    @Override
    public DrawingPass getDrawingPass() {
        return DrawingPass.ALL;
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (vendor == Vendor.ERROR) {
            return;
        }
        final int[] values = new int[]{NO_VALUE, NO_VALUE};
        // try nvidia first
        if (vendor == Vendor.NVIDIA || vendor == Vendor.NONE) {
            try {
                gl.glGetIntegerv(GL_TOTAL_AVAILABLE_MEM, values, 0);
                gl.glGetIntegerv(GL_CURRENT_AVAILABLE_MEM, values, 1);
                if (values[1] >= 0) {
                    vendor = Vendor.NVIDIA;
                }
            } catch (final GLException ex) {
                // failed to get values
                DebugUtil.clearGLErrors(gl);
                // error will be logged when values are not assigned
                values[0] = ERROR_VALUE;
                values[1] = ERROR_VALUE;
            }
        }
        // try amd/ati
        if (vendor == Vendor.AMD || vendor == Vendor.NONE) {
            try {
                if (vendor == Vendor.NONE) {
                    if (gl instanceof WGLExt) {
                        final WGLExt wgl = (WGLExt) gl;
                        final int gpuCount = wgl.wglGetGPUIDsAMD(0, null);
                        final int[] gpuIds = new int[gpuCount];
                        final IntBuffer totalMem = IntBuffer.allocate(1);
                        for (int i = 0; i < gpuIds.length; i++) {
                            wgl.wglGetGPUInfoAMD(gpuIds[i], WGLExt.WGL_GPU_RAM_AMD, GL2.GL_UNSIGNED_INT, 1, totalMem);
                            values[0] = Math.max(values[0], totalMem.get(0));
                        }
                    }
                } else {
                    synchronized (this) {
                        values[0] = totalAvailableMem;
                    }
                }
                gl.glGetIntegerv(GL2.GL_TEXTURE_FREE_MEMORY_ATI, values, 1);
                if (values[1] >= 0) {
                    vendor = Vendor.AMD;
                }
            } catch (final GLException ex) {
                // failed to get values
                DebugUtil.clearGLErrors(gl);
                // error will be logged when values are not assigned
                values[0] = ERROR_VALUE;
                values[1] = ERROR_VALUE;
            }
        }
        // catch errors
        if (vendor == Vendor.NONE && values[0] < 0 || values[1] < 0) {
            vendor = Vendor.ERROR;
        }
        synchronized (this) {
            totalAvailableMem = values[0];
            currentAvailableMem = values[1];
        }
    }

    /**
     * The text uses MessageFormat and will insert the following metrics:
     * <p/>
     * Total Available GPU Memory: {0}
     * <p/>
     * Current Available GPU Memory: {1}
     * <p/>
     * Vendor name: {2}
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
                output = MessageFormat.format(text, getMessage(totalAvailableMem), getMessage(currentAvailableMem), getVendor(vendor));
                logger.log(Level.INFO, output);
                // reset counts
                elapsed = 0L;
                totalAvailableMem = NO_VALUE;
                currentAvailableMem = NO_VALUE;
            }
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "MemLogger{" + "interval=" + interval + '}';
        }
    }

    /**
     * Log the message once.
     *
     * @param scene reference to the current scene
     * @param gl    reference to the current gl
     */
    public static void logMessage(final Scene scene, final GL2 gl) {
        final MemLogger instance = new MemLogger(scene, 0L);
        instance.draw(gl, null, null);
        instance.update(0L);
    }

    private static String getMessage(final int value) {
        switch (value) {
            case NO_VALUE:
                return NO_VALUE_MESSAGE;
            case ERROR_VALUE:
                return ERROR_VALUE_MESSAGE;
            default:
                return MessageFormat.format("{0} KB", value);
        }
    }

    private static String getVendor(final Vendor vendor) {
        switch (vendor) {
            case NONE:
            case ERROR:
                return "unknown";
            case AMD:
                return "AMD";
            case NVIDIA:
                return "Nvidia";
            default:
                throw new IllegalArgumentException("Unhandled enumeration passed to switch statement: " + vendor);
        }
    }
}
