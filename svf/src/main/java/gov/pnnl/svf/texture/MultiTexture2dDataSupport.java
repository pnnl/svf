package gov.pnnl.svf.texture;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.texture.TextureType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * Multi-texture support object allows an actor to select various texture
 * objects.
 *
 * @author Arthur Bleeker
 * @author Lyndsey Franklin
 *
 */
class MultiTexture2dDataSupport extends MultiTexture2dSupport {

    private static final Logger logger = Logger.getLogger(MultiTexture2dDataSupport.class.toString());
    private final List<TextureData> data = new ArrayList<>();

    MultiTexture2dDataSupport(final Actor actor, final List<TextureData> data) {
        super(actor, TextureType.RGBA);
        if (data != null) {
            this.data.addAll(data);
        }
    }

    /**
     * Loads the PNG image from file as a texture.
     */
    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        unInitialize(gl, glu);
        int textureObject = -1;
        synchronized (this) {
            if (isDisposed() || data.isEmpty()) {
                return;
            }
            textures.clear();
            try {
                for (final TextureData d : data) {
                    if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                        logger.log(Level.INFO, String.format("Loading the texture from texture data %s.", d));
                    }
                    final Texture texture = TextureIO.newTexture(d);
                    textures.add(texture);
                    textureObject = texture.getTextureObject(gl);
                }
            } catch (final RuntimeException ex) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to load the texture.", getScene()), ex);
                // roll back
                for (final Texture texture : textures) {
                    texture.destroy(gl);
                }
                textureObject = -1;
                data.clear();
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
     * true if the texture has been loaded
     */
    @Override
    public boolean isInitialized() {
        synchronized (this) {
            return !textures.isEmpty() || data.isEmpty();
        }
    }

    @Override
    public MultiTexture2dDataSupport nextIndex() {
        super.nextIndex();
        return this;
    }

    @Override
    public void unInitialize(final GL2 gl, final GLUgl2 glu) {
        final int textureObject = -1;
        synchronized (this) {
            if (isDisposed() || textures.isEmpty()) {
                return;
            }
            for (int i = 0; i < textures.size(); i++) {
                if (data.get(i) != null && getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                    logger.log(Level.INFO, String.format("Unloading the texture %s.", data.get(i)));
                }
                textures.get(i).destroy(gl);
            }
            textures.clear();
        }
        // notify the listeners
        for (final TextureSupportListener listener : getListeners()) {
            listener.textureUnloaded(this);
        }
        getPropertyChangeSupport().firePropertyChange(TEXTURE, textureObject, -1);
    }
}
