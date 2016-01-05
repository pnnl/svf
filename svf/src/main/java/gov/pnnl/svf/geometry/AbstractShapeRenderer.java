package gov.pnnl.svf.geometry;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ShapeUtil;

/**
 * Abstract base class for a shape renderer.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractShapeRenderer implements ShapeRenderer {

    protected final Scene scene;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    protected AbstractShapeRenderer(final Scene scene) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
    }

    @Override
    public void prepare(final GL2 gl, final Shape shape) {
        // default implementation does nothing
    }

    @Override
    public int drawBackground(final GL2 gl, final Shape shape) {
        // default implementation does nothing
        return 0;
    }

    @Override
    public int pickingDrawShape(final GL2 gl, final Shape shape) {
        return drawShape(gl, shape);
    }

    @Override
    public int colorPickingDrawShape(final GL2 gl, final Shape shape, final ColorPickingSupport support) {
        final Color color = support.getMapping(support.getActor());
        if (color != null) {
            ShapeUtil.pushColor(gl, color);
            final int v = pickingDrawShape(gl, shape);
            ShapeUtil.popColor(gl, color);
            return v;
        }
        return 0;
    }

    @Override
    public int colorPickingDrawShape(final GL2 gl, final Shape shape, final Border border, final double thickness, final ColorPickingSupport support) {
        final Color color = support.getMapping(support.getActor());
        if (color != null) {
            ShapeUtil.pushColor(gl, color);
            int v = pickingDrawShape(gl, shape);
            v += drawBorder(gl, shape, border, thickness);
            ShapeUtil.popColor(gl, color);
            return v;
        }
        return 0;
    }

    @Override
    public boolean useColorPicking() {
        return false;
    }

}
