package gov.pnnl.svf.texture;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.texture.TextureType;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Initializable;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.AbstractSupport;
import gov.pnnl.svf.update.UninitializeTask;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * Abstract base class for textures.
 *
 * @author Arthur Bleeker
 */
public abstract class TextureSupport extends AbstractSupport<TextureSupportListener> implements Drawable, Initializable {

    /**
     * String representation of a field in this object.
     */
    public static final String TEXTURE = "texture";
    protected final int[] id = new int[]{-1};
    private final TextureType type;
    private final TextureSupportParams params;

    /**
     *
     * @param actor
     * @param type
     * @param params
     */
    protected TextureSupport(final Actor actor, final TextureType type, final TextureSupportParams params) {
        super(actor);
        if (type == null) {
            throw new NullPointerException("type");
        }
        this.type = type;
        this.params = params;
    }

    /**
     *
     * @return the texture type
     */
    public TextureType getType() {
        return type;
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (drawState.isAttrib()) {
            gl.glPopAttrib();
        }
        drawState.clearValues();
    }

    @Override
    public void dispose() {
        super.dispose();
        UninitializeTask.schedule(getActor().getScene(), this);
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
    public boolean isSlow() {
        return true;
    }

    /**
     * @return the id for the texture
     */
    public int getTexture() {
        synchronized (this) {
            return id[0];
        }
    }

    /**
     *
     * @return a reference to the texture support override params object
     */
    protected TextureSupportParams getParams() {
        return params;
    }
}
