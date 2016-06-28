package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Circle2D;
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
public class Circle2DVboFactory extends AbstractVboShapeFactory {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Circle2DVboFactory(final Scene scene) {
        super(scene);
    }

    @Override
    public List<VertexBufferObject> createShapeVbos(final Shape shape, final Color color, final boolean texCoords) {
        if (shape instanceof Circle2D) {
            return Collections.singletonList(VboShape2DUtil.createShape((Circle2D) shape, color, texCoords));
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBorderVbos(final Shape shape, final Border border, final double thickness, final Color color) {
        if (shape instanceof Circle2D) {
            return Collections.singletonList(VboBorder2DUtil.createBorder((Circle2D) shape, border, thickness, color));
        }
        return Collections.emptyList();
    }

}
