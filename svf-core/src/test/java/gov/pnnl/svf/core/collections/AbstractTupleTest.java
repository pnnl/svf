package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class AbstractTupleTest extends AbstractObjectTestBase<AbstractTuple> {

    private final Random random = new Random();

    /**
     * Test of get method, of class AbstractTuple.
     */
    @Test
    public void testGet() {
        final AbstractTuple a = new AbstractTupleImpl(1, Integer.valueOf(2), Integer.valueOf(3));
        Assert.assertEquals(1, a.get(0));
        Assert.assertEquals(2, a.get(1));
        Assert.assertEquals(3, a.get(2));
    }

    /**
     * Test of size method, of class AbstractTuple.
     */
    @Test
    public void testSize() {
        final AbstractTuple a = new AbstractTupleImpl(1, Integer.valueOf(2), Integer.valueOf(3));
        Assert.assertEquals(3, a.size());
    }

    @Override
    protected AbstractTuple copyValueObject(final AbstractTuple object) {
        final Object[] args = new Object[object.size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = object.get(i);
        }
        return new AbstractTupleImpl(args);
    }

    @Override
    protected AbstractTuple newValueObject() {
        final Object[] args = new Object[random.nextInt(9) + 1];
        for (int i = 0; i < args.length; i++) {
            args[i] = random.nextInt();
        }
        return new AbstractTupleImpl(args);
    }

    @Override
    protected void setFieldsToNull(final AbstractTuple object) {
        // no operation
    }

    protected static class AbstractTupleImpl extends AbstractTuple {

        private static final long serialVersionUID = 1L;

        protected AbstractTupleImpl(final Object... values) {
            super(values);
        }
    }
}
