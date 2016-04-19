package gov.pnnl.svf.util;

import com.jogamp.opengl.DebugGL2;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.DebugGL3bc;
import com.jogamp.opengl.DebugGL4;
import com.jogamp.opengl.DebugGL4bc;
import com.jogamp.opengl.DebugGLES1;
import com.jogamp.opengl.DebugGLES2;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import gov.pnnl.svf.core.util.ClassUtil;
import gov.pnnl.svf.hint.AutoConfigHint;
import gov.pnnl.svf.hint.CameraHint;
import gov.pnnl.svf.hint.FileConfigHint;
import gov.pnnl.svf.hint.GLProfileHint;
import gov.pnnl.svf.hint.OpenGLHint;
import gov.pnnl.svf.hint.PickingHint;
import gov.pnnl.svf.scene.AbstractSceneBuilder;
import gov.pnnl.svf.scene.SceneBuilder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * Utility for working with configuration for a scene.
 *
 * @author Arthur Bleeker
 */
public class ConfigUtil {

    /**
     * OpenGL configuration resource location.
     */
    public static final String OPENGL_CONFIG_RESOURCE = "gov/pnnl/svf/resources/opengl.properties";
    /**
     * Scene Builder configuration resource location.
     */
    public static final String SVF_CONFIG_RESOURCE = "gov/pnnl/svf/resources/svf.properties";
    /**
     * Logging configuration resource location.
     */
    public static final String LOGGING_CONFIG_RESOURCE = "gov/pnnl/svf/resources/logging.properties";
    /**
     * Property used by JOGL for verbose flag.
     */
    public static final String JOGL_VERBOSE_PROPERTY = "jogl.verbose";
    /**
     * Property used by JOGL for debug flag.
     */
    public static final String JOGL_DEBUG_PROPERTY = "jogl.debug";
    private static final Logger logger = Logger.getLogger(ConfigUtil.class.getName());
    private static final Properties OPENGL_CONFIG_DEFAULTS = new Properties();
    private static final Properties SVF_CONFIG_DEFAULTS = new Properties();
    private static final Properties LOGGING_CONFIG_DEFAULTS = new Properties();
    private static final String HINTS_STRING;

    static {
        // read in opengl config file
        InputStream input = null;
        try {
            input = ConfigUtil.class.getClassLoader().getResourceAsStream(OPENGL_CONFIG_RESOURCE);
            if (input == null) {
                throw new IOException("Could not locate resource.");
            }
            OPENGL_CONFIG_DEFAULTS.load(input);
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to load default OpenGL config: " + OPENGL_CONFIG_RESOURCE, ex);
        } finally {
            if (input != null) {
                IOUtils.closeQuietly(input);
            }
        }
        // read in svf config file
        input = null;
        try {
            input = ConfigUtil.class.getClassLoader().getResourceAsStream(SVF_CONFIG_RESOURCE);
            if (input == null) {
                throw new IOException("Could not locate resource.");
            }
            SVF_CONFIG_DEFAULTS.load(input);
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to load default SVF config: " + SVF_CONFIG_RESOURCE, ex);
        } finally {
            if (input != null) {
                IOUtils.closeQuietly(input);
            }
        }
        // read in logging file
        input = null;
        try {
            input = ConfigUtil.class.getClassLoader().getResourceAsStream(LOGGING_CONFIG_RESOURCE);
            if (input == null) {
                throw new IOException("Could not locate resource.");
            }
            LOGGING_CONFIG_DEFAULTS.load(input);
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to load default logging config: " + LOGGING_CONFIG_RESOURCE, ex);
        } finally {
            if (input != null) {
                IOUtils.closeQuietly(input);
            }
        }
        // build hints string
        final StringBuilder sb = new StringBuilder();
        final List<Class<?>> classes = ClassUtil.getClasses("gov.pnnl.svf.hint");
        for (final Class<?> cls : classes) {
            if (cls.isEnum()) {
                sb.append("\n");
                sb.append(cls.getSimpleName());
                sb.append(": ");
                final Object[] enumConstants = cls.getEnumConstants();
                for (int i = 0; i < enumConstants.length; i++) {
                    final Enum<?> enumConstant = (Enum<?>) enumConstants[i];
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(enumConstant.name());
                }
            }
        }
        HINTS_STRING = sb.toString();
        logger.log(Level.INFO, "Available hints: {0}", HINTS_STRING);
    }

