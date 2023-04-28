package pl.kompo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kompo.model.exceptions.CloneException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SudokuUnitTest {

    SudokuBoard sudokuBoard;
    List<SudokuField> numbers;
    List<SudokuField> numbers2;
    final static Logger logger = LoggerFactory.getLogger(SudokuUnitTest.class);

    @BeforeEach
    void setUp() {
        sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();
        logger.debug(sudokuBoard.toString());

        numbers = Arrays.asList(new SudokuField[9]);
        numbers2 = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            numbers.set(i, new SudokuField(i+1));
            numbers2.set(i, new SudokuField(9-i));
        }
    }

    @Test
    void verifyCorrectRowTest() {
        SudokuRow sudokuRow = sudokuBoard.getRow(0);
        assertTrue(sudokuRow.verify());
    }

    @Test
    void verifyCorrectColumnTest() {
        SudokuColumn sudokuColumn = sudokuBoard.getColumn(0);
        assertTrue(sudokuColumn.verify());
    }

    @Test
    void verifyCorrectBoxTest() {
        SudokuBox sudokuBox = sudokuBoard.getBox(0, 0);
        assertTrue(sudokuBox.verify());
    }

    @Test
    void verifyWrongRowTest() {
        sudokuBoard.set(0, 0, 1);
        sudokuBoard.set(0, 1, 1);
        SudokuRow sudokuRow = sudokuBoard.getRow(0);
        assertFalse(sudokuRow.verify());
    }

    @Test
    void verifyWrongColumnTest() {
        sudokuBoard.set(0, 0, 1);
        sudokuBoard.set(1, 0, 1);
        SudokuColumn sudokuColumn = sudokuBoard.getColumn(0);
        assertFalse(sudokuColumn.verify());
    }

    @Test
    void verifyWrongBoxTest() {
        sudokuBoard.set(0, 2, 1);
        sudokuBoard.set(1, 1, 1);
        SudokuBox sudokuBox = sudokuBoard.getBox(0, 0);
        assertFalse(sudokuBox.verify());
    }

    @Test
    void wrongRowSizeTest() {
        List<SudokuField> fields = Arrays.asList(new SudokuField[5]);
        SudokuRow sudokuRow = new SudokuRow(fields);
        assertNotEquals(sudokuRow.getFields(), fields);
    }

    //EQUALS TESTS
    @Test
    void equalsSudokuRow() {
        SudokuRow sudokuRow2 = sudokuBoard.getRow(0);
        assertEquals(sudokuRow2, sudokuBoard.getRow(0));
    }

    @Test
    void notEqualsSudokuRow() {
        SudokuRow sudokuRow2 = sudokuBoard.getRow(1);
        assertNotEquals(sudokuRow2, sudokuBoard.getRow(0));
    }

    @Test
    void equalsSameSudokuRow() {
        SudokuRow sudokuRow2 = sudokuBoard.getRow(0);
        assertEquals(sudokuRow2, sudokuRow2);
    }

    @Test
    void equalsNullSudokuRow() {
        SudokuRow sudokuRow2 = null;
        assertFalse(sudokuBoard.getRow(0).equals(sudokuRow2));
    }

    @Test
    void equalsDifferentClassSudokuRow() {
        SudokuColumn sudokuColumn2 = sudokuBoard.getColumn(0);
        assertFalse(sudokuColumn2.equals(sudokuBoard.getRow(0)));
    }

    @Test
    void equalsSudokuColumn() {
        SudokuColumn sudokuColumn2 = sudokuBoard.getColumn(0);
        assertEquals(sudokuColumn2, sudokuBoard.getColumn(0));
    }

    @Test
    void notEqualsSudokuColumn() {
        SudokuColumn sudokuColumn2 = sudokuBoard.getColumn(1);
        assertNotEquals(sudokuColumn2, sudokuBoard.getColumn(0));
    }

    @Test
    void equalsSudokuBox() {
        SudokuBox sudokuBox2 = sudokuBoard.getBox(0, 0);
        assertEquals(sudokuBox2, sudokuBoard.getBox(0, 0));
    }

    @Test
    void notEqualsSudokuBox() {
        SudokuBox sudokuBox2 = sudokuBoard.getBox(0, 1);
        assertNotEquals(sudokuBox2, sudokuBoard.getBox(0, 0));
    }

    //HASHCODE TESTS

    @Test
    void equalHashCodesRow() {
        SudokuRow row1 = new SudokuRow(numbers);
        SudokuRow row2 = new SudokuRow(numbers);
        assertEquals(row1.hashCode(), row2.hashCode());
    }

    @Test
    void notEqualHashCodesRow() {
        SudokuRow row1 = new SudokuRow(numbers);
        SudokuRow row2 = new SudokuRow(numbers2);
        assertNotEquals(row1.hashCode(), row2.hashCode());
    }

    @Test
    void equalHashCodesColumn() {
        SudokuColumn column1 = new SudokuColumn(numbers);
        SudokuColumn column2 = new SudokuColumn(numbers);
        assertEquals(column1.hashCode(), column2.hashCode());
    }

    @Test
    void notEqualHashCodesColumn() {
        SudokuColumn column1 = new SudokuColumn(numbers);
        SudokuColumn column2 = new SudokuColumn(numbers2);
        assertNotEquals(column1.hashCode(), column2.hashCode());
    }

    @Test
    void equalHashCodesBox() {
        SudokuBox box1 = new SudokuBox(numbers);
        SudokuBox box2 = new SudokuBox(numbers);
        assertEquals(box1.hashCode(), box2.hashCode());
    }

    @Test
    void notEqualHashCodesBox() {
        SudokuBox box1 = new SudokuBox(numbers);
        SudokuBox box2 = new SudokuBox(numbers2);
        assertNotEquals(box1.hashCode(), box2.hashCode());
    }

    @Test
    void notEqualHashCodesDifferentSudokuUnitSubclasses() {
        SudokuRow sudokuRow = new SudokuRow(numbers);
        SudokuColumn sudokuColumn = new SudokuColumn(numbers);
        logger.debug(String.valueOf(sudokuRow));
        logger.debug(String.valueOf(sudokuRow.hashCode()));
        logger.debug(String.valueOf(sudokuColumn));
        logger.debug(String.valueOf(sudokuColumn.hashCode()));
        assertNotEquals(sudokuRow.hashCode(), sudokuColumn.hashCode());
    }

    @Test
    void cloneSudokuRowTest() {
        SudokuRow row = new SudokuRow(numbers);
        SudokuRow cloned = (SudokuRow) row.clone();
        logger.debug(String.valueOf(row));
        logger.debug(String.valueOf(cloned));
        assertEquals(row, cloned);
        row.setFields(numbers2);
        logger.debug(String.valueOf(row));
        logger.debug(String.valueOf(cloned));
        assertNotEquals(row, cloned);
    }

    @Test
    void cloneSudokuColumnTest() {
        SudokuColumn column = new SudokuColumn(numbers);
        SudokuColumn cloned = (SudokuColumn) column.clone();
        assertEquals(column, cloned);
        column.setFields(numbers2);
        assertNotEquals(column, cloned);
    }

    @Test
    void cloneSudokuBoxTest() {
        SudokuBox box = new SudokuBox(numbers);
        SudokuBox cloned = (SudokuBox) box.clone();;
        assertEquals(box, cloned);
        box.setFields(numbers2);
        assertNotEquals(box, cloned);
    }
}