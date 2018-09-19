package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Generator {

	private static Logger logger = LogManager.getLogger();

    private int[][] board;
    public int[] depth;

    public void initBoard() {
        board = new int[9][9];
        depth = new int[9];
    }

    public int[][] createBoard(int xStart, int yStart) {

        logger.debug("createBoard [row " + xStart + ", depth=" + depth[xStart] + "]");

        List<Integer> fullRange = new ArrayList<Integer>();
        for (int i = 1; i <= 9; i++)
            fullRange.add(i);

        // for every index track if every number has been tried
        List<Integer> range = null;

        Random rand = new Random();
        for (int x = xStart; x < 9; x++) {
        	logger.debug("x=" + x);
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
	                        return createBoard(x,0);

                        // if after 10 recursive calls we still can't fill the row
	                    // reset this and previous row to 0 and try recursively from the beginning of previous row
                    	} else {
                    		for (int i = 0; i < 9; i++) {
	                            board[x][i] = 0;
	                            board[x-1][i] = 0;
	                        }
                    		return createBoard(x-1,0);
                    	}

                    } else {
                        range.remove(new Integer(random));
                    }
                } while (!isRandomNumberCandidate(x, y, random));

                board[x][y] = random;
                range.remove(new Integer(random));
            }
        }
        return board;
    }

    public boolean isRandomNumberCandidate(int x, int y, int number) {
        if (
                board[x][y] == 0 &&
                isPossibleX(x, y, number) &&
                isPossibleY(x, y, number) &&
                isPossibleBlock(x, y, number)
            ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPossibleX(int x, int y, int number) {
        for (int i = 0; i <= y; i++) {
            if (board[x][i] == number) {
                return false;
            }
        }
        return true;
    }

    public boolean isPossibleY(int x, int y, int number) {
        for (int i = 0; i <= x; i++) {
            if (board[i][y] == number) {
                return false;
            }
        }
        return true;
    }

    public boolean isPossibleBlock(int x, int y, int number) {
        int x1 = x < 3 ? 0 : x < 6 ? 3 : 6;
        int y1 = y < 3 ? 0 : y < 6 ? 3 : 6;
        for (int xx = x1; xx < x1 + 3; xx++) {
            for (int yy = y1; yy < y1 + 3; yy++) {
                if (board[xx][yy] == number) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard(int[][] game) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0)
                logger.info(StringUtils.repeat("-", 19));
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0) {
                    sb.append("|" + game[i][j]);
                } else {
                    sb.append(" " + game[i][j]);
                }
                if (j == 8)
                    sb.append("|");
            }
            logger.info(sb.toString());
        }
        logger.info(StringUtils.repeat("-", 19));
    }
}
