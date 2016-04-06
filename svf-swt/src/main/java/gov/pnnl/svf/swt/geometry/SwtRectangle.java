package gov.pnnl.svf.swt.geometry;

import gov.pnnl.svf.geometry.Rectangle;
import java.io.Serializable;

/**
 * Immutable shape class that provides an x and y coordinate along with the
 * width and height. This rectangle should be used for user interface components
 * and is in OpenGL viewport coordinates. The x and y will be located at the
 * bottom left of the rectangle. This version just provides a convenience
 * constructor for SWT.
 *
 * @author Arthur Bleeker
 *
 */
public class SwtRectangle extends Rectangle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Convenience Constructor
     * <p>
     * This constructor will create a rectangle with the origin moved from the
     * top left to the bottom left.
     *
     * @param rectangle swt rectangle object
     *
     * @throws NullPointerException if rectangle argument is null
     */
    public SwtRectangle(final org.eclipse.swt.graphics.Rectangle rectangle) {
        super(rectangle.x, rectangle.height - rectangle.y, rectangle.width, rectangle.height);
    }
}
