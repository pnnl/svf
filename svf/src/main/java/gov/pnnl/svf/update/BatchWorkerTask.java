package gov.pnnl.svf.update;

/**
 * Interface used for a batch worker task.
 *
 * @author Arthur Bleeker
 */
public interface BatchWorkerTask extends WorkerTask {

    /**
     * Get the number of tasks that have been run.
     *
     * @return the number of tasks run
     */
    int getTasksRun();

    /**
     * Get the total number of tasks in this batch.
     *
     * @return the number of tasks
     */
    int getTasksSize();
}
