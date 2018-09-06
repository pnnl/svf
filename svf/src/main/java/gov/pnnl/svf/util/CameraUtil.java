package gov.pnnl.svf.util;

import gov.pnnl.svf.animation.LocationAnimationSupport;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Shape2D;
import gov.pnnl.svf.geometry.Shape3D;
import java.util.Objects;
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
     * Moves the camera so that the entire shape is visible. This only works for
     * default camera orientation and shapes on the 0 z plane.
     *
     * @param camera  the camera to relocate
     * @param shape   the shape
     * @param animate true to animate the camera to the new location
     *
     * @throws NullPointerException if any arguments are null
     */
    public static void fitInViewport(final Camera camera, final Shape shape, final boolean animate) {
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

    /**
     * Find the width and height of the viewable area at the distance of this
     * camera from the scene 0 plane.
     *
     * @param camera the camera
     *
     * @return the view area
     */
    public static Rectangle2D findViewArea(final Camera camera) {
        Objects.requireNonNull(camera, "Camera is required");
        final Rectangle viewport = camera.getViewport();
        if (viewport.getWidth() == 0.0 || viewport.getHeight() == 0.0) {
            return Rectangle2D.ZERO;
        }
        // if shape aspect ratio is equal or greater than camera then use width, otherwise use height, to calculate the dimensions
        final double ar = (double) viewport.getWidth() / (double) viewport.getHeight();
        final double fov = Math.toRadians(camera.getFieldOfView() / 2.0);
        final double z;
        if (camera instanceof OrbitCamera) {
            z = camera.getLocation().subtract(Vector3D.ZERO).getNorm();
        } else if (camera.getLook().equals(Vector3D.PLUS_I) || camera.getLook().equals(Vector3D.MINUS_I)) {
            z = Math.abs(camera.getLocation().getX());
        } else if (camera.getLook().equals(Vector3D.PLUS_J) || camera.getLook().equals(Vector3D.MINUS_J)) {
            z = Math.abs(camera.getLocation().getY());
        } else {
            z = Math.abs(camera.getLocation().getZ());
        }
        final double h = Math.tan(fov) * 2.0 * z;
        final double w = h * ar;
        return new Rectangle2D(0.0, 0.0, w, h);
    }
}
