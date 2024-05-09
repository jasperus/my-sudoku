package sudoku.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import sudoku.util.BoardUtils;

@Slf4j
public class Solver {

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

    int[][] partiallySolvedBoard;

    private int step;

    public int[][] solveBoard(int[][] board) {
        this.unsolvedBoard = board;
        numberOfPossibleValuesBoard = new int[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                possibleValuesBoard[x][y] = new ArrayList<>();
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
                    possibleValues = new ArrayList<>();
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

        	log.info("#{}.1 - Solving one solution fields...", loop);
        	partiallySolvedBoard = solveOneSolutionFields(unsolvedBoard);
        	if (BoardUtils.isSolved(partiallySolvedBoard))
        		return partiallySolvedBoard;
        	printStep();

        	// mo�da nam ova 3 ne trebaju, nisu rije�ila ni�ta (?)
        	// dodu�e, za sada sam pokrenuo samo za jedan "cleared board"
        	log.info("#{}.2 - Solving rows...", loop);
        	partiallySolvedBoard = solveSections(partiallySolvedBoard, TYPE_ROW);
        	if (BoardUtils.isSolved(partiallySolvedBoard))
        		return partiallySolvedBoard;
        	printStep();

        	log.info("#{}.3 - Solving columns...", loop);
        	partiallySolvedBoard = solveSections(partiallySolvedBoard, TYPE_COLUMN);
        	if (BoardUtils.isSolved(partiallySolvedBoard))
        		return partiallySolvedBoard;
        	printStep();

        	log.info("#{}.4 - Solving blocks...", loop);
        	partiallySolvedBoard = solveSections(partiallySolvedBoard, TYPE_BLOCK);
        	if (BoardUtils.isSolved(partiallySolvedBoard))
        		return partiallySolvedBoard;
        	printStep();

        } while (numOfSolutionsForStep > 0);

        return partiallySolvedBoard;
    }

    private void printStep() {
    	log.debug("Partially solved board:");
    	BoardUtils.printBoard(partiallySolvedBoard);
//    	logger.debug("Number of possible values:");
//    	BoardUtils.printBoard(numberOfPossibleValuesBoard);
//    	logger.debug("Possible values board:");
//    	BoardUtils.printPossibleValuesBoard(possibleValuesBoard);
    }

    private int[][] solveOneSolutionFields(int[][] board) {
        step++;
        List<Integer> possibleValues;
        int numSolvedFields = 0;
        int boardIndex;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y <9; y++) {
                if (numberOfPossibleValuesBoard[x][y] == 1) {
                    numSolvedFields++;
                    numOfSolutionsForStep++;
                    possibleValues = possibleValuesBoard[x][y];
                    boardIndex = ((x / 3) * 3) + (y / 3);
                    updateArraysAfterSolving(x, y, boardIndex, possibleValues.get(0));
                }
            }
        }
        log.info("Step {}...solved {} fields, remaining {} fields",
                step, numSolvedFields, BoardUtils.unsolvedFieldsRemaining(board));
//        logger.debug("Possible values board:");
//        BoardUtils.printPossibleValuesBoard(possibleValuesBoard);
        if (numSolvedFields > 0) {
            return solveOneSolutionFields(board);
        }
        return board;
    }

    private int[][] solveSections(int[][] board, String type) {
        step++;
        int numSolvedFields = 0;
        List<Integer>[] possibleValuesArray;
        List<Integer> possibleValuesPositions;

        // *** Inverted map ***
        // it does not contain that for positions 5 and 7 in section possible values are 2 and 3
        // it does contain that for values 2 and 3 possible positions are 5 and 7
        Map<Integer,List<Integer>> possibleValuesPositionsMap;

        // FIXME: ovdje je ne�to jako krivo...nemogu�e da je uspio rije�iti 12 polja u prvom stepu, a ova beskona�na petlja nakon toga...uh
        // #1.2 - Solving rows...
        // Step 5...solved 12 fields
        // Step 6...solved 7 fields
        // Step 7...solved 7 fields

        // #1
        // Find if there is only one position in unsolved fields for given value in section.
        // If found, solve field and remove value from other unsolved fields in section.
        for (int m = 0; m < 9; m++) {	// iterate through rows / columns / blocks
            possibleValuesArray = getPossibleValuesSection(possibleValuesBoard, m, type);
            possibleValuesPositionsMap = new HashMap<>();

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
            		numSolvedFields++;
            		numOfSolutionsForStep++;
            		value = entry.getKey();
            		position = entry.getValue().get(0);

            		int x = 0, y = 0, b;
                    switch (type) {
                        case (TYPE_ROW) -> {
                            x = m;
                            y = position;
                        }
                        case (TYPE_COLUMN) -> {
                            x = position;
                            y = m;
                        }
                        case (TYPE_BLOCK) -> {
                            x = ((m / 3) * 3) + (position / 3);
                            y = ((m % 3) * 3) + (position % 3);
                        }
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

                        // TODO: napraviti istu petlju za 3 (za 4 mislim da ne treba... ali kroz testove �u ustanoviti)
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

        log.info("Step {}...solved {} fields, remaining {} fields",
                step, numSolvedFields, BoardUtils.unsolvedFieldsRemaining(board));
        if (numSolvedFields > 0) {
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
        List<Integer>[] section = switch (type) {
            case (TYPE_ROW) -> getPossibleValuesRow(board, index);
            case (TYPE_COLUMN) -> getPossibleValuesColumn(board, index);
            case (TYPE_BLOCK) -> getPossibleValuesBlock(board, index);
            default -> null;
        };
        return section;
    }

    private List<Integer>[] getPossibleValuesRow(List<Integer>[][] possibleValuesBoard, int rowIndex) {
        @SuppressWarnings("unchecked")
        List<Integer>[] row = new ArrayList[9];
        System.arraycopy(possibleValuesBoard[rowIndex], 0, row, 0, BOARD_SIZE);
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
