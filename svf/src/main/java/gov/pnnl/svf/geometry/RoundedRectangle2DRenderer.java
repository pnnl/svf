package gov.pnnl.svf.geometry;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Shape2DUtil;

/**
 * Shape renderer implementation.
 *
 * @author Arthur Bleeker
 */
public class RoundedRectangle2DRenderer extends AbstractShapeRenderer {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public RoundedRectangle2DRenderer(final Scene scene) {
        super(scene);
    }

    @Override
    public int drawShape(final GL2 gl, final Shape shape) {
        if (shape instanceof RoundedRectangle2D) {
            return Shape2DUtil.drawShape(gl, (RoundedRectangle2D) shape);
        }
        return 0;
    }

    @Override
    public int drawBorder(final GL2 gl, final Shape shape, final Border border, final double thickness) {
        if (shape instanceof RoundedRectangle2D) {
            return Shape2DUtil.drawBorder(gl, (RoundedRectangle2D) shape, border, thickness);
        }
        return 0;
    }

}
