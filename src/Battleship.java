
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
import java.io.*;
import java.util.ArrayList;

public class Battleship {

	public static int shipLengths[] = { 2, 3, 3, 4, 5 };// Each index represents the size of a individual ship

	// Each index represents the size of a individual ship
	public static String shipNames[] = { "Destroyer", "Cruiser", "Submarine", "Battleship", "Aircraft Carrier" };
	public static int boardSizeXY[] = { 10, 10 };// x and y size of the board
	public static boolean[] enemyShipsSunk = new boolean[shipLengths.length];// Which enemy ships are sunk
	public static boolean[] homeShipsSunk = new boolean[shipLengths.length];// Which home ships are sunk
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// State of enemy grid
	public static Square[][] homeGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// State of home grid
	public static Ship[] homeShips = new Ship[shipLengths.length];// A list of home ships
	public static Ship[] enemyShips = new Ship[shipLengths.length];// A list of home ships
	public static ArrayList<Square> enemyShotLog = new ArrayList<Square>(20);// A log of enemy shots
	public static ArrayList<Square> homeShotLog = new ArrayList<Square>(20);// A log of AI's shots

	// A list of used ship names (for ships of the same size with different names)
	public static ArrayList<String> usedShipNames = new ArrayList<String>(Battleship.shipNames.length);

	// Buffered reader to read user input
	private static BufferedReader br = new BufferedReader(new java.io.InputStreamReader(System.in));

	public static void main(String[] args) throws IOException {
		// while (true)
		newGameProcedure();
	}

	/**
	 * Functions that only need to be run at the beginning of a new game
	 * 
	 * @throws IOException
	 */
	public static void newGameProcedure() throws IOException {

		// Initialize enemy grid
		for (int i = 0; i < enemyGrid.length; i++)
			for (int j = 0; j < enemyGrid[i].length; j++)
				enemyGrid[i][j] = new Square(j, i);

		// Initialize home grid
		for (int i = 0; i < homeGrid.length; i++)
			for (int j = 0; j < homeGrid[i].length; j++)
				homeGrid[i][j] = new Square(j, i);

		// Create a new AI - God's warrior angel
		AI Michael = new AI();
		displayPD(enemyGrid);
		displayShips(homeGrid);
		game(Michael);
	}

