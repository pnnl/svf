package gov.pnnl.svf.picking;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import gov.pnnl.svf.core.collections.CountingHashSet;
import gov.pnnl.svf.core.collections.CountingSet;
import gov.pnnl.svf.core.collections.KeyValuePair;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.DrawState;
import gov.pnnl.svf.core.util.StateUtil;
import static gov.pnnl.svf.picking.AbstractColorPickingCamera.HEIGHT;
import static gov.pnnl.svf.picking.AbstractColorPickingCamera.WIDTH;
import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.scene.SceneExt;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.nio.IntBuffer;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for working with color picking in a scene. This class is not
 * intended to be instantiated or used directly.
 *
 * @author Arthur Bleeker
 */
public class ColorPickingUtils implements Disposable {

    private static final Logger logger = Logger.getLogger(ColorPickingUtils.class.getName());
    /**
     * The amount to increment when determining the next color.
     */
    public static final int STEP_NUMBER = 1;
    /**
     * The maximum number that can be used for determining a color.
     */
    public static final int MAX_NUMBER = 16777215;
    private static final int UNINITIALIZED = -1;
    private static final int ID_TEXTURE = 0;
    private static final int ID_FRAMEBUFFER = 1;
    private static final int ID_RENDERBUFFER = 2;
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[0];
    private byte state = StateUtil.NONE;
    private final DrawState drawState = new DrawState();
    private final Map<Color, KeyValuePair<ColorPickingSupport, Object>> mapping = Collections
            .synchronizedMap(new HashMap<>());
    private int counter = STEP_NUMBER;
    private final SceneExt scene;
    // temporary viewport buffer
    private final int[] buffer = new int[1];
    private final int[] viewport = new int[4];
    private final int[] ids = new int[]{UNINITIALIZED, UNINITIALIZED, UNINITIALIZED};
    // image fields, this code was ported from AbstractReadbackBackend in the JOGL library
    // This image is exactly the correct size to render into the panel
    private BufferedImage offscreenImage;
    private Raster offscreenRaster;
    private final Object offscreenImageSync = new Object();
    // store the read back pixels before storing
    // in the BufferedImage
    private IntBuffer readBackInts;
    // For saving/restoring of OpenGL state during ReadPixels
    private final int[] swapbytes = new int[1];
    private final int[] rowlength = new int[1];
    private final int[] skiprows = new int[1];
    private final int[] skippixels = new int[1];
    private final int[] alignment = new int[1];

