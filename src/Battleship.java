/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
public class Battleship {

	public static int shipSizes[] = { 2, 3, 4, 4, 5 };
	public static int boardSizeXY[] = { 10, 10 };
	public static int numberOfShips = shipSizes.length;
	public static boolean[] isShipSunk = new boolean[numberOfShips];
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];;
	public static SquareTypes homeGrid[][] = new SquareTypes[boardSizeXY[0]][boardSizeXY[1]];

	public static void main(String[] args) {
		AI.generatePDDG();

	}

	/**
	 * Functions that only need to be run at the beginning of a new game
	 */
	public void newGameProcedure() {

	}
}
