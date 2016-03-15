package gov.pnnl.svf.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class LongStateUtilTest {

    /**
     * Test of isFlag method, of class FlagUtil.
     */
    @Test
    public void testIsFlag() {
        long state;
        for (final long mask : LongStateUtil.getMasks()) {
            state = LongStateUtil.NONE;
            // initial state
            Assert.assertFalse(LongStateUtil.isValue(state, mask));
            // trip flag
            state = LongStateUtil.setValue(state, mask);
            // check all state
            for (final long test : LongStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(LongStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(LongStateUtil.isValue(state, test));
                }
            }
        }
    }

    /**
     * Test of setFlag method, of class FlagUtil.
     */
    @Test
    public void testSetFlag_Boolean() {
        long state;
        // set to true
        for (final long mask : LongStateUtil.getMasks()) {
            state = LongStateUtil.NONE;
            // initial state
            Assert.assertFalse(LongStateUtil.isValue(state, mask));
            // set flag
            state = LongStateUtil.setValue(state, mask, true);
            // check all state
            for (final long test : LongStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(LongStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(LongStateUtil.isValue(state, test));
                }
            }
        }
        // set to false
        for (final long mask : LongStateUtil.getMasks()) {
            state = LongStateUtil.ALL;
            // initial state
            Assert.assertTrue(LongStateUtil.isValue(state, mask));
            // unset flag
            state = LongStateUtil.setValue(state, mask, false);
            // check all state
            for (final long test : LongStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertFalse(LongStateUtil.isValue(state, test));
                } else {
                    Assert.assertTrue(LongStateUtil.isValue(state, test));
                }
            }
        }
    }

    /**
     * Test of setFlag method, of class FlagUtil.
     */
    @Test
    public void testSetRenderedFlag() {
        long state;
        for (final long mask : LongStateUtil.getMasks()) {
            state = LongStateUtil.NONE;
            // initial state
            Assert.assertFalse(LongStateUtil.isValue(state, mask));
            // trip flag
            state = LongStateUtil.setValue(state, mask, true);
            // check all state
            for (final long test : LongStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(LongStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(LongStateUtil.isValue(state, test));
                }
            }
            // untrip flag
            state = LongStateUtil.setValue(state, mask, false);
            // check all state
            for (final long test : LongStateUtil.getMasks()) {
                Assert.assertFalse(LongStateUtil.isValue(state, test));
            }
        }
    }

    /**
     * Test of clearFlags method, of class FlagUtil.
     */
    @Test
    public void testClearFlags() {
        long state = LongStateUtil.NONE;
        for (final long mask : LongStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(LongStateUtil.isValue(state, mask));
        }
        for (final long mask : LongStateUtil.getMasks()) {
            // trip flag
            state = LongStateUtil.setValue(state, mask, true);
        }
        for (final long mask : LongStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(LongStateUtil.isValue(state, mask));
        }
        // clear the flags
        state = LongStateUtil.clearValues(state);
        for (final long mask : LongStateUtil.getMasks()) {
            // check state
            Assert.assertFalse(LongStateUtil.isValue(state, mask));
        }
    }

    /**
     * Test of setFlags method, of class FlagUtil.
     */
    @Test
    public void testSetFlags() {
        long state = LongStateUtil.NONE;
        for (final long mask : LongStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(LongStateUtil.isValue(state, mask));
        }
        // set the flags
        state = LongStateUtil.setValues(state);
        for (final long mask : LongStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(LongStateUtil.isValue(state, mask));
        }
    }

    /**
     * Test of setFlags method, of class FlagUtil.
     */
    @Test
    public void testSetFlags_boolean() {
        long state = LongStateUtil.NONE;
        for (final long mask : LongStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(LongStateUtil.isValue(state, mask));
        }
        // set the flags
        state = LongStateUtil.setValues(state, true);
        for (final long mask : LongStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(LongStateUtil.isValue(state, mask));
        }
        // set the flags
        state = LongStateUtil.setValues(state, false);
        for (final long mask : LongStateUtil.getMasks()) {
            // check state
            Assert.assertFalse(LongStateUtil.isValue(state, mask));
        }
    }
}
