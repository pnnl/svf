package gov.pnnl.svf.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Amelia Bleeker
 */
public class FxWindowDemo extends Application {

    @Override
    public void start(final Stage stage) throws Exception {
        final Parent root = FXMLLoader.load(FxWindowDemo.class.getResource("FxWindowDemo.fxml"));

        final Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Scientific Visualization Framework JavaFx Window Demo");
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
