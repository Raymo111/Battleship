
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
		for (int i = 0; i < Battleship.numberOfShips; i++)
			for (int j = 0; j < grid.length; j++)
				for (int k = 0; k < grid[j].length; k++) {
					int[] distance = new int[] { j, k, grid.length - j, grid[j].length - k };
					graph[j][k] = generatePD(shipLength, distance);
				}
		return graph;
	}

	public static void main(String[] args) {
		Battleship.display2Darray(generatePDDG(2, Battleship.enemyGrid));
	}

}