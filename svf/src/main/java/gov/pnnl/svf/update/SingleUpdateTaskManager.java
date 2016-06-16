package gov.pnnl.svf.update;

import gov.pnnl.svf.core.util.NamedThreadFactory;
import gov.pnnl.svf.scene.Scene;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Single update task manager is used to schedule a task when there should be no
 * more than one task of this type running at one time. The previous task will
 * be canceled before a new task is scheduled.
 *
 * @author Arthur Bleeker
 */
public class SingleUpdateTaskManager implements TaskManager {

    private final Scene scene;
    private Task task = null;
    private final ExecutorService executor;
    private boolean disposed = false;

    /**
     * Constructor
     *
     * @param scene reference to the parent scene
     */
    public SingleUpdateTaskManager(final Scene scene) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        executor = Executors.newSingleThreadExecutor(new NamedThreadFactory(getClass(), "Update"));
    }

    /**
     * Constructor
     *
     * @param scene reference to the parent scene
     * @param size the size of the thread pool for asynchronous tasks
     */
    public SingleUpdateTaskManager(final Scene scene, final int size) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        if (size < 1) {
            throw new IllegalArgumentException("size");
        }
        executor = Executors.newFixedThreadPool(size, new NamedThreadFactory(getClass(), "Update"));
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return disposed;
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (disposed) {
                return;
            }
            disposed = true;
        }
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    @Override
    public Task schedule(final UpdateTaskRunnable runnable) {
        return schedule(runnable, 0L, true);
    }

    @Override
    public Task schedule(final UpdateTaskRunnable runnable, final long delay) {
        return schedule(runnable, delay, true);
    }

    @Override
    public Task schedule(final UpdateTaskRunnable runnable, final long delay, final boolean showBusy) {
        final Task task = UpdateTask.schedule(scene, runnable, delay, showBusy);
        synchronized (this) {
            if (this.task != null) {
                this.task.cancel();
            }
            this.task = task;
        }
        return task;
    }

    @Override
    public WorkerTask schedule(final WorkerUpdateTaskRunnable runnable) {
        return schedule(runnable, 0L, true);
    }

    @Override
    public WorkerTask schedule(final WorkerUpdateTaskRunnable runnable, final long delay) {
        return schedule(runnable, delay, true);
    }

    @Override
    public WorkerTask schedule(final WorkerUpdateTaskRunnable runnable, final long delay, final boolean showBusy) {
        final WorkerTask task = WorkerUpdateTask.schedule(scene, runnable, delay, showBusy, executor);
        synchronized (this) {
            if (this.task != null) {
                this.task.cancel();
            }
            this.task = task;
        }
        return task;
    }

    @Override
    public BatchWorkerTask schedule(final Collection<WorkerUpdateTaskRunnable> runnables) {
        return schedule(runnables, 0L, true);
    }

    @Override
    public BatchWorkerTask schedule(final Collection<WorkerUpdateTaskRunnable> runnables, final long delay) {
        return schedule(runnables, delay, true);
    }

    @Override
    public BatchWorkerTask schedule(final Collection<WorkerUpdateTaskRunnable> runnables, final long delay, final boolean showBusy) {
        final BatchWorkerTask task = BatchWorkerUpdateTask.schedule(scene, runnables, delay, showBusy, executor);
        synchronized (this) {
            if (this.task != null) {
                this.task.cancel();
            }
            this.task = task;
        }
        return task;
    }
}
