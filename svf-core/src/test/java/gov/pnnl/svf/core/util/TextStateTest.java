package gov.pnnl.svf.core.util;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class TextStateTest extends AbstractObjectTestBase<TextState> {

    private byte counter = 0x00;

    public TextStateTest() {
        collisionIterateLength = 0;
    }

    /**
     * Test of isAntiAliasedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsRenderedFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isAntiAliased());
        // trip flag
        instance.setAntiAliased();
        // tripped state
        Assert.assertTrue(instance.isAntiAliased());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        //        Assert.assertFalse(instance.isAntiAliasedFlag());
        // toString test
        System.out.println("AntiAliased: " + instance);
    }

    /**
     * Test of isDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsAttribFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isDirty());
        // trip flag
        instance.setDirty();
        // tripped state
        Assert.assertTrue(instance.isDirty());
        // check others
        //        Assert.assertFalse(instance.isDirtyFlag());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Dirty: " + instance);
    }

    /**
     * Test of isInitializedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixModelviewFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isInitialized());
        // trip flag
        instance.setInitialized();
        // tripped state
        Assert.assertTrue(instance.isInitialized());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        //        Assert.assertFalse(instance.isInitializedFlag());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Initialized: " + instance);
    }

    /**
     * Test of isInternalDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixProjectionFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isInternalDirty());
        // trip flag
        instance.setInternalDirty();
        // tripped state
        Assert.assertTrue(instance.isInternalDirty());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        //        Assert.assertFalse(instance.isInternalDirtyFlag());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("InternalDirty: " + instance);
    }

    /**
     * Test of isSmoothingFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixTextureFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isSmoothing());
        // trip flag
        instance.setSmoothing();
        // tripped state
        Assert.assertTrue(instance.isSmoothing());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        //        Assert.assertFalse(instance.isSmoothingFlag());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Smoothing: " + instance);
    }

    /**
     * Test of isFractionalFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraAFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isFractional());
        // trip flag
        instance.setFractional();
        // tripped state
        Assert.assertTrue(instance.isFractional());
        // check others
        Assert.assertFalse(instance.isDirty());
        //        Assert.assertFalse(instance.isFractionalFlag());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Fractional: " + instance);
    }

    /**
     * Test of isMipMapsFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraBFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isMipMaps());
        // trip flag
        instance.setMipMaps();
        // tripped state
        Assert.assertTrue(instance.isMipMaps());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        //        Assert.assertFalse(instance.isMipMapsFlag());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("MipMaps: " + instance);
    }

    /**
     * Test of isDisposedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraCFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isDisposed());
        // trip flag
        instance.setDisposed();
        // tripped state
        Assert.assertTrue(instance.isDisposed());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        //        Assert.assertFalse(instance.isDisposedFlag());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Disposed: " + instance);
    }

    /**
     * Test of setAntiAliasedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetRenderedFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isAntiAliased());
        // trip flag
        instance.setAntiAliased(true);
        // tripped state
        Assert.assertTrue(instance.isAntiAliased());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        //        Assert.assertFalse(instance.isAntiAliasedFlag());
        // toString test
        System.out.println("AntiAliased: " + instance);
        // untrip flag
        instance.setAntiAliased(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of setDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetAttribFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isDirty());
        // trip flag
        instance.setDirty(true);
        // tripped state
        Assert.assertTrue(instance.isDirty());
        // check others
        //        Assert.assertFalse(instance.isDirtyFlag());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Dirty: " + instance);
        // untrip flag
        instance.setDirty(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of setInitializedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixModelviewFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isInitialized());
        // trip flag
        instance.setInitialized(true);
        // tripped state
        Assert.assertTrue(instance.isInitialized());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        //        Assert.assertFalse(instance.isInitializedFlag());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Initialized: " + instance);
        // untrip flag
        instance.setInitialized(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of setInternalDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixProjectionFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isInternalDirty());
        // trip flag
        instance.setInternalDirty(true);
        // tripped state
        Assert.assertTrue(instance.isInternalDirty());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        //        Assert.assertFalse(instance.isInternalDirtyFlag());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("InternalDirty: " + instance);
        // untrip flag
        instance.setInternalDirty(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of setSmoothingFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixTextureFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isSmoothing());
        // trip flag
        instance.setSmoothing(true);
        // tripped state
        Assert.assertTrue(instance.isSmoothing());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        //        Assert.assertFalse(instance.isSmoothingFlag());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Smoothing: " + instance);
        // untrip flag
        instance.setSmoothing(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of setFractionalFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraAFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isFractional());
        // trip flag
        instance.setFractional(true);
        // tripped state
        Assert.assertTrue(instance.isFractional());
        // check others
        Assert.assertFalse(instance.isDirty());
        //        Assert.assertFalse(instance.isFractionalFlag());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Fractional: " + instance);
        // untrip flag
        instance.setFractional(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of setMipMapsFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraBFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isMipMaps());
        // trip flag
        instance.setMipMaps(true);
        // tripped state
        Assert.assertTrue(instance.isMipMaps());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        //        Assert.assertFalse(instance.isMipMapsFlag());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("MipMaps: " + instance);
        // untrip flag
        instance.setMipMaps(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of setDisposedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraCFlag() {
        final TextState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isDisposed());
        // trip flag
        instance.setDisposed(true);
        // tripped state
        Assert.assertTrue(instance.isDisposed());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        //        Assert.assertFalse(instance.isDisposedFlag());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // toString test
        System.out.println("Disposed: " + instance);
        // untrip flag
        instance.setDisposed(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of resetFlags method, of class AbstractFlag.
     */
    @Test
    public void testResetFlags() {
        final TextState instance = newValueObject();
        // trip flag
        instance.setDirty();
        instance.setFractional();
        instance.setMipMaps();
        instance.setDisposed();
        instance.setInitialized();
        instance.setInternalDirty();
        instance.setSmoothing();
        instance.setAntiAliased();
        // check cleared state
        Assert.assertTrue(instance.isDirty());
        Assert.assertTrue(instance.isFractional());
        Assert.assertTrue(instance.isMipMaps());
        Assert.assertTrue(instance.isDisposed());
        Assert.assertTrue(instance.isInitialized());
        Assert.assertTrue(instance.isInternalDirty());
        Assert.assertTrue(instance.isSmoothing());
        Assert.assertTrue(instance.isAntiAliased());
        // clear flags
        instance.clearValues();
        // check cleared state
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
    }

    /**
     * Test of setFlags method, of class AbstractFlag.
     */
    @Test
    public void testSetFlags() {
        final TextState instance = newValueObject();
        // check cleared state
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isFractional());
        Assert.assertFalse(instance.isMipMaps());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isSmoothing());
        Assert.assertFalse(instance.isAntiAliased());
        // trip flags
        instance.setValues();
        // check tripped state
        Assert.assertTrue(instance.isDirty());
        Assert.assertTrue(instance.isFractional());
        Assert.assertTrue(instance.isMipMaps());
        Assert.assertTrue(instance.isDisposed());
        Assert.assertTrue(instance.isInitialized());
        Assert.assertTrue(instance.isInternalDirty());
        Assert.assertTrue(instance.isSmoothing());
        Assert.assertTrue(instance.isAntiAliased());
    }

    @Override
    protected TextState copyValueObject(final TextState object) {
        final TextState copy = newValueObject();
        copy.clearValues();
        if (object.isDirty()) {
            copy.setDirty();
        }
        if (object.isFractional()) {
            copy.setFractional();
        }
        if (object.isMipMaps()) {
            copy.setMipMaps();
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
        if (object.isSmoothing()) {
            copy.setSmoothing();
        }
        if (object.isAntiAliased()) {
            copy.setAntiAliased();
        }
        return copy;
    }

    @Override
    protected TextState newValueObject() {
        final TextState instance = new TextState();
        instance.clearValues();
        final byte next = counter;
        counter++;
        if ((next & 0x01) == 0x01) {
            instance.setDirty();
        }
        if ((next & 0x02) == 0x02) {
            instance.setFractional();
        }
        if ((next & 0x04) == 0x04) {
            instance.setMipMaps();
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
            instance.setSmoothing();
        }
        if ((next & 0x80) == 0x80) {
            instance.setAntiAliased();
        }
        return instance;
    }

    @Override
    protected void setFieldsToNull(final TextState object) {
        // no operation
    }
}
