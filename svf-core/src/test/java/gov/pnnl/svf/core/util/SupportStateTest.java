package gov.pnnl.svf.core.util;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class SupportStateTest extends AbstractObjectTestBase<SupportState> {

    private byte counter = 0x00;

    public SupportStateTest() {
        collisionIterateLength = 0;
    }

    /**
     * Test of isSelectedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsRenderedFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isSelected());
        // trip flag
        instance.setSelected();
        // tripped state
        Assert.assertTrue(instance.isSelected());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        //        Assert.assertFalse(instance.isSelectedFlag());
        // toString test
        System.out.println("Selected: " + instance);
    }

    /**
     * Test of isDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsAttribFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isDirty());
        // trip flag
        instance.setDirty();
        // tripped state
        Assert.assertTrue(instance.isDirty());
        // check others
        //        Assert.assertFalse(instance.isDirtyFlag());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Dirty: " + instance);
    }

    /**
     * Test of isInitializedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixModelviewFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isInitialized());
        // trip flag
        instance.setInitialized();
        // tripped state
        Assert.assertTrue(instance.isInitialized());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        //        Assert.assertFalse(instance.isInitializedFlag());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Initialized: " + instance);
    }

    /**
     * Test of isInternalDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixProjectionFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isInternalDirty());
        // trip flag
        instance.setInternalDirty();
        // tripped state
        Assert.assertTrue(instance.isInternalDirty());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        //        Assert.assertFalse(instance.isInternalDirtyFlag());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("InternalDirty: " + instance);
    }

    /**
     * Test of isExtraFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixTextureFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isExtra());
        // trip flag
        instance.setExtra();
        // tripped state
        Assert.assertTrue(instance.isExtra());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        //        Assert.assertFalse(instance.isExtraFlag());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Extra: " + instance);
    }

    /**
     * Test of isHighlightedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraAFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isHighlighted());
        // trip flag
        instance.setHighlighted();
        // tripped state
        Assert.assertTrue(instance.isHighlighted());
        // check others
        Assert.assertFalse(instance.isDirty());
        //        Assert.assertFalse(instance.isHighlightedFlag());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Highlighted: " + instance);
    }

    /**
     * Test of isRelatedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraBFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isRelated());
        // trip flag
        instance.setRelated();
        // tripped state
        Assert.assertTrue(instance.isRelated());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        //        Assert.assertFalse(instance.isRelatedFlag());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Related: " + instance);
    }

    /**
     * Test of isDisposedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraCFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isDisposed());
        // trip flag
        instance.setDisposed();
        // tripped state
        Assert.assertTrue(instance.isDisposed());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        //        Assert.assertFalse(instance.isDisposedFlag());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Disposed: " + instance);
    }

    /**
     * Test of setSelectedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetRenderedFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isSelected());
        // trip flag
        instance.setSelected(true);
        // tripped state
        Assert.assertTrue(instance.isSelected());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        //        Assert.assertFalse(instance.isSelectedFlag());
        // toString test
        System.out.println("Selected: " + instance);
        // untrip flag
        instance.setSelected(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of setDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetAttribFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isDirty());
        // trip flag
        instance.setDirty(true);
        // tripped state
        Assert.assertTrue(instance.isDirty());
        // check others
        //        Assert.assertFalse(instance.isDirtyFlag());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Dirty: " + instance);
        // untrip flag
        instance.setDirty(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of setInitializedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixModelviewFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isInitialized());
        // trip flag
        instance.setInitialized(true);
        // tripped state
        Assert.assertTrue(instance.isInitialized());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        //        Assert.assertFalse(instance.isInitializedFlag());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Initialized: " + instance);
        // untrip flag
        instance.setInitialized(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of setInternalDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixProjectionFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isInternalDirty());
        // trip flag
        instance.setInternalDirty(true);
        // tripped state
        Assert.assertTrue(instance.isInternalDirty());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        //        Assert.assertFalse(instance.isInternalDirtyFlag());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("InternalDirty: " + instance);
        // untrip flag
        instance.setInternalDirty(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of setExtraFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixTextureFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isExtra());
        // trip flag
        instance.setExtra(true);
        // tripped state
        Assert.assertTrue(instance.isExtra());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        //        Assert.assertFalse(instance.isExtraFlag());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Extra: " + instance);
        // untrip flag
        instance.setExtra(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of setHighlightedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraAFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isHighlighted());
        // trip flag
        instance.setHighlighted(true);
        // tripped state
        Assert.assertTrue(instance.isHighlighted());
        // check others
        Assert.assertFalse(instance.isDirty());
        //        Assert.assertFalse(instance.isHighlightedFlag());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Highlighted: " + instance);
        // untrip flag
        instance.setHighlighted(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of setRelatedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraBFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isRelated());
        // trip flag
        instance.setRelated(true);
        // tripped state
        Assert.assertTrue(instance.isRelated());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        //        Assert.assertFalse(instance.isRelatedFlag());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Related: " + instance);
        // untrip flag
        instance.setRelated(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of setDisposedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraCFlag() {
        final SupportState instance = new SupportState();
        // initial state
        Assert.assertFalse(instance.isDisposed());
        // trip flag
        instance.setDisposed(true);
        // tripped state
        Assert.assertTrue(instance.isDisposed());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        //        Assert.assertFalse(instance.isDisposedFlag());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // toString test
        System.out.println("Disposed: " + instance);
        // untrip flag
        instance.setDisposed(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of resetFlags method, of class AbstractFlag.
     */
    @Test
    public void testResetFlags() {
        final SupportState instance = new SupportState();
        // trip flag
        instance.setDirty();
        instance.setHighlighted();
        instance.setRelated();
        instance.setDisposed();
        instance.setInitialized();
        instance.setInternalDirty();
        instance.setExtra();
        instance.setSelected();
        // check cleared state
        Assert.assertTrue(instance.isDirty());
        Assert.assertTrue(instance.isHighlighted());
        Assert.assertTrue(instance.isRelated());
        Assert.assertTrue(instance.isDisposed());
        Assert.assertTrue(instance.isInitialized());
        Assert.assertTrue(instance.isInternalDirty());
        Assert.assertTrue(instance.isExtra());
        Assert.assertTrue(instance.isSelected());
        // clear flags
        instance.clearValues();
        // check cleared state
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
    }

    /**
     * Test of setFlags method, of class AbstractFlag.
     */
    @Test
    public void testSetFlags() {
        final SupportState instance = new SupportState();
        // check cleared state
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isHighlighted());
        Assert.assertFalse(instance.isRelated());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isSelected());
        // trip flags
        instance.setValues();
        // check tripped state
        Assert.assertTrue(instance.isDirty());
        Assert.assertTrue(instance.isHighlighted());
        Assert.assertTrue(instance.isRelated());
        Assert.assertTrue(instance.isDisposed());
        Assert.assertTrue(instance.isInitialized());
        Assert.assertTrue(instance.isInternalDirty());
        Assert.assertTrue(instance.isExtra());
        Assert.assertTrue(instance.isSelected());
    }

    @Override
    protected SupportState copyValueObject(final SupportState object) {
        final SupportState copy = new SupportState();
        if (object.isDirty()) {
            copy.setDirty();
        }
        if (object.isHighlighted()) {
            copy.setHighlighted();
        }
        if (object.isRelated()) {
            copy.setRelated();
        }
        if (object.isDisposed()) {
            copy.setDisposed();
        }
        if (object.isInitialized()) {
            copy.setInitialized();
        }
        if (object.isInternalDirty()) {
            copy.setInternalDirty();
        }
        if (object.isExtra()) {
            copy.setExtra();
        }
        if (object.isSelected()) {
            copy.setSelected();
        }
        return copy;
    }

    @Override
    protected SupportState newValueObject() {
        final SupportState instance = new SupportState();
        final byte next = counter;
        counter++;
        if ((next & 0x01) == 0x01) {
            instance.setDirty();
        }
        if ((next & 0x02) == 0x02) {
            instance.setHighlighted();
        }
        if ((next & 0x04) == 0x04) {
            instance.setRelated();
        }
        if ((next & 0x08) == 0x08) {
            instance.setDisposed();
        }
        if ((next & 0x10) == 0x10) {
            instance.setInitialized();
        }
        if ((next & 0x20) == 0x20) {
            instance.setInternalDirty();
        }
        if ((next & 0x40) == 0x40) {
            instance.setExtra();
        }
        if ((next & 0x80) == 0x80) {
            instance.setSelected();
        }
        return instance;
    }

    @Override
    protected void setFieldsToNull(final SupportState object) {
        // no operation
    }
}
