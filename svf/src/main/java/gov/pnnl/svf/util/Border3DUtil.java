/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.util;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Text3D;
import java.util.logging.Logger;

/**
 * Utility class containing all of the 3D border methods.
 *
 * @author D3X573
 */
public class Border3DUtil extends ShapeUtil {

    private static final Logger logger = Logger.getLogger(Border3DUtil.class.getName());

    protected Border3DUtil() {
    }

    /**
     * Draws a border around a rectangle.
     *
     * @param gl        reference to the current GL
     * @param text      the text to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the rectangle border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Text3D text, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return 0;
        }
        GeometryUtil.drawRectangleBorder(gl, text.getX(), text.getY(), text.getWidth(), text.getHeight(), border, thickness);
        // calculate metrics
        int vertices = 0;
        if (border.containsBorder(Border.ALL)) {
            vertices += 16;
        } else {
            vertices += border.isTop() ? 4 : 0;
            vertices += border.isBottom() ? 4 : 0;
            vertices += border.isLeft() ? 4 : 0;
            vertices += border.isRight() ? 4 : 0;
            vertices += border.isLeft() || border.isTop() ? 4 : 0;
            vertices += border.isLeft() || border.isBottom() ? 4 : 0;
            vertices += border.isRight() || border.isTop() ? 4 : 0;
            vertices += border.isRight() || border.isBottom() ? 4 : 0;
        }
        return vertices;
    }

}
