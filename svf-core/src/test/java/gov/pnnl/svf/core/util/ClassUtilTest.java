package gov.pnnl.svf.core.util;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ClassUtilTest {

    public ClassUtilTest() {
    }

    /**
     * Test of getClasses method, of class ClassUtil.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetClasses_String() throws Exception {
        final String packageName = ClassUtil.class.getName().replace(".ClassUtil", "");
        final List<Class<?>> list = ClassUtil.getClasses(packageName);
        Assert.assertTrue(list.contains(ClassUtil.class));
    }

    /**
     * Test of getClasses method, of class ClassUtil.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetClasses_ClassLoader_String() throws Exception {
        final String packageName = ClassUtil.class.getName().replace(".ClassUtil", "");
        final List<Class<?>> list = ClassUtil.getClasses(ClassUtil.class.getClassLoader(), packageName);
        Assert.assertTrue(list.contains(ClassUtil.class));
    }

}
