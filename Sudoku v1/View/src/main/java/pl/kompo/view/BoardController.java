package pl.kompo.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kompo.model.BacktrackingSudokuSolver;
import pl.kompo.model.dao.Dao;
import pl.kompo.model.SudokuBoard;
import pl.kompo.model.dao.SudokuBoardDaoFactory;
import pl.kompo.model.exceptions.FileException;

public class BoardController implements Initializable {
    @FXML
    public GridPane boardGrid;
    @FXML
    public Button returnToMenuBtn;
    @FXML
    public Button solveSudokuBtn;

    @FXML
    public Button checkBoardBtn;

    @FXML
    public Button saveToFileBtn;

    @FXML
    public Button readFromFileBtn;

    @FXML
    public TextField sudokuNameTxt;

    private SudokuBoard sudokuBoard;
    private SudokuBoard solvedSudokuBoard;

    final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("Languages");

    private final FileChooser fileChooser = new FileChooser();
    private final SudokuBoardDaoFactory daoFactory = new SudokuBoardDaoFactory();

    //private ObjectProperty<SudokuBoard> sudokuBoardProperty;

    private final StringConverter converter = new SudokuStringConverter();
    private JavaBeanIntegerProperty[][] SudokuFieldProperty = new JavaBeanIntegerProperty[9][9];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BoardGenerator boardGenerator = new BoardGenerator(SceneManager.getDifficulty());
        sudokuBoard = boardGenerator.getBoard();
        solvedSudokuBoard = sudokuBoard.clone();
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(solvedSudokuBoard);

        //sudokuBoardProperty = new SimpleObjectProperty<>(sudokuBoard);

        logger.info(sudokuBoard.toString());
        logger.info(solvedSudokuBoard.toString());

