package gov.pnnl.svf.scene;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.FpsActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.CameraExt;
import gov.pnnl.svf.camera.DrawingCamera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.util.StateUtil;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Frustum;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ColorPickingUtils;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.ItemPickingSupport;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.picking.PickingSupport;
import gov.pnnl.svf.service.DrawableService;
import gov.pnnl.svf.support.BlendingSupport;
import gov.pnnl.svf.support.ChildSupport;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.CullingSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.update.Task;
import gov.pnnl.svf.update.UninitializeTask;
import gov.pnnl.svf.update.UpdateTask;
import gov.pnnl.svf.update.UpdateTaskRunnable;
import gov.pnnl.svf.util.DebugUtil;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.MemLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Renderer class for the scene. This is to separate rendering functionality
 * that is common to various implementations of the abstract scenes.
 *
 * @author Arthur Bleeker
 *
 */
public class SceneRenderer implements Disposable {

    /**
     * Type and id used for the overlay camera actor.
     */
    public static final String OVERLAY_CAMERA = "overlay-camera";

    /**
     * Enumeration for the various attrib stack depths utilized during
     * rendering.
     */
    private static enum AttribStackDepths {

        START,
        UNINITIALIZE,
        INITIALIZE,
        PICKING_RENDER,
        COLOR_RENDER,
        SCENE_RENDER;
    }

    /**
     * Enumeration for the various matrix stack depths utilized during
     * rendering.
     */
    private static enum MatrixStackDepths {

        MODELVIEW,
        PROJECTION,
        TEXTURE;
    }

    private static final long FPS_LOGGER_INTERVAL = 10 * 1000;
    private static final Logger logger = Logger.getLogger(SceneRenderer.class.toString());
    /**
     * State mask for boolean field in this actor.
     */
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[0];
    /**
     * State mask for boolean field in this actor.
     */
    private static final byte DOUBLE_BUFFER_MASK = StateUtil.getMasks()[1];
    /**
     * State mask for boolean field in this actor.
     */
    private static final byte STEREO_MASK = StateUtil.getMasks()[2];
    /**
     * State mask for boolean field in this actor.
     */
    private static final byte AUX_BUFFER_MASK = StateUtil.getMasks()[3];
    /**
     * State mask for boolean field in this actor.
     */
    private static final byte ATTRIB_STACK_MASK = StateUtil.getMasks()[4];
    /**
     * State mask for boolean field in this actor.
     */
    private static final byte MATRIX_STACK_MASK = StateUtil.getMasks()[5];
    /**
     * State used by flags in this actor to save memory space. Up to 8 states
     * can be used and should go in the following order: 0x01, 0x02, 0x04, 0x08,
     * 0x10, 0x20, 0x40, and (byte) 0x80. Set the initial state in the
     * constructor by or-ing the new states with the current state.
     */
    private byte state = StateUtil.NONE;
    private final AtomicBoolean initLogged = new AtomicBoolean(false);
    private final AtomicBoolean disposed = new AtomicBoolean(false);
    private final List<Class<? extends Drawable>> drawableTypes;
    private final Set<Class<? extends Drawable>> drawableTypesSet;
    private final SceneCollections collections;
    private final SceneTimers timers;
    private final SceneExt scene;
    private final SceneBuilder builder;
    private final Camera overlay;
    private long updateTime = 0L;
    private long culledActors = 0L;
    private Color background;
    private Vector3D boundary = new Vector3D(10.0, 10.0, 10.0);
    private Vector3D center = new Vector3D(0.0, 0.0, 0.0);
    private int maxInitializations;

