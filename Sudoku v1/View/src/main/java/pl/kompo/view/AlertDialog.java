package pl.kompo.view;

import javafx.scene.control.Alert;

public class AlertDialog {
    public static void show(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
