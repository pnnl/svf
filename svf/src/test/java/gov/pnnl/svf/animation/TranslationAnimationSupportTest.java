package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.actor.ShapeActorTest;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Random;
import junit.framework.Assert;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Test;

public class TranslationAnimationSupportTest extends AbstractObjectTestBase<TranslationAnimationSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final ShapeActorTestImpl shapeActorTest = new ShapeActorTestImpl();
    final Random random = new Random();

    public TranslationAnimationSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test method for {link
     * gov.pnnl.svf.animation.TranslationAnimationSupport#animate(float)}.
     */
    @Test
    public void testAnimate() {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(new Rectangle2D(1.0, 1.0));
        final TransformSupport transform = TransformSupport.newInstance(actor);
        transform.setTranslation(Vector3D.ZERO);
        final Vector3D target = new Vector3D(1.0, 1.0, 1.0);
        final TranslationAnimationSupport support = TranslationAnimationSupport.newInstance(actor, 100, 0L, false, target);
        // update the color
        support.update(50);
        // check the results
        Assert.assertEquals(0.5, transform.getTranslation().getX(), 0.001);
        Assert.assertEquals(0.5, transform.getTranslation().getY(), 0.001);
        Assert.assertEquals(0.5, transform.getTranslation().getZ(), 0.001);
    }

    @Override
    protected TranslationAnimationSupport copyValueObject(final TranslationAnimationSupport object) {
        return object;
    }

    @Override
    protected TranslationAnimationSupport newValueObject() {
        return TranslationAnimationSupport.newInstance(shapeActorTest.newValueObject(), random.nextLong(), random.nextLong(), random.nextBoolean(), new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    @Override
    protected void setFieldsToNull(final TranslationAnimationSupport object) {
        // no fields to set
    }

    private class ShapeActorTestImpl extends ShapeActorTest {

        private ShapeActorTestImpl() {
        }

        @Override
        public ShapeActor newValueObject() {
            return super.newValueObject();
        }
    }
}
