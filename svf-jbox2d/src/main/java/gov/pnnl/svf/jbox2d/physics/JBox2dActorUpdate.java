package gov.pnnl.svf.jbox2d.physics;

import gov.pnnl.svf.animation.RotationAnimationSupport;
import gov.pnnl.svf.animation.TranslationAnimationSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.update.Task;
import gov.pnnl.svf.update.UpdateTaskRunnable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math.geometry.Vector3D;

/**
 * This class will update all of the actors in the scene after physics has
 * determined where the actors should be placed.
 *
 * @author Amelia
 */
class JBox2dActorUpdate implements UpdateTaskRunnable {

    private final List<UpdateInfo> updateInfos = new ArrayList<>();
    private final boolean animate;
    private final long time;

    /**
     * Constructor that will not animate objects. Objects will be placed
     * directly into the new location.
     */
    JBox2dActorUpdate() {
        animate = false;
        time = 0L;
    }

    /**
     * Constructor that will animate objects. Objects will be animated into the
     * new location over the specified time.
     *
     * @param time the amount of milliseconds to take to animate the objects.
     */
    JBox2dActorUpdate(final long time) {
        animate = true;
        this.time = time;
    }

    void addUpdateInfo(final JBox2dPhysicsSupport support, final TransformSupport transform, final float x, final float y, final float r) {
        updateInfos.add(new UpdateInfo(support, transform, x, y, r));
    }

    @Override
    public void disposed(final Task task) {
        // no operation
    }

    @Override
    public boolean run(final Task task) {
        if (!animate) {
            // just move the objects directly
            for (final UpdateInfo updateInfo : updateInfos) {
                updateInfo.transform.setTranslation(new Vector3D(updateInfo.x, updateInfo.y, updateInfo.transform.getTranslation().getZ()));
                if (updateInfo.support.isApplyRotation()) {
                    updateInfo.transform.setRotation(Math.toDegrees(updateInfo.r));
                }
            }
        } else {
            // animate the objects to the new spot
            for (final UpdateInfo updateInfo : updateInfos) {
                TranslationAnimationSupport.newInstance(updateInfo.transform.getActor(), time, 0L, false, new Vector3D(updateInfo.x, updateInfo.y,
                                                                                                                       updateInfo.transform.getTranslation().getZ()));
                if (updateInfo.support.isApplyRotation()) {
                    RotationAnimationSupport.newInstance(updateInfo.transform.getActor(), time, 0L, false, Math.toDegrees(updateInfo.r));
                }
            }
        }
        return true;
    }

    private static class UpdateInfo {

        private final JBox2dPhysicsSupport support;
        private final TransformSupport transform;
        private final float x;
        private final float y;
        private final float r;

        private UpdateInfo(final JBox2dPhysicsSupport support, final TransformSupport transform, final float x, final float y, final float r) {
            this.support = support;
            this.transform = transform;
            this.x = x;
            this.y = y;
            this.r = r;
        }
    }
}
