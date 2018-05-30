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
		AI.generateHeatMap();
		Square grid[] = new Square[numberOfShips];
		Square homeGrid[] = new Square[numberOfShips];

	}
}
