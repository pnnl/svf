package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.Scene;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for a VBO shape factory.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractVboShapeFactory implements VboShapeFactory {

    protected final Scene scene;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    protected AbstractVboShapeFactory(final Scene scene) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
    }

    @Override
    public List<VertexBufferObject> createBackgroundVbos(final Shape shape, final Color color) {
        // default implementation does nothing
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createPickingVbos(final Shape shape) {
        return createShapeVbos(shape, null, false);
    }

    @Override
    public List<VertexBufferObject> createColorPickingVbos(final Shape shape, final ColorPickingSupport support) {
        final Color color = support.getMapping(support.getActor());
        if (color != null) {
            return createShapeVbos(shape, color, false);
        }
        return Collections.emptyList();
    }

    @Override
    public List<VertexBufferObject> createColorPickingVbos(final Shape shape, final Border border, final double thickness, final ColorPickingSupport support) {
        final Color color = support.getMapping(support.getActor());
        if (color != null) {
            final List<VertexBufferObject> vbos = new ArrayList<>();
            vbos.addAll(createColorPickingVbos(shape, support));
            if (border != Border.NONE && thickness > 0.0) {
                vbos.addAll(createBorderVbos(shape, border, thickness, color));
            }
            return vbos;
        }
        return Collections.emptyList();
    }

}
