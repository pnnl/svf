package gov.pnnl.svf.texture;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.texture.TextureType;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Support for a 2d texture.
 *
 * @author Arthur Bleeker
 */
class Texture2dByteSupport extends Texture2dSupport {

    private static final Logger logger = Logger.getLogger(Texture2dByteSupport.class.toString());
    private final int width;
    private final int height;
    private final ByteBuffer data;

    Texture2dByteSupport(final Actor actor, final TextureType type, final TextureSupportParams params, final ByteBuffer data, final int width, final int height) {
        super(actor, type, params);
        this.data = data;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
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
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glGenTextures(1, id, 0);
            gl.glBindTexture(GL.GL_TEXTURE_2D, id[0]);
            textureObject = id[0];
            // check for parameter override
            if (getParams() != null) {
                getParams().overrideParams(gl);
            } else {
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
            }
            final TextureType type = getType();
            switch (type) {
                case ALPHA:
                    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_ALPHA, width, height, 0, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE, data.rewind());
                    break;
                case RGB:
                    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, width, height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, data.rewind());
                    break;
                case RGBA:
                    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, data.rewind());
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
