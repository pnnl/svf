package gov.pnnl.svf.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ByteStateUtilTest {

    /**
     * Test of isFlag method, of class FlagUtil.
     */
    @Test
    public void testIsFlag() {
        byte state;
        for (final byte mask : ByteStateUtil.getMasks()) {
            state = ByteStateUtil.NONE;
            // initial state
            Assert.assertFalse(ByteStateUtil.isValue(state, mask));
            // trip flag
            state = ByteStateUtil.setValue(state, mask);
            // check all state
            for (final byte test : ByteStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(ByteStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(ByteStateUtil.isValue(state, test));
                }
            }
        }
    }

    /**
     * Test of setFlag method, of class FlagUtil.
     */
    @Test
    public void testSetFlag_Boolean() {
        byte state;
        // set to true
        for (final byte mask : ByteStateUtil.getMasks()) {
            state = ByteStateUtil.NONE;
            // initial state
            Assert.assertFalse(ByteStateUtil.isValue(state, mask));
            // set flag
            state = ByteStateUtil.setValue(state, mask, true);
            // check all state
            for (final byte test : ByteStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(ByteStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(ByteStateUtil.isValue(state, test));
                }
            }
        }
        // set to false
        for (final byte mask : ByteStateUtil.getMasks()) {
            state = ByteStateUtil.ALL;
            // initial state
            Assert.assertTrue(ByteStateUtil.isValue(state, mask));
            // unset flag
            state = ByteStateUtil.setValue(state, mask, false);
            // check all state
            for (final byte test : ByteStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertFalse(ByteStateUtil.isValue(state, test));
                } else {
                    Assert.assertTrue(ByteStateUtil.isValue(state, test));
                }
            }
        }
    }

    /**
     * Test of setFlag method, of class FlagUtil.
     */
    @Test
    public void testSetRenderedFlag() {
        byte state;
        for (final byte mask : ByteStateUtil.getMasks()) {
            state = ByteStateUtil.NONE;
            // initial state
            Assert.assertFalse(ByteStateUtil.isValue(state, mask));
            // trip flag
            state = ByteStateUtil.setValue(state, mask, true);
            // check all state
            for (final byte test : ByteStateUtil.getMasks()) {
                if (mask == test) {
                    Assert.assertTrue(ByteStateUtil.isValue(state, test));
                } else {
                    Assert.assertFalse(ByteStateUtil.isValue(state, test));
                }
            }
            // untrip flag
            state = ByteStateUtil.setValue(state, mask, false);
            // check all state
            for (final byte test : ByteStateUtil.getMasks()) {
                Assert.assertFalse(ByteStateUtil.isValue(state, test));
            }
        }
    }

    /**
     * Test of clearFlags method, of class FlagUtil.
     */
    @Test
    public void testClearFlags() {
        byte state = ByteStateUtil.NONE;
        for (final byte mask : ByteStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(ByteStateUtil.isValue(state, mask));
        }
        for (final byte mask : ByteStateUtil.getMasks()) {
            // trip flag
            state = ByteStateUtil.setValue(state, mask, true);
        }
        for (final byte mask : ByteStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(ByteStateUtil.isValue(state, mask));
        }
        // clear the flags
        state = ByteStateUtil.clearValues(state);
        for (final byte mask : ByteStateUtil.getMasks()) {
            // check state
            Assert.assertFalse(ByteStateUtil.isValue(state, mask));
        }
    }

    /**
     * Test of setFlags method, of class FlagUtil.
     */
    @Test
    public void testSetFlags() {
        byte state = ByteStateUtil.NONE;
        for (final byte mask : ByteStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(ByteStateUtil.isValue(state, mask));
        }
        // set the flags
        state = ByteStateUtil.setValues(state);
        for (final byte mask : ByteStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(ByteStateUtil.isValue(state, mask));
        }
    }

    /**
     * Test of setFlags method, of class FlagUtil.
     */
    @Test
    public void testSetFlags_boolean() {
        byte state = ByteStateUtil.NONE;
        for (final byte mask : ByteStateUtil.getMasks()) {
            // initial state
            Assert.assertFalse(ByteStateUtil.isValue(state, mask));
        }
        // set the flags
        state = ByteStateUtil.setValues(state, true);
        for (final byte mask : ByteStateUtil.getMasks()) {
            // check state
            Assert.assertTrue(ByteStateUtil.isValue(state, mask));
        }
        // set the flags
        state = ByteStateUtil.setValues(state, false);
        for (final byte mask : ByteStateUtil.getMasks()) {
            // check state
            Assert.assertFalse(ByteStateUtil.isValue(state, mask));
        }
    }
}
