package gov.pnnl.svf.core.util;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ActorStateTest extends AbstractObjectTestBase<ActorState> {

    private byte counter = 0x00;

    public ActorStateTest() {
        collisionIterateLength = 0;
    }

    /**
     * Test of isVisibleFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsRenderedFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isVisible());
        // trip flag
        instance.setVisible();
        // tripped state
        Assert.assertTrue(instance.isVisible());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        //        Assert.assertFalse(instance.isVisibleFlag());
        // toString test
        System.out.println("Rendered: " + instance);
    }

    /**
     * Test of isDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsAttribFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isDirty());
        // trip flag
        instance.setDirty();
        // tripped state
        Assert.assertTrue(instance.isDirty());
        // check others
        //        Assert.assertFalse(instance.isDirtyFlag());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("Attrib: " + instance);
    }

    /**
     * Test of isInitializedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixModelviewFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isInitialized());
        // trip flag
        instance.setInitialized();
        // tripped state
        Assert.assertTrue(instance.isInitialized());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        //        Assert.assertFalse(instance.isInitializedFlag());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("MatrixModelview: " + instance);
    }

    /**
     * Test of isInternalDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixProjectionFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isInternalDirty());
        // trip flag
        instance.setInternalDirty();
        // tripped state
        Assert.assertTrue(instance.isInternalDirty());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        //        Assert.assertFalse(instance.isInternalDirtyFlag());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("MatrixProjection: " + instance);
    }

    /**
     * Test of isExtraFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixTextureFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isExtra());
        // trip flag
        instance.setExtra();
        // tripped state
        Assert.assertTrue(instance.isExtra());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        //        Assert.assertFalse(instance.isExtraFlag());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("MatrixTexture: " + instance);
    }

    /**
     * Test of isRootFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraAFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isRoot());
        // trip flag
        instance.setRoot();
        // tripped state
        Assert.assertTrue(instance.isRoot());
        // check others
        Assert.assertFalse(instance.isDirty());
        //        Assert.assertFalse(instance.isRootFlag());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("ExtraA: " + instance);
    }

    /**
     * Test of isWireFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraBFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isWire());
        // trip flag
        instance.setWire();
        // tripped state
        Assert.assertTrue(instance.isWire());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        //        Assert.assertFalse(instance.isWireFlag());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("ExtraB: " + instance);
    }

    /**
     * Test of isDisposedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraCFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isDisposed());
        // trip flag
        instance.setDisposed();
        // tripped state
        Assert.assertTrue(instance.isDisposed());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        //        Assert.assertFalse(instance.isDisposedFlag());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("ExtraC: " + instance);
    }

    /**
     * Test of setVisibleFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetRenderedFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isVisible());
        // trip flag
        instance.setVisible(true);
        // tripped state
        Assert.assertTrue(instance.isVisible());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        //        Assert.assertFalse(instance.isVisibleFlag());
        // toString test
        System.out.println("Rendered: " + instance);
        // untrip flag
        instance.setVisible(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of setDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetAttribFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isDirty());
        // trip flag
        instance.setDirty(true);
        // tripped state
        Assert.assertTrue(instance.isDirty());
        // check others
        //        Assert.assertFalse(instance.isDirtyFlag());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("Attrib: " + instance);
        // untrip flag
        instance.setDirty(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of setInitializedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixModelviewFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isInitialized());
        // trip flag
        instance.setInitialized(true);
        // tripped state
        Assert.assertTrue(instance.isInitialized());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        //        Assert.assertFalse(instance.isInitializedFlag());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("MatrixModelview: " + instance);
        // untrip flag
        instance.setInitialized(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of setInternalDirtyFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixProjectionFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isInternalDirty());
        // trip flag
        instance.setInternalDirty(true);
        // tripped state
        Assert.assertTrue(instance.isInternalDirty());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        //        Assert.assertFalse(instance.isInternalDirtyFlag());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("MatrixProjection: " + instance);
        // untrip flag
        instance.setInternalDirty(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of setExtraFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixTextureFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isExtra());
        // trip flag
        instance.setExtra(true);
        // tripped state
        Assert.assertTrue(instance.isExtra());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        //        Assert.assertFalse(instance.isExtraFlag());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("MatrixTexture: " + instance);
        // untrip flag
        instance.setExtra(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of setRootFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraAFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isRoot());
        // trip flag
        instance.setRoot(true);
        // tripped state
        Assert.assertTrue(instance.isRoot());
        // check others
        Assert.assertFalse(instance.isDirty());
        //        Assert.assertFalse(instance.isRootFlag());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("ExtraA: " + instance);
        // untrip flag
        instance.setRoot(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of setWireFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraBFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isWire());
        // trip flag
        instance.setWire(true);
        // tripped state
        Assert.assertTrue(instance.isWire());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        //        Assert.assertFalse(instance.isWireFlag());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("ExtraB: " + instance);
        // untrip flag
        instance.setWire(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of setDisposedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraCFlag() {
        final ActorState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isDisposed());
        // trip flag
        instance.setDisposed(true);
        // tripped state
        Assert.assertTrue(instance.isDisposed());
        // check others
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        //        Assert.assertFalse(instance.isDisposedFlag());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // toString test
        System.out.println("ExtraC: " + instance);
        // untrip flag
        instance.setDisposed(false);
        // check
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of resetFlags method, of class AbstractFlag.
     */
    @Test
    public void testResetFlags() {
        final ActorState instance = newValueObject();
        // trip flag
        instance.setDirty();
        instance.setRoot();
        instance.setWire();
        instance.setDisposed();
        instance.setInitialized();
        instance.setInternalDirty();
        instance.setExtra();
        instance.setVisible();
        // check cleared state
        Assert.assertTrue(instance.isDirty());
        Assert.assertTrue(instance.isRoot());
        Assert.assertTrue(instance.isWire());
        Assert.assertTrue(instance.isDisposed());
        Assert.assertTrue(instance.isInitialized());
        Assert.assertTrue(instance.isInternalDirty());
        Assert.assertTrue(instance.isExtra());
        Assert.assertTrue(instance.isVisible());
        // clear flags
        instance.clearValues();
        // check cleared state
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
    }

    /**
     * Test of setFlags method, of class AbstractFlag.
     */
    @Test
    public void testSetFlags() {
        final ActorState instance = newValueObject();
        // check cleared state
        Assert.assertFalse(instance.isDirty());
        Assert.assertFalse(instance.isRoot());
        Assert.assertFalse(instance.isWire());
        Assert.assertFalse(instance.isDisposed());
        Assert.assertFalse(instance.isInitialized());
        Assert.assertFalse(instance.isInternalDirty());
        Assert.assertFalse(instance.isExtra());
        Assert.assertFalse(instance.isVisible());
        // trip flags
        instance.setValues();
        // check tripped state
        Assert.assertTrue(instance.isDirty());
        Assert.assertTrue(instance.isRoot());
        Assert.assertTrue(instance.isWire());
        Assert.assertTrue(instance.isDisposed());
        Assert.assertTrue(instance.isInitialized());
        Assert.assertTrue(instance.isInternalDirty());
        Assert.assertTrue(instance.isExtra());
        Assert.assertTrue(instance.isVisible());
    }

    @Override
    protected ActorState copyValueObject(final ActorState object) {
        final ActorState copy = newValueObject();
        copy.clearValues();
        if (object.isDirty()) {
            copy.setDirty();
        }
        if (object.isRoot()) {
            copy.setRoot();
        }
        if (object.isWire()) {
            copy.setWire();
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
        if (object.isVisible()) {
            copy.setVisible();
        }
        return copy;
    }

    @Override
    protected ActorState newValueObject() {
        final ActorState instance = new ActorState();
        instance.clearValues();
        final byte next = counter;
        counter++;
        if ((next & 0x01) == 0x01) {
            instance.setDirty();
        }
        if ((next & 0x02) == 0x02) {
            instance.setRoot();
        }
        if ((next & 0x04) == 0x04) {
            instance.setWire();
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
            instance.setVisible();
        }
        return instance;
    }

    @Override
    protected void setFieldsToNull(final ActorState object) {
        // no operation
    }
}
