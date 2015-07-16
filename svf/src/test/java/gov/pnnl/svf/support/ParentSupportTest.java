package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;

/**
 *
 * @author Arthur Bleeker
 */
public class ParentSupportTest extends AbstractObjectTestBase<ParentSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public ParentSupportTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected ParentSupport copyValueObject(final ParentSupport object) {
        return object;
    }

    @Override
    protected ParentSupport newValueObject() {
        return ParentSupport.newInstance(new ShapeActor(scene));
    }

    @Override
    protected void setFieldsToNull(final ParentSupport object) {
        // no nullable fields
    }
}
