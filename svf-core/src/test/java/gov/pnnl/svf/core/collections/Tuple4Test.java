package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Tuple4Test extends AbstractObjectTestBase<Tuple4<Integer, Integer, Integer, Integer>> {

    private final Random random = new Random();

    /**
     * Test of get method, of class Tuple4.
     */
    @Test
    public void testGet() {
        final Tuple4<Integer, Integer, Integer, Integer> a = new Tuple4<>(1, 2, 3, 4);
        Assert.assertEquals(1, a.get(0));
        Assert.assertEquals(2, a.get(1));
        Assert.assertEquals(3, a.get(2));
        Assert.assertEquals(4, a.get(3));
    }

    /**
     * Test of get method, of class Tuple4.
     */
    @Test
    public void testGetBuilder() {
        final Tuple4<Integer, Integer, Integer, Integer> a = Tuple4.Builder.<Integer, Integer, Integer, Integer>construct()
                .first(1)
                .second(2)
                .third(3)
                .fourth(4)
                .build();
        Assert.assertEquals(1, a.get(0));
        Assert.assertEquals(2, a.get(1));
        Assert.assertEquals(3, a.get(2));
        Assert.assertEquals(4, a.get(3));
    }

    /**
     * Test of size method, of class Tuple4.
     */
    @Test
    public void testSize() {
        final Tuple4<Integer, Integer, Integer, Integer> a = new Tuple4<>(1, 2, 3, 3);
        Assert.assertEquals(4, a.size());
    }

    @Override
    protected Tuple4<Integer, Integer, Integer, Integer> copyValueObject(final Tuple4<Integer, Integer, Integer, Integer> object) {
        return new Tuple4<>(object.getFirst(), object.getSecond(), object.getThird(), object.getFourth());
    }

    @Override
    protected Tuple4<Integer, Integer, Integer, Integer> newValueObject() {
        return new Tuple4<>(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt());
    }

    @Override
    protected void setFieldsToNull(final Tuple4<Integer, Integer, Integer, Integer> object) {
        // no operation
    }
}
