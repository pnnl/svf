package gov.pnnl.svf.util;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureData;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.InvisibleActor;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.RectanglePacker;
import gov.pnnl.svf.core.geometry.SimpleRectanglePacker;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneExt;
import gov.pnnl.svf.texture.Texture2dSupport;
import gov.pnnl.svf.texture.TextureRegionMapSupport;
import gov.pnnl.svf.texture.TextureRegionSupport;
import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Utility for working with text strings.
 *
 * @author Arthur Bleeker
 */
public class TextUtil {

    private static final Logger logger = Logger.getLogger(TextUtil.class.getName());
    /**
     * Maximum size for a font region texture.
     */
    public static final int MAX_FONT_REGION_SIZE = 2048;
    /**
     * Set of characters used by default for creating text regions and textures.
     */
    public static final Set<Character> DEFAULT_CHARACTERS;
    private static final float RELATIVE_PADDING = 1.5f;

    static {
        final Set<Character> characters = new HashSet<>();
        findCharacters(Character.UnicodeBlock.BASIC_LATIN, characters);
        // additional characters take up too much room and are rarely necessary
//        findCharacters(Character.UnicodeBlock.LATIN_1_SUPPLEMENT, characters);
//        findCharacters(Character.UnicodeBlock.LATIN_EXTENDED_A, characters);
//        findCharacters(Character.UnicodeBlock.LATIN_EXTENDED_B, characters);
//        findCharacters(Character.UnicodeBlock.IPA_EXTENSIONS, characters);
//        findCharacters(Character.UnicodeBlock.SPACING_MODIFIER_LETTERS, characters);
//        findCharacters(Character.UnicodeBlock.GENERAL_PUNCTUATION, characters);
//        findCharacters(Character.UnicodeBlock.SUPERSCRIPTS_AND_SUBSCRIPTS, characters);
//        findCharacters(Character.UnicodeBlock.CURRENCY_SYMBOLS, characters);
//        findCharacters(Character.UnicodeBlock.COMBINING_MARKS_FOR_SYMBOLS, characters);
//        findCharacters(Character.UnicodeBlock.LETTERLIKE_SYMBOLS, characters);
//        findCharacters(Character.UnicodeBlock.NUMBER_FORMS, characters);
//        findCharacters(Character.UnicodeBlock.ARROWS, characters);
//        findCharacters(Character.UnicodeBlock.MATHEMATICAL_OPERATORS, characters);
//        findCharacters(Character.UnicodeBlock.MISCELLANEOUS_TECHNICAL, characters);
//        findCharacters(Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS, characters);
        DEFAULT_CHARACTERS = Collections.unmodifiableSet(new HashSet<>(characters));
    }

    /**
     * Constructor is private to prevent instantiation of a static helper class.
     */
    private TextUtil() {
        super();
    }

    /**
     * Attempt to truncate the text so it fits within the given width. Ellipse
     * will be utilized if able.
     *
     * @param text  the source text
     * @param width the maximum width
     *
     * @return the truncated text
     */
    public static Text2D fitText(final Text2D text, final double width) {
        if (width < 0.0) {
            throw new IllegalArgumentException("width");
        }
        if (text.getWidth() <= width || text.getText().isEmpty()) {
            return text;
        }
        // determine how many characters must be removed
        for (int length = text.getText().length() - 1; length > 0; length--) {
            final double w = text.getOffsets().get(length);
            if (w <= width) {
                final String str;
                if (length > 3) {
                    str = StringUtils.abbreviate(text.getText(), length);
                } else {
                    str = text.getText().substring(0, length);
                }
                final Text2D ret = new Text2D(text.getX(), text.getY(), text.getFont(), str);
                // verify
                if (ret.getWidth() <= width) {
                    return ret;
                }
            }
        }
        // no characters fit
        return new Text2D(text.getX(), text.getY(), text.getFont(), "");
    }

