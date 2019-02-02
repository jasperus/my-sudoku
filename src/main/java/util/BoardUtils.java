package util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BoardUtils {

	private static Logger logger = LogManager.getLogger();

	public static void printBoard(int[][] board) {
		int REPEAT = 19;
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0)
            	logger.info(StringUtils.repeat("-", REPEAT));
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0) {
                    sb.append("|" + board[i][j]);
                } else {
                    sb.append(" " + board[i][j]);
                }
                if (j == 8)
                    sb.append("|");
            }
            logger.info(sb.toString());
        }
        logger.info(StringUtils.repeat("-", REPEAT));
    }

	public static void printPossibleValuesBoard(List<Integer>[][] possibleValuesBoard) {
		int[][][][] board4D = new int[9][9][3][3];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				List<Integer> possibleValuesList = possibleValuesBoard[i][j];
				int[] possibleValues1D = new int[9];
				if (possibleValuesList != null) {
					for (int possibleValue : possibleValuesList) {
						possibleValues1D[possibleValue - 1] = possibleValue;
					}
				}
				int[][] possibleValues2D = makeArray2D(possibleValues1D);
				board4D[i][j] = possibleValues2D;
			}
		}

		int REPEAT = 68;
		for (int i = 0; i < 9; i++) {					// #1 - board rows
			if (i % 3 == 0) {
				logger.debug(StringUtils.repeat("=", REPEAT));
			} else {
				logger.debug(StringUtils.repeat("-", REPEAT));
			}
			for (int k = 0; k < 3; k++) {				// #2 - possible values rows
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < 9; j++) {			// #3 - board columns
					if (j % 3 == 0) {
						sb.append("||");
					} else {
						sb.append("|");
					}
					for (int l = 0; l < 3; l++) {		// #4 - possible values columns
						sb.append(" " + board4D[i][j][k][l]);
					}
					if (j == 8)
						sb.append("||");
				}
				logger.debug(sb.toString());
			}
		}
		logger.debug(StringUtils.repeat("=", REPEAT));
	}

	private static int[][] makeArray2D(int[] array1D) {
		int[][] array2D = new int[3][3];
		for (int i = 0; i < 9; i++) {
			array2D[i/3][i%3] = array1D[i];
		}
		return array2D;
	}

    public static boolean isNumberCandidate(int[][] board, int x, int y, int number) {
        if (
                board[x][y] == NO_VALUE &&
                isPossibleX(board, x, y, number) &&
                isPossibleY(board, x, y, number) &&
                isPossibleBlock(board, x, y, number)
            ) {
            return true;
        }
        return false;
    }

    private static boolean isPossibleX(int[][] board, int x, int y, int number) {
        for (int i = 0; i < 9; i++) {
            if (board[x][i] == number) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPossibleY(int[][] board, int x, int y, int number) {
        for (int i = 0; i < 9; i++) {
            if (board[i][y] == number) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPossibleBlock(int[][] board, int x, int y, int number) {
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

    private static final int BOARD_START_INDEX = 0;
    private static final int BOARD_SIZE = 9;
    private static final int NO_VALUE = 0;

    public static boolean isSolved(int[][] board) {
    	for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (board[row][column] == NO_VALUE) {
                	return false;
                }
            }
    	}
    	return true;
    }

    public static int unsolvedFieldsRemaining(int[][] board) {
    	int remaining = 0;
    	for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (board[row][column] == NO_VALUE) {
                	remaining++;
                }
            }
    	}
    	return remaining;
    }

    //*****************************************************************
    // Java - How to get next or previous item from a Collection?
    // https://www.logicbig.com/how-to/java-collections/collection-next-previous.html
    private static final int NO_PREV_ELEMENT = -1;
    private static final int NO_NEXT_ELEMENT = 100;

	public static int getNext(List<Integer> list, int value) {
        int idx = list.indexOf(value);
        if (idx < 0 || idx+1 == list.size()) return NO_NEXT_ELEMENT;
        return list.get(idx + 1);
    }

	public static int getPrevious(List<Integer> list, int value) {
        int idx = list.indexOf(value);
        if (idx <= 0) return NO_PREV_ELEMENT;
        return list.get(idx - 1);
    }
	//*****************************************************************


	public static int[][] cloneArray(int[][] src) {
		int length = src.length;
		int[][] target = new int[length][src[0].length];
		for (int i = 0; i < length; i++) {
			System.arraycopy(src[i], 0, target[i], 0, src[i].length);
		}
		return target;
	}
}
