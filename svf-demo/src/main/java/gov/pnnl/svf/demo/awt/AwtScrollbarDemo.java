package gov.pnnl.svf.demo.awt;

import gov.pnnl.svf.actor.ScrollbarActor;
import gov.pnnl.svf.animation.RotationAnimationSupport;
import gov.pnnl.svf.awt.actor.AwtScrollbarActor;
import gov.pnnl.svf.awt.scene.AwtScene;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.SimpleCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.demo.Demo;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ItemPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.scene.AbstractScene;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.MemLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import org.apache.commons.math.geometry.Vector3D;

/**
 *
 * @author Arthur Bleeker
 */
public class AwtScrollbarDemo extends AbstractAwtDemo {

    /**
     * This main method must be copied into the final class for reflection to
     * work correctly.
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        boolean debug = false;
        for (final String string : args) {
            if ("debug".equals(string)) {
                debug = true;
            }
        }
        final Class<?> current = Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());
        final Constructor<?> constructor = current.getConstructor((Class<?>[]) null);
        final Demo<?, ?> demo = (Demo<?, ?>) constructor.newInstance((Object[]) null);
        demo.runDemo(debug);
    }

    @Override
    public AbstractScene<GLCanvas> createScene(final JFrame window, final SceneBuilder builder) {
        final GLCanvas component = new GLCanvas(builder.getGLCapabilities());
        final AwtScene scene = new AwtScene(component, builder) {
            @Override
            public void load() {
                // create a camera
                final Camera camera = new SimpleCamera(this);
                camera.setLocation(new Vector3D(1.0, 0.0, 15.0));
                ColorSupport.newInstance(camera).setColor(Color.BLACK);
                add(camera);
                // create a picking camera
                final PickingCamera picking = getFactory().createPickingCamera(this, camera, PickingCamera.class);
                add(picking);
                // create an item picking camera
                final ItemPickingCamera itemPicking = getFactory().createPickingCamera(this, camera, ItemPickingCamera.class);
                add(itemPicking);
                // create a color picking camera
                final ColorPickingCamera colorPicking = getFactory().createPickingCamera(this, camera, ColorPickingCamera.class);
                add(colorPicking);
                // log the fps and vps
                if (!getSceneBuilder().isDebug()) {
                    FpsLogger.newInstance(this, 1000 * 10);
                    PerfLogger.newInstance(this, 1000 * 10);
                    MemLogger.newInstance(this, 1000 * 10);
                }
                // add a vertical scrollbar
                final ScrollbarActor vertical = createVerticalScrollbar(this);
                add(vertical);
                // add a horizontal scrollbar
                final ScrollbarActor horizontal = createHorizontalScrollbar(this);
                add(horizontal);
                // add a rotated scrollbar
                final ScrollbarActor rotated = createRotatedScrollbar(this);
                add(rotated);
            }

        };
        return scene;
    }

    private ScrollbarActor createVerticalScrollbar(final Scene scene) {
        final ScrollbarActor scrollbar = new AwtScrollbarActor(scene, "vertical-scrollbar", scene.getFactory().newUuid(scene));
        scrollbar.setDrawingPass(DrawingPass.INTERFACE_PICKING);
        scrollbar.setScrollUnit(10.0);
        scrollbar.setMinimum(0.0);
        scrollbar.setMaximum(10000);
        scrollbar.setWidth(20.0);
        ColorPickingSupport.newInstance(scrollbar)
                .newMapping(scrollbar.getKnob())
                .addListener(scrollbar);
        final TransformSupport scrollbarTransform = TransformSupport.newInstance(scrollbar)
                .setRotationAxis(Vector3D.MINUS_K)
                .setRotation(180.0);
        final PropertyChangeListener scrollbarListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final Rectangle viewport = scene.getViewport();
                scrollbarTransform.setTranslation(new Vector3D(viewport.getWidth() - 10.0, (viewport.getHeight() * 0.5) + 10.0, 0.0));
                scrollbar.setExtent(viewport.getHeight() - 20.0);
            }
        };
        scene.getPropertyChangeSupport().addPropertyChangeListener(Scene.VIEWPORT, scrollbarListener);
        scrollbarListener.propertyChange(null);
        return scrollbar;
    }

    private ScrollbarActor createHorizontalScrollbar(final Scene scene) {
        final ScrollbarActor scrollbar = new AwtScrollbarActor(scene, "horizontal-scrollbar", scene.getFactory().newUuid(scene));
        scrollbar.setDrawingPass(DrawingPass.INTERFACE_PICKING);
        scrollbar.setScrollUnit(10.0);
        scrollbar.setMinimum(0.0);
        scrollbar.setMaximum(10000);
        scrollbar.setWidth(20.0);
        ColorPickingSupport.newInstance(scrollbar)
                .newMapping(scrollbar.getKnob())
                .addListener(scrollbar);
        final TransformSupport scrollbarTransform = TransformSupport.newInstance(scrollbar)
                .setRotationAxis(Vector3D.PLUS_K)
                .setRotation(90.0);
        final PropertyChangeListener scrollbarListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final Rectangle viewport = scene.getViewport();
                scrollbarTransform.setTranslation(new Vector3D((viewport.getWidth() * 0.5) - 10.0, 10.0, 0.0));
                scrollbar.setExtent(viewport.getWidth() - 20.0);
            }
        };
        scene.getPropertyChangeSupport().addPropertyChangeListener(Scene.VIEWPORT, scrollbarListener);
        scrollbarListener.propertyChange(null);
        return scrollbar;
    }

    private ScrollbarActor createRotatedScrollbar(final Scene scene) {
        final ScrollbarActor scrollbar = new AwtScrollbarActor(scene, "rotated-scrollbar", scene.getFactory().newUuid(scene));
        scrollbar.setDrawingPass(DrawingPass.INTERFACE_PICKING);
        scrollbar.setScrollUnit(10.0);
        scrollbar.setMinimum(0.0);
        scrollbar.setMaximum(10000);
        scrollbar.setWidth(20.0);
        ColorPickingSupport.newInstance(scrollbar)
                .newMapping(scrollbar.getKnob())
                .addListener(scrollbar);
        final TransformSupport scrollbarTransform = TransformSupport.newInstance(scrollbar)
                .setRotationAxis(Vector3D.PLUS_K)
                .setRotation(0.0);
        final PropertyChangeListener scrollbarListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final Rectangle viewport = scene.getViewport();
                scrollbarTransform.setTranslation(new Vector3D(viewport.getWidth() * 0.5, viewport.getHeight() * 0.5, 0.0));
                scrollbar.setExtent(Math.min(viewport.getWidth(), viewport.getHeight()));
            }
        };
        scene.getPropertyChangeSupport().addPropertyChangeListener(Scene.VIEWPORT, scrollbarListener);
        scrollbarListener.propertyChange(null);
        RotationAnimationSupport.newInstance(scrollbar, 60L * 1000L, 0L, true, 360.0);
        return scrollbar;
    }
}
