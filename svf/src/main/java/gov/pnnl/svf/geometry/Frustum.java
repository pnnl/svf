package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.util.MathUtil;
import java.io.Serializable;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Immutable shape that represents a frustum mainly used for camera visible
 * bounds.
 *
 * @author Amelia Bleeker
 */
public class Frustum extends Shape3D implements Serializable {

    /**
     * Plane used to identify a plane of the frustum.
     */
    public static enum Plane {

        /**
         * Left of the frustum
         */
        LEFT,
        /**
         * Right of the frustum
         */
        RIGHT,
        /**
         * Top of the frustum
         */
        TOP,
        /**
         * Bottom of the frustum
         */
        BOTTOM,
        /**
         * Towards the point of the frustum
         */
        NEAR,
        /**
         * Away from the point of the frustum
         */
        FAR;
    }

    /**
     * A zero frustum.
     */
    public static final Frustum ZERO = new Frustum();
    private static final long serialVersionUID = 1L;
    protected final double nearDistance;
    protected final double farDistance;
    protected final Rectangle2D nearViewableArea;
    protected final Rectangle2D farViewableArea;
    protected final Vector3D farCenter;
    protected final Vector3D farLeftTop;
    protected final Vector3D farRightTop;
    protected final Vector3D farLeftBottom;
    protected final Vector3D farRightBottom;
    protected final Vector3D nearCenter;
    protected final Vector3D nearLeftTop;
    protected final Vector3D nearRightTop;
    protected final Vector3D nearLeftBottom;
    protected final Vector3D nearRightBottom;
    protected final Plane3D[] planes;

    /**
     * Constructor for a zero frustum.
     */
    private Frustum() {
        super();
        nearDistance = 0.0;
        farDistance = 0.0;
        nearViewableArea = Rectangle2D.ZERO;
        farViewableArea = Rectangle2D.ZERO;
        farCenter = Vector3D.ZERO;
        farLeftTop = Vector3D.ZERO;
        farRightTop = Vector3D.ZERO;
        farLeftBottom = Vector3D.ZERO;
        farRightBottom = Vector3D.ZERO;
        nearCenter = Vector3D.ZERO;
        nearLeftTop = Vector3D.ZERO;
        nearRightTop = Vector3D.ZERO;
        nearLeftBottom = Vector3D.ZERO;
        nearRightBottom = Vector3D.ZERO;
        planes = new Plane3D[0];
    }

    /**
     * Constructor
     *
     * @param location     the location of the camera
     * @param look         the normalized look vector
     * @param up           the normalized up vector
     * @param right        the normalized perpendicular right vector
     * @param fieldOfView  the field of view in radians
     * @param aspectRatio  the aspect ratio (width / height)
     * @param nearDistance the distance to the near plane (frustum point to near
     *                     plane)
     * @param farDistance  the distance to the far plane (frustum point to far
     *                     plane)
     *
     * @throws NullPointerException     if any arguments are null
     * @throws IllegalArgumentException if any arguments are negative or far
     *                                  distance is less than near distance
     */
    public Frustum(final Vector3D location, final Vector3D look, final Vector3D up, final Vector3D right, final double fieldOfView, final double aspectRatio,
                   final double nearDistance, final double farDistance) {
        super(location.getX(), location.getY(), location.getZ());
        if (look == null) {
            throw new NullPointerException("look");
        }
        if (up == null) {
            throw new NullPointerException("up");
        }
        if (right == null) {
            throw new NullPointerException("right");
        }
        if (fieldOfView < 0.0) {
            throw new IllegalArgumentException("fieldOfView");
        }
        if (aspectRatio < 0.0) {
            throw new IllegalArgumentException("aspectRatio");
        }
        if (nearDistance < 0.0) {
            throw new IllegalArgumentException("nearDistance");
        }
        if (farDistance < nearDistance) {
            throw new IllegalArgumentException("farDistance");
        }
        this.nearDistance = nearDistance;
        this.farDistance = farDistance;
        // near and far planes
        final double nearHeight = Math.max(0.0, 2.0 * Math.tan(fieldOfView / 2.0) * nearDistance);
        final double nearWidth = Math.max(0.0, nearHeight * aspectRatio);
        final double farHeight = Math.max(0.0, 2.0 * Math.tan(fieldOfView / 2.0) * farDistance);
        final double farWidth = Math.max(0.0, farHeight * aspectRatio);
        nearViewableArea = new Rectangle2D(0.0, 0.0, nearWidth, nearHeight);
        farViewableArea = new Rectangle2D(0.0, 0.0, farWidth, farHeight);
        // the eight points (plus near and far centers) of the fustrum in scene space
        // far center
        farCenter = location.add(look.scalarMultiply(farDistance));
        // far points
        farLeftTop = farCenter.add(up.scalarMultiply(farHeight / 2.0)).subtract(right.scalarMultiply(farWidth / 2.0));
        farRightTop = farCenter.add(up.scalarMultiply(farHeight / 2.0)).add(right.scalarMultiply(farWidth / 2.0));
        farLeftBottom = farCenter.subtract(up.scalarMultiply(farHeight / 2.0)).subtract(right.scalarMultiply(farWidth / 2.0));
        farRightBottom = farCenter.subtract(up.scalarMultiply(farHeight / 2.0)).add(right.scalarMultiply(farWidth / 2.0));
        // near center
        nearCenter = location.add(look.scalarMultiply(nearDistance));
        // near points
        nearLeftTop = nearCenter.add(up.scalarMultiply(nearHeight / 2.0)).subtract(right.scalarMultiply(nearWidth / 2.0));
        nearRightTop = nearCenter.add(up.scalarMultiply(nearHeight / 2.0)).add(right.scalarMultiply(nearWidth / 2.0));
        nearLeftBottom = nearCenter.subtract(up.scalarMultiply(nearHeight / 2.0)).subtract(right.scalarMultiply(nearWidth / 2.0));
        nearRightBottom = nearCenter.subtract(up.scalarMultiply(nearHeight / 2.0)).add(right.scalarMultiply(nearWidth / 2.0));
        // planess
        planes = calculatePlanes();
    }

