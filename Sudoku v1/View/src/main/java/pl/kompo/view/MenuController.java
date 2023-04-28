package pl.kompo.view;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuController implements Initializable {

    @FXML
    public Button startGameBtn;
    @FXML
    public Button showAuthorsBtn;
    @FXML
    private ComboBox<String> selectDifficultyCombo;
    @FXML
    private ComboBox<String> selectLanguageCombo;

    final Logger logger = LoggerFactory.getLogger(MenuController.class);
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("Languages");

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        SceneManager.setDifficulty(Difficulty.EASY);
        selectDifficultyCombo.getItems().addAll(
                resourceBundle.getString("lvl_easy"),
                resourceBundle.getString("lvl_medium"),
                resourceBundle.getString("lvl_hard"));
        selectLanguageCombo.getItems().addAll(
                resourceBundle.getString("lang_en"),
                resourceBundle.getString("lang_pl"));
        selectLanguageCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals(resourceBundle.getString("lang_pl"))) {
                    Locale.setDefault(new Locale("pl"));
                } else if (newValue.equals(resourceBundle.getString("lang_en"))) {
                    Locale.setDefault(new Locale("en"));
                } else {
                    logger.error(resourceBundle.getString("err_wrong_lang"));
                    throw new ViewException(resourceBundle.getString("wrong_lang"));
                }
                resourceBundle = ResourceBundle.getBundle("Languages");
                try {
                    SceneManager.switchScene("menu-view.fxml", resourceBundle);
                } catch (IOException e) {
                    throw new ViewException(resourceBundle.getString("refresh_lang_fail"), e);
                }
            }
        });
    }

    @FXML
    protected void onStartGameButtonClick() {
        String level = selectDifficultyCombo.getSelectionModel().getSelectedItem();

        Difficulty difficulty;
        if (level == null) {
            difficulty = Difficulty.EASY;
        } else {
            if (level.equals(resourceBundle.getString("lvl_easy"))) {
                difficulty = Difficulty.EASY;
            } else if (level.equals(resourceBundle.getString("lvl_medium"))) {
                difficulty = Difficulty.MEDIUM;
            } else if (level.equals(resourceBundle.getString("lvl_hard"))) {
                difficulty = Difficulty.HARD;
            } else {
                difficulty = Difficulty.EASY;
            }
        }
        SceneManager.setDifficulty(difficulty);

        logger.debug(resourceBundle.getString("sel_diff") + ": {} - {}",
                level, difficulty.getFieldsToRemove());
        try {
            SceneManager.switchScene("board-view.fxml", resourceBundle);
        } catch (IOException e) {
            throw new ViewException(resourceBundle.getString("refresh_lang_fail"), e);
        }
    }

    @FXML
    protected void onShowAuthorsButtonClick() {
        Authors authors = new Authors();
        AlertDialog.show(resourceBundle.getString("alert_authors_title"),
                authors.getString("a1")
                + "\n" + authors.getString("a2"), Alert.AlertType.INFORMATION);
    }
}