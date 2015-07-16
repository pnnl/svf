package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Vbo2DUtil;
import java.util.Collections;
import java.util.List;

/**
 * VBO shape factory implementation.
 *
 * @author Arthur Bleeker
 */
public class Rectangle2DVboFactory extends AbstractVboShapeFactory {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Rectangle2DVboFactory(final Scene scene) {
        super(scene);
    }

    @Override
    public List<VertexBufferObject> createShapeVbos(final Shape shape, final Color color, final boolean texCoords) {
        if (shape instanceof Rectangle2D) {
            return Collections.singletonList(Vbo2DUtil.createShape((Rectangle2D) shape, color, texCoords));
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBorderVbos(final Shape shape, final Border border, final double thickness, final Color color) {
        if (shape instanceof Rectangle2D) {
            return Vbo2DUtil.createBorder((Rectangle2D) shape, border, thickness, color);
        }
        return Collections.emptyList();
    }

}
