package gov.pnnl.svf.scene;

import com.jogamp.opengl.DebugGL2;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.util.PropertyChangeSupportWrapper;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.picking.ColorPickingUtils;
import gov.pnnl.svf.update.TaskManager;
import gov.pnnl.svf.update.UpdateTaskManager;
import gov.pnnl.svf.util.FxEventFilter;
import gov.pnnl.svf.util.FxEventHandler;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.apache.commons.math.geometry.Vector3D;

/**
 * <p>
 * Abstract class for OpenGL scenes. </P>
 * <p>
 * Subclass this to create your own scene. Override default scene values by
 * overriding the initialize method. Initialize all cameras and actors in the
 * load method. </p>
 * <p>
 * Lists of actors and references to cameras should not be kept in subclasses.
 * These objects should be located using the lookup (inversion of control). This
 * allows flexibility in that other classes can add and remove actors and
 * cameras during runtime. It also allows for a single place of reference for
 * access to these objects.</P>
 *
 * FIXME This class is not being maintained and currently may not work.
 *
 * @author Arthur Bleeker
 */
public abstract class FxAbstractScene extends Canvas implements SceneExt {

    private static final Logger logger = Logger.getLogger(FxAbstractScene.class.toString());
    /**
     * The default type for this scene.
     */
    public static final String DEFAULT_TYPE = "scene";
    protected final PropertyChangeSupport pcs = new PropertyChangeSupportWrapper(this);
    protected final Collection<Camera> cameras = new HashSet<>();
    protected final AtomicBoolean drawing = new AtomicBoolean(false);
    protected final TaskScheduler scheduler = new TaskScheduler();
    protected final SceneLookupImpl lookup;
    protected final ColorPickingUtils colorPickingUtils;
    protected final TaskManager taskManager;
    protected final GLDrawableFactory factory;
    protected final String type;
    protected final GLUT glut;
    protected Tooltip tooltip;
    protected ScreenshotImpl screenshot;
    protected SceneBuilder builder;
    protected SceneRenderer sceneRenderer;
    protected boolean multiscreen;
    protected Timer drawTimer;
    protected GLContext glContext;
    protected GLUgl2 glu;
    protected GLCapabilities capabilities;
    protected GLDrawable drawable;
    protected Rectangle viewport;
    // image fields, this code was ported from AbstractReadbackBackend in the JOGL library
    // This image is exactly the correct size to render into the panel
    /**
     *
     */
    protected WritableImage offscreenImage;
    protected DrawToCanvasRunnable renderer;
    /**
     *
     */
    protected final Object offscreenImageSync = new Object();
    // store the read back pixels before storing
    // in the BufferedImage
    /**
     *
     */
    protected IntBuffer readBackInts;
    // For saving/restoring of OpenGL state during ReadPixels
    protected int[] swapbytes = new int[1];
    protected int[] rowlength = new int[1];
    protected int[] skiprows = new int[1];
    protected int[] skippixels = new int[1];
    protected int[] alignment = new int[1];
    // boolean to indicate that this scene has been constructed
    protected boolean constructed = false;
    private boolean loaded = false;
    private boolean disposed = false;

    /**
     * Constructor
     *
     * The construct method must be called after this constructor and before any
     * other method is called.
     */
    public FxAbstractScene() {
        super();
        this.type = DEFAULT_TYPE;
        factory = GLDrawableFactory.getDesktopFactory();
        glut = new GLUT();
        viewport = new Rectangle(0, 0, (int) getWidth(), (int) getHeight());
        lookup = new SceneLookupImpl(this);
        taskManager = new UpdateTaskManager(this);
        colorPickingUtils = new ColorPickingUtils(this);
    }

    /**
     * Constructor
     *
     * @param builder The builder object used to instantiate the GLScene.
     */
    public void construct(final SceneBuilder builder) {
        construct(builder, false);
    }

