package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.DynamicShapeActor;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.scene.Scene;

/**
 * This actor is used to represent a scene boundary when using JBox2d physics.
 *
 * @author Arthur Bleeker
 */
public class JBox2dSceneBoundaryActor extends DynamicShapeActor {

    /**
     * The actor type used to identify a scene boundary.
     */
    public static final String SCENE_BOUNDARY_TYPE = "scene-boundary";

    /**
     * Constructor
     *
     * @param scene reference to the scene
     * @param id    id used to identify this actor
     */
    public JBox2dSceneBoundaryActor(final Scene scene, final String id) {
        super(scene, id);
        setType(SCENE_BOUNDARY_TYPE);
        setShape(Rectangle2D.ONE);
    }

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public JBox2dSceneBoundaryActor(final Scene scene) {
        super(scene);
        setType(SCENE_BOUNDARY_TYPE);
        setShape(Rectangle2D.ONE);
    }
}
