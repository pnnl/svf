package gov.pnnl.svf.animation;

import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.camera.DraggingCameraTest;
import gov.pnnl.svf.camera.ProxyDraggingCamera;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Random;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Assert;
import org.junit.Test;

public class LocationAnimationSupportTest extends AbstractObjectTestBase<LocationAnimationSupport> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    private final DraggingCameraTestImpl cameraActorTest = new DraggingCameraTestImpl();
    final Random random = new Random();

    public LocationAnimationSupportTest() {
        collisionIterateLength = 1000;
    }

    /**
     * Test method for {link
     * gov.pnnl.svf.animation.LocationAnimationSupport#animate(float)}.
     */
    @Test
    public void testAnimate() {
        final DraggingCamera camera = new ProxyDraggingCamera(scene);
        camera.setLocation(new Vector3D(1.0, 1.0, 1.0));
        final Vector3D target = new Vector3D(0.0, 0.0, 0.0);
        final LocationAnimationSupport support = LocationAnimationSupport.newInstance(camera, 100, 0L, false, target);
        // update the color
        support.update(50);
        // check the results
        Assert.assertEquals(0.5, camera.getLocation().getX(), 0.001);
        Assert.assertEquals(0.5, camera.getLocation().getY(), 0.001);
        Assert.assertEquals(0.5, camera.getLocation().getZ(), 0.001);
    }

    @Override
    protected LocationAnimationSupport copyValueObject(final LocationAnimationSupport object) {
        return object;
    }

    @Override
    protected LocationAnimationSupport newValueObject() {
        return LocationAnimationSupport.newInstance(cameraActorTest.newValueObject(), random.nextLong(), random.nextLong(), random.nextBoolean(), new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    @Override
    protected void setFieldsToNull(final LocationAnimationSupport object) {
        // no fields to set
    }

    private class DraggingCameraTestImpl extends DraggingCameraTest {

        private DraggingCameraTestImpl() {
        }

        @Override
        public DraggingCamera newValueObject() {
            return super.newValueObject();
        }
    }
}
