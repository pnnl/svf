package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Chart2D;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Vbo2DUtil;
import java.util.Collections;
import java.util.List;

/**
 * VBO shape factory implementation.
 *
 * @author Arthur Bleeker
 */
public class Chart2DVboFactory extends AbstractVboShapeFactory {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Chart2DVboFactory(final Scene scene) {
        super(scene);
    }

    @Override
    public List<VertexBufferObject> createShapeVbos(final Shape shape, final Color color, final boolean texCoords) {
        if (shape instanceof Chart2D) {
            return Vbo2DUtil.createShape((Chart2D) shape, color);
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBorderVbos(final Shape shape, final Border border, final double thickness, final Color color) {
        if (shape instanceof Chart2D) {
            return Vbo2DUtil.createBorder((Chart2D) shape, border, thickness, color);
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBackgroundVbos(final Shape shape, final Color color) {
        if (shape instanceof Chart2D) {
            final Chart2D chart = (Chart2D) shape;
            switch (chart.getStyle()) {
                // rectangle charts
                case NONE:
                case SCATTER:
                case LINE:
                case AREA:
                case BAR:
                    return Collections.singletonList(Vbo2DUtil.createShape(new Rectangle2D(chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight()), color, false));
                // circular charts
                case PIE:
                    return Collections.emptyList();
                default:
                    return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createPickingVbos(final Shape shape) {
        if (shape instanceof Chart2D) {
            final Chart2D chart = (Chart2D) shape;
            switch (chart.getStyle()) {
                // rectangle charts
                case NONE:
                case SCATTER:
                case LINE:
                case AREA:
                case BAR:
                    return Collections.singletonList(Vbo2DUtil.createShape(new Rectangle2D(chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight()), null, false));
                // circular charts
                case PIE:
                    return Collections.singletonList(Vbo2DUtil.createShape(new Circle2D(chart.getX(), chart.getY(), Math.max(chart.getWidth(), chart.getHeight()) / 2.0), null, false));
                default:
                    return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createColorPickingVbos(final Shape shape, final ColorPickingSupport support) {
        if (shape instanceof Chart2D) {
            final Color color = support.getMapping(support.getActor());
            if (color != null) {
                final Chart2D chart = (Chart2D) shape;
                switch (chart.getStyle()) {
                    // rectangle charts
                    case NONE:
                    case SCATTER:
                    case LINE:
                    case AREA:
                    case BAR:
                        return Collections.singletonList(Vbo2DUtil.createShape(new Rectangle2D(chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight()), color, false));
                    // circular charts
                    case PIE:
                        return Collections.singletonList(Vbo2DUtil.createShape(new Circle2D(chart.getX(), chart.getY(), Math.max(chart.getWidth(), chart.getHeight()) / 2.0), color, false));
                    default:
                        return Collections.emptyList();
                }
            }
        }
        return Collections.emptyList();
    }

}
