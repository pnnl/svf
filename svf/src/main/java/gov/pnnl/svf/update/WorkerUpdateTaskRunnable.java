package gov.pnnl.svf.update;

/**
 * Runnable used for work done in an external thread and during an update task.
 *
 * @author Arthur Bleeker
 */
public interface WorkerUpdateTaskRunnable {

    /**
     * Do work on the update thread. When this call returns true, run will be
     * called off of the update thread.
     *
     * @param task the task that is currently being run
     *
     * @return true if the task is finished
     */
    boolean runBefore(Task task);

    /**
     * Do work off of the update thread. When this call returns, run after will
     * be called on the update thread.
     *
     * @param task the task that is currently being run
     */
    void run(Task task);

    /**
     * Do work on the update thread. When this call returns true, the task will
     * be be marked as finished and removed from the scene.
     *
     * @param task the task that is currently being run
     *
     * @return true if the task is finished
     */
    boolean runAfter(Task task);

    /**
     * Called when the update task is disposed.
     *
     * @param task the task that is being disposed
     */
    void disposed(Task task);
}
