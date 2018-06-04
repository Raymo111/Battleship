import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.javafx.scene.control.skin.CustomColorDialog;

import javafx.scene.layout.Border;

/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
public class Battleship {
	Random rand = new Random();
	public static Scanner input = new Scanner(System.in);
	public static int shipSizes[] = { 2, 3, 3, 4, 5 };
	public static int boardSizeXY[] = { 10, 10 };
	public static int numberOfShips = shipSizes.length;
	public static boolean[] isShipSunk = new boolean[numberOfShips];
	public static Square[][] enemyGrid = new Square[boardSizeXY[0]][boardSizeXY[1]];;
	public static SquareTypes homeGrid[][] = new SquareTypes[boardSizeXY[0]][boardSizeXY[1]];
	public static int homeShipPlacement[][] = new int[boardSizeXY[0]][boardSizeXY[1]];
	/*
	 * #0=Empty #5=Carrier-5 #4=Battleship-4 #3=Cruiser-3 #2=Submarine-3
	 * #1=Destroyer-2
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

	public static void placeShips() {
		int totalGrid[] = new int[boardSizeXY[0] * boardSizeXY[1]];
		int counter = 0;
		int maxMin = 0;
		for (int i = 0; i < shipSizes.length; i++) {
			maxMin += shipSizes[i] * 2;
		}
		for (int x = 0; x < boardSizeXY[0]; x++) {
			for (int y = 0; y < boardSizeXY[1]; y++) {
				if (enemyGrid[x][y].totalSquareValue < maxMin) {
					totalGrid[counter] = enemyGrid[x][y].totalSquareValue;
				}

			}
		}
		Arrays.sort(totalGrid);

		System.out.println("Select the mode. \n1-Corners \n2-PDM\n3-Random");
		int mode = 0;
		try {
			mode = input.nextInt();
		} catch (Exception e) {

		}

		if (mode == 1) {
			homeShipPlacement[boardSizeXY[0]][boardSizeXY[1]] = 5;
			homeShipPlacement[boardSizeXY[0]][boardSizeXY[1] - 1] = 5;
			homeShipPlacement[boardSizeXY[0]][boardSizeXY[1] - 2] = 5;
			homeShipPlacement[boardSizeXY[0]][boardSizeXY[1] - 3] = 5;
			homeShipPlacement[boardSizeXY[0]][boardSizeXY[1] - 4] = 5;
			homeShipPlacement[boardSizeXY[0] - 2][boardSizeXY[1]] = 1;
			homeShipPlacement[boardSizeXY[0] - 2][boardSizeXY[1] - 1] = 1;
			// Custom placement of two ships for a specific case.

		} else if (mode == 2) {
			for (int i = 0; i < numberOfShips; i++) {
				do {

				} while (condition);
			}

		} else if (mode == 3) {
			for (int i = 0; i < shipSizes.length; i++) {

			}

		}

		else {

		}
	}

	/**
	 * Check to see if a ship placement is in a valid position
	 * 
	 * @param X
	 * @param Y
	 * @param endX
	 * @param endY
	 * @return
	 */
	public boolean checkValidShipPosition(int X, int Y, int endX, int endY) {

		if (X > boardSizeXY[0] || X < 0 || Y > boardSizeXY[1] || Y < 0) {
			return false;
		}
		for (int i = 0; i < Math.abs(endX - X); i++)
			for (int j = 0; j < Math.abs(endY - Y); j++)
				if (homeShipPlacement[X][Y] != 0)
					return false;
		return true;
	}
}
