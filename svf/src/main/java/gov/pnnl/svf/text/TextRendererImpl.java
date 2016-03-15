package gov.pnnl.svf.text;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureData;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.InvisibleActor;
import gov.pnnl.svf.core.collections.Tuple2;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.RectanglePacker;
import gov.pnnl.svf.core.geometry.SimpleRectanglePacker;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Text;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.texture.Texture2dSupport;
import gov.pnnl.svf.texture.TextureRegionMapSupport;
import gov.pnnl.svf.texture.TextureRegionSupport;
import gov.pnnl.svf.util.GeometryUtil;
import gov.pnnl.svf.util.LayoutUtil;
import gov.pnnl.svf.util.TextUtil;
import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * Implementation of text that utilizes textures and regions for individual
 * characters.
 *
 * @author Arthur Bleeker
 */
public class TextRendererImpl extends AbstractText {

    private static final Integer UNINITIALIZED = -1;
    private static final float RELATIVE_PADDING = 1.0f;
    private static final double[] NORMAL = new double[]{0.0, 0.0, 1.0};
    //    private static final double[][] TEX_COORDS = new double[][]{{0.0, 1.0}, {0.0, 0.0}, {1.0, 0.0}, {1.0, 1.0}};
    private final Actor actor;
    private final Map<Integer, Tuple2<Integer, Rectangle>> map = new HashMap<>();
    private final List<TextureRegionMapSupport> regions = Collections.synchronizedList(new ArrayList<TextureRegionMapSupport>());
    private final List<Texture2dSupport> textures = Collections.synchronizedList(new ArrayList<Texture2dSupport>());
    private Map<Integer, Rectangle> cached;
    private Integer index = UNINITIALIZED;
    private RectanglePacker packer;
    private BufferedImage image;
    private Graphics2D graphics;
    private int size = UNINITIALIZED;

    /**
     * Constructor
     *
     * @param scene       reference to the scene
     * @param font        the font for the text renderer
     * @param antiAliased true for antialiased text
     * @param mipMaps     true for mip maps
     * @param fractional  true for fractional metrics
     * @param smoothing   true for smoothing
     */
    public TextRendererImpl(final Scene scene, final Font font, final boolean antiAliased, final boolean mipMaps, final boolean fractional,
                            final boolean smoothing) {
        super(scene, font, antiAliased, mipMaps, fractional, smoothing);
        actor = new InvisibleActor(scene);
        actor.setDrawingPass(DrawingPass.SCENE_INTERFACE_OVERLAY);
        scene.add(actor);
    }

    @Override
    public void dispose() {
        super.dispose();
        getScene().remove(actor);
        actor.dispose();
        createGraphics(null);
    }

    @Override
    public void initialize(final GL2 gl, final GLUgl2 glu) {
        synchronized (textures) {
            for (final Texture2dSupport texture : textures) {
                if (!texture.isInitialized()) {
                    texture.initialize(gl, glu);
                }
            }
        }
        synchronized (this) {
            state.setInitialized(true);
        }
    }

    @Override
    public void unInitialize(final GL2 gl, final GLUgl2 glu) {
        synchronized (textures) {
            for (final Texture2dSupport texture : textures) {
                if (texture.isInitialized()) {
                    texture.unInitialize(gl, glu);
                }
            }
        }
        synchronized (this) {
            state.setInitialized(false);
        }
    }

    @Override
    public void prepare(final GL2 gl, final String text) {
        // ensure it's initialized
        if (index.equals(UNINITIALIZED)) {
            newTextImage(gl);
            initialize(gl);
        }
        for (int i = 0; i < text.length(); i++) {
            findCharacter(gl, Integer.valueOf(text.charAt(i)), true);
        }
        createRegion();
        createTexture(gl);
    }

    @Override
    public void draw(final GL2 gl, final Text text) {
        if (text instanceof Text2D) {
            final Text2D t = (Text2D) text;
            draw(gl, t.getText(), t.getX(), t.getY(), 0.0, t.getWidth(), t.getHeight(), t.getOffsets(), 1.0);
        } else if (text instanceof Text3D) {
            final Text3D t = (Text3D) text;
            draw(gl, t.getText(), t.getX(), t.getY(), t.getZ(), t.getWidth(), t.getHeight(), t.getOffsets(), Text3D.TEXT_SCALE);
        }
    }

