package gov.pnnl.svf.texture;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.texture.TextureType;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.glu.gl2.GLUgl2;

/**
 * Multi-texture support object allows an actor to select various texture
 * objects.
 *
 * @author Arthur Bleeker
 * @author Lyndsey Franklin
 *
 */
public abstract class MultiTexture2dSupport extends Texture2dSupport {

    protected final List<Texture> textures = new ArrayList<>();
    protected int currentIndex = 0;

    /**
     *
     * @param actor
     * @param type
     */
    protected MultiTexture2dSupport(final Actor actor, final TextureType type) {
        super(actor, type, null);
    }

    /**
     * Caller of wrapped constructor which adds the support to the given actor
     *
     * @param actor AbstractActor to receive the report
     * @param data  List of the data loaded from the images used for the texture
     *              generation.
     *
     * @return MultiTextureSupport constructed in the call
     */
    public static MultiTexture2dSupport newInstance(final Actor actor, final TextureData... data) {
        final MultiTexture2dSupport instance = new MultiTexture2dDataSupport(actor, Arrays.asList(data));
        actor.add(instance);
        return instance;
    }

    /**
     * Caller of wrapped constructor which adds the support to the given actor
     *
     * @param actor AbstractActor to receive the report
     * @param urls  URLs of images to use for animation
     *
     * @return MultiTextureSupport constructed in the call
     */
    public static MultiTexture2dSupport newInstance(final Actor actor, final URL... urls) {
        return MultiTexture2dSupport.newInstance(actor, Arrays.asList(urls));
    }

    /**
     * Caller of wrapped constructor which adds the support to the given actor
     *
     * @param actor AbstractActor to receive the report
     * @param urls  URLs of images to use for animation
     *
     * @return MultiTextureSupport constructed in the call
     */
    public static MultiTexture2dSupport newInstance(final Actor actor, final List<URL> urls) {
        final MultiTexture2dSupport instance = new MultiTexture2dUrlSupport(actor, TextureType.RGBA, urls);
        actor.add(instance);
        return instance;
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        final Texture texture;
        synchronized (this) {
            if (textures.isEmpty()) {
                return;
            }
            texture = textures.get(currentIndex);
        }
        if (texture != null) {
            gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_TEXTURE_BIT);
            drawState.setAttrib();
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureObject(gl));
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexEnvf(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL2ES1.GL_MODULATE);
            if (!texture.getMustFlipVertically()) {
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
        final Texture texture;
        synchronized (this) {
            texture = textures.get(0);
        }
        if (texture != null) {
            return texture.getHeight();
        }
        return 0;
    }

    @Override
    public int getTexture() {
        final Texture texture;
        synchronized (this) {
            texture = textures.get(0);
        }
        if (texture != null) {
            return texture.getTextureObject(null);
        }
        return 0;
    }

    @Override
    public int getWidth() {
        final Texture texture;
        synchronized (this) {
            texture = textures.get(0);
        }
        if (texture != null) {
            return texture.getWidth();
        }
        return 0;
    }

    /**
     * Controls the index value within the array of URLs used in animation
     *
     * @return reference to this MultiTextureSupport
     */
    /**
     * Controls the index value within the array of URLs used in animation
     *
     * @return reference to this MultiTextureSupport
     */
    public MultiTexture2dSupport nextIndex() {
        synchronized (this) {
            if (textures.isEmpty()) {
                currentIndex = 0;
            } else {
                currentIndex++;
                if (currentIndex == textures.size()) {
                    currentIndex = 0;
                }
            }
        }
        return this;
    }
}
