package gov.pnnl.svf.picking;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Arthur Bleeker
 *
 */
public class PickingSupportTest {

    private final Scene scene = new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure());
    final SimpleCamera camera = new SimpleCamera(scene);
    final PickingCamera picking = new AbstractPickingCamera(scene, camera) {
    };

    public PickingSupportTest() {
    }

    /**
     * Test method for
     * {@link gov.pnnl.canopy.jogl.ui.support.PickingSupport#notifyPicked(gov.pnnl.canopy.jogl.ui.camera.PickingCamera.CameraEventType)}.
     */
    @Test
    public void testNotifyPicked() {
        final ShapeActor actor = new ShapeActor(scene);
        actor.setShape(Rectangle2D.ONE);
        final PickingSupport support = PickingSupport.newInstance(actor);
        final AtomicBoolean passed = new AtomicBoolean(false);
        support.addListener(new PickingSupportListener() {
            @Override
            public void picked(final Actor actor, final PickingCameraEvent event) {
                if (event.getTypes().containsAll(EnumSet.of(CameraEventType.RIGHT, CameraEventType.SINGLE, CameraEventType.SHIFT))) {
                    passed.set(true);
                }
            }
        });
        support.notifyPicked(new PickingCameraEvent(picking, 0, 0, 1, EnumSet.of(CameraEventType.SINGLE, CameraEventType.RIGHT, CameraEventType.SHIFT)));
        Assert.assertTrue(passed.get());
    }
}
