package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Shape2DUtil;
import com.jogamp.opengl.GL2;

/**
 * Shape renderer implementation.
 *
 * @author Arthur Bleeker
 */
public class Arc2DRenderer extends AbstractShapeRenderer {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Arc2DRenderer(final Scene scene) {
        super(scene);
    }

    @Override
    public int drawShape(final GL2 gl, final Shape shape) {
        if (shape instanceof Arc2D) {
            return Shape2DUtil.drawShape(gl, (Arc2D) shape);
        }
        return 0;
    }

    @Override
    public int drawBorder(final GL2 gl, final Shape shape, final Border border, final double thickness) {
        if (shape instanceof Arc2D) {
            return Shape2DUtil.drawBorder(gl, (Arc2D) shape, border, thickness);
        }
        return 0;
    }

}
