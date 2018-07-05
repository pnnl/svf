package gov.pnnl.svf.update;

import gov.pnnl.svf.core.util.NamedThreadFactory;
import gov.pnnl.svf.scene.Scene;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Update task manager is used to schedule a task on the update thread.
 *
 * @author Amelia Bleeker
 */
public class UpdateTaskManager implements TaskManager {

    private final Scene scene;
    private final ExecutorService executor;
    private boolean disposed = false;

    /**
     * Constructor
     *
     * @param scene reference to the parent scene
     */
    public UpdateTaskManager(final Scene scene) {
        this(scene, Runtime.getRuntime().availableProcessors() * 4);
    }

    /**
     * Constructor
     *
     * @param scene reference to the parent scene
     * @param size  the size of the thread pool for asynchronous tasks
     */
    public UpdateTaskManager(final Scene scene, final int size) {
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
        return UpdateTask.schedule(scene, runnable);
    }

    @Override
    public Task schedule(final UpdateTaskRunnable runnable, final long delay) {
        return UpdateTask.schedule(scene, runnable, delay);
    }

    @Override
    public Task schedule(final UpdateTaskRunnable runnable, final long delay, final boolean showBusy) {
        return UpdateTask.schedule(scene, runnable, delay, showBusy);
    }

    @Override
    public WorkerTask schedule(final WorkerUpdateTaskRunnable runnable) {
        return WorkerUpdateTask.schedule(scene, runnable, executor);
    }

    @Override
    public WorkerTask schedule(final WorkerUpdateTaskRunnable runnable, final long delay) {
        return WorkerUpdateTask.schedule(scene, runnable, delay, executor);
    }

    @Override
    public WorkerTask schedule(final WorkerUpdateTaskRunnable runnable, final long delay, final boolean showBusy) {
        return WorkerUpdateTask.schedule(scene, runnable, delay, showBusy, executor);
    }

    @Override
    public BatchWorkerTask schedule(final Collection<WorkerUpdateTaskRunnable> runnables) {
        return BatchWorkerUpdateTask.schedule(scene, runnables, executor);
    }

    @Override
    public BatchWorkerTask schedule(final Collection<WorkerUpdateTaskRunnable> runnables, final long delay) {
        return BatchWorkerUpdateTask.schedule(scene, runnables, delay, executor);
    }

    @Override
    public BatchWorkerTask schedule(final Collection<WorkerUpdateTaskRunnable> runnables, final long delay, final boolean showBusy) {
        return BatchWorkerUpdateTask.schedule(scene, runnables, delay, showBusy, executor);
    }
}
