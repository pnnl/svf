package gov.pnnl.svf.jbox2d.physics;

/**
 * Type of physics actor. These types are close to the Box2d definitions.
 *
 * @author Amelia
 */
public enum JBox2dPhysicsType {

    /**
     * STATIC physics actor types do not move under simulation.
     */
    STATIC,
    /**
     * DYNAMIC physics actor types are moved by simulation.
     */
    DYNAMIC,
    /**
     * KINEMATIC physics actor types are only notified of physics actions with
     * dynamic actors.
     */
    KINEMATIC
}
