package sudoku.work;

import lombok.extern.slf4j.Slf4j;
import sudoku.util.BoardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Generator {

    // these two must be instance variables b/c of recursion
    private final int[][] board;
    private final int[] depth;        // limit number of recursions for every row

    public Generator() {
        board = new int[9][9];
        depth = new int[9];
    }

    public int[][] createBoard() {
        return createBoard(0, 0);
    }

    @SuppressWarnings("ReplaceArraysFill")
    private int[][] createBoard(int xStart, int yStart) {

        log.trace("createBoard [row {}, depth={}]", xStart, depth[xStart]);

        List<Integer> fullRange = new ArrayList<Integer>();

        for (int i = 1; i <= 9; i++) {
            fullRange.add(i);
        }

        // for every index track if every number has been tried
        List<Integer> range = null;

        Random rand = new Random();
        for (int x = xStart; x < 9; x++) {
            log.trace("x={}", x);
            for (int y = yStart; y < 9; y++) {
                range = new ArrayList<Integer>(fullRange);
                int random;
                do {
                    //random = rand.nextInt((MAX - MIN) + 1) + MIN;
                    random = rand.nextInt(fullRange.size() + 1);

                    // if tried all 9 numbers for this position, and no one is candidate
                    // reset whole row to 0 and try recursively from the beginning of the row
                    if (range.size() == 0) {
                        // go max 10 recursive calls for row
                        if (depth[xStart] < 10) {
                            for (int i = 0; i < 9; i++) {
                                board[x][i] = 0;
                            }
                            depth[xStart]++;
                            return createBoard(x, 0);

                            // if after 10 recursive calls we still can't fill the row
                            // reset this and previous row to 0 and try recursively from the beginning of previous row
                        } else {
                            for (int i = 0; i < 9; i++) {
                                board[x][i] = 0;
                                board[x - 1][i] = 0;
                            }
                            return createBoard(x - 1, 0);
                        }

                    } else {
                        range.remove(Integer.valueOf(random));
                    }
                } while (!BoardUtils.isNumberCandidate(board, x, y, random));

                board[x][y] = random;
                range.remove(Integer.valueOf(random));
            }
        }
        return board;
    }

}