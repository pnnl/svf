package gov.pnnl.svf.scene;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.ColorUtil;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Random;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ImmutableSceneBuilderTest extends AbstractObjectTestBase<ImmutableSceneBuilder> {

    private final Random random = new Random();

    /**
     * Test of setGLCapabilities method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetGLCapabilities() {
        newValueObject().setGLCapabilities(null);
    }

    /**
     * Test of setBackground method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetBackground() {
        newValueObject().setBackground(Color.WHITE);
    }

    /**
     * Test of setDebug method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetDebug() {
        newValueObject().setDebug(true);
    }

    /**
     * Test of setDisplayFps method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetDisplayFps() {
        newValueObject().setDisplayFps(true);
    }

    /**
     * Test of setMaxInitializations method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetMaxInitializations() {
        newValueObject().setMaxInitializations(0);
    }

    /**
     * Test of setNumberOfSceneDrawingPasses method, of class
     * ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetNumberOfSceneDrawingPasses() {
        newValueObject().setNumberOfSceneDrawingPasses(0);
    }

    /**
     * Test of setNumberOfUserInterfaceDrawingPasses method, of class
     * ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetNumberOfUserInterfaceDrawingPasses() {
        newValueObject().setNumberOfUserInterfaceDrawingPasses(0);
    }

    /**
     * Test of setTargetFps method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetTargetFps() {
        newValueObject().setTargetFps(0);
    }

    /**
     * Test of setVerbose method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetVerbose() {
        newValueObject().setVerbose(true);
    }

    /**
     * Test of setAuxiliaryBuffers method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAuxiliaryBuffers() {
        newValueObject().setAuxiliaryBuffers(true);
    }

    /**
     * Test of setBlending method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetBlending() {
        newValueObject().setBlending(true);
    }

    /**
     * Test of setDebugColorPicking method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetColorPicking() {
        newValueObject().setDebugColorPicking(true);
    }

    /**
     * Test of setFullScreenAntiAliasing method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFullScrenAntiAliasing() {
        newValueObject().setFullScreenAntiAliasing(true);
    }

    /**
     * Test of setLighting method, of class ImmutableSceneBuilder.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetLighting() {
        newValueObject().setLighting(true);
    }

    @Override
    protected ImmutableSceneBuilder copyValueObject(final ImmutableSceneBuilder object) {
        return new ImmutableSceneBuilder(object);
    }

    @Override
    protected ImmutableSceneBuilder newValueObject() {
        final SceneBuilder sceneBuilder = ConfigUtil.configure();
        sceneBuilder.setAuxiliaryBuffers(random.nextBoolean());
        sceneBuilder.setBackground(ColorUtil.createRandomColor(0.0f, 1.0f));
        sceneBuilder.setBlending(random.nextBoolean());
        sceneBuilder.setDebugColorPicking(random.nextBoolean());
        sceneBuilder.setDebug(random.nextBoolean());
        sceneBuilder.setDisplayFps(random.nextBoolean());
        sceneBuilder.setFullScreenAntiAliasing(random.nextBoolean());
        sceneBuilder.setLighting(random.nextBoolean());
        sceneBuilder.setMaxInitializations(random.nextInt(Integer.MAX_VALUE));
        sceneBuilder.setNumberOfSceneDrawingPasses(random.nextInt(100));
        sceneBuilder.setNumberOfUserInterfaceDrawingPasses(random.nextInt(100));
        sceneBuilder.setTargetFps(random.nextInt(1000));
        sceneBuilder.setVerbose(random.nextBoolean());
        return new ImmutableSceneBuilder(sceneBuilder);
    }

    @Override
    protected void setFieldsToNull(final ImmutableSceneBuilder object) {
        // no operation
    }
}