    @Override
    public double getWidth() {
        return Math.max(nearViewableArea.getWidth(), farViewableArea.getWidth());
    }

    @Override
    public double getHeight() {
        return Math.max(nearViewableArea.getHeight(), farViewableArea.getHeight());
    }

    @Override
    public double getDepth() {
        return farDistance - nearDistance;
    }

    /**
     * Get the near viewable area.
     *
     * @return the near viewable area
     */
    public Rectangle2D getNearViewableArea() {
        return nearViewableArea;
    }

    /**
     * Get the far viewable area.
     *
     * @return the far viewable area
     */
    public Rectangle2D getFarViewableArea() {
        return farViewableArea;
    }

    /**
     * Get the viewable area with the origin set at zero.
     *
     * @param distance the distance to get the viewable area for
     *
     * @return the viewable area at the specified distance
     *
     * @throws IllegalArgumentException if distance is less than zero
     */
    public Rectangle2D getViewableArea(final double distance) {
        if (distance < 0.0) {
            throw new IllegalArgumentException("distance");
        }
        final double width = MathUtil.scale(distance, nearDistance, farDistance, nearViewableArea.getWidth(), farViewableArea.getWidth());
        final double height = MathUtil.scale(distance, nearDistance, farDistance, nearViewableArea.getHeight(), farViewableArea.getHeight());
        return new Rectangle2D(0.0, 0.0, width, height);
    }

    /**
     * Get the specified plane or null if there are no planes for this frustum.
     *
     * @param plane the plane to get
     *
     * @return the plane or null
     *
     * @throws NullPointerException if plane is null
     */
    public Plane3D getPlane(final Plane plane) {
        if (plane == null) {
            throw new NullPointerException("plane");
        }
        return planes.length == 0 ? null : planes[plane.ordinal()];
    }

