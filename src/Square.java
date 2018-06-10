/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-05-30
 * Description: Battleship squares
 */

public class Square {

	public int totalSquarePD, huntPDx, huntPDy, targetPDx, targetPDy;
	public SquareTypes status;
	public int x, y;
	public Ship shipType; // #0 for no ship, #n for n-length ship

	/**
	 * sets contains to EMPTY
	 */
	public Square(int startX, int startY) {
		status = SquareTypes.UNKNOWN;
		x = startX;
		y = startY;
	}

	public void combinehuntPDXY() {
		totalSquarePD = huntPDx + huntPDy;
	}

	public void combinetargetPDXY() {
		totalSquarePD = targetPDx + targetPDy;
	}
}