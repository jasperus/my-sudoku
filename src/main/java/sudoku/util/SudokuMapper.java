package sudoku.util;

import org.apache.commons.lang3.StringUtils;
import sudoku.entity.Sudoku;

public class SudokuMapper {

    public static final String ALLOWED_DIGITS = "123456789";
    public static final String INIT_EMPTY = "";
    public static final String SOLVING_SEQUENCE_INIT = "0";
    public static final String SOLVING_SEQUENCE_EMPTY = "";

    public static Sudoku prepareBoardForSaving(String[][] board) {
        String[][] boardToSave = new String[9][9];
        String[][] solvingSequence = new String[9][9];;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (StringUtils.containsAny(board[i][j], ALLOWED_DIGITS)) {
                    boardToSave[i][j] = board[i][j];
                    solvingSequence[i][j] = SOLVING_SEQUENCE_INIT;
                } else {
                    boardToSave[i][j] = INIT_EMPTY;
                    solvingSequence[i][j] = SOLVING_SEQUENCE_EMPTY;
                }
            }
        }

        Sudoku sudoku = new Sudoku();
        sudoku.setInitialBoard(boardToSave);
        sudoku.setBoard(boardToSave);
        sudoku.setSolvingSequence(solvingSequence);

        return sudoku;
    }
}
