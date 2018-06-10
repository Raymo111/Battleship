
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Battleship {
	public static int shipLengths[] = { 2, 3, 3, 4, 5 };// Each index represents the size of a individual ship

	// Each index represents the size of a individual ship
	public static String shipNames[] = { "Destroyer", "Cruiser", "Submarine", "Battleship", "Aircraft Carrier" };
	public static int boardSizeXY[] = { 10, 10 };// x and y size of the board
	private static int[] enemyShipsSunk = new int[shipLengths.length];// which enemy ships are sunk
	private static int[] homeShipsSunk = new int[shipLengths.length];// which home ships are sunk
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// state of enemy grid
	public static Square[][] homeGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];// state of home grid
	public static Ship[] homeShips = new Ship[shipLengths.length];// A list of home ships

	// Buffered reader to read user input
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) {
		// while (true)
		newGameProcedure();
	}

	/**
	 * Functions that only need to be run at the beginning of a new game
	 */
	public static void newGameProcedure() {

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
	}

	/**
	 * The actual game method
	 * 
	 * @throws IOException
	 */
	public static void game() throws IOException {

		// Local variables
		boolean AIFirst, AIWin = false, userWin = false;
		int round = 1, x, y;

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
		try {
			System.in.read();
		} catch (Exception e) {
		}

		// Only executes once for when user goes first
		if (!AIFirst) {

			// Get's user's shot
			System.out.println("Round 1. Your turn.\nWhat square would you like to shoot at? (e.g. A1)");
			input = br.readLine();
			input.toLowerCase();
			x = ((int) input.charAt(1)) - 1;
			y = ((int) input.charAt(0)) - 65;

		}

		// Game do-while loop
		do {

			// Increment round
			round++;

			//

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