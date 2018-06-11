
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Battleship implements java.io.Serializable {

	/**
	 * Generated serial version UID for serialization (part of machine learning)
	 */
	private static final long serialVersionUID = 5377521207075619332L;

	public static int shipLengths[] = { 2, 3, 3, 4, 5 };// Each index represents the size of a individual ship

	// Each index represents the size of a individual ship
	public static String shipNames[] = { "Destroyer", "Cruiser", "Submarine", "Battleship", "Aircraft Carrier" };
	public static int boardSizeXY[] = { 10, 10 };// x and y size of the board
	private static boolean[] enemyShipsSunk = new boolean[shipLengths.length];// Which enemy ships are sunk
	private static boolean[] homeShipsSunk = new boolean[shipLengths.length];// Which home ships are sunk
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// State of enemy grid
	public static Square[][] homeGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// State of home grid
	public static Ship[] homeShips = new Ship[shipLengths.length];// A list of home ships
	public static ArrayList<Square> enemyShotLog = new ArrayList<Square>(20);// A log of enemy shots
	public static ArrayList<Square> homeShotLog = new ArrayList<Square>(20);// A log of AI's shots

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
		int round = 0, x, y, shipNumber = 0;// The index of a ship in homeShips array
		Square userShot = null, AIShot = null;
		Ship ship = homeShips[0];
		boolean flag;

		// Who goes first
		System.out.println("You first or Michael (the AI) first?");
		String input = br.readLine().toLowerCase();

		// User wants AI to go first
		if (input.contains("a")) {
			AIFirst = true;
			System.out.println("Michael is going first.");
		} else {
			AIFirst = false;
			System.out.println("You are going first.");
		}

		// Wait for user to place ships
		System.out.println("Place your ships on a separate grid. When you're ready, press ENTER to continue...");
		br.readLine();

		// Only executes once for when user goes first
		if (!AIFirst) {

			// Get user's shot
			System.out.println("Round 1. Your turn.\nWhat square would you like to shoot at? (e.g. A1)");
			input = br.readLine().toUpperCase();
			x = Integer.parseInt(input.substring(1, 2)) - 1;
			y = ((int) input.charAt(0)) - 65;// ASCII value for A~J = 65~74

			// Check for hit or miss on home grid
			userShot = homeGrid[y][x];
			if (userShot.shipType == null) {// Miss
				userShot.status = SquareTypes.MISS;
				System.out.println("MISS");
			} else {// Hit
				flag = true;
				for (int i = 0; i < homeShips.length; i++)
					for (int j = 0; j < homeShips[i].location.length; j++)
						if (homeShips[i].location[j] == userShot) {
							ship = homeShips[i];
							i = shipNumber;
							if (ship.location[j].status == SquareTypes.UNKNOWN) {
								flag = false;
								break;
							}
						}
				if (flag)
					homeShipsSunk[shipNumber] = true;
				else
					homeShipsSunk[shipNumber] = false;
				homeGrid[y][x].status = SquareTypes.HIT;
				if (homeShipsSunk[shipNumber])
					System.out.println("HIT, SUNK " + homeShips[shipNumber].shipName);
				else
					System.out.println("HIT, " + ship.shipName);
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
			AIShot = Michael.aim(AI.Mode.HUNT, AIShot, enemyGrid, shipLengths);
			homeShotLog.add(AIShot);// Add home shot to log

			// Print AI's shot's y-x coordinate converted to Battleship standards (e.g. A1)
			System.out.println("AI's shot coordinates: "
					+ (Character.toString((char) (AIShot.y + 65)) + Integer.toString(AIShot.x + 1)).toUpperCase());

			// Get user's response
			System.out.println("HIT or MISS?");
			input = br.readLine().toUpperCase();
			if (input.contains("HIT")) {
				AIShot.status = SquareTypes.HIT;
				System.out.println("Ship SUNK?");
				if (input.contains("SUNK")) {
					AIShot.status = SquareTypes.SUNK;

				}
			} else if (input.contains("MISS"))
				AIShot.status = SquareTypes.MISS;

			// Get user's shot
			System.out.println("Round " + round + ". Your turn.\nEnter coordinates to fire:");
			input = br.readLine().toUpperCase();
			x = Integer.parseInt(input.substring(1, 2)) - 1;
			y = ((int) input.charAt(0)) - 65;// ASCII value for A~J = 65~74

			// Check for hit or miss on home grid
			if (homeGrid[y][x].shipType == null) {// Miss
				homeGrid[y][x].status = SquareTypes.MISS;
				System.out.println("MISS");
			} else {// Hit
				for (int i = 0; i < homeShips.length; i++)
					for (int j = 0; j < homeShips[i].location.length; j++)
						if (homeShips[i].location[j] == userShot) {
							ship = homeShips[i];
							i = shipNumber;
							if (ship.location[j].status == SquareTypes.UNKNOWN) {
								homeShipsSunk[i] = false;
								break;
							}
						}
				homeGrid[y][x].status = SquareTypes.HIT;
				if (homeShipsSunk[shipNumber])
					System.out.println("HIT, SUNK " + homeShips[shipNumber].shipName);
				else
					System.out.println("HIT, " + ship.shipName);
			}
			enemyShotLog.add(homeGrid[y][x]);// Add enemy shot to log

		} while (!AIWin && !userWin);// Continues running until someone wins

	}

	public static void display2Darray(Square[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				System.out.print("|" + array[i][j].totalSquarePD);
			}
			System.out.println();
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++)
				System.out.print("|" + array[i][j].shipType);
			System.out.println();
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