	/**
	 * The actual game method
	 * 
	 * @throws IOException
	 */
	public static void game(AI Michael) throws IOException {

		// Local variables
		boolean AIFirst, AIWin = false, userWin = false;
		int round = 0, x, y, shipNumber = 0;// The index of a ship in homeShips grid
		Square userShot = null, AIShot = null;
		Ship ship = homeShips[0];
		boolean flag;
		String input, validate;

		// Who goes first
		System.out.println("You first or Michael (the AI) first?");

		while (true) {
			input = br.readLine().toLowerCase();
			System.out.println("Are you sure of: " + input + "?");
			validate = br.readLine();
			if (validate.equalsIgnoreCase("y"))
				break;
		}

		// ~ String input = system.firstHand;

		// User wants AI to go first
		if (input.contains("a")) {
			AIFirst = true;
			System.out.println("Michael is going first.");
		} else {
			AIFirst = false;
			System.out.println("You are going first.");
		}

		// Place user's ships
		System.out.println("Place your ships on a separate grid. When you're ready, press ENTER to continue...");
		br.readLine();
		for (int i = 0; i < enemyShips.length; i++)
			enemyShips[i] = new Ship(enemyGrid, shipNames[i], shipLengths[i]);

		// Only executes once for when user goes first
		if (!AIFirst) {

			// Get user's shot
			System.out.println("Round 1. Your turn.\nEnter coordinates to fire:");
			while (true) {
				input = br.readLine().toUpperCase();
				System.out.println("Are you sure of: " + input + "?");
				validate = br.readLine();
				if (validate.equalsIgnoreCase("y"))
					break;
			}

			// input = askFire();

			x = Integer.parseInt(input.substring(1, 2)) - 1;
			y = ((int) input.charAt(0)) - 65;// ASCII value for A~J = 65~74
			if (x == 1) {// Get second digit, could be 10
				try {
					if (Integer.parseInt(input.substring(2, 3)) == 0)
						x = 9;
				} catch (Exception e) {
				}
			}

			// Check for hit or miss on home grid
			userShot = homeGrid[y][x];
			if (userShot.shipType == null) {// Miss
				userShot.status = SquareTypes.MISS;
				System.out.println("MISS");

				// fireResult("MISS");

			} else {// Hit
				homeGrid[y][x].status = SquareTypes.HIT;
				for (int i = 0; i < homeShips.length; i++)
					for (int j = 0; j < homeShips[i].location.size(); j++)
						if (homeShips[i].location.get(j) == userShot) {
							ship = homeShips[i];
							break;
						}
				System.out.println("HIT, " + ship.shipName);

				// fireResult("HIT");

			}
			enemyShotLog.add(homeGrid[y][x]);// Add enemy shot to log
			round++;
		}

		// Game do-while loop
		do {

			// Increment round
			round++;

			// Get AI's shot
			System.out.println("Round " + round + ". Michael's turn.");
			AIShot = Michael.aim(AIShot, enemyGrid, shipLengths);
			displayPD(enemyGrid);
			homeShotLog.add(AIShot);// Add home shot to log

			// Print AI's shot's y-x coordinate converted to Battleship standards (e.g. A1)
			System.out.println("AI's shot coordinates: "
					+ (Character.toString((char) (AIShot.y + 65)) + Integer.toString(AIShot.x + 1)).toUpperCase());

			// Get user's response
			System.out.println("HIT or MISS?");
			while (true) {
				input = br.readLine().toUpperCase();
				System.out.println("Are you sure of: " + input + "?");
				validate = br.readLine();
				if (validate.equalsIgnoreCase("y"))
					break;
			}

			// input = getFire(AIShot.x,AIShot.y);

			// AI hit a ship
			if (input.contains("HIT")) {
				AIShot.status = SquareTypes.HIT;

				// Get ship name
				for (int i = 0; i < shipNames.length; i++)
					if (input.contains(shipNames[i].toUpperCase())) {
						ship = enemyShips[i];
						ship.location.add(AIShot);
						break;
					}

				// Check if sunk
				if (input.contains("SUNK")) {
					int temp = 0;
					for (int i = 0; i < ship.location.size(); i++)// Set all location squares of ship to status sunk
						ship.location.get(i).status = SquareTypes.SUNK;
					for (int i = 0; i < shipNames.length; i++)
						if (ship.shipName.equals(shipNames[i])) {
							temp = i;
							break;
						}
					enemyShipsSunk[temp] = true;

					// Check if AI has won
					AIWin = true;
					for (boolean b : enemyShipsSunk)
						if (!b) {
							AIWin = false;
							break;
						}
					if (AIWin) {
						break;
					}
				}
			}

			// AI missed
			else if (input.contains("MISS"))
				AIShot.status = SquareTypes.MISS;

			// Get user's shot
			System.out.println("Round " + round + ". Your turn.\nEnter coordinates to fire:");
			while (true) {
				input = br.readLine().toUpperCase();
				System.out.println("Are you sure of: " + input + "?");
				validate = br.readLine();
				if (validate.equalsIgnoreCase("y"))
					break;
			}

			// input = askFire();

			x = Integer.parseInt(input.substring(1, 2)) - 1;
			y = ((int) input.charAt(0)) - 65;// ASCII value for A~J = 65~74
			if (x == 1) {// Get second digit, could be 10
				try {
					if (Integer.parseInt(input.substring(2, 3)) == 0)
						x = 9;
				} catch (Exception e) {
				}
			}

			// Check for hit or miss on home grid
			userShot = homeGrid[y][x];
			if (userShot.shipType == null) {// Miss
				userShot.status = SquareTypes.MISS;
				System.out.println("MISS");

				// fireResult("MISS");

			} else {// Hit
				userShot.status = SquareTypes.HIT;
				for (int i = 0; i < homeShips.length; i++)
					for (int j = 0; j < homeShips[i].location.size(); j++)
						if (homeShips[i].location.get(j) == userShot) {
							ship = homeShips[i];
							shipNumber = i;
							break;
						}

				// Check for ship sunk
				flag = true;
				for (int i = 0; i < ship.location.size(); i++)
					if (ship.location.get(i).status == SquareTypes.UNKNOWN) {
						flag = false;
						break;
					}
				if (flag)
					homeShipsSunk[shipNumber] = true;
				else
					homeShipsSunk[shipNumber] = false;
				if (homeShipsSunk[shipNumber]) {
					System.out.println("HIT, SUNK " + homeShips[shipNumber].shipName);

					// fireResult("HIT");

					// Check for win (all ships sunk)
					flag = true;
					for (int i = 0; i < homeShipsSunk.length; i++)
						if (!homeShipsSunk[i]) {
							flag = false;
							break;
						}
					if (flag) // User has won
						userWin = true;
				} else
					System.out.println("HIT, " + ship.shipName);

				// fireResult("HIT");

			}
			enemyShotLog.add(homeGrid[y][x]);// Add enemy shot to log

		} while (!AIWin && !userWin);// Continues running until someone wins

		// If user wins
		if (userWin)
			System.out.println("Congrats, you have won!");

		// endGame(true);

		if (AIWin)
			System.out.println("Sorry, you have lost.");

		// endGame(false);

	}

	public static void displayPD(Square[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.printf("|%2d", grid[i][j].totalSquarePD);
			}
			System.out.println();
		}
	}

	public static void displayShips(Square[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				if (grid[i][j].shipType != null && grid[i][j].shipType.shipName != null)
					System.out.print("|" + grid[i][j].shipType.shipName.charAt(0));
				else
					System.out.print("| ");
			}
			System.out.print("|\n");
		}
	}

	public static void display2Darray(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++)
				System.out.print("|" + array[j][i]);
			System.out.println();
		}
	}

}