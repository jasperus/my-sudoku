package sudoku;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sudoku {

	private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {

//		Statistics stats = new Statistics();
//		stats.howManyRandomsDoesItTake();

		Generator generator = new Generator();

		for (int i = 1; i <= 10; i++) {
			logger.info("BOARD " + i + ":");
			generator.initBoard();
			int[][] newBoard = generator.createBoard(0,0);
			generator.printBoard(newBoard);
		}
	}

}
