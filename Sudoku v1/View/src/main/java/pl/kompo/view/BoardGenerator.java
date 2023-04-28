package pl.kompo.view;

import java.util.Random;
import pl.kompo.model.BacktrackingSudokuSolver;
import pl.kompo.model.SudokuBoard;

public class BoardGenerator {
    private final SudokuBoard sudokuBoard;

    public BoardGenerator(Difficulty level) {
        sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();
        removeRandomDigits(level.getFieldsToRemove());
    }

    private void removeRandomDigits(int n) {
        while (n != 0) {
            Random rand  = new Random();
            int i = rand.nextInt(81);
            int row = i / 9;
            int col = i % 9;
            if (sudokuBoard.get(row, col) != 0) {
                sudokuBoard.set(row, col, 0);
                n--;
            }
        }
    }

    public SudokuBoard getBoard() {
        return sudokuBoard;
    }
}
