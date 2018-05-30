
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-05-30
 * Description: Battleship squares
 */

import java.io.Serializable;

public class Square implements Serializable {

	private static final long serialVersionUID = -4896377842037028342L;

	public int graphArray[] = new int[Battleship.numberOfShips];
	private SquareTypes contains;
	private boolean isAMine = false;

	/**
	 * sets contains to EMPTY
	 */
	public Square() {
		contains = SquareTypes.EMPTY;
	}

	/**
	 * sets isAMine to true
	 */
	public void setToMine() {
		isAMine = true;
	}

	/**
	 * sets isAMine to false
	 */
	public void removeMine() {
		isAMine = false;
	}

	/**
	 * return
	 */
	public boolean checkMine() {
		return isAMine;
	}

	/**
	 * returns to value of contains
	 */
	public SquareTypes getMineType() {
		return contains;
	}

	/**
	 * sets the value of contains to type
	 */
	public void changeType(SquareTypes type) {
		contains = type;
	}
}