    /**
     * Constructor is private to prevent instantiation of a static helper class.
     */
    protected ConfigUtil() {
        super();
    }

    /**
     * Create a new fully configured SceneBuilder and GLCapabilities. Logging
     * will also be updated accordingly.
     *
     * @return the new configuration
     */
    public static SceneBuilder configure() {
        return configure((Properties) null, (Properties) null, (Properties) null);
    }

    /**
     * Create a new fully configured SceneBuilder and GLCapabilities. Logging
     * will also be updated accordingly.
     *
     * @param svf     the optional svf properties file
     * @param opengl  the optional opengl properties file
     * @param logging the optional logging properties file
     *
     * @return the new configuration
     *
     * @throws IOException if property files can't be read
     */
    public static SceneBuilder configure(final String svf, final String opengl, final String logging) throws IOException {
        return configure(new File(svf), new File(opengl), new File(logging));
    }

    /**
     * Create a new fully configured SceneBuilder and GLCapabilities. Logging
     * will also be updated accordingly.
     *
     * @param svf     the optional svf properties file
     * @param opengl  the optional opengl properties file
     * @param logging the optional logging properties file
     *
     * @return the new configuration
     *
     * @throws IOException if property files can't be read
     */
    public static SceneBuilder configure(final File svf, final File opengl, final File logging) throws IOException {
        return configure(svf != null && svf.exists() ? ConfigUtil.loadProperties(svf, SVF_CONFIG_DEFAULTS) : null,
                         opengl != null && opengl.exists() ? ConfigUtil.loadProperties(opengl, OPENGL_CONFIG_DEFAULTS) : null,
                         logging != null && logging.exists() ? ConfigUtil.loadProperties(logging, LOGGING_CONFIG_DEFAULTS) : null);
    }

    /**
     * Create a new fully configured SceneBuilder and GLCapabilities. Logging
     * will also be updated accordingly.
     *
     * @param svf     the optional svf properties
     * @param opengl  the optional opengl properties
     * @param logging the optional logging properties
     *
     * @return the new configuration
     */
    public static SceneBuilder configure(final Properties svf, final Properties opengl, final Properties logging) {
        // locate configuration hints in svf properties
        SceneBuilder sceneBuilder = newSceneBuilder(svf);
        final Set<AutoConfigHint> autoConfigHints = sceneBuilder.copyHints(AutoConfigHint.class);
        final Set<FileConfigHint> fileConfigHints = sceneBuilder.copyHints(FileConfigHint.class);
        // use the default
        sceneBuilder = newSceneBuilder(newSceneBuilderProperties());
        // auto configure the scene builder
        if (autoConfigHints.contains(AutoConfigHint.SVF)) {
            sceneBuilder = newSceneBuilder();
        }
        // apply file configuration
        if (fileConfigHints.contains(FileConfigHint.SVF)) {
            if (svf != null) {
                populateSceneBuilder(sceneBuilder, svf);
            } else {
                logger.log(Level.WARNING, "FileConfigHint.SVF ignored because SVF properties file not specified.");
            }
        }
        // configure opengl
        final Set<GLProfileHint> gLProfileHints = sceneBuilder.copyHints(GLProfileHint.class);
        GLProfileHint version = GLProfileHint.GL_DEFAULT;
        for (final GLProfileHint gLProfileHint : GLProfileHint.values()) {
            if (gLProfileHints.contains(gLProfileHint)) {
                version = gLProfileHint;
                break;
            }
        }
        GLCapabilities capabilities = newGLCapabilities(version.name(), newGLCapabilitiesProperties());
        // auto configure the scene builder
        if (autoConfigHints.contains(AutoConfigHint.OPENGL)) {
            capabilities = newGLCapabilities(version.name());
        }
        // apply file configuration
        if (fileConfigHints.contains(FileConfigHint.OPENGL)) {
            if (opengl != null) {
                populateGLCapabilities(capabilities, opengl);
            } else {
                logger.log(Level.WARNING, "FileConfigHint.OPENGL ignored because OpenGL properties file not specified.");
            }
        }
        sceneBuilder.setGLCapabilities(capabilities);
        // configure logging
        if (autoConfigHints.contains(AutoConfigHint.LOGGING)) {
            updateLoggingConfiguration(newLoggingProperties());
        }
        if (fileConfigHints.contains(FileConfigHint.LOGGING)) {
            if (logging != null) {
                updateLoggingConfiguration(logging);
            } else {
                logger.log(Level.WARNING, "FileConfigHint.LOGGING ignored because logging properties file not specified.");
            }
        }
        return sceneBuilder;
    }

