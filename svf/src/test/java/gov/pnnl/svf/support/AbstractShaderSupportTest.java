package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
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
public class AbstractShaderSupportTest extends AbstractObjectTestBase<AbstractShaderSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public AbstractShaderSupportTest() {
        collisionIterateLength = 1000;
    }

    @Override
    protected AbstractShaderSupport copyValueObject(final AbstractShaderSupport object) {
        return object;
    }

    @Override
    protected AbstractShaderSupport newValueObject() {
        return new AbstractShaderSupportImpl(new ShapeActor(scene), null, null);
    }

    @Override
    protected void setFieldsToNull(final AbstractShaderSupport object) {
        // no nullable fields
    }

    public static class AbstractShaderSupportImpl extends AbstractShaderSupport {

        public AbstractShaderSupportImpl(final Actor actor, final String vertex, final String fragment) {
            super(actor, vertex, fragment);
        }
    }
}
