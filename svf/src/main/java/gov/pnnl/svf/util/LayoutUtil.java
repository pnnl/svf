package gov.pnnl.svf.util;

import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Shape2D;
import gov.pnnl.svf.geometry.Shape3D;

/**
 * Utility for working with text strings.
 *
 * @author Amelia Bleeker
 */
public class LayoutUtil {

    /**
     * Constructor is private to prevent instantiation of a static helper class.
     */
    private LayoutUtil() {
        super();
    }

    /**
     * Find the drawing origin offset for the specified alignment. This will
     * return zero for the default origin of <code>CENTER</code>.
     *
     * @param shape     the shape
     * @param alignment the alignment
     *
     * @return the origin offset for the alignment
     */
    public static Point2D findOrigin(final Shape shape, final Alignment alignment) {
        if (shape == null) {
            throw new NullPointerException("shape");
        }
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }
        if (alignment == Alignment.CENTER) {
            return Point2D.ZERO;
        }
        if (shape instanceof Shape2D) {
            final Shape2D s = (Shape2D) shape;
            return LayoutUtil.findOrigin(s.getX(), s.getY(), s.getWidth(), s.getHeight(), alignment);
        } else if (shape instanceof Shape3D) {
            final Shape3D s = (Shape3D) shape;
            return LayoutUtil.findOrigin(s.getX(), s.getY(), s.getWidth(), s.getHeight(), alignment);
        } else {
            return Point2D.ZERO;
        }
    }

    /**
     * Find the drawing origin offset for the given alignment inside of the
     * area.
     *
     * @param shape     the text to align
     * @param area      the area to align in
     * @param alignment the alignment of the text
     *
     * @return the relative start point
     *
     * @throws NullPointerException if any arguments are null
     */
    public static Point2D findOrigin(final Shape shape, final Shape area, final Alignment alignment) {
        if (shape == null) {
            throw new NullPointerException("shape");
        }
        if (area == null) {
            throw new NullPointerException("area");
        }
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }
        if (shape instanceof Shape2D && area instanceof Shape2D) {
            final Shape2D s = (Shape2D) shape;
            final Shape2D a = (Shape2D) area;
            final double x = s.getX();
            final double y = s.getY();
            final double h = s.getHeight();
            final double w = s.getWidth();
            final double ax = a.getX();
            final double ay = a.getY();
            final double ah = a.getHeight();
            final double aw = a.getWidth();
            return LayoutUtil.findOrigin(x, y, w, h, ax, ay, aw, ah, alignment);
        } else if (shape instanceof Shape3D && area instanceof Shape3D) {
            final Shape3D s = (Shape3D) shape;
            final Shape3D a = (Shape3D) area;
            final double x = s.getX();
            final double y = s.getY();
            final double h = s.getHeight();
            final double w = s.getWidth();
            final double ax = a.getX();
            final double ay = a.getY();
            final double ah = a.getHeight();
            final double aw = a.getWidth();
            return LayoutUtil.findOrigin(x, y, w, h, ax, ay, aw, ah, alignment);
        } else {
            throw new IllegalArgumentException("shape and area must be same type");
        }
    }

    private static Point2D findOrigin(final double x, final double y, final double w, final double h, final double ax, final double ay, final double aw,
                                      final double ah, final Alignment alignment) throws IllegalArgumentException {
        double fx = x;
        double fy = y;
        switch (alignment) {
            // fx align center
            case CENTER:
                fx += ax;
                fy += ay;
                break;
            case TOP:
                fx += ax;
                fy += ay + ah / 2.0 - h / 2.0;
                break;
            case BOTTOM:
                fx += ax;
                fy += ay - ah / 2.0 + h / 2.0;
                break;
            // fx align on the left side of the tefxt
            case LEFT:
                fx += ax - aw / 2.0 + w / 2.0;
                fy += ay;
                break;
            case LEFT_TOP:
                fx += ax - aw / 2.0 + w / 2.0;
                fy += ay + ah / 2.0 - h / 2.0;
                break;
            case LEFT_BOTTOM:
                fx += ax - aw / 2.0 + w / 2.0;
                fy += ay - ah / 2.0 + h / 2.0;
                break;
            // fx align on the right side of the tefxt
            case RIGHT:
                fx += ax + aw / 2.0 - w / 2.0;
                fy += ay;
                break;
            case RIGHT_TOP:
                fx += ax + aw / 2.0 - w / 2.0;
                fy += ay + ah / 2.0 - h / 2.0;
                break;
            case RIGHT_BOTTOM:
                fx += ax + aw / 2.0 - w / 2.0;
                fy += ay - ah / 2.0 + h / 2.0;
                break;
            default:
                throw new IllegalArgumentException("alignment");
        }
        return new Point2D(fx, fy);
    }

    private static Point2D findOrigin(final double x, final double y, final double w, final double h, final Alignment alignment) {
        switch (alignment) {
            // x align center
            case CENTER:
                return Point2D.ZERO;
            case TOP:
                return new Point2D(x, y - h / 2.0);
            case BOTTOM:
                return new Point2D(x, y + h / 2.0);
            case LEFT:
                return new Point2D(x + w / 2.0, y);
            case LEFT_TOP:
                return new Point2D(x + w / 2.0, y - h / 2.0);
            case LEFT_BOTTOM:
                return new Point2D(x + w / 2.0, y + h / 2.0);
            case RIGHT:
                return new Point2D(x - w / 2.0, y);
            case RIGHT_TOP:
                return new Point2D(x - w / 2.0, y - h / 2.0);
            case RIGHT_BOTTOM:
                return new Point2D(x - w / 2.0, y + h / 2.0);
            default:
                throw new IllegalArgumentException("alignment");
        }
    }
}
