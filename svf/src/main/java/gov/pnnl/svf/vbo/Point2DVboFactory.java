package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.VboBorder2DUtil;
import gov.pnnl.svf.util.VboShape2DUtil;
import java.util.Collections;
import java.util.List;

/**
 * VBO shape factory implementation.
 *
 * @author Arthur Bleeker
 */
public class Point2DVboFactory extends AbstractVboShapeFactory {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Point2DVboFactory(final Scene scene) {
        super(scene);
    }

    @Override
    public List<VertexBufferObject> createShapeVbos(final Shape shape, final Color color, final boolean texCoords) {
        if (shape instanceof Point2D) {
            return Collections.singletonList(VboShape2DUtil.createShape((Point2D) shape, color));
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBorderVbos(final Shape shape, final Border border, final double thickness, final Color color) {
        if (shape instanceof Point2D) {
            return VboBorder2DUtil.createBorder((Point2D) shape, border, thickness, color);
        }
        return Collections.emptyList();
    }

}
