package gov.pnnl.svf.camera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.util.Transformer;
import gov.pnnl.svf.geometry.Frustum;
import gov.pnnl.svf.geometry.Rectangle;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Interface for a camera into a scene. The scene can handle multiple cameras.
 * Actors can also define which camera they are visible in or choose to be
 * visible in all cameras by not specifying a camera.
 *
 * @author Arthur Bleeker
 */
public interface Camera extends Actor {

    /**
     * String representation of a field in this object.
     */
    String VIEWPORT = "viewport";
    /**
     * String representation of a field in this object.
     */
    String VIEWPORT_TRANSFORMER = "viewportTransformer";
    /**
     * String representation of a field in this object.
     */
    String LOCATION = "location";
    /**
     * String representation of a field in this object.
     */
    String UP = "up";
    /**
     * String representation of a field in this object.
     */
    String LOOK = "look";
    /**
     * String representation of a field in this object.
     */
    String CAMERA_UP = "cameraUp";
    /**
     * String representation of a field in this object.
     */
    String PERP = "perp";
    /**
     * String representation of a field in this object.
     */
    String FIELD_OF_VIEW = "fieldOfView";
    /**
     * String representation of a field in this object.
     */
    String NEAR_CLIP = "nearClip";
    /**
     * String representation of a field in this object.
     */
    String FAR_CLIP = "farClip";
    /**
     * String representation of a field in this object.
     */
    String FRUSTUM = "frustum";

    /**
     * @return the normalized vector that represents up according to the camera
     *         orientation
     */
    Vector3D getCameraUp();

    /**
     * The farthest distance from the camera that will be drawn.
     *
     * @return the farClip
     */
    double getFarClip();

    /**
     * The size, in degrees, of the up and down viewable frustum. This, along
     * with the aspect ratio, can be used to determine the left and right
     * viewable size.
     *
     * @return the fieldOfView
     */
    double getFieldOfView();

    /**
     * @return the location of the camera in scene space
     */
    Vector3D getLocation();

    /**
     * @return the normalized vector for the location to the target
     */
    Vector3D getLook();

    /**
     * @return the nearClip
     */
    double getNearClip();

    /**
     * @return the vector that is perpendicular to the look at and up vectors
     */
    Vector3D getPerp();

    /**
     * @return the normalized vector that represents up to the camera
     */
    Vector3D getUp();

    /**
     * @return the upper left location and size of the viewport
     */
    Rectangle getViewport();

    /**
     * @return the viewport transformer
     */
    Transformer<Rectangle> getViewportTransformer();

    /**
     * @return the frustum of the viewable area of this camera
     */
    Frustum getFrustum();

    /**
     * Sets this camera as the current viewing transformation.
     *
     * @param gl  Reference to GL
     * @param glu Reference to GLU
     */
    void setLookAt(GL2 gl, GLUgl2 glu);

    /**
     * <p>
     * Override this method for cameras that don't display using the entire view
     * viewport. </p>
     * <pre>
     * <code>
     * &#64;Override
     *  public void setViewport(final Rectangle viewport) &#123;
     *      super.setViewport(0, 0, 100, 100);
     *  &#125;
     * </code>
     * </pre>
     *
     * @param viewport the upper left location and size of the viewport to set
     *
     * @return this instance
     */
    Camera setViewport(Rectangle viewport);

    /**
     * @param transformer the viewport transformer
     *
     * @return this instance
     */
    Camera setViewportTransformer(Transformer<Rectangle> transformer);

    /**
     * The location of the camera in scene space.
     *
     * @param location the location of the camera in scene space
     *
     * @return this instance
     */
    Camera setLocation(Vector3D location);

    /**
     * The minimum distance that objects can be viewed.
     *
     * @param nearClip the nearClip to set
     *
     * @return this instance
     */
    Camera setNearClip(double nearClip);

    /**
     * The maximum distance that objects can be viewed.
     *
     * @param farClip the farClip to set
     *
     * @return this instance
     */
    Camera setFarClip(double farClip);

    /**
     * The field of view in degrees.
     *
     * @param fieldOfView the fieldOfView to set
     *
     * @return this instance
     */
    Camera setFieldOfView(final double fieldOfView);

    /**
     * The vector that represents the direction the camera is facing.
     *
     * @param look The normalized look vector for the camera.
     *
     * @return this instance
     */
    Camera setLook(Vector3D look);

    /**
     * The vector that represents up to the camera.
     *
     * @param up the normalized vector that represents up in the scene
     *
     * @return this instance
     */
    Camera setUp(Vector3D up);

    /**
     * Get a reference to the extended API for the camera.
     *
     * @return a reference to the extended interface
     */
    CameraExt<?> getExtended();
}
