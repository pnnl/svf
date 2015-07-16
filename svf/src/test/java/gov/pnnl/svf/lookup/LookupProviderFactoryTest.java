package gov.pnnl.svf.lookup;

import gov.pnnl.svf.core.lookup.LookupProvider;
import gov.pnnl.svf.core.lookup.LookupProviderFactory;
import gov.pnnl.svf.core.lookup.MultiLookupProvider;
import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.SceneLookupImpl;
import gov.pnnl.svf.test.PerformanceStats;
import gov.pnnl.svf.util.ConfigUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class LookupProviderFactoryTest {

    private static final int ITERATIONS = 10000;

    public LookupProviderFactoryTest() {
    }

    @SuppressWarnings("unused")
    @Test
    public void testPerformance() {
        final List<LookupProvider> instances = new ArrayList<>();
        instances.add(LookupProviderFactory.newLookupProvider());
        instances.add(LookupProviderFactory.newMultiLookupProvider());
        instances.add(new SceneLookupImpl(new ProxyScene(new ProxyGLCanvas(), ConfigUtil.configure())));
        //        instances.add(LookupProviderFactory.newMultiLookupProvider(true));
        //        instances.add(LookupProviderFactory.newMultiLookupProvider(false));
        // build the objects
        final List<Foo> objects = new ArrayList<>(ITERATIONS);
        for (int i = 0; i < ITERATIONS; i++) {
            objects.add(new Foo());
        }
        // iterate and retrieve class info so the tests are fair
        final LookupProvider lookup = LookupProviderFactory.newLookupProvider();
        for (final Foo object : objects) {
            lookup.add(object);
        }
        for (final Foo object : objects) {
            lookup.remove(object);
        }
        // test performance
        for (final LookupProvider instance : instances) {

            // performance test of add
            long start = System.currentTimeMillis();
            for (final Foo object : objects) {
                instance.add(object);
            }
            long end = System.currentTimeMillis();
            PerformanceStats.write(instance.getClass().getSimpleName() + ".add(object)", ITERATIONS, end - start);

            // performance test of lookup
            start = System.currentTimeMillis();
            for (final Foo object : objects) {
                instance.lookup(Foo.class);
            }
            end = System.currentTimeMillis();
            PerformanceStats.write(instance.getClass().getSimpleName() + ".lookup(class)", ITERATIONS, end - start);

            // performance test of lookupAll
            start = System.currentTimeMillis();
            for (final Foo object : objects) {
                instance.lookupAll();
            }
            end = System.currentTimeMillis();
            PerformanceStats.write(instance.getClass().getSimpleName() + ".lookupAll", ITERATIONS, end - start);

            // performance test of lookupAll
            start = System.currentTimeMillis();
            Collection<Object> all = new ArrayList<>();
            for (final Foo object : objects) {
                instance.lookupAll(all);
            }
            end = System.currentTimeMillis();
            PerformanceStats.write(instance.getClass().getSimpleName() + ".lookupAll(list)", ITERATIONS, end - start);

            // performance test of lookupAll
            start = System.currentTimeMillis();
            all = new HashSet<>();
            for (final Foo object : objects) {
                instance.lookupAll(all);
            }
            end = System.currentTimeMillis();
            PerformanceStats.write(instance.getClass().getSimpleName() + ".lookupAll(set)", ITERATIONS, end - start);

            //performance test of remove
            start = System.currentTimeMillis();
            for (final Foo object : objects) {
                instance.remove(object);
            }
            end = System.currentTimeMillis();
            PerformanceStats.write(instance.getClass().getSimpleName() + ".remove(object)", ITERATIONS, end - start);

            // multi lookup providers
            if (instance instanceof MultiLookupProvider) {
                final MultiLookupProvider multi = (MultiLookupProvider) instance;

                // performance test of addAll
                start = System.currentTimeMillis();
                multi.addAll(objects);
                end = System.currentTimeMillis();
                PerformanceStats.write(instance.getClass().getSimpleName() + ".addAll(collection)", ITERATIONS, end - start);

                // performance test of lookupAll
                start = System.currentTimeMillis();
                for (final Foo object : objects) {
                    multi.lookupAll(Foo.class);
                }
                end = System.currentTimeMillis();
                PerformanceStats.write(instance.getClass().getSimpleName() + ".lookupAll(class)", ITERATIONS, end - start);

                // performance test of lookupAll
                final Collection<Foo> list = new ArrayList<>();
                start = System.currentTimeMillis();
                for (final Foo object : objects) {
                    multi.lookupAll(Foo.class, list);
                }
                end = System.currentTimeMillis();
                PerformanceStats.write(instance.getClass().getSimpleName() + ".lookupAll(class, list)", ITERATIONS, end - start);

                // performance test of lookupAll
                final Collection<Foo> set = new HashSet<>();
                start = System.currentTimeMillis();
                for (final Foo object : objects) {
                    multi.lookupAll(Foo.class, set);
                }
                end = System.currentTimeMillis();
                PerformanceStats.write(instance.getClass().getSimpleName() + ".lookupAll(class, set)", ITERATIONS, end - start);

                // performance test of lookupAll
                start = System.currentTimeMillis();
                multi.removeAll(objects);
                end = System.currentTimeMillis();
                PerformanceStats.write(instance.getClass().getSimpleName() + ".removeAll(collection)", ITERATIONS, end - start);
            }
        }
    }

    /**
     * Test of newLookupProvider method, of class LookupProviderFactory.
     */
    @Test
    public void testNewLookupProvider() {
        final LookupProvider instance = LookupProviderFactory.newLookupProvider();
        Assert.assertNotNull(instance);
    }

    /**
     * Test of newMultiLookupProvider method, of class LookupProviderFactory.
     */
    @Test
    public void testNewMultiLookupProvider_0args() {
        final MultiLookupProvider instance = LookupProviderFactory.newMultiLookupProvider();
        Assert.assertNotNull(instance);
    }

    public static class Foo extends Bar {
    }

    protected static class Bar implements Ed {
    }

    static interface Ed extends Y {
    }

    private static interface Y {
    }
}
