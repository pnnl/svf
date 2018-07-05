package gov.pnnl.svf.update;

/**
 * Runnable interface used to perform work on the update thread.
 *
 * @author Amelia Bleeker
 */
public interface UpdateTaskRunnable {

    /**
     * Runs the update task on the update thread that is lock stepped with the
     * OpenGL display thread.
     *
     * @param task the task that is currently being run
     *
     * @return true if the task is finished
     */
    boolean run(Task task);

    /**
     * Called when the update task is disposed.
     *
     * @param task the task that is being disposed
     */
    void disposed(Task task);
}
