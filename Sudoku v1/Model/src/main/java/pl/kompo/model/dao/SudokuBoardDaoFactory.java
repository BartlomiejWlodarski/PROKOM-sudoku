package pl.kompo.model.dao;

import java.nio.file.Path;
import pl.kompo.model.SudokuBoard;

public class SudokuBoardDaoFactory {
    public Dao<SudokuBoard> getFileDao(Path filename) {
        return new FileSudokuBoardDao(filename);
    }

    public Dao<SudokuBoard> getJdbcDao(String boardName) {
        return new JdbcSudokuBoardDao(boardName);
    }
}
