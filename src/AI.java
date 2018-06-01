import java.util.ArrayList;
import java.util.Random;

/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-05-30
 * Description: The artificial intelligence used to select where to fire
 */
public class AI {
	Random rand = new Random();

	/**
	 * Generates the initial population distribution density graph for the given
	 * number of ships. Runs only at the beginning of the game.
	 */
	public static void generatePDDG() {
		for (int i = 0; i < Battleship.numberOfShips; i++) {

		}
	}

	public static void generateHomeGrid() {

	}

	public static void refreshTotalSquareValue() {
		int temp = 0;
		for (int x = 0; x < Battleship.enemyGrid[0].length; x++) {
			for (int y = 0; y < Battleship.enemyGrid.length; y++) {
				for (int j2 = 0; j2 < Battleship.numberOfShips; j2++) {

					temp += Battleship.enemyGrid[x][y].graphArray[j2];

				}
				Battleship.enemyGrid[x][y].totalSquareValue = temp;
				temp = 0;
			}
		}
	}

	public int[] findHighestPD() {
		int[] currentTarget = { 0, 0 };
		int max = Battleship.enemyGrid[0][0].totalSquareValue;
		ArrayList<ArrayList<Integer>> locationsOfMax = new ArrayList<ArrayList<Integer>>();

		for (int x = 0; x < Battleship.enemyGrid[0].length; x++) {
			for (int y = 0; y < Battleship.enemyGrid.length; y++) {
				if ((x + y) % 2 == 0) {
					if (Battleship.enemyGrid[x][y].status == SquareTypes.UNKNOWN) {
						if (Battleship.enemyGrid[x][y].totalSquareValue > max) {
							if (locationsOfMax.contains(max)) {
								locationsOfMax.get(0).set(x, y);
							} else {
								locationsOfMax.clear();
								locationsOfMax.get(0).set(x, y);
							}
						}
					}
				}
			}
		}
		currentTarget[0] = 9;
		currentTarget[1] = 8;
		return currentTarget;

	}
}
