package gov.pnnl.svf.util;

import com.jogamp.opengl.JoglVersion;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLException;

/**
 * Utility class for working with the GLScene.
 *
 * @author Arthur Bleeker
 *
 */
public class DebugUtil {

    private static final Logger logger = Logger.getLogger(DebugUtil.class.toString());

    /**
     * Constructor private to prevent instantiation of static helper class.
     */
    private DebugUtil() {
        super();
    }

    /**
     * Check the GL error stack for the top most error and print it.
     *
     * @param gl Reference to the current GL.
     */
    public static void checkGLError(final GL gl) {
        final int error = gl.glGetError();
        if (error != GL.GL_NO_ERROR) {
            final String message = gl.glGetString(error);
            logger.log(Level.WARNING, String.format("GL error code: %s message: %s", DebugUtil.getTextForError(error), message));
        }
    }

    /**
     * Check the GL error stack for errors and print them.
     *
     * @param gl Reference to the current GL.
     *
     * @return the error count
     */
    public static int checkGLErrors(final GL gl) {
        int count = 0;
        int error = gl.glGetError();
        while (error != GL.GL_NO_ERROR) {
            final String message = gl.glGetString(error);
            logger.log(Level.WARNING, String.format("GL error code: %s message: %s", DebugUtil.getTextForError(error), message));
            error = gl.glGetError();
            count++;
        }
        return count;
    }

    /**
     * Check the GL error stack for the top most error and print it.
     *
     * @param gl   Reference to the current GL.
     * @param call Extra text (the call and params) to print in the log message.
     */
    public static void checkGLError(final GL gl, final String call) {
        final int error = gl.glGetError();
        if (error != GL.GL_NO_ERROR) {
            final String message = gl.glGetString(error);
            logger.log(Level.WARNING, String.format("GL error code: %s message: %s call: %s", DebugUtil.getTextForError(error), message, call));
        }
    }

    /**
     * Check the GL error stack for errors and print them.
     *
     * @param gl   Reference to the current GL.
     * @param call Extra text (the call and params) to print in the log message.
     *
     * @return the error count
     */
    public static int checkGLErrors(final GL gl, final String call) {
        int count = 0;
        int error = gl.glGetError();
        while (error != GL.GL_NO_ERROR) {
            final String message = gl.glGetString(error);
            logger.log(Level.WARNING, String.format("GL error code: %s message: %s call: %s", DebugUtil.getTextForError(error), message, call));
            error = gl.glGetError();
            count++;
        }
        return count;
    }

    /**
     * Clears all of the errors in the GL error stack.
     *
     * @param gl Reference to the current GL.
     *
     * @return the error count
     */
    public static int clearGLErrors(final GL gl) {
        int count = 0;
        int error = gl.glGetError();
        while (error != GL.GL_NO_ERROR) {
            error = gl.glGetError();
            count++;
        }
        return count;
    }

    /**
     * Logs the current state of GL to a logger.
     *
     * @param gl reference to the current GL
     */
    public static void logGLInfo(final GL2 gl) {
        final char[] chars = new char[101];
        Arrays.fill(chars, '-');
        final String div = String.valueOf(chars);
        final StringBuilder sb = new StringBuilder();
        sb.append("The OpenGL renderer is being reset...\n");
        sb.append(div);
        sb.append("\nSystem Info: \n");
        sb.append(div);
        sb.append("\n Operating System Name: ");
        sb.append(System.getProperty("os.name", "Not Available"));
        sb.append("\n Operating System Architecture: ");
        sb.append(System.getProperty("os.arch", "Not Available"));
        sb.append("\n Operating System Version: ");
        sb.append(System.getProperty("os.version", "Not Available"));
        sb.append("\n OpenGL Vendor: ");
        try {
            sb.append(gl.glGetString(GL.GL_VENDOR));
        } catch (final GLException ex) {
            sb.append("Not Available");
        }
        sb.append("\n OpenGL Renderer: ");
        try {
            sb.append(gl.glGetString(GL.GL_RENDERER));
        } catch (final GLException ex) {
            sb.append("Not Available");
        }
        sb.append("\n OpenGL Version: ");
        try {
            sb.append(gl.glGetString(GL.GL_VERSION));
        } catch (final GLException ex) {
            sb.append("Not Available");
        }
        sb.append("\n OpenGL SL Version: ");
        try {
            sb.append(gl.glGetString(GL2ES2.GL_SHADING_LANGUAGE_VERSION));
        } catch (final GLException ex) {
            sb.append("Not Available");
        }
        sb.append("\n");
        sb.append(div);
        sb.append("\nJOGL Info: \n");
        sb.append(div);
        sb.append("\n");
        JoglVersion.getInstance().getFullManifestInfo(sb);
        sb.append(div);
        sb.append("\nOpenGL Info: \n");
        JoglVersion.getGLInfo(gl, sb);
        logger.log(Level.INFO, sb.toString());
    }

    private static String getTextForError(final int error) {
        switch (error) {
            case GL.GL_NO_ERROR:
                return "GL_NO_ERROR";
            case GL.GL_INVALID_ENUM:
                return "GL_INVALID_ENUM";
            case GL.GL_INVALID_VALUE:
                return "GL_INVALID_VALUE";
            case GL.GL_INVALID_OPERATION:
                return "GL_INVALID_OPERATION";
            case javax.media.opengl.GL2ES1.GL_STACK_OVERFLOW:
                //            case javax.media.opengl.GL2ES2.GL_STACK_OVERFLOW:
                return "GL_STACK_OVERFLOW";
            case javax.media.opengl.GL2ES1.GL_STACK_UNDERFLOW:
                //            case javax.media.opengl.GL2ES2.GL_STACK_UNDERFLOW:
                return "GL_STACK_UNDERFLOW";
            case GL.GL_OUT_OF_MEMORY:
                return "GL_OUT_OF_MEMORY";
            case GL2.GL_TABLE_TOO_LARGE:
                return "GL_TABLE_TOO_LARGE";
            default:
                return "UNNKOWN_[" + error + "]";
        }
    }
}
