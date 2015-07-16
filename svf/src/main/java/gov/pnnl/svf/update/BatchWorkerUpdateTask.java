package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.Scene;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * This task is meant to be scheduled on the update thread by adding it to a
 * scene. This task will only run once and then remove itself from the scene.
 *
 * @author Arthur Bleeker
 *
 */
public class BatchWorkerUpdateTask extends AbstractWorkerTask implements BatchWorkerTask {

    protected final Collection<WorkerUpdateTaskRunnable> runnables;
    protected final Collection<WorkerUpdateTaskRunnable> preRunnables;
    protected final Collection<WorkerUpdateTaskRunnable> postRunnables;
    protected final int size;

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use cases.
     *
     * @param scene     The scene for this update tasks.
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     *
     * @return the scheduled task
     */
    public static BatchWorkerTask schedule(final Scene scene, final Collection<WorkerUpdateTaskRunnable> runnables) {
        final BatchWorkerUpdateTask task = new BatchWorkerUpdateTask(scene, runnables, 0L, true, null);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use cases.
     *
     * @param scene     The scene for this update tasks.
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     * @param delay     Minimum number of milliseconds to wait before running
     *                  the task.
     *
     * @return the scheduled task
     */
    public static BatchWorkerTask schedule(final Scene scene, final Collection<WorkerUpdateTaskRunnable> runnables, final long delay) {
        final BatchWorkerUpdateTask task = new BatchWorkerUpdateTask(scene, runnables, delay, true, null);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use cases.
     *
     * @param scene     The scene for this update tasks.
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     * @param delay     Minimum number of milliseconds to wait before running
     *                  the task.
     * @param showBusy  True if this task should show as busy
     *
     * @return the scheduled task
     */
    public static BatchWorkerTask schedule(final Scene scene, final Collection<WorkerUpdateTaskRunnable> runnables, final long delay, final boolean showBusy) {
        final BatchWorkerUpdateTask task = new BatchWorkerUpdateTask(scene, runnables, delay, showBusy, null);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle.
     *
     * @param scene     The scene for this update tasks.
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     * @param executor  The thread pool to run this task on, if null a new one
     *                  will be created
     *
     * @return the scheduled task
     */
    public static BatchWorkerTask schedule(final Scene scene, final Collection<WorkerUpdateTaskRunnable> runnables, final ExecutorService executor) {
        final BatchWorkerUpdateTask task = new BatchWorkerUpdateTask(scene, runnables, 0L, true, executor);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle.
     *
     * @param scene     The scene for this update tasks.
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     * @param delay     Minimum number of milliseconds to wait before running
     *                  the task.
     * @param executor  The thread pool to run this task on, if null a new one
     *                  will be created
     *
     * @return the scheduled task
     */
    public static BatchWorkerTask schedule(final Scene scene, final Collection<WorkerUpdateTaskRunnable> runnables, final long delay,
                                           final ExecutorService executor) {
        final BatchWorkerUpdateTask task = new BatchWorkerUpdateTask(scene, runnables, delay, true, executor);
        scene.add(task);
        return task;
    }

    /**
     * Schedules an update task to be run during the next update cycle.
     *
     * @param scene     The scene for this update tasks.
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     * @param delay     Minimum number of milliseconds to wait before running
     *                  the task.
     * @param executor  The thread pool to run this task on, if null a new one
     *                  will be created
     * @param showBusy  True if this task should show as busy
     *
     * @return the scheduled task
     */
    public static BatchWorkerTask schedule(final Scene scene, final Collection<WorkerUpdateTaskRunnable> runnables, final long delay,
                                           final boolean showBusy, final ExecutorService executor) {
        final BatchWorkerUpdateTask task = new BatchWorkerUpdateTask(scene, runnables, delay, showBusy, executor);
        scene.add(task);
        return task;
    }

    /**
     * Constructor
     *
     * @param scene     The scene for this update tasks.
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     * @param delay     Minimum number of milliseconds to wait before running
     *                  the task.
     * @param showBusy  True if this task should show as busy
     * @param executor  The thread pool to run this task on, if null a new one
     *                  will be created
     */
    BatchWorkerUpdateTask(final Scene scene, final Collection<WorkerUpdateTaskRunnable> runnables, final long delay, final boolean showBusy, final ExecutorService executor) {
        super(scene, delay, showBusy, executor);
        if (runnables == null || runnables.isEmpty()) {
            throw new NullPointerException("runnable");
        }
        size = runnables.size();
        this.runnables = new ArrayList<>(runnables);
        preRunnables = new ArrayList<>(runnables);
        postRunnables = new ArrayList<>(runnables);
    }

    @Override
    public void dispose() {
        super.dispose();
        for (final WorkerUpdateTaskRunnable runnable : runnables) {
            runnable.disposed(this);
        }
    }

    @Override
    public int getTasksRun() {
        synchronized (this) {
            return size - runnables.size();
        }
    }

    @Override
    public int getTasksSize() {
        return size;
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
                for (final Iterator<WorkerUpdateTaskRunnable> it = preRunnables.iterator(); it.hasNext();) {
                    final WorkerUpdateTaskRunnable runnable = it.next();
                    // run the pretask and if done remove it from the pre-runnables list
                    if (runnable.runBefore(this)) {
                        it.remove();
                    } else {
                        // flag this stage as not finished
                        finishedTemp = false;
                    }
                }
            } finally {
                if (finishedTemp) {
                    setStage(Stage.SCHEDULE);
                }
            }
        }
        // check for schedule
        if (Stage.SCHEDULE.equals(getStage())) {
            if (!executor.isShutdown()) {
                setStage(Stage.RUN);
                synchronized (this) {
                    for (final Iterator<WorkerUpdateTaskRunnable> it = runnables.iterator(); it.hasNext();) {
                        final WorkerUpdateTaskRunnable runnable = it.next();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    runnable.run(BatchWorkerUpdateTask.this);
                                } finally {
                                    final int remaining;
                                    synchronized (BatchWorkerUpdateTask.this) {
                                        runnables.remove(runnable);
                                        remaining = runnables.size();
                                    }
                                    if (remaining <= 0) {
                                        setStage(Stage.POST_RUN);
                                    }
                                }
                            }
                        });
                    }
                }
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
                for (final Iterator<WorkerUpdateTaskRunnable> it = postRunnables.iterator(); it.hasNext();) {
                    final WorkerUpdateTaskRunnable runnable = it.next();
                    // run the pretask and if done remove it from the pre-runnables list
                    if (runnable.runAfter(this)) {
                        it.remove();
                    } else {
                        // flag this stage as not finished
                        finishedTemp = false;
                    }
                }
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
