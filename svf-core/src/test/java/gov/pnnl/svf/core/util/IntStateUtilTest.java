package gov.pnnl.svf.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class IntStateUtilTest {

    /**
     * Test of isFlag method, of class FlagUtil.
     */
    @Test
    public void testIsFlag() {
        int state;
        for (final int mask : IntStateUtil.getMasks()) {
            state = IntStateUtil.NONE;
            // initial state
            Assert.assertFalse(IntStateUtil.isValue(state, mask));
            // trip flag
            state = IntStateUtil.setValue(state, mask);
            // check all state
            for (final int test : IntStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(IntStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(IntStateUtil.isValue(state, test));
                }
            }
        }
    }

    /**
     * Test of setFlag method, of class FlagUtil.
     */
    @Test
    public void testSetFlag_Boolean() {
        int state;
        // set to true
        for (final int mask : IntStateUtil.getMasks()) {
            state = IntStateUtil.NONE;
            // initial state
            Assert.assertFalse(IntStateUtil.isValue(state, mask));
            // set flag
            state = IntStateUtil.setValue(state, mask, true);
            // check all state
            for (final int test : IntStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(IntStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(IntStateUtil.isValue(state, test));
                }
            }
        }
        // set to false
        for (final int mask : IntStateUtil.getMasks()) {
            state = IntStateUtil.ALL;
            // initial state
            Assert.assertTrue(IntStateUtil.isValue(state, mask));
            // unset flag
            state = IntStateUtil.setValue(state, mask, false);
            // check all state
            for (final int test : IntStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertFalse(IntStateUtil.isValue(state, test));
                } else {
                    Assert.assertTrue(IntStateUtil.isValue(state, test));
                }
            }
        }
    }

    /**
     * Test of setFlag method, of class FlagUtil.
     */
    @Test
    public void testSetRenderedFlag() {
        int state;
        for (final int mask : IntStateUtil.getMasks()) {
            state = IntStateUtil.NONE;
            // initial state
            Assert.assertFalse(IntStateUtil.isValue(state, mask));
            // trip flag
            state = IntStateUtil.setValue(state, mask, true);
            // check all state
            for (final int test : IntStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(IntStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(IntStateUtil.isValue(state, test));
                }
            }
            // untrip flag
            state = IntStateUtil.setValue(state, mask, false);
            // check all state
            for (final int test : IntStateUtil.getMasks()) {
                Assert.assertFalse(IntStateUtil.isValue(state, test));
            }
        }
    }

    /**
     * Test of clearFlags method, of class FlagUtil.
     */
    @Test
    public void testClearFlags() {
        int state = IntStateUtil.NONE;
        for (final int mask : IntStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(IntStateUtil.isValue(state, mask));
        }
        for (final int mask : IntStateUtil.getMasks()) {
            // trip flag
            state = IntStateUtil.setValue(state, mask, true);
        }
        for (final int mask : IntStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(IntStateUtil.isValue(state, mask));
        }
        // clear the flags
        state = IntStateUtil.clearValues(state);
        for (final int mask : IntStateUtil.getMasks()) {
            // check state
            Assert.assertFalse(IntStateUtil.isValue(state, mask));
        }
    }

    /**
     * Test of setFlags method, of class FlagUtil.
     */
    @Test
    public void testSetFlags() {
        int state = IntStateUtil.NONE;
        for (final int mask : IntStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(IntStateUtil.isValue(state, mask));
        }
        // set the flags
        state = IntStateUtil.setValues(state);
        for (final int mask : IntStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(IntStateUtil.isValue(state, mask));
        }
    }

    /**
     * Test of setFlags method, of class FlagUtil.
     */
    @Test
    public void testSetFlags_boolean() {
        int state = IntStateUtil.NONE;
        for (final int mask : IntStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(IntStateUtil.isValue(state, mask));
        }
        // set the flags
        state = IntStateUtil.setValues(state, true);
        for (final int mask : IntStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(IntStateUtil.isValue(state, mask));
        }
        // set the flags
        state = IntStateUtil.setValues(state, false);
        for (final int mask : IntStateUtil.getMasks()) {
            // check state
            Assert.assertFalse(IntStateUtil.isValue(state, mask));
        }
    }
}
