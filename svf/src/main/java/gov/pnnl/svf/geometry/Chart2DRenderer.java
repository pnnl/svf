package gov.pnnl.svf.geometry;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Border2DUtil;
import gov.pnnl.svf.util.GeometryUtil;
import gov.pnnl.svf.util.Shape2DUtil;

/**
 * Shape renderer implementation.
 *
 * @author Amelia Bleeker
 */
public class Chart2DRenderer extends AbstractShapeRenderer {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Chart2DRenderer(final Scene scene) {
        super(scene);
    }

    @Override
    public int drawBackground(final GL2 gl, final Shape shape) {
        if (shape instanceof Chart2D) {
            final Chart2D chart = (Chart2D) shape;
            switch (chart.getStyle()) {
                // rectangle charts
                case NONE:
                case SCATTER:
                case LINE:
                case AREA:
                case BAR:
                    GeometryUtil.drawRectangle(gl, chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight());
                    return 4;
                // circular charts
                case PIE:
                    return 0;
                default:
                    return 0;
            }
        }
        return 0;
    }

    @Override
    public int drawShape(final GL2 gl, final Shape shape) {
        if (shape instanceof Chart2D) {
            return Shape2DUtil.drawShape(gl, (Chart2D) shape);
        }
        return 0;
    }

    @Override
    public int drawBorder(final GL2 gl, final Shape shape, final Border border, final double thickness) {
        if (shape instanceof Chart2D) {
            return Border2DUtil.drawBorder(gl, (Chart2D) shape, border, thickness);
        }
        return 0;
    }

    @Override
    public int pickingDrawShape(final GL2 gl, final Shape shape) {
        if (shape instanceof Chart2D) {
            final Chart2D chart = (Chart2D) shape;
            switch (chart.getStyle()) {
                // rectangle charts
                case NONE:
                case SCATTER:
                case LINE:
                case AREA:
                case BAR:
                    GeometryUtil.drawRectangle(gl, chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight());
                    return 4;
                // circular charts
                case PIE:
                    GeometryUtil.drawCircle(gl, chart.getX(), chart.getY(), Math.max(chart.getWidth(), chart.getHeight()) / 2.0);
                    return 360 + 1;
                default:
                    return 0;
            }
        }
        return 0;
    }

}
