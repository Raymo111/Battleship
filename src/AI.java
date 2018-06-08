
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-05-30
 * Description: The artificial intelligence used to select where to fire
 */
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class AI {
	private static Random rand = new Random();
	private static Scanner sc = new Scanner(System.in);

	/**
	 * Generates the initial population density for the given number of ships for a
	 * given square. Runs only at the beginning of the game.
	 * 
	 * @param shipLength
	 *            The length of a ship to calculate the PD for
	 * @param distance
	 *            The distances of a square to the edges of the grid (0 for a square
	 *            on the edge)
	 * @return The PD of a square
	 */
	public static int generatePD(int shipLength, int[] distance) {
		int pD = 2 * shipLength; // Population density of a square
		for (int l = 0; l < distance.length; l++)
			if (distance[l] < shipLength - 1)
				pD -= (shipLength - 1 - distance[l]);
		return pD;
	}

	/**
	 * Generates the initial population density distributed graph for the given
	 * number of ships for each square in a given grid. Runs only at the beginning
	 * of the game.
	 */
	public static void generatePDDG(Square[][] grid) {
		int[] distance;
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				for (int k : Battleship.shipLengths) {
					distance = new int[] { i, j, grid.length - i - 1, grid[i].length - j - 1 };
					grid[i][j].totalSquareValue += generatePD(k, distance);
				}
	}

	/**
	 * Finds the highest likely location of a ship
	 * 
	 * @return
	 */
	public static Square target(Square[][] grid) {
		int max = grid[0][0].totalSquareValue;
		Square target = grid[0][0];
		for (int i = 0; i < grid[0].length; i++)
			for (int j = 0; j < grid.length; j++)
				if ((i + j) % 2 == 0 && grid[i][j].status == SquareTypes.UNKNOWN && grid[i][j].totalSquareValue > max)
					target = grid[i][j];
		return target;
	}

	/**
	 * Generates where to place the ships on the home grid
	 */
	public static void placeShips(int passedMode, Square[][] grid, int[] shipLengths) {
		System.out.println("Select the mode. \n1-Corners \n2-PDM\n3-Random");

		int mode = 0;

		try {
			mode = sc.nextInt();
		} catch (Exception e) {
			System.err.println(e);
		}

		if (mode == 1) {// Corner priority mode ship placement
			Battleship.homeShips[4] = new Ship(grid[5][9], grid[9][9]);// Carrier in bottom right
		} else if (mode == 2) {// PDDG placement
			int[] totalArray = new int[grid.length * grid[0].length];
			int index = 0;
			for (int i = 0; i < grid[0].length; i++) {
				for (int j = 0; j < grid.length; j++) {
					totalArray[index] = grid[i][j].totalSquareValue;
					index++;
				}
			}
			Arrays.sort(totalArray);
			for (int i = 0; i < shipLengths.length; i++) {
				boolean correct = false;
				int count = 0;
				System.out.println(i + 1);
				int y = rand.nextInt(grid.length - 1);// random x coordinate. Start of ship
				int x = rand.nextInt(grid[0].length - 1);// random y coordinate. Start of ship

				do {
					correct = false;
					if (count > 4) {
						System.out.println("Over");
						y = rand.nextInt(grid.length - 1);// random y coordinate. Start of ship
						x = rand.nextInt(grid[0].length - 1);// random x coordinate. Start of ship

					}

					int rotation = rand.nextInt(4);// random int to represent the orientation of the ship
					if (rotation == 0) {// ship vertical down
						if (checkValidShipPosition(y, x, y + shipLengths[i], x, rotation, grid)) {
							correct = true;
							count = 0;
							System.out.println(x + "," + y);
							// for (int j = 0; j < shipLengths[i]; j++)
							// homeShipPlacement[y + j][x] = shipLengths[i];
						}
					} else if (rotation == 1) {// ship vertical up
						if (checkValidShipPosition(y, x, y - shipLengths[i], x, rotation, grid)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							// for (int j = 0; j < shipLengths[i]; j++)
							// homeShipPlacement[y - j][x] = shipLengths[i];
						}
					} else if (rotation == 2) {// ship horizontal to right
						if (checkValidShipPosition(y, x, y, x + shipLengths[i], rotation, grid)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							// for (int j = 0; j < shipLengths[i]; j++)
							// homeShipPlacement[y][x + j] = shipLengths[i];
						}
					} else if (rotation == 3)// ship horizontal to left
						if (checkValidShipPosition(y, x, y, x - shipLengths[i], rotation, grid)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							// for (int j = 0; j < shipLengths[i]; j++)
							// homeShipPlacement[y][x - j] = shipLengths[i];
						}
					count++;
				} while (correct == false);
			}

		} else if (mode == 3)

		{// random ship placement
			for (int i = 0; i < shipLengths.length; i++) {
				boolean correct = false;
				int count = 0;
				System.out.println(i + 1);
				int y = rand.nextInt(grid.length - 1);// random x coordinate. Start of ship
				int x = rand.nextInt(grid[0].length - 1);// random y coordinate. Start of ship
				int rotation = rand.nextInt(4);// random int to represent the orientation of the ship

				do {
					correct = false;
					if (count > 4) {
						System.out.println("Over");
						y = rand.nextInt(grid.length - 1);// random y coordinate. Start of ship
						x = rand.nextInt(grid[0].length - 1);// random x coordinate. Start of ship
						rotation = rand.nextInt(4);// random int to represent the orientation of the ship
						count = 0;
					}
					rotation = (rotation + 1) % 4;
					if (rotation == 0) {// ship vertical down
						if (checkValidShipPosition(y, x, y + shipLengths[i], x, rotation, grid)) {
							correct = true;
							count = 0;
							System.out.println(x + "," + y);
							// for (int j = 0; j < shipLengths[j]; j++)
							// homeShipPlacement[y + j][x] = shipLengths[j];
						}
					} else if (rotation == 1) {// ship vertical up
						if (checkValidShipPosition(y, x, y - shipLengths[i], x, rotation, grid)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							// for (int j = 0; j < shipLengths[j]; j++)
							// homeShipPlacement[y - j][x] = shipLengths[i];
						}
					} else if (rotation == 2) {// ship horizontal to right
						if (checkValidShipPosition(y, x, y, x + shipLengths[i], rotation, grid)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							// for (int j = 0; j < shipLengths[j]; j++)
							// homeShipPlacement[y][x + j] = shipLengths[j];
						}
					} else if (rotation == 3)// ship horizontal to left
						if (checkValidShipPosition(y, x, y, x - shipLengths[i], rotation, grid)) {
							count = 0;
							correct = true;
							System.out.println(x + "," + y);
							// for (int j = 0; j < shipLengths[j]; j++)
							// homeShipPlacement[y][x - j] = shipLengths[j];
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
	public static boolean checkValidShipPosition(int Y, int X, int endY, int endX, int rotation, Square[][] grid) {
		// first we must check to see if the end values are within the board size
		if (endX >= grid.length || endX < 0 || endY >= grid[0].length || endY < 0) {
			return false;
		}
		int sign = -1;
		if (rotation % 2 == 0) {
			sign = 1;
		}
		for (int i = 0; i <= Math.abs(endX - X); i++)
			for (int j = 0; j <= Math.abs(endY - Y); j++) {
				// if (homeShipPlacement[Y + (sign * j)][X + (sign * i)] != 0) {
				// System.out.println("overlace");
				// return false;
				// }
			}

		return true;
	}

}