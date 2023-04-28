package pl.kompo.model;

import java.util.Random;

public class BacktrackingSudokuSolver implements SudokuSolver {

    public void solve(SudokuBoard board) {
        fillZeros(0, board);
    }

    private boolean fillZeros(int i, SudokuBoard board) {
        if (i == 81) {
            return true;
        }

        int row = i / 9;
        int col = i % 9;

        if (board.get(row, col) != 0) {
            return fillZeros(i + 1, board);
        }

        Random rand = new Random();
        int number = rand.nextInt(9) + 1;

        for (int count = 0; count < 9; count++) {
            number = number % 9 + 1;

            if (isCorrect(board, row, col, number)) {
                board.set(row, col, number);
                if (fillZeros(i + 1, board)) {
                    return true;
                } else {
                    board.set(row, col, 0);
                }
            }
        }
        return false;
    }

    public boolean isCorrect(final SudokuBoard board, int row, int col, int number) {

        //checking rows
        for (int i = 0; i < 9; i++) {
            if (board.get(row, i) == number) {
                return false;
            }
        }

        //checking columns
        for (int i = 0; i < 9; i++) {
            if (board.get(i, col) == number) {
                return false;
            }
        }

        //checking 3x3 squares
        int squareRow = row / 3;
        int squareCol = col / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.get(i + squareRow * 3, j + squareCol * 3) == number) {
                    return false;
                }
            }
        }
        return true;
    }
}
