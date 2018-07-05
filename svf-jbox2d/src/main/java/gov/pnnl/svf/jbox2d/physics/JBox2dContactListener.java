package gov.pnnl.svf.jbox2d.physics;

/**
 * Listener used to listen for contact events.
 *
 * @author Amelia Bleeker
 */
public interface JBox2dContactListener {

    /**
     * Notified when two objects begin to make contact.
     *
     * @param obj1 the object involved in the contact event
     * @param obj2 the object involved in the contact event
     */
    void beginContact(JBox2dPhysicsSupport obj1, JBox2dPhysicsSupport obj2);

    /**
     * Notified when two objects cease to make contact.
     *
     * @param obj1 the object involved in the contact event
     * @param obj2 the object involved in the contact event
     */
    void endContact(JBox2dPhysicsSupport obj1, JBox2dPhysicsSupport obj2);
}
