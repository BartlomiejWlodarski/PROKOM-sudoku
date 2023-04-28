package pl.kompo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class BacktrackingSudokuSolverTest {

    SudokuBoard sudokuBoard;
    final static Logger logger = LoggerFactory.getLogger(BacktrackingSudokuSolverTest.class);
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ModelLanguages");

    @BeforeEach
    void setUp() {
        sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();
        logger.debug(sudokuBoard.toString());
    }

    @Test
    void validRowFillBoardTest() {
        boolean repeats = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = j + 1; k < 9; k++) {
                    if (sudokuBoard.get(i, j) == sudokuBoard.get(i, k)) {
                        repeats = true;
                        break;
                    }
                }
            }
        }
        assertFalse(repeats);
    }

    @Test
    void validColFillBoardTest() {
        boolean repeats = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = i + 1; k < 9; k++) {
                    if (sudokuBoard.get(i, j) == sudokuBoard.get(k, j)) {
                        repeats = true;
                        break;
                    }
                }
            }
        }

        assertFalse(repeats);
    }

    @Test
    void validBoxFillBoardTest() {
        boolean repeats = false;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int squareRow = row / 3;
                int squareCol = col / 3;
                int number = sudokuBoard.get(row, col);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (sudokuBoard.get(i + squareRow * 3, j + squareCol * 3) == number
                                && (i + squareRow * 3) != row && (j + squareCol * 3) != col) {
                            repeats = true;
                            break;
                        }
                    }
                }
            }
        }
        assertFalse(repeats);
    }

    @Test
    void differentBoardsTest() {
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard2.solveGame();

        assertTrue(sudokuBoard.isNotEqual(sudokuBoard2));
    }

    @Test
    void sameBoardsTest() {
        SudokuBoard sudokuBoard2 = sudokuBoard;
        assertFalse(sudokuBoard.isNotEqual(sudokuBoard2));
    }
}