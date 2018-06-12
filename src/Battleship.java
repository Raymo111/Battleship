
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

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
		display2Darray(homeGrid);
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
		
//		String input = readLog();

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
		
//		while(!system.inGame){}//wait for the startButton to be clicked

		// Only executes once for when user goes first
		if (!AIFirst) {

			// Get user's shot
			System.out.println("Round 1. Your turn.\nEnter coordinates to fire:");
			input = br.readLine().toUpperCase();
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
			} else {// Hit
				homeGrid[y][x].status = SquareTypes.HIT;
				for (int i = 0; i < homeShips.length; i++)
					for (int j = 0; j < homeShips[i].location.length; j++)
						if (homeShips[i].location[j] == userShot) {
							ship = homeShips[i];
							break;
						}
				System.out.println("HIT, " + ship.shipName);
			}
			enemyShotLog.add(homeGrid[y][x]);// Add enemy shot to log
			round++;
		}

		// Game do-while loop
		do

		{

			// Increment round
			round++;

			// Get AI's shot
			System.out.println("Round " + round + ". Michael's turn.");
			AIShot = Michael.aim(AIShot, enemyGrid, shipLengths);
			homeShotLog.add(AIShot);// Add home shot to log

			// Print AI's shot's y-x coordinate converted to Battleship standards (e.g. A1)
			System.out.println("AI's shot coordinates: "
					+ (Character.toString((char) (AIShot.y + 65)) + Integer.toString(AIShot.x + 1)).toUpperCase());

			// Get user's response
			System.out.println("HIT or MISS?");
			input = br.readLine().toUpperCase();
			if (input.contains("HIT")) {
				AIShot.status = SquareTypes.HIT;
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
			} else {// Hit
				userShot.status = SquareTypes.HIT;
				for (int i = 0; i < homeShips.length; i++)
					for (int j = 0; j < homeShips[i].location.length; j++)
						if (homeShips[i].location[j] == userShot) {
							ship = homeShips[i];
							shipNumber = i;
							break;
						}

				// Check for ship sunk
				flag = true;
				for (int i = 0; i < ship.location.length; i++)
					if (ship.location[i].status == SquareTypes.UNKNOWN) {
						flag = false;
						break;
					}
				if (flag)
					homeShipsSunk[shipNumber] = true;
				else
					homeShipsSunk[shipNumber] = false;
				if (homeShipsSunk[shipNumber]) {
					System.out.println("HIT, SUNK " + homeShips[shipNumber].shipName);

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
			}
			enemyShotLog.add(homeGrid[y][x]);// Add enemy shot to log

		} while (!AIWin && !userWin);// Continues running until someone wins

		// If user wins
		if (userWin)
			System.out.println("Congrats, you have won!");
		//	system.inGame = false;
		if (AIWin)
			System.out.println("Sorry, you have lost.");
		//	system.inGame = true;
	}

	public static void display2Darray(Square[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				System.out.print("|" + array[i][j].totalSquarePD);
			}
			System.out.println();
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (array[i][j].shipType != null && array[i][j].shipType.shipName != null)
					System.out.print("|" + array[i][j].shipType.shipName.charAt(0));
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
	public static String readLog(){
		while(!system.workDone){}//wait for the system to mark processed
		system.workDone = false;//reset for next command
		return system.log;//return command from system
	}
}