package gov.pnnl.svf.swt.geometry;

import gov.pnnl.svf.geometry.Rectangle2D;
import java.io.Serializable;

/**
 * Immutable shape class that provides an x and y coordinate along with the
 * width and height. This rectangle should be used for scene components and is
 * in OpenGL coordinates. The x an y will be located at the center of the
 * rectangle. This version just provides a convenience constructor for SWT.
 *
 * @author Arthur Bleeker
 *
 */
public class SwtRectangle2D extends Rectangle2D implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Convenience Constructor
     * <p>
     * This constructor will create a rectangle with the origin moved from the
     * top left to the center.
     *
     * @param rectangle swt rectangle object
     *
     * @throws NullPointerException if rectangle argument is null
     */
    public SwtRectangle2D(final org.eclipse.swt.graphics.Rectangle rectangle) {
        super(rectangle.x + (rectangle.width * 0.5), rectangle.height - rectangle.y + (rectangle.height * 0.5), rectangle.width, rectangle.height);
    }
}
