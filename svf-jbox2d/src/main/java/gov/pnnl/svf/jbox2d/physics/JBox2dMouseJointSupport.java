package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.event.CameraEventType;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.picking.PickingCameraAdapter;
import gov.pnnl.svf.support.MatrixTransformSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.SceneCoordsHelper;
import org.apache.commons.math.geometry.Vector3D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.MouseJoint;

/**
 * Support object for creating a mouse joint between an actor and a mouse.
 *
 * @author Amelia Bleeker
 */
public class JBox2dMouseJointSupport extends JBox2dJointSupport {

    private final PickingCameraListenerImpl listener;
    private final Vector3D anchor;

    private JBox2dMouseJointSupport(final PickingCamera camera, final JBox2dJointType type, final JBox2dPhysicsSupport support, final Vector3D anchor) {
        super(camera, type, support, support);
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }
        this.anchor = anchor;
        listener = new PickingCameraListenerImpl(this);
        ((PickingCamera) getActor()).addListener(listener);
        //        System.out.println("Mouse joint added");
    }

    /**
     * Constructor for a joint with no limit, motor, or desired angle.
     *
     * @param camera The picking camera being used to drag the actor
     * @param actor  The actor being dragged
     * @param anchor the anchor point for the joint in world coordinates
     *
     * @return the newly created support object
     */
    public static JBox2dMouseJointSupport newInstance(final PickingCamera camera, final Actor actor, final Vector3D anchor) {
        final JBox2dPhysicsSupport support = actor.lookup(JBox2dPhysicsSupport.class);
        final JBox2dMouseJointSupport instance = new JBox2dMouseJointSupport(camera, JBox2dJointType.MOUSE, support, anchor);
        final JBox2dPhysicsEngine engine = actor.getScene().lookup(JBox2dPhysicsEngine.class);
        if (engine != null) {
            engine.addJoint(instance);
        }
        return instance;
    }

    @Override
    public void dispose() {
        super.dispose();
        ((PickingCamera) getActor()).removeListener(listener);
    }

    /**
     * The initial anchor point.
     *
     * @return the anchor
     */
    public Vector3D getAnchor() {
        return anchor;
    }

    private static class PickingCameraListenerImpl extends PickingCameraAdapter {

        private final double[] modelview = new double[16];
        private final double[] projection = new double[16];
        private final SceneCoordsHelper helper;
        private final JBox2dMouseJointSupport joint;

        private PickingCameraListenerImpl(final JBox2dMouseJointSupport joint) {
            helper = new SceneCoordsHelper(joint.getActor().getScene());
            this.joint = joint;
        }

        @Override
        public void picked(final PickingCameraEvent event) {
            // the target needs to get updated when the mouse moves
            if (event.getTypes().contains(CameraEventType.MOVE)) {
                final MouseJoint data = (MouseJoint) joint.getPhysicsData();
                if (data != null) {
                    final MatrixTransformSupport cameraTransform = joint.getActor().lookup(MatrixTransformSupport.class);
                    final TransformSupport actorTransform = joint.getActor().lookup(TransformSupport.class);
                    // find the coordinates of the mouse in world space
                    if (cameraTransform != null) {
                        cameraTransform.getModelviewMatrix(modelview);
                        cameraTransform.getProjectionMatrix(projection);
                        final double cameraZ = cameraTransform.getTranslation().getZ();
                        final Rectangle rectangle = joint.getActor().getScene().getViewport();
                        final int[] viewport = new int[]{rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()};
                        final Vector3D location = helper.unProject(event.getX(),
                                                                   event.getY(),
                                                                   Math.abs(cameraZ - (actorTransform != null ? actorTransform.getTranslation().getZ() : 0.0)),
                                                                   modelview, projection, viewport);
                        //                        System.out.println("Target set for: " + location);
                        data.setTarget(new Vec2((float) location.getX(), (float) location.getY()));
                    }
                }
            }
            // remove the joint when a button is no longer pressed
            if (event.getTypes().contains(CameraEventType.NONE)) {
                joint.getActor().remove(joint);
                joint.dispose();
                //                System.out.println("Mouse joint removed");
            }
        }

        @Override
        public void nothingPicked(final PickingCameraEvent event) {
            // remove the joint when a button is no longer pressed
            if (event.getTypes().contains(CameraEventType.LEFT)) {
                joint.getActor().remove(joint);
                joint.dispose();
                //                System.out.println("Mouse joint removed");
            }
        }
    }
}
