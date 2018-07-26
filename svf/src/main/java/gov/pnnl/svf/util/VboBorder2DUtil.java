package gov.pnnl.svf.util;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.constant.DimensionConst;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.util.MathUtil;
import gov.pnnl.svf.geometry.Arc2D;
import gov.pnnl.svf.geometry.Chart2D;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.RoundedRectangle2D;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.vbo.VertexBufferObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * Utility class containing all of the 2D VBO border methods.
 *
 * @author Amelia Bleeker
 */
public abstract class VboBorder2DUtil extends VboUtil implements DimensionConst {

    private static final Logger logger = Logger.getLogger(VboBorder2DUtil.class.getName());

    protected VboBorder2DUtil() {
    }

    /**
     * Creates a border around a point.
     *
     * @param point     the point to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Point2D point, final Border border, final double thickness, final Color color) {
        // draw border around a point
        return createBorder(new Rectangle2D(point.getX(), point.getY(), 1.0, 1.0), border, thickness, color);
    }

    /**
     * Creates a border around a rectangle.
     *
     * @param rectangle the rectangle to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Rectangle2D rectangle, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
        }
        final List<VertexBufferObject> vbos = new ArrayList<>();
        final List<DoubleList> vertices = new ArrayList<>();
        final int[] modes = addRectangleBorder(vertices, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), border, thickness);
        for (int i = 0; i < vertices.size(); i++) {
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(modes[i]).vertices(vertices.get(i).toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        return vbos;
    }

    /**
     * Create a border around a rounded rectangle.
     *
     * @param rectangle the rectangle to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final RoundedRectangle2D rectangle, final Border border, final double thickness, final Color color) {
        final List<VertexBufferObject> vbos = new ArrayList<>();
        final double roundness = rectangle.getRoundness();
        final double width = rectangle.getWidth() - (roundness * 2.0);
        final double height = rectangle.getHeight() - (roundness * 2.0);
        // top
        if (border.isTop()) {
            final DoubleList vertices = new ArrayDoubleList(12);
            final int mode = addRectangle(vertices, rectangle.getX(), rectangle.getY() + (height + roundness + roundness + thickness) / 2.0, width, thickness);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // right
        if (border.isRight()) {
            final DoubleList vertices = new ArrayDoubleList(12);
            final int mode = addRectangle(vertices, rectangle.getX() + (width + roundness + roundness + thickness) / 2.0, rectangle.getY(), thickness, height);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // bottom
        if (border.isBottom()) {
            final DoubleList vertices = new ArrayDoubleList(12);
            final int mode = addRectangle(vertices, rectangle.getX(), rectangle.getY() - (height + roundness + roundness + thickness) / 2.0, width, thickness);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // left
        if (border.isLeft()) {
            final DoubleList vertices = new ArrayDoubleList(12);
            final int mode = addRectangle(vertices, rectangle.getX() - (width + roundness + roundness + thickness) / 2.0, rectangle.getY(), thickness, height);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // top right arc
        if (border.isTop() && border.isRight()) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, rectangle.getX() + width / 2.0, rectangle.getY() + height / 2.0, roundness, roundness + thickness, 0.0, 90.0);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // bottom right arc
        if (border.isBottom() && border.isRight()) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, rectangle.getX() + width / 2.0, rectangle.getY() - height / 2.0, roundness, roundness + thickness, 90.0, 180.0);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // bottom left arc
        if (border.isBottom() && border.isLeft()) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, rectangle.getX() - width / 2.0, rectangle.getY() - height / 2.0, roundness, roundness + thickness, 180.0, 270.0);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        // top left arc
        if (border.isTop() && border.isLeft()) {
            final DoubleList vertices = new ArrayDoubleList();
            final int mode = addArc(vertices, rectangle.getX() - width / 2.0, rectangle.getY() + height / 2.0, roundness, roundness + thickness, 270.0, 360.0);
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        return vbos;
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
    public static List<VertexBufferObject> createBorder(final Text2D text, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
        }
        final List<VertexBufferObject> vbos = new ArrayList<>();
        final List<DoubleList> vertices = new ArrayList<>();
        final int[] modes = addRectangleBorder(vertices, text.getX(), text.getY(), text.getWidth(), text.getHeight(), border, thickness);
        for (int i = 0; i < vertices.size(); i++) {
            final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(modes[i]).vertices(vertices.get(i).toArray());
            vbos.add(buildVbo(vbo, NORMAL, null, color));
        }
        return vbos;
    }

    /**
     * Creates a border around a chart.
     *
     * @param chart     the chart to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Chart2D chart, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
        }
        switch (chart.getStyle()) {
            case NONE:
            case SCATTER:
            case LINE:
            case AREA:
            case BAR: {
                final List<VertexBufferObject> vbos = new ArrayList<>();
                final List<DoubleList> vertices = new ArrayList<>();
                final int[] modes = addRectangleBorder(vertices, chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight(), border, thickness);
                for (int i = 0; i < vertices.size(); i++) {
                    final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(modes[i]).vertices(vertices.get(i).toArray());
                    vbos.add(buildVbo(vbo, NORMAL, null, color));
                }
                return vbos;
            }
            case PIE: {
                final List<VertexBufferObject> vbos = new ArrayList<>();
                final DoubleList vertices = new ArrayDoubleList();
                final int mode = addCircleBorder(vertices, chart.getX(), chart.getY(), 0.5, border, thickness);
                final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
                vbos.add(buildVbo(vbo, NORMAL, null, color));
                return vbos;
            }
            default:
                throw new IllegalArgumentException("Unhandled enum passed to switch statement: " + chart.getStyle());
        }
    }

    /**
     * Creates a border around a circle.
     *
     * @param circle    the circle to draw
     * @param border    the border, any border is treated as all borders
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer object
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static VertexBufferObject createBorder(final Circle2D circle, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return null;
        }
        final DoubleList vertices = new ArrayDoubleList();
        final int mode = addCircleBorder(vertices, circle.getX(), circle.getY(), circle.getRadius(), border, thickness);
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct().vertexDimension(TWO_D).mode(mode).vertices(vertices.toArray());
        return buildVbo(vbo, NORMAL, null, color);
    }

    /**
     * Creates a border around an arc.
     *
     * @param arc       the arc to draw
     * @param border    the border
     * @param thickness the thickness of the border
     * @param color     the optional color
     *
     * @return the vertex buffer objects
     *
     * @throws NullPointerException     if shape or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static List<VertexBufferObject> createBorder(final Arc2D arc, final Border border, final double thickness, final Color color) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return Collections.emptyList();
        }
        // calculate metrics
        double borderSector = (180.0 * thickness) / (Math.PI * arc.getRadius());
        borderSector = Math.max(0.0, borderSector);
        double innerRadius = arc.getRadius() - ((arc.getArcHeight() + thickness) * 0.5);
        innerRadius = Math.max(0.0, innerRadius);
        double outerRadius = arc.getRadius() + ((arc.getArcHeight() + thickness) * 0.5);
        outerRadius = Math.max(0.0, outerRadius);
        double innerWidth = MathUtil.scale(innerRadius, arc.getRadius(), 0.0, arc.getArcWidth(), 0.0);
        innerWidth = Math.max(0.0, innerWidth);
        double outerWidth = MathUtil.scale(outerRadius, arc.getRadius(), 0.0, arc.getArcWidth(), 0.0);
        outerWidth = Math.max(0.0, outerWidth);
        // draw the border as multiple arcs
        final List<VertexBufferObject> vbos = new ArrayList<>();
        if (border.containsBorder(Border.ALL)) {
            // top
            vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), outerRadius, outerWidth, thickness), color, false));
            // bottom
            vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), innerRadius, innerWidth, thickness), color, false));
            // left
            vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, thickness + arc.getArcHeight() + thickness), color, false));
            // right
            vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, thickness + arc.getArcHeight() + thickness), color, false));
        } else {
            // top
            if (border.isTop()) {
                vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), outerRadius, outerWidth, thickness), color, false));
            }
            // bottom
            if (border.isBottom()) {
                vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), innerRadius, innerWidth, thickness), color, false));
            }
            // left
            if (border.isLeft()) {
                vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, arc.getArcHeight()), color, false));
            }
            // right
            if (border.isRight()) {
                vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, arc.getArcHeight()), color, false));
            }
            // top left
            if (border.isTop() || border.isLeft()) {
                vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), outerRadius, thickness, thickness), color, false));
            }
            // top right
            if (border.isTop() || border.isRight()) {
                vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), outerRadius, thickness, thickness), color, false));
            }
            // bottom left
            if (border.isBottom() || border.isLeft()) {
                vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), innerRadius, thickness, thickness), color, false));
            }
            // bottom right
            if (border.isBottom() || border.isRight()) {
                vbos.add(VboShape2DUtil.createShape(new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), innerRadius, thickness, thickness), color, false));
            }
        }
        return vbos;
    }

}
