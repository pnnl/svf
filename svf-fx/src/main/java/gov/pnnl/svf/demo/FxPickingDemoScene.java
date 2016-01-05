package gov.pnnl.svf.demo;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.scene.FxAbstractScene;

/**
 *
 * @author Arthur Bleeker
 */
public class FxPickingDemoScene extends FxAbstractScene {

    /**
     * Constructor
     */
    public FxPickingDemoScene() {
        super();
    }

    @Override
    public void initialize(final GL2 gl) {
        // no operation
    }

    @Override
    public void load() {
        // load the scene
        // TODO when JavaFX is more mature move demo to svf-demo
        //        new PickingDemoLoader().load(this);
    }
}
