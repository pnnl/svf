package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.test.AbstractObjectTestBase;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.concurrent.atomic.AtomicBoolean;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class AbstractTaskTest extends AbstractObjectTestBase<AbstractTaskImpl> {

    //    private static final int RUNNABLE_COUNT = 10;
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

    public AbstractTaskTest() {
    }

    /**
     * Test of cancel method, of class AbstractTask.
     */
    @Test
    public void testCancel() {
        testBoundField(newValueObject(), Task.CANCELED, true);
    }

    /**
     * Test of isDisposed method, of class AbstractTask.
     */
    @Test
    public void testIsDisposed() {
        final AbstractTask instance = newValueObject();
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isCanceled());
        Assert.assertFalse(instance.isFinished());
        Assert.assertFalse(instance.isUpdating());
        instance.dispose();
        Assert.assertTrue(instance.isDisposed());
        Assert.assertFalse(instance.isCanceled());
        Assert.assertFalse(instance.isFinished());
        Assert.assertFalse(instance.isUpdating());
    }

    /**
     * Test of isCanceled method, of class AbstractTask.
     */
    @Test
    public void testIsCanceled() {
        final AbstractTask instance = newValueObject();
        Assert.assertFalse(instance.isCanceled());
        Assert.assertFalse(instance.isFinished());
        Assert.assertFalse(instance.isUpdating());
        instance.setCanceled(true);
        Assert.assertTrue(instance.isCanceled());
        Assert.assertFalse(instance.isFinished());
        Assert.assertFalse(instance.isUpdating());
    }

    /**
     * Test of isFinished method, of class AbstractTask.
     */
    @Test
    public void testIsFinished() {
        final AbstractTask instance = newValueObject();
        Assert.assertFalse(instance.isCanceled());
        Assert.assertFalse(instance.isFinished());
        Assert.assertFalse(instance.isUpdating());
        instance.setFinished(true);
        Assert.assertFalse(instance.isCanceled());
        Assert.assertTrue(instance.isFinished());
        Assert.assertFalse(instance.isUpdating());
    }

    /**
     * Test of isUpdating method, of class AbstractTask.
     */
    @Test
    public void testIsUpdating() {
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AbstractTask instance = new AbstractTaskImpl(scene) {
            @Override
            public void run() {
                passed.set(isUpdating());
            }
        };
        Assert.assertFalse(instance.isCanceled());
        Assert.assertFalse(instance.isFinished());
        Assert.assertFalse(instance.isUpdating());
        instance.update(10L);
        Assert.assertTrue(passed.get());
        Assert.assertFalse(instance.isCanceled());
        Assert.assertFalse(instance.isFinished());
        Assert.assertFalse(instance.isUpdating());
    }

    /**
     * Test of getPropertyChangeSupport method, of class AbstractTask.
     */
    @Test
    public void testGetPropertyChangeSupport() {
        Assert.assertNotNull(newValueObject().getPropertyChangeSupport());
    }

    /**
     * Test of setCanceled method, of class AbstractTask.
     */
    @Test
    public void testSetCanceled() {
        testBoundField(newValueObject(), Task.CANCELED, true);
    }

    /**
     * Test of setFinished method, of class AbstractTask.
     */
    @Test
    public void testSetFinished() {
        testBoundField(newValueObject(), Task.FINISHED, true);
    }

    @Override
    protected AbstractTaskImpl copyValueObject(final AbstractTaskImpl object) {
        return object;
    }

    @Override
    protected AbstractTaskImpl newValueObject() {
        return new AbstractTaskImpl(scene);
    }

    @Override
    protected void setFieldsToNull(final AbstractTaskImpl object) {
        // no operation
    }
}
