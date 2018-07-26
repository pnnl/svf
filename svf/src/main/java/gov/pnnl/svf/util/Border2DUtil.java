package gov.pnnl.svf.util;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.util.MathUtil;
import gov.pnnl.svf.geometry.Arc2D;
import gov.pnnl.svf.geometry.Chart2D;
import gov.pnnl.svf.geometry.Circle2D;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.RoundedRectangle2D;
import gov.pnnl.svf.geometry.Text2D;
import java.util.logging.Logger;

/**
 * Utility class containing all of the 2D border methods.
 *
 * @author Amelia Bleeker
 */
public class Border2DUtil extends ShapeUtil {

    private static final Logger logger = Logger.getLogger(Border2DUtil.class.getName());

    protected Border2DUtil() {
    }

    /**
     * Draws a border around a point.
     *
     * @param gl        reference to the current GL
     * @param point     the point to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the point border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Point2D point, final Border border, final double thickness) {
        // draw border around a point
        return drawBorder(gl, new Rectangle2D(point.getX(), point.getY(), 1.0, 1.0), border, thickness);
    }

    /**
     * Draws a border around a rectangle.
     *
     * @param gl        reference to the current GL
     * @param rectangle the rectangle to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the rectangle border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Rectangle2D rectangle, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return 0;
        }
        GeometryUtil.drawRectangleBorder(gl, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), border, thickness);
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

    /**
     * Draw a border around a rounded rectangle.
     *
     * @param gl        reference to the current GL
     * @param rectangle the rectangle to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final RoundedRectangle2D rectangle, final Border border, final double thickness) {
        final double roundness = rectangle.getRoundness();
        final double width = rectangle.getWidth() - (roundness * 2.0);
        final double height = rectangle.getHeight() - (roundness * 2.0);
        // top
        if (border.isTop()) {
            GeometryUtil.drawRectangle(gl, rectangle.getX(), rectangle.getY() + (height + roundness + roundness + thickness) / 2.0, width, thickness);
        }
        // right
        if (border.isRight()) {
            GeometryUtil.drawRectangle(gl, rectangle.getX() + (width + roundness + roundness + thickness) / 2.0, rectangle.getY(), thickness, height);
        }
        // bottom
        if (border.isBottom()) {
            GeometryUtil.drawRectangle(gl, rectangle.getX(), rectangle.getY() - (height + roundness + roundness + thickness) / 2.0, width, thickness);
        }
        // left
        if (border.isLeft()) {
            GeometryUtil.drawRectangle(gl, rectangle.getX() - (width + roundness + roundness + thickness) / 2.0, rectangle.getY(), thickness, height);
        }
        // top right arc
        if (border.isTop() && border.isRight()) {
            GeometryUtil.drawArc(gl, rectangle.getX() + width / 2.0, rectangle.getY() + height / 2.0, roundness, roundness + thickness, 0.0, 90.0);
        }
        // bottom right arc
        if (border.isBottom() && border.isRight()) {
            GeometryUtil.drawArc(gl, rectangle.getX() + width / 2.0, rectangle.getY() - height / 2.0, roundness, roundness + thickness, 90.0, 180.0);
        }
        // bottom left arc
        if (border.isBottom() && border.isLeft()) {
            GeometryUtil.drawArc(gl, rectangle.getX() - width / 2.0, rectangle.getY() - height / 2.0, roundness, roundness + thickness, 180.0, 270.0);
        }
        // top left arc
        if (border.isTop() && border.isLeft()) {
            GeometryUtil.drawArc(gl, rectangle.getX() - width / 2.0, rectangle.getY() + height / 2.0, roundness, roundness + thickness, 270.0, 360.0);
        }
        // vertices
        return (4 * (90 + 1)) + (4 * 4);
    }

    /**
     * Draws a border around text.
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
    public static int drawBorder(final GL2 gl, final Text2D text, final Border border, final double thickness) {
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

    /**
     * Draws a border around a chart.
     *
     * @param gl        reference to the current GL
     * @param chart     the chart to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the rectangle border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Chart2D chart, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0) {
            return 0;
        }
        int vertices = 0;
        switch (chart.getStyle()) {
            case NONE:
            case SCATTER:
            case LINE:
            case AREA:
            case BAR:
                GeometryUtil.drawRectangleBorder(gl, chart.getX(), chart.getY(), chart.getWidth(), chart.getHeight(), border, thickness);
                // calculate metrics
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
                break;
            case PIE:
                GeometryUtil.drawCircleBorder(gl, chart.getX(), chart.getY(), 0.5, border, thickness);
                vertices = drawBorder(gl, new Circle2D(chart.getX(), chart.getY(), 0.5), border, thickness);
                break;
            default:
                throw new IllegalArgumentException("Unhandled enum passed to switch statement: " + chart.getStyle());
        }
        return vertices;
    }

    /**
     * Draws a border around a circle.
     *
     * @param gl        reference to the current GL
     * @param circle    the circle to draw
     * @param border    the border, any border is treated as all borders
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the circle border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Circle2D circle, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return 0;
        }
        GeometryUtil.drawCircleBorder(gl, circle.getX(), circle.getY(), circle.getRadius(), border, thickness);
        return CIRCLE_POINTS.length + CIRCLE_POINTS.length;
    }

    /**
     * Draws a border around an arc.
     *
     * @param gl        reference to the current GL
     * @param arc       the arc to draw
     * @param border    the border
     * @param thickness the thickness of the border
     *
     * @return the number of vertices used to draw the arc border
     *
     * @throws NullPointerException     if gl, shape, or border are null
     * @throws IllegalArgumentException if thickness is less than zero
     */
    public static int drawBorder(final GL2 gl, final Arc2D arc, final Border border, final double thickness) {
        if (thickness < 0.0) {
            throw new IllegalArgumentException("thickness");
        }
        if (thickness == 0.0 || border == Border.NONE) {
            return 0;
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
        int vertices = 0;
        // draw the border as multiple arcs
        if (border.containsBorder(Border.ALL)) {
            // top
            vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), outerRadius, outerWidth, thickness));
            // bottom
            vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), innerRadius, innerWidth, thickness));
            // left
            vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, thickness + arc.getArcHeight() + thickness));
            // right
            vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, thickness + arc.getArcHeight() + thickness));
        } else {
            // top
            if (border.isTop()) {
                vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), outerRadius, outerWidth, thickness));
            }
            // bottom
            if (border.isBottom()) {
                vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle(), innerRadius, innerWidth, thickness));
            }
            // left
            if (border.isLeft()) {
                vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, arc.getArcHeight()));
            }
            // right
            if (border.isRight()) {
                vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), arc.getRadius(), thickness, arc.getArcHeight()));
            }
            // top left
            if (border.isTop() || border.isLeft()) {
                vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), outerRadius, thickness, thickness));
            }
            // top right
            if (border.isTop() || border.isRight()) {
                vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), outerRadius, thickness, thickness));
            }
            // bottom left
            if (border.isBottom() || border.isLeft()) {
                vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() - ((arc.getSector() + borderSector) * 0.5), innerRadius, thickness, thickness));
            }
            // bottom right
            if (border.isBottom() || border.isRight()) {
                vertices += Shape2DUtil.drawShape(gl, new Arc2D(arc.getX(), arc.getY(), arc.getAngle() + ((arc.getSector() + borderSector) * 0.5), innerRadius, thickness, thickness));
            }
        }
        return vertices;
    }

}
