package gov.pnnl.svf.jbox2d.physics;

/**
 * Type of physics joint.
 *
 * @author Amelia Bleeker
 */
public enum JBox2dJointType {

    /**
     * Joint type that attempts to maintain a certain distance from two points.
     */
    DISTANCE,
    /**
     * Joint type that allows two objects to rotate around a fixed point.
     */
    REVOLUTE,
    /**
     * Joint type that allows manipulation of an object by the mouse.
     */
    MOUSE
}