    /**
     * Constructor
     *
     * @param scene reference to the parent scene
     */
    public ColorPickingUtils(final SceneExt scene) {
        super();
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return StateUtil.isValue(state, DISPOSED_MASK);
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            state = StateUtil.setValue(state, DISPOSED_MASK);
        }
        mapping.clear();
        synchronized (offscreenImageSync) {
            offscreenRaster = null;
            offscreenImage = null;
            readBackInts = null;
        }
    }

    /**
     * This method will return the next unique color in the series. It is thread
     * safe.
     *
     * @return the next unique color
     */
    private Color nextColor() {
        final int red;
        final int green;
        final int blue;
        final int next;
        synchronized (this) {
            // increment by the step size
            next = counter += STEP_NUMBER;
            if (next >= MAX_NUMBER - STEP_NUMBER) {
                // start at magic number so that the next increment is not black
                // (black is the background color for color picking)
                counter = STEP_NUMBER;
            }
        }
        red = next & 0xFF;
        green = next >> 8 & 0xFF;
        blue = next >> 16 & 0xFF;
        //        System.out.println(String.format("r:%03d g:%03d b:%03d", red, green, blue));
        final Color color = new Color(red, green, blue);
        return color;
    }

    /**
     * Create a new mapping or return an existing mapping if one already exists.
     *
     * @param support the color picking support
     * @param item    the item
     *
     * @return the color associated with the item
     *
     * @throws IllegalStateException if no more color mappings can be created
     */
    public Color newMapping(final ColorPickingSupport support, final Object item) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final Color color = nextColor();
            synchronized (mapping) {
                if (!mapping.containsKey(color)) {
                    mapping.put(color, new KeyValuePair<>(support, item));
                    return color;
                }
            }
            // there is already a mapping for this color so try another one
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, "{0}: A mapping for the newly created color already exists: {1}", new Object[]{support.getActor().getScene(), color});
            }
        }
        throw new IllegalStateException("Unable to create another color mapping.");
    }

    /**
     * Clears the mapping for the specified item.
     *
     * @param color the color associated with an item
     */
    public void clearMapping(final Color color) {
        mapping.remove(color);
    }

    /**
     * Get the item that is mapped to the specified color.
     *
     * @param color the color associated with an item
     *
     * @return the item that is mapped or null
     */
    public KeyValuePair<ColorPickingSupport, Object> getMapping(final Color color) {
        return mapping.get(color);
    }

    /**
     * @return the total number of mappings
     */
    public int getMappingSize() {
        return mapping.size();
    }

    /**
     * @return the number of mappings remaining
     */
    public int getMappingRemaining() {
        return (MAX_NUMBER - STEP_NUMBER - mapping.size()) / STEP_NUMBER;
    }

    /**
     * Check for a picking hit using the specified screen coordinates. Only one
     * item per pixel can be returned regardless of how many items are occupying
     * the same space.
     *
     * @param x the x coordinate of the center of the pick in screen pixels
     * @param y the y coordinate of the center of the pick in screen pixels
     *
     * @return the color picking support object and item for the actor picked or
     *         null
     */
    KeyValuePair<ColorPickingSupport, Object> checkPickingHit(final int x, final int y) {
        final Set<KeyValuePair<ColorPickingSupport, Object>> hits = checkPickingHit(x, y, 1, 1);
        // there will be 1 hit at most using a single pixel to pick
        final Iterator<KeyValuePair<ColorPickingSupport, Object>> iterator = hits.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    /**
     * Check for a picking hit using the specified screen coordinates. Only one
     * item per pixel can be returned regardless of how many items are occupying
     * the same space.
     *
     * @param x the x coordinate of the center of the pick in screen pixels
     * @param y the y coordinate of the center of the pick in screen pixels
     * @param w the width of the pick in pixels
     * @param h the height of the pick in pixels
     *
     * @return the set of color picking support objects and items for the actors
     *         picked or null
     */
    public CountingSet<KeyValuePair<ColorPickingSupport, Object>> checkPickingHit(final int x, final int y, final int w, final int h) {
        synchronized (offscreenImageSync) {
            // make sure we are within the image bounds
            if (offscreenImage == null || x < 0 || y < 0 || x > offscreenImage.getWidth() - 1 || y > offscreenImage.getHeight() - 1) {
                return CountingHashSet.<KeyValuePair<ColorPickingSupport, Object>>emptySet();
            }
            if (w == 1 && h == 1) {
                // get the color of the pixel
                final int[] found = offscreenRaster.getPixel(x, y, (int[]) null);
                // single pick
                final Color color = new Color(found[0], found[1], found[2]);
                if (scene.getSceneBuilder().isVerbose()) {
                    logger.log(Level.FINE, "{0}: Picked Color at ({1},{2}) --> {3}", new Object[]{scene, x, y, color.toString()});
                }
                final KeyValuePair<ColorPickingSupport, Object> support = getMapping(color);
                if (scene.getSceneBuilder().isVerbose() && support == null && !(Color.BLACK.equals(color) || Color.WHITE.equals(color))) {
                    logger.log(Level.INFO, "{0}: Mapping not found for color: {1}", new Object[]{scene, color});
                }
                return support == null ? CountingHashSet.<KeyValuePair<ColorPickingSupport, Object>>emptySet() : CountingHashSet.singleton(support);
            } else {
                // get the color of the pixels
                final CountingSet<KeyValuePair<ColorPickingSupport, Object>> picks = new CountingHashSet<>();
                final int hw = Math.max(1, (w / 2));
                final int hh = Math.max(1, (h / 2));
                for (int xs = Math.max(0, x - hw); xs < Math.min(offscreenImage.getWidth() - 1, x + hw); xs++) {
                    for (int ys = Math.max(0, y - hh); ys < Math.min(offscreenImage.getHeight() - 1, y + hh); ys++) {
                        final int[] found = offscreenRaster.getPixel(xs, ys, (int[]) null);
                        // can be multiple picks
                        final Color color = new Color(found[0], found[1], found[2]);
                        final KeyValuePair<ColorPickingSupport, Object> support = getMapping(color);
                        if (scene.getSceneBuilder().isVerbose() && support == null && !Color.BLACK.equals(color)) {
                            logger.log(Level.INFO, "{0}: Mapping not found for color: {1}", new Object[]{scene, color});
                        }
                        if (support != null) {
                            picks.add(support);
                        }
                    }
                }
                return picks;
            }
        }
    }

    /**
     * Called at the start of the color picking render cycle. This is utilized
     * by the scene. This start and stop method pairs will utilize a texture for
     * the color picking buffer.
     *
     * @param gl reference to gl
     *
     * @return the buffer used
     */
    public int start(final GL2 gl) {
        // cleanup
        cleanupBuffers(gl);
        // initialize
        start(gl, GL.GL_FRONT_AND_BACK);
        // setup for using a texture
        try {
            // framebuffer
            gl.glGenFramebuffers(1, ids, ID_FRAMEBUFFER);
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, ids[ID_FRAMEBUFFER]);
            drawState.setExtraA();
            // texture
            // create a new texture
            gl.glGenTextures(1, ids, ID_TEXTURE);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, ids[ID_TEXTURE]);
            drawState.setExtraB();
            gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGB, viewport[2], viewport[3], 0, GL2.GL_RGB, GL2.GL_FLOAT, null);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
            // depth buffer
            gl.glGenRenderbuffers(1, ids, ID_RENDERBUFFER);
            gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, ids[ID_RENDERBUFFER]);
            drawState.setExtraC();
            gl.glRenderbufferStorage(GL2.GL_RENDERBUFFER, GL2.GL_DEPTH_COMPONENT,
                                     viewport[2], viewport[3]);
            gl.glFramebufferRenderbuffer(GL2.GL_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT,
                                         GL2.GL_RENDERBUFFER, ids[ID_RENDERBUFFER]);
            // configure
            gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0,
                                      GL2.GL_TEXTURE_2D, ids[ID_TEXTURE], 0);
            gl.glDrawBuffer(GL2.GL_COLOR_ATTACHMENT0);
            // verify
            if (gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER) == GL2.GL_FRAMEBUFFER_COMPLETE) {
                this.buffer[0] = GL2.GL_COLOR_ATTACHMENT0;
            } else if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, "{0}: Framebuffer for rendering color picking layer is not complete!", scene);
            }
        } catch (final GLException ex) {
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Unable to create framebuffer for rendering color picking layer!", scene), ex);
            }
        }
        // return the active buffer
        return this.buffer[0];
    }

    /**
     * Called at the start of the color picking render cycle. This is utilized
     * by the scene.
     *
     * @param gl     reference to gl
     * @param buffer the requested buffer
     *
     * @return the buffer used
     */
    public int start(final GL2 gl, final int buffer) {
        this.buffer[0] = buffer;
        drawState.clearValues();
        // disable anything that can change the final color
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);
        drawState.setAttrib();
        gl.glDisable(GL.GL_MULTISAMPLE);
        gl.glDisable(GL2ES1.GL_POINT_SMOOTH);
        gl.glDisable(GL.GL_LINE_SMOOTH);
        gl.glDisable(GL2GL3.GL_POLYGON_SMOOTH);
        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL.GL_DITHER);
        gl.glDisable(GL2ES1.GL_FOG);
        gl.glDisable(GLLightingFunc.GL_LIGHTING);
        gl.glDisable(GL2GL3.GL_TEXTURE_1D);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glDisable(GL2ES2.GL_TEXTURE_3D);
        gl.glShadeModel(GLLightingFunc.GL_FLAT);
        // read in viewport
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
        // return the active buffer
        return this.buffer[0];
    }

    /**
     * Called at the end of the color picking render cycle. This is utilized by
     * the scene.
     *
     * @param gl reference to gl
     */
    public void end(final GL2 gl) {
        // enable attribs
        if (drawState.isAttrib()) {
            gl.glPopAttrib();
        }
        synchronized (offscreenImageSync) {
            // check for no area
            if ((viewport[WIDTH] == 0 || viewport[HEIGHT] == 0)) {
                offscreenImage = null;
                offscreenRaster = null;
                readBackInts = null;
                return;
            }
            // check for wrong offscreen image size
            if (offscreenImage != null && (offscreenImage.getWidth() != viewport[WIDTH] || offscreenImage.getHeight() != viewport[HEIGHT])) {
                offscreenImage = null;
                offscreenRaster = null;
                readBackInts = null;
            }
            // Must now copy pixels from offscreen context into surface
            if (offscreenImage == null && viewport[WIDTH] > 0 && viewport[HEIGHT] > 0) {
                offscreenImage = new BufferedImage(
                        viewport[WIDTH],
                        viewport[HEIGHT],
                        BufferedImage.TYPE_INT_RGB);
                readBackInts = IntBuffer.allocate(viewport[WIDTH] * viewport[HEIGHT]);
            } else {
                readBackInts.clear();
            }

            if (offscreenImage != null) {
                // Save current modes
                gl.glGetIntegerv(GL2GL3.GL_PACK_SWAP_BYTES, swapbytes, 0);
                gl.glGetIntegerv(GL2ES3.GL_PACK_ROW_LENGTH, rowlength, 0);
                gl.glGetIntegerv(GL2ES3.GL_PACK_SKIP_ROWS, skiprows, 0);
                gl.glGetIntegerv(GL2ES3.GL_PACK_SKIP_PIXELS, skippixels, 0);
                gl.glGetIntegerv(GL.GL_PACK_ALIGNMENT, alignment, 0);

                // set state for read
                gl.glReadBuffer(buffer[0]);
                gl.glPixelStorei(GL2GL3.GL_PACK_SWAP_BYTES, GL.GL_FALSE);
                gl.glPixelStorei(GL2ES3.GL_PACK_ROW_LENGTH, viewport[WIDTH]);
                gl.glPixelStorei(GL2ES3.GL_PACK_SKIP_ROWS, 0);
                gl.glPixelStorei(GL2ES3.GL_PACK_SKIP_PIXELS, 0);
                gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);

                // Actually read the pixels.
                gl.glReadPixels(0, 0, viewport[WIDTH], viewport[HEIGHT], GL.GL_BGRA, GL2GL3.GL_UNSIGNED_INT_8_8_8_8_REV, readBackInts);

                // Restore saved modes.
                gl.glPixelStorei(GL2GL3.GL_PACK_SWAP_BYTES, swapbytes[0]);
                gl.glPixelStorei(GL2ES3.GL_PACK_ROW_LENGTH, rowlength[0]);
                gl.glPixelStorei(GL2ES3.GL_PACK_SKIP_ROWS, skiprows[0]);
                gl.glPixelStorei(GL2ES3.GL_PACK_SKIP_PIXELS, skippixels[0]);
                gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, alignment[0]);

                if (readBackInts != null) {
                    // Copy temporary data into raster of BufferedImage for faster
                    // blitting Note that we could avoid this copy in the cases
                    // where !offscreenContext.offscreenImageNeedsVerticalFlip(),
                    // but that's the software rendering path which is very slow
                    // anyway
                    final int[] src = readBackInts.array();
                    offscreenRaster = offscreenImage.getRaster();
                    final int[] dest = ((DataBufferInt) offscreenRaster.getDataBuffer()).getData();

                    int srcPos = 0;
                    int destPos = (viewport[HEIGHT] - 1) * viewport[WIDTH];
                    for (; destPos >= 0; srcPos += viewport[WIDTH], destPos -= viewport[WIDTH]) {
                        System.arraycopy(src, srcPos, dest, destPos, viewport[WIDTH]);
                    }
                }
            }
            // uncomment to write the buffered image used for picking
            //            if (scene.getSceneBuilder().isDebug()) {
            //                final File file = new File("picking.png");
            //                try {
            //                    ImageIO.write(offscreenImage, "png", file);
            //                } catch (final IOException ex) {
            //                    logger.log(Level.WARNING, "Unable to write picking image to file.", ex);
            //                }
            //            }
        }
        //  frame buffer
        if (drawState.isExtraA()) {
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        }
        // texture
        if (drawState.isExtraB()) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        }
        //  depth buffer
        if (drawState.isExtraC()) {
            gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, 0);
        }
        // cleanup
        cleanupBuffers(gl);
        // reset
        drawState.clearValues();
    }

    private void cleanupBuffers(final GL2 gl) {
        if (ids[ID_FRAMEBUFFER] != UNINITIALIZED) {
            gl.glDeleteFramebuffers(1, ids, ID_FRAMEBUFFER);
            ids[ID_FRAMEBUFFER] = UNINITIALIZED;
        }
        if (ids[ID_TEXTURE] != UNINITIALIZED) {
            gl.glDeleteTextures(1, ids, ID_TEXTURE);
            ids[ID_TEXTURE] = UNINITIALIZED;
        }
        if (ids[ID_RENDERBUFFER] != UNINITIALIZED) {
            gl.glDeleteRenderbuffers(1, ids, ID_RENDERBUFFER);
            ids[ID_RENDERBUFFER] = UNINITIALIZED;
        }
    }
}
