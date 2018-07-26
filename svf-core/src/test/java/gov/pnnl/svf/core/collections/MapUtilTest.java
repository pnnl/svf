package gov.pnnl.svf.core.collections;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur
 */
public class MapUtilTest {

    @Test(expected = IllegalAccessException.class)
    public void testConstructor() throws Exception {
        MapUtil.class.newInstance();
    }

    /**
     * Test of construct method, of class Builder.
     */
    @Test
    public void testConstruct_0args() {
        final Map<String, Tuple2<Integer, Double>> expected = new HashMap<>();
        expected.put("one", new Tuple2<>(1, 1.0));
        expected.put("two", new Tuple2<>(2, 2.0));
        expected.put("three", new Tuple2<>(3, 3.0));
        Assert.assertNotEquals(expected, MapUtil.Builder.<String, Tuple2<Integer, Double>>construct()
                               .with("one", new Tuple2<>(1, 1.0))
                               .with("two", new Tuple2<>(2, 2.0))
                               .with("three", new Tuple2<>(3, 3.1))
                               .build());
        Assert.assertEquals(expected, MapUtil.Builder.<String, Tuple2<Integer, Double>>construct()
                            .with("one", new Tuple2<>(1, 1.0))
                            .with("two", new Tuple2<>(2, 2.0))
                            .with("three", new Tuple2<>(3, 3.0))
                            .build());
        Assert.assertEquals(expected.getClass(), MapUtil.Builder.<String, Tuple2<Integer, Double>>construct().build().getClass());
    }

    /**
     * Test of construct method, of class Builder.
     */
    @Test
    public void testConstruct_Class_Class() {
        final Map<String, Tuple2<Integer, Double>> expected = new HashMap<>();
        expected.put("one", new Tuple2<>(1, 1.0));
        expected.put("two", new Tuple2<>(2, 2.0));
        expected.put("three", new Tuple2<>(3, 3.0));
        Assert.assertNotEquals(expected, MapUtil.Builder.construct(String.class, Tuple2.class)
                               .with("one", new Tuple2<>(1, 1.0))
                               .with("two", new Tuple2<>(2, 2.0))
                               .with("three", new Tuple2<>(3, 3.1))
                               .build());
        Assert.assertEquals(expected, MapUtil.Builder.construct(String.class, Tuple2.class)
                            .with("one", new Tuple2<>(1, 1.0))
                            .with("two", new Tuple2<>(2, 2.0))
                            .with("three", new Tuple2<>(3, 3.0))
                            .build());
        Assert.assertEquals(expected.getClass(), MapUtil.Builder.construct(String.class, Tuple2.class).build().getClass());
    }

    /**
     * Test of construct method, of class Builder.
     */
    @Test
    public void testConstruct_Map() {
        final Map<String, Tuple2<Integer, Double>> expected = new HashMap<>();
        expected.put("one", new Tuple2<>(1, 1.0));
        expected.put("two", new Tuple2<>(2, 2.0));
        expected.put("three", new Tuple2<>(3, 3.0));
        Assert.assertNotEquals(expected, MapUtil.Builder.construct(new HashMap())
                               .with("one", new Tuple2<>(1, 1.0))
                               .with("two", new Tuple2<>(2, 2.0))
                               .with("three", new Tuple2<>(3, 3.1))
                               .build());
        Assert.assertEquals(expected, MapUtil.Builder.construct(new HashMap())
                            .with("one", new Tuple2<>(1, 1.0))
                            .with("two", new Tuple2<>(2, 2.0))
                            .with("three", new Tuple2<>(3, 3.0))
                            .build());
        Assert.assertEquals(expected, MapUtil.Builder.construct(new HashMap(expected))
                            .build());
        Assert.assertEquals(expected.getClass(), MapUtil.Builder.construct(new HashMap(expected)).build().getClass());
    }

    /**
     * Test of construct method, of class Builder.
     */
    @Test
    public void testConstruct_Class() throws Exception {
        final Map<String, Tuple2<Integer, Double>> expected = new HashMap<>();
        expected.put("one", new Tuple2<>(1, 1.0));
        expected.put("two", new Tuple2<>(2, 2.0));
        expected.put("three", new Tuple2<>(3, 3.0));
        Assert.assertNotEquals(expected, MapUtil.Builder.construct(HashMap.class)
                               .with("one", new Tuple2<>(1, 1.0))
                               .with("two", new Tuple2<>(2, 2.0))
                               .with("three", new Tuple2<>(3, 3.1))
                               .build());
        Assert.assertEquals(expected, MapUtil.Builder.construct(HashMap.class)
                            .with("one", new Tuple2<>(1, 1.0))
                            .with("two", new Tuple2<>(2, 2.0))
                            .with("three", new Tuple2<>(3, 3.0))
                            .build());
        Assert.assertEquals(expected.getClass(), MapUtil.Builder.construct(HashMap.class).build().getClass());
    }

    /**
     * Test of construct method, of class Builder.
     */
    @Test
    public void testConstruct_Class_Alt() throws Exception {
        final Map<String, Tuple2<Integer, Double>> expected = Collections.synchronizedMap(new HashMap<>());
        expected.put("one", new Tuple2<>(1, 1.0));
        expected.put("two", new Tuple2<>(2, 2.0));
        expected.put("three", new Tuple2<>(3, 3.0));
        Assert.assertNotEquals(expected, MapUtil.Builder.construct(HashMap.class)
                               .with("one", new Tuple2<>(1, 1.0))
                               .with("two", new Tuple2<>(2, 2.0))
                               .with("three", new Tuple2<>(3, 3.1))
                               .build());
        Assert.assertEquals(expected, MapUtil.Builder.construct(HashMap.class)
                            .with("one", new Tuple2<>(1, 1.0))
                            .with("two", new Tuple2<>(2, 2.0))
                            .with("three", new Tuple2<>(3, 3.0))
                            .build());
    }

    /**
     * Test of construct method, of class Builder.
     */
    @Test
    public void testConstruct_3args_2() throws Exception {
        final Map<String, Tuple2<Integer, Double>> expected = new HashMap<>();
        expected.put("one", new Tuple2<>(1, 1.0));
        expected.put("two", new Tuple2<>(2, 2.0));
        expected.put("three", new Tuple2<>(3, 3.0));
        Assert.assertNotEquals(expected, MapUtil.Builder.construct(HashMap.class, String.class, Tuple2.class)
                               .with("one", new Tuple2<>(1, 1.0))
                               .with("two", new Tuple2<>(2, 2.0))
                               .with("three", new Tuple2<>(3, 3.1))
                               .build());
        Assert.assertEquals(expected, MapUtil.Builder.construct(HashMap.class, String.class, Tuple2.class)
                            .with("one", new Tuple2<>(1, 1.0))
                            .with("two", new Tuple2<>(2, 2.0))
                            .with("three", new Tuple2<>(3, 3.0))
                            .build());
        Assert.assertEquals(expected.getClass(), MapUtil.Builder.construct(HashMap.class, String.class, Tuple2.class).build().getClass());
    }

}
