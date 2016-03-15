package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.actor.ShapeActorTest;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.texture.TextureRegionListSupport;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class TextureAnimationSupportTest extends AbstractObjectTestBase<TextureAnimationSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final ShapeActorTestImpl shapeActorTest = new ShapeActorTestImpl();
    final Random random = new Random();

    public TextureAnimationSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test method for {link
     * gov.pnnl.svf.animation.TextureAnimationSupport#animate(float)}.
     */
    @Test
    public void testAnimate() {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(new Rectangle2D(1.0, 1.0));
        final List<Rectangle> regions = Arrays.asList(new Rectangle[]{
            new Rectangle(0, 0, 64, 64),
            new Rectangle(32, 0, 64, 64),
            new Rectangle(32, 32, 64, 64),
            new Rectangle(0, 32, 64, 64)
        });
        final TextureRegionListSupport textureRegionListSupport = TextureRegionListSupport.newInstance(actor, regions, 128, 128);
        final TextureAnimationSupport support = TextureAnimationSupport.newInstance(actor, 100, 0L, true);
        // update and check results
        support.update(50);
        Assert.assertEquals(0, textureRegionListSupport.getCurrentRegion());
        support.update(50);
        Assert.assertEquals(1, textureRegionListSupport.getCurrentRegion());
        support.update(150);
        Assert.assertEquals(2, textureRegionListSupport.getCurrentRegion());
    }

    @Override
    protected TextureAnimationSupport copyValueObject(final TextureAnimationSupport object) {
        return object;
    }

    @Override
    protected TextureAnimationSupport newValueObject() {
        return TextureAnimationSupport.newInstance(shapeActorTest.newValueObject(), random.nextLong(), random.nextLong(), random.nextBoolean());
    }

    @Override
    protected void setFieldsToNull(final TextureAnimationSupport object) {
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
