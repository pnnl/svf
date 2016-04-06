package gov.pnnl.svf.awt.geometry;

import gov.pnnl.svf.geometry.Rectangle2D;
import java.io.Serializable;

/**
 * Immutable shape class that provides an x and y coordinate along with the
 * width and height. This rectangle should be used for scene components and is
 * in OpenGL coordinates. The x an y will be located at the center of the
 * rectangle. This version just provides a convenience constructor for AWT and
 * Swing.
 *
 * @author Arthur Bleeker
 *
 */
public class AwtRectangle2D extends Rectangle2D implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Convenience Constructor
     * <p>
     * This constructor will create a rectangle with the origin moved from the
     * top left to the center.
     *
     * @param rectangle awt rectangle object
     *
     * @throws NullPointerException if rectangle argument is null
     */
    public AwtRectangle2D(final java.awt.Rectangle rectangle) {
        super(rectangle.getX() + (rectangle.getWidth() * 0.5), rectangle.getHeight() - rectangle.getY() + (rectangle.getHeight() * 0.5), rectangle.getWidth(),
              rectangle.getHeight());
    }

    /**
     * Convenience Constructor
     * <p>
     * This constructor will create a rectangle with the origin moved from the
     * top left to the center.
     *
     * @param rectangle awt rectangle object
     *
     * @throws NullPointerException if rectangle argument is null
     */
    public AwtRectangle2D(final java.awt.geom.Rectangle2D rectangle) {
        super(rectangle.getX() + (rectangle.getWidth() * 0.5), rectangle.getHeight() - rectangle.getY() + (rectangle.getHeight() * 0.5), rectangle.getWidth(),
              rectangle.getHeight());
    }
}
