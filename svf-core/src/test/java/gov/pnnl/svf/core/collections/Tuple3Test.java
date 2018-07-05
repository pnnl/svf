package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class Tuple3Test extends AbstractObjectTestBase<Tuple3<Integer, Integer, Integer>> {

    private final Random random = new Random();

    /**
     * Test of get method, of class Tuple3.
     */
    @Test
    public void testGet() {
        final Tuple3<Integer, Integer, Integer> a = new Tuple3<>(1, 2, 3);
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
                .first(1)
                .second(2)
                .third(3)
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
        final Tuple3<Integer, Integer, Integer> a = new Tuple3<>(1, 2, 3);
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
