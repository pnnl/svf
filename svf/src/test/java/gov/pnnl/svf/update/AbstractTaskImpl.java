package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.Scene;

/**
 *
 * @author Arthur Bleeker
 */
public class AbstractTaskImpl extends AbstractTask {

    public AbstractTaskImpl(final Scene scene) {
        super(scene, 0L, true);
    }

    @Override
    public void run() {
    }

    @Override
    public void setCanceled(final boolean canceled) {
        super.setCanceled(canceled);
    }

    @Override
    public void setFinished(final boolean finished) {
        super.setFinished(finished);
    }
}