    /**
     * Constructor
     *
     * @param builder     The builder object used to instantiate the GLScene.
     * @param multiscreen true if this scene should draw across multiple screens
     */
    public void construct(final SceneBuilder builder, final boolean multiscreen) {
        synchronized (this) {
            if (constructed) {
                throw new IllegalStateException("Scene can only be constructed once.");
            }
            this.builder = new ImmutableSceneBuilder(builder);
        }
        this.multiscreen = multiscreen;
        capabilities = builder.getGLCapabilities();
        capabilities.setPBuffer(true);
        capabilities.setFBO(false);
        capabilities.setOnscreen(false);
        capabilities.setDoubleBuffered(false);
        logger.log(Level.FINE, "Creating a new FxAbstractScene.");
        // catch both the cases where null is passed as an argument for drawableTypes
        sceneRenderer = new SceneRenderer(this, builder);
        sceneRenderer.setBackground(builder.getBackground());
        sceneRenderer.setMaxInitializations(builder.getMaxInitializations());
        renderer = new DrawToCanvasRunnable(this);
        tooltip = new TooltipImpl(this);
        synchronized (this) {
            constructed = true;
        }
        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> observable, final Number old, final Number value) {
                FxAbstractScene.this.reset();
            }
        });
        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> observable, final Number old, final Number value) {
                FxAbstractScene.this.reset();
            }
        });
        visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observable, final Boolean old, final Boolean value) {
                FxAbstractScene.this.reset();
            }
        });
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public GLAutoDrawable getComponent() {
        return null;
    }

    @Override
    public SceneFactory getFactory() {
        return new FxSceneFactory();
    }

    @Override
    public void clear() {
        lookup.clear();
    }

    @Override
    public <T> void add(final T object) {
        lookup.add(object);
        if (object instanceof Camera) {
            final Camera camera = (Camera) object;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    final double width = getWidth();
                    final double height = getHeight();
                    final Rectangle viewport = new Rectangle(0, 0, (int) width, (int) height);
                    camera.setViewport(viewport);
                }
            });
        }
    }

    @Override
    public <T> void addAll(final Collection<T> objects) {
        lookup.addAll(objects);
        for (final T object : objects) {
            if (object instanceof Camera) {
                final Camera camera = (Camera) object;

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        final double width = getWidth();
                        final double height = getHeight();
                        final Rectangle viewport = new Rectangle(0, 0, (int) width, (int) height);
                        camera.setViewport(viewport);
                    }
                });
            }
        }
    }

    @Override
    public void addListeners(final Object object) {
        if (object instanceof FxEventHandler) {
            Platform.runLater(new Runnable() {
                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public void run() {
                    final FxEventHandler handler = (FxEventHandler) object;
                    for (final Object object : handler.getEventTypes()) {
                        final EventType<?> eventType = (EventType) object;
                        addEventHandler(eventType, handler);
                    }
                }
            });
        }
        if (object instanceof FxEventFilter) {
            Platform.runLater(new Runnable() {
                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public void run() {
                    final FxEventFilter handler = (FxEventFilter) object;
                    for (final Object object : handler.getEventTypes()) {
                        final EventType<?> eventType = (EventType) object;
                        addEventFilter(eventType, handler);
                    }
                }
            });
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void dispose() {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            disposed = true;
        }
        shutdown();
        final Set<EventHandler> objects = lookupAll(EventHandler.class);
        for (final Object object : objects) {
            removeListeners(object);
        }
        // disposables
        final Set<Disposable> disposables = lookupAll(Disposable.class);
        for (final Disposable disposable : disposables) {
            disposable.dispose();
        }
        // clear the lookup
        clear();
        final PropertyChangeListener[] listeners = pcs.getPropertyChangeListeners();
        for (final PropertyChangeListener listener : listeners) {
            pcs.removePropertyChangeListener(listener);
        }
        cameras.clear();
        sceneRenderer.dispose();
        colorPickingUtils.dispose();
        tooltip.dispose();
        taskManager.dispose();
    }

    @Override
    public void draw() {
        sceneRenderer.setRepaint(DrawingPass.ALL);
    }

    @Override
    public void draw(final DrawingPass drawingPass) {
        sceneRenderer.setRepaint(drawingPass);
    }

    @Override
    public DrawingPass getDirtied() {
        return sceneRenderer.getRepaint();
    }

    /**
     * Draw the GL scene.
     */
    protected void drawGLScene() {
        final GLContext glContext;
        final GLDrawable drawable;
        final GLUgl2 glu;
        synchronized (this) {
            glContext = this.glContext;
            drawable = this.drawable;
            glu = this.glu;
        }
        if ((glContext != null && drawable.isRealized()) && isVisible()) {
            // set the scene and context as current
            final int state = glContext.makeCurrent();
            final GL2 gl;
            synchronized (this) {
                gl = builder.isDebug() ? new DebugGL2(glContext.getGL().getGL2()) : glContext.getGL().getGL2();
            }
            switch (state) {
                case GLContext.CONTEXT_CURRENT_NEW:
                    initialize(gl);
                    break;
                case GLContext.CONTEXT_CURRENT:
                    if (sceneRenderer.performRender(gl, glu)) {
                        // copy it to the buffered image
                        copyToImage(getGL().getGL2());
                        renderer.enqueue();
                        Platform.runLater(renderer);
                    }
                    break;
                case GLContext.CONTEXT_NOT_CURRENT:
                default:
                    // ignore this message
                    break;
            }
            // screenshot
            screenshot.dispatch(gl);
            // release the context
            glContext.release();
        }
    }

    @Override
    public Actor getActor(final String id) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        return lookup.getActor(id);
    }

    @Override
    public <T extends Actor> T getActor(final Class<T> clazz, final String type, final String id, final Object data) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        if (id == null) {
            throw new NullPointerException("id");
        }
        return lookup.getActor(clazz, type, id, data);
    }

    @Override
    public Vector3D getBoundary() {
        return sceneRenderer.getBoundary();
    }

    @Override
    public Vector3D getCenter() {
        return sceneRenderer.getCenter();
    }

    @Override
    public Rectangle getViewport() {
        synchronized (this) {
            return viewport;
        }
    }

    @Override
    public Set<Class<? extends Drawable>> getDrawableTypes() {
        return sceneRenderer.getDrawableTypes();
    }

    @Override
    public GL getGL() {
        synchronized (this) {
            if (glContext != null && drawable.isRealized()) {
                return glContext.getGL();
            } else {
                return null;
            }
        }
    }

    @Override
    public GLU getGLU() {
        synchronized (this) {
            return glu;
        }
    }

    @Override
    public GLUT getGLUT() {
        return glut;
    }

    @Override
    public GLProfile getGLProfile() {
        synchronized (this) {
            if (glContext != null && drawable.isRealized()) {
                return glContext.getGL().getGLProfile();
            } else {
                return null;
            }
        }
    }

    @Override
    public SceneExt getExtended() {
        return this;
    }

    @Override
    public Tooltip getTooltip() {
        return tooltip;
    }

    @Override
    public Screenshot getScreenshot() {
        return screenshot;
    }

    @Override
    public ColorPickingUtils getColorPickingUtils() {
        return colorPickingUtils;
    }

    @Override
    public DrawingPass getCurrentDrawingPass() {
        return sceneRenderer.getCurrentDrawingPass();
    }

    @Override
    public TaskManager getDefaultTaskManager() {
        return taskManager;
    }

    @Override
    public int getMaxInitializations() {
        return sceneRenderer.getMaxInitializations();
    }

    @Override
    public Color getSceneBackground() {
        return sceneRenderer.getBackground();
    }

    @Override
    public SceneMetrics getSceneMetrics() {
        return sceneRenderer.getSceneMetrics();
    }

    @Override
    public Collection<Actor> getVisibleRootActors() {
        return lookup.getVisibleRootActors();
    }

    @Override
    public void getVisibleRootActors(final Collection<Actor> out) {
        lookup.getVisibleRootActors(out);
    }

    /**
     * Perform all open OpenGL initialization logic here. This function already
     * performs basic initialization in the base class. This should be called
     * once after this class has been instantiated.
     */
    public void initialize() {
        // create a new GLContext
        try {
            synchronized (this) {
                int width = 1;
                int height = 1;
                // multiscreen needs to take into account all monitors and their configuration
                if (multiscreen) {
                    final Area area = new Area();
                    for (final GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                        final GraphicsConfiguration config = graphicsDevice.getDefaultConfiguration();
                        area.add(new Area(config.getBounds()));
                    }
                    final Rectangle2D bounds = area.getBounds2D();
                    width = Math.max(width, (int) bounds.getWidth());
                    height = Math.max(height, (int) bounds.getHeight());
                } else {
                    // single display
                    for (final GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                        // find the largest display so it works well moving from one monitor to the other
                        final DisplayMode displayMode = graphicsDevice.getDisplayMode();
                        width = Math.max(width, displayMode.getWidth());
                        height = Math.max(height, displayMode.getHeight());
                    }
                }
                try {
                    drawable = factory.createOffscreenDrawable(null, capabilities, null, width, height);
                } catch (final GLException ex) {
                    logger.log(Level.WARNING, "Failed to create drawing with existing capabilities, reverting to default profile.", ex);
                    capabilities = new GLCapabilities(GLProfile.getDefault());
                    // this type of scene needs to have a pbuffer
                    capabilities.setPBuffer(true);
                    capabilities.setFBO(false);
                    capabilities.setOnscreen(false);
                    capabilities.setDoubleBuffered(false);
                    drawable = factory.createOffscreenDrawable(null, capabilities, null, width, height);
                }
                // final GLProfile glprofile = GLProfile.get(GLProfile.GL2);
                drawable.setRealized(true);
                glContext = drawable.createContext(null);//GLDrawableFactory.getFactory(glprofile).createExternalGLContext();
                glu = new GLUgl2();
                // reset all of the OpenGL scene options
                glContext.makeCurrent();
                // set up the gl fields
                final GL2 gl = builder.isDebug() ? new DebugGL2(glContext.getGL().getGL2()) : glContext.getGL().getGL2();
                glu = new GLUgl2();
                // reset all of the OpenGL flags
                sceneRenderer.initialize(gl, glu);
                initialize(gl);
                // release the current gl context
                glContext.release();
                sceneRenderer.setRepaint(DrawingPass.ALL);
            }
        } catch (final RuntimeException ex) {
            logger.log(Level.WARNING, "Unable to create a GLContext for OpenGL version 2.0, please update your video card drivers.");
            throw ex;
        }
        reset();
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return disposed;
        }
    }

    @Override
    public <T> T lookup(final Class<? extends T> object) {
        return lookup.lookup(object);
    }

    @Override
    public <T> Set<T> lookupAll(final Class<T> object) {
        return lookup.lookupAll(object);
    }

    @Override
    public <T> void lookupAll(final Class<T> object, final Collection<T> out) {
        lookup.lookupAll(object, out);
    }

    @Override
    public <T> boolean remove(final T object) {
        return lookup.remove(object);
    }

    @Override
    public <T> boolean removeAll(final Collection<T> objects) {
        return lookup.removeAll(objects);
    }

    @Override
    public Set<Object> lookupAll() {
        return lookup.lookupAll();
    }

    @Override
    public void lookupAll(final Collection<Object> out) {
        lookup.lookupAll(out);
    }

    @Override
    public void removeListeners(final Object object) {
        if (object instanceof FxEventHandler) {
            Platform.runLater(new Runnable() {
                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public void run() {
                    final FxEventHandler handler = (FxEventHandler) object;
                    for (final Object object : handler.getEventTypes()) {
                        final EventType<?> eventType = (EventType) object;
                        removeEventHandler(eventType, handler);
                    }
                }
            });
        }
        if (object instanceof FxEventFilter) {
            Platform.runLater(new Runnable() {
                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public void run() {
                    final FxEventFilter filter = (FxEventFilter) object;
                    for (final Object object : filter.getEventTypes()) {
                        final EventType<?> eventType = (EventType) object;
                        removeEventFilter(eventType, filter);
                    }
                }
            });
        }
    }

    /**
     * Resets the GLScene.
     */
    public void reset() {
        synchronized (this) {
            final Rectangle bounds = new Rectangle(0, 0, (int) getWidth(), (int) getHeight());
            viewport = bounds;
            scheduler.schedule(new ResetTask(bounds));
        }
    }

    @Override
    public void setBoundary(final Vector3D boundary) {
        final Vector3D old = sceneRenderer.getBoundary();
        sceneRenderer.setBoundary(boundary);
        pcs.firePropertyChange(BOUNDARY, old, boundary);
    }

    @Override
    public void setCenter(final Vector3D center) {
        final Vector3D old = sceneRenderer.getCenter();
        sceneRenderer.setCenter(center);
        pcs.firePropertyChange(CENTER, old, center);
    }

    @Override
    public void setMaxInitializations(final int maxInitializations) {
        sceneRenderer.setMaxInitializations(maxInitializations);
    }

    @Override
    public void setSceneBackground(final Color background) {
        final Color old = sceneRenderer.getBackground();
        sceneRenderer.setBackground(background);
        pcs.firePropertyChange(SCENE_BACKGROUND, old, background);
    }

    @Override
    public SceneBuilder getSceneBuilder() {
        synchronized (this) {
            return builder;
        }
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return pcs;
    }

    /**
     * Shutdown the GLScene. The scene will no longer render once this method is
     * called.
     */
    private void shutdown() {
        logger.log(Level.FINE, "Shutting down the FxAbstractScene.");
        synchronized (this) {
            stop();
            scheduler.schedule(new ShutdownTask());
        }
    }

    @Override
    public void start() {
        stop();
        logger.log(Level.FINE, "Starting the FxAbstractScene.");
        synchronized (this) {
            drawTimer = new Timer("OpenGL_UpdateAndDrawTimer", true);
            drawTimer.scheduleAtFixedRate(new StartTask(), 1000L / builder.getTargetFps(), 1000L / builder.getTargetFps());
        }
        sceneRenderer.setRepaint(DrawingPass.ALL);
    }

    @Override
    public void stop() {
        logger.log(Level.FINE, "Stopping the FxAbstractScene.");
        synchronized (this) {
            if (drawTimer != null) {
                drawTimer.cancel();
                drawTimer.purge();
            }
        }
    }

    @Override
    public boolean isLoaded() {
        synchronized (this) {
            return loaded;
        }
    }

    @Override
    public boolean waitForLoad(final long timeout) {
        final CountDownLatch latch = new CountDownLatch(1);
        final PropertyChangeListener listener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                latch.countDown();
            }
        };
        getPropertyChangeSupport().addPropertyChangeListener(Scene.LOADED, listener);
        while (!isLoaded()) {
            try {
                latch.await(timeout, TimeUnit.MILLISECONDS);
            } catch (final InterruptedException ex) {
                // ignore
            }
        }
        getPropertyChangeSupport().removePropertyChangeListener(Scene.LOADED, listener);
        return isLoaded();
    }

    /**
     * Perform all initial scene loading logic here. This function will get
     * called once after the initial GL initialization.
     * <br/>
     * <h3>e.g.:</h3>
     * <p>
     * <code>
     * final SimpleCamera camera = new SimpleCamera(this);<br/>
     * add(camera);<br/>
     * <br/>
     * final ColoredPlaneActor plane = new ColoredPlaneActor(this);<br/>
     * add(plane);<br/>
     * </code> </p>
     */
    protected abstract void load();

    private void copyToImage(final GL2 gl) {
        // push the buffer to an image that can be queried later
        final int panelWidth = Math.max(1, (int) getWidth());
        final int panelHeight = Math.max(1, (int) getHeight());
        synchronized (offscreenImageSync) {
            if (offscreenImage != null && ((int) offscreenImage.getWidth() != panelWidth || (int) offscreenImage.getHeight() != panelHeight)) {
                offscreenImage = null;
            }
            // Must now copy pixels from offscreen context into surface
            if (offscreenImage == null && panelWidth > 0 && panelHeight > 0) {
                offscreenImage = new WritableImage(panelWidth, panelHeight);
                readBackInts = IntBuffer.allocate(panelWidth * panelHeight);
            }

            if (offscreenImage != null) {
                // Save current modes
                gl.glGetIntegerv(GL2GL3.GL_PACK_SWAP_BYTES, swapbytes, 0);
                gl.glGetIntegerv(GL2ES3.GL_PACK_ROW_LENGTH, rowlength, 0);
                gl.glGetIntegerv(GL2ES3.GL_PACK_SKIP_ROWS, skiprows, 0);
                gl.glGetIntegerv(GL2ES3.GL_PACK_SKIP_PIXELS, skippixels, 0);
                gl.glGetIntegerv(GL.GL_PACK_ALIGNMENT, alignment, 0);

                gl.glPixelStorei(GL2GL3.GL_PACK_SWAP_BYTES, GL.GL_FALSE);
                gl.glPixelStorei(GL2ES3.GL_PACK_ROW_LENGTH, panelWidth);
                gl.glPixelStorei(GL2ES3.GL_PACK_SKIP_ROWS, 0);
                gl.glPixelStorei(GL2ES3.GL_PACK_SKIP_PIXELS, 0);
                gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);

                // Actually read the pixels.
                gl.glReadBuffer(GL.GL_FRONT);
                gl.glReadPixels(0, 0, panelWidth, panelHeight, GL.GL_BGRA, GL2GL3.GL_UNSIGNED_INT_8_8_8_8_REV, readBackInts);

                // Restore saved modes.
                gl.glPixelStorei(GL2GL3.GL_PACK_SWAP_BYTES, swapbytes[0]);
                gl.glPixelStorei(GL2ES3.GL_PACK_ROW_LENGTH, rowlength[0]);
                gl.glPixelStorei(GL2ES3.GL_PACK_SKIP_ROWS, skiprows[0]);
                gl.glPixelStorei(GL2ES3.GL_PACK_SKIP_PIXELS, skippixels[0]);
                gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, alignment[0]);

                // Copy temporary data into raster of BufferedImage for faster
                // blitting Note that we could avoid this copy in the cases
                // where !offscreenContext.offscreenImageNeedsVerticalFlip(),
                // but that's the software rendering path which is very slow
                // anyway
                final int[] src = readBackInts.array();
                final PixelWriter writer = offscreenImage.getPixelWriter();
                // set alpha to 1 (probably want to preserve alpha but for now do what the other scenes do)
                for (int i = 0; i < src.length; i++) {
                    src[i] |= (0xff << 24);
                }
                // swap order of y axis
                for (int y = 0; y < panelHeight; y++) {
                    writer.setPixels(0, panelHeight - 1 - y, panelWidth, 1, PixelFormat.getIntArgbInstance(), src, y * panelWidth, panelWidth);
                }
            }
        }
    }

    private class TaskScheduler {

        private final Map<String, TimerTask> last = new HashMap<>();

        private TaskScheduler() {
            super();
        }

        /**
         * Schedule this task for execution
         */
        private void schedule(final TimerTask task) {
            synchronized (this) {
                last.put(task.getClass().getName(), task);
                synchronized (FxAbstractScene.this) {
                    if (drawTimer != null) {
                        drawTimer.schedule(new TaskSchedulerRunnable(this, task), 0L);
                    }
                }
            }
        }

        /**
         * @return true if this task should run
         */
        private boolean shouldRun(final TimerTask task) {
            synchronized (this) {
                return last.get(task.getClass().getName()) == task;
            }
        }
    }

    private static class TaskSchedulerRunnable extends TimerTask {

        final TaskScheduler scheduler;
        private final TimerTask task;

        private TaskSchedulerRunnable(final TaskScheduler scheduler, final TimerTask task) {
            this.scheduler = scheduler;
            this.task = task;
        }

        @Override
        public void run() {
            if (scheduler.shouldRun(task)) {
                task.run();
            }
        }
    }

    private class ResetTask extends TimerTask {

        private final Rectangle bounds;

        private ResetTask(final Rectangle bounds) {
            super();
            this.bounds = bounds;
        }

        @Override
        public void run() {
            synchronized (FxAbstractScene.this) {
                if (glContext != null && drawable.isRealized()) {
                    logger.log(Level.FINE, "Resetting the FxAbstractScene.");
                    // get all of the cameras
                    lookupAll(Camera.class, cameras);
                    for (final Camera camera : cameras) {
                        camera.setViewport(bounds);
                    }
                    // reset all of the OpenGL scene options
                    glContext.makeCurrent();
                    // set up the gl fields
                    final GL2 gl = builder.isDebug() ? new DebugGL2(glContext.getGL().getGL2()) : glContext.getGL().getGL2();
                    glu = new GLUgl2();
                    // reset all of the OpenGL flags
                    sceneRenderer.initialize(gl, glu);
                    initialize(gl);
                    // release the current gl context
                    glContext.release();
                    // check load status
                    final boolean load;
                    synchronized (FxAbstractScene.this) {
                        load = !loaded;
                        if (load) {
                            loaded = true;
                        }
                    }
                    if (load) {
                        // initialize services
//                        TextRendererFactory.newInstance(FxAbstractScene.this);
//                        TextSupportFactory.newInstance(FxAbstractScene.this);
                        load();
                        getPropertyChangeSupport().firePropertyChange(Scene.LOADED, false, true);
                    }
                    sceneRenderer.setRepaint(DrawingPass.ALL);
                }
            }
        }
    }

    private class StartTask extends TimerTask {

        private StartTask() {
            super();
        }

        @Override
        public void run() {
            try {
                if (FxAbstractScene.this != null && !drawing.getAndSet(true)) {
                    sceneRenderer.performUpdate();
                    drawGLScene();
                    drawing.set(false);
                }
            } catch (final RuntimeException ex) {
                logger.log(Level.WARNING, "Exception during draw method.", ex);
                drawing.set(false);
            }
        }
    }

    private class ShutdownTask extends TimerTask {

        private ShutdownTask() {
            super();
        }

        @Override
        public void run() {
            synchronized (FxAbstractScene.this) {
                // set the scene and context as current
                if (glContext != null && drawable.isRealized()) {
                    glContext.makeCurrent();
                    // shutdown all the initialized objects and actors
                    final GL2 gl = builder.isDebug() ? new DebugGL2(glContext.getGL().getGL2()) : glContext.getGL().getGL2();
                    final GLUgl2 glu = FxAbstractScene.this.glu;
                    final Set<Initializable> inits = lookupAll(Initializable.class);
                    for (final Initializable initializable : inits) {
                        initializable.unInitialize(gl, glu);
                    }
                    glContext.release();
                    glContext.destroy();
                }
            }
        }
    }

    protected static class DrawToCanvasRunnable implements Runnable {

        private final FxAbstractScene scene;
        private final AtomicBoolean queued = new AtomicBoolean(true);

        protected DrawToCanvasRunnable(final FxAbstractScene scene) {
            this.scene = scene;
        }

        /**
         * Queue the runnable to draw.
         */
        protected void enqueue() {
            queued.set(true);
        }

        @Override
        public void run() {
            if (!queued.getAndSet(false)) {
                return;
            }
            final GraphicsContext graphics = scene.getGraphicsContext2D();
            synchronized (scene.offscreenImageSync) {
                final double width = scene.getWidth();
                final double height = scene.getHeight();
                graphics.clearRect(0.0, 0.0, width, height);
                graphics.drawImage(scene.offscreenImage, 0, 0, width, height);
            }
        }
    }
}
