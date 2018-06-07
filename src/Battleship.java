
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Battleship {
	public static Random rand = new Random();
	public static Scanner input = new Scanner(System.in);
	public static int shipSizes[] = { 2, 3, 3, 4, 5 };// Each index represents the size of a individual ship
	public static int boardSizeXY[] = { 10, 10 };// x and y size of the board
	public static int numberOfShips = shipSizes.length;
	public static boolean[] isShipSunk = new boolean[numberOfShips];// boolean values of id a ship is sunk
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];;// state of enemy grid
	public static SquareTypes homeGrid[][] = new SquareTypes[boardSizeXY[0]][boardSizeXY[1]];// state of home grid
	public static int homeShipPlacement[][] = new int[boardSizeXY[0]][boardSizeXY[1]];// location of home ships
	// homeShipPlacement[X-coordinate][Y-coordinate]
	// #0=Empty #5=Carrier-5 #4=Battleship-4 #3=Cruiser-3 #2=Submarine-3
	// #1=Destroyer-2

	public static void main(String[] args) {
		for (int i = 0; i < enemyGrid.length; i++)
			for (int j = 0; j < enemyGrid[i].length; j++)
				enemyGrid[i][j] = new Square();
		AI.generatePDDG(enemyGrid);
		placeShips(3);
		display2Darray(homeShipPlacement);
		display2Darray(enemyGrid);

	}

	/**
	 * Functions that only need to be run at the beginning of a new game
	 */
	public void newGameProcedure() {

	}

	/**
	 * find the highest likely location of a ship
	 * 
	 * @return
	 */
	public int[] findHighestPD() {
		int[] currentTarget = { 0, 0 };
		int max = enemyGrid[0][0].totalSquareValue;
		for (int x = 0; x < enemyGrid[0].length; x++)
			for (int y = 0; y < enemyGrid.length; y++)
				if ((x + y) % 2 == 0)
					if (enemyGrid[x][y].status == SquareTypes.UNKNOWN)
						if (enemyGrid[x][y].totalSquareValue > max) {
							currentTarget[0] = x;
							currentTarget[1] = y;
						}
		return currentTarget;
	}

	public static void refreshTotalSquareValue() {
		int temp = 0;
		for (int x = 0; x < enemyGrid[0].length; x++)
			for (int y = 0; y < enemyGrid.length; y++) {
				for (int j2 = 0; j2 < numberOfShips; j2++)
					temp += enemyGrid[x][y].graphArray[j2];
				enemyGrid[x][y].totalSquareValue = temp;
				temp = 0;
			}
	}

	/**
	 * Generates where to place the ships on the home grid
	 */
	public static void placeShips(int passedMode) {
		/*
		 * int totalGrid[] = new int[boardSizeXY[0] * boardSizeXY[1]]; int counter = 0;
		 * int maxMin = 0; for (int i = 0; i < shipSizes.length; i++) { maxMin +=
		 * shipSizes[i] * 2; } for (int x = 0; x < boardSizeXY[0]; x++) { for (int y =
		 * 0; y < boardSizeXY[1]; y++) { if (enemyGrid[x][y].totalSquareValue < maxMin)
		 * { totalGrid[counter] = enemyGrid[x][y].totalSquareValue; }
		 * 
		 * } } Arrays.sort(totalGrid);
		 */
		System.out.println("Select the mode. \n1-Corners \n2-PDM\n3-Random");

		int mode = 0;

		try {
			mode = input.nextInt();
		} catch (Exception e) {
			System.err.println(e);
		}

		if (mode == 1) {// Corner priority mode ship placement
			homeShipPlacement[boardSizeXY[0] - 1][boardSizeXY[1] - 5] = 5;
			homeShipPlacement[boardSizeXY[0] - 1][boardSizeXY[1] - 4] = 5;
			homeShipPlacement[boardSizeXY[0] - 1][boardSizeXY[1] - 3] = 5;
			homeShipPlacement[boardSizeXY[0] - 1][boardSizeXY[1] - 2] = 5;
			homeShipPlacement[boardSizeXY[0] - 1][boardSizeXY[1] - 1] = 5;
			homeShipPlacement[boardSizeXY[0] - 3][boardSizeXY[1] - 1] = 1;
			homeShipPlacement[boardSizeXY[0] - 3][boardSizeXY[1] - 2] = 1;

			// Custom placement of two ships for a specific case.

		} else if (mode == 2) {// PDDG placement
			int[] totalArray = new int[boardSizeXY[0] * boardSizeXY[1]];
			int index = 0;
			for (int i = 0; i < enemyGrid[0].length; i++) {
				for (int j = 0; j < enemyGrid.length; j++) {
					totalArray[index] = enemyGrid[i][j].totalSquareValue;
					index++;
				}
			}
			Arrays.sort(totalArray);
			for (int i = 0; i < shipSizes.length; i++) {
				boolean correct = false;
				int count = 0;
				System.out.println(i + 1);
				int y = rand.nextInt(boardSizeXY[0] - 1);// random x coordinate. Start of ship
				int x = rand.nextInt(boardSizeXY[1] - 1);// random y coordinate. Start of ship

				do {
					correct = false;
					if (count > 4) {
						System.out.println("Over");
						y = rand.nextInt(boardSizeXY[0] - 1);// random y coordinate. Start of ship
						x = rand.nextInt(boardSizeXY[1] - 1);// random x coordinate. Start of ship

					}

					int rotation = rand.nextInt(4);// random int to represent the orientation of the ship
					if (rotation == 0) {// ship vertical down
						if (checkValidShipPosition(y, x, y + shipSizes[i], x, rotation)) {
							correct = true;
							count = 0;
							System.out.println(x + "," + y);
							for (int j = 0; j < shipSizes[i]; j++)
								homeShipPlacement[y + j][x] = shipSizes[i];
						}
					} else if (rotation == 1) {// ship vertical up
						if (checkValidShipPosition(y, x, y - shipSizes[i], x, rotation)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							for (int j = 0; j < shipSizes[i]; j++)
								homeShipPlacement[y - j][x] = shipSizes[i];
						}
					} else if (rotation == 2) {// ship horizontal to right
						if (checkValidShipPosition(y, x, y, x + shipSizes[i], rotation)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							for (int j = 0; j < shipSizes[i]; j++)
								homeShipPlacement[y][x + j] = shipSizes[i];
						}
					} else if (rotation == 3)// ship horizontal to left
						if (checkValidShipPosition(y, x, y, x - shipSizes[i], rotation)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							for (int j = 0; j < shipSizes[i]; j++)

								homeShipPlacement[y][x - j] = shipSizes[i];
						}
					count++;
				} while (correct == false);
			}

		} else if (mode == 3)

		{// random ship placement
			for (int i = 0; i < shipSizes.length; i++) {
				boolean correct = false;
				int count = 0;
				System.out.println(i + 1);
				int y = rand.nextInt(boardSizeXY[0] - 1);// random x coordinate. Start of ship
				int x = rand.nextInt(boardSizeXY[1] - 1);// random y coordinate. Start of ship
				int rotation = rand.nextInt(4);// random int to represent the orientation of the ship

				do {
					correct = false;
					if (count > 4) {
						System.out.println("Over");
						y = rand.nextInt(boardSizeXY[0] - 1);// random y coordinate. Start of ship
						x = rand.nextInt(boardSizeXY[1] - 1);// random x coordinate. Start of ship
						rotation = rand.nextInt(4);// random int to represent the orientation of the ship
						count = 0;
					}
					rotation = (rotation + 1) % 4;
					if (rotation == 0) {// ship vertical down
						if (checkValidShipPosition(y, x, y + shipSizes[i], x, rotation)) {
							correct = true;
							count = 0;
							System.out.println(x + "," + y);
							for (int j = 0; j < shipSizes[i]; j++)
								homeShipPlacement[y + j][x] = shipSizes[i];
						}
					} else if (rotation == 1) {// ship vertical up
						if (checkValidShipPosition(y, x, y - shipSizes[i], x, rotation)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							for (int j = 0; j < shipSizes[i]; j++)
								homeShipPlacement[y - j][x] = shipSizes[i];
						}
					} else if (rotation == 2) {// ship horizontal to right
						if (checkValidShipPosition(y, x, y, x + shipSizes[i], rotation)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							for (int j = 0; j < shipSizes[i]; j++)
								homeShipPlacement[y][x + j] = shipSizes[i];
						}
					} else if (rotation == 3)// ship horizontal to left
						if (checkValidShipPosition(y, x, y, x - shipSizes[i], rotation)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							for (int j = 0; j < shipSizes[i]; j++)

								homeShipPlacement[y][x - j] = shipSizes[i];
						}
					count++;
				} while (correct == false);
			}
		}

	}

	/**
	 * Check to see if a ship placement is in a valid position
	 * 
	 * @param X
	 *            the starting x coordinate that is always within the grid
	 * @param Y
	 *            the starting y coordinate that is always within the grid
	 * @param endX
	 *            the ending x coordinate for the ship that might be out of the
	 *            board
	 * @param endY
	 *            the ending y coordinate for the ship that might be out of the
	 *            board
	 * @param rotation
	 *            the orientation of the ship from x,y to endX,endY. 0=down 1=up
	 *            2=right 3=left
	 * @return If true, the ship is inside the grid and is not overlapping any other
	 *         ship. If false, the ships placement is either outside of the board or
	 *         is overlapping another ship
	 */
	public static boolean checkValidShipPosition(int Y, int X, int endY, int endX, int rotation) {
		// first we must check to see if the end values are within the bourd size
		if (endX >= boardSizeXY[0] || endX < 0 || endY >= boardSizeXY[1] || endY < 0) {
			return false;
		}
		int sign = -1;
		if (rotation % 2 == 0) {
			sign = 1;
		}
		for (int i = 0; i <= Math.abs(endX - X); i++)
			for (int j = 0; j <= Math.abs(endY - Y); j++) {
				if (homeShipPlacement[Y + (sign * j)][X + (sign * i)] != 0) {

					System.out.println("overlace");
					return false;
				}
			}

		return true;
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