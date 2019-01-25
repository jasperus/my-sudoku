package work;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.BoardExamples;
import util.BoardUtils;
import util.Statistics;

public class Sudoku {

	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {

//		cleanBoardIncrementalAndTestForSolutions();
//		cleanBoardFixedAndTestForSolutions(55, 100);
//		cleanAndSolveBoard(30, 1);


		BoardExamples examples = new BoardExamples();
//		testCleanedBoardsForSolutions(examples.boardDemoCleaned_30_3)
//		testCleanedBoardsForSolutions(examples.boardDemoCleaned_70_2)
//		testCleanedBoardsForSolutions(examples.hardestBoardCleaned);		// za pronalaženje rješenja ovog boarda treba 18 sec
		solveBoard(examples.boardDemo2, examples.boardDemo2_cleaned_30);

	}

	private static void solveBoard(int[][] board, int[][] cleanedBoard) {
		Solutions solutions = new Solutions();
		Solver solver = new Solver();

		logger.info("Generated board:");
		BoardUtils.printBoard(board);

		logger.info("Cleaned board:");
		BoardUtils.printBoard(cleanedBoard);
		solutions.findPossibleSolutions(cleanedBoard);
		if (solutions.getNumOfSolutions() == 1) {
			int[][] solvedBoard = solver.solveBoard(cleanedBoard);
			logger.info("Solved board:");
			BoardUtils.printBoard(solvedBoard);
		}
	}

	private static void cleanAndSolveBoard(int numOfEmptyFields, int iterations) {
		Generator generator = new Generator();
		Cleaner cleaner = new Cleaner();
		Solutions solutions = new Solutions();
		Solver solver = new Solver();

		// #1 - generate 1 board
		int[][] board = generator.createBoard();
		logger.info("Generated board:");
		BoardUtils.printBoard(board);

		// #2 - clean and solve generated board
		for (int i = 0; i < iterations; i++) {
			int[][] cleanedBoard = cleaner.cleanBoard(BoardUtils.cloneArray(board), numOfEmptyFields);
			logger.info("Cleaned board:");
			BoardUtils.printBoard(cleanedBoard);
			solutions.findPossibleSolutions(cleanedBoard);
			if (solutions.getNumOfSolutions() == 1) {
				int[][] solvedBoard = solver.solveBoard(cleanedBoard);
				logger.info("Solved board:");
				BoardUtils.printBoard(solvedBoard);
			}
		}
	}

	private static void cleanBoardFixedAndTestForSolutions(int numOfEmptyFields, int iterations) {
		Generator generator = new Generator();
		Cleaner cleaner = new Cleaner();
		Solutions solutions = new Solutions();

		// #1 - generate 1 board
		int[][] board = generator.createBoard();

		// #2 - clean generated board
		System.out.println("Empty fields: " + numOfEmptyFields);
		for (int i = 0; i < iterations; i++) {
			int[][] cleanedBoard = cleaner.cleanBoard(BoardUtils.cloneArray(board), numOfEmptyFields);
			solutions.findPossibleSolutions(cleanedBoard);
			logger.info("  Possible solutions: " + solutions.getNumOfSolutions());
		}
	}

	private static void cleanBoardIncrementalAndTestForSolutions() {
		/*
		for (int i = 1; i <= 10; i++) {
			logger.info("BOARD " + i + ":");
			generator.initBoard();
			int[][] newBoard = generator.createBoard(0,0);
			generator.printBoard(newBoard);
		}
		*/

		Generator generator = new Generator();
		Cleaner cleaner = new Cleaner();
		Solutions solutions = new Solutions();

		// #1 - generate 1 board
		int[][] board = generator.createBoard();

		int initNumOfEmptyFields = 5;
		int numOfEmptyFields;

		// #2 - clean generated board
		// go from 5 to 50 empty fields
		// for every number of empty fields, create 10x cleaned version
		for (int i = 0; i < 10; i++) {
			numOfEmptyFields = initNumOfEmptyFields + (i * 5);

			System.out.println("Empty fields: " + numOfEmptyFields);
			for (int j = 0; j < 10; j++) {
				int[][] cleanedBoard = cleaner.cleanBoard(BoardUtils.cloneArray(board), numOfEmptyFields);
//				logger.info("Cleaned board " + i + ": ");
//				BoardUtils.printBoard(cleanedBoard);
				solutions.findPossibleSolutions(cleanedBoard);
//				System.out.println("#" + j);
				System.out.println("  Possible solutions: " + solutions.getNumOfSolutions());

//				Solver solver = new Solver(cleanedBoard);
//				solver.solveBoardLikeHuman();
			}
		}
	}

	private static void testCleanedBoardsForSolutions(int[][] cleanedBoard) {
		logger.info("Unsolved board:");
		BoardUtils.printBoard(cleanedBoard);
		Solutions solutions = new Solutions();
		solutions.findPossibleSolutions(cleanedBoard);
		logger.info("Number of solutions: " + solutions.getNumOfSolutions());
	}

	private static void misc() {
		Statistics stats = new Statistics();
		stats.howManyRandomsDoesItTake();
	}

}