    /**
     * Constructor
     *
     * @param scene   reference to the parent scene
     * @param builder reference to the builder
     */
    public SceneRenderer(final SceneExt scene, final SceneBuilder builder) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        if (builder == null) {
            throw new NullPointerException("builder");
        }
        collections = new SceneCollectionsImpl();
        timers = new SceneTimersImpl();
        this.scene = scene;
        this.builder = new ImmutableSceneBuilder(builder);
        // do debug tasks if enabled
        if (this.builder.isDebug() || this.builder.isDisplayFps()) {
            UpdateTask.schedule(scene, new DebugLoadUpdateTaskRunnable(scene, this.builder), 400L);
        }
        drawableTypesSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(builder.copyDrawableTypes())));
        drawableTypes = new ArrayList<>(drawableTypesSet);
        // instantiate overlay camera
        overlay = new SimpleCamera(scene, OVERLAY_CAMERA, OVERLAY_CAMERA);
        overlay.setDrawingPass(DrawingPass.OVERLAY);
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return StateUtil.isValue(state, DISPOSED_MASK);
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            state = StateUtil.setValue(state, DISPOSED_MASK);
        }
        if (!disposed.getAndSet(true)) {
            overlay.dispose();
            collections.dispose();
        }
    }

    /**
     * @return the metrics
     */
    SceneMetrics getSceneMetrics() {
        return timers;
    }

    /**
     * @return the background
     */
    Color getBackground() {
        synchronized (this) {
            return background;
        }
    }

    /**
     * @return the boundary
     */
    Vector3D getBoundary() {
        synchronized (this) {
            return boundary;
        }
    }

    /**
     * @return the center
     */
    Vector3D getCenter() {
        synchronized (this) {
            return center;
        }
    }

    /**
     * @return the maxInitializations
     */
    int getMaxInitializations() {
        synchronized (this) {
            return maxInitializations;
        }
    }

    /**
     * @return true if currently drawing
     */
    boolean isDrawing() {
        return timers.getDrawing();
    }

    /**
     * @return true if currently updating
     */
    boolean isUpdating() {
        return timers.getUpdating();
    }

    /**
     * @return the repaint
     */
    DrawingPass getRepaint() {
        return timers.getRepaint();
    }

    /**
     * @return the current drawing pass
     */
    DrawingPass getCurrentDrawingPass() {
        return timers.getCurrentDrawingPass();
    }

    /**
     * @return the set of drawable types
     */
    Set<Class<? extends Drawable>> getDrawableTypes() {
        return drawableTypesSet;
    }

    /**
     * Main method and entry point for drawing the scene.
     *
     * @param gl  reference to GL
     * @param glu reference to GLU
     *
     * @return true if the scene was rendered
     */
    boolean performRender(final GL2 gl, final GLUgl2 glu) {
        // scene is diposed
        if (disposed.get()) {
            return false;
        }
        // start drawing, unless already drawing and return false since this call didn't render anything
        if (timers.getAndSetDrawing(true)) {
            return false;
        }
        try {
            // track time
            final long start = System.currentTimeMillis();
            // get repaint state and initialize metrics and collections
            long verticesRendered = 0L;
            culledActors = 0L;
            final boolean stereo;
            final boolean doubleBuffer;
            final boolean auxBuffer;
            final boolean attribStack;
            final boolean matrixStack;
            synchronized (this) {
                stereo = StateUtil.isValue(state, STEREO_MASK);
                doubleBuffer = StateUtil.isValue(state, DOUBLE_BUFFER_MASK);
                auxBuffer = StateUtil.isValue(state, AUX_BUFFER_MASK);
                attribStack = StateUtil.isValue(state, ATTRIB_STACK_MASK);
                matrixStack = StateUtil.isValue(state, MATRIX_STACK_MASK);
            }
            DrawingPass repaint = timers.getAndSetRepaint(DrawingPass.NONE);
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.FINE, "{0}: Repainting the scene drawing passes: {1}", new Object[]{scene, repaint});
            }
            // uncomment the following line to view which passes got dirtied each time
            // System.out.println("Dirtied scene drawing passes: " + repaint);
            // attempt to utilize off screen frame buffers to handle various layers
            if (builder.isDebugColorPicking()) {
                // display the color picking drawing
                repaint = DrawingPass.PICKING;
            } else if (!auxBuffer && doubleBuffer && (repaint.isScene() || repaint.isInterface() || repaint.isOverlay())) {
                // if aux buffers is not available, then repaint all visible layers
                repaint = repaint.addDrawingPass(DrawingPass.SCENE_INTERFACE_OVERLAY);
            } else if (!auxBuffer && !doubleBuffer && (repaint != DrawingPass.NONE)) {
                // if no aux buffers or double buffer then repaint all layers
                repaint = repaint.addDrawingPass(DrawingPass.ALL);
            }
            // uncomment the following line to view which passes get drawn each time
            // System.out.println("Repainting scene drawing passes: " + repaint);
            // define the back buffers
            final int[] backBuffers;
            if (stereo) {
                backBuffers = doubleBuffer
                              ? new int[]{GL2.GL_BACK_LEFT, GL2.GL_BACK_RIGHT}
                              : new int[]{GL2.GL_FRONT_LEFT, GL2.GL_FRONT_RIGHT};
            } else {
                backBuffers = doubleBuffer
                              ? new int[]{GL.GL_BACK}
                              : new int[]{GL.GL_FRONT};
            }
            // monitor the stack depths
            final int[] attribStackDepths = new int[AttribStackDepths.values().length];
            if (attribStack) {
                gl.glGetIntegerv(GL2.GL_ATTRIB_STACK_DEPTH, attribStackDepths, AttribStackDepths.START.ordinal());
            }
            // un-initialization
            performUninitialize(gl, glu);
            if (attribStack) {
                gl.glGetIntegerv(GL2.GL_ATTRIB_STACK_DEPTH, attribStackDepths, AttribStackDepths.UNINITIALIZE.ordinal());
            }
            // initialization
            performInitialize(gl, glu);
            if (attribStack) {
                gl.glGetIntegerv(GL2.GL_ATTRIB_STACK_DEPTH, attribStackDepths, AttribStackDepths.INITIALIZE.ordinal());
            }
            // populate collections required for rendering
            if (repaint != DrawingPass.NONE) {
                // get the visible actor list
                scene.getVisibleRootActors(collections.getActors());
                // update the list of services
                scene.lookupAll(DrawableService.class, collections.getServices());
                // update the list of drawables
                scene.lookupAll(DrawableItem.class, collections.getDrawables());
                // get the drawing camera list
                scene.lookupAll(DrawingCamera.class, collections.getDrawingCameras());
                // get the picking camera list
                scene.lookupAll(PickingCamera.class, collections.getPickingCameras());
            }
            // start services for all repaint types
            if (repaint != DrawingPass.NONE) {
                // start the services
                for (int i = 0; i < collections.getServices().size(); i++) {
                    collections.getServices().get(i).draw(gl, glu, null);
                }
            }
            // only draw the color picking layer if the picking layer requires redrawing
            if (repaint.isPicking()) {
                // determine if color picking is necessary
                boolean perform = false;
                for (final PickingCamera test : collections.getPickingCameras()) {
                    if (test.isVisible() && test instanceof ColorPickingCamera) {
                        perform = true;
                        break;
                    }
                }
                if (perform) {
                    // continue
                    final ColorPickingUtils colorPickingUtils = scene.getColorPickingUtils();
                    // determine the color picking buffer to use
                    // start the color picking render for all cameras
                    // this logic is repeated in the initialize(GL2,GLUgl2)
                    final int buffer;
                    if (builder.isDebugColorPicking()) {
                        // debug draws to the visible screen
                        buffer = colorPickingUtils.start(gl, doubleBuffer ? GL.GL_BACK : GL.GL_FRONT);
                    } else if (builder.isTextureColorPicking()) {
                        // the returned buffer will point to a texture buffer
                        buffer = colorPickingUtils.start(gl);
                    } else if (auxBuffer) {
                        // utilize an aux buffer
                        buffer = colorPickingUtils.start(gl, GL2.GL_AUX0);
                    } else if (doubleBuffer) {
                        // double buffered uses the back buffer
                        buffer = colorPickingUtils.start(gl, GL.GL_BACK);
                    } else {
                        // single buffered must use a texture buffer
                        buffer = colorPickingUtils.start(gl);
                    }
                    // clear the entire canvas for color picking
                    clearScreen(gl, null, Color.WHITE, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, buffer);
                    // do color picking camera drawing first
                    for (final PickingCamera camera : collections.getPickingCameras()) {
                        if (!camera.isVisible()) {
                            // only visible cameras need to be drawn
                            continue;
                        }
                        if (camera instanceof ColorPickingCamera) {
                            // draw the color picking shapes, which also creates the new picking canvas for the camera
                            final Rectangle viewport = camera.getViewport();
                            final ColorSupport color = camera.lookup(ColorSupport.class);
                            clearScreen(gl, viewport, color != null ? Color.BLACK : null, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, buffer);
                            performColorPickingRender(gl, glu, (ColorPickingCamera) camera, collections.getActors());
                        }
                        // increment rendering performance stat
                        verticesRendered += camera.getExtended().getVerticesCounter();
                        camera.getExtended().resetVerticesCounter();
                    }
                    // end the color picking render for all cameras
                    clearScreen(gl, null, null, 0, buffer);
                    gl.glFinish();
                    colorPickingUtils.end(gl);
                }
            }
            if (attribStack) {
                gl.glGetIntegerv(GL2.GL_ATTRIB_STACK_DEPTH, attribStackDepths, AttribStackDepths.COLOR_RENDER.ordinal());
            }
            // clear the scene if the main layer requires redrawing
            if (repaint.isScene() || repaint.isInterface() || repaint.isOverlay()) {
                // clear for main rendering
                final Color bg = getBackground();
                clearScreen(gl, null, bg != null ? bg : Color.BLACK, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, backBuffers);
                if (auxBuffer) {
                    if (repaint.isScene()) {
                        clearScreen(gl, null, bg != null ? bg : Color.BLACK, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, GL2.GL_AUX1);
                    }
                    if (repaint.isInterface()) {
                        clearScreen(gl, null, Color.TRANSPARENT, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, GL2.GL_AUX2);
                    }
                    if (repaint.isOverlay()) {
                        clearScreen(gl, null, Color.TRANSPARENT, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, GL2.GL_AUX3);
                    }
                }
            }
            // iterate through the cameras and perform picking rendering
            for (final PickingCamera camera : collections.getPickingCameras()) {
                if (!camera.isVisible()) {
                    // only visible cameras need to be drawn
                    continue;
                }
                if (!(camera instanceof ColorPickingCamera)) {
                    // color picking render happened before this loop
                    final Rectangle viewport = camera.getViewport();
                    clearScreen(gl, viewport, null, 0, backBuffers);
                    performPickingRender(gl, glu, camera, collections.getActors());
                }
                // increment rendering performance stat
                verticesRendered += camera.getExtended().getVerticesCounter();
                camera.getExtended().resetVerticesCounter();
            }
            if (attribStack) {
                gl.glGetIntegerv(GL2.GL_ATTRIB_STACK_DEPTH, attribStackDepths, AttribStackDepths.PICKING_RENDER.ordinal());
            }
            // paint the scene if necessary
            if (repaint.isScene()) {
                // render from cameras
                for (final Camera camera : collections.getDrawingCameras()) {
                    if (!camera.isVisible()) {
                        // only visible cameras need to be drawn
                        continue;
                    }
                    // set up camera rendering area
                    final Rectangle viewport = camera.getViewport();
                    // clear area if the camera has color
                    final ColorSupport color = camera.lookup(ColorSupport.class);
                    if (auxBuffer) {
                        clearScreen(gl, viewport, color != null ? color.getColor() : null, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, GL2.GL_AUX1);
                    } else {
                        clearScreen(gl, viewport, color != null ? color.getColor() : null, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, backBuffers);
                    }
                    // draw rendering area
                    if (stereo) {
                        gl.glDrawBuffer(doubleBuffer ? GL2.GL_BACK_LEFT : GL2.GL_FRONT_LEFT);
                        performSceneRender(gl, glu, camera.getExtended(), collections.getActors(), attribStack);
                        gl.glDrawBuffer(doubleBuffer ? GL2.GL_BACK_RIGHT : GL2.GL_FRONT_RIGHT);
                        performSceneRender(gl, glu, camera.getExtended(), collections.getActors(), attribStack);
                    } else {
                        performSceneRender(gl, glu, camera.getExtended(), collections.getActors(), attribStack);
                    }
                    // increment rendering performance stat
                    verticesRendered += camera.getExtended().getVerticesCounter();
                    camera.getExtended().resetVerticesCounter();
                }
            }
            // paint the scene and user interface if necessary
            if (repaint.isInterface()) {
                // render from cameras
                for (final Camera camera : collections.getDrawingCameras()) {
                    if (!camera.isVisible()) {
                        // only visible cameras need to be drawn
                        continue;
                    }
                    // set up camera rendering area
                    final Rectangle viewport = camera.getViewport();
                    if (auxBuffer) {
                        clearScreen(gl, viewport, null, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, GL2.GL_AUX2);
                    } else {
                        clearScreen(gl, viewport, null, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, backBuffers);
                    }
                    // draw rendering area
                    if (stereo) {
                        gl.glDrawBuffer(doubleBuffer ? GL2.GL_BACK_LEFT : GL2.GL_FRONT_LEFT);
                        performInterfaceRender(gl, glu, camera.getExtended(), collections.getActors(), attribStack);
                        gl.glDrawBuffer(doubleBuffer ? GL2.GL_BACK_RIGHT : GL2.GL_FRONT_RIGHT);
                        performInterfaceRender(gl, glu, camera.getExtended(), collections.getActors(), attribStack);
                    } else {
                        performInterfaceRender(gl, glu, camera.getExtended(), collections.getActors(), attribStack);
                    }
                    // increment rendering performance stat
                    verticesRendered += camera.getExtended().getVerticesCounter();
                    camera.getExtended().resetVerticesCounter();
                }
            }
            // render overlay if necessary
            if (repaint.isOverlay()) {
                if (auxBuffer) {
                    clearScreen(gl, null, null, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, GL2.GL_AUX3);
                } else {
                    clearScreen(gl, null, null, GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, backBuffers);
                }
                if (stereo) {
                    gl.glDrawBuffer(doubleBuffer ? GL2.GL_BACK_LEFT : GL2.GL_FRONT_LEFT);
                    performOverlayRender(gl, glu, overlay.getExtended(), collections.getActors(), attribStack);
                    gl.glDrawBuffer(doubleBuffer ? GL2.GL_BACK_RIGHT : GL2.GL_FRONT_RIGHT);
                    performOverlayRender(gl, glu, overlay.getExtended(), collections.getActors(), attribStack);
                } else {
                    performOverlayRender(gl, glu, overlay.getExtended(), collections.getActors(), attribStack);
                }
                // increment rendering performance stat
                verticesRendered += overlay.getExtended().getVerticesCounter();
                overlay.getExtended().resetVerticesCounter();
            }
            // copy the specific buffers to the back or front buffer as applicable
            if (auxBuffer && (repaint.isScene() || repaint.isInterface() || repaint.isOverlay()) && !builder.isDebugColorPicking()) {
                final int buffer = doubleBuffer ? GL.GL_BACK : GL.GL_FRONT;
                clearScreen(gl, null, null, 0, buffer);
                final int[] viewport = new int[4];
                gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
                // scene
                gl.glReadBuffer(GL2.GL_AUX1);
                gl.glCopyPixels(viewport[0], viewport[1], viewport[2], viewport[3], GL2.GL_COLOR);
                // interface
                gl.glReadBuffer(GL2.GL_AUX2);
                gl.glCopyPixels(viewport[0], viewport[1], viewport[2], viewport[3], GL2.GL_COLOR);
                // overlay
                gl.glReadBuffer(GL2.GL_AUX3);
                gl.glCopyPixels(viewport[0], viewport[1], viewport[2], viewport[3], GL2.GL_COLOR);
                // set the read buffer back
                gl.glReadBuffer(buffer);
            }
            // stop the services
            if (repaint != DrawingPass.NONE) {
                for (int i = collections.getServices().size() - 1; i >= 0; i--) {
                    collections.getServices().get(i).endDraw(gl, glu, null);
                }
            }
            if (attribStack) {
                gl.glGetIntegerv(GL2.GL_ATTRIB_STACK_DEPTH, attribStackDepths, AttribStackDepths.SCENE_RENDER.ordinal());
            }
            // check attrib stack depths
            int lastAttribStackDepth = 0;
            for (int i = 0; i < attribStackDepths.length; i++) {
                if (attribStackDepths[i] != lastAttribStackDepth) {
                    if (scene.getSceneBuilder().isVerbose()) {
                        logger.log(Level.WARNING, "{0}: Attrib stack depth after {1} was not zero: {2}", new Object[]{scene, AttribStackDepths.values()[i],
                                                                                                                      attribStackDepths[i] - lastAttribStackDepth});
                    }
                    lastAttribStackDepth = attribStackDepths[i];
                }
            }
            // pop the rest of the attrib stack
            for (int i = 0; i < lastAttribStackDepth; i++) {
                gl.glPopAttrib();
            }
            // check the matrix stack depths
            final int[] matrixStackDepths = new int[MatrixStackDepths.values().length];
            if (matrixStack) {
                for (int i = 0; i < matrixStackDepths.length; i++) {
                    final MatrixStackDepths matrix = MatrixStackDepths.values()[i];
                    switch (matrix) {
                        case MODELVIEW:
                            gl.glGetIntegerv(GL2.GL_MODELVIEW_STACK_DEPTH, matrixStackDepths, i);
                            break;
                        case PROJECTION:
                            gl.glGetIntegerv(GL2.GL_PROJECTION_STACK_DEPTH, matrixStackDepths, i);
                            break;
                        case TEXTURE:
                            gl.glGetIntegerv(GL2.GL_TEXTURE_STACK_DEPTH, matrixStackDepths, i);
                            break;
                        default:
                            throw new IllegalArgumentException("Unhandled enumeration type passed to control statement: " + matrix);
                    }
                    if (matrixStackDepths[i] > 1 && scene.getSceneBuilder().isVerbose()) {
                        logger.log(Level.WARNING, "{0}: Matrix stack for {1} was not zero or one: {2}", new Object[]{scene, matrix.toString(), matrixStackDepths[i]});
                    }
                }
            }
            // check for gl errors
            final int errorsReported;
            if (scene.getSceneBuilder().isVerbose()) {
                errorsReported = DebugUtil.checkGLErrors(gl);
            } else {
                errorsReported = DebugUtil.clearGLErrors(gl);
            }
            // gather statistics
            timers.setLastDrawLength(System.currentTimeMillis() - start);
            timers.setLastVerticesRendered(verticesRendered);
            timers.setLastCulledActors(culledActors);
            timers.setLastAttribStackDepth(lastAttribStackDepth);
            timers.setLastModelviewStackDepth(matrixStackDepths[MatrixStackDepths.MODELVIEW.ordinal()]);
            timers.setLastProjectionStackDepth(matrixStackDepths[MatrixStackDepths.PROJECTION.ordinal()]);
            timers.setLastTextureStackDepth(matrixStackDepths[MatrixStackDepths.TEXTURE.ordinal()]);
            timers.setLastErrorsReported(errorsReported);
            timers.setCollectionsInfo(collections.toString());
            if (scene.getSceneBuilder().isVerbose() && isSlow()) {
                logger.log(Level.INFO, "{0}: Scene took too long to render: draw time {1} ms, update time {2} ms", new Object[]{scene, timers.getLastDrawLength(),
                                                                                                                                timers.getLastUpdateLength()});
            }
            return repaint.isScene() || repaint.isInterface() || repaint.isOverlay() || builder.isDebugColorPicking();
        } catch (final RuntimeException ex) {
            logger.log(Level.WARNING, MessageFormat.format("{0}: Runtime Exception during drawGLScene method.", scene), ex);
            return false;
        } finally {
            timers.setDrawing(false);
        }
    }

    private void performUninitialize(final GL2 gl, final GLUgl2 glu) {
        // uninitialize tasks
        DrawingPass uninitialized = DrawingPass.NONE;
        scene.lookupAll(UninitializeTask.class, collections.getUninitializables());
        for (final UninitializeTask task : collections.getUninitializables()) {
            final boolean initialized = task.isInitialized();
            task.unInitialize(gl, glu);
            if (initialized && task.isVisible()) {
                uninitialized = uninitialized.addDrawingPass(task.getDrawingPass());
            }
        }
        if (uninitialized != DrawingPass.NONE) {
            timers.setRepaint(uninitialized);
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.INFO, "{0}: Initializable that was un-initialized during draw scene caused a redraw.", scene);
            }
        }
    }

    private void performInitialize(final GL2 gl, final GLUgl2 glu) {
        // initialize some objects
        int loads;
        synchronized (this) {
            loads = maxInitializations;
        }
        DrawingPass initialized = DrawingPass.NONE;
        scene.lookupAll(Initializable.class, collections.getInitializables());
        for (final Initializable initializable : collections.getInitializables()) {
            if (initializable.isVisible()) {
                // the initializable is visible
                if (!initializable.isInitialized()) {
                    if (initializable.isSlow()) {
                        // it's a slow init type so only initialize if the max number hasn't been exceeded
                        if (loads > 0) {
                            initializable.initialize(gl, glu);
                            if (initializable.isInitialized()) {
                                loads--;
                            }
                        }
                    } else {
                        initializable.initialize(gl, glu);
                    }
                    // ensure scene remains dirty until all objects are initialized
                    if (!initializable.isInitialized()) {
                        initialized = initialized.addDrawingPass(initializable.getDrawingPass());
                    }
                }
            } else // not visible so uninitialize
            {
                if (initializable.isInitialized()) {
                    initializable.unInitialize(gl, glu);
                }
            }
        }
        if (initialized != DrawingPass.NONE) {
            timers.setRepaint(initialized);
            if (scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.INFO, "{0}: Initializable that was initialized during draw scene caused a redraw.", scene);
            }
        }
    }

    private void performOverlayRender(final GL2 gl, final GLUgl2 glu, final CameraExt<?> camera, final Collection<Actor> actors, final boolean attribStack) {
        camera.setDirty(false);
        // check camera pass
        if (!camera.getDrawingPass().containsDrawingPass(DrawingPass.OVERLAY)) {
            return;
        }
        timers.setCurrentDrawingPass(DrawingPass.OVERLAY);
        camera.setViewport(scene.getViewport());
        // start with a fresh transformation matrix
        gl.glLoadIdentity();
        camera.makeOrtho2D(gl, glu);
        // draw the user interface components
        for (final Actor actor : actors) {
            drawActor(gl, glu, camera, actor, DrawingPass.OVERLAY, 0, attribStack);
        }
        camera.endOrtho2D(gl, glu);
        timers.setCurrentDrawingPass(null);
    }

    private void performSceneRender(final GL2 gl, final GLUgl2 glu, final CameraExt<?> camera, final Collection<Actor> actors, final boolean attribStack) {
        camera.setDirty(false);
        // check camera pass
        if (!camera.getDrawingPass().containsDrawingPass(DrawingPass.SCENE)) {
            return;
        }
        timers.setCurrentDrawingPass(DrawingPass.SCENE);
        // draw the scene components that aren't actors
        if (!collections.getDrawables().isEmpty()) {
            // draw the components
            for (int i = 0; i < collections.getDrawables().size(); i++) {
                collections.getDrawables().get(i).draw(gl, glu, camera);
            }
        }
        // set up blending if enabled on the camera
        final BlendingSupport blending = camera.lookup(BlendingSupport.class);
        if (blending != null) {
            blending.draw(gl, glu, camera);
        }
        // render the scene layer
        for (int i = builder.getNumberOfSceneDrawingPasses() - 1; i >= 0; i--) {
            // start with a fresh transformation matrix
            gl.glLoadIdentity();
            camera.makePerspective(gl, glu);
            camera.setLookAt(gl, glu);
            // get the current camera frustum
            final Frustum frustum = camera.getFrustum();
            // draw the scene components
            for (final Actor actor : actors) {
                // check if it's been culled
                final CullingSupport culling = actor.lookup(CullingSupport.class);
                if (culling != null) {
                    if (frustum.contains(culling.getLocation(), culling.getRadius())) {
                        drawActor(gl, glu, camera, actor, DrawingPass.SCENE, i, attribStack);
                    } else if (actor.getPassNumber() == i) {
                        culledActors++;
                    }
                } else {
                    drawActor(gl, glu, camera, actor, DrawingPass.SCENE, i, attribStack);
                }
            }
            camera.endPerspective(gl, glu);
        }
        // end draw on the drawables
        if (!collections.getDrawables().isEmpty()) {
            for (int i = collections.getDrawables().size() - 1; i >= 0; i--) {
                collections.getDrawables().get(i).endDraw(gl, glu, camera);
            }
        }
        // stop blending
        if (blending != null) {
            blending.endDraw(gl, glu, camera);
        }
        timers.setCurrentDrawingPass(null);
    }

    private void performInterfaceRender(final GL2 gl, final GLUgl2 glu, final CameraExt<?> camera, final Collection<Actor> actors, final boolean attribStack) {
        camera.setDirty(false);
        // check camera pass
        if (!camera.getDrawingPass().containsDrawingPass(DrawingPass.INTERFACE)) {
            return;
        }
        timers.setCurrentDrawingPass(DrawingPass.INTERFACE);
        // draw the scene components that aren't actors
        if (!collections.getDrawables().isEmpty()) {
            // draw the components
            for (int i = 0; i < collections.getDrawables().size(); i++) {
                collections.getDrawables().get(i).draw(gl, glu, camera);
            }
        }
        // set up blending if enabled on the camera
        final BlendingSupport blending = camera.lookup(BlendingSupport.class);
        if (blending != null) {
            blending.draw(gl, glu, camera);
        }
        // render the user interface layer
        for (int i = 0; i < builder.getNumberOfUserInterfaceDrawingPasses(); i++) {
            // start with a fresh transformation matrix
            gl.glLoadIdentity();
            camera.makeOrtho2D(gl, glu);
            // draw the user interface components
            for (final Actor actor : actors) {
                drawActor(gl, glu, camera, actor, DrawingPass.INTERFACE, i, attribStack);
            }
            camera.endOrtho2D(gl, glu);
        }
        // end draw on the drawables
        if (!collections.getDrawables().isEmpty()) {
            for (int i = collections.getDrawables().size() - 1; i >= 0; i--) {
                collections.getDrawables().get(i).endDraw(gl, glu, camera);
            }
        }
        // stop blending
        if (blending != null) {
            blending.endDraw(gl, glu, camera);
        }
        timers.setCurrentDrawingPass(null);
    }

    private void performPickingRender(final GL2 gl, final GLUgl2 glu, final PickingCamera camera, final Collection<Actor> actors) {
        if (camera.getDrawingPass().containsDrawingPass(DrawingPass.SCENE)) {
            camera.setDirty(false);
            // get the current camera frustum
            final Frustum frustum = camera.getFrustum();
            for (final PickingCameraEvent event : camera.getEvents()) {
                camera.start(event);
                // process the picks
                for (final Actor actor : actors) {
                    // only pick the root actors from the base scene draw call
                    // only pick if this actor is viewable in the camera
                    // ray casting picking only works for scene objects
                    if (actor.getDrawingPass().containsDrawingPass(DrawingPass.SCENE)
                        && (actor.getCamera() == null || actor.getCamera() == camera.getCamera())) {
                        timers.setCurrentDrawingPass(DrawingPass.SCENE);
                        // start with a fresh transformation matrix
                        gl.glLoadIdentity();
                        camera.makePerspective(gl, glu);
                        camera.setLookAt(gl, glu);
                        // check the actor and children for picks
                        // check if it's been culled
                        final CullingSupport culling = actor.lookup(CullingSupport.class);
                        if (culling != null) {
                            if (frustum.contains(culling.getLocation(), culling.getRadius())) {
                                pickActor(gl, glu, camera, camera.getReferenceCamera(), actor);
                            }
                        } else {
                            pickActor(gl, glu, camera, camera.getReferenceCamera(), actor);
                        }
                        // end
                        camera.endPerspective(gl, glu);
                        timers.setCurrentDrawingPass(null);
                    }
                }
                camera.end(event);
            }
        }
        if (camera.getDrawingPass().containsDrawingPass(DrawingPass.INTERFACE)) {
            camera.setDirty(false);
            for (final PickingCameraEvent event : camera.getEvents()) {
                camera.start(event);
                // process the picks
                for (final Actor actor : actors) {
                    // only pick the root actors from the base scene draw call
                    // only pick if this actor is viewable in the camera
                    // ray casting picking only works for scene objects
                    if (actor.getDrawingPass().containsDrawingPass(DrawingPass.INTERFACE)
                        && (actor.getCamera() == null || actor.getCamera() == camera.getCamera())) {
                        timers.setCurrentDrawingPass(DrawingPass.INTERFACE);
                        // start with a fresh transformation matrix
                        gl.glLoadIdentity();
                        camera.makeOrtho2D(gl, glu);
                        // check the actor and children for picks
                        pickActor(gl, glu, camera, camera.getReferenceCamera(), actor);
                        // end
                        camera.endOrtho2D(gl, glu);
                        timers.setCurrentDrawingPass(null);
                    }
                }
                camera.end(event);
            }
        }
    }

    private void performColorPickingRender(final GL2 gl, final GLUgl2 glu, final ColorPickingCamera camera, final Collection<Actor> actors) {
        if (camera.getDrawingPass().containsDrawingPass(DrawingPass.SCENE_PICKING)) {
            camera.setDirty(false);
            timers.setCurrentDrawingPass(DrawingPass.SCENE_PICKING);
            for (int i = builder.getNumberOfSceneDrawingPasses() - 1; i >= 0; i--) {
                // start with a fresh transformation matrix
                gl.glLoadIdentity();
                camera.makePerspective(gl, glu);
                camera.setLookAt(gl, glu);
                // get the current camera frustum
                final Frustum frustum = camera.getFrustum();
                // draw the scene components
                for (final Actor actor : actors) {
                    // check if it's been culled
                    final CullingSupport culling = actor.lookup(CullingSupport.class);
                    if (culling != null) {
                        if (frustum.contains(culling.getLocation(), culling.getRadius())) {
                            colorPickingDrawActor(gl, glu, camera, actor, DrawingPass.SCENE, i);
                        }
                    } else {
                        colorPickingDrawActor(gl, glu, camera, actor, DrawingPass.SCENE, i);
                    }
                }
                camera.endPerspective(gl, glu);
            }
            timers.setCurrentDrawingPass(null);
        }
        if (camera.getDrawingPass().containsDrawingPass(DrawingPass.INTERFACE_PICKING)) {
            camera.setDirty(false);
            timers.setCurrentDrawingPass(DrawingPass.INTERFACE_PICKING);
            for (int i = 0; i < builder.getNumberOfUserInterfaceDrawingPasses(); i++) {
                // start with a fresh transformation matrix
                gl.glLoadIdentity();
                camera.makeOrtho2D(gl, glu);
                // draw the user interface components
                for (final Actor actor : actors) {
                    colorPickingDrawActor(gl, glu, camera, actor, DrawingPass.INTERFACE, i);
                }
                camera.endOrtho2D(gl, glu);
            }
            timers.setCurrentDrawingPass(null);
        }
    }

    private void drawActor(final GL2 gl, final GLUgl2 glu, final Camera camera, final Actor actor, final DrawingPass drawingPass, final int passNumber, final boolean attribStack) {
        // only draw visible actors
        if (!actor.isVisible()) {
            return;
        }
        // only draw actors during the correct pass
        if (!actor.getDrawingPass().containsDrawingPass(drawingPass)) {
            return;
        }
        // continue for actors with the correct pass number or those with
        // children
        final ChildSupport childSupport = actor.lookup(ChildSupport.class);
        if (passNumber == actor.getPassNumber() || childSupport != null) {
            final int[] attribStackDepths = new int[2];
            if (attribStack) {
                gl.glGetIntegerv(GL2.GL_ATTRIB_STACK_DEPTH, attribStackDepths, 0);
            }
            // check for transformable support
            final TransformSupport transformable = actor.lookup(TransformSupport.class);
            if (transformable != null) {
                // this actor requires transformation
                transformable.pushTransform(gl, glu, drawingPass.containsDrawingPass(DrawingPass.SCENE) ? camera : null);
            }
            // get a temp list of drawable types so that the same set is used for both pre and post operations
            final List<Drawable> actorDrawables;
            if (drawableTypes.isEmpty()) {
                actorDrawables = Collections.emptyList();
            } else {
                actorDrawables = new ArrayList<>(drawableTypes.size());
                for (int i = 0; i < drawableTypes.size(); i++) {
                    final Class<? extends Drawable> type = drawableTypes.get(i);
                    final Drawable drawable = actor.lookup(type);
                    if (drawable != null) {
                        actorDrawables.add(drawable);
                    }
                }
                // set up the actor for drawing
                for (int i = 0; i < actorDrawables.size(); i++) {
                    actorDrawables.get(i).draw(gl, glu, camera);
                }
            }
            // draw the actor
            // if a camera has been set then only draw when that camera is
            // visible and during that camera's actual draw cycle
            final boolean draw = (actor.getPassNumber() == passNumber)
                                 && (actor.getCamera() == null || actor.getCamera() == camera);
            if (draw) {
                // set up some base fields
                gl.glLineWidth(actor.getThickness());
                gl.glPointSize(actor.getThickness());
                if (actor.isWire()) {
                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
                } else {
                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
                }
                actor.setDirty(false);
                actor.draw(gl, glu, camera);
            }
            // look for children that need to be drawn
            if (childSupport != null && childSupport.isInherit()) {
                final Collection<Actor> children = childSupport.getChildren();
                for (final Actor child : children) {
                    drawActor(gl, glu, camera, child, drawingPass, passNumber, attribStack);
                }
            }
            if (draw) {
                actor.endDraw(gl, glu, camera);
            }
            // close out the actor
            // reverse the order of this iteration so push and pop get called in the correct order
            for (int i = actorDrawables.size() - 1; i >= 0; i--) {
                actorDrawables.get(i).endDraw(gl, glu, camera);
            }
            // pop transform matrix
            if (transformable != null) {
                transformable.popTransform(gl, glu);
            }
            // look for children that need to be drawn
            if (childSupport != null && !childSupport.isInherit()) {
                final Collection<Actor> children = childSupport.getChildren();
                for (final Actor child : children) {
                    drawActor(gl, glu, camera, child, drawingPass, passNumber, attribStack);
                }
            }
            if (attribStack) {
                gl.glGetIntegerv(GL2.GL_ATTRIB_STACK_DEPTH, attribStackDepths, 1);
            }
            if (attribStackDepths[0] != attribStackDepths[1] && scene.getSceneBuilder().isVerbose()) {
                logger.log(Level.WARNING, "{0}: The actor {1} or a child of actor is leaking {2} attrib stacks.", new Object[]{scene, actor.toString(),
                                                                                                                               attribStackDepths[1] - attribStackDepths[0]});
            }
        }
    }

    private void colorPickingDrawActor(final GL2 gl, final GLUgl2 glu, final ColorPickingCamera camera, final Actor actor, final DrawingPass drawingPass,
                                       final int passNumber) {
        // only draw visible actors
        if (!actor.isVisible()) {
            return;
        }
        // only draw actors during the correct pass
        if (!actor.getDrawingPass().containsDrawingPass(drawingPass)) {
            return;
        }
        // continue for actors with the correct pass number or those with
        // children
        final ChildSupport childSupport = actor.lookup(ChildSupport.class);
        if (passNumber == actor.getPassNumber() || childSupport != null) {
            // check for transformable support
            final TransformSupport transformable = actor.lookup(TransformSupport.class);
            if (transformable != null) {
                // this actor requires transformation
                // transformables only get a reference to the camera when it's an actual drawing render for the scene pass
                if (!drawingPass.isInterface()) {
                    transformable.pushTransform(gl, glu, null);
                } else {
                    transformable.pushTransformInt(gl, glu, null);
                }
            }
            // do a color picking draw on the actor
            // if a camera has been set then only draw when that camera is
            // visible and during that camera's actual draw cycle
            final boolean draw = actor.getDrawingPass().containsDrawingPass(drawingPass.addDrawingPass(DrawingPass.PICKING))
                                 && (actor.getPassNumber() == passNumber)
                                 && (actor.getCamera() == null || actor.getCamera() == camera.getReferenceCamera());
            if (draw) {
                // set up some base fields
                gl.glLineWidth(actor.getThickness());
                gl.glPointSize(actor.getThickness());
                if (actor.isWire()) {
                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
                } else {
                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
                }
                final ColorPickingSupport support = actor.lookup(ColorPickingSupport.class);
                if (support != null) {
                    support.colorPickingDraw(gl, glu, camera, support);
                }
            }
            // look for children that need to be drawn
            if (childSupport != null && childSupport.isInherit()) {
                final Collection<Actor> children = childSupport.getChildren();
                for (final Actor child : children) {
                    colorPickingDrawActor(gl, glu, camera, child, drawingPass, passNumber);
                }
            }
            // pop transform matrix
            if (transformable != null) {
                if (!drawingPass.isInterface()) {
                    transformable.popTransform(gl, glu);
                } else {
                    transformable.popTransformInt(gl, glu);
                }
            }
            // look for children that need to be drawn
            if (childSupport != null && !childSupport.isInherit()) {
                final Collection<Actor> children = childSupport.getChildren();
                for (final Actor child : children) {
                    colorPickingDrawActor(gl, glu, camera, child, drawingPass, passNumber);
                }
            }
        }
    }

    private void pickActor(final GL2 gl, final GLUgl2 glu, final PickingCamera picking, final Camera camera, final Actor actor) {
        // check for transformable support
        final TransformSupport transformable = actor.lookup(TransformSupport.class);
        if (transformable != null) {
            // this actor requires transformation
            // transformables only get a reference to the camera when it's an actual drawing render for the scene pass
            transformable.pushTransform(gl, glu, null);
        }
        // check for pickable support
        if (picking instanceof ColorPickingCamera) {
            // we don't want to check picking hits on a color picking camera during the gl context
            // this happens on the update thread
        } else if (picking instanceof ItemPickingCamera) {
            final ItemPickingSupport itemPickable = actor.lookup(ItemPickingSupport.class);
            if (itemPickable != null && actor.getCamera() == null || actor.getCamera() == picking.getReferenceCamera()) {
                ((ItemPickingCamera) picking).checkPickingHit(gl, glu, itemPickable);
            }
        } else {
            final PickingSupport pickable = actor.lookup(PickingSupport.class);
            if (pickable != null) {
                // set up some base fields
                gl.glLineWidth(actor.getThickness());
                if (actor.isWire()) {
                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
                } else {
                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
                }
                picking.checkPickingHit(gl, glu, pickable);
            }
        }
        // look for children that need to be checked for a pick
        final ChildSupport childSupport = actor.lookup(ChildSupport.class);
        if (childSupport != null && childSupport.isInherit()) {
            final Collection<Actor> children = childSupport.getChildren();
            for (final Actor child : children) {
                pickActor(gl, glu, picking, camera, child);
            }
        }
        // pop transform matrix
        if (transformable != null) {
            transformable.popTransform(gl, glu);
        }
        // look for children that need to be checked for a pick
        if (childSupport != null && !childSupport.isInherit()) {
            final Collection<Actor> children = childSupport.getChildren();
            for (final Actor child : children) {
                pickActor(gl, glu, picking, camera, child);
            }
        }
    }

    /**
     * Updates all of the updatable objects in the scene. This should not be
     * called when the scene is rendering.
     */
    void performUpdate() {
        if (timers.getAndSetUpdating(true)) {
            return;
        }
        try {
            if (updateTime == 0L) {
                // don't call update for the first update cycle
                updateTime = System.currentTimeMillis();
            } else {
                // update all of the updatable objects
                final long current = System.currentTimeMillis();
                final long delta = current - updateTime;
                // no need to update yet
                if (delta == 0L) {
                    // System.out.println("update not required yet");
                    return;
                }
                updateTime = current;
                // update any extra updatable objects in the lookup
                scene.lookupAll(Updatable.class, collections.getUpdatables());
                for (final Updatable updatable : collections.getUpdatables()) {
                    updatable.update(delta);
                }
                timers.setLastUpdateLength(System.currentTimeMillis() - updateTime);
            }
        } catch (final RejectedExecutionException ex) {
            if (!scene.isDisposed()) {
                logger.log(Level.WARNING, MessageFormat.format("{0}: Runtime Exception during updateGLScene method.", scene), ex);
            }
        } catch (final RuntimeException ex) {
            logger.log(Level.WARNING, MessageFormat.format("{0}: Runtime Exception during updateGLScene method.", scene), ex);
        } finally {
            timers.setUpdating(false);
        }
    }

    /**
     * Initialize OpenGL using the provided builder settings.
     *
     * @param gl  reference to current GL
     * @param glu reference to GLU
     */
    void initialize(final GL2 gl, final GLUgl2 glu) {
        if (timers.getAndSetDrawing(true)) {
            return;
        }
        try {
            // print the JOGL and OpenGL info
            if (scene.getSceneBuilder().isVerbose() || !initLogged.get()) {
                // just print the info once unless verbose
                DebugUtil.logGLInfo(gl);
            }
            // logging
            final StringBuilder sb;
            final String message = MessageFormat.format("{0}: Initializing the OpenGL scene...", scene);
            if (scene.getSceneBuilder().isVerbose() || !initLogged.get()) {
                sb = new StringBuilder(message);
            } else if (scene.getSceneBuilder().isVerbose()) {
                sb = null;
                logger.log(Level.INFO, message);
            } else {
                sb = null;
            }
            // initial setup
            gl.setSwapInterval(1);
            gl.glDisable(GL.GL_SCISSOR_TEST);
            gl.glClearDepth(1.0);
            gl.glEnable(GL.GL_DEPTH_TEST);
            gl.glDepthFunc(GL.GL_LEQUAL);
            gl.glDisable(GL.GL_CULL_FACE);
            // setup shade model
            try {
                gl.glShadeModel(GL2.GL_SMOOTH);
            } catch (final GLException ex) {
                if (sb != null) {
                    sb.append("\nShade model is not available.");
                }
            }
            // setup material
            try {
                gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
                gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
                gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 50.0f);
            } catch (final GLException ex) {
                if (sb != null) {
                    sb.append("\nMaterial is not available.");
                }
            }
            // set up textures
            gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
            try {
                gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
                gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
            } catch (final GLException ex) {
                if (sb != null) {
                    sb.append("\nTexture environment is not available.");
                }
            }
            // setup lighting
            try {
                if (builder.isLighting()) {
                    if (sb != null) {
                        sb.append("\nEnabling lighting...");
                    }
                    gl.glEnable(GL2.GL_LIGHTING);
                    gl.glEnable(GL2.GL_COLOR_MATERIAL);
                    gl.glEnable(GL2.GL_RESCALE_NORMAL);
                    gl.glLightModelf(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);
                    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.1f, 0.1f, 0.1f, 1.0f}, 0);
                } else {
                    gl.glDisable(GL2.GL_LIGHTING);
                    gl.glDisable(GL2.GL_COLOR_MATERIAL);
                    gl.glDisable(GL2.GL_RESCALE_NORMAL);
                }
                gl.glDisable(GL2.GL_AUTO_NORMAL);
            } catch (final GLException ex) {
                if (sb != null) {
                    sb.append("\nLighting is not available.");
                }
            }
            // setup NURBS evaluator
            try {
                gl.glEnable(GL2.GL_MAP1_VERTEX_3);
            } catch (final GLException ex) {
                if (sb != null) {
                    sb.append("\nNURBS is not available.");
                }
            }
            // setup blending
            if (builder.isBlending()) {
                if (sb != null) {
                    sb.append("\nEnabling blending...");
                }
                gl.glEnable(GL.GL_BLEND);
                gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            }
            // enable multi sampling if it is available
            final int[] buffers = new int[1];
            gl.glGetIntegerv(GL.GL_SAMPLE_BUFFERS, buffers, 0);
            final int[] samples = new int[1];
            gl.glGetIntegerv(GL.GL_SAMPLES, samples, 0);
            // try for full screen antialiasing first
            boolean fsaaEnabled = false;
            if (builder.isFullScreenAntiAliasing() && buffers[0] == 1 && samples[0] >= 2) {
                if (sb != null) {
                    sb.append("\nAttempting to enable multi-sampling...");
                }
                gl.glEnable(GL.GL_MULTISAMPLE);
                try {
                    gl.glHint(GL2.GL_MULTISAMPLE_FILTER_HINT_NV, GL.GL_NICEST);
                } catch (final GLException ex) {
                    // ignore
                }
                // check
                final int[] enabled = new int[1];
                gl.glGetIntegerv(GL.GL_MULTISAMPLE, enabled, 0);
                fsaaEnabled = enabled[0] == 1;
            }
            if (fsaaEnabled) {
                // disable point, line, and polygon antialiasing
                if (sb != null) {
                    sb.append(MessageFormat.format("\nEnabling multi-sampling with {0} buffer and {1} samples; Using full screen antialiasing.", buffers[0], samples[0]));
                }
                // disable individual anti aliasing
                gl.glDisable(GL.GL_LINE_SMOOTH);
                try {
                    gl.glDisable(GL2.GL_POINT_SMOOTH);
                    gl.glDisable(GL2.GL_POLYGON_SMOOTH);
                } catch (final GLException ex) {
                    // ignore
                }
            } else {
                // fall back to point and line antialiasing
                if (sb != null) {
                    sb.append(MessageFormat.format("\nUnable to enable multi-sampling with {0} buffers and {1} samples; Reverting to point and line smoothing.", buffers[0], samples[0]));
                }
                // setup anti aliasing
                gl.glEnable(GL.GL_LINE_SMOOTH);
                gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
                try {
                    gl.glEnable(GL2.GL_POINT_SMOOTH);
                    gl.glHint(GL2.GL_POINT_SMOOTH_HINT, GL.GL_NICEST);
                    // polygon antialiasing is problematic so disable it
                    gl.glDisable(GL2.GL_POLYGON_SMOOTH);
                    gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
                } catch (final GLException ex) {
                    if (sb != null) {
                        sb.append("\nPoint smoothing is not available.");
                    }
                }
            }
            // check if color picking will work
            if (builder.isDebugColorPicking()) {
                final int[] colorBits = new int[3];
                gl.glGetIntegerv(GL.GL_RED_BITS, colorBits, 0);
                gl.glGetIntegerv(GL.GL_GREEN_BITS, colorBits, 1);
                gl.glGetIntegerv(GL.GL_BLUE_BITS, colorBits, 2);
                if (sb != null) {
                    if (colorBits[0] < 8) {
                        sb.append(MessageFormat.format("\nColor picking will not work correctly using a display with less than 8 red color bits; "
                                                       + "\nOpenGL is currently using only {0} bits.", colorBits[0]));
                    }
                    if (colorBits[1] < 8) {
                        sb.append(MessageFormat.format("\nColor picking will not work correctly using a display with less than 8 green color bits; "
                                                       + "\nOpenGL is currently using only {0} bits.", colorBits[1]));
                    }
                    if (colorBits[2] < 8) {
                        sb.append(MessageFormat.format("\nColor picking will not work correctly using a display with less than 8 blue color bits; "
                                                       + "\nOpenGL is currently using only {0} bits.", colorBits[2]));
                    }
                }
            }
            // determine if stereo has been enabled
            final byte[] stereo = new byte[]{0};
            try {
                gl.glGetBooleanv(GL2.GL_STEREO, stereo, 0);
            } catch (final GLException ex) {
                if (sb != null) {
                    sb.append("\nStereo is not available.");
                }
            }
            synchronized (this) {
                state = StateUtil.setValue(state, STEREO_MASK, stereo[0] == 1);
            }
            if (sb != null) {
                sb.append(MessageFormat.format("\n{0} stereo rendering", (stereo[0] == 1 ? "Enabling" : "Disabling")));
            }
            // assert (stereo == caps.getStereo());
            // determine if double buffering has been enabled
            final byte[] doubleBuffer = new byte[]{0};
            try {
                gl.glGetBooleanv(GL2.GL_DOUBLEBUFFER, doubleBuffer, 0);
            } catch (final GLException ex) {
                if (sb != null) {
                    sb.append("\nDouble buffer is not available.");
                }
            }
            synchronized (this) {
                state = StateUtil.setValue(state, DOUBLE_BUFFER_MASK, doubleBuffer[0] == 1);
            }
            if (sb != null) {
                sb.append(MessageFormat.format("\n{0} double buffer rendering", (doubleBuffer[0] == 1 ? "Enabling" : "Disabling")));
            }
            // get number of auxiliary buffers
            final int[] auxBuffer = new int[]{0};
            try {
                if (builder.isAuxiliaryBuffers()) {
                    gl.glGetIntegerv(GL2.GL_AUX_BUFFERS, auxBuffer, 0);
                }
            } catch (final GLException ex) {
                if (sb != null) {
                    sb.append("\nAuxilliary buffers are not available.");
                }
            }
            synchronized (this) {
                state = StateUtil.setValue(state, AUX_BUFFER_MASK, auxBuffer[0] >= 4);
            }
            if (sb != null) {
                sb.append(MessageFormat.format("\n{0} auxiliary buffers", auxBuffer[0]));
                sb.append(MessageFormat.format("\n{0} auxiliary buffer rendering", (auxBuffer[0] >= 4 ? "Enabling" : "Disabling")));
            }
            // determine which color picking buffer will be used
            // this logic is repeated in the color picking section of performRender(GL2,GLUgl2)
            if (sb != null) {
                final String buffer;
                if (builder.isDebugColorPicking()) {
                    // debug draws to the visible screen
                    buffer = (doubleBuffer[0] == 1 ? "back" : "front");
                } else if (builder.isTextureColorPicking()) {
                    // the returned buffer will point to a texture buffer
                    buffer = "texture";
                } else if (auxBuffer[0] >= 4) {
                    // utilize an aux buffer
                    buffer = "auxiliary";
                } else if (doubleBuffer[0] == 1) {
                    // double buffered uses the back buffer
                    buffer = "back";
                } else {
                    // single buffered must use a texture buffer
                    buffer = "texture";
                }
                sb.append(MessageFormat.format("\nColor picking rendering occuring on {0} buffer", buffer));
            }
            // check stack sizes
            try {
                synchronized (this) {
                    state = StateUtil.setValue(state, ATTRIB_STACK_MASK, true);
                }
                final int[] attrib = new int[1];
                gl.glGetIntegerv(GL2.GL_MAX_ATTRIB_STACK_DEPTH, attrib, 0);
                if (sb != null) {
                    sb.append(MessageFormat.format("\nPush attrib stack size: {0}", attrib[0]));
                }
            } catch (final GLException ex) {
                synchronized (this) {
                    state = StateUtil.setValue(state, ATTRIB_STACK_MASK, false);
                }
                if (sb != null) {
                    sb.append("\nAttrib stack depth not available.");
                }
            }
            try {
                synchronized (this) {
                    state = StateUtil.setValue(state, MATRIX_STACK_MASK, true);
                }
            } catch (final GLException ex) {
                synchronized (this) {
                    state = StateUtil.setValue(state, MATRIX_STACK_MASK, false);
                }
                if (sb != null) {
                    sb.append("\nMatrix stack depth not available.");
                }
            }
            // log
            if (sb != null) {
                logger.log(Level.INFO, sb.toString());
            }
            // check memory
            if (scene.getSceneBuilder().isVerbose() || !initLogged.get()) {
                MemLogger.logMessage(scene, gl);
            }
            // only log once if not verbose
            initLogged.set(true);
        } finally {
            timers.setDrawing(false);
        }
    }

    /**
     * @param background the background to set
     */
    void setBackground(final Color background) {
        if (background == null) {
            throw new NullPointerException("background");
        }
        final Color old;
        synchronized (this) {
            old = this.background;
            this.background = background;
        }
        scene.getPropertyChangeSupport().firePropertyChange(Scene.SCENE_BACKGROUND, old, background);
    }

    /**
     * @param boundary the boundary to set
     */
    void setBoundary(final Vector3D boundary) {
        if (boundary == null) {
            throw new NullPointerException("boundary");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.boundary;
            this.boundary = boundary;
        }
        scene.getPropertyChangeSupport().firePropertyChange(Scene.BOUNDARY, old, boundary);
    }

    /**
     * @param center the center to set
     */
    void setCenter(final Vector3D center) {
        if (center == null) {
            throw new NullPointerException("center");
        }
        final Vector3D old;
        synchronized (this) {
            old = this.center;
            this.center = center;
        }
        scene.getPropertyChangeSupport().firePropertyChange(Scene.CENTER, old, center);
    }

    /**
     * @param maxInitializations the maxInitializations to set
     */
    void setMaxInitializations(final int maxInitializations) {
        synchronized (this) {
            this.maxInitializations = maxInitializations;
        }
    }

    /**
     * @return true if the scene is currently rendering slowly
     */
    boolean isSlow() {
        final long maxTimeLength = 1000L / builder.getTargetFps();
        return maxTimeLength < timers.getLastDrawLength() + timers.getLastUpdateLength();
    }

    /**
     * @param repaint the repaint to set
     */
    void setRepaint(final DrawingPass repaint) {
        if (repaint == null) {
            throw new NullPointerException("repaint");
        }
        timers.setRepaint(repaint);
    }

    private void clearScreen(final GL2 gl, final Rectangle viewport, final Color color, final int mask, final int... buffers) {
        // define the buffers
        if (buffers.length == 1) {
            // single draw buffer
            gl.glDrawBuffer(buffers[0]);
        } else {
            // multiple draw buffers
            gl.glDrawBuffers(buffers.length, buffers, 0);
        }
        // set up the scissor
        if (viewport != null) {
            gl.glEnable(GL.GL_SCISSOR_TEST);
            gl.glScissor(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
        } else {
            gl.glDisable(GL.GL_SCISSOR_TEST);
        }
        // clear the screen if color is defined
        if (color != null) {
            // set the color
            gl.glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            // clear the screen
            gl.glClear(mask);
        }
    }

    private static class DebugLoadUpdateTaskRunnable implements UpdateTaskRunnable {

        public static final String ID = "fps-actor";
        private final Scene scene;
        private final SceneBuilder builder;

        private DebugLoadUpdateTaskRunnable(final Scene scene, final SceneBuilder builder) {
            this.scene = scene;
            this.builder = builder;
        }

        @Override
        public void disposed(final Task task) {
            // no operation
        }

        @Override
        public boolean run(final Task task) {
            // add a fps and perf logger to the scene
            if (builder.isDebug()) {
                // clear existing ones
                final Set<FpsLogger> existingFpsLogger = scene.lookupAll(FpsLogger.class);
                scene.removeAll(existingFpsLogger);
                final Set<PerfLogger> existingPerfLogger = scene.lookupAll(PerfLogger.class);
                scene.removeAll(existingPerfLogger);
                final Set<MemLogger> existingMemLogger = scene.lookupAll(MemLogger.class);
                scene.removeAll(existingMemLogger);
                // make new ones
                FpsLogger.newInstance(scene, FPS_LOGGER_INTERVAL);
                PerfLogger.newInstance(scene, FPS_LOGGER_INTERVAL);
                MemLogger.newInstance(scene, FPS_LOGGER_INTERVAL);
            }
            // create a fps actor if necessary
            if (builder.isDisplayFps()) {
                // clear existing actor
                final Actor existingFpsActor = scene.getActor(ID);
                if (existingFpsActor != null) {
                    scene.remove(existingFpsActor);
                    existingFpsActor.dispose();
                }
                // make a new one
                final FpsActor actor = new FpsActor(scene, ID);
                actor.setDrawingPass(DrawingPass.OVERLAY);
                actor.setOrigin(Alignment.LEFT_BOTTOM);
                actor.setShape(new Text2D(Tooltip.DEFAULT_FONT, " "));
                actor.setVisible(true);
                actor.setBorderThickness(4.0);
                actor.setBackgroundColor(new Color(Tooltip.DEFAULT_BACKGROUND_COLOR, 0.8f));
                actor.setBorderColor(new Color(Tooltip.DEFAULT_BORDER_COLOR, 0.8f));
                // color
                ColorSupport.newInstance(actor).setColor(new Color(Tooltip.DEFAULT_FONT_COLOR, 0.8f));
                // transform
                TransformSupport.newInstance(actor).setTranslation(new Vector3D(10, 10, 0));
                // add fps actor to the scene
                scene.add(actor);
            }
            return true;
        }
    }
}
