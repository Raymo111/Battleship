
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-05-30
 * Description: The artificial intelligence used to select where to fire
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AI {
	private static Random rand = new Random();
	private static BufferedReader br = new BufferedReader(new java.io.InputStreamReader(System.in));
	private static String input, validate;

	// Start with hunt mode
	private Mode mode = Mode.HUNT;

	/**
	 * An enum for which mode the AI is in.
	 * 
	 * @type Hunt -> Parity is on, looking in entire grid for the highest PD after a
	 *       miss.
	 * 
	 * @type TARGET -> Parity is off, looking around a specific square after a hit.
	 */
	public enum Mode {
		HUNT, TARGET
	}

	// Constructor
	public AI() throws IOException {

		// Generate Probability Density Distributed Graph for both grids
		generatePDDG(Battleship.enemyGrid);

		// Offset PD of edges of grid?
		System.out.println("Offset PD?");
		while (true) {
			input = br.readLine();
			System.out.println("Are you sure of: " + input + "?");
			validate = br.readLine();
			if (validate.equalsIgnoreCase("y"))
				break;
		}

		// User wants to offset edges
		if (input.equalsIgnoreCase("y")) {
			for (int i = 0; i < Battleship.enemyGrid.length; i++)
				for (int j = 0; j < Battleship.enemyGrid[i].length; j++) {
					if (Battleship.enemyGrid[i][j].x <= 1 || Battleship.enemyGrid[i][j].x >= 8) {// x in outer 2
						Battleship.enemyGrid[i][j].huntPDx += 6;
						Battleship.enemyGrid[i][j].combinehuntPDXY();
					}
					if (Battleship.enemyGrid[i][j].y <= 1 || Battleship.enemyGrid[i][j].y >= 8) {// y in outer 2
						Battleship.enemyGrid[i][j].huntPDy += 6;
						Battleship.enemyGrid[i][j].combinehuntPDXY();
					}
				}
		}

		// Place ships on home grid
		placeShips(Battleship.homeGrid, Battleship.shipLengths);
	}

	/**
	 * Generates where to place ships
	 * 
	 * @param grid
	 *            The grid to place ships on
	 * @param shipLengths
	 *            The lengths of ships to place
	 */
	public void placeShips(Square[][] grid, int[] shipLengths) {

		// Get mode
		System.out.println("Select the mode. \n1. Manual - Raymond\n2. Random\n3. Manual - David");
		while (true) {
			try {
				input = br.readLine();
				Integer.parseInt(input);
				System.out.println("Are you sure of: " + input + "?");
				validate = br.readLine();
				if (validate.equalsIgnoreCase("y"))
					break;
			} catch (Exception e) {
				System.err.println("Input integer.");
			}
		}
		int gameMode = Integer.parseInt(input);

		int[][] rotationModifiers = new int[2][4];
		// [0][i]=y
		// [1][i]=x
		rotationModifiers[0][0] = 1;
		rotationModifiers[1][0] = 0;

		rotationModifiers[0][1] = -1;
		rotationModifiers[1][1] = 0;

		rotationModifiers[0][2] = 0;
		rotationModifiers[1][2] = 1;

		rotationModifiers[0][3] = 0;
		rotationModifiers[1][3] = -1;
		if (gameMode == 1) {// Anti-PD placement by Raymond
			Battleship.homeShips[0] = new Ship(grid, grid[0][7], grid[0][9]);
			Battleship.homeShips[1] = new Ship(grid, grid[0][4], grid[0][6]);
			Battleship.homeShips[2] = new Ship(grid, grid[9][6], grid[9][9]);
			Battleship.homeShips[3] = new Ship(grid, grid[1][5], grid[1][9]);
			Battleship.homeShips[4] = new Ship(grid, grid[4][3], grid[5][3]);
		} else if (gameMode == 2) {// random ship placement
			for (int i = 0; i < shipLengths.length; i++) {// loop for number of ships
				boolean correct = false;
				int count = 0;

				int y = rand.nextInt(grid.length - 1);// random x coordinate. Start of ship
				int x = rand.nextInt(grid[0].length - 1);// random y coordinate. Start of ship

				do {
					correct = false;
					if (count > 4) {
						y = rand.nextInt(grid.length - 1);// random y coordinate. Start of ship
						x = rand.nextInt(grid[0].length - 1);// random x coordinate. Start of ship
					}
					for (int a = 0; a < 4; a++) {
						int rotation = rand.nextInt(4);// random int to represent the orientation of the ship

						if (checkValidShipPosition(y, x, (y + (shipLengths[i] * rotationModifiers[0][rotation])),
								(x + (shipLengths[i] * rotationModifiers[1][rotation])), rotation, grid)) {
							correct = true;
							count = 0;
							System.out.println(i);
							System.out.println("Y:" + y + "  X:" + x + "  endY:"
									+ (y + ((shipLengths[i] - 1) * rotationModifiers[0][rotation])) + "  endX:"
									+ (x + ((shipLengths[i] - 1) * rotationModifiers[1][rotation])));

							Battleship.homeShips[i] = new Ship(grid, grid[y][x],
									grid[y + ((shipLengths[i] - 1) * rotationModifiers[0][rotation])][x
											+ ((shipLengths[i] - 1) * rotationModifiers[1][rotation])]);
							break;
						}
						count++;
					}

				} while (correct == false);
			}
		} else if (gameMode == 3) {// Anti-PD placement by David
			Battleship.homeShips[0] = new Ship(grid, grid[1][2], grid[2][2]);
			Battleship.homeShips[1] = new Ship(grid, grid[9][9], grid[7][9]);
			Battleship.homeShips[2] = new Ship(grid, grid[9][7], grid[7][7]);
			Battleship.homeShips[3] = new Ship(grid, grid[5][5], grid[2][5]);
			Battleship.homeShips[4] = new Ship(grid, grid[0][0], grid[0][4]);
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
		for (int i = 0; i < Math.abs(endX - X) + 1; i++)
			for (int j = 0; j < Math.abs(endY - Y) + 1; j++)
				if (Battleship.homeGrid[Y + (sign * j)][X + (sign * i)].shipType != null)
					return false;
		return true;
	}

	/**
	 * Generates the initial probability density distributed graph for the given
	 * number of ships for each square in a given grid. Runs only at the beginning
	 * of the game.
	 * 
	 * @param grid
	 *            The grid for which to calculate the initial PDDG
	 */
	public void generatePDDG(Square[][] grid) {
		int[] distanceX, distanceY;
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				for (int k : Battleship.shipLengths) {
					distanceX = new int[] { i, grid.length - i - 1 };
					distanceY = new int[] { j, grid[i].length - j - 1 };
					grid[j][i].huntPDx += generatePD(k, distanceX);
					grid[j][i].huntPDy += generatePD(k, distanceY);
					grid[j][i].combinehuntPDXY();
				}
	}

	/**
	 * Generates the initial probability density for the given number of ships for a
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
	public int generatePD(int shipLength, int[] distance) {
		int pD = shipLength; // Probability density of a component (horizontal or vertical) of a square
		for (int l = 0; l < distance.length; l++)
			if (distance[l] < shipLength - 1)
				pD -= (shipLength - 1 - distance[l]);
		return pD;
	}

	/**
	 * Selects a square to fire at after updating the probability density for a
	 * given grid and ship lengths after a given lastShot
	 * 
	 * @param mode
	 *            The mode in which the AI is in (determined by hit or miss)
	 * 
	 * @param lastShot
	 *            The lastShot which was fired
	 * @param grid
	 *            The grid for which to calculate
	 * @param shipLengths
	 *            The lengths of ships still in play
	 */
	public Square aim(Square lastShot, Square[][] grid, int[] shipLengths) {

		// Try-catch to handle exceptions
		try {

			// If lastShot was a hit, set aim mode to target, update hit PD and target
			if (lastShot.status == SquareTypes.HIT) {
				for (int i = 0; i < shipLengths.length; i++)
					updateHitPD(grid, lastShot, shipLengths[i]);
				mode = Mode.TARGET;
				return target(mode, grid);
			}

			// If ship was sunk, check for unsunk ships and target those
			else if (lastShot.status == SquareTypes.SUNK) {
				boolean flag = false;
				for (int i = 0; i < grid.length; i++)
					for (int j = 0; j < grid[i].length; j++)
						if (grid[i][j].status == SquareTypes.HIT) {
							flag = true;
							break;
						}
				if (flag) {// More hit squares than ships sunk
					return target(mode, grid);
				} else {// All hit ships were sunk
					lastShot.huntPDx = 0;
					lastShot.huntPDy = 0;
					lastShot.combinehuntPDXY();
					for (int i = 0; i < grid.length; i++)
						for (int j = 0; j < grid[i].length; j++) {
							grid[i][j].targetPDx = 0;
							grid[i][j].targetPDy = 0;
							grid[i][j].combinetargetPDXY();
						}
					mode = Mode.HUNT;
					return aim(null, grid, shipLengths);
				}
			}

			/*
			 * If lastShot was a miss, update miss PD and hunt for a target if in hunt mode,
			 * or target a target if in target mode
			 */
			else if (lastShot.status == SquareTypes.MISS) {
				for (int i = 0; i < shipLengths.length; i++)
					updateMissPD(mode, grid, lastShot, shipLengths[i]);
				if (mode == Mode.HUNT)
					return target(mode, grid);
				else
					return target(mode, grid);
			}

		} catch (Exception e) {
		}

		// No lastShot
		return target(mode, grid);
	}

	/**
	 * Updates the probability density for a ship for a missed square in a grid
	 * 
	 * @param grid
	 *            The grid in which the square is located
	 * @param lastShot
	 *            The square for which to calculate
	 * @param shipLength
	 *            The specific ship length for which to calculate
	 */
	public void updateMissPD(Mode mode, Square[][] grid, Square lastShot, int shipLength) {

		int[] bounds = getBounds(lastShot, grid);

		if (mode == Mode.HUNT) {

			// Set last shot (miss)'s PD to 0
			lastShot.huntPDx = 0;
			lastShot.huntPDy = 0;

			// Going up
			if (lastShot.y - bounds[0] >= shipLength) // No bounds
				for (int i = 1; i < shipLength; i++)
					grid[lastShot.y - i][lastShot.x].huntPDy -= shipLength - i;
			else // With bounds
				for (int i = bounds[0]; i < lastShot.y; i++)
					grid[i][lastShot.x].huntPDy -= lastShot.y - i;

			// Going down
			if (bounds[2] - lastShot.y >= shipLength) // No bounds
				for (int i = 1; i < shipLength; i++)
					grid[lastShot.y + i][lastShot.x].huntPDy -= shipLength - i;
			else // With bounds
				for (int i = bounds[2]; i > lastShot.y; i--)
					grid[i][lastShot.x].huntPDy -= i - lastShot.y;

			// Going left
			if (lastShot.x - bounds[1] >= shipLength) // No bounds
				for (int i = 1; i < shipLength; i++)
					grid[lastShot.y][lastShot.x - i].huntPDx -= shipLength - i;
			else // With bounds
				for (int i = bounds[1]; i < lastShot.x; i++)
					grid[lastShot.y][i].huntPDx -= lastShot.x - i;

			// Going right
			if (bounds[3] - lastShot.x >= shipLength) // No bounds
				for (int i = 1; i < shipLength; i++)
					grid[lastShot.y][lastShot.x + i].huntPDx -= shipLength - i;
			else // With bounds
				for (int i = bounds[3]; i > lastShot.x; i--)
					grid[lastShot.y][i].huntPDx -= i - lastShot.x;

			// Recombine an updated probability density distributed graph for hunt mode
			for (int i = 0; i < grid.length; i++)
				for (int j = 0; j < grid[i].length; j++)
					grid[i][j].combinehuntPDXY();
		} else {
			lastShot.targetPDx = 0;
			lastShot.targetPDy = 0;
			lastShot.combinetargetPDXY();
		}
	}

	/**
	 * Updates the probability density for a ship for a hit square in a grid
	 * 
	 * @param grid
	 *            The grid in which the square is located
	 * @param lastShot
	 *            The square for which to calculate
	 * @param shipLength
	 *            The specific ship length for which to calculate
	 */
	public void updateHitPD(Square[][] grid, Square lastShot, int shipLength) {
		int[] bounds = getBounds(lastShot, grid);

		// Going up
		if (lastShot.y - 1 != bounds[0])
			grid[lastShot.y - 1][lastShot.x].targetPDy += 2;
		if (lastShot.y - 2 != bounds[0])
			grid[lastShot.y - 2][lastShot.x].targetPDy++;

		// Going down
		if (lastShot.y + 1 != bounds[1])
			grid[lastShot.y + 1][lastShot.x].targetPDy += 2;
		if (lastShot.y + 2 != bounds[1])
			grid[lastShot.y + 2][lastShot.x].targetPDy++;

		// Going left
		if (lastShot.x - 1 != bounds[2])
			grid[lastShot.y][lastShot.x - 1].targetPDx += 2;
		if (lastShot.x - 2 != bounds[2])
			grid[lastShot.y][lastShot.x - 2].targetPDy++;

		// Going right
		if (lastShot.x + 1 != bounds[3])
			grid[lastShot.y][lastShot.x + 1].targetPDx += 2;
		if (lastShot.x + 2 != bounds[3])
			grid[lastShot.y][lastShot.x + 2].targetPDy++;

		// Recombine an updated probability density distributed graph for target mode
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				grid[i][j].combinetargetPDXY();
	}

	/**
	 * Gets the boundaries for a decrement of probability density in all 4
	 * directions
	 * 
	 * @param lastShot
	 *            The lastShot that was fired
	 * @param grid
	 *            The grid for which to calculate boundaries
	 * @return The boundaries as an int array
	 */
	private int[] getBounds(Square lastShot, Square[][] grid) {

		// Boundaries set for decrementing total square values
		int[] bounds = new int[] { -1, -1, grid.length, grid[0].length };

		// Going up
		for (int i = lastShot.y - 1; i >= 0; i--)
			if (grid[i][lastShot.x].status != SquareTypes.UNKNOWN) {
				bounds[0] = i;
				break;
			}

		// Going down
		for (int i = lastShot.y + 1; i < grid.length; i++)
			if (grid[i][lastShot.x].status != SquareTypes.UNKNOWN) {
				bounds[1] = i;
				break;
			}

		// Going left
		for (int i = lastShot.x - 1; i >= 0; i--)
			if (grid[lastShot.y][i].status != SquareTypes.UNKNOWN) {
				bounds[2] = i;
				break;
			}

		// Going right
		for (int i = lastShot.x + 1; i < grid.length; i++)
			if (grid[lastShot.y][i].status != SquareTypes.UNKNOWN) {
				bounds[3] = i;
				break;
			}
		return bounds;
	}

	/**
	 * Finds the highest likely location of a ship from the entire grid (with
	 * parity)
	 * 
	 * @return The target to fire at
	 */
	public Square target(Mode mode, Square[][] grid) {
		int max = 0;
		ArrayList<Square> targets = new ArrayList<Square>();
		if (mode == Mode.HUNT)
			for (int i = 0; i < grid.length; i++)
				for (int j = 0; j < grid[0].length; j++) {
					if ((i + j) % 2 == 0 && grid[i][j].status == SquareTypes.UNKNOWN
							&& grid[i][j].totalSquarePD > max) {
						max = grid[i][j].totalSquarePD;
						targets.clear();
						targets.add(grid[i][j]);
					} else if ((i + j) % 2 == 0 && grid[i][j].status == SquareTypes.UNKNOWN
							&& grid[i][j].totalSquarePD == max)
						targets.add(grid[i][j]);
				}
		if (mode == Mode.TARGET)
			for (int i = 0; i < grid[0].length; i++)
				for (int j = 0; j < grid.length; j++)
					if (grid[i][j].status == SquareTypes.UNKNOWN && grid[i][j].totalSquarePD > max) {
						max = grid[i][j].totalSquarePD;
						targets.clear();
						targets.add(grid[i][j]);
					} else if (grid[i][j].status == SquareTypes.UNKNOWN && grid[i][j].totalSquarePD == max)
						targets.add(grid[i][j]);
		return targets.get(rand.nextInt(targets.size()));
	}

}