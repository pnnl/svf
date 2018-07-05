package gov.pnnl.svf.update;

/**
 * Runnable used for work done in an external thread and during an update task.
 *
 * @author Amelia Bleeker
 */
public abstract class WorkerUpdateTaskAdapter implements WorkerUpdateTaskRunnable {

    @Override
    public boolean runBefore(final Task task) {
        // no default operation
        return true;
    }

    @Override
    public boolean runAfter(final Task task) {
        // no default operation
        return true;
    }

    @Override
    public void disposed(final Task task) {
        // no default operation
    }
}
