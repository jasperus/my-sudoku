package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Statistics {

	private static Logger logger = LogManager.getLogger();

	public void howManyRandomsDoesItTake() {
		for (int i = 0; i < 10; i++) {
//			List<Integer> list = calculateStatisticsBruteForce();
			List<Integer> list = calculateStatisticsOptimized();
			printStatistics(list);
			logger.info("--------------------");
		}
	}

	public List<Integer> calculateStatisticsBruteForce() {
		Random rand = new Random();
		int random;
		int countUntil9;

		List<Integer> countUntil9List = new ArrayList<Integer>();

		for (int i = 0; i < 100; i++) {
			countUntil9 = 0;
			do {
				random = rand.nextInt(9) + 1;
				countUntil9++;
			} while (random != 9);
			countUntil9List.add(countUntil9);
		}

		return countUntil9List;
	}

	public List<Integer> calculateStatisticsOptimized() {
		List<Integer> fullRange = new ArrayList<Integer>();
		for (int i = 1; i <= 9; i++)
			fullRange.add(i);

		List<Integer> range;
		List<Integer> countUntil9List = new ArrayList<Integer>();

		Random rand = new Random();
		int number;
		int countUntil9;
		int position;

		for (int i = 0; i < 100; i++) {
			range = new ArrayList<Integer>(fullRange);
			countUntil9 = 0;
			do {
				position = rand.nextInt(range.size()) + 1;
				number = range.get(position - 1);
				range.remove(position - 1);
				countUntil9++;
			} while (number != 9);
			countUntil9List.add(countUntil9);
		}

		return countUntil9List;
	}

	public void printStatistics(List<Integer> list) {
		logger.info("Maximum:" + Collections.max(list));

		Double average = list
				.stream()
				.mapToInt(val -> val)
				.average()
				.orElse(0.0);
		logger.info("Average:" + average);

		Collections.sort(list);

		double median;
		int listSize = list.size();
		if (listSize % 2 == 0)
		    median = ((double) list.get(listSize/2) + (double) list.get(listSize/2-1))/2;
		else
		    median = (double) list.get(listSize/2);
		logger.info("Median:" + median);
	}
}
