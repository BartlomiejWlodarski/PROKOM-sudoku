package pl.kompo.view;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle
                .getBundle("Languages", new Locale("en"));
        Locale.setDefault(new Locale("en"));

        Parent root = FXMLLoader.load(Objects
                .requireNonNull(getClass()
                        .getResource("menu-view.fxml")), resourceBundle);
        Scene scene = new Scene(root);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.show();
        SceneManager.setStage(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}