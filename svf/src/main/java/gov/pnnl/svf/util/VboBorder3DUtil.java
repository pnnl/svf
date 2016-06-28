/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.util;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.vbo.VertexBufferObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * Utility class containing all of the 3D VBO border methods.
 *
 * @author D3X573
 */
public class VboBorder3DUtil extends VboUtil {

    private static final Logger logger = Logger.getLogger(VboBorder3DUtil.class.getName());

    protected VboBorder3DUtil() {
    }

    /**
     * Creates a border around text.
     *
     * @param text      the text to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Text3D text, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
        }
        final List<VertexBufferObject> vbos = new ArrayList<>();
        final List<DoubleList> vertices = new ArrayList<>();
        final int[] modes = addRectangleBorder(vertices, text.getX(), text.getY(), text.getZ(), text.getWidth(), text.getHeight(), border, thickness);
        for (int i = 0; i < vertices.size(); i++) {
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(THREE_D).mode(modes[i]).vertices(vertices.get(i).toArray()).texCoordDimension(TWO_D);
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        return vbos;
    }

}
