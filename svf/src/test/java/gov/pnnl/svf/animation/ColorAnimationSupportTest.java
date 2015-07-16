package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.actor.ShapeActorTest;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.ColorUtil;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Test;

public class ColorAnimationSupportTest extends AbstractObjectTestBase<ColorAnimationSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final ShapeActorTestImpl shapeActorTest = new ShapeActorTestImpl();
    final Random random = new Random();

    public ColorAnimationSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test method for {link
     * gov.pnnl.svf.animation.ColorAnimationSupport#animate(float)}.
     */
    @Test
    public void testAnimate() {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(new Rectangle2D(1.0, 1.0));
        final ColorSupport color = ColorSupport.newInstance(actor);
        color.setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        final Color target = new Color(0.2f, 0.4f, 0.6f, 1.0f);
        final ColorAnimationSupport support = ColorAnimationSupport.newInstance(actor, 100, 0L, false, target);
        // update the color
        support.update(50);
        // check the results
        Assert.assertEquals(0.6f, color.getColor().getRed(), 2.0f / 255.0f);
        Assert.assertEquals(0.7f, color.getColor().getGreen(), 2.0f / 255.0f);
        Assert.assertEquals(0.8f, color.getColor().getBlue(), 2.0f / 255.0f);
        Assert.assertEquals(0.5f, color.getColor().getAlpha(), 2.0f / 255.0f);
    }

    @Override
    protected ColorAnimationSupport copyValueObject(final ColorAnimationSupport object) {
        return object;
    }

    @Override
    protected ColorAnimationSupport newValueObject() {
        return ColorAnimationSupport.newInstance(shapeActorTest.newValueObject(), random.nextLong(), random.nextLong(), random.nextBoolean(), ColorUtil.createRandomColor(0.0f, 1.0f));
    }

    @Override
    protected void setFieldsToNull(final ColorAnimationSupport object) {
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
