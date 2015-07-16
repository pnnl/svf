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
import org.junit.Test;

public class RotationAnimationSupportTest extends AbstractObjectTestBase<RotationAnimationSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final ShapeActorTestImpl shapeActorTest = new ShapeActorTestImpl();
    final Random random = new Random();

    public RotationAnimationSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test method for {link
     * gov.pnnl.svf.animation.RotationAnimationSupport#animate(float)}.
     */
    @Test
    public void testAnimate() {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(new Rectangle2D(1.0, 1.0));
        final TransformSupport transform = TransformSupport.newInstance(actor);
        transform.setRotation(0.0);
        final double target = 1.0;
        final RotationAnimationSupport support = RotationAnimationSupport.newInstance(actor, 100, 0L, false, target);
        // update the color
        support.update(50);
        // check the results
        Assert.assertEquals(0.5, transform.getRotation(), 0.001);
    }

    @Override
    protected RotationAnimationSupport copyValueObject(final RotationAnimationSupport object) {
        return object;
    }

    @Override
    protected RotationAnimationSupport newValueObject() {
        return RotationAnimationSupport.newInstance(shapeActorTest.newValueObject(), random.nextLong(), random.nextLong(), random.nextBoolean(), random.nextDouble());
    }

    @Override
    protected void setFieldsToNull(final RotationAnimationSupport object) {
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
