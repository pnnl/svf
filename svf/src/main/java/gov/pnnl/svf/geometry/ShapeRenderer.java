package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.picking.ColorPickingSupport;
import javax.media.opengl.GL2;

/**
 * Renderer for a geometry shape type.
 *
 * @author Arthur Bleeker
 */
public interface ShapeRenderer {

    /**
     * Called before rendering while the context is current. Logic that
     * shouldn't be in the call list can be placed here.
     *
     * @param gl    reference to the current GL
     * @param shape the shape to draw
     *
     * @throws NullPointerException     if gl or shape are null
     * @throws IllegalArgumentException if shape is not supported
     */
    void prepare(GL2 gl, Shape shape);

    /**
     * Draw the shape. This method gets cached to a call list.
     *
     * @param gl    reference to the current GL
     * @param shape the shape to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException     if gl or shape are null
     * @throws IllegalArgumentException if shape is not supported
     */
    int drawShape(GL2 gl, Shape shape);

    /**
     * Draw a background for this shape. This method gets cached to a call list.
     * The majority of shapes don't utilize a background so this method may do
     * nothing.
     *
     * @param gl    reference to the current GL
     * @param shape the shape to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException     if gl or shape are null
     * @throws IllegalArgumentException if shape is not supported
     */
    int drawBackground(GL2 gl, Shape shape);

    /**
     * Draw a border around the shape. This method gets cached to a call list.
     *
     * @param gl        reference to the current GL
     * @param shape     the shape to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the shape border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if shape is not supported or thickness
     *                                  is less than zero
     */
    int drawBorder(GL2 gl, Shape shape, Border border, double thickness);

    /**
     * Draw the shape using simple geometry for picking. This method gets cached
     * to a call list.
     *
     * @param gl    reference to the current GL
     * @param shape the shape to draw
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException     if gl or shape are null
     * @throws IllegalArgumentException if shape is not supported
     */
    int pickingDrawShape(GL2 gl, Shape shape);

    /**
     * Draw the shape using simple geometry for the specified items. This method
     * gets called during the color picking draw method.
     * <p/>
     * Override <code>useColorPicking</code> and return true to utilize this
     * method.
     *
     * @param gl      reference to the current GL
     * @param shape   the shape to draw
     * @param support the color picking support object
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException     if gl or shape are null
     * @throws IllegalArgumentException if shape is not supported
     */
    int colorPickingDrawShape(GL2 gl, Shape shape, ColorPickingSupport support);

    /**
     * Draw the shape using simple geometry for the specified items. This method
     * gets called during the color picking draw method.
     * <p/>
     * Override <code>useColorPicking</code> and return true to utilize this
     * method.
     *
     * @param gl        reference to the current GL
     * @param shape     the shape to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param support   the color picking support object
     *
     * @return the number of vertices used to draw the shape
     *
     * @throws NullPointerException     if gl or shape are null
     * @throws IllegalArgumentException if shape is not supported
     */
    int colorPickingDrawShape(GL2 gl, Shape shape, Border border, double thickness, ColorPickingSupport support);

    /**
     * Override and return true to utilize the
     * <code>colorPickingDrawShape</code> method.
     *
     * @return true if using color picking
     */
    boolean useColorPicking();
}