    /**
     * Automatically configure the hints based on the supplied GL reference.
     *
     * @param builder the builder to configure
     * @param gl      reference to GL used to configure hints
     *
     * @throws NullPointerException if any arguments are null
     */
    public static void configureHints(final SceneBuilder builder, final GL gl) {
        // cull extra camera hints
        final Set<CameraHint> cameraHints = builder.copyHints(CameraHint.class);
        if (cameraHints.isEmpty()) {
            // specify a default camera
            builder.setHint(CameraHint.SIMPLE);
        } else if (cameraHints.contains(CameraHint.NONE)) {
            // remove all other camera hints if none is specified
            builder.setHint(CameraHint.NONE);
        } else if (cameraHints.size() > 1) {
            // keep the specified camera hints
        }
        // configure the picking hints
        final Set<PickingHint> pickingHints = builder.copyHints(PickingHint.class);
        if (pickingHints.isEmpty()) {
            // no picking cameras is the default
            builder.setHint(PickingHint.NONE);
        } else if (pickingHints.contains(PickingHint.NONE)) {
            // remove all other picking hints if none is specified
            builder.setHint(PickingHint.NONE);
        } else {
            // find the best supported picking camera(s)
            for (final PickingHint pickingHint : PickingHint.values()) {
                if (pickingHints.contains(pickingHint)) {
                    switch (pickingHint) {
                        case COLOR_PICKING:
                            if (!isRemoteDesktopSession()) {
                                // color bits required
                                final int[] colorBits = new int[3];
                                gl.glGetIntegerv(GL.GL_RED_BITS, colorBits, 0);
                                gl.glGetIntegerv(GL.GL_GREEN_BITS, colorBits, 1);
                                gl.glGetIntegerv(GL.GL_BLUE_BITS, colorBits, 2);
                                final boolean colorBitsSupported = !((colorBits[0] < 8) || (colorBits[1] < 8) || (colorBits[2] < 8));
                                // check if FBO is supported
                                final boolean frameBufferSupported = gl.hasFullFBOSupport();
                                // auxilliary buffer utilized for color picking
                                final int[] auxBuffer = new int[]{0};
                                if (builder.isAuxiliaryBuffers()) {
                                    gl.glGetIntegerv(GL2.GL_AUX_BUFFERS, auxBuffer, 0);
                                }
                                final boolean auxBufferSupported = auxBuffer[0] >= 4;
                                // determine if double buffering has been enabled
                                final byte[] doubleBuffer = new byte[]{0};
                                gl.glGetBooleanv(GL2.GL_DOUBLEBUFFER, doubleBuffer, 0);
                                final boolean doubleBufferSupported = doubleBuffer[0] == 1;
                                // check if color picking can be utilized
                                final boolean colorPickingSupported;
                                if (colorBitsSupported
                                    && frameBufferSupported
                                    && (builder.isDebugColorPicking()
                                        || auxBufferSupported
                                        || doubleBufferSupported)) {
                                    builder.addHint(PickingHint.COLOR_PICKING);
                                    colorPickingSupported = true;
                                } else {
                                    colorPickingSupported = false;
                                }
                                logger.log(Level.FINE, "Color picking {0}: color bits {1}, frame buffer {2}, auxiliary buffers {3}, double buffer {4}",
                                           new Object[]{supportedMessage(colorPickingSupported),
                                                        supportedMessage(colorBitsSupported),
                                                        supportedMessage(frameBufferSupported),
                                                        supportedMessage(auxBufferSupported),
                                                        supportedMessage(doubleBufferSupported)});
                            } else {
                                builder.removeHint(PickingHint.COLOR_PICKING);
                                logger.log(Level.FINE, "Color picking is disabled when running over remote desktop session.");
                            }
                            break;
                        case ITEM_PICKING:
                            builder.addHint(PickingHint.ITEM_PICKING);
                            break;
                        case PICKING:
                            builder.addHint(PickingHint.PICKING);
                            break;
                        default:
                            throw new IllegalArgumentException("Unhandled enumeration sent to switch statement: " + pickingHint);
                    }
                }
            }
        }
        // determine rendering pipeline to use
        final Set<OpenGLHint> openGLHints = builder.copyHints(OpenGLHint.class);
        if (openGLHints.isEmpty()) {
            // set the default to immediate for backwards compatibility
            builder.setHint(OpenGLHint.IMMEDIATE_DRAW);
        } else {
            // find the usable draw style
            for (final OpenGLHint openGLHint : OpenGLHint.values()) {
                if (openGLHints.contains(openGLHint)) {
                    switch (openGLHint) {
                        case VBO_DRAW:
                            // enable if the required functions are available
                            if (gl.isFunctionAvailable("glBindBufferARB")
                                && gl.isFunctionAvailable("glBufferDataARB")
                                && gl.isFunctionAvailable("glDeleteLists")
                                && gl.isFunctionAvailable("glBufferSubData")
                                && gl.isFunctionAvailable("glVertexPointer")
                                && gl.isFunctionAvailable("glTexCoordPointer")
                                && gl.isFunctionAvailable("glNormalPointer")
                                && gl.isFunctionAvailable("glDrawArrays")) {
                                builder.addHint(OpenGLHint.VBO_DRAW);
                                break;
                            }
                            builder.removeHint(OpenGLHint.VBO_DRAW);
                            break;
                        case DISPLAY_LISTS:
                            // enable if the required functions are available
                            if (gl.isFunctionAvailable("glIsList")
                                && gl.isFunctionAvailable("glCallList")
                                && gl.isFunctionAvailable("glDeleteLists")
                                && gl.isFunctionAvailable("glNewList")
                                && gl.isFunctionAvailable("glEndList")) {
                                builder.addHint(OpenGLHint.DISPLAY_LISTS);
                                break;
                            }
                            builder.removeHint(OpenGLHint.DISPLAY_LISTS);
                            break;
                        case ARRAY_DRAW:
                            // enable if the required functions are available
                            if (gl.isFunctionAvailable("glDrawArrays")
                                && gl.isFunctionAvailable("glDrawElements")) {
                                builder.addHint(OpenGLHint.ARRAY_DRAW);
                                break;
                            }
                            builder.removeHint(OpenGLHint.ARRAY_DRAW);
                            break;
                        case IMMEDIATE_DRAW:
                            builder.addHint(OpenGLHint.IMMEDIATE_DRAW);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Loads the properties from a file.
     *
     * @param file       the file to load from
     * @param properties the properties to populate or null to crete a new one
     *
     * @return the loaded properties
     *
     * @throws IOException
     */
    public static Properties loadProperties(final File file, final Properties properties) throws IOException {
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            return ConfigUtil.loadProperties(input, properties);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * Loads the properties from a file.
     *
     * @param input      the input stream to load from
     * @param properties the properties to populate or null to crete a new one
     *
     * @return the loaded properties
     *
     * @throws IOException
     */
    public static Properties loadProperties(final InputStream input, Properties properties) throws IOException {
        if (properties == null) {
            properties = new Properties();
        }
        properties.load(input);
        return properties;
    }

    /**
     * Create a new debug GL based on the profile.
     *
     * @param gl reference to GL or null
     *
     * @return the debug wrapped GL or null
     */
    public static GL newDebugGL(final GL gl) {
        if (gl == null) {
            return null;
        }
        if (gl.isGL4bc()) {
            // GL4bc
            return new DebugGL4bc(gl.getGL4bc());
        } else if (gl.isGL4()) {
            // GL4
            return new DebugGL4(gl.getGL4());
        } else if (gl.isGL3bc()) {
            // GL3bc
            return new DebugGL3bc(gl.getGL3bc());
        } else if (gl.isGL3()) {
            // GL3
            return new DebugGL3(gl.getGL3());
        } else if (gl.isGL2()) {
            // GL2
            return new DebugGL2(gl.getGL2());
        } else if (gl.isGLES2()) {
            // GLES2
            return new DebugGLES2(gl.getGLES2());
        } else if (gl.isGLES1()) {
            // GLES1
            return new DebugGLES1(gl.getGLES1());
        } else {
            return gl;
        }
    }

    /**
     * Attempts to determine if this session is being run over RDP.
     *
     * @return true if running over RDP
     */
    public static boolean isRemoteDesktopSession() {
        final String session = System.getenv("sessionname");
        return session != null ? session.contains("RDP") : false;
    }

    /**
     * Update the logging configuration from a key value properties object.
     *
     * @param properties the properties
     */
    public static void updateLoggingConfiguration(final Properties properties) {
        if (properties == null) {
            throw new NullPointerException("properties");
        }
        // set the system properties
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            final String key = String.valueOf(entry.getKey());
            final String value = String.valueOf(entry.getValue());
            System.setProperty(key, value);
        }
        // read the configuration
        OutputStream out = null;
        InputStream ina = null;
        InputStream inb = null;
        SequenceInputStream sis = null;
        BufferedInputStream bin = null;
        try {
            // find the default logging configuration file
            String fname = System.getProperty("java.util.logging.config.file");
            if (fname == null) {
                fname = System.getProperty("java.home");
                if (fname == null) {
                    throw new Error("Can't find java.home ??");
                }
                File f = new File(fname, "lib");
                f = new File(f, "logging.properties");
                fname = f.getCanonicalPath();
            }
            // create a temp file for the properties
            final File temp = File.createTempFile(UUID.randomUUID().toString(), ".properties");
            temp.deleteOnExit();
            out = new FileOutputStream(temp);
            properties.store(out, null);
            // combine the two streams
            ina = new FileInputStream(fname);
            inb = new FileInputStream(temp);
            sis = new SequenceInputStream(ina, inb);
            bin = new BufferedInputStream(sis);
            // read the configuration
            LogManager.getLogManager().readConfiguration(bin);
        } catch (final IOException | SecurityException ex) {
            logger.log(Level.WARNING, "Unable to read the default configuration for logging.", ex);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(ina);
            IOUtils.closeQuietly(inb);
            IOUtils.closeQuietly(sis);
            IOUtils.closeQuietly(bin);
        }
    }

    /**
     * Creates an auto configured SVF SceneBuilder object.
     *
     * @return a new default SceneBuilder
     */
    private static SceneBuilder newSceneBuilder() {
        final SceneBuilderImpl builder = new SceneBuilderImpl();
        return builder;
    }

    /**
     * Creates a copy of a SVF SceneBuilder object.
     *
     * @param builder the builder to copy
     *
     * @return a new copied SceneBuilder
     */
    protected static SceneBuilder newSceneBuilder(final SceneBuilder builder) {
        final SceneBuilderImpl copy = new SceneBuilderImpl(builder);
        return copy;
    }

    /**
     * Creates a new properties configured SVF SceneBuilder object.
     *
     * @param properties the builder properties to set
     *
     * @return a new default SceneBuilder
     */
    protected static SceneBuilder newSceneBuilder(final Properties properties) {
        final SceneBuilder builder = ConfigUtil.newSceneBuilder();
        if (properties != null) {
            populateSceneBuilder(builder, properties);
        }
        return builder;
    }

    /**
     * Populate the supplied SceneBuilder using the supplied properties.
     *
     * @param builder    the builder to populate
     * @param properties the properties to populate with
     *
     * @throws NullPointerException if builder or properties are null
     */
    protected static void populateSceneBuilder(final SceneBuilder builder, final Properties properties) {
        if (builder == null) {
            throw new NullPointerException("builder");
        }
        if (properties == null) {
            throw new NullPointerException("properties");
        }
        builder.setAuxiliaryBuffers(Boolean.parseBoolean(properties.getProperty("auxiliaryBuffers", String.valueOf(builder.isAuxiliaryBuffers()))));
        builder.setTextureColorPicking(Boolean.parseBoolean(properties.getProperty("textureColorPicking", String.valueOf(builder.isTextureColorPicking()))));
        builder.setBlending(Boolean.parseBoolean(properties.getProperty("blending", String.valueOf(builder.isBlending()))));
        builder.setDebugColorPicking(Boolean.parseBoolean(properties.getProperty("debugColorPicking", String.valueOf(builder.isDebugColorPicking()))));
        builder.setDebug(Boolean.parseBoolean(properties.getProperty("debug", String.valueOf(builder.isDebug()))));
        builder.setDisplayFps(Boolean.parseBoolean(properties.getProperty("displayFps", String.valueOf(builder.isDisplayFps()))));
        builder.setDrawableTypes(properties.getProperty("drawableTypes", builder.getDrawableTypes()));
        builder.setFullScreenAntiAliasing(Boolean.parseBoolean(properties.getProperty("fullScreenAntiAliasing", String.valueOf(builder.isFullScreenAntiAliasing()))));
        builder.setLighting(Boolean.parseBoolean(properties.getProperty("lighting", String.valueOf(builder.isLighting()))));
        builder.setMaxInitializations(Integer.parseInt(properties.getProperty("maxInitializations", String.valueOf(builder.getMaxInitializations()))));
        builder.setNumberOfSceneDrawingPasses(Integer.parseInt(properties.getProperty("numberOfSceneDrawingPasses", String.valueOf(builder.getNumberOfSceneDrawingPasses()))));
        builder.setNumberOfUserInterfaceDrawingPasses(Integer.parseInt(properties.getProperty("numberOfUserInterfaceDrawingPasses", String.valueOf(builder.getNumberOfUserInterfaceDrawingPasses()))));
        builder.setTargetFps(Integer.parseInt(properties.getProperty("targetFps", String.valueOf(builder.getTargetFps()))));
        builder.setVerbose(Boolean.parseBoolean(properties.getProperty("verbose", String.valueOf(builder.isVerbose()))));
        builder.setHints(properties.getProperty("hints", builder.getHints()));
        // BeanUtils doesn't work on beans which return an object in the setter
        //            try {
        //                BeanUtils.populate(builder, properties);
        //            } catch (final IllegalAccessException ex) {
        //                logger.log(Level.WARNING, "Unable to populate SVF scene builder.", ex);
        //            } catch (final InvocationTargetException ex) {
        //                logger.log(Level.WARNING, "Unable to populate SVF scene builder.", ex);
        //            }
    }

    /**
     * Creates a new auto configured GLCapabilities object.
     *
     * @return a new GLCapabilities
     */
    protected static GLCapabilities newGLCapabilities() {
        final GLCapabilities capabilities = ConfigUtil.newGLCapabilities((String) null);
        return capabilities;
    }

    /**
     * Creates a new auto configured GLCapabilities object.
     *
     * @param version the profile version or null for default
     *
     * @return a new GLCapabilities
     */
    protected static GLCapabilities newGLCapabilities(final String version) {
        final GLProfile profile;
        if (version == null) {
            profile = GLProfile.getMaximum(true);
        } else {
            profile = GLProfile.get(version);
        }
        final GLCapabilities capabilities = new GLCapabilities(profile);
//        capabilities.setHardwareAccelerated(true);
//        capabilities.setRedBits(8);
//        capabilities.setGreenBits(8);
//        capabilities.setBlueBits(8);
//        capabilities.setAlphaBits(8);
//        capabilities.setAccumRedBits(8);
//        capabilities.setAccumGreenBits(8);
//        capabilities.setAccumBlueBits(8);
//        capabilities.setAccumAlphaBits(8);
//        capabilities.setDepthBits(24);
//        capabilities.setStencilBits(8);
//        capabilities.setNumSamples(4);
//        capabilities.setSampleBuffers(true);
//        capabilities.setDoubleBuffered(true);
        return capabilities;
    }

    /**
     * Creates a properties configured SVF GLCapabilities object.
     *
     * @param properties the capability properties to set
     *
     * @return a new default GLCapabilities
     */
    protected static GLCapabilities newGLCapabilities(final Properties properties) {
        final GLCapabilities capabilities = ConfigUtil.newGLCapabilities(null, properties);
        return capabilities;
    }

    /**
     * Creates a properties configured SVF GLCapabilities object.
     *
     * @param version    the profile version or null for default
     * @param properties the capability properties to set
     *
     * @return a new default GLCapabilities
     */
    protected static GLCapabilities newGLCapabilities(final String version, final Properties properties) {
        final GLCapabilities capabilities = ConfigUtil.newGLCapabilities(version);
        if (properties != null) {
            populateGLCapabilities(capabilities, properties);
        }
        return capabilities;
    }

    /**
     * Populate the supplied GLCapabilities using the supplied properties.
     *
     * @param capabilities the capabilities to populate
     * @param properties   the properties to populate with
     *
     * @throws NullPointerException if capabilities or properties are null
     */
    protected static void populateGLCapabilities(final GLCapabilities capabilities, final Properties properties) {
        if (capabilities == null) {
            throw new NullPointerException("capabilities");
        }
        if (properties == null) {
            throw new NullPointerException("properties");
        }
        // properties with side effects
        capabilities.setSampleBuffers(Boolean.parseBoolean(properties.getProperty("sampleBuffers", String.valueOf(capabilities.getSampleBuffers()))));
        capabilities.setBackgroundOpaque(Boolean.parseBoolean(properties.getProperty("backgroundOpaque", String.valueOf(capabilities.isBackgroundOpaque()))));
        capabilities.setBitmap(Boolean.parseBoolean(properties.getProperty("bitmap", String.valueOf(capabilities.isBitmap()))));
        capabilities.setFBO(Boolean.parseBoolean(properties.getProperty("FBO", String.valueOf(capabilities.isFBO()))));
        capabilities.setPBuffer(Boolean.parseBoolean(properties.getProperty("PBuffer", String.valueOf(capabilities.isPBuffer()))));
        capabilities.setOnscreen(Boolean.parseBoolean(properties.getProperty("onscreen", String.valueOf(capabilities.isOnscreen()))));
        // other properties
        capabilities.setAccumAlphaBits(Integer.parseInt(properties.getProperty("accumAlphaBits", String.valueOf(capabilities.getAccumAlphaBits()))));
        capabilities.setAccumBlueBits(Integer.parseInt(properties.getProperty("accumBlueBits", String.valueOf(capabilities.getAccumBlueBits()))));
        capabilities.setAccumGreenBits(Integer.parseInt(properties.getProperty("accumGreenBits", String.valueOf(capabilities.getAccumGreenBits()))));
        capabilities.setAccumRedBits(Integer.parseInt(properties.getProperty("accumRedBits", String.valueOf(capabilities.getAccumRedBits()))));
        capabilities.setAlphaBits(Integer.parseInt(properties.getProperty("alphaBits", String.valueOf(capabilities.getAlphaBits()))));
        capabilities.setBlueBits(Integer.parseInt(properties.getProperty("blueBits", String.valueOf(capabilities.getBlueBits()))));
        capabilities.setGreenBits(Integer.parseInt(properties.getProperty("greenBits", String.valueOf(capabilities.getGreenBits()))));
        capabilities.setRedBits(Integer.parseInt(properties.getProperty("redBits", String.valueOf(capabilities.getRedBits()))));
        capabilities.setDepthBits(Integer.parseInt(properties.getProperty("depthBits", String.valueOf(capabilities.getDepthBits()))));
        capabilities.setTransparentAlphaValue(Integer.parseInt(properties.getProperty("transparentAlphaValue", String.valueOf(capabilities.getTransparentAlphaValue()))));
        capabilities.setTransparentBlueValue(Integer.parseInt(properties.getProperty("transparentBlueValue", String.valueOf(capabilities.getTransparentBlueValue()))));
        capabilities.setTransparentGreenValue(Integer.parseInt(properties.getProperty("transparentGreenValue", String.valueOf(capabilities.getTransparentGreenValue()))));
        capabilities.setTransparentRedValue(Integer.parseInt(properties.getProperty("transparentRedValue", String.valueOf(capabilities.getTransparentRedValue()))));
        capabilities.setNumSamples(Integer.parseInt(properties.getProperty("numSamples", String.valueOf(capabilities.getNumSamples()))));
        capabilities.setSampleExtension(properties.getProperty("sampleExtension", capabilities.getSampleExtension()));
        capabilities.setStencilBits(Integer.parseInt(properties.getProperty("stencilBits", String.valueOf(capabilities.getStencilBits()))));
        capabilities.setDoubleBuffered(Boolean.parseBoolean(properties.getProperty("doubleBuffered", String.valueOf(capabilities.getDoubleBuffered()))));
        capabilities.setHardwareAccelerated(Boolean.parseBoolean(properties.getProperty("hardwareAccelerated", String.valueOf(capabilities.getHardwareAccelerated()))));
        capabilities.setStereo(Boolean.parseBoolean(properties.getProperty("stereo", String.valueOf(capabilities.getStereo()))));
        // BeanUtils causes problems when SVF is added to a container such as OSGI
//            try {
//                BeanUtils.populate(capabilities, properties);
//            } catch (final IllegalAccessException ex) {
//                logger.log(Level.WARNING, "Unable to populate GL capabilities.", ex);
//            } catch (final InvocationTargetException ex) {
//                logger.log(Level.WARNING, "Unable to populate GL capabilities.", ex);
//            }
    }

    /**
     * Generate a new GL properties instance with the defaults populated.
     *
     * @return a default properties
     */
    protected static Properties newGLCapabilitiesProperties() {
        final Properties properties = new Properties();
        properties.putAll(OPENGL_CONFIG_DEFAULTS);
        return properties;
    }

    /**
     * Generate a new SceneBuilder properties instance with the defaults
     * populated.
     *
     * @return a default properties
     */
    protected static Properties newSceneBuilderProperties() {
        final Properties properties = new Properties();
        properties.putAll(SVF_CONFIG_DEFAULTS);
        return properties;
    }

    /**
     * Generate a new logging properties instance with the defaults populated.
     *
     * @return a default properties
     */
    protected static Properties newLoggingProperties() {
        final Properties properties = new Properties();
        properties.putAll(LOGGING_CONFIG_DEFAULTS);
        return properties;
    }

    /**
     * Returns either "is supported" or "not supported".
     *
     * @param supported whether it is supported or not
     *
     * @return the message
     */
    protected static String supportedMessage(final boolean supported) {
        return supported ? "is supported" : "not supported";
    }

    /**
     * Default scene builder for use with AbstractScene.
     *
     * @author Arthur Bleeker
     */
    protected static class SceneBuilderImpl extends AbstractSceneBuilder<SceneBuilderImpl> {

        protected SceneBuilderImpl() {
        }

        protected SceneBuilderImpl(final SceneBuilder sceneBuilder) {
            super(sceneBuilder);
        }
    }
}
