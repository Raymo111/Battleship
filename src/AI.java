
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-05-30
 * Description: The artificial intelligence used to select where to fire
 */
import java.util.Random;

public class AI {
	Random rand = new Random();

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
				for (int k : Battleship.shipSizes) {
					distance = new int[] { i, j, grid.length - i - 1, grid[i].length - j - 1 };
					grid[i][j].totalSquareValue += generatePD(k, distance);
				}
	}

}