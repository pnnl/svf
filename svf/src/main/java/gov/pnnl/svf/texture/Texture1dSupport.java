package gov.pnnl.svf.texture;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.texture.TextureType;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Support for a lookup 1d texture.
 *
 * @author Arthur Bleeker
 */
public class Texture1dSupport extends TextureSupport {

    private static final Logger logger = Logger.getLogger(Texture1dSupport.class.toString());
    private final int length;
    private final ByteBuffer data;

    /**
     *
     * @param actor
     * @param type
     * @param params
     * @param data
     * @param length
     */
    protected Texture1dSupport(final Actor actor, final TextureType type, final TextureSupportParams params, final ByteBuffer data, final int length) {
        super(actor, type, params);
        this.data = data;
        this.length = length;
    }

    /**
     * Constructor
     *
     * @param actor  The actor that owns this texture support object.
     * @param type   The type of texture
     * @param data   The byte buffer that represents the data
     * @param length The length of this texture
     *
     * @return the new support object
     *
     * @throws NullPointerException if the actor or type is null
     */
    public static Texture1dSupport newInstance(final Actor actor, final TextureType type, final ByteBuffer data, final int length) {
        final Texture1dSupport instance = new Texture1dSupport(actor, type, null, data, length);
        actor.add(instance);
        return instance;
    }

    /**
     * Constructor
     *
     * @param actor  The actor that owns this texture support object.
     * @param type   The type of texture
     * @param params used to override the initialization parameters
     * @param data   The byte buffer that represents the data
     * @param length The length of this texture
     *
     * @return the new support object
     *
     * @throws NullPointerException if the actor or type is null
     */
    public static Texture1dSupport newInstance(final Actor actor, final TextureType type, final TextureSupportParams params, final ByteBuffer data,
                                               final int length) {
        final Texture1dSupport instance = new Texture1dSupport(actor, type, params, data, length);
        actor.add(instance);
        return instance;
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        final int target;
        synchronized (this) {
            if (id[0] == -1) {
                return;
            }
            target = id[0];
        }
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_TEXTURE_BIT);
        drawState.setAttrib();
        gl.glEnable(GL2GL3.GL_TEXTURE_1D);
        gl.glBindTexture(GL2GL3.GL_TEXTURE_1D, target);
        gl.glTexParameteri(GL2GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL2GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexEnvf(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL2ES1.GL_MODULATE);
    }

    /**
     * @return the length of this texture
     */
    public int getLength() {
        return length;
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        unInitialize(gl, glu);
        int textureObject;
        synchronized (this) {
            if (isDisposed() || data == null) {
                return;
            }
            if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                logger.log(Level.INFO, String.format("Loading the texture from byte array %s.", data));
            }
            // create the texture id and bind it
            gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_TEXTURE_BIT);
            gl.glEnable(GL2GL3.GL_TEXTURE_1D);
            gl.glGenTextures(1, id, 0);
            gl.glBindTexture(GL2GL3.GL_TEXTURE_1D, id[0]);
            textureObject = id[0];
            // check for parameter override
            if (getParams() != null) {
                getParams().overrideParams(gl);
            } else {
                gl.glTexParameteri(GL2GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL2GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL2GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
            }
            final TextureType type = getType();
            switch (type) {
                case ALPHA:
                    gl.glTexImage1D(GL2GL3.GL_TEXTURE_1D, 0, GL.GL_ALPHA, length, 0, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE, data.rewind());
                    break;
                case RGB:
                    gl.glTexImage1D(GL2GL3.GL_TEXTURE_1D, 0, GL.GL_RGB, length, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, data.rewind());
                    break;
                case RGBA:
                    gl.glTexImage1D(GL2GL3.GL_TEXTURE_1D, 0, GL.GL_RGBA, length, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, data.rewind());
                    break;
                default:
                    if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                        logger.log(Level.WARNING, "{0}: Unknown enum type {1}", new Object[]{getScene(), type});
                    }
            }
            gl.glPopAttrib();
        }
        // notify the listeners
        if (textureObject > -1) {
            for (final TextureSupportListener listener : getListeners()) {
                listener.textureLoaded(this);
            }
        } else {
            for (final TextureSupportListener listener : getListeners()) {
                listener.textureError(this);
            }
        }
        getPropertyChangeSupport().firePropertyChange(TEXTURE, -1, textureObject);
    }

    @Override
    public boolean isInitialized() {
        synchronized (this) {
            return (id[0] != -1) || (data == null);
        }
    }

    @Override
    public void unInitialize(final GL2 gl, final GLUgl2 glu) {
        int textureObject;
        synchronized (this) {
            if (isDisposed() || id[0] == -1) {
                return;
            }
            if (data != null && getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                logger.log(Level.INFO, String.format("Unloading the texture from byte array %s.", data));
            }
            gl.glDeleteTextures(1, id, 0);
            textureObject = id[0];
            id[0] = -1;
        }
        // notify the listeners
        for (final TextureSupportListener listener : getListeners()) {
            listener.textureUnloaded(this);
        }
        getPropertyChangeSupport().firePropertyChange(TEXTURE, textureObject, -1);
    }
}
