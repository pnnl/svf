package gov.pnnl.svf.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ShortStateUtilTest {

    /**
     * Test of isFlag method, of class FlagUtil.
     */
    @Test
    public void testIsFlag() {
        short state;
        for (final short mask : ShortStateUtil.getMasks()) {
            state = ShortStateUtil.NONE;
            // initial state
            Assert.assertFalse(ShortStateUtil.isValue(state, mask));
            // trip flag
            state = ShortStateUtil.setValue(state, mask);
            // check all state
            for (final short test : ShortStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(ShortStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(ShortStateUtil.isValue(state, test));
                }
            }
        }
    }

    /**
     * Test of setFlag method, of class FlagUtil.
     */
    @Test
    public void testSetFlag_Boolean() {
        short state;
        // set to true
        for (final short mask : ShortStateUtil.getMasks()) {
            state = ShortStateUtil.NONE;
            // initial state
            Assert.assertFalse(ShortStateUtil.isValue(state, mask));
            // set flag
            state = ShortStateUtil.setValue(state, mask, true);
            // check all state
            for (final short test : ShortStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(ShortStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(ShortStateUtil.isValue(state, test));
                }
            }
        }
        // set to false
        for (final short mask : ShortStateUtil.getMasks()) {
            state = ShortStateUtil.ALL;
            // initial state
            Assert.assertTrue(ShortStateUtil.isValue(state, mask));
            // unset flag
            state = ShortStateUtil.setValue(state, mask, false);
            // check all state
            for (final short test : ShortStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertFalse(ShortStateUtil.isValue(state, test));
                } else {
                    Assert.assertTrue(ShortStateUtil.isValue(state, test));
                }
            }
        }
    }

    /**
     * Test of setFlag method, of class FlagUtil.
     */
    @Test
    public void testSetRenderedFlag() {
        short state;
        for (final short mask : ShortStateUtil.getMasks()) {
            state = ShortStateUtil.NONE;
            // initial state
            Assert.assertFalse(ShortStateUtil.isValue(state, mask));
            // trip flag
            state = ShortStateUtil.setValue(state, mask, true);
            // check all state
            for (final short test : ShortStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(ShortStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(ShortStateUtil.isValue(state, test));
                }
            }
            // untrip flag
            state = ShortStateUtil.setValue(state, mask, false);
            // check all state
            for (final short test : ShortStateUtil.getMasks()) {
                Assert.assertFalse(ShortStateUtil.isValue(state, test));
            }
        }
    }

    /**
     * Test of clearFlags method, of class FlagUtil.
     */
    @Test
    public void testClearFlags() {
        short state = ShortStateUtil.NONE;
        for (final short mask : ShortStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(ShortStateUtil.isValue(state, mask));
        }
        for (final short mask : ShortStateUtil.getMasks()) {
            // trip flag
            state = ShortStateUtil.setValue(state, mask, true);
        }
        for (final short mask : ShortStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(ShortStateUtil.isValue(state, mask));
        }
        // clear the flags
        state = ShortStateUtil.clearValues(state);
        for (final short mask : ShortStateUtil.getMasks()) {
            // check state
            Assert.assertFalse(ShortStateUtil.isValue(state, mask));
        }
    }

    /**
     * Test of setFlags method, of class FlagUtil.
     */
    @Test
    public void testSetFlags() {
        short state = ShortStateUtil.NONE;
        for (final short mask : ShortStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(ShortStateUtil.isValue(state, mask));
        }
        // set the flags
        state = ShortStateUtil.setValues(state);
        for (final short mask : ShortStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(ShortStateUtil.isValue(state, mask));
        }
    }

    /**
     * Test of setFlags method, of class FlagUtil.
     */
    @Test
    public void testSetFlags_boolean() {
        short state = ShortStateUtil.NONE;
        for (final short mask : ShortStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(ShortStateUtil.isValue(state, mask));
        }
        // set the flags
        state = ShortStateUtil.setValues(state, true);
        for (final short mask : ShortStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(ShortStateUtil.isValue(state, mask));
        }
        // set the flags
        state = ShortStateUtil.setValues(state, false);
        for (final short mask : ShortStateUtil.getMasks()) {
            // check state
            Assert.assertFalse(ShortStateUtil.isValue(state, mask));
        }
    }
}
