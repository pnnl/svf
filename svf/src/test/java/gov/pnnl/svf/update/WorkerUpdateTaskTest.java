package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
@Ignore
public class WorkerUpdateTaskTest {

    private static final long DELAY_TIME = 100L;
    private static final long DELAY_SLEEP_TIME = 50L;
    private static final long WAIT_SLEEP_TIME = 10L;
    private static final long WAIT_TIMEOUT = 60L * 1000L;
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
        final AtomicBoolean before = new AtomicBoolean(false);
        final AtomicBoolean run = new AtomicBoolean(false);
        final AtomicBoolean after = new AtomicBoolean(false);
        final Task task = WorkerUpdateTask.schedule(scene, new WorkerUpdateTaskRunnable() {

                                                @Override
                                                public void disposed(final Task task) {
                                                    // no operation
                                                }

                                                @Override
                                                public boolean runBefore(final Task task) {
                                                    if (task.isUpdating()) {
                                                        before.set(true);
                                                    }
                                                    return true;
                                                }

                                                @Override
                                                public void run(final Task task) {
                                                    run.set(true);
                                                }

                                                @Override
                                                public boolean runAfter(final Task task) {
                                                    if (task.isUpdating()) {
                                                        after.set(true);
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
            if (before.get() && run.get() && after.get()) {
                break;
            }
        }
        Assert.assertTrue(before.get());
        Assert.assertTrue(run.get());
        Assert.assertTrue(after.get());
        Assert.assertTrue(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertFalse(task.isCanceled());
        Assert.assertTrue(task.isDisposed());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }

    @Test
    public void testSchedule_Scene_UpdateTaskRunnable_Long() {
        final AtomicBoolean before = new AtomicBoolean(false);
        final AtomicBoolean run = new AtomicBoolean(false);
        final AtomicBoolean after = new AtomicBoolean(false);
        final Task task = WorkerUpdateTask.schedule(scene, new WorkerUpdateTaskRunnable() {

                                                @Override
                                                public void disposed(final Task task) {
                                                    // no operation
                                                }

                                                @Override
                                                public boolean runBefore(final Task task) {
                                                    if (task.isUpdating()) {
                                                        before.set(true);
                                                    }
                                                    return true;
                                                }

                                                @Override
                                                public void run(final Task task) {
                                                    run.set(true);
                                                }

                                                @Override
                                                public boolean runAfter(final Task task) {
                                                    if (task.isUpdating()) {
                                                        after.set(true);
                                                    }
                                                    return true;
                                                }
                                            }, DELAY_TIME);
        try {
            Thread.sleep(DELAY_SLEEP_TIME);
        } catch (final InterruptedException ex) {
            // ignore
        }
        Assert.assertFalse(before.get());
        Assert.assertFalse(run.get());
        Assert.assertFalse(after.get());
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
            if (before.get() && run.get() && after.get()) {
                break;
            }
        }
        Assert.assertTrue(before.get());
        Assert.assertTrue(run.get());
        Assert.assertTrue(after.get());
        Assert.assertTrue(task.isFinished());
        Assert.assertFalse(task.isUpdating());
        Assert.assertFalse(task.isCanceled());
        Assert.assertTrue(task.isDisposed());
        Assert.assertFalse(scene.lookupAll().contains(task));
    }

    @Test
    public void testCancelSchedule_Scene_UpdateTaskRunnable() {
        final Task task = WorkerUpdateTask.schedule(scene, new WorkerUpdateTaskRunnable() {

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
        final Task task = WorkerUpdateTask.schedule(scene, new WorkerUpdateTaskRunnable() {

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
