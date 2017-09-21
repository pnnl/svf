package gov.pnnl.svf.demo;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.camera.DrivingCamera;
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
 * @author Arthur Bleeker
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
        final Camera first = scene.lookup(DraggingCamera.class);
        // create a second camera
        final Camera second = scene.getFactory().createCamera(scene, OrbitCamera.class);
        second.setLocation(new Vector3D(1.0, 0.0, 15.0));
        ColorSupport.newInstance(second).setColor(new Color("#ffffff"));
        scene.add(second);
        // create a third camera
        final Camera third = scene.getFactory().createCamera(scene, DrivingCamera.class);
        third.setLocation(new Vector3D(1.0, 0.0, 15.0));
        ColorSupport.newInstance(third).setColor(new Color("#dddddd"));
        scene.add(third);
        // property change to update camera viewports
        final PropertyChangeListener viewportListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
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
            }
        };
        scene.getPropertyChangeSupport().addPropertyChangeListener(Scene.VIEWPORT, viewportListener);
        viewportListener.propertyChange(new PropertyChangeEvent(scene, Scene.VIEWPORT, null, scene.getViewport()));
    }

}
