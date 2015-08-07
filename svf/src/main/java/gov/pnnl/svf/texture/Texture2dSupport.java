package gov.pnnl.svf.texture;

import com.jogamp.opengl.util.texture.TextureData;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.texture.TextureType;
import java.net.URL;
import java.nio.ByteBuffer;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.glu.gl2.GLUgl2;

/**
 * This class provides texturing of an object. Add this class to actor's that
 * require texturing.
 *
 * @author Arthur Bleeker
 */
public abstract class Texture2dSupport extends TextureSupport {

    /**
     * Constructor protected to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor  The actor that owns this texture support object.
     * @param type   the texture type
     * @param params used to override the initialization parameters
     *
     * @throws NullPointerException if the actor is null
     */
    protected Texture2dSupport(final Actor actor, final TextureType type, final TextureSupportParams params) {
        super(actor, type, params);
    }

    /**
     * Constructor
     *
     * @param actor The actor that owns this texture support object.
     * @param data  The data loaded from the image used for the texture
     *              generation.
     *
     * @return a new instance of this object
     *
     * @throws NullPointerException if the actor is null
     */
    public static Texture2dSupport newInstance(final Actor actor, final TextureData data) {
        final Texture2dSupport instance = new Texture2dDataSupport(actor, data);
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
     *
     * @return the new support object
     *
     * @throws NullPointerException if the actor or type is null
     */
    public static Texture2dSupport newInstance(final Actor actor, final TextureType type, final ByteBuffer data, final int width, final int height) {
        final Texture2dSupport instance = new Texture2dByteSupport(actor, type, null, data, width, height);
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
     *
     * @return the new support object
     *
     * @throws NullPointerException if the actor or type is null
     */
    public static Texture2dSupport newInstance(final Actor actor, final TextureType type, final TextureSupportParams params, final ByteBuffer data,
                                               final int width, final int height) {
        final Texture2dSupport instance = new Texture2dByteSupport(actor, type, params, data, width, height);
        actor.add(instance);
        return instance;
    }

    /**
     * Constructor
     *
     * @param actor The actor that owns this texture support object.
     * @param url   The URL that points to the PNG image used for the texture
     *              generation. Pass null to prevent the actor from inheriting
     *              TextureSupport from a parent actor.
     *
     * @return a new instance of this object
     *
     * @throws NullPointerException if the actor is null
     */
    public static Texture2dSupport newInstance(final Actor actor, final URL url) {
        final Texture2dSupport instance = new Texture2dUrlSupport(actor, TextureType.RGBA, url);
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
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, target);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexEnvi(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL2ES1.GL_MODULATE);
    }

    /**
     *
     * @return the height of this texture
     */
    public abstract int getHeight();

    /**
     *
     * @return the width of this texture
     */
    public abstract int getWidth();
}
