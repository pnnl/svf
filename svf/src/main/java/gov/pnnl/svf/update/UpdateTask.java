package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.Scene;

/**
 * This task is meant to be scheduled on the update thread by adding it to a
 * scene. This task will only run once and then remove itself from the scene.
 * This task type will perform work in lockstep with the UI thread in a scene.
 * Tasks should remain in the scene lookup until finished. Issues will occur if
 * tasks are removed by any means other than the task itself.
 *
 * @author Arthur Bleeker
 *
 */
public class UpdateTask extends AbstractTask {

    private final UpdateTaskRunnable runnable;

    /**
     * Schedules an update task to be run during the next update cycle.
     *
     * @param scene    The scene for this update tasks.
     * @param runnable The runnable that will be run as part of the task.
     *
     * @return the scheduled task
     */
    public static Task schedule(final Scene scene, final UpdateTaskRunnable runnable) {
        final UpdateTask task = new UpdateTask(scene, runnable, 0L, true);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle.
     *
     * @param scene    The scene for this update tasks.
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     *
     * @return the scheduled task
     */
    public static Task schedule(final Scene scene, final UpdateTaskRunnable runnable, final long delay) {
        final UpdateTask task = new UpdateTask(scene, runnable, delay, true);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle.
     *
     * @param scene    The scene for this update tasks.
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     * @param showBusy True if this task should show as busy
     *
     * @return the scheduled task
     */
    public static Task schedule(final Scene scene, final UpdateTaskRunnable runnable, final long delay, final boolean showBusy) {
        final UpdateTask task = new UpdateTask(scene, runnable, delay, showBusy);
        scene.add(task);
        return task;
    }

    /**
     * Constructor
     *
     * @param scene    The scene for this update tasks.
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     * @param showBusy True if this task should show as busy
     */
    UpdateTask(final Scene scene, final UpdateTaskRunnable runnable, final long delay, final boolean showBusy) {
        super(scene, delay, showBusy);
        if (runnable == null) {
            throw new NullPointerException("runnable");
        }
        this.runnable = runnable;
    }

    @Override
    public void dispose() {
        super.dispose();
        runnable.disposed(this);
    }

    @Override
    protected void run() {
        if (isDisposed()) {
            return;
        }
        boolean finishedTemp = true;
        try {
            // run the task and remove it from the scene
            finishedTemp = runnable.run(this);
        } finally {
            setFinished(finishedTemp);
        }
    }

    @Override
    public String toString() {
        return "UpdateTask{" + "updating=" + isUpdating() + ", finished=" + isFinished() + ", canceled=" + isCanceled() + '}';
    }
}
