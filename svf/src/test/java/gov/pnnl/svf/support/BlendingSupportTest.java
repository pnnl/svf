package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class BlendingSupportTest extends AbstractObjectTestBase<BlendingSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public BlendingSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test of setDestinationAlpha method, of class BlendingSupport.
     */
    @Test
    public void testSetDestinationAlpha() {
        final BlendingSupport blending = newValueObject();
        blending.setDestinationAlpha(BlendingType.NONE);
        testBoundField(blending, BlendingSupport.DESTINATION_ALPHA, BlendingType.ONE);
    }

    /**
     * Test of setDestinationFactor method, of class BlendingSupport.
     */
    @Test
    public void testSetDestinationFactor() {
        final BlendingSupport blending = newValueObject();
        blending.setDestinationFactor(BlendingType.NONE);
        testBoundField(blending, BlendingSupport.DESTINATION_FACTOR, BlendingType.ONE);
    }

    /**
     * Test of setSourceAlpha method, of class BlendingSupport.
     */
    @Test
    public void testSetSourceAlpha() {
        final BlendingSupport blending = newValueObject();
        blending.setSourceAlpha(BlendingType.NONE);
        testBoundField(blending, BlendingSupport.SOURCE_ALPHA, BlendingType.ONE);
    }

    /**
     * Test of setSourceFactor method, of class BlendingSupport.
     */
    @Test
    public void testSetSourceFactor() {
        final BlendingSupport blending = newValueObject();
        blending.setSourceFactor(BlendingType.NONE);
        testBoundField(blending, BlendingSupport.SOURCE_FACTOR, BlendingType.ONE);
    }

    @Override
    protected BlendingSupport copyValueObject(final BlendingSupport object) {
        return object;
    }

    @Override
    protected BlendingSupport newValueObject() {
        final ShapeActor actor = new ShapeActor(scene);
        final BlendingSupport support = BlendingSupport.newInstance(actor);
        return support;
    }

    @Override
    protected void setFieldsToNull(final BlendingSupport object) {
        // no fields settable to null
    }
}
