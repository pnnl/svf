package gov.pnnl.svf.event;

import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.picking.ProxyPickingCamera;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.Random;

/**
 * Test class for ProxyMouseEvent.
 *
 * @author Arthur Bleeker
 */
public class ProxyMouseEventTest extends AbstractObjectTestBase<ProxyMouseEvent> {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    final SimpleCamera camera = new SimpleCamera(scene);
    final PickingCamera picking = new ProxyPickingCamera(scene, camera);
    private final Random random = new Random();

    public ProxyMouseEventTest() {
    }

    @Override
    protected ProxyMouseEvent copyValueObject(final ProxyMouseEvent object) {
        return ProxyMouseEvent.Builder.construct()
                .x(object.getX())
                .y(object.getY())
                .button(object.getButton())
                .clickCount(object.getClickCount())
                .wheelRotation(object.getWheelRotation())
                .alt(object.isAltDown())
                .control(object.isControlDown())
                .shift(object.isShiftDown())
                .build();
    }

    @Override
    protected ProxyMouseEvent newValueObject() {
        if (random.nextBoolean()) {
            return new ProxyMouseEvent(random.nextInt(10) - 5, random.nextInt(10000) - 5000, random.nextInt(10000) - 5000, random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
        } else {
            return new ProxyMouseEvent(ProxyMouseEvent.Type.values()[random.nextInt(ProxyMouseEvent.Type.values().length - 1)], random.nextInt(3) + 1, random.nextInt(10000) - 5000, random.nextInt(10000) - 5000, random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
        }
    }

    @Override
    protected void setFieldsToNull(final ProxyMouseEvent object) {
        // class is immutable
    }
}
