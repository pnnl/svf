package gov.pnnl.svf.texture;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.support.AbstractSupport;
import java.util.List;
import java.util.Map;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * Support object for mapping specific texture regions.
 *
 * @author Arthur Bleeker
 */
public abstract class TextureRegionSupport extends AbstractSupport<Object> {

    private final int totalWidth;
    private final int totalHeight;
    private final int minWidth;
    private final int maxWidth;
    private final int minHeight;
    private final int maxHeight;

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor       The actor that owns this support instance.
     * @param totalWidth  The total width of the regions.
     * @param totalHeight The total height of the regions.
     * @param regions     list of the regions used for generating metrics
     */
    protected TextureRegionSupport(final Actor actor, final int totalWidth, final int totalHeight, final List<Rectangle> regions) {
        super(actor);
        if (totalWidth <= 0) {
            throw new IllegalArgumentException("totalWidth");
        }
        if (totalHeight <= 0) {
            throw new IllegalArgumentException("totalHeight");
        }
        if (regions == null) {
            throw new NullPointerException("regions");
        }
        int minW = regions.isEmpty() ? 0 : regions.get(0).getWidth();
        int maxW = minW;
        int minH = regions.isEmpty() ? 0 : regions.get(0).getHeight();
        int maxH = minH;
        for (int i = 1; i < regions.size(); i++) {
            final Rectangle region = regions.get(i);
            minW = Math.min(minW, region.getWidth());
            maxW = Math.max(maxW, region.getWidth());
            minH = Math.min(minH, region.getHeight());
            maxH = Math.max(maxH, region.getHeight());
        }
        if (minW < 0) {
            throw new IllegalArgumentException("minWidth");
        }
        if (maxW < minW) {
            throw new IllegalArgumentException("maxWidth");
        }
        if (minH < 0) {
            throw new IllegalArgumentException("minHeight");
        }
        if (maxH < minH) {
            throw new IllegalArgumentException("maxHeight");
        }
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        minWidth = minW;
        maxWidth = maxW;
        minHeight = minH;
        maxHeight = maxH;
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor       The actor that owns this support instance.
     * @param regions     The list of regions that relate to pixel coordinates
     *                    in a 2d texture.
     * @param totalWidth  The total width of the regions.
     * @param totalHeight The total height of the regions.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static TextureRegionListSupport newInstance(final Actor actor, final List<Rectangle> regions, final int totalWidth, final int totalHeight) {
        final TextureRegionListSupport instance = new TextureRegionListSupport(actor, regions, totalWidth, totalHeight);
        actor.add(instance);
        return instance;
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor       The actor that owns this support instance.
     * @param regions     The map of regions that relate to pixel coordinates in
     *                    a 2d texture.
     * @param totalWidth  The total width of the regions.
     * @param totalHeight The total height of the regions.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static TextureRegionMapSupport newInstance(final Actor actor, final Map<Integer, Rectangle> regions, final int totalWidth, final int totalHeight) {
        final TextureRegionMapSupport instance = new TextureRegionMapSupport(actor, regions, totalWidth, totalHeight);
        actor.add(instance);
        return instance;
    }

    /**
     * @return the total height
     */
    public int getTotalHeight() {
        return totalHeight;
    }

    /**
     * @return the total width
     */
    public int getTotalWidth() {
        return totalWidth;
    }

    /**
     * @return the minimum width in the set of regions
     */
    public int getMinWidth() {
        return minWidth;
    }

    /**
     * @return the maximum width in the set of regions
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * @return the minimum height in the set of regions
     */
    public int getMinHeight() {
        return minHeight;
    }

    /**
     * @return the maximum height in the set of regions
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Set the region as the currently active texture region.
     *
     * @param gl        Reference to GL
     * @param rectangle The region to set as active.
     */
    public void setTextureRegion(final GL2 gl, final Rectangle rectangle) {
        drawState.clearValues();
        // get the region
        double transX = 0.0;
        double transY = 0.0;
        double scaleX = 1.0;
        double scaleY = 1.0;
        if (rectangle != null) {
            transX = (double) rectangle.getX() / (double) totalWidth;
            transY = (double) rectangle.getY() / (double) totalHeight;
            scaleX = (double) rectangle.getWidth() / (double) totalWidth;
            scaleY = (double) rectangle.getHeight() / (double) totalHeight;
        }
        // set the texture transform
        gl.glPushAttrib(GL2.GL_TRANSFORM_BIT);
        gl.glMatrixMode(GL.GL_TEXTURE);
        gl.glPushMatrix();
        drawState.setMatrixTexture();
        gl.glTranslated(transX, transY, 0.0);
        gl.glScaled(scaleX, scaleY, 1.0);
        gl.glPopAttrib();
    }

    @Override
    public String toString() {
        return "TextureRegionSupport{" + "totalWidth=" + totalWidth + "totalHeight=" + totalHeight + '}';
    }

    /**
     * Unset the currently active texture region.
     *
     * @param gl Reference to GL
     */
    public void unsetTextureRegion(final GL2 gl) {
        if (drawState.isMatrixTexture()) {
            // unset the texture transform
            gl.glPushAttrib(GL2.GL_TRANSFORM_BIT);
            gl.glMatrixMode(GL.GL_TEXTURE);
            gl.glPopMatrix();
            gl.glPopAttrib();
        }
        drawState.clearValues();
    }
}
