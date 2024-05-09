package sudoku.work;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

import lombok.Getter;
import sudoku.model.Board;
import sudoku.util.BoardUtils;

@Slf4j
public class Solutions {

    private static final int BOARD_SIZE = 9;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;
    private static final int NO_VALUE = 0;

    private static final int BOARD_START_INDEX = 0;
    private static final int SUBSECTION_SIZE = 3;

    @Getter
    private int numOfSolutions;
    @Getter
    private List<Board> solvedBoards;

    public boolean findPossibleSolutions(int[][] board) {
        numOfSolutions = 0;
        solvedBoards = new ArrayList<Board>();

        return findPossibleSolutionsInner(board);
    }

    private boolean findPossibleSolutionsInner(int[][] board) {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (board[row][column] == NO_VALUE) {
                    for (int k = MIN_VALUE; k <= MAX_VALUE; k++) {
                        board[row][column] = k;
                        int[][] solution = BoardUtils.cloneArray(board);        // very important to create clone of array for every recursion
                        if (isValid(solution, row, column) && findPossibleSolutionsInner(solution)) {
                            // FIXME: ovo radi, ali ne bi smjelo ovako !!!
                            if (!BoardUtils.isSolved(board))
                                return false;
                            Board b = new Board();
                            b.setBoard(solution);
                            if (!solvedBoards.contains(b)) {
                                solvedBoards.add(b);
                                numOfSolutions++;
                                // logger.debug("Possible solution " + numOfSolutions + ":");
                                // BoardUtils.printBoard(solution);
                            }
                            return true;
                        }
                        board[row][column] = NO_VALUE;
                    }
                    return false;
                }
            }
        }
        return true;    // here we have whole board solved and deepest recursion returns true
    }

    private boolean isValid(int[][] board, int row, int column) {
        return (rowConstraint(board, row)
                && columnConstraint(board, column)
                && subsectionConstraint(board, row, column));
    }

    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
                .allMatch(column -> checkConstraint(board, row, constraint, column));
    }

    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
                .allMatch(row -> checkConstraint(board, row, constraint, column));
    }

    private boolean subsectionConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;

        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                if (!checkConstraint(board, r, constraint, c)) return false;
            }
        }
        return true;
    }

    boolean checkConstraint(
            int[][] board,
            int row,
            boolean[] constraint,
            int column) {
        if (board[row][column] != NO_VALUE) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }
}
