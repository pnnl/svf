package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.VboBorder3DUtil;
import gov.pnnl.svf.util.VboShape3DUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * VBO shape factory implementation.
 *
 * @author Arthur Bleeker
 */
public class Text3DVboFactory extends AbstractVboShapeFactory {

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Text3DVboFactory(final Scene scene) {
        super(scene);
    }

    @Override
    public List<VertexBufferObject> createShapeVbos(final Shape shape, final Color color, final boolean texCoords) {
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBackgroundVbos(final Shape shape, final Color color) {
        if (shape instanceof Text3D) {
            return Collections.singletonList(VboShape3DUtil.createShape((Text3D) shape, color, false));
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createBorderVbos(final Shape shape, final Border border, final double thickness, final Color color) {
        if (shape instanceof Text3D) {
            return VboBorder3DUtil.createBorder((Text3D) shape, border, thickness, color);
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createPickingVbos(final Shape shape) {
        if (shape instanceof Text3D) {
            return Collections.singletonList(VboShape3DUtil.createShape((Text3D) shape, null, false));
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
            vbos.add(VboShape3DUtil.createShape((Text3D) shape, color, false));
            if (border != Border.NONE && thickness > 0.0) {
                vbos.addAll(createBorderVbos(shape, border, thickness, color));
            }
            return vbos;
        }
        return Collections.emptyList();
    }

}
