package gov.pnnl.svf.demo;

import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.camera.OrbitCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Demo loader for multiple cameras in a scene.
 *
 * @author Amelia Bleeker
 */
public class CameraDemoLoader extends ShapeDemoLoader implements DemoLoader {

    /**
     * Constructor
     */
    public CameraDemoLoader() {
    }

    @Override
    public void load(final Scene scene) {
        super.load(scene);
        // find the first camera that was previously created
        final DraggingCamera first = scene.lookup(DraggingCamera.class);
        first.setZoomMin(5.0f);
        first.setZoomMax(20.0f);
        // create a second camera
        final OrbitCamera second = scene.getFactory().createCamera(scene, OrbitCamera.class);
        second.setLocation(new Vector3D(1.0, 0.0, 15.0));
        ColorSupport.newInstance(second).setColor(new Color("#ffffff"));
        scene.add(second);
        // create a third camera
        final DraggingCamera third = scene.getFactory().createCamera(scene, DraggingCamera.class);
//        third.setDragMultiplier(0.01f);
        third.setNearClip(0.001);
        third.setZoomMin(0.01f);
        third.setZoomMax(5.0f);
        third.setLocation(new Vector3D(1.0, 0.0, 1.0));
        ColorSupport.newInstance(third).setColor(new Color("#dddddd"));
        scene.add(third);
        // property change to update camera viewports
        final PropertyChangeListener viewportListener = (final PropertyChangeEvent evt) -> {
            final Rectangle viewport = (Rectangle) evt.getNewValue();
            first.setViewport(Rectangle.Builder.construct()
                    .x(viewport.getWidth() / 2)
                    .width(viewport.getWidth() / 2)
                    .height(viewport.getHeight())
                    .build());
            second.setViewport(Rectangle.Builder.construct()
                    .y(viewport.getHeight() / 2)
                    .width(viewport.getWidth() / 2)
                    .height(viewport.getHeight() / 2)
                    .build());
            third.setViewport(Rectangle.Builder.construct()
                    .width(viewport.getWidth() / 2)
                    .height(viewport.getHeight() / 2)
                    .build());
        };
        scene.getPropertyChangeSupport().addPropertyChangeListener(Scene.VIEWPORT, viewportListener);
        viewportListener.propertyChange(new PropertyChangeEvent(scene, Scene.VIEWPORT, null, scene.getViewport()));
    }

}
