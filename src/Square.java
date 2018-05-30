
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

	/**
	 * sets contains to EMPTY
	 */
	public Square() {
		contains = SquareTypes.UNKNOWN;
	}

	/**
	 * returns to value of contains
	 */
	public SquareTypes getSquareType() {
		return contains;
	}

	/**
	 * sets the value of contains to type
	 */
	public void changeSquareType(SquareTypes type) {
		contains = type;
	}
}