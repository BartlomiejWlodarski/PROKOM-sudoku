package pl.kompo.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kompo.model.dao.Dao;
import pl.kompo.model.dao.SudokuBoardDaoFactory;
import pl.kompo.model.exceptions.DbException;
import pl.kompo.model.exceptions.FileException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class DaoTest {

    SudokuBoard sudokuBoard;
    SudokuBoard sudokuBoard2;
    SudokuBoardDaoFactory daoFactory;
    final static Logger logger = LoggerFactory.getLogger(DaoTest.class);
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("ModelLanguages");

    @BeforeEach
    void setUp() {
        sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        daoFactory = new SudokuBoardDaoFactory();

    }

    @Test
    public void readWriteTest() {
        try (Dao<SudokuBoard> fileSudokuBoardDao = daoFactory.getFileDao(Path.of("test.txt"))) {
            fileSudokuBoardDao.write(sudokuBoard);
            sudokuBoard2 = fileSudokuBoardDao.read();
        } catch (Exception e) {
            throw new FileException(resourceBundle.getString("write_fail"), e);
        }

        Path path = FileSystems.getDefault().getPath("test.txt");
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileException(resourceBundle.getString("delete_test_fail"),e);
        }

        assertEquals(sudokuBoard, sudokuBoard2);
    }

    @Test
    void readIOExceptionTest() {
        assertThrows(RuntimeException.class ,() -> {
            try (Dao<SudokuBoard> fileSudokuBoardDao = daoFactory.getFileDao(Path.of("notFound"))) {
                sudokuBoard2 = fileSudokuBoardDao.read();
            } catch (Exception e) {
                throw new FileException(resourceBundle.getString("read_file_fail"), e);
            }
        });
    }

    @Test
    void writeIOExceptionTest() {
        assertThrows(RuntimeException.class ,() -> {
            try (Dao<SudokuBoard> fileSudokuBoardDao = daoFactory.getFileDao(Path.of("<<wrongName>>"))) {
                fileSudokuBoardDao.write(sudokuBoard);
            } catch (Exception e) {
                throw new FileException(resourceBundle.getString("write_file_fail"), e);
            }
        });
    }

//    @Test
//    void JdbcCreateTableTest() {
//        try (Dao<SudokuBoard> jdbcDao = daoFactory.getJdbcDao("test")) {
//            jdbcDao.write(sudokuBoard);
//        } catch (Exception e) {
//            throw new DBException(e);
//        }
//    }

    @Test
    void JdbcSameNameTest() {
        assertThrows(Exception.class, () -> {
            try (Dao<SudokuBoard> jdbcDao = daoFactory.getJdbcDao("test1")) {
                jdbcDao.write(sudokuBoard);
                jdbcDao.write(sudokuBoard);
            } catch (Exception e) {
                throw new DbException(e);
            }
        });
    }

    @Test
    void JdbcNoSuchSudokuNameTest() {
        assertThrows(Exception.class, () -> {
            SudokuBoard readBoard;
            try (Dao<SudokuBoard> jdbcDao = daoFactory.getJdbcDao("no-such-name")) {
                readBoard = jdbcDao.read();
            } catch (Exception e) {
                throw new DbException(e);
            }
        });
    }

    @Test
    void JdbcReadWriteSudokuBoardTest() {
        sudokuBoard.solveGame();
        SudokuBoard readBoard;
        try (Dao<SudokuBoard> jdbcDao = daoFactory.getJdbcDao("test2")) {
            jdbcDao.write(sudokuBoard);
            readBoard = jdbcDao.read();
        } catch (Exception e) {
            throw new DbException(e);
        }
        logger.debug(readBoard.toString());
        assertEquals(readBoard, sudokuBoard);
    }
}