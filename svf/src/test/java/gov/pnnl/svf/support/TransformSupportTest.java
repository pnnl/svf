package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class TransformSupportTest extends AbstractObjectTestBase<TransformSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public TransformSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test of setRotation method, of class TransformSupport.
     */
    @Test
    public void testSetRotation() {
        testBoundField(newValueObject(), TransformSupport.ROTATION, 2.456);
    }

    /**
     * Test of setRotationAxis method, of class TransformSupport.
     */
    @Test
    public void testSetRotationAxis() {
        testBoundField(newValueObject(), TransformSupport.ROTATION_AXIS, Vector3D.MINUS_I);
    }

    /**
     * Test of setScale method, of class TransformSupport.
     */
    @Test
    public void testSetScale() {
        testBoundField(newValueObject(), TransformSupport.SCALE, Vector3D.MINUS_I);
    }

    /**
     * Test of setTranslation method, of class TransformSupport.
     */
    @Test
    public void testSetTranslation() {
        testBoundField(newValueObject(), TransformSupport.TRANSLATION, Vector3D.MINUS_I);
    }

    @Override
    protected TransformSupport copyValueObject(final TransformSupport object) {
        return object;
    }

    @Override
    protected TransformSupport newValueObject() {
        return TransformSupport.newInstance(new ShapeActor(scene));
    }

    @Override
    protected void setFieldsToNull(final TransformSupport object) {
        // no nullable fields
    }
}
