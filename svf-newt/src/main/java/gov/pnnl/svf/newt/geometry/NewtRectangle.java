package gov.pnnl.svf.newt.geometry;

import gov.pnnl.svf.geometry.Rectangle;
import java.io.Serializable;

/**
 * Immutable shape class that provides an x and y coordinate along with the
 * width and height. This rectangle should be used for user interface components
 * and is in OpenGL viewport coordinates. The x and y will be located at the
 * bottom left of the rectangle. This version just provides a convenience
 * constructor for NEWT.
 *
 * @author Arthur Bleeker
 *
 */
public class NewtRectangle extends Rectangle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Convenience Constructor
     * <p/>
     * This constructor will create a rectangle with the origin moved from the
     * top left to the bottom left.
     *
     * @param rectangle newt rectangle object
     *
     * @throws NullPointerException if rectangle argument is null
     */
    public NewtRectangle(final com.jogamp.nativewindow.util.Rectangle rectangle) {
        super((int) rectangle.getX(), (int) (rectangle.getHeight() - rectangle.getY()), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }
}
