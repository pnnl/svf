package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.support.AbstractSupport;

/**
 * Abstract base support object for JBox2d support objects.
 *
 * @author Arthur Bleeker
 */
abstract class AbstractJBox2dSupport<I> extends AbstractSupport<I> {

    /**
     * String representation of a field in this object.
     */
    public static final String PHYSICS_ENGINE = "physicsEngine";
    private JBox2dPhysicsEngine physicsEngine;

    /**
     * Constructor
     *
     * @param actor
     */
    AbstractJBox2dSupport(final Actor actor) {
        super(actor);
    }

    /**
     * @return a reference to the physics engine, may be null before this object
     *         gets added to the physics engine
     */
    public JBox2dPhysicsEngine getPhysicsEngine() {
        synchronized (this) {
            return physicsEngine;
        }
    }

    /**
     * JBox2d support objects must be added to a JBox2d physics engine and can
     * only be added once.
     *
     * @param engine the physics engine
     *
     * @throws NullPointerException if engine is null
     * @throws returns              true if the engine was set
     */
    boolean setPhysicsEngine(final JBox2dPhysicsEngine engine) {
        if (engine == null) {
            throw new NullPointerException("engine");
        }
        final boolean set;
        synchronized (this) {
            if (this.physicsEngine == null) {
                this.physicsEngine = engine;
                set = true;
            } else {
                set = false;
            }
        }
        if (set) {
            getPropertyChangeSupport().firePropertyChange(PHYSICS_ENGINE, null, engine);
        }
        return set;
    }
}
