package gov.pnnl.svf.test;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.junit.Before;

/**
 *
 * @param <D> Demo
 * @param <W> Window
 * @param <B> Builder
 * @param <S> Scene
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractDemoHelper<D, W, B, S> {

    protected static final boolean DEBUG = false;
    protected static final long BEGIN_WAIT_TIME = 3000L;
    protected static final long END_WAIT_TIME = 500L;
    protected static final int NUM_RETRIES = 10;
    protected D demo;
    protected W window;
    protected B builder;
    protected S scene;
    private MemLeakTracker tracker;
    private Object obj;

    @Before
    public void setUp() {
        tracker = new MemLeakTracker();
        obj = new Object();
        demo = null;
        builder = null;
        scene = null;
        window = null;
    }

    /**
     * Convenience method for calling beginMemLeakCheck, disposeScene, and
     * endMemLeakCheck.
     */
    protected void memLeakCheck() {
        beginMemLeakCheck();
        boolean disposed = false;
        int tries = 0;
        while (!disposed && tries < NUM_RETRIES) {
            tries++;
            try {
                disposeScene();
                disposed = true;
            } catch (final Exception ex) {
                System.out.println("Exception when attempting to dispose scene: " + ex.toString());
            }
        }
        endMemLeakCheck();
    }

    /**
     * Called after beginMemLeakCheck and before endMemLeakCheck.
     */
    protected abstract void disposeScene();

    /**
     * Call after creating and starting demo.
     *
     * @throws IllegalStateException if demo, builder, scene, or window fields
     *                               are not set
     */
    protected void beginMemLeakCheck() {
        if (demo == null) {
            throw new IllegalStateException("demo field must be set.");
        }
        if (builder == null) {
            throw new IllegalStateException("builder field must be set.");
        }
        if (scene == null) {
            throw new IllegalStateException("scene field must be set.");
        }
        if (window == null) {
            throw new IllegalStateException("window field must be set.");
        }
        // track a reference to the scene
        tracker.register(obj, "obj");
        tracker.register(builder, "builder");
        tracker.register(scene, "scene");
        try {
            // let it initialize and display
            Thread.sleep(BEGIN_WAIT_TIME);
        } catch (final InterruptedException ex) {
            // ignore
        }
    }

    /**
     * Call after stopping and disposing demo.
     */
    protected void endMemLeakCheck() {
        // release references
        obj = null;
        demo = null;
        builder = null;
        scene = null;
        window = null;
        int count = 0;
        while (true) {
            try {
                // let it finish disposing
                Thread.sleep(END_WAIT_TIME);
            } catch (final InterruptedException ex) {
                // ignore
            }
            // verify
            try {
                Assert.assertTrue("new Object() is not garbage collectable", tracker.isGarbageCollectable("obj"));
                Assert.assertTrue("builder is not garbage collectable", tracker.isGarbageCollectable("builder"));
                Assert.assertTrue("scene is not garbage collectable", tracker.isGarbageCollectable("scene"));
                return;
            } catch (final AssertionFailedError ex) {
                if (count++ < NUM_RETRIES) {
                    System.out.println(count + " - Waiting for the scene to dispose...");
                    continue;
                }
                System.out.println("All currently active threads:");
                for (final Thread thread : MemLeakTracker.getActiveThreads()) {
                    System.out.println(thread.getName());
                }
                throw ex;
            }
        }
    }
}
