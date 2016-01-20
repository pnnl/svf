package gov.pnnl.svf.texture;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.texture.TextureType;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides texturing of an object. Add this class to actor's that
 * require texturing.
 *
 * @author Arthur Bleeker
 *
 */
class Texture2dDataSupport extends Texture2dSupport {

    private static final Logger logger = Logger.getLogger(Texture2dDataSupport.class.toString());
    private TextureData data;
    private Texture texture;

    /**
     * Constructor protected to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The actor that owns this texture support object.
     * @param type  the texture type
     * @param data  the texture data object
     *
     * @throws NullPointerException if the actor is null
     */
    Texture2dDataSupport(final Actor actor, final TextureData data) {
        super(actor, TextureType.RGBA, null);
        this.data = data;
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        final int target;
        final boolean flip;
        final boolean mipmaps;
        synchronized (this) {
            if (texture == null) {
                return;
            }
            target = texture.getTextureObject(gl);
            flip = !texture.getMustFlipVertically();
            mipmaps = data.getMipmap();
        }
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_TEXTURE_BIT);
        drawState.setAttrib();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, target);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        if (mipmaps) {
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
        } else {
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        }
        gl.glTexEnvi(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL2ES1.GL_MODULATE);
        if (flip) {
            // set the texture transform
            gl.glPushAttrib(GL2.GL_TRANSFORM_BIT);
            gl.glMatrixMode(GL.GL_TEXTURE);
            gl.glPushMatrix();
            drawState.setMatrixTexture();
            gl.glScaled(1.0, -1.0, 1.0);
            gl.glTranslated(0.0, -1.0, 0.0);
            gl.glPopAttrib();
        }
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (drawState.isMatrixTexture()) {
            gl.glPushAttrib(GL2.GL_TRANSFORM_BIT);
            gl.glMatrixMode(GL.GL_TEXTURE);
            gl.glPopMatrix();
            gl.glPopAttrib();
        }
        if (drawState.isAttrib()) {
            gl.glPopAttrib();
        }
        drawState.clearValues();
    }

    @Override
    public int getHeight() {
        synchronized (this) {
            if (texture != null) {
                return texture.getHeight();
            }
        }
        return 0;
    }

    @Override
    public int getTexture() {
        synchronized (this) {
            if (texture != null) {
                return texture.getTextureObject(null);
            }
        }
        return 0;
    }

    @Override
    public int getWidth() {
        synchronized (this) {
            if (texture != null) {
                return texture.getWidth();
            }
        }
        return 0;
    }

    /**
     * Loads the PNG image from file as a texture.
     */
    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        unInitialize(gl, glu);
        int textureObject = -1;
        synchronized (this) {
            if (isDisposed() || data == null) {
                return;
            }
            if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                logger.log(Level.INFO, String.format("Loading the texture from texture data %s.", data));
            }
            try {
                texture = TextureIO.newTexture(data);
                textureObject = texture.getTextureObject(gl);
            } catch (final RuntimeException ex) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to load the texture.", getScene()), ex);
                data = null;
            }
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

    /**
     * Checks whether the texture has been loaded.
     */
    @Override
    public boolean isInitialized() {
        synchronized (this) {
            return (texture != null) || (data == null);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TextureSupport [texture=");
        builder.append(texture);
        builder.append(", data=");
        builder.append(data);
        builder.append("]");
        return builder.toString();
    }

    /**
     * Unloads the currently loaded texture.
     */
    @Override
    public void unInitialize(final GL2 gl, final GLUgl2 glu) {
        int textureObject;
        synchronized (this) {
            if (isDisposed() || texture == null) {
                return;
            }
            if (data != null && getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                logger.log(Level.INFO, String.format("Unloading the texture %s.", data));
            }
            // destroying the texture when using a texture map destroys
            // unintended textures that are also in the
            // same map
            textureObject = texture.getTextureObject(gl);
            texture.destroy(gl);
            texture = null;
        }
        // notify the listeners
        for (final TextureSupportListener listener : getListeners()) {
            listener.textureUnloaded(this);
        }
        getPropertyChangeSupport().firePropertyChange(TEXTURE, textureObject, -1);
    }
}
