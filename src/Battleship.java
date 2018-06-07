/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */

public class Battleship {
	public static int shipSizes[] = { 2, 3, 3, 4, 5 };// Each index represents the size of a individual ship

	// Each index represents the size of a individual ship
	public static String shipNames[] = { "Destroyer", "Cruiser", "Submarine", "Battleship", "Aircraft Carrier" };
	public static int boardSizeXY[] = { 10, 10 };// x and y size of the board
	public static boolean[] isShipSunk = new boolean[shipSizes.length];// boolean values of id a ship is sunk
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// state of enemy grid
	public static Square[][] homeGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// state of home grid
	public static int homeShipPlacement[][] = new int[boardSizeXY[0]][boardSizeXY[1]];// location of home ships
	// homeShipPlacement[X-coordinate][Y-coordinate]
	// #0=Empty #5=Carrier-5 #4=Battleship-4 #3=Cruiser-3 #2=Submarine-3
	// #1=Destroyer-2

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
		AI.generatePDDG(enemyGrid);
		AI.placeShips(3, homeGrid, shipSizes);
		display2Darray(homeShipPlacement);
		display2Darray(enemyGrid);
	}

	public static void display2Darray(Square[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				System.out.print("|" + array[j][i].totalSquareValue);
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