package gov.pnnl.svf.jbox2d.physics;

/**
 * This interface provides a means for physics objects to filter who they
 * collide with.
 *
 * @author Amelia Bleeker
 */
public interface JBox2dContactFilter {

    /**
     * The parameter is null for a scene boundary.
     *
     * @param obj1 the object that this physics support object should test
     * @param obj2 the object that this physics support object should test
     *
     * @return true if these objects should collide
     */
    boolean shouldCollide(JBox2dPhysicsSupport obj1, JBox2dPhysicsSupport obj2);
}
