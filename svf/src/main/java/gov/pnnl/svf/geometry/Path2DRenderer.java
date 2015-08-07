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
public class Path2DRenderer extends AbstractShapeRenderer {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Path2DRenderer(final Scene scene) {
        super(scene);
    }

    @Override
    public int drawShape(final GL2 gl, final Shape shape) {
        if (shape instanceof Path2D) {
            return Shape2DUtil.drawShape(gl, (Path2D) shape);
        }
        return 0;
    }

    @Override
    public int drawBorder(final GL2 gl, final Shape shape, final Border border, final double thickness) {
        throw new IllegalArgumentException("shape");
    }

}
