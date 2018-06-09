
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

	// Constructor
	public AI() {

		// Generate Population Density Distributed Graph for both grids
		generatePDDG(Battleship.enemyGrid);
		generatePDDG(Battleship.homeGrid);

		// Place ships on home grid
		try {
			// placeShips(Battleship.homeGrid, Battleship.shipLengths);
		} catch (Exception e) {
		}
	}

	/**
	 * Generates where to place ships
	 * 
	 * @param grid
	 *            The grid to place ships on
	 * @param shipLengths
	 *            The lengths of ships to place
	 */
	public static void placeShips(Square[][] grid, int[] shipLengths) {
		System.out.println("Select the mode. \n1-PDM\n2-Random");

		int mode = 0;

		try {
			mode = sc.nextInt();
		} catch (Exception e) {
			System.err.println("Input integer.");
		}

		if (mode == 1) {// PDDG placement
			int[] totalArray = new int[grid.length * grid[0].length];
			int index = 0;
			for (int i = 0; i < grid[0].length; i++) {
				for (int j = 0; j < grid.length; j++) {
					totalArray[index] = grid[i][j].totalSquareValue;
					index++;
				}
			}
			Arrays.sort(totalArray);
			int[][] rotationModifiers = new int[2][4];

			for (int i = 0; i < shipLengths.length; i++) {
				boolean correct = false;
				int count = 0;
				System.out.println(i + 1);
				int y = rand.nextInt(grid.length - 1);// random x coordinate. Start of ship
				int x = rand.nextInt(grid[0].length - 1);// random y coordinate. Start of ship

				rotationModifiers[0][0] = y + shipLengths[i];
				rotationModifiers[1][0] = x;

				rotationModifiers[0][1] = y - shipLengths[i];
				rotationModifiers[1][1] = x;

				rotationModifiers[0][2] = y;
				rotationModifiers[1][2] = x + shipLengths[i];

				rotationModifiers[0][3] = y;
				rotationModifiers[1][3] = x - shipLengths[i];
				do {
					correct = false;
					if (count > 4) {
						System.out.println("Over");
						y = rand.nextInt(grid.length - 1);// random y coordinate. Start of ship
						x = rand.nextInt(grid[0].length - 1);// random x coordinate. Start of ship
					}
					for (int a = 0; a < 4; a++) {// loop
						int rotation = rand.nextInt(4);// random int to represent the orientation of the ship
						if (checkValidShipPosition(y, x, rotationModifiers[0][rotation], rotationModifiers[1][rotation],
								rotation, grid)) {
							correct = true;
							count = 0;
							System.out.println(x + "," + y);
							Battleship.homeShips[i] = new Ship(grid, grid[y][x],
									grid[rotationModifiers[0][rotation]][rotationModifiers[1][rotation]]);
						}
						count++;
					}
				} while (correct == false);
			}
		} else if (mode == 2) {// random ship placement
			for (int i = 0; i < shipLengths.length; i++) {
				boolean correct = false;
				int count = 0;
				System.out.println(i + 1);
				int y = rand.nextInt(grid.length - 1);// random x coordinate. Start of ship
				int x = rand.nextInt(grid[0].length - 1);// random y coordinate. Start of ship
				int[][] rotationModifiers = new int[2][4];
				rotationModifiers[0][0] = y + shipLengths[i];
				rotationModifiers[1][0] = x;

				rotationModifiers[0][1] = y - shipLengths[i];
				rotationModifiers[1][1] = x;

				rotationModifiers[0][2] = y;
				rotationModifiers[1][2] = x + shipLengths[i];

				rotationModifiers[0][3] = y;
				rotationModifiers[1][3] = x - shipLengths[i];
				do {
					correct = false;
					if (count > 4) {
						System.out.println("Over");
						y = rand.nextInt(grid.length - 1);// random y coordinate. Start of ship
						x = rand.nextInt(grid[0].length - 1);// random x coordinate. Start of ship
						rotationModifiers[0][0] = y + shipLengths[i];
						rotationModifiers[1][0] = x;

						rotationModifiers[0][1] = y - shipLengths[i];
						rotationModifiers[1][1] = x;

						rotationModifiers[0][2] = y;
						rotationModifiers[1][2] = x + shipLengths[i];

						rotationModifiers[0][3] = y;
						rotationModifiers[1][3] = x - shipLengths[i];
					}
					for (int a = 0; a < 4; a++) {// loop
						int rotation = rand.nextInt(4);// random int to represent the orientation of the ship

						if (checkValidShipPosition(y, x, rotationModifiers[0][rotation], rotationModifiers[1][rotation],
								rotation, grid)) {
							correct = true;
							count = 0;
							System.out.println(x + "," + y);
							Battleship.homeShips[i] = new Ship(grid, grid[y][x],
									grid[rotationModifiers[0][rotation]][rotationModifiers[1][rotation]]);
						}
						count++;
					}

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
		if (endX >= grid.length || endX < 0 || endY >= grid[0].length || endY < 0)
			return false;
		int sign = -1;
		if (rotation % 2 == 0)
			sign = 1;
		for (int i = 0; i < Math.abs(endX - X); i++)
			for (int j = 0; j < Math.abs(endY - Y); j++)
				if (Battleship.homeGrid[Y + (sign * j)][X + (sign * i)].shipType != 0) {
					System.out.println("overlace");
					return false;
				}

		return true;
	}

	/**
	 * Generates the initial population density distributed graph for the given
	 * number of ships for each square in a given grid. Runs only at the beginning
	 * of the game.
	 * 
	 * @param grid
	 *            The grid for which to calculate the initial PDDG
	 */
	public static void generatePDDG(Square[][] grid) {
		int[] distanceX, distanceY;
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				for (int k : Battleship.shipLengths) {
					distanceX = new int[] { i, grid.length - i - 1 };
					distanceY = new int[] { j, grid[i].length - j - 1 };
					grid[j][i].PDx += generatePD(k, distanceX);
					grid[j][i].PDy += generatePD(k, distanceY);
					grid[j][i].combinePDXY();
				}
	}

	/**
	 * Generates the initial population density for the given number of ships for a
	 * component (horizontal or vertical) of a given square. Runs only at the
	 * beginning of the game.
	 * 
	 * @param shipLength
	 *            The length of a ship to calculate the PD for
	 * @param distance
	 *            The distances of a square to the edges of the grid (0 for a square
	 *            on the edge)
	 * @return The PD of a component (horizontal or vertical) of a square
	 */
	public static int generatePD(int shipLength, int[] distance) {
		int pD = shipLength; // Population density of a component (horizontal or vertical) of a square
		for (int l = 0; l < distance.length; l++)
			if (distance[l] < shipLength - 1)
				pD -= (shipLength - 1 - distance[l]);
		return pD;
	}

	/**
	 * Updates the population density after a given shot for a given grid and ship
	 * lengths
	 * 
	 * @param shot
	 *            The shot which was fired
	 * @param grid
	 *            The grid for which to calculate
	 * @param shipLengths
	 *            The lengths of ships still in play
	 */
	public static void updatePDDG(Square shot, Square[][] grid, int[] shipLengths) {

		// If shot was a miss
		if (shot.status == SquareTypes.MISS) {
			for (int i = 0; i < shipLengths.length; i++)
				updatePD(grid, shot, shipLengths[i]);
			hunt(grid);
		}

		// If shot was a hit

	}

	/**
	 * Updates the population density for a ship for a square in a grid
	 * 
	 * @param grid
	 *            The grid in which the square is located
	 * @param shot
	 *            The square for which to calculate
	 * @param shipLength
	 *            The specific ship length for which to calculate
	 */
	public static void updatePD(Square[][] grid, Square shot, int shipLength) {

		int[] bounds = getBounds(shot, grid);

		// Going up
		if (shot.y - bounds[0] >= shipLength) // No bounds
			for (int i = 0; i < shipLength; i++)
				grid[i][shot.x].PDy -= i;
		else // With bounds
			for (int i = bounds[0]; i < shot.y; i++)
				grid[i][shot.x].PDy -= shot.y - i;

		// Going down
		if (bounds[1] - shot.y >= shipLength) // No bounds
			for (int i = 0; i < shipLength; i++)
				grid[i][shot.x].PDy -= i;
		else // With bounds
			for (int i = bounds[1]; i > shot.y; i--)
				grid[i][shot.x].PDy -= shot.y - i;

		// Going left
		if (shot.x - bounds[2] >= shipLength) // No bounds
			for (int i = 0; i < shipLength; i++)
				grid[i][shot.y].PDx -= i;
		else // With bounds
			for (int i = bounds[2]; i < shot.x; i++)
				grid[i][shot.y].PDx -= shot.x - i;

		// Going right
		if (bounds[3] - shot.x >= shipLength) // No bounds
			for (int i = 0; i < shipLength; i++)
				grid[i][shot.y].PDx -= i;
		else // With bounds
			for (int i = bounds[3]; i > shot.x; i--)
				grid[i][shot.y].PDx -= shot.x - i;
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				grid[i][j].combinePDXY();
	}

	/**
	 * Gets the boundaries for a decrement of population density in all 4 directions
	 * 
	 * @param shot
	 *            The shot that was fired
	 * @param grid
	 *            The grid for which to calculate boundaries
	 * @return The boundaries as an int array
	 */
	private static int[] getBounds(Square shot, Square[][] grid) {

		// Boundaries set for decrementing total square values
		int[] bounds = new int[] { -1, -1, -1, -1 };

		// Going up
		for (int i = shot.y - 1; i >= 0; i--)
			if (grid[i][shot.x].status == SquareTypes.MISS || grid[i][shot.x].status == SquareTypes.SUNK) {
				bounds[0] = i;
				break;
			}

		// Going down
		for (int i = shot.y + 1; i < grid.length; i++)
			if (grid[i][shot.x].status == SquareTypes.MISS || grid[i][shot.x].status == SquareTypes.SUNK) {
				bounds[1] = i;
				break;
			}

		// Going left
		for (int i = shot.x - 1; i >= 0; i--)
			if (grid[shot.y][i].status == SquareTypes.MISS || grid[shot.y][i].status == SquareTypes.SUNK) {
				bounds[2] = i;
				break;
			}

		// Going right
		for (int i = shot.x + 1; i < grid.length; i++)
			if (grid[shot.y][i].status == SquareTypes.MISS || grid[shot.y][i].status == SquareTypes.SUNK) {
				bounds[3] = i;
				break;
			}
		return bounds;
	}

	/**
	 * Finds the highest likely location of a ship while in hunt mode. Switches to
	 * target mode once a hit is found
	 * 
	 * @return The target to fire at
	 */
	public static Square hunt(Square[][] grid) {
		int max = grid[0][0].totalSquareValue;
		Square target = grid[0][0];
		for (int i = 0; i < grid[0].length; i++)
			for (int j = 0; j < grid.length; j++)
				if ((i + j) % 2 == 0 && grid[i][j].status == SquareTypes.UNKNOWN && grid[i][j].totalSquareValue > max)
					target = grid[i][j];
		return target;
	}

}