package work;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import util.BoardUtils;

public class Solver {

    private static Logger logger = LogManager.getLogger();

    private static final int NO_VALUE = 0;
    private static final int BOARD_SIZE = 9;

    private final static String TYPE_ROW = "row";
    private final static String TYPE_COLUMN = "column";
    private final static String TYPE_BLOCK = "block";

    // this must be instance variable b/c of recursion
    private int[][] unsolvedBoard;

    @SuppressWarnings("unchecked")
    List<Integer>[][] possibleValuesBoard = new ArrayList[9][9];

    int[][] numberOfPossibleValuesBoard;

    public int[][] solveBoard(int[][] board) {
    	this.unsolvedBoard = board;
        numberOfPossibleValuesBoard = new int[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                possibleValuesBoard[x][y] = new ArrayList<Integer>();
                numberOfPossibleValuesBoard[x][y] = 0;
            }
        }
        return solveBoardLikeHuman();
    }

    private int[][] solveBoardLikeHuman() {
    	List<Integer> possibleValues;

        // find all possible values for empty fields
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (unsolvedBoard[x][y] == NO_VALUE) {
                    possibleValues = new ArrayList<Integer>();
                    for (int possibleValue = 1; possibleValue <= 9; possibleValue++) {
                        if (BoardUtils.isNumberCandidate(unsolvedBoard, x, y, possibleValue)) {
                            possibleValues.add(possibleValue);
                        }
                    }
                    possibleValuesBoard[x][y] = possibleValues;
                    numberOfPossibleValuesBoard[x][y] = possibleValues.size();
                } else {
                    possibleValuesBoard[x][y] = null;
                }
            }
        }

        // helper statistic: show number of possible values per fields
        //   key: number of solutions
        //   value: number of fields that have "key" number of solutions
        Map<Integer, Integer> occurencesMap = new HashMap<Integer, Integer>();
        int occurences;
        int possibleValue;
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                possibleValue = numberOfPossibleValuesBoard[x][y];
                if (occurencesMap.containsKey(possibleValue)) {
                    occurences = occurencesMap.get(possibleValue);
                } else {
                    occurences = 0;
                }
                occurences++;
                occurencesMap.put(possibleValue, occurences);
            }
        }
        //logger.info("Possible values board (" + Arrays.toString(occurencesMap.entrySet().toArray()) + "):");
        //BoardUtils.printBoard(numberOfPossibleValuesBoard);

        int[][] partialySolvedBoard;

        logger.info("#1 - Solving one solution fields...");
        partialySolvedBoard = solveOneSolutionFields(unsolvedBoard);
        if (BoardUtils.isSolved(partialySolvedBoard))
        	return partialySolvedBoard;
        logger.info("Partially solved board:");
       	BoardUtils.printBoard(partialySolvedBoard);
       	logger.info("Number of possible values:");
       	BoardUtils.printBoard(numberOfPossibleValuesBoard);

        logger.info("#2 - Solving rows...");
        partialySolvedBoard = solveSections(partialySolvedBoard, TYPE_ROW);
        if (BoardUtils.isSolved(partialySolvedBoard))
        	return partialySolvedBoard;
        logger.info("Partially solved board:");
       	BoardUtils.printBoard(partialySolvedBoard);
       	logger.info("Number of possible values:");
       	BoardUtils.printBoard(numberOfPossibleValuesBoard);

       	logger.info("#3 - Solving columns...");
       	partialySolvedBoard = solveSections(partialySolvedBoard, TYPE_COLUMN);
       	if (BoardUtils.isSolved(partialySolvedBoard))
       		return partialySolvedBoard;
       	logger.info("Partially solved board:");
       	BoardUtils.printBoard(partialySolvedBoard);
       	logger.info("Number of possible values:");
       	BoardUtils.printBoard(numberOfPossibleValuesBoard);

       	logger.info("#4 - Solving blocks...");
       	partialySolvedBoard = solveSections(partialySolvedBoard, TYPE_BLOCK);
       	if (BoardUtils.isSolved(partialySolvedBoard))
       		return partialySolvedBoard;
       	logger.info("Partially solved board:");
       	BoardUtils.printBoard(partialySolvedBoard);
       	logger.info("Number of possible values:");
       	BoardUtils.printBoard(numberOfPossibleValuesBoard);

       	return partialySolvedBoard;
    }

    private int step;

    private int[][] solveOneSolutionFields(int[][] board) {
    	step++;
    	List<Integer> possibleValues;
    	int solutions = 0;
    	for (int x = 0; x < 9; x++) {
    		for (int y = 0; y <9; y++) {
    			if (numberOfPossibleValuesBoard[x][y] == 1) {
    				solutions++;
    				possibleValues = possibleValuesBoard[x][y];

    				// solve field
    				unsolvedBoard[x][y] = possibleValues.get(0);

    				// update helper arrays
    				numberOfPossibleValuesBoard[x][y]--;
    				possibleValuesBoard[x][y] = null;
    			}
    		}
    	}
    	logger.info("Step " + step + "...solved " + solutions + " fields");
    	if (solutions > 0) {
    		return solveOneSolutionFields(board);
    	}
    	return board;
    }

    private int[][] solveSections(int[][] board, String type) {
    	step++;
    	List<Integer>[] possibleValuesArray;
    	Map<Integer,List<Integer>> possibleValuesPositionsMap = new HashMap<Integer,List<Integer>>();
    	int solutions = 0;

        for (int m = 0; m < 9; m++) {	// iterate through rows / columns / blocks
        	possibleValuesArray = getPossibleValuesSection(possibleValuesBoard, m, type);
        	List<Integer> possibleValuesPositions;

        	for (int n = 0; n < 9; n++) {		// iterate through columns in the row / rows in the column / fields in the block
                if (possibleValuesArray[n] != null) {

                    for (int possibleValue : possibleValuesArray[n]) {
                    	if (possibleValuesPositionsMap.containsKey(possibleValue)) {
                    		possibleValuesPositions = possibleValuesPositionsMap.get(possibleValue);
                    	} else {
                    		possibleValuesPositions = new ArrayList<>();
                    	}
                    	possibleValuesPositionsMap.put(possibleValue, possibleValuesPositions);
                    }
                }
            }

        	int value;
        	int position;
        	for (Entry<Integer,List<Integer>> entry : possibleValuesPositionsMap.entrySet()) {
        		if (entry.getValue().size() == 1) {
        			solutions++;
        			value = entry.getKey();
        			possibleValuesPositions = entry.getValue();
        			position = possibleValuesPositions.get(0);

        			// solve field + update helper arrays
        			switch(type) {
        			case(TYPE_ROW):
        				unsolvedBoard[m][position] = value;
        				numberOfPossibleValuesBoard[m][position]--;
        				possibleValuesBoard[m][position] = null;
        			case(TYPE_COLUMN):
        				unsolvedBoard[position][m] = value;
        				numberOfPossibleValuesBoard[position][m]--;
        				possibleValuesBoard[position][m] = null;
        			case(TYPE_BLOCK):
        				int x1 = ((m / 3 ) * 3) + (position / 3);
        	        	int y1 = ((m % 3) * 3) + (position % 3);
        				unsolvedBoard[x1][y1] = value;
        				numberOfPossibleValuesBoard[x1][y1]--;
        				possibleValuesBoard[x1][y1] = null;
        			}
        			//logger.info("Row " + m + ", found only 1 possible possition " + position + " for value " + value);
        		}
        	}
        }
        logger.info("Step " + step + "...solved " + solutions + " fields");
    	if (solutions > 0) {
    		return solveSections(board, type);
    	}
    	return board;
    }

    private List<Integer>[] getPossibleValuesSection(List<Integer>[][] board, int index, String type) {
    	List<Integer>[] section = null;
    	switch (type) {
    	case(TYPE_ROW):
    		section = getPossibleValuesRow(board, index);
    		break;
    	case(TYPE_COLUMN):
    		section = getPossibleValuesColumn(board, index);
    		break;
    	case(TYPE_BLOCK):
    		section = getPossibleValuesBlock(board, index);
    		break;
    	}
    	return section;
    }

    private List<Integer>[] getPossibleValuesRow(List<Integer>[][] possibleValuesBoard, int rowIndex) {
        @SuppressWarnings("unchecked")
        List<Integer>[] row = new ArrayList[9];
        for (int y = 0; y < BOARD_SIZE; y++) {
            row[y] = possibleValuesBoard[rowIndex][y];
        }
        return row;
    }

    private List<Integer>[] getPossibleValuesColumn(List<Integer>[][] possibleValuesBoard, int columnIndex) {
        @SuppressWarnings("unchecked")
        List<Integer>[] column = new ArrayList[9];
        for (int x = 0; x < BOARD_SIZE; x++) {
            column[x] = possibleValuesBoard[x][columnIndex];
        }
        return column;
    }

    private List<Integer>[] getPossibleValuesBlock(List<Integer>[][] possibleValuesBoard, int blockIndex) {
        @SuppressWarnings("unchecked")
        List<Integer>[] block = new ArrayList[9];
        int i = 0;
        int x1 = (blockIndex / 3 ) * 3;
        int y1 = (blockIndex % 3) * 3;
        //logger.info("blockIndex=" + blockIndex + ", x1=" + x1 + ", y1=" + y1);
        for (int x = x1; x < x1 + 3; x++) {
            for (int y = y1; y < y1 + 3; y++) {
                block[i++] = possibleValuesBoard[x][y];
            }
        }
        return block;
    }

}
