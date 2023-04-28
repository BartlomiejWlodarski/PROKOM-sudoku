package pl.kompo.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SudokuBoard implements Serializable, Cloneable {
    private final SudokuField[][] board = new SudokuField[9][9];
    private final SudokuSolver sudokuSolver;

    public SudokuBoard(SudokuSolver sudokuSolver) {
        Objects.requireNonNull(sudokuSolver);
        this.sudokuSolver = sudokuSolver;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board[i][j] = new SudokuField();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuBoard that = (SudokuBoard) o;

        return new EqualsBuilder().append(board, that.board).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(board).toHashCode();
    }

    public SudokuRow getRow(int y) {
        List<SudokuField> row = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            row.set(i, board[y][i]);
        }
        return new SudokuRow(row);
    }


    public SudokuColumn getColumn(int x) {
        List<SudokuField> col = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            col.set(i, board[i][x]);
        }
        return new SudokuColumn(col);
    }

    public SudokuBox getBox(int x, int y) {
        List<SudokuField> box = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            box.set(i, board[x * 3 + i / 3][y * 3 + i % 3]);
        }
        return new SudokuBox(box);
    }

    public boolean isNotEqual(final SudokuBoard sudokuBoard) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getFieldValue() != sudokuBoard.board[i][j].getFieldValue()) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean checkBoard() {
        for (int i = 0; i < 9; i++) {
            //checking rows
            if (!getRow(i).verify()) {
                return false;
            }
            //checking columns
            if (!getColumn(i).verify()) {
                return false;
            }
            //checking 3x3 squares
            if (!getBox(i / 3, i % 3).verify()) {
                return false;
            }
        }
        return true;
    }

    public void solveGame() {
        sudokuSolver.solve(this);
    }

    public int get(int x, int y) {
        return board[x][y].getFieldValue();
    }

    public void set(int x, int y, int value) {
        board[x][y].setFieldValue(value);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("""
                   0 1 2   3 4 5   6 7 8
                  ----------------------
                """);
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0 && i != 0) {
                s.append("  ----------------------\n");
            }
            for (int j = 0; j < 9; j++) {
                if (j == 0) {
                    s.append(i).append("| ");
                }
                if (j % 3 == 0 && j != 0) {
                    s.append("| ");
                }
                s.append(board[i][j].getFieldValue()).append(" ");
            }
            s.append("\n");
        }

        return s.toString();
    }

    @Override
    public SudokuBoard clone() {
        SudokuBoard sudokuBoard = new SudokuBoard(sudokuSolver);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuBoard.set(i, j, board[i][j].getFieldValue());
            }
        }
        return sudokuBoard;
    }
}
