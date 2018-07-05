package gov.pnnl.svf.geometry;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Shape2DUtil;

/**
 * Shape renderer implementation.
 *
 * @author Amelia Bleeker
 */
public class RectangleRenderer extends AbstractShapeRenderer {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public RectangleRenderer(final Scene scene) {
        super(scene);
    }

    @Override
    public int drawShape(final GL2 gl, final Shape shape) {
        if (shape instanceof Rectangle) {
            return Shape2DUtil.drawShape(gl, (Rectangle) shape);
        }
        return 0;
    }

    @Override
    public int drawBorder(final GL2 gl, final Shape shape, final Border border, final double thickness) {
        throw new IllegalArgumentException("shape");
    }

}
