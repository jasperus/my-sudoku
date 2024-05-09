package sudoku.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sudoku.model.Board;
import sudoku.model.BoardExamples;

public class Test {

	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		//isSolved();
	}

	private static void isSolved() {
		BoardExamples examples = new BoardExamples();
		boolean isSolved_1 = BoardUtils.isSolved(examples.board1);
		boolean isSolved_4 = BoardUtils.isSolved(examples.board4);
		boolean isSolved_30_1 = BoardUtils.isSolved(examples.boardDemo1_cleaned30_1);
		System.out.println("isSolved_1:" + isSolved_1);
		System.out.println("isSolved_4:" + isSolved_4);
		System.out.println("isSolved_30_1:" + isSolved_30_1);
	}

	public static void compareBoards() {
		BoardExamples misc = new BoardExamples();
		logger.info("Comparing boards...");

		Board board1 = new Board();
		board1.setBoard(misc.board1);
		Board board2 = new Board();
		board2.setBoard(misc.board2);
		Board board3 = new Board();
		board3.setBoard(misc.board3);

		logger.info("board1.equals(board2):" + board1.equals(board2));
		logger.info("board1.equals(board3):" + board1.equals(board3));
	}

}