    @Override
    public void draw(final GL2 gl, final Text text, final Alignment alignment) {
        if (text instanceof Text2D) {
            final Text2D t = (Text2D) text;
            final Point2D offset = LayoutUtil.findOrigin(t, alignment);
            draw(gl, t.getText(), offset.getX(), offset.getY(), 0.0, t.getWidth(), t.getHeight(), t.getOffsets(), 1.0);
        } else if (text instanceof Text3D) {
            final Text3D t = (Text3D) text;
            final Point2D offset = LayoutUtil.findOrigin(t, alignment);
            draw(gl, t.getText(), offset.getX(), offset.getY(), t.getZ(), t.getWidth(), t.getHeight(), t.getOffsets(), Text3D.TEXT_SCALE);
        }
    }

    @Override
    public void draw(final GL2 gl, final Text text, final Shape area, final Alignment alignment) {
        if (text instanceof Text2D) {
            final Text2D t = (Text2D) text;
            final Point2D offset = LayoutUtil.findOrigin(t, area, alignment);
            draw(gl, t.getText(), offset.getX(), offset.getY(), 0.0, t.getWidth(), t.getHeight(), t.getOffsets(), 1.0);
        } else if (text instanceof Text3D) {
            final Text3D t = (Text3D) text;
            final Point2D offset = LayoutUtil.findOrigin(t, area, alignment);
            draw(gl, t.getText(), offset.getX(), offset.getY(), t.getZ(), t.getWidth(), t.getHeight(), t.getOffsets(), Text3D.TEXT_SCALE);
        }
    }

