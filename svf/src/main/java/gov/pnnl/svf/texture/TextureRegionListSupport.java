package gov.pnnl.svf.texture;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import java.util.List;

/**
 * Support object for creating a list of texture regions.
 *
 * @author Amelia Bleeker
 */
public class TextureRegionListSupport extends TextureRegionSupport implements Drawable {

    /**
     * String representation of a field in this object.
     */
    public static final String CURRENT_REGION = "currentRegion";
    private final List<Rectangle> regions;
    private int currentRegion = 0;

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor       The actor that owns this support instance.
     * @param regions     The list of regions that relate to pixel coordinates
     *                    in a 2d texture.
     * @param totalWidth  The total width of the regions.
     * @param totalHeight The total height of the regions.
     */
    protected TextureRegionListSupport(final Actor actor, final List<Rectangle> regions, final int totalWidth, final int totalHeight) {
        super(actor, totalWidth, totalHeight, regions);
        if (regions == null) {
            throw new NullPointerException("regions");
        }
        if (regions.isEmpty()) {
            throw new IllegalArgumentException("regions");
        }
        this.regions = regions;
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
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        setTextureRegion(gl, getRegions().get(getCurrentRegion()));
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        unsetTextureRegion(gl);
    }

    /**
     *
     * @return the current region
     */
    public int getCurrentRegion() {
        synchronized (this) {
            return currentRegion;
        }
    }

    /**
     * The collection returned from this method should never be modified.
     *
     * @return the list of regions that correspond to texture coordinates
     */
    public List<Rectangle> getRegions() {
        return regions;
    }

    /**
     * Decrement the current region by one. Decrementing will loop through the
     * regions.
     */
    public void decrementCurrentRegion() {
        final int old;
        final int current;
        synchronized (this) {
            old = currentRegion;
            if (currentRegion == 0) {
                // loop back to end
                current = regions.size() - 1;
            } else {
                // decrement
                current = currentRegion - 1;
            }
            currentRegion = current;
        }
        getPropertyChangeSupport().firePropertyChange(CURRENT_REGION, old, current);
    }

    /**
     * Increment the current region by one. Incrementing will loop through the
     * regions.
     */
    public void incrementCurrentRegion() {
        final int old;
        final int current;
        synchronized (this) {
            old = currentRegion;
            if (currentRegion == regions.size() - 1) {
                // loop back to start
                current = 0;
            } else {
                // increment
                current = currentRegion + 1;
            }
            currentRegion = current;
        }
        getPropertyChangeSupport().firePropertyChange(CURRENT_REGION, old, current);
    }

    /**
     *
     * @param currentRegion the current region
     */
    public void setCurrentRegion(final int currentRegion) {
        if (currentRegion < 0) {
            throw new IllegalArgumentException("currentRegion");
        }
        if (currentRegion >= regions.size()) {
            throw new IllegalArgumentException("currentRegion");
        }
        int old;
        synchronized (this) {
            old = this.currentRegion;
            this.currentRegion = currentRegion;
        }
        getPropertyChangeSupport().firePropertyChange(CURRENT_REGION, old, currentRegion);
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "TextureRegionListSupport{" + "regions=" + regions + "currentRegion=" + currentRegion + '}';
        }
    }
}
