package gov.pnnl.svf.util;

import gov.pnnl.svf.animation.LocationAnimationSupport;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Shape2D;
import gov.pnnl.svf.geometry.Shape3D;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Utility class for working with the various cameras.
 *
 * @author Amelia Bleeker
 */
public class CameraUtil {

    /**
     * Constructor private for util class
     */
    private CameraUtil() {
        super();
    }

    /**
     * Moves the camera so that the entire shape is visible.
     *
     * @param camera  the camera to relocate
     * @param shape   the shape
     * @param animate true to animate the camera to the new location
     *
     * @throws NullPointerException if any arguments are null
     */
    public static void fitInViewport(final DraggingCamera camera, final Shape shape, final boolean animate) {
        if (camera == null) {
            throw new NullPointerException("camera");
        }
        if (shape == null) {
            throw new NullPointerException("shape");
        }
        final Rectangle viewport = camera.getViewport();
        if (viewport.getWidth() == 0.0 || viewport.getHeight() == 0.0) {
            return;
        }
        final double x;
        final double y;
        final double w;
        final double h;
        if (shape instanceof Shape2D) {
            x = ((Shape2D) shape).getX();
            y = ((Shape2D) shape).getY();
            w = ((Shape2D) shape).getWidth();
            h = ((Shape2D) shape).getHeight();
        } else if (shape instanceof Shape3D) {
            x = ((Shape3D) shape).getX();
            y = ((Shape3D) shape).getY();
            w = ((Shape3D) shape).getWidth();
            h = ((Shape3D) shape).getHeight();
        } else {
            return;
        }
        // if shape aspect ratio is equal or greater than camera then use width, otherwise use height, as constraint
        final double cameraAspectRatio = (double) viewport.getWidth() / (double) viewport.getHeight();
        final double shapeAspectRatio = w == 0.0 || h == 0.0 ? 1.0 : w / h;
        final double fov = Math.toRadians(camera.getFieldOfView() / 2.0);
        final double z;
        if (shapeAspectRatio >= cameraAspectRatio) {
            // width constrained
            z = (w * 0.5) / (Math.tan(fov) * cameraAspectRatio);
        } else {
            // height constrained
            z = (h * 0.5) / Math.tan(fov);
        }
        final Vector3D location = new Vector3D(x, y, z);
        if (animate) {
            camera.add(LocationAnimationSupport.newInstance(camera, 400L, 0L, false, location));
        } else {
            camera.setLocation(location);
        }
    }
}
