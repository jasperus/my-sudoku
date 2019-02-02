package work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.ListUtils;
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

    int numOfSolutionsForStep = 0;

    int[][] partialySolvedBoard;

    private int step;

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

        /*
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
         */

        int loop = 0;
        do {
        	step = 0;
        	numOfSolutionsForStep = 0;
        	loop++;

        	logger.info("#" + loop + ".1 - Solving one solution fields...");
        	partialySolvedBoard = solveOneSolutionFields(unsolvedBoard);
        	if (BoardUtils.isSolved(partialySolvedBoard))
        		return partialySolvedBoard;
        	printStep();

        	// možda nam ova 3 ne trebaju, nisu riješila ništa (?)
        	// doduše, za sada sam pokrenuo samo za jedan "cleared board"
        	logger.info("#" + loop + ".2 - Solving rows...");
        	partialySolvedBoard = solveSections(partialySolvedBoard, TYPE_ROW);
        	if (BoardUtils.isSolved(partialySolvedBoard))
        		return partialySolvedBoard;
        	printStep();

        	logger.info("#" + loop + ".3 - Solving columns...");
        	partialySolvedBoard = solveSections(partialySolvedBoard, TYPE_COLUMN);
        	if (BoardUtils.isSolved(partialySolvedBoard))
        		return partialySolvedBoard;
        	printStep();

        	logger.info("#" + loop + ".4 - Solving blocks...");
        	partialySolvedBoard = solveSections(partialySolvedBoard, TYPE_BLOCK);
        	if (BoardUtils.isSolved(partialySolvedBoard))
        		return partialySolvedBoard;
        	printStep();

        } while (numOfSolutionsForStep > 0);

        return partialySolvedBoard;
    }

    private void printStep() {
    	logger.debug("Partially solved board:");
    	BoardUtils.printBoard(partialySolvedBoard);
//    	logger.debug("Number of possible values:");
//    	BoardUtils.printBoard(numberOfPossibleValuesBoard);
//    	logger.debug("Possible values board:");
//    	BoardUtils.printPossibleValuesBoard(possibleValuesBoard);
    }

    private int[][] solveOneSolutionFields(int[][] board) {
        step++;
        List<Integer> possibleValues;
        int solutions = 0;
        int boardIndex = 0;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y <9; y++) {
                if (numberOfPossibleValuesBoard[x][y] == 1) {
                    solutions++;
                    numOfSolutionsForStep++;
                    possibleValues = possibleValuesBoard[x][y];
                    boardIndex = ((x / 3) * 3) + (y / 3);
                    updateArraysAfterSolving(x, y, boardIndex, possibleValues.get(0));
                }
            }
        }
        logger.info("Step " + step + "...solved " + solutions + " fields, remaining " + BoardUtils.unsolvedFieldsRemaining(board) + " fields");
