package gov.pnnl.svf.vbo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.picking.ColorPickingSupport;
import java.util.List;

/**
 * Factory for creating geometry VBOs.
 *
 * @author Arthur Bleeker
 */
public interface VboShapeFactory {

    /**
     * Create the shape VBO.
     *
     * @param shape     the shape
     * @param color     the optional color
     * @param texCoords true to include texture coordinates
     *
     * @return the VBOs
     *
     * @throws NullPointerException     if shape is null
     * @throws IllegalArgumentException if shape is not supported
     */
    List<VertexBufferObject> createShapeVbos(Shape shape, Color color, boolean texCoords);

    /**
     * Create a background VBO for this shape. The majority of shapes don't
     * utilize a background so this method may do nothing.
     *
     * @param shape the shape
     * @param color the optional color
     *
     * @return the VBOs
     *
     * @throws NullPointerException     if shape is null
     * @throws IllegalArgumentException if shape is not supported
     */
    List<VertexBufferObject> createBackgroundVbos(Shape shape, Color color);

    /**
     * Create a border VBO for this shape.
     *
     * @param shape     the shape to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the VBOs
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if shape is not supported or thickness
     *                                  is less than zero
     */
    List<VertexBufferObject> createBorderVbos(Shape shape, Border border, double thickness, Color color);

    /**
     * Create a VBO for the shape using simple geometry for picking.
     *
     * @param shape the shape to draw
     *
     * @return the VBOs
     *
     * @throws NullPointerException     if shape is null
     * @throws IllegalArgumentException if shape is not supported
     */
    List<VertexBufferObject> createPickingVbos(Shape shape);

    /**
     * Create a VBO for the shape using simple geometry for the specified items.
     * <p>
     * Override <code>useColorPicking</code> and return true to utilize this
     * method.
     *
     * @param shape   the shape to draw
     * @param support the color picking support object
     *
     * @return the VBOs
     *
     * @throws NullPointerException     if shape is null
     * @throws IllegalArgumentException if shape is not supported
     */
    List<VertexBufferObject> createColorPickingVbos(Shape shape, ColorPickingSupport support);

    /**
     * Create a VBO for the shape using simple geometry for the specified items.
     * <p>
     * Override <code>useColorPicking</code> and return true to utilize this
     * method.
     *
     * @param shape     the shape to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param support   the color picking support object
     *
     * @return the VBOs
     *
     * @throws NullPointerException     if shape is null
     * @throws IllegalArgumentException if shape is not supported
     */
    List<VertexBufferObject> createColorPickingVbos(Shape shape, Border border, double thickness, ColorPickingSupport support);

}
