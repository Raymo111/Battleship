
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-05-30
 * Description: Battleship squares
 */
import java.io.Serializable;

public class Square implements Serializable {

	private static final long serialVersionUID = -4896377842037028342L;

	public int graphArray[] = new int[Battleship.shipSizes.length];
	public int totalSquareValue;
	public SquareTypes status;
	public int x, y;

	/**
	 * sets contains to EMPTY
	 */
	public Square(int x, int y) {
		status = SquareTypes.UNKNOWN;
	}

	public int[] returnGraphArray() {
		return graphArray;
	}
}