    /**
     * Attempt to truncate the text so it fits within the given width. Ellipse
     * will be utilized if able.
     *
     * @param text  the source text
     * @param width the maximum width
     *
     * @return the truncated text
     */
    public static Text3D fitText(final Text3D text, final double width) {
        if (width < 0.0) {
            throw new IllegalArgumentException("width");
        }
        if (text.getWidth() <= width || text.getText().isEmpty()) {
            return text;
        }
        // determine how many characters must be removed
        for (int length = text.getText().length() - 1; length > 0; length--) {
            final double w = text.getOffsets().get(length);
            if (w <= width) {
                final String str;
                if (length > 3) {
                    str = StringUtils.abbreviate(text.getText(), length);
                } else {
                    str = text.getText().substring(0, length);
                }
                final Text3D ret = new Text3D(text.getX(), text.getY(), text.getZ(), text.getFont(), str);
                // verify
                if (ret.getWidth() <= width) {
                    return ret;
                }
            }
        }
        // no characters fit
        return new Text3D(text.getX(), text.getY(), text.getZ(), text.getFont(), "");
    }

    /**
     * Creates a {@link TextureRegionSupport} object for the specified font.
     * Will return null if generating the regions fails.
     *
     * @param scene The scene to create the font region for
     * @param font  The font used for measuring the regions
     *
     * @return the support object or null if this method fails
     *
     * @throws NullPointerException if scene is null
     */
    public static TextureRegionMapSupport createFontRegions(final Scene scene, final Font font) {
        return TextUtil.createFontRegions(scene, font, MAX_FONT_REGION_SIZE, null);
    }

    /**
     * Creates a {@link TextureRegionSupport} object for the specified font.
     * Will return null if generating the regions fails.
     *
     * @param scene The scene to create the font region for
     * @param font  The font used for measuring the regions
     * @param size  the maximum region size (width and height)
     *
     * @return the support object or null if this method fails
     *
     * @throws NullPointerException if scene is null
     */
    public static TextureRegionMapSupport createFontRegions(final Scene scene, final Font font, final int size) {
        return TextUtil.createFontRegions(scene, font, size, null);
    }

