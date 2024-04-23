package work;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cleaner {

	private static final Logger logger = LogManager.getLogger();

	private static final int NO_VALUE = 0;
    private static final int BOARD_SIZE = 9;

	private int[][] board;

	private List<Integer> emptyPositions;

	// TODO: eventualno doraditi da po težini miče polja po boardovima (dodatno proučiti literaturu na što treba paziti)
    public int[][] cleanBoard(int[][] board, int numEmptyFields) {
    	this.board = board;
    	this.emptyPositions = new ArrayList<Integer>();
    	return cleanBoard(numEmptyFields);
    }

    public int[][] cleanBoard(int numFieldsForCleaning) {
        Random rand = new Random();
        int random;

        int numCleanFields = 0;

        do {
             random = rand.nextInt(BOARD_SIZE * BOARD_SIZE);
             if (!emptyPositions.contains(random)) {
                 emptyPositions.add(random);
                 numCleanFields++;
             }
        } while (numCleanFields < numFieldsForCleaning);

        Collections.sort(emptyPositions);

        logger.trace("Empty positions:" + emptyPositions.toString());

        int emptyPosition, emptyX, emptyY;
        int emptyPositionsSize = emptyPositions.size();
        for (int i = 0; i < emptyPositionsSize; i++) {
            emptyPosition = emptyPositions.get(i);
            emptyX = emptyPosition / 9;
            emptyY = emptyPosition % 9;
            logger.trace("Position:" + emptyPosition + ", X=" + emptyX + ", Y=" + emptyY);

            board[emptyX][emptyY] = NO_VALUE;
        }
        return board;
    }
}
