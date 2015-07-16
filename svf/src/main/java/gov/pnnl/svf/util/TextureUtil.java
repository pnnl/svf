package gov.pnnl.svf.util;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import gov.pnnl.svf.core.texture.TextureType;
import gov.pnnl.svf.scene.Scene;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 * Utility for working with textures.
 *
 * @author Arthur Bleeker
 */
public class TextureUtil {

    private static final Logger logger = Logger.getLogger(TextureUtil.class.toString());
    /**
     * List of image extensions in priority order.
     */
    public static final List<String> IMAGE_EXTENSIONS = Collections.unmodifiableList(Arrays.asList("bmp", "jpg", "png", "gif", null));

    /**
     * Constructor is private to prevent instantiation of a static helper class.
     */
    private TextureUtil() {
        super();
    }

    /**
     * Creates a direct byte buffer from an AWT buffered image. This is a
     * convenience method for createByteBuffer(image, TextureType.RGBA).
     *
     * @param image the buffered image to use as the source
     *
     * @return the newly created byte buffer
     */
    public static ByteBuffer createByteBuffer(final BufferedImage image) {
        return TextureUtil.createByteBuffer(image, TextureType.RGBA);
    }

    /**
     * Creates a direct byte buffer from an AWT buffered image.
     *
     * @param image the buffered image to use as the source
     * @param type  the type of byte buffer to create
     *
     * @return the newly created byte buffer
     */
    public static ByteBuffer createByteBuffer(final BufferedImage image, final TextureType type) {
        if (image == null) {
            throw new NullPointerException("image");
        }
        if (type == null) {
            throw new NullPointerException("type");
        }
        final Raster raster = image.getData();
        int size = 0;
        switch (type) {
            case RGBA:
                size = 4;
                break;
            case RGB:
                size = 3;
                break;
            case ALPHA:
                size = 1;
                break;
            default:
                throw new IllegalArgumentException("type");
        }
        final ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * size);
        final int w = image.getWidth();
        final int h = image.getHeight();
        final int[] temp = new int[w * size];
        for (int y = 0; y < h; y++) {
            raster.getPixels(0, y, w, 1, temp);
            for (final int b : temp) {
                buffer.put((byte) b);
            }
        }
        return buffer;
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene   the scena that this texture data will be used in
     * @param file    the image file to load
     * @param mipmaps true to generate mipmaps
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final File file, final boolean mipmaps) {
        return TextureUtil.createTextureData(scene, file, mipmaps, IMAGE_EXTENSIONS);
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene          the scena that this texture data will be used in
     * @param file           the image file to load
     * @param mipmaps        true to generate mipmaps
     * @param imageExtension prioritized image extension to use for parsing
     *                       image
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final File file, final boolean mipmaps, final String imageExtension) {
        final List<String> imageExtensions = new ArrayList<>(IMAGE_EXTENSIONS);
        imageExtensions.remove(imageExtension);
        imageExtensions.add(0, imageExtension);
        return TextureUtil.createTextureData(scene, file, mipmaps, imageExtensions);
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene           the scena that this texture data will be used in
     * @param file            the image file to load
     * @param mipmaps         true to generate mipmaps
     * @param imageExtensions list of prioritized image extensions to use for
     *                        parsing image
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final File file, final boolean mipmaps, final List<String> imageExtensions) {
        List<Exception> faults = null;
        for (final String ext : imageExtensions) {
            try {
                return TextureIO.newTextureData(scene.getExtended().getGL().getGLProfile(), file, mipmaps, ext);
            } catch (final IOException | RuntimeException ex) {
                if (faults == null) {
                    faults = new ArrayList<>();
                }
                faults.add(ex);
            }
        }
        logger.log(Level.WARNING, "Unable to generate a texture from file \"{0}\" because of faults: {1}", new Object[]{file, faults});
        return null;
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene   the scena that this texture data will be used in
     * @param stream  the image stream to load from
     * @param mipmaps true to generate mipmaps
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final InputStream stream, final boolean mipmaps) {
        return TextureUtil.createTextureData(scene, stream, mipmaps, IMAGE_EXTENSIONS);
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene          the scena that this texture data will be used in
     * @param stream         the image stream to load from
     * @param mipmaps        true to generate mipmaps
     * @param imageExtension prioritized image extension to use for parsing
     *                       image
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final InputStream stream, final boolean mipmaps, final String imageExtension) {
        final List<String> imageExtensions = new ArrayList<>(IMAGE_EXTENSIONS);
        imageExtensions.remove(imageExtension);
        imageExtensions.add(0, imageExtension);
        return TextureUtil.createTextureData(scene, stream, mipmaps, imageExtensions);
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene           the scena that this texture data will be used in
     * @param stream          the image stream to load from
     * @param mipmaps         true to generate mipmaps
     * @param imageExtensions list of prioritized image extensions to use for
     *                        parsing image
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final InputStream stream, final boolean mipmaps, final List<String> imageExtensions) {
        List<Exception> faults = null;
        for (final String ext : imageExtensions) {
            try {
                return TextureIO.newTextureData(scene.getExtended().getGL().getGLProfile(), stream, mipmaps, ext);
            } catch (final IOException | RuntimeException ex) {
                if (faults == null) {
                    faults = new ArrayList<>();
                }
                faults.add(ex);
            }
        }
        logger.log(Level.WARNING, "Unable to generate a texture from stream \"{0}\" because of faults: {1}", new Object[]{stream, faults});
        return null;
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene   the scena that this texture data will be used in
     * @param url     the image url to load
     * @param mipmaps true to generate mipmaps
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final URL url, final boolean mipmaps) {
        return TextureUtil.createTextureData(scene, url, mipmaps, FilenameUtils.getExtension(url.getPath()));
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene          the scena that this texture data will be used in
     * @param url            the image url to load
     * @param mipmaps        true to generate mipmaps
     * @param imageExtension prioritized image extension to use for parsing
     *                       image
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final URL url, final boolean mipmaps, final String imageExtension) {
        final List<String> imageExtensions = new ArrayList<>(IMAGE_EXTENSIONS);
        imageExtensions.remove(imageExtension);
        imageExtensions.add(0, imageExtension);
        return TextureUtil.createTextureData(scene, url, mipmaps, imageExtensions);
    }

    /**
     * Creates texture data for the creation of a texture.
     *
     * @param scene           the scena that this texture data will be used in
     * @param url             the image url to load
     * @param mipmaps         true to generate mipmaps
     * @param imageExtensions list of prioritized image extensions to use for
     *                        parsing image
     *
     * @return the new texture data or null if it failed
     */
    public static TextureData createTextureData(final Scene scene, final URL url, final boolean mipmaps, final List<String> imageExtensions) {
        List<Exception> faults = null;
        for (final String ext : imageExtensions) {
            try {
                return TextureIO.newTextureData(scene.getExtended().getGL().getGLProfile(), url, mipmaps, ext);
            } catch (final IOException | RuntimeException ex) {
                if (faults == null) {
                    faults = new ArrayList<>();
                }
                faults.add(ex);
            }
        }
        logger.log(Level.WARNING, "Unable to generate a texture from URL \"{0}\" because of faults: {1}", new Object[]{url, faults});
        return null;
    }
}
