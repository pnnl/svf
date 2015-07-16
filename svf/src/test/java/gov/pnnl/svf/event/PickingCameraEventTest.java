package gov.pnnl.svf.event;

import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.picking.AbstractPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Collections;
import java.util.Random;

/**
 * Test class for PickingCameraEvent.
 *
 * @author Arthur Bleeker
 */
public class PickingCameraEventTest extends AbstractObjectTestBase<PickingCameraEvent> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    final SimpleCamera camera = new SimpleCamera(scene);
    final PickingCamera picking = new AbstractPickingCamera(scene, camera) {
    };
    private final Random random = new Random();

    public PickingCameraEventTest() {
    }

    @Override
    protected PickingCameraEvent copyValueObject(final PickingCameraEvent object) {
//        return new PickingCameraEvent(picking, object.getX(), object.getY(), object.getWidth(), object.getHeight(), object.getClicks(), object.getTypes());
        return PickingCameraEvent.Builder.construct()
                .source(object.getSource())
                .y(object.getY())
                .x(object.getX())
                .width(object.getWidth())
                .height(object.getHeight())
                .clicks(object.getClicks())
                .types(object.getTypes())
                .build();
    }

    @Override
    protected PickingCameraEvent newValueObject() {
        if (random.nextBoolean()) {
            return new PickingCameraEvent(picking, random.nextInt(100000), random.nextInt(100000), random.nextInt(10) + 1, random.nextInt(10) + 1, random.nextInt(10),
                                          Collections.singleton(CameraEventType.values()[random.nextInt(CameraEventType.values().length)]));
        } else {
            return new PickingCameraEvent(picking, random.nextInt(100000), random.nextInt(100000), random.nextInt(10), Collections.singleton(CameraEventType.values()[random
                                          .nextInt(CameraEventType.values().length)]));
        }
    }

    @Override
    protected void setFieldsToNull(final PickingCameraEvent object) {
        // class is immutable
    }
}
