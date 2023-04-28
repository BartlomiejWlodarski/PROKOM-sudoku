package pl.kompo.view;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage stage;
    private static Difficulty difficulty;

    public static void setDifficulty(Difficulty difficulty) {
        SceneManager.difficulty = difficulty;
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
    }

    public static void switchScene(String filePath, ResourceBundle bundle) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Languages");
        Scene scene = new Scene(FXMLLoader
                .load(Objects.requireNonNull(SceneManager.class.getResource(filePath)), bundle));
        stage.setScene(scene);
    }

    public static void switchScene(String filePath, ResourceBundle bundle, Locale locale) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Languages", locale);
        Scene scene = new Scene(FXMLLoader
                .load(Objects.requireNonNull(SceneManager.class.getResource(filePath)), bundle));
        stage.setScene(scene);
    }

    private static Parent loadFxml(String fxml, ResourceBundle bundle) throws IOException {
        return new FXMLLoader(SceneManager.class.getResource(fxml), bundle).load();
    }
}