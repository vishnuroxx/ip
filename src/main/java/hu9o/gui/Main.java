package hu9o.gui;

import java.io.IOException;

import hu9o.Hu9o;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The JavaFX entry point: loads {@code MainWindow.fxml}, hands the controller a
 * {@link Hu9o} back end, and shows the primary window.
 */
public class Main extends Application {
    private final Hu9o hu9o = Hu9o.createForGui();

    /**
     * Builds the scene from FXML, injects the {@link Hu9o} back end into the
     * controller, and shows the window.
     *
     * @param stage the primary stage supplied by the JavaFX runtime.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Hu9o");
            stage.setMinHeight(600.0);
            stage.setMinWidth(400.0);
            MainWindow controller = fxmlLoader.getController();
            controller.setHu9o(hu9o);
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
