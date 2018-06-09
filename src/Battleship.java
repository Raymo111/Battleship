/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */

public class Battleship {
	public static int shipLengths[] = { 2, 3, 3, 4, 5 };// Each index represents the size of a individual ship

	// Each index represents the size of a individual ship
	public static String shipNames[] = { "Destroyer", "Cruiser", "Submarine", "Battleship", "Aircraft Carrier" };
	public static int boardSizeXY[] = { 10, 10 };// x and y size of the board
	public static boolean[] isShipSunk = new boolean[shipLengths.length];// boolean values of if a ship is sunk
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// state of enemy grid
	public static Square[][] homeGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// state of home grid
	public static Ship[] homeShips = new Ship[shipLengths.length];

	public static void main(String[] args) {
		// while (true)
		newGameProcedure();
	}

	/**
	 * Functions that only need to be run at the beginning of a new game
	 */
	public static void newGameProcedure() {
		for (int i = 0; i < enemyGrid.length; i++)
			for (int j = 0; j < enemyGrid[i].length; j++)
				enemyGrid[i][j] = new Square(i, j);
		for (int i = 0; i < homeGrid.length; i++)
			for (int j = 0; j < homeGrid[i].length; j++)
				homeGrid[i][j] = new Square(i, j);
		AI.generatePDDG(enemyGrid);
		AI.generatePDDG(homeGrid);
		AI.placeShips(homeGrid, shipLengths);
		display2Darray(homeGrid);
		display2Darray(enemyGrid);
	}

	public static void display2Darray(Square[][] array) {
		try {
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array.length; j++) {
					System.out.print("|" + array[i][j].totalSquareValue);
				}
				System.out.println();
			}
		} catch (Exception e) {
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				System.out.print("|" + array[i][j].shipType);
			}
			System.out.println();
		}
	}

	public static void display2Darray(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				System.out.print("|" + array[j][i]);
			}
			System.out.println();
		}
	}
}