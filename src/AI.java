
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
	public static int[][] generatePDDG(int shipLength, Square[][] grid) {
		int[][] graph = new int[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++) {
				int[] distance = new int[] { i, j, grid.length - i - 1, grid[i].length - j - 1 };
				graph[i][j] = generatePD(shipLength, distance);
			}
		return graph;
	}

	/**
	 * Generates the initial population density distributed graph for the entire
	 * given grid. Runs once for each grid (home and enemy) at the beginning of the
	 * game.
	 */
	public static int[][] initializePDDG(Square[][] grid) {
		int[][] graph = new int[grid.length][grid[0].length];

		return graph;
	}

	public static void main(String[] args) {
		Battleship.display2Darray(generatePDDG(2, Battleship.enemyGrid));
	}

}