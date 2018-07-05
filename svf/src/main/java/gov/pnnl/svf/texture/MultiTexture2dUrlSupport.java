package gov.pnnl.svf.texture;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.texture.TextureType;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Multi-texture support object allows an actor to select various texture
 * objects.
 *
 * @author Amelia Bleeker
 * @author Lyndsey Franklin
 *
 */
class MultiTexture2dUrlSupport extends MultiTexture2dSupport {

    private static final Logger logger = Logger.getLogger(MultiTexture2dUrlSupport.class.toString());
    private final List<URL> urls = new ArrayList<>();

    MultiTexture2dUrlSupport(final Actor actor, final TextureType type, final List<URL> urls) {
        super(actor, type);
        if (urls != null) {
            this.urls.addAll(urls);
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
            if (isDisposed() || urls.isEmpty()) {
                return;
            }
            textures.clear();
            try {
                for (final URL url : urls) {
                    if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                        logger.log(Level.INFO, String.format("Loading the texture from URL %s.", url.getPath()));
                    }
                    final Texture texture = TextureIO.newTexture(url, false, null);
                    textures.add(texture);
                    textureObject = texture.getTextureObject(gl);
                }
            } catch (final IOException | RuntimeException ex) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to load the textures.", getScene()), ex);
                // roll back
                for (final Texture texture : textures) {
                    texture.destroy(gl);
                }
                textureObject = -1;
                urls.clear();
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
            return !textures.isEmpty() || urls.isEmpty();
        }
    }

    @Override
    public MultiTexture2dUrlSupport nextIndex() {
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
                if (urls.get(i) != null && getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                    logger.log(Level.INFO, String.format("Unloading the texture %s.", urls.get(i)));
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