        try {
            fillBoard(sudokuBoard);
        } catch (PropertyException e) {
            try {
                SceneManager.switchScene("menu-view.fxml", resourceBundle);
            } catch (IOException ex) {
                throw new ViewException();
            }
        }
        //boardGrid.getChildren().get(0).requestFocus();
    }

    private void fillBoard(SudokuBoard sudokuBoard) throws PropertyException {
        boolean firstSelected = false;
        boardGrid.getChildren().clear();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuTextField textField = new SudokuTextField();
                if (sudokuBoard.get(i, j) != 0) {
                    textField.setDisable(true);
                }
                try {
                    SudokuFieldProperty[i][j] = JavaBeanIntegerPropertyBuilder.create()
                            .bean(new SudokuBoardPropertyAdapter(sudokuBoard, i, j))
                            .name("Field")
                            .build();
                } catch (NoSuchMethodException e) {
                    throw new PropertyException();
                }
                textField.textProperty().bindBidirectional(SudokuFieldProperty[i][j], converter);
                textField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                                        String previous, String current) {
                        if (!current.matches("[1-9]") && !current.equals("")) {
                            textField.setText(previous);
                        }
                    }
                });
                if (!firstSelected && sudokuBoard.get(i, j) == 0) {
                    textField.requestFocus();
                    firstSelected = true;
                }
                boardGrid.add(textField, j, i);
            }
        }
    }

    private boolean hasZero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudokuBoard.get(i, j) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @FXML
    public void onCheckBoardButtonClick() {
        boolean win = sudokuBoard.checkBoard();

        if (win && !hasZero()) {
            logger.info("win");
            AlertDialog.show(resourceBundle.getString("alert_win_title"),
                    resourceBundle.getString("alert_win_message"),
                    Alert.AlertType.INFORMATION);
        } else {
            AlertDialog.show(resourceBundle.getString("alert_lose_title"),
                    resourceBundle.getString("alert_lose_message"),
                    Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void onSaveToFile() {
        fileChooser.setTitle(resourceBundle.getString("save_board_dialog_title"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialFileName("sudoku.txt");
        File parentDir = new File("Boards");
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        fileChooser.setInitialDirectory(parentDir);
        File file = fileChooser.showSaveDialog(saveToFileBtn.getScene().getWindow());
        if (file != null) {
            logger.debug(resourceBundle.getString("saving_to_file") + file.getAbsolutePath());
            try (Dao<SudokuBoard> dao = daoFactory.getFileDao(Path.of(file.getPath()))) {
                dao.write(sudokuBoard);
            } catch (Exception e) {
                throw new FileException(resourceBundle
                        .getString("failed_to_save_board_file"), e);
            }
        } else {
            logger.debug(resourceBundle.getString("failed_to_save_board_file"));
        }
    }

    @FXML
    public void onReadFromFile() {
        fileChooser.setTitle(resourceBundle.getString("open_board_dialog_title"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File parentDir = new File("Boards");
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        fileChooser.setInitialDirectory(parentDir);
        File file = fileChooser.showOpenDialog(saveToFileBtn.getScene().getWindow());
        if (file != null) {
            logger.debug(resourceBundle.getString("reading_from_file") + file.getAbsolutePath());
            try (Dao<SudokuBoard> dao = daoFactory.getFileDao(Path.of(file.getPath()))) {
                sudokuBoard = dao.read();
                //sudokuBoardProperty = new SimpleObjectProperty<>(sudokuBoard);
                fillBoard(sudokuBoard);
            } catch (Exception e) {
                throw new FileException(resourceBundle.getString("failed_to_open_board_file"), e);
            }
            logger.debug(sudokuBoard.toString());
            //logger.debug(sudokuBoardProperty.toString());
        } else {
            logger.debug(resourceBundle.getString("failed_to_open_board_file"));
        }
    }

    @FXML
    public void onReturnToMenuButtonClick() throws ViewException {
        try {
            SceneManager.switchScene("menu-view.fxml", resourceBundle);
        } catch (IOException e) {
            throw new ViewException(resourceBundle.getString("board_view_fail"), e);
        }
    }

    @FXML
    public void onSolveSudokuButtonClick() {
        boardGrid.getChildren().clear();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuTextField textField =
                        new SudokuTextField(Integer.toString(solvedSudokuBoard.get(i, j)));
                textField.setDisable(true);
                textField.setStyle("-fx-opacity: 1.0;");
                boardGrid.add(textField, i, j);
            }
        }
    }

    @FXML
    public void onSaveToDB() {
        if (Objects.equals(sudokuNameTxt.getText(), "")) {
            AlertDialog.show(resourceBundle.getString("no_name_db_title"),
                    resourceBundle.getString("no_name_db_message"),
                    Alert.AlertType.INFORMATION);
            return;
        }
        try (Dao<SudokuBoard> jdbcDao = daoFactory.getJdbcDao(sudokuNameTxt.getText())) {
            jdbcDao.write(sudokuBoard);
        } catch (Exception e) {
            AlertDialog.show(resourceBundle.getString("fail_save_db_title"),
                    resourceBundle.getString("fail_save_db_message"),
                    Alert.AlertType.INFORMATION);
        }
        logger.info(resourceBundle.getString("logger_save_to_db") + "\n" + sudokuNameTxt.getText()
                + "\n" + sudokuBoard.toString());
    }

    @FXML
    public void onReadFromDB() throws PropertyException {
        if (Objects.equals(sudokuNameTxt.getText(), "")) {
            AlertDialog.show(resourceBundle.getString("no_name_db_title"),
                    resourceBundle.getString("no_name_db_message"),
                    Alert.AlertType.INFORMATION);
            return;
        }
        try (Dao<SudokuBoard> jdbcDao = daoFactory.getJdbcDao(sudokuNameTxt.getText())) {
            sudokuBoard = jdbcDao.read();
        } catch (Exception e) {
            AlertDialog.show(resourceBundle.getString("fail_read_db_title"),
                    resourceBundle.getString("fail_read_db_message"),
                    Alert.AlertType.INFORMATION);
        }
        fillBoard(sudokuBoard);
        logger.info(resourceBundle.getString("logger_read_from_db") + "\n" + sudokuNameTxt.getText()
                + "\n" + sudokuBoard.toString());
    }
}
