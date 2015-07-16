package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
@Ignore
public class BatchWorkerUpdateTaskTest {

    private static final long DELAY_TIME = 100L;
    private static final long DELAY_SLEEP_TIME = 50L;
    private static final long WAIT_SLEEP_TIME = 10L;
    private static final long WAIT_TIMEOUT = 60L * 1000L;
    private static final int RUNNABLE_COUNT = 10;
    private Scene scene;

    @Before
    public void beforeTest() {
        scene = ProxyScene.newInstance(new ProxyGLCanvas(), ConfigUtil.configure());
    }

    @After
    public void afterTest() {
        if (scene != null) {
            scene.dispose();
        }
    }

    @Test
    public void testSchedule_Scene_UpdateTaskRunnable() {
        final AtomicInteger before = new AtomicInteger(0);
        final AtomicInteger run = new AtomicInteger(0);
        final AtomicInteger after = new AtomicInteger(0);
        final WorkerUpdateTaskRunnable runnable = new WorkerUpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean runBefore(final Task task) {
                if (task.isUpdating()) {
                    before.incrementAndGet();
                }
                return true;
            }

            @Override
            public void run(final Task task) {
                run.incrementAndGet();
            }

            @Override
            public boolean runAfter(final Task task) {
                if (task.isUpdating()) {
                    after.incrementAndGet();
                }
                return true;
            }
        };
        final List<WorkerUpdateTaskRunnable> runnables = new ArrayList<>(RUNNABLE_COUNT);
        for (int i = 0; i < RUNNABLE_COUNT; i++) {
            runnables.add(runnable);
        }
        final Task task = BatchWorkerUpdateTask.schedule(scene, runnables);
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < WAIT_TIMEOUT) {
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (final InterruptedException ex) {
                // ignore
            }
            if (before.get() == RUNNABLE_COUNT && run.get() == RUNNABLE_COUNT && after.get() == RUNNABLE_COUNT) {
                break;
            }
        }
        Assert.assertEquals(RUNNABLE_COUNT, before.get());
        Assert.assertEquals(RUNNABLE_COUNT, run.get());
        Assert.assertEquals(RUNNABLE_COUNT, after.get());
        Assert.assertTrue(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertFalse(task.isCanceled());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }

    @Test
    public void testSchedule_Scene_UpdateTaskRunnable_Long() {
        final AtomicInteger before = new AtomicInteger(0);
        final AtomicInteger run = new AtomicInteger(0);
        final AtomicInteger after = new AtomicInteger(0);
        final WorkerUpdateTaskRunnable runnable = new WorkerUpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean runBefore(final Task task) {
                if (task.isUpdating()) {
                    before.incrementAndGet();
                }
                return true;
            }

            @Override
            public void run(final Task task) {
                run.incrementAndGet();
            }

            @Override
            public boolean runAfter(final Task task) {
                if (task.isUpdating()) {
                    after.incrementAndGet();
                }
                return true;
            }
        };
        final List<WorkerUpdateTaskRunnable> runnables = new ArrayList<>(RUNNABLE_COUNT);
        for (int i = 0; i < RUNNABLE_COUNT; i++) {
            runnables.add(runnable);
        }
        final Task task = BatchWorkerUpdateTask.schedule(scene, runnables, DELAY_TIME);
        try {
            Thread.sleep(DELAY_SLEEP_TIME);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertEquals(0, before.get());
        Assert.assertEquals(0, run.get());
        Assert.assertEquals(0, after.get());
        Assert.assertFalse(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertFalse(task.isCanceled());
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < WAIT_TIMEOUT) {
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (final InterruptedException ex) {
                // ignore
            }
            if (before.get() == RUNNABLE_COUNT && run.get() == RUNNABLE_COUNT && after.get() == RUNNABLE_COUNT) {
                break;
            }
        }
        Assert.assertEquals(RUNNABLE_COUNT, before.get());
        Assert.assertEquals(RUNNABLE_COUNT, run.get());
        Assert.assertEquals(RUNNABLE_COUNT, after.get());
        Assert.assertTrue(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertFalse(task.isCanceled());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }

    @Test
    public void testCancelSchedule_Scene_UpdateTaskRunnable() {
        final WorkerUpdateTaskRunnable runnable = new WorkerUpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean runBefore(final Task task) {
                return true;
            }

            @Override
            public void run(final Task task) {
            }

            @Override
            public boolean runAfter(final Task task) {
                return true;
            }
        };
        final List<WorkerUpdateTaskRunnable> runnables = new ArrayList<>(RUNNABLE_COUNT);
        for (int i = 0; i < RUNNABLE_COUNT; i++) {
            runnables.add(runnable);
        }
        final Task task = BatchWorkerUpdateTask.schedule(scene, runnables);
        task.cancel();
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < WAIT_TIMEOUT) {
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (final InterruptedException ex) {
                // ignore
            }
            if (task.isCanceled()) {
                break;
            }
        }
        Assert.assertFalse(task.isUpdating());
        Assert.assertTrue(task.isCanceled());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }

    @Test
    public void testCancelSchedule_Scene_UpdateTaskRunnable_Long() {
        final AtomicBoolean passed = new AtomicBoolean(true);
        final WorkerUpdateTaskRunnable runnable = new WorkerUpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean runBefore(final Task task) {
                passed.set(false);
                return true;
            }

            @Override
            public void run(final Task task) {
                passed.set(false);
            }

            @Override
            public boolean runAfter(final Task task) {
                passed.set(false);
                return true;
            }
        };
        final List<WorkerUpdateTaskRunnable> runnables = new ArrayList<>(RUNNABLE_COUNT);
        for (int i = 0; i < RUNNABLE_COUNT; i++) {
            runnables.add(runnable);
        }
        final Task task = BatchWorkerUpdateTask.schedule(scene, runnables, 100L);
        task.cancel();
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < WAIT_TIMEOUT) {
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (final InterruptedException ex) {
                // ignore
            }
            if (task.isCanceled()) {
                break;
            }
        }
        Assert.assertTrue(passed.get());
        Assert.assertFalse(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertTrue(task.isCanceled());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }
}
