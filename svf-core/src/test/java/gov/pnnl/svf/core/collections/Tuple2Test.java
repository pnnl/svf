package gov.pnnl.svf.core.collections;

import gov.pnnl.svf.test.AbstractObjectTestBase;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class Tuple2Test extends AbstractObjectTestBase<Tuple2<Integer, Integer>> {

    private final Random random = new Random();

    /**
     * Test of get method, of class Tuple2.
     */
    @Test
    public void testGet() {
        final Tuple2<Integer, Integer> a = new Tuple2<>(Integer.valueOf(1), Integer.valueOf(2));
        Assert.assertEquals(1, a.get(0));
        Assert.assertEquals(2, a.get(1));
    }

    /**
     * Test of get method, of class Tuple2.
     */
    @Test
    public void testGetBuilder() {
        final Tuple2<Integer, Integer> a = Tuple2.Builder.<Integer, Integer>construct()
                .first(Integer.valueOf(1))
                .second(Integer.valueOf(2))
                .build();
        Assert.assertEquals(1, a.get(0));
        Assert.assertEquals(2, a.get(1));
    }

    /**
     * Test of size method, of class Tuple2.
     */
    @Test
    public void testSize() {
        final Tuple2<Integer, Integer> a = new Tuple2<>(Integer.valueOf(1), Integer.valueOf(2));
        Assert.assertEquals(2, a.size());
    }

    @Override
    protected Tuple2<Integer, Integer> copyValueObject(final Tuple2<Integer, Integer> object) {
        return new Tuple2<>(object.getFirst(), object.getSecond());
    }

    @Override
    protected Tuple2<Integer, Integer> newValueObject() {
        return new Tuple2<>(random.nextInt(), random.nextInt());
    }

    @Override
    protected void setFieldsToNull(final Tuple2<Integer, Integer> object) {
        // no operation
    }
}
