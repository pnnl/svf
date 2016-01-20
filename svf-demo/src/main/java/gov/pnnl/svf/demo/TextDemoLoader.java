package gov.pnnl.svf.demo;

import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.util.ColorUtil;
import gov.pnnl.svf.geometry.Text3D;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.PickingCamera;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.CullingSupport;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.util.FpsLogger;
import gov.pnnl.svf.util.MemLogger;
import gov.pnnl.svf.util.PerfLogger;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Demo loader for displaying a lot of text in a scene.
 *
 * @author Arthur Bleeker
 */
public class TextDemoLoader implements DemoLoader {

    private static final int TEXT_COUNT = 1000;
    private static final Random random = new Random();
    private static final Font FONT = new Font("Arial", Font.PLAIN, 36);

    /**
     * Constructor
     */
    public TextDemoLoader() {
    }

    @Override
    public void load(final Scene scene) {
        // create a camera
        final Camera camera = scene.getFactory().createCamera(scene, DraggingCamera.class);
        camera.setLocation(new Vector3D(0.0, 0.0, 15.0));
        ColorSupport.newInstance(camera).setColor(Color.BLACK);
        scene.add(camera);
        // create a picking camera
        final PickingCamera picking = scene.getFactory().createPickingCamera(scene, camera, ColorPickingCamera.class);
        scene.add(picking);
        // log the fps and vps
        if (!scene.getExtended().getSceneBuilder().isDebug()) {
            FpsLogger.newInstance(scene, 1000 * 10);
            PerfLogger.newInstance(scene, 1000 * 10);
            MemLogger.newInstance(scene, 1000 * 10);
        }
        scene.setBoundary(new Vector3D(40.0, 40.0, 40.0));
        // text actors
        for (int i = 0; i < TEXT_COUNT; i++) {
            final ShapeActor text = new ShapeActor(scene);
            text.setOrigin(Alignment.CENTER);
            text.setDrawingPass(DrawingPass.SCENE);
            text.setPassNumber(0);
            final Text3D shape = new Text3D(random.nextDouble() * 2.0 - 1.0, random.nextDouble() * 2.0 - 1.0, random.nextDouble() * 2.0 - 1.0, FONT,
                                            " Text Actor ");
            text.setShape(shape);
            ColorSupport.newInstance(text).setColor(ColorUtil.createRandomColor(0.2f, 0.8f));//new Color(1.0f, 1.0f, 1.0f, 0.3f));
            final double scale = random.nextDouble() * 0.2 + 0.05;
            final TransformSupport transform = TransformSupport.newInstance(text)
                    .setTranslation(new Vector3D(random.nextDouble() * 10.0 - 5.0, random.nextDouble() * 10.0 - 5.0, random.nextDouble() * 10.0 - 5.0))
                    .setScale(new Vector3D(scale, scale, 1.0));
            final CullingSupport culling = CullingSupport.newInstance(text, transform);
            culling.setScale(Math.max(shape.getWidth(), shape.getHeight()));
            text.getPropertyChangeSupport().addPropertyChangeListener(ShapeActor.SHAPE, new PropertyChangeListener() {
                                                                  @Override
                                                                  public void propertyChange(final PropertyChangeEvent evt) {
                                                                      final Text3D shape = (Text3D) evt.getNewValue();
                                                                      culling.setScale(Math.max(shape.getWidth(), shape.getHeight()) / 2.0);
                                                                  }
                                                              });
            scene.add(text);
        }
    }
}
