package gov.pnnl.svf.texture;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.geometry.Rectangle;
import java.util.ArrayList;
import java.util.Map;

/**
 * Support object for mapping specific texture regions.
 *
 * @author Arthur Bleeker
 */
public class TextureRegionMapSupport extends TextureRegionSupport {

    private final Map<Integer, Rectangle> regions;

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor       The actor that owns this support instance.
     * @param regions     The map of regions that relate to pixel coordinates in
     *                    a 2d texture.
     * @param totalWidth  The total width of the regions.
     * @param totalHeight The total height of the regions.
     */
    protected TextureRegionMapSupport(final Actor actor, final Map<Integer, Rectangle> regions, final int totalWidth, final int totalHeight) {
        super(actor, totalWidth, totalHeight, new ArrayList<>(regions.values()));
        this.regions = regions;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TextureRegionMapSupport other = (TextureRegionMapSupport) obj;
        if (regions != other.regions && (regions == null || !regions.equals(other.regions))) {
            return false;
        }
        return true;
    }

    /**
     * The collection returned from this method should never be modified.
     *
     * @return the map of regions that correspond to texture coordinates
     */
    public Map<Integer, Rectangle> getRegions() {
        return regions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (regions != null ? regions.hashCode() : 0);
        return hash;
    }

    /**
     * Set the region as the currently active texture region.
     *
     * @param gl     Reference to GL
     * @param region The region to set as active.
     */
    public void setTextureRegion(final GL2 gl, final int region) {
        super.setTextureRegion(gl, regions.get(region));
    }

    @Override
    public String toString() {
        return "TextureRegionMapSupport{" + "regions=" + regions + '}';
    }
}
