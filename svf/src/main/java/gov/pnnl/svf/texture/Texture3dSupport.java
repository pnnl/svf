package gov.pnnl.svf.texture;

import gov.pnnl.svf.actor.AbstractActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.texture.TextureType;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * Support for a volumetric 3d texture.
 *
 * @author Arthur Bleeker
 */
public class Texture3dSupport extends TextureSupport {

    private static final Logger logger = Logger.getLogger(Texture3dSupport.class.toString());
    private final int width;
    private final int height;
    private final int depth;
    private ByteBuffer data = null;

    /**
     *
     * @param actor
     * @param type
     * @param params
     * @param data
     * @param size
     */
    protected Texture3dSupport(final AbstractActor actor, final TextureType type, final TextureSupportParams params, final ByteBuffer data, final int size) {
        super(actor, type, params);
        this.data = data;
        width = size;
        height = size;
        depth = size;
    }

    /**
     *
     * @param actor
     * @param type
     * @param params
     * @param data
     * @param width
     * @param height
     * @param depth
     */
    protected Texture3dSupport(final AbstractActor actor, final TextureType type, final TextureSupportParams params, final ByteBuffer data, final int width,
                               final int height, final int depth) {
        super(actor, type, params);
        this.data = data;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    /**
     * Constructor
     *
     * @param actor The actor that owns this texture support object.
     * @param type  The type of texture
     * @param data  The byte buffer that represents the data
     * @param size  The width, height, and depth of this texture
     *
     * @return the new support object
     *
     * @throws NullPointerException if the actor or type is null
     */
    public static Texture3dSupport newInstance(final AbstractActor actor, final TextureType type, final ByteBuffer data, final int size) {
        final Texture3dSupport instance = new Texture3dSupport(actor, type, null, data, size);
        actor.add(instance);
        return instance;
    }

    /**
     * Constructor
     *
     * @param actor  The actor that owns this texture support object.
     * @param type   The type of texture
     * @param data   The byte buffer that represents the data
     * @param width  The width of this texture
     * @param height The height of this texture
     * @param depth  The depth of this texture
     *
     * @return the new support object
     *
     * @throws NullPointerException if the actor or type is null
     */
    public static Texture3dSupport newInstance(final AbstractActor actor, final TextureType type, final ByteBuffer data, final int width, final int height,
                                               final int depth) {
        final Texture3dSupport instance = new Texture3dSupport(actor, type, null, data, width, height, depth);
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
     * @param size   The width, height, and depth of this texture
     *
     * @return the new support object
     *
     * @throws NullPointerException if the actor or type is null
     */
    public static Texture3dSupport newInstance(final AbstractActor actor, final TextureType type, final TextureSupportParams params, final ByteBuffer data,
                                               final int size) {
        final Texture3dSupport instance = new Texture3dSupport(actor, type, params, data, size);
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
     * @param width  The width of this texture
     * @param height The height of this texture
     * @param depth  The depth of this texture
     *
     * @return the new support object
     *
     * @throws NullPointerException if the actor or type is null
     */
    public static Texture3dSupport newInstance(final AbstractActor actor, final TextureType type, final TextureSupportParams params, final ByteBuffer data,
                                               final int width, final int height,
                                               final int depth) {
        final Texture3dSupport instance = new Texture3dSupport(actor, type, params, data, width, height, depth);
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
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_CURRENT_BIT | GL2.GL_TEXTURE_BIT);
        drawState.setAttrib();
        gl.glEnable(GL2ES2.GL_TEXTURE_3D);
        gl.glBindTexture(GL2ES2.GL_TEXTURE_3D, target);
        gl.glTexParameteri(GL2ES2.GL_TEXTURE_3D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL2ES2.GL_TEXTURE_3D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexEnvf(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL2ES1.GL_MODULATE);
    }

    /**
     *
     * @return the texture depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     *
     * @return the texture height
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return the texture width
     */
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
            gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_CURRENT_BIT | GL2.GL_TEXTURE_BIT);
            gl.glEnable(GL2ES2.GL_TEXTURE_3D);
            gl.glGenTextures(1, id, 0);
            gl.glBindTexture(GL2ES2.GL_TEXTURE_3D, id[0]);
            textureObject = id[0];
            // check for parameter override
            if (getParams() != null) {
                getParams().overrideParams(gl);
            } else {
                gl.glTexParameteri(GL2ES2.GL_TEXTURE_3D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL2ES2.GL_TEXTURE_3D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL2ES2.GL_TEXTURE_3D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
                gl.glTexParameteri(GL2ES2.GL_TEXTURE_3D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
                gl.glTexParameteri(GL2ES2.GL_TEXTURE_3D, GL2ES2.GL_TEXTURE_WRAP_R, GL2.GL_CLAMP);
            }
            final TextureType type = getType();
            switch (type) {
                case ALPHA:
                    gl.glTexImage3D(GL2ES2.GL_TEXTURE_3D, 0, GL.GL_ALPHA, width, height, depth, 0, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE, data.rewind());
                    break;
                case RGB:
                    gl.glTexImage3D(GL2ES2.GL_TEXTURE_3D, 0, GL.GL_RGB, width, height, depth, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, data.rewind());
                    break;
                case RGBA:
                    gl.glTexImage3D(GL2ES2.GL_TEXTURE_3D, 0, GL.GL_RGBA, width, height, depth, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, data.rewind());
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
