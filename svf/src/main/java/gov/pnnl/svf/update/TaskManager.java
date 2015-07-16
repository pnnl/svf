package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.Disposable;
import java.util.Collection;

/**
 * Manager for tasks that perform work off the UI thread in a scene.
 *
 * @author Arthur Bleeker
 */
public interface TaskManager extends Disposable {

    /**
     * Schedules an update task to be run during the next update cycle.Tasks
     * should remain in the scene lookup until finished. Issues will occur if
     * tasks are removed by any means other than the task itself.
     *
     * @param runnable The runnable that will be run as part of the task.
     *
     * @return the scheduled task
     */
    Task schedule(UpdateTaskRunnable runnable);

    /**
     * Schedules an update task to be run during the next update cycle.Tasks
     * should remain in the scene lookup until finished. Issues will occur if
     * tasks are removed by any means other than the task itself.
     *
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     *
     * @return the scheduled task
     */
    Task schedule(UpdateTaskRunnable runnable, long delay);

    /**
     * Schedules an update task to be run during the next update cycle.Tasks
     * should remain in the scene lookup until finished. Issues will occur if
     * tasks are removed by any means other than the task itself.
     *
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     * @param showBusy True if this task should show as busy
     *
     * @return the scheduled task
     */
    Task schedule(UpdateTaskRunnable runnable, long delay, boolean showBusy);

    /**
     * Schedules an update task to be run on an external thread and during the
     * next update cycle.Tasks should remain in the scene lookup until finished.
     * Issues will occur if tasks are removed by any means other than the task
     * itself.
     *
     * @param runnable The runnable that will be run as part of the task.
     *
     * @return the scheduled task
     */
    WorkerTask schedule(WorkerUpdateTaskRunnable runnable);

    /**
     * Schedules an update task to be run on an external thread and during the
     * next update cycle.Tasks should remain in the scene lookup until finished.
     * Issues will occur if tasks are removed by any means other than the task
     * itself.
     *
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     *
     * @return the scheduled task
     */
    WorkerTask schedule(WorkerUpdateTaskRunnable runnable, long delay);

    /**
     * Schedules an update task to be run on an external thread and during the
     * next update cycle.Tasks should remain in the scene lookup until finished.
     * Issues will occur if tasks are removed by any means other than the task
     * itself.
     *
     * @param runnable The runnable that will be run as part of the task.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     * @param showBusy True if this task should show as busy
     *
     * @return the scheduled task
     */
    WorkerTask schedule(WorkerUpdateTaskRunnable runnable, long delay, boolean showBusy);

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use
     * cases.Tasks should remain in the scene lookup until finished. Issues will
     * occur if tasks are removed by any means other than the task itself.
     *
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     *
     * @return the scheduled task
     */
    BatchWorkerTask schedule(Collection<WorkerUpdateTaskRunnable> runnables);

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use
     * cases.Tasks should remain in the scene lookup until finished. Issues will
     * occur if tasks are removed by any means other than the task itself.
     *
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     * @param delay     Minimum number of milliseconds to wait before running
     *                  the task.
     *
     * @return the scheduled task
     */
    BatchWorkerTask schedule(Collection<WorkerUpdateTaskRunnable> runnables, long delay);

    /**
     * Schedules an update task to be run during the next update cycle. This
     * will create a new thread pool and is not recommended for most use
     * cases.Tasks should remain in the scene lookup until finished. Issues will
     * occur if tasks are removed by any means other than the task itself.
     *
     * @param runnables The collection of runnables that will be run as part of
     *                  the task.
     * @param delay     Minimum number of milliseconds to wait before running
     *                  the task.
     * @param showBusy  True if this task should show as busy
     *
     * @return the scheduled task
     */
    BatchWorkerTask schedule(Collection<WorkerUpdateTaskRunnable> runnables, long delay, boolean showBusy);
}