    private void draw(final GL2 gl, final String str, final double x, final double y, final double z, final double w, final double h, final DoubleList offsets,
                      final double scale) {
        // ensure it's initialized
        if (index.equals(UNINITIALIZED)) {
            newTextImage(gl);
            initialize(gl);
        }
        // set the start x vertex position
        final double yBottom = y - (h / 2.0);
        final double yTop = y + (h / 2.0);
        double xLeft = 0.0;
        // verts
        final double verts[][] = new double[][]{
            {xLeft, yBottom, z},
            {xLeft, yTop, z},
            {xLeft, yTop, z},
            {xLeft, yBottom, z}};
        final double texCoords[][] = new double[4][2];
        // setup drawing environment
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_TEXTURE_BIT | GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL2.GL_TRANSFORM_BIT);
        gl.glDisable(GLLightingFunc.GL_LIGHTING);
        gl.glDisable(GLLightingFunc.GL_COLOR_MATERIAL);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, Color.TRANSPARENT.toRgbArray(), 0);
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA);
        // draw the characters from left to right
        // changed to utilize texture coordinates instead of texture transformation
        //        TextureRegionMapSupport region;
        Texture2dSupport texture = null;
        for (int i = 0; i < str.length(); i++) {
            final Integer c = (int) str.charAt(i);
            final Tuple2<Integer, Rectangle> item = findCharacter(gl, c, false);
            //                region = regions.get(item.getFirst());
            final Texture2dSupport temp = textures.get(item.getFirst());
            if (texture != temp) {
                if (texture != null) {
                    texture.endDraw(gl, null, null);
                }
                texture = temp;
                texture.draw(gl, null, null);
            }
            final Rectangle region = item.getSecond();
            // verts
            xLeft = x - (w / 2.0) + offsets.get(i);
            verts[0][0] = xLeft;
            verts[1][0] = xLeft;
            xLeft += region.getWidth() * scale;
            verts[2][0] = xLeft;
            verts[3][0] = xLeft;
            // texture coords
            // texture can't be null at this point
            texCoords[0][0] = (double) region.getX() / (double) texture.getWidth();
            texCoords[0][1] = (double) (region.getY() + region.getHeight()) / (double) texture.getHeight();
            texCoords[1][0] = (double) region.getX() / (double) texture.getWidth();
            texCoords[1][1] = (double) region.getY() / (double) texture.getHeight();
            texCoords[2][0] = (double) (region.getX() + region.getWidth()) / (double) texture.getWidth();
            texCoords[2][1] = (double) region.getY() / (double) texture.getHeight();
            texCoords[3][0] = (double) (region.getX() + region.getWidth()) / (double) texture.getWidth();
            texCoords[3][1] = (double) (region.getY() + region.getHeight()) / (double) texture.getHeight();
            // draw
            //                region.setTextureRegion(gl, c);
            GeometryUtil.drawPolygon(gl, verts, texCoords, NORMAL);
            //                region.unsetTextureRegion(gl);
        }
        if (texture != null) {
            texture.endDraw(gl, null, null);
        }
        gl.glPopAttrib();
    }

    private Tuple2<Integer, Rectangle> findCharacter(final GL2 gl, final Integer c, final boolean defer) {
        // look for the character
        Tuple2<Integer, Rectangle> item = map.get(c);
        if (item == null) {
            // character wasn't found
            // test to see if it will fit in the current regions
            final Graphics2D graphics = getGraphics();
            final FontMetrics metrics = graphics.getFontMetrics();
            final int width = (int) Math.ceil(metrics.getStringBounds(String.valueOf(c), graphics).getWidth());
            final int height = metrics.getHeight();
            final int widthPadding = (int) (width * RELATIVE_PADDING);
            final int heightPadding = (int) (height * RELATIVE_PADDING);
            Point point = packer.pack(width + widthPadding, height + heightPadding);
            // update the region
            if (point == null) {
                // if defer then create previous region and image
                if (defer) {
                    createRegion();
                    createTexture(gl);
                }
                // didn't fit so make a new image
                newTextImage(gl);
                point = packer.pack(width + widthPadding, height + heightPadding);
            }
            final Rectangle rec = new Rectangle((int) point.getX() + widthPadding / 2, (int) point.getY() + heightPadding / 2, width, height);
            graphics.drawString(new String(new char[]{(char) c.intValue()}), rec.getX(), rec.getY() + (int) (rec.getHeight() * 0.8));
            cached.put(c, rec);
            if (!defer) {
                createRegion();
            }
            // create a new texture based on the updated regions
            if (!defer) {
                createTexture(gl);
            }
            // place in map and return
            item = new Tuple2<>(index, rec);
            map.put(c, item);
        }
        return item;
    }

    private void newTextImage(final GL2 gl) {
        if (size == UNINITIALIZED) {
            // set the texture size
            final int[] maxTextureSize = new int[1];
            gl.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
            size = Math.min(maxTextureSize[0], TextUtil.MAX_FONT_REGION_SIZE);
        }
        cached = new HashMap<>();
        packer = new SimpleRectanglePacker(size, size);
        final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D graphics = createGraphics(image);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, isAntiAliased()
                                                                        ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                                                                        : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, isFractional()
                                                                        ? RenderingHints.VALUE_FRACTIONALMETRICS_ON
                                                                        : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, size, size);
        graphics.setComposite(AlphaComposite.Src);
        graphics.setColor(java.awt.Color.WHITE);
        graphics.setFont(font);
        index++;
    }

    private void initialize(final GL2 gl) {
        final Set<Character> set = new HashSet<>(TextUtil.DEFAULT_CHARACTERS);
        for (final Character c : set) {
            findCharacter(gl, Integer.valueOf(c), true);
        }
        createRegion();
        createTexture(gl);
    }

    private void createRegion() {
        if (regions.size() > index) {
            final TextureRegionMapSupport dispose = regions.get(index);
            dispose.dispose();
        }
        // ensure last region and texture get created
        final TextureRegionMapSupport region = TextureRegionSupport.newInstance(new InvisibleActor(scene), cached, size, size);
        regions.add(index, region);
    }

    private void createTexture(final GL2 gl) {
        if (textures.size() > index) {
            final Texture2dSupport dispose = textures.get(index);
            dispose.dispose();
        }
        final boolean mipmaps = isMipMaps();
        final TextureData data = new AWTTextureData(gl == null ? GLProfile.getDefault() : gl.getGLProfile(), GL2.GL_INTENSITY, 0, mipmaps, image);
        final Texture2dSupport texture = Texture2dSupport.newInstance(actor, data);
        texture.initialize(gl, null);
        textures.add(index, texture);
    }

    private Graphics2D createGraphics(final BufferedImage image) {
        synchronized (this) {
            if (this.graphics != null) {
                this.graphics.dispose();
            }
            if (this.image != null) {
                this.image.flush();
            }
            this.image = image;
            if (this.image != null) {
                this.graphics = this.image.createGraphics();
            }
            return this.graphics;
        }
    }

    public Graphics2D getGraphics() {
        synchronized (this) {
            return graphics;
        }
    }

}
