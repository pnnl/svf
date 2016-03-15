package gov.pnnl.svf.util;

import com.jogamp.opengl.GLCapabilities;
import gov.pnnl.svf.scene.SceneBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ConfigUtilTest {

    private static final Logger logger = Logger.getLogger(ConfigUtilTest.class.getName());

    @Test
    @SuppressWarnings("unchecked")
    public void writeOpenGLProperties() throws Exception {
        File dir = new File(ConfigUtilTest.class.getResource("/root").getFile());
        while (dir != null && !"svf".equals(dir.getName())) {
            dir = dir.getParentFile();
        }
        File file = new File(dir, "/src/main/resources/" + ConfigUtil.OPENGL_CONFIG_RESOURCE);
        file = new File(file.getAbsolutePath().replaceAll("%20", " "));
        if (file.exists()) {
            file.delete();
        }
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            final Properties properties = new Properties();
            final GLCapabilities capabilities = ConfigUtil.newGLCapabilities("GL");
            properties.putAll(BeanUtils.describe(capabilities));
            properties.remove("class");
            properties.remove("GLProfile");
            properties.store(output, "GL Capabilities Default Properties");

//            try {
//            final Map<String, Object> converted = new HashMap<String, Object>();
//            for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
//                converted.put(entry.getKey().toString(), entry.getValue());
//            }
//            BeanUtils.populate(capabilities, converted);
//            } catch (final IllegalAccessException ex) {
//                logger.log(Level.WARNING, "Unable to populate GL capabilities.", ex);
//            } catch (final InvocationTargetException ex) {
//                logger.log(Level.WARNING, "Unable to populate GL capabilities.", ex);
//            }
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to write default OpenGL properties.", ex);
            throw ex;
        } finally {
            if (output != null) {
                IOUtils.closeQuietly(output);
            }
        }
        ConfigUtilTest.removeDateComment(file);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void writeSceneBuilderProperties() throws Exception {
        File dir = new File(getClass().getResource("/root").getFile());
        while (dir != null && !"svf".equals(dir.getName())) {
            dir = dir.getParentFile();
        }
        File file = new File(dir, "/src/main/resources/" + ConfigUtil.SVF_CONFIG_RESOURCE);
        file = new File(file.getAbsolutePath().replaceAll("%20", " "));
        if (file.exists()) {
            file.delete();
        }
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            final Properties properties = new Properties();
            final SceneBuilder builder = ConfigUtil.configure();
            properties.putAll(BeanUtils.describe(builder));
            properties.remove("class");
            properties.remove("background");
            properties.remove("GLCapabilities");
            properties.store(output, "Scene Builder Default Properties");
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to write default SVF properties.", ex);
            throw ex;
        } finally {
            if (output != null) {
                IOUtils.closeQuietly(output);
            }
        }
        ConfigUtilTest.removeDateComment(file);
    }

    private static void removeDateComment(final File file) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            // leave the first line
            raf.readLine();
            // skip a line
            long writePos = raf.getFilePointer();
            raf.readLine();
            long readPos = raf.getFilePointer();
            // shift the remaining lines up one
            final byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = raf.read(buf))) {
                raf.seek(writePos);
                raf.write(buf, 0, n);
                readPos += n;
                writePos += n;
                raf.seek(readPos);
            }
            // set the new end of file length
            raf.setLength(writePos);
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to remove date comment from properties file.", ex);
            throw ex;
        } finally {
            if (raf != null) {
                IOUtils.closeQuietly(raf);
            }
        }
    }

    /**
     * Test of newGLCapabilities method, of class ConfigUtil.
     */
    @Test
    public void testNewGLCapabilities_0args() {
        Assert.assertNotNull(ConfigUtil.newGLCapabilities());
    }

    /**
     * Test of newGLCapabilities method, of class ConfigUtil.
     */
    @Test
    public void testNewGLCapabilities_String() {
        Assert.assertNotNull(ConfigUtil.newGLCapabilities((String) null));
        Assert.assertNotNull(ConfigUtil.newGLCapabilities("GL"));
    }

    /**
     * Test of newGLCapabilities method, of class ConfigUtil.
     */
    @Test
    public void testNewGLCapabilities_Properties() {
        Assert.assertNotNull(ConfigUtil.newGLCapabilities(ConfigUtil.newGLCapabilitiesProperties()));
    }

    /**
     * Test of newGLCapabilities method, of class ConfigUtil.
     */
    @Test
    public void testNewGLCapabilities_String_Properties() {
        Assert.assertNotNull(ConfigUtil.newGLCapabilities("GL", ConfigUtil.newGLCapabilitiesProperties()));
    }

    /**
     * Test of newDebugGL method, of class ConfigUtil.
     */
    @Test
    public void testNewDebugGL() {
        Assert.assertNull(ConfigUtil.newDebugGL(null));
    }

    /**
     * Test of newSceneBuilder method, of class ConfigUtil.
     */
    @Test
    public void testNewSceneBuilder_Properties() {
        final Properties props = ConfigUtil.newSceneBuilderProperties();
        Assert.assertEquals("false", props.getProperty("verbose"));
        props.put("verbose", "true");
        final SceneBuilder builder = ConfigUtil.newSceneBuilder(props);
        Assert.assertTrue(builder.isVerbose());
    }

    /**
     * Test of newSceneBuilderProperties method, of class ConfigUtil.
     */
    @Test
    public void testNewSceneBuilderProperties() {
        Assert.assertNotNull(ConfigUtil.newSceneBuilderProperties());
    }
}