//        logger.debug("Possible values board:");
//        BoardUtils.printPossibleValuesBoard(possibleValuesBoard);
        if (solutions > 0) {
            return solveOneSolutionFields(board);
        }
        return board;
    }

    private int[][] solveSections(int[][] board, String type) {
        step++;
        int solutions = 0;
        List<Integer>[] possibleValuesArray;
        List<Integer> possibleValuesPositions;

        // *** Inverted map ***
        // it does not contain that for positions 5 and 7 in section possible values are 2 and 3
        // it does contain that for values 2 and 3 possible positions are 5 and 7
        Map<Integer,List<Integer>> possibleValuesPositionsMap;

        // FIXME: ovdje je nešto jako krivo...nemoguæe da je uspio riješiti 12 polja u prvom stepu, a ova beskonaèna petlja nakon toga...uh
        // #1.2 - Solving rows...
        // Step 5...solved 12 fields
        // Step 6...solved 7 fields
        // Step 7...solved 7 fields

        // #1
        // Find if there is only one position in unsolved fields for given value in section.
        // If found, solve field and remove value from other unsolved fields in section.
        for (int m = 0; m < 9; m++) {	// iterate through rows / columns / blocks
            possibleValuesArray = getPossibleValuesSection(possibleValuesBoard, m, type);
            possibleValuesPositionsMap = new HashMap<Integer,List<Integer>>();

            for (int n = 0; n < 9; n++) {		// iterate through columns in the row / rows in the column / fields in the block
                if (possibleValuesArray[n] != null) {
                    for (int possibleValue : possibleValuesArray[n]) {
                        if (possibleValuesPositionsMap.containsKey(possibleValue)) {
                            possibleValuesPositions = possibleValuesPositionsMap.get(possibleValue);
                        } else {
                            possibleValuesPositions = new ArrayList<>();
                        }
                        possibleValuesPositions.add(n);
                        possibleValuesPositionsMap.put(possibleValue, possibleValuesPositions);
                    }
                }
            }

            int value;
            int position;
            for (Entry<Integer,List<Integer>> entry : possibleValuesPositionsMap.entrySet()) {
            	if (entry.getValue().size() == 1) {
            		solutions++;
            		numOfSolutionsForStep++;
            		value = entry.getKey();
            		position = entry.getValue().get(0);

            		int x = 0, y = 0, b = 0;
            		switch(type) {
            		case(TYPE_ROW):
            			x = m;
            			y = position;
            			break;
            		case(TYPE_COLUMN):
            			x = position;
            			y = m;
            			break;
            		case(TYPE_BLOCK):
            			x = ((m / 3 ) * 3) + (position / 3);
            			y = ((m % 3) * 3) + (position % 3);
            			break;
            		}
            		b = ((x / 3) * 3) + (y / 3);
            		updateArraysAfterSolving(x, y, b, value);
            	}
            }

            /*
            // #2
            // Find if there are only 2 possible fields for 2 values (or 3 for 3, etc.) in section, and fields are the same.
            // Remove these values from other unsolved fields in section, to further eliminate possible values and bring us closer to solution.

            List<Integer> possiblePositionsMultiple1;
            List<Integer> possiblePositionsMultiple2;
            List<Integer> possiblePositionsIntersection;
            int solvedIntersections = 0;

            for (int num1 = 1; num1 <= 9; num1++) {
                for (int num2 = 1; num2 <= 9; num2++) {

                	if (num2 == num1)
                		continue;

                    if (possibleValuesPositionsMap.containsKey(num1) && possibleValuesPositionsMap.containsKey(num2)) {
                        possiblePositionsMultiple1 = possibleValuesPositionsMap.get(num1);
                        possiblePositionsMultiple2 = possibleValuesPositionsMap.get(num2);
                        possiblePositionsIntersection = ListUtils.intersection(possiblePositionsMultiple1, possiblePositionsMultiple2);

                        // TODO: napraviti istu petlju za 3 (za 4 mislim da ne treba... ali kroz testove æu ustanoviti)
                        if (possiblePositionsIntersection.size() == 2) {
                        	for (int possibleValue : possiblePositionsIntersection) {
	                            for (int n = 0; n < 9; n++) {		// iterate through columns in the row / rows in the column / fields in the block

	                            	// if we found that for 2 numbers ony 2 positions are possible (-> intersection),
	                            	// remove these numbers from other positions in section
	                                if (possibleValuesArray[n] != null && !possiblePositionsIntersection.contains(Integer.valueOf(n))) {
	                                    possibleValuesArray[n].remove(Integer.valueOf(num1));
	                                    possibleValuesArray[n].remove(Integer.valueOf(num2));
	                                    solvedIntersections++;
	                                }
	                            }
                            }
                        }
                    }
                }
            }
            if (solvedIntersections > 0) {
	        	logger.debug("Possible values board (after solving intersections):");
	        	BoardUtils.printPossibleValuesBoard(possibleValuesBoard);
            }
            */
        }

        logger.info("Step " + step + "...solved " + solutions + " fields, remaining " + BoardUtils.unsolvedFieldsRemaining(board) + " fields");
        if (solutions > 0) {
            return solveSections(board, type);
        }
        return board;
    }

    private void updateArraysAfterSolving(int x, int y, int b, int value) {
        unsolvedBoard[x][y] = value;
        possibleValuesBoard[x][y] = null;

        // remove solved value from other fields in sections
        for (List<Integer> possibleValues : getPossibleValuesRow(possibleValuesBoard, x)) {
            if (possibleValues != null) {
                possibleValues.remove(Integer.valueOf(value));
                if (possibleValues.size() == 0) {
                    possibleValues = null;
                }
            }
        }
        for (List<Integer> possibleValues : getPossibleValuesColumn(possibleValuesBoard, y)) {
            if (possibleValues != null) {
                possibleValues.remove(Integer.valueOf(value));
                if (possibleValues.size() == 0) {
                    possibleValues = null;
                }
            }
        }
        for (List<Integer> possibleValues : getPossibleValuesBlock(possibleValuesBoard, b)) {
            if (possibleValues != null) {
                possibleValues.remove(Integer.valueOf(value));
                if (possibleValues.size() == 0) {
                    possibleValues = null;
                }
            }
        }

        // decrease numberOfPossibleValuesBoard fields after updating possibleValuesBoard
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                List<Integer> possibleValues = possibleValuesBoard[row][column];
                if (possibleValues == null) {
                    numberOfPossibleValuesBoard[row][column] = 0;
                } else {
                    numberOfPossibleValuesBoard[row][column] = possibleValues.size();
                }
            }
        }
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
        for (int x = x1; x < x1 + 3; x++) {
            for (int y = y1; y < y1 + 3; y++) {
                block[i] = possibleValuesBoard[x][y];
                i++;
            }
        }
        return block;
    }

}
