package pl.kompo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {
    SudokuBoard sudokuBoard;
    final static Logger logger = LoggerFactory.getLogger(SudokuBoardTest.class);
    @BeforeEach
    void createSudoku() {
        sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();
        logger.info(sudokuBoard.toString());
    }

    @Test
    void correctCheckBoardTest() {
        assertTrue(sudokuBoard.checkBoard());
    }

    @Test
    void wrongRowCheckBoardTest() {
        sudokuBoard.set(0, 0, 1);
        sudokuBoard.set(0, 1, 1);
        assertFalse(sudokuBoard.checkBoard());
    }

    @Test
    void wrongColumnCheckBoardTest() {
        sudokuBoard.set(1, 0, 1);
        sudokuBoard.set(2, 0, 1);
        assertFalse(sudokuBoard.checkBoard());
    }

    @Test
    void wrongBoxCheckBoardTest() {
        for (int i = 0; i < 9; i++) {
            int temp = sudokuBoard.get(5, i);
            sudokuBoard.set(5, i, sudokuBoard.get(6, i));
            sudokuBoard.set(6, i, temp);
        }
        assertFalse(sudokuBoard.checkBoard());
    }

    @Test
    void getRowTest() {
        assertNotNull(sudokuBoard.getRow(0));
    }

    @Test
    void getColumnTest() {
        assertNotNull(sudokuBoard.getColumn(1));
    }

    @Test
    void getBoxTest() {
        assertNotNull(sudokuBoard.getBox(1, 1));
    }

    @Test
    void equalsSudokuBoardTest() {
        SudokuBoard sudokuBoard2 = sudokuBoard;
        assertEquals(sudokuBoard2, sudokuBoard);
    }

    @Test
    void notEqualsSudokuBoardTest() {
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());
        assertNotEquals(sudokuBoard2, sudokuBoard);
    }

    @Test
    void equalsNullSudokuBoardTest() {
        SudokuBoard sudokuBoard2 = null;
        assertFalse(sudokuBoard.equals(sudokuBoard2));
    }

    @Test
    void equalsDifferentClassSudokuBoardTest() {
        SudokuRow sudokuRow = new SudokuRow(Arrays.asList(new SudokuField[9]));
        assertFalse(sudokuBoard.equals(sudokuRow));
    }

    @Test
    void equalsHashCodeSudokuBoardTest() {
        assertEquals(sudokuBoard.hashCode(), sudokuBoard.hashCode());
    }

    @Test
    void notEqualsHashCodeSudokuBoardTest() {
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());
        assertNotEquals(sudokuBoard.hashCode(), sudokuBoard2.hashCode());
    }

    @Test
    void SudokuBoardCloneTest() {                       //Testowanie tworzenia kopii i jej rozlacznosci z obiektem klonowanym
        sudokuBoard.set(0,0,1);
        SudokuBoard cloned = sudokuBoard.clone();
        assertEquals(sudokuBoard, cloned);
        sudokuBoard.set(0,0,9);
        assertNotEquals(sudokuBoard, cloned);
    }
}