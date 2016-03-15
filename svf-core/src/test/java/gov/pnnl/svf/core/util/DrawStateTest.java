package gov.pnnl.svf.core.util;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class DrawStateTest extends AbstractObjectTestBase<DrawState> {

    private byte counter = 0x00;

    public DrawStateTest() {
        collisionIterateLength = 0;
    }

    /**
     * Test of isRenderedFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsRenderedFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isRendered());
        // trip flag
        instance.setRendered();
        // tripped state
        Assert.assertTrue(instance.isRendered());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        //        Assert.assertFalse(instance.isRenderedFlag());
        // toString test
        System.out.println("Rendered: " + instance);
    }

    /**
     * Test of isAttribFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsAttribFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isAttrib());
        // trip flag
        instance.setAttrib();
        // tripped state
        Assert.assertTrue(instance.isAttrib());
        // check others
        //        Assert.assertFalse(instance.isAttribFlag());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("Attrib: " + instance);
    }

    /**
     * Test of isMatrixModelviewFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixModelviewFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isMatrixModelview());
        // trip flag
        instance.setMatrixModelview();
        // tripped state
        Assert.assertTrue(instance.isMatrixModelview());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        //        Assert.assertFalse(instance.isMatrixModelviewFlag());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("MatrixModelview: " + instance);
    }

    /**
     * Test of isMatrixProjectionFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixProjectionFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isMatrixProjection());
        // trip flag
        instance.setMatrixProjection();
        // tripped state
        Assert.assertTrue(instance.isMatrixProjection());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        //        Assert.assertFalse(instance.isMatrixProjectionFlag());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("MatrixProjection: " + instance);
    }

    /**
     * Test of isMatrixTextureFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsMatrixTextureFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isMatrixTexture());
        // trip flag
        instance.setMatrixTexture();
        // tripped state
        Assert.assertTrue(instance.isMatrixTexture());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        //        Assert.assertFalse(instance.isMatrixTextureFlag());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("MatrixTexture: " + instance);
    }

    /**
     * Test of isExtraAFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraAFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isExtraA());
        // trip flag
        instance.setExtraA();
        // tripped state
        Assert.assertTrue(instance.isExtraA());
        // check others
        Assert.assertFalse(instance.isAttrib());
        //        Assert.assertFalse(instance.isExtraAFlag());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("ExtraA: " + instance);
    }

    /**
     * Test of isExtraBFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraBFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isExtraB());
        // trip flag
        instance.setExtraB();
        // tripped state
        Assert.assertTrue(instance.isExtraB());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        //        Assert.assertFalse(instance.isExtraBFlag());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("ExtraB: " + instance);
    }

    /**
     * Test of isExtraCFlag method, of class AbstractFlag.
     */
    @Test
    public void testIsExtraCFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isExtraC());
        // trip flag
        instance.setExtraC();
        // tripped state
        Assert.assertTrue(instance.isExtraC());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        //        Assert.assertFalse(instance.isExtraCFlag());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("ExtraC: " + instance);
    }

    /**
     * Test of setRenderedFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetRenderedFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isRendered());
        // trip flag
        instance.setRendered(true);
        // tripped state
        Assert.assertTrue(instance.isRendered());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        //        Assert.assertFalse(instance.isRenderedFlag());
        // toString test
        System.out.println("Rendered: " + instance);
        // untrip flag
        instance.setRendered(false);
        // check
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of setAttribFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetAttribFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isAttrib());
        // trip flag
        instance.setAttrib(true);
        // tripped state
        Assert.assertTrue(instance.isAttrib());
        // check others
        //        Assert.assertFalse(instance.isAttribFlag());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("Attrib: " + instance);
        // untrip flag
        instance.setAttrib(false);
        // check
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of setMatrixModelviewFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixModelviewFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isMatrixModelview());
        // trip flag
        instance.setMatrixModelview(true);
        // tripped state
        Assert.assertTrue(instance.isMatrixModelview());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        //        Assert.assertFalse(instance.isMatrixModelviewFlag());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("MatrixModelview: " + instance);
        // untrip flag
        instance.setMatrixModelview(false);
        // check
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of setMatrixProjectionFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixProjectionFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isMatrixProjection());
        // trip flag
        instance.setMatrixProjection(true);
        // tripped state
        Assert.assertTrue(instance.isMatrixProjection());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        //        Assert.assertFalse(instance.isMatrixProjectionFlag());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("MatrixProjection: " + instance);
        // untrip flag
        instance.setMatrixProjection(false);
        // check
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of setMatrixTextureFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetMatrixTextureFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isMatrixTexture());
        // trip flag
        instance.setMatrixTexture(true);
        // tripped state
        Assert.assertTrue(instance.isMatrixTexture());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        //        Assert.assertFalse(instance.isMatrixTextureFlag());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("MatrixTexture: " + instance);
        // untrip flag
        instance.setMatrixTexture(false);
        // check
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of setExtraAFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraAFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isExtraA());
        // trip flag
        instance.setExtraA(true);
        // tripped state
        Assert.assertTrue(instance.isExtraA());
        // check others
        Assert.assertFalse(instance.isAttrib());
        //        Assert.assertFalse(instance.isExtraAFlag());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("ExtraA: " + instance);
        // untrip flag
        instance.setExtraA(false);
        // check
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of setExtraBFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraBFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isExtraB());
        // trip flag
        instance.setExtraB(true);
        // tripped state
        Assert.assertTrue(instance.isExtraB());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        //        Assert.assertFalse(instance.isExtraBFlag());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("ExtraB: " + instance);
        // untrip flag
        instance.setExtraB(false);
        // check
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of setExtraCFlag method, of class AbstractFlag.
     */
    @Test
    public void testSetExtraCFlag() {
        final DrawState instance = newValueObject();
        // initial state
        Assert.assertFalse(instance.isExtraC());
        // trip flag
        instance.setExtraC(true);
        // tripped state
        Assert.assertTrue(instance.isExtraC());
        // check others
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        //        Assert.assertFalse(instance.isExtraCFlag());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // toString test
        System.out.println("ExtraC: " + instance);
        // untrip flag
        instance.setExtraC(false);
        // check
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of resetFlags method, of class AbstractFlag.
     */
    @Test
    public void testResetFlags() {
        final DrawState instance = newValueObject();
        // trip flag
        instance.setAttrib();
        instance.setExtraA();
        instance.setExtraB();
        instance.setExtraC();
        instance.setMatrixModelview();
        instance.setMatrixProjection();
        instance.setMatrixTexture();
        instance.setRendered();
        // check cleared state
        Assert.assertTrue(instance.isAttrib());
        Assert.assertTrue(instance.isExtraA());
        Assert.assertTrue(instance.isExtraB());
        Assert.assertTrue(instance.isExtraC());
        Assert.assertTrue(instance.isMatrixModelview());
        Assert.assertTrue(instance.isMatrixProjection());
        Assert.assertTrue(instance.isMatrixTexture());
        Assert.assertTrue(instance.isRendered());
        // clear flags
        instance.clearValues();
        // check cleared state
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
    }

    /**
     * Test of setFlags method, of class AbstractFlag.
     */
    @Test
    public void testSetFlags() {
        final DrawState instance = newValueObject();
        // check cleared state
        Assert.assertFalse(instance.isAttrib());
        Assert.assertFalse(instance.isExtraA());
        Assert.assertFalse(instance.isExtraB());
        Assert.assertFalse(instance.isExtraC());
        Assert.assertFalse(instance.isMatrixModelview());
        Assert.assertFalse(instance.isMatrixProjection());
        Assert.assertFalse(instance.isMatrixTexture());
        Assert.assertFalse(instance.isRendered());
        // trip flags
        instance.setValues();
        // check tripped state
        Assert.assertTrue(instance.isAttrib());
        Assert.assertTrue(instance.isExtraA());
        Assert.assertTrue(instance.isExtraB());
        Assert.assertTrue(instance.isExtraC());
        Assert.assertTrue(instance.isMatrixModelview());
        Assert.assertTrue(instance.isMatrixProjection());
        Assert.assertTrue(instance.isMatrixTexture());
        Assert.assertTrue(instance.isRendered());
    }

    @Override
    protected DrawState copyValueObject(final DrawState object) {
        final DrawState copy = new DrawState();
        if (object.isAttrib()) {
            copy.setAttrib();
        }
        if (object.isExtraA()) {
            copy.setExtraA();
        }
        if (object.isExtraB()) {
            copy.setExtraB();
        }
        if (object.isExtraC()) {
            copy.setExtraC();
        }
        if (object.isMatrixModelview()) {
            copy.setMatrixModelview();
        }
        if (object.isMatrixProjection()) {
            copy.setMatrixProjection();
        }
        if (object.isMatrixTexture()) {
            copy.setMatrixTexture();
        }
        if (object.isRendered()) {
            copy.setRendered();
        }
        return copy;
    }

    @Override
    protected DrawState newValueObject() {
        final DrawState instance = new DrawState();
        final byte next = counter;
        counter++;
        if ((next & 0x01) == 0x01) {
            instance.setAttrib();
        }
        if ((next & 0x02) == 0x02) {
            instance.setExtraA();
        }
        if ((next & 0x04) == 0x04) {
            instance.setExtraB();
        }
        if ((next & 0x08) == 0x08) {
            instance.setExtraC();
        }
        if ((next & 0x10) == 0x10) {
            instance.setMatrixModelview();
        }
        if ((next & 0x20) == 0x20) {
            instance.setMatrixProjection();
        }
        if ((next & 0x40) == 0x40) {
            instance.setMatrixTexture();
        }
        if ((next & 0x80) == 0x80) {
            instance.setRendered();
        }
        return instance;
    }

    @Override
    protected void setFieldsToNull(final DrawState object) {
        // no operation
    }
}
