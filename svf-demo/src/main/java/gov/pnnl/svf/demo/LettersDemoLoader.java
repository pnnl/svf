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
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Demo loader for displaying the alphabet in a scene.
 *
 * @author Arthur Bleeker
 */
public class LettersDemoLoader implements DemoLoader {

    private static final int MAX_FONT_FAMILIES = 40;
    private static final List<Font> FONTS;

    static {
        final Set<String> duplicate = new HashSet<>();
        final List<Font> fonts = new ArrayList<>();
        for (final String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.US)) {
            if (fonts.size() >= MAX_FONT_FAMILIES) {
                break;
            }
            if (!duplicate.add(name.split(" ")[0])) {
                fonts.add(new Font(name, Font.PLAIN, 10));
                fonts.add(new Font(name, Font.BOLD, 48));
                fonts.add(new Font(name, Font.ITALIC, 14));
                fonts.add(new Font(name, Font.PLAIN, 32));
                fonts.add(new Font(name, Font.BOLD | Font.ITALIC, 24));
                fonts.add(new Font(name, Font.PLAIN, 18));
                fonts.add(new Font(name, Font.PLAIN, 128));
            }
        }
        FONTS = Collections.unmodifiableList(fonts);
    }

    /**
     * Constructor
     */
    public LettersDemoLoader() {

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
        double y = 0.0;
        for (final Font font : FONTS) {
            final ShapeActor text = new ShapeActor(scene);
            text.setOrigin(Alignment.CENTER);
            text.setDrawingPass(DrawingPass.SCENE);
            text.setPassNumber(0);
            final Text3D shape = new Text3D(font, MessageFormat.format("{0} [{1}|{2}]: ABCDEFGHIJKLMOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 0123456789", font.getName(), getStyle(font.getStyle()), font.getSize()));
            text.setShape(shape);
            ColorSupport.newInstance(text).setColor(ColorUtil.createRandomColor(0.2f, 0.8f));//new Color(1.0f, 1.0f, 1.0f, 0.3f));
            final Vector3D scale;
            if (font.getSize() > 50) {
                scale = new Vector3D(50.0 / font.getSize(), 50.0 / font.getSize(), 1.0);
            } else {
                scale = new Vector3D(1.0, 1.0, 1.0);
            }
            final TransformSupport transform = TransformSupport.newInstance(text)
                    .setTranslation(new Vector3D(0.0, y, 0.0))
                    .setScale(scale);
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
            y -= 0.25;
        }
    }

    private static final String getStyle(final int style) {
        switch (style) {
            case 3:
                return "bi";
            case 2:
                return "i";
            case 1:
                return "b";
            case 0:
                return "p";
            default:
                return "";
        }
    }
}
