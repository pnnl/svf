package gov.pnnl.svf.update;

/**
 * Interface for a task that performs work off of the UI thread in a scene.
 * Tasks should remain in the scene lookup until finished. Issues will occur if
 * tasks are removed by any means other than the task itself.
 *
 * @author Amelia Bleeker
 */
public interface WorkerTask extends Task {

    /**
     * Stage of the worker task.
     */
    public static enum Stage {

        /**
         * The task has not been scheduled yet.
         */
        PRE,
        /**
         * The run before stage of the worker update task.
         */
        PRE_RUN,
        /**
         * The schedule stage of the worker update task.
         */
        SCHEDULE,
        /**
         * The run stage of the worker update task.
         */
        RUN,
        /**
         * The run after stage of the worker update task.
         */
        POST_RUN,
        /**
         * The task has finished running.
         */
        POST
    }

    /**
     * @return the current stage of this task
     */
    Stage getStage();
}
