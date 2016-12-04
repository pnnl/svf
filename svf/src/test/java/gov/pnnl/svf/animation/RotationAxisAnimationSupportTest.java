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
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Assert;
import org.junit.Test;

public class RotationAxisAnimationSupportTest extends AbstractObjectTestBase<RotationAxisAnimationSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final ShapeActorTestImpl shapeActorTest = new ShapeActorTestImpl();
    final Random random = new Random();

    public RotationAxisAnimationSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test method for {link
     * gov.pnnl.svf.animation.RotationAxisAnimationSupport#animate(float)}.
     */
    @Test
    public void testAnimate() {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(new Rectangle2D(1.0, 1.0));
        final TransformSupport transform = TransformSupport.newInstance(actor);
        transform.setRotationAxis(Vector3D.PLUS_I);
        final Vector3D target = Vector3D.PLUS_J;
        final RotationAxisAnimationSupport support = RotationAxisAnimationSupport.newInstance(actor, 100, 0L, false, target);
        // update the color
        support.update(50);
        // check the results
        final Vector3D normalized = new Vector3D(0.5, 0.5, 0.0).normalize();
        Assert.assertEquals(normalized.getX(), transform.getRotationAxis().getX(), 0.001);
        Assert.assertEquals(normalized.getY(), transform.getRotationAxis().getY(), 0.001);
        Assert.assertEquals(normalized.getZ(), transform.getRotationAxis().getZ(), 0.001);
    }

    @Override
    protected RotationAxisAnimationSupport copyValueObject(final RotationAxisAnimationSupport object) {
        return object;
    }

    @Override
    protected RotationAxisAnimationSupport newValueObject() {
        return RotationAxisAnimationSupport.newInstance(shapeActorTest.newValueObject(), random.nextLong(), random.nextLong(), random.nextBoolean(), new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    @Override
    protected void setFieldsToNull(final RotationAxisAnimationSupport object) {
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
