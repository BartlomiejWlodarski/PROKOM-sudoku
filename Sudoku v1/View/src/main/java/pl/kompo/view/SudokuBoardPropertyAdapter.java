package pl.kompo.view;

import pl.kompo.model.SudokuBoard;

public class SudokuBoardPropertyAdapter {
    private SudokuBoard board;
    private int row;
    private int column;

    public SudokuBoardPropertyAdapter(SudokuBoard board, int row, int column) {
        this.board = board;
        this.row = row;
        this.column = column;
    }

    public int getField() {
        return board.get(row, column);
    }

    public void setField(int value) {
        board.set(row, column, value);
    }
}
