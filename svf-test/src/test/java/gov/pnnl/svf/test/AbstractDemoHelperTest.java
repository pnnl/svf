package gov.pnnl.svf.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author D3X573
 */
public class AbstractDemoHelperTest {

    /**
     * Test of beginMemLeakCheck method, of class AbstractDemoHelper.
     */
    @Test
    public void testMemLeakCheckPass() {
        final DemoHelperImpl instance = new DemoHelperImpl(false);
        instance.startTest();
        instance.stopTest();
    }

    /**
     * Test of endMemLeakCheck method, of class AbstractDemoHelper.
     */
    @Test(expected = AssertionError.class)
    public void testMemLeakCheckFail() {
        final DemoHelperImpl instance = new DemoHelperImpl(true);
        instance.startTest();
        instance.stopTest();
    }

    public class DemoHelperImpl extends AbstractDemoHelper {

        private final boolean leak;
        private Object keep;

        public DemoHelperImpl(boolean leak) {
            this.leak = leak;
        }

        private void createMemoryLeak() {
            ((List) scene).add(this);
            keep = scene;
        }

        public void startTest() {
            setUp();
            demo = new Object();
            builder = new Object();
            window = new Object();
            scene = new ArrayList();
            if (leak) {
                createMemoryLeak();
            }
        }

        public void stopTest() {
            memLeakCheck();
        }

        @Override
        public void disposeScene() {
        }
    }

}
