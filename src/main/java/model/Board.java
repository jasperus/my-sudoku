package model;

import java.util.Arrays;
import java.util.List;

/**
 * Model class for storing <code>int[][] board</code>,
 * It has overriden <code>hashCode</code> and <code>equals</code> methods
 * for comparing 2D arrays, so we can create <code>List&lt;Board&gt;</code>
 * and check if the same board already exists in the list.
 *
 * @author nsus04
 */
public class Board {

	private int[][] board;

	/**
	 * 2D array that contains a list of possible values for every empty position,
	 * that are in accordance with Sudoku rules (row/column/subsection).
	 */
	public List<Integer>[][] possibleValues;

	/**
	 * 2D array that contains number of possible values for every empty position.
	 * Effectively, this is size of list in previous variable, for easier calculations.
	 */
	private int[][] numberOfPossibleValues;


	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public List<Integer>[][] getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<Integer>[][] possibleValues) {
		this.possibleValues = possibleValues;
	}

	public int[][] getNumberOfPossibleValues() {
		return numberOfPossibleValues;
	}

	public void setNumberOfPossibleValues(int[][] numberOfPossibleValues) {
		this.numberOfPossibleValues = numberOfPossibleValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(board);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		return true;
	}
}
