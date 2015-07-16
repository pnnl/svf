package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Vbo2DUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * VBO shape factory implementation.
 *
 * @author Arthur Bleeker
 */
public class Text2DVboFactory extends AbstractVboShapeFactory {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Text2DVboFactory(final Scene scene) {
        super(scene);
    }

    @Override
    public List<VertexBufferObject> createShapeVbos(final Shape shape, final Color color, final boolean texCoords) {
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBackgroundVbos(final Shape shape, final Color color) {
        if (shape instanceof Text2D) {
            return Collections.singletonList(Vbo2DUtil.createShape((Text2D) shape, color, false));
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBorderVbos(final Shape shape, final Border border, final double thickness, final Color color) {
        if (shape instanceof Text2D) {
            return Vbo2DUtil.createBorder((Text2D) shape, border, thickness, color);
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createPickingVbos(final Shape shape) {
        if (shape instanceof Text2D) {
            return Collections.singletonList(Vbo2DUtil.createShape((Text2D) shape, null, false));
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createColorPickingVbos(final Shape shape, final ColorPickingSupport support) {
        final Color color = support.getMapping(support.getActor());
        if (color != null) {
            return createBackgroundVbos(shape, color);
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createColorPickingVbos(final Shape shape, final Border border, final double thickness, final ColorPickingSupport support) {
        final Color color = support.getMapping(support.getActor());
        if (color != null) {
            final List<VertexBufferObject> vbos = new ArrayList<>();
            vbos.add(Vbo2DUtil.createShape((Text2D) shape, color, false));
            vbos.addAll(createBorderVbos(shape, border, thickness, color));
            return vbos;
        }
        return Collections.emptyList();
    }

}
