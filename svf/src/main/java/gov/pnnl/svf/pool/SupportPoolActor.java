package gov.pnnl.svf.pool;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.AbstractActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.Scene;

/**
 * Actor used as a parent for a support object that can be shared in a scene.
 *
 * @author Amelia Bleeker
 */
public class SupportPoolActor extends AbstractActor {

    public static final String TYPE = "support-pool-actor";

    /**
     * Constructor
     *
     * @param scene reference to the parent scene
     * @param id    unique id which should be the same as the support id
     */
    public SupportPoolActor(final Scene scene, final String id) {
        super(scene, TYPE, id);
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no implementation
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no implementation

    }

}
