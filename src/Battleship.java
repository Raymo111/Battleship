/*
 * Authors: Raymond Li, David Tuck
 * Date created: 30/05/2018
 * Description: Main class for battleship game
 */
public class Battleship {

	public static int shipSizes[] = { 2, 3, 4, 4, 5 };
	public static int boardSizeXY[] = { 10, 10 };
	public static int numberOfShips = shipSizes.length;

	public static void main(String[] args) {
<<<<<<< HEAD
		AI.generateHeatMap();
		Square grid[] = new Square[numberOfShips];
<<<<<<< HEAD
		Square homeGrid[] = new Square[numberOfShips];
=======
>>>>>>> ba92be681e00b41c186401815ebecbe46c032fea
=======
		AI.generatePDDG();
		Square homeGrid[] = new Square[numberOfShips];
		Square enemyGrid[] = new Square[numberOfShips];
>>>>>>> 72034e7a33f77adbbf4449de8ff0bf2240c7c205

	}
}
