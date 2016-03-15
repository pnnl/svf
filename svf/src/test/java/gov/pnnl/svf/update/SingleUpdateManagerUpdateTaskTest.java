package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
@Ignore
public class SingleUpdateManagerUpdateTaskTest {

    private static final long DELAY_TIME = 100L;
    private static final long DELAY_SLEEP_TIME = 50L;
    private static final long WAIT_SLEEP_TIME = 10L;
    private static final long WAIT_TIMEOUT = 60L * 1000L;
    private static final int TASK_COUNT = 8;
    private Scene scene;
    private SingleUpdateTaskManager manager;

    @Before
    public void beforeTest() {
        scene = ProxyScene.newInstance(new ProxyGLCanvas(), ConfigUtil.configure());
        manager = new SingleUpdateTaskManager(scene);
    }

    @After
    public void afterTest() {
        if (scene != null) {
            scene.dispose();
        }
        if (manager != null) {
            manager.dispose();
        }
    }

    @Test
    public void testSchedule_Scene_UpdateTaskRunnable() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final Task task = manager.schedule(new UpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean run(final Task task) {
                if (task.isUpdating()) {
                    passed.set(true);
                }
                return true;
            }
        });
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < WAIT_TIMEOUT) {
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (final InterruptedException ex) {
                // ignore
            }
            if (passed.get()) {
                break;
            }
        }
        Assert.assertTrue(passed.get());
        Assert.assertTrue(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertFalse(task.isCanceled());
        Assert.assertTrue(task.isDisposed());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }

    @Test
    public void testSchedule_Scene_UpdateTaskRunnable_Long() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final Task task = manager.schedule(new UpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean run(final Task task) {
                if (task.isUpdating()) {
                    passed.set(true);
                }
                return true;
            }
        }, DELAY_TIME);
        try {
            Thread.sleep(DELAY_SLEEP_TIME);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertFalse(passed.get());
        Assert.assertFalse(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertFalse(task.isCanceled());
        Assert.assertFalse(task.isDisposed());
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < WAIT_TIMEOUT) {
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (final InterruptedException ex) {
                // ignore
            }
            if (passed.get()) {
                break;
            }
        }
        Assert.assertTrue(passed.get());
        Assert.assertTrue(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertFalse(task.isCanceled());
        Assert.assertTrue(task.isDisposed());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }

    @Test
    public void testScheduleMultiple_Scene_UpdateTaskRunnable_Long() {
        final AtomicInteger passed = new AtomicInteger(0);
        final List<Task> tasks = new ArrayList<>(TASK_COUNT);
        for (int i = 0; i < TASK_COUNT; i++) {
            final Task task = manager.schedule(new UpdateTaskRunnable() {

                @Override
                public void disposed(final Task task) {
                    // no operation
                }

                @Override
                public boolean run(final Task task) {
                    if (task.isUpdating()) {
                        passed.incrementAndGet();
                    }
                    return true;
                }
            }, DELAY_TIME);
            tasks.add(task);
        }
        try {
            Thread.sleep(DELAY_SLEEP_TIME);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertTrue(passed.get() == 0);
        for (final Task task : tasks) {
            Assert.assertFalse(task.isFinished());
            Assert.assertFalse(task.isUpdating());
        }
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < WAIT_TIMEOUT) {
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (final InterruptedException ex) {
                // ignore
            }
            if (passed.get() >= TASK_COUNT) {
                break;
            }
        }
        Assert.assertTrue(passed.get() == 1);
        int finished = 0;
        int updating = 0;
        int canceled = 0;
        int disposed = 0;
        for (final Task task : tasks) {
            finished += task.isFinished() ? 1 : 0;
            updating += task.isUpdating() ? 1 : 0;
            canceled += task.isCanceled() ? 1 : 0;
            disposed += task.isDisposed() ? 1 : 0;
            Assert.assertFalse(scene.lookupAll().contains(task));
        }
        Assert.assertTrue(finished == 1);
        Assert.assertTrue(updating == 0);
        Assert.assertTrue(canceled == TASK_COUNT - 1);
        Assert.assertTrue(disposed == TASK_COUNT);
    }

    @Test
    public void testCancelSchedule_Scene_UpdateTaskRunnable() {
        final Task task = manager.schedule(new UpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean run(final Task task) {
                return true;
            }
        });
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
        Assert.assertTrue(task.isDisposed());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }

    @Test
    public void testCancelSchedule_Scene_UpdateTaskRunnable_Long() {
        final AtomicBoolean passed = new AtomicBoolean(true);
        final Task task = manager.schedule(new UpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean run(final Task task) {
                passed.set(false);
                return true;
            }
        }, 100L);
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
        Assert.assertTrue(task.isDisposed());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }
}
