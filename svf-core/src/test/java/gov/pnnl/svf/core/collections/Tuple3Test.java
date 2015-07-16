package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Tuple3Test extends AbstractObjectTestBase<Tuple3<Integer, Integer, Integer>> {

    private final Random random = new Random();

    /**
     * Test of get method, of class Tuple3.
     */
    @Test
    public void testGet() {
        final Tuple3<Integer, Integer, Integer> a = new Tuple3<>(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
        Assert.assertEquals(1, a.get(0));
        Assert.assertEquals(2, a.get(1));
        Assert.assertEquals(3, a.get(2));
    }

    /**
     * Test of get method, of class Tuple3.
     */
    @Test
    public void testGetBuilder() {
        final Tuple3<Integer, Integer, Integer> a = Tuple3.Builder.<Integer, Integer, Integer>construct()
                .first(Integer.valueOf(1))
                .second(Integer.valueOf(2))
                .third(Integer.valueOf(3))
                .build();
        Assert.assertEquals(1, a.get(0));
        Assert.assertEquals(2, a.get(1));
        Assert.assertEquals(3, a.get(2));
    }

    /**
     * Test of size method, of class Tuple3.
     */
    @Test
    public void testSize() {
        final Tuple3<Integer, Integer, Integer> a = new Tuple3<>(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
        Assert.assertEquals(3, a.size());
    }

    @Override
    protected Tuple3<Integer, Integer, Integer> copyValueObject(final Tuple3<Integer, Integer, Integer> object) {
        return new Tuple3<>(object.getFirst(), object.getSecond(), object.getThird());
    }

    @Override
    protected Tuple3<Integer, Integer, Integer> newValueObject() {
        return new Tuple3<>(random.nextInt(), random.nextInt(), random.nextInt());
    }

    @Override
    protected void setFieldsToNull(final Tuple3<Integer, Integer, Integer> object) {
        // no operation
    }
}
