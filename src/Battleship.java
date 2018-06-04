import java.util.ArrayList;
import java.util.Scanner;

import com.sun.corba.se.spi.orbutil.fsm.Input;

/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
public class Battleship {
	public static Scanner input = new Scanner(System.in);
	public static int shipSizes[] = { 2, 3, 3, 4, 5 };
	public static int boardSizeXY[] = { 10, 10 };
	public static int numberOfShips = shipSizes.length;
	public static boolean[] isShipSunk = new boolean[numberOfShips];
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];;
	public static SquareTypes homeGrid[][] = new SquareTypes[boardSizeXY[0]][boardSizeXY[1]];
	public static int homeShipPlacement[][] = new int[boardSizeXY[0]][boardSizeXY[1]];
	/*
	 * 
	 * 
	 * 
	 * # Class of ship Size 1 Carrier 5 2 Battleship 4 3 Cruiser 3 4 Submarine 3 5
	 * Destroyer 2
	 */

	public static void main(String[] args) {
		AI.generatePDDG();

	}

	/**
	 * Functions that only need to be run at the beginning of a new game
	 */
	public void newGameProcedure() {

	}

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

	public static void placeShip() {
		System.out.println("Select the mode. \n1-Corners \n2-PDM\n3-Random");
		int mode = 0;
		try {
			mode = input.nextInt();
		} catch (Exception e) {

		}

		if (mode == 1) {

		} else if (mode == 2) {
			

		} else if (mode == 3) {

		}

		else {

		}
	}
}