    /**
     * Test whether a point is inside of the viewable frustum area.
     *
     * @param point the point in scene space
     *
     * @return true if the frustum contains the point
     *
     * @throws NullPointerException if point is null
     */
    public boolean contains(final Vector3D point) {
        if (point == null) {
            throw new NullPointerException("point");
        }
        // check if this is a zero space Frustum
        if (planes.length == 0) {
            return false;
        }
        // check the planes
        for (final Plane3D plane : planes) {
            final double distance = plane.distance(point);
            if (distance < 0.0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Test whether a sphere is inside of the viewable frustum area.
     *
     * @param point  the center point of the sphere in scene space
     * @param radius the radius of the sphere in scene space
     *
     * @return true if the frustum contains the sphere
     *
     * @throws NullPointerException     if point is null
     * @throws IllegalArgumentException if radius is less than zero
     */
    public boolean contains(final Vector3D point, final double radius) {
        if (point == null) {
            throw new NullPointerException("point");
        }
        if (radius < 0.0) {
            throw new IllegalArgumentException("radius");
        }
        // check if this is a zero space Frustum
        if (planes.length == 0) {
            return false;
        }
        // check the planes
        if (Double.compare(radius, 0.0) == 0) {
            return contains(point);
        }
        for (final Plane3D plane : planes) {
            final double distance = plane.distance(point);
            if (distance < -radius) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(final double x, final double y, final double z) {
        return contains(new Vector3D(x, y, z));
    }

    /**
     * Test whether a sphere is inside of the viewable frustum area.
     *
     * @param x      the x point of the sphere in scene space
     * @param y      the y point of the sphere in scene space
     * @param z      the z point of the sphere in scene space
     * @param radius the radius of the sphere in scene space
     *
     * @return true if the frustum contains the sphere
     *
     * @throws IllegalArgumentException if radius is less than zero
     */
    public boolean contains(final double x, final double y, final double z, final double radius) {
        return contains(new Vector3D(x, y, z), radius);
    }

    private Plane3D[] calculatePlanes() {
        if (Vector3D.ZERO.equals(nearLeftTop)
            || Vector3D.ZERO.equals(nearRightTop)
            || Vector3D.ZERO.equals(nearLeftBottom)
            || Vector3D.ZERO.equals(nearRightBottom)
            || Vector3D.ZERO.equals(farLeftTop)
            || Vector3D.ZERO.equals(farRightTop)
            || Vector3D.ZERO.equals(farLeftBottom)
            || Vector3D.ZERO.equals(farRightBottom)) {
            return new Plane3D[0];
        }
        // planes
        final Plane3D[] temp = new Plane3D[Plane.values().length];
        temp[Plane.LEFT.ordinal()] = Plane3D.newInstance(nearLeftTop, nearLeftBottom, farLeftBottom);
        temp[Plane.RIGHT.ordinal()] = Plane3D.newInstance(nearRightBottom, nearRightTop, farRightBottom);
        temp[Plane.TOP.ordinal()] = Plane3D.newInstance(nearRightTop, nearLeftTop, farLeftTop);
        temp[Plane.BOTTOM.ordinal()] = Plane3D.newInstance(nearLeftBottom, nearRightBottom, farRightBottom);
        temp[Plane.NEAR.ordinal()] = Plane3D.newInstance(nearLeftTop, nearRightTop, nearRightBottom);
        temp[Plane.FAR.ordinal()] = Plane3D.newInstance(farRightTop, farLeftTop, farLeftBottom);
        // return array
        return temp;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 89 * hash + (nearViewableArea != null ? nearViewableArea.hashCode() : 0);
        hash = 89 * hash + (farViewableArea != null ? farViewableArea.hashCode() : 0);
        hash = 89 * hash + (farCenter != null ? farCenter.hashCode() : 0);
        hash = 89 * hash + (farLeftTop != null ? farLeftTop.hashCode() : 0);
        hash = 89 * hash + (farRightTop != null ? farRightTop.hashCode() : 0);
        hash = 89 * hash + (farLeftBottom != null ? farLeftBottom.hashCode() : 0);
        hash = 89 * hash + (farRightBottom != null ? farRightBottom.hashCode() : 0);
        hash = 89 * hash + (nearCenter != null ? nearCenter.hashCode() : 0);
        hash = 89 * hash + (nearLeftTop != null ? nearLeftTop.hashCode() : 0);
        hash = 89 * hash + (nearRightTop != null ? nearRightTop.hashCode() : 0);
        hash = 89 * hash + (nearLeftBottom != null ? nearLeftBottom.hashCode() : 0);
        hash = 89 * hash + (nearRightBottom != null ? nearRightBottom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Frustum other = (Frustum) obj;
        if (nearViewableArea != other.nearViewableArea && (nearViewableArea == null || !nearViewableArea.equals(other.nearViewableArea))) {
            return false;
        }
        if (farViewableArea != other.farViewableArea && (farViewableArea == null || !farViewableArea.equals(other.farViewableArea))) {
            return false;
        }
        if (farCenter != other.farCenter && (farCenter == null || !farCenter.equals(other.farCenter))) {
            return false;
        }
        if (farLeftTop != other.farLeftTop && (farLeftTop == null || !farLeftTop.equals(other.farLeftTop))) {
            return false;
        }
        if (farRightTop != other.farRightTop && (farRightTop == null || !farRightTop.equals(other.farRightTop))) {
            return false;
        }
        if (farLeftBottom != other.farLeftBottom && (farLeftBottom == null || !farLeftBottom.equals(other.farLeftBottom))) {
            return false;
        }
        if (farRightBottom != other.farRightBottom && (farRightBottom == null || !farRightBottom.equals(other.farRightBottom))) {
            return false;
        }
        if (nearCenter != other.nearCenter && (nearCenter == null || !nearCenter.equals(other.nearCenter))) {
            return false;
        }
        if (nearLeftTop != other.nearLeftTop && (nearLeftTop == null || !nearLeftTop.equals(other.nearLeftTop))) {
            return false;
        }
        if (nearRightTop != other.nearRightTop && (nearRightTop == null || !nearRightTop.equals(other.nearRightTop))) {
            return false;
        }
        if (nearLeftBottom != other.nearLeftBottom && (nearLeftBottom == null || !nearLeftBottom.equals(other.nearLeftBottom))) {
            return false;
        }
        if (nearRightBottom != other.nearRightBottom && (nearRightBottom == null || !nearRightBottom.equals(other.nearRightBottom))) {
            return false;
        }
        return true;
    }
}