    /**
     * Creates a {@link TextureRegionSupport} object for the specified font.
     * Will return null if generating the regions fails.
     *
     * @param scene      The scene to create the font region for
     * @param font       The font used for measuring the regions
     * @param size       the maximum region size (width and height)
     * @param characters the characters to use in this font region or null for
     *                   the set (0 to 128)
     *
     * @return the support object or null if this method fails
     *
     * @throws NullPointerException if scene is null
     */
    public static TextureRegionMapSupport createFontRegions(final Scene scene, final Font font, final int size, final Set<Character> characters) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        return TextUtil.createFontRegions(new InvisibleActor(scene), font, size, characters);
    }

    /**
     * Creates a {@link TextureRegionSupport} object for the specified font.
     * Will return null if generating the regions fails.
     *
     * @param actor The actor to create the font region for
     * @param font  The font used for measuring the regions
     *
     * @return the support object or null if this method fails
     *
     * @throws NullPointerException if actor is null
     */
    public static TextureRegionMapSupport createFontRegions(final Actor actor, final Font font) {
        return TextUtil.createFontRegions(actor, font, MAX_FONT_REGION_SIZE, null);
    }

    /**
     * Creates a {@link TextureRegionSupport} object for the specified font.
     * Will return null if generating the regions fails.
     *
     * @param actor The actor to create the font region for
     * @param font  The font used for measuring the regions
     * @param size  the maximum region size (width and height)
     *
     * @return the support object or null if this method fails
     *
     * @throws NullPointerException if actor is null
     */
    public static TextureRegionMapSupport createFontRegions(final Actor actor, final Font font, final int size) {
        return TextUtil.createFontRegions(actor, font, size, null);
    }

    /**
     * Creates a {@link TextureRegionSupport} object for the specified font.
     * Will return null if generating the regions fails.
     *
     * @param actor      The actor to create the font region for
     * @param font       The font used for measuring the regions
     * @param size       the maximum region size (width and height)
     * @param characters the characters to use in this font region or null for
     *                   the set (0 to 128)
     *
     * @return the support object or null if this method fails
     *
     * @throws NullPointerException if actor is null
     */
    public static TextureRegionMapSupport createFontRegions(final Actor actor, final Font font, final int size, Set<Character> characters) {
        if (actor == null) {
            throw new NullPointerException("actor");
        }
        if (font == null) {
            throw new NullPointerException("font");
        }
        // just assign the parameter if none is supplied
        if (characters == null) {
            characters = new HashSet<>(DEFAULT_CHARACTERS);
        }
        // create a base buffered image for creating a graphics device to use
        // for metrics
        final BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // set the font for metrics
        graphics.setFont(font);
        final FontMetrics metrics = graphics.getFontMetrics();
        // create a packing algorithm object
        final RectanglePacker packer = new SimpleRectanglePacker(size, size);
        // create a texture region support object
        final Map<Integer, Rectangle> regions = new HashMap<>();
        for (final Character c : characters) {
            final int height = metrics.getHeight();
            final int width = metrics.charWidth(c);
            final int widthPadding = (int) (width * RELATIVE_PADDING);
            final int heightPadding = (int) (height * RELATIVE_PADDING);
            final Point point = packer.pack(width + widthPadding, height + heightPadding);
            if (point == null) {
                logger.log(Level.WARNING, "Unable to create a font region object. Can't fit all the chars in a {0} x {0} texture.", size);
                // can't fit all the chars in a texture
                return null;
            }
            final Rectangle rec = new Rectangle((int) point.getX() + widthPadding / 2, (int) point.getY() + heightPadding / 2, width, height);
            regions.put(Integer.valueOf(c), rec);
            // logger.log(Level.INFO, "Char: {0} is at {1}", new
            // Object[]{(char) c, rec});
        }
        final TextureRegionMapSupport support = TextureRegionSupport.newInstance(actor, regions,
                                                                                 (int) packer.getActualArea().getWidth(), (int) packer.getActualArea().getHeight());
        return support;
    }

    /**
     * Creates a texture with a series of characters that are used to draw text.
     *
     * @param regions    the region object for this font texture
     * @param font       The font used for measuring the regions
     * @param mipmaps    true to generate mipmaps
     * @param antialias  true to use antialiased text
     * @param fractional true to use fraction metrics
     *
     * @return the support object or null if this method fails
     */
    public static Texture2dSupport createFontTexture(final TextureRegionMapSupport regions, final Font font,
                                                     final boolean mipmaps, final boolean antialias, final boolean fractional) {
        if (regions == null) {
            throw new NullPointerException("regions");
        }
        if (font == null) {
            throw new NullPointerException("font");
        }
        final BufferedImage image = TextUtil.createFontImage(regions, Color.TRANSPARENT, Color.WHITE, font, antialias, fractional);
        final Actor actor = regions.getActor();
        final Scene scene = actor.getScene();
        final SceneExt extended = scene.getExtended();
        final GL gl = extended.getGL();
        final TextureData data = new AWTTextureData(gl == null ? GLProfile.getDefault() : gl.getGLProfile(), 0, 0, mipmaps, image);
        return Texture2dSupport.newInstance(actor, data);
    }

    /**
     * Find all of the characters that belong to the specified block.
     *
     * @param block      the unicode block
     * @param characters collection to add the characters to
     */
    public static void findCharacters(final Character.UnicodeBlock block, final Collection<Character> characters) {
        if (block == null) {
            throw new NullPointerException("block");
        }
        if (characters == null) {
            throw new NullPointerException("characters");
        }
        for (int codePoint = Character.MIN_CODE_POINT; codePoint <= Character.MAX_CODE_POINT; codePoint++) {
            if (!Character.isISOControl(codePoint) && block == Character.UnicodeBlock.of(codePoint)) {
                characters.add((char) codePoint);
            }
        }
    }

    /**
     * Find all of the characters that belong to the specified block.
     *
     * @param block the unicode block
     *
     * @return the newly created set
     */
    public static Set<Character> findCharacters(final Character.UnicodeBlock block) {
        final Set<Character> characters = new HashSet<>();
        TextUtil.findCharacters(block, characters);
        return characters;
    }

    private static BufferedImage createFontImage(final TextureRegionMapSupport regions, final Color background, final Color color, final Font font,
                                                 final boolean antialias, final boolean fractional) {
        final BufferedImage image = new BufferedImage(regions.getTotalWidth(), regions.getTotalHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        final Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (antialias) {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        if (fractional) {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
        // ensure image is clear
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setComposite(AlphaComposite.Src);
        // clear the background
        if (background.getAlpha() > 0.0f) {
            graphics.setColor(background.toAwtColor());
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        }
        // set the color and font that is going to be drawn with
        graphics.setColor(color.toAwtColor());
        graphics.setFont(font);
        // draw the characters
        for (final Map.Entry<Integer, Rectangle> entry : regions.getRegions().entrySet()) {
            graphics.drawString(new String(new char[]{(char) ((int) entry.getKey())}), entry.getValue().getX(), entry.getValue().getY()
                                                                                                                + (int) (entry.getValue().getHeight() * 0.8));
        }
        graphics.dispose();
        return image;
    }
}
