package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.Scene;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract task type for all worker tasks.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractWorkerTask extends AbstractTask implements WorkerTask {

    protected final ExecutorService executor;
    private Stage stage = Stage.PRE;

    /**
     * Constructor
     *
     * @param scene    The scene for this update tasks.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     * @param showBusy True if this task should show as busy
     * @param executor The thread pool to run this task on, if null a new one
     *                 will be created
     */
    protected AbstractWorkerTask(final Scene scene, final long delay, final boolean showBusy, final ExecutorService executor) {
        super(scene, delay, showBusy);
        if (executor == null) {
            this.executor = Executors.newSingleThreadExecutor();
        } else {
            this.executor = executor;
        }
    }

    @Override
    public Stage getStage() {
        synchronized (this) {
            return stage;
        }
    }

    /**
     * Set the stage.
     *
     * @param stage the stage to set
     */
    protected void setStage(final Stage stage) {
        synchronized (this) {
            this.stage = stage;
        }
    }
}
