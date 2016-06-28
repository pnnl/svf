package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Polygon2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.VboShape2DUtil;
import java.util.Collections;
import java.util.List;

/**
 * VBO shape factory implementation.
 *
 * @author Arthur Bleeker
 */
public class Polygon2DVboFactory extends AbstractVboShapeFactory {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Polygon2DVboFactory(final Scene scene) {
        super(scene);
    }

    @Override
    public List<VertexBufferObject> createShapeVbos(final Shape shape, final Color color, final boolean texCoords) {
        if (shape instanceof Polygon2D) {
            return VboShape2DUtil.createShape((Polygon2D) shape, color);
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBorderVbos(final Shape shape, final Border border, final double thickness, final Color color) {
        throw new IllegalArgumentException("shape");
    }

}
