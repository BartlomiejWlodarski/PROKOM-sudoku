package pl.kompo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SudokuFieldTest {

    SudokuField sudokuField;
    final static Logger logger = LoggerFactory.getLogger(SudokuFieldTest.class);

    @BeforeEach
    void setUp() {
        sudokuField = new SudokuField(5);
    }

    @Test
    void getFieldValueTest() {
        assertEquals(sudokuField.getFieldValue(), 5);
    }

    @Test
    void setFieldValueTest() {
        sudokuField.setFieldValue(7);
        assertEquals(sudokuField.getFieldValue(), 7);
    }

    @Test
    void equalsNullSudokuFieldTest() {
        SudokuField sudokuField1 = null;
        assertFalse(sudokuField.equals(sudokuField1));
    }

    @Test
    void equalsDifferentClassesTest() {
        SudokuRow sudokuRow = new SudokuRow(Arrays.asList(new SudokuField[9]));
        assertFalse(sudokuField.equals(sudokuRow));
    }

    @Test
    void cloneSudokuFieldTest() {
        SudokuField cloned = sudokuField.clone();
        logger.debug(String.valueOf(sudokuField));
        logger.debug(String.valueOf(cloned));
        assertEquals(sudokuField, cloned);
    }

    @Test
    void compareSudokuFieldTest() {
        SudokuField sudokuField1 = new SudokuField(2);
        SudokuField sudokuField2 = new SudokuField(6);
        SudokuField sudokuField3 = new SudokuField(6);

        assertTrue(sudokuField1.compareTo(sudokuField2) < 0);
        assertTrue(sudokuField2.compareTo(sudokuField3) == 0);
        assertTrue(sudokuField2.compareTo(sudokuField1) > 0);
    }

    @Test
    void compareNullSudokuFieldTest() {
        assertThrows(NullPointerException.class, () -> sudokuField.compareTo(null));
    }
}