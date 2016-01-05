package gov.pnnl.svf.scene;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.ColorUtil;
import gov.pnnl.svf.hint.CameraHint;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.EnumSet;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class AbstractSceneBuilderTest extends AbstractObjectTestBase<AbstractSceneBuilder<?>> {

    private final Random random = new Random();

    /**
     * Test of setGLCapabilities method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetGLCapabilities() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        builder.setGLCapabilities(new GLCapabilities(GLProfile.getDefault()));
        final Object old = builder.getGLCapabilities();
        builder.setGLCapabilities(null);
        Assert.assertNotSame(old, builder.getGLCapabilities());
    }

    /**
     * Test of setBackground method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetBackground() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final Object old = builder.getBackground();
        builder.setBackground(new Color(0.1f, 0.2f, 0.3f, 0.4f));
        Assert.assertNotSame(old, builder.getBackground());
    }

    /**
     * Test of setDebug method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetDebug() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final boolean old = builder.isDebug();
        builder.setDebug(!old);
        Assert.assertNotSame(old, builder.isDebug());
    }

    /**
     * Test of setDisplayFps method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetDisplayFps() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final boolean old = builder.isDisplayFps();
        builder.setDisplayFps(!old);
        Assert.assertNotSame(old, builder.isDisplayFps());
    }

    /**
     * Test of setMaxInitializations method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetMaxInitializations() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final int old = builder.getMaxInitializations();
        builder.setMaxInitializations(old + 1);
        Assert.assertNotSame(old, builder.getMaxInitializations());
    }

    /**
     * Test of setNumberOfSceneDrawingPasses method, of class
     * AbstractSceneBuilder.
     */
    @Test
    public void testSetNumberOfSceneDrawingPasses() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final int old = builder.getNumberOfSceneDrawingPasses();
        builder.setNumberOfSceneDrawingPasses(old + 1);
        Assert.assertNotSame(old, builder.getNumberOfSceneDrawingPasses());
    }

    /**
     * Test of setNumberOfUserInterfaceDrawingPasses method, of class
     * AbstractSceneBuilder.
     */
    @Test
    public void testSetNumberOfUserInterfaceDrawingPasses() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final int old = builder.getNumberOfUserInterfaceDrawingPasses();
        builder.setNumberOfUserInterfaceDrawingPasses(old + 1);
        Assert.assertNotSame(old, builder.getNumberOfUserInterfaceDrawingPasses());
    }

    /**
     * Test of setTargetFps method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetTargetFps() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final int old = builder.getTargetFps();
        builder.setTargetFps(old + 1);
        Assert.assertNotSame(old, builder.getTargetFps());
    }

    /**
     * Test of setVerbose method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetVerbose() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final boolean old = builder.isVerbose();
        builder.setVerbose(!old);
        Assert.assertNotSame(old, builder.isVerbose());
    }

    /**
     * Test of setAuxiliaryBuffers method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetAuxiliaryBuffers() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final boolean old = builder.isAuxiliaryBuffers();
        builder.setAuxiliaryBuffers(!old);
        Assert.assertNotSame(old, builder.isAuxiliaryBuffers());
    }

    /**
     * Test of setBlending method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetBlending() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final boolean old = builder.isBlending();
        builder.setBlending(!old);
        Assert.assertNotSame(old, builder.isBlending());
    }

    /**
     * Test of setDebugColorPicking method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetColorPicking() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final boolean old = builder.isDebugColorPicking();
        builder.setDebugColorPicking(!old);
        Assert.assertNotSame(old, builder.isDebugColorPicking());
    }

    /**
     * Test of setFullScreenAntiAliasing method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetFullScrenAntiAliasing() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final boolean old = builder.isFullScreenAntiAliasing();
        builder.setFullScreenAntiAliasing(!old);
        Assert.assertNotSame(old, builder.isFullScreenAntiAliasing());
    }

    /**
     * Test of setLighting method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetLighting() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final boolean old = builder.isLighting();
        builder.setLighting(!old);
        Assert.assertNotSame(old, builder.isLighting());
    }

    /**
     * Test of setDrawableTypes method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetDrawableTypes() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final String old = builder.getDrawableTypes();
        builder.setDrawableTypes(null);
        Assert.assertNotSame(old, builder.getDrawableTypes());
    }

    /**
     * Test of setHints method, of class AbstractSceneBuilder.
     */
    @Test
    public void testSetHints() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        final String old = builder.getHints();
        builder.setHints(CameraHint.SIMPLE.getClass() + "." + CameraHint.SIMPLE.name());
        Assert.assertNotSame(old, builder.getDrawableTypes());
    }

    @Test
    public void testCopyDrawableTypes() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        builder.updateDrawableTypes(Actor.class);
        Assert.assertArrayEquals(new Class[]{Actor.class}, builder.copyDrawableTypes());
    }

    @Test
    public void testSetHint() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        builder.addHint(CameraHint.SIMPLE);
        builder.addHint(CameraHint.DRIVING);
        builder.addHint(CameraHint.ORBIT);
        Assert.assertEquals(EnumSet.of(CameraHint.SIMPLE, CameraHint.DRIVING, CameraHint.ORBIT), builder.copyHints(CameraHint.class));
        builder.setHint(CameraHint.NONE);
        Assert.assertEquals(EnumSet.of(CameraHint.NONE), builder.copyHints(CameraHint.class));
    }

    @Test
    public void testAddHint() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        builder.addHint(CameraHint.SIMPLE);
        Assert.assertEquals(EnumSet.of(CameraHint.SIMPLE), builder.copyHints(CameraHint.class));
        builder.addHint(CameraHint.DRIVING);
        builder.addHint(CameraHint.ORBIT);
        Assert.assertEquals(EnumSet.of(CameraHint.SIMPLE, CameraHint.DRIVING, CameraHint.ORBIT), builder.copyHints(CameraHint.class));
    }

    @Test
    public void testRemoveHint() {
        final AbstractSceneBuilder<?> builder = newValueObject();
        builder.addHint(CameraHint.SIMPLE);
        builder.removeHint(CameraHint.SIMPLE);
        Assert.assertEquals(EnumSet.noneOf(CameraHint.class), builder.copyHints(CameraHint.class));
    }

    @Override
    protected AbstractSceneBuilder<?> copyValueObject(final AbstractSceneBuilder<?> object) {
        return new AbstractSceneBuilderImpl(object);
    }

    @Override
    protected AbstractSceneBuilder<?> newValueObject() {
        final AbstractSceneBuilderImpl builder = new AbstractSceneBuilderImpl();
        builder.setAuxiliaryBuffers(random.nextBoolean());
        builder.setBackground(ColorUtil.createRandomColor(0.0f, 1.0f));
        builder.setBlending(random.nextBoolean());
        builder.setDebugColorPicking(random.nextBoolean());
        builder.setDebug(random.nextBoolean());
        builder.setDisplayFps(random.nextBoolean());
        builder.setFullScreenAntiAliasing(random.nextBoolean());
        builder.setLighting(random.nextBoolean());
        builder.setMaxInitializations(random.nextInt(Integer.MAX_VALUE));
        builder.setNumberOfSceneDrawingPasses(random.nextInt(100));
        builder.setNumberOfUserInterfaceDrawingPasses(random.nextInt(100));
        builder.setTargetFps(random.nextInt(1000));
        builder.setVerbose(random.nextBoolean());
        builder.setHints("");
        return builder;
    }

    @Override
    protected void setFieldsToNull(final AbstractSceneBuilder<?> object) {
        // no operation
    }

    protected static class AbstractSceneBuilderImpl extends AbstractSceneBuilder<AbstractSceneBuilderImpl> {

        protected AbstractSceneBuilderImpl() {
        }

        protected AbstractSceneBuilderImpl(final SceneBuilder sceneBuilder) {
            super(sceneBuilder);
        }
    }
}
