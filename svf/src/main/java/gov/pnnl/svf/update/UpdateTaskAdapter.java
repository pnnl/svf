package gov.pnnl.svf.update;

/**
 * Runnable interface used to perform work on the update thread.
 *
 * @author Arthur Bleeker
 */
public abstract class UpdateTaskAdapter implements UpdateTaskRunnable {

    @Override
    public void disposed(final Task task) {
        // no default operation
    }
}
