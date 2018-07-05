package gov.pnnl.svf.support;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Initializable;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * You must override this class in order to provide functionality for setting
 * the uniform fields.
 *
 * @author Amelia Bleeker
 */
public abstract class AbstractShaderSupport extends AbstractSupport<Object> implements Drawable, Initializable {

    /**
     * Maximum amount of characters that can be contained in the info log.
     */
    public static final int MAX_INFO_LOG_LENGTH = 1024;
    private static final Logger logger = Logger.getLogger(AbstractShaderSupport.class.toString());
    private final String vertex;
    private final String fragment;
    private int vertexProgram;
    private int fragmentProgram;
    private int shaderProgram;

    /**
     * Constructor protected to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor    The actor that owns this support object.
     * @param vertex   resource location
     * @param fragment resource location
     *
     * @throws NullPointerException if the actor is null
     */
    protected AbstractShaderSupport(final Actor actor, final String vertex, final String fragment) {
        super(actor);
        this.vertex = vertex;
        this.fragment = fragment;
    }

    @Override
    public Scene getScene() {
        return getActor().getScene();
    }

    @Override
    public boolean isVisible() {
        return getActor().isVisible();
    }

    @Override
    public DrawingPass getDrawingPass() {
        return getActor().getDrawingPass();
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_TEXTURE_BIT);
        drawState.setAttrib();
        // start the shader
        if (isInitialized()) {
            synchronized (this) {
                gl.glUseProgram(shaderProgram);
            }
        }
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // stop the shader
        if (isInitialized()) {
            gl.glUseProgram(0);
        }
        if (drawState.isAttrib()) {
            gl.glPopAttrib();
        }
        drawState.clearValues();
    }

    /**
     *
     * @return the location of the fragment program
     */
    protected int getFragmentProgram() {
        synchronized (this) {
            return fragmentProgram;
        }
    }

    /**
     *
     * @return the location of the shader program
     */
    protected int getShaderProgram() {
        synchronized (this) {
            return shaderProgram;
        }
    }

    /**
     *
     * @return the location of the vertex program
     */
    protected int getVertexProgram() {
        synchronized (this) {
            return vertexProgram;
        }
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            vertexProgram = loadShader(gl, vertex, GL2ES2.GL_VERTEX_SHADER);
            fragmentProgram = loadShader(gl, fragment, GL2ES2.GL_FRAGMENT_SHADER);
            shaderProgram = gl.glCreateProgram();
            if (shaderProgram > 0) {
                if (vertexProgram > 0) {
                    gl.glAttachShader(shaderProgram, vertexProgram);
                    if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                        logger.log(Level.INFO, "{0}: Custom vertex shader program enabled.", getScene());
                    }
                } else if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                    logger.log(Level.INFO, "{0}: Fixed function vertex shader program enabled.", getScene());
                }
                if (fragmentProgram > 0) {
                    gl.glAttachShader(shaderProgram, fragmentProgram);
                    if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                        logger.log(Level.INFO, "{0}: Custom fragment shader program enabled.", getScene());
                    }
                } else if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                    logger.log(Level.INFO, "{0}: Fixed function fragment shader program enabled.", getScene());
                }
                gl.glLinkProgram(shaderProgram);
                gl.glValidateProgram(shaderProgram);
                final String log = readProgramInfoLog(gl, shaderProgram);
                if (!log.isEmpty() && getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                    logger.log(Level.INFO, String.format("Shader Program Info Log: %n%s", log));
                }
            } else {
                logger.log(Level.WARNING, "{0}: Shader Program is not valid.", getScene());
            }
        }
    }

    @Override
    public boolean isInitialized() {
        synchronized (this) {
            return shaderProgram > 0;
        }
    }

    @Override
    public boolean isSlow() {
        return false;
    }

    private int loadShader(final GL2 gl, final String shader, final int type) {
        if (shader == null) {
            return 0;
        }
        final String source = readShader(shader);
        final int program = gl.glCreateShader(type);
        gl.glShaderSource(program, 1, new String[]{source}, null, 0);
        gl.glCompileShader(program);
        // check the compilation
        final IntBuffer buffer = IntBuffer.allocate(1);
        gl.glGetShaderiv(program, GL2ES2.GL_COMPILE_STATUS, buffer);
        final String log = readShaderInfoLog(gl, program);
        if (!log.isEmpty()) {
            logger.log(Level.INFO, String.format("%s Shader Message Info: %n%s", type == GL2ES2.GL_VERTEX_SHADER ? "Vertex" : "Fragment", log));
        }
        if (buffer.get() == GL.GL_FALSE) {
            logger.log(Level.WARNING, String.format("Compilation of the GLSL %s shader failed.", type == GL2ES2.GL_VERTEX_SHADER ? "vertex" : "fragment"));
            return 0;
        }
        return program;
    }

    private String readProgramInfoLog(final GL2 gl, final int program) {
        final IntBuffer intBuffer = IntBuffer.allocate(1);
        final ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_INFO_LOG_LENGTH);
        gl.glGetProgramInfoLog(program, MAX_INFO_LOG_LENGTH, intBuffer, byteBuffer);
        final StringBuffer message = new StringBuffer();
        while (byteBuffer.hasRemaining()) {
            message.append((char) byteBuffer.get());
        }
        return message.toString().trim();
    }

    private String readShader(final String shader) {
        // read the shader code to a string
        InputStream in = null;
        try {
            // we want to be able to access the resource from where the extended class resides
            in = this.getClass().getResourceAsStream(shader);
            return StringUtil.streamToString(in);
        } catch (final IOException ex) {
            logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to read the shader file.", getScene()), ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException ex) {
                    logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to close the shader file reader.", getScene()), ex);
                }
            }
        }
        return null;
    }

    private String readShaderInfoLog(final GL2 gl, final int shader) {
        final IntBuffer intBuffer = IntBuffer.allocate(1);
        final ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_INFO_LOG_LENGTH);
        gl.glGetShaderInfoLog(shader, MAX_INFO_LOG_LENGTH, intBuffer, byteBuffer);
        final StringBuffer message = new StringBuffer();
        while (byteBuffer.hasRemaining()) {
            message.append((char) byteBuffer.get());
        }
        return message.toString().trim();
    }

    @Override
    public void unInitialize(final GL2 gl, final GLUgl2 glu) {
        synchronized (this) {
            vertexProgram = 0;
            fragmentProgram = 0;
            shaderProgram = 0;
        }
    }
}
