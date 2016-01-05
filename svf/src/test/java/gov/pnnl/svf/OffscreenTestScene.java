package gov.pnnl.svf;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.picking.ColorPickingUtils;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.OffscreenScene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.scene.SceneExt;
import gov.pnnl.svf.scene.SceneFactory;
import gov.pnnl.svf.scene.SceneMetrics;
import gov.pnnl.svf.scene.Screenshot;
import gov.pnnl.svf.scene.Tooltip;
import gov.pnnl.svf.update.TaskManager;
import gov.pnnl.svf.util.ConfigUtil;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Offscreen scene implementation used for testing.
 *
 * @author Arthur Bleeker
 */
public class OffscreenTestScene implements SceneExt {

    private final int width;
    private final int height;
    private SceneExt scene;

    public OffscreenTestScene() {
        this(800, 600);
    }

    public OffscreenTestScene(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @return the scene
     */
    public SceneExt getScene() {
        return scene;
    }

    /**
     * Set up for testing
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        final SceneBuilder sceneBuilder = ConfigUtil.configure();
        sceneBuilder.setAuxiliaryBuffers(false)
                .setDebug(false)
                .setFullScreenAntiAliasing(false)
                .setLighting(false)
                .setTextureColorPicking(false);
        final GLOffscreenAutoDrawable component = GLDrawableFactory
                .getFactory(sceneBuilder.getGLCapabilities().getGLProfile())
                .createOffscreenAutoDrawable(null, sceneBuilder.getGLCapabilities(), null, width, height);
        scene = OffscreenScene.newInstance(component, sceneBuilder);
        scene.start();
        // wait for load
        scene.waitForLoad(5000L);
    }

    /**
     * Clean up after testing
     *
     * @throws Exception
     */
    public void tearDown() throws Exception {
        if (scene != null) {
            scene.dispose();
        }
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getType() {
        return "test-scene";
    }

    @Override
    public GL getGL() {
        return scene.getGL();
    }

    @Override
    public GLU getGLU() {
        return scene.getGLU();
    }

    @Override
    public GLUT getGLUT() {
        return scene.getGLUT();
    }

    @Override
    public GLProfile getGLProfile() {
        return scene.getGLProfile();
    }

    @Override
    public Collection<Actor> getVisibleRootActors() {
        return scene.getVisibleRootActors();
    }

    @Override
    public void getVisibleRootActors(final Collection<Actor> clctn) {
        scene.getVisibleRootActors(clctn);
    }

    @Override
    public ColorPickingUtils getColorPickingUtils() {
        return scene.getColorPickingUtils();
    }

    @Override
    public SceneBuilder getSceneBuilder() {
        return scene.getSceneBuilder();
    }

    @Override
    public Set<Class<? extends Drawable>> getDrawableTypes() {
        return scene.getDrawableTypes();
    }

    @Override
    public GLAutoDrawable getComponent() {
        return scene.getComponent();
    }

    @Override
    public SceneFactory getFactory() {
        return scene.getFactory();
    }

    @Override
    public void addListeners(final Object o) {
        scene.addListeners(o);
    }

    @Override
    public void draw() {
        scene.draw();
    }

    @Override
    public void draw(final DrawingPass drawingPass) {
        scene.draw(drawingPass);
    }

    @Override
    public DrawingPass getDirtied() {
        return scene.getDirtied();
    }

    @Override
    public Vector3D getBoundary() {
        return scene.getBoundary();
    }

    @Override
    public Vector3D getCenter() {
        return scene.getCenter();
    }

    @Override
    public int getMaxInitializations() {
        return scene.getMaxInitializations();
    }

    @Override
    public Rectangle getViewport() {
        return scene.getViewport();
    }

    @Override
    public Color getSceneBackground() {
        return scene.getSceneBackground();
    }

    @Override
    public TaskManager getDefaultTaskManager() {
        return scene.getDefaultTaskManager();
    }

    @Override
    public void initialize(final GL2 gl2) {
        scene.initialize(gl2);
    }

    @Override
    public boolean isDisposed() {
        return scene.isDisposed();
    }

    @Override
    public boolean isLoaded() {
        return scene.isLoaded();
    }

    @Override
    public void removeListeners(final Object o) {
        scene.removeListeners(o);
    }

    @Override
    public void setBoundary(final Vector3D vd) {
        scene.setBoundary(vd);
    }

    @Override
    public void setCenter(final Vector3D vd) {
        scene.setCenter(vd);
    }

    @Override
    public void setMaxInitializations(final int i) {
        scene.setMaxInitializations(i);
    }

    @Override
    public void setSceneBackground(final Color color) {
        scene.setSceneBackground(color);
    }

    @Override
    public void start() {
        scene.start();
    }

    @Override
    public void stop() {
        scene.stop();
    }

    @Override
    public DrawingPass getCurrentDrawingPass() {
        return scene.getCurrentDrawingPass();
    }

    @Override
    public Tooltip getTooltip() {
        return scene.getTooltip();
    }

    @Override
    public Screenshot getScreenshot() {
        return scene.getScreenshot();
    }

    @Override
    public SceneExt getExtended() {
        return scene.getExtended();
    }

    @Override
    public Actor getActor(final String string) {
        return scene.getActor(string);
    }

    @Override
    public <T extends Actor> T getActor(final Class<T> type, final String string, final String string1, final Object o) {
        return scene.getActor(type, string, string1, o);
    }

    @Override
    public <T> void add(final T t) {
        scene.add(t);
    }

    @Override
    public <T> void addAll(final Collection<T> clctn) {
        scene.addAll(clctn);
    }

    @Override
    public <T> boolean removeAll(final Collection<T> clctn) {
        return scene.removeAll(clctn);
    }

    @Override
    public <T> Set<T> lookupAll(final Class<T> type) {
        return scene.lookupAll(type);
    }

    @Override
    public <T> void lookupAll(final Class<T> type, final Collection<T> clctn) {
        scene.lookupAll(type, clctn);
    }

    @Override
    public void dispose() {
        scene.dispose();
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return scene.getPropertyChangeSupport();
    }

    @Override
    public SceneMetrics getSceneMetrics() {
        return scene.getSceneMetrics();
    }

    @Override
    public <T> T lookup(final Class<? extends T> type) {
        return scene.lookup(type);
    }

    @Override
    public <T> boolean remove(final T t) {
        return scene.remove(t);
    }

    @Override
    public Set<Object> lookupAll() {
        return scene.lookupAll();
    }

    @Override
    public void lookupAll(final Collection<Object> clctn) {
        scene.lookupAll(clctn);
    }

    @Override
    public void clear() {
        scene.clear();
    }

    @Override
    public boolean waitForLoad(final long timeout) {
        return scene.waitForLoad(timeout);
    }
}
