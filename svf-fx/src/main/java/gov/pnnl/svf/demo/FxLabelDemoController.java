package gov.pnnl.svf.demo;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.util.ConfigUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Arthur Bleeker
 */
public class FxLabelDemoController implements Initializable {

    private static final Logger logger = Logger.getLogger(FxLabelDemoController.class.getName());
    /**
     * Demo window height.
     */
    private static final int WINDOW_SIZE_HEIGHT = 800;
    /**
     * Demo window width.
     */
    private static final int WINDOW_SIZE_WIDTH = 1200;
    @FXML
    private AnchorPane root;
    @FXML
    private FxLabelDemoScene scene;

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        // look for opengl.config
        final File file = new File("opengl.config");
        System.out.println(file.getAbsolutePath());
        Properties opengl = null;
        if (file.isFile()) {
            try {
                opengl = ConfigUtil.loadProperties(file, null);
                logger.log(Level.INFO, "Loaded custom OpenGL config.");
            } catch (final IOException ex) {
                logger.log(Level.WARNING, "Unable to load OpenGL config.", ex);
            }
        }
        final SceneBuilder builder = ConfigUtil.configure(null, opengl, null);
        builder.setTargetFps(60)
                .setBackground(Color.BLACK)
                .setNumberOfSceneDrawingPasses(2)
                .setDebug(true)
                .setDisplayFps(true)
                .setVerbose(false);
        scene.construct(builder);
        scene.setWidth(WINDOW_SIZE_WIDTH);
        scene.setHeight(WINDOW_SIZE_HEIGHT);
        root.widthProperty().addListener((final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) -> {
            scene.setWidth((Double) newValue);
        });
        root.heightProperty().addListener((final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) -> {
            scene.setHeight((Double) newValue);
        });
        // initialize and start the demo scene
        scene.initialize();
        scene.start();
        final Thread thread = new Thread(scene::load);
        thread.start();
    }
}
