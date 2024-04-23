package work;

import model.BoardExamples;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.BoardUtils;
import util.Statistics;

public class Sudoku {

	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) {

//		cleanBoardIncrementalAndTestForSolutions();
//		cleanBoardFixedAndTestForSolutions(53, 100);
//		cleanAndSolveBoard(30, 1);

		BoardExamples examples = new BoardExamples();

//		testCleanedBoardsForSolutions(examples.boardDemoCleaned_30_3)
//		testCleanedBoardsForSolutions(examples.boardDemoCleaned_70_2)
//		testCleanedBoardsForSolutions(examples.hardestBoardCleaned);		// za pronalaženje rješenja ovog boarda treba 18 sec

//		solveBoard(examples.hardestBoard, examples.hardestBoardCleaned);	// ovo ipak ostaviti za kraj, biti će jako teško napraviti algoritam koji ovo uspijeva riješiti
//		solveBoard(examples.testBoard, examples.testBoard_cleaned_50_1);	// ovo uspije rije�iti sa osnovnim algoritmom
		solveBoard(examples.sudokuExample_hard_cleaned);			// imam grešku u algoritmu, ostala su samo 3 polja za riješiti, ali izgleda da je krivo riješilo, pa je došlo do nemoguće situacije

	}

	// pass cleaned board that has unique solution
	private static void solveBoard(int[][] cleanedBoard) {
		Solutions solutions = new Solutions();
		Solver solver = new Solver();

		logger.info("Cleaned board:");
		BoardUtils.printBoard(cleanedBoard);

		solutions.findPossibleSolutions(cleanedBoard);
		if (solutions.getNumOfSolutions() > 1) {
			logger.info("Found " + solutions.getNumOfSolutions() + " possible solutions");
			return;
		}

		int[][] solvedBoard = solver.solveBoard(cleanedBoard);

		if (BoardUtils.isSolved(solvedBoard)) {
			logger.info("\n*** Board is solved ***");
		} else {
			logger.info("\n*** Board is not solved ***");
		}
		BoardUtils.printBoard(solvedBoard);
	}

	private static void solveBoard(int[][] board, int[][] cleanedBoard) {
		Solutions solutions = new Solutions();
		Solver solver = new Solver();

		logger.info("Generated board:");
		BoardUtils.printBoard(board);

		logger.info("Cleaned board:");
		BoardUtils.printBoard(cleanedBoard);
		solutions.findPossibleSolutions(cleanedBoard);
		// FIXME: dok nisam istitrao algoritam, neće riješiti do kraja,
		// ali će ipak isprintati, jer board zapravo ima samo jedno rješenje
		// razmišljao sam u budućnost...
		if (solutions.getNumOfSolutions() == 1) {
			logger.info("Found 1 possible solution:");
			BoardUtils.printBoard(solutions.getSolvedBoards().get(0).getBoard());

			int[][] solvedBoard = solver.solveBoard(cleanedBoard);
			logger.info("\n*** Solved board ***");
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
		logger.debug("Generated board:");
		BoardUtils.printBoard(board);

		// #2 - clean generated board
		System.out.println("\nClean board - Empty fields: " + numOfEmptyFields + "\n");
		for (int i = 0; i < iterations; i++) {

			int[][] cleanedBoard = cleaner.cleanBoard(BoardUtils.cloneArray(board), numOfEmptyFields);

			solutions.findPossibleSolutions(cleanedBoard);
			logger.info("#" + String.format("%0" + (int)(Math.log10(iterations)+1) + "d", (i+1))+ " iteration - possible solutions: " + solutions.getNumOfSolutions());

			if (solutions.getNumOfSolutions() == 1) {
				logger.debug("Cleaned board:");
				BoardUtils.printBoard(cleanedBoard);
				//BoardUtils.printBoard(solutions.getSolvedBoards().get(0).getBoard());
			}
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
