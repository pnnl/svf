package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.Scene;
import java.util.concurrent.ExecutorService;

/**
 * This task is meant to be scheduled on the update thread by adding it to a
 * scene. This task will only run once and then remove itself from the scene.
 * This task type will perform work off of the UI thread in a scene. Tasks
 * should remain in the scene lookup until finished. Issues will occur if tasks
 * are removed by any means other than the task itself.
 *
 * @author Arthur Bleeker
 *
 */
public class WorkerUpdateTask extends AbstractWorkerTask {

    protected final WorkerUpdateTaskRunnable runnable;

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use cases.
     *
     * @param scene    The scene for this update tasks.
     * @param runnable The runnable that will be run as part of the task.
     *
     * @return the scheduled task
     */
    public static WorkerTask schedule(final Scene scene, final WorkerUpdateTaskRunnable runnable) {
        final WorkerUpdateTask task = new WorkerUpdateTask(scene, runnable, 0L, true, null);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use cases.
     *
     * @param scene    The scene for this update tasks.
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     *
     * @return the scheduled task
     */
    public static WorkerTask schedule(final Scene scene, final WorkerUpdateTaskRunnable runnable, final long delay) {
        final WorkerUpdateTask task = new WorkerUpdateTask(scene, runnable, delay, true, null);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use cases.
     *
     * @param scene    The scene for this update tasks.
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     * @param showBusy True if this task should show as busy
     *
     * @return the scheduled task
     */
    public static WorkerTask schedule(final Scene scene, final WorkerUpdateTaskRunnable runnable, final long delay, final boolean showBusy) {
        final WorkerUpdateTask task = new WorkerUpdateTask(scene, runnable, delay, showBusy, null);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle.
     *
     * @param scene    The scene for this update tasks.
     * @param runnable The runnable that will be run as part of the task.
     * @param executor The thread pool to run this task on, if null a new one
     *                 will be created
     *
     * @return the scheduled task
     */
    public static WorkerTask schedule(final Scene scene, final WorkerUpdateTaskRunnable runnable, final ExecutorService executor) {
        final WorkerUpdateTask task = new WorkerUpdateTask(scene, runnable, 0L, true, executor);
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
     * @param executor The thread pool to run this task on, if null a new one
     *                 will be created
     *
     * @return the scheduled task
     */
    public static WorkerTask schedule(final Scene scene, final WorkerUpdateTaskRunnable runnable, final long delay, final ExecutorService executor) {
        final WorkerUpdateTask task = new WorkerUpdateTask(scene, runnable, delay, true, executor);
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
     * @param executor The thread pool to run this task on, if null a new one
     *                 will be created
     *
     * @return the scheduled task
     */
    public static WorkerTask schedule(final Scene scene, final WorkerUpdateTaskRunnable runnable, final long delay, final boolean showBusy, final ExecutorService executor) {
        final WorkerUpdateTask task = new WorkerUpdateTask(scene, runnable, delay, showBusy, executor);
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
     * @param executor The thread pool to run this task on, if null a new one
     *                 will be created
     */
    WorkerUpdateTask(final Scene scene, final WorkerUpdateTaskRunnable runnable, final long delay, final boolean showBusy, final ExecutorService executor) {
        super(scene, delay, showBusy, executor);
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
        // move to pre run if it hasn't started yet
        if (Stage.PRE.equals(getStage())) {
            setStage(Stage.PRE_RUN);
        }
        // check for pre run
        if (Stage.PRE_RUN.equals(getStage())) {
            boolean finishedTemp = true;
            try {
                // run the task and determine if we should go to the next step
                finishedTemp = runnable.runBefore(this);
            } finally {
                if (finishedTemp) {
                    setStage(Stage.SCHEDULE);
                }
            }
        }
        // check for schedule stage
        if (Stage.SCHEDULE.equals(getStage())) {
            if (!executor.isShutdown()) {
                setStage(Stage.RUN);
                executor.execute(() -> {
                    if (isCanceled()) {
                        return;
                    }
                    try {
                        runnable.run(WorkerUpdateTask.this);
                    } finally {
                        setStage(Stage.POST_RUN);
                    }
                });
            } else {
                // executor is shutdown
                cancel();
                return;
            }
        }
        // running
        //        if (Stage.RUN.equals(getStage())) {
        //            // just wait for it to complete
        //        }
        // check for post run stage
        if (Stage.POST_RUN.equals(getStage())) {
            boolean finishedTemp = true;
            try {
                // run the task and determine if we should go to the next step
                finishedTemp = runnable.runAfter(this);
            } finally {
                if (finishedTemp) {
                    setStage(Stage.POST);
                }
            }
        }
        // check for finish
        if (Stage.POST.equals(getStage())) {
            setFinished(true);
        }
    }

    @Override
    public String toString() {
        return "WorkerUpdateTask{" + "updating=" + isUpdating() + ", finished=" + isFinished() + ", canceled=" + isCanceled() + ", stage=" + getStage() + '}';
    }
}
