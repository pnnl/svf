package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.ColorUtil;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import org.junit.Test;

/**
 * @author Arthur Bleeker
 *
 */
public class ColorSupportTest extends AbstractObjectTestBase<ColorSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());

    public ColorSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test method for
     * {gov.pnnl.canopy.jogl.ui.support.ColorSupport#setColor(javax.vecmath.Color4f)}.
     */
    @Test
    public void testSetColor() {
        final ColorSupport support = newValueObject();
        support.setColor(Color.WHITE);
        testBoundField(support, ColorSupport.COLOR, Color.RED);
    }

    @Override
    protected ColorSupport copyValueObject(final ColorSupport object) {
        return object;
    }

    @Override
    protected ColorSupport newValueObject() {
        final ShapeActor actor = new ShapeActor(scene);
        final ColorSupport support = ColorSupport.newInstance(actor);
        support.setColor(ColorUtil.createRandomColor(0.0f, 1.0f));
        return support;
    }

    @Override
    protected void setFieldsToNull(final ColorSupport object) {
        // no fields to set
    }
}
